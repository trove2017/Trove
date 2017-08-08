package graph;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.swing.JLabel;


import constants.stringConstant;
import dataType.nodePath;
import dataType.nodePathCommon;
import database.postgreSQL;
import edu.uci.ics.jung.algorithms.importance.BetweennessCentrality;
import edu.uci.ics.jung.algorithms.scoring.EigenvectorCentrality;
import edu.uci.ics.jung.algorithms.shortestpath.UnweightedShortestPath;
import edu.uci.ics.jung.graph.Hypergraph;
import edu.uci.ics.jung.graph.SparseGraph;
import edu.uci.ics.jung.graph.util.EdgeType;

public class JUNG_graph 
{
	private SparseGraph<String,edge> bipartiteGraph;
	private SparseGraph<String,edge> bDagGraph;
	private SparseGraph<String,edge> reverseBDagGraph;
	private ArrayList<String> dagList;
	private ArrayList<BigDecimal> dagTDEval;
	private graph bG, dagG; 
	private postgreSQL DB;
	private int SCALE = 20;
	private int IN_NEIGHBOUR=0, OUT_NEIGHBOUR=1, CONSTANT=0;
	private stringConstant strConstant;
	private String DB_IN_DEGREE, DB_OUT_DEGREE, DB_TOTAL_DEGREE, DB_EIGENVECTOR, DB_BETWEENNESS, DB_BRIDGING_COEFF;
	private String DB_BRIDGING_CENTRALITY, DB_UNDIR_CLUSTERING, DB_IN_CLUSTERING, DB_OUT_CLUSTERING, DB_CYC_CLUSTERING;
	private String DB_MID_CLUSTERING, DB_TDE, DB_PROB_COEFF;
	private String SCC_PREFIX = "SCC_";
	private String PREORDER = "Preorder";
	private String POSTORDER = "Postorder";
	private ArrayList<ArrayList<String>> connectionPaths;
	private ArrayList<String> descendantNodes_diseaseNodes;
	private ArrayList<String> descendantNodes_targetNodes;
	private ArrayList<String> descendantNodes_complementaryNodes; 
	private ArrayList<nodePath> nodePathList=new ArrayList<nodePath>();
	private ArrayList<nodePathCommon> nodePathCommonList=new ArrayList<nodePathCommon>();
	
	public JUNG_graph(postgreSQL database) 
	{
		strConstant=new stringConstant();
		bG=new graph();
		dagG=new graph();
		bipartiteGraph = new SparseGraph<String,edge>();
		bDagGraph = new SparseGraph<String,edge>();
		DB=database;
		initializeConstants();
		connectionPaths=new ArrayList<ArrayList<String>>();
	}

	private void initializeConstants()
	{
		DB_IN_DEGREE=strConstant.getDBInDegree();
		DB_OUT_DEGREE=strConstant.getDBOutDegree();
		DB_TOTAL_DEGREE=strConstant.getDBTotalDegree();
		DB_EIGENVECTOR=strConstant.getDBEigenvector();
		DB_BETWEENNESS=strConstant.getDBBetweenness();
		DB_BRIDGING_COEFF=strConstant.getDBBridgingCoeff();
		DB_BRIDGING_CENTRALITY=strConstant.getDBBridgingCentrality();
		DB_UNDIR_CLUSTERING=strConstant.getDBUndirClustering();
		DB_IN_CLUSTERING=strConstant.getDBInClustering();
		DB_OUT_CLUSTERING=strConstant.getDBOutClustering();
		DB_CYC_CLUSTERING=strConstant.getDBCycClustering();
		DB_MID_CLUSTERING=strConstant.getDBMidClustering();
		DB_TDE=strConstant.getDBTDE();
		DB_PROB_COEFF=strConstant.getDBProbCoeff();
	}
	
	public void resetGraph() 
	{
		bipartiteGraph = new SparseGraph<String, edge>();
		bG=new graph();
	}
	
	public void addANode(String name)
	{
		bipartiteGraph.addVertex(name);
		bG.getNode().newRecord();
		bG.getNode().setNodeId(bG.getNode().getSize()-1, name);
		bG.getNode().setIsVirtualNode(bG.getNode().getSize() - 1, false);
	}
	
	public void addADirectedEdge(String source, String target)
	{
		//System.out.println("JUNG add a directed edge "+source+"->"+target);
		edge e=new edge(source, source, target, target, "DIRECTED");
		bipartiteGraph.addEdge(e, source, target, EdgeType.DIRECTED);
	}
	
	public void addAnUndirectedEdge(String source, String target)
	{
		//System.out.println("JUNG add an undirected edge "+source+"-"+target);
		edge e=new edge(source, source, target, target, "UNDIRECTED");
		bipartiteGraph.addEdge(e, source, target, EdgeType.UNDIRECTED);
	}

	public void getDegree(JLabel status, boolean WHOLE_NETWORK) 
	{
		//calculate all node degree coz need it for TDE
		System.out.println("computing degree centrality");
		int inDegree=-1, outDegree=-1, totalDegree;
		Object[] vertices=bipartiteGraph.getVertices().toArray();
		int totalCount=bipartiteGraph.getVertexCount();
		float percentComplete;
		for (int i=0; i<totalCount; i++) 
		{
			String node=vertices[i].toString();
			percentComplete=(float)(100.0*i/(totalCount-1));
			DecimalFormat df = new DecimalFormat();
			df.setMaximumFractionDigits(2);
			status.setText("computing features...degree centrality ["+df.format(percentComplete)+"%] computing value");
			inDegree=bipartiteGraph.inDegree(node);
			outDegree=bipartiteGraph.outDegree(node);
			totalDegree=inDegree+outDegree;
			status.setText("computing features...degree centrality ["+df.format(percentComplete)+"%] updating value to database");
			int nodeDBID=DB.getNodeDBID(node);
			if(WHOLE_NETWORK)
			{
				DB.update_feature(DB.getNodeID(nodeDBID), DB.getNodeName(nodeDBID), DB_IN_DEGREE, String.valueOf(inDegree));
				DB.update_feature(DB.getNodeID(nodeDBID), DB.getNodeName(nodeDBID), DB_OUT_DEGREE, String.valueOf(outDegree));
				DB.update_feature(DB.getNodeID(nodeDBID), DB.getNodeName(nodeDBID), DB_TOTAL_DEGREE, String.valueOf(totalDegree));
			}
			else
			{
				DB.update_pathwayFeature(DB.getNodeID(nodeDBID), DB.getNodeName(nodeDBID), DB_IN_DEGREE, String.valueOf(inDegree));
				DB.update_pathwayFeature(DB.getNodeID(nodeDBID), DB.getNodeName(nodeDBID), DB_OUT_DEGREE, String.valueOf(outDegree));
				DB.update_pathwayFeature(DB.getNodeID(nodeDBID), DB.getNodeName(nodeDBID), DB_TOTAL_DEGREE, String.valueOf(totalDegree));
			}
		}
		DB.setComputedFeature(DB_IN_DEGREE, true);
		DB.setComputedFeature(DB_OUT_DEGREE, true);
		DB.setComputedFeature(DB_TOTAL_DEGREE, true);
		System.out.println("computed degree centrality");
	}
	
	public void getEigenvector(JLabel status, boolean WHOLE_NETWORK) 
	{
		System.out.println("computing eigenvector centrality");
		EigenvectorCentrality<String,edge> eigenvector = new EigenvectorCentrality<String,edge>((Hypergraph<String,edge>)bipartiteGraph);
		eigenvector.acceptDisconnectedGraph(true);
		status.setText("computing features...eigenvector centrality - evaluating eigenvector. This may take couple of minutes. Please wait.");
		eigenvector.evaluate();
		Object[] vertices=bipartiteGraph.getVertices().toArray();
		float percentComplete;
		int totalCount=bipartiteGraph.getVertexCount();
		for (int i=0; i<totalCount; i++) 
		{
			String node=vertices[i].toString();
			Double eigenvectorCentrality;
			percentComplete=(float)(100.0*i/(totalCount-1));
			DecimalFormat df = new DecimalFormat();
			df.setMaximumFractionDigits(2);
			status.setText("computing features...eigenvector centrality ["+df.format(percentComplete)+"%] getting value");
			eigenvectorCentrality=eigenvector.getVertexScore(node);
			status.setText("computing features...eigenvector centrality ["+df.format(percentComplete)+"%] updating value to database");
			int nodeDBID=DB.getNodeDBID(node);
			if(WHOLE_NETWORK)
				DB.update_feature(DB.getNodeID(nodeDBID), DB.getNodeName(nodeDBID), DB_EIGENVECTOR, eigenvectorCentrality.toString());
			else
				DB.update_pathwayFeature(DB.getNodeID(nodeDBID), DB.getNodeName(nodeDBID), DB_EIGENVECTOR, eigenvectorCentrality.toString());
		}
		System.out.println("computed eigenvector centrality");
	}
	
	public void getBetweenness(JLabel status, boolean WHOLE_NETWORK) 
	{
		System.out.println("computing betweenness");
		BetweennessCentrality<String,edge> betweennessRanker = new BetweennessCentrality<String,edge>(bipartiteGraph);
		betweennessRanker.setRemoveRankScoresOnFinalize(false);
		status.setText("computing features...betweenness centrality - evaluating betweenness. This may take couple of minutes. Please wait.");
		betweennessRanker.evaluate();
		BigDecimal rankScore;
		Object[] vertices=bipartiteGraph.getVertices().toArray();
		float percentComplete;
		int totalCount=bipartiteGraph.getVertexCount();
		for(int i=0; i<totalCount; i++) 
		{
			String node=vertices[i].toString();
			percentComplete=(float)(100.0*i/(totalCount-1));
			DecimalFormat df = new DecimalFormat();
			df.setMaximumFractionDigits(2);
			status.setText("computing features...betweenness centrality ["+df.format(percentComplete)+"%] getting value");
			rankScore= BigDecimal.valueOf(betweennessRanker.getVertexRankScore(node));
			status.setText("computing features...betweenness centrality ["+df.format(percentComplete)+"%] updating value to database");
			int nodeDBID=DB.getNodeDBID(node);
			if(WHOLE_NETWORK)
				DB.update_feature(DB.getNodeID(nodeDBID), DB.getNodeName(nodeDBID), DB_BETWEENNESS, rankScore.toString());
			else
				DB.update_pathwayFeature(DB.getNodeID(nodeDBID), DB.getNodeName(nodeDBID), DB_BETWEENNESS, rankScore.toString());
		}
		System.out.println("computed betweenness");
		DB.setComputedFeature(DB_BETWEENNESS, true);
	}
	
	public void getBridgingCoeff(JLabel status, boolean WHOLE_NETWORK) 
	{
		System.out.println("computing bridging coefficient");
		Object[] neighbours;
		BigDecimal coeff, degree, neighbourOutEdgeCount, neighbourDegree;
		Object[] vertices=bipartiteGraph.getVertices().toArray();
		float percentComplete;
		int totalCount=bipartiteGraph.getVertexCount();
		for(int i=0; i<totalCount; i++) 
		{
			String node=vertices[i].toString();
			percentComplete=(float)(100.0*i/(totalCount-1));
			DecimalFormat df = new DecimalFormat();
			df.setMaximumFractionDigits(2);
			status.setText("computing features...bridging coefficient ["+df.format(percentComplete)+"%] computing value");
			coeff = BigDecimal.ZERO;
			neighbours = bipartiteGraph.getNeighbors(node).toArray();
			degree= BigDecimal.valueOf(bipartiteGraph.degree(node));
			for(int j=0; j<neighbours.length; j++) 
			{
				neighbourOutEdgeCount = BigDecimal.valueOf(bipartiteGraph.getSuccessorCount(neighbours[j].toString()));
				neighbourDegree = BigDecimal.valueOf(bipartiteGraph.degree(neighbours[j].toString()) - 1);
				if (neighbourDegree.compareTo(BigDecimal.ZERO) > 0)
					coeff = coeff.add(neighbourOutEdgeCount.divide(neighbourDegree, SCALE, BigDecimal.ROUND_UP));
			}
			if (degree.compareTo(BigDecimal.ZERO)>0)
				coeff = coeff.divide(degree, SCALE, BigDecimal.ROUND_UP);
			status.setText("computing features...bridging coefficient ["+df.format(percentComplete)+"%] updating value to database");
			int nodeDBID=DB.getNodeDBID(node);
			if(WHOLE_NETWORK)
				DB.update_feature(DB.getNodeID(nodeDBID), DB.getNodeName(nodeDBID), DB_BRIDGING_COEFF, coeff.toString());
			else
				DB.update_pathwayFeature(DB.getNodeID(nodeDBID), DB.getNodeName(nodeDBID), DB_BRIDGING_COEFF, coeff.toString());
		}
		System.out.println("computed bridging coefficient");
		DB.setComputedFeature(DB_BRIDGING_COEFF, true);
	}
	
