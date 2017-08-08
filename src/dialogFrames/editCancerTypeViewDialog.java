package dialogFrames;

//Imports are listed in full to show what's being used could just import javax.swing.* and java.awt.* etc..
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
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
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import javax.swing.text.AbstractDocument;
import javax.swing.text.DocumentFilter;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;

import fileObject.fReaderWriter;

import java.awt.Toolkit;
@SuppressWarnings("serial")

public class editCancerTypeViewDialog extends JDialog{
	private JButton deleteButton, updateButton, addButton;
	private JList<String> cancerTypeViewList;
	private JScrollPane cancerTypeViewScrollPane;
	private ArrayList<ArrayList<String>> cancerTypeViewDetailedList=new ArrayList<ArrayList<String>>();
	private ArrayList<String> cancerTypeViewNameList=new ArrayList<String>();
	private JTextField cancerTypeTextfield=new JTextField();
	private JTextField essentialGeneFileTextfield=new JTextField();
	private JTextField foldChangeFileTextfield=new JTextField();
	private JTextField mutationFreqFileTextfield=new JTextField();

	protected final static String UPDATE = "UPDATE";
	protected final static String DELETE = "DELETE";
	protected final static String ADD = "ADD";

	private String directory, currView, viewFile;
	private String cancerType, essentialGeneFile, foldChangeFile, mutationFreqFile;
	int selectedIndex;
	private String folder;

	private fReaderWriter readerWriter=new fReaderWriter();

	public editCancerTypeViewDialog(String dir, String thisView, String vFile, String sysfolder)
	{
		//initialize variables
		directory=dir;
		currView=thisView;
		viewFile=vFile;
		folder=sysfolder;

		//set JDialog properties
		setTitle("Edit Cancer Type View");
		setSize(680,370);
		setLocationRelativeTo(null);
		setResizable(false);
		setModal(true);

		//configure components in JDialog
		initializeComponents();
	}

	private ActionListener actionListener = new ActionListener() 
	{
		public void actionPerformed(ActionEvent act) {
			if (act.getActionCommand() == UPDATE)
				update_actionPerformed();
			if (act.getActionCommand() == DELETE)
				delete_actionPerformed();
			if (act.getActionCommand() == ADD)
				add_actionPerformed();
		}
	};

	private ArrayList<ArrayList<String>> getCancerTypeView()
	{
		ArrayList<ArrayList<String>> detailedList=new ArrayList<ArrayList<String>>();

		BufferedReader reader, viewReader;
		int totalLineCount=0;
		String line;
		System.out.println("viewFile:"+viewFile);
		try{
			reader=new BufferedReader(new FileReader(viewFile));
			while(reader.readLine()!=null) totalLineCount++;
			reader.close();
			if(totalLineCount>0)
			{
				reader=new BufferedReader(new FileReader(viewFile));
				for (int i=0; i<totalLineCount; i++)
				{
					line=reader.readLine();
					int spaceIndex=line.indexOf(" ");
					String fileType=line.substring(0, spaceIndex);
					String fName=line.substring(spaceIndex).trim();
					ArrayList<String> element=new ArrayList<String>();
					element.add(fileType); //view name
					element.add(fName); //view top layer file

					viewReader=new BufferedReader(new FileReader((directory.concat(folder)).concat(fName)));
					while((line=viewReader.readLine())!=null)
						element.add(line); //line 1= human essential gene file, line 2=fold change file, line 3=mutation freq file
					viewReader.close();
					detailedList.add(element);
				}
				reader.close();
			}
		}catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return detailedList;
	}

	private ArrayList<String> getViewNameList(ArrayList<ArrayList<String>> detailedList)
	{
		ArrayList<String> list=new ArrayList<String>();
		for(int i=0; i<detailedList.size(); i++)
			list.add(detailedList.get(i).get(0));

		return list;
	}

