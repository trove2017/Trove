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
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import constants.stringConstant;

import fileObject.fReaderWriter;

@SuppressWarnings("serial")

public class editHallmarkGOMappingDialog extends JDialog{
	private JButton deleteHallmarkButton, deleteGOTermButton, updateButton, addButton;
	private JList<String> hallmarkList;
	private JScrollPane hallmarkScrollPane;
	private JList<String> GOList;
	private JScrollPane GOScrollPane;
	private ArrayList<ArrayList<String>> hallmarkGOMapping=new ArrayList<ArrayList<String>>();
	private ArrayList<String> hallmarkNameList=new ArrayList<String>();
	private JTextField GOIDTextfield, GONameTextfield;
	private JComboBox<String> hallmarkNameComboBox;
	private JComboBox<String> goTypeComboBox;

	private ArrayList<String> hallmarkNameListForDisplay=new ArrayList<String>();
	private ArrayList<String> goTermListForDisplay=new ArrayList<String>();
	private ArrayList<String> entireHallmarkList=new ArrayList<String>();
	private ArrayList<String> entireHallmarkListDB=new ArrayList<String>();
	private ArrayList<String> entireGOTypeList=new ArrayList<String>();

	protected final static String UPDATE = "Update";
	protected final static String DELETE_HALLMARK = "Delete Hallmark";
	protected final static String DELETE_GO_TERM = "Delete GO Term";
	protected final static String ADD = "Add";

	private String mappingFile;
	int selectedIndexHallmark, selectedIndexGO;

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

	private String BIOLOGICAL_PROCESS="biological process";
	private String MOLECULAR_FUNCTION="molecular function";
	private String LOCALIZATION="localization";

	private stringConstant staticString=new stringConstant();
	private fReaderWriter readerWriter=new fReaderWriter();

	public editHallmarkGOMappingDialog(String dir, String mFile)
	{
		//initialize variables
		mappingFile=mFile;

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

		entireGOTypeList.add(BIOLOGICAL_PROCESS);
		entireGOTypeList.add(MOLECULAR_FUNCTION);
		entireGOTypeList.add(LOCALIZATION);

		//set JDialog properties
		setTitle("Edit Cancer Hallmark Mapping");
		setSize(655,370);
		setLocationRelativeTo(null);
		setResizable(false);
		setModal(true);

		//configure components in JDialog
		ArrayList<String> temp_hallmarkGOMapping=readerWriter.readFromFileWithBufferedReaderArraySkipHeader(mappingFile);
		hallmarkNameList=retrieveHallmarkNameList(temp_hallmarkGOMapping);
		hallmarkNameListForDisplay=retrieveHallmarkNameListForDisplay(hallmarkNameList);
		hallmarkGOMapping=retrieveHallmarkGOMappingDetails(hallmarkNameList, temp_hallmarkGOMapping);
		initializeComponents();
	}

	private ArrayList<String> retrieveHallmarkNameListForDisplay(ArrayList<String> list)
	{
		ArrayList<String> listToDisplay=new ArrayList<String>();

		for(int i=0; i<list.size(); i++)
		{
			int index=entireHallmarkListDB.indexOf(list.get(i));
			listToDisplay.add(entireHallmarkList.get(index));
		}

		return listToDisplay;
	}

	private ArrayList<String> retrieveHallmarkNameList(ArrayList<String> tempArray)
	{
		ArrayList<String> nameList=new ArrayList<String>();
		String delimiter=",";
		int delimiterIndex;

		for(int i=0; i<tempArray.size(); i++)
		{
			String str=tempArray.get(i);
			delimiterIndex=str.indexOf(delimiter);
			String hallmarkName=str.substring(0,delimiterIndex);

			if(nameList.contains(hallmarkName)==false)
				nameList.add(hallmarkName);
		}
		return nameList;
	}

