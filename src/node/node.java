package node;

import java.util.ArrayList;
import constants.stringConstant;

public class node {
	private ArrayList<String> nodeId, nodeName;
	private ArrayList<Boolean> isVirtualNode;
	private annotationList annotation;
	private nSimpleProperty preorder, postorder;
	private int size;
	private stringConstant staticString;

	public node() {
		nodeId = new ArrayList<String>();
		nodeName = new ArrayList<String>();
		preorder = new nSimpleProperty();
		postorder = new nSimpleProperty();
		isVirtualNode = new ArrayList<Boolean>();
		annotation = new annotationList();
		size = 0;
		staticString = new stringConstant();
	}

	public void newRecord() {
		nodeId.add(null);
		nodeName.add(null);
		preorder.addItem(-1);
		postorder.addItem(-1);
		isVirtualNode.add(null);
		annotation.newRecord();
			size++;
	}

	public int getSize() {
		return size;
	}

	public annotationList getAnnotation() {
		return annotation;
	}

	public nSimpleProperty getPreorder() {
		return preorder;
	}
	
	public nSimpleProperty getPostorder() {
		return postorder;
	}
	
	public void removeNodeWithId(String id) {
		int index=nodeId.indexOf(id);
		
		if(index!=-1)
		{
			nodeId.remove(index);
			nodeName.remove(index);
			isVirtualNode.remove(index);
			size=size-1;
			annotation.removeAnnotation(index);
			preorder.removeItem(index);
			postorder.removeItem(index);
			preorder.reset();
			postorder.reset();
		}
	}
	
	public int getNumRealNodesInList(ArrayList<String> idList) {
		int counter=0;
		for(int i=0; i<idList.size(); i++)
		{
			int index=nodeId.indexOf(idList.get(i));
			if(index!=-1 && isVirtualNode.get(index)==false)
				counter=counter+1;
		}
		return counter;
	}
	
	public void printAllProperties() {
		System.out.println("Node Name\tNode ID\t\tPreorder\tPostorder\tVirtual Node");
		for (int i = 0; i < size; i++)
			System.out.println("[" + i + "] " + nodeName.get(i) + "\t" + nodeId.get(i) + "\t\t" + preorder.getItem(i)
					+ "\t" + postorder.getItem(i) + "\t" + isVirtualNode.get(i));
	}
	
	public String getDataStream() {
		String dataStream;
		String newLine=staticString.getNewline(), tab=staticString.getTab();
		
		dataStream="Node Name"+tab+"Node ID"+tab+tab+"Preorder"+tab+"Postorder"+tab+"Virtual Node"+newLine; 
		
		for (int i = 0; i < size; i++) 
		{
			dataStream=dataStream + "[" + i + "] " + nodeName.get(i) + tab + nodeId.get(i) + tab + tab + preorder.getItem(i) + tab
					+ postorder.getItem(i) + tab + isVirtualNode.get(i) + newLine;
		}
		
		return dataStream;
	}

	// ******* nodeId ******************
	public ArrayList<String> getNodeIdList() {
		return nodeId;
	}

	public String getNodeId(int i) {
		if (i >= 0 && i < nodeId.size())
			return nodeId.get(i);
		else
			return null;
	}

	public void setNodeId(int i, String id) {
		if (i >= 0 && i < size)
			nodeId.set(i, id);
	}

	public int getNodeIdIndex(String id) {
		return nodeId.indexOf(id);
	}

	// ******* nodeName **************
	public ArrayList<String> getNodeNameList() {
		return nodeName;
	}

	public String getNodeName(int i) {
		if (i >= 0 && i < size)
			return nodeName.get(i);
		else
			return null;
	}

	public void setNodeName(int i, String name) {
		if (i >= 0 && i < size)
			nodeName.set(i, name);
	}
	
	public int getNodeNameIndex(String name) {
		return nodeName.indexOf(name);
	}

	// ******* isVirtualNode **************
	public Boolean getIsVirtualNode(int i) {
		if (i >= 0 && i < size)
			return isVirtualNode.get(i);
		else
			return null;
	}

	public void setIsVirtualNode(int i, Boolean bool) {
		if (i >= 0 && i < size)
			isVirtualNode.set(i, bool);
	}
}