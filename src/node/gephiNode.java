package node;

import java.util.ArrayList;

public class gephiNode {
	private ArrayList<String> nodeName;
	private ArrayList<Float> nodeSize;
	private ArrayList<Float> nodeR;
	private ArrayList<Float> nodeG;
	private ArrayList<Float> nodeB;
	private ArrayList<Float> nodeAlpha;
	
	public gephiNode() {
		nodeName=new ArrayList<String>();
		nodeSize=new ArrayList<Float>();
		nodeR=new ArrayList<Float>();
		nodeG=new ArrayList<Float>();
		nodeB=new ArrayList<Float>();
		nodeAlpha=new ArrayList<Float>();
	}

	public void addNode(String name, float size, float r, float g, float b, float alpha) {
		nodeName.add(name);
		nodeSize.add(size);
		nodeR.add(r);
		nodeG.add(g);
		nodeB.add(b);
		nodeAlpha.add(alpha);
	}

	public int size() {
		return nodeName.size();
	}

	public String getNodeName(int i)
	{
		if(i>=0 && i<size())
			return nodeName.get(i);
		else
			return null;
	}
	
	public float getNodeSize(int i)
	{
		if(i>=0 && i<size())
			return nodeSize.get(i);
		else
			return 0f;
	}
	
	public float getNodeR(int i)
	{
		if(i>=0 && i<size())
			return nodeR.get(i);
		else
			return 0f;
	}
	
	public float getNodeG(int i)
	{
		if(i>=0 && i<size())
			return nodeG.get(i);
		else
			return 0f;
	}
	
	public float getNodeB(int i)
	{
		if(i>=0 && i<size())
			return nodeB.get(i);
		else
			return 0f;
	}
	
	public float getNodeAlpha(int i)
	{
		if(i>=0 && i<size())
			return nodeAlpha.get(i);
		else
			return 0f;
	}
	
	public void print() {
		System.out.println("index\tnodeName\tnodeSize\tnodeR\tnodeG\tnodeB\tnodeAlpha");
		for(int i=0; i<size(); i++)
			System.out.println(i+"\t"+nodeName.get(i)+"\t"+nodeSize.get(i)+"\t"+nodeR.get(i)+"\t"+nodeG.get(i)+"\t"+nodeB.get(i)+"\t"+nodeAlpha);
	}
}