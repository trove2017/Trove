package dialogFrames;

//Imports are listed in full to show what's being used could just import javax.swing.* and java.awt.* etc..
import graph.edge;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
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
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.EtchedBorder;

import constants.stringConstant;
import database.postgreSQL;

@SuppressWarnings("serial")
//	Layout: 
//  |  - existingPanel                            
//  |    + edgeLabel
//	|	 + edgePanel (edgeList, edgeScrollPane)				
//	|	 + removeEdgeInfoLabel					
//  |    + editEdgeAnnotationButton, removeEdgeButton
//  |  - addPanel								
//  |    + selectSourcePanel (YLayout)			
//	|	   * selectSourceNodeLabel				
//	|	   * sourceListPanel(leftLayout)		
//  |    	 = sourceList, sourceScrollPane
//	|	   * sourceButtonPanel (YLayout)		
//	|	     = selectSourceButton, deselectSourceButton	
//	|      * selectedSourceListPanel(leftLayout)+ annotationPanel		
//	|	     = selectedSourceList, selectedSourceScrollPane		
//	|    + selectTargetPanel (YLayout)
//	|	   * selectTargetNodeLabel				
//	|	   * targetListPanel(leftLayout)		
//	|    	 = targetList, targetScrollPane
//	|	   * targetButtonPanel (YLayout)		
//	|	     = selectTargetButton, deselectTargetButton	
//	|      * selectedTargetListPanel(leftLayout)+ annotationPanel		
//	|	     = selectedTargetList, selectedTargetScrollPane
//	|	 + buttonPanel (leftLayout)
//	|	   * addEdgeButton				
//	|	   * doneButton							

public class editEdgeDialog extends JDialog{
	private JDialog removeEdgeDialog=new JDialog();
	private JPanel existingPanel, addPanel;
	private JPanel selectSourcePanel, sourceListPanel, sourceButtonPanel, selectedSourceListPanel;
	private JPanel selectTargetPanel, targetListPanel, targetButtonPanel, selectedTargetListPanel, buttonPanel;
	private JButton editEdgeAnnotationButton, removeEdgeButton, selectSourceButton, deselectSourceButton;
	private JButton selectTargetButton, deselectTargetButton, annotationButton, addEdgeButton, doneButton;
	private JLabel edgeLabel, edgeInfoLabel, sourceLabel, selectSourceLabel, selectedSourceLabel;
	private JLabel targetLabel, selectTargetLabel, selectedTargetLabel, toEdgeTypeLabel, fromEdgeTypeLabel;
	private String[] toEdgeTypeOption, fromEdgeTypeOption;
	private JComboBox<String> toEdgeTypeComboBox, fromEdgeTypeComboBox;
	private DefaultComboBoxModel<String> toEdgeTypeComboBoxModel, fromEdgeTypeComboBoxModel;
	private JList<String> edgeSelectionList, sourceSelectionList, targetSelectionList, selectedSourceSelectionList, selectedTargetSelectionList;
	private ArrayList<String[]> nodeList;
	private ArrayList<edge> edgeList;
	private JScrollPane edgeScrollPane, sourceScrollPane, selectedSourceScrollPane, targetScrollPane, selectedTargetScrollPane;
	
	private String DONE, CANCEL;
	private stringConstant staticString=new stringConstant();
	
	private String currNode;
	private ArrayList<String[]> selectedSource, selectedTarget;
	
	protected final static String ANNOTATE = "Add edge annotation";
	protected final static String REMOVE_EDGE = "Remove";
	protected final static String EDIT_EDGE_ANNOTATION = "Edit Annotation";
	protected final static String CONFIRM_REMOVE_EDGE = "Confirm";
	protected final static String ADD_EDGE = "Add edge";
	protected final static String SELECT_SOURCE = "Select source";
	protected final static String DESELECT_SOURCE = "Deselect source";
	protected final static String SELECT_TARGET = "Select target";
	protected final static String DESELECT_TARGET = "Deselect target";
	
	postgreSQL postgreDB=new postgreSQL();
	
