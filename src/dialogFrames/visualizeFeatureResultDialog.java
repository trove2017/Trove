package dialogFrames;

//Imports are listed in full to show what's being used could just import javax.swing.* and java.awt.* etc..
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JViewport;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.DefaultTableModel;

import constants.stringConstant;
import database.postgreSQL;

@SuppressWarnings("serial")
//	Layout: 
//  |  - settingPanel                                             
//  |    + resultPanel (resultTable, resultScrollPane)                                       
//  |  - buttonPanel
//  |    + displayLabel, displayComboBox, updateButton, cancelButton

public class visualizeFeatureResultDialog extends JDialog{
	private JPanel settingPanel, buttonPanel, featureSelectSettingPanel, featureSelectButtonPanel;
	private JComboBox<String> displayComboBox, featureComboBox, orderComboBox;
	private JCheckBox inDegreeCheckBox, outDegreeCheckBox, totalDegreeCheckBox, eigenvectorCheckBox, betweennessCheckBox;
	private JCheckBox bridgingCoeffCheckBox, bridgingCentralityCheckBox, undirectedClusteringCoeffCheckBox, inClusteringCoeffCheckBox;
	private JCheckBox outClusteringCoeffCheckBox, cycleClusteringCoeffCheckBox, middlemanClusteringCoeffCheckBox, targetDownstreamEffectCheckBox;
	private JButton updateButton, doneButton, selectButton, cancelButton;
    private DefaultTableModel resultTableModel, fixedTableModel;
    private JTable resultTable, fixedTable;
    private JScrollPane resultTableScrollPane;
    private JDialog featureSelectDialog;
	private String CANCEL;
	private stringConstant staticString=new stringConstant();
	protected final static String UPDATE = "Update table";
	protected final static String SELECT = "Select feature";
	protected final static String DONE = "Done";
	private String currDisplay, currOrderByFeature, currOrder;
	private String RAW_VALUE, RANK, NODE, ASCENDING, DESCENDING;
	private JLabel status;
	private ArrayList<String> features;
	private ArrayList<String> nodeList;
	private postgreSQL DB;
	private boolean WHOLE_NETWORK;
	
	private String FEATURE_IN_DEGREE_DB;
	private String FEATURE_OUT_DEGREE_DB;
	private String FEATURE_TOTAL_DEGREE_DB;
	private String FEATURE_EIGENVECTOR_DB;
	private String FEATURE_BETWEENNESS_DB;
	private String FEATURE_BRIDGING_COEFF_DB;
	private String FEATURE_BRIDGING_CENTRALITY_DB;
	private String FEATURE_UNDIRECTED_CLUSTERING_COEFFICIENT_DB;
	private String FEATURE_IN_CLUSTERING_COEFFICIENT_DB;
	private String FEATURE_OUT_CLUSTERING_COEFFICIENT_DB;
	private String FEATURE_CYCLE_CLUSTERING_COEFFICIENT_DB;
	private String FEATURE_MIDDLEMAN_CLUSTERING_COEFFICIENT_DB;
	private String FEATURE_TARGET_DOWNSTREAM_EFFECT_DB;
	
	private String FEATURE_IN_DEGREE_GUI;
	private String FEATURE_OUT_DEGREE_GUI;
	private String FEATURE_TOTAL_DEGREE_GUI;
	private String FEATURE_EIGENVECTOR_GUI;
	private String FEATURE_BETWEENNESS_GUI;
	private String FEATURE_BRIDGING_COEFF_GUI;
	private String FEATURE_BRIDGING_CENTRALITY_GUI;
	private String FEATURE_UNDIRECTED_CLUSTERING_COEFFICIENT_GUI;
	private String FEATURE_IN_CLUSTERING_COEFFICIENT_GUI;
	private String FEATURE_OUT_CLUSTERING_COEFFICIENT_GUI;
	private String FEATURE_CYCLE_CLUSTERING_COEFFICIENT_GUI;
	private String FEATURE_MIDDLEMAN_CLUSTERING_COEFFICIENT_GUI;
	private String FEATURE_TARGET_DOWNSTREAM_EFFECT_GUI;
	
