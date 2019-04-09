package ex01.valves;


import ex01.Logger;
import ex01.ValveContext;
import ex01.util.StringManager;

public class SecondValve extends ValveBase {
	
	StringManager sm = StringManager.getManager(Constants.Package);

	@Override
	public void invoke(ValveContext context) {
		
		log("secondValve.invoke");
		context.invokeNext();
	}

	private void log(String message) {
		
		Logger logger = container.getLogger();
		logger.log(sm.getString(message));
		
	}
	
}
