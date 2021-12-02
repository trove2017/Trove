package dialogFrames;

//Imports are listed in full to show what's being used could just import javax.swing.* and java.awt.* etc..
import graph.JUNG_graph;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
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

public class targetNodeConnectionsFrame extends JDialog{
	private JPanel comboBoxPanel, settingPanel, buttonPanel, connectionGraphPanel, connectionNodePanel, targetNodeFromPanel, targetNodeToPanel, diseaseNodeToPanel;
	private JPanel cardLayoutPanel;
	private JComboBox<String> nodeToComboBox=new JComboBox<String>();
	private JButton cancelButton;
	private JList<String> targetNodeFromList, targetNodeToList, diseaseNodeToList, connectionNodeList;
	private JScrollPane targetNodeFromScrollPane, targetNodeToScrollPane, diseaseNodeToScrollPane, connectionScrollPane;
	private ArrayList<String> listOfTargetNodeFrom=new ArrayList<String>();
	private ArrayList<String> listOfTargetNodeTo=new ArrayList<String>();
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
	private String selectedTargetNodeFrom, selectedTargetNodeTo, selectedDiseaseNodeTo;
	private ArrayList<String> connectionList=new ArrayList<String>();
	private Node previousSelectedNode;
	private float previousSelectedNodeSize;
	private float previousSelectedNodeR, previousSelectedNodeG, previousSelectedNodeB;
	private JLabel statusLabel;
	private String TARGETNODE_TARGETNODE_VIEW="View target node to target node pathway connections";
	private String TARGETNODE_DISEASENODE_VIEW="View target node to disease node pathway connections";

