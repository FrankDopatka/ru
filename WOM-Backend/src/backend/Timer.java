package backend;

public class Timer {
	private long startzeit;

	public Timer(){
		startzeit=System.currentTimeMillis();
	}

	public String stop(){
		return (System.currentTimeMillis()-startzeit)+"ms";
	}
}