	private ArrayList<ArrayList<String>> retrieveHallmarkGOMappingDetails(ArrayList<String> nameList, ArrayList<String> tempArray)
	{
		ArrayList<ArrayList<String>> detailList=new ArrayList<ArrayList<String>>();
		String delimiter=",";
		int delimiterIndex;

		for(int h=0; h<nameList.size(); h++)
		{
			String thisHallmark=nameList.get(h);
			ArrayList<String> element=new ArrayList<String>();

			for(int i=0; i<tempArray.size(); i++)
			{
				String str=tempArray.get(i);
				delimiterIndex=str.indexOf(delimiter);
				String hallmarkName=str.substring(0,delimiterIndex);
				String goContents=str.substring(delimiterIndex+1);

				if(thisHallmark.compareTo(hallmarkName)==0)
					element.add(goContents);
			}
			detailList.add(element);
		}
		return detailList;
	}

	private ActionListener actionListener = new ActionListener() 
	{
		public void actionPerformed(ActionEvent act) {
			if (act.getActionCommand() == UPDATE)
				update_actionPerformed();
			if (act.getActionCommand() == DELETE_HALLMARK)
				deleteHallmark_actionPerformed();
			if (act.getActionCommand() == DELETE_GO_TERM)
				deleteGoTerm_actionPerformed();
			if (act.getActionCommand() == ADD)
				add_actionPerformed();
		}
	};

	private void add_actionPerformed() 
	{
		int selectedHallmarkIndex = hallmarkList.getSelectedIndex();
		int selectedGOIndex=GOList.getSelectedIndex();
		if(selectedHallmarkIndex!=-1 && selectedGOIndex!=-1)
		{
			// hallmark selected in combobox = hallmark A 
			// go selected in textfield = go C
			// add new entry (hallmark A, go C) if it does not exist. If new entry exist, update its GO name and type
			String temp_hallmarkName=hallmarkNameComboBox.getSelectedItem().toString();
			int hallmarkIndex=entireHallmarkList.indexOf(temp_hallmarkName);
			String hallmark_A=entireHallmarkListDB.get(hallmarkIndex);
			String go_C=GOIDTextfield.getText();

			String goName=GONameTextfield.getText();
			String goType=goTypeComboBox.getSelectedItem().toString();
			String entry=go_C+","+goName+","+goType;
			int newHallmarkIndex=hallmarkNameList.indexOf(hallmark_A);
			if(newHallmarkIndex==-1)//this hallmark don't even exists, so just add the whole thing
			{
				hallmarkNameList.add(hallmark_A);
				ArrayList<String> newHallmarkEntry=new ArrayList<String>();
				newHallmarkEntry.add(entry);
				hallmarkGOMapping.add(newHallmarkEntry);
			}
			else//check if go term for this hallmark exists
			{
				ArrayList<String> goTermForThisHallmark=new ArrayList<String>();
				ArrayList<String> thisHallmarkGODetails=hallmarkGOMapping.get(newHallmarkIndex);
				for(int i=0; i<thisHallmarkGODetails.size(); i++)
				{
					String goDetails=thisHallmarkGODetails.get(i);
					String delimiter=",";
					int delimiterIndex=goDetails.indexOf(delimiter);
					goTermForThisHallmark.add(goDetails.substring(0,delimiterIndex));
				}
				int goIndex=goTermForThisHallmark.indexOf(go_C);
				if(goIndex==-1)//this go term does not exists yet, so add entry
					thisHallmarkGODetails.add(entry);
				else//update that go term
				{
					thisHallmarkGODetails.set(goIndex, entry);
					JOptionPane.showMessageDialog(new JFrame(), "GO TERM: "+go_C+" already exists for HALLMARK "+hallmarkList.getSelectedValue()+". GO name and type are updated instead.", "Information", JOptionPane.INFORMATION_MESSAGE);
				}
				hallmarkGOMapping.set(newHallmarkIndex, thisHallmarkGODetails);
			}


			//update dialog
			goTermListForDisplay=retrieveGOTermListForDisplay();
			String[] formattedGOList=new String[goTermListForDisplay.size()];
			formattedGOList = goTermListForDisplay.toArray(formattedGOList);
			GOList.setListData(formattedGOList);

			//update mappingFile
			ArrayList<String> fileContentArray=new ArrayList<String>();
			fileContentArray.add("Hallmark,GO_id,GO_name,GO_type");//headerRow
			for(int i=0; i<hallmarkGOMapping.size(); i++)
			{
				String hallmark=hallmarkNameList.get(i);
				ArrayList<String> thisHallmarkGOMapping=hallmarkGOMapping.get(i);
				for(int j=0; j<thisHallmarkGOMapping.size(); j++)
				{
					String s=hallmark+","+thisHallmarkGOMapping.get(j);
					fileContentArray.add(s);
				}
			}
			readerWriter.writeToFileWithBufferedWriterArray(mappingFile, fileContentArray, true, false);
		}
	}

