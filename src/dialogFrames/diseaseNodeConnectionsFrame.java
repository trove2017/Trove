package dialogFrames;

//Imports are listed in full to show what's being used could just import javax.swing.* and java.awt.* etc..
import graph.JUNG_graph;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.gephi.graph.api.GraphController;
import org.gephi.graph.api.GraphModel;
import org.gephi.graph.api.MixedGraph;
import org.gephi.graph.api.Node;
import org.gephi.layout.api.LayoutController;
import org.gephi.layout.plugin.labelAdjust.LabelAdjust;
import org.gephi.layout.plugin.labelAdjust.LabelAdjustBuilder;
import org.gephi.preview.api.ManagedRenderer;
import org.gephi.preview.api.PreviewController;
import org.gephi.preview.api.PreviewModel;
import org.gephi.preview.api.PreviewProperty;
import org.gephi.preview.api.ProcessingTarget;
import org.gephi.preview.api.RenderTarget;
import org.gephi.preview.types.DependantOriginalColor;
import org.gephi.project.api.ProjectController;
import org.gephi.project.api.Workspace;
import org.openide.util.Lookup;

import processing.core.PApplet;

import constants.stringConstant;
import dataType.stringArray;

@SuppressWarnings("serial")
//	Layout: 
//  |  - settingPanel                                             
//  |    + diseaseNodePanel                                        
//  |      * diseaseNodeList, diseaseNodeScrollPane                        
//  |  - buttonPanel
//  |    + cancelButton

public class diseaseNodeConnectionsFrame extends JDialog{
	private JPanel settingPanel, buttonPanel, connectionGraphPanel;
	private JButton cancelButton;
	private JList<String> diseaseNodeFromList, diseaseNodeToList, connectionNodeList;
	private JScrollPane diseaseNodeFromScrollPane, diseaseNodeToScrollPane, connectionScrollPane;
	private ArrayList<String> listOfDiseaseNodeFrom=new ArrayList<String>();
	private ArrayList<String> listOfDiseaseNodeTo=new ArrayList<String>();
	private JUNG_graph originalJGraph; //for visualization
	private String CANCEL;

	private ProjectController nGraphPC;
	private Workspace nGraphWorkspace, workspace1;
	private GraphModel nGraphModel;
	private PreviewModel nGraphPreviewModel;
	private MixedGraph nGraph;
	private LayoutController nGraphLayoutController;
	private PreviewController nGraphPreviewController;
	private ProcessingTarget nGraphTarget;
	private PApplet nGraphApplet;

	private stringConstant staticString=new stringConstant();
	private String selectedDiseaseNodeFrom, selectedDiseaseNodeTo;
	private ArrayList<String> connectionList=new ArrayList<String>();
	private Node previousSelectedNode;
	private float previousSelectedNodeSize;
	private float previousSelectedNodeR, previousSelectedNodeG, previousSelectedNodeB;
	private JLabel statusLabel;

	public diseaseNodeConnectionsFrame(JLabel l, ArrayList<String> selectedDiseaseNode, JUNG_graph jGraph)
	{
		//initialize constants
		listOfDiseaseNodeFrom=selectedDiseaseNode;
		for(int i=0; i<listOfDiseaseNodeFrom.size(); i++)
			listOfDiseaseNodeTo.add(listOfDiseaseNodeFrom.get(i));
		CANCEL=staticString.getCancel();
		originalJGraph=jGraph;
		previousSelectedNode=null;
		statusLabel=l;

		//set JDialog properties
		setTitle("Visualize Disease Node Neighbourhood");
		setSize(1050,500);
		setLocationRelativeTo(null);
		setLayout(new BorderLayout());
		setResizable(true);
		setModal(true);

		//configure components in JDialog
		initializeComponents();
		generateGraph();
		configureGraphPreview();
		layoutGraph_labelAdjust();
		visualizeGraph(connectionGraphPanel);
	}

	private ActionListener actionListener = new ActionListener() 
	{
		public void actionPerformed(ActionEvent act) {
			if (act.getActionCommand() == CANCEL)
				cancel_actionPerformed();
		}
	};

