package ex01;

public interface Container {
	
	public static final String ADD_VALVE_EVENT = "addValve";
	
	public static final String REMOVE_VALVE_EVENT = "removeValve";
	
	public void setLogger(Logger logger);
	
	public Logger getLogger();
	
	public void invoke();
	
	
}
