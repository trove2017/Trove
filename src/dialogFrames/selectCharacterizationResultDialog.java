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
//  |    + hallmarkResultPanel                                        
//  |      * hallmarkResultList, hallmarkResultScrollPane                        
//  |  - buttonPanel
//  |    + viewButton
//  |    + cancelButton

public class selectCharacterizationResultDialog extends JDialog{
	private JPanel settingPanel, buttonPanel;
	private JButton viewButton, cancelButton;
	private JList<String> hallmarkResultList;
	private JScrollPane hallmarkResultScrollPane;
	private ArrayList<String> listOfHallmarkResult=new ArrayList<String>();
	private postgreSQL DB;
	
	private String CANCEL;
	private String selectedHallmarkResult;
	private stringConstant staticString=new stringConstant();
	
	protected final static String VIEW = "View Result";
	
	public selectCharacterizationResultDialog(postgreSQL database)
	{
		//initialize constants
		DB=database;
		CANCEL=staticString.getCancel();
		
		//set JDialog properties
		setTitle("Select Hallmark Characterization Result to View");
		setSize(300,350);
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
			if (act.getActionCommand() == VIEW)
				view_actionPerformed();
			if (act.getActionCommand() == CANCEL)
				cancel_actionPerformed();
		}
	};

	private void initializeComponents()
	{
		settingPanel=new JPanel();
		buttonPanel=new JPanel();
		
		//create hallmarkResultPanel
		JLabel hallmarkResultLabel=new JLabel();
		hallmarkResultLabel.setText("Characterized hallmark(s):");
		JPanel hallmarkResultLabelPanel=new JPanel();
		hallmarkResultLabelPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		hallmarkResultLabelPanel.add(hallmarkResultLabel);
		hallmarkResultList=new JList<String>();
		hallmarkResultScrollPane=new JScrollPane();
		hallmarkResultList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		listOfHallmarkResult=DB.getHallmark_characterizedHallmarkList();
		String[] javaArr=listOfHallmarkResult.toArray(new String[listOfHallmarkResult.size()]);
		hallmarkResultList.setListData(javaArr);
		hallmarkResultScrollPane = new JScrollPane(hallmarkResultList);
		hallmarkResultScrollPane.setViewportView(hallmarkResultList);
		hallmarkResultScrollPane.setPreferredSize(new Dimension(200, 250));
		hallmarkResultScrollPane.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		settingPanel.setLayout(new BoxLayout(settingPanel, BoxLayout.Y_AXIS));
		settingPanel.add(hallmarkResultLabelPanel);
		settingPanel.add(hallmarkResultScrollPane);
		//create buttonPanel
		cancelButton= new JButton();
		cancelButton.setSize(50, 20);
		cancelButton.setText(CANCEL);
		cancelButton.setActionCommand(CANCEL);
		cancelButton.addActionListener(actionListener);
		viewButton = new JButton();
		viewButton.setSize(50, 20);
		viewButton.setText(VIEW);
		viewButton.setActionCommand(VIEW);
		viewButton.addActionListener(actionListener);
		buttonPanel= new JPanel();
		buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
		buttonPanel.add(viewButton);
		buttonPanel.add(cancelButton);
		setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));
		add(settingPanel);
		add(buttonPanel);
	}

	private void view_actionPerformed(){
		selectedHallmarkResult=hallmarkResultList.getSelectedValue();
		if(selectedHallmarkResult!=null)
		{
			JDialog viewCharacterizationResult=new viewCharacterizationResultDialog(DB, selectedHallmarkResult);
			viewCharacterizationResult.setVisible(true);
			setVisible(false);
		}
		else
			JOptionPane.showMessageDialog(new JFrame(), "Please select a hallmark result to view", "Error", JOptionPane.ERROR_MESSAGE);
	}

	private void cancel_actionPerformed(){
		setVisible(false);
	}
	
	public String getSelectedHallmarkResult(){
		return selectedHallmarkResult;
	}
}