	private void configureGraphPreview()
	{
		//Preview configuration
		nGraph.writeLock();
		nGraphPreviewModel.getProperties().putValue(PreviewProperty.SHOW_NODE_LABELS, Boolean.TRUE);
		nGraphPreviewModel.getProperties().putValue(PreviewProperty.NODE_LABEL_COLOR, new DependantOriginalColor(Color.WHITE));
		nGraphPreviewModel.getProperties().putValue(PreviewProperty.EDGE_CURVED, Boolean.FALSE);
		nGraphPreviewModel.getProperties().putValue(PreviewProperty.EDGE_OPACITY, 50);
		nGraphPreviewModel.getProperties().putValue(PreviewProperty.EDGE_RADIUS, 0.01f);
		nGraphPreviewModel.getProperties().putValue(PreviewProperty.BACKGROUND_COLOR, Color.BLACK);
		nGraphPreviewModel.getProperties().putValue(PreviewProperty.DIRECTED, Boolean.TRUE);
		nGraphPreviewModel.getProperties().putValue(PreviewProperty.NODE_BORDER_WIDTH, 0.01);
		//interactivePreviewModel.getProperties().putValue(PreviewProperty.VISIBILITY_RATIO, 50);
		nGraphPreviewModel.getProperties().putValue(PreviewProperty.ARROW_SIZE, 5);
		nGraph.writeUnlock();
		nGraphPreviewController.refreshPreview();
		System.out.println("nGraph preview done");
	}

	private void colorAndResizeDiseaseNode(ArrayList<Double> nodeValue, int i, String node)
	{
		nGraph.getNode(node).getNodeData().setColor(0.0f, 1.0f, 1.0f); //yellow
		nGraph.getNode(node).getNodeData().setAlpha(1.0f); //opaque
		nGraph.getNode(node).getNodeData().setSize(25);
	}

	private void colorAndResizeOtherNode(ArrayList<Double> nodeValue, int i, String node)
	{
		nGraph.getNode(node).getNodeData().setColor(0.75f, 0.75f, 0.75f); //gray
		nGraph.getNode(node).getNodeData().setAlpha(1.0f); //opaque
		nGraph.getNode(node).getNodeData().setSize(10);
	}

	private void visualizeGraph(JPanel nGraphPanel)
	{
		//New Processing target, get the PApplet
		nGraphTarget = (ProcessingTarget) nGraphPreviewController.getRenderTarget(RenderTarget.PROCESSING_TARGET);
		nGraphApplet = nGraphTarget.getApplet();
		nGraphPanel.add(nGraphApplet);
		nGraph.writeLock();
		nGraphApplet.init();
		nGraphApplet.setPreferredSize(new Dimension(connectionGraphPanel.getWidth(),connectionGraphPanel.getHeight()));
		nGraphApplet.setSize(new Dimension(connectionGraphPanel.getWidth(),connectionGraphPanel.getHeight()));
		nGraph.writeUnlock();
		System.out.println("nGraph Applet done");

		//**previewMouse**

		ManagedRenderer [] mr = nGraphPreviewModel.getManagedRenderers();
		nGraphPreviewModel.setManagedRenderers(mr);
		/*mouseRenderer.needsPreviewMouseListener(mouseListener);
        mr2[mr.length] = new ManagedRenderer(mouseRenderer, true);
        interactivePreviewModel.setManagedRenderers(mr2);
		 */
		ArrayList<Double> val=new ArrayList<Double>();
		for(int i=0; i<listOfDiseaseNodeFrom.size(); i++)
			val.add(Double.valueOf(1));
		for(int i=0; i<listOfDiseaseNodeFrom.size(); i++)
			colorAndResizeDiseaseNode(val, i, listOfDiseaseNodeFrom.get(i));

		val=new ArrayList<Double>();
		for(Node n: nGraph.getNodes())
		{
			if(listOfDiseaseNodeFrom.contains(n.getNodeData().getLabel())==false)
				val.add(Double.valueOf(1));
		}
		int i=0;
		for(Node n: nGraph.getNodes())
		{
			if(listOfDiseaseNodeFrom.contains(n.getNodeData().getLabel())==false)
			{
				colorAndResizeOtherNode(val, i, n.getNodeData().getLabel());
				i++;
			}
		}

		//Refresh the preview and reset the zoom
		nGraphPreviewController.refreshPreview();
		nGraphPanel.repaint();
		nGraphTarget.refresh();
		nGraphTarget.resetZoom();
	}

