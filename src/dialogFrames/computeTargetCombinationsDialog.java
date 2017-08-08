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

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.AttributeSet;
import javax.swing.text.Position;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;

import constants.stringConstant;
import dataType.diseaseNode;
import dataType.nodePathCommon;
import database.postgreSQL;

@SuppressWarnings("serial")
//	Layout: 
//  |  - settingPanel                                             
//  |    + selectDiseaseNodePanel                                       
//  |      * selectionMethod radioButton (group)
//  |      * selectFromListPanel OR generateFromRulesPanel
//  |      * selectButton
//  |  - buttonPanel
//  |    + computeButton
//  |    + cancelButton

public class computeTargetCombinationsDialog extends JDialog{
	private String CANCEL;
	private stringConstant staticString=new stringConstant();
	protected final static String GENERATE_DISEASE_NODE_LIST = "Generate disease node list";
	protected final static String CONFIRM_DISEASE_NODE = "Select disease node(s)";
	protected final static String SEARCH = "Search";
	protected final static String SELECT_NODE = "Select node";
	protected final static String DESELECT_NODE = "Deselect node";
	protected final static String SELECT_DISEASE_NODE = "Select disease node";
	protected final static String DESELECT_DISEASE_NODE = "Deselect disease node";
	protected final static String COMPUTE = "Compute";
	protected final static String SELECT_FROM_LIST = "Select from list";
	protected final static String GENERATE_FROM_RULES = "Generate from rules";
	protected final static String GENERAL_OUTCOME = "General outcome";
	protected final static String FILTER_ONLY = "Filter-only";
	protected final static String HALLMARK_BASED = "Hallmark-based";
	protected final static String NODE_BASED = "Node-based";
	protected final static String GENERAL_OUTCOME_PANEL = "General outcome panel";
	protected final static String FILTER_ONLY_PANEL = "Filter-only panel";
	protected final static String HALLMARK_BASED_PANEL = "Hallmark-based panel";
	protected final static String NODE_BASED_PANEL = "Node-based panel";
	protected final static String CYTOSTATIC = "Cytostatic (inhibit cell growth)";
	protected final static String CYTOTOXIC = "Cytotoxic (kill cell)";
	protected final static String SELECT_HALLMARK = "Select hallmark";
	protected final static String DESELECT_HALLMARK = "Deselect hallmark";
	protected final static String SELECT_ALL_DISEASE_NODE = "Select all disease node";
	private boolean performComputation=false;
	private boolean perturbedEssential=false, perturbedOutDegree=false;

	private JPanel settingPanel, buttonPanel, cardLayoutPanel, upperPanel, lowerPanel;
	private JPanel selectDiseaseNodePanel, selectTreatmentGoalPanel, selectTreatmentGoalLabelPanel, selectDiseaseNodeLabelPanel;
	private JPanel generalOutcomePanel, hallmarkBasedPanel, nodeBasedPanel, filterOnlyPanel, rankedHallmarkPanel;
	private JPanel foldChangePanel, mutationFreqPanel; 
	private JPanel selectOffTargetsRulePanel, selectOffTargetsRuleLabelPanel, perturbedEssentialPanel, perturbedOutDegreePanel;
	private JPanel influenceMaxPanel, suggestCombiSizePanel, combiSizePanel;
	private JLabel minCombiSize=new JLabel();
	private	JTextField searchNodeTextField, combiSizeTextField;
	private	JTextField mutationFreqTextField, foldChangeTextField;
	private	JTextField perturbedEssentialTextField, perturbedOutDegreeTextField;
	private JTextField jaccardThresholdHallmarkTextField, jaccardThresholdGeneralOutcomeTextField;
	private JComboBox<String> foldChangeOperatorComboBox;
	private JComboBox<String> influenceMaxComboBox;
	private JComboBox<String> treatmentGoalComboBox;
	private JCheckBox mutationFreqCheckBox, foldChangeCheckBox;
	private JCheckBox selectAllDiseaseNodeCheckBox=new JCheckBox(SELECT_ALL_DISEASE_NODE);
	private JCheckBox perturbedEssentialCheckBox, perturbedOutDegreeCheckBox;
	private JList<String> nodeList=new JList<String>();
	private JList<String> selectedNodeList=new JList<String>();
	private JList<String> hallmarkList=new JList<String>();
	private JList<String> selectedHallmarkList=new JList<String>();
	private JList<String> rankedHallmarkList=new JList<String>();
	private DefaultListModel<String> diseaseNodeModel = new DefaultListModel<String>();
	private JList<String> diseaseNodeList=new JList<String>(diseaseNodeModel);
	private JList<String> selectedDiseaseNodeList=new JList<String>();
	private JScrollPane nodeScrollPane=new JScrollPane();
	private JScrollPane selectedNodeScrollPane=new JScrollPane();
	private JScrollPane hallmarkScrollPane=new JScrollPane();
	private JScrollPane selectedHallmarkScrollPane=new JScrollPane();
	private JScrollPane diseaseNodeScrollPane=new JScrollPane();
	private JScrollPane selectedDiseaseNodeScrollPane=new JScrollPane();
	private JScrollPane rankedHallmarkScrollPane=new JScrollPane();
	private JButton selectNodeButton, deselectNodeButton, searchButton, selectHallmarkButton, deselectHallmarkButton;
	private JButton selectDiseaseNodeButton, deselectDiseaseNodeButton, confirmDiseaseNodeButton;
	private JButton generateDiseaseNodeListButton, computeButton, cancelButton;
	private JRadioButton cytostaticRButton=new JRadioButton(CYTOSTATIC);
	private JRadioButton cytotoxicRButton=new JRadioButton(CYTOTOXIC);
	private ButtonGroup generalOutcomeRButtonGroup=new ButtonGroup();

	private ArrayList<String> node=new ArrayList<String>();
	private ArrayList<String> selectedNode=new ArrayList<String>();
	private ArrayList<String> hallmark=new ArrayList<String>();
	private ArrayList<String> selectedHallmark=new ArrayList<String>();
	private ArrayList<String> diseaseNode=new ArrayList<String>();
	private ArrayList<String> selectedDiseaseNode=new ArrayList<String>();
	private ArrayList<String> rankedHallmark=new ArrayList<String>();
	private diseaseNode diseaseNodeDetails=new diseaseNode();

	private JLabel diseaseNodeSelectedLabel=new JLabel("Selected disease node(s):");
	private JTextPane diseaseNodeSelectedTextPane=new JTextPane();
	private JScrollPane diseaseNodeSelectedScrollPane=new JScrollPane();
	private JPanel diseaseNodeSelectedPanel;

	private String selectedView;
	private boolean mutationFreqRuleSelected, foldChangeRuleSelected;
	private double mutationFreqValue, foldChangeValue;
	private String mutationFreqRuleOperator;
	private String combiSize, perturbedEssentialWt, perturbedOutDegreeWt;
	private String method;
	private postgreSQL postgreDB;
	private JUNG_graph jungGraph;

	public computeTargetCombinationsDialog(postgreSQL DB, String view, JList<String> nodeList, JUNG_graph jGraph)
	{
		//initialize constants
		CANCEL=staticString.getCancel();
		for(int i=0; i<nodeList.getModel().getSize(); i++)
			node.add(nodeList.getModel().getElementAt(i));
		selectedView=view;
		mutationFreqRuleSelected=false;
		foldChangeRuleSelected=false;
		postgreDB=DB;
		jungGraph=jGraph;

		hallmark.add(staticString.getGUIProliferation());
		hallmark.add(staticString.getGUIGrowthRepressor());
		hallmark.add(staticString.getGUIApoptosis());
		hallmark.add(staticString.getGUIReplicativeImmortality());
		hallmark.add(staticString.getGUIAngiogenesis());
		hallmark.add(staticString.getGUIMetastasis());
		hallmark.add(staticString.getGUIMetabolism());
		hallmark.add(staticString.getGUIImmuneDestruction());
		hallmark.add(staticString.getGUIGenomeInstability());
		hallmark.add(staticString.getGUITumorPromotingInflammation());

		//set JDialog properties
		setTitle("Compute Target Combinations");
		setSize(975,650);
		setLocationRelativeTo(null);
		setLayout(new BorderLayout());
		//setResizable(false);
		setModal(true);

		//configure components in JDialog
		initializeComponents();
	}

