package ex01.lifecycle;


public class LifecycleSupport {
	
	private LifecycleListener[]  listeners = new LifecycleListener[0];
	
	protected Lifecycle lifecycle = null;
	
	public LifecycleSupport(Lifecycle lifecycle) {
		
		super();
		this.lifecycle = lifecycle;
		
	}
	
	public void addLifecycleListener(LifecycleListener listener) {
		
		synchronized (listeners) {
			
			LifecycleListener[] results = new LifecycleListener[listeners.length + 1];
			System.arraycopy(listeners, 0, results, 0, listeners.length);
			results[listeners.length] = listener;
			listeners = results;
			
		}
		
	}
	
	public void removeLifecycleListener(LifecycleListener listener) {
		
		synchronized (listeners) {
			int n = -1;
			for(int i = 0; i<listeners.length; i++) {
				if(listeners[i] != listener) {
					n=i;
					break;
				}
			}
			
			if(n == -1) {
				return ;
			}
			
			int j = 0;
			LifecycleListener[] results = new LifecycleListener[listeners.length-1];
			for(int i =0 ;i<listeners.length; i++) {
				if(i != n) {
					results[j++] = listeners[i];
				}
			}
			listeners = results;
		}
		
	}
	
	public LifecycleListener[] findLifecycleListeners() {
		
		return listeners;
		
	}
	
	public void fireLifecycleEvent(String type, Object data) {
		LifecycleEvent event = new LifecycleEvent(lifecycle, type);
		LifecycleListener[] interested = null;
		synchronized (listeners) {
			interested = listeners.clone();
		}
		for(int i = 0; i<interested.length; i++) {
			interested[i].lifecycleEvent(event);
		}
		
	}
	
}