	private void update_actionPerformed() 
	{
		int selectedHallmarkIndex = hallmarkList.getSelectedIndex();
		int selectedGOIndex=GOList.getSelectedIndex();
		if(selectedHallmarkIndex!=-1 && selectedGOIndex!=-1)
		{
			// hallmark selected in combobox = hallmark A 
			// hallmark selected in list = hallmark B
			// go selected in textfield = go C
			// go selected in list = go D
			// hallmark A=hallmark B, go C=go D -> go to record (hallmark B, go D) to update GO name and type
			// hallmark A=hallmark B, go C<>go D -> go to record (hallmark B, go D) to update GO ID, name and type
			// hallmark A<>hallmark B -> go to record (hallmark B, go D) to remove entry, add new entry (hallmark A, go C) if it does not exist. 
			//                           If new entry exist, update its GO name and type
			String temp_hallmarkName=hallmarkList.getSelectedValue();
			int hallmarkIndex=entireHallmarkList.indexOf(temp_hallmarkName);
			String hallmark_B=entireHallmarkListDB.get(hallmarkIndex);
			temp_hallmarkName=hallmarkNameComboBox.getSelectedItem().toString();
			hallmarkIndex=entireHallmarkList.indexOf(temp_hallmarkName);
			String hallmark_A=entireHallmarkListDB.get(hallmarkIndex);

			String go_C=GOIDTextfield.getText();

			if(hallmark_A.compareTo(hallmark_B)==0)
			{
				String goName=GONameTextfield.getText();
				String goType=goTypeComboBox.getSelectedItem().toString();
				String entry=go_C+","+goName+","+goType;
				hallmarkGOMapping.get(selectedHallmarkIndex).set(selectedGOIndex, entry);
			}
			else
			{
				hallmarkGOMapping.get(selectedHallmarkIndex).remove(selectedGOIndex);
				String goName=GONameTextfield.getText();
				String goType=goTypeComboBox.getSelectedItem().toString();
				String entry=go_C+","+goName+","+goType;
				int newHallmarkIndex=hallmarkNameList.indexOf(hallmark_A);
				if(newHallmarkIndex==-1)//this hallmark don't even exists, so just add the whole thing
				{
					hallmarkNameList.add(hallmark_A);
					ArrayList<String> newHallmarkEntry=new ArrayList<String>();
					newHallmarkEntry.add(entry);
					hallmarkGOMapping.add(newHallmarkEntry);
				}
				else//check if go term for this hallmark exists
				{
					ArrayList<String> goTermForThisHallmark=new ArrayList<String>();
					ArrayList<String> thisHallmarkGODetails=hallmarkGOMapping.get(newHallmarkIndex);
					for(int i=0; i<thisHallmarkGODetails.size(); i++)
					{
						String goDetails=thisHallmarkGODetails.get(i);
						String delimiter=",";
						int delimiterIndex=goDetails.indexOf(delimiter);
						goTermForThisHallmark.add(goDetails.substring(0,delimiterIndex));
					}
					int goIndex=goTermForThisHallmark.indexOf(go_C);
					if(goIndex==-1)//this go term does not exists yet, so add entry
						thisHallmarkGODetails.add(entry);
					else//update that go term
						thisHallmarkGODetails.set(goIndex, entry);
					hallmarkGOMapping.set(newHallmarkIndex, thisHallmarkGODetails);
				}
			}

			//update dialog
			goTermListForDisplay=retrieveGOTermListForDisplay();
			String[] formattedGOList=new String[goTermListForDisplay.size()];
			formattedGOList = goTermListForDisplay.toArray(formattedGOList);
			GOList.setListData(formattedGOList);

			//update mappingFile
			ArrayList<String> fileContentArray=new ArrayList<String>();
			fileContentArray.add("Hallmark,GO_id,GO_name,GO_type");//headerRow
			for(int i=0; i<hallmarkGOMapping.size(); i++)
			{
				String hallmark=hallmarkNameList.get(i);
				ArrayList<String> thisHallmarkGOMapping=hallmarkGOMapping.get(i);
				for(int j=0; j<thisHallmarkGOMapping.size(); j++)
				{
					String s=hallmark+","+thisHallmarkGOMapping.get(j);
					fileContentArray.add(s);
				}
			}
			readerWriter.writeToFileWithBufferedWriterArray(mappingFile, fileContentArray, true, false);
		}
	}

