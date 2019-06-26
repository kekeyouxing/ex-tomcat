package ex02;

import java.io.OutputStream;

public interface HttpResponse {

	public void setStream(OutputStream output);
	
	public void setRequest(HttpRequest request);
}
