package graph;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JLabel;

import org.jgrapht.*;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.alg.shortestpath.AllDirectedPaths;
import org.jgrapht.alg.shortestpath.BellmanFordShortestPath;
import org.jgrapht.GraphPath;

import database.postgreSQL;

public class JGRAPHT_graph 
{
	private DirectedGraph<String,edge> bipartiteGraph;
	private postgreSQL postgreDB;

	public JGRAPHT_graph(postgreSQL DB) 
	{
		bipartiteGraph = new DefaultDirectedGraph<String,edge>(edge.class);
		postgreDB=DB;
	}

	public void resetGraph() 
	{
		bipartiteGraph = new DefaultDirectedGraph<String,edge>(edge.class);
	}

	public void addANode(String n)
	{
		bipartiteGraph.addVertex(n);
	}

	public void addAEdge(String n1, String n2)
	{
		edge e=new edge(n1, n1, n2, n2, "DIRECTED");
		bipartiteGraph.addEdge(n1, n2, e);
	}

	public ArrayList<String> BellmanFordShortestPath(JLabel statusLabel, ArrayList<String> targetNodeList, ArrayList<String> diseaseNodeList)
	{
		ArrayList<String> nodeList=new ArrayList<String>();
		BellmanFordShortestPath<String,edge> sp=new BellmanFordShortestPath<String,edge>(bipartiteGraph);
		for(int i=0; i<targetNodeList.size(); i++)
		{
			for(int j=0; j<diseaseNodeList.size(); j++)
			{
				String target=postgreDB.getNodeName(postgreDB.getNodeDBID(targetNodeList.get(i)));
				String disease=diseaseNodeList.get(j);
				//System.out.println(target+"->"+disease);
				if(target.compareTo(disease)!=0)
				{
					GraphPath<String,edge> pathNode=sp.findPathBetween(bipartiteGraph, target, disease);
					//if(pathNode==null)
					//	System.out.println("pathNode: null");
					if(pathNode!=null)
					{
						//System.out.println("pathNode: "+pathNode.toString());
						List<edge> edgeList=pathNode.getEdgeList();
						for(int k=0; k<edgeList.size(); k++)
						{
							edge e=edgeList.get(k);
							String s=e.getSourceName();
							String t=e.getTargetName();
							if(s.compareTo(target)!=0 && s.compareTo(disease)!=0 && nodeList.contains(s)==false)
								nodeList.add(s);
							if(t.compareTo(target)!=0 && t.compareTo(disease)!=0 && nodeList.contains(t)==false)
								nodeList.add(t);
							//System.out.println("edge "+k+": "+e.getSourceName()+"->"+e.getTargetName());
						}
					}
				}
			}
		}
		//System.out.println("nodeList:"+nodeList.toString());
		return nodeList;
	}
	
	public ArrayList<String> returnAllPaths(String source, String destination, int pathLength)
	{
		AllDirectedPaths<String,edge> allPath=new AllDirectedPaths<String,edge>(bipartiteGraph);
		ArrayList<String> uniqueNodesInAllPaths=new ArrayList<String>();
		List<GraphPath<String,edge>> pathList=allPath.getAllPaths(source, destination, true, pathLength);
		for(int i=0; i<pathList.size(); i++)
		{
			System.out.print(i+" :");
			List<String> vList=pathList.get(i).getVertexList();
			for(int j=0; j<vList.size(); j++)
			{
				String n=vList.get(j);
				System.out.print(n+",");
				if(uniqueNodesInAllPaths.contains(n)==false)
					uniqueNodesInAllPaths.add(n);
			}
			System.out.println("");
		}
		return uniqueNodesInAllPaths;	
	}
}
