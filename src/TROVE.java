import java.awt.AWTException;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FileDialog;
import java.awt.FlowLayout;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseAdapter;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Vector;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.DefaultButtonModel;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.Position;
import javax.swing.WindowConstants;
import javax.swing.JFrame;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.gephi.data.attributes.api.AttributeModel;
import org.gephi.datalab.api.AttributeColumnsController;
import org.gephi.graph.api.GraphController;
import org.gephi.graph.api.GraphModel;
import org.gephi.graph.api.MixedGraph;
import org.gephi.preview.api.ManagedRenderer;
import org.gephi.preview.api.PreviewController;
import org.gephi.preview.api.PreviewModel;
import org.gephi.preview.api.ProcessingTarget;
import org.gephi.project.api.Project;
import org.gephi.project.api.ProjectController;
import org.gephi.project.api.Workspace;
import org.openide.util.Lookup;

import processing.core.PApplet;

import kinetics.rate;

import hallmark.allHallmark;
import dataType.diseaseNode;
import dataType.nodePath;
import dataType.targetCombination;
import database.*;
import dialogFrames.*;
import graph.*;
import wrapper.java_GUIWrapper;
import constants.*;
import cz.cvut.fit.krizeji1.multicolour.MultiColourRenderer;
//in your swing gui event listener (e.g. button clicked, combo selected, ...)
import supportVectorMachine.crossValidationData;
import supportVectorMachine.svmAnalysis;
//import ExecutionContext.Implicits.global;
import utilities.popClickListener;
import fileObject.fReaderWriter;

@SuppressWarnings("serial")
public class TROVE extends JDialog{
	static JDialog ingotDialog=new JDialog();
	static stringConstant strConstants=new stringConstant();
	//menu bar
	static JMenuBar menu=new JMenuBar();
	static JMenu menuBarFile = new JMenu();
	static JMenu menuBarAction = new JMenu();
	static JMenu menuBarOption = new JMenu();
	static JMenu menuBarEdit = new JMenu();
	static JMenuItem menuItemFileTopology = new JMenuItem();
	static JMenuItem menuItemFileNetworkData = new JMenuItem();
	static JMenuItem menuItemFileCuratedCombi = new JMenuItem();
	static JMenuItem menuItemFileCuratedDrug = new JMenuItem();
	static JMenuItem menuItemActionVisualize = new JMenuItem();
	static JMenuItem menuItemActionComputeTargetCombi = new JMenuItem();
	static JMenuItem menuItemActionGenerateTargetCombi = new JMenuItem();
	static JMenuItem menuItemActionComputeTargetCombiSideEffects = new JMenuItem();
	static JMenuItem menuItemActionComputeTopologicalFeatures = new JMenuItem();
	static JMenuItem menuItemActionCharacterizeHallmarks = new JMenuItem();
	static JMenuItem menuItemActionHighlightDynamics = new JMenuItem();
	static JMenuItem menuItemActionCheckCombiPredictionAgainstCombiCuration = new JMenuItem();
	static JMenuItem menuItemActionCheckCombiPredictionAgainstMonoCuration = new JMenuItem();
	static JMenuItem menuItemOptionFullScreen = new JMenuItem();
	static JMenuItem menuItemOptionHallmark = new JMenuItem();
	static JMenuItem menuItemOptionCompound = new JMenuItem();
	static JMenuItem menuItemOptionKEGGPathway = new JMenuItem();
	static JMenuItem menuItemOptionUserPathway = new JMenuItem();
	static JMenuItem menuItemOptionNodePairPathway = new JMenuItem();
	static JMenuItem menuItemOptionFeatureResult = new JMenuItem();
	static JMenuItem menuItemOptionCharacterizationResult = new JMenuItem();
	static JMenuItem menuItemOptionDiseaseNode = new JMenuItem();
	static JMenuItem menuItemOptionTargetCombination = new JMenuItem();
	static JMenuItem menuItemEditGraph = new JMenuItem();
	static JMenuItem menuItemEditCancerTypeView = new JMenuItem();
	static JMenuItem menuItemEditHallmarkGOMapping = new JMenuItem();
	//layout panels
	JPanel contentPanel=new JPanel();
	static JPanel leftColumnPanel=new JPanel();
	static JPanel topRowPanel=new JPanel();
	static JPanel bottomRowPanel=new JPanel();
	static JPanel sccMapPanel=new JPanel();
	//main panels 
	static JPanel resultPanel=new JPanel();
	static JPanel statusPanel=new JPanel();
	static JTabbedPane vertexTabbedPanel=new JTabbedPane();
	static JPanel interactiveGraphPanel=new JPanel();
	static JLabel statusLabel=new JLabel();
	static JPanel settingPanel=new JPanel();
	static JPanel searchPanel=new JPanel();
	//tabbed node panel (top left)
	static JList<String> speciesList=new JList<String>();
	static String selectedNode;
	static String selectedView="";
	static String selectedLayout="";
	static JList<String> targetList=new JList<String>();
	static JList<String> pathwayList=new JList<String>();
	static JList<String> crosstalkPathwayList=new JList<String>();
	//static JList<String> selectedHallmarkNodeList=new JList<String>();
	static JScrollPane speciesListScrollPane=new JScrollPane();
	static JScrollPane diseaseNodeListScrollPane=new JScrollPane();
	static JScrollPane predictedCombinationListScrollPane=new JScrollPane();
	static JScrollPane targetListScrollPane=new JScrollPane();
	static JScrollPane pathwayListScrollPane=new JScrollPane();
	static JScrollPane crosstalkPathwayListScrollPane=new JScrollPane();
	//static JScrollPane selectedHallmarkNodeListScrollPane=new JScrollPane();
	static JTabbedPane resultTabbedPane=new JTabbedPane();
	static JPanel resultLabelPanel=new JPanel();
	static JPanel outputLabelPanel=new JPanel();
	static JLabel resultLabel=new JLabel();
	static JLabel outputLabel=new JLabel();
	static DefaultTableModel diseaseNodeTableModel = new DefaultTableModel();
	static JTable diseaseNodeTable=new JTable(diseaseNodeTableModel);
	static diseaseNode diseaseNodeList=new diseaseNode();
	static DefaultTableModel predictedCombinationTableModel = new DefaultTableModel();
	static JTable predictedCombinationTable=new JTable(predictedCombinationTableModel);
	static targetCombination predictedCombinationList=new targetCombination();
	//setting panel (center-top left)
	static JLabel graphViewLabel=new JLabel();
	static JComboBox<String> graphViewComboBox=new JComboBox<String>();
	static ArrayList<String> graphViewOption=new ArrayList<String>();
	static String NEIGHBOURVIEW="Neighbour only";
	static String GRAPHVIEW="Entire graph";
	static String CURRVIEW=GRAPHVIEW;
	static JButton editAnnotationButton=new JButton();
	static JButton editHallmarkButton=new JButton();
	static JButton editGraphButton=new JButton();
	static JPanel radioButtonPanel=new JPanel();
	static JRadioButton foldChangeRButton=new JRadioButton("Fold change");
	static JRadioButton mutationFrequencyRButton=new JRadioButton("Mutation frequency");
	static JRadioButton diseaseTargetNodeRButton=new JRadioButton("Disease/Target nodes");
	static ButtonGroup radioButtonGroup=new ButtonGroup();
	//search panel (center-bottom left)
	static JTextField searchText=new JTextField();
	static JButton searchButton=new JButton();
	//tabbed information panel (bottom right)
	static JPanel vertexReactionContent = new JPanel();
	static JScrollPane vertexReactionScroll = new JScrollPane(vertexReactionContent);
	static JPanel vertexNodeContent = new JPanel();
	static JScrollPane vertexNodeScroll = new JScrollPane(vertexNodeContent);
	JPanel vertexTargetWebContent = new JPanel();
	JScrollPane vertexTargetWebScroll = new JScrollPane(vertexTargetWebContent);
	static JPanel vertexLegendContent = new JPanel();
	static JScrollPane vertexLegendScroll = new JScrollPane(vertexLegendContent);
	static JPanel vertexTargetCombinationContent = new JPanel();
	static JScrollPane vertexTargetCombinationScroll = new JScrollPane(vertexTargetCombinationContent);
	//constant
	static int FRAMEHEIGHT = 720;
	static int GRAPHDISPLAYHEIGHT = 630;
	static int GRAPHDISPLAYWIDTH = 630;
	static int RESULTTABPANELHEIGHT=330-50;
	static int VERTEXTABPANELHEIGHT=200;
	static int TEXTHEIGHT=25;
	static int LEFTCOLWIDTH=300;
	int TABWIDTH = 280;
	static int SCALE = 5;
	static int SCREENWIDTH = Toolkit.getDefaultToolkit().getScreenSize().width;
	static int SCREENHEIGHT = Toolkit.getDefaultToolkit().getScreenSize().height;
	static String OPEN_TOPOLOGY_FILE = "Open Human Signaling Network File";
	static String OPEN_NETWORK_DATA_FILE = "Open Network Annotation File";
	static String OPEN_CURATED_COMBI_FILE = "Open Curated Combination File";
	static String OPEN_CURATED_DRUG_FILE = "Open Curated Drug File";
	static String VISUALIZE_PRELOADED_NETWORK = "Visualize Preloaded Network";
	static String COMPUTE_TARGET_COMBI_GREEDY = "Compute Target Combinations (Greedy)";
	static String COMPUTE_TARGET_COMBI = "Compute Target Combinations";
	static String COMPUTE_TARGET_COMBI_RANDOM = "Compute Target Combinations (Random)";
	static String COMPUTE_TARGET_COMBI_MCSA = "Compute Target Combinations (MCSA)";
	static String COMPUTE_TARGET_COMBI_SA_HEURISTICS = "Compute Target Combinations (SA Heuristics)";
	static String GENERATE_TARGET_COMBI = "Generate Target Combinations";
	static String COMPUTE_TARGET_COMBI_SIDE_EFFECTS = "Compute Target Combinations Side Effects";
	static String COMPUTE_TOPOLOGICAL_FEATURES = "Compute Topological Features";
	static String CHARACTERIZE_HALLMARKS = "Characterize Hallmarks";
	static String HIGHLIGHT_DYNAMICS = "Highlight Edges with Dynamics";
	static String CHECK_COMBI_PREDICTION_AGAINST_COMBI_CURATION = "Check Predicted Combinations Against Combination Curation";
	static String CHECK_COMBI_PREDICTION_AGAINST_MONO_CURATION = "Check Predicted Combinations Against Monotherapy Curation";
	static String SEARCH_NODE = "Search Species List";
	static String GRAPH_VIEW_SETTING = "Graph View";
	static String EDIT_ANNOTATION = "Edit Annotation";
	static String EDIT_HALLMARK = "Edit Hallmark";
	static String EDIT_GRAPH = "Edit Graph";
	static String EDIT_CANCER_TYPE_VIEW = "Edit Cancer Type View";
	static String EDIT_HALLMARK_GO_MAPPING = "Edit Hallmark GO Mapping";
	static String DISPLAY_FOLD_CHANGE = "Display Fold Change";
	static String DISPLAY_MUTATION_FREQUENCY = "Display Mutation Frequency";
	static String DISPLAY_DISEASE_TARGET_NODE = "Display Disease and Target Nodes";
	static String DISPLAY_DISEASE_NODE = "Display Disease Node";
	static String DISPLAY_TARGET_NODE = "Display Target Node";
	static String VIEW_FULLSCREEN = "View Full Screen Toggle";
	static String VIEW_HALLMARK = "View Hallmark";
	static String VIEW_HALLMARK_INDUCED = "View Hallmark Induced";
	static String VIEW_KEGG_ANNOTATED_PATHWAY = "View KEGG-Annotated Pathway";
	static String CREATE_AND_VIEW_PATHWAY = "Create and View Pathway ";
	static String VIEW_NODE_PAIR_PATHWAY = "View Node Pair Pathway ";
	static String VIEW_NODE_PAIR="View Pathway Between Node Set";
	static String VIEW_FEATURE_RESULT = "View Feature Results";
	static String VIEW_CHARACTERIZATION_RESULT = "View Characterization Results";
	static String VIEW_DISEASE_NODE = "View Disease Node(s)";
	static String VIEW_TARGET_COMBINATION = "View Target Combination(s)";
	static String VIEW_COMPOUND = "Collapse Physical Interaction";
	static String VIEW_NEIGHBOUR="View Neighbour";
	static String HALLMARK = "HALLMARK";
	static String HALLMARK_HALLMARK = "HALLMARK_HALLMARK";
	static String HALLMARK_SOURCE = "HALLMARK_SOURCE";
	static String HALLMARK_ANNOTATION = "HALLMARK_ANNOTATION";
	static String HALLMARK_GO_MAPPING = "HALLMARK_GO_MAPPING";
	static String KINETICS = "KINETICS";
	static String KINETICS_KINETICS = "KINETICS_KINETICS";
	static String KINETICS_SOURCE = "KINETICS_SOURCE";
	static String KINETICS_RATE_ANNOTATION = "KINETICS_RATE_ANNOTATION";
	static String KINETICS_RATE = "KINETICS_RATE";
	static String NODE = "NODE";
	static String NODE_ANNOTATION = "NODE_ANNOTATION";
	static String NODE_SOURCE = "NODE_SOURCE";
	static String PATHWAY = "PATHWAY";
	static String HALLMARK_PROLIFERATION_DB = strConstants.getDBProliferation();
	static String HALLMARK_GROWTH_REPRESSOR_DB = strConstants.getDBGrowthRepressor();
	static String HALLMARK_APOPTOSIS_DB = strConstants.getDBApoptosis();
	static String HALLMARK_REPLICATIVE_IMMORTALITY_DB = strConstants.getDBReplicativeImmortality();
	static String HALLMARK_ANGIOGENESIS_DB = strConstants.getDBAngiogenesis();
	static String HALLMARK_METASTASIS_DB = strConstants.getDBMetastasis();
	static String HALLMARK_METABOLISM_DB = strConstants.getDBMetabolism();
	static String HALLMARK_IMMUNE_DESTRUCTION_DB = strConstants.getDBImmuneDestruction();
	static String HALLMARK_GENOME_INSTABILITY_DB = strConstants.getDBGenomeInstability();
	static String HALLMARK_TUMOR_PROMOTING_INFLAMMATION_DB = strConstants.getDBTumorPromotingInflammation();
	static String HALLMARK_PROLIFERATION_GUI = strConstants.getGUIProliferation();
	static String HALLMARK_GROWTH_REPRESSOR_GUI = strConstants.getGUIGrowthRepressor();
	static String HALLMARK_APOPTOSIS_GUI = strConstants.getGUIApoptosis();
	static String HALLMARK_REPLICATIVE_IMMORTALITY_GUI = strConstants.getGUIReplicativeImmortality();
	static String HALLMARK_ANGIOGENESIS_GUI = strConstants.getGUIAngiogenesis();
	static String HALLMARK_METASTASIS_GUI = strConstants.getGUIMetastasis();
	static String HALLMARK_METABOLISM_GUI = strConstants.getGUIMetabolism();
	static String HALLMARK_IMMUNE_DESTRUCTION_GUI = strConstants.getGUIImmuneDestruction();
	static String HALLMARK_GENOME_INSTABILITY_GUI = strConstants.getGUIGenomeInstability();
	static String HALLMARK_TUMOR_PROMOTING_INFLAMMATION_GUI = strConstants.getGUITumorPromotingInflammation();
	static String EXPORT_HALLMARK_NODE_FILES = "EXPORT_HALLMARK_NODE_FILE";
	static String RELOAD_NETWORK="RELOAD_NETWORK";

	static String LAYOUT_YIFANHU="Yifanhu";
	static String LAYOUT_NOOVERLAP="No overlap";
	static String LAYOUT_FRUCHTERMAN_REINGOLD="Fruchterman Reingold";
	static String LAYOUT_FORCE_ATLAS="Force atlas";
	static String LAYOUT_FORCE_ATLAS_TWO="Force atlas 2";
	static String LAYOUT_LABEL_ADJUST="Label adjust";

	static int HALLMARK_PROLIFERATION_INT = 0;
	static int HALLMARK_GROWTH_REPRESSOR_INT = 1;
	static int HALLMARK_APOPTOSIS_INT = 2;
	static int HALLMARK_REPLICATIVE_IMMORTALITY_INT = 3;
	static int HALLMARK_ANGIOGENESIS_INT = 4;
	static int HALLMARK_METASTASIS_INT = 5;
	static int HALLMARK_METABOLISM_INT = 6;
	static int HALLMARK_IMMUNE_DESTRUCTION_INT = 7;
	static int HALLMARK_GENOME_INSTABILITY_INT = 8;
	static int HALLMARK_TUMOR_PROMOTING_INFLAMMATION_INT = 9;
	static Map<String, String> HallmarkMapGUIString = new HashMap<String, String>();
	static Map<Integer, String> HallmarkMapDBString = new HashMap<Integer, String>();
	static String CSV_EXTENSION_STRING = ".csv";
	static String DAT_EXTENSION_STRING = ".dat";
	static String SPACE=" ";
	static String COMMA=",";
	static String POS_EDGE=" ------> ";
	static String NEG_EDGE=" ------| ";
	static String NEUTRAL_EDGE=" ------- ";
	protected final static String VIEW_ON_CURRENT_GRAPH = "View on current graph";
	protected final static String VIEW_NODE_NEIGHBOURHOOD = "View node neighbourhood";
	protected final static String VIEW_INDUCED_GRAPH = "View induced graph";
	//databases
	static postgreSQL postgreDB=new postgreSQL();
	//graph
	static ArrayList<String> graphNodeIDList=new ArrayList<String>();
	static ArrayList<String> graphNodeNameList=new ArrayList<String>();
	static ArrayList<edge> graphEdgeList=new ArrayList<edge>();
	static JUNG_graph jungGraph=new JUNG_graph(postgreDB); //for computation
	static JGRAPHT_graph jGraphTGraph=new JGRAPHT_graph(postgreDB); //for computation
	static graph_pathways pathway_graph=new graph_pathways();
	private ProjectController interactivePC;
	private Project interactiveProject;
	private Workspace interactiveWorkspace;
	private Workspace viewNeighbourWorkspace;
	private Workspace viewHallmarkWorkspace;
	private Workspace viewNodePairWorkspace;
	private PreviewController interactivePreviewController;
	private ProcessingTarget interactiveTarget;
	private PreviewModel interactivePreviewModel;
	private MultiColourRenderer multiColorRenderer=new MultiColourRenderer();;
	static Gephi_graph gephiGraph; //for visualization
	static boolean GEPHIGRAPHREADY=false;
	static boolean GRAPHKINETICSREADY=false;
	static boolean FORCERECOLOR=false;
	//arrayList
	static ArrayList<String> allNodeArrList=new ArrayList<String>();
	static ArrayList<String> pathwayNodeList=new ArrayList<String>();
	static ArrayList<String> oneHopNeighbourNodeList=new ArrayList<String>();
	static ArrayList<String> twoHopNeighbourNodeList=new ArrayList<String>();
	static ArrayList<String> oldSelectedHallmarkList=new ArrayList<String>();
	static ArrayList<String> selectedHallmarkList=new ArrayList<String>();
	static ArrayList<String> selectedPathwayList=new ArrayList<String>();
	static ArrayList<String> selectedFeatures=new ArrayList<String>();
	static ArrayList<graphNodeData> pathwayNodeInfoList=new ArrayList<graphNodeData>();
	//GUI wrapper
	static java_GUIWrapper GUIwrapper=new java_GUIWrapper();
	//file and directory names
	static String directory;
	static String folder;
	static String topologyFileName;
	static String networkDataFileName;
	static String networkViewFileName;
	static String curatedCombiFileName;
	static String predictedCombiFileName;
	static String curatedDrugFileName;
	static String hallmarkGOMappingFileName;
	//svm
	//var svmObj=new svmAnalysis();

	//threading
	VisualizeNetworkWorker visualizeNetworkWorker;
	PerformAnalysisWorker performAnalysisWorker;
	//target combinations
	int totalEssentialNodes;
	ArrayList<String> tempCombi=new ArrayList<String>();
	ArrayList<String> tempCombiDiseaseNode=new ArrayList<String>();
	double bestCoverage=0, bestOffTargetScore=1000, bestOverall=1000, bestJaccard=1;
	
	private ArrayList<ArrayList<String>> t=new ArrayList<ArrayList<String>>();

	//for SCC map display in resultTabbedPane
	private JList<String> SCCList;
	private JScrollPane SCCScrollPane;
	private JList<String> nodeList;
	private JScrollPane nodeScrollPane;
	private ArrayList<String> SCCForDisplay=new ArrayList<String>();
	private ArrayList<String> NodeForDisplay=new ArrayList<String>();
	private ArrayList<ArrayList<String>> mapping=new ArrayList<ArrayList<String>>();
	
	
	//status
	private String HALLMARK_VIEW_COMPLETE="Finish visualizing hallmarks";
	private viewNeighbourDialog viewNeighbour=null;
	
	//OS-dependant stuff
	static int OS_WINDOWS=0;
	static int OS_LINUX=1;
	static int OS_MAC_OS_X=2;
	static int OS_SOLARIS=3;
	static int osType=-1;
	static String winDirectory="C:\\network_lib_TROVE\\";
	static String macDirectory="/Desktop/network_lib_TROVE/";
	static String linuxDirectory="/Desktop/network_lib_TROVE/";
	static String winFolder="\\";
	static String macFolder="/";
	static String linuxFolder="/";
	
	//fullscreen toggle stuff
	private boolean IS_FULLSCREEN=false;
	
	public TROVE() 
	{
		super();
		
		//check system os first
		String osName = System.getProperty("os.name");
		String osNameMatch = osName.toLowerCase();
		if(osNameMatch.contains("linux")) {
		    osType = OS_LINUX;
		}else if(osNameMatch.contains("windows")) {
		    osType = OS_WINDOWS;
		}else if(osNameMatch.contains("solaris") || osNameMatch.contains("sunos")) {
		    osType = OS_SOLARIS;
		}else if(osNameMatch.contains("mac os") || osNameMatch.contains("macos") || osNameMatch.contains("darwin")) {
		    osType = OS_MAC_OS_X;
		}
		if(osType==OS_WINDOWS)
		{
			directory=winDirectory;
			folder=winFolder;
		}
		else if(osType==OS_MAC_OS_X)
		{
			String username=System.getProperty("user.name");
			directory="/Users/"+username+macDirectory;
			folder=macFolder;
		}
		else if(osType==OS_LINUX)
		{
			String username=System.getProperty("user.name");
			directory="/home/"+username+linuxDirectory;
			folder=linuxFolder;
		}
		networkViewFileName=directory.concat("view_fileList.txt");
		hallmarkGOMappingFileName=directory.concat("hallmarkGOMapping.csv");
		System.out.println("System OS: (0)WINDOWS, (1)LINUX, (2)MAC. This system OS is "+osType);
		
		interactivePC=Lookup.getDefault().lookup(ProjectController.class);
		interactivePC.newProject();
		interactiveProject=interactivePC.getCurrentProject();
		interactiveWorkspace=interactivePC.newWorkspace(interactiveProject);
		interactivePC.openWorkspace(interactiveWorkspace); 
		interactivePreviewController = Lookup.getDefault().lookup(PreviewController.class);
		interactivePreviewModel=interactivePreviewController.getModel(interactivePC.getCurrentWorkspace());
		ManagedRenderer [] mr = interactivePreviewModel.getManagedRenderers();
		ManagedRenderer [] mr2 = Arrays.copyOf(mr, mr.length+1);
		mr2[mr.length] = new ManagedRenderer(multiColorRenderer, true);
		interactivePreviewModel.setManagedRenderers(mr2);
		  
		//interactivePreviewController=Lookup.getDefault().lookup(PreviewController.class);
		//interactivePreviewModel = interactivePreviewController.getModel(interactiveWorkspace);
		//interactiveTarget = (ProcessingTarget) interactivePreviewController.getRenderTarget(RenderTarget.PROCESSING_TARGET);
		  
		gephiGraph=new Gephi_graph(interactivePC, interactiveProject, interactiveWorkspace, interactivePreviewModel); //for visualization
		
		//gephiGraph=new Gephi_graph();
		initialize_component();
		initialize_map();
		//postgreDB.loadTrainingNetworkTarget();
		if(preloadedNetworkExists())
			menuItemActionVisualize.setEnabled(true);
		else
			menuItemActionVisualize.setEnabled(false);
		ingotDialog.setVisible(true);
	}

	public static void main(String[] args) 
	{
		new TROVE();
	}


	private void initialize_map() 
	{
		HallmarkMapGUIString.put(HALLMARK_PROLIFERATION_DB, HALLMARK_PROLIFERATION_GUI);
		HallmarkMapGUIString.put(HALLMARK_GROWTH_REPRESSOR_DB, HALLMARK_GROWTH_REPRESSOR_GUI);
		HallmarkMapGUIString.put(HALLMARK_APOPTOSIS_DB, HALLMARK_APOPTOSIS_GUI);
		HallmarkMapGUIString.put(HALLMARK_REPLICATIVE_IMMORTALITY_DB, HALLMARK_REPLICATIVE_IMMORTALITY_GUI);
		HallmarkMapGUIString.put(HALLMARK_ANGIOGENESIS_DB, HALLMARK_ANGIOGENESIS_GUI);
		HallmarkMapGUIString.put(HALLMARK_METASTASIS_DB, HALLMARK_METASTASIS_GUI);
		HallmarkMapGUIString.put(HALLMARK_METABOLISM_DB, HALLMARK_METABOLISM_GUI);
		HallmarkMapGUIString.put(HALLMARK_IMMUNE_DESTRUCTION_DB, HALLMARK_IMMUNE_DESTRUCTION_GUI);
		HallmarkMapGUIString.put(HALLMARK_GENOME_INSTABILITY_DB, HALLMARK_GENOME_INSTABILITY_GUI);
		HallmarkMapGUIString.put(HALLMARK_TUMOR_PROMOTING_INFLAMMATION_DB, HALLMARK_TUMOR_PROMOTING_INFLAMMATION_GUI);

		HallmarkMapDBString.put(HALLMARK_PROLIFERATION_INT, HALLMARK_PROLIFERATION_DB);
		HallmarkMapDBString.put(HALLMARK_GROWTH_REPRESSOR_INT, HALLMARK_GROWTH_REPRESSOR_DB);
		HallmarkMapDBString.put(HALLMARK_APOPTOSIS_INT, HALLMARK_APOPTOSIS_DB);
		HallmarkMapDBString.put(HALLMARK_REPLICATIVE_IMMORTALITY_INT, HALLMARK_REPLICATIVE_IMMORTALITY_DB);
		HallmarkMapDBString.put(HALLMARK_ANGIOGENESIS_INT, HALLMARK_ANGIOGENESIS_DB);
		HallmarkMapDBString.put(HALLMARK_METASTASIS_INT, HALLMARK_METASTASIS_DB);
		HallmarkMapDBString.put(HALLMARK_METABOLISM_INT, HALLMARK_METABOLISM_DB);
		HallmarkMapDBString.put(HALLMARK_IMMUNE_DESTRUCTION_INT, HALLMARK_IMMUNE_DESTRUCTION_DB);
		HallmarkMapDBString.put(HALLMARK_GENOME_INSTABILITY_INT, HALLMARK_GENOME_INSTABILITY_DB);
		HallmarkMapDBString.put(HALLMARK_TUMOR_PROMOTING_INFLAMMATION_INT, HALLMARK_TUMOR_PROMOTING_INFLAMMATION_DB);
	}

	private static boolean preloadedNetworkExists() 
	{
		ArrayList<String> preloadedNetworkList=postgreDB.getNetworkMetadata_networkList();
		System.out.println(preloadedNetworkList.toString());
		if(preloadedNetworkList.size()>0)
			return true;
		else
			return false;
	}

