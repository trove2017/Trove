package dialogFrames;

//Imports are listed in full to show what's being used could just import javax.swing.* and java.awt.* etc..
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import constants.stringConstant;
import dataType.targetCombination;
import database.postgreSQL;

@SuppressWarnings("serial")
//	Layout: 
//  |  - settingPanel                                             
//  |    + diseaseNodePanel                                        
//  |      * diseaseNodeList, diseaseNodeScrollPane                        
//	|	 + viewSettingPanel                                        
//	|      * settingComboBox
//  |  - buttonPanel
//  |    + visualizeButton
//  |    + cancelButton

public class visualizeTargetCombinationDialog extends JDialog{
	private JPanel settingPanel, includeDiseaseNodeCheckBoxPanel, buttonPanel;
	private JButton visualizeButton, cancelButton;
	private JCheckBox includeDiseaseNodeCheckBox;
	private JRadioButton viewOnCurrentGraphRButton, viewNodeNeighbourhoodRButton, viewTargetCombinationConnectionsRButton;
	private ButtonGroup viewRButtonGroup=new ButtonGroup();
	private JList<String> targetCombinationList;
	private JScrollPane targetCombinationScrollPane;
	private ArrayList<String> listOfTargetCombination=new ArrayList<String>();
	private String selectedView;
	private boolean includeDiseaseNode;
	private String CANCEL;
	private boolean performVisualization=false;
	private postgreSQL postgreDB;
	private ArrayList<String> bestTargetCombi=new ArrayList<String>();
	private targetCombination tc = new targetCombination();
	private int selectedTCIndex=-1; 
	private stringConstant staticString=new stringConstant();
	
	protected final static String VISUALIZE = "Visualize target combination";
	//protected final static String VIEW_ON_CURRENT_GRAPH = "View on current graph";
	protected final static String VIEW_NODE_NEIGHBOURHOOD = "View node neighbourhood";
	protected final static String VIEW_TARGET_COMBINATION_CONNECTIONS = "View target combination connections";

