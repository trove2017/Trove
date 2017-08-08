package dialogFrames;

//Imports are listed in full to show what's being used could just import javax.swing.* and java.awt.* etc..
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

import constants.stringConstant;

@SuppressWarnings("serial")
//	Layout: 
//  |  - settingPanel                                             
//  |    + topologicalFeaturePanel                                       
//  |      * topologicalCheckBox                        
//  |  - buttonPanel
//  |    + computeButton
//  |    + cancelButton

public class computeTopologicalFeaturesDialog extends JDialog{
	private JPanel settingPanel, buttonPanel;
	private JPanel inDegreePanel, outDegreePanel, totalDegreePanel, eigenvectorPanel, betweennessPanel, bridgingCoeffPanel;
	private JPanel bridgingCentralityPanel, undirectedClusteringCoeffPanel, inClusteringCoeffPanel, outClusteringCoeffPanel;
	private JPanel cycleClusteringCoeffPanel, middlemanClusteringCoeffPanel, targetDownstreamEffectPanel;
	private JButton computeButton, doneButton;
	private JCheckBox inDegreeCheckBox, outDegreeCheckBox, totalDegreeCheckBox, eigenvectorCheckBox, betweennessCheckBox;
	private JCheckBox bridgingCoeffCheckBox, bridgingCentralityCheckBox, undirectedClusteringCoeffCheckBox, inClusteringCoeffCheckBox;
	private JCheckBox outClusteringCoeffCheckBox, cycleClusteringCoeffCheckBox, middlemanClusteringCoeffCheckBox, targetDownstreamEffectCheckBox;
	private JDialog recomputeFeaturesDialog;
	
	private String CANCEL;
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
		
	private stringConstant staticString=new stringConstant();
	protected final static String CONFIRM_RECOMPUTE_FEATURES = "Confirm";
	protected final static String COMPUTE = "Compute";
	protected final static String DONE = "done";
	private boolean performComputation=false;
	private boolean WHOLE_NETWORK=false;
	private int nullRows;
	private ArrayList<String> featuresToCompute=new ArrayList<String>();
	
	public computeTopologicalFeaturesDialog(boolean currPathwayIsWholeNetwork, int rows)
	{
		//initialize constants
		CANCEL=staticString.getCancel();
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
		
		//set JDialog properties
		setTitle("Compute Topological Features");
		setSize(500,330);
		setLocationRelativeTo(null);
		setLayout(new BorderLayout());
		setResizable(false);
		setModal(true);

		//configure components in JDialog
		initializeComponents();
		WHOLE_NETWORK=currPathwayIsWholeNetwork;
		nullRows=rows;
	}

	private ActionListener actionListener = new ActionListener() 
	{
		public void actionPerformed(ActionEvent act) {
			if (act.getActionCommand() == COMPUTE)
				compute_actionPerformed();
			if (act.getActionCommand() == CONFIRM_RECOMPUTE_FEATURES)
				recompute_actionPerformed();
			if (act.getActionCommand() == DONE)
				done_actionPerformed();
			if (act.getActionCommand() == CANCEL)
				cancel_actionPerformed();
		}
	};

	public ArrayList<String> getFeaturesToCompute()
	{
		return featuresToCompute;
	}
	
