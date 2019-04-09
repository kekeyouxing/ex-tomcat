package ex01.logger;

public class SystemOutLogger extends LoggerBase{
	
	public void log(String msg) {
		
		System.out.println(msg);
		
	}
}
