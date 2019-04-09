package ex01.core;

import ex01.Contained;
import ex01.Container;
import ex01.Pipeline;
import ex01.Valve;
import ex01.ValveContext;
import ex01.lifecycle.Lifecycle;
import ex01.lifecycle.LifecycleException;
import ex01.lifecycle.LifecycleListener;
import ex01.lifecycle.LifecycleSupport;

public class StandarPipeline implements Pipeline, Contained, Lifecycle{
	
	Valve basic = null;
	
	Valve[] valves = new Valve[0];
	
	protected Container container = null;
	
	protected LifecycleSupport lifecycle = new LifecycleSupport(this);
	
	public StandarPipeline(Container container) {
		
		setContainer(container);
		
	}
	
	public void setContainer(Container container) {
		
		this.container = container;
		
	}
	
	public Container getContainer() {
		
		return container;
		
	}
	
	public Valve getBasic() {
		
		return basic;
		
	}

	
	public void setBasic(Valve valve) {
		
		Valve oldBasic = this.basic;
		if(oldBasic == valve) {
			return ;
		}
		if(oldBasic != null) {
			if(oldBasic instanceof Contained) {
				((Contained)oldBasic).setContainer(null);
			}
		}
		
		if(valve == null) {
			return ;
		}
		if(valve instanceof Contained) {
			((Contained)valve).setContainer(this.container);
		}
		this.basic = valve;
		
	}

	
	public void addValve(Valve valve) {
		
		if(valve instanceof Contained) {
			((Contained)valve).setContainer(this.container);
		}
		synchronized (valves) {
			Valve[] results = new Valve[valves.length+1];
			System.arraycopy(valves, 0, results, 0, valves.length);
			results[valves.length] = valve;
			valves = results;
		}
		
	}

	
	public Valve[] getValves() {
		
		if(basic ==  null) {
			return valves;
		}
		synchronized (valves) {
			Valve[] results = new Valve[valves.length+1];
			System.arraycopy(valves, 0, results, 0, valves.length);
			results[valves.length] = basic;
			valves = results;
			return valves;
		}
		
		
	}

	
	public void removeValve(Valve valve) {
		
		synchronized (valves) {
			int n = -1;
			for(int i = 0; i<valves.length; i++) {
				if(valves[i] == valve) {
					n = i;
					break;
				}
			}
			if(n < 0) {
				return ;
			}
			int j = 0;
			Valve[] results = new Valve[valves.length-1];
			for(int i = 0; i<valves.length; i++) {
				if(i != n) {
					results[j++] = valves[i];
				}
			}
			valves = results;
		}
		if(valve instanceof Contained) {
			((Contained)valve).setContainer(null);
		}
		
	}

	
	public void invoke() {
		(new StandarPipelineValveContext()).invokeNext();
	}
	
	protected class StandarPipelineValveContext implements ValveContext{
		
		protected int stage = 0;
		
		
		
		public void invokeNext() {
			int subscript = stage;
			stage = stage + 1;
			if(subscript < valves.length) {
				valves[subscript].invoke(this);
			}else if((subscript == valves.length) && (basic != null)) {
				basic.invoke(this);
			}
			else {
				//subscript > valves.length
			}
		}
		
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
		
		lifecycle.fireLifecycleEvent(START_EVENT, null);
		
	}

	
	public void stop() throws LifecycleException {
		
	}

}
