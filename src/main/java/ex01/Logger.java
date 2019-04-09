package ex01;

public interface Logger {

	public Container getContainer();
	
	public void setContainer(Container container);
	
	public void log(String message);
	
	public void log(String message, Throwable throwable);
}
