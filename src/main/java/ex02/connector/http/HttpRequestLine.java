package ex02.connector.http;

public class HttpRequestLine {
	private static final int INITIAL_METHOD_SIZE = 8;
	private static final int MAX_METHOD_SIZE = 1024;
	private static final int INITIAL_URI_SIZE = 64;
	private static final int MAX_URI_SIZE = 32768;
	private static final int INITIAL_PROTOCOL_SIZE = 8;
	private static final int MAX_PROTOCOL_SIZE = 1024;
	
	private char[] method;
	private char[] uri;
	private char[] protocol;
	private int methodEnd;
	private int uriEnd;
	private int protocolEnd;
	public HttpRequestLine() {
		
		this(new char[INITIAL_METHOD_SIZE], 0, new char[INITIAL_URI_SIZE], 0, new char[INITIAL_PROTOCOL_SIZE], 0);
		
	}
	public HttpRequestLine(char[] method, int methodEnd, char[] uri, int uriEnd, char[] protocol, int protocolEnd) {
		this.method= method;
		this.uri = uri;
		this.protocol = protocol;
		this.methodEnd = methodEnd;
		this.uriEnd = uriEnd;
		this.protocolEnd = protocolEnd;
	}
	
	public void recycle() {
		this.methodEnd =0;
		this.uriEnd = 0;
		this.protocolEnd = 0;
	}
	
}