	public void getBridgingCentrality(JLabel status, boolean WHOLE_NETWORK) 
	{
		System.out.println("computing bridging centrality");
		BigDecimal betweennessRank, bridgingCoeffRank;
		if(DB.getComputedFeature(DB_BRIDGING_COEFF)==false)
			getBridgingCoeff(status, WHOLE_NETWORK);
		if(DB.getComputedFeature(DB_BETWEENNESS)==false)
			getBetweenness(status, WHOLE_NETWORK);
		Object[] vertices=bipartiteGraph.getVertices().toArray();
		float percentComplete;
		int totalCount=bipartiteGraph.getVertexCount();
		for (int i=0; i<totalCount; i++) 
		{
			String node=vertices[i].toString();
			percentComplete=(float)(100.0*i/(totalCount-1));
			DecimalFormat df = new DecimalFormat();
			df.setMaximumFractionDigits(2);
			status.setText("computing features...bridging centrality ["+df.format(percentComplete)+"%] computing value");
			int nodeDBID=DB.getNodeDBID(node);
			if(WHOLE_NETWORK)
			{
				betweennessRank=BigDecimal.valueOf(Double.parseDouble(DB.get_featureAscRank(DB.getNodeID(nodeDBID), DB.getNodeName(nodeDBID), DB_BETWEENNESS)));
				bridgingCoeffRank=BigDecimal.valueOf(Double.parseDouble(DB.get_featureAscRank(DB.getNodeID(nodeDBID), DB.getNodeName(nodeDBID), DB_BRIDGING_COEFF)));
			}
			else
			{
				betweennessRank=BigDecimal.valueOf(Double.parseDouble(DB.get_pathwayFeatureAscRank(DB.getNodeID(nodeDBID), DB.getNodeName(nodeDBID), DB_BETWEENNESS)));
				bridgingCoeffRank=BigDecimal.valueOf(Double.parseDouble(DB.get_pathwayFeatureAscRank(DB.getNodeID(nodeDBID), DB.getNodeName(nodeDBID), DB_BRIDGING_COEFF)));
			}
			status.setText("computing features...bridging centrality ["+df.format(percentComplete)+"%] updating value to database");
			if(WHOLE_NETWORK)
				DB.update_feature(DB.getNodeID(nodeDBID), DB.getNodeName(nodeDBID), DB_BRIDGING_CENTRALITY, (betweennessRank.multiply(bridgingCoeffRank)).toString());
			else
				DB.update_pathwayFeature(DB.getNodeID(nodeDBID), DB.getNodeName(nodeDBID), DB_BRIDGING_CENTRALITY, (betweennessRank.multiply(bridgingCoeffRank)).toString());
		}
		System.out.println("computed bridging centrality");
	}
	
	// undirected clustering coefficient = N/M where N=# edges between neighbouring nodes,
	//                                               M=max # edges between neighbouring nodes
	// http://med.bioinf.mpi-inf.mpg.de/netanalyzer/help/2.6.1/#clustCoef
	public void getUndirectedClusteringCoefficient(JLabel status, boolean WHOLE_NETWORK) 
	{
		System.out.println("computing undirected clustering coefficient");
		Collection<String> nodeNeighbourCollection;
		ArrayList<String> nodeNeighbourList;
		int N=0;
		double M=0;
		BigDecimal clusteringCoeff=BigDecimal.ZERO;
		Object[] vertices=bipartiteGraph.getVertices().toArray();
		float percentComplete;
		int totalCount=bipartiteGraph.getVertexCount();
		//get each real nodes' clustering coefficient
		for(int i=0; i<totalCount; i++)
		{
			String node=vertices[i].toString();
			percentComplete=(float)(100.0*i/(totalCount-1));
			DecimalFormat df = new DecimalFormat();
			df.setMaximumFractionDigits(2);
			status.setText("computing features...undirected clustering coefficient ["+df.format(percentComplete)+"%] computing value");
			nodeNeighbourCollection=bipartiteGraph.getNeighbors(node);
			nodeNeighbourList = new ArrayList<String>(nodeNeighbourCollection);
			N=0;
			M=0;
			clusteringCoeff=BigDecimal.ZERO;
			//check if node's neighbours are neighbours of each other (i.e., number of undirected edges between node's neighbours)
			for(int j=0; j<nodeNeighbourList.size()-1; j++)
			{
				for(int k=j+1; k<nodeNeighbourList.size(); k++)
				{
					if(bipartiteGraph.isNeighbor(nodeNeighbourList.get(j), nodeNeighbourList.get(k))==true)
						N++;
				}
			}
			M=(double)nodeNeighbourList.size()*((double)nodeNeighbourList.size()-1)/2; //since a-b is the same as b-a
			if(nodeNeighbourList.size()<2)
				clusteringCoeff=BigDecimal.ZERO;
			else
				clusteringCoeff=(BigDecimal.valueOf(N)).divide(BigDecimal.valueOf(M), SCALE, BigDecimal.ROUND_UP);
			status.setText("computing features...undirected clustering coefficient ["+df.format(percentComplete)+"%] updating value to database");
			int nodeDBID=DB.getNodeDBID(node);
			if(WHOLE_NETWORK)
				DB.update_feature(DB.getNodeID(nodeDBID), DB.getNodeName(nodeDBID), DB_UNDIR_CLUSTERING, clusteringCoeff.toString());
			else
				DB.update_pathwayFeature(DB.getNodeID(nodeDBID), DB.getNodeName(nodeDBID), DB_UNDIR_CLUSTERING, clusteringCoeff.toString());
		}
		//compute network clustering coefficient which is the average of all clustering coefficient
		//http://med.bioinf.mpi-inf.mpg.de/netanalyzer/help/2.6.1/#cksDist
		System.out.println("computed undirected clustering coefficient");
	}
	
	// directed clustering coefficient = N/M where N=# edges between neighbouring nodes, considering only in-triangle pattern 
	//                                             M=max # edges between neighbouring nodes
	// in triangle pattern
	//		  u                 u
	//       + +               + +
	//      /   \             /   \
	//     a --+ b	         a +-- b
	public void getInClusteringCoefficient(JLabel status, boolean WHOLE_NETWORK) 
	{
		System.out.println("computing in clustering coefficient");
		BigDecimal clusteringCoeff;
		ArrayList<String> nodeInNeighbourList, neighbourInNeighbour, neighbourOutNeighbour;
		int N=0;
		double M=0;
		Object[] vertices=bipartiteGraph.getVertices().toArray();
		float percentComplete;
		int totalCount=bipartiteGraph.getVertexCount();
		//get each real nodes' clustering coefficient
		for(int i=0; i<totalCount; i++)
		{
			String node=vertices[i].toString();
			percentComplete=(float)(100.0*i/(totalCount-1));
			DecimalFormat df = new DecimalFormat();
			df.setMaximumFractionDigits(2);
			status.setText("computing features...in clustering coefficient ["+df.format(percentComplete)+"%] computing value");
			nodeInNeighbourList = new ArrayList<String>();
			N=0;
			M=0;
			clusteringCoeff=BigDecimal.ZERO;
			nodeInNeighbourList=getNeighbours(node, bipartiteGraph.getIncidentEdges(node), IN_NEIGHBOUR);
			if(nodeInNeighbourList.size()<2)
				clusteringCoeff=BigDecimal.ZERO;
			else
			{
				//check number of directed edges between node's neighbours (e.g., for neighbour a and b, a->b is distinct from b->a)
				for(int j=0; j<nodeInNeighbourList.size()-1; j++)
				{
					neighbourInNeighbour=new ArrayList<String>();
					neighbourOutNeighbour=new ArrayList<String>();
					neighbourInNeighbour=getNeighbours(nodeInNeighbourList.get(j), bipartiteGraph.getIncidentEdges(nodeInNeighbourList.get(j)), IN_NEIGHBOUR);
					neighbourOutNeighbour=getNeighbours(nodeInNeighbourList.get(j), bipartiteGraph.getIncidentEdges(nodeInNeighbourList.get(j)), OUT_NEIGHBOUR);
					for(int k=j+1; k<nodeInNeighbourList.size(); k++)
					{
						if(neighbourInNeighbour.contains(nodeInNeighbourList.get(k))==true)
							N++;
						if(neighbourOutNeighbour.contains(nodeInNeighbourList.get(k))==true)
							N++;
					}
				}
				M=(double)nodeInNeighbourList.size()*((double)nodeInNeighbourList.size()-1); //since a->b is different from a<-b
				clusteringCoeff=(BigDecimal.valueOf(N)).divide(BigDecimal.valueOf(M), SCALE, BigDecimal.ROUND_UP);
			}
			status.setText("computing features...in clustering coefficient ["+df.format(percentComplete)+"%] updating value to database");
			int nodeDBID=DB.getNodeDBID(node);
			if(WHOLE_NETWORK)
				DB.update_feature(DB.getNodeID(nodeDBID), DB.getNodeName(nodeDBID), DB_IN_CLUSTERING, clusteringCoeff.toString());
			else
				DB.update_pathwayFeature(DB.getNodeID(nodeDBID), DB.getNodeName(nodeDBID), DB_IN_CLUSTERING, clusteringCoeff.toString());
		}
		System.out.println("computed in clustering coefficient");
	}
	
	// directed clustering coefficient = N/M where N=# edges between neighbouring nodes, considering only out-triangle pattern 
	//                                             M=max # edges between neighbouring nodes
	// out triangle pattern
	//		  u                 u
	//       / \               / \
	//      +   +             +   +
	//     a --+ b	         a +-- b
	// for some strange reasons, JUNG hypergraph getOutEdges function does not seems to return the out edges of a node u but instead
	// returns all edges involving u.
	// will write a parser function to get the out edges and in edges instead. 
	// hyperedge in JUNG represented by ((a,b,c,...),(...,x,y,z)) where (a,b,c,...) are the in-nodes connecting to the out-nodes (...,x,y,z) of the hyperedge
	public void getOutClusteringCoefficient(JLabel status, boolean WHOLE_NETWORK) 
	{
		System.out.println("computing out clustering coefficient");
		BigDecimal clusteringCoeff;
		ArrayList<String> nodeOutNeighbourList, neighbourInNeighbour, neighbourOutNeighbour;
		int N=0;
		double M=0;
		Object[] vertices=bipartiteGraph.getVertices().toArray();
		float percentComplete;
		int totalCount=bipartiteGraph.getVertexCount();
		//get each real nodes' clustering coefficient
		for(int i=0; i<totalCount; i++)
		{
			String node=vertices[i].toString();
			percentComplete=(float)(100.0*i/(totalCount-1));
			DecimalFormat df = new DecimalFormat();
			df.setMaximumFractionDigits(2);
			status.setText("computing features...out clustering coefficient ["+df.format(percentComplete)+"%] computing value");
			nodeOutNeighbourList = new ArrayList<String>();
			N=0;
			M=0;
			clusteringCoeff=BigDecimal.ZERO;
			nodeOutNeighbourList=getNeighbours(node, bipartiteGraph.getIncidentEdges(node), OUT_NEIGHBOUR);
			if(nodeOutNeighbourList.size()<2)
				clusteringCoeff=BigDecimal.ZERO;
			else
			{
				//check number of directed edges between node's neighbours (e.g., for neighbour a and b, a->b is distinct from b->a)
				for(int j=0; j<nodeOutNeighbourList.size()-1; j++)
				{
					neighbourInNeighbour=new ArrayList<String>();
					neighbourOutNeighbour=new ArrayList<String>();
					neighbourInNeighbour=getNeighbours(nodeOutNeighbourList.get(j), bipartiteGraph.getIncidentEdges(nodeOutNeighbourList.get(j)), IN_NEIGHBOUR);
					neighbourOutNeighbour=getNeighbours(nodeOutNeighbourList.get(j), bipartiteGraph.getIncidentEdges(nodeOutNeighbourList.get(j)), OUT_NEIGHBOUR);
					for(int k=j+1; k<nodeOutNeighbourList.size(); k++)
					{
						if(neighbourInNeighbour.contains(nodeOutNeighbourList.get(k))==true)
							N++;
						if(neighbourOutNeighbour.contains(nodeOutNeighbourList.get(k))==true)
							N++;
					}
				}
				M=(double)nodeOutNeighbourList.size()*((double)nodeOutNeighbourList.size()-1); //since a->b is different from a<-b
				clusteringCoeff=(BigDecimal.valueOf(N)).divide(BigDecimal.valueOf(M), SCALE, BigDecimal.ROUND_UP);
			}
			status.setText("computing features...out clustering coefficient ["+df.format(percentComplete)+"%] updating value to database");
			int nodeDBID=DB.getNodeDBID(node);
			if(WHOLE_NETWORK)
				DB.update_feature(DB.getNodeID(nodeDBID), DB.getNodeName(nodeDBID), DB_OUT_CLUSTERING, clusteringCoeff.toString());
			else
				DB.update_pathwayFeature(DB.getNodeID(nodeDBID), DB.getNodeName(nodeDBID), DB_OUT_CLUSTERING, clusteringCoeff.toString());
		}
		System.out.println("computed out clustering coefficient");
	}
		
