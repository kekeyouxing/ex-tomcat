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
	
	private int status = Constants.PROCESSOR_IDLE;
	
	private Thread thread = null;
	
	private boolean keepAlive = true;
	
	public HttpProcessor(HttpConnector connector, int id) {
		super();
		this.connector = connector;
		this.request = (HttpRequestImpl)connector.createRequest();
		this.response = (HttpResponseImpl)connector.createResponse();
		this.threadName = "HttpProcessor[" + connector.getPort()+"]["+id+"]";
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
	}
	
	public void run() {
		
		while(!stopped) {
			Socket socket = await();
			if(socket == null) {
				continue;
			}
			
			processs(socket);
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

	private void processs(Socket socket) {
		boolean ok = true;
		boolean finishResponse = true;
		SocketInputStream input = null;
		OutputStream output = null;
		
		try {
			input = new SocketInputStream(socket.getInputStream(), connector.getBufferSize());
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		keepAlive = true;
	}

	public void stop() {
		started = false;
		threadStop();
	}

	private void threadStop() {
		
		stopped = true;
		assign(null);
		if(status != Constants.PROCESSOR_IDLE) {
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

}
