package ex02.connector;

import java.io.InputStream;
import java.net.InetAddress;
import java.net.Socket;

import ex02.HttpRequest;
import ex02.HttpResponse;

public class HttpRequestBase implements HttpRequest{

	private HttpResponse response = null;
	private InputStream input = null;
	private InetAddress inet = null;
	private int serverPort = 0;
	private Socket socket = null;
	private String queryString = null;
	private String sessionId;
	private boolean requestedSessionURL;
	private String method;
	private String requestURI;
	private String protocol;
	public String getMethod() {
		return method;
	}

	public String getRequestURI() {
		return requestURI;
	}

	public String getProtocol() {
		return protocol;
	}
	
	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public Socket getSocket() {
		return socket;
	}

	public String getQueryString() {
		return queryString;
	}

	public boolean isRequestedSessionURL() {
		return requestedSessionURL;
	}
	
	public InputStream getInput() {
		
		return input;
		
	}

	public void setInput(InputStream input) {
		
		this.input = input;
		
	}

	public HttpResponse getResponse() {
		
		return response;
		
	}

	public InetAddress getInet() {
		return inet;
	}

	public int getServerPort() {
		return serverPort;
	}

	public void setStream(InputStream input) {
		
		this.input = input;
		
	}

	public void setResponse(HttpResponse response) {
		
		this.response = response;
		
	}

	public void setInet(InetAddress inetAddress) {
		
		this.inet  = inetAddress;
		
	}

	public void setServerPort(int port) {
		
		this.serverPort  = port;
		
	}

	public void setSocket(Socket socket) {
		
		this.socket = socket;
		
	}

	public void setQueryString(String query) {
		
		this.queryString  = query;
		
	}

	public void setRequestedSessionId(String id) {
		
		this.sessionId = id;
		
	}
	

	public void setRequestedSessionURL(boolean flag) {
		
		this.requestedSessionURL = flag;
		
	}
	
	public void setMethod(String method) {
		
		this.method = method;
		
	}
	
	public void setRequestURI(String uri) {
		
		this.requestURI = uri;
		
	}

	public void setProtocol(String protocol) {
		
		this.protocol = protocol;
		
	}

	public void recycle() {
		response = null;
		input = null;
		inet = null;
		serverPort = 0;
		socket = null;
		queryString = null;
		sessionId = null;
		requestedSessionURL = false;
		method = null;
		requestURI = null;
		protocol = null;
	}

}
