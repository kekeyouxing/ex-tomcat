package ex02;

import ex02.connector.http.HttpRequestImpl;
import ex02.connector.http.HttpResponseImpl;

public interface Container {
	
	public void invoke(HttpRequestImpl request, HttpResponseImpl response);
	
}
