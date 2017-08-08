package dataType;

import java.util.ArrayList;

public class nodePath {

	private String metaNodeName;
	private ArrayList<String> metaNodeAncestors;
		
	public nodePath() {
		metaNodeName=null;
		metaNodeAncestors=new ArrayList<String>();
	}

	public nodePath(String nodeName, ArrayList<String> ancestor) {
		metaNodeName=nodeName;
		metaNodeAncestors=ancestor;
	}
	
	public int numAncestors() {
		return metaNodeAncestors.size();
	}

	public String getMetaNode() {
		return metaNodeName;
	}	

	public ArrayList<String> getMetaNodeAncestors() {
		return metaNodeAncestors;
	}

	public String getMetaNodeAncestorsAtIndex(int i) {
		if(i>=0 && i<numAncestors())
			return metaNodeAncestors.get(i);
		else
			return null;
	}

	public void remove(String ancestor)
	{
		metaNodeAncestors.remove(ancestor);
	}
	
	public void print() {
		System.out.println(metaNodeName+": "+metaNodeAncestors.toString());
	}
}