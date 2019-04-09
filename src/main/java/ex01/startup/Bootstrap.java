package ex01.startup;

import ex01.Pipeline;
import ex01.core.StandarContext;
import ex01.lifecycle.Lifecycle;
import ex01.lifecycle.LifecycleException;
import ex01.logger.SystemOutLogger;
import ex01.valves.FirstValve;
import ex01.valves.SecondValve;

public class Bootstrap {
	
	public static void main(String[] args) {
		//valve
		FirstValve firstValve = new FirstValve();
		SecondValve secondValve = new SecondValve();
		//lifecycleListener
		ContextConfig config = new ContextConfig();
		//logger
		SystemOutLogger logger = new SystemOutLogger();
		
		StandarContext context = new StandarContext();
		
		((Pipeline)context).addValve(firstValve);
		((Pipeline)context).addValve(secondValve);
		
		((Lifecycle)context).addLifecycleListener(config);
		
		context.setLogger(logger);
		try {
			((Lifecycle)context).start();
			context.invoke();
		} catch (LifecycleException e) {
			e.printStackTrace();
		}
		
		
	}
}
