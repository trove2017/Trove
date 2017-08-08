package stack;

import java.util.ArrayList;

public class simpleStack
{
	private ArrayList<Integer> stack;

	public simpleStack() {
		stack = new ArrayList<Integer>();
	}

	public int getSize() {
		return stack.size();
	}

	public int getItem(int index) {
		if (index >= 0 && index < getSize())
			return stack.get(index);
		else
			return -1;
	}

	public Boolean isInStack(int item) {
		if (stack.indexOf(item) == -1)
			return false;
		else
			return true;
	}

	public void setIndex(int index, int item) {
		if (index >= 0 && index < getSize())
			stack.set(index, item);
	}

	public void reset() {
		stack = new ArrayList<Integer>();
	}

	public void push(int item) {
		stack.add(item);
	}

	public int pop() {
		return stack.remove(stack.size()-1);
	}
}