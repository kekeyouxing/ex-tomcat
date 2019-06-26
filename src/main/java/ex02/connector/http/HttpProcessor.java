package ex02.connector.http;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

import ex02.connector.SocketInputStream;

public class HttpProcessor implements Runnable{

	private boolean started = false;
	
	private boolean stopped  = false;
	
	private boolean available = false;
	
	private Socket socket = null;
	
	private HttpConnector connector = new HttpConnector();
	
	private HttpRequestImpl request = null;
	
	private HttpResponseImpl response = null;
	
	private Object threadSync = new Object();
	
	private String threadName = null;
	
	private int status = 0;
	
	private Thread thread = null;
	
	private boolean keepAlive = true;

	private int serverPort = 0;
	
	private HttpRequestLine requestLine = new HttpRequestLine();

	private boolean http11;

	private boolean sendAck;

    private static final byte[] ack =
            (new String("HTTP/1.1 100 Continue\r\n\r\n")).getBytes();

	private static final String match = ";jsessionid=";
	public HttpProcessor(HttpConnector connector, int id) {
		super();
		this.connector = connector;
		this.request = (HttpRequestImpl)connector.createRequest();
		this.response = (HttpResponseImpl)connector.createResponse();
		this.threadName = "HttpProcessor["+id+"]";
	}

	public void start() {
		if(started) {
			return;
		}
		started =true;
		threadStart();
	}
	
	/**
	 * <pre>
	 * (1)start the processor thread, this method calls {@link #run()}
	 * (2){@link #run()}
	 *    (a)等待请求的到来
	 *    (b)请求来了之后,processor处理请求
	 *    (c)processor处理完请求后,回收processor,即把processor放入processor pool中
	 * </pre>
	 */
	private void threadStart() {
		thread = new Thread(this, threadName);
		thread.start();
		log("	Creating "+threadName+" successfully...");
	}
	
	public void run() {
		
		while(!stopped) {
			Socket socket = await();
			if(socket == null) {
				continue;
			}
			
			process(socket);
			connector.recycle(this);
		}
		
		synchronized (threadSync) {
			threadSync.notifyAll();
		}
		
	}
	
