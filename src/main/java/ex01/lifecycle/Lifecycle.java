package ex01.lifecycle;

public interface Lifecycle {

	public static final String START_EVENT="start"; 
	
	public static final String STOP_EVENT = "stop";
	
	public void addLifecycleListener(LifecycleListener listener);
	
	public void removeLifecycleListener(LifecycleListener listener);
	
	public LifecycleListener[] findLifecycleListeners();

	public void start() throws LifecycleException;
	
	public void stop() throws LifecycleException;
}
