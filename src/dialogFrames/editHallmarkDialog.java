package dialogFrames;

//Imports are listed in full to show what's being used could just import javax.swing.* and java.awt.* etc..
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.border.EtchedBorder;

import constants.stringConstant;
import database.postgreSQL;

@SuppressWarnings("serial")

public class editHallmarkDialog extends JDialog{
	private JPanel editMethodPanel, editPanel=new JPanel(), directEditPanel=new JPanel(), GOassistedEditPanel=new JPanel();
	private JPanel methodPanel, buttonPanel;
	private JButton selectButton, deselectButton, GOSelectButton, GODeselectButton, updateHallmarkButton, cancelButton;
	private JLabel methodLabel;
	private ArrayList<String> entireHallmarkList=new ArrayList<String>(), hallmarkToAdd=new ArrayList<String>(), hallmarkSelected=new ArrayList<String>();
	private JList<String> hallmarkToAddSelectionList, hallmarkSelectedSelectionList;
	private JScrollPane hallmarkToAddScrollPane, hallmarkSelectedScrollPane;
	private ArrayList<ArrayList<String>> entireHallmarkGOList=new ArrayList<ArrayList<String>>(), GOToAdd=new ArrayList<ArrayList<String>>(), GOSelected=new ArrayList<ArrayList<String>>();
	private ArrayList<String> GOHallmarkList=new ArrayList<String>();
	private JList<String> GOToAddSelectionList, GOSelectedSelectionList, GOHallmarkSelectedJList;
	private JScrollPane GOToAddScrollPane, GOSelectedScrollPane, GOHallmarkSelectedScrollPane;
	private ArrayList<String> entireHallmarkListDB=new ArrayList<String>();
	private ArrayList<String> entireHallmarkGOIDList=new ArrayList<String>();
	
	private JComboBox<String> methodComboBox;
	private String currNode, currLayout;
	
	private String DONE;
	private stringConstant staticString=new stringConstant();
	
	protected final static String DIRECT_HALLMARK_EDIT = "Direct editing of hallmark";
	protected final static String GO_HALLMARK_MAP_EDIT = "GO-assisted hallmark editing";
	
	protected final static String UPDATE_HALLMARK = "Update hallmark";
	protected final static String SELECT = "SELECT";
	protected final static String DESELECT = "DESELECT";
	protected final static String GO_SELECT = "GO_SELECT";
	protected final static String GO_DESELECT = "GO_DESELECT";
	
	postgreSQL postgreDB=new postgreSQL();
	
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
	