	private void add_actionPerformed() 
	{
		cancerType=cancerTypeTextfield.getText();
		essentialGeneFile=essentialGeneFileTextfield.getText();
		foldChangeFile=foldChangeFileTextfield.getText();
		mutationFreqFile=mutationFreqFileTextfield.getText();

		//check that the new view name is not already in the list
		if(cancerTypeViewNameList.contains(cancerType)==true)
			JOptionPane.showMessageDialog(new JFrame(), "A cancer type with the same name already exists!", "Information", JOptionPane.INFORMATION_MESSAGE);
		else
		{
			//add new file
			ArrayList<String> dataStream=new ArrayList<String>();
			dataStream.add(essentialGeneFile);
			dataStream.add(foldChangeFile);
			dataStream.add(mutationFreqFile);
			readerWriter.writeToFileWithBufferedWriterArray(directory, cancerType+"_fileList.txt", dataStream, true);
			//update view_fileList.txt
			ArrayList<String> fileContentArray=readerWriter.readFromFileWithBufferedReaderArray(viewFile);
			String rowToUpdate=cancerType+" "+cancerType+"_fileList.txt";
			fileContentArray.add(rowToUpdate);
			readerWriter.writeToFileWithBufferedWriterArray(directory, "view_fileList.txt", fileContentArray, true);
			//update dialog
			cancerTypeViewDetailedList=getCancerTypeView();
			cancerTypeViewNameList=getViewNameList(cancerTypeViewDetailedList);
			String[] formattedViewList=new String[cancerTypeViewNameList.size()];
			formattedViewList = cancerTypeViewNameList.toArray(formattedViewList);
			cancerTypeViewList.setListData(formattedViewList);
			cancerTypeViewScrollPane.setViewportView(cancerTypeViewList);
		}
	}

	private void update_actionPerformed() 
	{
		cancerType=cancerTypeTextfield.getText();
		essentialGeneFile=essentialGeneFileTextfield.getText();
		foldChangeFile=foldChangeFileTextfield.getText();
		mutationFreqFile=mutationFreqFileTextfield.getText();
		boolean correctFormat=true;

		selectedIndex = cancerTypeViewList.getSelectedIndex();
		if(selectedIndex!=-1)
		{
			//check file format
			int dotIndex=essentialGeneFile.indexOf(".");
			String essentialGeneFileFormat=essentialGeneFile.substring(dotIndex);
			dotIndex=foldChangeFile.indexOf(".");
			String foldChangeFileFormat=foldChangeFile.substring(dotIndex);
			dotIndex=mutationFreqFile.indexOf(".");
			String mutationFreqFileFormat=mutationFreqFile.substring(dotIndex);
			if(essentialGeneFileFormat.compareTo(".csv")==0 && foldChangeFileFormat.compareTo(".csv")==0 && mutationFreqFileFormat.compareTo(".csv")==0)
				correctFormat=true;
			else 
				correctFormat=false;

			if(correctFormat==false)
				JOptionPane.showMessageDialog(new JFrame(), "Ensure that the file format is in .csv!", "Information", JOptionPane.INFORMATION_MESSAGE);
			else
			{
				//get view to update - original view name
				String originalCancerTypeFileName=cancerTypeViewList.getSelectedValue();
				//delete old file and create new file with new values
				//update view_fileList.txt
				try{//delete old file
					String fileToRemove_filename = directory.concat(originalCancerTypeFileName+"_fileList.txt");
					//Delete if tempFile exists
					File fileToRemove = new File(fileToRemove_filename);
					if (fileToRemove.exists()){
						fileToRemove.delete();
					}   
				}catch(Exception e){
					// if any error occurs
					e.printStackTrace();
				}
				//add new file
				ArrayList<String> dataStream=new ArrayList<String>();
				dataStream.add(essentialGeneFile);
				dataStream.add(foldChangeFile);
				dataStream.add(mutationFreqFile);
				readerWriter.writeToFileWithBufferedWriterArray(directory, cancerType+"_fileList.txt", dataStream, true);
				//update view_fileList.txt
				ArrayList<String> fileContentArray=readerWriter.readFromFileWithBufferedReaderArray(viewFile);
				String rowToUpdate=cancerType+" "+cancerType+"_fileList.txt";
				fileContentArray.set(selectedIndex, rowToUpdate);
				readerWriter.writeToFileWithBufferedWriterArray(directory, "view_fileList.txt", fileContentArray, true);
				//update dialog
				cancerTypeViewDetailedList=getCancerTypeView();
				cancerTypeViewNameList=getViewNameList(cancerTypeViewDetailedList);
				String[] formattedViewList=new String[cancerTypeViewNameList.size()];
				formattedViewList = cancerTypeViewNameList.toArray(formattedViewList);
				cancerTypeViewList.setListData(formattedViewList);
				cancerTypeViewScrollPane.setViewportView(cancerTypeViewList);
			}
		}
	}

