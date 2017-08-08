package dialogFrames;

//Imports are listed in full to show what's being used could just import javax.swing.* and java.awt.* etc..
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;

import constants.stringConstant;

@SuppressWarnings("serial")
//	Layout: 
//  |  - settingPanel                                             
//  |    + hallmarkPanel (for each hallmark)                                       
//  |      * hallmarkCheckBox                        
//  |  - buttonPanel
//  |    + visualizeButton
//  |    + cancelButton

public class visualizeHallmarkDialog extends JDialog{
	private JPanel settingPanel, buttonPanel, exportFileCardPanel;
	private JPanel proliferationPanel, growthRepressorPanel, apoptosisPanel, replicativeImmortalityPanel;
	private JPanel angiogenesisPanel, metastasisPanel, metabolismPanel, immuneDestructionPanel, genomeInstabilityPanel, tumorPromotingInflammationPanel;
	private JComboBox<String> displayComboBox;
	private JButton visualizeButton, cancelButton;
	private JCheckBox proliferationCheckBox, growthRepressorCheckBox, apoptosisCheckBox, replicativeImmortalityCheckBox;
	private JCheckBox angiogenesisCheckBox, metastasisCheckBox, metabolismCheckBox, immuneDestructionCheckBox;
	private JCheckBox genomeInstabilityCheckBox, tumorPromotingInflammationCheckBox;
	
	private JCheckBox individualHallmarkFileCheckBox, combinedHallmarkFileCheckBox;
	private JTextField fileDestinationTextField;
	private JComboBox<String> exportFileYesNoComboBox;

	private String CANCEL;
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

	private boolean HALLMARK_PROLIFERATION_VIEW;
	private boolean HALLMARK_GROWTH_REPRESSOR_VIEW;
	private boolean HALLMARK_APOPTOSIS_VIEW;
	private boolean HALLMARK_REPLICATIVE_IMMORTALITY_VIEW;
	private boolean HALLMARK_ANGIOGENESIS_VIEW;
	private boolean HALLMARK_METASTASIS_VIEW;
	private boolean HALLMARK_METABOLISM_VIEW;
	private boolean HALLMARK_IMMUNE_DESTRUCTION_VIEW;
	private boolean HALLMARK_GENOME_INSTABILITY_VIEW;
	private boolean HALLMARK_TUMOR_PROMOTING_INFLAMMATION_VIEW;

	private String EXPORT_HALLMARK_NODE_FILE="Export nodes of selected hallmark to file";
	private String INDIVIDUAL_HALLMARK_FILE="Export nodes of individual hallmarks";
	private String COMBINED_HALLMARK_FILE="Export nodes of combined hallmarks";
	
	private stringConstant staticString=new stringConstant();
	protected final static String VISUALIZE = "Visualize network";
	private String display;
	private boolean performVisualization=false;
	
	private String outputDestination;
	private boolean individualHallmarkFiles=false;
	private boolean combinedHallmarkFile=false;
	private boolean outputFile=false;

	private String folder;
	
	public visualizeHallmarkDialog(ArrayList<String> selectedHallmark, String sysFolder)
	{
		//initialize constants
		CANCEL=staticString.getCancel();
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
		//initialize variables		
		updateHallmarkView(selectedHallmark);
		folder=sysFolder;
		
		//set JDialog properties
		setTitle("Visualize Cancer Hallmarks");
		setSize(600,420);
		setLocationRelativeTo(null);
		setLayout(new BorderLayout());
		setResizable(false);
		setModal(true);

		//configure components in JDialog
		initializeComponents();
	}