	// directed clustering coefficient = N/M where N=# edges between neighbouring nodes, considering cycle- and middleman-triangle pattern 
	//                                             M=max # edges between neighbouring nodes
	// cycle triangle pattern
	//		  u                           u
	//       + \    a: in-u              / +       a: out-u
	//      /   +   b: out-u            +   \      b: in-u
	//     a +-- b	b: in-a            a --+ b     b: out-a
	// middleman triangle pattern
	//		  u                           u
	//       + \    a: in-u              / +       a: out-u
	//      /   +   b: out-u            +   \      b: in-u
	//     a --+ b	b: out-a           a +-- b     b: in-a
	public void getCycleAndMiddlemanClusteringCoefficient(JLabel status, boolean WHOLE_NETWORK) 
	{
		System.out.println("computing cyc and mid clustering coefficient");
		ArrayList<String> nodeInNeighbourList, nodeOutNeighbourList, neighbourInNeighbour, neighbourOutNeighbour;
		int N_cycle=0, N_middleman=0;
		double M=0, common=0;
		BigDecimal cycleClusteringCoeff, middlemanClusteringCoeff;
		Object[] vertices=bipartiteGraph.getVertices().toArray();
		float percentComplete;
		int totalCount=bipartiteGraph.getVertexCount();
		//get each real nodes' clustering coefficient
		for(int i=0; i<totalCount; i++)
		{
			String node=vertices[i].toString();
			percentComplete=(float)(100.0*i/(totalCount-1));
			DecimalFormat df = new DecimalFormat();
			df.setMaximumFractionDigits(2);
			status.setText("computing features...cycle and middleman clustering coefficient ["+df.format(percentComplete)+"%] computing value");
			nodeInNeighbourList = new ArrayList<String>();
			nodeOutNeighbourList = new ArrayList<String>();
			N_cycle=0;
			N_middleman=0;
			M=0;
			common=0;
			cycleClusteringCoeff=BigDecimal.ZERO;
			middlemanClusteringCoeff=BigDecimal.ZERO;
			nodeInNeighbourList=getNeighbours(node, bipartiteGraph.getIncidentEdges(node), IN_NEIGHBOUR);
			nodeOutNeighbourList=getNeighbours(node, bipartiteGraph.getIncidentEdges(node), OUT_NEIGHBOUR);
			//check for common 
			//--check for edges: currNode<->nodeNeighbour
			for(int j=0; j<nodeOutNeighbourList.size(); j++) 
			{
				if(nodeInNeighbourList.contains(nodeOutNeighbourList.get(j))==true)
					common++;
			}
			M=(double)nodeInNeighbourList.size()*(double)nodeOutNeighbourList.size()-common; //since a->b is different from a<-b
			if(M<=0)
			{
				cycleClusteringCoeff=BigDecimal.ZERO;
				middlemanClusteringCoeff=BigDecimal.ZERO;
			}
			else
			{
				//check for cycle pattern and middleman pattern
				for(int j=0; j<nodeInNeighbourList.size(); j++) 
				{
					neighbourInNeighbour=new ArrayList<String>();
					neighbourOutNeighbour=new ArrayList<String>();
					neighbourInNeighbour=getNeighbours(nodeInNeighbourList.get(j), bipartiteGraph.getIncidentEdges(nodeInNeighbourList.get(j)), IN_NEIGHBOUR);
					neighbourOutNeighbour=getNeighbours(nodeInNeighbourList.get(j), bipartiteGraph.getIncidentEdges(nodeInNeighbourList.get(j)), OUT_NEIGHBOUR);
					for(int k=0; k<nodeOutNeighbourList.size(); k++)
					{
						if(neighbourInNeighbour.contains(nodeOutNeighbourList.get(k))==true)
							N_cycle++;
						if(neighbourOutNeighbour.contains(nodeOutNeighbourList.get(k))==true)
							N_middleman++;
					}
				}
				cycleClusteringCoeff=(BigDecimal.valueOf(N_cycle)).divide(BigDecimal.valueOf(M), SCALE, BigDecimal.ROUND_UP);
				middlemanClusteringCoeff=(BigDecimal.valueOf(N_middleman)).divide(BigDecimal.valueOf(M), SCALE, BigDecimal.ROUND_UP);
			}
			status.setText("computing features...cycle and middleman clustering coefficient ["+df.format(percentComplete)+"%] updating value to database");
			int nodeDBID=DB.getNodeDBID(node);
			if(WHOLE_NETWORK)
			{
				DB.update_feature(DB.getNodeID(nodeDBID), DB.getNodeName(nodeDBID), DB_CYC_CLUSTERING, cycleClusteringCoeff.toString());
				DB.update_feature(DB.getNodeID(nodeDBID), DB.getNodeName(nodeDBID), DB_MID_CLUSTERING, middlemanClusteringCoeff.toString());
			}
			else
			{
				DB.update_pathwayFeature(DB.getNodeID(nodeDBID), DB.getNodeName(nodeDBID), DB_CYC_CLUSTERING, cycleClusteringCoeff.toString());
				DB.update_pathwayFeature(DB.getNodeID(nodeDBID), DB.getNodeName(nodeDBID), DB_MID_CLUSTERING, middlemanClusteringCoeff.toString());
			}
			DB.setComputedFeature(DB_CYC_CLUSTERING, true);
			DB.setComputedFeature(DB_MID_CLUSTERING, true);
		}
		System.out.println("computed cyc and mid clustering coefficient");
	}
		
	//get a list of nodes that are the in-neighbours (or out-neighbours) of thisNode (node u)
	//a node v is an in-neighbour of u if for an e=(x,y), v=x and u=y
	//a node v is an out-neighbour of u if for an e=(x,y), u=x and v=y 
	private ArrayList<String> getNeighbours(String thisNode, Collection<edge> edgeCollection, int neighbourType)
	{
		ArrayList<String> neighbourList=new ArrayList<String>();
		ArrayList<edge> edgeList = new ArrayList<edge>(edgeCollection);
		//no edge involving node u
		if(edgeList==null || edgeList.size()==0)
			return neighbourList;
		for(int i=0; i<edgeList.size(); i++)
		{
			String sourceID, sourceName, targetID, targetName, type;
			sourceID=edgeList.get(i).getSourceID();
			sourceName=edgeList.get(i).getSourceName();
			targetID=edgeList.get(i).getTargetID();
			targetName=edgeList.get(i).getTargetName();
			type=edgeList.get(i).getType();
			
			if(neighbourType==IN_NEIGHBOUR)
			{
				//thisNode is the target
				if(targetID.compareTo(thisNode)==0 || targetName.compareTo(thisNode)==0)
				{
					String neighbour=sourceName;
					if(neighbour==null || neighbour.length()==0)
						neighbour=sourceID;
					if(neighbourList.contains(neighbour)==false)
						neighbourList.add(neighbour);
				}
				else
				{
					//special case: thisNode is the source and edge type is "Phy" (no direction)
					if(type.compareTo("Phy")==0 && (sourceID.compareTo(thisNode)==0 || sourceName.compareTo(thisNode)==0))
					{
						String neighbour=targetName;
						if(neighbour==null || neighbour.length()==0)
							neighbour=targetID;
						if(neighbourList.contains(neighbour)==false)
							neighbourList.add(neighbour);
					}
				}
			}
			else //OUT_NEIGHBOUR
			{
				//thisNode is the source
				if(sourceID.compareTo(thisNode)==0 || sourceName.compareTo(thisNode)==0)
				{
					String neighbour=targetName;
					if(neighbour==null || neighbour.length()==0)
						neighbour=targetID;
					if(neighbourList.contains(neighbour)==false)
						neighbourList.add(neighbour);
				}
				else
				{
					//special case: thisNode is the target and edge type is "Phy" (no direction)
					if(type.compareTo("Phy")==0 && (targetID.compareTo(thisNode)==0 || targetName.compareTo(thisNode)==0))
					{
						String neighbour=sourceName;
						if(neighbour==null || neighbour.length()==0)
							neighbour=sourceID;
						if(neighbourList.contains(neighbour)==false)
							neighbourList.add(neighbour);
					}
				}
			}
		}
		return neighbourList;
	}
	
	private void tarjan(int index, SparseGraph<String,edge>hyperG)
	{
		// let preorder represent index and postorder represent lowlink
		bG.getNode().getPreorder().setItem(index, bG.getIndexCounter().getCount());
		bG.getNode().getPostorder().setItem(index, bG.getIndexCounter().getCount());
		bG.getIndexCounter().increment();
		bG.getIndexStack().push(index);
		Collection<edge> edgeCollection=null;
		
		if(bG.getNode().getNodeId(index)!=null)
			edgeCollection=hyperG.getOutEdges(bG.getNode().getNodeId(index));
		if(edgeCollection==null && bG.getNode().getNodeName(index)!=null)
			edgeCollection=hyperG.getOutEdges(bG.getNode().getNodeName(index));
		if(edgeCollection==null)
			System.out.println("oh dear, still null");
		else
		{
			Object[] outEdges = edgeCollection.toArray();
			for(int i=0; i<outEdges.length; i++) 
			{
				edge outEdgeStr = (edge)outEdges[i];
				String outNode = outEdgeStr.getTargetID();
				int nodeIndex = bG.getNode().getNodeIdIndex(outNode);
				if(nodeIndex==(-1))
					nodeIndex=bG.getNode().getNodeNameIndex(outNode);
				if (bG.getNode().getPreorder().getItem(nodeIndex) == -1) 
				{
					tarjan(nodeIndex, hyperG);
					if (bG.getNode().getPostorder().getItem(index) < bG.getNode().getPostorder().getItem(nodeIndex))
						bG.getNode().getPostorder().setItem(index, bG.getNode().getPostorder().getItem(index));
					else
						bG.getNode().getPostorder().setItem(index, bG.getNode().getPostorder().getItem(nodeIndex));
				} 
				else if (bG.getIndexStack().isInStack(nodeIndex) == true) 
				{
					if (bG.getNode().getPreorder().getItem(nodeIndex) < bG.getNode().getPostorder().getItem(index))
						bG.getNode().getPostorder().setItem(index, bG.getNode().getPreorder().getItem(nodeIndex));
					else
					 	bG.getNode().getPostorder().setItem(index, bG.getNode().getPostorder().getItem(index));
				}
			}
			if (bG.getNode().getPostorder().getItem(index) == bG.getNode().getPreorder().getItem(index)) 
			{
				bG.getSCC().newRecord();
				int sccIndex = bG.getSCC().getSize()-1;
				int nodeIndex=0;
				bG.getSCC().setGroupName(sccIndex, SCC_PREFIX+String.valueOf(sccIndex));
				do {
					nodeIndex = bG.getIndexStack().pop();
					bG.getSCC().newItemRecord(sccIndex);
					bG.getSCC().setItemInGroup(sccIndex, bG.getSCC().getGroupSize(sccIndex)-1, bG.getNode().getNodeId(nodeIndex));
				} while (nodeIndex != index);
			}
		}
	}
	
