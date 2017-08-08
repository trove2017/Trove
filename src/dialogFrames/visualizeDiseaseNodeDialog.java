package dialogFrames;

//Imports are listed in full to show what's being used could just import javax.swing.* and java.awt.* etc..
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
import dataType.diseaseNode;

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

public class visualizeDiseaseNodeDialog extends JDialog{
	private JPanel settingPanel, checkBoxPanel, buttonPanel;
	private JButton visualizeButton, selectButton, deselectButton, cancelButton;
	private JCheckBox selectAllCheckBox;
	private JRadioButton viewOnCurrentGraphRButton, viewNodeNeighbourhoodRButton, viewDiseaseNodeConnectionsRButton;
	private ButtonGroup viewRButtonGroup=new ButtonGroup();
	private JList<String> diseaseNodeList, selectedDiseaseNodeList;
	private JScrollPane diseaseNodeScrollPane, selectedDiseaseNodeScrollPane;
	private ArrayList<String> listOfDiseaseNode=new ArrayList<String>();
	private ArrayList<String> listOfSelectedDiseaseNode=new ArrayList<String>();
	private diseaseNode dNodeList;
	private String selectedView;
	private String CANCEL;
	private boolean performVisualization=false;
	
	private stringConstant staticString=new stringConstant();
	
	protected final static String VISUALIZE = "Visualize disease node";
	protected final static String SELECT = "Select";
	protected final static String DESELECT = "Deselect";
	protected final static String SELECT_ALL = "Select all";
	//protected final static String VIEW_ON_CURRENT_GRAPH = "View on current graph";
	protected final static String VIEW_NODE_NEIGHBOURHOOD = "View node neighbourhood";
	protected final static String VIEW_DISEASE_NODE_CONNECTIONS = "View disease node connections";
	
