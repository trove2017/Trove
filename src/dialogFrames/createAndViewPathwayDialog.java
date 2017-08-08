package dialogFrames;

//Imports are listed in full to show what's being used could just import javax.swing.* and java.awt.* etc..
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

import constants.stringConstant;

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

public class createAndViewPathwayDialog extends JDialog{
	private JPanel settingPanel, buttonPanel;
	private JButton selectButton, deselectButton, visualizeButton, cancelButton;
	private JList<String> nodeList, selectedNodeList;
	private JScrollPane nodeListScrollPane, selectedNodeListScrollPane;
	private JComboBox<String> displayComboBox;
	private ArrayList<String> selectedNode;
	private ArrayList<String> allNodeList;
	private String CANCEL;
	private boolean performVisualization=false;
	private String display;
	private stringConstant staticString=new stringConstant();
	protected final static String VISUALIZE = "Visualize pathway";
	protected final static String SELECT = "Select";
	protected final static String DESELECT = "Deselect";
	
	public createAndViewPathwayDialog(ArrayList<String> nList)
	{
		//initialize constants
		CANCEL=staticString.getCancel();
		allNodeList=nList;
		selectedNode=new ArrayList<String>();
		//set JDialog properties
		setTitle("Visualize Pathway of Selected Nodes");
		setSize(480,370);
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
			if (act.getActionCommand() == CANCEL)
				cancel_actionPerformed();
		}
	};

	private void initializeComponents()
	{
		settingPanel=new JPanel();
		buttonPanel=new JPanel();
		
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
		nodeListScrollPane.setPreferredSize(new Dimension(200, 250));
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
		selectButtonPanel.add(selectButton);
		selectButtonPanel.add(emptyLabel);
		selectButtonPanel.add(deselectButton);
		//create selectedNodePanel
		JPanel selectedNodePanel=new JPanel();
		selectedNodePanel.setLayout(new BoxLayout(selectedNodePanel, BoxLayout.Y_AXIS));
		JLabel selectedNodeLabel=new JLabel();
		selectedNodeLabel.setText("Selected Node List");
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
		selectedNodeListScrollPane.setPreferredSize(new Dimension(200, 250));
		selectedNodeListScrollPane.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		selectedNodePanel.add(selectedNodeLabelPanel);
		selectedNodePanel.add(selectedNodeListScrollPane);
		
		settingPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		settingPanel.add(nodePanel);
		settingPanel.add(selectButtonPanel);
		settingPanel.add(selectedNodePanel);
		//create buttonPanel
		JLabel displayLabel=new JLabel("Display");
		displayComboBox=new JComboBox<String>();
		String[] displayOption=new String[2];
		displayOption[0]=staticString.getInducedGraph();
		displayOption[1]=staticString.getInduced1HopGraph();
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

		setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));
		add(settingPanel);
		add(buttonPanel);
	}

	private void visualize_actionPerformed(){
		if(selectedNode.size()>0)
		{
			display=displayComboBox.getSelectedItem().toString();
			performVisualization=true;
			setVisible(false);
		}
		else
			JOptionPane.showMessageDialog(new JFrame(), "Please select nodes from 'Node List' for creating the pathway.", "Error", JOptionPane.ERROR_MESSAGE);
	}

	private void cancel_actionPerformed(){
		setVisible(false);
	}
	
	private void select_actionPerformed(){
		int[] selectedIndices=nodeList.getSelectedIndices();
		if(selectedIndices.length>0)
		{
			for(int i=0; i<selectedIndices.length; i++)
			{
				String n=allNodeList.get(selectedIndices[i]);
				if(selectedNode.contains(n)==false)
					selectedNode.add(n);
				selectedNodeList.setListData(selectedNode.toArray(new String[selectedNode.size()]));
			}
		}
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
	}
	
	public boolean getPerformVisualization(){
		return performVisualization;
	}
	
	public String getDisplay(){
		return display;
	}
	
	public ArrayList<String> getSelectedNodes(){
		return selectedNode;
	}
}