	public void findSCC(JLabel status)
	{ 
		bG.getIndexCounter().reset();
		bG.getIndexStack().reset();
		float percentComplete;
		int totalCount=bG.getNode().getSize();
		for (int i=0; i<totalCount; i++)
		{
			percentComplete=(float)(100.0*i/(totalCount-1));
			DecimalFormat df = new DecimalFormat();
			df.setMaximumFractionDigits(2);
			status.setText("computing features...target downstream effect. Finding strongly connected components ["+df.format(percentComplete)+"%]");
			if (bG.getNode().getPreorder().getItem(i) == -1)
				tarjan(i, bipartiteGraph);
		}
	}
	
	private void rewireEdges(int sccIndex, String oldNodeId, String oldNodeName, String newNode, graph g)
	{ 
		Collection<edge> edgeCollection=bDagGraph.getIncidentEdges(oldNodeId);
		String oldNode;
		if(edgeCollection==null)
		{
			edgeCollection=bDagGraph.getIncidentEdges(oldNodeName);
			oldNode=oldNodeName;
		}
		else
			oldNode=oldNodeId;
		if(edgeCollection!=null)
		{
			Object[] edges = edgeCollection.toArray();
			System.out.println(oldNodeId+" "+oldNodeName);
			ArrayList<String> predecessor=new ArrayList<String>();
			ArrayList<String> successor=new ArrayList<String>();
			
			for (int i=0; i<edges.length; i++) 
			{
				edge e = (edge)edges[i];
				String source=e.getSourceName();
				String target=e.getTargetName();
				String type=e.getType();
				
				if(type.compareTo("PHY")!=0)
				{
					if(source.compareTo(oldNode)==0 && g.getSCC().getItemNameIndex(sccIndex, target) == -1)
						successor.add(target);
					else if(target.compareTo(oldNode)==0 && g.getSCC().getItemNameIndex(sccIndex, source) == -1)
						predecessor.add(source);
				}
				else
				{
					if(source.compareTo(oldNode)==0 && g.getSCC().getItemNameIndex(sccIndex, target) == -1)
					{
						successor.add(target);
						predecessor.add(target);
					}
					else if(target.compareTo(oldNode)==0 && g.getSCC().getItemNameIndex(sccIndex, source) == -1)
					{
						successor.add(source);
						predecessor.add(source);
					}
				}
			}
			for(int i=0; i<predecessor.size(); i++)
			{
				edge e = new edge(predecessor.get(i), predecessor.get(i), newNode, newNode, "Pos");
				bDagGraph.addEdge(e, predecessor.get(i), newNode);
				//System.out.println("add edge "+predecessor.get(i)+"->"+newNode);
			}
			for(int i=0; i<successor.size(); i++)
			{
				edge e = new edge(newNode, newNode, successor.get(i), successor.get(i), "Pos");
				bDagGraph.addEdge(e, newNode, successor.get(i));
				//System.out.println("add edge "+newNode+"->"+successor.get(i));
			}
			
			//System.out.println("predecessor:"+ predecessor.toString());
			//System.out.println("successor:"+ successor.toString());
		}
	}
	
	private void convertToDAG(JLabel status)
	{  
		// create a copy of the pseudoHyperG
		Object[] nodeSet = bipartiteGraph.getVertices().toArray();
		Object[] edgeSet = bipartiteGraph.getEdges().toArray();
		float percentComplete;
		int totalCount=bipartiteGraph.getVertexCount();
		for(int i=0; i<totalCount; i++)
		{
			percentComplete=(float)(100.0*i/(totalCount-1));
			DecimalFormat df = new DecimalFormat();
			df.setMaximumFractionDigits(2);
			status.setText("computing features...target downstream effect. Converting to DAG - adding DAG nodes ["+df.format(percentComplete)+"%]");
			bDagGraph.addVertex(nodeSet[i].toString());
		}
		totalCount=bipartiteGraph.getEdgeCount();
		for(int i=0; i<totalCount; i++) 
		{
			edge e=(edge)edgeSet[i];
			String sNode = e.getSourceID();
			if(bipartiteGraph.containsVertex(sNode)==false)
				sNode=e.getSourceName();
			String tNode = e.getTargetID();
			if(bipartiteGraph.containsVertex(tNode)==false)
				tNode=e.getTargetName();
			percentComplete=(float)(100.0*i/(totalCount-1));
			DecimalFormat df = new DecimalFormat();
			df.setMaximumFractionDigits(2);
			status.setText("computing features...target downstream effect. Converting to DAG - adding DAG edges ["+df.format(percentComplete)+"%]");
			bDagGraph.addEdge(e, sNode, tNode);
		}
		totalCount=bG.getSCC().getSize();
		for(int i=0; i<totalCount; i++) 
		{
			percentComplete=(float)(100.0*i/(totalCount-1));
			DecimalFormat df = new DecimalFormat();
			df.setMaximumFractionDigits(2);
			status.setText("computing features...target downstream effect. Converting to DAG - adding SCC node and rewiring edges ["+df.format(percentComplete)+"%]");
			bDagGraph.addVertex(bG.getSCC().getGroupName(i));
			for (int j=0; j<bG.getSCC().getGroupSize(i); j++) 
			{
				String nodeId=bG.getSCC().getItemInGroup(i, j);
				String nodeName=bG.getNode().getNodeName(bG.getNode().getNodeIdIndex(nodeId));
				status.setText("computing features...target downstream effect. Converting to DAG - adding SCC node and rewiring edges ["+df.format(percentComplete)+"%] rewiring edges");
				rewireEdges(i, nodeId, nodeName, bG.getSCC().getGroupName(i), bG);
				status.setText("computing features...target downstream effect. Converting to DAG - adding SCC node and rewiring edges ["+df.format(percentComplete)+"%] removing node");
				if(bDagGraph.containsVertex(nodeId))
					bDagGraph.removeVertex(nodeId);
				else if(bDagGraph.containsVertex(nodeName))
					bDagGraph.removeVertex(nodeName);
			}
		}
		nodeSet = bDagGraph.getVertices().toArray();
		totalCount=bDagGraph.getVertexCount();
		for(int i=0; i<totalCount; i++)
		{
			percentComplete=(float)(100.0*i/(totalCount-1));
			DecimalFormat df = new DecimalFormat();
			df.setMaximumFractionDigits(2);
			status.setText("computing features...target downstream effect. Converting to DAG - storing DAG graph information ["+df.format(percentComplete)+"%]");
			dagG.getNode().newRecord();
			dagG.getNode().setNodeId(dagG.getNode().getSize()-1, nodeSet[i].toString());
		}
	}
	
	private void updateCurrNode(String thisNode, String indexType)
	{
		if(bipartiteGraph.containsVertex(thisNode)==true)
		{
			int nodeIndex=bG.getNode().getNodeIdIndex(thisNode);
			if(nodeIndex==(-1))
			  nodeIndex=bG.getNode().getNodeNameIndex(thisNode);
			if(nodeIndex!=(-1))
			{
				int graphIndex=(-1);
				if(indexType.compareTo(PREORDER)==0)
				{
					graphIndex=bG.getNode().getPreorder().getItem(nodeIndex);
					if(graphIndex==(-1))
						bG.getNode().getPreorder().setItem(nodeIndex, bG.getIndexCounter().getCount());
				}
				else
				{
					graphIndex=bG.getNode().getPostorder().getItem(nodeIndex);
					if(graphIndex==(-1))
						bG.getNode().getPostorder().setItem(nodeIndex, bG.getIndexCounter().getCount());
				}
			}
		}
		else //this is part of SCC
		{
			int sccIndex=bG.getSCC().getGroupIndexWithGroupName(thisNode);
			ArrayList<String> sccNodeList=bG.getSCC().getItemNameList(sccIndex);
			for(int i=0; i<sccNodeList.size(); i++)
			{
				String sccItem=sccNodeList.get(i);
				int index=bG.getNode().getNodeIdIndex(sccItem);
				if(index==(-1))
					index=bG.getNode().getNodeNameIndex(sccItem);
				if(index!=(-1))
				{
					int graphIndex=(-1);
					if(indexType.compareTo(PREORDER)==0)
					{
						graphIndex=bG.getNode().getPreorder().getItem(index);
						if(graphIndex==(-1))
							bG.getNode().getPreorder().setItem(index, bG.getIndexCounter().getCount());
					}
					else
					{
						graphIndex=bG.getNode().getPostorder().getItem(index);
						if(graphIndex==(-1))
							bG.getNode().getPostorder().setItem(index, bG.getIndexCounter().getCount());
					}
				}
			}
		}
	}
	
	private int getPreorder(String thisNode)
	{
		if(bipartiteGraph.containsVertex(thisNode)==true)
		{
			int nodeIndex=bG.getNode().getNodeIdIndex(thisNode);
			if(nodeIndex==(-1))
				nodeIndex=bG.getNode().getNodeNameIndex(thisNode);
			if(nodeIndex!=(-1))
				return bG.getNode().getPreorder().getItem(nodeIndex);
			else
				return -2;
		}
		else //this is part of SCC
		{
			int sccIndex=bG.getSCC().getGroupIndexWithGroupName(thisNode);
			ArrayList<String> sccNodeList=bG.getSCC().getItemNameList(sccIndex);
			String item="";
			for(int i=0; i<sccNodeList.size(); i++)
			{
				String sccItem=sccNodeList.get(i);
				int index=bG.getNode().getNodeIdIndex(sccItem);
				if(index==(-1))
					index=bG.getNode().getNodeNameIndex(sccItem);
				if(index!=(-1))
					item=sccItem;
			}
			int index=bG.getNode().getNodeIdIndex(item);
			if(index==(-1))
				index=bG.getNode().getNodeNameIndex(item);
			if(index!=(-1))
				return bG.getNode().getPreorder().getItem(index);
			else
				return -2;
		}
	}
		
	private void DFS(String currNode, String parentNode)
	{ 
		//check if this node is part of SCC. If it is, then check if this SCC preorder has been set. If not set, then set it
		updateCurrNode(currNode, PREORDER);
		bG.getIndexCounter().increment();  	
		//check if this node has children and go to each in turn
		Object[] childrenList=bDagGraph.getSuccessors(currNode).toArray();
		if(childrenList.length==0)
			updateCurrNode(currNode, POSTORDER);
		else
		{
			for(int i=0; i<childrenList.length; i++)
			{
				String child=childrenList[i].toString();
				if(getPreorder(child)==(-1))
					DFS(child, currNode);
				else
				{
					int branchIndex=bG.getBranch().getGroupIndexWithGroupName(currNode);
					if(branchIndex==(-1))
					{
						bG.getBranch().newRecord();
						branchIndex = bG.getBranch().getSize()-1;
						bG.getBranch().setGroupName(branchIndex, currNode);
					}
					bG.getBranch().newItemRecord(branchIndex);
					bG.getBranch().setItemInGroup(branchIndex, bG.getBranch().getGroupSize(branchIndex)-1, child);
				}
			}
			updateCurrNode(currNode, POSTORDER);
		}
		bG.getIndexCounter().increment();  	
	}
	
