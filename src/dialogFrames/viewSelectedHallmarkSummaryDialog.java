package dialogFrames;

//Imports are listed in full to show what's being used could just import javax.swing.* and java.awt.* etc..
import graph.Gephi_graph;
import hallmark.allHallmark;

import java.awt.AWTException;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Robot;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.DecimalFormat;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextPane;
import javax.swing.ListModel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.EtchedBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;

import org.gephi.project.api.Project;
import org.gephi.project.api.ProjectController;
import org.gephi.project.api.Workspace;

import constants.stringConstant;
import database.postgreSQL;

@SuppressWarnings("serial")

public class viewSelectedHallmarkSummaryDialog extends JDialog{
	private JPanel summaryTablePanel, legendPanel, buttonPanel, interactiveGraphPanel;
	private JButton reapplyHallmarkMaskButton, doneButton;
	private MyModel summaryTableModel;
	private JTable summaryTable;
	private JScrollPane summaryTableScrollPane, legendScrollPane;
	private JLabel statusLabel;
	private ArrayList<JCheckBox> checkBoxArray=new ArrayList<JCheckBox>(); 

	private String DONE;
	private stringConstant staticString=new stringConstant();

	protected final static String REAPPLY_HALLMARK_MASK = "Reapply hallmark mask";

	postgreSQL postgreDB=new postgreSQL();
	private ArrayList<String> selectedHallmarks=new ArrayList<String>();
	private ArrayList<String> speciesList=new ArrayList<String>();
	private ArrayList<ArrayList<String>> hallmarkSummary=new ArrayList<ArrayList<String>>();
	private ArrayList<String> hallmarkIndexingList=new ArrayList<String>();
	private ArrayList<Integer> numNodeWithSelectedHallmark=new ArrayList<Integer>();
	private ArrayList<String> reapplySelectedHallmark=new ArrayList<String>();
	private ArrayList<String> originalSelectedHallmarks=new ArrayList<String>();
	private ArrayList<String> oldSelectedHallmarks=new ArrayList<String>();

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

	private String VIEW_HALLMARK = "View Hallmark";

	private Gephi_graph gephiGraph; 
	private JList<String> mainFrameNodeList;

	private int COLUMN_WIDTH=80;
	private int NODE_NAME_WIDTH=200;
	private int dialogWidth=900;

	private ProjectController interactivePC;
	private Project interactiveProject;
	private Workspace previousWorkspace, currWorkspace;
	
	//threading
	VisualizeNetworkWorker visualizeNetworkWorker;

	public class MyModel extends AbstractTableModel {
		private String[] columns;
		private ArrayList<ArrayList<String>> matrix=new ArrayList<ArrayList<String>>();

		private MyModel(ArrayList<ArrayList<String>> tableMatrix, ArrayList<String> hallmarkIndex) {
			System.out.println("hallmarkIndex:"+hallmarkIndex.toString());
			matrix=tableMatrix;
			columns=new String[hallmarkIndex.size()+1];
			columns[0]="Node name";
			for(int i=0; i<hallmarkIndex.size(); i++)
				columns[i+1]=hallmarkIndex.get(i);
		}

		public void addElement(ArrayList<String> e) {
			// Adds the element in the last position in the list
			matrix.add(e);
			fireTableRowsInserted(matrix.size()-1, matrix.size()-1);
		}

		@Override
		public int getRowCount() {
			return matrix.size();
		}

		@Override
		public int getColumnCount() {
			if(getRowCount()>1)
				return matrix.get(0).size();
			else
				return 0;
		}

		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {
			if(rowIndex>=0 && rowIndex<getRowCount() && columnIndex>=0 && columnIndex<getColumnCount())
				return matrix.get(rowIndex).get(columnIndex);
			else
				return null;
		}

	}

	public class EditableHeaderRenderer implements TableCellRenderer {

		private JTable table = null;
		private MouseEventReposter reporter = null;
		private JComponent editor;

		EditableHeaderRenderer(JComponent editor) {
			this.editor = editor;
			this.editor.setBorder(UIManager.getBorder("TableHeader.cellBorder"));
		}

		@Override
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int col) {
			if (table != null && this.table != table) {
				this.table = table;
				final JTableHeader header = table.getTableHeader();   
				if (header != null) {   
					this.editor.setForeground(header.getForeground());   
					this.editor.setBackground(header.getBackground());   
					this.editor.setFont(header.getFont());
					reporter = new MouseEventReposter(header, col, this.editor);
					header.addMouseListener(reporter);
				}
			}

			if (reporter != null) reporter.setColumn(col);

			return this.editor;
		}

