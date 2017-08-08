package dialogFrames;

//Imports are listed in full to show what's being used could just import javax.swing.* and java.awt.* etc..
import graph.Gephi_graph;
import graph.JGRAPHT_graph;
import graph.JUNG_graph;
import graph.graph_pathways;
import hallmark.allHallmark;

import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Robot;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.EtchedBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.Position;

import org.gephi.preview.api.ManagedRenderer;
import org.gephi.preview.api.PreviewController;
import org.gephi.preview.api.PreviewModel;
import org.gephi.project.api.Project;
import org.gephi.project.api.ProjectController;
import org.gephi.project.api.Workspace;
import org.openide.util.Lookup;

import constants.stringConstant;
import cz.cvut.fit.krizeji1.multicolour.MultiColourRenderer;
import database.postgreSQL;


@SuppressWarnings("serial")
//	Layout: 
//  |  - settingPanel                                             
//  |    + nodeListPanel                                        
//  |      * nodeList (multi-select), nodeListScrollPane        
//  |    + selectListButtonPanel
//  |      * selectButton, deselectButton
//	|    + selectedNodePanel                                        
//	|      * selectedNodeList (multi-select), selectedNodeListScrollPane        
//	|	 + viewSettingPanel                                        
//	|      * settingComboBox
//  |  - buttonPanel
//  |    + visualizeButton
//  |    + cancelButton

public class viewNodePairPathwayDialog extends JDialog{
	private JPanel settingPanel, buttonPanel, searchPanel, graphPanel;
	private JPanel bottomRightPanel;
	private JButton selectButton, deselectButton, visualizeButton, reapplyHallmarkButton;
	private JList<String> nodeList, selectedNodeList;
	private JScrollPane nodeListScrollPane, selectedNodeListScrollPane;
	private JList<String> pathwayNodeList, sccNodeList;
	private JScrollPane pathwayNodeListScrollPane, sccNodeListScrollPane;
	private JTextField pathLengthText=new JTextField();
	private ArrayList<String> selectedNode;
	private ArrayList<String> allNodeList;
	private ArrayList<String> pathwayNode;
	private ArrayList<String> sccNode;
	//search panel (center-bottom left)
	private JTextField searchText=new JTextField();
	private JButton searchButton=new JButton();
	private boolean performVisualization=false;
	protected final static String VISUALIZE = "Visualize";
	protected final static String SELECT = "Select";
	protected final static String DESELECT = "Deselect";
	protected final static String SEARCH_NODE = "Find";
	protected final static String REAPPLY_HALLMARK = "Apply Hallmark Mask";

	private postgreSQL postgreDB;
	private JUNG_graph jungGraph;
	private JGRAPHT_graph jGraph;
	private Gephi_graph gGraph;
	private stringConstant strConstants=new stringConstant();
	
	private ProjectController interactivePC;
	private Project interactiveProject;
	private Workspace currWorkspace;
	private Workspace previousWorkspace;
	private PreviewController interactivePreviewController;
	private PreviewModel interactivePreviewModel;
	private MultiColourRenderer multiColorRenderer=new MultiColourRenderer();
	
	private ArrayList<String> selectedHallmarks=new ArrayList<String>();
	private ArrayList<String> entireHallmarkList=new ArrayList<String>();
	
	private JPanel hallmarkLegendContent = new JPanel();
	private JScrollPane vertexLegendScroll = new JScrollPane(hallmarkLegendContent);
	private JCheckBox proliferationCheckBox, growthRepressorCheckBox, apoptosisCheckBox, replicativeImmortalityCheckBox;
	private JCheckBox angiogenesisCheckBox, metastasisCheckBox, metabolismCheckBox, immuneDestructionCheckBox;
	private JCheckBox genomeInstabilityCheckBox, tumorPromotingInflammationCheckBox;
	
	private String HALLMARK_PROLIFERATION_GUI = strConstants.getGUIProliferation();
	private String HALLMARK_GROWTH_REPRESSOR_GUI = strConstants.getGUIGrowthRepressor();
	private String HALLMARK_APOPTOSIS_GUI = strConstants.getGUIApoptosis();
	private String HALLMARK_REPLICATIVE_IMMORTALITY_GUI = strConstants.getGUIReplicativeImmortality();
	private String HALLMARK_ANGIOGENESIS_GUI = strConstants.getGUIAngiogenesis();
	private String HALLMARK_METASTASIS_GUI = strConstants.getGUIMetastasis();
	private String HALLMARK_METABOLISM_GUI = strConstants.getGUIMetabolism();
	private String HALLMARK_IMMUNE_DESTRUCTION_GUI = strConstants.getGUIImmuneDestruction();
	private String HALLMARK_GENOME_INSTABILITY_GUI = strConstants.getGUIGenomeInstability();
	private String HALLMARK_TUMOR_PROMOTING_INFLAMMATION_GUI = strConstants.getGUITumorPromotingInflammation();
	