	private void initializeComponents()
	{
		settingPanel=new JPanel();
		buttonPanel=new JPanel();
		
		//create topologicalPanel
		inDegreePanel=new JPanel();
		inDegreePanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		inDegreeCheckBox=new JCheckBox(FEATURE_IN_DEGREE_GUI);
		inDegreePanel.add(inDegreeCheckBox);
		outDegreePanel=new JPanel();
		outDegreePanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		outDegreeCheckBox=new JCheckBox(FEATURE_OUT_DEGREE_GUI);
		outDegreePanel.add(outDegreeCheckBox);
		totalDegreePanel=new JPanel();
		totalDegreePanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		totalDegreeCheckBox=new JCheckBox(FEATURE_TOTAL_DEGREE_GUI);
		totalDegreePanel.add(totalDegreeCheckBox);
		eigenvectorPanel=new JPanel();
		eigenvectorPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		eigenvectorCheckBox=new JCheckBox(FEATURE_EIGENVECTOR_GUI);
		eigenvectorPanel.add(eigenvectorCheckBox);
		betweennessPanel=new JPanel();
		betweennessPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		betweennessCheckBox=new JCheckBox(FEATURE_BETWEENNESS_GUI);
		betweennessPanel.add(betweennessCheckBox);
		bridgingCoeffPanel=new JPanel();
		bridgingCoeffPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		bridgingCoeffCheckBox=new JCheckBox(FEATURE_BRIDGING_COEFF_GUI);
		bridgingCoeffPanel.add(bridgingCoeffCheckBox);
		bridgingCentralityPanel=new JPanel();
		bridgingCentralityPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		bridgingCentralityCheckBox=new JCheckBox(FEATURE_BRIDGING_CENTRALITY_GUI);
		bridgingCentralityPanel.add(bridgingCentralityCheckBox);
		undirectedClusteringCoeffPanel=new JPanel();
		undirectedClusteringCoeffPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		undirectedClusteringCoeffCheckBox=new JCheckBox(FEATURE_UNDIRECTED_CLUSTERING_COEFFICIENT_GUI);
		undirectedClusteringCoeffPanel.add(undirectedClusteringCoeffCheckBox);
		inClusteringCoeffPanel=new JPanel();
		inClusteringCoeffPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		inClusteringCoeffCheckBox=new JCheckBox(FEATURE_IN_CLUSTERING_COEFFICIENT_GUI);
		inClusteringCoeffPanel.add(inClusteringCoeffCheckBox);
		outClusteringCoeffPanel=new JPanel();
		outClusteringCoeffPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		outClusteringCoeffCheckBox=new JCheckBox(FEATURE_OUT_CLUSTERING_COEFFICIENT_GUI);
		outClusteringCoeffPanel.add(outClusteringCoeffCheckBox);
		cycleClusteringCoeffPanel=new JPanel();
		cycleClusteringCoeffPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		cycleClusteringCoeffCheckBox=new JCheckBox(FEATURE_CYCLE_CLUSTERING_COEFFICIENT_GUI);
		cycleClusteringCoeffPanel.add(cycleClusteringCoeffCheckBox);
		middlemanClusteringCoeffPanel=new JPanel();
		middlemanClusteringCoeffPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		middlemanClusteringCoeffCheckBox=new JCheckBox(FEATURE_MIDDLEMAN_CLUSTERING_COEFFICIENT_GUI);
		middlemanClusteringCoeffPanel.add(middlemanClusteringCoeffCheckBox);
		targetDownstreamEffectPanel=new JPanel();
		targetDownstreamEffectPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		targetDownstreamEffectCheckBox=new JCheckBox(FEATURE_TARGET_DOWNSTREAM_EFFECT_GUI);
		targetDownstreamEffectPanel.add(targetDownstreamEffectCheckBox);
		
		JPanel leftPanel=new JPanel();
		leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
		leftPanel.add(inDegreePanel);
		leftPanel.add(outDegreePanel);
		leftPanel.add(totalDegreePanel);
		leftPanel.add(eigenvectorPanel);
		leftPanel.add(betweennessPanel);
		leftPanel.add(bridgingCoeffPanel);
		leftPanel.add(bridgingCentralityPanel);
		JPanel rightPanel=new JPanel();
		rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
		rightPanel.add(undirectedClusteringCoeffPanel);
		rightPanel.add(inClusteringCoeffPanel);
		rightPanel.add(outClusteringCoeffPanel);
		rightPanel.add(cycleClusteringCoeffPanel);
		rightPanel.add(middlemanClusteringCoeffPanel);
		//rightPanel.add(targetDownstreamEffectPanel); //to turn on TDE after fixing it
		settingPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		settingPanel.add(leftPanel);
		settingPanel.add(rightPanel);
		//create buttonPanel
		doneButton= new JButton();
		doneButton.setSize(50, 20);
		doneButton.setText(DONE);
		doneButton.setActionCommand(DONE);
		doneButton.addActionListener(actionListener);

		computeButton = new JButton();
		computeButton.setSize(50, 20);
		computeButton.setText(COMPUTE);
		computeButton.setActionCommand(COMPUTE);
		computeButton.addActionListener(actionListener);

		buttonPanel= new JPanel();
		buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
		buttonPanel.add(computeButton);
		buttonPanel.add(doneButton);

		setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));
		add(settingPanel);
		add(buttonPanel);
	}

	private void compute_actionPerformed(){
		if(inDegreeCheckBox.isSelected())
			featuresToCompute.add(FEATURE_IN_DEGREE_DB);
		if(outDegreeCheckBox.isSelected())
			featuresToCompute.add(FEATURE_OUT_DEGREE_DB);
		if(totalDegreeCheckBox.isSelected())
			featuresToCompute.add(FEATURE_TOTAL_DEGREE_DB);
		if(eigenvectorCheckBox.isSelected())
			featuresToCompute.add(FEATURE_EIGENVECTOR_DB);
		if(betweennessCheckBox.isSelected())
			featuresToCompute.add(FEATURE_BETWEENNESS_DB);
		if(bridgingCoeffCheckBox.isSelected())
			featuresToCompute.add(FEATURE_BRIDGING_COEFF_DB);
		if(bridgingCentralityCheckBox.isSelected())
			featuresToCompute.add(FEATURE_BRIDGING_CENTRALITY_DB);
		if(undirectedClusteringCoeffCheckBox.isSelected())
			featuresToCompute.add(FEATURE_UNDIRECTED_CLUSTERING_COEFFICIENT_DB);
		if(inClusteringCoeffCheckBox.isSelected())
			featuresToCompute.add(FEATURE_IN_CLUSTERING_COEFFICIENT_DB);
		if(outClusteringCoeffCheckBox.isSelected())
			featuresToCompute.add(FEATURE_OUT_CLUSTERING_COEFFICIENT_DB);
		if(cycleClusteringCoeffCheckBox.isSelected())
			featuresToCompute.add(FEATURE_CYCLE_CLUSTERING_COEFFICIENT_DB);
		if(middlemanClusteringCoeffCheckBox.isSelected())
			featuresToCompute.add(FEATURE_MIDDLEMAN_CLUSTERING_COEFFICIENT_DB);
		if(targetDownstreamEffectCheckBox.isSelected())
			featuresToCompute.add(FEATURE_TARGET_DOWNSTREAM_EFFECT_DB);
		if(!WHOLE_NETWORK)
			performComputation=true;
		else
		{
			if(nullRows>0)
				performComputation=true;
			else
			{
				//prompt and ask to confirm that you want to recompute for whole network.
				JPanel confirmationLabelPanel1=new JPanel();
				confirmationLabelPanel1.setLayout(new FlowLayout(FlowLayout.LEFT));
				JLabel confirmationLabel1=new JLabel("Some features for this network have been previously computed.");
				confirmationLabelPanel1.add(confirmationLabel1);
				JPanel confirmationLabelPanel2=new JPanel();
				confirmationLabelPanel2.setLayout(new FlowLayout(FlowLayout.LEFT));
				JLabel confirmationLabel2=new JLabel("Select the 'Confirm' button if you wish to recompute the features.");
				confirmationLabelPanel2.add(confirmationLabel2);
				JPanel confirmationLabelPanel3=new JPanel();
				confirmationLabelPanel3.setLayout(new FlowLayout(FlowLayout.LEFT));
				JLabel confirmationLabel3=new JLabel("This may take some time. Please wait.");
				confirmationLabelPanel3.add(confirmationLabel3);
				JPanel buttonPanel=new JPanel();
				buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
				JButton confirmButton= new JButton();
				confirmButton.setSize(55, 20);
				confirmButton.setText(CONFIRM_RECOMPUTE_FEATURES);
				confirmButton.setActionCommand(CONFIRM_RECOMPUTE_FEATURES);
				confirmButton.addActionListener(actionListener);
				JButton cancelButton= new JButton();
				cancelButton.setSize(50, 20);
				cancelButton.setText(CANCEL);
				cancelButton.setActionCommand(CANCEL);
				cancelButton.addActionListener(actionListener);
				buttonPanel.add(confirmButton);
				buttonPanel.add(cancelButton);
			
				recomputeFeaturesDialog=new JDialog();
				recomputeFeaturesDialog.setTitle("Confirmation of feature recomputation");
				recomputeFeaturesDialog.setSize(500,200);
				recomputeFeaturesDialog.setLocationRelativeTo(null);
				recomputeFeaturesDialog.setResizable(false);
				recomputeFeaturesDialog.setModal(true);
				recomputeFeaturesDialog.setLayout(new BoxLayout(recomputeFeaturesDialog.getContentPane(), BoxLayout.Y_AXIS));
				recomputeFeaturesDialog.add(confirmationLabelPanel1);
				recomputeFeaturesDialog.add(confirmationLabelPanel2);
				recomputeFeaturesDialog.add(confirmationLabelPanel3);
				recomputeFeaturesDialog.add(buttonPanel);
				recomputeFeaturesDialog.setVisible(true);
			}
		}
		setVisible(false);
	}

	private void recompute_actionPerformed(){
		performComputation=true;
		recomputeFeaturesDialog.setVisible(false);
		setVisible(false);
	}
	
	private void cancel_actionPerformed(){
		recomputeFeaturesDialog.setVisible(false);
		setVisible(false);
	}
	
	private void done_actionPerformed(){
		setVisible(false);
	}
	
	public boolean getPerformComputation(){
		return performComputation;
	}
}
