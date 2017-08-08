package dialogFrames;

//Imports are listed in full to show what's being used could just import javax.swing.* and java.awt.* etc..
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.border.EtchedBorder;

import constants.stringConstant;
import database.postgreSQL;

@SuppressWarnings("serial")
//	Layout: 
//  |  - settingPanel                                             
//  |    + topologicalFeaturePanel                                       
//  |      * topologicalCheckBox                        
//  |  - buttonPanel
//  |    + computeButton
//  |    + cancelButton

public class characterizeHallmarkDialog extends JDialog{
	private JPanel settingPanel, buttonPanel, step1Panel, step2Panel, step3Panel;
	private JPanel inDegreePanel, outDegreePanel, totalDegreePanel, eigenvectorPanel, betweennessPanel, bridgingCoeffPanel;
	private JPanel bridgingCentralityPanel, undirectedClusteringCoeffPanel, inClusteringCoeffPanel, outClusteringCoeffPanel;
	private JPanel cycleClusteringCoeffPanel, middlemanClusteringCoeffPanel, targetDownstreamEffectPanel;
	private JPanel proliferationPanel, growthRepressorPanel, apoptosisPanel, replicativeImmortalityPanel;
	private JPanel angiogenesisPanel, metastasisPanel, metabolismPanel, immuneDestructionPanel;
	private JPanel genomeInstabilityPanel, tumorPromotingInflammationPanel;
	private JButton characterizeButton, cancelButton;
	private JCheckBox inDegreeCheckBox, outDegreeCheckBox, totalDegreeCheckBox, eigenvectorCheckBox, betweennessCheckBox;
	private JCheckBox bridgingCoeffCheckBox, bridgingCentralityCheckBox, undirectedClusteringCoeffCheckBox, inClusteringCoeffCheckBox;
	private JCheckBox outClusteringCoeffCheckBox, cycleClusteringCoeffCheckBox, middlemanClusteringCoeffCheckBox, targetDownstreamEffectCheckBox;
	private JCheckBox proliferationCheckBox, growthRepressorCheckBox, apoptosisCheckBox, replicativeImmortalityCheckBox;
	private JCheckBox angiogenesisCheckBox, metastasisCheckBox, metabolismCheckBox, immuneDestructionCheckBox;
	private JCheckBox genomeInstabilityCheckBox, tumorPromotingInflammationCheckBox;
	private JSlider wmcSlider;
	private int computedFeature=0, uncomputedFeature=0, wmcWt;
	
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

	private String HALLMARK_PROLIFERATION_GUI;
	private String HALLMARK_GROWTH_REPRESSOR_GUI;
	private String HALLMARK_APOPTOSIS_GUI;
	private String HALLMARK_REPLICATIVE_IMMORTALITY_GUI;
	private String HALLMARK_ANGIOGENESIS_GUI;
	private String HALLMARK_METASTASIS_GUI;
	private String HALLMARK_METABOLISM_GUI;
	private String HALLMARK_IMMUNE_DESTRUCTION_GUI;
	private String HALLMARK_GENOME_INSTABILITY_GUI;
	private String HALLMARK_TUMOR_PROMOTING_INFLAMMATION_GUI;
	
	private stringConstant staticString=new stringConstant();
	protected final static String CHARACTERIZE = "Characterize";
	protected final static String DONE = "done";
	private boolean performCharacterization=false;
	private postgreSQL DB;
	private ArrayList<String> selectedFeatures=new ArrayList<String>();
	private ArrayList<String> selectedUncomputedFeatures=new ArrayList<String>();
	private ArrayList<String> selectedHallmarks=new ArrayList<String>();
	
	public characterizeHallmarkDialog(postgreSQL database)
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
		
		HALLMARK_PROLIFERATION_GUI=staticString.getGUIProliferation();
		HALLMARK_GROWTH_REPRESSOR_GUI=staticString.getGUIGrowthRepressor();
		HALLMARK_APOPTOSIS_GUI=staticString.getGUIApoptosis();
		HALLMARK_REPLICATIVE_IMMORTALITY_GUI=staticString.getGUIReplicativeImmortality();
		HALLMARK_ANGIOGENESIS_GUI=staticString.getGUIAngiogenesis();
		HALLMARK_METASTASIS_GUI=staticString.getGUIMetastasis();
		HALLMARK_METABOLISM_GUI=staticString.getGUIMetabolism();
		HALLMARK_IMMUNE_DESTRUCTION_GUI=staticString.getGUIImmuneDestruction();
		HALLMARK_GENOME_INSTABILITY_GUI=staticString.getGUIGenomeInstability();
		HALLMARK_TUMOR_PROMOTING_INFLAMMATION_GUI=staticString.getGUITumorPromotingInflammation();
		
