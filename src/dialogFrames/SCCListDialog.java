package dialogFrames;

//Imports are listed in full to show what's being used could just import javax.swing.* and java.awt.* etc..
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import fileObject.fReaderWriter;

@SuppressWarnings("serial")

public class SCCListDialog extends JDialog{
	private JList<String> SCCList;
	private JScrollPane SCCScrollPane;
	private JList<String> nodeList;
	private JScrollPane nodeScrollPane;
	private ArrayList<String> SCCForDisplay=new ArrayList<String>();
	private ArrayList<String> NodeForDisplay=new ArrayList<String>();
	private ArrayList<ArrayList<String>> mapping=new ArrayList<ArrayList<String>>();
	
	public SCCListDialog(ArrayList<String> sccList, ArrayList<ArrayList<String>> sccMapList)
	{
		//initialize variables
		SCCForDisplay=sccList;
		mapping=sccMapList;
		for(int i=0; i<mapping.size(); i++)
			mapping.get(i).remove(0);
		
		//for debugging
		/*ArrayList<String> logStream=new ArrayList<String>();
		for(int i=0; i<sccMapList.size(); i++)
			logStream.add(sccMapList.get(i).toString());
		fReaderWriter writer=new fReaderWriter();
		writer.writeToFileWithBufferedWriterArray("C:\\Users\\Chua Huey Eng\\workspace\\TROVE backups\\log1.txt", sccList, true, false);
		writer.writeToFileWithBufferedWriterArray("C:\\Users\\Chua Huey Eng\\workspace\\TROVE backups\\log1.txt", logStream, false, true);
		*/
		
		//set JDialog properties
		setTitle("SCC meta node and graph node mapping");
		setSize(370,370);
		setLocationRelativeTo(null);
		setResizable(false);
		setModal(false);

		//configure components in JDialog
		initializeComponents();
	}

	private ArrayList<String> retrieveNodeForDisplay(String sccMetaNode)
	{
		int index=SCCForDisplay.indexOf(sccMetaNode);
		System.out.println("index:"+index);
		if(index!=-1)
			return mapping.get(index);
		else 
			return null;
	}

	private void sccList_valueChanged(ListSelectionEvent e) 
	{
		if (!e.getValueIsAdjusting()) 
		{
			if(SCCList.getSelectedValue()!=null)
			{
				int selectedIndex = SCCList.getSelectedIndex();
				if(selectedIndex!=-1)
				{
					//update details of newly selected scc
					NodeForDisplay=retrieveNodeForDisplay(SCCList.getSelectedValue());
					String[] formattedNodeList=new String[NodeForDisplay.size()];
					formattedNodeList = NodeForDisplay.toArray(formattedNodeList);
					nodeList.setListData(formattedNodeList);
				}
			}
		}
	}

	private void initializeComponents()
	{
		JPanel leftPanel=new JPanel();
		leftPanel.setPreferredSize(new Dimension(220, 250));
		leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));

		JPanel rightPanel=new JPanel();
		rightPanel.setPreferredSize(new Dimension(120, 250));
		rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));

		//scc list for selection
		JPanel sccListLabelPanel=new JPanel();
		sccListLabelPanel.setPreferredSize(new Dimension(220, 20));
		sccListLabelPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		JLabel sccListLabel=new JLabel("SCC Meta Node List:");
		sccListLabelPanel.add(sccListLabel);
		String[] formattedsccList=new String[SCCForDisplay.size()];
		formattedsccList = SCCForDisplay.toArray(formattedsccList);
		SCCList=new JList<String>();
		SCCList.setListData(formattedsccList);
		SCCScrollPane = new JScrollPane(SCCList);
		SCCList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		SCCList.addListSelectionListener(new ListSelectionListener() 
		{
			public void valueChanged(ListSelectionEvent e){sccList_valueChanged(e);}
		});
		SCCScrollPane.setViewportView(SCCList);
		SCCScrollPane.setPreferredSize(new Dimension(220, 220));
		SCCScrollPane.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		leftPanel.add(sccListLabelPanel);
		leftPanel.add(SCCScrollPane);

		//node list for selection
		JPanel nodeListLabelPanel=new JPanel();
		nodeListLabelPanel.setPreferredSize(new Dimension(120, 20));
		nodeListLabelPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		JLabel nodeListLabel=new JLabel("Node List:");
		nodeListLabelPanel.add(nodeListLabel);
		String[] formattedNodeList=new String[NodeForDisplay.size()];
		formattedNodeList = NodeForDisplay.toArray(formattedNodeList);
		nodeList=new JList<String>();
		nodeList.setListData(formattedNodeList);
		nodeScrollPane = new JScrollPane(nodeList);
		nodeList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		nodeScrollPane.setViewportView(nodeList);
		nodeScrollPane.setPreferredSize(new Dimension(120, 220));
		nodeScrollPane.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		rightPanel.add(nodeListLabelPanel);
		rightPanel.add(nodeScrollPane);

		setLayout(new FlowLayout(FlowLayout.LEFT));
		add(leftPanel);
		add(rightPanel);
	}
}
