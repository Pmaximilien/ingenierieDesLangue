package x.ministart.utils;

/**
 * Singleton Chronometer class.
 * Simple class to measure the time between "start" and "stop".
 * 
 * TODO Add security.
 * 
 * @author Douglas DUTEIL
 *
 */
public class Chrono {
	private long start = 0;
	private long end = 0;
	
	public Chrono(){};
	
	/**
	 * Starts the chrono.
	 */
	public void start(){
		this.start = System.currentTimeMillis();
	}
	
	/**
	 * Stops the chrono.
	 */
	public void stop(){
		this.end = System.currentTimeMillis();
	}
	
	/**
	 * Returns the time lapses between "start" and "stop".
	 * @return The difference between the start and the end.
	 */
	public long getInterval(){
		return this.end - this.start;
	}
	
	/**
	 * 
	 * @return
	 */
	public String displayInterval(){
		return this.strtime(this.getInterval());
	}

	/**
	 * 
	 * @param t Time in ms.
	 * @return  
	 */
	public String strtime(long t) {
		if (t < 100) return t+"ms";
		long time = t / 1000;  
		String seconds = Integer.toString((int)(time % 60));  
		String minutes = Integer.toString((int)((time % 3600) / 60));  
		String hours = Integer.toString((int)(time / 3600));  
		for (int i = 0; i < 2; i++) {  
			if (seconds.length() < 2) {  
				seconds = "0" + seconds;  
			}  
			if (minutes.length() < 2) {  
				minutes = "0" + minutes;  
			}  
			if (hours.length() < 2) {  
				hours = "0" + hours;  
			}  
		}
		return hours + ":" + minutes + ":" + seconds + " ("+t+"ms)";  
	}
}
