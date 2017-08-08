package dialogFrames;

//Imports are listed in full to show what's being used could just import javax.swing.* and java.awt.* etc..
import graph.Gephi_graph;

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
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.gephi.graph.api.Edge;
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

@SuppressWarnings("serial")
//	Layout: 
//  |  - settingPanel                                             
//  |    + diseaseNodePanel                                        
//  |      * diseaseNodeList, diseaseNodeScrollPane                        
//  |  - buttonPanel
//  |    + cancelButton

public class targetNodeNeighbourhoodFrame extends JDialog{
	private JPanel settingPanel, buttonPanel, neighbourPanel, neighbourhoodGraphPanel;
	private JButton cancelButton;
	private JList<String> targetNodeList;
	private JTextArea neighbourTextArea;
	private JScrollPane targetNodeScrollPane, neighbourScrollPane;
	private ArrayList<String> listOfTargetNode=new ArrayList<String>();
	private Gephi_graph originalGraph; //for visualization
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
	private String selectedTargetNode;
	private String neighbour;
	private ArrayList<String> neighbourList;
	private ArrayList<String> listOfDiseaseNode=new ArrayList<String>();
	
	public targetNodeNeighbourhoodFrame(ArrayList<String> selectedTargetNode, ArrayList<String> diseaseNode, Gephi_graph graph)
	{
		//initialize constants
		listOfTargetNode=selectedTargetNode;
		CANCEL=staticString.getCancel();
		originalGraph=graph;
		listOfDiseaseNode=diseaseNode;
		
		//set JDialog properties
		setTitle("Visualize Combination Target Node Neighbourhood");
		setSize(800,650);
		setLocationRelativeTo(null);
		setLayout(new BorderLayout());
		setResizable(true);
		setModal(true);

		//configure components in JDialog
		initializeComponents();
		generateGraph();
		configureGraphPreview();
		layoutGraph_labelAdjust();
		visualizeGraph(neighbourhoodGraphPanel);
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
		nGraph.getNode(node).getNodeData().setSize(15);
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
	    nGraphApplet.setPreferredSize(new Dimension(neighbourhoodGraphPanel.getWidth(),neighbourhoodGraphPanel.getHeight()));
	    nGraphApplet.setSize(new Dimension(neighbourhoodGraphPanel.getWidth(),neighbourhoodGraphPanel.getHeight()));
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
        for(int i=0; i<listOfTargetNode.size(); i++)
        	val.add(Double.valueOf(1));
        for(int i=0; i<listOfTargetNode.size(); i++)
			colorAndResizeTargetNode(val, i, listOfTargetNode.get(i));
        
        if(listOfDiseaseNode!=null)
        {
        	val=new ArrayList<Double>();
        	for(int i=0; i<listOfDiseaseNode.size(); i++)
        		val.add(Double.valueOf(1));
        	for(int i=0; i<listOfDiseaseNode.size(); i++)
        		colorAndResizeDiseaseNode(val, i, listOfDiseaseNode.get(i));
        }
        val=new ArrayList<Double>();
        for(Node n: nGraph.getNodes())
        {
        	if(listOfTargetNode.contains(n.getNodeData().getLabel())==false)
        	{
        		if(listOfDiseaseNode==null || (listOfDiseaseNode!=null && listOfDiseaseNode.contains(n.getNodeData().getLabel())==false))
        			val.add(Double.valueOf(1));
        	}
        }
        int i=0;
        for(Node n: nGraph.getNodes())
        {
        	if(listOfTargetNode.contains(n.getNodeData().getLabel())==false)
        	{
        		if(listOfDiseaseNode==null || (listOfDiseaseNode!=null && listOfDiseaseNode.contains(n.getNodeData().getLabel())==false))
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
	    
	    ArrayList<String> neighbourList=new ArrayList<String>();
		for(int i=0; i<listOfTargetNode.size(); i++)
		{
			ArrayList<String> l=originalGraph.getNodeNeighbour(listOfTargetNode.get(i));
			if(neighbourList.contains(listOfTargetNode.get(i))==false)
				neighbourList.add(listOfTargetNode.get(i));
			for(int j=0; j<l.size(); j++)
			{
				if(neighbourList.contains(l.get(j))==false)
					neighbourList.add(l.get(j));
			}
		}
		if(listOfDiseaseNode!=null)
		{
			for(int i=0; i<listOfDiseaseNode.size(); i++)
			{
				ArrayList<String> l=originalGraph.getNodeNeighbour(listOfDiseaseNode.get(i));
				if(neighbourList.contains(listOfDiseaseNode.get(i))==false)
					neighbourList.add(listOfDiseaseNode.get(i));
				for(int j=0; j<l.size(); j++)
				{
					if(neighbourList.contains(l.get(j))==false)
						neighbourList.add(l.get(j));
				}
			}
		}
		System.out.println("neighbour:"+neighbourList);
		ArrayList<String> removeList=new ArrayList<String>();
		for(Node n : nGraph.getNodes()) {
	        if(neighbourList.contains(n.getNodeData().getLabel())==false)
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
		
		//create targetNodePanel
		JPanel targetNodePanel=new JPanel();
		targetNodePanel.setLayout(new BoxLayout(targetNodePanel, BoxLayout.Y_AXIS));
		JLabel targetNodeLabel=new JLabel();
		targetNodeLabel.setText("Combination Target Node List");
		JPanel targetNodeLabelPanel=new JPanel();
		targetNodeLabelPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		targetNodeLabelPanel.add(targetNodeLabel);
		targetNodeList=new JList<String>();
		targetNodeScrollPane=new JScrollPane();
		targetNodeList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		targetNodeList.addListSelectionListener(new ListSelectionListener() 
		{
			public void valueChanged(ListSelectionEvent e){targetNodeList_valueChanged(e);}
		});
		String[] javaArr=listOfTargetNode.toArray(new String[listOfTargetNode.size()]);
		targetNodeList.setListData(javaArr);
		targetNodeScrollPane = new JScrollPane(targetNodeList);
		targetNodeScrollPane.setViewportView(targetNodeList);
		targetNodeScrollPane.setPreferredSize(new Dimension(150, 380));
		targetNodeScrollPane.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		targetNodePanel.add(targetNodeLabelPanel);
		targetNodePanel.add(targetNodeScrollPane);
		
		neighbourhoodGraphPanel=new JPanel();
		neighbourhoodGraphPanel.setBackground(Color.BLACK);
		neighbourhoodGraphPanel.setPreferredSize(new Dimension(550, 410));
		
		settingPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		settingPanel.add(targetNodePanel);
		settingPanel.add(neighbourhoodGraphPanel);
		
		//create neighbourPanel
		neighbourPanel=new JPanel();
		neighbourPanel.setLayout(new BoxLayout(neighbourPanel, BoxLayout.Y_AXIS));
		JPanel neighbourTextAreaPanel=new JPanel();
		neighbourTextAreaPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		neighbourTextArea=new JTextArea();
		neighbourScrollPane=new JScrollPane(neighbourTextArea);
		neighbourScrollPane.setViewportView(neighbourTextArea);
		neighbourScrollPane.setPreferredSize(new Dimension(700, 80));
		neighbourScrollPane.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		neighbourTextAreaPanel.add(neighbourScrollPane);
		JPanel neighbourLabelPanel=new JPanel();
		neighbourLabelPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		JLabel neighbourLabel=new JLabel();
		neighbourLabel.setText("Disease node neighbour(s):");
		neighbourLabelPanel.add(neighbourLabel);
		neighbourPanel.add(neighbourLabelPanel);
		neighbourPanel.add(neighbourScrollPane);
		
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
		add(neighbourPanel);
		add(buttonPanel);
	}
	
	private void visualizeSelectedTargetNodeInGraph(String node)
	{
		//Preview configuration
		nGraph.writeLock();
		neighbourList=new ArrayList<String>();
		for(Node n: nGraph.getNodes())
		{
			if(node.compareTo(n.getNodeData().getLabel())==0) //selected target node (red, opaque)
			{
				n.getNodeData().setColor(1.0f, 0.0f, 0.0f); //red
				n.getNodeData().setSize(25); 
				n.getNodeData().setAlpha(1.0f); //opaque
			}
			else //other nodes node
			{
				Node n1=nGraph.getNode(node);
				Edge e1=nGraph.getEdge(n1, n);
				Edge e2=nGraph.getEdge(n, n1);
				if(e1==null && e2==null) //n is not a neighbour to the selected target node
				{
					if(listOfTargetNode.contains(n.getNodeData().getLabel())==true) //non-selected target node (cyan, transparent)
						n.getNodeData().setColor(0.0f, 1.0f, 1.0f); //cyan
					else if(listOfDiseaseNode!=null && listOfDiseaseNode.contains(n.getNodeData().getLabel())==true)//non-selected disease node
						n.getNodeData().setColor(1.0f, 1.0f, 0.0f); //yellow
					else
						n.getNodeData().setColor(0.75f, 0.75f, 0.75f); //gray
					n.getNodeData().setSize(2); 
					n.getNodeData().setAlpha(0.03f); //transparent
				}
				else
				{
					neighbourList.add(n.getNodeData().getLabel());
					if(listOfTargetNode.contains(n.getNodeData().getLabel())==true) //non-selected target node (cyan, transparent)
						n.getNodeData().setColor(0.0f, 1.0f, 1.0f); //cyan
					else if(listOfDiseaseNode!=null && listOfDiseaseNode.contains(n.getNodeData().getLabel())==true)//non-selected disease node
						n.getNodeData().setColor(1.0f, 1.0f, 0.0f); //yellow
					else
						n.getNodeData().setColor(1.0f, 0.5f, 0.0f); //orange
					n.getNodeData().setSize(10); 
					n.getNodeData().setAlpha(1.0f); //opaque
				}
			}
		}
	    nGraph.writeUnlock();
	    updateNeighbourPanel();
	    nGraphPreviewController.refreshPreview();
	    neighbourhoodGraphPanel.repaint();
	    nGraphTarget.refresh();
	    System.out.println("nGraph preview done");
	}
	
	private void updateNeighbourPanel()
	{
		neighbour="";
		for(int i=0; i<neighbourList.size(); i++)
		{
			neighbour=neighbour+neighbourList.get(i);
			if(i<neighbourList.size()-1)
				neighbour=neighbour+", ";
			if(i!=0 && i%12==0)
				neighbour=neighbour+"\n";
		}
		neighbourTextArea.setText(neighbour);
	}
	
	private void targetNodeList_valueChanged(ListSelectionEvent e) 
	{
		if (!e.getValueIsAdjusting()) 
		{
			if(targetNodeList.getSelectedValue()!=null)
			{
				selectedTargetNode = targetNodeList.getSelectedValue().toString();
				System.out.println(selectedTargetNode);
				visualizeSelectedTargetNodeInGraph(selectedTargetNode);
			}
		}
	}

	private void cancel_actionPerformed(){
		nGraphPC.openWorkspace(workspace1);       //Set as current workspace
		setVisible(false);
	}
}
