package counter;

public class simpleCounter
{
	private int counter;

	public simpleCounter() {
		counter = 0;
	}

	public void reset() {
		counter = 0;
	}

	public void increment() {
		counter++;
	}

	public void decrement() {
		counter--;
	}

	public int getCount() {
		return counter;
	}
}