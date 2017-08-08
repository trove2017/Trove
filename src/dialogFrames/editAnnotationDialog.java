package dialogFrames;

//Imports are listed in full to show what's being used could just import javax.swing.* and java.awt.* etc..
import graph.annotation;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
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
//  |    + annotationLabel
//	|	 + annotationList, annotationScrollPane
//  |    + removeAnnotationButton                        
//  |  - addPanel
//  |    + sourcePanel
//	|	   * sourceLabel
//	|	   * sourceOptionBox
//	|      * addNewSourceButton
//  |    + idPanel
//	|	   * idLabel
//	|	   * idText
//	|    + typePanel (to display only if source is "GO")
//	|	   * typeLabel
//	|	   * typeOptionBox
//	|    + addAnnotationButton 

public class editAnnotationDialog extends JDialog{
	private JPanel existingPanel, addPanel;
	private JPanel sourcePanel, urlPanel, idPanel, typePanel, buttonPanel;
	private JButton removeAnnotationButton, addNewSourceButton, addAnnotationButton, doneButton;
	private JLabel annotationLabel, sourceLabel, urlLabel, urlInfoLabel, idLabel, typeLabel;
	private JTextField urlTextField, idTextField;
	private JList<String> annotationSelectionList;
	private JScrollPane annotationScrollPane, urlScrollPane;
	private ArrayList<String> typeOption;
	private JComboBox<String> sourceComboBox, typeComboBox;
	private String[] sourceOption;
	private DefaultComboBoxModel<String> sourceComboBoxModel;
	private ArrayList<String[]> sourceList;
	private ArrayList<annotation> annotationArrayList;
	private String currNode;
	
	private String DONE, GO, MOLECULAR_FUNCTION, LOCALIZATION, BIOLOGICAL_PROCESS;
	private stringConstant staticString=new stringConstant();
	
	protected final static String REMOVE_ANNOTATION = "Remove annotation";
	protected final static String ADD_ANNOTATION = "Add annotation";
	protected final static String ADD_NEW_SOURCE = "Add new source";
	
	postgreSQL postgreDB=new postgreSQL();
	