		HALLMARK_PROLIFERATION_DB=staticString.getDBProliferation();
		HALLMARK_GROWTH_REPRESSOR_DB=staticString.getDBGrowthRepressor();
		HALLMARK_APOPTOSIS_DB=staticString.getDBApoptosis();
		HALLMARK_REPLICATIVE_IMMORTALITY_DB=staticString.getDBReplicativeImmortality();
		HALLMARK_ANGIOGENESIS_DB=staticString.getDBAngiogenesis();
		HALLMARK_METASTASIS_DB=staticString.getDBMetastasis();
		HALLMARK_METABOLISM_DB=staticString.getDBMetabolism();
		HALLMARK_IMMUNE_DESTRUCTION_DB=staticString.getDBImmuneDestruction();
		HALLMARK_GENOME_INSTABILITY_DB=staticString.getDBGenomeInstability();
		HALLMARK_TUMOR_PROMOTING_INFLAMMATION_DB=staticString.getDBTumorPromotingInflammation();
		
		DB=database;

		//set JDialog properties
		setTitle("Characterize Hallmarks");
		setSize(900,600);
		setLocationRelativeTo(null);
		setLayout(new BorderLayout());
		setResizable(true);
		setModal(true);

		//configure components in JDialog
		initializeComponents();
	}

	private ActionListener actionListener = new ActionListener() 
	{
		public void actionPerformed(ActionEvent act) {
			if (act.getActionCommand() == CHARACTERIZE)
				characterize_actionPerformed();
			if (act.getActionCommand() == CANCEL)
				cancel_actionPerformed();
		}
	};
	
	private void createFeaturePanel()
	{
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
		
		JPanel computedPanel=new JPanel();
		computedPanel.setLayout(new BoxLayout(computedPanel, BoxLayout.Y_AXIS));
		computedPanel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
		JPanel computedLabelPanel=new JPanel();
		computedLabelPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		JLabel computedLabel=new JLabel("Computed Feature(s):");
		computedLabelPanel.add(computedLabel);
		computedPanel.add(computedLabelPanel);
		JPanel notComputedPanel=new JPanel();
		notComputedPanel.setLayout(new BoxLayout(notComputedPanel, BoxLayout.Y_AXIS));
		notComputedPanel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
		JPanel notComputedLabelPanel=new JPanel();
		notComputedLabelPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		JLabel notComputedLabel=new JLabel("Uncomputed Feature(s):");
		notComputedLabelPanel.add(notComputedLabel);
		notComputedPanel.add(notComputedLabelPanel);

		if(DB.getFeature_featureComputed(FEATURE_IN_DEGREE_DB)==0)
		{
			computedPanel.add(inDegreePanel);
			computedFeature++;
		}
		else
		{
			notComputedPanel.add(inDegreePanel);
			uncomputedFeature++;
		}
		if(DB.getFeature_featureComputed(FEATURE_OUT_DEGREE_DB)==0)
		{
			computedPanel.add(outDegreePanel);
			computedFeature++;
		}
		else
		{
			notComputedPanel.add(outDegreePanel);
			uncomputedFeature++;
		}
		if(DB.getFeature_featureComputed(FEATURE_TOTAL_DEGREE_DB)==0)
		{
			computedPanel.add(totalDegreePanel);
			computedFeature++;
		}
		else
		{
			notComputedPanel.add(totalDegreePanel);
			uncomputedFeature++;
		}
		if(DB.getFeature_featureComputed(FEATURE_EIGENVECTOR_DB)==0)
		{
			computedPanel.add(eigenvectorPanel);
			computedFeature++;
		}
		else
		{
			notComputedPanel.add(eigenvectorPanel);
			uncomputedFeature++;
		}
		if(DB.getFeature_featureComputed(FEATURE_BETWEENNESS_DB)==0)
		{
			computedPanel.add(betweennessPanel);
			computedFeature++;
		}
		else
		{
			notComputedPanel.add(betweennessPanel);
			uncomputedFeature++;
		}
		if(DB.getFeature_featureComputed(FEATURE_BRIDGING_COEFF_DB)==0)
		{
			computedPanel.add(bridgingCoeffPanel);
			computedFeature++;
		}
		else
		{
			notComputedPanel.add(bridgingCoeffPanel);
			uncomputedFeature++;
		}
		if(DB.getFeature_featureComputed(FEATURE_BRIDGING_CENTRALITY_DB)==0)
		{
			computedPanel.add(bridgingCentralityPanel);
			computedFeature++;
		}
		else
		{
			notComputedPanel.add(bridgingCentralityPanel);
			uncomputedFeature++;
		}
		if(DB.getFeature_featureComputed(FEATURE_UNDIRECTED_CLUSTERING_COEFFICIENT_DB)==0)
		{
			computedPanel.add(undirectedClusteringCoeffPanel);
			computedFeature++;
		}
		else
		{
			notComputedPanel.add(undirectedClusteringCoeffPanel);
			uncomputedFeature++;
		}
		if(DB.getFeature_featureComputed(FEATURE_IN_CLUSTERING_COEFFICIENT_DB)==0)
		{
			computedPanel.add(inClusteringCoeffPanel);
			computedFeature++;
		}
		else
		{
			notComputedPanel.add(inClusteringCoeffPanel);
			uncomputedFeature++;
		}
		if(DB.getFeature_featureComputed(FEATURE_OUT_CLUSTERING_COEFFICIENT_DB)==0)
		{
			computedPanel.add(outClusteringCoeffPanel);
			computedFeature++;
		}
		else
		{
			notComputedPanel.add(outClusteringCoeffPanel);
			uncomputedFeature++;
		}
		if(DB.getFeature_featureComputed(FEATURE_CYCLE_CLUSTERING_COEFFICIENT_DB)==0)
		{
			computedPanel.add(cycleClusteringCoeffPanel);
			computedFeature++;
		}
		else
		{
			notComputedPanel.add(cycleClusteringCoeffPanel);
			uncomputedFeature++;
		}
		if(DB.getFeature_featureComputed(FEATURE_MIDDLEMAN_CLUSTERING_COEFFICIENT_DB)==0)
		{
			computedPanel.add(middlemanClusteringCoeffPanel);
			computedFeature++;
		}
		else
		{
			notComputedPanel.add(middlemanClusteringCoeffPanel);
			uncomputedFeature++;
		}
		//to turn on TDE after fixing it
	/*	if(DB.getFeature_featureComputed(FEATURE_TARGET_DOWNSTREAM_EFFECT_DB)==0)
		{
			computedPanel.add(targetDownstreamEffectPanel);
			computedFeature++;
		}
		else
		{
			notComputedPanel.add(targetDownstreamEffectPanel);
			uncomputedFeature++;
		}
	*/	
		step1Panel=new JPanel();
		step1Panel.setLayout(new BoxLayout(step1Panel, BoxLayout.Y_AXIS));
		step1Panel.setBorder(BorderFactory.createLineBorder(Color.black));
		JPanel step1LabelPanel=new JPanel();
		step1LabelPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		JLabel step1Label=new JLabel("Step 1: select feature(s) for characterization:");
		step1LabelPanel.add(step1Label);
		JPanel featurePanel=new JPanel();
		featurePanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		if(computedFeature>0)
			featurePanel.add(computedPanel);
		if(uncomputedFeature>0)
			featurePanel.add(notComputedPanel);
		step1Panel.add(step1LabelPanel);
		step1Panel.add(featurePanel);
	}

	private void createHallmarkPanel()
	{
		proliferationPanel=new JPanel();
		proliferationPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		proliferationCheckBox=new JCheckBox(HALLMARK_PROLIFERATION_GUI);
		proliferationPanel.add(proliferationCheckBox);
		growthRepressorPanel=new JPanel();
		growthRepressorPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		growthRepressorCheckBox=new JCheckBox(HALLMARK_GROWTH_REPRESSOR_GUI);
		growthRepressorPanel.add(growthRepressorCheckBox);
		apoptosisPanel=new JPanel();
		apoptosisPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		apoptosisCheckBox=new JCheckBox(HALLMARK_APOPTOSIS_GUI);
		apoptosisPanel.add(apoptosisCheckBox);
		replicativeImmortalityPanel=new JPanel();
		replicativeImmortalityPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		replicativeImmortalityCheckBox=new JCheckBox(HALLMARK_REPLICATIVE_IMMORTALITY_GUI);
		replicativeImmortalityPanel.add(replicativeImmortalityCheckBox);
		angiogenesisPanel=new JPanel();
		angiogenesisPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		angiogenesisCheckBox=new JCheckBox(HALLMARK_ANGIOGENESIS_GUI);
		angiogenesisPanel.add(angiogenesisCheckBox);
		metastasisPanel=new JPanel();
		metastasisPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		metastasisCheckBox=new JCheckBox(HALLMARK_METASTASIS_GUI);
		metastasisPanel.add(metastasisCheckBox);
		metabolismPanel=new JPanel();
		metabolismPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		metabolismCheckBox=new JCheckBox(HALLMARK_METABOLISM_GUI);
		metabolismPanel.add(metabolismCheckBox);
		immuneDestructionPanel=new JPanel();
		immuneDestructionPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		immuneDestructionCheckBox=new JCheckBox(HALLMARK_IMMUNE_DESTRUCTION_GUI);
		immuneDestructionPanel.add(immuneDestructionCheckBox);
		genomeInstabilityPanel=new JPanel();
		genomeInstabilityPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		genomeInstabilityCheckBox=new JCheckBox(HALLMARK_GENOME_INSTABILITY_GUI);
		genomeInstabilityPanel.add(genomeInstabilityCheckBox);
		tumorPromotingInflammationPanel=new JPanel();
		tumorPromotingInflammationPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		tumorPromotingInflammationCheckBox=new JCheckBox(HALLMARK_TUMOR_PROMOTING_INFLAMMATION_GUI);
		tumorPromotingInflammationPanel.add(tumorPromotingInflammationCheckBox);
		
		step2Panel=new JPanel();
		step2Panel.setLayout(new BoxLayout(step2Panel, BoxLayout.Y_AXIS));
		step2Panel.setBorder(BorderFactory.createLineBorder(Color.black));
		JPanel step2LabelPanel=new JPanel();
		step2LabelPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		JLabel step2Label=new JLabel("Step 2: select hallmark(s) to characterize:");
		step2LabelPanel.add(step2Label);
		step2Panel.add(step2LabelPanel);
		step2Panel.add(proliferationPanel);
		step2Panel.add(growthRepressorPanel);
		step2Panel.add(apoptosisPanel);
		step2Panel.add(replicativeImmortalityPanel);
		step2Panel.add(angiogenesisPanel);
		step2Panel.add(metastasisPanel);
		step2Panel.add(metabolismPanel);
		step2Panel.add(immuneDestructionPanel);
		step2Panel.add(genomeInstabilityPanel);
		step2Panel.add(tumorPromotingInflammationPanel);
	}
	
	private void initializeComponents()
	{
		settingPanel=new JPanel();
		buttonPanel=new JPanel();
		
		createFeaturePanel();
		createHallmarkPanel();
		
		//create step3Panel (wmc)
		step3Panel=new JPanel();
		step3Panel.setLayout(new BoxLayout(step3Panel, BoxLayout.Y_AXIS));
		step3Panel.setBorder(BorderFactory.createLineBorder(Color.black));
		JPanel step3LabelPanel=new JPanel();
		step3LabelPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		JLabel step3Label=new JLabel("Step 3: Select weighted misclassification cost of hallmark nodes:");
		step3LabelPanel.add(step3Label);
		wmcSlider=new JSlider(0, 10, 7);
		wmcSlider.setMajorTickSpacing(1);
		wmcSlider.setPaintTicks(true);
		wmcSlider.setSnapToTicks(true);
		wmcSlider.setPaintLabels(true);
		step3Panel.add(step3LabelPanel);
		step3Panel.add(wmcSlider);
		
		JLabel emptyLabel=new JLabel(" ");
		JPanel step2And3Panel=new JPanel();
		step2And3Panel.setLayout(new BoxLayout(step2And3Panel, BoxLayout.Y_AXIS));
		step2And3Panel.add(step2Panel);
		step2And3Panel.add(emptyLabel);
		step2And3Panel.add(step3Panel);
		settingPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		settingPanel.add(step1Panel);
		settingPanel.add(step2And3Panel);
				
		//create buttonPanel
		cancelButton= new JButton();
		cancelButton.setSize(50, 20);
		cancelButton.setText(DONE);
		cancelButton.setActionCommand(DONE);
		cancelButton.addActionListener(actionListener);

		characterizeButton = new JButton();
		characterizeButton.setSize(50, 20);
		characterizeButton.setText(CHARACTERIZE);
		characterizeButton.setActionCommand(CHARACTERIZE);
		characterizeButton.addActionListener(actionListener);

		buttonPanel= new JPanel();
		buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
		buttonPanel.add(characterizeButton);
		buttonPanel.add(cancelButton);

		setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));
		add(settingPanel);
		add(buttonPanel);
	}

	private void storeSelectedFeatures(){
		if(inDegreeCheckBox.isSelected())
		{
			selectedFeatures.add(FEATURE_IN_DEGREE_DB);
			if(DB.getFeature_featureComputed(FEATURE_IN_DEGREE_DB)>0)
				selectedUncomputedFeatures.add(FEATURE_IN_DEGREE_DB);
		}
		if(outDegreeCheckBox.isSelected())
		{
			selectedFeatures.add(FEATURE_OUT_DEGREE_DB);
			if(DB.getFeature_featureComputed(FEATURE_OUT_DEGREE_DB)>0)
				selectedUncomputedFeatures.add(FEATURE_OUT_DEGREE_DB);
		}
		if(totalDegreeCheckBox.isSelected())
		{
			selectedFeatures.add(FEATURE_TOTAL_DEGREE_DB);
			if(DB.getFeature_featureComputed(FEATURE_TOTAL_DEGREE_DB)>0)
				selectedUncomputedFeatures.add(FEATURE_TOTAL_DEGREE_DB);
		}
		if(eigenvectorCheckBox.isSelected())
		{
			selectedFeatures.add(FEATURE_EIGENVECTOR_DB);
			if(DB.getFeature_featureComputed(FEATURE_EIGENVECTOR_DB)>0)
				selectedUncomputedFeatures.add(FEATURE_EIGENVECTOR_DB);
		}
		if(betweennessCheckBox.isSelected())
		{
			selectedFeatures.add(FEATURE_BETWEENNESS_DB);
			if(DB.getFeature_featureComputed(FEATURE_BETWEENNESS_DB)>0)
				selectedUncomputedFeatures.add(FEATURE_BETWEENNESS_DB);
		}
		if(bridgingCoeffCheckBox.isSelected())
		{
			selectedFeatures.add(FEATURE_BRIDGING_COEFF_DB);
			if(DB.getFeature_featureComputed(FEATURE_BRIDGING_COEFF_DB)>0)
				selectedUncomputedFeatures.add(FEATURE_BRIDGING_COEFF_DB);
		}
		if(bridgingCentralityCheckBox.isSelected())
		{
			selectedFeatures.add(FEATURE_BRIDGING_CENTRALITY_DB);
			if(DB.getFeature_featureComputed(FEATURE_BRIDGING_CENTRALITY_DB)>0)
				selectedUncomputedFeatures.add(FEATURE_BRIDGING_CENTRALITY_DB);
		}
		if(undirectedClusteringCoeffCheckBox.isSelected())
		{
			selectedFeatures.add(FEATURE_UNDIRECTED_CLUSTERING_COEFFICIENT_DB);
			if(DB.getFeature_featureComputed(FEATURE_UNDIRECTED_CLUSTERING_COEFFICIENT_DB)>0)
				selectedUncomputedFeatures.add(FEATURE_UNDIRECTED_CLUSTERING_COEFFICIENT_DB);
		}
		if(inClusteringCoeffCheckBox.isSelected())
		{
			selectedFeatures.add(FEATURE_IN_CLUSTERING_COEFFICIENT_DB);
			if(DB.getFeature_featureComputed(FEATURE_IN_CLUSTERING_COEFFICIENT_DB)>0)
				selectedUncomputedFeatures.add(FEATURE_IN_CLUSTERING_COEFFICIENT_DB);
		}
		if(outClusteringCoeffCheckBox.isSelected())
		{
			selectedFeatures.add(FEATURE_OUT_CLUSTERING_COEFFICIENT_DB);
			if(DB.getFeature_featureComputed(FEATURE_OUT_CLUSTERING_COEFFICIENT_DB)>0)
				selectedUncomputedFeatures.add(FEATURE_OUT_CLUSTERING_COEFFICIENT_DB);
		}
		if(cycleClusteringCoeffCheckBox.isSelected())
		{
			selectedFeatures.add(FEATURE_CYCLE_CLUSTERING_COEFFICIENT_DB);
			if(DB.getFeature_featureComputed(FEATURE_CYCLE_CLUSTERING_COEFFICIENT_DB)>0)
				selectedUncomputedFeatures.add(FEATURE_CYCLE_CLUSTERING_COEFFICIENT_DB);
		}
		if(middlemanClusteringCoeffCheckBox.isSelected())
		{
			selectedFeatures.add(FEATURE_MIDDLEMAN_CLUSTERING_COEFFICIENT_DB);
			if(DB.getFeature_featureComputed(FEATURE_MIDDLEMAN_CLUSTERING_COEFFICIENT_DB)>0)
				selectedUncomputedFeatures.add(FEATURE_MIDDLEMAN_CLUSTERING_COEFFICIENT_DB);
		}
		if(targetDownstreamEffectCheckBox.isSelected())
		{
			selectedFeatures.add(FEATURE_TARGET_DOWNSTREAM_EFFECT_DB);
			if(DB.getFeature_featureComputed(FEATURE_TARGET_DOWNSTREAM_EFFECT_DB)>0)
				selectedUncomputedFeatures.add(FEATURE_TARGET_DOWNSTREAM_EFFECT_DB);
		}
	}
	
	private void storeSelectedHallmarks(){
		if(proliferationCheckBox.isSelected())
			selectedHallmarks.add(HALLMARK_PROLIFERATION_DB);
		if(growthRepressorCheckBox.isSelected())
			selectedHallmarks.add(HALLMARK_GROWTH_REPRESSOR_DB);
		if(apoptosisCheckBox.isSelected())
			selectedHallmarks.add(HALLMARK_APOPTOSIS_DB);
		if(replicativeImmortalityCheckBox.isSelected())
			selectedHallmarks.add(HALLMARK_REPLICATIVE_IMMORTALITY_DB);
		if(angiogenesisCheckBox.isSelected())
			selectedHallmarks.add(HALLMARK_ANGIOGENESIS_DB);
		if(metastasisCheckBox.isSelected())
			selectedHallmarks.add(HALLMARK_METASTASIS_DB);
		if(metabolismCheckBox.isSelected())
			selectedHallmarks.add(HALLMARK_METABOLISM_DB);
		if(immuneDestructionCheckBox.isSelected())
			selectedHallmarks.add(HALLMARK_IMMUNE_DESTRUCTION_DB);
		if(genomeInstabilityCheckBox.isSelected())
			selectedHallmarks.add(HALLMARK_GENOME_INSTABILITY_DB);
		if(tumorPromotingInflammationCheckBox.isSelected())
			selectedHallmarks.add(HALLMARK_TUMOR_PROMOTING_INFLAMMATION_DB);
	}
	
	private void characterize_actionPerformed(){
		storeSelectedFeatures();
		storeSelectedHallmarks();
		performCharacterization=true;
		wmcWt=wmcSlider.getValue();
		setVisible(false);
	}

	private void cancel_actionPerformed(){
		setVisible(false);
	}
	
	public boolean getPerformCharacterization(){
		return performCharacterization;
	}
	
	public ArrayList<String> getSelectedFeatures(){
		return selectedFeatures;
	}
	
	public ArrayList<String> getSelectedUncomputedFeatures(){
		return selectedUncomputedFeatures;
	}
	
	public ArrayList<String> getSelectedHallmarks(){
		return selectedHallmarks;
	}
	
	public int getWMCWeight(){
		return wmcWt;
	}
}

