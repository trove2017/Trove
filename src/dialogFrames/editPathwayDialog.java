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
//  |  - existingPanel                            
//  |    + pathwayLabel
//	|	 + pathwayPanel (pathwayList, pathwayScrollPane)				
//	|	 + removePathwayInfoLabel					
//  |    + removePathwayButton
//  |  - addPanel								
//  |    + pathwayIdPanel 		
//	|	   * pathwayIdLabel				
//	|	   * pathwayIdTextField		
//	|    + pathwayNamePanel 		
//	|	   * pathwayNameLabel				
//	|	   * pathwayNameTextField		
//	|	 + buttonPanel (leftLayout)
//	|	   * addPathwayButton				
//	|	   * doneButton							

public class editPathwayDialog extends JDialog{
	private JDialog removePathwayDialog=new JDialog();
	private JPanel existingPanel, addPanel;
	private JButton removePathwayButton, addPathwayButton, doneButton;
	private JList<String> pathwaySelectionList;
	private JTextField pathwayIdTextField, pathwayNameTextField;
	private JScrollPane pathwayScrollPane;
	private ArrayList<String> pathwayList;
	private ArrayList<String> pathwayToRemove;
	
	private String DONE, CANCEL;
	private stringConstant staticString=new stringConstant();
	
	protected final static String REMOVE_PATHWAY = "Remove";
	protected final static String CONFIRM_REMOVE_PATHWAY = "Confirm";
	protected final static String ADD_PATHWAY = "Add pathway";
	
	postgreSQL postgreDB=new postgreSQL();
	