	/**
	 * <pre>
	 * 请将{@link #await()}和{@link #assign(Socket)}联合分析,这两个方法属于不同的线程,相互进行通信
	 * await method belongs to "processor thread"
	 * (1)Initially, when the "processor thread" has just been started, available is false, so the
	 * thread waits inside the while loop. It will wait until another thread calls notify or notifyAll
	 * (4)After "connector thread" calls notifyAll method, this will wake up
	 * the processor thread and now the value of available is true so the program controls
	 * goes out of the while loop: assigning the instance's socket to a local variable, sets
	 * available to false, calls notifyAll, and returns the socket, which eventually causes the
	 * socket to be processed.
	 * </pre>
	 */
	private synchronized Socket await() {
		
		while(!available) {
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		Socket socket = this.socket;
		available = false;
		notifyAll();
		return socket;
		
	}
	/**
	 * <pre>
	 * 请将{@link #await()}和{@link #assign(Socket)}联合分析,这两个方法属于不同的线程,相互进行通信
	 * assign method belongs to "connector thread"
	 * (2) When a new socket is assigned, the "connector thread" calls
	 * the HttpProcessor's assign method. The value of available is false, so the while loop
	 * is skipped and the socket is assigned to the HttpProcessor instance's socket
	 * variable:
	 * this.socket = socket;
	 * (3)The "connector thread" then sets available to true and calls notifyAll. 
	 * </pre>
	 */
	public synchronized void assign(Socket socket) {
		while(available) {
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		this.socket = socket;
		available = true;
		notifyAll();
	}

	private void process(Socket socket) {
		log("\nrequest has come,"+threadName+" is processing request...");
		boolean ok = true;
		boolean finishResponse = true;
		SocketInputStream input = null;
		OutputStream output = null;
		
		try {
			input = new SocketInputStream(socket.getInputStream(), connector.getBufferSize());
		} catch (IOException e) {
			e.printStackTrace();
			ok = false;
		}
		
		keepAlive = true;
		
		while(!stopped && ok && keepAlive) {
			finishResponse = true;
			
			try {
				request.setStream(input);
				request.setResponse(response);
				output = socket.getOutputStream();
				response.setStream(output);
				response.setRequest(request);
			} catch (IOException e) {
				e.printStackTrace();
				ok = false;
			}
			
			try {
				if(ok) {
					parseConnection(socket);
					parseRequest(input, output);
					if(http11) {
						ackRequest(output);
						if(connector.isChunkingAllowed()) {
							response.setAllowChunking(true);
						}
					}
				}
			} catch (IOException e) {
				ok = false;
				finishResponse = false;
			}
			
			try {
				if(ok) {
					connector.getContainer().invoke(request, response);
				}
			} catch (Exception e) {
				ok = false;
			}
			if(finishResponse) {
				keepAlive = false;
			}
			request.recycle();
			response.recycle();
		}
		
		try {
			shutdownInput(input);
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		socket = null;
	}

	private void shutdownInput(SocketInputStream input) {
		try {
            int available = input.available();
            // skip any unread (bogus) bytes
            if (available > 0) {
                input.skip(available);
            }
        } catch (Throwable e) {
            ;
        }
	}

	private void ackRequest(OutputStream output) 
			throws IOException {
		if(sendAck) {
			output.write(ack);
		}
	}

	private void parseConnection(Socket socket) {
		request.setInet(socket.getInetAddress());
		request.setServerPort(serverPort );
		request.setSocket(socket);
	}
	
	private void parseRequest(SocketInputStream input, OutputStream output) throws IOException {
		
		input.readRequestLine(requestLine);
		status = 1;
		
        String method =
                new String(requestLine.method, 0, requestLine.methodEnd);
        String uri = null;
        String protocol = new String(requestLine.protocol, 0,
                                     requestLine.protocolEnd);
        
        // Now check if the connection should be kept alive after parsing the
        // request.
        if ( protocol.equals("HTTP/1.1") ) {
            http11 = true;
            sendAck = false;
        } else {
            http11 = false;
            sendAck = false;
            // For HTTP/1.0, connection are not persistent by default,
            // unless specified with a Connection: Keep-Alive header.
            keepAlive = false;
        }
        
        int question = requestLine.indexOf("?");
        if(question >= 0) {
        	request.setQueryString(new String(requestLine.uri, question + 1 , requestLine.uriEnd-question-1));
        	uri = new String(requestLine.uri, 0, question);
        }else {
        	request.setQueryString(null);
        	uri = new String(requestLine.uri, 0, requestLine.uriEnd);
        }
        // Checking for an absolute URI (with the HTTP protocol)
        if(!uri.startsWith("/")) {
        	int pos = uri.indexOf("://");
        	if(pos != -1) {
        		pos = uri.indexOf('/', pos+3);
        		if(pos == -1) {
        			uri = "";
        		}else {
        			uri = uri.substring(pos);
        		}
        	}
        }
        
        int semicolon = uri.indexOf(match);
        if (semicolon >= 0) {
            String rest = uri.substring(semicolon + match.length());
            int semicolon2 = rest.indexOf(';');
            if (semicolon2 >= 0) {
                request.setRequestedSessionId(rest.substring(0, semicolon2));
                rest = rest.substring(semicolon2);
            } else {
                request.setRequestedSessionId(rest);
                rest = "";
            }
            request.setRequestedSessionURL(true);
            uri = uri.substring(0, semicolon) + rest;
        } else {
            request.setRequestedSessionId(null);
            request.setRequestedSessionURL(false);
        }

        // Normalize URI (using String operations at the moment)
        String normalizedUri = normalize(uri);
        
        request.setMethod(method);
        if(normalizedUri != null) {
        	request.setRequestURI(normalizedUri);
        }else {
        	request.setRequestURI(uri);
        }
        request.setProtocol(protocol);
	}
	
    protected String normalize(String path) {

        if (path == null)
            return null;

        // Create a place for the normalized path
        String normalized = path;

        // Normalize "/%7E" and "/%7e" at the beginning to "/~"
        if (normalized.startsWith("/%7E") ||
            normalized.startsWith("/%7e"))
            normalized = "/~" + normalized.substring(4);

        // Prevent encoding '%', '/', '.' and '\', which are special reserved
        // characters
        if ((normalized.indexOf("%25") >= 0)
            || (normalized.indexOf("%2F") >= 0)
            || (normalized.indexOf("%2E") >= 0)
            || (normalized.indexOf("%5C") >= 0)
            || (normalized.indexOf("%2f") >= 0)
            || (normalized.indexOf("%2e") >= 0)
            || (normalized.indexOf("%5c") >= 0)) {
            return null;
        }

        if (normalized.equals("/."))
            return "/";

        // Normalize the slashes and add leading slash if necessary
        if (normalized.indexOf('\\') >= 0)
            normalized = normalized.replace('\\', '/');
        if (!normalized.startsWith("/"))
            normalized = "/" + normalized;

        // Resolve occurrences of "//" in the normalized path
        while (true) {
            int index = normalized.indexOf("//");
            if (index < 0)
                break;
            normalized = normalized.substring(0, index) +
                normalized.substring(index + 1);
        }

        // Resolve occurrences of "/./" in the normalized path
        while (true) {
            int index = normalized.indexOf("/./");
            if (index < 0)
                break;
            normalized = normalized.substring(0, index) +
                normalized.substring(index + 2);
        }

        // Resolve occurrences of "/../" in the normalized path
        while (true) {
            int index = normalized.indexOf("/../");
            if (index < 0)
                break;
            if (index == 0)
                return (null);  // Trying to go outside our context
            int index2 = normalized.lastIndexOf('/', index - 1);
            normalized = normalized.substring(0, index2) +
                normalized.substring(index + 3);
        }

        // Declare occurrences of "/..." (three or more dots) to be invalid
        // (on some Windows platforms this walks the directory tree!!!)
        if (normalized.indexOf("/...") >= 0)
            return (null);

        // Return the normalized path that we have completed
        return (normalized);

    }

	public void stop() {
		started = false;
		threadStop();
	}

	private void threadStop() {
		
		stopped = true;
		assign(null);
		if(status != 0) {
			synchronized (threadSync) {
				try {
					threadSync.wait(5000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		thread = null;
	}
	private void log(String message){
		System.out.println(message);
	}
}
