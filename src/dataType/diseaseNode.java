package dataType;

import java.util.ArrayList;

public class diseaseNode {

	private ArrayList<String> nodeList;
	private ArrayList<String> metaNodeList;
	private ArrayList<Double> logFCList;
	private ArrayList<Double> mutationFreqList;

	public diseaseNode() {
		nodeList=new ArrayList<String>();
		metaNodeList=new ArrayList<String>();
		logFCList=new ArrayList<Double>();
		mutationFreqList=new ArrayList<Double>();
	}

	public int size() {
		return nodeList.size();
	}

	public void add(String node, String metaNode, Double logFC, Double mutationFreq) {
		int i=nodeList.indexOf(node);
		if(i==-1)//node is not in nodeList
		{
			nodeList.add(node);
			metaNodeList.add(metaNode);
			logFCList.add(logFC);
			mutationFreqList.add(mutationFreq);
		}
	}	

	public void removeNode(int index) {
		if(index>=0 && index<size())
		{
			nodeList.remove(index);
			metaNodeList.remove(index);
			logFCList.remove(index);
			mutationFreqList.remove(index);
		}
	}	
	
	public ArrayList<String> getNodeList() {
		return nodeList;
	}

	public void setMetaNode(String node, String metaNode) {
		int index=nodeList.indexOf(node);
		if(index!=-1)
			metaNodeList.set(index, metaNode);
	}
	
	public ArrayList<String> getNodeBelongingToMetaNode(String metaNode)
	{
		ArrayList<String> l=new ArrayList<String>();
		
		for(int i=0; i<size(); i++)
		{
			if(metaNodeList.get(i).compareTo(metaNode)==0 && l.contains(nodeList.get(i))==false)
				l.add(nodeList.get(i));
		}
		return l;
	}
	
	public String getNodeAtIndex(int i) {
		if(i>=0 && i<size())
			return nodeList.get(i);
		else
			return null;
	}

	public ArrayList<Double> getLogFCList() {
		return logFCList;
	}

	public Double getLogFCAtIndex(int i) {
		if(i>=0 && i<size())
			return logFCList.get(i);
		else
			return null;
	}

	public ArrayList<Double> getMutationFreqList() {
		return mutationFreqList;
	}

	public Double getMutationFreqAtIndex(int i) {
		if(i>=0 && i<size())
			return mutationFreqList.get(i);
		else
			return null;
	}
	
	public boolean containsNode(String name) {
		return nodeList.contains(name);
	}
	
	public String getMetaNodeOf(String name) {
		int index=nodeList.indexOf(name);
		if(index!=-1)
			return metaNodeList.get(index);
		else
			return null;
	}
	
	public void print() {
		System.out.println("\tNode\tMetaNode\tLogFC\tMutationFreq");
		for(int i=0; i<size(); i++)
			System.out.println("["+i+"]:\t"+nodeList.get(i)+"\t"+metaNodeList.get(i)+"\t"+logFCList.get(i)+"\t"+mutationFreqList.get(i));
	}
}