	public editEdgeDialog(String node, postgreSQL db)
	{
		//initialize constants
		DONE=staticString.getDone();
		CANCEL=staticString.getCancel();
		
		//initialize variables
		currNode=node;
		postgreDB=db;
		nodeList=postgreDB.getNetworkNode_nodeList();
		edgeList=postgreDB.getNetworkEdge_edgeListOfNode(node);
		selectedSource=new ArrayList<String[]>();
		selectedTarget=new ArrayList<String[]>();
		
		//set JDialog properties
		setTitle("Edit edge(s) of "+node);
		setSize(500,650);
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
			if (act.getActionCommand() == REMOVE_EDGE)
				confirmEdgeRemoval_actionPerformed();
			///if (act.getActionCommand() == CONFIRM_REMOVE_EDGE)
			//	removeEdge_actionPerformed();
			if (act.getActionCommand() == SELECT_SOURCE)
				selectSource_actionPerformed();
			if (act.getActionCommand() == DESELECT_SOURCE)
				deselectSource_actionPerformed();
			if (act.getActionCommand() == SELECT_TARGET)
				selectTarget_actionPerformed();
			if (act.getActionCommand() == DESELECT_TARGET)
				deselectTarget_actionPerformed();
			if (act.getActionCommand() == ADD_EDGE)
				addEdge_actionPerformed();
		}
	};

	private void initializeComponents()
	{
		existingPanel=new JPanel();
		existingPanel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
		existingPanel.setPreferredSize(new Dimension(250, 100));
		addPanel=new JPanel();
		addPanel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
		addPanel.setPreferredSize(new Dimension(250, 250));
		
		//create existingPanel
		//edgeLabel
		JPanel edgeLabelPanel=new JPanel();
		edgeLabel=new JLabel("Select edge to edit (Ctrl+Alt for multiple selection):");
		edgeLabelPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		edgeLabelPanel.add(edgeLabel);
		//edgeList
		edgeSelectionList=new JList<String>();
		String[] formattedEdgeList=new String[edgeList.size()];
		for(int i=0; i<edgeList.size(); i++)
		{
			edge e=edgeList.get(i);
			String edgeArrow="";
			if(e.getType().compareTo(staticString.getDBEdgePos())==0)
				edgeArrow="->";
			else if(e.getType().compareTo(staticString.getDBEdgeNeg())==0)
				edgeArrow="-|";
			else
				edgeArrow="--";
			formattedEdgeList[i]=e.getSourceName()+edgeArrow+e.getTargetName();
		}
		edgeSelectionList.setListData(formattedEdgeList);
		edgeScrollPane = new JScrollPane(edgeSelectionList);
		edgeSelectionList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		edgeScrollPane.setViewportView(edgeSelectionList);
		edgeScrollPane.setPreferredSize(new Dimension(200, 100));
		edgeScrollPane.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		//edgeInfoLabel
		JPanel edgeInfoLabelPanel=new JPanel();
		edgeInfoLabel=new JLabel("Note: Multiple selection only applies for edge removal.");
		edgeInfoLabelPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		edgeInfoLabelPanel.add(edgeInfoLabel);
		//removeEdge button
		JPanel removeEdgeBottonPanel=new JPanel();
		removeEdgeBottonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
		editEdgeAnnotationButton= new JButton();
		editEdgeAnnotationButton.setSize(50, 20);
		editEdgeAnnotationButton.setText(EDIT_EDGE_ANNOTATION);
		editEdgeAnnotationButton.setActionCommand(EDIT_EDGE_ANNOTATION);
		editEdgeAnnotationButton.addActionListener(actionListener);
		removeEdgeButton= new JButton();
		removeEdgeButton.setSize(50, 20);
		removeEdgeButton.setText(REMOVE_EDGE);
		removeEdgeButton.setActionCommand(REMOVE_EDGE);
		removeEdgeButton.addActionListener(actionListener);
		//removeEdgeBottonPanel.add(editEdgeAnnotationButton); //to add this capability
		removeEdgeBottonPanel.add(removeEdgeButton);
		
		existingPanel.setLayout(new BoxLayout(existingPanel, BoxLayout.Y_AXIS));
		existingPanel.add(edgeLabelPanel);
		existingPanel.add(edgeScrollPane);
		existingPanel.add(edgeInfoLabelPanel);
		existingPanel.add(removeEdgeBottonPanel);
		
		//selectSource
		//selectSourceLabel
		JPanel sourceLabelPanel=new JPanel();
		sourceLabel=new JLabel("<<Source node>>");
		sourceLabelPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		sourceLabelPanel.add(sourceLabel);
		//selectSourceList
		selectSourcePanel=new JPanel();
		selectSourcePanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		//sourceSelection
		JPanel selectSourceLabelPanel=new JPanel();
		selectSourceLabelPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		selectSourceLabel=new JLabel("Select source node:");
		selectSourceLabelPanel.add(selectSourceLabel);		
		sourceListPanel=new JPanel();
		sourceListPanel.setLayout(new BoxLayout(sourceListPanel, BoxLayout.Y_AXIS));
		sourceSelectionList=new JList<String>();
		String[] formattedSourceList=new String[nodeList.size()];
		for(int i=0; i<nodeList.size(); i++)
		{
			String[] n=new String[2];
			n=nodeList.get(i);
			if(n[1].compareTo(currNode)!=0)
				formattedSourceList[i]=n[1]+" ["+n[0]+"]";
		}
		sourceSelectionList.setListData(formattedSourceList);
		sourceScrollPane = new JScrollPane(sourceSelectionList);
		sourceSelectionList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		sourceScrollPane.setViewportView(sourceSelectionList);
		sourceScrollPane.setPreferredSize(new Dimension(200, 100));
		sourceScrollPane.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		sourceListPanel.add(selectSourceLabelPanel);
		sourceListPanel.add(sourceScrollPane);
		//sourceButton
		sourceButtonPanel=new JPanel();
		sourceButtonPanel.setLayout(new BoxLayout(sourceButtonPanel, BoxLayout.Y_AXIS));
		selectSourceButton= new JButton(">");
		selectSourceButton.setSize(50, 20);
		selectSourceButton.setActionCommand(SELECT_SOURCE);
		selectSourceButton.addActionListener(actionListener);
		JLabel emptyLabel=new JLabel(" ");
		emptyLabel.setSize(50, 60);
		deselectSourceButton= new JButton("<");
		deselectSourceButton.setSize(50, 20);
		deselectSourceButton.setActionCommand(DESELECT_SOURCE);
		deselectSourceButton.addActionListener(actionListener);
		sourceButtonPanel.add(selectSourceButton);
		sourceButtonPanel.add(emptyLabel);
		sourceButtonPanel.add(deselectSourceButton);
		//selectedSourceList
		JPanel selectedSourceLabelPanel=new JPanel();
		selectedSourceLabelPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		selectedSourceLabel=new JLabel("Selected source node:");
		selectedSourceLabelPanel.add(selectedSourceLabel);		
		selectedSourceListPanel=new JPanel();
		selectedSourceListPanel.setLayout(new BoxLayout(selectedSourceListPanel, BoxLayout.Y_AXIS));
		selectedSourceSelectionList=new JList<String>();
		String[] formattedSelectedSourceList=new String[selectedSource.size()];
		if(selectedSource!=null && selectedSource.size()>0)
		{
			for(int i=0; i<selectedSource.size(); i++)
			{
				String[] n=new String[2];
				n=selectedSource.get(i);
				formattedSourceList[i]=n[1]+" ["+n[0]+"]";
			}
			selectedSourceSelectionList.setListData(formattedSelectedSourceList);
		}
		selectedSourceScrollPane = new JScrollPane(selectedSourceSelectionList);
		selectedSourceSelectionList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		selectedSourceScrollPane.setViewportView(selectedSourceSelectionList);
		selectedSourceScrollPane.setPreferredSize(new Dimension(200, 100));
		selectedSourceScrollPane.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		selectedSourceListPanel.add(selectedSourceLabelPanel);
		selectedSourceListPanel.add(selectedSourceScrollPane);
		
		selectSourcePanel.add(sourceListPanel);
		selectSourcePanel.add(sourceButtonPanel);
		selectSourcePanel.add(selectedSourceListPanel);
		//fromEdgeTypeComboBox
		JPanel fromEdgePanel=new JPanel();
		fromEdgePanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		fromEdgeTypeLabel=new JLabel("Edge type");
		fromEdgeTypeComboBox=new JComboBox<String>();
		fromEdgeTypeOption=new String[3];
		fromEdgeTypeOption[0]=staticString.getDBEdgePos();
		fromEdgeTypeOption[1]=staticString.getDBEdgeNeg();
		fromEdgeTypeOption[2]=staticString.getDBEdgePhy();
		fromEdgeTypeComboBoxModel = new DefaultComboBoxModel<String>(fromEdgeTypeOption);
		fromEdgeTypeComboBox.setModel(fromEdgeTypeComboBoxModel);
		fromEdgeTypeComboBox.setSelectedIndex(0);
		fromEdgePanel.add(fromEdgeTypeLabel);
		fromEdgePanel.add(fromEdgeTypeComboBox);
				
		//selectTarget
		//selectTargetLabel
		JPanel targetLabelPanel=new JPanel();
		targetLabel=new JLabel("<<Target node>>");
		targetLabelPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		targetLabelPanel.add(targetLabel);
		//selectTargetList
		selectTargetPanel=new JPanel();
		selectTargetPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		//targetSelection
		JPanel selectTargetLabelPanel=new JPanel();
		selectTargetLabelPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		selectTargetLabel=new JLabel("Select target node:");
		selectTargetLabelPanel.add(selectTargetLabel);		
		targetListPanel=new JPanel();
		targetListPanel.setLayout(new BoxLayout(targetListPanel, BoxLayout.Y_AXIS));
		targetSelectionList=new JList<String>();
		String[] formattedTargetList=new String[nodeList.size()];
		for(int i=0; i<nodeList.size(); i++)
		{
			String[] n=new String[2];
			n=nodeList.get(i);
			if(n[1].compareTo(currNode)!=0)
				formattedTargetList[i]=n[1]+" ["+n[0]+"]";
		}
		targetSelectionList.setListData(formattedTargetList);
		targetScrollPane = new JScrollPane(targetSelectionList);
		targetSelectionList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		targetScrollPane.setViewportView(targetSelectionList);
		targetScrollPane.setPreferredSize(new Dimension(200, 100));
		targetScrollPane.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		targetListPanel.add(selectTargetLabelPanel);
		targetListPanel.add(targetScrollPane);
		//targetButton
		targetButtonPanel=new JPanel();
		targetButtonPanel.setLayout(new BoxLayout(targetButtonPanel, BoxLayout.Y_AXIS));
		selectTargetButton= new JButton(">");
		selectTargetButton.setSize(50, 20);
		selectTargetButton.setActionCommand(SELECT_TARGET);
		selectTargetButton.addActionListener(actionListener);
		JLabel emptyLabel1=new JLabel(" ");
		emptyLabel1.setSize(50, 60);
		deselectTargetButton= new JButton("<");
		deselectTargetButton.setSize(50, 20);
		deselectTargetButton.setActionCommand(DESELECT_TARGET);
		deselectTargetButton.addActionListener(actionListener);
		targetButtonPanel.add(selectTargetButton);
		targetButtonPanel.add(emptyLabel1);
		targetButtonPanel.add(deselectTargetButton);
		//selectedTargetList
		JPanel selectedTargetLabelPanel=new JPanel();
		selectedTargetLabelPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		selectedTargetLabel=new JLabel("Selected target node:");
		selectedTargetLabelPanel.add(selectedTargetLabel);		
		selectedTargetListPanel=new JPanel();
		selectedTargetListPanel.setLayout(new BoxLayout(selectedTargetListPanel, BoxLayout.Y_AXIS));
		selectedTargetSelectionList=new JList<String>();
		String[] formattedSelectedTargetList=new String[selectedTarget.size()];
		if(selectedTarget!=null && selectedTarget.size()>0)
		{
			for(int i=0; i<selectedTarget.size(); i++)
			{
				String[] n=new String[2];
				n=selectedTarget.get(i);
				formattedTargetList[i]=n[1]+" ["+n[0]+"]";
			}
			selectedTargetSelectionList.setListData(formattedSelectedTargetList);
		}
		selectedTargetScrollPane = new JScrollPane(selectedTargetSelectionList);
		selectedTargetSelectionList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		selectedTargetScrollPane.setViewportView(selectedTargetSelectionList);
		selectedTargetScrollPane.setPreferredSize(new Dimension(200, 100));
		selectedTargetScrollPane.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		selectedTargetListPanel.add(selectedTargetLabelPanel);
		selectedTargetListPanel.add(selectedTargetScrollPane);
				
		selectTargetPanel.add(targetListPanel);
		selectTargetPanel.add(targetButtonPanel);
		selectTargetPanel.add(selectedTargetListPanel);
		//toEdgeTypeComboBox
		JPanel toEdgePanel=new JPanel();
		toEdgePanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		toEdgeTypeLabel=new JLabel("Edge type");
		toEdgeTypeComboBox=new JComboBox<String>();
		toEdgeTypeOption=new String[3];
		toEdgeTypeOption[0]=staticString.getDBEdgePos();
		toEdgeTypeOption[1]=staticString.getDBEdgeNeg();
		toEdgeTypeOption[2]=staticString.getDBEdgePhy();
		toEdgeTypeComboBoxModel = new DefaultComboBoxModel<String>(toEdgeTypeOption);
		toEdgeTypeComboBox.setModel(toEdgeTypeComboBoxModel);
		toEdgeTypeComboBox.setSelectedIndex(0);
		toEdgePanel.add(toEdgeTypeLabel);
		toEdgePanel.add(toEdgeTypeComboBox);
				
		//buttonPanel
		buttonPanel=new JPanel();
		buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
		addEdgeButton= new JButton(ADD_EDGE);
		addEdgeButton.setSize(50, 20);
		addEdgeButton.setActionCommand(ADD_EDGE);
		addEdgeButton.addActionListener(actionListener);
		doneButton= new JButton(DONE);
		doneButton.setSize(50, 20);
		doneButton.setActionCommand(DONE);
		doneButton.addActionListener(actionListener);
		buttonPanel.add(addEdgeButton);
		buttonPanel.add(doneButton);
		
		addPanel.setLayout(new BoxLayout(addPanel, BoxLayout.Y_AXIS));
		addPanel.add(sourceLabelPanel);
		addPanel.add(selectSourcePanel);
		addPanel.add(fromEdgePanel);
		addPanel.add(targetLabelPanel);
		addPanel.add(selectTargetPanel);
		addPanel.add(toEdgePanel);
		addPanel.add(buttonPanel);
		
		setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));
		add(existingPanel);
		add(addPanel);
	}
	
	private void confirmEdgeRemoval_actionPerformed(){
		int[] indices=edgeSelectionList.getSelectedIndices();
		
		if(indices.length>0)
		{
			JPanel confirmationLabelPanel=new JPanel();
			confirmationLabelPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
			JLabel confirmationLabel=new JLabel("Are you sure you want to remove the following edges?");
			confirmationLabelPanel.add(confirmationLabel);
			JPanel edgePanel=new JPanel();
			edgePanel.setLayout(new FlowLayout(FlowLayout.LEFT));
			JTextField edgeTextField=new JTextField();
			String edgeStr="";
			for(int i=0; i<indices.length; i++)
			{
				edge e=edgeList.get(indices[i]);
				String edgeArrow="";
				if(edgeStr.length()>0)
					edgeStr=edgeStr+", ";
				if(e.getType().compareTo(staticString.getDBEdgePos())==0)
					edgeArrow="->";
				else if(e.getType().compareTo(staticString.getDBEdgeNeg())==0)
					edgeArrow="-|";
				else
					edgeArrow="--";
				edgeStr=edgeStr+e.getSourceName()+edgeArrow+e.getTargetName();
			}
			edgeTextField.setText(edgeStr);
			edgeTextField.setEditable(false);
			JScrollPane edgeScrollPane = new JScrollPane(edgeTextField);
			edgeScrollPane.setViewportView(edgeTextField);
			edgeScrollPane.setPreferredSize(new Dimension(500, 80));
			edgeScrollPane.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
			edgePanel.add(edgeScrollPane);
		
			JPanel buttonPanel=new JPanel();
			buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
			JButton confirmButton= new JButton();
			confirmButton.setSize(55, 20);
			confirmButton.setText(CONFIRM_REMOVE_EDGE);
			confirmButton.setActionCommand(CONFIRM_REMOVE_EDGE);
			confirmButton.addActionListener(actionListener);
			JButton cancelButton= new JButton();
			cancelButton.setSize(50, 20);
			cancelButton.setText(CANCEL);
			cancelButton.setActionCommand(CANCEL);
			cancelButton.addActionListener(actionListener);
			buttonPanel.add(confirmButton);
			buttonPanel.add(cancelButton);
		
			removeEdgeDialog=new JDialog();
			removeEdgeDialog.setTitle("Confirmation of edge(s) removal");
			removeEdgeDialog.setSize(500,200);
			removeEdgeDialog.setLocationRelativeTo(null);
			removeEdgeDialog.setResizable(false);
			removeEdgeDialog.setModal(true);
			removeEdgeDialog.setLayout(new BoxLayout(removeEdgeDialog.getContentPane(), BoxLayout.Y_AXIS));
			removeEdgeDialog.add(confirmationLabelPanel);
			removeEdgeDialog.add(edgePanel);
			removeEdgeDialog.add(buttonPanel);
			removeEdgeDialog.setVisible(true);
		}
	}
		
	private void cancel_actionPerformed(){
		removeEdgeDialog.setVisible(false);
	}
	
	private void selectSource_actionPerformed(){
		if(sourceSelectionList.getSelectedIndex()!=-1)
		{
			String[] n=new String[2];
			n=nodeList.get(sourceSelectionList.getSelectedIndex());
			if(selectedSource.contains(n)==false)
				selectedSource.add(n);
			
			String[] formattedSelectedSourceList=new String[selectedSource.size()];
			for(int i=0; i<selectedSource.size(); i++)
			{
				n=selectedSource.get(i);
				formattedSelectedSourceList[i]=n[1]+" ["+n[0]+"]";
			}
			selectedSourceSelectionList.setListData(formattedSelectedSourceList);
		}
	}
	
	private void deselectSource_actionPerformed(){
		if(selectedSourceSelectionList.getSelectedIndex()!=-1)
		{
			String[] n=new String[2];
			n=selectedSource.get(selectedSourceSelectionList.getSelectedIndex());
			selectedSource.remove(n);
			
			String[] formattedSelectedSourceList=new String[selectedSource.size()];
			for(int i=0; i<selectedSource.size(); i++)
			{
				n=selectedSource.get(i);
				formattedSelectedSourceList[i]=n[1]+" ["+n[0]+"]";
			}
			selectedSourceSelectionList.setListData(formattedSelectedSourceList);
		}
	}
	
	private void selectTarget_actionPerformed(){
		if(targetSelectionList.getSelectedIndex()!=-1)
		{
			String[] n=new String[2];
			n=nodeList.get(targetSelectionList.getSelectedIndex());
			if(selectedTarget.contains(n)==false)
				selectedTarget.add(n);
			
			String[] formattedSelectedTargetList=new String[selectedTarget.size()];
			for(int i=0; i<selectedTarget.size(); i++)
			{
				n=selectedTarget.get(i);
				formattedSelectedTargetList[i]=n[1]+" ["+n[0]+"]";
			}
			selectedTargetSelectionList.setListData(formattedSelectedTargetList);
		}
	}
	
	private void deselectTarget_actionPerformed(){
		if(selectedTargetSelectionList.getSelectedIndex()!=-1)
		{
			String[] n=new String[2];
			n=selectedTarget.get(selectedTargetSelectionList.getSelectedIndex());
			selectedTarget.remove(n);
			
			String[] formattedSelectedTargetList=new String[selectedTarget.size()];
			for(int i=0; i<selectedTarget.size(); i++)
			{
				n=selectedTarget.get(i);
				formattedSelectedTargetList[i]=n[1]+" ["+n[0]+"]";
			}
			selectedTargetSelectionList.setListData(formattedSelectedTargetList);
		}
	}
	
	private void addEdge_actionPerformed(){
		
		String currNodeID=postgreDB.getSBMLNodeID(currNode);
		
		if(selectedTarget.size()>0)
		{
			String edgeType=(String)toEdgeTypeComboBox.getSelectedItem();
			for(int i=0; i<selectedTarget.size(); i++)
			{
				String[] n=new String[2];
				n=selectedTarget.get(i);
				
				postgreDB.addNewEdge(currNodeID, currNode, n[0], n[1], edgeType);
			}
		}
		
		if(selectedSource.size()>0)
		{
			String edgeType=(String)fromEdgeTypeComboBox.getSelectedItem();
			for(int i=0; i<selectedSource.size(); i++)
			{
				String[] n=new String[2];
				n=selectedSource.get(i);
				
				postgreDB.addNewEdge(n[0], n[1], currNodeID, currNode, edgeType);
			}
		}

		if(selectedTarget.size()>0 || selectedSource.size()>0)
		{
			edgeList=postgreDB.getNetworkEdge_edgeListOfNode(currNode);
			String[] formattedEdgeList=new String[edgeList.size()];
			for(int i=0; i<edgeList.size(); i++)
			{
				edge e=edgeList.get(i);
				String edgeArrow="";
				if(e.getType().compareTo(staticString.getDBEdgePos())==0)
					edgeArrow="->";
				else if(e.getType().compareTo(staticString.getDBEdgeNeg())==0)
					edgeArrow="-|";
				else
					edgeArrow="--";
				formattedEdgeList[i]=e.getSourceName()+edgeArrow+e.getTargetName();
			}
			edgeSelectionList.setListData(formattedEdgeList);
		}
	}
	
	private void done_actionPerformed(){
		setVisible(false);
	}

}
