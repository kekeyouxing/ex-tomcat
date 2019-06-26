package ex02.connector.http;

public class HttpRequestLine {
	public static final int INITIAL_METHOD_SIZE = 8;
	public static final int MAX_METHOD_SIZE = 1024;
	public static final int INITIAL_URI_SIZE = 64;
	public static final int MAX_URI_SIZE = 32768;
	public static final int INITIAL_PROTOCOL_SIZE = 8;
	public static final int MAX_PROTOCOL_SIZE = 1024;
	
	public char[] method;
	public char[] uri;
	public char[] protocol;
	public int methodEnd;
	public int uriEnd;
	public int protocolEnd;
	
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
	
	public int indexOf(String str) {
		return indexOf(str.toCharArray(), str.length());
	}
	
	private int indexOf(char[] buf, int length) {
		char firstChar = buf[0];
		int pos = 0;
		while(pos<uriEnd) {
			pos = indexOf(firstChar, pos);
			if(pos == -1) {
				return -1;
			}
			if(uriEnd-pos < length) {
				return -1;
			}
			int i = 0;
			while(i < length) {
				if(buf[i] != uri[pos+i])
					break;
				if(i == length-1)
					return i;
				i++;
			}
			pos++;
		}
		return -1;
	}
	
	private int indexOf(char c, int start) {
		for(int i = start; i < uriEnd;i++) {
			if(uri[i] == c)
				return i;
		}
		return -1;
	}

}
