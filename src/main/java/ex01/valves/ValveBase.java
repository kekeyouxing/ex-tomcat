package ex01.valves;

import ex01.Contained;
import ex01.Container;
import ex01.Valve;
import ex01.ValveContext;

public abstract class ValveBase implements Contained, Valve{
	protected Container container = null;
	
	public Container getContainer() {
		
		return container;
		
	}
	
	public void setContainer(Container container) {
		
		this.container = container;
		
	}
	
	public abstract void invoke(ValveContext context);
}