	private void delete_actionPerformed()
	{
		selectedIndex = cancerTypeViewList.getSelectedIndex();
		if(selectedIndex!=-1)
		{
			//get view to delete
			cancerType=cancerTypeTextfield.getText();
			//remove intermediate view file
			try{
				String fileToRemove_filename = directory.concat(cancerType+"_fileList.txt");
				//Delete if tempFile exists
				File fileToRemove = new File(fileToRemove_filename);
				if (fileToRemove.exists()){
					fileToRemove.delete();
				}   
			}catch(Exception e){
				// if any error occurs
				e.printStackTrace();
			}
			//update view_fileList.txt
			ArrayList<String> fileContentArray=readerWriter.readFromFileWithBufferedReaderArray(viewFile);
			fileContentArray.remove(selectedIndex);
			readerWriter.writeToFileWithBufferedWriterArray(directory, "view_fileList.txt", fileContentArray, true);
			//update dialog
			cancerTypeTextfield.setText("");
			essentialGeneFileTextfield.setText("");
			foldChangeFileTextfield.setText("");
			mutationFreqFileTextfield.setText("");
			//remove from list
			cancerTypeViewDetailedList.remove(selectedIndex);
			cancerTypeViewNameList.remove(selectedIndex);
			String[] formattedViewList=new String[cancerTypeViewNameList.size()];
			formattedViewList = cancerTypeViewNameList.toArray(formattedViewList);
			cancerTypeViewList.setListData(formattedViewList);
			cancerTypeViewScrollPane.setViewportView(cancerTypeViewList);
		}
	}

	private void cancerTypeViewList_valueChanged(ListSelectionEvent e) 
	{
		if (!e.getValueIsAdjusting()) 
		{
			if(cancerTypeViewList.getSelectedValue()!=null)
			{
				System.out.println("selectedValue="+cancerTypeViewList.getSelectedValue());
				cancerTypeViewDetailedList=getCancerTypeView();
				for(int i=0; i<cancerTypeViewDetailedList.size(); i++)
					System.out.println(cancerTypeViewDetailedList.get(i).toString());
				cancerTypeViewNameList=getViewNameList(cancerTypeViewDetailedList);
				System.out.println(cancerTypeViewNameList.toString());
				selectedIndex = cancerTypeViewNameList.indexOf(cancerTypeViewList.getSelectedValue());
				System.out.println("selectedIndex="+selectedIndex);
				
				System.out.println(cancerTypeViewDetailedList.get(selectedIndex).get(0));
				if(selectedIndex!=-1)
				{
					//update details of newly selected view
					cancerTypeTextfield.setText(cancerTypeViewDetailedList.get(selectedIndex).get(0));
					essentialGeneFileTextfield.setText(cancerTypeViewDetailedList.get(selectedIndex).get(2));
					foldChangeFileTextfield.setText(cancerTypeViewDetailedList.get(selectedIndex).get(3));
					mutationFreqFileTextfield.setText(cancerTypeViewDetailedList.get(selectedIndex).get(4));
				}
			}
		}
	}

