package ex01.startup;

import ex01.core.StandarContext;
import ex01.lifecycle.LifecycleEvent;
import ex01.lifecycle.LifecycleListener;

public class ContextConfig implements LifecycleListener{

	
	public void lifecycleEvent(LifecycleEvent event) {
		
		StandarContext context = (StandarContext)event.getLifecycle();
		String type = event.getType();
		if(context instanceof StandarContext) {
			if(type.equals("start")) {
				System.out.println("StandarContext starting");
			}
		}
		
	}

}