	private void deleteHallmark_actionPerformed()
	{
		int selectedIndex = hallmarkList.getSelectedIndex();
		if(selectedIndex!=-1)
		{
			//get hallmark to delete
			hallmarkGOMapping.remove(selectedIndex);
			hallmarkNameList.remove(selectedIndex);
			hallmarkNameListForDisplay=retrieveHallmarkNameListForDisplay(hallmarkNameList);
			goTermListForDisplay=new ArrayList<String>();
			//update dialog
			hallmarkNameComboBox.setSelectedIndex(0);
			goTypeComboBox.setSelectedIndex(0);
			GOIDTextfield.setText("");
			GONameTextfield.setText("");
			String[] formattedHallmarkList=new String[hallmarkNameListForDisplay.size()];
			formattedHallmarkList = hallmarkNameListForDisplay.toArray(formattedHallmarkList);
			hallmarkList.setListData(formattedHallmarkList);
			String[] formattedGOList=new String[goTermListForDisplay.size()];
			formattedGOList = goTermListForDisplay.toArray(formattedGOList);
			GOList.setListData(formattedGOList);

			//update mappingFile
			ArrayList<String> fileContentArray=new ArrayList<String>();
			fileContentArray.add("Hallmark,GO_id,GO_name,GO_type");//headerRow
			for(int i=0; i<hallmarkGOMapping.size(); i++)
			{
				String hallmark=hallmarkNameList.get(i);
				ArrayList<String> thisHallmarkGOMapping=hallmarkGOMapping.get(i);
				for(int j=0; j<thisHallmarkGOMapping.size(); j++)
				{
					String s=hallmark+","+thisHallmarkGOMapping.get(j);
					fileContentArray.add(s);
				}
			}
			readerWriter.writeToFileWithBufferedWriterArray(mappingFile, fileContentArray, true, false);
		}
	}

	private ArrayList<String> retrieveGOTermListForDisplay()
	{
		ArrayList<String> displayList=new ArrayList<String>();
		int selectedIndex = hallmarkList.getSelectedIndex();
		if(selectedIndex!=-1)
		{
			//update details of newly selected view
			ArrayList<String> thisHallmarkGOMapping=hallmarkGOMapping.get(selectedIndex);
			String delimiter=",";
			int delimiterIndex;
			displayList=new ArrayList<String>();

			for(int i=0; i<thisHallmarkGOMapping.size(); i++)
			{
				String details=thisHallmarkGOMapping.get(i);
				delimiterIndex=details.indexOf(delimiter);
				displayList.add(details.substring(0,delimiterIndex));
			}
		}

		return displayList;
	}

	private void deleteGoTerm_actionPerformed()
	{
		int selectedHallmarkIndex = hallmarkList.getSelectedIndex();
		int selectedGOIndex=GOList.getSelectedIndex();
		if(selectedHallmarkIndex!=-1 && selectedGOIndex!=-1)
		{
			//get GO term to delete
			hallmarkGOMapping.get(selectedHallmarkIndex).remove(selectedGOIndex);
			goTermListForDisplay=retrieveGOTermListForDisplay();

			//update dialog
			goTypeComboBox.setSelectedIndex(0);
			GOIDTextfield.setText("");
			GONameTextfield.setText("");
			String[] formattedGOList=new String[goTermListForDisplay.size()];
			formattedGOList = goTermListForDisplay.toArray(formattedGOList);
			GOList.setListData(formattedGOList);

			//update mappingFile
			ArrayList<String> fileContentArray=new ArrayList<String>();
			fileContentArray.add("Hallmark,GO_id,GO_name,GO_type");//headerRow
			for(int i=0; i<hallmarkGOMapping.size(); i++)
			{
				String hallmark=hallmarkNameList.get(i);
				ArrayList<String> thisHallmarkGOMapping=hallmarkGOMapping.get(i);
				for(int j=0; j<thisHallmarkGOMapping.size(); j++)
				{
					String s=hallmark+","+thisHallmarkGOMapping.get(j);
					fileContentArray.add(s);
				}
			}
			readerWriter.writeToFileWithBufferedWriterArray(mappingFile, fileContentArray, true, false);
		}
	}

