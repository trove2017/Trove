package dialogFrames;

//Imports are listed in full to show what's being used could just import javax.swing.* and java.awt.* etc..
import graph.Gephi_graph;
import graph.JUNG_graph;
import graph.edge;
import hallmark.allHallmark;

import java.awt.BorderLayout;
//import java.awt.FileDialog;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.text.DecimalFormat;
//import java.io.File;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import wrapper.java_GUIWrapper;

import constants.stringConstant;
import database.postgreSQL;

@SuppressWarnings("serial")
//	Layout: 
//  |  - settingPanel                                             
//  |    + comboPanel                                       
//  |      * networkLabel                         
//  |      * networkComboBox                        
//  |    + InformationPanel                 
//  |      * informationLabel               
//  |  - buttonPanel
//  |    + visualizeButton
//  |    + cancelButton

public class visualizeNetworkDialog extends JDialog{
	private JPanel settingPanel, networkComboPanel, viewComboPanel, extensionComboPanel, informationPanel, buttonPanel;
	private JPanel layoutComboPanel; 
	private JLabel networkLabel, informationLabel, viewLabel, extensionLabel, layoutLabel;
	private JButton visualizeButton, cancelButton;
	private JComboBox<String> networkComboBox, viewComboBox, extensionComboBox;
	private JComboBox<String> layoutComboBox;
	private JCheckBox computeCheckBox;
	private ArrayList<String> listOfNetwork=new ArrayList<String>(), listOfView=new ArrayList<String>(), listOfExtension=new ArrayList<String>(), listOfVersion=new ArrayList<String>();
	private ArrayList<String> listOfLayout=new ArrayList<String>();
	private String selectedNetwork, selectedView, selectedExtension, selectedLayout;
	private boolean computeNetworkTopologicalFeatures=false;
	private String  CANCEL;
	private stringConstant staticString=new stringConstant();
	protected final static String VISUALIZE = "Visualize network";
	
	private String VIEW_HALLMARK = "View Hallmark";
	
	public visualizeNetworkDialog(ArrayList<String> networkList, ArrayList<String> versionList, ArrayList<String> viewList, ArrayList<String> extensionList, ArrayList<String> layoutList)
	{
		//initialize constants
		CANCEL=staticString.getCancel();
		listOfNetwork=networkList;
		listOfVersion=versionList;
		listOfView=viewList;
		listOfExtension=extensionList;
		listOfLayout=layoutList;
		
		selectedNetwork=null;
		selectedView=null;
		selectedExtension=null;
		selectedLayout=null;
		
		//set JDialog properties
		setTitle("Visualize Preloaded Network");
		setSize(400,350);
		setLocationRelativeTo(null);
		setLayout(new BorderLayout());
		setModal(true);

		//configure components in JDialog
		initializeComponents();
	}

	private ActionListener actionListener = new ActionListener() 
	{
		public void actionPerformed(ActionEvent act) {
			if (act.getActionCommand() == VISUALIZE)
				visualize_actionPerformed();
			if (act.getActionCommand() == CANCEL)
				cancel_actionPerformed();
		}
	};

	private ItemListener networkItemChangeListener = new ItemListener()
	{
	   public void itemStateChanged(ItemEvent event) {
	       if (event.getStateChange() == ItemEvent.SELECTED) {
	          Object item = event.getItem();
	          selectedNetwork=item.toString();
	          int index=listOfNetwork.indexOf(selectedNetwork);
	          informationLabel=new JLabel("Details: "+listOfVersion.get(index));
	          informationPanel.repaint();
	       }
	    }       
	};
	
	private ItemListener viewItemChangeListener = new ItemListener()
	{
	   public void itemStateChanged(ItemEvent event) {
	       if (event.getStateChange() == ItemEvent.SELECTED) {
	          Object item = event.getItem();
	          selectedView=item.toString();
	          
	          if(selectedView.compareTo("Entire view")==0)
	          {
	        	  ArrayList<String> extensionList=new ArrayList<String>();
	        	  extensionList.add("None");
	        	  String[] extensionString =extensionList.toArray(new String[extensionList.size()]);
	        	  extensionComboBox.setModel((ComboBoxModel<String>) new DefaultComboBoxModel(extensionString));
	          }
	          else
	          {
	        	  String[] extensionString =listOfExtension.toArray(new String[listOfExtension.size()]);
	      		  extensionComboBox.setModel((ComboBoxModel<String>) new DefaultComboBoxModel(extensionString));
	          }
	          System.out.println("modified view");
	          //settingPanel.repaint();
	       }
	    }       
	};
	
	private ItemListener extensionItemChangeListener = new ItemListener()
	{
	   public void itemStateChanged(ItemEvent event) {
	       if (event.getStateChange() == ItemEvent.SELECTED) {
	          Object item = event.getItem();
	          selectedExtension=item.toString();
	       }
	    }       
	};
	
