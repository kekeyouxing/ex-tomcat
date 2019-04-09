package ex01.core;


import ex01.Container;
import ex01.Logger;
import ex01.Pipeline;
import ex01.Valve;
import ex01.lifecycle.Lifecycle;
import ex01.lifecycle.LifecycleException;
import ex01.lifecycle.LifecycleListener;
import ex01.lifecycle.LifecycleSupport;
import ex01.util.StringManager;

public class ContainerBase implements Container, Lifecycle, Pipeline{
	
	protected LifecycleSupport lifecycle = new LifecycleSupport(this);
	
	protected Pipeline pipeline = new StandarPipeline(this);
	
	protected Logger logger = null;
	
	private boolean started = false;
	
	StringManager sm = StringManager.getManager(Constants.Package);
	
	public Valve getBasic() {
		
		return pipeline.getBasic();
		
	}

	
	public void setBasic(Valve basic) {
		
		pipeline.setBasic(basic);
		
	}

	
	public void addValve(Valve valve) {
		
		pipeline.addValve(valve);
		
	}

	
	public Valve[] getValves() {
		
		return pipeline.getValves();
		
	}

	
	public void removeValve(Valve valve) {
		
		pipeline.removeValve(valve);
		
	}

	
	public void addLifecycleListener(LifecycleListener listener) {
		
		lifecycle.addLifecycleListener(listener);
		
	}

	
	public void removeLifecycleListener(LifecycleListener listener) {
		
		lifecycle.removeLifecycleListener(listener);
		
	}

	
	public LifecycleListener[] findLifecycleListeners() {
		
		return lifecycle.findLifecycleListeners();
		
	}

	
	public void start() throws LifecycleException {
		
		if(started) {
			throw new LifecycleException(sm.getString("containerBase.alreadyStarted", logName()));
		}
		
		if(pipeline instanceof Lifecycle) {
			((Lifecycle)pipeline).start();
		}
		
		lifecycle.fireLifecycleEvent(START_EVENT, null);
	}

	
	public void stop() throws LifecycleException {
		
		
		
	}

	
	public synchronized void setLogger(Logger logger) {
		
		Logger oldLogger = this.logger;
		if(oldLogger == logger) {
			return ;
		}
		this.logger = logger;
		if(logger != null) {
			logger.setContainer(this);
		}
	}

	
	public Logger getLogger() {
		
		return logger;
		
	}

	
	public void invoke() {
		pipeline.invoke();
	}

	protected String logName() {
		
		String className = this.getClass().getName();
		int period = className.lastIndexOf('.');
		if(period >= 0) {
			className = className.substring(period+1);
		}
		return className;
		
	}
}