	private void hallmarkList_valueChanged(ListSelectionEvent e) 
	{
		if (!e.getValueIsAdjusting()) 
		{
			if(hallmarkList.getSelectedValue()!=null)
			{
				int selectedIndex = hallmarkList.getSelectedIndex();
				if(selectedIndex!=-1)
				{
					//update details of newly selected view
					goTermListForDisplay=retrieveGOTermListForDisplay();
					String[] formattedGOList=new String[goTermListForDisplay.size()];
					formattedGOList = goTermListForDisplay.toArray(formattedGOList);
					GOList.setListData(formattedGOList);

					//update hallmarkName combo box
					int comboBoxIndex=entireHallmarkList.indexOf(hallmarkList.getSelectedValue());
					hallmarkNameComboBox.setSelectedIndex(comboBoxIndex);
				}
			}
		}
	}

	private void goList_valueChanged(ListSelectionEvent e) 
	{
		if (!e.getValueIsAdjusting()) 
		{
			if(GOList.getSelectedValue()!=null && hallmarkList.getSelectedValue()!=null)
			{
				int hallmarkSelectedIndex = hallmarkList.getSelectedIndex();
				int GOSelectedIndex=GOList.getSelectedIndex();
				if(hallmarkSelectedIndex!=-1 && GOSelectedIndex!=-1)
				{
					//update details of newly selected view
					ArrayList<String> thisHallmarkGOMapping=hallmarkGOMapping.get(hallmarkSelectedIndex);
					String GODetails=thisHallmarkGOMapping.get(GOSelectedIndex);

					String delimiter=",";
					int delimiterIndex;
					String thisGOID, thisGODescription, thisGOType;
					delimiterIndex=GODetails.indexOf(delimiter);
					thisGOID=GODetails.substring(0, delimiterIndex);
					GODetails=GODetails.substring(delimiterIndex+1);
					delimiterIndex=GODetails.indexOf(delimiter);
					thisGODescription=GODetails.substring(0, delimiterIndex);
					thisGOType=GODetails.substring(delimiterIndex+1);

					int index=entireGOTypeList.indexOf(thisGOType);

					GOIDTextfield.setText(thisGOID);
					GONameTextfield.setText(thisGODescription);
					goTypeComboBox.setSelectedIndex(index);
				}
			}
		}
	}

