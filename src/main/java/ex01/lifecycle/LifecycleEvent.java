package ex01.lifecycle;

import java.util.EventObject;

public class LifecycleEvent extends EventObject{

	private static final long serialVersionUID = 1L;
	
	protected Lifecycle lifecycle =null;
	
	protected String type = null;
	
	protected Object data = null;
	
	
	public Lifecycle getLifecycle() {
		return lifecycle;
	}

	public String getType() {
		return type;
	}

	public Object getData() {
		return data;
	}

	public LifecycleEvent(Lifecycle lifecycle, String type) {
		
		this(lifecycle, type, null);
		
	}
	
	public LifecycleEvent(Lifecycle lifecycle, String type, Object data) {
		
		super(lifecycle);
		this.lifecycle = lifecycle;
		this.type = type;
		this.data = data;
	}
	
}