	private void TDE_DFS(String currNode, String parentNode, JLabel status, float percentComplete, boolean WHOLE_NETWORK)
	{ 
		int nodeIndex;
		int dagTDEvalIndex;
		BigDecimal contextCoverageScore = BigDecimal.ZERO, currHubCoeff, currProbCoeff;
		DecimalFormat df = new DecimalFormat();
		//check if this node is part of SCC. If it is, then check if this SCC preorder has been set. If not set, then set it
		status.setText("computing features...target downstream effect - performing DFS ["+df.format(percentComplete)+"%] setting preorder");
		dagG.getNode().getPreorder().setItem(dagG.getNode().getNodeIdIndex(currNode), dagG.getIndexCounter().getCount());
		dagG.getIndexCounter().increment();  	
		//check if this node has children and go to each in turn
		Object[] childrenList=bDagGraph.getSuccessors(currNode).toArray();
		int sccIndex = bG.getSCC().getGroupIndexWithGroupName(currNode);
		if(sccIndex!=-1)
		{
			status.setText("computing features...target downstream effect - performing DFS ["+df.format(percentComplete)+"%] currNode is an SCC, get SCC TDE");
			for (int j = 0; j < bG.getSCC().getGroupSize(sccIndex); j++) 
			{
				String node = bG.getSCC().getItemInGroup(sccIndex, j);
				nodeIndex = bG.getNode().getNodeIdIndex(node);
				if(nodeIndex==-1)
					nodeIndex = bG.getNode().getNodeNameIndex(node);
				if (bG.getNode().getIsVirtualNode(nodeIndex) == false) 
				{
					int nodeDBID=DB.getNodeDBID(node);
					if(WHOLE_NETWORK)
					{
						currHubCoeff=BigDecimal.valueOf(Double.parseDouble(DB.get_feature(DB.getNodeID(nodeDBID), DB.getNodeName(nodeDBID), DB_TOTAL_DEGREE)));
						currProbCoeff=BigDecimal.valueOf(Double.parseDouble(DB.get_feature(DB.getNodeID(nodeDBID), DB.getNodeName(nodeDBID), DB_PROB_COEFF)));
					}
					else
					{
						currHubCoeff=BigDecimal.valueOf(Double.parseDouble(DB.get_pathwayFeature(DB.getNodeID(nodeDBID), DB.getNodeName(nodeDBID), DB_TOTAL_DEGREE)));
						currProbCoeff=BigDecimal.valueOf(Double.parseDouble(DB.get_pathwayFeature(DB.getNodeID(nodeDBID), DB.getNodeName(nodeDBID), DB_PROB_COEFF)));
					}
					currHubCoeff = currHubCoeff.multiply(currProbCoeff);
					contextCoverageScore = contextCoverageScore.add(currHubCoeff);
				}
			}
		}
		if(childrenList.length==0)
		{
			//no more children (at leaf)
			dagG.getNode().getPostorder().setItem(dagG.getNode().getNodeIdIndex(currNode), dagG.getIndexCounter().getCount());
			//check if this node is an scc and update tde of all nodes in this scc
			sccIndex = bG.getSCC().getGroupIndexWithGroupName(currNode);
			if(sccIndex!=-1)
			{
				status.setText("computing features...target downstream effect - performing DFS ["+df.format(percentComplete)+"%] currNode has no children, update dagTDEval");
				dagTDEvalIndex=dagList.indexOf(currNode);
				if(dagTDEvalIndex!=-1)
				{
					dagTDEval.set(dagTDEvalIndex, contextCoverageScore);
					System.out.println("DFS dagTDEval set "+currNode+"="+contextCoverageScore);
				}
				else
				{
					System.out.println("DFS dagTDEvalIndex!=-1 **************** ERROR 1!");
					return; 
				}
			}
		}
		else
		{
			HashMap<String, BigDecimal>map=new HashMap<String, BigDecimal>();
			ArrayList<String> key=new ArrayList<String>();
			ArrayList<BigDecimal> val=new ArrayList<BigDecimal>();
			status.setText("computing features...target downstream effect - performing DFS ["+df.format(percentComplete)+"%] currNode has children, update dagTDEval");
			for(int i=0; i<childrenList.length; i++)
			{
				String child=childrenList[i].toString();
				if(dagG.getNode().getPreorder().getItem(dagG.getNode().getNodeIdIndex(child))==(-1))
					TDE_DFS(child, currNode, status, percentComplete, WHOLE_NETWORK);
				else
				{
					int branchIndex=dagG.getBranch().getGroupIndexWithGroupName(currNode);
					if(branchIndex==(-1))
					{
						dagG.getBranch().newRecord();
						branchIndex = dagG.getBranch().getSize()-1;
						dagG.getBranch().setGroupName(branchIndex, currNode);
					}
					dagG.getBranch().newItemRecord(branchIndex);
					dagG.getBranch().setItemInGroup(branchIndex, dagG.getBranch().getGroupSize(branchIndex)-1, child);
				}
				dagTDEvalIndex=dagList.indexOf(child);
				if(dagTDEvalIndex!=-1)
				{
					map.put(child, dagTDEval.get(dagTDEvalIndex));
					key.add(child);
					val.add(dagTDEval.get(dagTDEvalIndex));
					System.out.println(currNode+"->"+child+" "+dagTDEval.get(dagTDEvalIndex));
				}
				else
				{
					System.out.println("DFS dagTDEvalIndex!=-1 **************** ERROR 2!");
					return; 
				}
			}
			BigDecimal maxVal=Collections.max(map.values());
			int index=val.indexOf(maxVal);
			String prevKeyName=key.get(index);
			contextCoverageScore = contextCoverageScore.add(maxVal);
			map.remove(prevKeyName);
			val.remove(index);
			key.remove(index);
			while(map.size()>0)
			{
				maxVal=Collections.max(map.values());
				index=val.indexOf(maxVal);
				String currKeyName=key.get(index);
				int currKeyDagGIndex, prevKeyDagGIndex;
				currKeyDagGIndex=dagG.getNode().getNodeIdIndex(currKeyName);
				prevKeyDagGIndex=dagG.getNode().getNodeIdIndex(prevKeyName);
				if(!(dagG.getNode().getPreorder().getItem(prevKeyDagGIndex) <= dagG.getNode().getPreorder().getItem(currKeyDagGIndex) 
						&& dagG.getNode().getPostorder().getItem(prevKeyDagGIndex) >= dagG.getNode().getPostorder().getItem(currKeyDagGIndex)))
					contextCoverageScore = contextCoverageScore.add(maxVal);
				map.remove(currKeyName);
				val.remove(index);
				key.remove(index);
				//System.out.println("prevKey:"+prevKeyName+" currKey:"+currKeyName);
				prevKeyName=currKeyName;
			}
			dagG.getNode().getPostorder().setItem(dagG.getNode().getNodeIdIndex(currNode), dagG.getIndexCounter().getCount());
			dagTDEvalIndex=dagList.indexOf(currNode);
			if(dagTDEvalIndex!=-1)
			{
				dagTDEval.set(dagTDEvalIndex, contextCoverageScore);
				System.out.println("DFS dagTDEval set "+currNode+"="+contextCoverageScore);
			}
			else
			{
				System.out.println("DFS dagTDEvalIndex!=-1 **************** ERROR 3!");
				return; 
			}
		}
		dagG.getIndexCounter().increment();  	
		//System.out.println(dagG.getNode().getDataStream());
		//System.out.println(dagList.toString()+"="+dagTDEval.toString());
	}
	
	private void getPrePostOrder(JLabel status)
	{ 
		Object[] vertexList = bDagGraph.getVertices().toArray();
		ArrayList<String> rootList = new ArrayList<String>();
		bG.getNode().getPreorder().initialize();
		bG.getNode().getPostorder().initialize();
		bG.getIndexCounter().reset();
		float percentComplete;
		int totalCount=vertexList.length;
		// identify all the roots in the graph
		for(int i=0; i<totalCount; i++) 
		{
			percentComplete=(float)(100.0*i/(totalCount-1));
			DecimalFormat df = new DecimalFormat();
			df.setMaximumFractionDigits(2);
			status.setText("computing features...target downstream effect. Getting preorder and postorder - identifying root nodes ["+df.format(percentComplete)+"%]");
			String currVertex = vertexList[i].toString();
			if (bDagGraph.getInEdges(currVertex).size() == 0)
				rootList.add(currVertex);
		}
		// for each root, do a DFS traversal and record the preorder, postorder and non-tree edge information
		totalCount=rootList.size();
		for (int i=0; i<totalCount; i++)
		{
			percentComplete=(float)(100.0*i/(totalCount-1));
			DecimalFormat df = new DecimalFormat();
			df.setMaximumFractionDigits(2);
			status.setText("computing features...target downstream effect. Getting preorder and postorder - performing DFS traversal and recording preorder, postorder and non-tree edge information ["+df.format(percentComplete)+"%]");
			DFS(rootList.get(i), null);
		}
		//System.out.println(bG.getSCC().getDataStream());
	}
	
