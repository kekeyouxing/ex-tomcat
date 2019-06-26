package ex02.startup;

import java.io.File;
import java.io.IOException;

import org.apache.commons.digester.Digester;
import org.xml.sax.SAXException;

import ex02.connector.http.HttpConnector;

public class Bootstrap {
	
	public static void main(String[] args) {
		String path = System.getProperty("user.dir") + File.separator  + "etc";
		File file = new File(path, "ex2-connector.xml");
		Digester digester = new Digester();
		digester.addRuleSet(new ConnectorRuleSet());
		try {
			HttpConnector connector = (HttpConnector)digester.parse(file);
			connector.initialize();
			connector.start();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		}
	}
}