	private ActionListener actionListener = new ActionListener() 
	{
		public void actionPerformed(ActionEvent actionEvent) 
		{
			if (actionEvent.getActionCommand() == OPEN_TOPOLOGY_FILE)
				openTopologyFile_actionPerformed();
			if (actionEvent.getActionCommand() == OPEN_NETWORK_DATA_FILE)
				openNetworkDataFile_actionPerformed();
			if (actionEvent.getActionCommand() == OPEN_CURATED_COMBI_FILE)
				openCuratedCombiFile_actionPerformed();
			if (actionEvent.getActionCommand() == OPEN_CURATED_DRUG_FILE)
				openCuratedDrugFile_actionPerformed();
			if (actionEvent.getActionCommand() == VISUALIZE_PRELOADED_NETWORK)
				visualizePreloadedNetwork_actionPerformed();
			if (actionEvent.getActionCommand() == COMPUTE_TOPOLOGICAL_FEATURES)
				computePathwayTopologicalFeatures_actionPerformed();
			if (actionEvent.getActionCommand() == CHARACTERIZE_HALLMARKS)
				characterizeHallmarks_actionPerformed();
			if (actionEvent.getActionCommand() == HIGHLIGHT_DYNAMICS)
				highlightDynamics_actionPerformed();
			if (actionEvent.getActionCommand() == CHECK_COMBI_PREDICTION_AGAINST_COMBI_CURATION)
				openPredictedCombiFileForCombiCuration_actionPerformed();
			if (actionEvent.getActionCommand() == CHECK_COMBI_PREDICTION_AGAINST_MONO_CURATION)
				openPredictedCombiFileForMonoCuration_actionPerformed();
			if (actionEvent.getActionCommand() == SEARCH_NODE)
				searchNode_actionPerformed();
			if (actionEvent.getActionCommand() == EDIT_ANNOTATION)
				editAnnotation_actionPerformed();
			if (actionEvent.getActionCommand() == EDIT_HALLMARK)
				editHallmark_actionPerformed();
			if (actionEvent.getActionCommand() == EDIT_GRAPH)
				editGraph_actionPerformed();
			if (actionEvent.getActionCommand() == EDIT_CANCER_TYPE_VIEW)
				editCancerTypeView_actionPerformed(selectedView);
			if (actionEvent.getActionCommand() == EDIT_HALLMARK_GO_MAPPING)
				editHallmarkGOMapping_actionPerformed();
			if (actionEvent.getActionCommand() == VIEW_FULLSCREEN)
				viewFullScreenToggle_actionPerformed();
			if (actionEvent.getActionCommand() == VIEW_HALLMARK)
				viewHallmark_actionPerformed();
			if (actionEvent.getActionCommand() == VIEW_COMPOUND)
				viewCompound_actionPerformed();
			if (actionEvent.getActionCommand() == VIEW_KEGG_ANNOTATED_PATHWAY)
				viewKEGGAnnotatedPathway_actionPerformed();
			if (actionEvent.getActionCommand() == CREATE_AND_VIEW_PATHWAY)
				createAndViewPathway_actionPerformed();
			if (actionEvent.getActionCommand() == VIEW_NODE_PAIR_PATHWAY)
				viewNodePairPathway_actionPerformed();
			if (actionEvent.getActionCommand() == VIEW_NODE_PAIR)
				viewNodePair_actionPerformed();
			if (actionEvent.getActionCommand() == VIEW_FEATURE_RESULT)
				viewFeatureResult_actionPerformed();
			if (actionEvent.getActionCommand() == VIEW_CHARACTERIZATION_RESULT)
				viewCharacterizationResult_actionPerformed();
			if (actionEvent.getActionCommand() == DISPLAY_FOLD_CHANGE)
			{
				gephiGraph.resetColorAndSizeOfNode();
				displayFoldChange_actionPerformed();
			}
			if (actionEvent.getActionCommand() == DISPLAY_MUTATION_FREQUENCY)
			{
				gephiGraph.resetColorAndSizeOfNode();
				displayMutationFrequency_actionPerformed();
			}
			if (actionEvent.getActionCommand() == DISPLAY_DISEASE_TARGET_NODE)
			{
				gephiGraph.resetColorAndSizeOfNode();
				displayDiseaseTargetNode_actionPerformed(tempCombi);
			}
			if (actionEvent.getActionCommand() == VIEW_DISEASE_NODE)
				viewDiseaseNode_actionPerformed();
			if (actionEvent.getActionCommand() == VIEW_TARGET_COMBINATION)
				viewTargetCombination_actionPerformed(null);
		}
	};

	private void viewFullScreenToggle_actionPerformed()
	{
		if(IS_FULLSCREEN==true)//switch from fullscreen to non-fullscreen
		{
			ingotDialog.setLocation(new Point((SCREENWIDTH-910)/2, (SCREENHEIGHT-FRAMEHEIGHT)/2));
			ingotDialog.setSize(new Dimension(1120, FRAMEHEIGHT));
			interactiveGraphPanel.setPreferredSize(new Dimension(GRAPHDISPLAYWIDTH, GRAPHDISPLAYHEIGHT));
			vertexTabbedPanel.setPreferredSize(new Dimension(LEFTCOLWIDTH, VERTEXTABPANELHEIGHT-30));
			speciesListScrollPane.setPreferredSize(new Dimension(LEFTCOLWIDTH, RESULTTABPANELHEIGHT));
			leftColumnPanel.setPreferredSize(new Dimension(LEFTCOLWIDTH+160, FRAMEHEIGHT-100));
			topRowPanel.setPreferredSize(new Dimension(1120, FRAMEHEIGHT-50));
			
			IS_FULLSCREEN=false;
		}
		else//switch from non-fullscreen to fullscreen
		{
			ingotDialog.setLocation(0,0);
		    ingotDialog.setSize(SCREENWIDTH, SCREENHEIGHT);
		    int newDisplayWidth=SCREENWIDTH-LEFTCOLWIDTH-200;
		    int newDisplayHeight=SCREENHEIGHT;
		    int newVertexTabPanelHeight=VERTEXTABPANELHEIGHT+150;
		    int newSpeciesListHeight=SCREENHEIGHT-newVertexTabPanelHeight-300;
		    int newLeftColHeight=SCREENHEIGHT-200;
		    interactiveGraphPanel.setPreferredSize(new Dimension(newDisplayWidth, newDisplayHeight));
		    vertexTabbedPanel.setPreferredSize(new Dimension(LEFTCOLWIDTH, newVertexTabPanelHeight));
		    speciesListScrollPane.setPreferredSize(new Dimension(LEFTCOLWIDTH, newSpeciesListHeight));
		    leftColumnPanel.setPreferredSize(new Dimension(LEFTCOLWIDTH+160, newLeftColHeight));
		    topRowPanel.setPreferredSize(new Dimension(SCREENWIDTH, SCREENHEIGHT-50));
		    
		    IS_FULLSCREEN=true;
		}
		if(GEPHIGRAPHREADY)
			gephiGraph.resizeGraph(interactiveGraphPanel);
		interactiveGraphPanel.revalidate();
		vertexTabbedPanel.revalidate();
		speciesListScrollPane.revalidate();
		leftColumnPanel.revalidate();
		topRowPanel.revalidate();
		ingotDialog.revalidate();
				
		System.out.println("IS_FULLSCREEN="+IS_FULLSCREEN);
	}
	
	private void editHallmarkGOMapping_actionPerformed()
	{
		editHallmarkGOMappingDialog editHallmarkGOMapping=new editHallmarkGOMappingDialog(directory, hallmarkGOMappingFileName);
		editHallmarkGOMapping.setVisible(true);
	}

	private void editCancerTypeView_actionPerformed(String currentView)
	{
		editCancerTypeViewDialog editCancerTypeView=new editCancerTypeViewDialog(directory, currentView, networkViewFileName, folder);
		editCancerTypeView.setVisible(true);
	}

	private void viewTargetCombination_actionPerformed(ArrayList<String> targetCombination)
	{
		visualizeTargetCombinationDialog visualizeTargetCombination=new visualizeTargetCombinationDialog(tempCombi, postgreDB);
		visualizeTargetCombination.setVisible(true);
		ArrayList<String> selectedTargetCombination=new ArrayList<String>();
		String selectedView=visualizeTargetCombination.getSelectedView();
		boolean includeDiseaseNode=visualizeTargetCombination.getIncludeDiseaseNode();
		if(targetCombination==null)
			selectedTargetCombination=visualizeTargetCombination.getSelectedTargetCombination();
		else
			selectedTargetCombination=targetCombination;
		System.out.println("selected target combination:"+selectedTargetCombination.toString());

		if(selectedView.compareTo(VIEW_ON_CURRENT_GRAPH)==0)
		{
			gephiGraph.restoreNodes();
			gephiGraph.colorAndResizeNodeSet(selectedTargetCombination, null, DISPLAY_TARGET_NODE);
			if(includeDiseaseNode==true)
				gephiGraph.colorAndResizeNodeSet(diseaseNodeList.getNodeList(), null, DISPLAY_DISEASE_NODE);
		}
		else if(selectedView.compareTo(VIEW_NODE_NEIGHBOURHOOD)==0)
		{
			//display a modal dialog with target node neighbourhood
			targetNodeNeighbourhoodFrame targetNodeNeighbourhood;
			if(includeDiseaseNode==true)
				targetNodeNeighbourhood=new targetNodeNeighbourhoodFrame(selectedTargetCombination, diseaseNodeList.getNodeList(), gephiGraph);
			else
				targetNodeNeighbourhood=new targetNodeNeighbourhoodFrame(selectedTargetCombination, null, gephiGraph);
			targetNodeNeighbourhood.setVisible(true);
		}
		else
		{
			//display a modal dialog with connections of target nodes 
			targetNodeConnectionsFrame targetNodeConnections;
			if(includeDiseaseNode==true)
				targetNodeConnections=new targetNodeConnectionsFrame(statusLabel, selectedTargetCombination, diseaseNodeList.getNodeList(), jungGraph);
			else
				targetNodeConnections=new targetNodeConnectionsFrame(statusLabel, selectedTargetCombination, null, jungGraph);
			targetNodeConnections.setVisible(true);
		}
		radioButtonGroup.clearSelection();
		diseaseTargetNodeRButton.setSelected(true);
		gephiGraph.resetColorAndSizeOfNode();
		displayDiseaseTargetNode_actionPerformed(selectedTargetCombination);
	}

	private void viewDiseaseNode_actionPerformed()
	{
		visualizeDiseaseNodeDialog visualizeDiseaseNode=new visualizeDiseaseNodeDialog(diseaseNodeList);
		visualizeDiseaseNode.setVisible(true);
		ArrayList<String> selectedDiseaseNodes=new ArrayList<String>();
		String selectedView=visualizeDiseaseNode.getSelectedView();
		selectedDiseaseNodes=visualizeDiseaseNode.getSelectedDiseaseNodeList();
		System.out.println("selected nodes:"+selectedDiseaseNodes.toString());
		if(selectedView.compareTo(VIEW_ON_CURRENT_GRAPH)==0)
			gephiGraph.colorAndResizeNodeSet(selectedDiseaseNodes, null, DISPLAY_DISEASE_NODE);
		else if(selectedView.compareTo(VIEW_NODE_NEIGHBOURHOOD)==0)
		{
			//display a modal dialog with disease node neighbourhood
			diseaseNodeNeighbourhoodFrame diseaseNodeNeighbourhood=new diseaseNodeNeighbourhoodFrame(selectedDiseaseNodes, gephiGraph);
			diseaseNodeNeighbourhood.setVisible(true);
		}
		else
		{
			//display a modal dialog with connections of disease nodes 
			diseaseNodeConnectionsFrame diseaseNodeConnections=new diseaseNodeConnectionsFrame(statusLabel, selectedDiseaseNodes, jungGraph);
			diseaseNodeConnections.setVisible(true);
		}
	}

	private void displayDiseaseTargetNode_actionPerformed(ArrayList<String> targetCombi)
	{
		System.out.println("display disease target node");

		String statusString="displaying disease nodes (red) and target combination nodes (green)...";
		updateStatus(statusString);		

		ArrayList<String> diseaseNode=diseaseNodeList.getNodeList();
		gephiGraph.colorAndResizeNodeSet(diseaseNode, null, DISPLAY_DISEASE_NODE);

		if(targetCombi.size()>0)
			gephiGraph.colorAndResizeNodeSet(targetCombi, null, DISPLAY_TARGET_NODE);
		else
			JOptionPane.showMessageDialog(new JFrame(), "There are no target combinations for this set of disease nodes", "Information", JOptionPane.INFORMATION_MESSAGE);

		statusString="disease nodes (red) and target combination nodes (green) displayed...";
		updateStatus(statusString);		
	}

	private void displayFoldChange_actionPerformed()
	{
		String statusString="displaying fold change...";
		updateStatus(statusString);		

		ArrayList<String> viewNodeNameList=postgreDB.getNetworkViewNode_foldChange_nodeNameList(selectedView);
		gephiGraph.colorAndResizeNodeSet(viewNodeNameList, postgreDB.getNetworkViewNode_foldChange_foldChangeList(selectedView, viewNodeNameList), DISPLAY_FOLD_CHANGE);

		statusString="fold change updated...";
		updateStatus(statusString);		
	}

	private void displayMutationFrequency_actionPerformed()
	{
		String statusString="displaying mutation frequency...";
		updateStatus(statusString);		

		ArrayList<String> viewNodeNameList=postgreDB.getNetworkViewNode_mutation_nodeNameList(selectedView);
		gephiGraph.colorAndResizeNodeSet(viewNodeNameList, postgreDB.getNetworkViewNode_mutation_PercentageList(selectedView, viewNodeNameList), DISPLAY_MUTATION_FREQUENCY);

		statusString="mutation frequency updated...";
		updateStatus(statusString);	
	}

	private void viewNodePairPathway_actionPerformed() 
	{
		if(GEPHIGRAPHREADY)
		{
			allHallmark hallmarkNodeList=postgreDB.getHallmark_hallmark();
			viewNodePairPathwayDialog viewNodePairPathway;

			viewNodePairWorkspace=interactivePC.newWorkspace(interactiveProject);
			interactivePC.openWorkspace(viewNodePairWorkspace); 
			
			if(pathwayNodeList.size()>0)
				viewNodePairPathway=new viewNodePairPathwayDialog(pathwayNodeList, jGraphTGraph, jungGraph,
						interactivePC, interactiveProject, interactiveWorkspace, viewNodePairWorkspace, postgreDB, selectedHallmarkList);
			else
				viewNodePairPathway=new viewNodePairPathwayDialog(allNodeArrList, jGraphTGraph, jungGraph,
						interactivePC, interactiveProject, interactiveWorkspace, viewNodePairWorkspace, postgreDB, selectedHallmarkList);
			viewNodePairPathway.setVisible(true);

			ArrayList<String> nodeList=new ArrayList<String>();
			nodeList=jungGraph.findConnectionOfCurrentGraphNodeList(statusLabel, viewNodePairPathway.getSelectedNodes());

			if(nodeList.size()==0)
				JOptionPane.showMessageDialog(new JFrame(), "There is no connection between "+viewNodePairPathway.getSelectedNodes().get(0)+" and "+viewNodePairPathway.getSelectedNodes().get(1), "No results", JOptionPane.INFORMATION_MESSAGE);
			else
			{
				pathwayNodeList=nodeList;
				//jungGraph.removeNodesNotInList(pathwayNodeList);
				postgreDB.clearPathwayTopologicalFeatures();
				//gephiGraph.removeNodesNotInList(pathwayNodeList);
				//speciesList=GUIwrapper.updateJListData(speciesList, pathwayNodeList);
				//ArrayList<String> crosstalkList=postgreDB.getPathway_pathwayNameListHavingNodes(pathwayNodeList);
				//crosstalkPathwayList=GUIwrapper.updateJListData(crosstalkPathwayList, crosstalkList);

				int pathwayTabIndex=resultTabbedPane.indexOfComponent(pathwayListScrollPane);
				if(pathwayTabIndex!=(-1))
					resultTabbedPane.remove(pathwayTabIndex);
				pathwayTabIndex=resultTabbedPane.indexOfComponent(crosstalkPathwayListScrollPane);
				if(pathwayTabIndex==(-1))
					resultTabbedPane.addTab("Crosstalk Pathway List", crosstalkPathwayListScrollPane);
				resultTabbedPane.setSelectedIndex(0);
				settingPanel.setVisible(false);

				visualizeNetworkWorker=new VisualizeNetworkWorker();
				ArrayList<Object> param=new ArrayList<Object>();
				param.add(viewNodePairPathway.getSelectedNodes());
				param.add(jungGraph);
				param.add(gephiGraph);
				param.add(hallmarkNodeList);
				param.add(selectedHallmarkList);
				visualizeNetworkWorker.Start(VIEW_NODE_PAIR_PATHWAY, param);
			}
			sendVirtualClickToRefreshGraphDisplay(); 
		}
	}

	private void viewNodePair_actionPerformed() 
	{
		if(GEPHIGRAPHREADY)
		{
			//allHallmark hallmarkNodeList=postgreDB.getHallmark_hallmark();
			viewNodePairPathwayDialog viewNodePairPathway;

			viewNodePairWorkspace=interactivePC.newWorkspace(interactiveProject);
			interactivePC.openWorkspace(viewNodePairWorkspace); 
			
			if(pathwayNodeList.size()>0)
				viewNodePairPathway=new viewNodePairPathwayDialog(pathwayNodeList, jGraphTGraph, jungGraph,
						interactivePC, interactiveProject, interactiveWorkspace, viewNodePairWorkspace, postgreDB, selectedHallmarkList);
			else
				viewNodePairPathway=new viewNodePairPathwayDialog(allNodeArrList, jGraphTGraph, jungGraph,
						interactivePC, interactiveProject, interactiveWorkspace, viewNodePairWorkspace, postgreDB, selectedHallmarkList);
			viewNodePairPathway.setVisible(true);
			sendVirtualClickToRefreshGraphDisplay(); 
		}
	}
	
	private void highlightDynamics_actionPerformed() 
	{
		if(GEPHIGRAPHREADY)
		{
			allHallmark hallmarkNodeList=postgreDB.getHallmark_hallmark();
			ArrayList<edge> edgesWithDynamics=new ArrayList<edge>();
			edgesWithDynamics=postgreDB.getKinetics_edgeList();
			System.out.println("dynamics:"+edgesWithDynamics.size());
			visualizeNetworkWorker=new VisualizeNetworkWorker();
			ArrayList<Object> param=new ArrayList<Object>();
			param.add(edgesWithDynamics);
			param.add(gephiGraph);
			param.add(hallmarkNodeList);
			param.add(selectedHallmarkList);
			visualizeNetworkWorker.Start(HIGHLIGHT_DYNAMICS, param);
		}
	}

	private void viewCompound_actionPerformed() 
	{
		if(GEPHIGRAPHREADY)
		{
			ArrayList<String> list=new ArrayList<String>();
			list.add("EDNRA");
			list.add("GNA14");
			gephiGraph.collapsePhysicalInteraction(list);
		}
	}

	private void viewCharacterizationResult_actionPerformed() 
	{
		selectCharacterizationResultDialog selectCharacterization=new selectCharacterizationResultDialog(postgreDB);
		selectCharacterization.setVisible(true);
	}

	private boolean currPathwayIsWholeNetwork() 
	{
		if(settingPanel.isVisible()==true)
			return true;
		else
			return false;
	}

	private void characterizeHallmarks_actionPerformed() 
	{
		if(GEPHIGRAPHREADY)
		{
			characterizeHallmarkDialog characterizeHallmark=new characterizeHallmarkDialog(postgreDB);
			characterizeHallmark.setVisible(true);
			boolean performCharacterization=characterizeHallmark.getPerformCharacterization();
			if(performCharacterization)
			{
				ArrayList<String> selectedFeatures=characterizeHallmark.getSelectedFeatures();
				ArrayList<String> selectedHallmarks=characterizeHallmark.getSelectedHallmarks();
				ArrayList<String> uncomputedFeatures=characterizeHallmark.getSelectedUncomputedFeatures();
				int wmcWt=characterizeHallmark.getWMCWeight();
				System.out.println(selectedFeatures);
				System.out.println(selectedHallmarks);
				System.out.println(uncomputedFeatures);
				System.out.println(wmcWt);

				if(selectedHallmarks.size()==0)
					JOptionPane.showMessageDialog(new JFrame(), "No hallmark is selected.", "No results", JOptionPane.INFORMATION_MESSAGE);
				else
				{
					performAnalysisWorker=new PerformAnalysisWorker();
					ArrayList<Object> param=new ArrayList<Object>();
					param.add(uncomputedFeatures);
					param.add(selectedFeatures);
					param.add(selectedHallmarks);
					param.add(wmcWt);
					performAnalysisWorker.Start(CHARACTERIZE_HALLMARKS, param);
					/*
					if(uncomputedFeatures.size()>0)
					{
						statusString="Characterizing hallmarks...computing uncomputed features";
						postgreDB.resetComputedFeature_subset(uncomputedFeatures);
						int totalCount=uncomputedFeatures.size();
						for(int i=0; i<totalCount; i++)
						{
							double percentComplete=100.0*i/(totalCount-1);
							DecimalFormat df = new DecimalFormat();
							df.setMaximumFractionDigits(2);
							statusString="Characterizing hallmarks...computing uncomputed features ["+df.format(percentComplete)+"%]";
							computeNetworkTopologicalFeature(uncomputedFeatures.get(i));
						}
					}
					statusString="Characterizing hallmarks...partitioning data - getting hallmark nodes";
					ArrayList<ArrayList<String>> hallmarkNodes=new ArrayList<ArrayList<String>>();
					hallmarkNodes=postgreDB.getHallmark_nodeNameIndividualListOfSelectedHallmarks(selectedHallmarks);
					statusString="Characterizing hallmarks...partitioning data - computing number of partitions";
					ArrayList<Integer> numOfFolds=new ArrayList<Integer>(); //ensure each fold has at least one target
					for(int i=0; i<hallmarkNodes.size(); i++)
					{
						if(hallmarkNodes.get(i).size()<10)
							numOfFolds.add(hallmarkNodes.get(i).size());
						else
							numOfFolds.add(maxFold);
					}
					int totalCount=selectedHallmarks.size();
					for(int i=0; i<totalCount; i++)
					{
						double percentComplete=100.0*i/(totalCount-1);
						if(totalCount==1)
							percentComplete=100;
						DecimalFormat df = new DecimalFormat();
						df.setMaximumFractionDigits(2);
						statusString="Characterizing hallmarks...for each hallmark ["+df.format(percentComplete)+"%] generate fold partitions for cross validation";
						int thisHallmarkMaxFold=numOfFolds.get(i);
						ArrayList<String> thisHallmarkHallmarkNodes=hallmarkNodes.get(i);
						ArrayList<String> allNodes=new ArrayList<String>();
						for(int j=0; j<allNodeArrList.size(); j++)
							allNodes.add(allNodeArrList.get(j));
						crossValidationData xValidationData=new crossValidationData(thisHallmarkMaxFold, thisHallmarkHallmarkNodes, allNodes);
						int tuneLvl=1;//tuneLvl=3;
						statusString="Characterizing hallmarks...for each hallmark ["+df.format(percentComplete)+"%] create and initialize SVM";
						svmAnalysis svmObj=new svmAnalysis();
						svmObj.initializeSVM(selectedHallmarks.get(i), thisHallmarkMaxFold, xValidationData, null, null, selectedFeatures, thisHallmarkHallmarkNodes, statusLabel, String.valueOf(df.format(percentComplete)));
						statusString="Characterizing hallmarks...for each hallmark ["+df.format(percentComplete)+"%] SVM performing training and feature selection (backward stepwise elimination)";
						if(wmcWt==0)
							svmObj.BSEModelSelection(tuneLvl, false, wmcWt, strConstants.getLinear(), directory, directory+"svmTraining\\");
						else
							svmObj.BSEModelSelection(tuneLvl, true, wmcWt, strConstants.getLinear(), directory, directory+"svmTraining\\");
						//computeNetworkTopologicalFeature(uncomputedFeatures.get(i));
					}
					statusString="finish characterizing hallmarks";
					updateStatus(statusString);
					 */
				}
			}
		}
	}

	private void viewFeatureResult_actionPerformed() 
	{
		if(GEPHIGRAPHREADY)
		{
			ArrayList<String> nList=new ArrayList<String>();
			if(settingPanel.isVisible())
				nList=allNodeArrList;
			else
				nList=pathwayNodeList;
			System.out.println("nList.size():"+nList.size());
			System.out.println(selectedFeatures.toString());

			performAnalysisWorker=new PerformAnalysisWorker();
			ArrayList<Object> param=new ArrayList<Object>();
			param.add(selectedFeatures);
			param.add(nList);
			param.add(postgreDB);
			param.add(statusLabel);
			param.add(currPathwayIsWholeNetwork());
			performAnalysisWorker.Start(VIEW_FEATURE_RESULT, param);
		}
	}

	private void computeNetworkTopologicalFeature(String feature) 
	{
		ArrayList<String> features=new ArrayList<String>();
		if(feature.compareTo(strConstants.getDBInDegree())==0 || feature.compareTo(strConstants.getDBOutDegree())==0 ||
				feature.compareTo(strConstants.getDBTotalDegree())==0)
		{
			if(postgreDB.getComputedFeature(strConstants.getDBInDegree())==false || postgreDB.getComputedFeature(strConstants.getDBOutDegree())==false ||
					postgreDB.getComputedFeature(strConstants.getDBTotalDegree())==false)
			{
				jungGraph.getDegree(statusLabel, true);
				features.add(strConstants.getDBInDegree());
				features.add(strConstants.getDBOutDegree());
				features.add(strConstants.getDBTotalDegree());
			}
		}
		if(feature.compareTo(strConstants.getDBEigenvector())==0)
		{
			jungGraph.getEigenvector(statusLabel, true);
			features.add(strConstants.getDBEigenvector());
		}
		if(feature.compareTo(strConstants.getDBBetweenness())==0)
		{
			jungGraph.getBetweenness(statusLabel, true);
			features.add(strConstants.getDBBetweenness());
		}
		if(feature.compareTo(strConstants.getDBBridgingCoeff())==0)
		{
			jungGraph.getBridgingCoeff(statusLabel, true);
			features.add(strConstants.getDBBridgingCoeff());
		}
		if(feature.compareTo(strConstants.getDBBridgingCentrality())==0)
		{
			jungGraph.getBridgingCentrality(statusLabel, true);
			features.add(strConstants.getDBBridgingCentrality());
		}
		if(feature.compareTo(strConstants.getDBUndirClustering())==0)
		{
			jungGraph.getUndirectedClusteringCoefficient(statusLabel, true);
			features.add(strConstants.getDBUndirClustering());
		}
		if(feature.compareTo(strConstants.getDBInClustering())==0)
		{
			jungGraph.getInClusteringCoefficient(statusLabel, true);
			features.add(strConstants.getDBInClustering());
		}
		if(feature.compareTo(strConstants.getDBOutClustering())==0)
		{
			jungGraph.getOutClusteringCoefficient(statusLabel, true);
			features.add(strConstants.getDBOutClustering());
		}
		if(feature.compareTo(strConstants.getDBCycClustering())==0 || feature.compareTo(strConstants.getDBMidClustering())==0)
		{
			if(postgreDB.getComputedFeature(strConstants.getDBCycClustering())==false || postgreDB.getComputedFeature(strConstants.getDBMidClustering())==false)
			{
				jungGraph.getCycleAndMiddlemanClusteringCoefficient(statusLabel, true);
				features.add(strConstants.getDBCycClustering());
				features.add(strConstants.getDBMidClustering());
			}
		}
		postgreDB.updateNodeRanks(features, statusLabel);
	}