	private ActionListener actionListener = new ActionListener() 
	{
		public void actionPerformed(ActionEvent act) {
			if (act.getActionCommand() == SEARCH)
				searchNode_actionPerformed();
			if (act.getActionCommand() == SELECT_NODE)
				selectNode_actionPerformed();
			if (act.getActionCommand() == DESELECT_NODE)
				deselectNode_actionPerformed();
			if (act.getActionCommand() == SELECT_DISEASE_NODE)
				selectDiseaseNode_actionPerformed();
			if (act.getActionCommand() == DESELECT_DISEASE_NODE)
				deselectDiseaseNode_actionPerformed();
			if (act.getActionCommand() == COMPUTE)
				computeCombinations_actionPerformed();
			if (act.getActionCommand() == GENERATE_DISEASE_NODE_LIST)
				generateDiseaseNodeList_actionPerformed();
			if (act.getActionCommand() == CANCEL)
				cancel_actionPerformed();
			if (act.getActionCommand() == SELECT_HALLMARK)
				selectHallmark_actionPerformed();
			if (act.getActionCommand() == DESELECT_HALLMARK)
				deselectHallmark_actionPerformed();
			if (act.getActionCommand() == CONFIRM_DISEASE_NODE)
				confirmDiseaseNode_actionPerformed();
		}
	};

