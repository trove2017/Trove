package graph;

public class graphNodeData {

	private String node;
	private float nodeR;
	private float nodeG;
	private float nodeB;
	private float nodeAlpha;
	private float nodeSize;
	private String nodeLabel;
	
	public graphNodeData(String n, float r, float g, float b, float alpha, float size, String label) {
		node=n;
		nodeR=r;
		nodeG=g;
		nodeB=b;
		nodeAlpha=alpha;
		nodeSize=size;
		nodeLabel=label;
	}

	public String getNode() {
		return node;
	}

	public float getR() {
		return nodeR;
	}	

	public float getG() {
		return nodeG;
	}

	public float getB() {
		return nodeB;
	}	

	public float getAlpha() {
		return nodeAlpha;
	}
	
	public float getSize() {
		return nodeSize;
	}	
	
	public String getLabel() {
		return nodeLabel;
	}
	
	public void print() {
		System.out.println("["+node+"]"+nodeR+","+nodeG+","+nodeB+","+nodeAlpha+" ("+nodeSize+") "+nodeLabel);
	}
}