	public targetNodeConnectionsFrame(JLabel l, ArrayList<String> selectedTargetNode, ArrayList<String> diseaseNode, JUNG_graph jGraph)
	{
		//initialize constants
		listOfTargetNodeFrom=selectedTargetNode;
		for(int i=0; i<listOfTargetNodeFrom.size(); i++)
			listOfTargetNodeTo.add(listOfTargetNodeFrom.get(i));
		listOfDiseaseNodeTo=diseaseNode;
		CANCEL=staticString.getCancel();
		originalJGraph=jGraph;
		previousSelectedNode=null;
		statusLabel=l;

		//set JDialog properties
		setTitle("Visualize Disease Node Neighbourhood");
		setSize(1050,550);
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

	private void colorAndResizeTargetNode(ArrayList<Double> nodeValue, int i, String node)
	{
		nGraph.getNode(node).getNodeData().setColor(0.0f, 1.0f, 0.0f); //green
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
		for(int i=0; i<listOfTargetNodeFrom.size(); i++)
			val.add(Double.valueOf(1));
		for(int i=0; i<listOfTargetNodeFrom.size(); i++)
			colorAndResizeTargetNode(val, i, listOfTargetNodeFrom.get(i));

		if(listOfDiseaseNodeTo!=null)
		{
			val=new ArrayList<Double>();
			for(int i=0; i<listOfDiseaseNodeTo.size(); i++)
				val.add(Double.valueOf(1));
			for(int i=0; i<listOfDiseaseNodeTo.size(); i++)
			{
				System.out.println("diseaseNode:"+listOfDiseaseNodeTo.get(i));
				colorAndResizeDiseaseNode(val, i, listOfDiseaseNodeTo.get(i));
			}
		}

		val=new ArrayList<Double>();
		for(Node n: nGraph.getNodes())
		{
			if(listOfTargetNodeFrom.contains(n.getNodeData().getLabel())==false)
				if(listOfDiseaseNodeTo==null || (listOfDiseaseNodeTo!=null && listOfDiseaseNodeTo.contains(n.getNodeData().getLabel())==false))
					val.add(Double.valueOf(1));
		}
		int i=0;
		for(Node n: nGraph.getNodes())
		{
			if(listOfTargetNodeFrom.contains(n.getNodeData().getLabel())==false)
			{
				if(listOfDiseaseNodeTo==null || (listOfDiseaseNodeTo!=null && listOfDiseaseNodeTo.contains(n.getNodeData().getLabel())==false))
				{
					colorAndResizeOtherNode(val, i, n.getNodeData().getLabel());
					i++;
				}
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
		ArrayList<stringArray> nodePairList=new ArrayList<stringArray>();
		ArrayList<String> nodeList=new ArrayList<String>();
		for(int i=0; i<listOfTargetNodeFrom.size(); i++)
			nodeList.add(listOfTargetNodeFrom.get(i));
		if(listOfDiseaseNodeTo!=null)
		{
			for(int i=0; i<listOfDiseaseNodeTo.size(); i++)
				nodeList.add(listOfDiseaseNodeTo.get(i));
		}

		for(int i=0; i<nodeList.size(); i++)
		{
			for(int j=i+1; j<nodeList.size(); j++)
			{
				stringArray a=new stringArray(2);
				a.setStringArrayAtIndex(0, nodeList.get(i));
				a.setStringArrayAtIndex(1, nodeList.get(j));
				nodePairList.add(a);
			}
		}

		for(int i=0; i<nodePairList.size(); i++)
		{
			JLabel s=new JLabel();
			ArrayList<String> nodePair=new ArrayList<String>();
			nodePair.add(nodePairList.get(i).getStringArrayAtIndex(0));
			nodePair.add(nodePairList.get(i).getStringArrayAtIndex(1));
			//ArrayList<String> thisConnection=originalJGraph.findConnectionOfCurrentGraph(s, nodePair);
			ArrayList<String> thisConnection=originalJGraph.findConnectionOfCurrentGraphNodeList(s, nodePair);
			for(int j=0; j<thisConnection.size(); j++)
			{
				if(connectingNode.contains(thisConnection.get(j))==false)
					connectingNode.add(thisConnection.get(j));
			}
		}
		for(int i=0; i<listOfTargetNodeFrom.size(); i++)
		{
			if(connectingNode.contains(listOfTargetNodeFrom.get(i))==false)
				connectingNode.add(listOfTargetNodeFrom.get(i));
		}
		if(listOfDiseaseNodeTo!=null)
		{
			for(int i=0; i<listOfDiseaseNodeTo.size(); i++)
			{
				if(connectingNode.contains(listOfDiseaseNodeTo.get(i))==false)
					connectingNode.add(listOfDiseaseNodeTo.get(i));
			}
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
		comboBoxPanel=new JPanel();
		cardLayoutPanel=new JPanel(new CardLayout());

		//create comboBoxPanel
		if(listOfTargetNodeFrom.size()>1)
			nodeToComboBox.addItem(TARGETNODE_TARGETNODE_VIEW);
		if(listOfDiseaseNodeTo!=null)
			nodeToComboBox.addItem(TARGETNODE_DISEASENODE_VIEW);
		if(listOfTargetNodeFrom.size()>1)
			nodeToComboBox.setSelectedItem(TARGETNODE_TARGETNODE_VIEW);
		nodeToComboBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e){
				Object item=e.getItem();
				if(e.getStateChange()==ItemEvent.SELECTED)
				{
					if(String.valueOf(item).compareTo(TARGETNODE_TARGETNODE_VIEW)==0)
					{
						CardLayout cl=(CardLayout)(cardLayoutPanel.getLayout());
						cl.show(cardLayoutPanel, TARGETNODE_TARGETNODE_VIEW);
					}
					else
					{
						CardLayout cl=(CardLayout)(cardLayoutPanel.getLayout());
						cl.show(cardLayoutPanel, TARGETNODE_DISEASENODE_VIEW);
					}
				}
			}
		});
		comboBoxPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		comboBoxPanel.add(nodeToComboBox);

		//create targetNodeFromPanel
		targetNodeFromPanel=new JPanel();
		targetNodeFromPanel.setLayout(new BoxLayout(targetNodeFromPanel, BoxLayout.Y_AXIS));
		JLabel targetNodeFromLabel=new JLabel();
		targetNodeFromLabel.setText("Target Node List (From)");
		JPanel targetNodeFromLabelPanel=new JPanel();
		targetNodeFromLabelPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		targetNodeFromLabelPanel.add(targetNodeFromLabel);
		targetNodeFromList=new JList<String>();
		targetNodeFromScrollPane=new JScrollPane();
		targetNodeFromList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		targetNodeFromList.addListSelectionListener(new ListSelectionListener() 
		{
			public void valueChanged(ListSelectionEvent e){targetNodeList_valueChanged(e);}
		});
		String[] javaArr=listOfTargetNodeFrom.toArray(new String[listOfTargetNodeFrom.size()]);
		targetNodeFromList.setListData(javaArr);
		targetNodeFromScrollPane = new JScrollPane(targetNodeFromList);
		targetNodeFromScrollPane.setViewportView(targetNodeFromList);
		targetNodeFromScrollPane.setPreferredSize(new Dimension(150, 380));
		targetNodeFromScrollPane.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		targetNodeFromPanel.add(targetNodeFromLabelPanel);
		targetNodeFromPanel.add(targetNodeFromScrollPane);

		//create targetNodeToPanel
		targetNodeToPanel=new JPanel();
		targetNodeToPanel.setLayout(new BoxLayout(targetNodeToPanel, BoxLayout.Y_AXIS));
		JLabel targetNodeToLabel=new JLabel();
		targetNodeToLabel.setText("Target Node List (To)");
		JPanel targetNodeToLabelPanel=new JPanel();
		targetNodeToLabelPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		targetNodeToLabelPanel.add(targetNodeToLabel);
		targetNodeToList=new JList<String>();
		targetNodeToScrollPane=new JScrollPane();
		targetNodeToList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		targetNodeToList.addListSelectionListener(new ListSelectionListener() 
		{
			public void valueChanged(ListSelectionEvent e){targetNodeList_valueChanged(e);}
		});
		String[] javaArr1=listOfTargetNodeTo.toArray(new String[listOfTargetNodeTo.size()]);
		targetNodeToList.setListData(javaArr1);
		targetNodeToScrollPane = new JScrollPane(targetNodeToList);
		targetNodeToScrollPane.setViewportView(targetNodeToList);
		targetNodeToScrollPane.setPreferredSize(new Dimension(150, 380));
		targetNodeToScrollPane.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		targetNodeToPanel.add(targetNodeToLabelPanel);
		targetNodeToPanel.add(targetNodeToScrollPane);
		cardLayoutPanel.add(targetNodeToPanel, TARGETNODE_TARGETNODE_VIEW);

		//create diseaseNodeToPanel
		if(listOfDiseaseNodeTo!=null)
		{
			diseaseNodeToPanel=new JPanel();
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
				public void valueChanged(ListSelectionEvent e){targetNodeList_valueChanged(e);}
			});
			String[] javaArr2=listOfDiseaseNodeTo.toArray(new String[listOfDiseaseNodeTo.size()]);
			diseaseNodeToList.setListData(javaArr2);
			diseaseNodeToScrollPane = new JScrollPane(diseaseNodeToList);
			diseaseNodeToScrollPane.setViewportView(diseaseNodeToList);
			diseaseNodeToScrollPane.setPreferredSize(new Dimension(150, 380));
			diseaseNodeToScrollPane.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
			diseaseNodeToPanel.add(diseaseNodeToLabelPanel);
			diseaseNodeToPanel.add(diseaseNodeToScrollPane);
			cardLayoutPanel.add(diseaseNodeToPanel, TARGETNODE_DISEASENODE_VIEW);
		}