	public visualizeFeatureResultDialog(ArrayList<String> selectedFeature, ArrayList<String> nList, 
			postgreSQL database, JLabel s, boolean isWholeNetwork)
	{
		//initialize constants
		CANCEL=staticString.getCancel();
		
		RAW_VALUE="Raw Value";
		RANK="Rank";
		NODE="Node";
		ASCENDING="Ascending";
		DESCENDING="Descending";
		FEATURE_IN_DEGREE_GUI=staticString.getInDegree();
		FEATURE_OUT_DEGREE_GUI=staticString.getOutDegree();
		FEATURE_TOTAL_DEGREE_GUI=staticString.getTotalDegree();
		FEATURE_EIGENVECTOR_GUI=staticString.getEigenvector();
		FEATURE_BETWEENNESS_GUI=staticString.getBetweenness();
		FEATURE_BRIDGING_COEFF_GUI=staticString.getBridgingCoeff();
		FEATURE_BRIDGING_CENTRALITY_GUI=staticString.getBridgingCentrality();
		FEATURE_UNDIRECTED_CLUSTERING_COEFFICIENT_GUI=staticString.getUndirClustering();
		FEATURE_IN_CLUSTERING_COEFFICIENT_GUI=staticString.getInClustering();
		FEATURE_OUT_CLUSTERING_COEFFICIENT_GUI=staticString.getOutClustering();
		FEATURE_CYCLE_CLUSTERING_COEFFICIENT_GUI=staticString.getCycClustering();
		FEATURE_MIDDLEMAN_CLUSTERING_COEFFICIENT_GUI=staticString.getMidClustering();
		FEATURE_TARGET_DOWNSTREAM_EFFECT_GUI=staticString.getTDE();
		
		FEATURE_IN_DEGREE_DB=staticString.getDBInDegree();
		FEATURE_OUT_DEGREE_DB=staticString.getDBOutDegree();
		FEATURE_TOTAL_DEGREE_DB=staticString.getDBTotalDegree();
		FEATURE_EIGENVECTOR_DB=staticString.getDBEigenvector();
		FEATURE_BETWEENNESS_DB=staticString.getDBBetweenness();
		FEATURE_BRIDGING_COEFF_DB=staticString.getDBBridgingCoeff();
		FEATURE_BRIDGING_CENTRALITY_DB=staticString.getDBBridgingCentrality();
		FEATURE_UNDIRECTED_CLUSTERING_COEFFICIENT_DB=staticString.getDBUndirClustering();
		FEATURE_IN_CLUSTERING_COEFFICIENT_DB=staticString.getDBInClustering();
		FEATURE_OUT_CLUSTERING_COEFFICIENT_DB=staticString.getDBOutClustering();
		FEATURE_CYCLE_CLUSTERING_COEFFICIENT_DB=staticString.getDBCycClustering();
		FEATURE_MIDDLEMAN_CLUSTERING_COEFFICIENT_DB=staticString.getDBMidClustering();
		FEATURE_TARGET_DOWNSTREAM_EFFECT_DB=staticString.getDBTDE();
		//initialize variables
		features=selectedFeature;
		nodeList=nList;
		DB=database;
		status=s;
		WHOLE_NETWORK=isWholeNetwork;
		//display list of features users can select from
		if(WHOLE_NETWORK && selectedFeature.size()==0)
		{
			initializeFeatureSelectComponents();
			featureSelectDialog=new JDialog();
			featureSelectDialog.setTitle("Select Results of Features to View");
			featureSelectDialog.setSize(300,500);
			featureSelectDialog.setLocationRelativeTo(null);
			featureSelectDialog.setResizable(false);
			featureSelectDialog.setModal(true);
			featureSelectDialog.setLayout(new BoxLayout(featureSelectDialog.getContentPane(), BoxLayout.Y_AXIS));
			featureSelectDialog.add(featureSelectSettingPanel);
			featureSelectDialog.add(featureSelectButtonPanel);
			featureSelectDialog.setVisible(true);
		}
		
		//set JDialog properties
		setTitle("Results of Topological Features Computation");
		setSize(740,550);
		setLocationRelativeTo(null);
		setLayout(new BorderLayout());
		setResizable(true);
		setModal(false);

		//configure components in JDialog
		System.out.println(features.toString());
		initializeComponents();
	}
	
