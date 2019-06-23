package ex02.startup;

import ex02.connector.http.HttpConnector;

public class Bootstrap {
	
	public String requestLine = "";
	public static void main(String[] args) {
	    HttpConnector connector = new HttpConnector();
	    connector.initialize();
	    connector.start();
	    
	}
}