	private void initializeComponents()
	{
		JPanel deleteLeftPanel=new JPanel();
		deleteLeftPanel.setPreferredSize(new Dimension(220, 250));
		deleteLeftPanel.setLayout(new BoxLayout(deleteLeftPanel, BoxLayout.Y_AXIS));

		JPanel deleteRightPanel=new JPanel();
		deleteRightPanel.setPreferredSize(new Dimension(120, 250));
		deleteRightPanel.setLayout(new BoxLayout(deleteRightPanel, BoxLayout.Y_AXIS));

		JPanel deleteMainPanel=new JPanel();
		deleteMainPanel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
		deleteMainPanel.setPreferredSize(new Dimension(360, 265));
		deleteMainPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

		deleteMainPanel.add(deleteLeftPanel);
		deleteMainPanel.add(deleteRightPanel);

		//hallmark list for selection
		JPanel hallmarkListLabelPanel=new JPanel();
		hallmarkListLabelPanel.setPreferredSize(new Dimension(220, 20));
		hallmarkListLabelPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		JLabel hallmarkListLabel=new JLabel("Hallmark List:");
		hallmarkListLabelPanel.add(hallmarkListLabel);
		String[] formattedHallmarkList=new String[hallmarkNameListForDisplay.size()];
		formattedHallmarkList = hallmarkNameListForDisplay.toArray(formattedHallmarkList);
		hallmarkList=new JList<String>();
		hallmarkList.setListData(formattedHallmarkList);
		hallmarkScrollPane = new JScrollPane(hallmarkList);
		hallmarkList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		hallmarkList.addListSelectionListener(new ListSelectionListener() 
		{
			public void valueChanged(ListSelectionEvent e){hallmarkList_valueChanged(e);}
		});
		hallmarkScrollPane.setViewportView(hallmarkList);
		hallmarkScrollPane.setPreferredSize(new Dimension(220, 220));
		hallmarkScrollPane.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		deleteLeftPanel.add(hallmarkListLabelPanel);
		deleteLeftPanel.add(hallmarkScrollPane);

		//GO term list for selection
		JPanel goListLabelPanel=new JPanel();
		goListLabelPanel.setPreferredSize(new Dimension(120, 20));
		goListLabelPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		JLabel goListLabel=new JLabel("GO List:");
		goListLabelPanel.add(goListLabel);
		String[] formattedGOList=new String[goTermListForDisplay.size()];
		formattedGOList = goTermListForDisplay.toArray(formattedGOList);
		GOList=new JList<String>();
		GOList.setListData(formattedGOList);
		GOScrollPane = new JScrollPane(GOList);
		GOList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		GOList.addListSelectionListener(new ListSelectionListener() 
		{
			public void valueChanged(ListSelectionEvent e){goList_valueChanged(e);}
		});
		GOScrollPane.setViewportView(GOList);
		GOScrollPane.setPreferredSize(new Dimension(120, 220));
		GOScrollPane.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		deleteRightPanel.add(goListLabelPanel);
		deleteRightPanel.add(GOScrollPane);

		//edit panel
		JPanel editPanel=new JPanel();
		editPanel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
		editPanel.setPreferredSize(new Dimension(270, 265));
		editPanel.setLayout(new BoxLayout(editPanel, BoxLayout.Y_AXIS));

		//hallmark label
		JPanel editHallmarkLabelPanel=new JPanel();
		editHallmarkLabelPanel.setPreferredSize(new Dimension(260, 20));
		editHallmarkLabelPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		JLabel editHallmarkLabel=new JLabel("Hallmark:");
		editHallmarkLabelPanel.add(editHallmarkLabel);

		//hallmarkName combo box
		hallmarkNameComboBox=new JComboBox<String>();
		hallmarkNameComboBox.setPreferredSize(new Dimension(250, 20));
		hallmarkNameComboBox.addItem(HALLMARK_PROLIFERATION_GUI);
		hallmarkNameComboBox.addItem(HALLMARK_GROWTH_REPRESSOR_GUI);
		hallmarkNameComboBox.addItem(HALLMARK_APOPTOSIS_GUI);
		hallmarkNameComboBox.addItem(HALLMARK_REPLICATIVE_IMMORTALITY_GUI);
		hallmarkNameComboBox.addItem(HALLMARK_ANGIOGENESIS_GUI);
		hallmarkNameComboBox.addItem(HALLMARK_METASTASIS_GUI);
		hallmarkNameComboBox.addItem(HALLMARK_METABOLISM_GUI);
		hallmarkNameComboBox.addItem(HALLMARK_IMMUNE_DESTRUCTION_GUI);
		hallmarkNameComboBox.addItem(HALLMARK_GENOME_INSTABILITY_GUI);
		hallmarkNameComboBox.addItem(HALLMARK_TUMOR_PROMOTING_INFLAMMATION_GUI);
		hallmarkNameComboBox.setSelectedIndex(0);

		//go id label
		JPanel editGOIDLabelPanel=new JPanel();
		editGOIDLabelPanel.setPreferredSize(new Dimension(260, 20));
		editGOIDLabelPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		JLabel GOIDLabel=new JLabel("GO ID:");
		editGOIDLabelPanel.add(GOIDLabel);

		//go id textfield
		JPanel editGOIDTextfieldPanel=new JPanel();
		editGOIDTextfieldPanel.setPreferredSize(new Dimension(260, 20));
		editGOIDTextfieldPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		GOIDTextfield=new JTextField();
		GOIDTextfield.setPreferredSize(new Dimension(250, 20));
		editGOIDTextfieldPanel.add(GOIDTextfield);

		//go description label
		JPanel editGODescriptionLabelPanel=new JPanel();
		editGODescriptionLabelPanel.setPreferredSize(new Dimension(260, 20));
		editGODescriptionLabelPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		JLabel GODescriptionLabel=new JLabel("GO Description:");
		editGODescriptionLabelPanel.add(GODescriptionLabel);

		//go name textfield
		JPanel editGONameTextfieldPanel=new JPanel();
		editGONameTextfieldPanel.setPreferredSize(new Dimension(260, 20));
		editGONameTextfieldPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		GONameTextfield=new JTextField();
		GONameTextfield.setPreferredSize(new Dimension(250, 20));
		editGONameTextfieldPanel.add(GONameTextfield);

		//go type label
		JPanel editGOTypeLabelPanel=new JPanel();
		editGOTypeLabelPanel.setPreferredSize(new Dimension(260, 20));
		editGOTypeLabelPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		JLabel GOTypeLabel=new JLabel("GO Type:");
		editGOTypeLabelPanel.add(GOTypeLabel);

		//goType combo box
		goTypeComboBox=new JComboBox<String>();
		goTypeComboBox.setPreferredSize(new Dimension(250, 20));
		goTypeComboBox.addItem(BIOLOGICAL_PROCESS);
		goTypeComboBox.addItem(MOLECULAR_FUNCTION);
		goTypeComboBox.addItem(LOCALIZATION);
		goTypeComboBox.setSelectedIndex(0);

		editPanel.add(editHallmarkLabelPanel);
		editPanel.add(hallmarkNameComboBox);
		editPanel.add(editGOIDLabelPanel);
		editPanel.add(editGOIDTextfieldPanel);
		editPanel.add(editGODescriptionLabelPanel);
		editPanel.add(editGONameTextfieldPanel);
		editPanel.add(editGOTypeLabelPanel);
		editPanel.add(goTypeComboBox);

		JPanel mainPanel=new JPanel();
		mainPanel.setPreferredSize(new Dimension(700, 250));
		mainPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		mainPanel.add(deleteMainPanel);
		mainPanel.add(editPanel);

		JPanel buttonPanel=new JPanel();
		buttonPanel.setPreferredSize(new Dimension(700, 50));
		buttonPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

		JPanel leftButtonPanel=new JPanel();
		leftButtonPanel.setPreferredSize(new Dimension(300, 50));
		leftButtonPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

		deleteHallmarkButton= new JButton(DELETE_HALLMARK);
		deleteHallmarkButton.setSize(90, 20);
		deleteHallmarkButton.setActionCommand(DELETE_HALLMARK);
		deleteHallmarkButton.addActionListener(actionListener);

		deleteGOTermButton= new JButton(DELETE_GO_TERM);
		deleteGOTermButton.setSize(90, 20);
		deleteGOTermButton.setActionCommand(DELETE_GO_TERM);
		deleteGOTermButton.addActionListener(actionListener);

		leftButtonPanel.add(deleteHallmarkButton);
		leftButtonPanel.add(deleteGOTermButton);

		JPanel rightButtonPanel=new JPanel();
		rightButtonPanel.setPreferredSize(new Dimension(330, 50));
		rightButtonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));

		updateButton= new JButton(UPDATE);
		updateButton.setSize(90, 20);
		updateButton.setActionCommand(UPDATE);
		updateButton.addActionListener(actionListener);

		addButton= new JButton(ADD);
		addButton.setSize(90, 20);
		addButton.setActionCommand(ADD);
		addButton.addActionListener(actionListener);

		rightButtonPanel.add(updateButton);
		rightButtonPanel.add(addButton);

		buttonPanel.add(leftButtonPanel);
		buttonPanel.add(rightButtonPanel);

		setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));
		add(mainPanel);
		add(buttonPanel);
	}
}