	private void initializeComponents()
	{
		settingPanel=new JPanel();
		buttonPanel=new JPanel();
		cardLayoutPanel=new JPanel(new CardLayout());
		upperPanel=new JPanel();

		//create selectTreatmentGoalPanel
		selectTreatmentGoalPanel=new JPanel();
		selectTreatmentGoalPanel.setLayout(new BoxLayout(selectTreatmentGoalPanel, BoxLayout.Y_AXIS));
		selectTreatmentGoalPanel.setBorder(BorderFactory.createEtchedBorder());

		selectTreatmentGoalLabelPanel=new JPanel();
		selectTreatmentGoalLabelPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		JLabel selectTreatmentGoalLabel=new JLabel("Step 1: Select treatment goal");
		selectTreatmentGoalLabelPanel.add(selectTreatmentGoalLabel);

		//create cardLayoutPanel
		generalOutcomePanel=new JPanel();
		generalOutcomePanel.setLayout(new BoxLayout(generalOutcomePanel, BoxLayout.Y_AXIS));
		cytostaticRButton.setSelected(true);
		generalOutcomeRButtonGroup.add(cytostaticRButton);
		generalOutcomeRButtonGroup.add(cytotoxicRButton);
		JLabel jaccardThresholdGeneralOutcomeLabel=new JLabel("Select for nodes with Jaccard index <=");
		JPanel jaccardThresholdGeneralOutcomePanel=new JPanel();
		jaccardThresholdGeneralOutcomePanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		jaccardThresholdGeneralOutcomeTextField=new JTextField("0.7");
		jaccardThresholdGeneralOutcomeTextField.setPreferredSize(new Dimension(50,30));
		jaccardThresholdGeneralOutcomePanel.add(jaccardThresholdGeneralOutcomeLabel);
		jaccardThresholdGeneralOutcomePanel.add(jaccardThresholdGeneralOutcomeTextField);
		JPanel cytostaticRButtonPanel=new JPanel();
		cytostaticRButtonPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		cytostaticRButtonPanel.add(cytostaticRButton);
		JPanel cytotoxicRButtonPanel=new JPanel();
		cytotoxicRButtonPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		cytotoxicRButtonPanel.add(cytotoxicRButton);
		generalOutcomePanel.add(cytostaticRButtonPanel);
		generalOutcomePanel.add(cytotoxicRButtonPanel);
		generalOutcomePanel.add(jaccardThresholdGeneralOutcomePanel);
		cardLayoutPanel.add(generalOutcomePanel, GENERAL_OUTCOME_PANEL);
		//****************************
		hallmarkBasedPanel=new JPanel();
		hallmarkBasedPanel.setLayout(new BoxLayout(hallmarkBasedPanel, BoxLayout.Y_AXIS));
		JPanel hallmarkPanel=new JPanel();
		hallmarkPanel.setLayout(new BoxLayout(hallmarkPanel, BoxLayout.Y_AXIS));
		JPanel hallmarkLabelPanel=new JPanel();
		hallmarkLabelPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		JLabel hallmarkLabel=new JLabel("Hallmark list:");
		hallmarkLabelPanel.add(hallmarkLabel);
		hallmarkList=new JList<String>();
		hallmarkList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		String[] javaArr=hallmark.toArray(new String[hallmark.size()]);
		hallmarkList.setListData(javaArr);
		hallmarkScrollPane = new JScrollPane(hallmarkList);
		hallmarkScrollPane.setViewportView(hallmarkList);
		hallmarkScrollPane.setPreferredSize(new Dimension(200, 170));
		hallmarkScrollPane.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		hallmarkPanel.add(hallmarkLabelPanel);
		hallmarkPanel.add(hallmarkScrollPane);
		JPanel selectDeselectHallmarkPanel=new JPanel();
		selectDeselectHallmarkPanel.setLayout(new BoxLayout(selectDeselectHallmarkPanel, BoxLayout.Y_AXIS));
		selectHallmarkButton= new JButton(">");
		selectHallmarkButton.setSize(50, 20);
		selectHallmarkButton.setActionCommand(SELECT_HALLMARK);
		selectHallmarkButton.addActionListener(actionListener);
		selectHallmarkButton.setEnabled(true);
		JLabel emptyHallmarkButtonLabel=new JLabel(" ");
		emptyHallmarkButtonLabel.setSize(50, 60);
		deselectHallmarkButton= new JButton("<");
		deselectHallmarkButton.setSize(50, 20);
		deselectHallmarkButton.setActionCommand(DESELECT_HALLMARK);
		deselectHallmarkButton.addActionListener(actionListener);
		deselectHallmarkButton.setEnabled(false);
		selectDeselectHallmarkPanel.add(selectHallmarkButton);
		selectDeselectHallmarkPanel.add(emptyHallmarkButtonLabel);
		selectDeselectHallmarkPanel.add(deselectHallmarkButton);
		JPanel selectedHallmarkPanel=new JPanel();
		selectedHallmarkPanel.setLayout(new BoxLayout(selectedHallmarkPanel, BoxLayout.Y_AXIS));
		JPanel selectedHallmarkLabelPanel=new JPanel();
		selectedHallmarkLabelPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		JLabel selectedHallmarkLabel=new JLabel("Selected hallmark:");
		selectedHallmarkLabelPanel.add(selectedHallmarkLabel);
		selectedHallmarkList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		selectedHallmarkScrollPane = new JScrollPane(selectedHallmarkList);
		selectedHallmarkScrollPane.setViewportView(selectedHallmarkList);
		selectedHallmarkScrollPane.setPreferredSize(new Dimension(200, 170));
		selectedHallmarkScrollPane.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		selectedHallmarkPanel.add(selectedHallmarkLabelPanel);
		selectedHallmarkPanel.add(selectedHallmarkScrollPane);
		JPanel hallmarkSelectionPanel=new JPanel();
		hallmarkSelectionPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		hallmarkSelectionPanel.add(hallmarkPanel);
		hallmarkSelectionPanel.add(selectDeselectHallmarkPanel);
		hallmarkSelectionPanel.add(selectedHallmarkPanel);
		JLabel jaccardThresholdHallmarkLabel=new JLabel("Select for nodes with Jaccard index <=");
		JPanel jaccardThresholdHallmarkPanel=new JPanel();
		jaccardThresholdHallmarkPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		jaccardThresholdHallmarkTextField=new JTextField("0.8");
		jaccardThresholdHallmarkTextField.setPreferredSize(new Dimension(50,30));
		jaccardThresholdHallmarkPanel.add(jaccardThresholdHallmarkLabel);
		jaccardThresholdHallmarkPanel.add(jaccardThresholdHallmarkTextField);
		hallmarkBasedPanel.add(hallmarkSelectionPanel);
		hallmarkBasedPanel.add(jaccardThresholdHallmarkPanel);
		cardLayoutPanel.add(hallmarkBasedPanel, HALLMARK_BASED_PANEL);
		//****************************
		nodeBasedPanel=new JPanel();
		nodeBasedPanel.setLayout(new BoxLayout(nodeBasedPanel, BoxLayout.Y_AXIS));
		JPanel nodeBasedSelectionPanel=new JPanel();
		nodeBasedSelectionPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		JPanel nodePanel=new JPanel();
		nodePanel.setLayout(new BoxLayout(nodePanel, BoxLayout.Y_AXIS));
		JPanel nodeLabelPanel=new JPanel();
		nodeLabelPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		JLabel nodeLabel=new JLabel("Node list:");
		nodeLabelPanel.add(nodeLabel);
		nodeList=new JList<String>();
		nodeList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		javaArr=node.toArray(new String[node.size()]);
		nodeList.setListData(javaArr);
		nodeScrollPane = new JScrollPane(nodeList);
		nodeScrollPane.setViewportView(nodeList);
		nodeScrollPane.setPreferredSize(new Dimension(200, 170));
		nodeScrollPane.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		nodePanel.add(nodeLabelPanel);
		nodePanel.add(nodeScrollPane);
		JPanel selectDeselectNodePanel=new JPanel();
		selectDeselectNodePanel.setLayout(new BoxLayout(selectDeselectNodePanel, BoxLayout.Y_AXIS));
		selectNodeButton= new JButton(">");
		selectNodeButton.setSize(50, 20);
		selectNodeButton.setActionCommand(SELECT_NODE);
		selectNodeButton.addActionListener(actionListener);
		selectNodeButton.setEnabled(true);
		JLabel emptyNodeButtonLabel=new JLabel(" ");
		emptyNodeButtonLabel.setSize(50, 60);
		deselectNodeButton= new JButton("<");
		deselectNodeButton.setSize(50, 20);
		deselectNodeButton.setActionCommand(DESELECT_NODE);
		deselectNodeButton.addActionListener(actionListener);
		deselectNodeButton.setEnabled(false);
		selectDeselectNodePanel.add(selectNodeButton);
		selectDeselectNodePanel.add(emptyNodeButtonLabel);
		selectDeselectNodePanel.add(deselectNodeButton);
		JPanel selectedNodePanel=new JPanel();
		selectedNodePanel.setLayout(new BoxLayout(selectedNodePanel, BoxLayout.Y_AXIS));
		JPanel selectedNodeLabelPanel=new JPanel();
		selectedNodeLabelPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		JLabel selectedNodeLabel=new JLabel("Selected node:");
		selectedNodeLabelPanel.add(selectedNodeLabel);
		selectedNodeList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		selectedNodeScrollPane = new JScrollPane(selectedNodeList);
		selectedNodeScrollPane.setViewportView(selectedNodeList);
		selectedNodeScrollPane.setPreferredSize(new Dimension(200, 170));
		selectedNodeScrollPane.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		selectedNodePanel.add(selectedNodeLabelPanel);
		selectedNodePanel.add(selectedNodeScrollPane);
		JPanel searchNodePanel=new JPanel();
		searchNodePanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		searchNodeTextField = new JTextField();
		searchNodeTextField.setPreferredSize(new Dimension(150,30));
		searchButton = new JButton();
		searchButton.setSize(50, 20);
		searchButton.setText(SEARCH);
		searchButton.setActionCommand(SEARCH);
		searchButton.addActionListener(actionListener);
		searchNodePanel.add(searchNodeTextField);
		searchNodePanel.add(searchButton);
		nodeBasedSelectionPanel.add(nodePanel);
		nodeBasedSelectionPanel.add(selectDeselectNodePanel);
		nodeBasedSelectionPanel.add(selectedNodePanel);
		nodeBasedPanel.add(nodeBasedSelectionPanel);
		nodeBasedPanel.add(searchNodePanel);
		cardLayoutPanel.add(nodeBasedPanel, NODE_BASED_PANEL);
		//*************************************
		filterOnlyPanel=new JPanel();
		filterOnlyPanel.setLayout(new BoxLayout(filterOnlyPanel, BoxLayout.Y_AXIS));
		cardLayoutPanel.add(filterOnlyPanel, FILTER_ONLY_PANEL);
		//End cardLayout
		treatmentGoalComboBox=new JComboBox<String>();
		treatmentGoalComboBox.addItem(GENERAL_OUTCOME);
		treatmentGoalComboBox.addItem(HALLMARK_BASED);
		treatmentGoalComboBox.addItem(NODE_BASED);
		treatmentGoalComboBox.addItem(FILTER_ONLY);
		treatmentGoalComboBox.setSelectedIndex(0);
		CardLayout cl=(CardLayout)(cardLayoutPanel.getLayout());
		cl.show(cardLayoutPanel, GENERAL_OUTCOME_PANEL);
		treatmentGoalComboBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e){
				Object item=e.getItem();
				if(e.getStateChange()==ItemEvent.SELECTED)
				{
					if(String.valueOf(item).compareTo(GENERAL_OUTCOME)==0)
					{
						CardLayout cl=(CardLayout)(cardLayoutPanel.getLayout());
						cl.show(cardLayoutPanel, GENERAL_OUTCOME_PANEL);
					}
					else if(String.valueOf(item).compareTo(HALLMARK_BASED)==0)
					{
						CardLayout cl=(CardLayout)(cardLayoutPanel.getLayout());
						cl.show(cardLayoutPanel, HALLMARK_BASED_PANEL);
					}
					else if(String.valueOf(item).compareTo(NODE_BASED)==0)
					{
						CardLayout cl=(CardLayout)(cardLayoutPanel.getLayout());
						cl.show(cardLayoutPanel, NODE_BASED_PANEL);
					}
					else
					{
						CardLayout cl=(CardLayout)(cardLayoutPanel.getLayout());
						cl.show(cardLayoutPanel, FILTER_ONLY_PANEL);
					}
				}
			}
		});
		//********Optional filter panel*****************
		JPanel optionalFilterPanel=new JPanel();
		optionalFilterPanel.setLayout(new BoxLayout(optionalFilterPanel, BoxLayout.Y_AXIS));
		JPanel optionalFilterLabelPanel=new JPanel();
		optionalFilterLabelPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		JLabel optionalFilterLabel=new JLabel("Optional filter:");
		optionalFilterLabelPanel.add(optionalFilterLabel);
		foldChangePanel=new JPanel();
		foldChangePanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		foldChangeCheckBox=new JCheckBox("Fold change >= ");
		foldChangeTextField=new JTextField("2.33");
		foldChangeTextField.setPreferredSize(new Dimension(50,30));
		foldChangeOperatorComboBox=new JComboBox<String>();
		foldChangeOperatorComboBox.addItem("AND");
		foldChangeOperatorComboBox.addItem("OR");
		foldChangeOperatorComboBox.setSelectedIndex(0);
		foldChangePanel.add(foldChangeCheckBox);
		foldChangePanel.add(foldChangeTextField);
		foldChangePanel.add(foldChangeOperatorComboBox);
		mutationFreqPanel=new JPanel();
		mutationFreqPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		mutationFreqCheckBox=new JCheckBox("Mutation frequency % >= ");
		mutationFreqTextField=new JTextField("2");
		mutationFreqTextField.setPreferredSize(new Dimension(50,30));
		JLabel emptyLabel1=new JLabel("               ");
		emptyLabel1.setSize(200,20);
		generateDiseaseNodeListButton = new JButton();
		generateDiseaseNodeListButton.setSize(50, 20);
		generateDiseaseNodeListButton.setText(GENERATE_DISEASE_NODE_LIST);
		generateDiseaseNodeListButton.setActionCommand(GENERATE_DISEASE_NODE_LIST);
		generateDiseaseNodeListButton.addActionListener(actionListener);
		mutationFreqPanel.add(mutationFreqCheckBox);
		mutationFreqPanel.add(mutationFreqTextField);
		mutationFreqPanel.add(emptyLabel1);
		mutationFreqPanel.add(generateDiseaseNodeListButton);
		optionalFilterPanel.add(optionalFilterLabelPanel);
		optionalFilterPanel.add(foldChangePanel);
		optionalFilterPanel.add(mutationFreqPanel);
		//***************************************************
		JPanel treatmentGoalComboBoxPanel=new JPanel();
		treatmentGoalComboBoxPanel.setLayout(new BoxLayout(treatmentGoalComboBoxPanel, BoxLayout.Y_AXIS));
		treatmentGoalComboBoxPanel.add(treatmentGoalComboBox);
		treatmentGoalComboBoxPanel.add(cardLayoutPanel);
		treatmentGoalComboBoxPanel.add(optionalFilterPanel);
		//***************************************************
		selectTreatmentGoalPanel.add(selectTreatmentGoalLabelPanel);
		selectTreatmentGoalPanel.add(treatmentGoalComboBoxPanel);

		//create selectDiseaseNodePanel
		selectDiseaseNodePanel=new JPanel();
		selectDiseaseNodePanel.setLayout(new BoxLayout(selectDiseaseNodePanel, BoxLayout.Y_AXIS));
		selectDiseaseNodePanel.setBorder(BorderFactory.createEtchedBorder());
		selectDiseaseNodeLabelPanel=new JPanel();
		selectDiseaseNodeLabelPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		JLabel selectDiseaseNodeLabel=new JLabel("Step 2: Select disease node(s)");
		selectDiseaseNodeLabelPanel.add(selectDiseaseNodeLabel);
		JPanel diseaseNodePanel=new JPanel();
		diseaseNodePanel.setLayout(new BoxLayout(diseaseNodePanel, BoxLayout.Y_AXIS));
		JPanel diseaseNodeLabelPanel=new JPanel();
		diseaseNodeLabelPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		JLabel diseaseNodeListLabel=new JLabel("Disease node(s) list:");
		diseaseNodeLabelPanel.add(diseaseNodeListLabel);
		diseaseNodeList=new JList<String>();
		diseaseNodeList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		diseaseNodeScrollPane = new JScrollPane(diseaseNodeList);
		diseaseNodeScrollPane.setViewportView(diseaseNodeList);
		diseaseNodeScrollPane.setPreferredSize(new Dimension(200, 170));
		diseaseNodeScrollPane.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		diseaseNodePanel.add(diseaseNodeLabelPanel);
		diseaseNodePanel.add(diseaseNodeScrollPane);
		JPanel selectDeselectDiseaseNodePanel=new JPanel();
		selectDeselectDiseaseNodePanel.setLayout(new BoxLayout(selectDeselectDiseaseNodePanel, BoxLayout.Y_AXIS));
		selectDiseaseNodeButton= new JButton(">");
		selectDiseaseNodeButton.setSize(50, 20);
		selectDiseaseNodeButton.setActionCommand(SELECT_DISEASE_NODE);
		selectDiseaseNodeButton.addActionListener(actionListener);
		selectDiseaseNodeButton.setEnabled(true);
		JLabel emptyDiseaseNodeButtonLabel=new JLabel(" ");
		emptyDiseaseNodeButtonLabel.setSize(50, 60);
		deselectDiseaseNodeButton= new JButton("<");
		deselectDiseaseNodeButton.setSize(50, 20);
		deselectDiseaseNodeButton.setActionCommand(DESELECT_DISEASE_NODE);
		deselectDiseaseNodeButton.addActionListener(actionListener);
		deselectDiseaseNodeButton.setEnabled(false);
		selectDeselectDiseaseNodePanel.add(selectDiseaseNodeButton);
		selectDeselectDiseaseNodePanel.add(emptyDiseaseNodeButtonLabel);
		selectDeselectDiseaseNodePanel.add(deselectDiseaseNodeButton);
		JPanel selectedDiseaseNodePanel=new JPanel();
		selectedDiseaseNodePanel.setLayout(new BoxLayout(selectedDiseaseNodePanel, BoxLayout.Y_AXIS));
		JPanel selectedDiseaseNodeLabelPanel=new JPanel();
		selectedDiseaseNodeLabelPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		JLabel selectedDiseaseNodeLabel=new JLabel("Selected disease node(s):");
		selectedDiseaseNodeLabelPanel.add(selectedDiseaseNodeLabel);
		selectedDiseaseNodeList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		selectedDiseaseNodeScrollPane = new JScrollPane(selectedDiseaseNodeList);
		selectedDiseaseNodeScrollPane.setViewportView(selectedDiseaseNodeList);
		selectedDiseaseNodeScrollPane.setPreferredSize(new Dimension(200, 170));
		selectedDiseaseNodeScrollPane.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		selectedDiseaseNodePanel.add(selectedDiseaseNodeLabelPanel);
		selectedDiseaseNodePanel.add(selectedDiseaseNodeScrollPane);
		JPanel diseaseNodeSelectionPanel=new JPanel();
		diseaseNodeSelectionPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		diseaseNodeSelectionPanel.add(diseaseNodePanel);
		diseaseNodeSelectionPanel.add(selectDeselectDiseaseNodePanel);
		diseaseNodeSelectionPanel.add(selectedDiseaseNodePanel);
		//****************************************
		selectAllDiseaseNodeCheckBox.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				boolean selected = selectAllDiseaseNodeCheckBox.isSelected();
				if(selected)
				{
					for(int i=0; i<diseaseNode.size(); i++)
					{
						String n=diseaseNode.get(i);
						if(selectedDiseaseNode.contains(n)==false)
							selectedDiseaseNode.add(n);
						selectedDiseaseNodeList.setListData(selectedDiseaseNode.toArray(new String[selectedDiseaseNode.size()]));
					}
					updateDiseaseNodeButtons();
				}
			}
		});
		JPanel selectAllDiseaseNodeCheckBoxPanel=new JPanel();
		selectAllDiseaseNodeCheckBoxPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		confirmDiseaseNodeButton = new JButton();
		confirmDiseaseNodeButton.setSize(50, 20);
		confirmDiseaseNodeButton.setText(CONFIRM_DISEASE_NODE);
		confirmDiseaseNodeButton.setActionCommand(CONFIRM_DISEASE_NODE);
		confirmDiseaseNodeButton.addActionListener(actionListener);
		confirmDiseaseNodeButton.setEnabled(false);
		selectAllDiseaseNodeCheckBoxPanel.add(selectAllDiseaseNodeCheckBox);
		selectAllDiseaseNodeCheckBoxPanel.add(new JLabel("                                       "));
		selectAllDiseaseNodeCheckBoxPanel.add(confirmDiseaseNodeButton);
		//selectedDiseaseNode Panel
		diseaseNodeSelectedPanel=new JPanel();
		diseaseNodeSelectedPanel.setLayout(new BoxLayout(diseaseNodeSelectedPanel, BoxLayout.Y_AXIS));
		diseaseNodeSelectedTextPane.setPreferredSize(new Dimension(225,110));
		//diseaseNodeSelectedTextPane.setEnabled(false);
		diseaseNodeSelectedScrollPane = new JScrollPane(diseaseNodeSelectedTextPane);
		diseaseNodeSelectedScrollPane.setViewportView(diseaseNodeSelectedTextPane);
		diseaseNodeSelectedScrollPane.setPreferredSize(new Dimension(225,110));
		diseaseNodeSelectedScrollPane.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		JPanel diseaseNodeSelectedLabelPanel=new JPanel();
		diseaseNodeSelectedLabelPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		diseaseNodeSelectedLabelPanel.add(diseaseNodeSelectedLabel);
		diseaseNodeSelectedPanel.add(diseaseNodeSelectedLabelPanel);
		diseaseNodeSelectedPanel.add(diseaseNodeSelectedScrollPane);
		//ranked hallmark
		rankedHallmarkPanel=new JPanel();
		rankedHallmarkPanel.setLayout(new BoxLayout(rankedHallmarkPanel, BoxLayout.Y_AXIS));
		rankedHallmarkScrollPane = new JScrollPane(rankedHallmarkList);
		rankedHallmarkScrollPane.setViewportView(rankedHallmarkList);
		rankedHallmarkScrollPane.setPreferredSize(new Dimension(225,110));
		rankedHallmarkScrollPane.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		JLabel rankedHallmarkLabel=new JLabel("Ranked hallmark:");
		JPanel rankedHallmarkLabelPanel=new JPanel();
		rankedHallmarkLabelPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		rankedHallmarkLabelPanel.add(rankedHallmarkLabel);
		rankedHallmarkPanel.add(rankedHallmarkLabelPanel);
		rankedHallmarkPanel.add(rankedHallmarkScrollPane);
		JPanel selectDiseaseNodeResultPanel=new JPanel();
		selectDiseaseNodeResultPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		selectDiseaseNodeResultPanel.add(diseaseNodeSelectedPanel);
		selectDiseaseNodeResultPanel.add(rankedHallmarkPanel);
		selectDiseaseNodePanel.add(selectDiseaseNodeLabelPanel);
		selectDiseaseNodePanel.add(diseaseNodeSelectionPanel);
		selectDiseaseNodePanel.add(selectAllDiseaseNodeCheckBoxPanel);
		selectDiseaseNodePanel.add(selectDiseaseNodeResultPanel);

		//upperPanel
		upperPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		upperPanel.add(selectTreatmentGoalPanel);
		upperPanel.add(selectDiseaseNodePanel);

		//create selectOffTargetsRulePanel
		selectOffTargetsRulePanel=new JPanel();
		selectOffTargetsRulePanel.setLayout(new BoxLayout(selectOffTargetsRulePanel, BoxLayout.Y_AXIS));
		selectOffTargetsRulePanel.setBorder(BorderFactory.createEtchedBorder());

		selectOffTargetsRuleLabelPanel=new JPanel();
		selectOffTargetsRuleLabelPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		JLabel selectOffTargetsRuleLabel=new JLabel("Step 3: Select off-target rule(s) and combination size");
		selectOffTargetsRuleLabelPanel.add(selectOffTargetsRuleLabel);

		JPanel offTargetPanel=new JPanel();
		offTargetPanel.setLayout(new BoxLayout(offTargetPanel, BoxLayout.Y_AXIS));
		offTargetPanel.setBorder(BorderFactory.createEtchedBorder());
		JLabel offTargetLabel=new JLabel("Off-target rules:");
		JPanel offTargetLabelPanel=new JPanel();
		offTargetLabelPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		offTargetLabelPanel.add(offTargetLabel);
		perturbedEssentialPanel=new JPanel();
		perturbedEssentialPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		perturbedEssentialCheckBox=new JCheckBox("Essential nodes perturbed");
		perturbedEssentialCheckBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e){
				if(e.getStateChange()==ItemEvent.SELECTED)
					perturbedEssential=true;
				else
					perturbedEssential=false;
				if(perturbedEssential==false && perturbedOutDegree==false)
					computeButton.setEnabled(false);
				else
					computeButton.setEnabled(true);
			}
		});
		JLabel emptyLabel2=new JLabel("                                      Weightage ratio: [0-1]");
		emptyLabel2.setSize(200,20);
		perturbedEssentialTextField=new JTextField("0.5");
		perturbedEssentialTextField.setPreferredSize(new Dimension(50,30));
		perturbedEssentialPanel.add(perturbedEssentialCheckBox);
		perturbedEssentialPanel.add(emptyLabel2);
		perturbedEssentialPanel.add(perturbedEssentialTextField);

		perturbedOutDegreePanel=new JPanel();
		perturbedOutDegreePanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		perturbedOutDegreeCheckBox=new JCheckBox("Weighted out degree of perturbed nodes");
		perturbedOutDegreeCheckBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e){
				if(e.getStateChange()==ItemEvent.SELECTED)
					perturbedOutDegree=true;
				else
					perturbedOutDegree=false;
				if(perturbedEssential==false && perturbedOutDegree==false)
					computeButton.setEnabled(false);
				else
					computeButton.setEnabled(true);
			}
		});
		JLabel emptyLabel3=new JLabel("            Weightage ratio: [0-1]");
		emptyLabel3.setSize(200,20);
		perturbedOutDegreeTextField=new JTextField("0.5");
		perturbedOutDegreeTextField.setPreferredSize(new Dimension(50,30));
		perturbedOutDegreePanel.add(perturbedOutDegreeCheckBox);
		perturbedOutDegreePanel.add(emptyLabel3);
		perturbedOutDegreePanel.add(perturbedOutDegreeTextField);
		offTargetPanel.add(offTargetLabelPanel);
		offTargetPanel.add(perturbedEssentialPanel);
		offTargetPanel.add(perturbedOutDegreePanel);
		
		JPanel combinationSizePanel=new JPanel();
		combinationSizePanel.setLayout(new BoxLayout(combinationSizePanel, BoxLayout.Y_AXIS));
		combinationSizePanel.setBorder(BorderFactory.createEtchedBorder());
		combinationSizePanel.setPreferredSize(new Dimension(200,110));
		JPanel combinationSizeLabelPanel=new JPanel();
		combinationSizeLabelPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		JLabel combinationSizeLabel=new JLabel("Target combination size:");
		combinationSizeLabelPanel.add(combinationSizeLabel);
		suggestCombiSizePanel=new JPanel();
		suggestCombiSizePanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		JLabel suggestCombiSizeLabel=new JLabel("Suggested minimum size= ");
		suggestCombiSizeLabel.setSize(200,20);
		suggestCombiSizePanel.add(suggestCombiSizeLabel);
		suggestCombiSizePanel.add(minCombiSize);
		combiSizePanel=new JPanel();
		combiSizePanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		JLabel combiSizeLabel=new JLabel("Required size ");
		combiSizeTextField=new JTextField(minCombiSize.getText());
		combiSizeTextField.setPreferredSize(new Dimension(50,30));
		combiSizePanel.add(combiSizeLabel);
		combiSizePanel.add(combiSizeTextField);
		combinationSizePanel.add(combinationSizeLabelPanel);
		//combinationSizePanel.add(suggestCombiSizePanel);
		combinationSizePanel.add(combiSizePanel);
		
		influenceMaxPanel=new JPanel();
		influenceMaxPanel.setLayout(new BoxLayout(influenceMaxPanel, BoxLayout.Y_AXIS));
		influenceMaxPanel.setBorder(BorderFactory.createEtchedBorder());
		influenceMaxPanel.setPreferredSize(new Dimension(250,110));
		JLabel influenceMaxLabel=new JLabel("Target combinations computation:");
		JPanel influenceMaxLabelPanel=new JPanel();
		influenceMaxLabelPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		influenceMaxLabelPanel.add(influenceMaxLabel);
		influenceMaxComboBox=new JComboBox<String>();
		influenceMaxComboBox.addItem("Greedy (Heuristics)");
		influenceMaxComboBox.addItem("Random");
		influenceMaxComboBox.addItem("MCSA");
		influenceMaxComboBox.addItem("SA (Heuristics)");
		influenceMaxComboBox.setSelectedIndex(0);
		JPanel influenceMaxComboBoxPanel=new JPanel();
		influenceMaxComboBoxPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		influenceMaxComboBoxPanel.add(influenceMaxComboBox);
		influenceMaxPanel.add(influenceMaxLabelPanel);
		influenceMaxPanel.add(influenceMaxComboBoxPanel);
		
		lowerPanel=new JPanel();
		lowerPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		lowerPanel.add(offTargetPanel);
		lowerPanel.add(combinationSizePanel);
		lowerPanel.add(influenceMaxPanel);
		
		selectOffTargetsRulePanel.add(selectOffTargetsRuleLabelPanel);
		selectOffTargetsRulePanel.add(lowerPanel);
		
		settingPanel.setLayout(new BoxLayout(settingPanel, BoxLayout.Y_AXIS));
		settingPanel.add(upperPanel);
		settingPanel.add(selectOffTargetsRulePanel);

		//create buttonPanel
		computeButton = new JButton();
		computeButton.setSize(50, 20);
		computeButton.setText(COMPUTE);
		computeButton.setActionCommand(COMPUTE);
		computeButton.addActionListener(actionListener);
		computeButton.setEnabled(false);

		cancelButton = new JButton();
		cancelButton.setSize(50, 20);
		cancelButton.setText(CANCEL);
		cancelButton.setActionCommand(CANCEL);
		cancelButton.addActionListener(actionListener);

		buttonPanel= new JPanel();
		buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
		buttonPanel.add(computeButton);
		buttonPanel.add(cancelButton);

		setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));
		add(settingPanel);
		add(buttonPanel);
	}

	private void searchNode_actionPerformed() 
	{
		String node=searchNodeTextField.getText();
		int index = nodeList.getNextMatch(node, 0, Position.Bias.Forward);
		System.out.println("node:"+node);
		if (index != -1)
			nodeList.setSelectedValue(node,true);
	}

	private void selectHallmark_actionPerformed(){
		int[] selectedIndices=hallmarkList.getSelectedIndices();
		for(int i=0; i<selectedIndices.length; i++)
		{
			String n=hallmark.get(selectedIndices[i]);
			if(selectedHallmark.contains(n)==false)
				selectedHallmark.add(n);
			selectedHallmarkList.setListData(selectedHallmark.toArray(new String[selectedHallmark.size()]));
		}
		updateHallmarkButtons();
	}

	private void deselectHallmark_actionPerformed(){
		int[] selectedIndices=selectedHallmarkList.getSelectedIndices();
		if(selectedIndices.length>0)
		{
			for(int i=0; i<selectedIndices.length; i++)
			{
				String n=selectedHallmark.get(selectedIndices[i]);
				if(selectedHallmark.contains(n)==true)
					selectedHallmark.remove(n);
				selectedHallmarkList.setListData(selectedHallmark.toArray(new String[selectedHallmark.size()]));
			}
		}
		updateHallmarkButtons();
	}

	private void updateHallmarkButtons()
	{
		if(selectedHallmarkList.getModel().getSize()==0)
		{
			deselectHallmarkButton.setEnabled(false);
			selectHallmarkButton.setEnabled(true);
		}
		else if(selectedHallmarkList.getModel().getSize()==hallmark.size())
		{
			deselectHallmarkButton.setEnabled(true);
			selectHallmarkButton.setEnabled(false);
		}
		else
		{
			deselectHallmarkButton.setEnabled(true);
			selectHallmarkButton.setEnabled(true);
		}
	}

	private void selectNode_actionPerformed(){
		int[] selectedIndices=nodeList.getSelectedIndices();
		for(int i=0; i<selectedIndices.length; i++)
		{
			String n=node.get(selectedIndices[i]);
			if(selectedNode.contains(n)==false)
				selectedNode.add(n);
			selectedNodeList.setListData(selectedNode.toArray(new String[selectedNode.size()]));
		}
		updateNodeButtons();
	}

	private void deselectNode_actionPerformed(){
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
		updateNodeButtons();
	}

	private void updateNodeButtons()
	{
		if(selectedNodeList.getModel().getSize()==0)
		{
			deselectNodeButton.setEnabled(false);
			selectNodeButton.setEnabled(true);
		}
		else if(selectedNodeList.getModel().getSize()==node.size())
		{
			deselectNodeButton.setEnabled(true);
			selectNodeButton.setEnabled(false);
		}
		else
		{
			deselectNodeButton.setEnabled(true);
			selectNodeButton.setEnabled(true);
		}
	}

	private void selectDiseaseNode_actionPerformed(){
		int[] selectedIndices=diseaseNodeList.getSelectedIndices();
		for(int i=0; i<selectedIndices.length; i++)
		{
			String n=diseaseNode.get(selectedIndices[i]);
			if(selectedDiseaseNode.contains(n)==false)
				selectedDiseaseNode.add(n);
			selectedDiseaseNodeList.setListData(selectedDiseaseNode.toArray(new String[selectedDiseaseNode.size()]));
		}
		updateDiseaseNodeButtons();
	}

	private void deselectDiseaseNode_actionPerformed(){
		int[] selectedIndices=selectedDiseaseNodeList.getSelectedIndices();
		if(selectedIndices.length>0)
		{
			for(int i=0; i<selectedIndices.length; i++)
			{
				String n=selectedDiseaseNode.get(selectedIndices[i]);
				if(selectedDiseaseNode.contains(n)==true)
					selectedDiseaseNode.remove(n);
				selectedDiseaseNodeList.setListData(selectedDiseaseNode.toArray(new String[selectedDiseaseNode.size()]));
			}
		}
		updateDiseaseNodeButtons();
	}

	private void updateDiseaseNodeButtons()
	{
		if(selectedDiseaseNodeList.getModel().getSize()==0)
		{
			deselectDiseaseNodeButton.setEnabled(false);
			selectDiseaseNodeButton.setEnabled(true);
			selectAllDiseaseNodeCheckBox.setSelected(false);
			confirmDiseaseNodeButton.setEnabled(false);
		}
		else if(selectedDiseaseNodeList.getModel().getSize()==diseaseNode.size())
		{
			deselectDiseaseNodeButton.setEnabled(true);
			selectDiseaseNodeButton.setEnabled(false);
			selectAllDiseaseNodeCheckBox.setSelected(true);
			confirmDiseaseNodeButton.setEnabled(true);
		}
		else
		{
			deselectDiseaseNodeButton.setEnabled(true);
			selectDiseaseNodeButton.setEnabled(true);
			selectAllDiseaseNodeCheckBox.setSelected(false);
			confirmDiseaseNodeButton.setEnabled(true);
		}
	}

	private void confirmDiseaseNode_actionPerformed(){
		String selectedDiseaseNodeStr="";

		for(int i=0; i<selectedDiseaseNode.size(); i++)
		{
			selectedDiseaseNodeStr=selectedDiseaseNodeStr+selectedDiseaseNode.get(i);
			if(i<selectedDiseaseNode.size()-1)
				selectedDiseaseNodeStr=selectedDiseaseNodeStr+", ";
			if(i!=0 && i%4==0)
				selectedDiseaseNodeStr=selectedDiseaseNodeStr+"\n";
		}
		appendToPane(diseaseNodeSelectedTextPane, selectedDiseaseNodeStr, Color.BLACK);
		//rank hallmark based on selected disease nodes
		ArrayList<Integer> hallmarkFreq=new ArrayList<Integer>();
		for(int i=0; i<hallmark.size(); i++)
			hallmarkFreq.add(0);
		for(int i=0; i<selectedDiseaseNode.size(); i++)
		{
			ArrayList<Integer> hallmarkOfNode=postgreDB.getNodeHallmark(selectedDiseaseNode.get(i));
			int count;
			for(int j=0; j<hallmarkOfNode.size(); j++)
			{
				count=hallmarkFreq.get(j);
				hallmarkFreq.set(j,count+hallmarkOfNode.get(j));
			}
		}
		rankedHallmark=new ArrayList<String>();
		ArrayList<String> tmpHallmarkList=new ArrayList<String>(); 
		ArrayList<Double> tmpHallmarkValList=new ArrayList<Double>();
		for(int i=0; i<hallmarkFreq.size(); i++)
		{
			if(hallmarkFreq.get(i)>0)
			{
				tmpHallmarkList.add(hallmark.get(i));
				tmpHallmarkValList.add(Double.valueOf(hallmarkFreq.get(i)));
			}
		}
		rankedHallmark=postgreDB.sortInDecreasing(tmpHallmarkList, tmpHallmarkValList, true, "RETURN_ALL", 0.0);
		String[] javaArr=rankedHallmark.toArray(new String[rankedHallmark.size()]);
		rankedHallmarkList.setListData(javaArr);
		
		jungGraph.findPathsToDiseaseNode(selectedDiseaseNode);
		ArrayList<nodePathCommon> npc=jungGraph.getNodePathCommon();
		if(npc!=null && npc.size()>0)
			minCombiSize.setText(String.valueOf(npc.size()));
		else
			minCombiSize.setText("0");
		//combiSizeTextField.setText(minCombiSize.getText());
		combiSizeTextField.setText(String.valueOf(selectedDiseaseNode.size()));
	}

	private void generateDiseaseNodeList_actionPerformed(){
		String treatmentGoal=treatmentGoalComboBox.getSelectedItem().toString();
		boolean proceed=true;
		//if treatmentGoal is hallmark-based, check at least one hallmark selected
		//if treatmentGoal is node-based, check at least one node selected
		//if treatmentGoal is filter-only, check at least one filter selected
		if(treatmentGoal.compareTo(GENERAL_OUTCOME)==0)
		{
			String jaccardThresholdGeneralOutcome=jaccardThresholdGeneralOutcomeTextField.getText();
			if(jaccardThresholdGeneralOutcome==null || isDecimal(jaccardThresholdGeneralOutcome)==false)
			{
				JOptionPane.showMessageDialog(new JFrame(), "Please enter valid threshold (decimal value in range [0-1]).", "Error", JOptionPane.ERROR_MESSAGE);
				proceed=false;
			}
			else
				selectDiseaseNodes_generalOutcome();
		}
		if(treatmentGoal.compareTo(HALLMARK_BASED)==0)
		{
			String jaccardThresholdHallmark=jaccardThresholdHallmarkTextField.getText();
			if(selectedHallmarkList.getModel().getSize()==0)
			{
				JOptionPane.showMessageDialog(new JFrame(), "Please select at least one hallmark.", "Error", JOptionPane.ERROR_MESSAGE);
				proceed=false;
			}
			else if(jaccardThresholdHallmark==null || isDecimal(jaccardThresholdHallmark)==false)
			{
				JOptionPane.showMessageDialog(new JFrame(), "Please enter valid threshold (decimal value in range [0-1]).", "Error", JOptionPane.ERROR_MESSAGE);
				proceed=false;
			}
			else
				selectDiseaseNodes_hallmarkBased();
		}
		if(treatmentGoal.compareTo(NODE_BASED)==0)
		{
			if(selectedNodeList.getModel().getSize()==0)
			{
				JOptionPane.showMessageDialog(new JFrame(), "Please select at least one node.", "Error", JOptionPane.ERROR_MESSAGE);
				proceed=false;
			}
			else
				selectDiseaseNodes_nodeBased();
		}
		if(treatmentGoal.compareTo(FILTER_ONLY)==0)
		{
			if(foldChangeCheckBox.isSelected()==false && mutationFreqCheckBox.isSelected()==false)
			{
				JOptionPane.showMessageDialog(new JFrame(), "Please select at least one optional filter.", "Error", JOptionPane.ERROR_MESSAGE);
				proceed=false;
			}
			else
				selectDiseaseNodes_filterOnly();
		}
		System.out.println("proceed:"+proceed);
		diseaseNodeDetails.print();
		System.out.println("diseaseNodeDetails size:"+diseaseNodeDetails.size());
		if(proceed==true)
		{
			diseaseNode=new ArrayList<String>();
			diseaseNodeModel.clear();
			selectedDiseaseNode=new ArrayList<String>();
			selectedDiseaseNodeList.setListData(selectedDiseaseNode.toArray(new String[selectedDiseaseNode.size()]));
			selectAllDiseaseNodeCheckBox.setSelected(false);
			appendToPane(diseaseNodeSelectedTextPane, "", Color.BLACK);
			rankedHallmark=new ArrayList<String>();
			rankedHallmarkList.setListData(rankedHallmark.toArray(new String[rankedHallmark.size()]));
			for(int i=0; i<diseaseNodeDetails.size(); i++)
			{
				System.out.println(diseaseNodeDetails.getNodeAtIndex(i));
				diseaseNode.add(diseaseNodeDetails.getNodeAtIndex(i));
				diseaseNodeModel.addElement(diseaseNodeDetails.getNodeAtIndex(i));
			}
			diseaseNodeList.setModel(diseaseNodeModel);
		}
	}

	private void computeCombinations_actionPerformed(){
		performComputation=true;
		combiSize=combiSizeTextField.getText();
		perturbedEssentialWt=perturbedEssentialTextField.getText();
		perturbedOutDegreeWt=perturbedOutDegreeTextField.getText();
		method=influenceMaxComboBox.getSelectedItem().toString();
		if(combiSize!=null && isInteger(combiSize)==true)
		{
			if(perturbedEssential==true && perturbedOutDegree==true)
			{
				if(perturbedEssentialWt!=null && isDecimal(perturbedEssentialWt)==true &&
						perturbedOutDegreeWt!=null && isDecimal(perturbedOutDegreeWt)==true)
				{
					Double essentialWt=Double.parseDouble(perturbedEssentialWt);
					Double outDegreeWt=Double.parseDouble(perturbedOutDegreeWt);
					if(essentialWt+outDegreeWt!=Double.parseDouble("1"))
						JOptionPane.showMessageDialog(new JFrame(), "Please ensure that the sum of the two weights equals 1.", "Error", JOptionPane.ERROR_MESSAGE);
					else
						setVisible(false);
				}
				else
					JOptionPane.showMessageDialog(new JFrame(), "Please enter valid weight(s) (decimal value in range [0-1]).", "Error", JOptionPane.ERROR_MESSAGE);
			}
			else if(perturbedEssential==true)
			{
				if(perturbedEssentialWt!=null && isDecimal(perturbedEssentialWt)==true)
					setVisible(false);
				else
					JOptionPane.showMessageDialog(new JFrame(), "Please enter valid weight(s) (decimal value in range [0-1]).", "Error", JOptionPane.ERROR_MESSAGE);
			}
			else
			{
				if(perturbedOutDegreeWt!=null && isDecimal(perturbedOutDegreeWt)==true)
					setVisible(false);
				else
					JOptionPane.showMessageDialog(new JFrame(), "Please enter valid weight(s) (decimal value in range [0-1]).", "Error", JOptionPane.ERROR_MESSAGE);
			}
		}
		else
			JOptionPane.showMessageDialog(new JFrame(), "Please enter a valid combination size (integer value >=1).", "Error", JOptionPane.ERROR_MESSAGE);
	}

	private boolean isDecimal(String str)  
	{  
		if(str.matches("\\d+(\\.\\d+)?"))
		{
			double d=Double.valueOf(str);
			if(d>=0 && d<=1)
				return true;
			else 
				return false;
		}
		else 
			return false;
	}

	private boolean isInteger(String str)  
	{  
		if(str.matches("\\d+?"))
		{
			int i=Integer.parseInt(str);
			if(i>=1)
				return true;
			else 
				return false;
		}
		else 
			return false;
	}

	private void selectDiseaseNodes_generalOutcome(){
		diseaseNode=new ArrayList<String>();
		String jaccardThresholdGeneralOutcome=jaccardThresholdGeneralOutcomeTextField.getText();
		System.out.println("jaccardThresholdGeneralOutcome:"+jaccardThresholdGeneralOutcome);
		ArrayList<String> generalOutcomeHallmark=new ArrayList<String>();
		if(cytostaticRButton.isSelected()==true)
		{
			generalOutcomeHallmark.add(staticString.getGUIProliferation());
			generalOutcomeHallmark.add(staticString.getGUIGrowthRepressor());
			generalOutcomeHallmark.add(staticString.getGUIReplicativeImmortality());
			generalOutcomeHallmark.add(staticString.getGUIAngiogenesis());
			generalOutcomeHallmark.add(staticString.getGUIMetastasis());
			generalOutcomeHallmark.add(staticString.getGUIMetabolism());
			//generalOutcomeHallmark.add(staticString.getGUIGenomeInstability());
			//generalOutcomeHallmark.add(staticString.getGUITumorPromotingInflammation());
		}
		else
		{
			generalOutcomeHallmark.add(staticString.getGUIApoptosis());
			generalOutcomeHallmark.add(staticString.getGUIImmuneDestruction());
		}
		//get filter information
		mutationFreqRuleSelected=mutationFreqCheckBox.isSelected();
		foldChangeRuleSelected=foldChangeCheckBox.isSelected();
		System.out.println("mutationFreqRuleSelected:"+mutationFreqRuleSelected);
		System.out.println("foldChangeRuleSelected:"+foldChangeRuleSelected);
		if(mutationFreqRuleSelected==false && foldChangeRuleSelected==false)
			diseaseNodeDetails=postgreDB.getNetworkViewNode_getDiseaseNodeFromHallmark(selectedView, generalOutcomeHallmark, Double.parseDouble(jaccardThresholdGeneralOutcome));
		else
		{
			if(mutationFreqRuleSelected==true)
			{
				String mutationFreqStr=mutationFreqTextField.getText();
				try
				{
					mutationFreqValue=Double.parseDouble(mutationFreqStr);
				}
				catch(NumberFormatException e)
				{	
					//not a double
					JOptionPane.showMessageDialog(new JFrame(), "Please enter a float value for mutation frequency %.", "Error", JOptionPane.ERROR_MESSAGE);
				}
			}
			if(foldChangeRuleSelected==true)
			{
				String foldChangeStr=foldChangeTextField.getText();
				try
				{
					foldChangeValue=Double.parseDouble(foldChangeStr);
				}
				catch(NumberFormatException e)
				{
					//not a double
					JOptionPane.showMessageDialog(new JFrame(), "Please enter a float value for fold change.", "Error", JOptionPane.ERROR_MESSAGE);
				}
			}
			mutationFreqRuleOperator=foldChangeOperatorComboBox.getSelectedItem().toString();
			diseaseNodeDetails=postgreDB.getNetworkViewNode_getDiseaseNodeFromHallmarkAndRule(selectedView, foldChangeRuleSelected, mutationFreqRuleSelected, mutationFreqRuleOperator, foldChangeValue, mutationFreqValue, generalOutcomeHallmark, Double.parseDouble(jaccardThresholdGeneralOutcome));
		}
		if(diseaseNodeDetails.size()==0)
			JOptionPane.showMessageDialog(new JFrame(), "There are no disease nodes satisfying this requirement. Please modify the requirements and select the disease nodes again.", "Error", JOptionPane.ERROR_MESSAGE);
		else
		{
			int i=0;
			//System.out.println("node:"+node.toString());
			while(diseaseNodeDetails.size()>i)
			{
				String diseaseNode=diseaseNodeDetails.getNodeAtIndex(i);
				if(node.contains(diseaseNode)==true)
				{
					String diseaseMetaNode=jungGraph.getMetaNodeOfGivenNode(diseaseNode);
					System.out.println(diseaseNode+" "+diseaseMetaNode);
					diseaseNodeDetails.setMetaNode(diseaseNode, diseaseMetaNode);
					i++;
				}
				else
					diseaseNodeDetails.removeNode(i);
			}
		}
	}
	
	private void selectDiseaseNodes_hallmarkBased(){
		diseaseNode=new ArrayList<String>();
		String jaccardThresholdHallmark=jaccardThresholdHallmarkTextField.getText();
		System.out.println("selectedHallmark:"+selectedHallmark.toString());
		System.out.println("jaccardThresholdHallmark:"+jaccardThresholdHallmark);
		//get filter information
		mutationFreqRuleSelected=mutationFreqCheckBox.isSelected();
		foldChangeRuleSelected=foldChangeCheckBox.isSelected();
		System.out.println("mutationFreqRuleSelected:"+mutationFreqRuleSelected);
		System.out.println("foldChangeRuleSelected:"+foldChangeRuleSelected);
		if(mutationFreqRuleSelected==false && foldChangeRuleSelected==false)
			diseaseNodeDetails=postgreDB.getNetworkViewNode_getDiseaseNodeFromHallmark(selectedView, selectedHallmark, Double.parseDouble(jaccardThresholdHallmark));
		else
		{
			if(mutationFreqRuleSelected==true)
			{
				String mutationFreqStr=mutationFreqTextField.getText();
				try
				{
					mutationFreqValue=Double.parseDouble(mutationFreqStr);
				}
				catch(NumberFormatException e)
				{	
					//not a double
					JOptionPane.showMessageDialog(new JFrame(), "Please enter a float value for mutation frequency %.", "Error", JOptionPane.ERROR_MESSAGE);
				}
			}
			if(foldChangeRuleSelected==true)
			{
				String foldChangeStr=foldChangeTextField.getText();
				try
				{
					foldChangeValue=Double.parseDouble(foldChangeStr);
				}
				catch(NumberFormatException e)
				{
					//not a double
					JOptionPane.showMessageDialog(new JFrame(), "Please enter a float value for fold change.", "Error", JOptionPane.ERROR_MESSAGE);
				}
			}
			mutationFreqRuleOperator=foldChangeOperatorComboBox.getSelectedItem().toString();
			diseaseNodeDetails=postgreDB.getNetworkViewNode_getDiseaseNodeFromHallmarkAndRule(selectedView, foldChangeRuleSelected, mutationFreqRuleSelected, mutationFreqRuleOperator, foldChangeValue, mutationFreqValue, selectedHallmark, Double.parseDouble(jaccardThresholdHallmark));
		}
		if(diseaseNodeDetails.size()==0)
			JOptionPane.showMessageDialog(new JFrame(), "There are no disease nodes satisfying this requirement. Please modify the requirements and select the disease nodes again.", "Error", JOptionPane.ERROR_MESSAGE);
		else
		{
			for(int i=0; i<diseaseNodeDetails.size(); i++)
			{
				String diseaseNode=diseaseNodeDetails.getNodeAtIndex(i);
				String diseaseMetaNode=jungGraph.getMetaNodeOfGivenNode(diseaseNode);
				System.out.println(diseaseNode+" "+diseaseMetaNode);
				diseaseNodeDetails.setMetaNode(diseaseNode, diseaseMetaNode);
			}
		}
	}
	
	private void selectDiseaseNodes_nodeBased(){
		System.out.println("selectedNode:"+selectedNode.toString());
		diseaseNode=new ArrayList<String>();
		for(int i=0; i<selectedNode.size(); i++)
			diseaseNode.add(selectedNode.get(i));
		//get filter information
		mutationFreqRuleSelected=mutationFreqCheckBox.isSelected();
		foldChangeRuleSelected=foldChangeCheckBox.isSelected();
		System.out.println("mutationFreqRuleSelected:"+mutationFreqRuleSelected);
		System.out.println("foldChangeRuleSelected:"+foldChangeRuleSelected);
		if(mutationFreqRuleSelected==false && foldChangeRuleSelected==false)
			diseaseNodeDetails=postgreDB.getNetworkViewNode_getDiseaseNodeFromNodeList(selectedView, diseaseNode);
		else
		{
			if(mutationFreqRuleSelected==true)
			{
				String mutationFreqStr=mutationFreqTextField.getText();
				try
				{
					mutationFreqValue=Double.parseDouble(mutationFreqStr);
				}
				catch(NumberFormatException e)
				{	
					//not a double
					JOptionPane.showMessageDialog(new JFrame(), "Please enter a float value for mutation frequency %.", "Error", JOptionPane.ERROR_MESSAGE);
				}
			}
			if(foldChangeRuleSelected==true)
			{
				String foldChangeStr=foldChangeTextField.getText();
				try
				{
					foldChangeValue=Double.parseDouble(foldChangeStr);
				}
				catch(NumberFormatException e)
				{
					//not a double
					JOptionPane.showMessageDialog(new JFrame(), "Please enter a float value for fold change.", "Error", JOptionPane.ERROR_MESSAGE);
				}
			}
			mutationFreqRuleOperator=foldChangeOperatorComboBox.getSelectedItem().toString();
			diseaseNodeDetails=postgreDB.getNetworkViewNode_getDiseaseNodeFromNodeListAndRule(selectedView, foldChangeRuleSelected, mutationFreqRuleSelected, mutationFreqRuleOperator, foldChangeValue, mutationFreqValue, diseaseNode);
		}
		if(diseaseNodeDetails.size()==0)
			JOptionPane.showMessageDialog(new JFrame(), "There are no disease nodes satisfying this requirement. Please modify the requirements and select the disease nodes again.", "Error", JOptionPane.ERROR_MESSAGE);
		else
		{
			for(int i=0; i<diseaseNodeDetails.size(); i++)
			{
				String diseaseNode=diseaseNodeDetails.getNodeAtIndex(i);
				String diseaseMetaNode=jungGraph.getMetaNodeOfGivenNode(diseaseNode);
				System.out.println(diseaseNode+" "+diseaseMetaNode);
				diseaseNodeDetails.setMetaNode(diseaseNode, diseaseMetaNode);
			}
		}
	}

	private void appendToPane(JTextPane tp, String msg, Color c)
	{
		StyleContext sc = StyleContext.getDefaultStyleContext();
		AttributeSet aset = sc.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Foreground, c);

		aset = sc.addAttribute(aset, StyleConstants.FontFamily, "Lucida Console");
		aset = sc.addAttribute(aset, StyleConstants.Alignment, StyleConstants.ALIGN_JUSTIFIED);

		tp.setText(msg);
	}

	public ArrayList<String> getDiseaseNode()
	{
		return diseaseNode;
	}
	
	public ArrayList<String> getRankedHallmark()
	{
		return rankedHallmark;
	}
	
	public ArrayList<String> getSelectedDiseaseNode()
	{
		return selectedDiseaseNode;
	}

	public diseaseNode getDiseaseNodeDetails()
	{
		return diseaseNodeDetails;
	}

	public String getCombiSize()
	{
		return combiSize;
	}

	public String getEssentialWt()
	{
		return perturbedEssentialWt;
	}

	public String getOutDegreeWt()
	{
		return perturbedOutDegreeWt;
	}

	public boolean getPerturbedEssential()
	{
		return perturbedEssential;
	}

	public boolean getPerturbedOutDegree()
	{
		return perturbedOutDegree;
	}

	public String getMethod()
	{
		return method;
	}
	
	private void selectDiseaseNodes_filterOnly(){
		mutationFreqRuleSelected=mutationFreqCheckBox.isSelected();
		foldChangeRuleSelected=foldChangeCheckBox.isSelected();
		if(mutationFreqRuleSelected==false && foldChangeRuleSelected==false)
			JOptionPane.showMessageDialog(new JFrame(), "Please select at least one rule.", "Error", JOptionPane.ERROR_MESSAGE);
		else
		{
			if(mutationFreqRuleSelected==true)
			{
				String mutationFreqStr=mutationFreqTextField.getText();
				try
				{
					mutationFreqValue=Double.parseDouble(mutationFreqStr);
				}
				catch(NumberFormatException e)
				{	
					//not a double
					JOptionPane.showMessageDialog(new JFrame(), "Please enter a float value for mutation frequency %.", "Error", JOptionPane.ERROR_MESSAGE);
				}
			}
			if(foldChangeRuleSelected==true)
			{
				String foldChangeStr=foldChangeTextField.getText();
				try
				{
					foldChangeValue=Double.parseDouble(foldChangeStr);
				}
				catch(NumberFormatException e)
				{
					//not a double
					JOptionPane.showMessageDialog(new JFrame(), "Please enter a float value for fold change.", "Error", JOptionPane.ERROR_MESSAGE);
				}
			}
			mutationFreqRuleOperator=foldChangeOperatorComboBox.getSelectedItem().toString();
			System.out.println("selectedView:"+selectedView);
			System.out.println("mutationFreqRuleSelected:"+mutationFreqRuleSelected+" foldChangeRuleSelected:"+foldChangeRuleSelected);
			System.out.println("mutationFreqValue:"+mutationFreqValue+" foldChangeValue:"+foldChangeValue);
			System.out.println("mutationFreqRuleOperator:"+mutationFreqRuleOperator);
			diseaseNodeDetails=postgreDB.getNetworkViewNode_getDiseaseNodeFromRule(selectedView, foldChangeRuleSelected, mutationFreqRuleSelected, mutationFreqRuleOperator, foldChangeValue, mutationFreqValue);
			System.out.println("diseaseNodeDetails size 1:"+diseaseNodeDetails.size());
			if(diseaseNodeDetails.size()==0)
				JOptionPane.showMessageDialog(new JFrame(), "There are no disease nodes satisfying this requirement. Please modify the requirements and select the disease nodes again.", "Error", JOptionPane.ERROR_MESSAGE);
			else
			{
				for(int i=0; i<diseaseNodeDetails.size(); i++)
				{
					String diseaseNode=diseaseNodeDetails.getNodeAtIndex(i);
					String diseaseMetaNode=jungGraph.getMetaNodeOfGivenNode(diseaseNode);
					System.out.println(diseaseNode+" "+diseaseMetaNode);
					diseaseNodeDetails.setMetaNode(diseaseNode, diseaseMetaNode);
				}
			}
		}
		//setVisible(false);
	}

	private void cancel_actionPerformed(){
		setVisible(false);
	}

	public boolean getPerformComputation(){
		return performComputation;
	}
}