	public visualizeDiseaseNodeDialog(diseaseNode nodeList)
	{
		//initialize constants
		dNodeList=nodeList;
		CANCEL=staticString.getCancel();
		
		//set JDialog properties
		setTitle("Visualize Disease Nodes");
		setSize(700,450);
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
			if (act.getActionCommand() == SELECT)
				select_actionPerformed();
			if (act.getActionCommand() == DESELECT)
				deselect_actionPerformed();
			if (act.getActionCommand() == SELECT_ALL)
				selectAll_actionPerformed();
			if (act.getActionCommand() == CANCEL)
				cancel_actionPerformed();
		}
	};

	private void initializeComponents()
	{
		settingPanel=new JPanel();
		checkBoxPanel=new JPanel();
		buttonPanel=new JPanel();
		
		//create diseaseNodePanel
		JPanel diseaseNodePanel=new JPanel();
		diseaseNodePanel.setLayout(new BoxLayout(diseaseNodePanel, BoxLayout.Y_AXIS));
		JLabel diseaseNodeLabel=new JLabel();
		diseaseNodeLabel.setText("Disease Node List");
		JPanel diseaseNodeLabelPanel=new JPanel();
		diseaseNodeLabelPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		diseaseNodeLabelPanel.add(diseaseNodeLabel);
		diseaseNodeList=new JList<String>();
		diseaseNodeScrollPane=new JScrollPane();
		diseaseNodeList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		listOfDiseaseNode=dNodeList.getNodeList();
		String[] javaArr=listOfDiseaseNode.toArray(new String[listOfDiseaseNode.size()]);
		diseaseNodeList.setListData(javaArr);
		diseaseNodeScrollPane = new JScrollPane(diseaseNodeList);
		diseaseNodeScrollPane.setViewportView(diseaseNodeList);
		diseaseNodeScrollPane.setPreferredSize(new Dimension(200, 250));
		diseaseNodeScrollPane.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		diseaseNodePanel.add(diseaseNodeLabelPanel);
		diseaseNodePanel.add(diseaseNodeScrollPane);
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
		//create selected selectedDiseaseNodePanel
		JPanel selectedDiseaseNodePanel=new JPanel();
		selectedDiseaseNodePanel.setLayout(new BoxLayout(selectedDiseaseNodePanel, BoxLayout.Y_AXIS));
		JLabel selectedDiseaseNodeLabel=new JLabel();
		selectedDiseaseNodeLabel.setText("Selected Disease Node List");
		JPanel selectedDiseaseNodeLabelPanel=new JPanel();
		selectedDiseaseNodeLabelPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		selectedDiseaseNodeLabelPanel.add(selectedDiseaseNodeLabel);
		selectedDiseaseNodeList=new JList<String>();
		selectedDiseaseNodeScrollPane=new JScrollPane();
		selectedDiseaseNodeList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		selectedDiseaseNodeList.setListData(listOfSelectedDiseaseNode.toArray(new String[listOfSelectedDiseaseNode.size()]));
		selectedDiseaseNodeScrollPane = new JScrollPane(selectedDiseaseNodeList);
		selectedDiseaseNodeScrollPane.setViewportView(selectedDiseaseNodeList);
		selectedDiseaseNodeScrollPane.setPreferredSize(new Dimension(200, 250));
		selectedDiseaseNodeScrollPane.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		selectedDiseaseNodePanel.add(selectedDiseaseNodeLabelPanel);
		selectedDiseaseNodePanel.add(selectedDiseaseNodeScrollPane);
		
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
		JPanel viewDiseaseNodeConnectionsRButtonPanel=new JPanel();
		viewDiseaseNodeConnectionsRButtonPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		viewDiseaseNodeConnectionsRButton=new JRadioButton(VIEW_DISEASE_NODE_CONNECTIONS);
		viewDiseaseNodeConnectionsRButton.setActionCommand(VIEW_DISEASE_NODE_CONNECTIONS);
		viewDiseaseNodeConnectionsRButton.addActionListener(actionListener);
		viewDiseaseNodeConnectionsRButtonPanel.add(viewDiseaseNodeConnectionsRButton);
		viewRButtonGroup.add(viewOnCurrentGraphRButton);
		viewRButtonGroup.add(viewNodeNeighbourhoodRButton);
		viewRButtonGroup.add(viewDiseaseNodeConnectionsRButton);
		RButtonPanel.setLayout(new BoxLayout(RButtonPanel, BoxLayout.Y_AXIS));
		//RButtonPanel.add(viewOnCurrentGraphRButtonPanel);
		RButtonPanel.add(viewNodeNeighbourhoodRButtonPanel);
		RButtonPanel.add(viewDiseaseNodeConnectionsRButtonPanel);
		
		settingPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		settingPanel.add(diseaseNodePanel);
		settingPanel.add(selectButtonPanel);
		settingPanel.add(selectedDiseaseNodePanel);
		settingPanel.add(RButtonPanel);
		
		//create checkBoxPanel
		selectAllCheckBox=new JCheckBox("Select all nodes");
		selectAllCheckBox.setActionCommand(SELECT_ALL);
		selectAllCheckBox.addActionListener(actionListener);
		checkBoxPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		checkBoxPanel.add(selectAllCheckBox);
		
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
		add(checkBoxPanel);
		add(buttonPanel);
	}

	private void visualize_actionPerformed(){
		if(listOfSelectedDiseaseNode.size()>0)
		{
			//if(viewOnCurrentGraphRButton.isSelected()==true)
			//	selectedView=VIEW_ON_CURRENT_GRAPH;
			if(viewNodeNeighbourhoodRButton.isSelected()==true)
				selectedView=VIEW_NODE_NEIGHBOURHOOD;
			else
				selectedView=VIEW_DISEASE_NODE_CONNECTIONS;
			performVisualization=true;
			setVisible(false);
		}
		else
			JOptionPane.showMessageDialog(new JFrame(), "Please select a disease node from 'Disease Node List'", "Error", JOptionPane.ERROR_MESSAGE);
	}

	private void select_actionPerformed(){
		int selectedIndex=diseaseNodeList.getSelectedIndex();
		if(selectedIndex!=-1)
		{
			String n=listOfDiseaseNode.get(selectedIndex);
			if(listOfSelectedDiseaseNode.contains(n)==false)
				listOfSelectedDiseaseNode.add(n);
			selectedDiseaseNodeList.setListData(listOfSelectedDiseaseNode.toArray(new String[listOfSelectedDiseaseNode.size()]));
		}
		updateSelectDeselectButtonStatus();
	}
	
	private void deselect_actionPerformed(){
		int selectedIndex=selectedDiseaseNodeList.getSelectedIndex();
		if(selectedIndex!=-1)
		{
			String n=listOfSelectedDiseaseNode.get(selectedIndex);
			if(listOfSelectedDiseaseNode.contains(n)==true)
				listOfSelectedDiseaseNode.remove(n);
			selectedDiseaseNodeList.setListData(listOfSelectedDiseaseNode.toArray(new String[listOfSelectedDiseaseNode.size()]));
		}
		updateSelectDeselectButtonStatus();
	}
	
	private void selectAll_actionPerformed(){
		if(selectAllCheckBox.isSelected()==true)
		{
			for(int i=0; i<listOfDiseaseNode.size(); i++)
			{
				String n=listOfDiseaseNode.get(i);
				if(listOfSelectedDiseaseNode.contains(n)==false)
					listOfSelectedDiseaseNode.add(n);
			}
			selectedDiseaseNodeList.setListData(listOfSelectedDiseaseNode.toArray(new String[listOfSelectedDiseaseNode.size()]));
		}
		updateSelectDeselectButtonStatus();
	}
	
	private void updateSelectDeselectButtonStatus()
	{
		if(listOfSelectedDiseaseNode.size()==0)
		{
			deselectButton.setEnabled(false);
			selectButton.setEnabled(true);
			selectAllCheckBox.setSelected(false);
		}
		else if(listOfSelectedDiseaseNode.size()==listOfDiseaseNode.size())
		{
			deselectButton.setEnabled(true);
			selectButton.setEnabled(false);
			selectAllCheckBox.setSelected(true);
		}
		else
		{
			deselectButton.setEnabled(true);
			selectButton.setEnabled(true);
			selectAllCheckBox.setSelected(false);
		}
	}
	
	private void cancel_actionPerformed(){
		setVisible(false);
	}
	
	public ArrayList<String> getSelectedDiseaseNodeList(){
		return listOfSelectedDiseaseNode;
	}
	
	public String getSelectedView(){
		return selectedView;
	}
	
	public boolean getPerformVisualization(){
		return performVisualization;
	}
}