	private void updateHallmarkView(ArrayList<String> selectedHallmark)
	{
		if(selectedHallmark.contains(HALLMARK_PROLIFERATION_DB)==true)
			HALLMARK_PROLIFERATION_VIEW=true;
		else
			HALLMARK_PROLIFERATION_VIEW=false;
		if(selectedHallmark.contains(HALLMARK_GROWTH_REPRESSOR_DB)==true)
			HALLMARK_GROWTH_REPRESSOR_VIEW=true;
		else
			HALLMARK_GROWTH_REPRESSOR_VIEW=false;
		if(selectedHallmark.contains(HALLMARK_APOPTOSIS_DB)==true)
			HALLMARK_APOPTOSIS_VIEW=true;
		else
			HALLMARK_APOPTOSIS_VIEW=false;
		if(selectedHallmark.contains(HALLMARK_REPLICATIVE_IMMORTALITY_DB)==true)
			HALLMARK_REPLICATIVE_IMMORTALITY_VIEW=true;
		else
			HALLMARK_REPLICATIVE_IMMORTALITY_VIEW=false;
		if(selectedHallmark.contains(HALLMARK_ANGIOGENESIS_DB)==true)
			HALLMARK_ANGIOGENESIS_VIEW=true;
		else
			HALLMARK_ANGIOGENESIS_VIEW=false;
		if(selectedHallmark.contains(HALLMARK_METASTASIS_DB)==true)
			HALLMARK_METASTASIS_VIEW=true;
		else
			HALLMARK_METASTASIS_VIEW=false;
		if(selectedHallmark.contains(HALLMARK_METABOLISM_DB)==true)
			HALLMARK_METABOLISM_VIEW=true;
		else
			HALLMARK_METABOLISM_VIEW=false;
		if(selectedHallmark.contains(HALLMARK_IMMUNE_DESTRUCTION_DB)==true)
			HALLMARK_IMMUNE_DESTRUCTION_VIEW=true;
		else
			HALLMARK_IMMUNE_DESTRUCTION_VIEW=false;
		if(selectedHallmark.contains(HALLMARK_GENOME_INSTABILITY_DB)==true)
			HALLMARK_GENOME_INSTABILITY_VIEW=true;
		else
			HALLMARK_GENOME_INSTABILITY_VIEW=false;
		if(selectedHallmark.contains(HALLMARK_TUMOR_PROMOTING_INFLAMMATION_DB)==true)
			HALLMARK_TUMOR_PROMOTING_INFLAMMATION_VIEW=true;
		else
			HALLMARK_TUMOR_PROMOTING_INFLAMMATION_VIEW=false;
	}

	private ActionListener actionListener = new ActionListener() 
	{
		public void actionPerformed(ActionEvent act) {
			if (act.getActionCommand() == VISUALIZE)
				visualize_actionPerformed();
			if (act.getActionCommand() == CANCEL)
				cancel_actionPerformed();
		}
	};

	public boolean getViewHallmark_Proliferation()
	{
		return HALLMARK_PROLIFERATION_VIEW;
	}

	public boolean getViewHallmark_GrowthRepressor()
	{
		return HALLMARK_GROWTH_REPRESSOR_VIEW;
	}

	public boolean getViewHallmark_Apoptosis()
	{
		return HALLMARK_APOPTOSIS_VIEW;
	}

	public boolean getViewHallmark_ReplicativeImmortality()
	{
		return HALLMARK_REPLICATIVE_IMMORTALITY_VIEW;
	}

	public boolean getViewHallmark_Angiogenesis()
	{
		return HALLMARK_ANGIOGENESIS_VIEW;
	}

	public boolean getViewHallmark_Metastasis()
	{
		return HALLMARK_METASTASIS_VIEW;
	}

	public boolean getViewHallmark_Metabolism()
	{
		return HALLMARK_METABOLISM_VIEW;
	}

	public boolean getViewHallmark_ImmuneDestruction()
	{
		return HALLMARK_IMMUNE_DESTRUCTION_VIEW;
	}

	public boolean getViewHallmark_GenomeInstability()
	{
		return HALLMARK_GENOME_INSTABILITY_VIEW;
	}

	public boolean getViewHallmark_TumorPromotingInflammation()
	{
		return HALLMARK_TUMOR_PROMOTING_INFLAMMATION_VIEW;
	}