	public ArrayList<ArrayList<String>> findConnectionOfCurrentGraphConnectionPath(JLabel status, ArrayList<String> nodePair)
	{
		ArrayList<String> nodeList=new ArrayList<String>();
		connectionPaths=new ArrayList<ArrayList<String>>();
		ArrayList<ArrayList<String>> sccNodeMapping=new ArrayList<ArrayList<String>>(); 
		
		status.setText("visualizing node pair connection ... finding SCC ");
		findSCC(status);
		convertToDAG(status);
		getPrePostOrder(status);
		
		print();
		
		//check if nodes are in a SCC
		String scc1, scc2;
		scc1=bG.getSCCMetaNodeOf(nodePair.get(0));
		scc2=bG.getSCCMetaNodeOf(nodePair.get(1));
		System.out.println("scc1:"+scc1+" scc2:"+scc2);
		if(scc1==null)
			scc1=nodePair.get(0);
		if(scc2==null)
			scc2=nodePair.get(1);
		
		//if nodes are in the same SCC, get those nodes in the SCC and draw an induced subgraph
		if(scc1.compareTo(scc2)==0)
		{
			nodeList=bG.getSCC().getItemNameList(bG.getSCC().getGroupIndexWithGroupName(scc1));
			ArrayList<String> path=new ArrayList<String>();
			path.add(nodePair.get(0));
			path.add(scc1);
			path.add(nodePair.get(1));
			connectionPaths.add(path);
			int SCCindex=bG.getSCC().getGroupIndexWithGroupName(scc1);
			if(SCCindex!=-1)
			{
				ArrayList<String> sccList=new ArrayList<String>();
				sccList=bG.getSCC().getItemNameList(SCCindex);
				ArrayList<String> sccNodeMappingElement=new ArrayList<String>();
				sccNodeMappingElement.add(scc1);
				for(int k=0; k<sccList.size(); k++)
				{
					if(sccNodeMappingElement.contains(sccList.get(k))==false)
						sccNodeMappingElement.add(sccList.get(k));
				}
				sccNodeMapping.add(sccNodeMappingElement);
			}
		}
		//else perform DFS and obtain connecting path 
		else
		{
			//traverse and find the path from scc1 to scc2
			Collection<edge> incidentEdges=bDagGraph.getIncidentEdges(scc1);
			ArrayList<edge> edgeList;
			ArrayList<String> children=new ArrayList<String>();
			ArrayList<String> currentPath=new ArrayList<String>();
			if(incidentEdges!=null)
			{
				edgeList = new ArrayList<edge>(incidentEdges);
				currentPath.add(scc1);
				for(int i=0; i<edgeList.size(); i++)
				{
					edge e=edgeList.get(i);
					System.out.println(scc1+" "+e.getSourceName()+" "+e.getTargetName());
					if(e.getSourceName().compareTo(scc1)==0)
						children.add(e.getTargetName());
				}
				for(int i=0; i<children.size(); i++)
				{
					currentPath.add(children.get(i));
					//found a path
					if(children.get(i).compareTo(scc2)==0)
					{
						ArrayList<String> foundPath=new ArrayList<String>();
						for(int j=0; j<currentPath.size(); j++)
						{
							String node=currentPath.get(j);
							int SCCindex=bG.getSCC().getGroupIndexWithGroupName(node);
							if(SCCindex==-1)
								foundPath.add(node);
							else
							{
								ArrayList<String> sccList=new ArrayList<String>();
								sccList=bG.getItemsOfMetaNode(node);
								if(sccList.size()<2 && sccList.size()>0)
									foundPath.add(sccList.get(0));
								else
								{
									foundPath.add(node);
									ArrayList<String> sccNodeMappingElement=new ArrayList<String>();
									sccNodeMappingElement.add(node);
									for(int k=0; k<sccList.size(); k++)
									{
										if(sccNodeMappingElement.contains(sccList.get(k))==false)
											sccNodeMappingElement.add(sccList.get(k));
									}
									sccNodeMapping.add(sccNodeMappingElement);
									System.out.println("added: "+node+" "+sccNodeMappingElement.toString());
								}
							}
						}
						if(scc1.compareTo(nodePair.get(0))!=0)
							foundPath.add(0,nodePair.get(0));
						if(scc2.compareTo(nodePair.get(1))!=0)
							foundPath.add(nodePair.get(1));
						connectionPaths.add(foundPath);
						System.out.println("found a path:"+currentPath.toString());
						currentPath=new ArrayList<String>();
						currentPath.add(scc1);
					}
					else
						currentPath=DFS_path(children.get(i), scc1, scc2, currentPath, "NORMAL");
				}
			}
			//traverse and find the path from scc2 to scc1
			incidentEdges=bDagGraph.getIncidentEdges(scc2);
			if(incidentEdges!=null)
			{
				edgeList = new ArrayList<edge>(incidentEdges);
				children=new ArrayList<String>();
				currentPath=new ArrayList<String>();
				currentPath.add(scc2);
				for(int i=0; i<edgeList.size(); i++)
				{
					edge e=edgeList.get(i);
					if(e.getSourceName().compareTo(scc2)==0)
						children.add(e.getTargetName());
				}
				for(int i=0; i<children.size(); i++)
				{
					currentPath.add(children.get(i));
					//found a path
					if(children.get(i).compareTo(scc1)==0)
					{
						ArrayList<String> foundPath=new ArrayList<String>();
						for(int j=0; j<currentPath.size(); j++)
						{
							String node=currentPath.get(j);
							int SCCindex=bG.getSCC().getGroupIndexWithGroupName(node);
							if(SCCindex==-1)
								foundPath.add(node);
							else
							{
								ArrayList<String> sccList=new ArrayList<String>();
								sccList=bG.getItemsOfMetaNode(node);
								if(sccList.size()<2 && sccList.size()>0)
									foundPath.add(sccList.get(0));
								else
								{
									foundPath.add(node);
									ArrayList<String> sccNodeMappingElement=new ArrayList<String>();
									sccNodeMappingElement.add(node);
									for(int k=0; k<sccList.size(); k++)
									{
										if(sccNodeMappingElement.contains(sccList.get(k))==false)
											sccNodeMappingElement.add(sccList.get(k));
									}
									sccNodeMapping.add(sccNodeMappingElement);
									System.out.println("added: "+node+" "+sccNodeMappingElement.toString());
								}
							}
						}
						if(scc2.compareTo(nodePair.get(1))!=0)
							foundPath.set(0,nodePair.get(1));
						if(scc1.compareTo(nodePair.get(0))!=0)
							foundPath.add(nodePair.get(0));
						connectionPaths.add(foundPath);
						System.out.println("found a path:"+currentPath.toString());
						currentPath=new ArrayList<String>();
						currentPath.add(scc2);
					}
					else
						currentPath=DFS_path(children.get(i), scc2, scc1, currentPath, "NORMAL");
				}
			}
			/*if(connectionPaths.size()>0)
			{
				for(int i=0; i<connectionPaths.size(); i++)
				{
					ArrayList<String> aPath=new ArrayList<String>();
					aPath=connectionPaths.get(i);
					for(int j=0; j<aPath.size(); j++)
					{
						String node=aPath.get(j);
						//check if node is a SCC metanode. If so, proceed to add members of SCC nodes.
						int SCCindex=bG.getSCC().getGroupIndexWithGroupName(node);
						if(SCCindex==-1)
						{
							if(nodeList.contains(node)==false)
								nodeList.add(node);
						}
						else
						{
							ArrayList<String> sccList=new ArrayList<String>();
							sccList=bG.getSCC().getItemNameList(SCCindex);
							for(int k=0; k<sccList.size(); k++)
							{
								if(nodeList.contains(sccList.get(k))==false)
									nodeList.add(sccList.get(k));
							}
						}
					}
				}
			}*/
		}
		
		if(sccNodeMapping.size()>0)
		{
			ArrayList<String> header=new ArrayList<String>();
			header.add("SCC_NODE_MAPPING");
			connectionPaths.add(header);
			for(int i=0; i<sccNodeMapping.size(); i++)
				connectionPaths.add(sccNodeMapping.get(i));
		}
		
		System.out.println(nodePair.toString());
		System.out.println("scc1:"+scc1+" scc2:"+scc2);
		status.setText("visualizing node pair connection completed");
		//return nodeList;
		for(int i=0; i<connectionPaths.size(); i++)
			System.out.println(connectionPaths.get(i).toString());
		
		return connectionPaths;
	}
	
	public ArrayList<String> findConnectionOfCurrentGraphNodeList(JLabel status, ArrayList<String> nodePair)
	{
		ArrayList<String> nodeList=new ArrayList<String>();
		connectionPaths=new ArrayList<ArrayList<String>>();
		
		status.setText("visualizing node pair connection ... finding SCC ");
		findSCC(status);
		convertToDAG(status);
		getPrePostOrder(status);
		
		print();
		
		//check if nodes are in a SCC
		String scc1, scc2;
		scc1=bG.getSCCMetaNodeOf(nodePair.get(0));
		scc2=bG.getSCCMetaNodeOf(nodePair.get(1));
		System.out.println("scc1:"+scc1+" scc2:"+scc2);
		if(scc1==null)
			scc1=nodePair.get(0);
		if(scc2==null)
			scc2=nodePair.get(1);
		
		//if nodes are in the same SCC, get those nodes in the SCC and draw an induced subgraph
		if(scc1.compareTo(scc2)==0)
		{
			nodeList=bG.getSCC().getItemNameList(bG.getSCC().getGroupIndexWithGroupName(scc1));
			ArrayList<String> path=new ArrayList<String>();
			path.add(scc1);
			connectionPaths.add(path);
		}
		//else perform DFS and obtain connecting path 
		else
		{
			//traverse and find the path from scc1 to scc2
			Collection<edge> incidentEdges=bDagGraph.getIncidentEdges(scc1);
			ArrayList<edge> edgeList;
			ArrayList<String> children=new ArrayList<String>();
			ArrayList<String> currentPath=new ArrayList<String>();
			if(incidentEdges!=null)
			{
				edgeList = new ArrayList<edge>(incidentEdges);
				currentPath.add(scc1);
				for(int i=0; i<edgeList.size(); i++)
				{
					edge e=edgeList.get(i);
					System.out.println(scc1+" "+e.getSourceName()+" "+e.getTargetName());
					if(e.getSourceName().compareTo(scc1)==0)
						children.add(e.getTargetName());
				}
				for(int i=0; i<children.size(); i++)
				{
					currentPath.add(children.get(i));
					//found a path
					if(children.get(i).compareTo(scc2)==0)
					{
						ArrayList<String> foundPath=new ArrayList<String>();
						for(int j=0; j<currentPath.size(); j++)
							foundPath.add(currentPath.get(j));
						connectionPaths.add(foundPath);
						System.out.println("found a path:"+currentPath.toString());
						currentPath=new ArrayList<String>();
						currentPath.add(scc1);
					}
					else
						currentPath=DFS_path(children.get(i), scc1, scc2, currentPath, "NORMAL");
				}
			}
			//traverse and find the path from scc2 to scc1
			incidentEdges=bDagGraph.getIncidentEdges(scc2);
			if(incidentEdges!=null)
			{
				edgeList = new ArrayList<edge>(incidentEdges);
				children=new ArrayList<String>();
				currentPath=new ArrayList<String>();
				currentPath.add(scc2);
				for(int i=0; i<edgeList.size(); i++)
				{
					edge e=edgeList.get(i);
					if(e.getSourceName().compareTo(scc2)==0)
						children.add(e.getTargetName());
				}
				for(int i=0; i<children.size(); i++)
				{
					currentPath.add(children.get(i));
					//found a path
					if(children.get(i).compareTo(scc1)==0)
					{
						ArrayList<String> foundPath=new ArrayList<String>();
						for(int j=0; j<currentPath.size(); j++)
							foundPath.add(currentPath.get(j));
						connectionPaths.add(foundPath);
						System.out.println("found a path:"+currentPath.toString());
						currentPath=new ArrayList<String>();
						currentPath.add(scc2);
					}
					else
						currentPath=DFS_path(children.get(i), scc2, scc1, currentPath, "NORMAL");
				}
			}
			if(connectionPaths.size()>0)
			{
				for(int i=0; i<connectionPaths.size(); i++)
				{
					ArrayList<String> aPath=new ArrayList<String>();
					aPath=connectionPaths.get(i);
					for(int j=0; j<aPath.size(); j++)
					{
						String node=aPath.get(j);
						//check if node is a SCC metanode. If so, proceed to add members of SCC nodes.
						int SCCindex=bG.getSCC().getGroupIndexWithGroupName(node);
						if(SCCindex==-1)
						{
							if(nodeList.contains(node)==false)
								nodeList.add(node);
						}
						else
						{
							ArrayList<String> sccList=new ArrayList<String>();
							sccList=bG.getSCC().getItemNameList(SCCindex);
							for(int k=0; k<sccList.size(); k++)
							{
								if(nodeList.contains(sccList.get(k))==false)
									nodeList.add(sccList.get(k));
							}
						}
					}
				}
			}
		}
		
		System.out.println(nodePair.toString());
		System.out.println("scc1:"+scc1+" scc2:"+scc2);
		status.setText("visualizing node pair connection completed");
		return nodeList;
		//return connectionPaths;
	}
	
	private ArrayList<String> DFS_path (String currNode, String startNode, String destNode, ArrayList<String> currentPath, String mode)
	{
		Collection<edge> incidentEdges;
		if(mode.compareTo("REVERSE")==0)
			incidentEdges=reverseBDagGraph.getIncidentEdges(currNode);
		else
			incidentEdges=bDagGraph.getIncidentEdges(currNode);
		ArrayList<edge> edgeList = new ArrayList<edge>(incidentEdges);
		ArrayList<String> children=new ArrayList<String>();
		if(currentPath.contains(currNode)==false)
			currentPath.add(currNode);
		for(int i=0; i<edgeList.size(); i++)
		{
			edge e=edgeList.get(i);
			if(e.getSourceName().compareTo(currNode)==0)
				children.add(e.getTargetName());
		}
		for(int i=0; i<children.size(); i++)
		{
			if(currentPath.contains(children.get(i))==false)
				currentPath.add(children.get(i));
			//found a path
			if(destNode!=null && children.get(i).compareTo(destNode)==0)
			{
				ArrayList<String> foundPath=new ArrayList<String>();
				for(int j=0; j<currentPath.size(); j++)
					foundPath.add(currentPath.get(j));
				connectionPaths.add(foundPath);
				System.out.println("found a path:"+currentPath.toString());
				currentPath=new ArrayList<String>();
				currentPath.add(startNode);
				return currentPath;
			}
			else
				DFS_path(children.get(i), startNode, destNode, currentPath, mode);
		}
		return currentPath;
	}
	
