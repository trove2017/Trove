package graph;

import java.util.ArrayList;

import constants.stringConstant;

import node.node;
import stack.simpleStack;
import counter.simpleCounter;

public class graph 
{
	private node vertex;
	private gProperty scc, branch, dag;
	private int numRealNodes;
	private simpleStack indexStack;
	private simpleCounter indexCounter;
	private stringConstant staticString=new stringConstant();
	
	public graph() {
		vertex = new node();
		scc = new gProperty();
		branch = new gProperty();
		dag = new gProperty();
		indexCounter = new simpleCounter();
		indexStack = new simpleStack();
		numRealNodes = 0;
	}
	
	public void removeNodes(ArrayList<String> list)
	{
		int numRealNodesInList=vertex.getNumRealNodesInList(list);
		scc = new gProperty();
		branch = new gProperty();
		dag = new gProperty();
		numRealNodes = numRealNodes-numRealNodesInList;
		for(int i=0; i<list.size(); i++)
			vertex.removeNodeWithId(list.get(i));
	}

	public node getNode() {
		return vertex;
	}
	
	public ArrayList<String> getComplementaryNodeList(ArrayList<String> list, ArrayList<String> cList){
		ArrayList<String> entireList=new ArrayList<String>();
		for(int i=0; i<vertex.getNodeIdList().size(); i++)
			entireList.add(vertex.getNodeIdList().get(i));
		
		for(int i=0; i<entireList.size(); i++)
		{
			if(list.contains(entireList.get(i))==false)
				cList.add(entireList.get(i));
		}
		return cList;
	}
	
	public gProperty getSCC() {
		return scc;
	}
	
	public ArrayList<String> getItemsOfMetaNode(String metaNode){
		return scc.getItemNameList(scc.getGroupIndexWithGroupName(metaNode));
	}
	
	public String getSCCMetaNodeOf(String node) {
		int index=scc.getGroupIndexWithItemName(node);
		if(index==-1)
			return null;
		else
			return scc.getGroupName(index);
	}
	
	public gProperty getBranch() {
		return branch;
	}
	
	public gProperty getDAG() {
		return dag;
	}
	
	public simpleCounter getIndexCounter() {
		return indexCounter;
	}
	
	public simpleStack getIndexStack() {
		return indexStack;
	}
	
	// ***** numRealNodes functions **************************************
	public void setNumRealNodes(int n) {
		numRealNodes = n;
	}

	public int getNumRealNodes() {
		return numRealNodes;
	}

	public String getDataStream() {
		String dataStream="";
		String newLine=staticString.getNewline();
/*				
		//print node information
		//dataStream="Node Info (All)---------------------------------"+ newLine + vertex.getDataStream() + newLine;
		//print SCC information
		dataStream=dataStream + "SCC Info ------------------------------------" + newLine + getSCC().getDataStream() + newLine;
		//print branch information
		dataStream=dataStream + "Branch Info ------------------------------------" + newLine + getBranch().getDataStream() + newLine;
		//print DAG information
		dataStream=dataStream + "DAG Info ------------------------------------" + newLine + getDAG().getDataStream();
*/		
		return dataStream;
	}
}