	public void layoutGraph_labelAdjust()
	{
		System.out.println("doing layout (label adjust)");
		LabelAdjustBuilder labelAdjustLayoutBuilder=Lookup.getDefault().lookup(LabelAdjustBuilder.class);
		LabelAdjust layout=labelAdjustLayoutBuilder.buildLayout();
		layout.resetPropertiesValues();
		layout.setAdjustBySize(true);
		layout.setGraphModel(nGraphModel);
		nGraphLayoutController=Lookup.getDefault().lookup(LayoutController.class);
		nGraphLayoutController.setLayout(layout);
		nGraphLayoutController.executeLayout();
		System.out.println("layout done");
	}

	private void generateGraph()
	{
		nGraphPC=Lookup.getDefault().lookup(ProjectController.class);
		workspace1 = nGraphPC.getCurrentWorkspace();
		nGraphWorkspace = nGraphPC.duplicateWorkspace(workspace1);
		nGraphPC.openWorkspace(nGraphWorkspace);       //Set as current workspace

		nGraphModel=Lookup.getDefault().lookup(GraphController.class).getModel();
		nGraph=nGraphModel.getMixedGraph();

		nGraphPreviewController = Lookup.getDefault().lookup(PreviewController.class);
		nGraphPreviewModel = nGraphPreviewController.getModel();
		nGraph.writeLock();
		nGraphPreviewModel.getProperties().putValue("MultiColourNode", Boolean.FALSE);
		nGraphPreviewModel.getProperties().putValue(PreviewProperty.NODE_OPACITY, 100);
		nGraph.writeUnlock();

		ArrayList<String> connectingNode=new ArrayList<String>();
		ArrayList<stringArray> diseaseNodePairList=new ArrayList<stringArray>();
		for(int i=0; i<listOfDiseaseNodeFrom.size(); i++)
		{
			for(int j=i+1; j<listOfDiseaseNodeFrom.size(); j++)
			{
				stringArray a=new stringArray(2);
				a.setStringArrayAtIndex(0, listOfDiseaseNodeFrom.get(i));
				a.setStringArrayAtIndex(1, listOfDiseaseNodeFrom.get(j));
				diseaseNodePairList.add(a);
			}
		}

		for(int i=0; i<diseaseNodePairList.size(); i++)
		{
			JLabel s=new JLabel();
			ArrayList<String> nodePair=new ArrayList<String>();
			nodePair.add(diseaseNodePairList.get(i).getStringArrayAtIndex(0));
			nodePair.add(diseaseNodePairList.get(i).getStringArrayAtIndex(1));
			ArrayList<String> thisConnection=originalJGraph.findConnectionOfCurrentGraph(s, nodePair);
			for(int j=0; j<thisConnection.size(); j++)
			{
				if(connectingNode.contains(thisConnection.get(j))==false)
					connectingNode.add(thisConnection.get(j));
			}
		}
		for(int i=0; i<listOfDiseaseNodeFrom.size(); i++)
		{
			if(connectingNode.contains(listOfDiseaseNodeFrom.get(i))==false)
				connectingNode.add(listOfDiseaseNodeFrom.get(i));
		}
		
		System.out.println("connectingNode:");
		System.out.println(connectingNode.toString());
		ArrayList<String> removeList=new ArrayList<String>();
		for(Node n : nGraph.getNodes()) {
			if(connectingNode.contains(n.getNodeData().getLabel())==false)
				removeList.add(n.getNodeData().getLabel());
		}
		for(int i=0; i<removeList.size(); i++)
		{
			Node n1=nGraph.getNode(removeList.get(i));
			nGraph.writeLock();
			nGraph.removeNode(n1);
			nGraph.writeUnlock();
		}
	}

