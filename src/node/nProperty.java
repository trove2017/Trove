package node;

import java.math.BigDecimal;
import java.util.ArrayList;

public class nProperty 
{
	private ArrayList<BigDecimal> value;
	private ArrayList<Integer> rank;
	private int maxRank;
	
	public nProperty() {
		value=new ArrayList<BigDecimal>();
		rank=new ArrayList<Integer>();
		maxRank=0;
	}

	public int getMaxRank() {
		return maxRank;
	}
	public void setMaxRank(int r) {
		maxRank=r;
	}
	public int getSize() {
		return value.size();
	}
	public int getRank(int i) {
		if(i>=0 && i<getSize())
			return rank.get(i);
		else
			return -1;
	}
	public void setRank(int i, int r) {
		if(i>=0 && i<getSize())
			rank.set(i, r);
	}
	public BigDecimal getValue(int i) {
		if(i>=0 && i<getSize())
			return value.get(i);
		else
			return null;
	}
	public void setValue(int i, BigDecimal val) {
		if(i>=0 && i<getSize())
			value.set(i, val);
	}
	public void addValueRank(BigDecimal val, int r) {
		value.add(val);
		rank.add(r);
	}
	public int getRankIndex(int startIndex, int r) {
		int lastIndex=rank.lastIndexOf(r);
		if(startIndex>rank.size()-1)
			return -1;

		for(int i=startIndex; i<=lastIndex; i++)
			if(rank.get(i)==r)
				return i;
		return -1;
	}
	public void reset() {
		for(int i=0; i<value.size(); i++)
		{
			value.set(i, null);
			rank.set(i, -1);
		}
		maxRank=0;
	}
	public void print() {
		System.out.println("\tValue\tRank");
		for(int i=0; i<getSize(); i++)
			System.out.print("["+i+"]\t"+value.get(i)+"\t"+rank.get(i));
	}
}