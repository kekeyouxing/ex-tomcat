package ex01;

public interface Pipeline {

	public Valve getBasic();
	
	public void setBasic(Valve valve);
	
	public void addValve(Valve valve);
	
	public Valve[] getValves();
	
	public void removeValve(Valve valve);
	
	public void invoke();
	
}
