package dialogFrames;

//Imports are listed in full to show what's being used could just import javax.swing.* and java.awt.* etc..
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

import database.postgreSQL;

@SuppressWarnings("serial")
//	Layout: 
//  |  - settingPanel                                             
//  |    + hallmarkResultPanel                                        
//  |      * hallmarkResultList, hallmarkResultScrollPane                        
//  |  - buttonPanel
//  |    + viewButton
//  |    + cancelButton

public class viewCharacterizationResultDialog extends JDialog{
	private JPanel settingPanel, buttonPanel;
	private JButton doneButton;
	private JLabel SVMType=new JLabel(), kernelType=new JLabel(), NrClass=new JLabel(), totalSV=new JLabel();
	private JLabel rho=new JLabel(), probA=new JLabel();
	private JList<String> predictiveFeatureList;
	private JScrollPane predictiveFeatureScrollPane, modelScrollPane;
	private ArrayList<String> listOfPredictiveFeature=new ArrayList<String>();
	private postgreSQL DB;
	
	private String selectedHallmarkResult;
	protected final static String DONE = "Done";
	
	public viewCharacterizationResultDialog(postgreSQL database, String result)
	{
		//initialize constants
		DB=database;
		selectedHallmarkResult=result;
		//set JDialog properties
		setTitle("View Hallmark Characterization Result");
		setSize(900,400);
		setLocationRelativeTo(null);
		setLayout(new BorderLayout());
		setResizable(true);
		setModal(true);
		//configure components in JDialog
		initializeComponents();
	}

	private ActionListener actionListener = new ActionListener() 
	{
		public void actionPerformed(ActionEvent act) {
			if (act.getActionCommand() == DONE)
				done_actionPerformed();
		}
	};