	public editHallmarkDialog(String node, postgreSQL db)
	{
		//initialize variables
		currNode=node;
		postgreDB=db;
				
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
		
		entireHallmarkList=new ArrayList<String>();
		entireHallmarkList.add(HALLMARK_PROLIFERATION_GUI);
		entireHallmarkList.add(HALLMARK_GROWTH_REPRESSOR_GUI);
		entireHallmarkList.add(HALLMARK_APOPTOSIS_GUI);
		entireHallmarkList.add(HALLMARK_REPLICATIVE_IMMORTALITY_GUI);
		entireHallmarkList.add(HALLMARK_ANGIOGENESIS_GUI);
		entireHallmarkList.add(HALLMARK_METASTASIS_GUI);
		entireHallmarkList.add(HALLMARK_METABOLISM_GUI);
		entireHallmarkList.add(HALLMARK_IMMUNE_DESTRUCTION_GUI);
		entireHallmarkList.add(HALLMARK_GENOME_INSTABILITY_GUI);
		entireHallmarkList.add(HALLMARK_TUMOR_PROMOTING_INFLAMMATION_GUI);
		
		entireHallmarkListDB=new ArrayList<String>();
		entireHallmarkListDB.add(HALLMARK_PROLIFERATION_DB);
		entireHallmarkListDB.add(HALLMARK_GROWTH_REPRESSOR_DB);
		entireHallmarkListDB.add(HALLMARK_APOPTOSIS_DB);
		entireHallmarkListDB.add(HALLMARK_REPLICATIVE_IMMORTALITY_DB);
		entireHallmarkListDB.add(HALLMARK_ANGIOGENESIS_DB);
		entireHallmarkListDB.add(HALLMARK_METASTASIS_DB);
		entireHallmarkListDB.add(HALLMARK_METABOLISM_DB);
		entireHallmarkListDB.add(HALLMARK_IMMUNE_DESTRUCTION_DB);
		entireHallmarkListDB.add(HALLMARK_GENOME_INSTABILITY_DB);
		entireHallmarkListDB.add(HALLMARK_TUMOR_PROMOTING_INFLAMMATION_DB);
		
		entireHallmarkGOList=postgreDB.getHallmark_entireGOIDNameList();
		for(int i=0; i<entireHallmarkGOList.size(); i++)
			entireHallmarkGOIDList.add(entireHallmarkGOList.get(i).get(0));
		
		//initialize constants
		DONE=staticString.getDone();
		
		//set JDialog properties
		setTitle("Hallmark of "+node);
		setSize(950,460);
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
				cancel_actionPerformed();
			if (act.getActionCommand() == UPDATE_HALLMARK)
				updateHallmark_actionPerformed();
			if (act.getActionCommand() == SELECT)
				select_actionPerformed();
			if (act.getActionCommand() == DESELECT)
				deselect_actionPerformed();
			if (act.getActionCommand() == GO_SELECT)
				GOSelect_actionPerformed();
			if (act.getActionCommand() == GO_DESELECT)
				GODeselect_actionPerformed();
		}
	};

	private void initializeComponents()
	{
		editMethodPanel=new JPanel();
		editMethodPanel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
		editMethodPanel.setPreferredSize(new Dimension(460, 50));
		editMethodPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		//create methodPanel
		methodPanel=new JPanel();
		methodPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		//method label
		methodLabel=new JLabel("Edit Method");
		methodPanel.add(methodLabel);
		//method option box
		methodComboBox=new JComboBox<String>();
		methodComboBox.addItem(DIRECT_HALLMARK_EDIT);
		methodComboBox.addItem(GO_HALLMARK_MAP_EDIT);
		methodComboBox.setSelectedIndex(0);
		methodComboBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e){
				if(e.getStateChange()==ItemEvent.SELECTED)
				{
					CardLayout cl = (CardLayout)(editPanel.getLayout());
					currLayout=(String)e.getItem();
				    cl.show(editPanel, currLayout);
					System.out.println("selected method:"+methodComboBox.getSelectedItem().toString());
				}
			}
		});
		methodPanel.add(methodComboBox);
		editMethodPanel.add(methodPanel);
		
		//editPanel
		editPanel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
		editPanel.setPreferredSize(new Dimension(460, 250));
		editPanel.setLayout(new CardLayout());
		editPanel.add(directEditPanel, DIRECT_HALLMARK_EDIT);
		editPanel.add(GOassistedEditPanel, GO_HALLMARK_MAP_EDIT);
		
		//directEditPanel
		directEditPanel.setPreferredSize(new Dimension(400, 400));
		directEditPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		JPanel directEditLeftPanel=new JPanel();
		directEditLeftPanel.setLayout(new BoxLayout(directEditLeftPanel, BoxLayout.Y_AXIS));
		JLabel hallmarkListLabel=new JLabel("Hallmark list:");
		JPanel hallmarkListLabelPanel=new JPanel();
		hallmarkListLabelPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		hallmarkListLabelPanel.add(hallmarkListLabel);
		//hallmarkList
		ArrayList<Integer> hallmarkListDB=postgreDB.getHallmark_hallmarkOfNode(currNode);
		hallmarkToAdd=new ArrayList<String>();
		hallmarkSelected=new ArrayList<String>();
		for(int i=0; i<hallmarkListDB.size(); i++)
		{
			if(hallmarkListDB.get(i)==1)
				hallmarkSelected.add(entireHallmarkList.get(i));
			if(hallmarkListDB.get(i)==0)
				hallmarkToAdd.add(entireHallmarkList.get(i));
		}
		System.out.println("hallmarkSelected:"+hallmarkSelected);
		System.out.println("hallmarkToAdd:"+hallmarkToAdd);
		String[] formattedHallmarkList=new String[hallmarkToAdd.size()];
		formattedHallmarkList = hallmarkToAdd.toArray(formattedHallmarkList);
		hallmarkToAddSelectionList=new JList<String>();
		hallmarkToAddSelectionList.setListData(formattedHallmarkList);
		hallmarkToAddScrollPane = new JScrollPane(hallmarkToAddSelectionList);
		hallmarkToAddSelectionList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		hallmarkToAddScrollPane.setViewportView(hallmarkToAddSelectionList);
		hallmarkToAddScrollPane.setPreferredSize(new Dimension(350, 250));
		hallmarkToAddScrollPane.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		directEditLeftPanel.add(hallmarkListLabelPanel);
		directEditLeftPanel.add(hallmarkToAddScrollPane);
		
		JPanel directEditMiddlePanel=new JPanel();
		directEditMiddlePanel.setLayout(new BoxLayout(directEditMiddlePanel, BoxLayout.Y_AXIS));
		selectButton= new JButton(">");
		selectButton.setSize(90, 20);
		selectButton.setActionCommand(SELECT);
		selectButton.addActionListener(actionListener);
		JLabel emptyLabel=new JLabel(" ");
		emptyLabel.setSize(90, 60);
		deselectButton= new JButton("<");
		deselectButton.setSize(90, 20);
		deselectButton.setActionCommand(DESELECT);
		deselectButton.addActionListener(actionListener);
		directEditMiddlePanel.add(selectButton);
		directEditMiddlePanel.add(emptyLabel);
		directEditMiddlePanel.add(deselectButton);
		
		JPanel directEditRightPanel=new JPanel();
		directEditRightPanel.setLayout(new BoxLayout(directEditRightPanel, BoxLayout.Y_AXIS));
		JLabel hallmarkOfNodeLabel=new JLabel("Hallmark of Node:");
		JPanel hallmarkOfNodeLabelPanel=new JPanel();
		hallmarkOfNodeLabelPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		hallmarkOfNodeLabelPanel.add(hallmarkOfNodeLabel);
		String[] formattedHallmarkList2=new String[hallmarkSelected.size()];
		formattedHallmarkList2 = hallmarkSelected.toArray(formattedHallmarkList2);
		hallmarkSelectedSelectionList=new JList<String>();
		hallmarkSelectedSelectionList.setListData(formattedHallmarkList2);
		hallmarkSelectedScrollPane = new JScrollPane(hallmarkSelectedSelectionList);
		hallmarkSelectedSelectionList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		hallmarkSelectedScrollPane.setViewportView(hallmarkSelectedSelectionList);
		hallmarkSelectedScrollPane.setPreferredSize(new Dimension(350, 250));
		hallmarkSelectedScrollPane.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		directEditRightPanel.add(hallmarkOfNodeLabelPanel);
		directEditRightPanel.add(hallmarkSelectedScrollPane);
		
		directEditPanel.add(directEditLeftPanel);
		directEditPanel.add(directEditMiddlePanel);
		directEditPanel.add(directEditRightPanel);
		
		//GO-assisted edit panel
		GOassistedEditPanel.setPreferredSize(new Dimension(300, 250));
		GOassistedEditPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		JPanel GOassistedEditLeftPanel=new JPanel();
		GOassistedEditLeftPanel.setLayout(new BoxLayout(GOassistedEditLeftPanel, BoxLayout.Y_AXIS));
		JLabel GOListLabel=new JLabel("GO list:");
		JPanel GOListLabelPanel=new JPanel();
		GOListLabelPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		GOListLabelPanel.add(GOListLabel);
		//GOList
		GOToAdd=new ArrayList<ArrayList<String>>();
		GOSelected=new ArrayList<ArrayList<String>>();
		ArrayList<String> GOListOfNodeFromDB=postgreDB.getHallmark_retrieveGOOfNodeInHallmarkGOMapping(currNode);
		ArrayList<ArrayList<String>> GOFromDB=new ArrayList<ArrayList<String>>();
		for(int i=0; i<GOListOfNodeFromDB.size(); i++)
		{
			int index=entireHallmarkGOIDList.indexOf(GOListOfNodeFromDB.get(i));
			GOFromDB.add(entireHallmarkGOList.get(index));
		}
		for(int i=0; i<entireHallmarkGOList.size(); i++)
		{
			if(GOFromDB.contains(entireHallmarkGOList.get(i))==true)
				GOSelected.add(entireHallmarkGOList.get(i));
			else
				GOToAdd.add(entireHallmarkGOList.get(i));
		}
		String[] formattedHallmarkGOList=new String[GOToAdd.size()];
		ArrayList<String> GOToAdd_Int=new ArrayList<String>();
		for(int i=0; i<GOToAdd.size(); i++)
			GOToAdd_Int.add(GOToAdd.get(i).get(0)+"; "+GOToAdd.get(i).get(1));
		formattedHallmarkGOList = GOToAdd_Int.toArray(formattedHallmarkGOList);
		GOToAddSelectionList=new JList<String>();
		GOToAddSelectionList.setListData(formattedHallmarkGOList);
		GOToAddScrollPane = new JScrollPane(GOToAddSelectionList);
		GOToAddSelectionList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		GOToAddScrollPane.setViewportView(GOToAddSelectionList);
		GOToAddScrollPane.setPreferredSize(new Dimension(300, 250));
		GOToAddScrollPane.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		GOassistedEditLeftPanel.add(GOListLabelPanel);
		GOassistedEditLeftPanel.add(GOToAddScrollPane);
		
		JPanel GOassistedEditMiddlePanel=new JPanel();
		GOassistedEditMiddlePanel.setLayout(new BoxLayout(GOassistedEditMiddlePanel, BoxLayout.Y_AXIS));
		GOSelectButton= new JButton(">");
		GOSelectButton.setSize(50, 20);
		GOSelectButton.setActionCommand(GO_SELECT);
		GOSelectButton.addActionListener(actionListener);
		JLabel emptyLabel1=new JLabel(" ");
		emptyLabel1.setSize(50, 60);
		GODeselectButton= new JButton("<");
		GODeselectButton.setSize(50, 20);
		GODeselectButton.setActionCommand(GO_DESELECT);
		GODeselectButton.addActionListener(actionListener);
		GOassistedEditMiddlePanel.add(GOSelectButton);
		GOassistedEditMiddlePanel.add(emptyLabel1);
		GOassistedEditMiddlePanel.add(GODeselectButton);
		
		JPanel GOassistedEditRightPanel=new JPanel();
		GOassistedEditRightPanel.setLayout(new BoxLayout(GOassistedEditRightPanel, BoxLayout.Y_AXIS));
		JLabel GOOfNodeLabel=new JLabel("Hallmark-GO of Node:");
		JPanel GOOfNodeLabelPanel=new JPanel();
		GOOfNodeLabelPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		GOOfNodeLabelPanel.add(GOOfNodeLabel);
		String[] formattedGOList2=new String[GOSelected.size()];
		ArrayList<String> GOSelected_Int=new ArrayList<String>();
		for(int i=0; i<GOSelected.size(); i++)
			GOSelected_Int.add(GOSelected.get(i).get(0)+"; "+GOSelected.get(i).get(1));
		formattedGOList2 = GOSelected_Int.toArray(formattedGOList2);
		GOSelectedSelectionList=new JList<String>();
		GOSelectedSelectionList.setListData(formattedGOList2);
		GOSelectedScrollPane = new JScrollPane(GOSelectedSelectionList);
		GOSelectedSelectionList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		GOSelectedScrollPane.setViewportView(GOSelectedSelectionList);
		GOSelectedScrollPane.setPreferredSize(new Dimension(300, 250));
		GOSelectedScrollPane.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		GOassistedEditRightPanel.add(GOOfNodeLabelPanel);
		GOassistedEditRightPanel.add(GOSelectedScrollPane);
		
		JPanel GOassistedEditHallmarkPanel=new JPanel();
		GOassistedEditHallmarkPanel.setLayout(new BoxLayout(GOassistedEditHallmarkPanel, BoxLayout.Y_AXIS));
		JLabel GOHallmarkOfNodeLabel=new JLabel("Hallmark of Node:");
		JPanel GOHallmarkOfNodeLabelPanel=new JPanel();
		GOHallmarkOfNodeLabelPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		GOHallmarkOfNodeLabelPanel.add(GOHallmarkOfNodeLabel);
		ArrayList<String> GOHallmarkListDB=postgreDB.getHallmark_retrieveHallmarkBasedOnGivenGOAnnotation(GOSelected);
		System.out.println("GOHallmarkListDB:"+GOHallmarkListDB.toString());
		GOHallmarkList=new ArrayList<String>();
		for(int i=0; i<GOHallmarkListDB.size(); i++)
		{
			int index=entireHallmarkListDB.indexOf(GOHallmarkListDB.get(i));
			GOHallmarkList.add(entireHallmarkList.get(index));
		}
		System.out.println("GOHallmarkList:"+GOHallmarkList.toString());
		String[] formattedGOHallmarkList=new String[GOHallmarkList.size()];
		formattedGOHallmarkList = GOHallmarkList.toArray(formattedGOHallmarkList);
		GOHallmarkSelectedJList=new JList<String>();
		GOHallmarkSelectedJList.setListData(formattedGOHallmarkList);
		GOHallmarkSelectedScrollPane = new JScrollPane(GOHallmarkSelectedJList);
		GOHallmarkSelectedJList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		GOHallmarkSelectedScrollPane.setViewportView(GOHallmarkSelectedJList);
		GOHallmarkSelectedScrollPane.setPreferredSize(new Dimension(250, 250));
		GOHallmarkSelectedScrollPane.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		GOassistedEditHallmarkPanel.add(GOHallmarkOfNodeLabelPanel);
		GOassistedEditHallmarkPanel.add(GOHallmarkSelectedScrollPane);
		
		GOassistedEditPanel.add(GOassistedEditLeftPanel);
		GOassistedEditPanel.add(GOassistedEditMiddlePanel);
		GOassistedEditPanel.add(GOassistedEditRightPanel);
		GOassistedEditPanel.add(GOassistedEditHallmarkPanel);
		
		
		//addAnnotation and cancel button
		buttonPanel=new JPanel();
		buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
		updateHallmarkButton= new JButton();
		updateHallmarkButton.setSize(50, 20);
		updateHallmarkButton.setText(UPDATE_HALLMARK);
		updateHallmarkButton.setActionCommand(UPDATE_HALLMARK);
		updateHallmarkButton.addActionListener(actionListener);
		cancelButton= new JButton();
		cancelButton.setSize(50, 20);
		cancelButton.setText(DONE);
		cancelButton.setActionCommand(DONE);
		cancelButton.addActionListener(actionListener);
		buttonPanel.add(updateHallmarkButton);
		buttonPanel.add(cancelButton);
		
		setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));
		add(editMethodPanel);
		add(editPanel);
		add(buttonPanel);
	}
	
	private void updateGOSelectionButtonAndGOSelectionList()
	{
		if(GOSelected.size()==entireHallmarkGOList.size())
			GOSelectButton.setEnabled(false);
		else
			GOSelectButton.setEnabled(true);
		if(GOToAdd.size()==entireHallmarkGOList.size())
			GODeselectButton.setEnabled(false);
		else
			GODeselectButton.setEnabled(true);
		
		String[] formattedGOList=new String[GOToAdd.size()];
		ArrayList<String> GOToAdd_Int=new ArrayList<String>();
		for(int i=0; i<GOToAdd.size(); i++)
			GOToAdd_Int.add(GOToAdd.get(i).get(0)+"; "+GOToAdd.get(i).get(1));
		formattedGOList = GOToAdd_Int.toArray(formattedGOList);
		GOToAddSelectionList.setListData(formattedGOList);
		
		String[] formattedGOList1=new String[GOSelected.size()];
		ArrayList<String> GOSelected_Int=new ArrayList<String>();
		for(int i=0; i<GOSelected.size(); i++)
			GOSelected_Int.add(GOSelected.get(i).get(0)+"; "+GOSelected.get(i).get(1));
		formattedGOList1 = GOSelected_Int.toArray(formattedGOList1);
		GOSelectedSelectionList.setListData(formattedGOList1);
		
		//update GOHallmarkList
		ArrayList<String> GOHallmarkListDB=postgreDB.getHallmark_retrieveHallmarkBasedOnGivenGOAnnotation(GOSelected);
		GOHallmarkList=new ArrayList<String>();
		for(int i=0; i<GOHallmarkListDB.size(); i++)
		{
			int index=entireHallmarkListDB.indexOf(GOHallmarkListDB.get(i));
			GOHallmarkList.add(entireHallmarkList.get(index));
		}
		String[] formattedGOHallmarkList=new String[GOHallmarkList.size()];
		formattedGOHallmarkList = GOHallmarkList.toArray(formattedGOHallmarkList);
		GOHallmarkSelectedJList.setListData(formattedGOHallmarkList);
	}
	
	private void GOSelect_actionPerformed()
	{
		if(GOToAddSelectionList.getSelectedIndex()!=-1)
		{
			String AGOToAddString=GOToAddSelectionList.getSelectedValue();
			ArrayList<String> AGOToAdd=new ArrayList<String>();
			String delimiter=";";
			int delimiterIndex=AGOToAddString.indexOf(delimiter);
			AGOToAdd.add(AGOToAddString.substring(0,delimiterIndex).trim());
			AGOToAdd.add(AGOToAddString.substring(delimiterIndex+1).trim());
			GOSelected.add(AGOToAdd);
			GOToAdd.remove(AGOToAdd);
			
			updateGOSelectionButtonAndGOSelectionList();
		}
	}
	
	private void GODeselect_actionPerformed()
	{
		if(GOSelectedSelectionList.getSelectedIndex()!=-1)
		{
			String AGOToAddString=GOSelectedSelectionList.getSelectedValue();
			ArrayList<String> AGOToAdd=new ArrayList<String>();
			String delimiter=";";
			int delimiterIndex=AGOToAddString.indexOf(delimiter);
			AGOToAdd.add(AGOToAddString.substring(0,delimiterIndex).trim());
			AGOToAdd.add(AGOToAddString.substring(delimiterIndex+1).trim());
			GOSelected.remove(AGOToAdd);
			GOToAdd.add(AGOToAdd);
			
			updateGOSelectionButtonAndGOSelectionList();
		}
	}
	
	private void updateSelectionButtonAndSelectionList()
	{
		if(hallmarkSelected.size()==entireHallmarkList.size())
			selectButton.setEnabled(false);
		else
			selectButton.setEnabled(true);
		if(hallmarkToAdd.size()==entireHallmarkList.size())
			deselectButton.setEnabled(false);
		else
			deselectButton.setEnabled(true);
		
		String[] formattedHallmarkList=new String[hallmarkToAdd.size()];
		formattedHallmarkList = hallmarkToAdd.toArray(formattedHallmarkList);
		hallmarkToAddSelectionList.setListData(formattedHallmarkList);
		
		String[] formattedHallmarkList1=new String[hallmarkSelected.size()];
		formattedHallmarkList1 = hallmarkSelected.toArray(formattedHallmarkList1);
		hallmarkSelectedSelectionList.setListData(formattedHallmarkList1);
	}
	
	private void select_actionPerformed()
	{
		if(hallmarkToAddSelectionList.getSelectedIndex()!=-1)
		{
			String AHallmarkToAdd=hallmarkToAddSelectionList.getSelectedValue();
			hallmarkSelected.add(AHallmarkToAdd);
			hallmarkToAdd.remove(AHallmarkToAdd);
			
			updateSelectionButtonAndSelectionList();
		}
	}
	
	private void deselect_actionPerformed()
	{
		if(hallmarkSelectedSelectionList.getSelectedIndex()!=-1)
		{
			String AHallmarkToRemove=hallmarkSelectedSelectionList.getSelectedValue();
			hallmarkSelected.remove(AHallmarkToRemove);
			hallmarkToAdd.add(AHallmarkToRemove);
			
			updateSelectionButtonAndSelectionList();
		}
	}
	
	private void updateHallmark_actionPerformed(){
		ArrayList<String> hallmarkToPassToDB=new ArrayList<String>();
		ArrayList<String> hallmarkArrayToUse=new ArrayList<String>();
		for(int i=0; i<entireHallmarkList.size(); i++)
			hallmarkToPassToDB.add("0");
		
		if(currLayout.compareTo(DIRECT_HALLMARK_EDIT)==0)
			hallmarkArrayToUse=hallmarkSelected;
		else
			hallmarkArrayToUse=GOHallmarkList;
		for(int i=0; i<hallmarkArrayToUse.size(); i++)
		{
			int indexToEntireHallmarkList=entireHallmarkList.indexOf(hallmarkArrayToUse.get(i));
			hallmarkToPassToDB.set(indexToEntireHallmarkList,"1");
		}
			
		postgreDB.update_hallmark(currNode, hallmarkToPassToDB);
		if(currLayout.compareTo(GO_HALLMARK_MAP_EDIT)==0)
			postgreDB.update_hallmarkGO(currNode, GOSelected);
	}
	
	private void cancel_actionPerformed(){
		setVisible(false);
	}
}