		if(listOfTargetNodeFrom.size()>1)
		{
			CardLayout cl=(CardLayout)(cardLayoutPanel.getLayout());
			cl.show(cardLayoutPanel, TARGETNODE_TARGETNODE_VIEW);
		}
		else if(listOfDiseaseNodeTo!=null)
		{
			CardLayout cl=(CardLayout)(cardLayoutPanel.getLayout());
			cl.show(cardLayoutPanel, TARGETNODE_TARGETNODE_VIEW);
		}

		connectionGraphPanel=new JPanel();
		connectionGraphPanel.setBackground(Color.BLACK);
		connectionGraphPanel.setPreferredSize(new Dimension(550, 410));

		//create connectionNodePanel
		connectionNodePanel=new JPanel();
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
		String[] javaArr3=connectionList.toArray(new String[connectionList.size()]);
		connectionNodeList.setListData(javaArr3);
		connectionScrollPane = new JScrollPane(connectionNodeList);
		connectionScrollPane.setViewportView(connectionNodeList);
		connectionScrollPane.setPreferredSize(new Dimension(150, 380));
		connectionScrollPane.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		connectionNodePanel.add(connectionNodeLabelPanel);
		connectionNodePanel.add(connectionScrollPane);

		settingPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		settingPanel.add(targetNodeFromPanel);
		settingPanel.add(cardLayoutPanel);
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
		add(comboBoxPanel);
		add(settingPanel);
		add(buttonPanel);
	}

	private void visualizeSelectedTargetNodeConnectionsInGraph(String fromNode, String toNode)
	{
		//Preview configuration
		nGraph.writeLock();
		connectionList=new ArrayList<String>();
		ArrayList<String> nodePair=new ArrayList<String>();
		nodePair.add(fromNode);
		nodePair.add(toNode);
		//ArrayList<String> thisConnection=originalJGraph.findConnectionOfCurrentGraph(statusLabel, nodePair);
		ArrayList<String> thisConnection=originalJGraph.findConnectionOfCurrentGraphNodeList(statusLabel, nodePair);

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
					if(listOfTargetNodeFrom.contains(n.getNodeData().getLabel())==true)
						n.getNodeData().setColor(0.0f, 1.0f, 1.0f); //cyan
					else if(listOfDiseaseNodeTo!=null && listOfDiseaseNodeTo.contains(n.getNodeData().getLabel())==true)
						n.getNodeData().setColor(1.0f, 1.0f, 0.0f); //yellow
					else	
						n.getNodeData().setColor(1.0f, 0.5f, 0.0f); //orange
					n.getNodeData().setSize(10); 
				}
				else
				{
					if(listOfTargetNodeFrom.contains(n.getNodeData().getLabel())==true)
						n.getNodeData().setColor(0.0f, 1.0f, 1.0f); //cyan
					else if(listOfDiseaseNodeTo!=null && listOfDiseaseNodeTo.contains(n.getNodeData().getLabel())==true)
						n.getNodeData().setColor(1.0f, 1.0f, 0.0f); //yellow
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

	private void targetNodeList_valueChanged(ListSelectionEvent e) 
	{
		if (!e.getValueIsAdjusting()) 
		{
			if(nodeToComboBox.getSelectedIndex()==0 && targetNodeFromList.getSelectedValue()!=null && targetNodeToList.getSelectedValue()!=null)
			{
				selectedTargetNodeFrom = targetNodeFromList.getSelectedValue().toString();
				selectedTargetNodeTo = targetNodeToList.getSelectedValue().toString();
				System.out.println(selectedTargetNodeFrom+"->"+selectedTargetNodeTo);
				visualizeSelectedTargetNodeConnectionsInGraph(selectedTargetNodeFrom, selectedTargetNodeTo);
			}
			if(nodeToComboBox.getSelectedIndex()==1 && targetNodeFromList.getSelectedValue()!=null && diseaseNodeToList.getSelectedValue()!=null)
			{
				selectedTargetNodeFrom = targetNodeFromList.getSelectedValue().toString();
				selectedDiseaseNodeTo = diseaseNodeToList.getSelectedValue().toString();
				System.out.println(selectedTargetNodeFrom+"->"+selectedDiseaseNodeTo);
				visualizeSelectedTargetNodeConnectionsInGraph(selectedTargetNodeFrom, selectedDiseaseNodeTo);
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
