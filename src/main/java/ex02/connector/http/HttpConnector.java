package ex02.connector.http;

import java.io.IOException;

import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Stack;
import java.util.Vector;

/**
 * <p>
 * <pre>
 * 1.The main method of {@link ex02.startup.Bootstrap} calls HttpConnector's initialize method:}
 * (1)create a sever socket
 * 2.The main method of {@link ex02.startup.Bootstrap} calls HttpConnector's start method:}
 * (1)start the thread
 * (2)create the specified minimum number of processors
 * </pre>
 * </p>
 * @author Ke
 *
 */
public class HttpConnector implements Runnable{
	

	private boolean stopped = false;
	
	private boolean initialized = false;
	
	private ServerSocket server = null;
	
	private int acceptCount = 10;
	
	private int port = 8080;
	
	private String address = "127.0.0.1";
	
	private Stack<HttpProcessor> processors = new Stack<HttpProcessor>();
	
	private Vector<HttpProcessor> created = new Vector<HttpProcessor>();
	
	private int curProcessors = 0;
	
	private int maxProcessors = 20;
	
	private int minProcessors = 5;
	
	private boolean started = false;
	
	private int connectionTimeout = Constants.DEFAULT_CONNECTION_TIMEOUT;
	
	private int bufferSize = 2048;
	
	public int getBufferSize() {
		return bufferSize;
	}
	
	/**
	 * <p>
	 * create a sever socket
	 * </p>
	 */
	public void initialize() {
		if(initialized) {
			return ;
		}
		initialized = true;
		try {
			server = open();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private ServerSocket open() throws UnknownHostException, IOException {
		
		return new ServerSocket(port, acceptCount, InetAddress.getByName(address));
		
	}

	/**
	 * <p>
	 * <pre>
	 * (1)start the connector thread
	 * (2)create the specified minimum number of processors
	 * </pre>
	 * </p>
	 */
	public void start() {
		
		if(started) {
			return;
		}
		started = true;
		
		threadStart();
		
		while(curProcessors<minProcessors) {
			if((maxProcessors > 0) && (curProcessors >= maxProcessors)) {
				break;
			}
			HttpProcessor processor = newProcessor();
			recycle(processor);
		}
		
	}

	/**
	 * <pre>
	 * (1)start the connector thread, this method calls {@link #run()}
	 * (2){@link #run()} 
	 *    (a)create socket
	 *    (b)get the processor form the processor pool
	 *    (c)assign socket to processor
	 * </pre>
	 */
	public void threadStart() {
		
		Thread thread = new Thread(this);
		thread.setName("httpConnector");
		thread.start();
		
	}

	public void run() {
		
		if(!stopped) {
			
			Socket socket = null;
			
			try {
				socket = server.accept();
				if(connectionTimeout > 0)
					socket.setSoTimeout(connectionTimeout);
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			HttpProcessor processor = createProcessor();
			
			if(processor == null) {
				try {
					socket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			processor.assign(socket);
			//The processor will recycle itself when it finishes
		}
		
	}
	
	/**
	 * <pre>
	 * 当HTTP request来的时候,HttpConnector 就会从processor pool中获取processor,
	 * 1.如果processor pool不为空, 那么processor pool有空闲的processor, 可以直接pop出栈
	 * 2.如果processor pool为空, 此时有三种情况
	 *   (a)如果HttpConnector正在使用的processor个数小于processor pool的最大值, 说明还可以创建processor并返回
	 *   (b)如果HttpConnector正在使用的processor个数大于等于processor pool的最大值, 说明不可以创建processor并返回空
	 *   (c)如果processor pool的最大值设置为负数, 说明可以创建无限个processor
	 * </pre>
	 * @return
	 */
	private HttpProcessor createProcessor() {
		
		synchronized (processors) {
			if(processors.size()>0) {
				return processors.pop();
			}
			if(maxProcessors>0 && curProcessors < maxProcessors) {
				return (newProcessor());
			}else 
			{
				if(maxProcessors<0) {
					return (newProcessor());
				}else {
					return null;
				}
			}
		}
		
	}

	private HttpProcessor newProcessor() {
		
		HttpProcessor processor = new HttpProcessor(this, curProcessors++);
		processor.start();
		
		created.add(processor);
		return processor;
		
	}

	public void recycle(HttpProcessor processor) {
		
		processors.push(processor);
		
	}

	public HttpRequestImpl createRequest() {
		
		HttpRequestImpl request = new HttpRequestImpl();
		request.setConnector(this);
		return request;
		
	}

	public HttpResponseImpl createResponse() {
		
		HttpResponseImpl response = new HttpResponseImpl();
		response.setConnector(this);
		return response;
		
	}

	public int getPort() {
		
		return port;
		
	}
}