		public class MouseEventReposter extends MouseAdapter {

			private Component dispatchComponent;
			private JTableHeader header;
			private int column  = -1;
			private Component editor;

			public MouseEventReposter(JTableHeader header, int column, Component editor) {
				this.header = header;
				this.column = column;
				this.editor = editor;
			}

			public void setColumn(int column) {
				this.column = column;
			}

			private void setDispatchComponent(MouseEvent e) {
				int col = header.getTable().columnAtPoint(e.getPoint());
				if (col != column || col == -1) return;

				Point p = e.getPoint();
				Point p2 = SwingUtilities.convertPoint(header, p, editor);
				dispatchComponent = SwingUtilities.getDeepestComponentAt(editor, p2.x, p2.y);
			}

			private boolean repostEvent(MouseEvent e) {
				if (dispatchComponent == null) {
					return false;
				}
				MouseEvent e2 = SwingUtilities.convertMouseEvent(header, e, dispatchComponent);
				dispatchComponent.dispatchEvent(e2);
				return true;
			}

			@Override
			public void mousePressed(MouseEvent e) {
				if (header.getResizingColumn() == null) {
					Point p = e.getPoint();

					int col = header.getTable().columnAtPoint(p);
					if (col != column || col == -1) return;

					int index = header.getColumnModel().getColumnIndexAtX(p.x);
					if (index == -1) return;

					editor.setBounds(header.getHeaderRect(index));
					header.add(editor);
					editor.validate();
					setDispatchComponent(e);
					repostEvent(e);
				}
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				repostEvent(e);
				dispatchComponent = null;
				header.remove(editor);
			}
		}
	}

	public ArrayList<String> getSelectedHallmarks()
	{
		return selectedHallmarks;
	}
	
	public viewSelectedHallmarkSummaryDialog(postgreSQL db, ArrayList<String> hallmarkSelected, ArrayList<String> oldHallmarkSelected, JList<String> currViewNodeList,
			ProjectController pc, Project project, Workspace oldWorkspace, Workspace newWorkspace, JLabel label, JPanel graphPanel, ArrayList<String> allNodeList)
	{
		if(hallmarkSelected.size()==0)
			JOptionPane.showMessageDialog(new JFrame(), "No hallmark selected for viewing.", "Information", JOptionPane.INFORMATION_MESSAGE);
		else
		{
			//initialize variables
			postgreDB=db;
			selectedHallmarks=new ArrayList<String>();
			for(int i=0; i<hallmarkSelected.size(); i++)
			{
				if(selectedHallmarks.contains(hallmarkSelected.get(i))==false)
					selectedHallmarks.add(hallmarkSelected.get(i));
			}
			for(int i=0; i<oldHallmarkSelected.size(); i++)
			{
				if(oldSelectedHallmarks.contains(oldHallmarkSelected.get(i))==false)
					oldSelectedHallmarks.add(oldHallmarkSelected.get(i));
			}
			for(int i=0; i<selectedHallmarks.size(); i++)
			{
				if(originalSelectedHallmarks.contains(selectedHallmarks.get(i))==false)
					originalSelectedHallmarks.add(selectedHallmarks.get(i));
			}
			interactivePC=pc;
			interactiveProject=project;
			previousWorkspace=oldWorkspace;
			currWorkspace=newWorkspace;
			speciesList=new ArrayList<String>();
			interactiveGraphPanel=graphPanel;
			mainFrameNodeList=currViewNodeList;
			gephiGraph=new Gephi_graph(interactivePC, interactiveProject, currWorkspace, mainFrameNodeList); //for visualization
			statusLabel=label;
			//visualizeNetworkWorker=vnWorker;
			ListModel<String> nodeListModel=currViewNodeList.getModel();
			int listSize=nodeListModel.getSize();
			for(int i=0; i<listSize; i++)
				speciesList.add(nodeListModel.getElementAt(i));
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

			hallmarkIndexingList.add(HALLMARK_PROLIFERATION_DB);
			hallmarkIndexingList.add(HALLMARK_GROWTH_REPRESSOR_DB);
			hallmarkIndexingList.add(HALLMARK_APOPTOSIS_DB);
			hallmarkIndexingList.add(HALLMARK_REPLICATIVE_IMMORTALITY_DB);
			hallmarkIndexingList.add(HALLMARK_ANGIOGENESIS_DB);
			hallmarkIndexingList.add(HALLMARK_METASTASIS_DB);
			hallmarkIndexingList.add(HALLMARK_METABOLISM_DB);
			hallmarkIndexingList.add(HALLMARK_IMMUNE_DESTRUCTION_DB);
			hallmarkIndexingList.add(HALLMARK_GENOME_INSTABILITY_DB);
			hallmarkIndexingList.add(HALLMARK_TUMOR_PROMOTING_INFLAMMATION_DB);

			hallmarkSummary=postgreDB.getHallmark_nodeListOfAllSelectedHallmark(selectedHallmarks, speciesList);
			numNodeWithSelectedHallmark=postgreDB.getHallmark_numNodeOfAllSelectedHallmark(selectedHallmarks, speciesList);
			//initialize constants
			DONE=staticString.getDone();

			if(hallmarkSummary.size()==0)
				JOptionPane.showMessageDialog(new JFrame(), "Nodes do not contain any of the hallmarks selected", "Information", JOptionPane.INFORMATION_MESSAGE);
			else
			{
				//set JDialog properties
				setTitle("Summary of selected hallmark(s)");
				dialogWidth=selectedHallmarks.size()*COLUMN_WIDTH+NODE_NAME_WIDTH;
				setSize(dialogWidth+20,460);
				setLocationRelativeTo(null);
				setResizable(false);
				setModal(false);
				setFocusableWindowState(true) ;
				//setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
				
				//configure components in JDialog
				initializeComponents();
				reapplyHallmarkMask_actionPerformed();
			}
		}
	}

	private void speciesList_valueChanged(ListSelectionEvent e) 
	{
		if (!e.getValueIsAdjusting()) 
		{
			int selectedRow=summaryTable.getSelectedRow();
			if(selectedRow!=-1)
			{
				String selectedNode = summaryTable.getValueAt(selectedRow, 0).toString();
				System.out.println("summaryTable selectedNode is "+selectedNode);
				mainFrameNodeList.setSelectedValue(selectedNode, true);
				System.out.println(selectedNode);
				if(postgreDB.checkNodeNameExists(selectedNode)==true)
				{
					visualizeSelectedNodeInGraph(selectedNode);
					sendVirtualClickToRefreshGraphDisplay();
				}
			}
		}
	}

	private void visualizeSelectedNodeInGraph(String node) 
	{
		//uncolor all previous hallmarks
		gephiGraph.resetHallmarkNode();
		//color selected hallmarks
		allHallmark hallmarkNodeList=postgreDB.getHallmark_hallmark();
		gephiGraph.colorAndResizeHallmarkNode(hallmarkNodeList, reapplySelectedHallmark, true);
		gephiGraph.colorAndResizeSelectedNode(node, true); 
		interactiveGraphPanel.revalidate();
		interactiveGraphPanel.repaint();
	}

	private ActionListener actionListener = new ActionListener() 
	{
		public void actionPerformed(ActionEvent act) {
			if (act.getActionCommand() == DONE)
				cancel_actionPerformed();
			if (act.getActionCommand() == REAPPLY_HALLMARK_MASK)
				reapplyHallmarkMask_actionPerformed();
		}
	};

	private void reapplyHallmarkMask_actionPerformed()
	{
		allHallmark hallmarkNodeList=postgreDB.getHallmark_hallmark();
		reapplySelectedHallmark=new ArrayList<String>();
		for(int i=0; i<checkBoxArray.size(); i++)
		{
			if(checkBoxArray.get(i).isSelected()==true && reapplySelectedHallmark.contains(originalSelectedHallmarks.get(i))==false)
				reapplySelectedHallmark.add(originalSelectedHallmarks.get(i));
		}
		selectedHallmarks=reapplySelectedHallmark;
		System.out.println("viewSelectedHallmarkSummaryDialog.java selectedHallmarks:"+selectedHallmarks.toString());
		visualizeNetworkWorker=new VisualizeNetworkWorker();
		gephiGraph.resetHallmarkNode();
		ArrayList<Object> param=new ArrayList<Object>();
		param.add(gephiGraph);
		param.add(hallmarkNodeList);
		param.add(reapplySelectedHallmark);
		//param.add(oldSelectedHallmarks);
		visualizeNetworkWorker.Start(VIEW_HALLMARK, param);
	}

	private void initializeComponents()
	{
		summaryTablePanel=new JPanel();
		summaryTablePanel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
		summaryTablePanel.setPreferredSize(new Dimension(dialogWidth, 280));
		summaryTablePanel.setLayout(new FlowLayout(FlowLayout.LEFT));

		System.out.println("hallmarkSummary: "+hallmarkSummary.size());
		summaryTableModel=new MyModel(hallmarkSummary, selectedHallmarks);
		for(int i=0; i<hallmarkSummary.size(); i++)
			System.out.println(hallmarkSummary.get(i).toString());
		summaryTable=new JTable(summaryTableModel);
		summaryTable.setAutoCreateRowSorter(true);
		summaryTableScrollPane=new JScrollPane(summaryTable); 
		summaryTableScrollPane.setPreferredSize(new Dimension(dialogWidth,260)); 
		summaryTableScrollPane.setViewportView(summaryTable);
		summaryTablePanel.add(summaryTableScrollPane);
		int totalNodes=speciesList.size();
		DecimalFormat df = new DecimalFormat();
		df.setMaximumFractionDigits(2);
		JTableHeader th = summaryTable.getTableHeader();
		TableColumnModel tcm = th.getColumnModel();
		TableColumn tc = tcm.getColumn(0);
		tc.setHeaderValue( "Node name" );
		for(int i=0; i<selectedHallmarks.size(); i++)
		{
			int index=hallmarkIndexingList.indexOf(selectedHallmarks.get(i));
			tc=tcm.getColumn(i+1);
			double percentage=((double)numNodeWithSelectedHallmark.get(i))/((double)totalNodes)*((double)100);
			JCheckBox hallmarkHeaderCheckBox=new JCheckBox((index+1)+" ["+(df.format(percentage))+"%]");
			hallmarkHeaderCheckBox.setSelected(true);
			hallmarkHeaderCheckBox.setBorderPaintedFlat(true);
			checkBoxArray.add(hallmarkHeaderCheckBox);
			//tc.setHeaderValue((index+1)+" ["+(df.format(percentage))+"%]");

			tc.setHeaderRenderer(new EditableHeaderRenderer(hallmarkHeaderCheckBox));
		}
		th.repaint();
		summaryTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() 
		{
			public void valueChanged(ListSelectionEvent e){speciesList_valueChanged(e);}
		});

		//legendPanel ***********************************************
		legendPanel=new JPanel();
		legendPanel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
		legendPanel.setPreferredSize(new Dimension(dialogWidth, 100));
		legendPanel.setLayout(new BoxLayout(legendPanel, BoxLayout.Y_AXIS));

		StyleContext context = new StyleContext();
		StyledDocument document = new DefaultStyledDocument(context);
		Style style = context.getStyle(StyleContext.DEFAULT_STYLE);
		StyleConstants.setUnderline(style, true);
		StyleConstants.setBold(style, true);
		try {
			document.insertString(document.getLength(), "Hallmark Legend:", style);
		} catch (BadLocationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		JTextPane legendTitleTextPane=new JTextPane(document);

		JTextPane legendContentsTextPane=new JTextPane();
		StringBuilder hallmarkContents=new StringBuilder();
		if(selectedHallmarks.size()>3)
		{
			hallmarkContents.append(HALLMARK_PROLIFERATION_GUI+"\t\t"+HALLMARK_METASTASIS_GUI+"\n");
			hallmarkContents.append(HALLMARK_GROWTH_REPRESSOR_GUI+"\t\t"+HALLMARK_METABOLISM_GUI+"\n");
			hallmarkContents.append(HALLMARK_APOPTOSIS_GUI+"\t\t"+HALLMARK_IMMUNE_DESTRUCTION_GUI+"\n");
			hallmarkContents.append(HALLMARK_REPLICATIVE_IMMORTALITY_GUI+"\t"+HALLMARK_GENOME_INSTABILITY_GUI+"\n");
			hallmarkContents.append(HALLMARK_ANGIOGENESIS_GUI+"\t\t"+HALLMARK_TUMOR_PROMOTING_INFLAMMATION_GUI+"\n");
		}
		else
		{
			hallmarkContents.append(HALLMARK_PROLIFERATION_GUI+"\n");
			hallmarkContents.append(HALLMARK_GROWTH_REPRESSOR_GUI+"\n");
			hallmarkContents.append(HALLMARK_APOPTOSIS_GUI+"\n");
			hallmarkContents.append(HALLMARK_REPLICATIVE_IMMORTALITY_GUI+"\n");
			hallmarkContents.append(HALLMARK_ANGIOGENESIS_GUI+"\n");
			hallmarkContents.append(HALLMARK_METASTASIS_GUI+"\n");
			hallmarkContents.append(HALLMARK_METABOLISM_GUI+"\n");
			hallmarkContents.append(HALLMARK_IMMUNE_DESTRUCTION_GUI+"\n");
			hallmarkContents.append(HALLMARK_GENOME_INSTABILITY_GUI+"\n");
			hallmarkContents.append(HALLMARK_TUMOR_PROMOTING_INFLAMMATION_GUI+"\n");
		}
		legendContentsTextPane.setText(hallmarkContents.toString());

		JPanel legendContentsPanel=new JPanel();
		legendContentsPanel.setLayout(new BoxLayout(legendContentsPanel, BoxLayout.Y_AXIS));
		legendContentsPanel.add(legendTitleTextPane);
		legendContentsPanel.add(legendContentsTextPane);
		legendScrollPane=new JScrollPane(legendContentsPanel); 
		legendScrollPane.setPreferredSize(new Dimension(dialogWidth,100)); 
		legendScrollPane.setViewportView(legendContentsPanel);
		legendPanel.add(legendScrollPane);

		//buttonPanel ***********************************************
		buttonPanel=new JPanel();
		buttonPanel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
		buttonPanel.setPreferredSize(new Dimension(dialogWidth, 50));
		buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));

		//reapplyHallmarkMask and done button
		reapplyHallmarkMaskButton= new JButton();
		reapplyHallmarkMaskButton.setSize(50, 20);
		reapplyHallmarkMaskButton.setText(REAPPLY_HALLMARK_MASK);
		reapplyHallmarkMaskButton.setActionCommand(REAPPLY_HALLMARK_MASK);
		reapplyHallmarkMaskButton.addActionListener(actionListener);
		doneButton= new JButton();
		doneButton.setSize(50, 20);
		doneButton.setText(DONE);
		doneButton.setActionCommand(DONE);
		doneButton.addActionListener(actionListener);
		buttonPanel.add(reapplyHallmarkMaskButton);
		buttonPanel.add(doneButton);

		addWindowListener(new WindowAdapter()
        {
            @Override
            public void windowClosing(WindowEvent e)
            {
                System.out.println("Closed. switch workspace back");
                interactivePC.openWorkspace(previousWorkspace); 
                setVisible(false);
            }
        });
		setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));
		add(summaryTablePanel);
		add(legendPanel);
		add(buttonPanel);

	}

	private void cancel_actionPerformed(){
		setVisible(false);
	}

	private void sendVirtualClickToRefreshGraphDisplay() 
	{
		System.out.println("send virtual click");
		Point currMousePosition=MouseInfo.getPointerInfo().getLocation();
		Robot robot;
		try {
			robot = new Robot();
			robot.mouseMove(interactiveGraphPanel.getLocationOnScreen().x+10, interactiveGraphPanel.getLocationOnScreen().y+10);
			robot.mousePress(InputEvent.BUTTON1_MASK); //BUTTON1_MASK is the left button,
			robot.mouseRelease(InputEvent.BUTTON1_MASK);
			robot.mouseMove(currMousePosition.x, currMousePosition.y);
		} catch (AWTException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	class VisualizeNetworkWorker
	{
		Thread worker;
		boolean working;
		String ACTION;
		ArrayList<Object> paramList;

		VisualizeNetworkWorker()
		{
			worker=new Thread(new Runnable(){
				public void run(){
					if(ACTION.compareTo(VIEW_HALLMARK)==0)
					{
						working=true;
						gephiGraph=(Gephi_graph)paramList.get(0);
						allHallmark hallmarkNodeList=(allHallmark)paramList.get(1);
						ArrayList<String> newHallmarks=(ArrayList<String>)paramList.get(2);
						//ArrayList<String> oldHallmarks=(ArrayList<String>)paramList.get(3);
						statusLabel.setText("visualizing hallmark on current graph...");
						//uncolor all previous hallmarks
						gephiGraph.resetHallmarkNode();
						//color selected hallmarks
						gephiGraph.colorAndResizeHallmarkNode(hallmarkNodeList, newHallmarks, true);
						sendVirtualClickToRefreshGraphDisplay();
						statusLabel.setText("finish visualizing hallmark on current graph.");
						working=false;
					}
				}
			});
		}
		public void Start(String action, ArrayList<Object> parameterList)
		{
			if(!working)
			{
				ACTION=action;
				paramList=parameterList;
				worker.start();
			}
		}
	}
}
