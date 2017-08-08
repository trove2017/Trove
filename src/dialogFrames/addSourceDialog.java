package dialogFrames;

//Imports are listed in full to show what's being used could just import javax.swing.* and java.awt.* etc..
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import constants.stringConstant;
import database.postgreSQL;

@SuppressWarnings("serial")
//	Layout: 
//  |  - sourcePanel                                             
//  |    + sourceLabel
//	|	 + sourceTextField
//  |  - URLPanel
//  |    + URLLabel
//  |    + URLTextField
//	|  - buttonPanel
//	|	 + addSourceButton
//	|    + cancelButton 

public class addSourceDialog extends JDialog{
	private JPanel sourcePanel, URLPanel, URLInfoPanel, buttonPanel;
	private JButton addSourceButton, doneButton;
	private JLabel sourceLabel, URLLabel, URLInfoLabel;
	private JTextField sourceTextField, URLTextField;
	
	String source, url;
	postgreSQL postgreDB=new postgreSQL();
	
	private String DONE;
	private stringConstant staticString=new stringConstant();
	
	protected final static String ADD_NEW_SOURCE = "Add new source";
	
	public addSourceDialog(postgreSQL db)
	{
		//initialize constants
		DONE=staticString.getDone();
		
		//initalize variables
		postgreDB=db;
		
		//set JDialog properties
		setTitle("Add new annotation source");
		setSize(417,160);
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
			if (act.getActionCommand() == ADD_NEW_SOURCE)
				addNewSource_actionPerformed();
		}
	};

	private void initializeComponents()
	{
		//sourcePanel
		sourcePanel=new JPanel();
		sourcePanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		sourceLabel=new JLabel("Source");
		sourceTextField=new JTextField();
		sourceTextField.setEditable(true);
		sourceTextField.setColumns(32);
		sourcePanel.add(sourceLabel);
		sourcePanel.add(sourceTextField);
		
		//URLPanel
		URLPanel=new JPanel();
		URLPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		URLLabel=new JLabel("URL      ");
		URLTextField=new JTextField();
		URLTextField.setEditable(true);
		URLTextField.setColumns(32);
		URLPanel.add(URLLabel);
		URLPanel.add(URLTextField);
		
		//URLInfoPanel
		URLInfoPanel=new JPanel();
		URLInfoPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		URLInfoLabel=new JLabel("Note: Please use <v> as placeholder for ID in the URL.");
		URLInfoPanel.add(URLInfoLabel);
		
		//buttonPanel
		buttonPanel=new JPanel();
		buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
		addSourceButton= new JButton();
		addSourceButton.setSize(50, 20);
		addSourceButton.setText(ADD_NEW_SOURCE);
		addSourceButton.setActionCommand(ADD_NEW_SOURCE);
		addSourceButton.addActionListener(actionListener);
		doneButton= new JButton();
		doneButton.setSize(50, 20);
		doneButton.setText(DONE);
		doneButton.setActionCommand(DONE);
		doneButton.addActionListener(actionListener);
		buttonPanel.add(addSourceButton);
		buttonPanel.add(doneButton);
		
		setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));
		add(sourcePanel);
		add(URLPanel);
		add(URLInfoPanel);
		add(buttonPanel);
	}
	
	private void addNewSource_actionPerformed(){
		source=sourceTextField.getText();
		url=URLTextField.getText();
		if((source==null || source.length()==0) && (url==null || url.length()==0))
			JOptionPane.showMessageDialog(new JFrame(), "Please key in the source name and URL address.", "Error", JOptionPane.ERROR_MESSAGE);
		else if(source==null || source.length()==0)
			JOptionPane.showMessageDialog(new JFrame(), "Please key in the source name.", "Error", JOptionPane.ERROR_MESSAGE);
		else if(url==null || url.length()==0)
			JOptionPane.showMessageDialog(new JFrame(), "Please key in the URL address.", "Error", JOptionPane.ERROR_MESSAGE);
		else
		{
			if(url.contains("<v>")==false)
				JOptionPane.showMessageDialog(new JFrame(), "Please insert <v> placeholder appropriately in the URL address.", "Error", JOptionPane.ERROR_MESSAGE);
			else
				postgreDB.update_source(source, url, "1");
		}
	}
	
	private void done_actionPerformed(){
		setVisible(false);
	}
}
