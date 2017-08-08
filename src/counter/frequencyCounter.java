package counter;

import java.util.ArrayList;

public class frequencyCounter
{
	private ArrayList<Integer> item;
	private ArrayList<Integer> count;

	public frequencyCounter() {
		item=new ArrayList<Integer>();
		count=new ArrayList<Integer>();
	}

	public void addItem(int itemValue, int c) {
		item.add(itemValue);
		count.add(c);
	}

	public int getSize() {
		return item.size();
	}

	public int getItemIndex(int itemValue) {
		return item.indexOf(itemValue);
	}

	public int getItem(int index) {
		if(index>=0 && index<getSize())
			return item.get(index);
		else
			return -1;
	}

	public int getCount(int index) {
		if(index>=0 && index<getSize())
			return count.get(index);
		else
			return -1;
	}

	public void setItem(int index, int itemValue) {
		if(index>=0 && index<getSize())
			item.set(index, itemValue);
	}

	public void setCount(int index, int itemValue) {
		if(index>=0 && index<getSize())
			count.set(index, itemValue);
	}

	public void reset() {
		item=new ArrayList<Integer>();
		count=new ArrayList<Integer>();
	}
	
	public void print() {
		System.out.println("\tItem\tCount");
		for(int i=0; i<getSize(); i++)
			System.out.print("["+i+"]\t"+item.get(i)+"\t"+count.get(i));
	}
}