	public editAnnotationDialog(String node, postgreSQL db)
	{
		//initialize constants
		DONE=staticString.getDone();
		GO=staticString.getDBGO();
		MOLECULAR_FUNCTION=staticString.getDBMolecularFunction();
		LOCALIZATION=staticString.getDBLocalization();
		BIOLOGICAL_PROCESS=staticString.getDBBiologicalProcess();
		
		//initialize variables
		currNode=node;
		postgreDB=db;
		typeOption=new ArrayList<String>();
		typeOption.add(MOLECULAR_FUNCTION);
		typeOption.add(LOCALIZATION);
		typeOption.add(BIOLOGICAL_PROCESS);
		annotationArrayList=postgreDB.getNode_allAnnotationOfNode(currNode);
		sourceList=postgreDB.getSource_sourceList(1);
		
		//set JDialog properties
		setTitle("Annotation of "+node);
		setSize(470,460);
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
			if (act.getActionCommand() == REMOVE_ANNOTATION)
				removeAnnotation_actionPerformed();
			if (act.getActionCommand() == ADD_ANNOTATION)
				addAnnotation_actionPerformed();
			if (act.getActionCommand() == ADD_NEW_SOURCE)
				addNewSource_actionPerformed();
		}
	};

	private void initializeComponents()
	{
		existingPanel=new JPanel();
		existingPanel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
		existingPanel.setPreferredSize(new Dimension(400, 200));
		addPanel=new JPanel();
		addPanel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
		addPanel.setPreferredSize(new Dimension(400, 250));
		
		//create existingPanel
		//annotationLabel
		JPanel annotationLabelPanel=new JPanel();
		annotationLabel=new JLabel("Select annotation to remove (Ctrl+Alt for multiple selection):");
		annotationLabelPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		annotationLabelPanel.add(annotationLabel);
		//annotationList
		annotationSelectionList=new JList<String>();
		String[] formattedAnnotationList=new String[annotationArrayList.size()];
		for(int i=0; i<annotationArrayList.size(); i++)
		{
			if(annotationArrayList.get(i).getSource().compareTo(GO)==0)
				formattedAnnotationList[i]=annotationArrayList.get(i).getSource()+", "+annotationArrayList.get(i).getID()+", "+annotationArrayList.get(i).getType();
			else
				formattedAnnotationList[i]=annotationArrayList.get(i).getSource()+", "+annotationArrayList.get(i).getID();
		}
		annotationSelectionList.setListData(formattedAnnotationList);
		annotationScrollPane = new JScrollPane(annotationSelectionList);
		annotationSelectionList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		annotationScrollPane.setViewportView(annotationSelectionList);
		annotationScrollPane.setPreferredSize(new Dimension(200, 100));
		annotationScrollPane.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		//removeAnnotation button
		JPanel removeAnnotationBottonPanel=new JPanel();
		removeAnnotationBottonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
		removeAnnotationButton= new JButton();
		removeAnnotationButton.setSize(50, 20);
		removeAnnotationButton.setText(REMOVE_ANNOTATION);
		removeAnnotationButton.setActionCommand(REMOVE_ANNOTATION);
		removeAnnotationButton.addActionListener(actionListener);
		removeAnnotationBottonPanel.add(removeAnnotationButton);
		
		existingPanel.setLayout(new BoxLayout(existingPanel, BoxLayout.Y_AXIS));
		existingPanel.add(annotationLabelPanel);
		existingPanel.add(annotationScrollPane);
		existingPanel.add(removeAnnotationBottonPanel);
		
		//create addPanel
		sourcePanel=new JPanel();
		sourcePanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		//source label
		sourceLabel=new JLabel("Source");
		sourcePanel.add(sourceLabel);
		//source option box
		sourceComboBox=new JComboBox<String>();
		sourceOption=new String[sourceList.size()];
		for(int i=0; i<sourceList.size(); i++)
		{
			String[] sourceData=new String[2];
			sourceData=sourceList.get(i);
			sourceOption[i]=sourceData[0];
		}
		sourceComboBoxModel = new DefaultComboBoxModel<String>(sourceOption);
		sourceComboBox.setModel(sourceComboBoxModel);
		sourceComboBox.setSelectedIndex(0);
		sourceComboBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e){
				if(e.getStateChange()==ItemEvent.SELECTED)
				{
					int index=sourceComboBox.getSelectedIndex();
					String[] sourceData=new String[2];
					sourceData=sourceList.get(index);
					urlTextField.setText(sourceData[1]);
					if(sourceData[0].compareTo(GO)==0)
						typePanel.setVisible(true);
					else
						typePanel.setVisible(false);
				}
			}
		});
		
		sourcePanel.add(sourceComboBox);
		//add new source button
		addNewSourceButton= new JButton();
		addNewSourceButton.setSize(50, 20);
		addNewSourceButton.setText(ADD_NEW_SOURCE);
		addNewSourceButton.setActionCommand(ADD_NEW_SOURCE);
		addNewSourceButton.addActionListener(actionListener);
		sourcePanel.add(addNewSourceButton);
		//source url
		urlPanel=new JPanel();
		urlPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		String[] sourceData=new String[2];
		sourceData=sourceList.get(0);
		urlTextField=new JTextField(sourceData[1]);
		urlTextField.setEditable(false);
		urlScrollPane = new JScrollPane(urlTextField);
		urlScrollPane.setViewportView(urlTextField);
		urlScrollPane.setPreferredSize(new Dimension(355, 50));
		urlScrollPane.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		urlLabel=new JLabel("URL      ");
		urlPanel.add(urlLabel);
		urlPanel.add(urlScrollPane);
		//source url information label
		JPanel urlInfoPanel=new JPanel();
		urlInfoPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		urlInfoLabel=new JLabel("Note: <v> is a placeholder in the URL and will be replaced by the ID.");
		urlInfoPanel.add(urlInfoLabel);
		//source id
		idPanel=new JPanel();
		idPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		idLabel=new JLabel("ID          ");
		idTextField=new JTextField();
		idTextField.setColumns(30);
		idTextField.setEditable(true);
		idPanel.add(idLabel);
		idPanel.add(idTextField);
		//source type
		typePanel=new JPanel();
		typePanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		typeLabel=new JLabel("Type     ");
		typeComboBox=new JComboBox<String>();
		for(int i=0; i<typeOption.size(); i++)
			typeComboBox.addItem(typeOption.get(i));
		typeComboBox.setSelectedIndex(0);
		typePanel.add(typeLabel);
		typePanel.add(typeComboBox);
		if(sourceData[0].compareTo(GO)==0)
			typePanel.setVisible(true);
		else
			typePanel.setVisible(false);
		//addAnnotation and cancel button
		buttonPanel=new JPanel();
		buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
		addAnnotationButton= new JButton();
		addAnnotationButton.setSize(50, 20);
		addAnnotationButton.setText(ADD_ANNOTATION);
		addAnnotationButton.setActionCommand(ADD_ANNOTATION);
		addAnnotationButton.addActionListener(actionListener);
		doneButton= new JButton();
		doneButton.setSize(50, 20);
		doneButton.setText(DONE);
		doneButton.setActionCommand(DONE);
		doneButton.addActionListener(actionListener);
		buttonPanel.add(addAnnotationButton);
		buttonPanel.add(doneButton);
		
		addPanel.setLayout(new BoxLayout(addPanel, BoxLayout.Y_AXIS));
		addPanel.add(sourcePanel);
		addPanel.add(urlPanel);
		addPanel.add(urlInfoPanel);
		addPanel.add(idPanel);
		addPanel.add(typePanel);
		addPanel.add(buttonPanel);
		

		setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));
		add(existingPanel);
		add(addPanel);
	}
	
	private void addAnnotation_actionPerformed(){
		String source=(String)sourceComboBox.getSelectedItem();
		String id=idTextField.getText();
		String type="";
		
		if(id==null || id.length()==0)
			JOptionPane.showMessageDialog(new JFrame(), "Please key in the ID that is to replace <v> in the URL address.", "Error", JOptionPane.ERROR_MESSAGE);
		else
		{
			if(source.compareTo(GO)==0)
				type=(String)typeComboBox.getSelectedItem();
			postgreDB.update_nodeAnnotation(currNode, source, id, type);
		
			annotationArrayList=postgreDB.getNode_allAnnotationOfNode(currNode);
			String[] formattedAnnotationList=new String[annotationArrayList.size()];
			for(int i=0; i<annotationArrayList.size(); i++)
			{
				if(annotationArrayList.get(i).getSource().compareTo(GO)==0)
					formattedAnnotationList[i]=annotationArrayList.get(i).getSource()+", "+annotationArrayList.get(i).getID()+", "+annotationArrayList.get(i).getType();
				else
					formattedAnnotationList[i]=annotationArrayList.get(i).getSource()+", "+annotationArrayList.get(i).getID();
			}
			annotationSelectionList.setListData(formattedAnnotationList);
		}
	}
	
	private void removeAnnotation_actionPerformed(){
		int[] indices=annotationSelectionList.getSelectedIndices();
		for(int i=0; i<indices.length; i++)
			postgreDB.remove_nodeAnnotation(currNode, annotationArrayList.get(indices[i]).getSource(), annotationArrayList.get(indices[i]).getID(), annotationArrayList.get(indices[i]).getType());

		annotationArrayList=postgreDB.getNode_allAnnotationOfNode(currNode);
		String[] formattedAnnotationList=new String[annotationArrayList.size()];
		for(int i=0; i<annotationArrayList.size(); i++)
		{
			if(annotationArrayList.get(i).getSource().compareTo(GO)==0)
				formattedAnnotationList[i]=annotationArrayList.get(i).getSource()+", "+annotationArrayList.get(i).getID()+", "+annotationArrayList.get(i).getType();
			else
				formattedAnnotationList[i]=annotationArrayList.get(i).getSource()+", "+annotationArrayList.get(i).getID();
		}
		annotationSelectionList.setListData(formattedAnnotationList);
	}
	
	private void addNewSource_actionPerformed(){
		addSourceDialog addSource=new addSourceDialog(postgreDB);
		addSource.setVisible(true);
		sourceList=postgreDB.getSource_sourceList(1);
		sourceOption=new String[sourceList.size()];
		for(int i=0; i<sourceList.size(); i++)
		{
			String[] sourceData=new String[2];
			sourceData=sourceList.get(i);
			sourceOption[i]=sourceData[0];
		}
		sourceComboBoxModel = new DefaultComboBoxModel<String>(sourceOption);
		sourceComboBox.setModel(sourceComboBoxModel);
	}
	
	private void done_actionPerformed(){
		setVisible(false);
	}
}
