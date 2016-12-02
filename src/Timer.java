
public class Timer {
	private int time;
	
	public Timer() {
		this.time = 0;
	}
	
	public int getTime() {
		return this.time;
	}
	
	public void increment() { 
		this.time++;
	}
}