	public void getTDE(JLabel status, boolean WHOLE_NETWORK)
	{
		status.setText("computing features...target downstream effect. Finding strongly connected components ");
		findSCC(status);
		convertToDAG(status);
		getPrePostOrder(status);
		if(DB.getComputedFeature(DB_IN_DEGREE)==false || DB.getComputedFeature(DB_OUT_DEGREE)==false || DB.getComputedFeature(DB_TOTAL_DEGREE)==false)
			getDegree(status, WHOLE_NETWORK);
		if(DB.getComputedFeature(DB_PROB_COEFF)==false)
			getProbCoeff(status, CONSTANT, WHOLE_NETWORK);
		System.out.println("computing TDE");
		int nodeIndex, dagTDEvalIndex;
		BigDecimal contextCoverageScore = BigDecimal.ZERO, currHubCoeff, currProbCoeff;
		ArrayList<String> rootList = new ArrayList<String>();
		dagList=new ArrayList<String>();
		dagTDEval=new ArrayList<BigDecimal>();
		dagG.getNode().getPreorder().initialize();
		dagG.getNode().getPostorder().initialize();
		dagG.getIndexCounter().reset();
		Object[] vertices=bDagGraph.getVertices().toArray();
		float percentComplete;
		int totalCount=bDagGraph.getVertexCount();
		// identify all the roots in the graph
		for (int i=0; i<totalCount; i++) 
		{
			String node=vertices[i].toString();
			percentComplete=(float)(100.0*i/(totalCount-1));
			DecimalFormat df = new DecimalFormat();
			df.setMaximumFractionDigits(2);
			status.setText("computing features...target downstream effect - identifying root nodes in DAG ["+df.format(percentComplete)+"%]");
			dagList.add(node);
			dagTDEval.add(BigDecimal.ZERO);
			if(bDagGraph.getInEdges(node).size() == 0)
				rootList.add(node);
		}
		System.out.println(rootList.size());
		System.out.println(rootList.toString());
		// for each root, do a DFS traversal and record the preorder, postorder and non-tree edge information
		totalCount=rootList.size();
		for (int i=0; i<totalCount; i++)
		{
			percentComplete=(float)(100.0*i/(totalCount-1));
			DecimalFormat df = new DecimalFormat();
			df.setMaximumFractionDigits(2);
			status.setText("computing features...target downstream effect - performing DFS of DAG and recording information ["+df.format(percentComplete)+"%]");
			TDE_DFS(rootList.get(i), null, status, percentComplete, WHOLE_NETWORK);
		}
		totalCount=bDagGraph.getVertexCount();
		for (int i=0; i<totalCount; i++) 
		{
			String currVertex=vertices[i].toString();
			percentComplete=(float)(100.0*i/(totalCount-1));
			DecimalFormat df = new DecimalFormat();
			df.setMaximumFractionDigits(2);
			status.setText("computing features...target downstream effect ["+df.format(percentComplete)+"%] computing value");
			int sccIndex = bG.getSCC().getGroupIndexWithGroupName(currVertex);
			if(sccIndex!=-1)
			{
				for (int j=0; j<bG.getSCC().getGroupSize(sccIndex); j++) 
				{
					String node = bG.getSCC().getItemInGroup(sccIndex, j);
					nodeIndex = bG.getNode().getNodeIdIndex(node);
					if(nodeIndex==-1)
						nodeIndex = bG.getNode().getNodeNameIndex(node);
					if (bG.getNode().getIsVirtualNode(nodeIndex) == false) 
					{
						dagTDEvalIndex=dagList.indexOf(currVertex);
						if(dagTDEvalIndex!=-1)
							contextCoverageScore=dagTDEval.get(dagTDEvalIndex);
						else
						{
							System.out.println("getTDE dagTDEvalIndex!=-1 **************** ERROR!");
							return; 
						}
						int nodeDBID=DB.getNodeDBID(node);
						if(WHOLE_NETWORK)
						{
							currHubCoeff=BigDecimal.valueOf(Double.parseDouble(DB.get_feature(DB.getNodeID(nodeDBID), DB.getNodeName(nodeDBID), DB_TOTAL_DEGREE)));
							currProbCoeff=BigDecimal.valueOf(Double.parseDouble(DB.get_feature(DB.getNodeID(nodeDBID), DB.getNodeName(nodeDBID), DB_PROB_COEFF)));
						}
						else
						{
							currHubCoeff=BigDecimal.valueOf(Double.parseDouble(DB.get_pathwayFeature(DB.getNodeID(nodeDBID), DB.getNodeName(nodeDBID), DB_TOTAL_DEGREE)));
							currProbCoeff=BigDecimal.valueOf(Double.parseDouble(DB.get_pathwayFeature(DB.getNodeID(nodeDBID), DB.getNodeName(nodeDBID), DB_PROB_COEFF)));
						}
						currHubCoeff = currHubCoeff.multiply(currProbCoeff);
						//System.out.println("node:"+node+" contextCoverage:"+contextCoverageScore+" currHubCoeff:"+currHubCoeff);
						contextCoverageScore = contextCoverageScore.subtract(currHubCoeff);
						status.setText("computing features...target downstream effect ["+df.format(percentComplete)+"%] updating value to database");
						if(WHOLE_NETWORK)
							DB.update_feature(DB.getNodeID(nodeDBID), DB.getNodeName(nodeDBID), DB_TDE, contextCoverageScore.toString());
						else
							DB.update_pathwayFeature(DB.getNodeID(nodeDBID), DB.getNodeName(nodeDBID), DB_TDE, contextCoverageScore.toString());
					}
				}
			}
		}
		System.out.println(bG.getSCC().getDataStream());
		System.out.println(dagList.toString()+"="+dagTDEval.toString());
		System.out.println("computed TDE");
	}
	
	public void getProbCoeff(JLabel status, int type, boolean WHOLE_NETWORK) 
	{
		System.out.println("computing probability coefficient");
		BigDecimal value;
		Random generator = new Random();
		Object[] vertices=bipartiteGraph.getVertices().toArray();
		float percentComplete;
		int totalCount=bipartiteGraph.getVertexCount();
		for(int i=0; i<totalCount; i++) 
		{
			String node=vertices[i].toString();
			percentComplete=(float)(100.0*i/(totalCount-1));
			DecimalFormat df = new DecimalFormat();
			df.setMaximumFractionDigits(2);
			status.setText("computing features...probability coefficient ["+df.format(percentComplete)+"%] computing value");
			if (type == CONSTANT)
				value = BigDecimal.ONE;
			else
				value = BigDecimal.valueOf(generator.nextDouble());
			status.setText("computing features...probability coefficient ["+df.format(percentComplete)+"%] updating value to database");
			int nodeDBID=DB.getNodeDBID(node);
			if(WHOLE_NETWORK)
				DB.update_feature(DB.getNodeID(nodeDBID), DB.getNodeName(nodeDBID), DB_PROB_COEFF, String.valueOf(value));
			else
				DB.update_pathwayFeature(DB.getNodeID(nodeDBID), DB.getNodeName(nodeDBID), DB_PROB_COEFF, String.valueOf(value));
		}
		DB.setComputedFeature(DB_PROB_COEFF, true);
		System.out.println("computed probability coefficient");
	}
	
	public void removeNodesNotInList(JLabel status, ArrayList<String> nodeList)
	{
		ArrayList<String> complementaryNodeList=new ArrayList<String>();
		complementaryNodeList=bG.getComplementaryNodeList(nodeList, complementaryNodeList);
		System.out.println("nodeList:"+nodeList.toString());
		System.out.println("complementary list:"+complementaryNodeList.toString());
		
		dagList=new ArrayList<String>();
		dagTDEval=new ArrayList<BigDecimal>();
		dagG=new graph();
		bDagGraph = new SparseGraph<String,edge>();
		bG.removeNodes(complementaryNodeList);
		float percentComplete;
		int totalCount=complementaryNodeList.size();
		for(int i=0; i<complementaryNodeList.size(); i++)
		{
			bipartiteGraph.removeVertex(complementaryNodeList.get(i));
			percentComplete=(float)(100.0*i/(totalCount-1));
			DecimalFormat df = new DecimalFormat();
			df.setMaximumFractionDigits(2);
			status.setText("removing nodes from JUNG graph... ["+df.format(percentComplete)+"%] ");
		}
	}
	
	public void print()
	{
		System.out.println(bG.getNode().getDataStream());
		System.out.println(bG.getDataStream());
	}
	
	public void findDescendantNodes_convertToDAG(JLabel status)
	{
		System.out.println("findAllPathsBetweenNodePairs");
		findSCC(status);
		convertToDAG(status);
		System.out.println(bG.getDataStream());
	}
	
	public void findShortestPath(JLabel status, ArrayList<String> targetNodeList, ArrayList<String> diseaseNodeList)
	{
		UnweightedShortestPath<String, edge> sp=new UnweightedShortestPath<String, edge>(bipartiteGraph);
		//System.out.println("bipartiteGraph:"+bipartiteGraph.toString());
		System.out.println("targetNodeList:"+targetNodeList.toString());
		System.out.println("diseaseNodeList:"+diseaseNodeList.toString());
		for(int i=0; i<targetNodeList.size(); i++)
		{
			for(int j=0; j<diseaseNodeList.size(); j++)
			{
				String target=DB.getNodeName(DB.getNodeDBID(targetNodeList.get(i)));
				Number dist=sp.getDistance(target, diseaseNodeList.get(j));
				if(dist==null)
					System.out.println(target+"->"+diseaseNodeList.get(j)+": "+null);
				else
				{
					System.out.println(target+"->"+diseaseNodeList.get(j)+": "+dist);
					Map<String, edge> incomingEdgeMap=sp.getIncomingEdgeMap(target);
					for(String key: incomingEdgeMap.keySet())
					{
					   System.out.print(key + " - " + incomingEdgeMap.get(key));
					   edge e=incomingEdgeMap.get(key);
					   System.out.println(" "+e.getSourceName()+"->"+e.getTargetName());
					}
					System.out.println(incomingEdgeMap.containsKey(target));
					edge e=incomingEdgeMap.get(target);
					System.out.println(e);
				}
			}
		}
	}
	
	public ArrayList<String> findDescendantNodes_nodeList(JLabel status, ArrayList<String> nodeList, String mode)
	{
		ArrayList<String> nodeListMetaNode=new ArrayList<String>();
		ArrayList<String> actualNodeList=new ArrayList<String>();
		System.out.println("nodeList:"+nodeList.toString());
		if(mode.compareTo("DISEASE_NODE")==0)
			descendantNodes_diseaseNodes=new ArrayList<String>();
		if(mode.compareTo("TARGET_NODE")==0)
			descendantNodes_targetNodes=new ArrayList<String>();
		String metaNode;
		int totalCount=nodeList.size();
		for(int i=0; i<totalCount; i++)
		{
			metaNode=bG.getSCCMetaNodeOf(nodeList.get(i));
			//System.out.println("metaNode:"+metaNode.toString());
			if(metaNode!=null && nodeListMetaNode.contains(metaNode)==false)
				nodeListMetaNode.add(metaNode);
			//if(mode.compareTo("DISEASE_NODE")==0)
			//	status.setText("findDescendantNodes_nodeList...find meta node of disease node list");
			//if(mode.compareTo("TARGET_NODE")==0)
			//	status.setText("findDescendantNodes_nodeList...find meta node of target node list");
		}
		//System.out.println("diseaseNodeListMetaNode:"+diseaseNodeListMetaNode.toString());
		
		totalCount=nodeListMetaNode.size();
		for(int i=0; i<totalCount; i++)
		{
			ArrayList<String> visited = new ArrayList<String>();
			visited.add(nodeListMetaNode.get(i));
			breadthFirstTraversal(status, visited);
			for(int j=0; j<visited.size(); j++)
			{
				if(mode.compareTo("DISEASE_NODE")==0)
				{
					if(descendantNodes_diseaseNodes.contains(visited.get(j))==false)
						descendantNodes_diseaseNodes.add(visited.get(j));
				}
				if(mode.compareTo("TARGET_NODE")==0)
				{
					if(descendantNodes_targetNodes.contains(visited.get(j))==false)
						descendantNodes_targetNodes.add(visited.get(j));
				}
			}
		}
//		if(mode.compareTo("DISEASE_NODE")==0)
//			System.out.println("descendantNodes_diseaseNodes:"+descendantNodes_diseaseNodes.size()+" "+descendantNodes_diseaseNodes.toString());
//		if(mode.compareTo("TARGET_NODE")==0)
//			System.out.println("descendantNodes_targetNodes:"+descendantNodes_targetNodes.size()+" "+descendantNodes_targetNodes.toString());
		
		if(mode.compareTo("TARGET_NODE")==0)
		{
			ArrayList<String> commonNodes=new ArrayList<String>();
			for(int i=0; i<descendantNodes_diseaseNodes.size(); i++)
				commonNodes.add(descendantNodes_diseaseNodes.get(i));
			commonNodes.retainAll(descendantNodes_targetNodes);
//			System.out.println("commonNodes:"+commonNodes.size()+" "+commonNodes.toString());
			descendantNodes_complementaryNodes=new ArrayList<String>();
			for(int i=0; i<descendantNodes_targetNodes.size(); i++)
				descendantNodes_complementaryNodes.add(descendantNodes_targetNodes.get(i));
			descendantNodes_complementaryNodes.removeAll(commonNodes);
//			System.out.println("descendantNodes_complementaryNodes:"+descendantNodes_complementaryNodes.size()+" "+descendantNodes_complementaryNodes.toString());
			
			for(int i=0; i<descendantNodes_complementaryNodes.size(); i++)
			{
				int sccIndex=bG.getSCC().getGroupIndexWithGroupName(descendantNodes_complementaryNodes.get(i));
				ArrayList<String> sccNodeList=bG.getSCC().getItemNameList(sccIndex);
				for(int j=0; j<sccNodeList.size(); j++)
				{
					if(actualNodeList.contains(sccNodeList.get(j))==false)
						actualNodeList.add(sccNodeList.get(j));					
				}
			}
		}
		return actualNodeList;
	}
	
