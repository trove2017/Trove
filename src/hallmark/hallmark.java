package hallmark;

import java.util.ArrayList;

public class hallmark {

	private ArrayList<String> nodeID;
	private ArrayList<String> nodeName;
	private ArrayList<Integer> nodeAction;
	
	public hallmark() {
		nodeID=new ArrayList<String>();
		nodeName=new ArrayList<String>();
		nodeAction=new ArrayList<Integer>();
	}

	public void add(String id, String name, int action) {
		nodeID.add(id);
		nodeName.add(name);
		nodeAction.add(action);
	}

	public String getNodeNameAt(int i) {
		if(i>=0 && i<nodeName.size())
			return nodeName.get(i);
		else
			return null;
	}	

	public int getNodeActionAt(int i) {
		if(i>=0 && i<nodeAction.size())
			return nodeAction.get(i);
		else
			return 0;
	}

	public int getSize() {
		return nodeAction.size();
	}
	
	public void print() {
		for(int i=0; i<nodeID.size(); i++)
			System.out.println("["+nodeID.get(i)+"]"+nodeName.get(i)+"="+nodeAction.get(i));
	}
}