	private String HALLMARK_PROLIFERATION_DB;
	private String HALLMARK_GROWTH_REPRESSOR_DB;
	private String HALLMARK_APOPTOSIS_DB;
	private String HALLMARK_REPLICATIVE_IMMORTALITY_DB;
	private String HALLMARK_ANGIOGENESIS_DB;
	private String HALLMARK_METASTASIS_DB;
	private String HALLMARK_METABOLISM_DB;
	private String HALLMARK_IMMUNE_DESTRUCTION_DB;
	private String HALLMARK_GENOME_INSTABILITY_DB;
	private String HALLMARK_TUMOR_PROMOTING_INFLAMMATION_DB;
	
	private int pathLength;
	
	//threading
	VisualizeNetworkWorker visualizeNetworkWorker;
	
	public viewNodePairPathwayDialog(ArrayList<String> nList, JGRAPHT_graph g, JUNG_graph jung_Graph, ProjectController pc, 
			Project project, Workspace oldWorkspace, Workspace newWorkspace, postgreSQL dB, ArrayList<String> hallmarkSelected)
	{
		//initialize constants
		jGraph=g;
		jungGraph=jung_Graph;
		allNodeList=nList;
		selectedNode=new ArrayList<String>();
		postgreDB=dB;
		selectedHallmarks=hallmarkSelected;
		
		HALLMARK_PROLIFERATION_DB=strConstants.getDBProliferation();
		HALLMARK_GROWTH_REPRESSOR_DB=strConstants.getDBGrowthRepressor();
		HALLMARK_APOPTOSIS_DB=strConstants.getDBApoptosis();
		HALLMARK_REPLICATIVE_IMMORTALITY_DB=strConstants.getDBReplicativeImmortality();
		HALLMARK_ANGIOGENESIS_DB=strConstants.getDBAngiogenesis();
		HALLMARK_METASTASIS_DB=strConstants.getDBMetastasis();
		HALLMARK_METABOLISM_DB=strConstants.getDBMetabolism();
		HALLMARK_IMMUNE_DESTRUCTION_DB=strConstants.getDBImmuneDestruction();
		HALLMARK_GENOME_INSTABILITY_DB=strConstants.getDBGenomeInstability();
		HALLMARK_TUMOR_PROMOTING_INFLAMMATION_DB=strConstants.getDBTumorPromotingInflammation();
		
		entireHallmarkList.add(HALLMARK_PROLIFERATION_DB);
		entireHallmarkList.add(HALLMARK_GROWTH_REPRESSOR_DB);
		entireHallmarkList.add(HALLMARK_APOPTOSIS_DB);
		entireHallmarkList.add(HALLMARK_REPLICATIVE_IMMORTALITY_DB);
		entireHallmarkList.add(HALLMARK_ANGIOGENESIS_DB);
		entireHallmarkList.add(HALLMARK_METASTASIS_DB);
		entireHallmarkList.add(HALLMARK_METABOLISM_DB);
		entireHallmarkList.add(HALLMARK_IMMUNE_DESTRUCTION_DB);
		entireHallmarkList.add(HALLMARK_GENOME_INSTABILITY_DB);
		entireHallmarkList.add(HALLMARK_TUMOR_PROMOTING_INFLAMMATION_DB);
		
		interactivePC=pc;
		interactiveProject=project;
		currWorkspace=newWorkspace;
		previousWorkspace=oldWorkspace;
		interactivePC.openWorkspace(currWorkspace); 
		interactivePreviewController = Lookup.getDefault().lookup(PreviewController.class);
		interactivePreviewModel=interactivePreviewController.getModel(interactivePC.getCurrentWorkspace());
		ManagedRenderer [] mr = interactivePreviewModel.getManagedRenderers();
		ManagedRenderer [] mr2 = Arrays.copyOf(mr, mr.length+1);
		mr2[mr.length] = new ManagedRenderer(multiColorRenderer, true);
		interactivePreviewModel.setManagedRenderers(mr2);
		
		//set JDialog properties
		setTitle("Visualize Connection of Selected Node Pair");
		setSize(940,710);
		setLocationRelativeTo(null);
		setLayout(new BorderLayout());
		setResizable(false);
		setModal(true);

		//configure components in JDialog
		initializeComponents();
				
		gGraph=new Gephi_graph(interactivePC, interactiveProject, currWorkspace, interactivePreviewModel); //for visualization
		gGraph.configureInteractivePreview();
		gGraph.visualizeGraph(graphPanel);
	}

	private ActionListener actionListener = new ActionListener() 
	{
		public void actionPerformed(ActionEvent act) {
			if (act.getActionCommand() == VISUALIZE)
				visualize_actionPerformed();
			if (act.getActionCommand() == SELECT)
				select_actionPerformed();
			if (act.getActionCommand() == DESELECT)
				deselect_actionPerformed();
			if (act.getActionCommand() == SEARCH_NODE)
				searchNode_actionPerformed();
			if (act.getActionCommand() == REAPPLY_HALLMARK)
				visualize_actionPerformed();
		}
	};