	private void initializeFeatureSelectComponents()
	{
		featureSelectSettingPanel=new JPanel();
		featureSelectButtonPanel=new JPanel();
		
		//create topologicalPanel
		JPanel inDegreePanel=new JPanel();
		inDegreePanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		inDegreeCheckBox=new JCheckBox(FEATURE_IN_DEGREE_GUI);
		inDegreePanel.add(inDegreeCheckBox);
		JPanel outDegreePanel=new JPanel();
		outDegreePanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		outDegreeCheckBox=new JCheckBox(FEATURE_OUT_DEGREE_GUI);
		outDegreePanel.add(outDegreeCheckBox);
		JPanel totalDegreePanel=new JPanel();
		totalDegreePanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		totalDegreeCheckBox=new JCheckBox(FEATURE_TOTAL_DEGREE_GUI);
		totalDegreePanel.add(totalDegreeCheckBox);
		JPanel eigenvectorPanel=new JPanel();
		eigenvectorPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		eigenvectorCheckBox=new JCheckBox(FEATURE_EIGENVECTOR_GUI);
		eigenvectorPanel.add(eigenvectorCheckBox);
		JPanel betweennessPanel=new JPanel();
		betweennessPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		betweennessCheckBox=new JCheckBox(FEATURE_BETWEENNESS_GUI);
		betweennessPanel.add(betweennessCheckBox);
		JPanel bridgingCoeffPanel=new JPanel();
		bridgingCoeffPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		bridgingCoeffCheckBox=new JCheckBox(FEATURE_BRIDGING_COEFF_GUI);
		bridgingCoeffPanel.add(bridgingCoeffCheckBox);
		JPanel bridgingCentralityPanel=new JPanel();
		bridgingCentralityPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		bridgingCentralityCheckBox=new JCheckBox(FEATURE_BRIDGING_CENTRALITY_GUI);
		bridgingCentralityPanel.add(bridgingCentralityCheckBox);
		JPanel undirectedClusteringCoeffPanel=new JPanel();
		undirectedClusteringCoeffPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		undirectedClusteringCoeffCheckBox=new JCheckBox(FEATURE_UNDIRECTED_CLUSTERING_COEFFICIENT_GUI);
		undirectedClusteringCoeffPanel.add(undirectedClusteringCoeffCheckBox);
		JPanel inClusteringCoeffPanel=new JPanel();
		inClusteringCoeffPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		inClusteringCoeffCheckBox=new JCheckBox(FEATURE_IN_CLUSTERING_COEFFICIENT_GUI);
		inClusteringCoeffPanel.add(inClusteringCoeffCheckBox);
		JPanel outClusteringCoeffPanel=new JPanel();
		outClusteringCoeffPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		outClusteringCoeffCheckBox=new JCheckBox(FEATURE_OUT_CLUSTERING_COEFFICIENT_GUI);
		outClusteringCoeffPanel.add(outClusteringCoeffCheckBox);
		JPanel cycleClusteringCoeffPanel=new JPanel();
		cycleClusteringCoeffPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		cycleClusteringCoeffCheckBox=new JCheckBox(FEATURE_CYCLE_CLUSTERING_COEFFICIENT_GUI);
		cycleClusteringCoeffPanel.add(cycleClusteringCoeffCheckBox);
		JPanel middlemanClusteringCoeffPanel=new JPanel();
		middlemanClusteringCoeffPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		middlemanClusteringCoeffCheckBox=new JCheckBox(FEATURE_MIDDLEMAN_CLUSTERING_COEFFICIENT_GUI);
		middlemanClusteringCoeffPanel.add(middlemanClusteringCoeffCheckBox);
		JPanel targetDownstreamEffectPanel=new JPanel();
		targetDownstreamEffectPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		targetDownstreamEffectCheckBox=new JCheckBox(FEATURE_TARGET_DOWNSTREAM_EFFECT_GUI);
		targetDownstreamEffectPanel.add(targetDownstreamEffectCheckBox);
		
		featureSelectSettingPanel.setLayout(new BoxLayout(featureSelectSettingPanel, BoxLayout.Y_AXIS));
		if(DB.getFeature_featureComputed(FEATURE_IN_DEGREE_DB)==0)
		{
			inDegreeCheckBox.setSelected(true);
			featureSelectSettingPanel.add(inDegreePanel);
		}
		if(DB.getFeature_featureComputed(FEATURE_OUT_DEGREE_DB)==0)
		{
			outDegreeCheckBox.setSelected(true);
			featureSelectSettingPanel.add(outDegreePanel);
		}
		if(DB.getFeature_featureComputed(FEATURE_TOTAL_DEGREE_DB)==0)
		{
			totalDegreeCheckBox.setSelected(true);
			featureSelectSettingPanel.add(totalDegreePanel);
		}
		if(DB.getFeature_featureComputed(FEATURE_EIGENVECTOR_DB)==0)
		{
			eigenvectorCheckBox.setSelected(true);
			featureSelectSettingPanel.add(eigenvectorPanel);
		}
		if(DB.getFeature_featureComputed(FEATURE_BETWEENNESS_DB)==0)
		{
			betweennessCheckBox.setSelected(true);
			featureSelectSettingPanel.add(betweennessPanel);
		}
		if(DB.getFeature_featureComputed(FEATURE_BRIDGING_COEFF_DB)==0)
		{
			bridgingCoeffCheckBox.setSelected(true);
			featureSelectSettingPanel.add(bridgingCoeffPanel);
		}
		if(DB.getFeature_featureComputed(FEATURE_BRIDGING_CENTRALITY_DB)==0)
		{
			bridgingCentralityCheckBox.setSelected(true);
			featureSelectSettingPanel.add(bridgingCentralityPanel);
		}
		if(DB.getFeature_featureComputed(FEATURE_UNDIRECTED_CLUSTERING_COEFFICIENT_DB)==0)
		{
			undirectedClusteringCoeffCheckBox.setSelected(true);
			featureSelectSettingPanel.add(undirectedClusteringCoeffPanel);
		}
		if(DB.getFeature_featureComputed(FEATURE_IN_CLUSTERING_COEFFICIENT_DB)==0)
		{
			inClusteringCoeffCheckBox.setSelected(true);
			featureSelectSettingPanel.add(inClusteringCoeffPanel);
		}
		if(DB.getFeature_featureComputed(FEATURE_OUT_CLUSTERING_COEFFICIENT_DB)==0)
		{
			outClusteringCoeffCheckBox.setSelected(true);
			featureSelectSettingPanel.add(outClusteringCoeffPanel);
		}
		if(DB.getFeature_featureComputed(FEATURE_CYCLE_CLUSTERING_COEFFICIENT_DB)==0)
		{
			cycleClusteringCoeffCheckBox.setSelected(true);
			featureSelectSettingPanel.add(cycleClusteringCoeffPanel);
		}
		if(DB.getFeature_featureComputed(FEATURE_MIDDLEMAN_CLUSTERING_COEFFICIENT_DB)==0)
		{
			middlemanClusteringCoeffCheckBox.setSelected(true);
			featureSelectSettingPanel.add(middlemanClusteringCoeffPanel);
		}
		if(DB.getFeature_featureComputed(FEATURE_TARGET_DOWNSTREAM_EFFECT_DB)==0)
		{
			targetDownstreamEffectCheckBox.setSelected(true);
			featureSelectSettingPanel.add(targetDownstreamEffectPanel);
		}
		//create buttonPanel
		cancelButton= new JButton();
		cancelButton.setSize(50, 20);
		cancelButton.setText(CANCEL);
		cancelButton.setActionCommand(CANCEL);
		cancelButton.addActionListener(actionListener);

		selectButton = new JButton();
		selectButton.setSize(50, 20);
		selectButton.setText(SELECT);
		selectButton.setActionCommand(SELECT);
		selectButton.addActionListener(actionListener);

		featureSelectButtonPanel= new JPanel();
		featureSelectButtonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
		featureSelectButtonPanel.add(selectButton);
		featureSelectButtonPanel.add(cancelButton);

		setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));
		add(featureSelectSettingPanel);
		add(featureSelectButtonPanel);
	}

	private ActionListener actionListener = new ActionListener() 
	{
		public void actionPerformed(ActionEvent act) {
			if (act.getActionCommand() == UPDATE)
				update_actionPerformed();
			if (act.getActionCommand() == DONE)
				done_actionPerformed();
			if (act.getActionCommand() == SELECT)
				select_actionPerformed();
			if (act.getActionCommand() == CANCEL)
				cancel_actionPerformed();
		}
	};

	private void select_actionPerformed() {
		features=new ArrayList<String>();
		if(inDegreeCheckBox.isSelected())
			features.add(FEATURE_IN_DEGREE_DB);
		if(outDegreeCheckBox.isSelected())
			features.add(FEATURE_OUT_DEGREE_DB);
		if(totalDegreeCheckBox.isSelected())
			features.add(FEATURE_TOTAL_DEGREE_DB);
		if(eigenvectorCheckBox.isSelected())
			features.add(FEATURE_EIGENVECTOR_DB);
		if(betweennessCheckBox.isSelected())
			features.add(FEATURE_BETWEENNESS_DB);
		if(bridgingCoeffCheckBox.isSelected())
			features.add(FEATURE_BRIDGING_COEFF_DB);
		if(bridgingCentralityCheckBox.isSelected())
			features.add(FEATURE_BRIDGING_CENTRALITY_DB);
		if(undirectedClusteringCoeffCheckBox.isSelected())
			features.add(FEATURE_UNDIRECTED_CLUSTERING_COEFFICIENT_DB);
		if(inClusteringCoeffCheckBox.isSelected())
			features.add(FEATURE_IN_CLUSTERING_COEFFICIENT_DB);
		if(outClusteringCoeffCheckBox.isSelected())
			features.add(FEATURE_OUT_CLUSTERING_COEFFICIENT_DB);
		if(cycleClusteringCoeffCheckBox.isSelected())
			features.add(FEATURE_CYCLE_CLUSTERING_COEFFICIENT_DB);
		if(middlemanClusteringCoeffCheckBox.isSelected())
			features.add(FEATURE_MIDDLEMAN_CLUSTERING_COEFFICIENT_DB);
		if(targetDownstreamEffectCheckBox.isSelected())
			features.add(FEATURE_TARGET_DOWNSTREAM_EFFECT_DB);
		featureSelectDialog.setVisible(false);
	}
	
	private String[] createHeader(ArrayList<String> feature)
	{
		String[] header=new String[feature.size()];
		int columnCount=0;
		float percentComplete;
		int totalCount=feature.size();
		for(int i=0; i<totalCount; i++)
		{
			percentComplete=(float)(100.0*i/(totalCount-1));
			DecimalFormat df = new DecimalFormat();
			df.setMaximumFractionDigits(2);
			status.setText("formatting display of topological features computation results...generating table header ["+df.format(percentComplete)+"%] ");
			if(feature.contains(feature.get(i)))
				header[columnCount++]=feature.get(i);
		}
		return header;
	}
	
	private String[][] createData(ArrayList<String> nodeList, ArrayList<String> feature, boolean retrieveData, String type, ArrayList<String> orderedNodeList)
	{
		ArrayList<String> nList=new ArrayList<String>();
		if(orderedNodeList!=null && orderedNodeList.size()>0)
			nList=orderedNodeList;
		else
			nList=nodeList;
		String[][] data=new String[nList.size()][feature.size()];
		float percentComplete;
		int totalCount=nList.size();
		for(int x=0; x<totalCount; x++)
		{
			percentComplete=(float)(100.0*x/(totalCount-1));
			DecimalFormat df = new DecimalFormat();
			df.setMaximumFractionDigits(2);
			status.setText("formatting display of topological features computation results...generating feature values ["+df.format(percentComplete)+"%] ");
			String node=nList.get(x);
			int columnCount=0;
			ArrayList<String> valueList=new ArrayList<String>();
			int nodeDBID=DB.getNodeDBID(node);
			if(WHOLE_NETWORK)
			{
				if(type.compareTo(RAW_VALUE)==0)
					valueList=DB.get_multipleFeatures(DB.getNodeID(nodeDBID), DB.getNodeName(nodeDBID), feature);
				else
					valueList=DB.get_multipleFeatureRanks(DB.getNodeID(nodeDBID), DB.getNodeName(nodeDBID), feature);
			}
			else
			{
				if(type.compareTo(RAW_VALUE)==0)
					valueList=DB.get_multiplePathwayFeatures(DB.getNodeID(nodeDBID), DB.getNodeName(nodeDBID), feature);
				else
					valueList=DB.get_multiplePathwayFeatureRanks(DB.getNodeID(nodeDBID), DB.getNodeName(nodeDBID), feature);
			}
			//System.out.println(valueList.toString());
			for(int i=0; i<valueList.size(); i++)
				data[x][columnCount++]=valueList.get(i);
		}
		return data;
	}
	
	private String[][] createDataFixed(ArrayList<String> nodeList, ArrayList<String> orderedNodeList)
	{
		ArrayList<String> nList=new ArrayList<String>();
		if(orderedNodeList!=null && orderedNodeList.size()>0)
			nList=orderedNodeList;
		else
			nList=nodeList;
		String[][] data=new String[nList.size()][1];
		float percentComplete;
		int totalCount=nList.size();
		for(int x=0; x<totalCount; x++)
		{
			percentComplete=(float)(100.0*x/(totalCount-1));
			DecimalFormat df = new DecimalFormat();
			df.setMaximumFractionDigits(2);
			status.setText("formatting display of topological features computation results...generating node column ["+df.format(percentComplete)+"%] ");
			String node=nList.get(x);
			data[x][0]=node;
		}
		return data;
	}
	
	private void checkSelection(boolean isFixedTable) 
	{
		int fixedSelectedIndex = fixedTable.getSelectedRow();
	    int selectedIndex = resultTable.getSelectedRow();
	    if (fixedSelectedIndex != selectedIndex) 
	    {
	    	if (isFixedTable) 
	    		resultTable.setRowSelectionInterval(fixedSelectedIndex, fixedSelectedIndex);
	        else 
	        	fixedTable.setRowSelectionInterval(selectedIndex, selectedIndex);
	    }
	    fixedTable.invalidate();
	    resultTable.invalidate();
	}

	private void createTable(boolean retrieveData, boolean reorderData, String display, String orderByFeature, String order)
	{
		ArrayList<String> orderedNodeList=new ArrayList<String>();
		if(WHOLE_NETWORK)
		{
			if(orderByFeature.compareTo(NODE)!=0)
			{
				if(display.compareTo(RANK)==0)
				{
					if(order.compareTo(ASCENDING)==0)
						orderedNodeList=DB.getNode_orderByFeatureRankAsc(orderByFeature);
					else
						orderedNodeList=DB.getNode_orderByFeatureRankDesc(orderByFeature);
				}
				else
				{
					if(order.compareTo(ASCENDING)==0)
						orderedNodeList=DB.getNode_orderByFeatureAsc(orderByFeature);
					else
						orderedNodeList=DB.getNode_orderByFeatureDesc(orderByFeature);
				}
			}
			else
			{
				if(order.compareTo(ASCENDING)==0)
					orderedNodeList=DB.getNode_orderByNodeAsc();
				else
					orderedNodeList=DB.getNode_orderByNodeDesc();
			}
		}
		else
		{
			if(orderByFeature.compareTo(NODE)!=0)
			{
				if(display.compareTo(RANK)==0)
				{
					if(order.compareTo(ASCENDING)==0)
						orderedNodeList=DB.getNode_orderByPathwayFeatureRankAsc(orderByFeature);
					else
						orderedNodeList=DB.getNode_orderByPathwayFeatureRankDesc(orderByFeature);
				}
				else
				{
					if(order.compareTo(ASCENDING)==0)
						orderedNodeList=DB.getNode_orderByPathwayFeatureAsc(orderByFeature);
					else
						orderedNodeList=DB.getNode_orderByPathwayFeatureDesc(orderByFeature);
				}
			}
			else
			{
				if(order.compareTo(ASCENDING)==0)
					orderedNodeList=DB.getNode_orderByPathwayNodeAsc();
				else
					orderedNodeList=DB.getNode_orderByPathwayNodeDesc();
			}
		}
		String[] header = createHeader(features);
		String[][] data = createData(nodeList, features, retrieveData, display, orderedNodeList);
		resultTableModel = new DefaultTableModel(data, header);
		String[] headerFixed = {"Node"};
		String[][] dataFixed = createDataFixed(nodeList, orderedNodeList);
		fixedTableModel = new DefaultTableModel(dataFixed, headerFixed);
		fixedTable.setModel(fixedTableModel);
		resultTable.setModel(resultTableModel);
		fixedTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		resultTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		fixedTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		resultTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		/*if(orderByFeature.compareTo(NODE)==0)
		{
			try {
				resultTable.setAutoCreateRowSorter(true);
				fixedTable.setAutoCreateRowSorter(true);
			} catch(Exception continuewithNoSort) {
			}
		}*/
		resultTableScrollPane = new JScrollPane(resultTable);
	    JViewport viewport = new JViewport();
	    viewport.setView(fixedTable);
	    fixedTable.setPreferredSize(new Dimension(100, 500));
	    viewport.setPreferredSize(fixedTable.getPreferredSize());
	    resultTableScrollPane.setRowHeaderView(viewport);
	    resultTableScrollPane.setCorner(JScrollPane.UPPER_LEFT_CORNER, fixedTable.getTableHeader());
	    resultTableScrollPane.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		settingPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
	    settingPanel.add(resultTableScrollPane);
	}
	
	private void initializeComponents()
	{
		settingPanel=new JPanel();
		buttonPanel=new JPanel();
		
		//create buttonPanel
		JLabel displayLabel=new JLabel("Display");
		displayComboBox=new JComboBox<String>();
		String[] displayOption=new String[2];
		displayOption[0]=RAW_VALUE;
		displayOption[1]=RANK;
		DefaultComboBoxModel<String> displayComboBoxModel = new DefaultComboBoxModel<String>(displayOption);
		displayComboBox.setModel(displayComboBoxModel);
		displayComboBox.setSelectedIndex(0);
		currDisplay=RAW_VALUE;
		JLabel orderByLabel=new JLabel("Order by");
		featureComboBox=new JComboBox<String>();
		String[] featureOption=new String[features.size()+1];
		featureOption[0]=NODE;
		for(int i=1; i<=features.size(); i++)
			featureOption[i]=features.get(i-1);
		DefaultComboBoxModel<String> featureComboBoxModel = new DefaultComboBoxModel<String>(featureOption);
		featureComboBox.setModel(featureComboBoxModel);
		featureComboBox.setSelectedIndex(0);
		currOrderByFeature=NODE;
		orderComboBox=new JComboBox<String>();
		String[] orderOption=new String[features.size()+1];
		orderOption[0]=ASCENDING;
		orderOption[1]=DESCENDING;
		DefaultComboBoxModel<String> orderComboBoxModel = new DefaultComboBoxModel<String>(orderOption);
		orderComboBox.setModel(orderComboBoxModel);
		orderComboBox.setSelectedIndex(0);
		currOrder=ASCENDING;
		//create resultPanel
		fixedTable = new JTable() {
			public void valueChanged(ListSelectionEvent e) {
		        super.valueChanged(e);
		        checkSelection(true);
		    }
		};
		resultTable = new JTable(){
			public void valueChanged(ListSelectionEvent e) {
				super.valueChanged(e);
		        checkSelection(false);
		    }
		};
		createTable(true, true, currDisplay, currOrderByFeature, currOrder);
			   
		doneButton= new JButton();
		doneButton.setSize(50, 20);
		doneButton.setText(DONE);
		doneButton.setActionCommand(DONE);
		doneButton.addActionListener(actionListener);

		updateButton = new JButton();
		updateButton.setSize(50, 20);
		updateButton.setText(UPDATE);
		updateButton.setActionCommand(UPDATE);
		updateButton.addActionListener(actionListener);

		buttonPanel= new JPanel();
		buttonPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		buttonPanel.add(displayLabel);
		buttonPanel.add(displayComboBox);
		buttonPanel.add(orderByLabel);
		buttonPanel.add(featureComboBox);
		buttonPanel.add(orderComboBox);
		buttonPanel.add(updateButton);
		buttonPanel.add(doneButton);

		setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));
		add(settingPanel);
		add(buttonPanel);
	}

	private void cancel_actionPerformed(){
		featureSelectDialog.setVisible(false);
		setVisible(false);
	}
	
	private void done_actionPerformed(){
		setVisible(false);
	}
	
	private void update_actionPerformed(){
		if(currDisplay.compareTo(displayComboBox.getSelectedItem().toString())!=0 ||
				currOrderByFeature.compareTo(featureComboBox.getSelectedItem().toString())!=0 ||
				currOrder.compareTo(orderComboBox.getSelectedItem().toString())!=0)
		{
			settingPanel.removeAll();
			boolean retrieveData=false, reorderData=false;
			if(currDisplay.compareTo(displayComboBox.getSelectedItem().toString())!=0)
			{
				retrieveData=true;
				currDisplay=displayComboBox.getSelectedItem().toString();
			}
			if(currOrderByFeature.compareTo(featureComboBox.getSelectedItem().toString())!=0)
			{
				reorderData=true;
				currOrderByFeature=featureComboBox.getSelectedItem().toString();
			}
			if(currOrder.compareTo(orderComboBox.getSelectedItem().toString())!=0)
			{
				reorderData=true;
				currOrder=orderComboBox.getSelectedItem().toString();
			}
			createTable(retrieveData, reorderData, currDisplay, currOrderByFeature, currOrder);
			resultTableModel.fireTableDataChanged();
			this.repaint();
			this.invalidate();
		}
	}
}