	private ItemListener layoutItemChangeListener = new ItemListener()
	{
	   public void itemStateChanged(ItemEvent event) {
	       if (event.getStateChange() == ItemEvent.SELECTED) {
	          Object item = event.getItem();
	          selectedLayout=item.toString();
	       }
	    }       
	};
	
	private void initializeComponents()
	{
		settingPanel=new JPanel();
		buttonPanel=new JPanel();
		
		//create comboPanel
		networkComboPanel=new JPanel();
		networkLabel=new JLabel("Select preloaded network:");
		String[] networkString =listOfNetwork.toArray(new String[listOfNetwork.size()]);
		networkComboBox=new JComboBox<String>(networkString);
		networkComboBox.setSelectedIndex(0);
		networkComboBox.addItemListener(networkItemChangeListener);
		networkComboPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		networkComboPanel.add(networkLabel);
		networkComboPanel.add(networkComboBox);
		//create subNetwork view (e.g., breast cancer network)
		viewComboPanel=new JPanel();
		viewLabel=new JLabel("Network view:");
		String[] viewString =listOfView.toArray(new String[listOfView.size()]);
		viewComboBox=new JComboBox<String>(viewString);
		viewComboBox.setSelectedIndex(0);
		viewComboBox.addItemListener(viewItemChangeListener);
		viewComboPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		viewComboPanel.add(viewLabel);
		viewComboPanel.add(viewComboBox);
		//extending subNetwork view (e.g., breast cancer network with one-hop)
		extensionComboPanel=new JPanel();
		extensionLabel=new JLabel("View extension:");
		if(viewComboBox.getSelectedItem().toString().compareTo("Entire view")==0)
        {
			ArrayList<String> extensionList=new ArrayList<String>();
       	  	extensionList.add("None");
       	  	String[] extensionString =extensionList.toArray(new String[extensionList.size()]);
       	  	extensionComboBox=new JComboBox<String>(extensionString);
        }
        else
        {
        	String[] extensionString =listOfExtension.toArray(new String[listOfExtension.size()]);
     		extensionComboBox=new JComboBox<String>(extensionString);
        }
		extensionComboBox.setSelectedIndex(0);
		extensionComboBox.addItemListener(extensionItemChangeListener);
		extensionComboPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		extensionComboPanel.add(extensionLabel);
		extensionComboPanel.add(extensionComboBox);
		//create layout list (e.g., yifanhu)
		layoutComboPanel=new JPanel();
		layoutLabel=new JLabel("Network layout:");
		String[] layoutString =listOfLayout.toArray(new String[listOfLayout.size()]);
		layoutComboBox=new JComboBox<String>(layoutString);
		layoutComboBox.setSelectedIndex(0);
		layoutComboBox.addItemListener(layoutItemChangeListener);
		layoutComboPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		layoutComboPanel.add(layoutLabel);
		layoutComboPanel.add(layoutComboBox);
		selectedLayout=layoutComboBox.getSelectedItem().toString();
				
		//create informationPanel
		informationPanel=new JPanel();
		informationLabel=new JLabel("Details: "+listOfVersion.get(0));
		informationPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		informationPanel.add(informationLabel);
		//compute features
		JLabel computeLabel=new JLabel("Compute all topological features for this network");
		computeCheckBox=new JCheckBox();
		computeCheckBox.setSelected(false);
		JPanel computePanel=new JPanel();
		computePanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		computePanel.add(computeCheckBox);
		computePanel.add(computeLabel);
		settingPanel.setLayout(new BoxLayout(settingPanel, BoxLayout.Y_AXIS));
		settingPanel.add(networkComboPanel);
		settingPanel.add(viewComboPanel);
		settingPanel.add(extensionComboPanel);
		settingPanel.add(layoutComboPanel);
		settingPanel.add(informationPanel);
		settingPanel.add(computePanel);
		//create buttonPanel
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
		buttonPanel.add(visualizeButton);
		buttonPanel.add(cancelButton);

		setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));
		add(settingPanel);
		add(buttonPanel);
	}

	public String getSelectedNetwork(){
		return selectedNetwork;
	}
	

	public String getSelectedView(){
		return selectedView;
	}
	
	public String getSelectedExtension(){
		return selectedExtension;
	}
	
	public String getSelectedLayout(){
		return selectedLayout;
	}
	
	public boolean getComputeNetworkTopologicalFeatures(){
		return computeNetworkTopologicalFeatures;
	}
	
	private void visualize_actionPerformed(){
		computeNetworkTopologicalFeatures=computeCheckBox.isSelected();
		selectedNetwork=networkComboBox.getSelectedItem().toString();
		selectedView=viewComboBox.getSelectedItem().toString();
		selectedExtension=extensionComboBox.getSelectedItem().toString();
		setVisible(false);
	}

	private void cancel_actionPerformed(){
		setVisible(false);
	}
}
