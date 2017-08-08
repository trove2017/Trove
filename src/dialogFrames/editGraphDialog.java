package dialogFrames;

//Imports are listed in full to show what's being used could just import javax.swing.* and java.awt.* etc..
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
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

import constants.stringConstant;
import database.postgreSQL;

@SuppressWarnings("serial")
//	Layout: 
//  |  - existingPanel                  		|	- selectSourcePanel (YLayout)                           
//  |    + removeNodeLabel						|		+ selectSourceNodeLabel
//	|	 + nodeList, nodeScrollPane				|		+ sourceListPanel (leftLayout)
//	|	 + removeNodeInfoLabel					|			* sourceList, sourceScrollPane
//  |    + editEdgeButton, removeNodeButton     |           * sourceButtonPanel (YLayout)
//  |  - addPanel								|				= selectSourceButton, deselectSourceButton
//  |    + nodePanel							|			* selectedSourceList, selectedSourceScrollPane
//	|	   * nodeLabel							|	- selectTargetPanel (YLayout)
//	|	   * nodeText							|		+ selectTargetNodeLabel
//  |    + idPanel								|		+ targetListPanel (leftLayout)
//	|	   * idLabel							|			* targetList, targetScrollPane
//	|	   * idText								|			* targetButtonPanel (YLayout)
//	|    + annotationPanel						|				= selectTargetButton, deselectTargetButton
//	|	   * annotationButton					|			* selectedTargetList, selectedTargetScrollPane
//	|    + buttonPanel 							|	- buttonPanel (leftLayout)
//	|	   * addNodeButton						|		+ addEdgesButton
//	|	   * doneButton							

public class editGraphDialog extends JDialog{
	private JDialog removeNodeDialog=new JDialog();
	private JPanel existingPanel, addPanel;
	private JPanel nodePanel, idPanel, buttonPanel;
	private JButton removeNodeButton, editNodeButton, annotationButton, addNodeButton, doneButton;
	private JLabel removeNodeLabel, removeNodeInfoLabel, removeNodeMoreInfoLabel, nodeLabel, idLabel;
	private JTextField nodeTextField, idTextField;
	private JList<String> nodeSelectionList;
	private ArrayList<String[]> nodeList;
	private JScrollPane nodeScrollPane;
	
	private String name, entrezID;
	private boolean UPDATE_GRAPH=false;
	
	private String DONE, CANCEL;
	private stringConstant staticString=new stringConstant();
	
	protected final static String ANNOTATE = "Add node annotation";
	protected final static String REMOVE_NODE = "Remove node(s)";
	protected final static String EDIT_NODE = "Edit edge(s)";
	protected final static String CONFIRM_REMOVE_NODE = "Confirm";
	protected final static String ADD_NODE = "Add node";
	
	postgreSQL postgreDB=new postgreSQL();
	
	public editGraphDialog(postgreSQL db)
	{
		//initialize constants
		DONE=staticString.getDone();
		CANCEL=staticString.getCancel();
		
		//initialize variables
		postgreDB=db;
		nodeList=postgreDB.getNetworkNode_nodeList();
		
		//set JDialog properties
		setTitle("Edit graph");
		setSize(440,460);
		setLocationRelativeTo(null);
		setResizable(false);
		setModal(true);

		//configure components in JDialog
		initializeComponents();
	}

	private ActionListener actionListener = new ActionListener() 
	{
		public void actionPerformed(ActionEvent act) {
			if (act.getActionCommand() == DONE)
				done_actionPerformed();
			if (act.getActionCommand() == CANCEL)
				cancel_actionPerformed();
			if (act.getActionCommand() == ADD_NODE)
				addNode_actionPerformed();
			if (act.getActionCommand() == REMOVE_NODE)
				confirmNodeRemoval_actionPerformed();
			if (act.getActionCommand() == CONFIRM_REMOVE_NODE)
				removeNode_actionPerformed();
			if (act.getActionCommand() == EDIT_NODE)
				editNode_actionPerformed();
			if (act.getActionCommand() == ANNOTATE)
				annotate_actionPerformed();
		}
	};

