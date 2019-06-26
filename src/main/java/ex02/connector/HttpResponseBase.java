package ex02.connector;

import java.io.OutputStream;

import ex02.HttpRequest;
import ex02.HttpResponse;

public class HttpResponseBase implements HttpResponse{
	
	private HttpResponseFacade facade= new HttpResponseFacade(this);
	private OutputStream output = null;
	private HttpRequest request = null;
	
	public HttpResponseFacade getResponse() {
		
		return facade;
		
	}

	public void setStream(OutputStream output) {
		
		this.output  = output;
		
	}

	public void setRequest(HttpRequest request) {
		
		this.request  = request ;
		
	}

	public void recycle() {
		output = null;
		request = null;
	}
}