	private void computePathwayTopologicalFeatures_actionPerformed() 
	{
		if(GEPHIGRAPHREADY)
		{
			computeTopologicalFeaturesDialog computeTopologicalFeatures=new computeTopologicalFeaturesDialog(currPathwayIsWholeNetwork(), postgreDB.getFeature_featureComputed(strConstants.getDBInDegree()));
			computeTopologicalFeatures.setVisible(true);
			boolean performComputation=computeTopologicalFeatures.getPerformComputation();
			if(performComputation)
			{
				selectedFeatures=computeTopologicalFeatures.getFeaturesToCompute();
				//postgreDB.clearPathwayTopologicalFeatures();
				postgreDB.resetComputedFeature();

				performAnalysisWorker=new PerformAnalysisWorker();
				ArrayList<Object> param=new ArrayList<Object>();
				param.add(selectedFeatures);
				performAnalysisWorker.Start(COMPUTE_TOPOLOGICAL_FEATURES, param);
			}
		}	
	}

	private void createAndViewPathway_actionPerformed() 
	{
		if(GEPHIGRAPHREADY)
		{
			allHallmark hallmarkNodeList=postgreDB.getHallmark_hallmark();
			createAndViewPathwayDialog createAndViewPathway=new createAndViewPathwayDialog(allNodeArrList);
			createAndViewPathway.setVisible(true);
			ArrayList<String> pathwayNodeList=createAndViewPathway.getSelectedNodes();
			String pathwayDisplay=createAndViewPathway.getDisplay();
			boolean performVisualization=createAndViewPathway.getPerformVisualization();
			if(performVisualization==true)
			{
				if(pathwayNodeList.size()==0)
					JOptionPane.showMessageDialog(new JFrame(), "No nodes are selected for forming the pathway.", "No results", JOptionPane.INFORMATION_MESSAGE);
				else
				{
					vertexNodeContent.removeAll();
					vertexReactionContent.removeAll();
					jungGraph.resetGraph();
					gephiGraph.destroyGraph();
					postgreDB.clearPathwayTopologicalFeatures();
					oneHopNeighbourNodeList=new ArrayList<String>();
					speciesList=GUIwrapper.updateJListData(speciesList, pathwayNodeList);
					int pathwayTabIndex=resultTabbedPane.indexOfComponent(pathwayListScrollPane);
					if(pathwayTabIndex!=(-1))
						resultTabbedPane.remove(pathwayTabIndex);
					pathwayTabIndex=resultTabbedPane.indexOfComponent(crosstalkPathwayListScrollPane);
					if(pathwayTabIndex!=(-1))
						resultTabbedPane.remove(pathwayTabIndex);
					//update legend 
					addVertexLegend(true);
					//resultTabbedPane.repaint();
					settingPanel.setVisible(false);

					visualizeNetworkWorker=new VisualizeNetworkWorker();
					ArrayList<Object> param=new ArrayList<Object>();
					param.add(pathwayDisplay);
					param.add(pathwayNodeList);
					param.add(gephiGraph);
					param.add(hallmarkNodeList);
					param.add(selectedHallmarkList);
					visualizeNetworkWorker.Start(CREATE_AND_VIEW_PATHWAY, param);
				}
			}
		}
	}

	private void viewKEGGPathway(ArrayList<String> selectedPathway) 
	{
		//String statusString;
		vertexNodeContent.removeAll();
		vertexReactionContent.removeAll();
		jungGraph.resetGraph();
		gephiGraph.destroyGraph();
		postgreDB.clearPathwayTopologicalFeatures();

		int pathwayTabIndex=resultTabbedPane.indexOfComponent(pathwayListScrollPane);
		if(pathwayTabIndex==(-1))
			resultTabbedPane.addTab("Pathway List", pathwayListScrollPane);
		pathwayTabIndex=resultTabbedPane.indexOfComponent(crosstalkPathwayListScrollPane);
		if(pathwayTabIndex==(-1))
			resultTabbedPane.addTab("Crosstalk Pathway List", crosstalkPathwayListScrollPane);
		resultTabbedPane.setSelectedIndex(0);
		settingPanel.setVisible(false);

		visualizeNetworkWorker=new VisualizeNetworkWorker();
		ArrayList<Object> param=new ArrayList<Object>();
		param.add(postgreDB);
		param.add(selectedPathway);
		param.add(speciesList);
		param.add(GUIwrapper);
		//param.add(pathwayNodeList);
		param.add(pathwayList);
		param.add(selectedPathwayList);
		param.add(gephiGraph);
		visualizeNetworkWorker.Start(VIEW_KEGG_ANNOTATED_PATHWAY, param);
	}

	private void viewKEGGAnnotatedPathway_actionPerformed() 
	{
		if(GEPHIGRAPHREADY)
		{
			visualizePathwayDialog visualizePathway=new visualizePathwayDialog(postgreDB);
			visualizePathway.setVisible(true);
			selectedPathwayList=visualizePathway.getSelectedPathwayList();
			boolean performVisualization=visualizePathway.getPerformVisualization();
			if(performVisualization==true)
			{
				System.out.println("selectedPathway: "+selectedPathwayList.toString());
				if(selectedPathwayList.size()==1)
				{
					ArrayList<String> nodeList=postgreDB.getPathway_getNodeInPathway(processName(selectedPathwayList.get(0)));
					if(nodeList.size()==0)
					{
						String pathway=selectedPathwayList.get(0);
						JOptionPane.showMessageDialog(new JFrame(), "No nodes in the network is annotated with the pathway "+pathway, "No results", JOptionPane.INFORMATION_MESSAGE);
					}
					else
						viewKEGGPathway(selectedPathwayList);
				}
				else
					viewKEGGPathway(selectedPathwayList);	
			}
			//update legend 
			addVertexLegend(false);
			sendVirtualClickToRefreshGraphDisplay();
		}
	}

	private void drawPathwayInducedGraph(ArrayList<String> nodeList) 
	{
		String statusString;
		ArrayList<edge> edgeList=postgreDB.getNetworkEdge_edgeListOfInducedGraph(nodeList);
		int currCount=1;
		int totalCount=edgeList.size();
		for(int i=0; i<edgeList.size(); i++)
		{
			String sourceNodeName;
			String targetNodeName;
			if(edgeList.get(i).getSourceName()!=null)
				sourceNodeName=edgeList.get(i).getSourceName();
			else
				sourceNodeName=edgeList.get(i).getSourceID();
			if(edgeList.get(i).getTargetName()!=null)
				targetNodeName=edgeList.get(i).getTargetName();
			else
				targetNodeName=edgeList.get(i).getTargetID();
			double percentComplete=100.0*currCount/totalCount;
			if(edgeList.get(i).getType().compareTo("Phy")==0)
			{
				jungGraph.addAnUndirectedEdge(sourceNodeName, targetNodeName);
				gephiGraph.addAnUndirectedEdge(sourceNodeName, targetNodeName);
				statusString=String.format("creating pathway [%,.2f%% complete]...adding edge %s---%s", percentComplete, sourceNodeName, targetNodeName);
			}
			else
			{
				jungGraph.addADirectedEdge(sourceNodeName, targetNodeName);
				gephiGraph.addADirectedEdge(sourceNodeName, targetNodeName);
				statusString=String.format("creating pathway [%,.2f%% complete]...adding edge %s--->%s", percentComplete, sourceNodeName, targetNodeName);
			}
			updateStatus(statusString);		
			currCount=currCount+1;
		}	
	}

	private void createPathway(ArrayList<String> nodeList, String display) 
	{
		String name;
		String statusString;
		int currCount=1;
		int totalCount=nodeList.size();
		String nodeSize=suggestGephiNodeSizeToUse(nodeList.size());
		int nodeDBID=0;
		System.out.println("createPathway");
		System.out.println("nodeList:"+nodeList.toString());
		for(int i=0; i<nodeList.size(); i++)
		{
			name=nodeList.get(i);
			jungGraph.addANode(name);
			gephiGraph.addANode(name, nodeSize);
			nodeDBID=postgreDB.getNodeDBID(name);
			postgreDB.addNewPathwayNode(postgreDB.getNodeID(nodeDBID), postgreDB.getNodeName(nodeDBID));
			//for updating status
			double percentComplete=100.0*currCount/totalCount;
			statusString=String.format("creating pathway [%,.2f%% complete]...adding node %s", percentComplete, name);
			updateStatus(statusString);	
			currCount=currCount+1;
			System.out.println("draw node "+name);
		}
		if(display.compareTo(strConstants.getInducedGraph())==0)
			drawPathwayInducedGraph(nodeList);
		else
		{
			oneHopNeighbourNodeList=postgreDB.getNetworkEdge_oneHopNeighbourNameListOfNodeList(nodeList);
			System.out.println("oneHopNeighbourNodeList:"+oneHopNeighbourNodeList.toString());
			//add nHop vertices
			currCount=1;
			totalCount=oneHopNeighbourNodeList.size();
			if(oneHopNeighbourNodeList.size()>0)
			{
				for(int i=0; i<oneHopNeighbourNodeList.size(); i++)
				{
					name=oneHopNeighbourNodeList.get(i);
					jungGraph.addANode(name);
					gephiGraph.addANode(name, nodeSize+totalCount);
					nodeDBID=postgreDB.getNodeDBID(name);
					postgreDB.addNewPathwayNode(postgreDB.getNodeID(nodeDBID), postgreDB.getNodeName(nodeDBID));
					//for updating status
					double percentComplete=100.0*currCount/totalCount;
					statusString=String.format("creating pathway [%,.2f%% complete]...adding one-hop node %s", percentComplete, name);
					updateStatus(statusString);	
					currCount=currCount+1;
				}
			}
			//add nHop edges
			ArrayList<String> newNodeList=new ArrayList<String>();
			for(int i=0; i<nodeList.size(); i++)
				newNodeList.add(nodeList.get(i));
			if(oneHopNeighbourNodeList.size()>0)
			{
				for(int i=0; i<oneHopNeighbourNodeList.size(); i++)
				{
					if(newNodeList.contains(oneHopNeighbourNodeList.get(i))==false)
						newNodeList.add(oneHopNeighbourNodeList.get(i));
				}
			}
			ArrayList<edge> edgeList=postgreDB.getNetworkEdge_edgeListOfInducedGraph(newNodeList);
			//ArrayList<String> neighbourList=new ArrayList<String>();
			currCount=1;
			totalCount=edgeList.size();
			for(int i=0; i<edgeList.size(); i++)
			{
				String sourceNodeName;
				String targetNodeName;
				if(edgeList.get(i).getSourceName()!=null)
					sourceNodeName=edgeList.get(i).getSourceName();
				else
					sourceNodeName=edgeList.get(i).getSourceID();
				if(edgeList.get(i).getTargetName()!=null)
					targetNodeName=edgeList.get(i).getTargetName();
				else
					targetNodeName=edgeList.get(i).getTargetID();
				double percentComplete=100.0*currCount/totalCount;
				if(edgeList.get(i).getType().compareTo("Phy")==0)
				{
					jungGraph.addAnUndirectedEdge(sourceNodeName, targetNodeName);
					gephiGraph.addAnUndirectedEdge(sourceNodeName, targetNodeName);
					statusString=String.format("creating pathway [%,.2f%% complete]...adding edge %s---%s", percentComplete, sourceNodeName, targetNodeName);
				}
				else
				{
					jungGraph.addADirectedEdge(sourceNodeName, targetNodeName);
					gephiGraph.addADirectedEdge(sourceNodeName, targetNodeName);
					statusString=String.format("creating pathway [%,.2f%% complete]...adding edge %s--->%s", percentComplete, sourceNodeName, targetNodeName);
				}
				updateStatus(statusString);		
				currCount=currCount+1;
			}
			currCount=1;
			totalCount=newNodeList.size();
			for(int i=0; i<newNodeList.size(); i++)
			{
				name=newNodeList.get(i);
				gephiGraph.updateNodeSize(name, suggestGephiNodeSizeToUse(newNodeList.size()));
				//for updating status
				double percentComplete=100.0*currCount/totalCount;
				statusString=String.format("creating pathway [%,.2f%% complete]...updating node size of %s", percentComplete, name);
				updateStatus(statusString);	
				currCount=currCount+1;
			}
			nodeSize=suggestGephiNodeSizeToUse(newNodeList.size());
		}
		gephiGraph.updateEdgeThickness(nodeSize);	
	}

	private ArrayList<String> updateSelectedHallmarkList(visualizeHallmarkDialog dialog) 
	{
		ArrayList<String> hallmarkList=new ArrayList<String>();

		if(dialog.getViewHallmark_Proliferation()==true)
			hallmarkList.add(HALLMARK_PROLIFERATION_DB);
		if(dialog.getViewHallmark_GrowthRepressor()==true)
			hallmarkList.add(HALLMARK_GROWTH_REPRESSOR_DB);
		if(dialog.getViewHallmark_Apoptosis()==true)
			hallmarkList.add(HALLMARK_APOPTOSIS_DB);
		if(dialog.getViewHallmark_ReplicativeImmortality()==true)
			hallmarkList.add(HALLMARK_REPLICATIVE_IMMORTALITY_DB);
		if(dialog.getViewHallmark_Angiogenesis()==true)
			hallmarkList.add(HALLMARK_ANGIOGENESIS_DB);
		if(dialog.getViewHallmark_Metastasis()==true)
			hallmarkList.add(HALLMARK_METASTASIS_DB);
		if(dialog.getViewHallmark_Metabolism()==true)
			hallmarkList.add(HALLMARK_METABOLISM_DB);
		if(dialog.getViewHallmark_ImmuneDestruction()==true)
			hallmarkList.add(HALLMARK_IMMUNE_DESTRUCTION_DB);
		if(dialog.getViewHallmark_GenomeInstability()==true)
			hallmarkList.add(HALLMARK_GENOME_INSTABILITY_DB);
		if(dialog.getViewHallmark_TumorPromotingInflammation()==true)
			hallmarkList.add(HALLMARK_TUMOR_PROMOTING_INFLAMMATION_DB);

		return hallmarkList;
	}

	private void viewHallmark_actionPerformed() 
	{
		allHallmark hallmarkNodeList=postgreDB.getHallmark_hallmark();
		if(GEPHIGRAPHREADY)
		{
			visualizeHallmarkDialog visualizeHallmark=new visualizeHallmarkDialog(selectedHallmarkList, directory);
			visualizeHallmark.setVisible(true);
			boolean performVisualization=visualizeHallmark.getPerformVisualization();
			String pathwayDisplay=visualizeHallmark.getDisplay();
			String outputDestination=visualizeHallmark.getOutputDestination();
			boolean exportIndividualHallmarkFiles=visualizeHallmark.getExportIndividualHallmarkFiles();
			boolean exportCombinedHallmarkFiles=visualizeHallmark.getExportCombinedHallmarkFiles();
			boolean exportFiles=visualizeHallmark.getExportFiles();

			System.out.println("performVisualization:"+performVisualization);
			if(performVisualization==true)
			{
				oldSelectedHallmarkList=new ArrayList<String>();
				for(int i=0; i<selectedHallmarkList.size(); i++)
					oldSelectedHallmarkList.add(selectedHallmarkList.get(i));
				
				selectedHallmarkList=updateSelectedHallmarkList(visualizeHallmark);
				if(pathwayDisplay.compareTo(strConstants.getInducedGraph())==0)
				{
					System.out.println("pathwayDisplay is inducedGraph");
					pathwayNodeList=postgreDB.getHallmark_nodeNameCombinedListOfSelectedHallmarks(selectedHallmarkList);
					if(pathwayNodeList.size()==0)
						JOptionPane.showMessageDialog(new JFrame(), "No nodes are annotated with the selected hallmark(s).", "No results", JOptionPane.INFORMATION_MESSAGE);
					else
					{
						vertexNodeContent.removeAll();
						vertexReactionContent.removeAll();
						jungGraph.resetGraph();
						gephiGraph.destroyGraph();
						postgreDB.clearPathwayTopologicalFeatures();
						oneHopNeighbourNodeList=new ArrayList<String>();
						speciesList=GUIwrapper.updateJListData(speciesList, pathwayNodeList);
						int pathwayTabIndex=resultTabbedPane.indexOfComponent(pathwayListScrollPane);
						if(pathwayTabIndex==(-1))
							resultTabbedPane.addTab("Pathway List", pathwayListScrollPane);
						pathwayTabIndex=resultTabbedPane.indexOfComponent(crosstalkPathwayListScrollPane);
						if(pathwayTabIndex!=(-1))
							resultTabbedPane.remove(pathwayTabIndex);
						resultTabbedPane.setSelectedIndex(0);
						settingPanel.setVisible(false);

						visualizeNetworkWorker=new VisualizeNetworkWorker();
						ArrayList<Object> param=new ArrayList<Object>();
						param.add(selectedPathwayList);
						param.add(pathwayList);
						param.add(postgreDB);
						param.add(pathwayNodeList);
						param.add(GUIwrapper);
						param.add(pathwayDisplay);
						param.add(oneHopNeighbourNodeList);
						param.add(gephiGraph);
						param.add(hallmarkNodeList);
						param.add(selectedHallmarkList);
						visualizeNetworkWorker.Start(VIEW_HALLMARK_INDUCED, param);
						
						/*
						selectedPathwayList=postgreDB.getPathway_pathwayNameListHavingNodes(pathwayNodeList);
						pathwayList=GUIwrapper.updateJListData(pathwayList, selectedPathwayList);
						createPathway(pathwayNodeList, pathwayDisplay);
						sendVirtualClickToRefreshGraphDisplay();
						statusString="visualizing hallmark pathway...configuring pathway graph layout";
						updateStatus(statusString);
						int size=pathwayNodeList.size()+oneHopNeighbourNodeList.size();
						if(size<50)
							gephiGraph.layoutInteractiveGraph_labelAdjust();
						else if(size>=50 && size<100)
							gephiGraph.layoutInteractiveGraph_fruchtermanReingold();
						else
							gephiGraph.layoutInteractiveGraph_yifanhu();
						statusString="visualizing hallmark pathway...coloring hallmark nodes";
						updateStatus(statusString);
						//uncolor all previous hallmarks
						gephiGraph.resetHallmarkNode();
						//color selected hallmarks
						gephiGraph.colorAndResizeHallmarkNode(hallmarkNodeList, selectedHallmarkList);
						//sendVirtualClickToRefreshGraphDisplay();

						statusString="finish visualizing hallmark pathway";
						updateStatus(statusString);
						 */
					}
				}
				else
				{
					vertexNodeContent.removeAll();
					vertexReactionContent.removeAll();
					//proceed to visualize network
					ArrayList<String> list=getSpeciesListName(postgreDB.getNetworkNode_nodeIDList(), postgreDB.getNetworkNode_nodeNameList());
					int i=0;
					for(i=0; i<list.size(); i++)
						allNodeArrList.add(list.get(i));

					visualizeNetworkWorker=new VisualizeNetworkWorker();
					ArrayList<Object> param=new ArrayList<Object>();
					param.add(gephiGraph);
					param.add(hallmarkNodeList);
					param.add(selectedHallmarkList);
					param.add(pathwayListScrollPane);
					param.add(interactiveGraphPanel);

					visualizeNetworkWorker.Start(VIEW_HALLMARK, param);

					if(selectedHallmarkList.size()>0 && exportFiles==true)
					{
						performAnalysisWorker=new PerformAnalysisWorker();
						param=new ArrayList<Object>();
						param.add(selectedHallmarkList);
						param.add(outputDestination);
						param.add(exportIndividualHallmarkFiles);
						param.add(exportCombinedHallmarkFiles);
						param.add(speciesList);
						param.add(postgreDB);
						param.add(directory);
						performAnalysisWorker.Start(EXPORT_HALLMARK_NODE_FILES, param);
					}
					viewHallmarkWorkspace=interactivePC.duplicateWorkspace(interactiveWorkspace);
					interactivePC.openWorkspace(viewHallmarkWorkspace); 
					viewSelectedHallmarkSummaryDialog hallmarkSummary=new viewSelectedHallmarkSummaryDialog(postgreDB, selectedHallmarkList, oldSelectedHallmarkList, speciesList,
					interactivePC, interactiveProject, interactiveWorkspace, viewHallmarkWorkspace, statusLabel, interactiveGraphPanel, allNodeArrList);
					hallmarkSummary.setVisible(true);
					
					/*
 			    	statusString="visualizing hallmark on current graph...";
					updateStatus(statusString);		

					//uncolor all previous hallmarks
					gephiGraph.resetHallmarkNode();
					//color selected hallmarks
					gephiGraph.colorAndResizeHallmarkNode(hallmarkNodeList, selectedHallmarkList);
					pathwayListScrollPane.setVisible(true);
					sendVirtualClickToRefreshGraphDisplay();

					GEPHIGRAPHREADY=true;
					statusLabel.setText("finish visualizing hallmark on current graph.");
					//sendVirtualClickToRefreshGraphDisplay();

					 */
				}
				//update legend 
				addVertexLegend(true);
				vertexTabbedPanel.setSelectedIndex(2);
			}
		}
	}

	private void searchNode_actionPerformed() 
	{
		String node=searchText.getText();
		int index = speciesList.getNextMatch(node, 0, Position.Bias.Forward);
		if (index != -1)
			speciesList.setSelectedValue(node,true);
	}

	private ArrayList<String> getSpeciesListName(ArrayList<String> IdList, ArrayList<String> NameList) 
	{
		ArrayList<String> speciesName = new ArrayList<String>();
		int i=0;
		for (i=0; i<IdList.size(); i++) 
		{
			if (NameList.get(i) == null || NameList.get(i).length() == 0)
				speciesName.add(IdList.get(i));
			else
				speciesName.add(NameList.get(i));
		}
		return speciesName;
	}

	private void editAnnotation_actionPerformed() 
	{
		String node=null;
		if(speciesList.getSelectedValue()!=null)
			node=speciesList.getSelectedValue().toString();
		if(node==null)
			JOptionPane.showMessageDialog(new JFrame(), "Please select a node that you wish to annotate from 'Species List'", "Error", JOptionPane.ERROR_MESSAGE);
		else
		{
			editAnnotationDialog editAnnotation=new editAnnotationDialog(node, postgreDB);
			editAnnotation.setVisible(true);
		}
	}

	private void editHallmark_actionPerformed() 
	{
		String node=null;
		if(speciesList.getSelectedValue()!=null)
			node=speciesList.getSelectedValue().toString();
		if(node==null)
			JOptionPane.showMessageDialog(new JFrame(), "Please select a node that you wish to edit hallmark from 'Species List'", "Error", JOptionPane.ERROR_MESSAGE);
		else
		{
			editHallmarkDialog editHallmark=new editHallmarkDialog(node, postgreDB);
			editHallmark.setVisible(true);
			getAndDisplaySpeciesInformation(node);
		}
	}

	private void editGraph_actionPerformed() 
	{
		if(GEPHIGRAPHREADY)
		{
			editGraphDialog editGraph=new editGraphDialog(postgreDB);
			editGraph.setVisible(true);

			if(editGraph.getUpdateGraph()==true)
			{
				gephiGraph.resetGraph();
				vertexNodeContent.removeAll();
				vertexReactionContent.removeAll();

				allNodeArrList=new ArrayList<String>();
				ArrayList<String> list=getSpeciesListName(postgreDB.getNetworkNode_nodeIDList(), postgreDB.getNetworkNode_nodeNameList());
				int i=0;
				for(i=0; i<list.size(); i++)
					allNodeArrList.add(list.get(i));

				visualizeNetworkWorker=new VisualizeNetworkWorker();
				ArrayList<Object> param=new ArrayList<Object>();
				param.add(gephiGraph);
				param.add(postgreDB);
				param.add(GUIwrapper);
				param.add(speciesList);
				param.add(allNodeArrList);
				visualizeNetworkWorker.Start(EDIT_GRAPH, param);
			}
		}
	}

