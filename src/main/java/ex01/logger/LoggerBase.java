package ex01.logger;

import ex01.Container;
import ex01.Logger;

public abstract class LoggerBase implements Logger{

	protected Container container = null;
	
	public Container getContainer() {
		
		return container;
		
	}

	
	public void setContainer(Container container) {
		
		this.container = container;
		
	}

	
	public abstract void log(String message);
	
	
	public void log(String message, Throwable throwable) {
		
	}

}