	private void initializeComponents()
	{
		settingPanel=new JPanel();
		buttonPanel=new JPanel();

		//create hallmarkPanel
		proliferationPanel=new JPanel();
		proliferationPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		proliferationCheckBox=new JCheckBox(HALLMARK_PROLIFERATION_GUI, HALLMARK_PROLIFERATION_VIEW);
		proliferationPanel.add(proliferationCheckBox);
		growthRepressorPanel=new JPanel();
		growthRepressorPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		growthRepressorCheckBox=new JCheckBox(HALLMARK_GROWTH_REPRESSOR_GUI, HALLMARK_GROWTH_REPRESSOR_VIEW);
		growthRepressorPanel.add(growthRepressorCheckBox);
		apoptosisPanel=new JPanel();
		apoptosisPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		apoptosisCheckBox=new JCheckBox(HALLMARK_APOPTOSIS_GUI, HALLMARK_APOPTOSIS_VIEW);
		apoptosisPanel.add(apoptosisCheckBox);
		replicativeImmortalityPanel=new JPanel();
		replicativeImmortalityPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		replicativeImmortalityCheckBox=new JCheckBox(HALLMARK_REPLICATIVE_IMMORTALITY_GUI, HALLMARK_REPLICATIVE_IMMORTALITY_VIEW);
		replicativeImmortalityPanel.add(replicativeImmortalityCheckBox);
		angiogenesisPanel=new JPanel();
		angiogenesisPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		angiogenesisCheckBox=new JCheckBox(HALLMARK_ANGIOGENESIS_GUI, HALLMARK_ANGIOGENESIS_VIEW);
		angiogenesisPanel.add(angiogenesisCheckBox);
		metastasisPanel=new JPanel();
		metastasisPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		metastasisCheckBox=new JCheckBox(HALLMARK_METASTASIS_GUI, HALLMARK_METASTASIS_VIEW);
		metastasisPanel.add(metastasisCheckBox);
		metabolismPanel=new JPanel();
		metabolismPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		metabolismCheckBox=new JCheckBox(HALLMARK_METABOLISM_GUI, HALLMARK_METABOLISM_VIEW);
		metabolismPanel.add(metabolismCheckBox);
		immuneDestructionPanel=new JPanel();
		immuneDestructionPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		immuneDestructionCheckBox=new JCheckBox(HALLMARK_IMMUNE_DESTRUCTION_GUI, HALLMARK_IMMUNE_DESTRUCTION_VIEW);
		immuneDestructionPanel.add(immuneDestructionCheckBox);
		genomeInstabilityPanel=new JPanel();
		genomeInstabilityPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		genomeInstabilityCheckBox=new JCheckBox(HALLMARK_GENOME_INSTABILITY_GUI, HALLMARK_GENOME_INSTABILITY_VIEW);
		genomeInstabilityPanel.add(genomeInstabilityCheckBox);
		tumorPromotingInflammationPanel=new JPanel();
		tumorPromotingInflammationPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		tumorPromotingInflammationCheckBox=new JCheckBox(HALLMARK_TUMOR_PROMOTING_INFLAMMATION_GUI, HALLMARK_TUMOR_PROMOTING_INFLAMMATION_VIEW);
		tumorPromotingInflammationPanel.add(tumorPromotingInflammationCheckBox);

		JPanel settingLeftPanel=new JPanel();
		JPanel settingRightPanel=new JPanel();
		settingLeftPanel.setLayout(new BoxLayout(settingLeftPanel, BoxLayout.Y_AXIS));
		settingRightPanel.setLayout(new BoxLayout(settingRightPanel, BoxLayout.Y_AXIS));

		settingLeftPanel.add(proliferationPanel);
		settingLeftPanel.add(growthRepressorPanel);
		settingLeftPanel.add(apoptosisPanel);
		settingLeftPanel.add(replicativeImmortalityPanel);
		settingLeftPanel.add(angiogenesisPanel);

		settingRightPanel.add(metastasisPanel);
		settingRightPanel.add(metabolismPanel);
		settingRightPanel.add(immuneDestructionPanel);
		settingRightPanel.add(genomeInstabilityPanel);
		settingRightPanel.add(tumorPromotingInflammationPanel);

		JPanel selectHallmarkLabelPanel=new JPanel();
		selectHallmarkLabelPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		JLabel selectHallmarkLabel=new JLabel("Select one or more hallmarks:");
		selectHallmarkLabelPanel.add(selectHallmarkLabel);

		JPanel hallmarkPanel=new JPanel();
		hallmarkPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		hallmarkPanel.add(settingLeftPanel);
		hallmarkPanel.add(settingRightPanel);

		settingPanel.setLayout(new BoxLayout(settingPanel, BoxLayout.Y_AXIS));
		settingPanel.add(selectHallmarkLabelPanel);
		settingPanel.add(hallmarkPanel);

		JPanel hallmarkListPanel=new JPanel();
		hallmarkListPanel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
		hallmarkListPanel.setPreferredSize(new Dimension(460, 200));
		hallmarkListPanel.setLayout(new BoxLayout(hallmarkListPanel, BoxLayout.Y_AXIS));
		hallmarkListPanel.add(settingPanel);

		//exportFilePanel
		JPanel exportFileYesNoPanel=new JPanel();
		exportFileYesNoPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		JLabel exportFileYesNoLabel=new JLabel(EXPORT_HALLMARK_NODE_FILE);
		exportFileYesNoComboBox=new JComboBox<String>();
		String[] exportFileYesNoOption=new String[2];
		exportFileYesNoOption[0]="NO";
		exportFileYesNoOption[1]="YES";
		DefaultComboBoxModel<String> exportFileYesNoComboBoxModel = new DefaultComboBoxModel<String>(exportFileYesNoOption);
		exportFileYesNoComboBox.setModel(exportFileYesNoComboBoxModel);
		exportFileYesNoComboBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e){
				if(e.getStateChange()==ItemEvent.SELECTED)
				{
					CardLayout cl = (CardLayout)(exportFileCardPanel.getLayout());
					String currLayout=(String)e.getItem();
				    cl.show(exportFileCardPanel, currLayout);
				}
			}
		});
		exportFileYesNoComboBox.setSelectedIndex(0);
		exportFileYesNoPanel.add(exportFileYesNoLabel);
		exportFileYesNoPanel.add(exportFileYesNoComboBox);
		
		JPanel exportFileOptionYesPanel=new JPanel();
		exportFileOptionYesPanel.setLayout(new BoxLayout(exportFileOptionYesPanel, BoxLayout.Y_AXIS));
		JLabel exportFileOptionLabel=new JLabel("File Export Option (** files will be exported to "+folder+" **):");
		JPanel exportFileOptionLabelPanel=new JPanel();
		exportFileOptionLabelPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		exportFileOptionLabelPanel.add(exportFileOptionLabel);
		JPanel individualHallmarkFilePanel=new JPanel();
		individualHallmarkFilePanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		individualHallmarkFileCheckBox=new JCheckBox(INDIVIDUAL_HALLMARK_FILE, false);
		individualHallmarkFilePanel.add(individualHallmarkFileCheckBox);
		JPanel combinedHallmarkFilePanel=new JPanel();
		combinedHallmarkFilePanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		combinedHallmarkFileCheckBox=new JCheckBox(COMBINED_HALLMARK_FILE, true);
		combinedHallmarkFilePanel.add(combinedHallmarkFileCheckBox);
		JPanel fileDestinationPanel=new JPanel();
		fileDestinationPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		JLabel fileDestinationLabel=new JLabel("Destination of file(s):");
		fileDestinationTextField= new JTextField(folder, 60);
		fileDestinationPanel.add(fileDestinationLabel);
		fileDestinationPanel.add(fileDestinationTextField);
		exportFileOptionYesPanel.add(exportFileOptionLabelPanel);
		exportFileOptionYesPanel.add(individualHallmarkFilePanel);
		exportFileOptionYesPanel.add(combinedHallmarkFilePanel);
		//exportFileOptionYesPanel.add(fileDestinationPanel);
		
		JPanel exportFileOptionNoPanel=new JPanel();
		exportFileOptionNoPanel.setLayout(new BoxLayout(exportFileOptionNoPanel, BoxLayout.Y_AXIS));
		
		exportFileCardPanel=new JPanel(new CardLayout());
		exportFileCardPanel.add(exportFileOptionYesPanel, "YES");
		exportFileCardPanel.add(exportFileOptionNoPanel, "NO");
		CardLayout cl = (CardLayout)(exportFileCardPanel.getLayout());
		cl.show(exportFileCardPanel, "NO");
		
		JPanel exportFilePanel=new JPanel();
		exportFilePanel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
		exportFilePanel.setPreferredSize(new Dimension(460, 50));
		exportFilePanel.setLayout(new BoxLayout(exportFilePanel, BoxLayout.Y_AXIS));
		exportFilePanel.add(exportFileYesNoPanel);
		exportFilePanel.add(exportFileCardPanel);
		//exportFilePanel.add(buttonPanel);
		
		//create buttonPanel
		JLabel displayLabel=new JLabel("Display");
		displayComboBox=new JComboBox<String>();
		String[] displayOption=new String[2];
		displayOption[0]=staticString.getOverlayCurrentGraph();
		//displayOption[1]=staticString.getInducedGraph();
		DefaultComboBoxModel<String> displayComboBoxModel = new DefaultComboBoxModel<String>(displayOption);
		displayComboBox.setModel(displayComboBoxModel);
		displayComboBox.setSelectedIndex(0);

		cancelButton= new JButton();
		cancelButton.setSize(50, 20);
		cancelButton.setText(CANCEL);
		cancelButton.setActionCommand(CANCEL);
		cancelButton.addActionListener(actionListener);

		visualizeButton = new JButton();
		visualizeButton.setSize(50, 20);
		visualizeButton.setText(VISUALIZE);
		visualizeButton.setActionCommand(VISUALIZE);
		visualizeButton.addActionListener(actionListener);

		buttonPanel= new JPanel();
		buttonPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		buttonPanel.add(displayLabel);
		buttonPanel.add(displayComboBox);
		buttonPanel.add(visualizeButton);
		buttonPanel.add(cancelButton);

		JPanel buttonEtchedPanel=new JPanel();
		buttonEtchedPanel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
		buttonEtchedPanel.setPreferredSize(new Dimension(460, 50));
		buttonEtchedPanel.setLayout(new BoxLayout(buttonEtchedPanel, BoxLayout.Y_AXIS));
		buttonEtchedPanel.add(buttonPanel);

		setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));
		add(hallmarkListPanel);
		add(exportFilePanel);
		add(buttonEtchedPanel);
	}

	private void visualize_actionPerformed(){
		if(proliferationCheckBox.isSelected())
			HALLMARK_PROLIFERATION_VIEW=true;
		else
			HALLMARK_PROLIFERATION_VIEW=false;
		if(growthRepressorCheckBox.isSelected())
			HALLMARK_GROWTH_REPRESSOR_VIEW=true;
		else
			HALLMARK_GROWTH_REPRESSOR_VIEW=false;
		if(apoptosisCheckBox.isSelected())
			HALLMARK_APOPTOSIS_VIEW=true;
		else
			HALLMARK_APOPTOSIS_VIEW=false;
		if(replicativeImmortalityCheckBox.isSelected())
			HALLMARK_REPLICATIVE_IMMORTALITY_VIEW=true;
		else
			HALLMARK_REPLICATIVE_IMMORTALITY_VIEW=false;
		if(angiogenesisCheckBox.isSelected())
			HALLMARK_ANGIOGENESIS_VIEW=true;
		else
			HALLMARK_ANGIOGENESIS_VIEW=false;
		if(metastasisCheckBox.isSelected())
			HALLMARK_METASTASIS_VIEW=true;
		else
			HALLMARK_METASTASIS_VIEW=false;
		if(metabolismCheckBox.isSelected())
			HALLMARK_METABOLISM_VIEW=true;
		else
			HALLMARK_METABOLISM_VIEW=false;
		if(immuneDestructionCheckBox.isSelected())
			HALLMARK_IMMUNE_DESTRUCTION_VIEW=true;
		else
			HALLMARK_IMMUNE_DESTRUCTION_VIEW=false;
		if(genomeInstabilityCheckBox.isSelected())
			HALLMARK_GENOME_INSTABILITY_VIEW=true;
		else
			HALLMARK_GENOME_INSTABILITY_VIEW=false;
		if(tumorPromotingInflammationCheckBox.isSelected())
			HALLMARK_TUMOR_PROMOTING_INFLAMMATION_VIEW=true;
		else
			HALLMARK_TUMOR_PROMOTING_INFLAMMATION_VIEW=false;
		performVisualization=true;
		display=displayComboBox.getSelectedItem().toString();
		outputDestination=fileDestinationTextField.getText();
		individualHallmarkFiles=individualHallmarkFileCheckBox.isSelected();
		combinedHallmarkFile=combinedHallmarkFileCheckBox.isSelected();
		if(validateOutputDestination(outputDestination)==true)
		{
			if(exportFileYesNoComboBox.getSelectedItem().toString().compareTo("YES")==0)
				outputFile=true;
			else
				outputFile=false;
		}
		else
			outputFile=false;
		setVisible(false);
	}

	private boolean validateOutputDestination(String dest)
	{
		File file=new File(dest);
		//Check if the file is a directory:
		if(file.isDirectory()==false)
		{
			JOptionPane.showMessageDialog(new JFrame(), "Destination path is not a valid directory. Unable to export files.", "ERROR", JOptionPane.INFORMATION_MESSAGE);
			return false;
		}
			//report file not directory error
		if(file.canWrite()==false)
		{
			//report file cannot be written error
			JOptionPane.showMessageDialog(new JFrame(), "No permission to write to destination folder. Unable to export files.", "ERROR", JOptionPane.INFORMATION_MESSAGE);
			return false;
		}
		return true;

	}
	
	private void cancel_actionPerformed(){
		setVisible(false);
	}

	public String getDisplay(){
		return display;
	}

	public boolean getPerformVisualization(){
		return performVisualization;
	}
	
	public String getOutputDestination()
	{
		return outputDestination;
	}
	public boolean getExportIndividualHallmarkFiles()
	{
		return individualHallmarkFiles;
	}
	public boolean getExportCombinedHallmarkFiles()
	{
		return combinedHallmarkFile;
	}
	public boolean getExportFiles()
	{
		return outputFile;
	}
}
