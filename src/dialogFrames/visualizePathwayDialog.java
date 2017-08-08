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
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import constants.stringConstant;
import database.postgreSQL;

@SuppressWarnings("serial")
//	Layout: 
//  |  - settingPanel                                             
//  |    + pathwayPanel                                        
//  |      * pathwayList, pathwayScrollPane                        
//	|	 + viewSettingPanel                                        
//	|      * settingComboBox
//  |  - buttonPanel
//  |    + visualizeButton
//  |    + cancelButton

public class visualizePathwayDialog extends JDialog{
	private JPanel settingPanel, buttonPanel;
	private JButton visualizeButton, selectButton, deselectButton, cancelButton, editButton;
	private JList<String> pathwayList, selectedPathwayList;
	private JScrollPane pathwayScrollPane, selectedPathwayScrollPane;
	private ArrayList<String> listOfPathway=new ArrayList<String>();
	private ArrayList<String> listOfSelectedPathway=new ArrayList<String>();
	private postgreSQL DB;
	
	private String CANCEL;
	private boolean performVisualization=false;
	
	private stringConstant staticString=new stringConstant();
	
	protected final static String VISUALIZE = "Visualize pathway";
	protected final static String EDIT = "Edit pathway";
	protected final static String SELECT = "Select";
	protected final static String DESELECT = "Deselect";
	
	public visualizePathwayDialog(postgreSQL database)
	{
		//initialize constants
		DB=database;
		CANCEL=staticString.getCancel();
		
		//set JDialog properties
		setTitle("Visualize Pathway");
		setSize(480,350);
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
			if (act.getActionCommand() == EDIT)
				edit_actionPerformed();
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
		
		//create pathwayPanel
		JPanel pathwayPanel=new JPanel();
		pathwayPanel.setLayout(new BoxLayout(pathwayPanel, BoxLayout.Y_AXIS));
		JLabel pathwayLabel=new JLabel();
		pathwayLabel.setText("Pathway List");
		JPanel pathwayLabelPanel=new JPanel();
		pathwayLabelPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		pathwayLabelPanel.add(pathwayLabel);
		pathwayList=new JList<String>();
		pathwayScrollPane=new JScrollPane();
		pathwayList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		listOfPathway=DB.getPathway_pathwayNameList();
		String[] javaArr=listOfPathway.toArray(new String[listOfPathway.size()]);
		pathwayList.setListData(javaArr);
		pathwayScrollPane = new JScrollPane(pathwayList);
		pathwayScrollPane.setViewportView(pathwayList);
		pathwayScrollPane.setPreferredSize(new Dimension(200, 250));
		pathwayScrollPane.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		pathwayPanel.add(pathwayLabelPanel);
		pathwayPanel.add(pathwayScrollPane);
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
		//create selected selectedPathwayPanel
		JPanel selectedPathwayPanel=new JPanel();
		selectedPathwayPanel.setLayout(new BoxLayout(selectedPathwayPanel, BoxLayout.Y_AXIS));
		JLabel selectedPathwayLabel=new JLabel();
		selectedPathwayLabel.setText("Selected Pathway List (up to 2)");
		JPanel selectedPathwayLabelPanel=new JPanel();
		selectedPathwayLabelPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		selectedPathwayLabelPanel.add(selectedPathwayLabel);
		selectedPathwayList=new JList<String>();
		selectedPathwayScrollPane=new JScrollPane();
		selectedPathwayList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		selectedPathwayList.setListData(listOfSelectedPathway.toArray(new String[listOfSelectedPathway.size()]));
		selectedPathwayScrollPane = new JScrollPane(selectedPathwayList);
		selectedPathwayScrollPane.setViewportView(selectedPathwayList);
		selectedPathwayScrollPane.setPreferredSize(new Dimension(200, 250));
		selectedPathwayScrollPane.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		selectedPathwayPanel.add(selectedPathwayLabelPanel);
		selectedPathwayPanel.add(selectedPathwayScrollPane);
		
		settingPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		settingPanel.add(pathwayPanel);
		settingPanel.add(selectButtonPanel);
		settingPanel.add(selectedPathwayPanel);
		//create buttonPanel
		editButton= new JButton();
		editButton.setSize(50, 20);
		editButton.setText(EDIT);
		editButton.setActionCommand(EDIT);
		editButton.addActionListener(actionListener);
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
		buttonPanel.add(editButton);
		buttonPanel.add(visualizeButton);
		buttonPanel.add(cancelButton);

		setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));
		add(settingPanel);
		add(buttonPanel);
	}

	private void visualize_actionPerformed(){
		if(listOfSelectedPathway.size()>0)
		{
			performVisualization=true;
			setVisible(false);
		}
		else
			JOptionPane.showMessageDialog(new JFrame(), "Please select a pathway from 'Pathway List'", "Error", JOptionPane.ERROR_MESSAGE);
	}

	private void select_actionPerformed(){
		int selectedIndex=pathwayList.getSelectedIndex();
		if(selectedIndex!=-1)
		{
			String n=listOfPathway.get(selectedIndex);
			if(listOfSelectedPathway.contains(n)==false)
				listOfSelectedPathway.add(n);
			selectedPathwayList.setListData(listOfSelectedPathway.toArray(new String[listOfSelectedPathway.size()]));
		}
		if(listOfSelectedPathway.size()==2)
			selectButton.setEnabled(false);
		else
			selectButton.setEnabled(true);
	}
	
	private void deselect_actionPerformed(){
		int selectedIndex=selectedPathwayList.getSelectedIndex();
		if(selectedIndex!=-1)
		{
			String n=listOfSelectedPathway.get(selectedIndex);
			if(listOfSelectedPathway.contains(n)==true)
				listOfSelectedPathway.remove(n);
			selectedPathwayList.setListData(listOfSelectedPathway.toArray(new String[listOfSelectedPathway.size()]));
		}
		if(listOfSelectedPathway.size()==2)
			selectButton.setEnabled(false);
		else
			selectButton.setEnabled(true);
	}
	
	private void edit_actionPerformed(){
		editPathwayDialog editPathway=new editPathwayDialog(DB);
		editPathway.setVisible(true);
		listOfPathway=DB.getPathway_pathwayNameList();
		String[] javaArr=listOfPathway.toArray(new String[listOfPathway.size()]);
		pathwayList.setListData(javaArr);
	}
	
	private void cancel_actionPerformed(){
		setVisible(false);
	}
	
	public ArrayList<String> getSelectedPathwayList(){
		return listOfSelectedPathway;
	}
	public boolean getPerformVisualization(){
		return performVisualization;
	}
}