	private void sendVirtualClickToRefreshGraphDisplay() 
	{
		System.out.println("send virtual click");
		Point currMousePosition=MouseInfo.getPointerInfo().getLocation();
		Robot robot;
		try {
			robot = new Robot();
			robot.mouseMove(interactiveGraphPanel.getLocationOnScreen().x+10, interactiveGraphPanel.getLocationOnScreen().y+10);
			robot.mousePress(InputEvent.BUTTON1_MASK); //BUTTON1_MASK is the left button,
			robot.mouseRelease(InputEvent.BUTTON1_MASK);
			robot.mouseMove(currMousePosition.x, currMousePosition.y);
		} catch (AWTException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private boolean computedNetworkFeatureExists() 
	{
		if (postgreDB.getFeature_featureComputed(strConstants.getDBInDegree())==0)
			return true;
		if (postgreDB.getFeature_featureComputed(strConstants.getDBOutDegree())==0)
			return true;
		if (postgreDB.getFeature_featureComputed(strConstants.getDBTotalDegree())==0)
			return true;
		if (postgreDB.getFeature_featureComputed(strConstants.getDBEigenvector())==0)
			return true;
		if (postgreDB.getFeature_featureComputed(strConstants.getDBBetweenness())==0)
			return true;
		if (postgreDB.getFeature_featureComputed(strConstants.getDBBridgingCoeff())==0)
			return true;
		if (postgreDB.getFeature_featureComputed(strConstants.getDBBridgingCentrality())==0)
			return true;
		if (postgreDB.getFeature_featureComputed(strConstants.getDBUndirClustering())==0)
			return true;
		if (postgreDB.getFeature_featureComputed(strConstants.getDBInClustering())==0)
			return true;
		if (postgreDB.getFeature_featureComputed(strConstants.getDBOutClustering())==0)
			return true;
		if (postgreDB.getFeature_featureComputed(strConstants.getDBCycClustering())==0)
			return true;
		if (postgreDB.getFeature_featureComputed(strConstants.getDBMidClustering())==0)
			return true;
		if (postgreDB.getFeature_featureComputed(strConstants.getDBTDE())==0)
			return true;
		return false;
	}

	private void reloadNetwork() 
	{
		visualizeNetworkWorker=new VisualizeNetworkWorker();
		ArrayList<Object> param=new ArrayList<Object>();
		param.add(allNodeArrList);
		param.add(selectedView);
		param.add(selectedLayout);
		visualizeNetworkWorker.Start(RELOAD_NETWORK, param);

	}
	
	private void visualizePreloadedNetwork_actionPerformed() 
	{
		ArrayList<String> preloadedNetworkList=new ArrayList<String>();
		ArrayList<String> networkVersionList=new ArrayList<String>();
		ArrayList<String> viewList=new ArrayList<String>();
		ArrayList<String> extensionList=new ArrayList<String>();
		ArrayList<String> layoutList=new ArrayList<String>();
		preloadedNetworkList=postgreDB.getNetworkMetadata_networkList();
		networkVersionList=postgreDB.getNetworkMetadata_versionList();
		viewList=postgreDB.getNetworkViewNode_foldChange_viewList();
		viewList.add(0,"Entire view");
		extensionList.add("None");
		extensionList.add("1-Hop");
		extensionList.add("2-Hop");
		extensionList.add("3-Hop");
		layoutList.add(LAYOUT_YIFANHU);
		layoutList.add(LAYOUT_FORCE_ATLAS_TWO);
		//layoutList.add(LAYOUT_NOOVERLAP);
		layoutList.add(LAYOUT_FRUCHTERMAN_REINGOLD);
		layoutList.add(LAYOUT_FORCE_ATLAS);
		layoutList.add(LAYOUT_LABEL_ADJUST);
		visualizeNetworkDialog visualizeNetwork=new visualizeNetworkDialog(preloadedNetworkList, networkVersionList, viewList, extensionList, layoutList);
		visualizeNetwork.setVisible(true);
		selectedView=visualizeNetwork.getSelectedView();
		selectedLayout=visualizeNetwork.getSelectedLayout();
		String extensionView=visualizeNetwork.getSelectedExtension();
		int numHops=0;

		System.out.println("[visualizePreloadedNetwork_actionPerformed] selectedView:"+selectedView);
		System.out.println("[visualizePreloadedNetwork_actionPerformed] extensionView:"+extensionView);

		if(visualizeNetwork.getSelectedNetwork()!=null)
		{
			boolean computeNetworkFeatures=visualizeNetwork.getComputeNetworkTopologicalFeatures();
			visualizeNetworkWorker=new VisualizeNetworkWorker();
			// reset everything
			//jungGraph.resetGraph();
			gephiGraph.resetGraph();
			vertexNodeContent.removeAll();
			vertexReactionContent.removeAll();
			selectedPathwayList=new ArrayList<String>();
			selectedFeatures=new ArrayList<String>();
			pathwayNodeList=new ArrayList<String>();
			ArrayList<String> list=new ArrayList<String>();

			radioButtonPanel.setVisible(false);

			if(selectedView.compareTo("Entire view")==0)
				list=getSpeciesListName(postgreDB.getNetworkNode_nodeIDList(), postgreDB.getNetworkNode_nodeNameList());
			else
			{
				if(extensionView.compareTo("1-Hop")==0)
					numHops=1;
				else if(extensionView.compareTo("2-Hop")==0)
					numHops=2;
				else if(extensionView.compareTo("3-Hop")==0)
					numHops=3;

				radioButtonPanel.setVisible(true);
				if(numHops==0)
					list=getSpeciesListName(postgreDB.getNetworkViewNode_foldChange_nodeIDList(selectedView), postgreDB.getNetworkViewNode_foldChange_nodeNameList(selectedView));
				else
				{
					System.out.println("here");
					ArrayList<String> tempNodeIDList=postgreDB.getNetworkViewNode_foldChange_nodeIDList(selectedView);
					System.out.println("before: "+tempNodeIDList.size()+" tempNodeIDList:"+tempNodeIDList.toString());
					for(int i=0; i<numHops; i++)
					{
						tempNodeIDList=postgreDB.getNetworkViewNode_foldChange_networkViewExtension(tempNodeIDList);
						System.out.println("numHops:"+i+" "+tempNodeIDList.size()+" tempNodeIDList:"+tempNodeIDList.toString());
					}
					list=getSpeciesListName(tempNodeIDList, postgreDB.getNodeNameList(tempNodeIDList));
				}

				/*if(postgreDB.getNetworkViewNode_foldChange_NonZerofoldChangeList(selectedView, postgreDB.getNetworkViewNode_foldChange_nodeIDList(selectedView)).size()>0)
				{
					diseaseNodeTableModel.addColumn("Node");
					diseaseNodeTableModel.addColumn("LogFC");
					diseaseNodeTableModel.addColumn("Mutation frequency");
					diseaseNodeList=postgreDB.getNetworkViewNode_getDiseaseNode(selectedView);
					int tableRow=diseaseNodeList.size();
					for(int i=1; i<=tableRow; i++)
					{
						Vector row = new Vector();
						row.add(diseaseNodeList.getNodeAtIndex(i-1));
						row.add(diseaseNodeList.getLogFCAtIndex(i-1));
						row.add(diseaseNodeList.getMutationFreqAtIndex(i-1));
						diseaseNodeTableModel.addRow(row);
					}

					radioButtonPanel.setVisible(true);
					int diseaseNodeTabIndex=resultTabbedPane.indexOfComponent(diseaseNodeListScrollPane);
					if(diseaseNodeTabIndex==(-1))
						resultTabbedPane.addTab("Disease Node List", diseaseNodeListScrollPane);
					resultTabbedPane.setSelectedIndex(0);
					settingPanel.setVisible(false);
				}*/
			}
			//proceed to visualize network
			int i=0;
			for(i=0; i<list.size(); i++)
				allNodeArrList.add(list.get(i));
			//************************
			System.out.println("TNBC node list:");
			for(i=0; i<list.size(); i++)
			{
				System.out.print(list.get(i)+",");
				if(i!=0 && i%10==0)
					System.out.println();
			}
			System.out.println();
			//************************
			addVertexLegend(false);
			int pathwayTabIndex=resultTabbedPane.indexOfComponent(pathwayListScrollPane);
			if(pathwayTabIndex!=(-1))
				resultTabbedPane.remove(pathwayTabIndex);
			pathwayTabIndex=resultTabbedPane.indexOfComponent(crosstalkPathwayListScrollPane);
			if(pathwayTabIndex!=(-1))
				resultTabbedPane.remove(pathwayTabIndex);
			int sccListTabIndex=resultTabbedPane.indexOfComponent(sccMapPanel);
			if(sccListTabIndex!=(-1))
				resultTabbedPane.remove(sccListTabIndex);
			if(computedNetworkFeatureExists())
				menuItemOptionFeatureResult.setEnabled(true);
			if(postgreDB.getHallmark_hallmarkCharacterized())
				menuItemOptionCharacterizationResult.setEnabled(true);
			String statusString="creating and visualizing graph...";
			updateStatus(statusString);		

			ArrayList<Object> param=new ArrayList<Object>();
			param.add(computeNetworkFeatures);
			param.add(list);
			param.add(selectedView);
			param.add(selectedLayout);
			visualizeNetworkWorker.Start(VISUALIZE_PRELOADED_NETWORK, param);

			//load species list
			speciesList=GUIwrapper.updateJListData(speciesList, allNodeArrList);
		}

		settingPanel.setVisible(true);
		System.out.println("loaded species list...");
	}

	private void displayNeighbourGraph() 
	{
		String selectedNode = speciesList.getSelectedValue().toString();
		ArrayList<Object> param=new ArrayList<Object>();
		param.add(selectedNode);
		param.add(gephiGraph);
		visualizeNetworkWorker.Start(NEIGHBOURVIEW, param);
	}

	private void displayWholeGraph() 
	{
		String selectedNode = speciesList.getSelectedValue().toString();
		ArrayList<Object> param=new ArrayList<Object>();
		param.add(selectedNode);
		param.add(gephiGraph);
		visualizeNetworkWorker.Start(GRAPHVIEW, param);
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

	private void createNeighbourGraph(String node) 
	{
		ArrayList<String> nodeIDList=new ArrayList<String>();
		ArrayList<String> nodeNameList=new ArrayList<String>();
		ArrayList<edge> edgeList=new ArrayList<edge>();
		String name;

		//jungGraph.resetGraph();
		gephiGraph.destroyGraph();

		String statusString="creating neighbour graph ...retrieving node ID data from database";
		updateStatus(statusString);	
		nodeIDList=postgreDB.getNetworkEdge_neighbourIDOfNode(node);
		statusString="creating neighbour graph ...retrieving node name data from database";
		updateStatus(statusString);	
		nodeNameList=postgreDB.getNetworkEdge_neighbourNameOfNode(node);
		statusString="creating neighbour graph ...retrieving edge data from database";
		updateStatus(statusString);	
		edgeList=postgreDB.getNetworkEdge_edgeListOfNode(node);

		//int totalCount=nodeIDList.size()+edgeList.size();
		String nodeSize=suggestGephiNodeSizeToUse(nodeIDList.size());

		//add all vertices
		for(int i=0; i<nodeIDList.size(); i++)
		{
			if(nodeNameList.get(i)!=null && nodeNameList.get(i).length()>0)
				name=nodeNameList.get(i);
			else
				name=nodeIDList.get(i);

			//jungGraph.addANode(name);
			gephiGraph.addANode(name, nodeSize);
		}

		//add all edges
		for(int i=0; i<edgeList.size(); i++)
		{
			String sourceNodeName;
			String targetNodeName;

			if(edgeList.get(i).getSourceName()!=null)
				sourceNodeName=edgeList.get(i).getSourceName();
			else
				sourceNodeName=edgeList.get(i).getSourceID();
			if(edgeList.get(i).getTargetName()!=null)
				targetNodeName=edgeList.get(i).getTargetName();
			else
				targetNodeName=edgeList.get(i).getTargetID();
			//check if node present and add if absent

			if(edgeList.get(i).getType().compareTo("Phy")==0)
				//jungGraph.addAnUndirectedEdge(sourceNodeName, targetNodeName);
				gephiGraph.addAnUndirectedEdge(sourceNodeName, targetNodeName);
			else
				//jungGraph.addADirectedEdge(sourceNodeName, targetNodeName);
				gephiGraph.addADirectedEdge(sourceNodeName, targetNodeName);
		}	
		gephiGraph.updateEdgeThickness(nodeSize);
	}

	private void getGraphDataFromDB(ArrayList<String> list) 
	{
		String statusString="creating graph ...retrieving node ID data from database";
		updateStatus(statusString);	

		if(list==null || list.size()==0)
			graphNodeIDList=postgreDB.getNetworkNode_nodeIDList();
		else
			graphNodeIDList=postgreDB.getNodeIDList(list);

		//System.out.println("[getGraphDataFromDB] graphNodeIDList:"+graphNodeIDList.toString());

		statusString="creating graph ...retrieving node name data from database";
		updateStatus(statusString);	
		if(list==null || list.size()==0)
			graphNodeNameList=postgreDB.getNetworkNode_nodeNameList();
		else
			graphNodeNameList=list;

		//System.out.println("[getGraphDataFromDB] graphNodeNameList:"+graphNodeNameList.toString());

		statusString="creating graph ...retrieving edge data from database";
		updateStatus(statusString);	
		if(list==null || list.size()==0)
			graphEdgeList=postgreDB.getNetworkEdge_edgeList(statusLabel);
		else
			graphEdgeList=postgreDB.getEdgeList(list);
	}

	private void createGraph() 
	{
		int i=0;
		String name;
		int currCount=0;
		int totalCount=graphNodeIDList.size()+graphEdgeList.size();
		String nodeSize=suggestGephiNodeSizeToUse(graphNodeIDList.size());
		String statusString;

		jungGraph.resetGraph();
		gephiGraph.destroyGraph();
		jGraphTGraph.resetGraph();
		pathway_graph=new graph_pathways();

		System.out.println("graphNodeIDList size:"+graphNodeIDList.size());
		System.out.println("graphEdgeList size:"+graphEdgeList.size());

		//add all vertices
		for(i=0; i<graphNodeIDList.size(); i++)
		{
			if(graphNodeNameList.get(i)!=null && graphNodeNameList.get(i).length()>0)
				name=graphNodeNameList.get(i);
			else
				name=graphNodeIDList.get(i);

			jungGraph.addANode(name);
			gephiGraph.addANode(name, nodeSize);
			jGraphTGraph.addANode(name);
			
			//for updating status
			double percentComplete=100.0*currCount/totalCount;
			statusString=String.format("creating graph [%,.2f%% complete]...adding node %s", percentComplete, name);
			updateStatus(statusString);	
			currCount=currCount+1;
		}

		//add all edges
		for(i=0; i<graphEdgeList.size(); i++)
		{
			String sourceNodeName;
			String targetNodeName;

			if(graphEdgeList.get(i).getSourceName()!=null)
				sourceNodeName=graphEdgeList.get(i).getSourceName();
			else
				sourceNodeName=graphEdgeList.get(i).getSourceID();
			if(graphEdgeList.get(i).getTargetName()!=null)
				targetNodeName=graphEdgeList.get(i).getTargetName();
			else
				targetNodeName=graphEdgeList.get(i).getTargetID();

			double percentComplete=100.0*currCount/totalCount;

			if(graphEdgeList.get(i).getType().compareTo("Phy")==0)
			{
				jungGraph.addAnUndirectedEdge(sourceNodeName, targetNodeName);
				gephiGraph.addAnUndirectedEdge(sourceNodeName, targetNodeName);
				jGraphTGraph.addAEdge(sourceNodeName, targetNodeName);
				jGraphTGraph.addAEdge(targetNodeName, sourceNodeName);
				pathway_graph.addTwoWayVertex(sourceNodeName, targetNodeName);

				statusString=String.format("creating graph [%,.2f%% complete]...adding edge %s---%s", percentComplete, sourceNodeName, targetNodeName);
			}
			else
			{
				jungGraph.addADirectedEdge(sourceNodeName, targetNodeName);
				gephiGraph.addADirectedEdge(sourceNodeName, targetNodeName);
				jGraphTGraph.addAEdge(sourceNodeName, targetNodeName);
				pathway_graph.addEdge(sourceNodeName, targetNodeName);
				statusString=String.format("creating graph [%,.2f%% complete]...adding edge %s--->%s", percentComplete, sourceNodeName, targetNodeName);
			}

			updateStatus(statusString);		
			currCount=currCount+1;
		}	
		gephiGraph.updateEdgeThickness(nodeSize);
	}
	
	private void createSubGraphFromNodeList(String node, ArrayList<String> neighbourList) 
	{
		int currCount=0;
		String nodeSize=suggestGephiNodeSizeToUse(neighbourList.size());
		String statusString;

		ArrayList<String> nodeList=new ArrayList<String>();
		nodeList.add(node);
		for(int i=0; i<neighbourList.size(); i++)
			nodeList.add(neighbourList.get(i));
		ArrayList<ArrayList<String>>inducedEdges=postgreDB.getNetworkEdge_inducedEdges(nodeList);
		int totalCount=nodeList.size()+inducedEdges.size();
		
		int i=0;
		String name;
		
		gephiGraph.destroyGraph();

		System.out.println("graphNodeIDList size:"+nodeList.size());
		System.out.println("graphEdgeList size:"+inducedEdges.size());

		//add all vertices
		for(i=0; i<nodeList.size(); i++)
		{
			name=nodeList.get(i);
			gephiGraph.addANode(name, nodeSize);
			
			//for updating status
			double percentComplete=100.0*currCount/totalCount;
			statusString=String.format("creating graph [%,.2f%% complete]...adding node %s", percentComplete, name);
			updateStatus(statusString);	
			currCount=currCount+1;
		}

		//add all edges
		for(i=0; i<inducedEdges.size(); i++)
		{
			ArrayList<String> inducedEdge=inducedEdges.get(i);
			String sourceNodeName=inducedEdge.get(0);
			String targetNodeName=inducedEdge.get(1);
			String type=inducedEdge.get(2);

			System.out.println(sourceNodeName+" "+targetNodeName+" "+type);
			if(type.compareTo("Phy")==0)
				gephiGraph.addAnUndirectedEdge(sourceNodeName, targetNodeName);
			else
				gephiGraph.addADirectedEdge(sourceNodeName, targetNodeName);
			
			//for updating status
			double percentComplete=100.0*currCount/totalCount;
			statusString=String.format("creating graph [%,.2f%% complete]...adding edge %s", percentComplete, inducedEdge.toString());
			updateStatus(statusString);	
			currCount=currCount+1;
		}
		gephiGraph.updateEdgeThickness(nodeSize);
		
	}

	private void createSubGraphFromPath(ArrayList<ArrayList<String>> pathList) 
	{
		int i=0;
		String name;
		int currCount=0;
		ArrayList<String> nodeList=new ArrayList<String>();
		ArrayList<ArrayList<String>> edgeList=new ArrayList<ArrayList<String>>();
		
		for(i=0; i<pathList.size(); i++)
		{
			for(int j=0; j<pathList.get(i).size(); j++)
			{
				String node=pathList.get(i).get(j);
				if(nodeList.contains(node)==false)
					nodeList.add(node);
			}
			for(int j=0; j<pathList.get(i).size()-1; j++)
			{
				String sourceNode=pathList.get(i).get(j);
				String targetNode=pathList.get(i).get(j+1);
				ArrayList<String> edge=new ArrayList<String>();
				edge.add(sourceNode);
				edge.add(targetNode);
				if(edgeList.contains(edge)==false)
					edgeList.add(edge);
			}
		}
		//ArrayList<ArrayList<String>> inducedEdges=postgreDB.getNetworkEdge_inducedEdges(nodeList);
		int totalCount=nodeList.size()+edgeList.size();
		String nodeSize=suggestGephiNodeSizeToUse(nodeList.size());
		String statusString;

		jungGraph.resetGraph();
		gephiGraph.destroyGraph();
		jGraphTGraph.resetGraph();

		//add all vertices
		for(i=0; i<nodeList.size(); i++)
		{
			name=nodeList.get(i);
			jungGraph.addANode(name);
			gephiGraph.addANode(name, nodeSize);
			jGraphTGraph.addANode(name);

			//for updating status
			double percentComplete=100.0*currCount/totalCount;
			statusString=String.format("creating graph [%,.2f%% complete]...adding node %s", percentComplete, name);
			updateStatus(statusString);	
			currCount=currCount+1;
		}

		//add all edges
		for(i=0; i<edgeList.size(); i++)
		{
			ArrayList<String> inducedEdge=edgeList.get(i);
			String sourceNodeName=inducedEdge.get(0);
			String targetNodeName=inducedEdge.get(1);
			
			double percentComplete=100.0*currCount/totalCount;
			jungGraph.addADirectedEdge(sourceNodeName, targetNodeName);
			gephiGraph.addADirectedEdge(sourceNodeName, targetNodeName);
			jGraphTGraph.addAEdge(sourceNodeName, targetNodeName);
			statusString=String.format("creating graph [%,.2f%% complete]...adding edge %s--->%s", percentComplete, sourceNodeName, targetNodeName);
			
			updateStatus(statusString);
			currCount=currCount+1;
		}
		gephiGraph.updateEdgeThickness(nodeSize);
	}

	private void openNetworkDataFile_actionPerformed() 
	{
		// Create a file dialog box to prompt for a new file to display
		FileDialog f = new FileDialog(this, "Open Network Annotation File", FileDialog.LOAD);
		f.setFile("*".concat(DAT_EXTENSION_STRING)); // Filename filter
		// Display the dialog and wait for the user's response
		f.setVisible(true);
		f.setLocation(new Point((SCREENWIDTH-910)/2, (SCREENHEIGHT-FRAMEHEIGHT)/2));

		directory = f.getDirectory(); // Remember new default directory
		if(directory!=null)
			networkDataFileName = directory.concat(f.getFile());

		f.dispose(); // Get rid of the dialog box

		if(directory!=null)
		{
			System.out.println("networkDataFileName:".concat(networkDataFileName));

			if(networkDataFileName!=null)
			{
				performAnalysisWorker=new PerformAnalysisWorker();
				ArrayList<Object> param=new ArrayList<Object>();
				param.add(networkDataFileName);
				performAnalysisWorker.Start(OPEN_NETWORK_DATA_FILE, param);
			}
		}
	}

	private void openPredictedCombiFileForCombiCuration_actionPerformed() 
	{
		// Create a file dialog box to prompt for a new file to display
		FileDialog f = new FileDialog(this, "Open Predicted Combination File", FileDialog.LOAD);
		f.setFile("*".concat(DAT_EXTENSION_STRING)); // Filename filter
		// Display the dialog and wait for the user's response
		f.setVisible(true);
		f.setLocation(new Point((SCREENWIDTH-910)/2, (SCREENHEIGHT-FRAMEHEIGHT)/2));

		directory = f.getDirectory(); // Remember new default directory
		if(directory!=null)
			predictedCombiFileName = directory.concat(f.getFile());

		f.dispose(); // Get rid of the dialog box

		if(directory!=null)
		{
			System.out.println("predictedCombiFileName:".concat(predictedCombiFileName));

			if(predictedCombiFileName!=null)
			{
				performAnalysisWorker=new PerformAnalysisWorker();
				ArrayList<Object> param=new ArrayList<Object>();
				param.add(predictedCombiFileName);
				performAnalysisWorker.Start(CHECK_COMBI_PREDICTION_AGAINST_COMBI_CURATION, param);
			}
		}
	}

	private void openPredictedCombiFileForMonoCuration_actionPerformed() 
	{
		// Create a file dialog box to prompt for a new file to display
		FileDialog f = new FileDialog(this, "Open Predicted Combination File", FileDialog.LOAD);
		f.setFile("*".concat(DAT_EXTENSION_STRING)); // Filename filter
		// Display the dialog and wait for the user's response
		f.setVisible(true);
		f.setLocation(new Point((SCREENWIDTH-910)/2, (SCREENHEIGHT-FRAMEHEIGHT)/2));

		directory = f.getDirectory(); // Remember new default directory
		if(directory!=null)
			predictedCombiFileName = directory.concat(f.getFile());

		f.dispose(); // Get rid of the dialog box

		if(directory!=null)
		{
			System.out.println("predictedCombiFileName:".concat(predictedCombiFileName));

			if(predictedCombiFileName!=null)
			{
				performAnalysisWorker=new PerformAnalysisWorker();
				ArrayList<Object> param=new ArrayList<Object>();
				param.add(predictedCombiFileName);
				performAnalysisWorker.Start(CHECK_COMBI_PREDICTION_AGAINST_MONO_CURATION, param);
			}
		}
	}

	private void openCuratedDrugFile_actionPerformed() 
	{
		// Create a file dialog box to prompt for a new file to display
		FileDialog f = new FileDialog(this, "Open Curated Drug File", FileDialog.LOAD);
		f.setFile("*".concat(DAT_EXTENSION_STRING)); // Filename filter
		// Display the dialog and wait for the user's response
		f.setVisible(true);
		f.setLocation(new Point((SCREENWIDTH-910)/2, (SCREENHEIGHT-FRAMEHEIGHT)/2));

		directory = f.getDirectory(); // Remember new default directory
		if(directory!=null)
			curatedDrugFileName = directory.concat(f.getFile());

		f.dispose(); // Get rid of the dialog box

		if(directory!=null)
		{
			System.out.println("curatedDrugFileName:".concat(curatedDrugFileName));

			if(curatedDrugFileName!=null)
			{
				performAnalysisWorker=new PerformAnalysisWorker();
				ArrayList<Object> param=new ArrayList<Object>();
				param.add(curatedDrugFileName);
				performAnalysisWorker.Start(OPEN_CURATED_DRUG_FILE, param);
			}
		}
	}

	private void openCuratedCombiFile_actionPerformed() 
	{
		// Create a file dialog box to prompt for a new file to display
		FileDialog f = new FileDialog(this, "Open Curated Combination File", FileDialog.LOAD);
		f.setFile("*".concat(DAT_EXTENSION_STRING)); // Filename filter
		// Display the dialog and wait for the user's response
		f.setVisible(true);
		f.setLocation(new Point((SCREENWIDTH-910)/2, (SCREENHEIGHT-FRAMEHEIGHT)/2));

		directory = f.getDirectory(); // Remember new default directory
		if(directory!=null)
			curatedCombiFileName = directory.concat(f.getFile());

		f.dispose(); // Get rid of the dialog box

		if(directory!=null)
		{
			System.out.println("curatedCombiFileName:".concat(curatedCombiFileName));

			if(curatedCombiFileName!=null)
			{
				performAnalysisWorker=new PerformAnalysisWorker();
				ArrayList<Object> param=new ArrayList<Object>();
				param.add(curatedCombiFileName);
				performAnalysisWorker.Start(OPEN_CURATED_COMBI_FILE, param);
			}
		}
	}

	private ArrayList<String> getArrayFromString(String delimiter, String s)
	{
		ArrayList<String> arr=new ArrayList<String>();
		String d=delimiter, element;
		int dIndex=s.indexOf(d);
		while(s!=null)
		{
			dIndex=s.indexOf(d);
			if(dIndex==-1)
			{
				element=s;
				s=null;
			}
			else
			{
				element=s.substring(0, dIndex);
				s=s.substring(dIndex+1);
				dIndex=s.indexOf(d);
			}
			if(element.length()>0)
				arr.add(element);
		}

		return arr;
	}

	private void processNetworkDataFile(String fileName) 
	{
		BufferedReader reader;
		String line;

		try {
			reader = new BufferedReader(new FileReader(fileName));
			updateStatus("reading ".concat(fileName));
			int totalLineCount=0;
			while(reader.readLine()!=null) totalLineCount++;
			reader.close();

			if(totalLineCount>0)
			{
				reader=new BufferedReader(new FileReader(fileName));
				for (int i=0; i<totalLineCount; i++)
				{
					line=reader.readLine();
					double percentComplete=100.0*i/totalLineCount;
					String statusString=String.format("%,.2f%% process network data task completed. Processing %s", percentComplete, line);
					updateStatus(statusString);

					int spaceIndex=line.indexOf(" ");
					String fileType=line.substring(0, spaceIndex);
					String fName=line.substring(spaceIndex).trim();

					if(fileType.compareTo(HALLMARK)==0)
					{
						System.out.println("HALLMARK lineCounter=".concat(String.valueOf(i)));
						processHallmarkFile((directory.concat(folder)).concat(fName));
					}
					if(fileType.compareTo(KINETICS)==0)
					{
						System.out.println("KINETICS lineCounter=".concat(String.valueOf(i)));
						processKineticsFile((directory.concat(folder)).concat(fName));
						GRAPHKINETICSREADY=true;
					}
					if(fileType.compareTo(NODE)==0)
					{
						System.out.println("NODE lineCounter=".concat(String.valueOf(i)));
						processNodeFile((directory.concat(folder)).concat(fName));
					}
				}
				reader.close();
			}	
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("fileName:"+fileName);
	}

	private void processNodeFile(String fileName) 
	{
		BufferedReader reader;
		updateStatus("reading ".concat(fileName));
		String nodeAnnotationFile=null;
		String nodeSourceFile=null;
		String pathwayFile=null;
		String line;
		int totalLineCount=0;

		try {
			reader = new BufferedReader(new FileReader(fileName));
			while(reader.readLine()!=null) totalLineCount++;
			reader.close();
			if(totalLineCount>0)
			{
				reader = new BufferedReader(new FileReader(fileName));
				for (int i=0; i<totalLineCount; i++)
				{
					line=reader.readLine();
					String statusString="Processing "+line;
					updateStatus(statusString);
					int spaceIndex=line.indexOf(SPACE);
					String fileType=line.substring(0, spaceIndex);
					String fName=line.substring(spaceIndex+1).trim();
					if(fileType.compareTo(NODE_SOURCE)==0)
						nodeSourceFile=(directory.concat(folder)).concat(fName);
					if(fileType.compareTo(NODE_ANNOTATION)==0)
						nodeAnnotationFile=(directory.concat(folder)).concat(fName);
					if(fileType.compareTo(PATHWAY)==0)
						pathwayFile=(directory.concat(folder)).concat(fName);
				}	
				reader.close();
				postgreDB.loadNodeAnnotation();
			}

			//read in the nodeSourceFile first
			if(nodeSourceFile!=null)
			{
				totalLineCount=0;
				reader = new BufferedReader(new FileReader(nodeSourceFile));
				while(reader.readLine()!=null) totalLineCount++;
				reader.close();
				if(totalLineCount>0)
				{
					reader = new BufferedReader(new FileReader(nodeSourceFile));
					for (int i=0; i<totalLineCount; i++)
					{
						line=reader.readLine();
						double percentComplete=100.0*i/totalLineCount;
						String statusString=String.format("%,.2f%% process node source task completed. Processing %s", percentComplete, line);
						updateStatus(statusString);
						if(i!=0)
						{
							int commaIndex=line.indexOf(COMMA);
							String sourceName=line.substring(0, commaIndex);
							String string=line.substring(commaIndex+1);
							commaIndex=string.indexOf(COMMA);
							String sourceUrl=string.substring(0, commaIndex);
							String sourceParam=string.substring(commaIndex+1);
							postgreDB.update_source(sourceName, sourceUrl, sourceParam);
						}
					}
					reader.close();
				}
			}

			//read in the nodeAnnotationFile next
			if(nodeAnnotationFile!=null)
			{
				totalLineCount=0;
				reader = new BufferedReader(new FileReader(nodeAnnotationFile));
				while(reader.readLine()!=null) totalLineCount++;
				reader.close();
				if(totalLineCount>0)
				{
					reader = new BufferedReader(new FileReader(nodeAnnotationFile));
					for (int i=0; i<totalLineCount; i++)
					{
						line=reader.readLine();
						double percentComplete=100.0*i/totalLineCount;
						String statusString=String.format("%,.2f%% process node annotation task completed. Processing %s", percentComplete, line);
						updateStatus(statusString);
						String lineString=line;
						if(i!=0)
						{
							int commaIndex=lineString.indexOf(COMMA);
							lineString.substring(0, commaIndex);//nodeName
							lineString=lineString.substring(commaIndex+1);

							commaIndex=lineString.indexOf(COMMA);
							String entrezID=lineString.substring(0, commaIndex);
							lineString=lineString.substring(commaIndex+1);

							commaIndex=lineString.indexOf(COMMA);
							String sourceName=lineString.substring(0, commaIndex);
							lineString=lineString.substring(commaIndex+1);

							commaIndex=lineString.indexOf(COMMA);
							String sourceID=lineString.substring(0, commaIndex);
							String sourceType=lineString.substring(commaIndex+1);

							postgreDB.update_nodeAnnotation(entrezID, sourceName, sourceID, sourceType);
						}
					}
					reader.close();
				}
			}

			//read in the pathwayFile next
			if(pathwayFile!=null)
			{
				totalLineCount=0;
				reader = new BufferedReader(new FileReader(pathwayFile));
				while(reader.readLine()!=null) totalLineCount++;
				reader.close();
				if(totalLineCount>0)
				{
					reader = new BufferedReader(new FileReader(pathwayFile));
					for (int i=0; i<totalLineCount; i++)
					{
						line=reader.readLine();
						double percentComplete=100.0*i/totalLineCount;
						String statusString=String.format("%,.2f%% process pathway task completed. Processing %s", percentComplete, line);
						updateStatus(statusString);
						String lineString=line;
						if(i!=0)
						{
							int commaIndex=lineString.indexOf(COMMA);
							String pathwayId=lineString.substring(0, commaIndex);
							String pathwayName=lineString.substring(commaIndex+1);
							postgreDB.update_pathway(pathwayId, processName(pathwayName), false);
							System.out.println(i+" id:"+pathwayId+" name:"+pathwayName);
						}
					}
					reader.close();
				}
			}
		}catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private String processNameForSpecialCharacter(String name, String specialChar, String newChar) 
	{
		String special=specialChar;
		int index=name.indexOf(special);
		String n=name;
		String newName="";

		while(index!=(-1))
		{
			newName=n.substring(0,index);
			newName=newName.concat(newChar);
			n=n.substring(index+1);
			index=n.indexOf(special);
		}
		newName=newName.concat(n);

		return newName;
	}

	private String processName(String name) 
	{
		//String newName=processNameForSpecialCharacter(name, ",", "\,");
		String newName=processNameForSpecialCharacter(name, "'", "''");

		return newName;
	}

	private void processHallmarkFile(String fileName) 
	{
		updateStatus("reading ".concat(fileName));
		String hallMarkFile=null;
		String hallMarkSourceFile=null;
		String hallMarkAnnotationFile=null;
		String hallmarkGOMappingFile=null;
		String line;
		int totalLineCount=0;
		BufferedReader reader;

		try{
			reader=new BufferedReader(new FileReader(fileName));
			while(reader.readLine()!=null) totalLineCount++;
			reader.close();
			if(totalLineCount>0)
			{
				reader=new BufferedReader(new FileReader(fileName));
				for (int i=0; i<totalLineCount; i++)
				{
					line=reader.readLine();
					String statusString="Processing "+line;
					updateStatus(statusString);
					int spaceIndex=line.indexOf(SPACE);
					String fileType=line.substring(0, spaceIndex);
					String fName=line.substring(spaceIndex+1).trim();
					if(fileType.compareTo(HALLMARK_HALLMARK)==0)
						hallMarkFile=(directory.concat(folder)).concat(fName);
					if(fileType.compareTo(HALLMARK_SOURCE)==0)
						hallMarkSourceFile=(directory.concat(folder)).concat(fName);
					if(fileType.compareTo(HALLMARK_ANNOTATION)==0)
						hallMarkAnnotationFile=(directory.concat(folder)).concat(fName);
					if(fileType.compareTo(HALLMARK_GO_MAPPING)==0)
						hallmarkGOMappingFile=(directory.concat(folder)).concat(fName);
				}
				reader.close();
				postgreDB.loadHallmark();
			}

			//read in the hallMarkSourceFile first
			if(hallMarkSourceFile!=null)
			{
				totalLineCount=0;
				//System.out.println("hallMarkSourceFile:"+hallMarkSourceFile);
				reader=new BufferedReader(new FileReader(hallMarkSourceFile));
				while(reader.readLine()!=null) totalLineCount++;
				reader.close();
				//System.out.println("totalLineCount:"+totalLineCount);
				if(totalLineCount>0)
				{
					reader=new BufferedReader(new FileReader(hallMarkSourceFile));
					for (int i=0; i<totalLineCount; i++)
					{
						line=reader.readLine();
						double percentComplete=100.0*i/totalLineCount;
						String statusString=String.format("%,.2f%% process hallmark source task completed. Processing %s", percentComplete, line);
						updateStatus(statusString);
						if(i!=0)
						{
							int commaIndex=line.indexOf(COMMA);
							String sourceName=line.substring(0, commaIndex);
							String string=line.substring(commaIndex+1);
							commaIndex=string.indexOf(COMMA);
							String sourceUrl=string.substring(0, commaIndex);
							String sourceParam=string.substring(commaIndex+1);
							postgreDB.update_source(sourceName, sourceUrl, sourceParam);
						}
					}
					reader.close();
				}
			}

			//read in the hallMarkFile next
			if(hallMarkFile!=null)
			{
				totalLineCount=0;
				reader=new BufferedReader(new FileReader(hallMarkFile));
				while(reader.readLine()!=null) totalLineCount++;
				reader.close();
				if(totalLineCount>0)
				{
					reader=new BufferedReader(new FileReader(hallMarkFile));
					for (int i=0; i<totalLineCount; i++)
					{
						line=reader.readLine();
						double percentComplete=100.0*i/totalLineCount;
						String statusString=String.format("%,.2f%% process hallmark task completed. Processing %s", percentComplete, line);
						updateStatus(statusString);
						if(i!=0)
						{
							int commaIndex=line.indexOf(COMMA);
							String remainingLine=line.substring(commaIndex+1);
							ArrayList<String> hallmark = new ArrayList<String>();
							commaIndex=remainingLine.indexOf(COMMA);
							String nodeID=remainingLine.substring(0, commaIndex);
							remainingLine=remainingLine.substring(commaIndex+1);
							int count=0;
							while(remainingLine.length()>0)
							{
								commaIndex=remainingLine.indexOf(COMMA);
								if(commaIndex>0)
								{
									hallmark.add(remainingLine.substring(0, commaIndex));
									remainingLine=remainingLine.substring(commaIndex+1);
									count=count+1;
								}
								else
								{
									hallmark.add(remainingLine);
									remainingLine="";
									count=count+1;
								}
							}
							//System.out.println("count:"+count);
							postgreDB.insert_hallmark(nodeID, hallmark);
						}
					}
					reader.close();
				}					
			}

			//read in the hallMarkAnnotation next
			if(hallMarkAnnotationFile!=null)
			{
				totalLineCount=0;
				reader=new BufferedReader(new FileReader(hallMarkAnnotationFile));
				while(reader.readLine()!=null) totalLineCount++;
				reader.close();
				if(totalLineCount>0)
				{
					reader=new BufferedReader(new FileReader(hallMarkAnnotationFile));
					for (int i=0; i<totalLineCount; i++)
					{
						line=reader.readLine();
						double percentComplete=100.0*i/totalLineCount;
						String statusString=String.format("%,.2f%% process hallmark annotation task completed. Processing %s", percentComplete, line);
						updateStatus(statusString);
						if(i!=0)
						{
							int commaIndex=line.indexOf(COMMA);
							String remainingLine=line.substring(commaIndex+1);
							commaIndex=remainingLine.indexOf(COMMA);
							String nodeID=remainingLine.substring(0, commaIndex);
							remainingLine=remainingLine.substring(commaIndex+1);
							commaIndex=remainingLine.indexOf(COMMA);
							String source=remainingLine.substring(0, commaIndex);
							String sourceID=remainingLine.substring(commaIndex+1);
							postgreDB.update_hallmark_annotation(nodeID, source, sourceID);
						}
					}
					reader.close();
				}
			}

			//read in the hallmarkGOMappingFile last
			if(hallmarkGOMappingFile!=null)
			{
				totalLineCount=0;
				//System.out.println("hallmarkGOMappingFile:"+hallmarkGOMappingFile);
				reader=new BufferedReader(new FileReader(hallmarkGOMappingFile));
				while(reader.readLine()!=null) totalLineCount++;
				reader.close();
				//System.out.println("totalLineCount:"+totalLineCount);
				if(totalLineCount>0)
				{
					reader=new BufferedReader(new FileReader(hallmarkGOMappingFile));
					for (int i=0; i<totalLineCount; i++)
					{
						line=reader.readLine();
						double percentComplete=100.0*i/totalLineCount;
						String statusString=String.format("%,.2f%% process hallmark source task completed. Processing %s", percentComplete, line);
						updateStatus(statusString);
						if(i!=0)
						{
							int commaIndex=line.indexOf(COMMA);
							String hallmark=line.substring(0, commaIndex);
							String remainingLine=line.substring(commaIndex+1);
							commaIndex=remainingLine.indexOf(COMMA);
							String GO_id=remainingLine.substring(0, commaIndex);
							remainingLine=remainingLine.substring(commaIndex+1);
							commaIndex=remainingLine.indexOf(COMMA);
							String GO_name=remainingLine.substring(0, commaIndex);
							remainingLine=remainingLine.substring(commaIndex+1);
							commaIndex=remainingLine.indexOf(COMMA);
							String GO_type=remainingLine.substring(commaIndex+1);
							postgreDB.insert_hallmark_go_mapping(hallmark, GO_id, GO_name, GO_type);
						}
					}
					reader.close();
				}
			}
		}catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void processKineticsFile(String fileName) 
	{
		BufferedReader reader;
		int totalLineCount;

		try {
			reader = new BufferedReader(new FileReader(fileName));
			updateStatus("reading ".concat(fileName));
			String kineticsFile=null;
			String kineticsSourceFile=null;
			String kineticsRateAnnotationFile=null;
			String kineticsRateFile=null;
			String line;

			while((line=reader.readLine())!=null)
			{
				String statusString="Processing "+line;
				updateStatus(statusString);
				int spaceIndex=line.indexOf(SPACE);
				String fileType=line.substring(0, spaceIndex);
				String fName=line.substring(spaceIndex+1).trim();
				if(fileType.compareTo(KINETICS_KINETICS)==0)
					kineticsFile=(directory.concat(folder)).concat(fName);
				if(fileType.compareTo(KINETICS_SOURCE)==0)
					kineticsSourceFile=(directory.concat(folder)).concat(fName);
				if(fileType.compareTo(KINETICS_RATE_ANNOTATION)==0)
					kineticsRateAnnotationFile=(directory.concat(folder)).concat(fName);
				if(fileType.compareTo(KINETICS_RATE)==0)
					kineticsRateFile=(directory.concat(folder)).concat(fName);
			}	
			reader.close();

			postgreDB.loadKinetics();

			//read in the kineticsSourceFile first
			if(kineticsSourceFile!=null)
			{
				totalLineCount=0;
				reader=new BufferedReader(new FileReader(kineticsSourceFile));
				while(reader.readLine()!=null) totalLineCount++;
				reader.close();
				reader=new BufferedReader(new FileReader(kineticsSourceFile));
				if(totalLineCount>0)
				{
					for (int i=0; i<totalLineCount; i++)
					{
						line=reader.readLine();
						double percentComplete=100.0*i/totalLineCount;
						String statusString=String.format("%,.2f%% process kinetics source task completed. Processing %s", percentComplete, line);
						updateStatus(statusString);
						if(i!=0)
						{
							int commaIndex=line.indexOf(COMMA);
							String sourceName=line.substring(0, commaIndex);
							String string=line.substring(commaIndex+1);
							commaIndex=string.indexOf(COMMA);
							String sourceUrl=string.substring(0, commaIndex);
							String sourceParam=string.substring(commaIndex+1);
							postgreDB.update_source(sourceName, sourceUrl, sourceParam);
						}
					}
				}
				reader.close();
			}

			//read in the kineticsRateAnnotationFile next
			if(kineticsRateAnnotationFile!=null)
			{
				totalLineCount=0;
				reader=new BufferedReader(new FileReader(kineticsRateAnnotationFile));
				while(reader.readLine()!=null) totalLineCount++;
				reader.close();
				reader=new BufferedReader(new FileReader(kineticsRateAnnotationFile));
				if(totalLineCount>0)
				{
					for (int i=0; i<totalLineCount; i++)
					{
						line=reader.readLine();
						double percentComplete=100.0*i/totalLineCount;
						String statusString=String.format("%,.2f%% process kinetics rate annotation task completed. Processing %s", percentComplete, line);
						updateStatus(statusString);
						String lineString=line;
						if(i!=0)
						{
							int commaIndex=lineString.indexOf(COMMA);
							String rateID=lineString.substring(0, commaIndex);
							lineString=lineString.substring(commaIndex+1);

							commaIndex=lineString.indexOf(COMMA);
							String organism=lineString.substring(0, commaIndex);
							lineString=lineString.substring(commaIndex+1);

							commaIndex=lineString.indexOf(COMMA);
							String cellLine=lineString.substring(0, commaIndex);
							lineString=lineString.substring(commaIndex+1);

							commaIndex=lineString.indexOf(COMMA);
							String source=lineString.substring(0, commaIndex);
							lineString=lineString.substring(commaIndex+1);

							commaIndex=lineString.indexOf(COMMA);
							String rateParam=lineString.substring(0, commaIndex);
							String rateValue=lineString.substring(commaIndex+1);

							postgreDB.update_kineticsRateAnnotation(rateID, organism, cellLine, source, rateParam, rateValue);
						}
					}
				}
				reader.close();
			}

			//read in the kineticsRateFile first
			if(kineticsRateFile!=null)
			{
				totalLineCount=0;
				reader=new BufferedReader(new FileReader(kineticsRateFile));
				while(reader.readLine()!=null) totalLineCount++;
				reader.close();
				reader=new BufferedReader(new FileReader(kineticsRateFile));
				if(totalLineCount>0)
				{
					for (int i=0; i<totalLineCount; i++)
					{
						line=reader.readLine();
						double percentComplete=100.0*i/totalLineCount;
						String statusString=String.format("%,.2f%% process kinetics rate task completed. Processing %s", percentComplete, line);
						updateStatus(statusString);
						if(i!=0)
						{
							int commaIndex=line.indexOf(COMMA);
							String kineticID=line.substring(0, commaIndex);
							String rateID=line.substring(commaIndex+1);
							postgreDB.update_kineticsRate(kineticID, rateID);
						}
					}
				}
				reader.close();
			}

			//read in the kineticsFile last
			if(kineticsFile!=null)
			{
				totalLineCount=0;
				reader=new BufferedReader(new FileReader(kineticsFile));
				while(reader.readLine()!=null) totalLineCount++;
				reader.close();
				reader=new BufferedReader(new FileReader(kineticsFile));
				if(totalLineCount>0)
				{
					for (int i=0; i<totalLineCount; i++)
					{
						line=reader.readLine();
						double percentComplete=100.0*i/totalLineCount;
						String statusString=String.format("%,.2f%% process hallmark annotation task completed. Processing %s", percentComplete, line);
						updateStatus(statusString);
						String lineString=line;
						if(i!=0)
						{
							int commaIndex=lineString.indexOf(COMMA);
							String sourceId=lineString.substring(0, commaIndex);
							lineString=lineString.substring(commaIndex+1);

							commaIndex=lineString.indexOf(COMMA);
							String sourceName=lineString.substring(0, commaIndex);
							lineString=lineString.substring(commaIndex+1);

							commaIndex=lineString.indexOf(COMMA);
							String targetId=lineString.substring(0, commaIndex);
							lineString=lineString.substring(commaIndex+1);

							commaIndex=lineString.indexOf(COMMA);
							String targetName=lineString.substring(0, commaIndex);
							lineString=lineString.substring(commaIndex+1);

							commaIndex=lineString.indexOf(COMMA);
							String edgeType=lineString.substring(0, commaIndex);
							String kineticsId=lineString.substring(commaIndex+1);

							postgreDB.update_kinetics(sourceId, sourceName, targetId, targetName, edgeType, kineticsId);
						}
					}
				}
				reader.close();
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void openTopologyFile_actionPerformed() 
	{
		// Create a file dialog box to prompt for a new file to display
		FileDialog f = new FileDialog(this, "Open Human Signaling Network File", FileDialog.LOAD);
		f.setFile("*".concat(CSV_EXTENSION_STRING)); // Filename filter
		// Display the dialog and wait for the user's response
		f.setVisible(true);
		f.setLocation(new Point((SCREENWIDTH-910)/2, (SCREENHEIGHT-FRAMEHEIGHT)/2));

		directory = f.getDirectory(); // Remember new default directory
		if(directory!=null)
		{
			topologyFileName = directory.concat(f.getFile());
			networkViewFileName = directory.concat("view_fileList.txt");
			//System.out.println("networkViewFileName:"+networkViewFileName);
		}

		f.dispose(); // Get rid of the dialog box

		if(directory!=null)
		{
			System.out.println("topologyFileName:".concat(topologyFileName));
			// reset everything
			speciesList.removeAll();
			targetList.removeAll();
			//selectedHallmarkNodeList.removeAll();
			interactiveGraphPanel.removeAll();
			vertexNodeContent.removeAll();
			vertexReactionContent.removeAll();
			postgreDB.loadThisNetwork();

			if(topologyFileName!=null)
			{
				performAnalysisWorker=new PerformAnalysisWorker();
				ArrayList<Object> param=new ArrayList<Object>();
				param.add(topologyFileName);
				performAnalysisWorker.Start(OPEN_TOPOLOGY_FILE, param);

				//processTopologyFile(topologyFileName);
				//statusLabel.setText("Finish processing ".concat(topologyFileName));
			}
		}
	}

	private void updateStatus(String txt) 
	{
		statusLabel.setText(txt);
	}

	private void processIndividualNetworkView_nodeListAndFoldChange(String viewType, String filename)
	{
		int totalLineCount=0;
		BufferedReader reader;
		String line;
		String delimiter=",";
		int delimiterIndex=0;
		String entrez="", foldChange="";

		try{
			reader = new BufferedReader(new FileReader(filename));

			while(reader.readLine()!=null) totalLineCount++;
			reader.close();
			if(totalLineCount>0)
			{
				reader = new BufferedReader(new FileReader(filename));

				for (int i=0; i<totalLineCount; i++)
				{
					line=reader.readLine();
					double percentComplete=100.0*i/totalLineCount;
					String statusString=String.format("%,.2f%% read network view nodelist task completed. Processing %s", percentComplete, line);

					updateStatus(statusString);
					if(i!=0)
					{
						//System.out.println("line1:"+line);
						delimiterIndex=line.indexOf(delimiter);
						if(delimiterIndex!=-1)
						{
							line=line.substring(delimiterIndex+1);
							//System.out.println("line2:"+line);
							delimiterIndex=line.indexOf(delimiter);
							if(delimiterIndex!=-1)
							{
								entrez=line.substring(0,delimiterIndex);
								foldChange=line.substring(delimiterIndex+1);
							}
						}

						System.out.println("entrez:"+entrez+" foldChange:"+foldChange);
						postgreDB.addNewViewNode_foldChange(viewType, entrez, foldChange);
					}
				}
				reader.close();
			}
		}catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void processIndividualNetworkView_nodeListAndMutationFrequency(String viewType, String filename)
	{
		int totalLineCount=0;
		BufferedReader reader;
		String line;
		String delimiter=",";
		int delimiterIndex=0;
		String entrez="", percentage="";

		try{
			reader = new BufferedReader(new FileReader(filename));

			while(reader.readLine()!=null) totalLineCount++;
			reader.close();
			if(totalLineCount>0)
			{
				reader = new BufferedReader(new FileReader(filename));

				for (int i=0; i<totalLineCount; i++)
				{
					line=reader.readLine();
					double percentComplete=100.0*i/totalLineCount;
					String statusString=String.format("%,.2f%% read network view mutation task completed. Processing %s", percentComplete, line);

					updateStatus(statusString);
					if(i!=0)
					{
						delimiterIndex=line.indexOf(delimiter);
						if(delimiterIndex!=-1)
						{
							line=line.substring(delimiterIndex+1);//remove no. column
							delimiterIndex=line.indexOf(delimiter);
							if(delimiterIndex!=-1)
							{
								line=line.substring(delimiterIndex+1);//remove gene name column
								delimiterIndex=line.indexOf(delimiter);
								if(delimiterIndex!=-1)
								{
									entrez=line.substring(0,delimiterIndex); //grep entrez column
									line=line.substring(delimiterIndex+1);//remove gene name column
									delimiterIndex=line.indexOf(delimiter);
									if(delimiterIndex!=-1)
									{
										line=line.substring(delimiterIndex+1);//remove mutated sample column
										delimiterIndex=line.indexOf(delimiter);

										if(delimiterIndex!=-1)
										{
											line=line.substring(delimiterIndex+1);//remove sample tested column
											delimiterIndex=line.indexOf(delimiter);
											percentage=line.substring(delimiterIndex+1); //grep percentage column
										}
									}
								}
							}
						}

						System.out.println("entrez:"+entrez+" percentage:"+percentage);
						postgreDB.addNewViewNode_mutationFrequency(viewType, entrez, percentage);
					}
				}
				reader.close();
			}
		}catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void processIndividualNetworkView_nodeListAndEssentialGene(String viewType, String filename)
	{
		int totalLineCount=0;
		BufferedReader reader;
		String line;
		String delimiter=",";
		int delimiterIndex=0;
		String entrez="";

		try{
			reader = new BufferedReader(new FileReader(filename));

			while(reader.readLine()!=null) totalLineCount++;
			reader.close();
			if(totalLineCount>0)
			{
				reader = new BufferedReader(new FileReader(filename));

				for (int i=0; i<totalLineCount; i++)
				{
					line=reader.readLine();
					double percentComplete=100.0*i/totalLineCount;
					String statusString=String.format("%,.2f%% read network view essential gene task completed. Processing %s", percentComplete, line);

					updateStatus(statusString);
					if(i!=0)
					{
						delimiterIndex=line.indexOf(delimiter);
						if(delimiterIndex!=-1)
							entrez=line.substring(delimiterIndex+1); //grep entrez column

						System.out.println("entrez:"+entrez);
						postgreDB.addNewViewNode_essentialGenes(viewType, entrez);
					}
				}
				reader.close();
			}
		}catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void processIndividualNetworkView(String viewType, String filename)
	{
		int totalLineCount=0;
		BufferedReader reader;
		String line;

		try{
			System.out.println("processIndividualNetworkView:"+filename);
			reader = new BufferedReader(new FileReader(filename));
			while(reader.readLine()!=null) totalLineCount++;
			reader.close();
			if(totalLineCount>0)
			{
				reader = new BufferedReader(new FileReader(filename));

				for (int i=0; i<totalLineCount; i++)
				{
					line=reader.readLine();
					double percentComplete=100.0*i/totalLineCount;
					String statusString=String.format("%,.2f%% read network view individual files completed. Processing %s", percentComplete, line);
					updateStatus(statusString);

					if(i==0)//essential gene file
					{
						if(line.compareTo("null")!=0)
							processIndividualNetworkView_nodeListAndEssentialGene(viewType, (directory.concat(folder)).concat(line));
					}
					if(i==1)//fold change file
					{
						if(line.compareTo("null")!=0)
							processIndividualNetworkView_nodeListAndFoldChange(viewType, (directory.concat(folder)).concat(line));
					}
					if(i==2)//mutation frequency file
					{
						if(line.compareTo("null")!=0)
							processIndividualNetworkView_nodeListAndMutationFrequency(viewType, (directory.concat(folder)).concat(line));
					}
				}
				reader.close();
			}
		}catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void processViewFile()
	{
		int totalLineCount=0;
		String line;
		updateStatus("reading network view files");
		BufferedReader reader;

		try{
			reader=new BufferedReader(new FileReader(networkViewFileName));
			while(reader.readLine()!=null) totalLineCount++;
			reader.close();
			if(totalLineCount>0)
			{
				reader=new BufferedReader(new FileReader(networkViewFileName));
				for (int i=0; i<totalLineCount; i++)
				{
					line=reader.readLine();
					double percentComplete=100.0*i/totalLineCount;
					String statusString=String.format("%,.2f%% task completed. Processing line: %s", percentComplete, line);
					updateStatus(statusString);

					int spaceIndex=line.indexOf(" ");
					String fileType=line.substring(0, spaceIndex);
					String fName=line.substring(spaceIndex).trim();

					System.out.println("fName:"+fName);
					processIndividualNetworkView(fileType, (directory).concat(fName));
				}
				reader.close();
			}
		}catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if(preloadedNetworkExists())
			menuItemActionVisualize.setEnabled(true);
		else
			menuItemActionVisualize.setEnabled(false);
	}

	private boolean processTopologyFile(String fileName) 
	{
		String truncatedline;
		String delimiter=",";
		int delimiterIndex=0;
		//boolean CONTINUE=true;
		int totalLineCount=0;
		String line;
		updateStatus("reading ".concat(fileName));
		BufferedReader reader;

		try{
			reader=new BufferedReader(new FileReader(fileName));
			while(reader.readLine()!=null) totalLineCount++;
			reader.close();
			if(totalLineCount>0)
			{
				reader=new BufferedReader(new FileReader(fileName));
				for (int i=0; i<totalLineCount; i++)
				{
					line=reader.readLine();
					double percentComplete=100.0*i/totalLineCount;
					String statusString=String.format("%,.2f%% task completed. Processing line: %s", percentComplete, line);
					updateStatus(statusString);
					if(i==0)
					{
						delimiterIndex=line.indexOf(delimiter);
						String networkName=line.substring(0, delimiterIndex);
						truncatedline=line.substring(delimiterIndex+1);
						delimiterIndex=truncatedline.indexOf(delimiter);
						String version=truncatedline.substring(0, delimiterIndex);
						postgreDB.addNewMetadata(networkName, version);
					}
					else
					{
						delimiterIndex=line.indexOf(delimiter);
						String sourceNodeID=line.substring(0, delimiterIndex);
						truncatedline=line.substring(delimiterIndex+1);
						delimiterIndex=truncatedline.indexOf(delimiter);
						String sourceNodeName=truncatedline.substring(0, delimiterIndex);
						truncatedline=truncatedline.substring(delimiterIndex+1);
						delimiterIndex=truncatedline.indexOf(delimiter);
						String targetNodeID=truncatedline.substring(0, delimiterIndex);
						truncatedline=truncatedline.substring(delimiterIndex+1);
						delimiterIndex=truncatedline.indexOf(delimiter);
						String targetNodeName=truncatedline.substring(0, delimiterIndex);
						truncatedline=truncatedline.substring(delimiterIndex+1);
						delimiterIndex=truncatedline.indexOf(delimiter);
						String edgeType=truncatedline;
						postgreDB.addNewEdge(sourceNodeID, sourceNodeName, targetNodeID, targetNodeName, edgeType);
					}
				}
				reader.close();
			}
		}catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		/*if(preloadedNetworkExists())
			menuItemActionVisualize.setEnabled(true);
		else
			menuItemActionVisualize.setEnabled(false);
		 */
		return true;
	}

	private void visualizeSelectedNodeInGraph(String node) 
	{
		String statusString;

		if(GEPHIGRAPHREADY==true)
		{
			editAnnotationButton.setEnabled(true);
			editHallmarkButton.setEnabled(true);
			System.out.println("selected node "+node);
			if(CURRVIEW.compareTo(NEIGHBOURVIEW)==0)
			{
				statusString="Creating neighbour graph of "+selectedNode+". Please wait ...";
				updateStatus(statusString);
				displayNeighbourGraph();
			}
			else
			{
				//if CURRVIEW is GRAPHVIEW and FORCERECOLOR is true => has switched from neighbourview to graphview
				//need to redraw whole graph
				if(FORCERECOLOR==true)
				{
					statusString="Creating entire graph. Please wait ...";
					updateStatus(statusString);
					displayWholeGraph();
				}
			}
			statusString="Coloring and resizing selected node "+selectedNode+" ...";
			updateStatus(statusString);
			gephiGraph.colorAndResizeSelectedNode(selectedNode, FORCERECOLOR); 
			//uncolor all previous hallmarks
			gephiGraph.resetHallmarkNode();
			//color selected hallmarks
			statusString="Coloring selected hallmark (if any) ...";
			updateStatus(statusString);
			allHallmark hallmarkNodeList=postgreDB.getHallmark_hallmark();
			gephiGraph.colorAndResizeHallmarkNode(hallmarkNodeList, selectedHallmarkList, true);
			gephiGraph.colorAndResizeSelectedNode(selectedNode, FORCERECOLOR);
			statusString="Displaying connections of "+selectedNode+" ...";
			updateStatus(statusString);
			getAndDisplayNodeConnection(selectedNode);
			statusString="Displaying information of "+selectedNode+" ...";
			updateStatus(statusString);
			getAndDisplaySpeciesInformation(selectedNode);
			interactiveGraphPanel.revalidate();
			interactiveGraphPanel.repaint();
			System.out.println("repainted interactiveGraphPanel");
			statusLabel.setText("Selected ".concat(selectedNode));
			FORCERECOLOR=false;
		}
	}

	private void speciesList_valueChanged(ListSelectionEvent e) 
	{
		if (!e.getValueIsAdjusting()) 
		{
			if(speciesList.getSelectedValue()!=null)
			{
				selectedNode = speciesList.getSelectedValue().toString();
				System.out.println(selectedNode);
				if(postgreDB.checkNodeNameExists(selectedNode)==true)
				{
					visualizeSelectedNodeInGraph(selectedNode);
					sendVirtualClickToRefreshGraphDisplay();
				}
			}
		}
	}

	private String processSingleParamURL(String url, String id) 
	{
		String formattedURL="";
		String placementString="<v>";
		int placementIndex=url.indexOf(placementString);

		if(placementIndex==(-1))
			System.out.println("ERROR in INGOT_S.scala [processURL]: placement string <v> not found in URL");
		else
		{
			String frontString=url.substring(0, placementIndex);
			String backString=url.substring(placementIndex+placementString.length());
			formattedURL=(frontString.concat(id)).concat(backString);
		}

		System.out.println("formattedURL: ".concat(formattedURL));

		return formattedURL;
	}

	private JPanel createNodeAnnotationLabel_others(JPanel annotationPanel, String node, String sourceMode, String sourceType) 
	{
		final ArrayList<annotation> nodeAnnotationList=postgreDB.getNode_annotationOfNode(node, sourceMode, sourceType);
		for(int i=0; i<nodeAnnotationList.size(); i++)
		{
			JLabel nodeAnnotationLabel = new JLabel();
			nodeAnnotationLabel.setText((nodeAnnotationList.get(i).getSource()).concat(","));
			nodeAnnotationLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			final int j=i;
			nodeAnnotationLabel.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e)
				{
					if (e.getClickCount() > 0) {
						if (Desktop.isDesktopSupported()) {
							Desktop desktop = Desktop.getDesktop();
							try {
								URI uri = new URI(processSingleParamURL(nodeAnnotationList.get(j).getURL(), nodeAnnotationList.get(j).getID()));
								desktop.browse(uri);
							} catch (IOException ex) {
								// TODO Auto-generated catch block
								ex.printStackTrace();
							} catch (URISyntaxException ex) {
								// TODO Auto-generated catch block
								ex.printStackTrace();
							}
						}
					}
				}
			});
			annotationPanel.add(nodeAnnotationLabel);
		}
		return annotationPanel;
	}

	private JPanel createNodeAnnotationLabel_multiple(String node, String sourceMode, String sourceType) 
	{
		final ArrayList<annotation> nodeAnnotationList=postgreDB.getNode_annotationOfNode(node, sourceMode, sourceType);
		JPanel annotationPanel=new JPanel();
		annotationPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		if(nodeAnnotationList.size()>0)
		{
			if(sourceMode.compareTo(strConstants.getDBKEGGPathway())==0)
				annotationPanel.add(new JLabel("[KEGG pathway] "));
			else
				annotationPanel.add(new JLabel("[GO ".concat(sourceType).concat("] ")));
			for(int i=0; i<nodeAnnotationList.size(); i++)
			{
				JLabel nodeAnnotationLabel = new JLabel();
				nodeAnnotationLabel.setText((nodeAnnotationList.get(i).getID()).concat(","));
				nodeAnnotationLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
				final int j=i;
				nodeAnnotationLabel.addMouseListener(new MouseAdapter() {
					public void mouseClicked(MouseEvent e)
					{
						if (e.getClickCount() > 0) {
							if (Desktop.isDesktopSupported()) {
								Desktop desktop = Desktop.getDesktop();
								try {
									URI uri = new URI(processSingleParamURL(nodeAnnotationList.get(j).getURL(), nodeAnnotationList.get(j).getID()));
									desktop.browse(uri);
								} catch (IOException ex) {
									// TODO Auto-generated catch block
									ex.printStackTrace();
								} catch (URISyntaxException ex) {
									// TODO Auto-generated catch block
									ex.printStackTrace();
								}
							}
						}
					}
				});
				annotationPanel.add(nodeAnnotationLabel);
			}
		}
		return annotationPanel;
	}

	private void displayAnnotationInfo(String node) 
	{
		JPanel annotationTitlePanel = new JPanel();
		annotationTitlePanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		JLabel annotationTitleLabel=new JLabel("<<ANNOTATION>>");
		annotationTitlePanel.add(annotationTitleLabel);
		vertexNodeContent.add(annotationTitlePanel);

		JPanel annotationPanel=new JPanel();
		annotationPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

		//add other annotations (non-KEGG, non-GO annotations)
		annotationPanel=createNodeAnnotationLabel_others(annotationPanel, node, strConstants.getDBOthers(), null);
		vertexNodeContent.add(annotationPanel);
		//add KEGG pathway annotations
		JPanel KEGGPathwayAnnotationPanel=createNodeAnnotationLabel_multiple(node, strConstants.getDBKEGGPathway(), null);
		vertexNodeContent.add(KEGGPathwayAnnotationPanel);
		//add GO annotations (molecular function)
		JPanel GOAnnotationPanel=createNodeAnnotationLabel_multiple(node, strConstants.getDBGO(), strConstants.getDBMolecularFunction());
		vertexNodeContent.add(GOAnnotationPanel);
		//add GO annotations (localization)
		JPanel GOLocalizationPanel=createNodeAnnotationLabel_multiple(node, strConstants.getDBGO(), strConstants.getDBLocalization());
		vertexNodeContent.add(GOLocalizationPanel);
		//add GO annotations (biological process)
		JPanel GOBiologicalProcessPanel=createNodeAnnotationLabel_multiple(node, strConstants.getDBGO(), strConstants.getDBBiologicalProcess());
		vertexNodeContent.add(GOBiologicalProcessPanel);

		vertexNodeContent.add(new JLabel(" "));
	}

	private void displayHallmarkInfo(String node) 
	{
		ArrayList<Integer> hallmarkList=postgreDB.getHallmark_hallmarkOfNode(node);
		int i=0;
		boolean HAS_HALLMARK=false;

		System.out.println(hallmarkList);

		if(hallmarkList.size()>0)
		{
			JPanel hallmarkPanel = new JPanel();
			hallmarkPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
			JLabel hallmarkTitleLabel=new JLabel("<<HALLMARK>>");
			hallmarkPanel.add(hallmarkTitleLabel);
			vertexNodeContent.add(hallmarkPanel);
		}

		for(i=0; i<hallmarkList.size(); i++)
		{
			JPanel hallmarkPanel = new JPanel();
			hallmarkPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
			JLabel hallmarkGUILabel = new JLabel();

			if(hallmarkList.get(i)==1)
			{
				String hallmarkDB=(String) HallmarkMapDBString.get(i);
				String hallmarkGUI=(String) HallmarkMapGUIString.get(hallmarkDB);
				hallmarkGUILabel.setText("(+) ".concat(hallmarkGUI));	
			}
			else if(hallmarkList.get(i)==(-1))
			{
				String hallmarkDB=(String) HallmarkMapDBString.get(i);
				String hallmarkGUI=(String) HallmarkMapGUIString.get(hallmarkDB);
				hallmarkGUILabel.setText("(-) ".concat(hallmarkGUI));	
			}

			if(hallmarkList.get(i)!=0)
			{
				hallmarkPanel.add(hallmarkGUILabel);
				vertexNodeContent.add(hallmarkPanel);
				HAS_HALLMARK=true;
			}
		}

		if(HAS_HALLMARK) //retrieve the hallmark annotation information 
		{
			final ArrayList<annotation> hallmarkAnnotationList=postgreDB.getHallmark_annotationOfNode(node);
			JPanel annotationPanel = new JPanel();
			annotationPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
			annotationPanel.add(new JLabel("[Evidence]:"));

			for(i=0; i<hallmarkAnnotationList.size(); i++)
			{
				System.out.println(hallmarkAnnotationList.get(i).getSource());
				JLabel hallmarkAnnotationLabel = new JLabel();
				annotationPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
				hallmarkAnnotationLabel.setText(hallmarkAnnotationList.get(i).getSource().concat(", "));
				hallmarkAnnotationLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
				final int j=i;
				hallmarkAnnotationLabel.addMouseListener(new MouseAdapter() {
					public void mouseClicked(MouseEvent e)
					{
						if (e.getClickCount() > 0) {
							if (Desktop.isDesktopSupported()) {
								Desktop desktop = Desktop.getDesktop();
								try {
									URI uri = new URI(processSingleParamURL(hallmarkAnnotationList.get(j).getURL(), hallmarkAnnotationList.get(j).getID()));
									desktop.browse(uri);
								} catch (IOException ex) {
									// TODO Auto-generated catch block
									ex.printStackTrace();
								} catch (URISyntaxException ex) {
									// TODO Auto-generated catch block
									ex.printStackTrace();
								}
							}
						}
					}
				});
				annotationPanel.add(hallmarkAnnotationLabel);
			}
			vertexNodeContent.add(annotationPanel);
		}
	}

	private void displayExpressionInfo(String node) 
	{
		JPanel expressionTitlePanel = new JPanel();
		expressionTitlePanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		JLabel expressionTitleLabel=new JLabel("<<logFC>>");
		expressionTitlePanel.add(expressionTitleLabel);
		vertexNodeContent.add(expressionTitlePanel);

		JPanel expressionPanel=new JPanel();
		expressionPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		JLabel expressionLabel=new JLabel(postgreDB.getNetworkViewNode_foldChange_foldChangeOfNode(selectedView, node).toString());
		expressionPanel.add(expressionLabel);
		vertexNodeContent.add(expressionPanel);
		vertexNodeContent.add(new JLabel(" "));
	}

	private void displayMutationInfo(String node) 
	{
		JPanel mutationTitlePanel = new JPanel();
		mutationTitlePanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		JLabel mutationTitleLabel=new JLabel("<<Mutation frequency=no. of mutated sample/total samples>>");
		mutationTitlePanel.add(mutationTitleLabel);
		vertexNodeContent.add(mutationTitlePanel);

		JPanel mutationPanel=new JPanel();
		mutationPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		JLabel mutationLabel=new JLabel(postgreDB.getNetworkViewNode_mutation_percentageOfNode(selectedView, node).toString()+"%");
		mutationPanel.add(mutationLabel);
		vertexNodeContent.add(mutationPanel);
		vertexNodeContent.add(new JLabel(" "));
	}


	private void getAndDisplaySpeciesInformation(String node) 
	{
		vertexNodeContent.removeAll();
		vertexNodeContent.setLayout(new BoxLayout(vertexNodeContent, BoxLayout.Y_AXIS));

		displayAnnotationInfo(node);
		displayHallmarkInfo(node);
		displayExpressionInfo(node);
		displayMutationInfo(node);

		vertexNodeContent.repaint();
	}

	private void getAndDisplayNodeConnection(String node) 
	{
		ArrayList<edge> connectionList=new ArrayList<edge>();
		int i=0;
		popClickListener popClick=new popClickListener();

		if(settingPanel.isVisible()==false)
		{
			ArrayList<String> list=new ArrayList<String>();
			for(i=0; i<pathwayNodeList.size(); i++)
				list.add(pathwayNodeList.get(i));
			for(i=0; i<oneHopNeighbourNodeList.size(); i++)
				list.add(oneHopNeighbourNodeList.get(i));
			connectionList=postgreDB.getNetworkEdge_edgeListOfNodeInInducedGraph(node, list);
		}
		else
			connectionList=postgreDB.getNetworkEdge_reactionList(node);
		ArrayList<rate> kineticsLibrary=postgreDB.getKinetics_rateAnnotation();

		vertexReactionContent.removeAll();
		vertexReactionContent.setLayout(new BoxLayout(vertexReactionContent, BoxLayout.Y_AXIS));

		for(i=0; i<connectionList.size(); i++)
		{
			JPanel rxnPanel=new JPanel();
			JLabel edgeLabel=new JLabel();
			ArrayList<String> edgeKinetics=postgreDB.getKinetics_kineticList(connectionList.get(i).getSourceName(), connectionList.get(i).getTargetName(), connectionList.get(i).getType());
			rxnPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

			//add arrows according to edge type
			if(connectionList.get(i).getType().compareTo("Pos")==0)
				edgeLabel.setText(connectionList.get(i).getSourceName().concat(POS_EDGE).concat(connectionList.get(i).getTargetName()));
			else if(connectionList.get(i).getType().compareTo("Neg")==0)
				edgeLabel.setText(connectionList.get(i).getSourceName().concat(NEG_EDGE).concat(connectionList.get(i).getTargetName()));
			else
				edgeLabel.setText(connectionList.get(i).getSourceName().concat(NEUTRAL_EDGE).concat(connectionList.get(i).getTargetName()));
			edgeLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			edgeLabel.addMouseListener(popClick);
			if(popClick.getLeftMouse()==true)
			{
				String sourceNode=getSourceNodeFromEdgeLabel(edgeLabel.getText());
				String targetNode=getTargetNodeFromEdgeLabel(edgeLabel.getText());
				String statusString;

				statusString="Selected edge ("+sourceNode+", "+targetNode+")...";
				updateStatus(statusString);
				gephiGraph.colorSelectedEdge(sourceNode, targetNode, "10");
			}
			rxnPanel.add(edgeLabel);

			//append " [kinetic]" if this edge has kinetic information
			//clicking on this label displays details of the edge kinetics
			if(edgeKinetics.size()>0)
			{
				JLabel kineticLabel = new JLabel(" [kinetic]");
				kineticLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
				final ArrayList<String> edgeKineticsCopy=edgeKinetics;
				final ArrayList<rate> kineticsLibraryCopy=kineticsLibrary;
				kineticLabel.addMouseListener(new MouseAdapter() {
					public void mouseClicked(MouseEvent e)
					{
						if (e.getClickCount() > 0) {
							displayKineticsDialog displayKinetics=new displayKineticsDialog(edgeKineticsCopy, kineticsLibraryCopy);
							displayKinetics.setVisible(true);
						}
					}
				});
				rxnPanel.add(kineticLabel);
			}
			vertexReactionContent.add(rxnPanel);
		}
		vertexReactionContent.repaint();
	}

	private String getSourceNodeFromEdgeLabel(String s) 
	{
		int index=s.indexOf(POS_EDGE);
		if(index==(-1))
		{
			index=s.indexOf(NEG_EDGE);
			if(index==(-1))
			{
				index=s.indexOf(NEUTRAL_EDGE);
				if(index==(-1))
					return null;
				else
					return s.substring(0, index);
			}
			else
				return s.substring(0, index);
		}
		else
			return s.substring(0, index);
	}

	private String getTargetNodeFromEdgeLabel(String s) 
	{
		int index=s.indexOf(POS_EDGE);
		if(index==(-1))
		{
			index=s.indexOf(NEG_EDGE);
			if(index==(-1))
			{
				index=s.indexOf(NEUTRAL_EDGE);
				if(index==(-1))
					return null;
				else
					return s.substring(index+NEUTRAL_EDGE.length());
			}
			else
				return s.substring(index+NEG_EDGE.length());
		}
		else
			return s.substring(index+POS_EDGE.length());
	}

	private void pathwayList_valueChanged(ListSelectionEvent e) 
	{
		if (!e.getValueIsAdjusting()) 
		{
			String selectedPathway = pathwayList.getSelectedValue().toString();
			System.out.println(selectedPathway);
			gephiGraph.resetHallmarkNode();
			ArrayList<String> nodeInPathway=postgreDB.getPathway_getNodeInPathway(processName(selectedPathway));
			//check if pathwayNodeInfoList is empty, if not empty, restore these nodes to their original
			if(pathwayNodeInfoList.size()>0)
			{
				for(int i=0; i<pathwayNodeInfoList.size(); i++)
					gephiGraph.restoreNode(pathwayNodeInfoList.get(i).getNode(), pathwayNodeInfoList.get(i).getR(),
							pathwayNodeInfoList.get(i).getG(), pathwayNodeInfoList.get(i).getB(),
							pathwayNodeInfoList.get(i).getAlpha(), pathwayNodeInfoList.get(i).getSize(),
							pathwayNodeInfoList.get(i).getLabel());
			}
			pathwayNodeInfoList=new ArrayList<graphNodeData>();
			pathwayNodeInfoList=gephiGraph.recolorNodeOfSelectedPathway(nodeInPathway);
			sendVirtualClickToRefreshGraphDisplay();
		}
	}

	private void crosstalkPathwayList_valueChanged(ListSelectionEvent e) 
	{
		if (!e.getValueIsAdjusting()) 
		{
			String selectedPathway = crosstalkPathwayList.getSelectedValue().toString();
			System.out.println(selectedPathway);
			gephiGraph.resetHallmarkNode();
			ArrayList<String> nodeInPathway=postgreDB.getPathway_getNodeInPathway(processName(selectedPathway));
			//check if pathwayNodeInfoList is empty, if not empty, restore these nodes to their original
			if(pathwayNodeInfoList.size()>0)
			{
				for(int i=0; i<pathwayNodeInfoList.size(); i++)
					gephiGraph.restoreNode(pathwayNodeInfoList.get(i).getNode(), pathwayNodeInfoList.get(i).getR(),
							pathwayNodeInfoList.get(i).getG(), pathwayNodeInfoList.get(i).getB(),
							pathwayNodeInfoList.get(i).getAlpha(), pathwayNodeInfoList.get(i).getSize(),
							pathwayNodeInfoList.get(i).getLabel());
			}
			pathwayNodeInfoList=new ArrayList<graphNodeData>();
			pathwayNodeInfoList=gephiGraph.recolorNodeOfSelectedPathway(nodeInPathway);
			sendVirtualClickToRefreshGraphDisplay();
		}
	}

	private void targetList_valueChanged(ListSelectionEvent e) 
	{

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

		vertexLegendContent.add(legendLabelColorPanel);
	}

	private void addVertexLegend(boolean ignorePathway) 
	{
		vertexLegendContent.removeAll();
		if(ignorePathway==false && selectedPathwayList.size()>0)
			addVertexLegendPathwayColor();
		addVertexLegendHallmarkColor();
	}

	private void addVertexLegendPathwayColor() 
	{
		JLabel legendPathwayTitle=new JLabel("Pathway node color:");
		JPanel legendPathwayTitlePanel=new JPanel();
		legendPathwayTitlePanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		legendPathwayTitlePanel.add(legendPathwayTitle);
		vertexLegendContent.add(legendPathwayTitlePanel);
		if(selectedPathwayList.size()==1)
			addVertexLegendLabelColor(selectedPathwayList.get(0), new Color(0xCCCCFF)); 
		else
		{
			addVertexLegendLabelColor(selectedPathwayList.get(0), new Color(0xCCCCFF)); 
			addVertexLegendLabelColor(selectedPathwayList.get(1), new Color(0x00FF00));
			addVertexLegendLabelColor("Both", new Color(0x0099FF)); 
		}
	}

	private void addVertexLegendHallmarkColor() 
	{
		JLabel legendHallmarkTitle=new JLabel("Hallmark color:");
		JPanel legendHallmarkTitlePanel=new JPanel();
		legendHallmarkTitlePanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		legendHallmarkTitlePanel.add(legendHallmarkTitle);
		vertexLegendContent.add(legendHallmarkTitlePanel);
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

	private ArrayList<String> stringToArray(String s, String delimiter)
	{
		ArrayList<String> arr=new ArrayList<String>();
		int delimiterIndex=s.indexOf(delimiter);

		while(s!=null)
		{
			delimiterIndex=s.indexOf(delimiter);
			if(delimiterIndex!=-1)
			{
				arr.add(s.substring(0,delimiterIndex));
				s=s.substring(delimiterIndex+1);
			}
			else
			{
				arr.add(s);
				s=null;
			}
		}

		return arr;
	}

	private void initialize_component() 
	{
		// =============================tool bar start================
		/************************************************/
		menuBarFile.setText("File");
		/************************************************/
		menuItemFileTopology.setText(OPEN_TOPOLOGY_FILE);
		menuItemFileTopology.setActionCommand(OPEN_TOPOLOGY_FILE);
		menuItemFileTopology.addActionListener(actionListener);
		/************************************************/
		menuItemFileNetworkData.setText(OPEN_NETWORK_DATA_FILE);
		menuItemFileNetworkData.setActionCommand(OPEN_NETWORK_DATA_FILE);
		menuItemFileNetworkData.addActionListener(actionListener);
		/************************************************/
		menuItemFileCuratedCombi.setText(OPEN_CURATED_COMBI_FILE);
		menuItemFileCuratedCombi.setActionCommand(OPEN_CURATED_COMBI_FILE);
		menuItemFileCuratedCombi.addActionListener(actionListener);
		/************************************************/
		menuItemFileCuratedDrug.setText(OPEN_CURATED_DRUG_FILE);
		menuItemFileCuratedDrug.setActionCommand(OPEN_CURATED_DRUG_FILE);
		menuItemFileCuratedDrug.addActionListener(actionListener);
		/************************************************/
		menuBarAction.setText("Action");
		/************************************************/
		menuItemActionVisualize.setText(VISUALIZE_PRELOADED_NETWORK);
		menuItemActionVisualize.setActionCommand(VISUALIZE_PRELOADED_NETWORK);
		menuItemActionVisualize.addActionListener(actionListener);
		menuItemActionComputeTargetCombi.setText(COMPUTE_TARGET_COMBI);
		menuItemActionComputeTargetCombi.setActionCommand(COMPUTE_TARGET_COMBI);
		menuItemActionComputeTargetCombi.addActionListener(actionListener);
		menuItemActionGenerateTargetCombi.setText(GENERATE_TARGET_COMBI);
		menuItemActionGenerateTargetCombi.setActionCommand(GENERATE_TARGET_COMBI);
		menuItemActionGenerateTargetCombi.addActionListener(actionListener);
		menuItemActionComputeTargetCombiSideEffects.setText(COMPUTE_TARGET_COMBI_SIDE_EFFECTS);
		menuItemActionComputeTargetCombiSideEffects.setActionCommand(COMPUTE_TARGET_COMBI_SIDE_EFFECTS);
		menuItemActionComputeTargetCombiSideEffects.addActionListener(actionListener);
		menuItemActionComputeTopologicalFeatures.setText(COMPUTE_TOPOLOGICAL_FEATURES);
		menuItemActionComputeTopologicalFeatures.setActionCommand(COMPUTE_TOPOLOGICAL_FEATURES);
		menuItemActionComputeTopologicalFeatures.addActionListener(actionListener);
		menuItemActionCharacterizeHallmarks.setText(CHARACTERIZE_HALLMARKS);
		menuItemActionCharacterizeHallmarks.setActionCommand(CHARACTERIZE_HALLMARKS);
		menuItemActionCharacterizeHallmarks.addActionListener(actionListener);
		menuItemActionHighlightDynamics.setText(HIGHLIGHT_DYNAMICS);
		menuItemActionHighlightDynamics.setActionCommand(HIGHLIGHT_DYNAMICS);
		menuItemActionHighlightDynamics.addActionListener(actionListener);
		menuItemActionCheckCombiPredictionAgainstCombiCuration.setText(CHECK_COMBI_PREDICTION_AGAINST_COMBI_CURATION);
		menuItemActionCheckCombiPredictionAgainstCombiCuration.setActionCommand(CHECK_COMBI_PREDICTION_AGAINST_COMBI_CURATION);
		menuItemActionCheckCombiPredictionAgainstCombiCuration.addActionListener(actionListener);
		menuItemActionCheckCombiPredictionAgainstMonoCuration.setText(CHECK_COMBI_PREDICTION_AGAINST_MONO_CURATION);
		menuItemActionCheckCombiPredictionAgainstMonoCuration.setActionCommand(CHECK_COMBI_PREDICTION_AGAINST_MONO_CURATION);
		menuItemActionCheckCombiPredictionAgainstMonoCuration.addActionListener(actionListener);
		/************************************************/
		menuBarOption.setText("View");
		/************************************************/
		menuItemOptionFullScreen.setText(VIEW_FULLSCREEN);
		menuItemOptionFullScreen.setActionCommand(VIEW_FULLSCREEN);
		menuItemOptionFullScreen.addActionListener(actionListener);
		menuItemOptionHallmark.setText(VIEW_HALLMARK);
		menuItemOptionHallmark.setActionCommand(VIEW_HALLMARK);
		menuItemOptionHallmark.addActionListener(actionListener);
		//menuItemOptionCompound.setText(VIEW_COMPOUND);
		//menuItemOptionCompound.setActionCommand(VIEW_COMPOUND);
		//menuItemOptionCompound.addActionListener(actionListener);
		menuItemOptionKEGGPathway.setText(VIEW_KEGG_ANNOTATED_PATHWAY);
		menuItemOptionKEGGPathway.setActionCommand(VIEW_KEGG_ANNOTATED_PATHWAY);
		menuItemOptionKEGGPathway.addActionListener(actionListener);
		menuItemOptionUserPathway.setText(CREATE_AND_VIEW_PATHWAY);
		menuItemOptionUserPathway.setActionCommand(CREATE_AND_VIEW_PATHWAY);
		menuItemOptionUserPathway.addActionListener(actionListener);
		//menuItemOptionNodePairPathway.setText(VIEW_NODE_PAIR_PATHWAY);
		//menuItemOptionNodePairPathway.setActionCommand(VIEW_NODE_PAIR_PATHWAY);
		menuItemOptionNodePairPathway.setText(VIEW_NODE_PAIR);
		menuItemOptionNodePairPathway.setActionCommand(VIEW_NODE_PAIR);
		menuItemOptionNodePairPathway.addActionListener(actionListener);
		menuItemOptionFeatureResult.setText(VIEW_FEATURE_RESULT);
		menuItemOptionFeatureResult.setActionCommand(VIEW_FEATURE_RESULT);
		menuItemOptionFeatureResult.addActionListener(actionListener);
		menuItemOptionFeatureResult.setEnabled(false);
		menuItemOptionCharacterizationResult.setText(VIEW_CHARACTERIZATION_RESULT);
		menuItemOptionCharacterizationResult.setActionCommand(VIEW_CHARACTERIZATION_RESULT);
		menuItemOptionCharacterizationResult.addActionListener(actionListener);
		menuItemOptionCharacterizationResult.setEnabled(false);
		menuItemOptionDiseaseNode.setText(VIEW_DISEASE_NODE);
		menuItemOptionDiseaseNode.setActionCommand(VIEW_DISEASE_NODE);
		menuItemOptionDiseaseNode.addActionListener(actionListener);
		menuItemOptionDiseaseNode.setEnabled(false);
		menuItemOptionTargetCombination.setText(VIEW_TARGET_COMBINATION);
		menuItemOptionTargetCombination.setActionCommand(VIEW_TARGET_COMBINATION);
		menuItemOptionTargetCombination.addActionListener(actionListener);
		menuItemOptionTargetCombination.setEnabled(false);
		/************************************************/
		menuBarEdit.setText("Edit");
		/************************************************/
		menuItemEditGraph.setText(EDIT_GRAPH);
		menuItemEditGraph.setActionCommand(EDIT_GRAPH);
		menuItemEditGraph.addActionListener(actionListener);
		menuItemEditCancerTypeView.setText(EDIT_CANCER_TYPE_VIEW);
		menuItemEditCancerTypeView.setActionCommand(EDIT_CANCER_TYPE_VIEW);
		menuItemEditCancerTypeView.addActionListener(actionListener);
		menuItemEditHallmarkGOMapping.setText(EDIT_HALLMARK_GO_MAPPING);
		menuItemEditHallmarkGOMapping.setActionCommand(EDIT_HALLMARK_GO_MAPPING);
		menuItemEditHallmarkGOMapping.addActionListener(actionListener);

		menuBarFile.add(menuItemFileTopology);
		menuBarFile.add(menuItemFileNetworkData);
		menuBarAction.add(menuItemActionVisualize);
		menuBarAction.add(menuItemActionComputeTopologicalFeatures);
		menuBarAction.add(menuItemActionCharacterizeHallmarks);
		menuBarAction.add(menuItemActionHighlightDynamics);
		menuBarOption.add(menuItemOptionFullScreen);
		menuBarOption.add(menuItemOptionHallmark);
		menuBarOption.add(menuItemOptionNodePairPathway);
		menuBarOption.add(menuItemOptionCharacterizationResult);
		menuBarEdit.add(menuItemEditGraph);
		menuBarEdit.add(menuItemEditCancerTypeView);
		menuBarEdit.add(menuItemEditHallmarkGOMapping);
		menu.add(menuBarFile);
		menu.add(menuBarAction);
		menu.add(menuBarOption);
		menu.add(menuBarEdit);

		// =============================node information (top left)================
		// speciesList
		speciesList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		speciesList.addListSelectionListener(new ListSelectionListener() 
		{
			public void valueChanged(ListSelectionEvent e){ speciesList_valueChanged(e);}
		});
		speciesList.addMouseListener(new  MouseAdapter() {
			public void mouseClicked(MouseEvent evt) {
				if(GEPHIGRAPHREADY)
				{
					JList theList = (JList) evt.getSource();
					if(evt.getButton() == java.awt.event.MouseEvent.BUTTON3) {
						int index = theList.locationToIndex(evt.getPoint());
						if (index >= 0) {
							String selected = theList.getModel().getElementAt(index).toString();
							System.out.println(selectedNode+" "+selected);
							if(selectedNode.compareTo(selected)==0)
							{
								viewNeighbourWorkspace=interactivePC.newWorkspace(interactiveProject);
								interactivePC.openWorkspace(viewNeighbourWorkspace); 
								viewNeighbour=new viewNeighbourDialog(interactivePC, interactiveProject, interactiveWorkspace, 
										viewNeighbourWorkspace, selectedNode, allNodeArrList, postgreDB, selectedHallmarkList, osType);
								viewNeighbour.setVisible(true);
							}
						}
					}
				}
			}
		});
		speciesListScrollPane = new JScrollPane(speciesList);
		speciesListScrollPane.setViewportView(speciesList);
		speciesListScrollPane.setPreferredSize(new Dimension(LEFTCOLWIDTH, RESULTTABPANELHEIGHT));
		speciesListScrollPane.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		// targetList
		targetList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		targetList.addListSelectionListener(new ListSelectionListener() 
		{
			public void valueChanged(ListSelectionEvent e){ targetList_valueChanged(e);}
		});
		targetListScrollPane = new JScrollPane(targetList);
		targetListScrollPane.setViewportView(targetList);
		targetListScrollPane.setPreferredSize(new Dimension(LEFTCOLWIDTH, RESULTTABPANELHEIGHT));
		targetListScrollPane.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		// pathwayList
		pathwayList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		pathwayList.addListSelectionListener(new ListSelectionListener() 
		{
			public void valueChanged(ListSelectionEvent e){ pathwayList_valueChanged(e);}
		});
		pathwayListScrollPane = new JScrollPane(pathwayList);
		pathwayListScrollPane.setViewportView(pathwayList);
		pathwayListScrollPane.setPreferredSize(new Dimension(LEFTCOLWIDTH, RESULTTABPANELHEIGHT));
		pathwayListScrollPane.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		// crosstalkPathwayList
		crosstalkPathwayList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		crosstalkPathwayList.addListSelectionListener(new ListSelectionListener() 
		{
			public void valueChanged(ListSelectionEvent e){ crosstalkPathwayList_valueChanged(e);}
		});
		crosstalkPathwayListScrollPane = new JScrollPane(crosstalkPathwayList);
		crosstalkPathwayListScrollPane.setViewportView(crosstalkPathwayList);
		crosstalkPathwayListScrollPane.setPreferredSize(new Dimension(LEFTCOLWIDTH, RESULTTABPANELHEIGHT));
		crosstalkPathwayListScrollPane.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		// diseaseNodeList
		ListSelectionModel diseaseNodeSelectionModel = diseaseNodeTable.getSelectionModel();
		diseaseNodeSelectionModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		diseaseNodeSelectionModel.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				String selectedNode = null;
				int[] selectedRow = diseaseNodeTable.getSelectedRows();
				for (int i = 0; i < selectedRow.length; i++) 
					selectedNode = (String) diseaseNodeTable.getValueAt(selectedRow[i], 0);
				System.out.println("Selected: " + selectedNode);
				speciesList.setSelectedValue(selectedNode,true);
			}
		});
		diseaseNodeListScrollPane = new JScrollPane(diseaseNodeTable);
		diseaseNodeListScrollPane.setViewportView(diseaseNodeTable);
		diseaseNodeListScrollPane.setPreferredSize(new Dimension(LEFTCOLWIDTH, RESULTTABPANELHEIGHT));
		diseaseNodeListScrollPane.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		// predictedCombinationList
		ListSelectionModel predictedCombinationSelectionModel = predictedCombinationTable.getSelectionModel();
		predictedCombinationSelectionModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		predictedCombinationSelectionModel.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				/*String selectedNode = null;
				int[] selectedRow = diseaseNodeTable.getSelectedRows();
				for (int i = 0; i < selectedRow.length; i++) 
					selectedNode = (String) diseaseNodeTable.getValueAt(selectedRow[i], 0);
				System.out.println("Selected: " + selectedNode);
				speciesList.setSelectedValue(selectedNode,true);*/
			}
		});
		predictedCombinationListScrollPane = new JScrollPane(predictedCombinationTable);
		predictedCombinationListScrollPane.setViewportView(predictedCombinationTable);
		predictedCombinationListScrollPane.setPreferredSize(new Dimension(LEFTCOLWIDTH, RESULTTABPANELHEIGHT));
		predictedCombinationListScrollPane.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);

		resultTabbedPane.setSize(new Dimension(LEFTCOLWIDTH, RESULTTABPANELHEIGHT));
		resultTabbedPane.addTab("Species List", speciesListScrollPane);
		//resultTabbedPane.addTab("Pathway List", pathwayListScrollPane);
		//resultTabbedPane.addTab("Target List (Red nodes)", targetListScrollPane);
		resultTabbedPane.setEnabled(true);

		// resultPanel
		resultPanel.add(resultTabbedPane);
		resultPanel.setPreferredSize(new Dimension(LEFTCOLWIDTH, RESULTTABPANELHEIGHT));
		// resultLabel
		resultLabel.setText("Result");
		// ResultLabelPanel
		resultLabelPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		resultLabelPanel.add(resultLabel, 0);
		// outputLabel
		outputLabel.setText("Choose output species:");
		// OutputLabelPanel
		outputLabelPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		outputLabelPanel.add(outputLabel, 0);
		// =============================setting (center-top left)================
		graphViewLabel.setText(GRAPH_VIEW_SETTING);
		graphViewOption.add(GRAPHVIEW);
		graphViewOption.add(NEIGHBOURVIEW);
		graphViewComboBox.addItem(GRAPHVIEW);
		graphViewComboBox.addItem(NEIGHBOURVIEW);
		graphViewComboBox.setSelectedIndex(graphViewOption.indexOf(CURRVIEW));
		graphViewComboBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e){
				Object item=e.getItem();
				if(e.getStateChange()==ItemEvent.SELECTED)
				{
					if(String.valueOf(item).compareTo(CURRVIEW)!=0)
					{
						String selectedNode=speciesList.getSelectedValue().toString();
						String statusString;
						if(CURRVIEW.compareTo(NEIGHBOURVIEW)==0)
							statusString="Changing graph view. Creating neighbour graph of "+selectedNode+". Please wait ...";
						else
							statusString="Changing graph view. Recreating entire graph. Please wait ...";
						updateStatus(statusString);

						FORCERECOLOR=true;
						CURRVIEW=String.valueOf(item);
						visualizeSelectedNodeInGraph(selectedNode);
					}
				}
			}
		});
		editAnnotationButton.setText(EDIT_ANNOTATION);
		editAnnotationButton.setEnabled(false);
		editAnnotationButton.setActionCommand(EDIT_ANNOTATION);
		editAnnotationButton.addActionListener(actionListener);
		editHallmarkButton.setText(EDIT_HALLMARK);
		editHallmarkButton.setEnabled(false);
		editHallmarkButton.setActionCommand(EDIT_HALLMARK);
		editHallmarkButton.addActionListener(actionListener);
		editGraphButton.setText(EDIT_GRAPH);
		editGraphButton.setActionCommand(EDIT_GRAPH);
		editGraphButton.addActionListener(actionListener);
		radioButtonGroup.add(foldChangeRButton);
		radioButtonGroup.add(mutationFrequencyRButton);
		//radioButtonGroup.add(diseaseTargetNodeRButton);
		radioButtonPanel.setLayout(new BoxLayout(radioButtonPanel, BoxLayout.Y_AXIS));
		radioButtonPanel.add(foldChangeRButton);
		foldChangeRButton.setActionCommand(DISPLAY_FOLD_CHANGE);
		foldChangeRButton.addActionListener(actionListener);
		radioButtonPanel.add(mutationFrequencyRButton);
		mutationFrequencyRButton.setActionCommand(DISPLAY_MUTATION_FREQUENCY);
		mutationFrequencyRButton.addActionListener(actionListener);
		//radioButtonPanel.add(diseaseTargetNodeRButton);
		diseaseTargetNodeRButton.setActionCommand(DISPLAY_DISEASE_TARGET_NODE);
		diseaseTargetNodeRButton.addActionListener(actionListener);
		diseaseTargetNodeRButton.setEnabled(false);
		foldChangeRButton.setSelected(true);
		radioButtonPanel.setVisible(false);
		settingPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		//settingPanel.add(graphViewLabel);
		//settingPanel.add(graphViewComboBox);
		JPanel annotationHallmarkPanel=new JPanel();
		annotationHallmarkPanel.setLayout(new BoxLayout(annotationHallmarkPanel, BoxLayout.Y_AXIS));
		annotationHallmarkPanel.add(editAnnotationButton);
		annotationHallmarkPanel.add(editHallmarkButton);
		settingPanel.add(annotationHallmarkPanel);
		//settingPanel.add(editGraphButton);
		settingPanel.add(radioButtonPanel);
		// =============================search node (center-bottom left)================
		searchButton.setText(SEARCH_NODE);
		searchButton.setActionCommand(SEARCH_NODE);
		searchButton.addActionListener(actionListener);
		searchText.setPreferredSize(new Dimension(LEFTCOLWIDTH,TEXTHEIGHT));
		searchPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		searchPanel.add(searchText);
		searchPanel.add(searchButton);
		// =============================status (bottom most - horizontal)================
		statusPanel.setPreferredSize(new Dimension(900,TEXTHEIGHT));
		statusLabel.setPreferredSize(new Dimension(900,TEXTHEIGHT));
		statusPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		statusPanel.add(statusLabel);
		statusLabel.addPropertyChangeListener(new PropertyChangeListener() {
		   @Override
			public void propertyChange(PropertyChangeEvent arg0) {
			   	String newStatus=statusLabel.getText();
				if(newStatus.compareTo(HALLMARK_VIEW_COMPLETE)==0)
				{
					/*viewHallmarkWorkspace=interactivePC.duplicateWorkspace(interactiveWorkspace);
					interactivePC.openWorkspace(viewHallmarkWorkspace); 
					hallmarkSummary=new viewSelectedHallmarkSummaryDialog(postgreDB, selectedHallmarkList, speciesList,
							interactivePC, interactiveProject, interactiveWorkspace, viewHallmarkWorkspace, statusLabel, interactiveGraphPanel);
					hallmarkSummary.setVisible(true);
					*/
				}
				
			}
		 });
		// =============================interactive graph (top right)================
		interactiveGraphPanel.setBackground(Color.BLACK);
		interactiveGraphPanel.setPreferredSize(new Dimension(GRAPHDISPLAYWIDTH, GRAPHDISPLAYHEIGHT));
		interactiveGraphPanel.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e){
				System.out.println("interactiveGraphPanel caught mouse event");
			}
		});
		// =============================tabbed information panel (bottom right)================
		vertexLegendContent.setLayout(new BoxLayout(vertexLegendContent, BoxLayout.Y_AXIS));
		vertexTargetCombinationContent.setLayout(new BoxLayout(vertexTargetCombinationContent, BoxLayout.Y_AXIS));
		addVertexLegend(false);
		vertexTabbedPanel.setPreferredSize(new Dimension(LEFTCOLWIDTH, VERTEXTABPANELHEIGHT-30));
		vertexTabbedPanel.addTab("Species", vertexNodeScroll);
		vertexTabbedPanel.addTab("Reactions", vertexReactionScroll);
		//vertexTabbedPanel.addTab("Target", vertexTargetWebScroll);
		vertexTabbedPanel.addTab("Legend", vertexLegendScroll);
		vertexTabbedPanel.setSelectedIndex(2);
		//vertexTabbedPanel.addTab("Best Target Combination", vertexTargetCombinationScroll);
		// ==========================add component to content Pane =====================
		ingotDialog.setVisible(true);
		ingotDialog.setJMenuBar(menu);
		ingotDialog.setTitle("TROVE");
		ingotDialog.setLocation(new Point((SCREENWIDTH-910)/2, (SCREENHEIGHT-FRAMEHEIGHT)/2));
		ingotDialog.setSize(new Dimension(1000, FRAMEHEIGHT+600));
		ingotDialog.addWindowListener(new WindowAdapter() {
			public void windowClosed(WindowEvent e){
				System.out.println("Before I close, close postgreDB connection...");
				postgreDB.closeConnection();
			}
			/*public void windowActivated(WindowEvent e){
				if(hallmarkSummary!=null && hallmarkSummary.isVisible()==true && viewNeighbour!=null && viewNeighbour.isVisible()==false)
				{
					hallmarkSummary.setVisible(false);
					hallmarkSummary.setVisible(true);
					selectedHallmarkList=hallmarkSummary.getSelectedHallmarks();
					viewNeighbour=null;
				}
				if(hallmarkSummary!=null && hallmarkSummary.isVisible()==false)
				{
					//selectedHallmarkList=hallmarkSummary.getSelectedHallmarks();
					hallmarkSummary=null;
				}
				
			}*/
		});
		ingotDialog.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e){
				//System.out.println("ingotDialog caught mouse event");
			}
		});
		ingotDialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		ingotDialog.setModal(true);
		ingotDialog.setResizable(false);
		ingotDialog.setLayout(new BoxLayout(ingotDialog.getContentPane(), BoxLayout.Y_AXIS));
		leftColumnPanel.setLayout(new BoxLayout(leftColumnPanel, BoxLayout.Y_AXIS));
		topRowPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		bottomRowPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		leftColumnPanel.add(resultTabbedPane);
		leftColumnPanel.add(settingPanel);
		leftColumnPanel.add(searchPanel);
		leftColumnPanel.add(vertexTabbedPanel);
		topRowPanel.add(leftColumnPanel);
		topRowPanel.add(interactiveGraphPanel);
		bottomRowPanel.add(statusPanel);
		ingotDialog.add(topRowPanel);
		ingotDialog.add(bottomRowPanel);
		ingotDialog.pack();
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
					if(ACTION.compareTo(VISUALIZE_PRELOADED_NETWORK)==0)
					{
						//send virtual click to refresh screen
						working=true;
						ArrayList<String> nodeList=(ArrayList<String>)paramList.get(1);
						String selectedView=paramList.get(2).toString();
						String selectedLayout=paramList.get(3).toString();
						sendVirtualClickToRefreshGraphDisplay();
						//Task 1: creating graph
						getGraphDataFromDB(nodeList);
						createGraph();
						//Task 2: visualizing graph
						statusLabel.setText("visualizing graph...configuring interactive graph layout");
						if(selectedLayout.compareTo(LAYOUT_YIFANHU)==0)
							gephiGraph.layoutInteractiveGraph_yifanhu();
						else if(selectedLayout.compareTo(LAYOUT_NOOVERLAP)==0)
							gephiGraph.layoutInteractiveGraph_noverlap();
						else if(selectedLayout.compareTo(LAYOUT_FRUCHTERMAN_REINGOLD)==0)
							gephiGraph.layoutInteractiveGraph_fruchtermanReingold();
						else if(selectedLayout.compareTo(LAYOUT_FORCE_ATLAS)==0)
							gephiGraph.layoutInteractiveGraph_forceAtlas();
						else if(selectedLayout.compareTo(LAYOUT_FORCE_ATLAS_TWO)==0)
							gephiGraph.layoutInteractiveGraph_forceAtlasTwo();
						else if(selectedLayout.compareTo(LAYOUT_LABEL_ADJUST)==0)
							gephiGraph.layoutInteractiveGraph_labelAdjust();
						else
							gephiGraph.layoutInteractiveGraph_yifanhu();											
						statusLabel.setText("visualizing graph...configuring interactive graph preview");
						gephiGraph.configureInteractivePreview();
						statusLabel.setText("visualizing graph...processing graph target");
						gephiGraph.visualizeGraph(interactiveGraphPanel);
						//Task 3: thicken edges 
						if(GRAPHKINETICSREADY)
						{
							ArrayList<edge> edgeWithKinetics=postgreDB.getKinetics_edgeList();
							statusLabel.setText("visualizing graph...processing kinetics edge information");
							System.out.println("thicken edges according to kinetics");
							gephiGraph.updateEdgeTable_kinetics(edgeWithKinetics);
						}
						if(selectedView.compareTo("Entire view")!=0)
						{
							displayFoldChange_actionPerformed();
							jungGraph.findDescendantNodes_convertToDAG(statusLabel);
							totalEssentialNodes=postgreDB.getNetworkViewNode_getTotalEssentialNode(selectedView);
							//ArrayList<String> viewNodeNameList=postgreDB.getNetworkViewNode_foldChange_nodeNameList(selectedView);
							//gephiGraph.colorAndResizeNodeSet(viewNodeNameList, postgreDB.getNetworkViewNode_foldChange_foldChangeList(selectedView, viewNodeNameList));
						}
						//Task 5: compute topological features (if selected)
						if((boolean)paramList.get(0)==true)
						{
							statusLabel.setText("clearing topological feature values...");
							postgreDB.clearTopologicalFeatures();
							statusLabel.setText("computing network topological features...");
							computeNetworkTopologicalFeature(strConstants.getDBInDegree());
							computeNetworkTopologicalFeature(strConstants.getDBOutDegree());
							computeNetworkTopologicalFeature(strConstants.getDBTotalDegree());
							computeNetworkTopologicalFeature(strConstants.getDBEigenvector());
							computeNetworkTopologicalFeature(strConstants.getDBBetweenness());
							computeNetworkTopologicalFeature(strConstants.getDBBridgingCoeff());
							computeNetworkTopologicalFeature(strConstants.getDBBridgingCentrality());
							computeNetworkTopologicalFeature(strConstants.getDBUndirClustering());
							computeNetworkTopologicalFeature(strConstants.getDBInClustering());
							computeNetworkTopologicalFeature(strConstants.getDBOutClustering());
							computeNetworkTopologicalFeature(strConstants.getDBCycClustering());
							computeNetworkTopologicalFeature(strConstants.getDBMidClustering());
						}

						statusLabel.setText("Graph visualized.");
						sendVirtualClickToRefreshGraphDisplay();
						interactiveWorkspace=gephiGraph.getWorkspace();
						GEPHIGRAPHREADY=true;
						System.out.println("graphNodeIDList size:"+graphNodeIDList.size());
						System.out.println("graphEdgeList size:"+graphEdgeList.size());
						working=false;
					}
					if(ACTION.compareTo(RELOAD_NETWORK)==0)
					{
						//send virtual click to refresh screen
						working=true;
						ArrayList<String> nodeList=(ArrayList<String>)paramList.get(0);
						String selectedView=paramList.get(1).toString();
						String selectedLayout=paramList.get(2).toString();
						sendVirtualClickToRefreshGraphDisplay();
						//Task 1: creating graph
						getGraphDataFromDB(nodeList);
						createGraph();
						//Task 2: visualizing graph
						statusLabel.setText("visualizing graph...configuring interactive graph layout");
						if(selectedLayout.compareTo(LAYOUT_YIFANHU)==0)
							gephiGraph.layoutInteractiveGraph_yifanhu();
						else if(selectedLayout.compareTo(LAYOUT_NOOVERLAP)==0)
							gephiGraph.layoutInteractiveGraph_noverlap();
						else if(selectedLayout.compareTo(LAYOUT_FRUCHTERMAN_REINGOLD)==0)
							gephiGraph.layoutInteractiveGraph_fruchtermanReingold();
						else if(selectedLayout.compareTo(LAYOUT_FORCE_ATLAS)==0)
							gephiGraph.layoutInteractiveGraph_forceAtlas();
						else if(selectedLayout.compareTo(LAYOUT_FORCE_ATLAS_TWO)==0)
							gephiGraph.layoutInteractiveGraph_forceAtlasTwo();
						else if(selectedLayout.compareTo(LAYOUT_LABEL_ADJUST)==0)
							gephiGraph.layoutInteractiveGraph_labelAdjust();
						else
							gephiGraph.layoutInteractiveGraph_yifanhu();											
						statusLabel.setText("visualizing graph...configuring interactive graph preview");
						gephiGraph.configureInteractivePreview();
						statusLabel.setText("visualizing graph...processing graph target");
						gephiGraph.visualizeGraph(interactiveGraphPanel);
						//Task 3: thicken edges 
						if(GRAPHKINETICSREADY)
						{
							ArrayList<edge> edgeWithKinetics=postgreDB.getKinetics_edgeList();
							statusLabel.setText("visualizing graph...processing kinetics edge information");
							System.out.println("thicken edges according to kinetics");
							gephiGraph.updateEdgeTable_kinetics(edgeWithKinetics);
						}
						if(selectedView.compareTo("Entire view")!=0)
						{
							displayFoldChange_actionPerformed();
							jungGraph.findDescendantNodes_convertToDAG(statusLabel);
							totalEssentialNodes=postgreDB.getNetworkViewNode_getTotalEssentialNode(selectedView);
							//ArrayList<String> viewNodeNameList=postgreDB.getNetworkViewNode_foldChange_nodeNameList(selectedView);
							//gephiGraph.colorAndResizeNodeSet(viewNodeNameList, postgreDB.getNetworkViewNode_foldChange_foldChangeList(selectedView, viewNodeNameList));
						}
						statusLabel.setText("Graph visualized.");
						sendVirtualClickToRefreshGraphDisplay();
						interactiveWorkspace=gephiGraph.getWorkspace();
						GEPHIGRAPHREADY=true;
						System.out.println("graphNodeIDList size:"+graphNodeIDList.size());
						System.out.println("graphEdgeList size:"+graphEdgeList.size());
						working=false;
					}
					if(ACTION.compareTo(VIEW_NEIGHBOUR)==0)
					{
						working=true;
						String selectedNode=(String)paramList.get(0);
						ArrayList<String> neighbourList=(ArrayList<String>)paramList.get(1);
						createSubGraphFromNodeList(selectedNode,neighbourList); 
						working=false;
					}
					if(ACTION.compareTo(HIGHLIGHT_DYNAMICS)==0)
					{
						working=true;
						ArrayList<edge> edgeList=(ArrayList<edge>)paramList.get(0);
						gephiGraph=(Gephi_graph)paramList.get(1);
						allHallmark hallmarkNodeList=(allHallmark)paramList.get(2);
						selectedHallmarkList=(ArrayList<String>)paramList.get(3);
						statusLabel.setText("highlighting dynamics...");
						for(int i=0; i<edgeList.size(); i++)
						{
							double percentComplete=100.0*i/(edgeList.size()-1);
							DecimalFormat df = new DecimalFormat();
							df.setMaximumFractionDigits(2);
							edge e=edgeList.get(i);
							gephiGraph.colorEdgeDynamics(e.getSourceName(), e.getTargetName(), "10");
							statusLabel.setText("highlighting dynamics ["+df.format(percentComplete)+"%]");
						}
						//uncolor all previous hallmarks
						gephiGraph.resetHallmarkNode();
						//color selected hallmarks
						gephiGraph.colorAndResizeHallmarkNode(hallmarkNodeList, selectedHallmarkList, true);
						sendVirtualClickToRefreshGraphDisplay();
						statusLabel.setText("finish highlighting dynamics");
						working=false;
					}
					if(ACTION.compareTo(CREATE_AND_VIEW_PATHWAY)==0)
					{
						working=true;
						String pathwayDisplay=(String)paramList.get(0);
						pathwayNodeList=(ArrayList<String>)paramList.get(1);
						gephiGraph=(Gephi_graph)paramList.get(2);
						allHallmark hallmarkNodeList=(allHallmark)paramList.get(3);
						selectedHallmarkList=(ArrayList<String>)paramList.get(4);
						createPathway(pathwayNodeList, pathwayDisplay);
						statusLabel.setText("visualizing pathway...color pathway nodes");
						gephiGraph.colorPathwayNode(pathwayNodeList, false, 0);
						statusLabel.setText("visualizing pathway...configuring pathway graph layout");
						int size=pathwayNodeList.size()+oneHopNeighbourNodeList.size();
						if(size<50)
							gephiGraph.layoutInteractiveGraph_labelAdjust();
						else if(size>=50 && size<100)
							gephiGraph.layoutInteractiveGraph_fruchtermanReingold();
						else
							gephiGraph.layoutInteractiveGraph_yifanhu();
						statusLabel.setText("visualizing pathway...updating hallmark nodes");
						//uncolor all previous hallmarks
						gephiGraph.resetHallmarkNode();
						//color selected hallmarks
						gephiGraph.colorAndResizeHallmarkNode(hallmarkNodeList, selectedHallmarkList, true);
						sendVirtualClickToRefreshGraphDisplay();
						statusLabel.setText("finish visualizing user-created pathway");
						working=false;
					}
					if(ACTION.compareTo(VIEW_KEGG_ANNOTATED_PATHWAY)==0)
					{
						working=true;
						postgreSQL postgreDB=(postgreSQL)paramList.get(0);
						ArrayList<String> selectedPathway=(ArrayList<String>)paramList.get(1);
						speciesList=(JList<String>)paramList.get(2);
						GUIwrapper=(java_GUIWrapper)paramList.get(3);
						pathwayList=(JList<String>)paramList.get(4);
						selectedPathwayList=(ArrayList<String>)paramList.get(5);
						gephiGraph=(Gephi_graph)paramList.get(6);

						ArrayList<ArrayList<String>> nodeListList=new ArrayList<ArrayList<String>>();
						pathwayNodeList=new ArrayList<String>();
						allHallmark hallmarkNodeList=postgreDB.getHallmark_hallmark();
						for(int i=0; i<selectedPathway.size(); i++)
						{
							ArrayList<String> list=postgreDB.getPathway_getNodeInPathway(processName(selectedPathway.get(i)));
							System.out.println("list: "+list.toString());
							System.out.println("nodeList: "+pathwayNodeList.toString());
							nodeListList.add(list);
							for(int j=0; j<list.size(); j++)
							{
								if(pathwayNodeList.contains(list.get(j))==false)
									pathwayNodeList.add(list.get(j));
							}
							System.out.println("nodeList: "+pathwayNodeList.toString());
						}
						ArrayList<String> crosstalkList=postgreDB.getPathway_pathwayNameListHavingNodes(pathwayNodeList);
						speciesList=GUIwrapper.updateJListData(speciesList, pathwayNodeList);
						pathwayList=GUIwrapper.updateJListData(pathwayList, selectedPathwayList);
						crosstalkPathwayList=GUIwrapper.updateJListData(crosstalkPathwayList, crosstalkList);
						System.out.println(pathwayNodeList.toString());
						createPathway(pathwayNodeList, strConstants.getInducedGraph());
						statusLabel.setText("visualizing pathway...color pathway nodes");
						for(int i=0; i<nodeListList.size(); i++)
							gephiGraph.colorPathwayNode(nodeListList.get(i), false, i);
						statusLabel.setText("visualizing pathway...configuring pathway graph layout");
						int size=pathwayNodeList.size();
						if(size<50)
							gephiGraph.layoutInteractiveGraph_labelAdjust();
						else
							gephiGraph.layoutInteractiveGraph_yifanhu();
						statusLabel.setText("visualizing pathway...updating hallmark nodes");
						//uncolor all previous hallmarks
						gephiGraph.resetHallmarkNode();
						//color selected hallmarks
						gephiGraph.colorAndResizeHallmarkNode(hallmarkNodeList, selectedHallmarkList, true);
						statusLabel.setText("finish visualizing pathway selected pathway");
						working=false;
					}
					if(ACTION.compareTo(VIEW_NODE_PAIR)==0)
					{
						working=true;
						ArrayList<String> selectedNodePair=(ArrayList<String>)paramList.get(0);
						jungGraph=(JUNG_graph)paramList.get(1);
						gephiGraph=(Gephi_graph)paramList.get(2);
						allHallmark hallmarkNodeList=(allHallmark)paramList.get(3);
						selectedHallmarkList=(ArrayList<String>)paramList.get(4);
						ArrayList<ArrayList<String>> connectionList=(ArrayList<ArrayList<String>>)paramList.get(5);
						
						statusLabel.setText("visualizing pathway of node pair...removing nodes");
						//jungGraph.removeNodesNotInList(statusLabel, pathwayNodeList);
						//gephiGraph.removeNodesNotInList(statusLabel, pathwayNodeList);
						//gephiGraph.reset();
						speciesList=GUIwrapper.updateJListData(speciesList, pathwayNodeList);

						System.out.println("VIEW_NODE_PAIR here");

						statusLabel.setText("visualizing pathway of node pair....identifying nodes in pathway");
						//redraw nodes
						//Task 1: creating graph
						createSubGraphFromPath(connectionList);
						sendVirtualClickToRefreshGraphDisplay();
						//Task 2: visualizing graph
						statusLabel.setText("visualizing graph...configuring interactive graph layout");
						/*if(selectedLayout.compareTo(LAYOUT_YIFANHU)==0)
							gephiGraph.layoutInteractiveGraph_yifanhu();
						else if(selectedLayout.compareTo(LAYOUT_NOOVERLAP)==0)
							gephiGraph.layoutInteractiveGraph_noverlap();
						else if(selectedLayout.compareTo(LAYOUT_FRUCHTERMAN_REINGOLD)==0)
							gephiGraph.layoutInteractiveGraph_fruchtermanReingold();
						else if(selectedLayout.compareTo(LAYOUT_FORCE_ATLAS)==0)
							gephiGraph.layoutInteractiveGraph_forceAtlas();
						else if(selectedLayout.compareTo(LAYOUT_FORCE_ATLAS_TWO)==0)
							gephiGraph.layoutInteractiveGraph_forceAtlasTwo();
						else if(selectedLayout.compareTo(LAYOUT_LABEL_ADJUST)==0)
							gephiGraph.layoutInteractiveGraph_labelAdjust();
						else
							gephiGraph.layoutInteractiveGraph_yifanhu();											
						 */statusLabel.setText("visualizing graph...configuring interactive graph preview");
						 gephiGraph.configureInteractivePreview();
						 statusLabel.setText("visualizing graph...processing graph target");
						 gephiGraph.visualizeGraph(interactiveGraphPanel);
						  //Task 3: thicken edges 
						 if(GRAPHKINETICSREADY)
						 {
							 ArrayList<edge> edgeWithKinetics=postgreDB.getKinetics_edgeList();
							 statusLabel.setText("visualizing graph...processing kinetics edge information");
							 System.out.println("thicken edges according to kinetics");
							 gephiGraph.updateEdgeTable_kinetics(edgeWithKinetics);
						 }
						 //displayFoldChange_actionPerformed();
						 jungGraph.findDescendantNodes_convertToDAG(statusLabel);
						 totalEssentialNodes=postgreDB.getNetworkViewNode_getTotalEssentialNode(selectedView);
						 //ArrayList<String> viewNodeNameList=postgreDB.getNetworkViewNode_foldChange_nodeNameList(selectedView);
						 //gephiGraph.colorAndResizeNodeSet(viewNodeNameList, postgreDB.getNetworkViewNode_foldChange_foldChangeList(selectedView, viewNodeNameList));

						 statusLabel.setText("Graph visualized.");
						 sendVirtualClickToRefreshGraphDisplay();
						 GEPHIGRAPHREADY=true;
						 System.out.println("graphNodeIDList size:"+graphNodeIDList.size());
						 System.out.println("graphEdgeList size:"+graphEdgeList.size());statusLabel.setText("visualizing pathway of node pair...updating hallmark nodes");
						 //uncolor all previous hallmarks
						 gephiGraph.resetHallmarkNode();
						 //color selected hallmarks
						 gephiGraph.colorAndResizeHallmarkNode(hallmarkNodeList, selectedHallmarkList, true);
						 statusLabel.setText("visualizing pathway of node pair completed");
						 interactiveWorkspace=gephiGraph.getWorkspace();
						 working=false;
					}
					if(ACTION.compareTo(VIEW_NODE_PAIR_PATHWAY)==0)
					{
						working=true;
						ArrayList<String> selectedNodePair=(ArrayList<String>)paramList.get(0);
						jungGraph=(JUNG_graph)paramList.get(1);
						gephiGraph=(Gephi_graph)paramList.get(2);
						allHallmark hallmarkNodeList=(allHallmark)paramList.get(3);
						selectedHallmarkList=(ArrayList<String>)paramList.get(4);

						statusLabel.setText("visualizing pathway of node pair...removing nodes");
						jungGraph.removeNodesNotInList(statusLabel, pathwayNodeList);
						gephiGraph.removeNodesNotInList(statusLabel, pathwayNodeList);
						speciesList=GUIwrapper.updateJListData(speciesList, pathwayNodeList);
						ArrayList<String> crosstalkList=postgreDB.getPathway_pathwayNameListHavingNodes(pathwayNodeList);
						crosstalkPathwayList=GUIwrapper.updateJListData(crosstalkPathwayList, crosstalkList);

						System.out.println("VIEW_NODE_PAIR_PATHWAY here");

						if(pathwayNodeList.size()>0)
							gephiGraph.colorPathwayNode(selectedNodePair, true, 0);
						statusLabel.setText("visualizing pathway of node pair...updating hallmark nodes");
						//uncolor all previous hallmarks
						gephiGraph.resetHallmarkNode();
						//color selected hallmarks
						gephiGraph.colorAndResizeHallmarkNode(hallmarkNodeList, selectedHallmarkList, true);
						statusLabel.setText("visualizing pathway of node pair completed");
						working=false;
					}
					if(ACTION.compareTo(VIEW_HALLMARK_INDUCED)==0)
					{
						working=true;
						selectedPathwayList=(ArrayList<String>)paramList.get(0);
						pathwayList=(JList<String>)paramList.get(1);
						postgreDB=(postgreSQL)paramList.get(2);
						pathwayNodeList=(ArrayList<String>)paramList.get(3);
						GUIwrapper=(java_GUIWrapper)paramList.get(4);
						String pathwayDisplay=(String)paramList.get(5);
						oneHopNeighbourNodeList=(ArrayList<String>)paramList.get(6);
						gephiGraph=(Gephi_graph)paramList.get(7);
						allHallmark hallmarkNodeList=(allHallmark)paramList.get(8);
						selectedHallmarkList=(ArrayList<String>)paramList.get(9);

						selectedPathwayList=postgreDB.getPathway_pathwayNameListHavingNodes(pathwayNodeList);
						pathwayList=GUIwrapper.updateJListData(pathwayList, selectedPathwayList);
						createPathway(pathwayNodeList, pathwayDisplay);
						sendVirtualClickToRefreshGraphDisplay();
						statusLabel.setText("visualizing hallmark pathway...configuring pathway graph layout");
						int size=pathwayNodeList.size()+oneHopNeighbourNodeList.size();
						if(size<50)
							gephiGraph.layoutInteractiveGraph_labelAdjust();
						else if(size>=50 && size<100)
							gephiGraph.layoutInteractiveGraph_fruchtermanReingold();
						else
							gephiGraph.layoutInteractiveGraph_yifanhu();
						statusLabel.setText("visualizing hallmark pathway...coloring hallmark nodes");
						//uncolor all previous hallmarks
						gephiGraph.resetHallmarkNode();
						//color selected hallmarks
						gephiGraph.colorAndResizeHallmarkNode(hallmarkNodeList, selectedHallmarkList, true);
						//sendVirtualClickToRefreshGraphDisplay();
						statusLabel.setText("finish visualizing hallmark pathway");
						working=false;
					}
					if(ACTION.compareTo(VIEW_HALLMARK)==0)
					{
						working=true;
						gephiGraph=(Gephi_graph)paramList.get(0);
						allHallmark hallmarkNodeList=(allHallmark)paramList.get(1);
						selectedHallmarkList=(ArrayList<String>)paramList.get(2);
						pathwayListScrollPane=(JScrollPane)paramList.get(3);
						
						statusLabel.setText("visualizing hallmark on current graph...");
						//uncolor all previous hallmarks
						gephiGraph.resetHallmarkNode();
						//color selected hallmarks
						System.out.println(selectedHallmarkList.toString());
						System.out.println("selectedHallmarkList VisualizeNetworkWorker:"+selectedHallmarkList.toString());
						gephiGraph.colorAndResizeHallmarkNode(hallmarkNodeList, selectedHallmarkList, true);
						pathwayListScrollPane.setVisible(true);
						
						sendVirtualClickToRefreshGraphDisplay();
						GEPHIGRAPHREADY=true;
						statusLabel.setText(HALLMARK_VIEW_COMPLETE);
						working=false;
					}
					if(ACTION.compareTo(EDIT_GRAPH)==0)
					{
						working=true;
						gephiGraph=(Gephi_graph)paramList.get(0);
						postgreDB=(postgreSQL)paramList.get(1);
						GUIwrapper=(java_GUIWrapper)paramList.get(2);
						speciesList=(JList<String>)paramList.get(3);
						allNodeArrList=(ArrayList<String>)paramList.get(4);

						statusLabel.setText("creating and visualizing graph...");
						//send virtual click to refresh screen
						sendVirtualClickToRefreshGraphDisplay();
						//Task 1: creating graph
						getGraphDataFromDB(null);
						createGraph();
						//Task 2: visualizing graph
						statusLabel.setText("visualizing graph...configuring interactive graph layout");
						gephiGraph.layoutInteractiveGraph_yifanhu();
						statusLabel.setText("visualizing graph...configuring interactive graph preview");
						gephiGraph.configureInteractivePreview();
						statusLabel.setText("visualizing graph...processing graph target");
						gephiGraph.visualizeGraph(interactiveGraphPanel);
						//Task 3: thicken edges 
						if(GRAPHKINETICSREADY)
						{
							ArrayList<edge> edgeWithKinetics=postgreDB.getKinetics_edgeList();
							statusLabel.setText("visualizing graph...processing kinetics edge information");
							System.out.println("thicken edges according to kinetics");
							gephiGraph.updateEdgeTable_kinetics(edgeWithKinetics);
						}

						statusLabel.setText("Graph visualized.");
						sendVirtualClickToRefreshGraphDisplay();
						speciesList=GUIwrapper.updateJListData(speciesList, allNodeArrList);
						interactiveWorkspace=gephiGraph.getWorkspace();
						working=false;
					}
					if(ACTION.compareTo(NEIGHBOURVIEW)==0)
					{
						working=true;
						selectedNode=(String)paramList.get(0);
						gephiGraph=(Gephi_graph)paramList.get(1);

						statusLabel.setText(String.format("Formatting neighbour graph display of selected node %s ...", selectedNode));
						createNeighbourGraph(selectedNode);
						gephiGraph.layoutInteractiveGraph_labelAdjust();
						statusLabel.setText("Neighbour graph of "+selectedNode+" visualized.");
						working=false;
					}
					if(ACTION.compareTo(GRAPHVIEW)==0)
					{
						working=true;
						selectedNode=(String)paramList.get(0);
						gephiGraph=(Gephi_graph)paramList.get(1);

						statusLabel.setText(String.format("Formatting whole graph display of selected node %s ...", selectedNode));
						createGraph();
						gephiGraph.layoutInteractiveGraph_yifanhu();
						statusLabel.setText("Graph visualized.");
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

	class PerformAnalysisWorker
	{
		Thread worker;
		boolean working;
		String ACTION;
		ArrayList<Object> paramList;
		
		PerformAnalysisWorker()
		{
			worker=new Thread(new Runnable()
			{
				private String getHallmarkNodeFileTitle(ArrayList<String> selectedHallmark)
				{
					String title="";
					for(int i=0; i<selectedHallmark.size(); i++)
					{
						title=title+selectedHallmark.get(i);
						if(i<selectedHallmark.size()-1)
							title=title+"_";
					}
					System.out.println("title:"+title);
					return title;
				}
				
				public void run()
				{
					if(ACTION.compareTo(EXPORT_HALLMARK_NODE_FILES)==0)
					{
						working=true;

						ArrayList<String> selectedHallmark=(ArrayList<String>)paramList.get(0);
						String outputDestination=(String)paramList.get(1);
						boolean exportIndividualHallmarkFiles=(boolean)paramList.get(2);
						boolean exportCombinedHallmarkFiles=(boolean)paramList.get(3);
						JList<String> speciesList=(JList<String>)paramList.get(4);
						postgreSQL postgreDB=(postgreSQL)paramList.get(5);
						String directory=(String)paramList.get(6);
						
						fReaderWriter writer=new fReaderWriter();

						if(exportIndividualHallmarkFiles)
						{
							ArrayList<String> speciesListNode=new ArrayList<String>();
							int speciesListSize=speciesList.getModel().getSize();
							for(int i=0; i<speciesListSize; i++)
								speciesListNode.add(speciesList.getModel().getElementAt(i));

							for(int i=0; i<selectedHallmark.size(); i++)
							{
								ArrayList<ArrayList<String>> nodeList=postgreDB.getHallmark_nodeListOfHallmark(selectedHallmark.get(i));
								ArrayList<ArrayList<String>> nodeListToWrite=new ArrayList<ArrayList<String>>();
								ArrayList<String> nodeName=new ArrayList<String>();
								ArrayList<String> nodeNameAll=new ArrayList<String>();
								for(int j=0; j<nodeList.size(); j++)
								{
									nodeName.add(nodeList.get(j).get(1));
									nodeNameAll.add(nodeList.get(j).get(1));
								}
								nodeName.retainAll(speciesListNode);
								for(int j=0; j<nodeName.size(); j++)
								{
									int index=nodeNameAll.indexOf(nodeName.get(j));
									nodeListToWrite.add(nodeList.get(index));
								}
								ArrayList<String> dataStream=new ArrayList<String>();
								for(int j=0; j<nodeListToWrite.size(); j++)
									dataStream.add(nodeListToWrite.get(j).get(0)+","+nodeListToWrite.get(j).get(1));
								//writer.writeToFileWithBufferedWriterArray(formatDirectoryString(outputDestination), selectedHallmark.get(i)+".txt", dataStream, true);
								writer.writeToFileWithBufferedWriterArray(directory, selectedHallmark.get(i)+".txt", dataStream, true);
							}
						}
						if(exportCombinedHallmarkFiles)
						{
							ArrayList<String> speciesListNode=new ArrayList<String>();
							int speciesListSize=speciesList.getModel().getSize();
							for(int i=0; i<speciesListSize; i++)
								speciesListNode.add(speciesList.getModel().getElementAt(i));

							ArrayList<ArrayList<String>> nodeList=postgreDB.getHallmark_nodeListOfHallmarkList(selectedHallmark);
							ArrayList<ArrayList<String>> nodeListToWrite=new ArrayList<ArrayList<String>>();
							ArrayList<String> nodeName=new ArrayList<String>();
							ArrayList<String> nodeNameAll=new ArrayList<String>();
							for(int j=0; j<nodeList.size(); j++)
							{
								nodeName.add(nodeList.get(j).get(1));
								nodeNameAll.add(nodeList.get(j).get(1));
							}
							nodeName.retainAll(speciesListNode);
							for(int j=0; j<nodeName.size(); j++)
							{
								int index=nodeNameAll.indexOf(nodeName.get(j));
								nodeListToWrite.add(nodeList.get(index));
							}
							ArrayList<String> dataStream=new ArrayList<String>();
							for(int j=0; j<nodeListToWrite.size(); j++)
								dataStream.add(nodeListToWrite.get(j).get(0)+","+nodeListToWrite.get(j).get(1));
							//writer.writeToFileWithBufferedWriterArray(formatDirectoryString(outputDestination), selectedHallmark.get(i)+".txt", dataStream, true);
							writer.writeToFileWithBufferedWriterArray(directory, getHallmarkNodeFileTitle(selectedHallmark)+".txt", dataStream, true);
						}
						working=false;
					}
					if(ACTION.compareTo(COMPUTE_TOPOLOGICAL_FEATURES)==0)
					{
						working=true;
						String IN_DEGREE=strConstants.getDBInDegree();
						String OUT_DEGREE=strConstants.getDBOutDegree();
						String TOTAL_DEGREE=strConstants.getDBTotalDegree();
						String EIGENVECTOR=strConstants.getDBEigenvector();
						String BETWEENNESS=strConstants.getDBBetweenness();
						String BRIDGING_COEFFICIENT=strConstants.getDBBridgingCoeff();
						String BRIDGING_CENTRALITY=strConstants.getDBBridgingCentrality();
						String UNDIRECTED_CLUSTERING_COEFFICIENT=strConstants.getDBUndirClustering();
						String IN_CLUSTERING_COEFFICIENT=strConstants.getDBInClustering();
						String OUT_CLUSTERING_COEFFICIENT=strConstants.getDBOutClustering();
						String CYCLE_CLUSTERING_COEFFICIENT=strConstants.getDBCycClustering();
						String MIDDLEMAN_CLUSTERING_COEFFICIENT=strConstants.getDBMidClustering();
						ArrayList<String> selectedFeatures=(ArrayList<String>)paramList.get(0);

						if(selectedFeatures.size()>0)
						{
							if(selectedFeatures.contains(IN_DEGREE) || selectedFeatures.contains(OUT_DEGREE) || selectedFeatures.contains(TOTAL_DEGREE))
							{
								statusLabel.setText("computing features...degree centrality");
								jungGraph.getDegree(statusLabel, currPathwayIsWholeNetwork());
							}
							if(selectedFeatures.contains(EIGENVECTOR))
							{
								statusLabel.setText("computing features...eigenvector centrality");
								jungGraph.getEigenvector(statusLabel, currPathwayIsWholeNetwork());
							}
							if(selectedFeatures.contains(BETWEENNESS))
							{
								statusLabel.setText("computing features...betweenness centrality");
								jungGraph.getBetweenness(statusLabel, currPathwayIsWholeNetwork());
							}
							if(selectedFeatures.contains(BRIDGING_COEFFICIENT))
							{
								statusLabel.setText("computing features...bridging coefficient");
								jungGraph.getBridgingCoeff(statusLabel, currPathwayIsWholeNetwork());
							}
							if(selectedFeatures.contains(BRIDGING_CENTRALITY))
							{
								statusLabel.setText("computing features...bridging centrality");
								jungGraph.getBridgingCentrality(statusLabel, currPathwayIsWholeNetwork());
							}
							if(selectedFeatures.contains(UNDIRECTED_CLUSTERING_COEFFICIENT))
							{
								statusLabel.setText("computing features...undirected clustering coefficient");
								jungGraph.getUndirectedClusteringCoefficient(statusLabel, currPathwayIsWholeNetwork());
							}
							if(selectedFeatures.contains(IN_CLUSTERING_COEFFICIENT))
							{
								statusLabel.setText("computing features...in clustering coefficient");
								jungGraph.getInClusteringCoefficient(statusLabel, currPathwayIsWholeNetwork());
							}
							if(selectedFeatures.contains(OUT_CLUSTERING_COEFFICIENT))
							{
								statusLabel.setText("computing features...out clustering coefficient");
								jungGraph.getOutClusteringCoefficient(statusLabel, currPathwayIsWholeNetwork());
							}
							if(selectedFeatures.contains(CYCLE_CLUSTERING_COEFFICIENT) || selectedFeatures.contains(MIDDLEMAN_CLUSTERING_COEFFICIENT))
							{
								statusLabel.setText("computing features...cycle and middleman clustering coefficient");
								jungGraph.getCycleAndMiddlemanClusteringCoefficient(statusLabel, currPathwayIsWholeNetwork());
							}
							if(currPathwayIsWholeNetwork())
								postgreDB.updateNodeRanks(selectedFeatures, statusLabel);
							else
								postgreDB.updatePathwayNodeRanks(selectedFeatures, statusLabel);
						}
						statusLabel.setText("finish computing features");
						menuItemOptionFeatureResult.setEnabled(true);
						working=false;
					}
					if(ACTION.compareTo(VIEW_FEATURE_RESULT)==0)
					{
						working=true;
						ArrayList<String> selectedFeatures=(ArrayList<String>)paramList.get(0);
						ArrayList<String> nList=(ArrayList<String>)paramList.get(1);
						postgreSQL postgreDB=(postgreSQL)paramList.get(2);
						JLabel statusLabel=(JLabel)paramList.get(3);
						boolean wholeNetwork=(boolean)paramList.get(4);
						visualizeFeatureResultDialog visualizeFeatureResult=new visualizeFeatureResultDialog(selectedFeatures, nList, postgreDB, statusLabel, currPathwayIsWholeNetwork());
						visualizeFeatureResult.setVisible(true);
						statusLabel.setText("finish formatting display for topological feature computation results");
						working=false;
					}
					if(ACTION.compareTo(CHARACTERIZE_HALLMARKS)==0)
					{
						working=true;
						int maxFold=10;
						ArrayList<String> uncomputedFeatures=(ArrayList<String>)paramList.get(0);
						ArrayList<String> selectedFeatures=(ArrayList<String>)paramList.get(1);
						ArrayList<String> selectedHallmarks=(ArrayList<String>)paramList.get(2);
						int wmcWt=(int)paramList.get(3);

						if(uncomputedFeatures.size()>0)
						{
							statusLabel.setText("Characterizing hallmarks...computing uncomputed features");
							postgreDB.resetComputedFeature_subset(uncomputedFeatures);
							int totalCount=uncomputedFeatures.size();
							for(int i=0; i<totalCount; i++)
							{
								double percentComplete=100.0*i/(totalCount-1);
								DecimalFormat df = new DecimalFormat();
								df.setMaximumFractionDigits(2);
								statusLabel.setText("Characterizing hallmarks...computing uncomputed features ["+df.format(percentComplete)+"%]");
								computeNetworkTopologicalFeature(uncomputedFeatures.get(i));
							}
						}
						statusLabel.setText("Characterizing hallmarks...partitioning data - getting hallmark nodes");
						ArrayList<ArrayList<String>> hallmarkNodes=new ArrayList<ArrayList<String>>();
						hallmarkNodes=postgreDB.getHallmark_nodeNameIndividualListOfSelectedHallmarks(selectedHallmarks);
						statusLabel.setText("Characterizing hallmarks...partitioning data - computing number of partitions");
						ArrayList<Integer> numOfFolds=new ArrayList<Integer>(); //ensure each fold has at least one target
						for(int i=0; i<hallmarkNodes.size(); i++)
						{
							if(hallmarkNodes.get(i).size()<10)
								numOfFolds.add(hallmarkNodes.get(i).size());
							else
								numOfFolds.add(maxFold);
						}
						int totalCount=selectedHallmarks.size();
						for(int i=0; i<totalCount; i++)
						{
							double percentComplete=100.0*i/(totalCount-1);
							if(totalCount==1)
								percentComplete=100;
							DecimalFormat df = new DecimalFormat();
							df.setMaximumFractionDigits(2);
							statusLabel.setText("Characterizing hallmarks...for each hallmark ["+df.format(percentComplete)+"%] generate fold partitions for cross validation");
							int thisHallmarkMaxFold=numOfFolds.get(i);
							ArrayList<String> thisHallmarkHallmarkNodes=hallmarkNodes.get(i);
							ArrayList<String> allNodes=new ArrayList<String>();
							for(int j=0; j<allNodeArrList.size(); j++)
								allNodes.add(allNodeArrList.get(j));
							crossValidationData xValidationData=new crossValidationData(thisHallmarkMaxFold, thisHallmarkHallmarkNodes, allNodes);
							int tuneLvl=1;//tuneLvl=3;
							statusLabel.setText("Characterizing hallmarks...for each hallmark ["+df.format(percentComplete)+"%] create and initialize SVM");
							svmAnalysis svmObj=new svmAnalysis();
							svmObj.initializeSVM(selectedHallmarks.get(i), thisHallmarkMaxFold, xValidationData, null, null, selectedFeatures, thisHallmarkHallmarkNodes, statusLabel, String.valueOf(df.format(percentComplete)));
							statusLabel.setText("Characterizing hallmarks...for each hallmark ["+df.format(percentComplete)+"%] SVM performing training and feature selection (backward stepwise elimination)");
							if(wmcWt==0)
								svmObj.BSEModelSelection(tuneLvl, false, wmcWt, strConstants.getLinear(), directory, directory+"svmTraining"+folder);
							else
								svmObj.BSEModelSelection(tuneLvl, true, wmcWt, strConstants.getLinear(), directory, directory+"svmTraining"+folder);
							//computeNetworkTopologicalFeature(uncomputedFeatures.get(i));
						}
						statusLabel.setText("finish characterizing hallmarks");
						menuItemOptionCharacterizationResult.setEnabled(true);
						working=false;
					}
					if(ACTION.compareTo(OPEN_NETWORK_DATA_FILE)==0)
					{
						working=true;
						String networkDataFileName=(String)paramList.get(0);

						processNetworkDataFile(networkDataFileName);
						statusLabel.setText("Finish processing network data files.");
						working=false;
					}
					if(ACTION.compareTo(OPEN_TOPOLOGY_FILE)==0)
					{
						working=true;
						String topologyFileName=(String)paramList.get(0);

						processTopologyFile(topologyFileName);
						processViewFile();
						statusLabel.setText("Finish processing ".concat(topologyFileName));
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



