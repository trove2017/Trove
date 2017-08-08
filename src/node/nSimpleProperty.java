package node;

import java.util.ArrayList;

public class nSimpleProperty
{
	private ArrayList<Integer> p;
	
	public nSimpleProperty() {
		p=new ArrayList<Integer>();
	}
	
	public void initialize() {
		for (int i = 0; i < getSize(); i++) {
			setItem(i, -1);
		}
	}

	public void addItem(int i) {
		p.add(i);
	}

	public int getSize() {
		return p.size();
	}

	public int getIndex(int item) {
		return p.indexOf(item);
	}

	public int getItem(int index) {
		if(index>=0 && index<getSize())
			return p.get(index);
		else
			return -1;
	}

	public void setItem(int index, int item) {
		if(index>=0 && index<getSize())
			p.set(index, item);
	}

	public void removeItem(int index) {
		if(index>=0 && index<getSize())
			p.remove(index);
	}
	
	public void reset() {
		for(int i=0; i<p.size(); i++)
			setItem(i, -1);
	}
	
	public void print() {
		for(int i=0; i<getSize(); i++)
			System.out.print("["+i+"]\t"+p.get(i));
	}
}