	public editPathwayDialog(postgreSQL db)
	{
		//initialize constants
		DONE=staticString.getDone();
		CANCEL=staticString.getCancel();
		
		//initialize variables
		postgreDB=db;
		pathwayList=postgreDB.getPathway_pathwayNameList();
		
		//set JDialog properties
		setTitle("Edit Pathway");
		setSize(350,370);
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
			if (act.getActionCommand() == REMOVE_PATHWAY)
				confirmPathwayRemoval_actionPerformed();
			if (act.getActionCommand() == CONFIRM_REMOVE_PATHWAY)
				removePathway_actionPerformed();
			if (act.getActionCommand() == ADD_PATHWAY)
				addPathway_actionPerformed();
		}
	};

	private void initializeComponents()
	{
		existingPanel=new JPanel();
		existingPanel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
		existingPanel.setPreferredSize(new Dimension(250, 150));
		addPanel=new JPanel();
		addPanel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
		addPanel.setPreferredSize(new Dimension(250, 100));
		//create existingPanel
		//pathwayLabel
		JPanel pathwayLabelPanel=new JPanel();
		pathwayLabelPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		JLabel pathwayLabel=new JLabel("Select pathway to remove (Ctrl+Alt for multiple selection):");
		pathwayLabelPanel.add(pathwayLabel);
		//pathwayList
		pathwaySelectionList=new JList<String>();
		String[] formattedEdgeList=pathwayList.toArray(new String[pathwayList.size()]);
		pathwaySelectionList.setListData(formattedEdgeList);
		pathwayScrollPane = new JScrollPane(pathwaySelectionList);
		pathwaySelectionList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		pathwayScrollPane.setViewportView(pathwaySelectionList);
		pathwayScrollPane.setPreferredSize(new Dimension(200, 100));
		pathwayScrollPane.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		//removePathway button
		JPanel removePathwayBottonPanel=new JPanel();
		removePathwayBottonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
		removePathwayButton= new JButton();
		removePathwayButton.setSize(50, 20);
		removePathwayButton.setText(REMOVE_PATHWAY);
		removePathwayButton.setActionCommand(REMOVE_PATHWAY);
		removePathwayButton.addActionListener(actionListener);
		removePathwayBottonPanel.add(removePathwayButton);
		existingPanel.setLayout(new BoxLayout(existingPanel, BoxLayout.Y_AXIS));
		existingPanel.add(pathwayLabelPanel);
		existingPanel.add(pathwayScrollPane);
		existingPanel.add(removePathwayBottonPanel);
		//addPanel
		JPanel pathwayIdPanel=new JPanel();
		pathwayIdPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		JLabel pathwayIdLabel=new JLabel("Pathway ID");
		pathwayIdTextField=new JTextField(30);
		pathwayIdPanel.add(pathwayIdLabel);
		pathwayIdPanel.add(pathwayIdTextField);
		JPanel pathwayNamePanel=new JPanel();
		pathwayNamePanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		JLabel pathwayNameLabel=new JLabel("Pathway name");
		pathwayNameTextField=new JTextField(30);
		pathwayNamePanel.add(pathwayNameLabel);
		pathwayNamePanel.add(pathwayNameTextField);
		JPanel buttonPanel=new JPanel();
		buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
		addPathwayButton= new JButton(ADD_PATHWAY);
		addPathwayButton.setSize(50, 20);
		addPathwayButton.setActionCommand(ADD_PATHWAY);
		addPathwayButton.addActionListener(actionListener);
		doneButton= new JButton(DONE);
		doneButton.setSize(50, 20);
		doneButton.setActionCommand(DONE);
		doneButton.addActionListener(actionListener);
		buttonPanel.add(addPathwayButton);
		buttonPanel.add(doneButton);
		
		addPanel.setLayout(new BoxLayout(addPanel, BoxLayout.Y_AXIS));
		addPanel.add(pathwayIdPanel);
		addPanel.add(pathwayNamePanel);
		addPanel.add(buttonPanel);
		
		setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));
		add(existingPanel);
		add(addPanel);
	}
	
	private void confirmPathwayRemoval_actionPerformed(){
		int[] indices=pathwaySelectionList.getSelectedIndices();
		
		if(indices.length>0)
		{
			pathwayToRemove=new ArrayList<String>();
			JPanel confirmationLabelPanel=new JPanel();
			confirmationLabelPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
			JLabel confirmationLabel=new JLabel("Are you sure you want to remove the following pathways?");
			confirmationLabelPanel.add(confirmationLabel);
			JPanel pathwayPanel=new JPanel();
			pathwayPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
			JTextField pathwayTextField=new JTextField();
			String pathwayStr="";
			for(int i=0; i<indices.length; i++)
			{
				pathwayToRemove.add(pathwaySelectionList.getModel().getElementAt(indices[i]).toString());
				pathwayStr=pathwayStr+pathwaySelectionList.getModel().getElementAt(indices[i]).toString();
				if(i<indices.length-1)
					pathwayStr=pathwayStr+", ";
			}
			pathwayTextField.setText(pathwayStr);
			pathwayTextField.setEditable(false);
			JScrollPane pathwayScrollPane = new JScrollPane(pathwayTextField);
			pathwayScrollPane.setViewportView(pathwayTextField);
			pathwayScrollPane.setPreferredSize(new Dimension(500, 80));
			pathwayScrollPane.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
			pathwayPanel.add(pathwayScrollPane);
		
			JPanel buttonPanel=new JPanel();
			buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
			JButton confirmButton= new JButton();
			confirmButton.setSize(55, 20);
			confirmButton.setText(CONFIRM_REMOVE_PATHWAY);
			confirmButton.setActionCommand(CONFIRM_REMOVE_PATHWAY);
			confirmButton.addActionListener(actionListener);
			JButton cancelButton= new JButton();
			cancelButton.setSize(50, 20);
			cancelButton.setText(CANCEL);
			cancelButton.setActionCommand(CANCEL);
			cancelButton.addActionListener(actionListener);
			buttonPanel.add(confirmButton);
			buttonPanel.add(cancelButton);
		
			removePathwayDialog=new JDialog();
			removePathwayDialog.setTitle("Confirmation of pathway(s) removal");
			removePathwayDialog.setSize(500,200);
			removePathwayDialog.setLocationRelativeTo(null);
			removePathwayDialog.setResizable(false);
			removePathwayDialog.setModal(true);
			removePathwayDialog.setLayout(new BoxLayout(removePathwayDialog.getContentPane(), BoxLayout.Y_AXIS));
			removePathwayDialog.add(confirmationLabelPanel);
			removePathwayDialog.add(pathwayPanel);
			removePathwayDialog.add(buttonPanel);
			removePathwayDialog.setVisible(true);
		}
	}
		
	private void cancel_actionPerformed(){
		removePathwayDialog.setVisible(false);
	}
	
	private void addPathway_actionPerformed(){
		String pathwayId=pathwayIdTextField.getText();
		String pathwayName=pathwayNameTextField.getText();
		if(pathwayId!=null && pathwayId.length()>0 && pathwayName!=null && pathwayName.length()>0)
		{
			postgreDB.update_pathway(pathwayId, pathwayName, true);
			pathwayList=postgreDB.getPathway_pathwayNameList();
			String[] formattedEdgeList=pathwayList.toArray(new String[pathwayList.size()]);
			pathwaySelectionList.setListData(formattedEdgeList);
		}
		else
			JOptionPane.showMessageDialog(new JFrame(), "Please enter a pathway ID and pathway name.", "Error", JOptionPane.ERROR_MESSAGE);
	}
	
	private void removePathway_actionPerformed(){
		removePathwayDialog.setVisible(true);
		for(int i=0; i<pathwayToRemove.size(); i++)
			postgreDB.remove_pathway(pathwayToRemove.get(i));
		pathwayList=postgreDB.getPathway_pathwayNameList();
		String[] formattedEdgeList=pathwayList.toArray(new String[pathwayList.size()]);
		pathwaySelectionList.setListData(formattedEdgeList);
	}
	
	private void done_actionPerformed(){
		setVisible(false);
	}

}