	private void initializeComponents()
	{
		settingPanel=new JPanel();
		buttonPanel=new JPanel();

		//create diseaseNodeFromPanel
		JPanel diseaseNodeFromPanel=new JPanel();
		diseaseNodeFromPanel.setLayout(new BoxLayout(diseaseNodeFromPanel, BoxLayout.Y_AXIS));
		JLabel diseaseNodeFromLabel=new JLabel();
		diseaseNodeFromLabel.setText("Disease Node List (From)");
		JPanel diseaseNodeFromLabelPanel=new JPanel();
		diseaseNodeFromLabelPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		diseaseNodeFromLabelPanel.add(diseaseNodeFromLabel);
		diseaseNodeFromList=new JList<String>();
		diseaseNodeFromScrollPane=new JScrollPane();
		diseaseNodeFromList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		diseaseNodeFromList.addListSelectionListener(new ListSelectionListener() 
		{
			public void valueChanged(ListSelectionEvent e){diseaseNodeList_valueChanged(e);}
		});
		String[] javaArr=listOfDiseaseNodeFrom.toArray(new String[listOfDiseaseNodeFrom.size()]);
		diseaseNodeFromList.setListData(javaArr);
		diseaseNodeFromScrollPane = new JScrollPane(diseaseNodeFromList);
		diseaseNodeFromScrollPane.setViewportView(diseaseNodeFromList);
		diseaseNodeFromScrollPane.setPreferredSize(new Dimension(150, 380));
		diseaseNodeFromScrollPane.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		diseaseNodeFromPanel.add(diseaseNodeFromLabelPanel);
		diseaseNodeFromPanel.add(diseaseNodeFromScrollPane);

		//create diseaseNodeToPanel
		JPanel diseaseNodeToPanel=new JPanel();
		diseaseNodeToPanel.setLayout(new BoxLayout(diseaseNodeToPanel, BoxLayout.Y_AXIS));
		JLabel diseaseNodeToLabel=new JLabel();
		diseaseNodeToLabel.setText("Disease Node List (To)");
		JPanel diseaseNodeToLabelPanel=new JPanel();
		diseaseNodeToLabelPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		diseaseNodeToLabelPanel.add(diseaseNodeToLabel);
		diseaseNodeToList=new JList<String>();
		diseaseNodeToScrollPane=new JScrollPane();
		diseaseNodeToList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		diseaseNodeToList.addListSelectionListener(new ListSelectionListener() 
		{
			public void valueChanged(ListSelectionEvent e){diseaseNodeList_valueChanged(e);}
		});
		String[] javaArr1=listOfDiseaseNodeTo.toArray(new String[listOfDiseaseNodeTo.size()]);
		diseaseNodeToList.setListData(javaArr1);
		diseaseNodeToScrollPane = new JScrollPane(diseaseNodeToList);
		diseaseNodeToScrollPane.setViewportView(diseaseNodeToList);
		diseaseNodeToScrollPane.setPreferredSize(new Dimension(150, 380));
		diseaseNodeToScrollPane.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		diseaseNodeToPanel.add(diseaseNodeToLabelPanel);
		diseaseNodeToPanel.add(diseaseNodeToScrollPane);

		connectionGraphPanel=new JPanel();
		connectionGraphPanel.setBackground(Color.BLACK);
		connectionGraphPanel.setPreferredSize(new Dimension(550, 410));

		//create connectionNodePanel
		JPanel connectionNodePanel=new JPanel();
		connectionNodePanel.setLayout(new BoxLayout(connectionNodePanel, BoxLayout.Y_AXIS));
		JLabel connectionNodeLabel=new JLabel();
		connectionNodeLabel.setText("Nodes in connection path:");
		JPanel connectionNodeLabelPanel=new JPanel();
		connectionNodeLabelPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		connectionNodeLabelPanel.add(connectionNodeLabel);
		connectionNodeList=new JList<String>();
		connectionScrollPane=new JScrollPane();
		connectionNodeList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		connectionNodeList.addListSelectionListener(new ListSelectionListener() 
		{
			public void valueChanged(ListSelectionEvent e){connectionNodeList_valueChanged(e);}
		});
		String[] javaArr2=connectionList.toArray(new String[connectionList.size()]);
		connectionNodeList.setListData(javaArr2);
		connectionScrollPane = new JScrollPane(connectionNodeList);
		connectionScrollPane.setViewportView(connectionNodeList);
		connectionScrollPane.setPreferredSize(new Dimension(150, 380));
		connectionScrollPane.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		connectionNodePanel.add(connectionNodeLabelPanel);
		connectionNodePanel.add(connectionScrollPane);

		settingPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		settingPanel.add(diseaseNodeFromPanel);
		settingPanel.add(diseaseNodeToPanel);
		settingPanel.add(connectionGraphPanel);
		settingPanel.add(connectionNodePanel);

		//create buttonPanel
		cancelButton= new JButton();
		cancelButton.setSize(50, 20);
		cancelButton.setText(CANCEL);
		cancelButton.setActionCommand(CANCEL);
		cancelButton.addActionListener(actionListener);
		buttonPanel= new JPanel();
		buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
		buttonPanel.add(cancelButton);

		setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));
		add(settingPanel);
		add(buttonPanel);
	}

	private void visualizeSelectedDiseaseNodeConnectionsInGraph(String fromNode, String toNode)
	{
		//Preview configuration
		nGraph.writeLock();
		connectionList=new ArrayList<String>();
		ArrayList<String> nodePair=new ArrayList<String>();
		nodePair.add(fromNode);
		nodePair.add(toNode);
		ArrayList<String> thisConnection=originalJGraph.findConnectionOfCurrentGraph(statusLabel, nodePair);

		for(Node n: nGraph.getNodes())
		{
			if(fromNode.compareTo(n.getNodeData().getLabel())==0 || toNode.compareTo(n.getNodeData().getLabel())==0) //selected disease node (red, opaque)
			{
				n.getNodeData().setColor(1.0f, 0.0f, 0.0f); //red
				n.getNodeData().setSize(25); 
			}
			else
			{
				if(thisConnection.contains(n.getNodeData().getLabel())==true)
				{
					connectionList.add(n.getNodeData().getLabel());
					if(listOfDiseaseNodeFrom.contains(n.getNodeData().getLabel())==true) //non-selected disease node (cyan, transparent)
						n.getNodeData().setColor(0.0f, 1.0f, 1.0f); //cyan
					else
						n.getNodeData().setColor(1.0f, 1.0f, 0.0f); //orange
					n.getNodeData().setSize(10); 
				}
				else
				{
					if(listOfDiseaseNodeFrom.contains(n.getNodeData().getLabel())==true) //non-selected disease node (cyan, transparent)
						n.getNodeData().setColor(0.0f, 1.0f, 1.0f); //cyan
					else
						n.getNodeData().setColor(0.75f, 0.75f, 0.75f); //gray
					n.getNodeData().setSize(2);
				}
			}
		}
		String[] javaArr2=connectionList.toArray(new String[connectionList.size()]);
		connectionNodeList.setListData(javaArr2);
		
		nGraph.writeUnlock();
		connectionGraphPanel.repaint();
		nGraphTarget.refresh();
		nGraphPreviewController.refreshPreview();
		System.out.println("nGraph preview done");
	}
	
	private void visualizeSelectedConnectionNodeInGraph(String selectedNode)
	{
		//Preview configuration
		nGraph.writeLock();
		Node n=nGraph.getNode(selectedNode);
		if(previousSelectedNode!=null)
		{
			previousSelectedNode.getNodeData().setColor(previousSelectedNodeR, previousSelectedNodeG, previousSelectedNodeB); 
			previousSelectedNode.getNodeData().setSize(previousSelectedNodeSize); 
		}
		if(n!=null)
		{
			previousSelectedNode=n;
			previousSelectedNodeR=n.getNodeData().r();
			previousSelectedNodeG=n.getNodeData().g();
			previousSelectedNodeB=n.getNodeData().b();
			previousSelectedNodeSize=n.getNodeData().getSize();
			n.getNodeData().setColor(0.0f, 1.0f, 0.0f); //green
			n.getNodeData().setSize(25); 
		}
		
		nGraph.writeUnlock();
		nGraphTarget.refresh();
		nGraphPreviewController.refreshPreview();
	}

	private void diseaseNodeList_valueChanged(ListSelectionEvent e) 
	{
		if (!e.getValueIsAdjusting()) 
		{
			if(diseaseNodeFromList.getSelectedValue()!=null && diseaseNodeToList.getSelectedValue()!=null)
			{
				selectedDiseaseNodeFrom = diseaseNodeFromList.getSelectedValue().toString();
				selectedDiseaseNodeTo = diseaseNodeToList.getSelectedValue().toString();
				System.out.println(selectedDiseaseNodeFrom+"->"+selectedDiseaseNodeTo);
				visualizeSelectedDiseaseNodeConnectionsInGraph(selectedDiseaseNodeFrom, selectedDiseaseNodeTo);
			}
		}
	}

	private void connectionNodeList_valueChanged(ListSelectionEvent e) 
	{
		if (!e.getValueIsAdjusting()) 
			if(connectionNodeList.getSelectedValue()!=null)
				visualizeSelectedConnectionNodeInGraph(connectionNodeList.getSelectedValue());
	}
	
	private void cancel_actionPerformed(){
		nGraphPC.openWorkspace(workspace1);       //Set as current workspace
		setVisible(false);
	}
}