	private void breadthFirstTraversal(JLabel status, ArrayList<String> visited) {
        String node=visited.get(visited.size()-1);
		ArrayList<edge> incidentEdges=new ArrayList<edge> (bDagGraph.getIncidentEdges(node));
		//System.out.println("incidentEdges:"+incidentEdges.size()+" "+incidentEdges.toString());
		// in breadth-first, recursion needs to come after visiting adjacent nodes
        for (edge e : incidentEdges) 
        {
        	String source=e.getSourceName();
        	String target=e.getTargetName();
        	if(source.compareTo(node)==0 && target.compareTo(node)!=0)
        	{		
        		//System.out.println("e:"+e.getSourceName()+"->"+e.getTargetName());
        		if (visited.contains(target)==false) 
        		{
        			visited.add(target);
        			breadthFirstTraversal(status, visited);
        		}
        	}
        }
    }
	
	public ArrayList<nodePathCommon> getNodePathCommon(){
		return nodePathCommonList;
	}
	
	public ArrayList<nodePath> getNodePath(){
		return nodePathList;
	}
	
	public String getMetaNode(String node){
		return bG.getSCCMetaNodeOf(node);
	}
	
	public ArrayList<String> getNodesInMetaNode(String metaNode){
		return bG.getItemsOfMetaNode(metaNode);
	}
	
	public void findPathsToDiseaseNode(ArrayList<String> diseaseNode){
		ArrayList<String> diseaseNodeMetaNode=new ArrayList<String>();
		nodePathList=new ArrayList<nodePath>();
		
		for(int i=0; i<diseaseNode.size(); i++)
		{
			String meta=bG.getSCCMetaNodeOf(diseaseNode.get(i));
			if(meta!=null && diseaseNodeMetaNode.contains(meta)==false)
				diseaseNodeMetaNode.add(meta);
		}
		System.out.println("diseaseNodeMetaNode:"+diseaseNodeMetaNode.toString());
		
		if(diseaseNodeMetaNode.size()>0)
		{
			//construct a reverse dagGraph
			reverseBDagGraph = new SparseGraph<String,edge>();
			Collection<String> vertexCollection=bDagGraph.getVertices();
			if(vertexCollection!=null)
			{
				Object[] vertices = vertexCollection.toArray();
				for(int i=0; i<vertices.length; i++)
					reverseBDagGraph.addVertex(vertices[i].toString());
			}
			Collection<edge> edgeCollection=bDagGraph.getEdges();
			if(edgeCollection!=null)
			{
				Object[] edges = edgeCollection.toArray();
				for(int i=0; i<edges.length; i++)
				{
					edge e=(edge) edges[i], newE;
					String source, target, type;
					source=e.getSourceName();
					target=e.getTargetName();
					type=e.getType();
					newE=new edge(target, target, source, source, type);
					reverseBDagGraph.addEdge(newE, target, source, EdgeType.DIRECTED);
				}
			}
			for(int i=0; i<diseaseNodeMetaNode.size(); i++)
			{
				ArrayList<String> path=new ArrayList<String>();
				path=DFS_path(diseaseNodeMetaNode.get(i), diseaseNodeMetaNode.get(i), null, path, "REVERSE");
				System.out.println("path: "+path.size()+" "+path.toString());
				nodePath np=new nodePath(diseaseNodeMetaNode.get(i), path);
				nodePathList.add(np);
			}
			for(int i=0; i<nodePathList.size(); i++)
				nodePathList.get(i).print();
		}
	}
	/*public void findPathsToDiseaseNode(ArrayList<String> diseaseNode){
		ArrayList<String> diseaseNodeMetaNode=new ArrayList<String>();
		boolean notDone=true;
		ArrayList<String> listOfDiseaseNodeToCover=new ArrayList<String>();
		ArrayList<String> prevConsoDiseaseNode=new ArrayList<String>();
		ArrayList<String> prevCommonMetaNodeAncestor=new ArrayList<String>();
		ArrayList<String> currConsoDiseaseNode=new ArrayList<String>();
		ArrayList<String> currCommonMetaNodeAncestor=new ArrayList<String>();
		nodePathList=new ArrayList<nodePath>();
		nodePathCommonList=new ArrayList<nodePathCommon>();
		
		for(int i=0; i<diseaseNode.size(); i++)
		{
			String meta=bG.getSCCMetaNodeOf(diseaseNode.get(i));
			if(meta!=null && diseaseNodeMetaNode.contains(meta)==false)
				diseaseNodeMetaNode.add(meta);
		}
		System.out.println("diseaseNodeMetaNode:"+diseaseNodeMetaNode.toString());
		
		if(diseaseNodeMetaNode.size()>0)
		{
			//construct a reverse dagGraph
			reverseBDagGraph = new SparseGraph<String,edge>();
			Collection<String> vertexCollection=bDagGraph.getVertices();
			if(vertexCollection!=null)
			{
				Object[] vertices = vertexCollection.toArray();
				for(int i=0; i<vertices.length; i++)
					reverseBDagGraph.addVertex(vertices[i].toString());
			}
			Collection<edge> edgeCollection=bDagGraph.getEdges();
			if(edgeCollection!=null)
			{
				Object[] edges = edgeCollection.toArray();
				for(int i=0; i<edges.length; i++)
				{
					edge e=(edge) edges[i], newE;
					String source, target, type;
					source=e.getSourceName();
					target=e.getTargetName();
					type=e.getType();
					newE=new edge(target, target, source, source, type);
					reverseBDagGraph.addEdge(newE, target, source, EdgeType.DIRECTED);
				}
			}
			for(int i=0; i<diseaseNodeMetaNode.size(); i++)
			{
				ArrayList<String> path=new ArrayList<String>();
				path=DFS_path(diseaseNodeMetaNode.get(i), diseaseNodeMetaNode.get(i), null, path, "REVERSE");
				System.out.println("path: "+path.size()+" "+path.toString());
				nodePath np=new nodePath(diseaseNodeMetaNode.get(i), path);
				nodePathList.add(np);
			}
			for(int i=0; i<nodePathList.size(); i++)
				nodePathList.get(i).print();
			
			for(int i=0; i<diseaseNodeMetaNode.size(); i++)
				listOfDiseaseNodeToCover.add(diseaseNodeMetaNode.get(i));
			//find common nodes of path
			while(notDone)
			{
				//System.out.println("listOfDiseaseNodeToCover:"+listOfDiseaseNodeToCover.toString());
				if(listOfDiseaseNodeToCover.size()>1)
				{
					//System.out.println("listOfDiseaseNodeToCover>1 **************************************");
					prevConsoDiseaseNode=new ArrayList<String>();
					prevConsoDiseaseNode.add(listOfDiseaseNodeToCover.get(0));
					prevCommonMetaNodeAncestor=new ArrayList<String>();
					int index=-1;
					for(int i=0; i<nodePathList.size() && index==-1; i++)
					{
						if(listOfDiseaseNodeToCover.get(0).compareTo(nodePathList.get(i).getMetaNode())==0)
							index=i;
					}
					if(index!=-1)
					{
						prevCommonMetaNodeAncestor=nodePathList.get(index).getMetaNodeAncestors();
						listOfDiseaseNodeToCover.remove(0);
					}
					
					//System.out.println("[A]prevConsoDiseaseNode:"+prevConsoDiseaseNode.toString());
					//System.out.println("[A]prevCommonMetaNodeAncestor:"+prevCommonMetaNodeAncestor.toString());
					
					for(int i=0; i<listOfDiseaseNodeToCover.size(); i++)
					{
						currConsoDiseaseNode=new ArrayList<String>();
						currConsoDiseaseNode.add(listOfDiseaseNodeToCover.get(i));
						currCommonMetaNodeAncestor=new ArrayList<String>();
						index=-1;
						for(int j=0; j<nodePathList.size() && index==-1; j++)
						{
							if(listOfDiseaseNodeToCover.get(i).compareTo(nodePathList.get(j).getMetaNode())==0)
								index=j;
						}
						if(index!=-1)
						{
							ArrayList<String> ref=nodePathList.get(index).getMetaNodeAncestors();
							for(int k=0; k<ref.size(); k++)
								currCommonMetaNodeAncestor.add(ref.get(k));
							
							//System.out.println("currCommonMetaNodeAncestor: ["+currCommonMetaNodeAncestor.size()+"]"+currCommonMetaNodeAncestor.toString());
							currCommonMetaNodeAncestor.retainAll(prevCommonMetaNodeAncestor);
							//System.out.println("AFTER RETAIN ALL: "+currCommonMetaNodeAncestor.size()+" "+currCommonMetaNodeAncestor.toString());
							if(currCommonMetaNodeAncestor.size()>0)
							{
								prevConsoDiseaseNode.add(listOfDiseaseNodeToCover.get(i));
								prevCommonMetaNodeAncestor=new ArrayList<String>();
								for(int j=0; j<currCommonMetaNodeAncestor.size(); j++)
									prevCommonMetaNodeAncestor.add(currCommonMetaNodeAncestor.get(j));
								listOfDiseaseNodeToCover.remove(i);
								//System.out.println(i+" prevConsoDiseaseNode:"+prevConsoDiseaseNode.toString());
								//System.out.println("prevCommonMetaNodeAncestor:"+prevCommonMetaNodeAncestor.size()+" "+prevCommonMetaNodeAncestor.toString());
							}
						}
					}
					//System.out.println("prevConsoDiseaseNode:"+prevConsoDiseaseNode.toString());
					//System.out.println("prevCommonMetaNodeAncestor:"+prevCommonMetaNodeAncestor.size()+" "+prevCommonMetaNodeAncestor.toString());
					nodePathCommon npc=new nodePathCommon(prevConsoDiseaseNode, prevCommonMetaNodeAncestor);
					if(nodePathCommonList.contains(npc)==false)
						nodePathCommonList.add(npc);
				}
				else
				{
					prevConsoDiseaseNode=new ArrayList<String>();
					prevConsoDiseaseNode.add(listOfDiseaseNodeToCover.get(0));
					prevCommonMetaNodeAncestor=new ArrayList<String>();
					int index=-1;
					for(int i=0; i<nodePathList.size() && index==-1; i++)
					{
						if(listOfDiseaseNodeToCover.get(0).compareTo(nodePathList.get(i).getMetaNode())==0)
							index=i;
					}
					if(index!=-1)
					{
						prevCommonMetaNodeAncestor=nodePathList.get(index).getMetaNodeAncestors();
						nodePathCommon npc=new nodePathCommon(prevConsoDiseaseNode, prevCommonMetaNodeAncestor);
						if(nodePathCommonList.contains(npc)==false)
							nodePathCommonList.add(npc);
						listOfDiseaseNodeToCover.remove(0);
					}
				}
				if(listOfDiseaseNodeToCover.size()>0)
					notDone=true;
				else
					notDone=false;
			}
			for(int i=0; i<nodePathCommonList.size(); i++)
				nodePathCommonList.get(i).print();
		}
	}
	*/
	
	public ArrayList<String> getSccOfMetaNode(String metaNode)
	{
		int index=bG.getSCC().getGroupIndexWithGroupName(metaNode);
		if(index!=-1)
			return bG.getSCC().getItemNameList(index);
		else
			return null;
	}
	
	public String getMetaNodeOfGivenNode(String node)
	{
		return bG.getSCC().getGroupName(bG.getSCC().getGroupIndexWithItemName(node));
	}
}
