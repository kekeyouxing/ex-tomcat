package ex01.core;


import ex01.lifecycle.Lifecycle;
import ex01.lifecycle.LifecycleException;
import ex01.valves.StandardContextValve;

public class StandarContext extends ContainerBase{
	
	private boolean started = false;
	
	public StandarContext() {
		
		pipeline.setBasic(new StandardContextValve());
		
	}
	
	

	@Override
	public void invoke() {
		
		super.invoke();
		
	}
	
	@Override
	public void start() throws LifecycleException {
		if(started) {
			throw new LifecycleException(sm.getString("Context.alreadyStarted", logName()));
		}
		if (pipeline instanceof Lifecycle)
            ((Lifecycle) pipeline).start();

        // Notify our interested LifecycleListeners
        lifecycle.fireLifecycleEvent(START_EVENT, null);
		
	}

	@Override
	public void stop() throws LifecycleException {
		
		
	}
	
}