	public visualizeTargetCombinationDialog(ArrayList<String> bestCombi, postgreSQL DB)
	{
		//initialize constants
		CANCEL=staticString.getCancel();
		postgreDB=DB;
		bestTargetCombi=bestCombi;
		
		//set JDialog properties
		setTitle("Visualize Target Combinations");
		setSize(550,400);
		setLocationRelativeTo(null);
		setLayout(new BorderLayout());
		setResizable(false);
		setModal(true);

		//configure components in JDialog
		initializeComponents();
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

	private void initializeComponents()
	{
		settingPanel=new JPanel();
		includeDiseaseNodeCheckBoxPanel=new JPanel();
		buttonPanel=new JPanel();

		//create targetCombinationPanel
		JPanel targetCombinationPanel=new JPanel();
		targetCombinationPanel.setLayout(new BoxLayout(targetCombinationPanel, BoxLayout.Y_AXIS));
		JLabel targetCombinationLabel=new JLabel();
		targetCombinationLabel.setText("Predicted Target Combination List");
		JPanel targetCombinationLabelPanel=new JPanel();
		targetCombinationLabelPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		targetCombinationLabelPanel.add(targetCombinationLabel);
		targetCombinationList=new JList<String>();
		targetCombinationScrollPane=new JScrollPane();
		targetCombinationList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		tc=postgreDB.getNetworkViewNode_getPredictedTargetCombiFromTempCandidate(bestTargetCombi);
		for(int i=0; i<tc.size(); i++)
		{
			ArrayList<String> t=tc.getTargetCombination(i);
			Double score=tc.getOffTargetScore(i);
			BigDecimal bd = new BigDecimal(score);
			bd = bd.round(new MathContext(3));
			double rounded = bd.doubleValue();
			String itemStr="["+rounded+"]";
			if(t.size()<=3)
			{
				for(int j=0; j<t.size(); j++)
				{
					itemStr=itemStr+" "+t.get(j);
					if(j<t.size()-1)
						itemStr=itemStr+",";
				}
			}
			else
			{
				for(int j=0; j<3; j++)
					itemStr=itemStr+" "+t.get(j)+",";
				itemStr=itemStr+"...";
			}
			listOfTargetCombination.add(itemStr);
		}		
		String[] javaArr=listOfTargetCombination.toArray(new String[listOfTargetCombination.size()]);
		targetCombinationList.setListData(javaArr);
		targetCombinationScrollPane = new JScrollPane(targetCombinationList);
		targetCombinationScrollPane.setViewportView(targetCombinationList);
		targetCombinationScrollPane.setPreferredSize(new Dimension(250, 250));
		targetCombinationScrollPane.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		targetCombinationPanel.add(targetCombinationLabelPanel);
		targetCombinationPanel.add(targetCombinationScrollPane);
		
		JPanel RButtonPanel=new JPanel();
		/*JPanel viewOnCurrentGraphRButtonPanel=new JPanel();
		viewOnCurrentGraphRButtonPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		viewOnCurrentGraphRButton=new JRadioButton(VIEW_ON_CURRENT_GRAPH);
		viewOnCurrentGraphRButton.setActionCommand(VIEW_ON_CURRENT_GRAPH);
		viewOnCurrentGraphRButton.addActionListener(actionListener);
		viewOnCurrentGraphRButton.setSelected(true);
		viewOnCurrentGraphRButtonPanel.add(viewOnCurrentGraphRButton);
		*/
		JPanel viewNodeNeighbourhoodRButtonPanel=new JPanel();
		viewNodeNeighbourhoodRButtonPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		viewNodeNeighbourhoodRButton=new JRadioButton(VIEW_NODE_NEIGHBOURHOOD);
		viewNodeNeighbourhoodRButton.setActionCommand(VIEW_NODE_NEIGHBOURHOOD);
		viewNodeNeighbourhoodRButton.setSelected(true);
		viewNodeNeighbourhoodRButton.addActionListener(actionListener);
		viewNodeNeighbourhoodRButtonPanel.add(viewNodeNeighbourhoodRButton);
		JPanel viewTargetCombinationConnectionsRButtonPanel=new JPanel();
		viewTargetCombinationConnectionsRButtonPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		viewTargetCombinationConnectionsRButton=new JRadioButton(VIEW_TARGET_COMBINATION_CONNECTIONS);
		viewTargetCombinationConnectionsRButton.setActionCommand(VIEW_TARGET_COMBINATION_CONNECTIONS);
		viewTargetCombinationConnectionsRButton.addActionListener(actionListener);
		viewTargetCombinationConnectionsRButtonPanel.add(viewTargetCombinationConnectionsRButton);
		viewRButtonGroup.add(viewOnCurrentGraphRButton);
		viewRButtonGroup.add(viewNodeNeighbourhoodRButton);
		viewRButtonGroup.add(viewTargetCombinationConnectionsRButton);
		RButtonPanel.setLayout(new BoxLayout(RButtonPanel, BoxLayout.Y_AXIS));
		//RButtonPanel.add(viewOnCurrentGraphRButtonPanel);
		RButtonPanel.add(viewNodeNeighbourhoodRButtonPanel);
		RButtonPanel.add(viewTargetCombinationConnectionsRButtonPanel);

		settingPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		settingPanel.add(targetCombinationPanel);
		settingPanel.add(RButtonPanel);

		//create includeDiseaseNodeCheckBoxPanel
		includeDiseaseNodeCheckBox=new JCheckBox("Include disease node in view");
		includeDiseaseNodeCheckBoxPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		includeDiseaseNodeCheckBoxPanel.add(includeDiseaseNodeCheckBox);

		//create buttonPanel
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
		buttonPanel.setLayout(new GridLayout(1,0)); 
		buttonPanel.add(visualizeButton);
		buttonPanel.add(cancelButton);

		setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));
		add(settingPanel);
		add(includeDiseaseNodeCheckBoxPanel);
		add(buttonPanel);
	}

	private void visualize_actionPerformed(){
		selectedTCIndex=targetCombinationList.getSelectedIndex();
		if(selectedTCIndex!=-1)
		{
			if(tc.getTargetCombination(selectedTCIndex).size()==1 && includeDiseaseNodeCheckBox.isSelected()==false
					&& viewTargetCombinationConnectionsRButton.isSelected()==true)
				JOptionPane.showMessageDialog(new JFrame(), "Selected combination size is 1! Please check \"Include disease node in view\" option for viewing the target combination connections.", "Error", JOptionPane.ERROR_MESSAGE);
			else
			{
				//if(viewOnCurrentGraphRButton.isSelected()==true)
				//	selectedView=VIEW_ON_CURRENT_GRAPH;
				if(viewNodeNeighbourhoodRButton.isSelected()==true)
					selectedView=VIEW_NODE_NEIGHBOURHOOD;
				else
					selectedView=VIEW_TARGET_COMBINATION_CONNECTIONS;

				if(includeDiseaseNodeCheckBox.isSelected()==true)
					includeDiseaseNode=true;
				else
					includeDiseaseNode=false;
			
				performVisualization=true;
				setVisible(false);
			}
		}
		else
			JOptionPane.showMessageDialog(new JFrame(), "Please select a disease node from 'Disease Node List'", "Error", JOptionPane.ERROR_MESSAGE);
	}

	private void cancel_actionPerformed(){
		setVisible(false);
	}

	public String getSelectedView(){
		return selectedView;
	}

	public boolean getIncludeDiseaseNode(){
		return includeDiseaseNode;
	}

	public ArrayList<String> getSelectedTargetCombination(){
		return tc.getTargetCombination(selectedTCIndex);
	}
	
	public String getSelectedTCDiseaseNodeList(){
		return tc.getDiseaseNodeList(selectedTCIndex);
	}
	
	public boolean getPerformVisualization(){
		return performVisualization;
	}
}
