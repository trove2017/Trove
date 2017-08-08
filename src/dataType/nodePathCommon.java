package dataType;

import java.util.ArrayList;

public class nodePathCommon {

	private ArrayList<String> consoDiseaseNodeList;
	private ArrayList<String> commonMetaNodeAncestors;
		
	public nodePathCommon() {
		consoDiseaseNodeList=new ArrayList<String>();;
		commonMetaNodeAncestors=new ArrayList<String>();
	}

	public nodePathCommon(ArrayList<String> nodeName, ArrayList<String> ancestor) {
		consoDiseaseNodeList=nodeName;
		commonMetaNodeAncestors=ancestor;
	}
	
	public int numAncestors() {
		return commonMetaNodeAncestors.size();
	}

	public ArrayList<String> getDiseaseNodeList() {
		return consoDiseaseNodeList;
	}	

	public ArrayList<String> getCommonMetaNodeAncestors() {
		return commonMetaNodeAncestors;
	}

	public String getCommonMetaNodeAncestorsAtIndex(int i) {
		if(i>=0 && i<numAncestors())
			return commonMetaNodeAncestors.get(i);
		else
			return null;
	}

	public void print() {
		System.out.println(consoDiseaseNodeList.toString()+": "+commonMetaNodeAncestors.toString());
	}
}