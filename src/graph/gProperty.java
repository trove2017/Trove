package graph;

import java.util.ArrayList;

import constants.stringConstant;

public class gProperty 
{
	ArrayList<String> groupName;
	ArrayList<ArrayList<String>> itemName;
	private stringConstant gString;
	
	public gProperty() {
		groupName=new ArrayList<String>();
		itemName=new ArrayList<ArrayList<String>>();
		gString=new stringConstant();
	}
	
	public void newRecord() {
		groupName.add(null);
		itemName.add(new ArrayList<String>());
	}
	public void removeRecord(int gpIndex) {
		if(gpIndex>=0 && gpIndex<getSize())
		{
			groupName.remove(gpIndex);
			itemName.remove(gpIndex);
		}
	}
	public int getSize() {
		return groupName.size();
	}
	public String getGroupName(int gpIndex) {
		if(gpIndex>=0 && gpIndex<getSize())
			return groupName.get(gpIndex);
		else
			return null;
	}
	
	public void setGroupName(int gpIndex, String gpName) {
		if(gpIndex>=0 && gpIndex<getSize())
			groupName.set(gpIndex, gpName);
	}
	
	public int getGroupIndexWithGroupName(String gpName) {
		return groupName.indexOf(gpName);
	}
	
	public String getItemInGroup(int gpIndex, int itemIndex) {
		if(gpIndex>=0 && gpIndex<getSize() && itemIndex>=0 && itemIndex<getGroupSize(gpIndex))
			return itemName.get(gpIndex).get(itemIndex);
		else
			return null;
	}
	
	public void addItemInGroup(int gpIndex, String gpName) {
		if(gpIndex>=0 && gpIndex<getSize())
			if(itemName.get(gpIndex).indexOf(gpName)==-1)
				itemName.get(gpIndex).add(gpName);
	}
	
	public void appendItemInGroup(int gpIndex, ArrayList<String> itemNameList) {
		if(gpIndex>=0 && gpIndex<getSize() && itemNameList!=null)
		{
			int itemNameListIndex=0;
			while(itemNameListIndex<itemNameList.size())
			{
				if(itemName.get(gpIndex).indexOf(itemNameList.get(itemNameListIndex))==-1)
					itemName.get(gpIndex).add(itemNameList.get(itemNameListIndex));
				itemNameListIndex++;
			}
		}
	}
	
	public void setItemInGroup(int gpIndex, int itemIndex, String iName) {
		if(gpIndex>=0 && gpIndex<getSize() && itemIndex>=0 && itemIndex<getGroupSize(gpIndex))
			itemName.get(gpIndex).set(itemIndex, iName);
	}
	
	public ArrayList<String> getItemNameList(int gpIndex) {
		if(gpIndex>=0 && gpIndex<getSize())
			return itemName.get(gpIndex);
		else
			return null;
	}	
	
	public int getItemNameIndex(int gpIndex, String iName) {
		return itemName.get(gpIndex).indexOf(iName);
	}
	
	public void newItemRecord(int gpIndex) {
		if(gpIndex>=0 && gpIndex<getSize())
			itemName.get(gpIndex).add(null);
	}
	
	public int getGroupSize(int gpIndex) {
		if(gpIndex>=0 && gpIndex<getSize())
			return itemName.get(gpIndex).size();
		else
			return -1;
	}
	
	public int getRealItemGroupSize(int gpIndex) {
		int count=0;
		for(int j=0; j<getGroupSize(gpIndex); j++)
		{
			if(getItemInGroup(gpIndex,j).indexOf(gString.getVirtualNodePrefix())==-1)
				count++;
		}
		return count;
	}
	
	public int getGroupIndexWithItemName(String iName) {
		for(int i=0; i<getSize(); i++)
		{
			if(itemName.get(i).indexOf(iName)!=-1)
				return i;
		}
		return -1;

	}
	
	public void print() {
		System.out.println("Group Name\tItem Name");
		for(int i=0; i<getSize(); i++)
		{
			System.out.print(groupName.get(i)+"\t");
			for(int j=0; j<getGroupSize(i); j++)
				System.out.print(itemName.get(i).get(j)+",");
			System.out.println("");
		}
	}

	public String getDataStream() {
		String dataStream;
		String newLine="\r\n";
		
		dataStream="Group Name\tItem Name" + newLine;
		for(int i=0; i<getSize(); i++)
		{
			dataStream=dataStream + groupName.get(i) + "("+getGroupSize(i)+")\t" + newLine;
			for(int j=0; j<getGroupSize(i); j++)
				dataStream=dataStream + itemName.get(i).get(j) + "," +newLine;
			
			dataStream=dataStream + newLine;
		}
		return dataStream;
	}
}