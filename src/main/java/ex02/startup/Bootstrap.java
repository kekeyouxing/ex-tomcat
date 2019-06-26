package ex02.startup;

import ex02.SimpleContainer;
import ex02.connector.http.HttpConnector;

public class Bootstrap {
	
	public static void main(String[] args) {
	    HttpConnector connector = new HttpConnector();
	    SimpleContainer container = new SimpleContainer();
	    
	    connector.setContainer(container);
	    connector.initialize();
	    connector.start();
	}
}
