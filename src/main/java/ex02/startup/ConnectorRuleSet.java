package ex02.startup;

import org.apache.commons.digester.Digester;
import org.apache.commons.digester.RuleSetBase;

public class ConnectorRuleSet extends RuleSetBase{

	@Override
	public void addRuleInstances(Digester digester) {
		digester.addObjectCreate("HttpConnector", "ex02.connector.http.HttpConnector");
		digester.addObjectCreate("HttpConnector/SimpleContainer", "ex02.SimpleContainer");
		digester.addSetNext("HttpConnector/SimpleContainer", "setContainer");
	}

}
