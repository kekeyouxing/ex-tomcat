package ex02;

import java.io.InputStream;

public interface HttpRequest {
	
	public void setStream(InputStream input);
	
	public void setResponse(HttpResponse response);
}