	private void initializeComponents()
	{
		JPanel deleteUpdatePanel=new JPanel();
		deleteUpdatePanel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
		deleteUpdatePanel.setPreferredSize(new Dimension(700, 250));
		deleteUpdatePanel.setLayout(new BoxLayout(deleteUpdatePanel, BoxLayout.Y_AXIS));

		JPanel deleteUpdateMainPanel=new JPanel();
		deleteUpdateMainPanel.setPreferredSize(new Dimension(700, 250));
		deleteUpdateMainPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

		JPanel existingCancerTypePanel=new JPanel();
		existingCancerTypePanel.setPreferredSize(new Dimension(250, 250));
		existingCancerTypePanel.setLayout(new BoxLayout(existingCancerTypePanel, BoxLayout.Y_AXIS));
		JPanel existingCancerTypeLabelPanel=new JPanel();
		existingCancerTypeLabelPanel.setPreferredSize(new Dimension(250, 50));
		existingCancerTypeLabelPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		JLabel existingCancerTypeLabel=new JLabel("Existing Cancer Type (View):");
		existingCancerTypeLabelPanel.add(existingCancerTypeLabel);
		//cancerTypeList
		cancerTypeViewDetailedList=getCancerTypeView();
		cancerTypeViewNameList=getViewNameList(cancerTypeViewDetailedList);
		String[] formattedViewList=new String[cancerTypeViewNameList.size()];
		formattedViewList = cancerTypeViewNameList.toArray(formattedViewList);
		cancerTypeViewList=new JList<String>();
		cancerTypeViewList.setListData(formattedViewList);
		cancerTypeViewScrollPane = new JScrollPane(cancerTypeViewList);
		cancerTypeViewList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		cancerTypeViewList.addListSelectionListener(new ListSelectionListener() 
		{
			public void valueChanged(ListSelectionEvent e){ cancerTypeViewList_valueChanged(e);}
		});
		cancerTypeViewScrollPane.setViewportView(cancerTypeViewList);
		cancerTypeViewScrollPane.setPreferredSize(new Dimension(250, 400));
		cancerTypeViewScrollPane.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		selectedIndex=cancerTypeViewNameList.indexOf(currView);
		if(selectedIndex!=-1)
			cancerTypeViewList.setSelectedIndex(selectedIndex);
		existingCancerTypePanel.add(existingCancerTypeLabelPanel);
		existingCancerTypePanel.add(cancerTypeViewScrollPane);

		JPanel existingCancerTypeDetailPanel=new JPanel();
		existingCancerTypeDetailPanel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
		existingCancerTypeDetailPanel.setPreferredSize(new Dimension(400, 250));
		existingCancerTypeDetailPanel.setLayout(new BoxLayout(existingCancerTypeDetailPanel, BoxLayout.Y_AXIS));
		JPanel cancerTypeDetailPanel=new JPanel();
		cancerTypeDetailPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		JLabel cancerTypeDetailLabel=new JLabel("Cancer Type (View) Details");
		cancerTypeDetailPanel.add(cancerTypeDetailLabel);
		//cancerType
		JPanel currCancerTypePanel=new JPanel();
		currCancerTypePanel.setLayout(new BoxLayout(currCancerTypePanel, BoxLayout.Y_AXIS));
		currCancerTypePanel.setPreferredSize(new Dimension(400, 50));
		JPanel currCancerTypeLabelPanel=new JPanel();
		currCancerTypeLabelPanel.setPreferredSize(new Dimension(400, 50));
		currCancerTypeLabelPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		JLabel currCancerTypeLabel=new JLabel("Cancer type (view) [Valid characters=A-Z,a-z,0-9,_]:");
		currCancerTypeLabelPanel.add(currCancerTypeLabel);
		cancerTypeTextfield=new JTextField();
		cancerTypeTextfield.setColumns(34);
		if(selectedIndex!=-1)
			cancerTypeTextfield.setText(cancerTypeViewDetailedList.get(selectedIndex).get(0));
		AbstractDocument document = (AbstractDocument) cancerTypeTextfield.getDocument();
		final int maxCharacters = 200;
		document.setDocumentFilter(new DocumentFilter() {
			public void replace(FilterBypass fb, int offs, int length,
					String str, AttributeSet a) throws BadLocationException {

				String text = fb.getDocument().getText(0,
						fb.getDocument().getLength());
				text += str;
				if ((fb.getDocument().getLength() + str.length() - length) <= maxCharacters
						&& text.matches("^[a-zA-Z0-9_]*$")) {
					super.replace(fb, offs, length, str, a);
				} else {
					Toolkit.getDefaultToolkit().beep();
				}
			}

			public void insertString(FilterBypass fb, int offs, String str,
					AttributeSet a) throws BadLocationException {

				String text = fb.getDocument().getText(0,
						fb.getDocument().getLength());
				text += str;
				if ((fb.getDocument().getLength() + str.length()) <= maxCharacters
						&& text.matches("^[a-zA-Z0-9_]*$")) {
					super.insertString(fb, offs, str, a);
				} else {
					Toolkit.getDefaultToolkit().beep();
				}
			}
		});		
		JPanel cancerTypeTextfieldPanel=new JPanel();
		cancerTypeTextfieldPanel.setPreferredSize(new Dimension(300, 50));
		cancerTypeTextfieldPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		cancerTypeTextfieldPanel.add(cancerTypeTextfield);
		currCancerTypePanel.add(currCancerTypeLabel);		
		currCancerTypePanel.add(cancerTypeTextfieldPanel);
		//essentialGene
		JPanel currEssentialGenePanel=new JPanel();
		currEssentialGenePanel.setPreferredSize(new Dimension(400, 50));
		currEssentialGenePanel.setLayout(new BoxLayout(currEssentialGenePanel, BoxLayout.Y_AXIS));
		JPanel currEssentialGeneLabelPanel=new JPanel();
		currEssentialGeneLabelPanel.setPreferredSize(new Dimension(400, 50));
		currEssentialGeneLabelPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		JLabel currEssentialGeneLabel=new JLabel("Essential gene file (.csv):");
		currEssentialGeneLabelPanel.add(currEssentialGeneLabel);
		essentialGeneFileTextfield=new JTextField(maxCharacters);
		essentialGeneFileTextfield.setColumns(34);
		if(selectedIndex!=-1)
			essentialGeneFileTextfield.setText(cancerTypeViewDetailedList.get(selectedIndex).get(2));
		AbstractDocument essentialGeneDocument = (AbstractDocument) essentialGeneFileTextfield.getDocument();
		essentialGeneDocument.setDocumentFilter(new DocumentFilter() {
			public void replace(FilterBypass fb, int offs, int length,
					String str, AttributeSet a) throws BadLocationException {

				String text = fb.getDocument().getText(0,
						fb.getDocument().getLength());
				text += str;
				if ((fb.getDocument().getLength() + str.length() - length) <= maxCharacters
						&& text.matches("^[a-zA-Z0-9_.]*$")) {
					super.replace(fb, offs, length, str, a);
				} else {
					Toolkit.getDefaultToolkit().beep();
				}
			}

			public void insertString(FilterBypass fb, int offs, String str,
					AttributeSet a) throws BadLocationException {

				String text = fb.getDocument().getText(0,
						fb.getDocument().getLength());
				text += str;
				if ((fb.getDocument().getLength() + str.length()) <= maxCharacters
						&& text.matches("^[a-zA-Z0-9_.]*$")) {
					super.insertString(fb, offs, str, a);
				} else {
					Toolkit.getDefaultToolkit().beep();
				}
			}
		});		
		JPanel essentialGeneFileTextfieldPanel=new JPanel();
		essentialGeneFileTextfieldPanel.setPreferredSize(new Dimension(300, 50));
		essentialGeneFileTextfieldPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		essentialGeneFileTextfieldPanel.add(essentialGeneFileTextfield);
		currEssentialGenePanel.add(currEssentialGeneLabelPanel);		
		currEssentialGenePanel.add(essentialGeneFileTextfieldPanel);
		//foldChange
		JPanel currFoldChangePanel=new JPanel();
		currFoldChangePanel.setPreferredSize(new Dimension(400, 50));
		currFoldChangePanel.setLayout(new BoxLayout(currFoldChangePanel, BoxLayout.Y_AXIS));
		JPanel currFoldChangeLabelPanel=new JPanel();
		currFoldChangeLabelPanel.setPreferredSize(new Dimension(400, 50));
		currFoldChangeLabelPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		JLabel currFoldChangeLabel=new JLabel("Fold change file (.csv):");
		currFoldChangeLabelPanel.add(currFoldChangeLabel);
		foldChangeFileTextfield=new JTextField(maxCharacters);
		foldChangeFileTextfield.setColumns(34);
		if(selectedIndex!=-1)
			foldChangeFileTextfield.setText(cancerTypeViewDetailedList.get(selectedIndex).get(3));
		AbstractDocument foldChangeDocument = (AbstractDocument) foldChangeFileTextfield.getDocument();
		foldChangeDocument.setDocumentFilter(new DocumentFilter() {
			public void replace(FilterBypass fb, int offs, int length,
					String str, AttributeSet a) throws BadLocationException {

				String text = fb.getDocument().getText(0,
						fb.getDocument().getLength());
				text += str;
				if ((fb.getDocument().getLength() + str.length() - length) <= maxCharacters
						&& text.matches("^[a-zA-Z0-9_.]*$")) {
					super.replace(fb, offs, length, str, a);
				} else {
					Toolkit.getDefaultToolkit().beep();
				}
			}

			public void insertString(FilterBypass fb, int offs, String str,
					AttributeSet a) throws BadLocationException {

				String text = fb.getDocument().getText(0,
						fb.getDocument().getLength());
				text += str;
				if ((fb.getDocument().getLength() + str.length()) <= maxCharacters
						&& text.matches("^[a-zA-Z0-9_.]*$")) {
					super.insertString(fb, offs, str, a);
				} else {
					Toolkit.getDefaultToolkit().beep();
				}
			}
		});		
		JPanel foldChangeFileTextfieldPanel=new JPanel();
		foldChangeFileTextfieldPanel.setPreferredSize(new Dimension(300, 50));
		foldChangeFileTextfieldPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		foldChangeFileTextfieldPanel.add(foldChangeFileTextfield);
		currFoldChangePanel.add(currFoldChangeLabelPanel);		
		currFoldChangePanel.add(foldChangeFileTextfieldPanel);
		//mutationFreq
		JPanel currMutationFreqPanel=new JPanel();
		currMutationFreqPanel.setPreferredSize(new Dimension(400, 50));
		currMutationFreqPanel.setLayout(new BoxLayout(currMutationFreqPanel, BoxLayout.Y_AXIS));
		JPanel currMutationFreqLabelPanel=new JPanel();
		currMutationFreqLabelPanel.setPreferredSize(new Dimension(400, 50));
		currMutationFreqLabelPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		JLabel currMutationFreqLabel=new JLabel("Mutation frequency file (.csv):");
		currMutationFreqLabelPanel.add(currMutationFreqLabel);
		mutationFreqFileTextfield=new JTextField(maxCharacters);
		mutationFreqFileTextfield.setColumns(34);
		if(selectedIndex!=-1)
			mutationFreqFileTextfield.setText(cancerTypeViewDetailedList.get(selectedIndex).get(4));
		AbstractDocument mutationFreqDocument = (AbstractDocument) mutationFreqFileTextfield.getDocument();
		mutationFreqDocument.setDocumentFilter(new DocumentFilter() {
			public void replace(FilterBypass fb, int offs, int length,
					String str, AttributeSet a) throws BadLocationException {

				String text = fb.getDocument().getText(0,
						fb.getDocument().getLength());
				text += str;
				if ((fb.getDocument().getLength() + str.length() - length) <= maxCharacters
						&& text.matches("^[a-zA-Z0-9_.]*$")) {
					super.replace(fb, offs, length, str, a);
				} else {
					Toolkit.getDefaultToolkit().beep();
				}
			}

			public void insertString(FilterBypass fb, int offs, String str,
					AttributeSet a) throws BadLocationException {

				String text = fb.getDocument().getText(0,
						fb.getDocument().getLength());
				text += str;
				if ((fb.getDocument().getLength() + str.length()) <= maxCharacters
						&& text.matches("^[a-zA-Z0-9_.]*$")) {
					super.insertString(fb, offs, str, a);
				} else {
					Toolkit.getDefaultToolkit().beep();
				}
			}
		});
		JPanel mutationFreqFileTextfieldPanel=new JPanel();
		mutationFreqFileTextfieldPanel.setPreferredSize(new Dimension(300, 50));
		mutationFreqFileTextfieldPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		mutationFreqFileTextfieldPanel.add(mutationFreqFileTextfield);
		currMutationFreqPanel.add(currMutationFreqLabelPanel);		
		currMutationFreqPanel.add(mutationFreqFileTextfieldPanel);

		//existingCancerTypeDetailPanel.add(cancerTypeDetailPanel);
		existingCancerTypeDetailPanel.add(currCancerTypePanel);
		existingCancerTypeDetailPanel.add(currEssentialGenePanel);
		existingCancerTypeDetailPanel.add(currFoldChangePanel);
		existingCancerTypeDetailPanel.add(currMutationFreqPanel);

		deleteUpdateMainPanel.add(existingCancerTypePanel);
		deleteUpdateMainPanel.add(existingCancerTypeDetailPanel);

		JPanel buttonPanel=new JPanel();
		buttonPanel.setPreferredSize(new Dimension(700, 50));
		buttonPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		JPanel deleteButtonPanel=new JPanel();
		deleteButtonPanel.setPreferredSize(new Dimension(250, 50));
		deleteButtonPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		deleteButton= new JButton(DELETE);
		deleteButton.setSize(90, 20);
		deleteButton.setActionCommand(DELETE);
		deleteButton.addActionListener(actionListener);
		deleteButtonPanel.add(deleteButton);
		JPanel addUpdateButtonPanel=new JPanel();
		addUpdateButtonPanel.setPreferredSize(new Dimension(400, 50));
		addUpdateButtonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
		addButton= new JButton(ADD);
		addButton.setSize(90, 20);
		addButton.setActionCommand(ADD);
		addButton.addActionListener(actionListener);
		updateButton= new JButton(UPDATE);
		updateButton.setSize(90, 20);
		updateButton.setActionCommand(UPDATE);
		updateButton.addActionListener(actionListener);
		addUpdateButtonPanel.add(addButton);
		addUpdateButtonPanel.add(updateButton);
		buttonPanel.add(deleteButtonPanel);
		buttonPanel.add(addUpdateButtonPanel);

		JPanel informationPanel=new JPanel();
		informationPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		JLabel informationLabel=new JLabel("Please save all files for cancer type to this directory: "+directory);
		informationPanel.add(informationLabel);

		deleteUpdatePanel.add(deleteUpdateMainPanel);
		deleteUpdatePanel.add(informationPanel);
		deleteUpdatePanel.add(buttonPanel);

		setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));
		add(deleteUpdatePanel);
	}
}
