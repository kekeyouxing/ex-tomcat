package ex02;

import ex02.connector.http.HttpRequestImpl;
import ex02.connector.http.HttpResponseImpl;

public class SimpleContainer implements Container{

	public void invoke(HttpRequestImpl request, HttpResponseImpl response) {
		
		System.out.println("	request's method is: "+request.getMethod());
		System.out.println("	request's URI is: "+request.getRequestURI());
		System.out.println("	request's protocol is: "+request.getProtocol());
		
		
	}

}