	private void initializeComponents()
	{
		settingPanel=new JPanel();
		buttonPanel=new JPanel();
		//create predictiveFeaturePanel
		JLabel predictiveFeatureLabel=new JLabel("Predictive feature(s):");
		JPanel predictiveFeatureLabelPanel=new JPanel();
		predictiveFeatureLabelPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		predictiveFeatureLabelPanel.add(predictiveFeatureLabel);
		predictiveFeatureList=new JList<String>();
		predictiveFeatureScrollPane=new JScrollPane();
		predictiveFeatureList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		listOfPredictiveFeature=DB.getHallmarkCharacterization_predictiveFeature(selectedHallmarkResult);
		String[] javaArr=listOfPredictiveFeature.toArray(new String[listOfPredictiveFeature.size()]);
		predictiveFeatureList.setListData(javaArr);
		predictiveFeatureScrollPane = new JScrollPane(predictiveFeatureList);
		predictiveFeatureScrollPane.setViewportView(predictiveFeatureList);
		predictiveFeatureScrollPane.setPreferredSize(new Dimension(200, 250));
		predictiveFeatureScrollPane.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		JPanel predictiveFeaturePanel=new JPanel();
		predictiveFeaturePanel.setLayout(new BoxLayout(predictiveFeaturePanel, BoxLayout.Y_AXIS));
		predictiveFeaturePanel.add(predictiveFeatureLabelPanel);
		predictiveFeaturePanel.add(predictiveFeatureScrollPane);
		//create modelPanel
		JLabel modelLabel=new JLabel("Model details:");
		JPanel modelLabelPanel=new JPanel();
		modelLabelPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		modelLabelPanel.add(modelLabel);
		JLabel weightLabel=new JLabel("Weight:"+DB.getHallmarkCharacterization_weight(selectedHallmarkResult));
		JPanel weightPanel=new JPanel();
		weightPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		weightPanel.add(weightLabel);
		JLabel CLabel=new JLabel("C:"+DB.getHallmarkCharacterization_C(selectedHallmarkResult));
		JPanel CPanel=new JPanel();
		CPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		CPanel.add(CLabel);
		JLabel accuracyLabel=new JLabel("Average validation accuracy:"+DB.getHallmarkCharacterization_accuracy(selectedHallmarkResult));
		JPanel accuracyPanel=new JPanel();
		accuracyPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		accuracyPanel.add(accuracyLabel);
		JLabel filenameLabel=new JLabel("Model filename:"+DB.getHallmarkCharacterization_modelFileName(selectedHallmarkResult));
		JPanel filenamePanel=new JPanel();
		filenamePanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		filenamePanel.add(filenameLabel);
		JPanel modelPanel=new JPanel();
		modelPanel.setLayout(new BoxLayout(modelPanel, BoxLayout.Y_AXIS));
		updateModelDisplay(DB.getHallmarkCharacterization_modelFileName(selectedHallmarkResult));
		JPanel SVMTypePanel=new JPanel();
		SVMTypePanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		SVMTypePanel.add(SVMType);
		JPanel kernelTypePanel=new JPanel();
		kernelTypePanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		kernelTypePanel.add(kernelType);
		JPanel NrClassPanel=new JPanel();
		NrClassPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		NrClassPanel.add(NrClass);
		JPanel totalSVPanel=new JPanel();
		totalSVPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		totalSVPanel.add(totalSV);
		JPanel rhoPanel=new JPanel();
		rhoPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		rhoPanel.add(rho);
		JPanel probAPanel=new JPanel();
		probAPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		probAPanel.add(probA);
		//modelPanel.add(modelLabelPanel);
		modelPanel.add(weightPanel);
		modelPanel.add(CPanel);
		modelPanel.add(accuracyPanel);
		modelPanel.add(filenamePanel);
		modelPanel.add(SVMTypePanel);
		modelPanel.add(kernelTypePanel);
		modelPanel.add(NrClassPanel);
		modelPanel.add(totalSVPanel);
		modelPanel.add(rhoPanel);
		modelPanel.add(probAPanel);
		modelScrollPane=new JScrollPane(modelPanel);
		modelScrollPane.setViewportView(modelPanel);
		modelScrollPane.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		JPanel overallModelPanel=new JPanel();
		overallModelPanel.setLayout(new BoxLayout(overallModelPanel, BoxLayout.Y_AXIS));
		overallModelPanel.add(modelLabelPanel);
		overallModelPanel.add(modelScrollPane);
		settingPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		settingPanel.add(predictiveFeaturePanel);
		settingPanel.add(overallModelPanel);
		//create buttonPanel
		doneButton= new JButton();
		doneButton.setSize(50, 20);
		doneButton.setText(DONE);
		doneButton.setActionCommand(DONE);
		doneButton.addActionListener(actionListener);
		buttonPanel= new JPanel();
		buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
		buttonPanel.add(doneButton);
		setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));
		add(settingPanel);
		add(buttonPanel);
	}

	private void updateModelDisplay(String fileName)
	{ 
		String delimiter=" ";
		int delimiterIndex=0;
		int lineCounter=0;
		String BEST_MODEL_SVM_TYPE = "SVM type: ";
		String BEST_MODEL_KERNEL_TYPE = "Kernel type: ";
		String BEST_MODEL_NR_CLASS = "NR class: ";
		String BEST_MODEL_TOTAL_SV = "Total SV: ";
		String BEST_MODEL_RHO = "Rho: ";
		String BEST_MODEL_PROB_A = "Prob A: ";
		
		if(fileName==null)//for resetting display
		{
		  	SVMType.setText(BEST_MODEL_SVM_TYPE+"");
			kernelType.setText(BEST_MODEL_KERNEL_TYPE+"");
			NrClass.setText(BEST_MODEL_NR_CLASS+"");
			totalSV.setText(BEST_MODEL_TOTAL_SV+"");
			rho.setText(BEST_MODEL_RHO+"");
			probA.setText(BEST_MODEL_PROB_A+"");
		}
		else
		{
			try {
				BufferedReader reader = new BufferedReader(new FileReader(fileName));
				String nextLine = reader.readLine();
				while (nextLine != null) 
				{
					delimiterIndex=nextLine.indexOf(delimiter);
					if(lineCounter==0)
						SVMType.setText(BEST_MODEL_SVM_TYPE+nextLine.substring(delimiterIndex+1));
					else if(lineCounter==1)
						kernelType.setText(BEST_MODEL_KERNEL_TYPE+nextLine.substring(delimiterIndex+1));
					else if(lineCounter==2)
						NrClass.setText(BEST_MODEL_NR_CLASS+nextLine.substring(delimiterIndex+1));
					else if(lineCounter==3)
						totalSV.setText(BEST_MODEL_TOTAL_SV+nextLine.substring(delimiterIndex+1));
					else if(lineCounter==4)
						rho.setText(BEST_MODEL_RHO+nextLine.substring(delimiterIndex+1));
					else if(lineCounter==5)
						probA.setText(BEST_MODEL_PROB_A+nextLine.substring(delimiterIndex+1));
					nextLine=reader.readLine();
					lineCounter=lineCounter+1;
					if(lineCounter==6)
						nextLine=null;
				}
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}	
		
	private void done_actionPerformed(){
		setVisible(false);
	}
}
