package hallmark;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

import org.gephi.clustering.api.Cluster;
import org.gephi.graph.api.Node;

public class hallmarkClusterImpl implements Cluster {
    private int hallmarkID;
    private String hallmarkName;
    private Node hallmarkMetaNode;
    private List<Node> hallmarkNodeList=new ArrayList<Node>();
    
    public hallmarkClusterImpl() {
    	super();
    }
    
    public hallmarkClusterImpl(Node[] nodes) {
    	super();
    	this.hallmarkNodeList=Arrays.asList(nodes);
    }
    
    public hallmarkClusterImpl(List<Node> nodes) {
    	super();
    	this.hallmarkNodeList=nodes;
    }
    
    @Override
    public Node[] getNodes() {
    	return this.hallmarkNodeList.toArray(new Node[0]);
    }

    @Override
    public int getNodesCount() {
        return this.hallmarkNodeList.size();
    }

    @Override
    public String getName() {
        return this.hallmarkName;
    }
    
    public void setName(String name) {
    	this.hallmarkName = name;
    }

    @Override
    public Node getMetaNode() {
        return this.hallmarkMetaNode;
    }

    @Override
    public void setMetaNode(Node node) {
    	this.hallmarkMetaNode = node;
    }
    
    public void setID(int id) {
    	this.hallmarkID = id;
    }
    
    public int getID() {
        return this.hallmarkID;
    }
    
    public void addNode(Node node) {
    	this.hallmarkNodeList.add(node);
    }
    
    public void print() {
    	System.out.println("["+this.hallmarkName+","+this.hallmarkID+"]");
    	for(int i=0; i<this.hallmarkNodeList.size(); i++)
    		System.out.println(this.hallmarkNodeList.get(i).getNodeData().getId());
    }
}