	private void initializeComponents()
	{
		settingPanel=new JPanel();
		buttonPanel=new JPanel();
		searchPanel=new JPanel();

		JPanel leftPanel=new JPanel();
		leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
		leftPanel.setPreferredSize(new Dimension(330, 670));

		JPanel leftTopPanel=new JPanel();
		leftTopPanel.setLayout(new BoxLayout(leftTopPanel, BoxLayout.Y_AXIS));
		leftTopPanel.setPreferredSize(new Dimension(330, 325));
		leftTopPanel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));

		graphPanel=new JPanel();
		graphPanel.setBackground(Color.BLACK);
		graphPanel.setPreferredSize(new Dimension(580, 540));

		//create nodePanel
		JPanel nodePanel=new JPanel();
		nodePanel.setLayout(new BoxLayout(nodePanel, BoxLayout.Y_AXIS));
		JLabel nodeLabel=new JLabel();
		nodeLabel.setText("Node List");
		JPanel nodeLabelPanel=new JPanel();
		nodeLabelPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		nodeLabelPanel.add(nodeLabel);
		nodeList=new JList<String>();
		nodeListScrollPane=new JScrollPane();
		nodeList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		String[] javaArr=allNodeList.toArray(new String[allNodeList.size()]);
		nodeList.setListData(javaArr);
		nodeListScrollPane = new JScrollPane(nodeList);
		nodeListScrollPane.setViewportView(nodeList);
		nodeListScrollPane.setPreferredSize(new Dimension(100, 180));
		nodeListScrollPane.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);

		nodePanel.add(nodeLabelPanel);
		nodePanel.add(nodeListScrollPane);
		//create selectedButtonPanel
		JPanel selectButtonPanel=new JPanel();
		selectButtonPanel.setLayout(new BoxLayout(selectButtonPanel, BoxLayout.Y_AXIS));
		selectButton= new JButton(">");
		selectButton.setSize(50, 20);
		selectButton.setActionCommand(SELECT);
		selectButton.addActionListener(actionListener);
		JLabel emptyLabel=new JLabel(" ");
		emptyLabel.setSize(50, 60);
		deselectButton= new JButton("<");
		deselectButton.setSize(50, 20);
		deselectButton.setActionCommand(DESELECT);
		deselectButton.addActionListener(actionListener);
		deselectButton.setEnabled(false);
		selectButtonPanel.add(selectButton);
		selectButtonPanel.add(emptyLabel);
		selectButtonPanel.add(deselectButton);
		//create selectedNodePanel
		JPanel selectedNodePanel=new JPanel();
		selectedNodePanel.setLayout(new BoxLayout(selectedNodePanel, BoxLayout.Y_AXIS));
		JLabel selectedNodeLabel=new JLabel();
		selectedNodeLabel.setText("Selected Node Pair");
		JPanel selectedNodeLabelPanel=new JPanel();
		selectedNodeLabelPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		selectedNodeLabelPanel.add(selectedNodeLabel);
		selectedNodeList=new JList<String>();
		selectedNodeListScrollPane=new JScrollPane();
		selectedNodeList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		String[] javaArr1=selectedNode.toArray(new String[selectedNode.size()]);
		selectedNodeList.setListData(javaArr1);
		selectedNodeListScrollPane = new JScrollPane(selectedNodeList);
		selectedNodeListScrollPane.setViewportView(selectedNodeList);
		selectedNodeListScrollPane.setPreferredSize(new Dimension(100, 180));
		selectedNodeListScrollPane.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		selectedNodePanel.add(selectedNodeLabelPanel);
		selectedNodePanel.add(selectedNodeListScrollPane);

		settingPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		settingPanel.add(nodePanel);
		settingPanel.add(selectButtonPanel);
		settingPanel.add(selectedNodePanel);
		//search panel
		searchButton.setText(SEARCH_NODE);
		searchButton.setActionCommand(SEARCH_NODE);
		searchButton.addActionListener(actionListener);
		searchText.setPreferredSize(new Dimension(205,25));
		searchPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		searchPanel.add(searchText);
		searchPanel.add(searchButton);

		JLabel pathLengthLabel=new JLabel("Max path length:");
		pathLengthText.setPreferredSize(new Dimension(70,25));
		JLabel emptyLabel2=new JLabel("      ");
		visualizeButton = new JButton();
		visualizeButton.setSize(50, 20);
		visualizeButton.setText(VISUALIZE);
		visualizeButton.setEnabled(false);
		visualizeButton.setActionCommand(VISUALIZE);
		visualizeButton.addActionListener(actionListener);

		buttonPanel= new JPanel();
		buttonPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		buttonPanel.setPreferredSize(new Dimension(200, 20));
		buttonPanel.add(pathLengthLabel);
		buttonPanel.add(pathLengthText);
		buttonPanel.add(emptyLabel2);
		buttonPanel.add(visualizeButton);

		leftTopPanel.add(settingPanel);
		leftTopPanel.add(searchPanel);
		leftTopPanel.add(buttonPanel);
		//buttonPanel.add(cancelButton);

		JPanel leftBottomPanel=new JPanel();
		leftBottomPanel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
		leftBottomPanel.setPreferredSize(new Dimension(300, 190));
		leftBottomPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

		//create pathwayNodePanel
		JPanel pathwayNodePanel=new JPanel();
		pathwayNodePanel.setLayout(new BoxLayout(pathwayNodePanel, BoxLayout.Y_AXIS));
		JLabel pathwayNodeLabel=new JLabel();
		pathwayNodeLabel.setText("Node in pathway");
		JPanel pathwayNodeLabelPanel=new JPanel();
		pathwayNodeLabelPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		pathwayNodeLabelPanel.add(pathwayNodeLabel);
		pathwayNodeList=new JList<String>();
		pathwayNodeListScrollPane=new JScrollPane();
		pathwayNodeList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		pathwayNodeList.addListSelectionListener(new ListSelectionListener() 
		{
			public void valueChanged(ListSelectionEvent e){pathwayNodeList_valueChanged(e);}
		});
		pathwayNode=new ArrayList<String>();
		javaArr=pathwayNode.toArray(new String[pathwayNode.size()]);
		pathwayNodeList.setListData(javaArr);
		pathwayNodeListScrollPane = new JScrollPane(pathwayNodeList);
		pathwayNodeListScrollPane.setViewportView(pathwayNodeList);
		pathwayNodeListScrollPane.setPreferredSize(new Dimension(100, 160));
		pathwayNodeListScrollPane.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);

		pathwayNodePanel.add(pathwayNodeLabelPanel);
		pathwayNodePanel.add(pathwayNodeListScrollPane);

		//create pathwayNodePanel
		JPanel sccNodePanel=new JPanel();
		sccNodePanel.setLayout(new BoxLayout(sccNodePanel, BoxLayout.Y_AXIS));
		JLabel sccNodeLabel=new JLabel();
		sccNodeLabel.setText("Node in selected SCC");
		JPanel sccNodeLabelPanel=new JPanel();
		sccNodeLabelPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		sccNodeLabelPanel.add(sccNodeLabel);
		sccNodeList=new JList<String>();
		sccNodeListScrollPane=new JScrollPane();
		sccNodeList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		sccNode=new ArrayList<String>();
		javaArr=sccNode.toArray(new String[sccNode.size()]);
		sccNodeList.setListData(javaArr);
		sccNodeListScrollPane = new JScrollPane(sccNodeList);
		sccNodeListScrollPane.setViewportView(sccNodeList);
		sccNodeListScrollPane.setPreferredSize(new Dimension(100, 160));
		sccNodeListScrollPane.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);

		sccNodePanel.add(sccNodeLabelPanel);
		sccNodePanel.add(sccNodeListScrollPane);

		JPanel emptyPanel=new JPanel();
		emptyPanel.setPreferredSize(new Dimension(20, 160));
		
		leftBottomPanel.add(pathwayNodePanel);
		leftBottomPanel.add(emptyPanel);
		leftBottomPanel.add(sccNodePanel);
		
		JPanel leftBottommostPanel=new JPanel();
		leftBottommostPanel.setLayout(new BoxLayout(leftBottommostPanel, BoxLayout.Y_AXIS));
		leftBottommostPanel.setPreferredSize(new Dimension(250, 90));
		JPanel hallmarkLegendLabelPanel=new JPanel();
		hallmarkLegendLabelPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		JLabel hallmarkLegendLabel=new JLabel("Hallmark Legend:");
		hallmarkLegendLabelPanel.add(hallmarkLegendLabel);
		hallmarkLegendContent.setLayout(new BoxLayout(hallmarkLegendContent, BoxLayout.Y_AXIS));
		vertexLegendScroll.setPreferredSize(new Dimension(250, 90));
		addVertexLegendHallmarkColor();
		leftBottommostPanel.add(hallmarkLegendLabelPanel);
		leftBottommostPanel.add(vertexLegendScroll);

		leftPanel.add(leftTopPanel);
		leftPanel.add(leftBottomPanel);
		leftPanel.add(leftBottommostPanel);
		
		bottomRightPanel=new JPanel();
		bottomRightPanel.setLayout(new BoxLayout(bottomRightPanel, BoxLayout.Y_AXIS));
		bottomRightPanel.setPreferredSize(new Dimension(590, 120));
		
		JPanel checkBoxesTopPanel=new JPanel();
		checkBoxesTopPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		checkBoxesTopPanel.setPreferredSize(new Dimension(530, 20));
		
		JPanel proliferationPanel=new JPanel();
		proliferationPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		if(selectedHallmarks.contains(entireHallmarkList.get(0))==true)
			proliferationCheckBox=new JCheckBox("Hallmark 1", true);
		else
			proliferationCheckBox=new JCheckBox("Hallmark 1", false);
		proliferationPanel.add(proliferationCheckBox);
		JPanel growthRepressorPanel=new JPanel();
		growthRepressorPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		if(selectedHallmarks.contains(entireHallmarkList.get(1))==true)
			growthRepressorCheckBox=new JCheckBox("Hallmark 2", true);
		else
			growthRepressorCheckBox=new JCheckBox("Hallmark 2", false);
		growthRepressorPanel.add(growthRepressorCheckBox);
		JPanel apoptosisPanel=new JPanel();
		apoptosisPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		if(selectedHallmarks.contains(entireHallmarkList.get(2))==true)
			apoptosisCheckBox=new JCheckBox("Hallmark 3", true);
		else
			apoptosisCheckBox=new JCheckBox("Hallmark 3", false);
		apoptosisPanel.add(apoptosisCheckBox);
		JPanel replicativeImmortalityPanel=new JPanel();
		replicativeImmortalityPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		if(selectedHallmarks.contains(entireHallmarkList.get(3))==true)
			replicativeImmortalityCheckBox=new JCheckBox("Hallmark 4", true);
		else
			replicativeImmortalityCheckBox=new JCheckBox("Hallmark 4", false);
		replicativeImmortalityPanel.add(replicativeImmortalityCheckBox);
		JPanel angiogenesisPanel=new JPanel();
		angiogenesisPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		if(selectedHallmarks.contains(entireHallmarkList.get(4))==true)
			angiogenesisCheckBox=new JCheckBox("Hallmark 5", true);
		else
			angiogenesisCheckBox=new JCheckBox("Hallmark 5", false);
		angiogenesisPanel.add(angiogenesisCheckBox);
		
		checkBoxesTopPanel.add(proliferationPanel);
		checkBoxesTopPanel.add(growthRepressorPanel);
		checkBoxesTopPanel.add(apoptosisPanel);
		checkBoxesTopPanel.add(replicativeImmortalityPanel);
		checkBoxesTopPanel.add(angiogenesisPanel);
		
		JPanel checkBoxesBottomPanel=new JPanel();
		checkBoxesBottomPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		checkBoxesBottomPanel.setPreferredSize(new Dimension(530, 20));
		
		JPanel metastasisPanel=new JPanel();
		metastasisPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		if(selectedHallmarks.contains(entireHallmarkList.get(5))==true)
			metastasisCheckBox=new JCheckBox("Hallmark 6", true);
		else
			metastasisCheckBox=new JCheckBox("Hallmark 6", false);
		metastasisPanel.add(metastasisCheckBox);
		JPanel metabolismPanel=new JPanel();
		metabolismPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		if(selectedHallmarks.contains(entireHallmarkList.get(6))==true)
			metabolismCheckBox=new JCheckBox("Hallmark 7", true);
		else
			metabolismCheckBox=new JCheckBox("Hallmark 7", false);
		metabolismPanel.add(metabolismCheckBox);
		JPanel immuneDestructionPanel=new JPanel();
		immuneDestructionPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		if(selectedHallmarks.contains(entireHallmarkList.get(7))==true)
			immuneDestructionCheckBox=new JCheckBox("Hallmark 8", true);
		else
			immuneDestructionCheckBox=new JCheckBox("Hallmark 8", false);
		immuneDestructionPanel.add(immuneDestructionCheckBox);
		JPanel genomeInstabilityPanel=new JPanel();
		genomeInstabilityPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		if(selectedHallmarks.contains(entireHallmarkList.get(8))==true)
			genomeInstabilityCheckBox=new JCheckBox("Hallmark 9", true);
		else
			genomeInstabilityCheckBox=new JCheckBox("Hallmark 9", false);
		genomeInstabilityPanel.add(genomeInstabilityCheckBox);
		JPanel tumorPromotingInflammationPanel=new JPanel();
		tumorPromotingInflammationPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		if(selectedHallmarks.contains(entireHallmarkList.get(9))==true)
			tumorPromotingInflammationCheckBox=new JCheckBox("Hallmark 10", true);
		else
			tumorPromotingInflammationCheckBox=new JCheckBox("Hallmark 10", false);
		tumorPromotingInflammationPanel.add(tumorPromotingInflammationCheckBox);
		
		checkBoxesBottomPanel.add(metastasisPanel);
		checkBoxesBottomPanel.add(metabolismPanel);
		checkBoxesBottomPanel.add(immuneDestructionPanel);
		checkBoxesBottomPanel.add(genomeInstabilityPanel);
		checkBoxesBottomPanel.add(tumorPromotingInflammationPanel);
		
		JPanel rightPanel=new JPanel();
		rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
		
		JPanel reApplyMaskPanel=new JPanel();
		reApplyMaskPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
		reapplyHallmarkButton = new JButton();
		reapplyHallmarkButton.setSize(50, 20);
		reapplyHallmarkButton.setText(REAPPLY_HALLMARK);
		reapplyHallmarkButton.setActionCommand(REAPPLY_HALLMARK);
		reapplyHallmarkButton.addActionListener(actionListener);
		reApplyMaskPanel.add(reapplyHallmarkButton);
		
		bottomRightPanel.add(checkBoxesTopPanel);
		bottomRightPanel.add(checkBoxesBottomPanel);
		bottomRightPanel.add(reApplyMaskPanel);
		
		rightPanel.add(graphPanel);
		rightPanel.add(bottomRightPanel);
		//bottomRightPanel.add(buttonPanel);
		
		addWindowListener(new WindowAdapter()
        {
            @Override
            public void windowClosing(WindowEvent e)
            {
                System.out.println("Closed. switch workspace back");
                interactivePC.openWorkspace(previousWorkspace); 
                setVisible(false);
            }
        });
		setLayout(new FlowLayout(FlowLayout.LEFT));
		add(leftPanel);
		add(rightPanel);
	}

	private void pathwayNodeList_valueChanged(ListSelectionEvent e) 
	{
		if (!e.getValueIsAdjusting()) 
		{
			if(pathwayNodeList.getSelectedValue()!=null)
			{
				int selectedIndex = pathwayNodeList.getSelectedIndex();
				String nodeSelected=pathwayNodeList.getSelectedValue().toString();
				if(selectedIndex!=-1)
				{
					//uncolor all previous hallmarks
					gGraph.resetHallmarkNode();
					//color selected hallmarks
					allHallmark hallmarkNodeList=postgreDB.getHallmark_hallmark();
					gGraph.colorAndResizeHallmarkNode(hallmarkNodeList, selectedHallmarks, true);
					gGraph.colorAndResizeSelectedNode(nodeSelected, true);
				}
				if(selectedIndex!=-1 && nodeSelected.contains("SCC_")==true)
				{
					//update details of newly selected scc
					sccNode=jungGraph.getSccOfMetaNode(nodeSelected);
					String[] javaArr=sccNode.toArray(new String[sccNode.size()]);
					sccNodeList.setListData(javaArr);
					sccNodeListScrollPane.setViewportView(sccNodeList);
				}
				else
				{
					sccNode=new ArrayList<String>();
					String[] javaArr=sccNode.toArray(new String[sccNode.size()]);
					sccNodeList.setListData(javaArr);
					sccNodeListScrollPane.setViewportView(sccNodeList);
				}
			}
		}
	}
	
	private boolean isNumeric(String str)  
	{  
	  try  
	  {  
	    double d = Double.parseDouble(str);  
	  }  
	  catch(NumberFormatException nfe)  
	  {  
	    return false;  
	  }  
	  return true;  
	}
	
	private void visualize_actionPerformed(){
		selectedHallmarks=new ArrayList<String>();
		if(proliferationCheckBox.isSelected()==true)
			selectedHallmarks.add(HALLMARK_PROLIFERATION_DB);
		if(growthRepressorCheckBox.isSelected()==true)
			selectedHallmarks.add(HALLMARK_GROWTH_REPRESSOR_DB);
		if(apoptosisCheckBox.isSelected()==true)
			selectedHallmarks.add(HALLMARK_APOPTOSIS_DB);
		if(replicativeImmortalityCheckBox.isSelected()==true)
			selectedHallmarks.add(HALLMARK_REPLICATIVE_IMMORTALITY_DB);
		if(angiogenesisCheckBox.isSelected()==true)
			selectedHallmarks.add(HALLMARK_ANGIOGENESIS_DB);
		if(metastasisCheckBox.isSelected()==true)
			selectedHallmarks.add(HALLMARK_METASTASIS_DB);
		if(metabolismCheckBox.isSelected()==true)
			selectedHallmarks.add(HALLMARK_METABOLISM_DB);
		if(immuneDestructionCheckBox.isSelected()==true)
			selectedHallmarks.add(HALLMARK_IMMUNE_DESTRUCTION_DB);
		if(genomeInstabilityCheckBox.isSelected()==true)
			selectedHallmarks.add(HALLMARK_GENOME_INSTABILITY_DB);
		if(tumorPromotingInflammationCheckBox.isSelected()==true)
			selectedHallmarks.add(HALLMARK_TUMOR_PROMOTING_INFLAMMATION_DB);
		
		if(isNumeric(pathLengthText.getText())==true)
		{
			setTitle("Visualize Connection of Selected Node Pair ("+selectedNode.get(0)+","+selectedNode.get(1)+")");
			ArrayList<String> pathwayNode=new ArrayList<String>();
			pathLength=Integer.parseInt(pathLengthText.getText());
			pathwayNode=jGraph.returnAllPaths(selectedNode.get(0), selectedNode.get(1), pathLength);
			
			gGraph.destroyGraph();
			
			if(pathwayNode.size()==0)
			{
				JOptionPane.showMessageDialog(new JFrame(), "There is no connection between "+selectedNode.get(0)+" and "+selectedNode.get(1), "No results", JOptionPane.INFORMATION_MESSAGE);
				pathwayNode=new ArrayList<String>();
				String[] javaArr=pathwayNode.toArray(new String[pathwayNode.size()]);
				pathwayNodeList.setListData(javaArr);
				pathwayNodeListScrollPane.setViewportView(pathwayNodeList);
			}
			else
			{
				allHallmark hallmarkNodeList=postgreDB.getHallmark_hallmark();
				
				visualizeNetworkWorker=new VisualizeNetworkWorker();
				ArrayList<Object> param=new ArrayList<Object>();
				param.add(selectedNode);
				param.add(gGraph);
				param.add(hallmarkNodeList);
				param.add(selectedHallmarks);
				param.add(pathwayNode);
				param.add(graphPanel);
				//param.add(pathList);
				visualizeNetworkWorker.Start(VISUALIZE, param);
			}

			//setVisible(false);
		}
		else
			JOptionPane.showMessageDialog(new JFrame(), "Please enter a valid (numeric) path length.", "Error", JOptionPane.ERROR_MESSAGE);
	}

	private void addVertexLegendHallmarkColor() 
	{
		addVertexLegendLabelColor(HALLMARK_PROLIFERATION_GUI, new Color(0x00FF00));
		addVertexLegendLabelColor(HALLMARK_GROWTH_REPRESSOR_GUI, new Color(0xFF8000));
		addVertexLegendLabelColor(HALLMARK_APOPTOSIS_GUI, new Color(0xFFFF00));
		addVertexLegendLabelColor(HALLMARK_REPLICATIVE_IMMORTALITY_GUI, new Color(0x0080FF));
		addVertexLegendLabelColor(HALLMARK_ANGIOGENESIS_GUI, new Color(0xFF80FF));
		addVertexLegendLabelColor(HALLMARK_METASTASIS_GUI, new Color(0x8080FF));
		addVertexLegendLabelColor(HALLMARK_METABOLISM_GUI, new Color(0x808000));
		addVertexLegendLabelColor(HALLMARK_IMMUNE_DESTRUCTION_GUI, new Color(0xFF0000));
		addVertexLegendLabelColor(HALLMARK_GENOME_INSTABILITY_GUI, new Color(0x008080));
		addVertexLegendLabelColor(HALLMARK_TUMOR_PROMOTING_INFLAMMATION_GUI, new Color(0xF5DEB3));
	}
	
	private void addVertexLegendLabelColor(String label, Color labelColor) 
	{
		JLabel legendLabel=new JLabel(label);
		JLabel legendLabelColor=new JLabel("    ");
		JPanel legendLabelColorPanel=new JPanel();

		legendLabelColor.setOpaque(true);
		legendLabelColor.setBackground(labelColor);
		legendLabelColorPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		legendLabelColorPanel.add(legendLabelColor);
		legendLabelColorPanel.add(legendLabel);

		hallmarkLegendContent.add(legendLabelColorPanel);
	}
	
	private void select_actionPerformed(){
		int[] selectedIndices=nodeList.getSelectedIndices();
		if((selectedNode.size()==1 && selectedIndices.length==1) ||
				(selectedNode.size()==0 && (selectedIndices.length==2 ||selectedIndices.length==1)))
		{
			for(int i=0; i<selectedIndices.length; i++)
			{
				String n=allNodeList.get(selectedIndices[i]);
				if(selectedNode.contains(n)==false)
					selectedNode.add(n);
				selectedNodeList.setListData(selectedNode.toArray(new String[selectedNode.size()]));
			}
		}
		updateButtons();
	}

	private void deselect_actionPerformed(){
		int[] selectedIndices=selectedNodeList.getSelectedIndices();
		if(selectedIndices.length>0)
		{
			for(int i=0; i<selectedIndices.length; i++)
			{
				String n=selectedNode.get(selectedIndices[i]);
				if(selectedNode.contains(n)==true)
					selectedNode.remove(n);
				selectedNodeList.setListData(selectedNode.toArray(new String[selectedNode.size()]));
			}
		}
		updateButtons();
	}

	private void searchNode_actionPerformed() 
	{
		String node=searchText.getText();
		int index = nodeList.getNextMatch(node, 0, Position.Bias.Forward);
		if (index != -1)
			nodeList.setSelectedValue(node,true);
	}

	private void updateButtons()
	{
		if(selectedNodeList.getModel().getSize()==0)
		{
			deselectButton.setEnabled(false);
			selectButton.setEnabled(true);
			visualizeButton.setEnabled(false);
		}
		else if(selectedNodeList.getModel().getSize()==2)
		{
			deselectButton.setEnabled(true);
			selectButton.setEnabled(false);
			visualizeButton.setEnabled(true);
		}
		else
		{
			deselectButton.setEnabled(true);
			selectButton.setEnabled(true);
			visualizeButton.setEnabled(false);
		}
	}

	public boolean getPerformVisualization(){
		return performVisualization;
	}

	public ArrayList<String> getSelectedNodes(){
		return selectedNode;
	}
	
	private void sendVirtualClickToRefreshGraphDisplay() 
	{
		System.out.println("send virtual click");
		Point currMousePosition=MouseInfo.getPointerInfo().getLocation();
		Robot robot;
		try {
			robot = new Robot();
			robot.mouseMove(graphPanel.getLocationOnScreen().x+10, graphPanel.getLocationOnScreen().y+10);
			robot.mousePress(InputEvent.BUTTON1_MASK); //BUTTON1_MASK is the left button,
			robot.mouseRelease(InputEvent.BUTTON1_MASK);
			robot.mouseMove(currMousePosition.x, currMousePosition.y);
		} catch (AWTException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private String suggestGephiNodeSizeToUse(int size) 
	{
		if(size<50)
			return strConstants.getGephiExtraLarge();
		if(size<100)
			return strConstants.getGephiLarge();
		else if(size<500)
			return strConstants.getGephiMedium();
		else
			return strConstants.getGephiSmall();
	}
	
	private ArrayList<String> createSubGraphFromPath(ArrayList<String> pathList) 
	{
		int i=0;
		String name;
		ArrayList<String> nodeList=pathList;
		ArrayList<ArrayList<String>> inducedEdges=postgreDB.getNetworkEdge_inducedEdges(nodeList);
		
		System.out.println("nodeList: "+nodeList.toString());
		System.out.println("edgeList:");
		for(int j=0; j<inducedEdges.size(); j++)
			System.out.println(inducedEdges.get(j).toString());
			
		ArrayList<String> sccList=new ArrayList<String>();
		//retrieve sccList
		for(i=0; i<nodeList.size(); i++)
		{
			String scc=jungGraph.getMetaNodeOfGivenNode(nodeList.get(i));
			if(scc.contains("SCC_")==true && sccList.contains(scc)==false)
				sccList.add(scc);
		}
		System.out.println("sccList:"+sccList.toString());
		i=0;
		while(i<sccList.size())
		{
			ArrayList<String> l=jungGraph.getSccOfMetaNode(sccList.get(i));
			if(l.size()==1)
				sccList.remove(i);
			else
				i++;
		}
		//remove nodes in scc
		for(i=0; i<sccList.size(); i++)
		{
			ArrayList<String> sccDetails=jungGraph.getSccOfMetaNode(sccList.get(i));
			nodeList.removeAll(sccDetails);
			nodeList.add(sccList.get(i));
		}
		//add back the source and target if they are consumed into the scc
		if(nodeList.contains(selectedNode.get(0))==false)
			nodeList.add(selectedNode.get(0));
		if(nodeList.contains(selectedNode.get(1))==false)
			nodeList.add(selectedNode.get(1));
		
		String nodeSize=suggestGephiNodeSizeToUse(nodeList.size());
		
		//add all vertices
		for(i=0; i<nodeList.size(); i++)
		{
			name=nodeList.get(i);
			gGraph.addANode(name, nodeSize);
		}

		//add all edges
		for(i=0; i<inducedEdges.size(); i++)
		{
			ArrayList<String> inducedEdge=inducedEdges.get(i);
			String sourceNodeName=inducedEdge.get(0);
			String targetNodeName=inducedEdge.get(1);
			String sourceInGraph, targetInGraph;
			
			String sourceSCC=jungGraph.getMetaNodeOfGivenNode(sourceNodeName);
			String targetSCC=jungGraph.getMetaNodeOfGivenNode(targetNodeName);
			
			if(sccList.contains(sourceSCC)==true && sourceNodeName.compareTo(selectedNode.get(0))!=0
					&& sourceNodeName.compareTo(selectedNode.get(1))!=0)
				sourceInGraph=sourceSCC;
			else
				sourceInGraph=sourceNodeName;
			
			if(sccList.contains(targetSCC)==true && targetNodeName.compareTo(selectedNode.get(0))!=0
					&& targetNodeName.compareTo(selectedNode.get(1))!=0)
				targetInGraph=targetSCC;
			else
				targetInGraph=targetNodeName;
			
			if(sourceInGraph.compareTo(targetInGraph)!=0)
				gGraph.addADirectedEdge(sourceInGraph, targetInGraph);
		}
		gGraph.updateEdgeThickness(nodeSize);
		return nodeList;
	}

	
	class VisualizeNetworkWorker
	{
		Thread worker;
		boolean working;
		String ACTION;
		ArrayList<Object> paramList;

		VisualizeNetworkWorker()
		{
			worker=new Thread(new Runnable(){
				public void run(){
					if(ACTION.compareTo(VISUALIZE)==0)
					{
						working=true;
						ArrayList<String> selectedNodePair=(ArrayList<String>)paramList.get(0);
						Gephi_graph graph=(Gephi_graph)paramList.get(1);
						allHallmark hallmarkNodeList=(allHallmark)paramList.get(2);
						ArrayList<String> selectedHallmarkList=(ArrayList<String>)paramList.get(3);
						//ArrayList<ArrayList<String>> connectionList=(ArrayList<ArrayList<String>>)paramList.get(4);
						ArrayList<String> connectionList=(ArrayList<String>)paramList.get(4);
						JPanel gPanel=(JPanel)paramList.get(5);
						
						//redraw nodes
						//Task 1: creating graph
						pathwayNode=createSubGraphFromPath(connectionList);
						graph.visualizeGraph(gPanel);
						String[] javaArr=pathwayNode.toArray(new String[pathwayNode.size()]);
						pathwayNodeList.setListData(javaArr);
						pathwayNodeListScrollPane.setViewportView(pathwayNodeList);
						
						//Task 2: visualizing graph
						//gGraph.configureInteractivePreview();
						//gGraph.visualizeGraph(graphPanel);
						//uncolor all previous hallmarks
						graph.resetHallmarkNode();
						//color selected hallmarks
						graph.colorAndResizeHallmarkNode(hallmarkNodeList, selectedHallmarkList, true);
						graph.colorAndResizeNodeSet(selectedNodePair, null, "Display Disease Node");
						sendVirtualClickToRefreshGraphDisplay();
						working=false;
					}
				}
			});
		}
		public void Start(String action, ArrayList<Object> parameterList)
		{
			if(!working)
			{
				ACTION=action;
				paramList=parameterList;
				worker.start();
			}
		}
	}
}
