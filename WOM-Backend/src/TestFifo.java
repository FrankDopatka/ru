import java.util.concurrent.LinkedBlockingQueue;


public class TestFifo {

	public static void main(String[] args) {
		LinkedBlockingQueue<String> fifo=new LinkedBlockingQueue<String>();
		System.out.println(fifo.poll());
		System.out.println(fifo.poll());
		fifo.add("a");
		fifo.add("b");
		fifo.add("c");
		fifo.add("d");
		System.out.println(fifo.poll());
		System.out.println(fifo.poll());
		System.out.println(fifo.poll());
		System.out.println(fifo.poll());
	}

}