	private void initializeComponents()
	{
		existingPanel=new JPanel();
		existingPanel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
		existingPanel.setPreferredSize(new Dimension(250, 300));
		addPanel=new JPanel();
		addPanel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
		addPanel.setPreferredSize(new Dimension(250, 100));
		
		//create existingPanel
		//removeNodeLabel
		JPanel removeNodeLabelPanel=new JPanel();
		removeNodeLabel=new JLabel("Select node to edit (Ctrl+Alt for multiple selection):");
		removeNodeLabelPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		removeNodeLabelPanel.add(removeNodeLabel);
		//nodeList
		nodeSelectionList=new JList<String>();
		String[] formattedNodeList=new String[nodeList.size()];
		for(int i=0; i<nodeList.size(); i++)
		{
			String[] n=new String[2];
			n=nodeList.get(i);
			formattedNodeList[i]=n[1]+" ["+n[0]+"]";
		}
		nodeSelectionList.setListData(formattedNodeList);
		nodeScrollPane = new JScrollPane(nodeSelectionList);
		nodeSelectionList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		nodeScrollPane.setViewportView(nodeSelectionList);
		nodeScrollPane.setPreferredSize(new Dimension(200, 200));
		nodeScrollPane.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		//removeNodeInfoLabel
		JPanel removeNodeInfoLabelPanel=new JPanel();
		removeNodeInfoLabel=new JLabel("Note: All edges connected to selected node will be removed as well.");
		removeNodeInfoLabelPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		removeNodeInfoLabelPanel.add(removeNodeInfoLabel);
		JPanel removeNodeMoreInfoLabelPanel=new JPanel();
		removeNodeMoreInfoLabel=new JLabel("Multiple selection only applies for node removal.");
		removeNodeMoreInfoLabelPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		removeNodeMoreInfoLabelPanel.add(removeNodeMoreInfoLabel);
		//removeNode button
		JPanel removeNodeBottonPanel=new JPanel();
		removeNodeBottonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
		editNodeButton= new JButton();
		editNodeButton.setSize(50, 20);
		editNodeButton.setText(EDIT_NODE);
		editNodeButton.setActionCommand(EDIT_NODE);
		editNodeButton.addActionListener(actionListener);
		removeNodeBottonPanel.add(editNodeButton);
		removeNodeButton= new JButton();
		removeNodeButton.setSize(50, 20);
		removeNodeButton.setText(REMOVE_NODE);
		removeNodeButton.setActionCommand(REMOVE_NODE);
		removeNodeButton.addActionListener(actionListener);
		removeNodeBottonPanel.add(removeNodeButton);
		
		existingPanel.setLayout(new BoxLayout(existingPanel, BoxLayout.Y_AXIS));
		existingPanel.add(removeNodeLabelPanel);
		existingPanel.add(nodeScrollPane);
		existingPanel.add(removeNodeInfoLabelPanel);
		existingPanel.add(removeNodeMoreInfoLabelPanel);
		existingPanel.add(removeNodeBottonPanel);
		
		//create addPanel
		nodePanel=new JPanel();
		nodePanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		//node label
		nodeLabel=new JLabel("Name     ");
		nodeTextField=new JTextField();
		nodeTextField.setColumns(25);
		nodeTextField.setEditable(true);
		nodePanel.add(nodeLabel);
		nodePanel.add(nodeTextField);
		//node id
		idPanel=new JPanel();
		idPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		idLabel=new JLabel("Entrez ID");
		idTextField=new JTextField();
		idTextField.setColumns(16);
		idTextField.setEditable(true);
		addNodeButton= new JButton();
		addNodeButton.setSize(55, 20);
		addNodeButton.setText(ADD_NODE);
		addNodeButton.setActionCommand(ADD_NODE);
		addNodeButton.addActionListener(actionListener);
		idPanel.add(idLabel);
		idPanel.add(idTextField);
		idPanel.add(addNodeButton);
		//addNode and done button
		buttonPanel=new JPanel();
		buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
		annotationButton= new JButton();
		annotationButton.setSize(55, 20);
		annotationButton.setText(ANNOTATE);
		annotationButton.setActionCommand(ANNOTATE);
		annotationButton.addActionListener(actionListener);
		annotationButton.setEnabled(false);
		doneButton= new JButton();
		doneButton.setSize(50, 20);
		doneButton.setText(DONE);
		doneButton.setActionCommand(DONE);
		doneButton.addActionListener(actionListener);
		buttonPanel.add(annotationButton);
		buttonPanel.add(doneButton);
		
		addPanel.setLayout(new BoxLayout(addPanel, BoxLayout.Y_AXIS));
		addPanel.add(nodePanel);
		addPanel.add(idPanel);
		addPanel.add(buttonPanel);
		
		setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));
		add(existingPanel);
		add(addPanel);
	}
	
	private void addNode_actionPerformed(){
		name=nodeTextField.getText();
		entrezID=idTextField.getText();
		
		if(name==null || name.length()==0)
			JOptionPane.showMessageDialog(new JFrame(), "Please key in the name of the node.", "Error", JOptionPane.ERROR_MESSAGE);
		else
		{
			if(!(name==null || name.length()==0) && !(entrezID==null || entrezID.length()==0) && postgreDB.checkNodeNameExists(name)==true && postgreDB.checkNodeIDExists(entrezID)==true )
				JOptionPane.showMessageDialog(new JFrame(), "This node is not added because the node name ["+name+"] and id ["+entrezID+"] exist in the database", "Error", JOptionPane.ERROR_MESSAGE);
			else if(!(name==null || name.length()==0) && postgreDB.checkNodeNameExists(name)==true)
				JOptionPane.showMessageDialog(new JFrame(), "This node is not added because the node name ["+name+"] exists in the database", "Error", JOptionPane.ERROR_MESSAGE);
			else if(!(entrezID==null || entrezID.length()==0) && postgreDB.checkNodeIDExists(entrezID)==true)
				JOptionPane.showMessageDialog(new JFrame(), "This node is not added because the node id ["+entrezID+"] exists in the database", "Error", JOptionPane.ERROR_MESSAGE);
			else
			{
				postgreDB.addNewNode(entrezID, name);
				nodeList=postgreDB.getNetworkNode_nodeList();
				String[] formattedNodeList=new String[nodeList.size()];
				for(int i=0; i<nodeList.size(); i++)
				{
					String[] n=new String[2];
					n=nodeList.get(i);
					formattedNodeList[i]=n[1]+" ["+n[0]+"]";
				}
				nodeSelectionList.setListData(formattedNodeList);
				annotationButton.setEnabled(true);
				UPDATE_GRAPH=true;
			}
		}
	}

	private void editNode_actionPerformed(){
		int index=nodeSelectionList.getSelectedIndex();
		
		if(index!=-1)
		{
			String[] n=new String[2];
			n=nodeList.get(index);
			editEdgeDialog editEdge=new editEdgeDialog(n[1], postgreDB);
			editEdge.setVisible(true);
		}
	}
	
	private void confirmNodeRemoval_actionPerformed(){
		int[] indices=nodeSelectionList.getSelectedIndices();
		
		if(indices.length>0)
		{
			JPanel confirmationLabelPanel=new JPanel();
			confirmationLabelPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
			JLabel confirmationLabel=new JLabel("Are you sure you want to remove the following nodes and all their associated edges?");
			confirmationLabelPanel.add(confirmationLabel);
			JPanel nodePanel=new JPanel();
			nodePanel.setLayout(new FlowLayout(FlowLayout.LEFT));
			JTextField nodeTextField=new JTextField();
			String nodeStr="";
			for(int i=0; i<indices.length; i++)
			{
				String[] n=new String[2];
				n=nodeList.get(indices[i]);
				if(nodeStr.length()>0)
					nodeStr=nodeStr+", ";
				nodeStr=nodeStr+n[1]+"["+n[0]+"]";
			}
			nodeTextField.setText(nodeStr);
			nodeTextField.setEditable(false);
			JScrollPane nodeScrollPane = new JScrollPane(nodeTextField);
			nodeScrollPane.setViewportView(nodeTextField);
			nodeScrollPane.setPreferredSize(new Dimension(500, 80));
			nodeScrollPane.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
			nodePanel.add(nodeScrollPane);
		
			JPanel buttonPanel=new JPanel();
			buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
			JButton confirmButton= new JButton();
			confirmButton.setSize(55, 20);
			confirmButton.setText(CONFIRM_REMOVE_NODE);
			confirmButton.setActionCommand(CONFIRM_REMOVE_NODE);
			confirmButton.addActionListener(actionListener);
			JButton cancelButton= new JButton();
			cancelButton.setSize(50, 20);
			cancelButton.setText(CANCEL);
			cancelButton.setActionCommand(CANCEL);
			cancelButton.addActionListener(actionListener);
			buttonPanel.add(confirmButton);
			buttonPanel.add(cancelButton);
		
			removeNodeDialog=new JDialog();
			removeNodeDialog.setTitle("Confirmation of node(s) removal");
			removeNodeDialog.setSize(500,200);
			removeNodeDialog.setLocationRelativeTo(null);
			removeNodeDialog.setResizable(false);
			removeNodeDialog.setModal(true);
			removeNodeDialog.setLayout(new BoxLayout(removeNodeDialog.getContentPane(), BoxLayout.Y_AXIS));
			removeNodeDialog.add(confirmationLabelPanel);
			removeNodeDialog.add(nodePanel);
			removeNodeDialog.add(buttonPanel);
			removeNodeDialog.setVisible(true);
		}
	}
		
	private void removeNode_actionPerformed(){	
		removeNodeDialog.setVisible(false);
		
		int[] indices=nodeSelectionList.getSelectedIndices();
		for(int i=0; i<indices.length; i++)
		{
			String[] n=new String[2];
			n=nodeList.get(indices[i]);
			postgreDB.remove_node(n[1], n[0]);
		}
		
		nodeList=postgreDB.getNetworkNode_nodeList();
		String[] formattedNodeList=new String[nodeList.size()];
		for(int i=0; i<nodeList.size(); i++)
		{
			String[] n=new String[2];
			n=nodeList.get(i);
			formattedNodeList[i]=n[1]+" ["+n[0]+"]";
		}
		nodeSelectionList.setListData(formattedNodeList);
		UPDATE_GRAPH=true;
	}
	
	private void annotate_actionPerformed(){
		editAnnotationDialog editAnnotation=new editAnnotationDialog(name, postgreDB);
		editAnnotation.setVisible(true);
	}
	
	public boolean getUpdateGraph(){
		return UPDATE_GRAPH;
	}
	
	private void done_actionPerformed(){
		setVisible(false);
	}
	
	private void cancel_actionPerformed(){
		removeNodeDialog.setVisible(false);
	}
}
