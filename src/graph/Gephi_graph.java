package graph;

import hallmark.allHallmark;
import hallmark.hallmarkClusterImpl;
import hallmark.hallmark;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.DecimalFormat;
//**previewMouse**
/*import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Arrays;
 */import java.util.ArrayList;
import java.util.Arrays;

 import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;

 import node.gephiNode;

import org.gephi.data.attributes.api.AttributeColumn;
import org.gephi.data.attributes.api.AttributeController;
import org.gephi.data.attributes.api.AttributeModel;
import org.gephi.data.attributes.api.AttributeType;
import org.gephi.datalab.api.AttributeColumnsController;
import org.gephi.filters.api.FilterController;
import org.gephi.graph.api.Edge;
import org.gephi.graph.api.Graph;
import org.gephi.graph.api.GraphController;
import org.gephi.graph.api.GraphModel;
import org.gephi.graph.api.MixedGraph;
import org.gephi.graph.api.Node;
import org.gephi.io.importer.api.Container;
import org.gephi.io.importer.api.ContainerFactory;
import org.gephi.io.importer.api.ImportController;
import org.gephi.io.processor.plugin.DefaultProcessor;
import org.gephi.layout.api.LayoutController;
import org.gephi.layout.plugin.force.yifanHu.YifanHu;
import org.gephi.layout.plugin.force.yifanHu.YifanHuLayout;
import org.gephi.layout.plugin.forceAtlas.ForceAtlas;
import org.gephi.layout.plugin.forceAtlas.ForceAtlasLayout;
import org.gephi.layout.plugin.forceAtlas2.ForceAtlas2;
import org.gephi.layout.plugin.forceAtlas2.ForceAtlas2Builder;
import org.gephi.layout.plugin.fruchterman.FruchtermanReingold;
import org.gephi.layout.plugin.fruchterman.FruchtermanReingoldBuilder;
import org.gephi.layout.plugin.labelAdjust.LabelAdjust;
import org.gephi.layout.plugin.labelAdjust.LabelAdjustBuilder;
import org.gephi.plugins.layout.noverlap.NoverlapLayout;
import org.gephi.plugins.layout.noverlap.NoverlapLayoutBuilder;
import org.gephi.preview.api.ManagedRenderer;
 //**previewMouse**
 /*import org.gephi.preview.api.ManagedRenderer;
import org.gephi.preview.api.PreviewMouseEvent;
  */import org.gephi.preview.api.PreviewController;
import org.gephi.preview.api.PreviewModel;
import org.gephi.preview.api.PreviewMouseEvent;
import org.gephi.preview.api.PreviewProperty;
import org.gephi.preview.api.ProcessingTarget;
import org.gephi.preview.api.RenderTarget;
import org.gephi.preview.types.DependantOriginalColor;
import org.gephi.project.api.Project;
import org.gephi.project.api.ProjectController;
import org.gephi.project.api.Workspace;
import org.openide.util.Lookup;

import processing.core.PApplet;

import constants.stringConstant;
import cz.cvut.fit.krizeji1.multicolour.MultiColourRenderer;

  public class Gephi_graph 
  {
	  private ProjectController interactivePC;
	  private Project interactiveProject;
	  private Workspace interactiveWorkspace;
	  private GraphController interactiveGraphController;
	  private GraphModel interactiveGraphModel;
	  private MixedGraph interactiveGraph;
	  private LayoutController interactiveLayoutController;
	  private PreviewController interactivePreviewController;
	  private PreviewModel interactivePreviewModel;
	  private ProcessingTarget interactiveTarget;
	  private PApplet interactiveApplet;
	  private AttributeColumnsController attributeColumnsController;
	  private AttributeModel attributeModel;
	  
	  //**previewMouse**
	  /*private Gephi_graph_previewMouseRenderer mouseRenderer;
    private Gephi_graph_previewMouseListener mouseListener;
	   */
	  private FilterController filterController;
	  /*    private GraphView viewMain;
    private GraphView viewEgo;
	   */
	  private ArrayList<Node> allNodeList;

	  private stringConstant strConstants=new stringConstant();

	  private String HALLMARK_PROLIFERATION_DB = strConstants.getDBProliferation();
	  private String HALLMARK_GROWTH_REPRESSOR_DB = strConstants.getDBGrowthRepressor();
	  private String HALLMARK_APOPTOSIS_DB = strConstants.getDBApoptosis();
	  private String HALLMARK_REPLICATIVE_IMMORTALITY_DB = strConstants.getDBReplicativeImmortality();
	  private String HALLMARK_ANGIOGENESIS_DB = strConstants.getDBAngiogenesis();
	  private String HALLMARK_METASTASIS_DB = strConstants.getDBMetastasis();
	  private String HALLMARK_METABOLISM_DB = strConstants.getDBMetabolism();
	  private String HALLMARK_IMMUNE_DESTRUCTION_DB = strConstants.getDBImmuneDestruction();
	  private String HALLMARK_GENOME_INSTABILITY_DB = strConstants.getDBGenomeInstability();
	  private String HALLMARK_TUMOR_PROMOTING_INFLAMMATION_DB = strConstants.getDBTumorPromotingInflammation();
	  private String GEPHI_WEIGHT=strConstants.getGephiWeight();
	  private String GEPHI_COLOR_LIST=strConstants.getGephiHallmarkColorList();
	  
	  graphNodeData prevSelectedNode;
	  ArrayList<graphNodeData> prevSelectedEdge; //prevSelectedEdge.get(0) is source node of edge and prevSelectedEdge.get(1) is target node of edge
	  graphNodeData originalNode; //reference size and color for resetting graph

	  private ArrayList<String> hallmarkNode_proliferation;
	  private ArrayList<String> hallmarkNode_growth_repressor;
	  private ArrayList<String> hallmarkNode_apoptosis;
	  private ArrayList<String> hallmarkNode_replicative_immortality;
	  private ArrayList<String> hallmarkNode_angiogenesis;
	  private ArrayList<String> hallmarkNode_metastasis;
	  private ArrayList<String> hallmarkNode_metabolism;
	  private ArrayList<String> hallmarkNode_immune_destruction;
	  private ArrayList<String> hallmarkNode_genome_instability;
	  private ArrayList<String> hallmarkNode_tumor_promoting_inflammation;

	  private boolean SET_ORIGINAL_NODE;
	  private boolean HALLMARK_PROLIFERATION_VIEW;
	  private boolean HALLMARK_GROWTH_REPRESSOR_VIEW;
	  private boolean HALLMARK_APOPTOSIS_VIEW;
	  private boolean HALLMARK_REPLICATIVE_IMMORTALITY_VIEW;
	  private boolean HALLMARK_ANGIOGENESIS_VIEW;
	  private boolean HALLMARK_METASTASIS_VIEW;
	  private boolean HALLMARK_METABOLISM_VIEW;
	  private boolean HALLMARK_IMMUNE_DESTRUCTION_VIEW;
	  private boolean HALLMARK_GENOME_INSTABILITY_VIEW;
	  private boolean HALLMARK_TUMOR_PROMOTING_INFLAMMATION_VIEW;

	  private gephiNode nodesToRestore=new gephiNode();

	  private boolean OLD_PIN_EXISTS=false;

	  public Gephi_graph(ProjectController PC, Project project, Workspace workspace)
	  //public Gephi_graph()
	  {
		  interactivePC=PC;
		  interactiveProject=project;
		  interactiveWorkspace=workspace;
		  interactiveGraphController=Lookup.getDefault().lookup(GraphController.class);
		  interactiveGraphModel=interactiveGraphController.getModel(interactiveWorkspace);
		  interactiveGraph=interactiveGraphModel.getMixedGraph(); 
		  interactivePreviewController = Lookup.getDefault().lookup(PreviewController.class);
		  interactivePreviewModel=interactivePreviewController.getModel(interactivePC.getCurrentWorkspace());
		  interactiveTarget=(ProcessingTarget) interactivePreviewController.getRenderTarget(RenderTarget.PROCESSING_TARGET);
		  interactiveApplet = interactiveTarget.getApplet();
		  attributeColumnsController=Lookup.getDefault().lookup(AttributeColumnsController.class);
		  attributeModel=Lookup.getDefault().lookup(AttributeController.class).getModel(interactiveWorkspace);

		  
		  /*interactivePC=Lookup.getDefault().lookup(ProjectController.class);
		  interactivePC.newProject();
		  interactiveProject=interactivePC.getCurrentProject();
		  interactiveWorkspace=interactivePC.newWorkspace(interactiveProject);
		  interactivePC.openWorkspace(interactiveWorkspace); 
		  System.out.println("interactiveWorkspace:"+interactiveWorkspace);
		  interactiveGraphController=Lookup.getDefault().lookup(GraphController.class);
		  interactiveGraphModel=interactiveGraphController.getModel(interactiveWorkspace);
		  interactiveGraph=interactiveGraphModel.getMixedGraph(); 
		  interactivePreviewController = Lookup.getDefault().lookup(PreviewController.class);
		  interactivePreviewModel = interactivePreviewController.getModel(interactiveWorkspace);
		  interactivePreviewModel=interactivePreviewController.getModel(interactivePC.getCurrentWorkspace());
		  interactiveTarget=(ProcessingTarget) interactivePreviewController.getRenderTarget(RenderTarget.PROCESSING_TARGET);
		  attributeColumnsController=Lookup.getDefault().lookup(AttributeColumnsController.class);
		  attributeModel=Lookup.getDefault().lookup(AttributeController.class).getModel(interactiveWorkspace);
		  */
		  
		 /* interactivePC=Lookup.getDefault().lookup(ProjectController.class);
		  interactivePC.newProject();
		  interactiveProject=interactivePC.getCurrentProject();
		  interactiveWorkspace=interactivePC.newWorkspace(interactiveProject);
		  interactivePC.openWorkspace(interactiveWorkspace);  
		  
		  //Generate dynamic graph into workspace 2
		  interactiveGraphController=Lookup.getDefault().lookup(GraphController.class);
		  interactiveGraphModel=interactiveGraphController.getModel(interactiveWorkspace);
		  
		  interactiveGraph=interactiveGraphModel.getMixedGraph(); 
		  interactiveWorkspace.add(interactiveGraph);
		  
		  //interactiveWorkspace=interactivePC.getCurrentWorkspace();
//		  interactiveGraphController=Lookup.getDefault().lookup(GraphController.class);
		  //interactiveWorkspace=interactivePC.getCurrentWorkspace();
		  //interactiveWorkspace.add();
//		  interactiveGraphModel=interactiveGraphController.getModel(interactiveWorkspace);
//		  interactiveGraph=interactiveGraphModel.getMixedGraph();

		  interactivePreviewController = Lookup.getDefault().lookup(PreviewController.class);
		  interactivePreviewModel = interactivePreviewController.getModel(interactiveWorkspace);
		  interactiveGraph.writeLock();
		  interactivePreviewModel.getProperties().putValue("MultiColourNode", Boolean.FALSE);
		  interactivePreviewModel.getProperties().putValue(PreviewProperty.NODE_OPACITY, 100);
		  interactiveGraph.writeUnlock();
		  //**previewMouse**        
		  /*mouseRenderer=new Gephi_graph_previewMouseRenderer();
        mouseListener=new Gephi_graph_previewMouseListener();
		   */
	/*	  multiColorRenderer=new MultiColourRenderer();

		  filterController = Lookup.getDefault().lookup(FilterController.class);

		  attributeColumnsController=Lookup.getDefault().lookup(AttributeColumnsController.class);
		  attributeModel=Lookup.getDefault().lookup(AttributeController.class).getModel();
		  */
		  
		  initialize();
	  }

	  public Gephi_graph(ProjectController PC, Project project, Workspace workspace, PreviewModel previewModel)
	  {
		  interactivePC=PC;
		  interactiveProject=project;
		  interactiveWorkspace=workspace;
		  interactiveGraphController=Lookup.getDefault().lookup(GraphController.class);
		  interactiveGraphModel=interactiveGraphController.getModel(interactiveWorkspace);
		  interactiveGraph=interactiveGraphModel.getMixedGraph(); 
		  interactivePreviewController = Lookup.getDefault().lookup(PreviewController.class);
		  interactivePreviewModel=previewModel;
		  interactiveTarget=(ProcessingTarget) interactivePreviewController.getRenderTarget(RenderTarget.PROCESSING_TARGET);
		  interactiveApplet = interactiveTarget.getApplet();
		  attributeColumnsController=Lookup.getDefault().lookup(AttributeColumnsController.class);
		  attributeModel=Lookup.getDefault().lookup(AttributeController.class).getModel(interactiveWorkspace);

		  initialize();
	  }
	  
	  public Gephi_graph(ProjectController PC, Project project, Workspace workspace, JList<String> nodeList) 
	  {
		  interactivePC=PC;
		  interactiveProject=project;
		  interactiveWorkspace=workspace;
		  interactiveGraphController=Lookup.getDefault().lookup(GraphController.class);
		  interactiveGraphModel=interactiveGraphController.getModel(interactiveWorkspace);
		  interactiveGraph=interactiveGraphModel.getMixedGraph(); 
		  interactivePreviewController = Lookup.getDefault().lookup(PreviewController.class);
		  interactivePreviewModel=interactivePreviewController.getModel(interactivePC.getCurrentWorkspace());
		  interactiveTarget=(ProcessingTarget) interactivePreviewController.getRenderTarget(RenderTarget.PROCESSING_TARGET);
		  interactiveApplet = interactiveTarget.getApplet();
		  attributeColumnsController=Lookup.getDefault().lookup(AttributeColumnsController.class);
		  attributeModel=Lookup.getDefault().lookup(AttributeController.class).getModel(interactiveWorkspace);
		  
		  initialize();
		  allNodeList=new ArrayList<Node>();
		  int numElements=nodeList.getModel().getSize();
		  for(int i=0; i<numElements; i++)
		  {
			  Node n=interactiveGraphModel.getGraph().getNode(nodeList.getModel().getElementAt(i));
			  allNodeList.add(n);
		  }
		  System.out.println("allNodeList: "+allNodeList.size());
	  }

	public void resetGraph() 
	  {
		  initialize();
	  }

	  private void initialize()
	  {
		  System.out.println("Gelph initialize");
		  allNodeList=new ArrayList<Node>();
		  prevSelectedNode=null;
		  originalNode=null;
		  prevSelectedEdge=new ArrayList<graphNodeData>();
		  SET_ORIGINAL_NODE=false;

		  hallmarkNode_proliferation=new ArrayList<String>();
		  hallmarkNode_growth_repressor=new ArrayList<String>();
		  hallmarkNode_apoptosis=new ArrayList<String>();
		  hallmarkNode_replicative_immortality=new ArrayList<String>();
		  hallmarkNode_angiogenesis=new ArrayList<String>();
		  hallmarkNode_metastasis=new ArrayList<String>();
		  hallmarkNode_metabolism=new ArrayList<String>();
		  hallmarkNode_immune_destruction=new ArrayList<String>();
		  hallmarkNode_genome_instability=new ArrayList<String>();
		  hallmarkNode_tumor_promoting_inflammation=new ArrayList<String>();

		  HALLMARK_PROLIFERATION_VIEW=false;
		  HALLMARK_GROWTH_REPRESSOR_VIEW=false;
		  HALLMARK_APOPTOSIS_VIEW=false;
		  HALLMARK_REPLICATIVE_IMMORTALITY_VIEW=false;
		  HALLMARK_ANGIOGENESIS_VIEW=false;
		  HALLMARK_METASTASIS_VIEW=false;
		  HALLMARK_METABOLISM_VIEW=false;
		  HALLMARK_IMMUNE_DESTRUCTION_VIEW=false;
		  HALLMARK_GENOME_INSTABILITY_VIEW=false;
		  HALLMARK_TUMOR_PROMOTING_INFLAMMATION_VIEW=false;
		  
		  interactiveGraph.writeLock();
		  interactivePreviewModel.getProperties().putValue("MultiColourNode", Boolean.FALSE);
		  interactivePreviewModel.getProperties().putValue(PreviewProperty.NODE_OPACITY, 100);
		  attributeColumnsController.addAttributeColumn(attributeModel.getEdgeTable(), GEPHI_WEIGHT, AttributeType.INT);
		  attributeColumnsController.addAttributeColumn(attributeModel.getNodeTable(), GEPHI_COLOR_LIST, AttributeType.STRING);
		  interactiveGraph.writeUnlock();
	  }

	  public Graph getGraph()
	  {
		  return interactiveGraph;
	  }
	  
	  public void refreshWorkspace(Workspace w)
	  {
		  interactivePreviewController.refreshPreview(w);
	  }
	  
	  public Workspace getWorkspace()
	  {
		  return interactiveWorkspace;
	  }

	  public void layoutInteractiveGraph_yifanhu()
	  {
		  System.out.println("doing interactive layout (yifanhu)");
		  YifanHu yifanHuLayoutBuilder=Lookup.getDefault().lookup(YifanHu.class);
		  YifanHuLayout layout=yifanHuLayoutBuilder.buildLayout();
		  layout.resetPropertiesValues();
		  layout.setGraphModel(interactiveGraphModel);
		  interactiveLayoutController=Lookup.getDefault().lookup(LayoutController.class);
		  interactiveLayoutController.setLayout(layout);
		  interactiveLayoutController.executeLayout();

		  System.out.println("interactive layout done");
	  }

	  public void layoutInteractiveGraph_noverlap()
	  {
		  System.out.println("doing interactive layout (noverlap)");
		  NoverlapLayoutBuilder noverlapLayoutBuilder=Lookup.getDefault().lookup(NoverlapLayoutBuilder.class);
		  NoverlapLayout layout=(NoverlapLayout) noverlapLayoutBuilder.buildLayout();
		  layout.resetPropertiesValues();
		  layout.setGraphModel(interactiveGraphModel);
		  interactiveLayoutController=Lookup.getDefault().lookup(LayoutController.class);
		  interactiveLayoutController.setLayout(layout);
		  interactiveLayoutController.executeLayout();
		  System.out.println("interactive layout done");
	  }



	  public void layoutInteractiveGraph_fruchtermanReingold()
	  {
		  System.out.println("doing interactive layout (fruchtermanReingold)");
		  FruchtermanReingoldBuilder fruchtermanReingoldLayoutBuilder=Lookup.getDefault().lookup(FruchtermanReingoldBuilder.class);
		  FruchtermanReingold layout=fruchtermanReingoldLayoutBuilder.buildLayout();
		  layout.setGraphModel(interactiveGraphModel);
		  layout.resetPropertiesValues();
		  interactiveLayoutController=Lookup.getDefault().lookup(LayoutController.class);
		  interactiveLayoutController.setLayout(layout);
		  interactiveLayoutController.executeLayout();
		  System.out.println("interactive layout done");
	  }

	  public void layoutInteractiveGraph_forceAtlasTwo()
	  {
		  System.out.println("doing interactive layout (forceAtlasTwo)");
		  ForceAtlas2Builder forceAtlas2LayoutBuilder=Lookup.getDefault().lookup(ForceAtlas2Builder.class);
		  ForceAtlas2 layout=forceAtlas2LayoutBuilder.buildLayout();
		  layout.resetPropertiesValues();
		  layout.setGravity(Double.valueOf(1));
		  layout.setAdjustSizes(true);
		  layout.setGraphModel(interactiveGraphModel);
		  interactiveLayoutController=Lookup.getDefault().lookup(LayoutController.class);
		  interactiveLayoutController.setLayout(layout);
		  interactiveLayoutController.executeLayout();
		  System.out.println("interactive layout done");
	  }

	  public void layoutInteractiveGraph_labelAdjust()
	  {
		  System.out.println("doing interactive layout (label adjust)");
		  LabelAdjustBuilder labelAdjustLayoutBuilder=Lookup.getDefault().lookup(LabelAdjustBuilder.class);
		  LabelAdjust layout=labelAdjustLayoutBuilder.buildLayout();
		  layout.resetPropertiesValues();
		  layout.setAdjustBySize(true);
		  layout.setGraphModel(interactiveGraphModel);
		  interactiveLayoutController=Lookup.getDefault().lookup(LayoutController.class);
		  interactiveLayoutController.setLayout(layout);
		  interactiveLayoutController.executeLayout();
		  System.out.println("interactive layout done");
	  }

	  public void layoutInteractiveGraph_forceAtlas()
	  {
		  System.out.println("doing interactive layout (force atlas)");
		  ForceAtlas forceAtlasLayoutBuilder=Lookup.getDefault().lookup(ForceAtlas.class);
		  ForceAtlasLayout layout=forceAtlasLayoutBuilder.buildLayout();
		  layout.resetPropertiesValues();
		  layout.setAdjustSizes(true);
		  //layout.setGravity(Double.valueOf(10));
		  layout.setGraphModel(interactiveGraphModel);
		  interactiveLayoutController=Lookup.getDefault().lookup(LayoutController.class);
		  interactiveLayoutController.setLayout(layout);
		  interactiveLayoutController.executeLayout();
		  System.out.println("interactive layout done");
	  }

	  public void configureInteractivePreview()
	  {
		  //Preview configuration
		  interactiveGraph.writeLock();
		  interactivePreviewModel.getProperties().putValue(PreviewProperty.SHOW_NODE_LABELS, Boolean.TRUE);
		  interactivePreviewModel.getProperties().putValue(PreviewProperty.NODE_LABEL_COLOR, new DependantOriginalColor(Color.WHITE));
		  interactivePreviewModel.getProperties().putValue(PreviewProperty.EDGE_CURVED, Boolean.FALSE);
		  interactivePreviewModel.getProperties().putValue(PreviewProperty.EDGE_OPACITY, 50);
		  interactivePreviewModel.getProperties().putValue(PreviewProperty.EDGE_RADIUS, 0.01f);
		  interactivePreviewModel.getProperties().putValue(PreviewProperty.BACKGROUND_COLOR, Color.BLACK);
		  interactivePreviewModel.getProperties().putValue(PreviewProperty.DIRECTED, Boolean.TRUE);
		  interactivePreviewModel.getProperties().putValue(PreviewProperty.NODE_BORDER_WIDTH, 0.01);
		  //interactivePreviewModel.getProperties().putValue(PreviewProperty.VISIBILITY_RATIO, 50);
		  interactivePreviewModel.getProperties().putValue(PreviewProperty.ARROW_SIZE, 5);
		  interactiveGraph.writeUnlock();
		  interactivePreviewController.refreshPreview();
		  System.out.println("current workspace="+interactivePC.getCurrentWorkspace());
		  System.out.println("interactive preview done");
	  }

	  public void destroyGraph()
	  {
		  interactiveGraph.writeLock();
		  interactiveGraph.clear();
		  interactiveGraph.writeUnlock();
	  }

	  public void resizeGraph(JPanel interactiveGraphPanel)
	  {
		  interactiveGraph.writeLock();
		  interactiveApplet.setPreferredSize(interactiveGraphPanel.getPreferredSize());
		  //interactiveApplet.setSize(interactiveGraphPanel.getPreferredSize());
		  interactiveGraph.writeUnlock();
		   
		  //Refresh the preview and reset the zoom
		  interactivePreviewController.refreshPreview();
		  interactiveGraphPanel.repaint();
		  interactiveTarget.refresh();
		  interactiveTarget.resetZoom();
	  }
	  
	  public void visualizeGraph(JPanel interactiveGraphPanel)
	  {
		  //New Processing target, get the PApplet
		  //interactiveTarget = (ProcessingTarget) interactivePreviewController.getRenderTarget(RenderTarget.PROCESSING_TARGET);
		  interactiveGraph.writeLock();
		  interactiveApplet.init();
		  interactiveApplet.setPreferredSize(interactiveGraphPanel.getPreferredSize());
		  //interactiveApplet.setSize(interactiveGraphPanel.getPreferredSize());
		  interactiveApplet.setLocation(interactiveGraphPanel.getLocation());
		  interactiveGraph.writeUnlock();
		  interactiveGraphPanel.add(interactiveApplet, BorderLayout.CENTER);
			 
		  //**previewMouse**
		  /*interactiveApplet.addMouseListener(new MouseAdapter() {
			@Override 
			public void mouseClicked(MouseEvent e){
				System.out.println("interactiveApplet caught mouse event");
				PreviewMouseEvent pe=new PreviewMouseEvent(e.getXOnScreen(), e.getYOnScreen(), PreviewMouseEvent.Type.CLICKED, PreviewMouseEvent.Button.LEFT, null);
				mouseListener.mouseClicked(pe, interactivePreviewModel.getProperties(), interactiveWorkspace);
			}
		});*/
		  System.out.println("interactiveApplet done");

		  //**previewMouse**


		  /*mouseRenderer.needsPreviewMouseListener(mouseListener);
        mr2[mr.length] = new ManagedRenderer(mouseRenderer, true);
        interactivePreviewModel.setManagedRenderers(mr2);
		   */

		  //Refresh the preview and reset the zoom
		  interactivePreviewController.refreshPreview();
		  interactiveGraphPanel.repaint();
		  interactiveTarget.refresh();
		  interactiveTarget.resetZoom();
		  //viewMain=interactiveGraphModel.getVisibleView();
		  Dimension d=interactiveApplet.getSize();
		  System.out.println("interactiveApplet width="+d.getWidth()+" height="+d.getHeight());
	  }

	  public void visualizeGraphNoSetLocation(JPanel interactiveGraphPanel, boolean SETLOCATION)
	  {
		  //New Processing target, get the PApplet
		  //interactiveTarget = (ProcessingTarget) interactivePreviewController.getRenderTarget(RenderTarget.PROCESSING_TARGET);
		  interactiveGraphPanel.add(interactiveApplet, BorderLayout.CENTER);
		  interactiveGraph.writeLock();
		  interactiveApplet.init();
		  interactiveApplet.setPreferredSize(new Dimension(interactiveGraphPanel.getPreferredSize().width+800, interactiveGraphPanel.getPreferredSize().height));
		  //interactiveApplet.setSize(interactiveGraphPanel.getPreferredSize());
		  if(SETLOCATION==true)
			  interactiveApplet.setLocation(interactiveGraphPanel.getLocation());
		  interactiveGraph.writeUnlock();
		  //**previewMouse**
		  /*interactiveApplet.addMouseListener(new MouseAdapter() {
			@Override 
			public void mouseClicked(MouseEvent e){
				System.out.println("interactiveApplet caught mouse event");
				PreviewMouseEvent pe=new PreviewMouseEvent(e.getXOnScreen(), e.getYOnScreen(), PreviewMouseEvent.Type.CLICKED, PreviewMouseEvent.Button.LEFT, null);
				mouseListener.mouseClicked(pe, interactivePreviewModel.getProperties(), interactiveWorkspace);
			}
		});*/
		  System.out.println("interactiveApplet done");

		  //**previewMouse**


		  /*mouseRenderer.needsPreviewMouseListener(mouseListener);
        mr2[mr.length] = new ManagedRenderer(mouseRenderer, true);
        interactivePreviewModel.setManagedRenderers(mr2);
		   */

		  //Refresh the preview and reset the zoom
		  interactivePreviewController.refreshPreview();
		  interactiveGraphPanel.repaint();
		  interactiveTarget.refresh();
		  interactiveTarget.resetZoom();
		  //viewMain=interactiveGraphModel.getVisibleView();
		  Dimension d=interactiveApplet.getSize();
		  System.out.println("interactiveApplet width="+d.getWidth()+" height="+d.getHeight());
	  }
	  
	  public boolean addANode(String name, String nodeSize)
	  {
		  Node n1=interactiveGraphModel.getGraph().getNode(name);
		  if(n1==null)
		  {
			  interactiveGraph.writeLock();
			  n1=interactiveGraphModel.factory().newNode(name);
			  n1.getNodeData().setLabel(name);
			  if(nodeSize.compareTo(strConstants.getGephiSmall())==0)
				  n1.getNodeData().setSize(5);
			  else if(nodeSize.compareTo(strConstants.getGephiMedium())==0)
				  n1.getNodeData().setSize(7);
			  else if(nodeSize.compareTo(strConstants.getGephiLarge())==0)
				  n1.getNodeData().setSize(10);
			  else if(nodeSize.compareTo(strConstants.getGephiExtraLarge())==0)
				  n1.getNodeData().setSize(20);
			  if(interactiveGraph.contains(n1)==false)
			  {
				  System.out.println("node added: "+name);
				  interactiveGraph.addNode(n1);
				  allNodeList.add(n1);
			  }
			  interactiveGraph.writeUnlock();

			  //System.out.println("added node "+name);
			  return true;			
		  }
		  return false;
	  }

	  public boolean addANodeAt(String name, float x, float y, float z)
	  {
		  Node n1=interactiveGraphModel.getGraph().getNode(name);
		  if(n1==null)
		  {
			  interactiveGraph.writeLock();
			  n1=interactiveGraphModel.factory().newNode(name);
			  n1.getNodeData().setLabel(name);
			  n1.getNodeData().setSize(30);
			  n1.getNodeData().setX(x);
			  n1.getNodeData().setY(y);
			  n1.getNodeData().setZ(z);
			  //n1.getNodeData().setZ(100);
			  interactiveGraph.addNode(n1);
			  allNodeList.add(n1);
			  interactiveGraph.writeUnlock();

			  //System.out.println("added node "+name);
			  return true;			
		  }
		  return false;
	  }

	  public ArrayList<String> getNodeNeighbour(String node)
	  {
		  Node n1=interactiveGraphModel.getGraph().getNode(node);
		  ArrayList<String> neighbour=new ArrayList<String>();
		  if(n1!=null)
		  {
			  interactiveGraph.writeLock();
			  for(Node n:interactiveGraphModel.getGraph().getNeighbors(n1))
				  neighbour.add(n.getNodeData().getLabel());
			  interactiveGraph.writeUnlock();
		  }
		  System.out.println("node:"+node);
		  System.out.println("neighbour:"+neighbour.toString());
		  return neighbour;
	  }

	  public void removeNodesNotInList(JLabel status, ArrayList<String> nodeList)
	  {
		  boolean RESET_ORIGINAL_NODE=false;
		  ArrayList<String> complementaryNodeList=new ArrayList<String>();
		  for(Node n : interactiveGraph.getNodes()) {
			  if(nodeList.contains(n.getNodeData().getLabel())==false)
				  complementaryNodeList.add(n.getNodeData().getLabel());
		  }
		  float percentComplete;
		  int totalCount=complementaryNodeList.size();

		  for(int i=0; i<complementaryNodeList.size(); i++)
		  {
			  Node n1=interactiveGraphModel.getGraph().getNode(complementaryNodeList.get(i));
			  if(n1!=null)
			  {
				  interactiveGraph.writeLock();
				  interactiveGraph.removeNode(n1);
				  allNodeList.remove(n1);
				  if(originalNode!=null && originalNode.getNode().compareTo(complementaryNodeList.get(i))==0)
					  RESET_ORIGINAL_NODE=true;
				  percentComplete=(float)(100.0*i/(totalCount-1));
				  DecimalFormat df = new DecimalFormat();
				  df.setMaximumFractionDigits(2);
				  status.setText("removing nodes from gephi graph... ["+df.format(percentComplete)+"%] ");
				  interactiveGraph.writeUnlock();
			  }
		  }
		  if(RESET_ORIGINAL_NODE==true)
			  setOriginalNode(allNodeList.get(0).getNodeData().getId());
	  }

	  public void updateNodeSize(String name, String nodeSize)
	  {
		  interactiveGraph.writeLock();
		  Node n1=interactiveGraphModel.getGraph().getNode(name);
		  if(nodeSize.compareTo(strConstants.getGephiSmall())==0)
			  n1.getNodeData().setSize(5);
		  else if(nodeSize.compareTo(strConstants.getGephiMedium())==0)
			  n1.getNodeData().setSize(10);
		  else if(nodeSize.compareTo(strConstants.getGephiLarge())==0)
			  n1.getNodeData().setSize(15);
		  interactiveGraph.writeUnlock();
	  }

	  public void addADirectedEdge(String source, String target)
	  {
		  //System.out.println("directed edge "+source+"->"+target);
		  interactiveGraph.writeLock();
		  Node s=interactiveGraph.getNode(source);
		  Node t=interactiveGraph.getNode(target);
		  Edge e=interactiveGraphModel.factory().newEdge(s, t, 1f, Boolean.TRUE);
		  if(interactiveGraph.contains(e)==false)
		  {
			  System.out.println("edge added: "+source+"->"+target);
			  interactiveGraph.addEdge(e);
		  }
		  interactiveGraph.writeUnlock();
	  }

	  public void addAnUndirectedEdge(String source, String target)
	  {
		  interactiveGraph.writeLock();
		  Node s=interactiveGraph.getNode(source);
		  Node t=interactiveGraph.getNode(target);
		  Edge e=interactiveGraphModel.factory().newEdge(s, t, 0.1f, Boolean.FALSE);
		  if(interactiveGraph.contains(e)==false)
		  {
			  System.out.println("edge added: "+source+"-"+target);
			  interactiveGraph.addEdge(e);
		  }
		  interactiveGraph.writeUnlock();
	  }

	  public void refreshApplet()
	  {
		  interactivePreviewController.refreshPreview();
		  interactiveApplet.redraw();
		  interactiveApplet.repaint();
	  }

	  private boolean isCurrentSelectedHallmarkNodes(String node)
	  {
		  if(HALLMARK_PROLIFERATION_VIEW==true && hallmarkNode_proliferation.contains(node))
			  return true;
		  if(HALLMARK_GROWTH_REPRESSOR_VIEW==true && hallmarkNode_growth_repressor.contains(node))
			  return true;
		  if(HALLMARK_APOPTOSIS_VIEW==true && hallmarkNode_apoptosis.contains(node))
			  return true;
		  if(HALLMARK_REPLICATIVE_IMMORTALITY_VIEW==true && hallmarkNode_replicative_immortality.contains(node))
			  return true;
		  if(HALLMARK_ANGIOGENESIS_VIEW==true && hallmarkNode_angiogenesis.contains(node))
			  return true;
		  if(HALLMARK_METASTASIS_VIEW==true && hallmarkNode_metastasis.contains(node))
			  return true;
		  if(HALLMARK_METABOLISM_VIEW==true && hallmarkNode_metabolism.contains(node))
			  return true;
		  if(HALLMARK_IMMUNE_DESTRUCTION_VIEW==true && hallmarkNode_immune_destruction.contains(node))
			  return true;
		  if(HALLMARK_GENOME_INSTABILITY_VIEW==true && hallmarkNode_genome_instability.contains(node))
			  return true;
		  if(HALLMARK_TUMOR_PROMOTING_INFLAMMATION_VIEW==true && hallmarkNode_tumor_promoting_inflammation.contains(node))
			  return true;

		  return false;
	  }

	  public void restoreNodes()
	  {
		  System.out.println("nodesToRestore:"+nodesToRestore.size());
		  for(int i=0; i<nodesToRestore.size(); i++)
		  {
			  Node n=interactiveGraph.getNode(nodesToRestore.getNodeName(i));
			  if(n!=null)
			  {
				  n.getNodeData().setSize(nodesToRestore.getNodeSize(i));
				  n.getNodeData().setR(nodesToRestore.getNodeR(i));
				  n.getNodeData().setG(nodesToRestore.getNodeG(i));
				  n.getNodeData().setB(nodesToRestore.getNodeB(i));
				  n.getNodeData().setAlpha(nodesToRestore.getNodeAlpha(i));
			  }
		  }
		  nodesToRestore=new gephiNode();
		  refreshApplet();
	  }

	  public void colorAndResizeSelectedNode(String node, boolean forceRecolor)
	  {
		  System.out.println("colorAndResizeSelectedNode");

		  //if(prevSelectedNode==null || prevSelectedNode.getNode().compareTo(node)==0 || forceRecolor==true)
		  //{
		  System.out.println("Selected "+node);
		  //reset the edge selection (if any) of previously selected node 
		  if(prevSelectedEdge!=null && prevSelectedEdge.size()>0)
		  {
			  Node gSource=interactiveGraph.getNode(prevSelectedEdge.get(0).getNode());
			  Node gTarget=interactiveGraph.getNode(prevSelectedEdge.get(1).getNode());

			  if(gSource!=null && gTarget!=null)
			  {
				  interactiveGraph.writeLock();
				  //reset prev edge sourceNode color and size
				  gSource.getNodeData().setColor(prevSelectedEdge.get(0).getR(), prevSelectedEdge.get(0).getG(), prevSelectedEdge.get(0).getB());
				  gSource.getNodeData().setSize(prevSelectedEdge.get(0).getSize());
				  //reset prev edge targetNode color and size
				  gTarget.getNodeData().setColor(prevSelectedEdge.get(1).getR(), prevSelectedEdge.get(1).getG(), prevSelectedEdge.get(1).getB());
				  gTarget.getNodeData().setSize(prevSelectedEdge.get(1).getSize());
				  //reset prev edge weight to 1
				  Edge[] edgeArr=new Edge[1];
				  edgeArr[0]=interactiveGraph.getEdge(gSource, gTarget);
				  attributeColumnsController.fillEdgesColumnWithValue(edgeArr, attributeModel.getEdgeTable().getColumn(GEPHI_WEIGHT), "1");
				  prevSelectedEdge=new ArrayList<graphNodeData>();
				  interactiveGraph.writeUnlock();
			  }
		  }
		  
		  if(prevSelectedNode!=null)
		  {
			  //reset prevSelectedNode if its still present in this current graph.
			  if(interactiveGraph.getNode(prevSelectedNode.getNode())!=null)
			  {
				  interactiveGraph.writeLock();
				  if(isCurrentSelectedHallmarkNodes(prevSelectedNode.getNode())==false)
				  {
					  interactiveGraph.getNode(prevSelectedNode.getNode()).getNodeData().setColor(prevSelectedNode.getR(), prevSelectedNode.getG(), prevSelectedNode.getB());
					  interactiveGraph.getNode(prevSelectedNode.getNode()).getNodeData().setSize(prevSelectedNode.getSize());
					  interactiveGraph.getNode(prevSelectedNode.getNode()).getNodeData().setAlpha(prevSelectedNode.getAlpha());
				  }
				  else //currently in view hallmark mode and prevSelectedNode is one of the hallmark nodes being viewed, so reset the alpha value to 0.5f
					  interactiveGraph.getNode(prevSelectedNode.getNode()).getNodeData().setAlpha(0.5f);
				  interactiveGraph.writeUnlock();
			  }
		  }

		  if(interactiveGraph.getNode(node)!=null)
			  prevSelectedNode=new graphNodeData(node, interactiveGraph.getNode(node).getNodeData().r(), interactiveGraph.getNode(node).getNodeData().g(), 
					  interactiveGraph.getNode(node).getNodeData().b(), interactiveGraph.getNode(node).getNodeData().alpha(),
					  interactiveGraph.getNode(node).getNodeData().getSize(), interactiveGraph.getNode(node).getNodeData().getLabel());
		  setOriginalNode(node);
		  interactiveGraph.writeLock();
		  interactiveGraph.getNode(node).getNodeData().setColor(1.0f, 1.0f, 0.0f); //purple
		  interactiveGraph.getNode(node).getNodeData().setAlpha(1.0f); //opaque
		  if(interactiveGraph.getNode(node).getNodeData().getSize()<30.0f)
			  interactiveGraph.getNode(node).getNodeData().setSize(30.0f);
		  interactiveGraph.writeUnlock();
		  float x;
		  float y;
		  float z;
		  x=interactiveGraph.getNode(node).getNodeData().x();
		  y=interactiveGraph.getNode(node).getNodeData().y();
		  z=interactiveGraph.getNode(node).getNodeData().z();
		  System.out.println(node+" position: x="+x+" y="+y+" z="+z);
		  //interactiveGraphModel.getGraph().getNode(node).getNodeData().setColor(1,1,0);
		  //remove old pin
		  //if(OLD_PIN_EXISTS==true)
		  //{
			//  Node n1=interactiveGraphModel.getGraph().getNode("*");
			 // if(n1!=null)
			  //{
			//	  interactiveGraph.writeLock();
			//	  interactiveGraph.removeNode(n1);
			//	  interactiveGraph.writeUnlock();
			//  }
		  //}
		  //addANodeAt("*", x, y, 0);
		  //interactiveGraphModel.getGraph().getNode("*").getNodeData().setColor(1,1,0);
		  //OLD_PIN_EXISTS=true;

		  refreshApplet();
		  //}
	  }

	  public void resetColorAndSizeOfNode()
	  {
		  System.out.println("resetColorAndSizeOfNode");

		  interactiveGraph.writeLock();
		  for(Node n : interactiveGraph.getNodes()) 
		  {
			  if(prevSelectedNode==null ||
					  (prevSelectedNode!=null && prevSelectedNode.getNode().compareTo(n.getNodeData().getLabel())!=0))
			  {
				  n.getNodeData().setColor(originalNode.getR(), originalNode.getG(), originalNode.getB());
				  n.getNodeData().setSize(originalNode.getSize());
			  }
		  }
		  interactiveGraph.writeUnlock();
		  refreshApplet();
	  }

	  private void colorAndResizeFoldChange(ArrayList<Double> nodeValue, int i, String node)
	  {
		  interactiveGraph.writeLock();
		  if(nodeValue.get(i)>0)
			  interactiveGraph.getNode(node).getNodeData().setColor(0.0f, 1.0f, 0.0f); //green
		  else if(nodeValue.get(i)<0)
			  interactiveGraph.getNode(node).getNodeData().setColor(1.0f, 0.0f, 0.0f); //red
		  interactiveGraph.getNode(node).getNodeData().setAlpha(1.0f); //opaque
		  Float original_size=interactiveGraph.getNode(node).getNodeData().getSize();
		  Double exp=nodeValue.get(i);
		  Double value=Math.pow(2, exp);
		  if(value<1.0f)
			  value=1/value;
		  //System.out.println(nodeList.get(i)+" "+nodeValue.get(i)+" "+value);
		  Float size=(float)value.doubleValue()*2*original_size/5;
		  interactiveGraph.getNode(node).getNodeData().setSize(size);
		  interactiveGraph.writeUnlock();
	  }

	  private void colorAndResizeMutation(ArrayList<Double> nodeValue, int i, String node)
	  {
		  float scale=2;

		  interactiveGraph.writeLock();
		  if(nodeValue.get(i)>0)
			  interactiveGraph.getNode(node).getNodeData().setColor(0.0f, 1.0f, 0.0f); //green
		  else if(nodeValue.get(i)<0)
			  interactiveGraph.getNode(node).getNodeData().setColor(1.0f, 0.0f, 0.0f); //red
		  interactiveGraph.getNode(node).getNodeData().setAlpha(1.0f); //opaque
		  Double value=nodeValue.get(i);
		  Float size=(float)value.doubleValue()*scale;
		  interactiveGraph.getNode(node).getNodeData().setSize(size);
		  interactiveGraph.writeUnlock();
	  }

	  private void colorAndResizeDiseaseNode(String node)
	  {
		  interactiveGraph.writeLock();
		  Node n=interactiveGraph.getNode(node);
		  System.out.println("node n:"+n+" "+node);
		  nodesToRestore.addNode(node, n.getNodeData().getSize(), n.getNodeData().r(), n.getNodeData().g(), n.getNodeData().b(), n.getNodeData().alpha());
		  n.getNodeData().setColor(1.0f, 0.0f, 0.0f); //red
		  n.getNodeData().setAlpha(1.0f); //opaque
		  //Float size=n.getNodeData().getSize()*2;
		  n.getNodeData().setSize(30);
		  interactiveGraph.writeUnlock();
	  }

	  private void colorAndResizeTargetNode(String node)
	  {
		  interactiveGraph.writeLock();
		  Node n=interactiveGraph.getNode(node);
		  nodesToRestore.addNode(node, n.getNodeData().getSize(), n.getNodeData().r(), n.getNodeData().g(), n.getNodeData().b(), n.getNodeData().alpha());
		  n.getNodeData().setColor(0.0f, 1.0f, 0.0f); //green
		  n.getNodeData().setAlpha(1.0f); //opaque
		  //Float size=n.getNodeData().getSize()*2;
		  n.getNodeData().setSize(30);
		  interactiveGraph.writeUnlock();
	  }

	  public void colorAndResizeNodeSet(ArrayList<String> nodeList, ArrayList<Double> nodeValue, String colorAndResizeType)
	  {
		  System.out.println("colorAndResizeNodeSet");
		  System.out.println("nodeList:"+nodeList.toString());
		  if(nodeValue!=null)
			  System.out.println("nodeValue:"+nodeValue.toString());
		  else
			  System.out.println("nodeValue is NULL!");

		  if(nodeValue==null && colorAndResizeType.compareTo("Display Disease Node")==0)
		  {
			  restoreNodes();
			  for(int i=0; i<nodeList.size(); i++)
			  {
				  String node=nodeList.get(i);
				  System.out.println(i+"-> "+node);
				  setOriginalNode(node);
				  colorAndResizeDiseaseNode(node);
			  }
		  }
		  else if(nodeValue==null && colorAndResizeType.compareTo("Display Target Node")==0)
		  {
			  restoreNodes();
			  for(int i=0; i<nodeList.size(); i++)
			  {
				  String node=nodeList.get(i);
				  setOriginalNode(node);
				  colorAndResizeTargetNode(node);
			  }
		  }
		  else if(nodeValue.size()>0)
		  {
			  for(int i=0; i<nodeList.size(); i++)
			  {
				  String node=nodeList.get(i);
				  setOriginalNode(node);
				  if(prevSelectedNode!=null)
				  {
					  if(prevSelectedNode.getNode().compareTo(node)!=0 && interactiveGraph.getNode(node)!=null)
					  {
						  if(colorAndResizeType.compareTo("Display Fold Change")==0)
							  colorAndResizeFoldChange(nodeValue, i, node);
						  else 
							  colorAndResizeMutation(nodeValue, i, node);
					  }
				  }
				  else if(interactiveGraph.getNode(node)!=null)
				  {
					  if(colorAndResizeType.compareTo("Display Fold Change")==0)
						  colorAndResizeFoldChange(nodeValue, i, node);
					  else 
						  colorAndResizeMutation(nodeValue, i, node);
				  }
			  }
		  }
		  refreshApplet();
	  }

	  public void colorPathwayNode(ArrayList<String> nodeList, boolean ALTCOLOR, int pathwayIndex)
	  {
		  interactiveGraph.writeLock();
		  restoreNodes();
		  for(int i=0; i<nodeList.size(); i++)
		  {
			  System.out.println("colorPathwayNode node:"+nodeList.get(i));
			  Node n=interactiveGraph.getNode(nodeList.get(i));
			  nodesToRestore.addNode(nodeList.get(i), n.getNodeData().getSize(), n.getNodeData().r(), n.getNodeData().g(), n.getNodeData().b(), n.getNodeData().alpha());
			  if(pathwayIndex==0)
			  {
				  if(ALTCOLOR==false)
					  n.getNodeData().setColor(0.8f, 0.8f, 1f); //dark purple
				  else
					  n.getNodeData().setColor(0f, 0f, 1f); //cyan
			  }
			  else
			  {
				  float currR, currG, currB;
				  currR=n.getNodeData().r();
				  currG=n.getNodeData().g();
				  currB=n.getNodeData().b();
				  if(currR==0.8f && currG==0.8f && currB==1f)
					  n.getNodeData().setColor(0f, 0.6f, 1f); //blue 
				  else
					  n.getNodeData().setColor(0f, 1f, 0f);//green 
			  }
		  }
		  interactiveGraph.writeUnlock();
		  refreshApplet();
	  }

	  public void colorEdgeDynamics(String sourceNode, String targetNode, String edgeWeight)
	  {
		  Node gSource, gTarget;
		  Edge[] edgeArr=new Edge[1];
		  gSource=interactiveGraph.getNode(sourceNode);
		  gTarget=interactiveGraph.getNode(targetNode);
		  if(gSource!=null && gTarget!=null)
		  {
			  edgeArr[0]=interactiveGraph.getEdge(gSource, gTarget);
			  if(edgeArr[0]!=null)
			  {
				  System.out.println("colorEdgeDynamics");
				  interactiveGraph.writeLock();
				  interactiveGraph.getNode(sourceNode).getNodeData().setColor(0.0f, 1.0f, 1.0f); //cyan
				  interactiveGraph.getNode(sourceNode).getNodeData().setSize(25.0f);
				  interactiveGraph.getNode(targetNode).getNodeData().setColor(0.0f, 1.0f, 1.0f); //cyan
				  interactiveGraph.getNode(targetNode).getNodeData().setSize(25.0f);
				  attributeColumnsController.fillEdgesColumnWithValue(edgeArr, attributeModel.getEdgeTable().getColumn(GEPHI_WEIGHT), edgeWeight);
				  interactiveGraph.writeUnlock();
				  refreshApplet();
			  }
		  }
	  }

	  public void colorSelectedEdge(String sourceNode, String targetNode, String edgeWeight)
	  {
		  if(prevSelectedNode!=null)
		  {
			  //reset prevSelectedNode if its still present in this current graph.
			  if(interactiveGraph.getNode(prevSelectedNode.getNode())!=null)
			  {
				  interactiveGraph.writeLock();
				  if(isCurrentSelectedHallmarkNodes(prevSelectedNode.getNode())==false)
				  {
					  interactiveGraph.getNode(prevSelectedNode.getNode()).getNodeData().setColor(prevSelectedNode.getR(), prevSelectedNode.getG(), prevSelectedNode.getB());
					  interactiveGraph.getNode(prevSelectedNode.getNode()).getNodeData().setSize(prevSelectedNode.getSize());
					  interactiveGraph.getNode(prevSelectedNode.getNode()).getNodeData().setAlpha(prevSelectedNode.getAlpha());
				  }
				  else //currently in view hallmark mode and prevSelectedNode is one of the hallmark nodes being viewed, so reset the alpha value to 0.5f
					  interactiveGraph.getNode(prevSelectedNode.getNode()).getNodeData().setAlpha(0.5f);
				  interactiveGraph.writeUnlock();
			  }
		  }
		  
		  if(prevSelectedEdge==null || prevSelectedEdge.size()==0 || 
				  !(prevSelectedEdge.get(0).getNode().compareTo(sourceNode)==0 && prevSelectedEdge.get(1).getNode().compareTo(targetNode)==0))
		  {
			  Edge[] edgeArr=new Edge[1];
			  Node gSource, gTarget;
			  graphNodeData source, target;

			  System.out.println("Selected edge ("+sourceNode+","+targetNode+")");
			  if(prevSelectedEdge!=null && prevSelectedEdge.size()>0)
			  {
				  gSource=interactiveGraph.getNode(prevSelectedEdge.get(0).getNode());
				  gTarget=interactiveGraph.getNode(prevSelectedEdge.get(1).getNode());

				  if(gSource!=null && gTarget!=null)
				  {
					  interactiveGraph.writeLock();
					  //reset prev edge sourceNode color and size
					  gSource.getNodeData().setColor(prevSelectedEdge.get(0).getR(), prevSelectedEdge.get(0).getG(), prevSelectedEdge.get(0).getB());
					  gSource.getNodeData().setSize(prevSelectedEdge.get(0).getSize());
					  //reset prev edge targetNode color and size
					  gTarget.getNodeData().setColor(prevSelectedEdge.get(1).getR(), prevSelectedEdge.get(1).getG(), prevSelectedEdge.get(1).getB());
					  gTarget.getNodeData().setSize(prevSelectedEdge.get(1).getSize());
					  //reset prev edge weight to 1
					  edgeArr[0]=interactiveGraph.getEdge(gSource, gTarget);
   					  attributeColumnsController.fillEdgesColumnWithValue(edgeArr, attributeModel.getEdgeTable().getColumn(GEPHI_WEIGHT), "1");

					  interactiveGraph.writeUnlock();
				  }
			  }
			  prevSelectedEdge=new ArrayList<graphNodeData>();
			  if(prevSelectedNode==null || prevSelectedNode.getNode().compareTo(sourceNode)!=0)
			  {
				  source=new graphNodeData(sourceNode, interactiveGraph.getNode(sourceNode).getNodeData().r(), 
						  interactiveGraph.getNode(sourceNode).getNodeData().g(), interactiveGraph.getNode(sourceNode).getNodeData().b(),
						  interactiveGraph.getNode(sourceNode).getNodeData().alpha(), interactiveGraph.getNode(sourceNode).getNodeData().getSize(),
						  interactiveGraph.getNode(sourceNode).getNodeData().getLabel());
			  }
			  else
				  source=prevSelectedNode;
			  if(prevSelectedNode==null || prevSelectedNode.getNode().compareTo(targetNode)!=0)
			  {
				  target=new graphNodeData(targetNode, interactiveGraph.getNode(targetNode).getNodeData().r(), 
						  interactiveGraph.getNode(targetNode).getNodeData().g(), interactiveGraph.getNode(targetNode).getNodeData().b(),
						  interactiveGraph.getNode(targetNode).getNodeData().alpha(), interactiveGraph.getNode(targetNode).getNodeData().getSize(),
						  interactiveGraph.getNode(targetNode).getNodeData().getLabel());
			  }
			  else
				  target=prevSelectedNode;

			  prevSelectedEdge.add(source);
			  prevSelectedEdge.add(target);
			  setOriginalNode(sourceNode);

			  interactiveGraph.writeLock();
			  interactiveGraph.getNode(sourceNode).getNodeData().setColor(0.0f, 1.0f, 1.0f); //cyan
			  interactiveGraph.getNode(sourceNode).getNodeData().setSize(25.0f);
			  interactiveGraph.getNode(targetNode).getNodeData().setColor(0.0f, 1.0f, 1.0f); //cyan
			  interactiveGraph.getNode(targetNode).getNodeData().setSize(25.0f);
			  gSource=interactiveGraph.getNode(sourceNode);
			  gTarget=interactiveGraph.getNode(targetNode);
			  edgeArr[0]=interactiveGraph.getEdge(gSource, gTarget);
			  attributeColumnsController.fillEdgesColumnWithValue(edgeArr, attributeModel.getEdgeTable().getColumn(GEPHI_WEIGHT), edgeWeight);
			  interactiveGraph.writeUnlock();
			  refreshApplet();
		  }
	  }

	  public void colorAndResizeEdge(String source, String target)
	  {
		  interactiveGraph.writeLock();
		  Node sourceNode=interactiveGraph.getNode(source);
		  Node targetNode=interactiveGraph.getNode(target);
		  interactiveGraph.getEdge(sourceNode, targetNode).getEdgeData().setColor(1.0f, 0.0f, 0.0f); //cyan
		  interactiveGraph.getEdge(sourceNode, targetNode).setWeight(10.0f);
		  interactiveGraph.writeUnlock();
		  refreshApplet();
	  }

	  private void resetPrevSelectedNode()
	  {
		  if(prevSelectedNode!=null)
		  {
			  if(interactiveGraph.getNode(prevSelectedNode.getNode())!=null)
			  {
				  interactiveGraph.writeLock();
				  interactiveGraph.getNode(prevSelectedNode.getNode()).getNodeData().setColor(originalNode.getR(), originalNode.getG(), originalNode.getB());
				  interactiveGraph.getNode(prevSelectedNode.getNode()).getNodeData().setSize(originalNode.getSize());
				  interactiveGraph.writeUnlock();
				  prevSelectedNode=null;
			  }
		  }
	  }

	  public void reset()
	  {
		  interactiveGraph.writeLock();
		  interactivePreviewModel.getProperties().putValue("MultiColourNode", Boolean.FALSE);
		  interactivePreviewModel.getProperties().putValue(PreviewProperty.NODE_OPACITY, 100);
		  interactiveGraph.writeUnlock();

		  if(SET_ORIGINAL_NODE==true)
		  {
			  resetPrevSelectedNode();
			  resetHallmarkNode();
			  refreshApplet();
		  }
	  }

	  public void restoreEdge()
	  {
		  //reset the edge selection (if any) of previously selected node 
		  if(prevSelectedEdge!=null && prevSelectedEdge.size()>0)
		  {
			  Node gSource=interactiveGraph.getNode(prevSelectedEdge.get(0).getNode());
			  Node gTarget=interactiveGraph.getNode(prevSelectedEdge.get(1).getNode());

			  if(gSource!=null && gTarget!=null)
			  {
				  interactiveGraph.writeLock();
				  //reset prev edge sourceNode color and size
				  gSource.getNodeData().setColor(prevSelectedEdge.get(0).getR(), prevSelectedEdge.get(0).getG(), prevSelectedEdge.get(0).getB());
				  gSource.getNodeData().setSize(prevSelectedEdge.get(0).getSize());
				  //reset prev edge targetNode color and size
				  gTarget.getNodeData().setColor(prevSelectedEdge.get(1).getR(), prevSelectedEdge.get(1).getG(), prevSelectedEdge.get(1).getB());
				  gTarget.getNodeData().setSize(prevSelectedEdge.get(1).getSize());
				  //reset prev edge weight to 1
				  Edge[] edgeArr=new Edge[1];
				  edgeArr[0]=interactiveGraph.getEdge(gSource, gTarget);
				  attributeColumnsController.fillEdgesColumnWithValue(edgeArr, attributeModel.getEdgeTable().getColumn(GEPHI_WEIGHT), "1");
				  prevSelectedEdge=new ArrayList<graphNodeData>();
				  interactiveGraph.writeUnlock();
				  refreshApplet();
			  }
		  }
	  }
	  
	  public void updateEdgeThickness(String nodeSize)
	  {
		  AttributeColumn weights=attributeModel.getEdgeTable().getColumn(GEPHI_WEIGHT);
		  ArrayList<Edge> edgeList=new ArrayList<Edge>();
		  String edgeThickness;

		  if(nodeSize==strConstants.getGephiSmall())
			  edgeThickness="1";
		  else if(nodeSize==strConstants.getGephiMedium())
			  edgeThickness="2";
		  else
			  edgeThickness="3";

		  interactiveGraph.writeLock();
		  attributeColumnsController.fillColumnWithValue(attributeModel.getEdgeTable(), weights, edgeThickness);
		  interactiveGraph.writeUnlock();

		  for(Edge e: interactiveGraph.getEdges())
		  {
			  Node source=e.getSource();
			  Node target=e.getTarget();
			  Edge edge=interactiveGraph.getEdge(source, target);
			  edgeList.add(edge);
		  }
		  Edge[] edgeArr=edgeList.toArray(new Edge[edgeList.size()]);
		  interactiveGraph.writeLock();
		  attributeColumnsController.fillEdgesColumnWithValue(edgeArr, weights, edgeThickness);
		  interactiveGraph.writeUnlock();
	  }

	  private void setOriginalNode(String nodeName)
	  {
		  if(SET_ORIGINAL_NODE==false && interactiveGraph.getNode(nodeName)!=null)
		  {
			  originalNode=new graphNodeData(nodeName, interactiveGraph.getNode(nodeName).getNodeData().r(), 
					  interactiveGraph.getNode(nodeName).getNodeData().g(), interactiveGraph.getNode(nodeName).getNodeData().b(),
					  interactiveGraph.getNode(nodeName).getNodeData().alpha(), interactiveGraph.getNode(nodeName).getNodeData().getSize(),
					  interactiveGraph.getNode(nodeName).getNodeData().getLabel());
			  SET_ORIGINAL_NODE=true;
		  }
	  }
	  
	  public void restoreNode(String node, float r, float g, float b, float alpha, float size, String label)
	  {
		  if(interactiveGraph.getNode(node)!=null)
		  {
			  interactiveGraph.writeLock();
			  interactiveGraph.getNode(node).getNodeData().setColor(r, g, b);
			  interactiveGraph.getNode(node).getNodeData().setAlpha(alpha);
			  interactiveGraph.getNode(node).getNodeData().setSize(size);
			  interactiveGraph.getNode(node).getNodeData().setLabel(label);
			  interactiveGraph.writeUnlock();
		  }
	  }

	  public void resetHallmarkNode()
	  {
		  AttributeColumn hallmarkColor=attributeModel.getNodeTable().getColumn(GEPHI_COLOR_LIST);
		  System.out.println("Gephi_graph.java resetHallmarkNode() allNodeList:"+allNodeList.size());
		  for(int i=0; i<allNodeList.size(); i++)
		  {
			  Node[] nodeArr=new Node[1];
			  nodeArr[0]=allNodeList.get(i);
			  attributeColumnsController.fillNodesColumnWithValue(nodeArr, hallmarkColor, null);
		  }
	  }
			  
	  
	  public void resetHallmarkNode1(ArrayList<String> oldSelectedHallmark)
	  {
		  AttributeColumn hallmarkColor=attributeModel.getNodeTable().getColumn(GEPHI_COLOR_LIST);
		  Node[] nodeArr=new Node[1];
		  
		  if(oldSelectedHallmark!=null)
		  {
		  for(int c=0; c<oldSelectedHallmark.size(); c++)
		  {
			  if(oldSelectedHallmark.get(c).compareTo(HALLMARK_PROLIFERATION_DB)==0)
			  {
				  HALLMARK_PROLIFERATION_VIEW=false;
				  if(hallmarkNode_proliferation.size()>0)
				  {
					  for(int i=0; i<hallmarkNode_proliferation.size(); i++)
					  {
						  String nodeName=hallmarkNode_proliferation.get(i);
						  if(interactiveGraph.getNode(nodeName)!=null)
						  {
							  interactiveGraph.writeLock();
							  interactiveGraph.getNode(nodeName).getNodeData().setColor(originalNode.getR(), originalNode.getG(), originalNode.getB());
							  interactiveGraph.getNode(nodeName).getNodeData().setSize(originalNode.getSize());
							  nodeArr[0]=interactiveGraph.getNode(nodeName);
							  attributeColumnsController.fillNodesColumnWithValue(nodeArr, hallmarkColor, null);
							  interactiveGraph.writeUnlock();
						  }
					  }
				  }
				  hallmarkNode_proliferation=new ArrayList<String>();
			  }
			  if(oldSelectedHallmark.get(c).compareTo(HALLMARK_GROWTH_REPRESSOR_DB)==0)
			  {
				  HALLMARK_GROWTH_REPRESSOR_VIEW=false;
				  if(hallmarkNode_growth_repressor.size()>0)
				  {
					  for(int i=0; i<hallmarkNode_growth_repressor.size(); i++)
					  {
						  String nodeName=hallmarkNode_growth_repressor.get(i);
						  if(interactiveGraph.getNode(nodeName)!=null)
						  {
							  interactiveGraph.writeLock();
							  interactiveGraph.getNode(nodeName).getNodeData().setColor(originalNode.getR(), originalNode.getG(), originalNode.getB());
							  interactiveGraph.getNode(nodeName).getNodeData().setSize(originalNode.getSize());
							  nodeArr[0]=interactiveGraph.getNode(nodeName);
							  attributeColumnsController.fillNodesColumnWithValue(nodeArr, hallmarkColor, null);
							  interactiveGraph.writeUnlock();
						  }
					  }
				  }
				  hallmarkNode_growth_repressor=new ArrayList<String>();
			  }
			  if(oldSelectedHallmark.get(c).compareTo(HALLMARK_APOPTOSIS_DB)==0)
			  {
				  HALLMARK_APOPTOSIS_VIEW=false;
				  if(hallmarkNode_apoptosis.size()>0)
				  {
					  for(int i=0; i<hallmarkNode_apoptosis.size(); i++)
					  {
						  String nodeName=hallmarkNode_apoptosis.get(i);
						  if(interactiveGraph.getNode(nodeName)!=null)
						  {
							  interactiveGraph.writeLock();
							  interactiveGraph.getNode(nodeName).getNodeData().setColor(originalNode.getR(), originalNode.getG(), originalNode.getB());
							  interactiveGraph.getNode(nodeName).getNodeData().setSize(originalNode.getSize());
							  nodeArr[0]=interactiveGraph.getNode(nodeName);
							  attributeColumnsController.fillNodesColumnWithValue(nodeArr, hallmarkColor, null);
							  interactiveGraph.writeUnlock();
						  }
					  }
				  }
				  hallmarkNode_apoptosis=new ArrayList<String>();
			  }
			  if(oldSelectedHallmark.get(c).compareTo(HALLMARK_REPLICATIVE_IMMORTALITY_DB)==0)
			  {
				  HALLMARK_REPLICATIVE_IMMORTALITY_VIEW=false;
				  if(hallmarkNode_replicative_immortality.size()>0)
				  {
					  for(int i=0; i<hallmarkNode_replicative_immortality.size(); i++)
					  {
						  String nodeName=hallmarkNode_replicative_immortality.get(i);
						  if(interactiveGraph.getNode(nodeName)!=null)
						  {
							  interactiveGraph.writeLock();
							  interactiveGraph.getNode(nodeName).getNodeData().setColor(originalNode.getR(), originalNode.getG(), originalNode.getB());
							  interactiveGraph.getNode(nodeName).getNodeData().setSize(originalNode.getSize());
							  nodeArr[0]=interactiveGraph.getNode(nodeName);
							  attributeColumnsController.fillNodesColumnWithValue(nodeArr, hallmarkColor, null);
							  interactiveGraph.writeUnlock();
						  }
					  }
				  }
				  hallmarkNode_replicative_immortality=new ArrayList<String>();
			  }
			  if(oldSelectedHallmark.get(c).compareTo(HALLMARK_ANGIOGENESIS_DB)==0)
			  {
				  HALLMARK_ANGIOGENESIS_VIEW=false;
				  if(hallmarkNode_angiogenesis.size()>0)
				  {
					  for(int i=0; i<hallmarkNode_angiogenesis.size(); i++)
					  {
						  String nodeName=hallmarkNode_angiogenesis.get(i);
						  if(interactiveGraph.getNode(nodeName)!=null)
						  {
							  interactiveGraph.writeLock();
							  interactiveGraph.getNode(nodeName).getNodeData().setColor(originalNode.getR(), originalNode.getG(), originalNode.getB());
							  interactiveGraph.getNode(nodeName).getNodeData().setSize(originalNode.getSize());
							  nodeArr[0]=interactiveGraph.getNode(nodeName);
							  attributeColumnsController.fillNodesColumnWithValue(nodeArr, hallmarkColor, null);
							  interactiveGraph.writeUnlock();
						  }
					  }
				  }
				  hallmarkNode_angiogenesis=new ArrayList<String>();
			  }
			  if(oldSelectedHallmark.get(c).compareTo(HALLMARK_METASTASIS_DB)==0)
			  {
				  HALLMARK_METASTASIS_VIEW=false;
				  if(hallmarkNode_metastasis.size()>0)
				  {
					  for(int i=0; i<hallmarkNode_metastasis.size(); i++)
					  {
						  String nodeName=hallmarkNode_metastasis.get(i);
						  if(interactiveGraph.getNode(nodeName)!=null)
						  {
							  interactiveGraph.writeLock();
							  interactiveGraph.getNode(nodeName).getNodeData().setColor(originalNode.getR(), originalNode.getG(), originalNode.getB());
							  interactiveGraph.getNode(nodeName).getNodeData().setSize(originalNode.getSize());
							  nodeArr[0]=interactiveGraph.getNode(nodeName);
							  attributeColumnsController.fillNodesColumnWithValue(nodeArr, hallmarkColor, null);
							  interactiveGraph.writeUnlock();
						  }
					  }
				  }
				  hallmarkNode_metastasis=new ArrayList<String>();
			  }
			  if(oldSelectedHallmark.get(c).compareTo(HALLMARK_METABOLISM_DB)==0)
			  {
				  HALLMARK_METABOLISM_VIEW=false;
				  if(hallmarkNode_metabolism.size()>0)
				  {
					  for(int i=0; i<hallmarkNode_metabolism.size(); i++)
					  {
						  String nodeName=hallmarkNode_metabolism.get(i);
						  if(interactiveGraph.getNode(nodeName)!=null)
						  {
							  interactiveGraph.writeLock();
							  interactiveGraph.getNode(nodeName).getNodeData().setColor(originalNode.getR(), originalNode.getG(), originalNode.getB());
							  interactiveGraph.getNode(nodeName).getNodeData().setSize(originalNode.getSize());
							  nodeArr[0]=interactiveGraph.getNode(nodeName);
							  attributeColumnsController.fillNodesColumnWithValue(nodeArr, hallmarkColor, null);
							  interactiveGraph.writeUnlock();
						  }
					  }
				  }
				  hallmarkNode_metabolism=new ArrayList<String>();
			  }
			  if(oldSelectedHallmark.get(c).compareTo(HALLMARK_IMMUNE_DESTRUCTION_DB)==0)
			  {
				  HALLMARK_IMMUNE_DESTRUCTION_VIEW=false;
				  if(hallmarkNode_immune_destruction.size()>0)
				  {
					  for(int i=0; i<hallmarkNode_immune_destruction.size(); i++)
					  {
						  String nodeName=hallmarkNode_immune_destruction.get(i);
						  if(interactiveGraph.getNode(nodeName)!=null)
						  {
							  interactiveGraph.writeLock();
							  interactiveGraph.getNode(nodeName).getNodeData().setColor(originalNode.getR(), originalNode.getG(), originalNode.getB());
							  interactiveGraph.getNode(nodeName).getNodeData().setSize(originalNode.getSize());
							  nodeArr[0]=interactiveGraph.getNode(nodeName);
							  attributeColumnsController.fillNodesColumnWithValue(nodeArr, hallmarkColor, null);
							  interactiveGraph.writeUnlock();
						  }
					  }
				  }
				  hallmarkNode_immune_destruction=new ArrayList<String>();
			  }
			  if(oldSelectedHallmark.get(c).compareTo(HALLMARK_GENOME_INSTABILITY_DB)==0)
			  {
				  HALLMARK_GENOME_INSTABILITY_VIEW=false;
				  if(hallmarkNode_genome_instability.size()>0)
				  {
					  for(int i=0; i<hallmarkNode_genome_instability.size(); i++)
					  {
						  String nodeName=hallmarkNode_genome_instability.get(i);
						  if(interactiveGraph.getNode(nodeName)!=null)
						  {
							  interactiveGraph.writeLock();
							  interactiveGraph.getNode(nodeName).getNodeData().setColor(originalNode.getR(), originalNode.getG(), originalNode.getB());
							  interactiveGraph.getNode(nodeName).getNodeData().setSize(originalNode.getSize());
							  nodeArr[0]=interactiveGraph.getNode(nodeName);
							  attributeColumnsController.fillNodesColumnWithValue(nodeArr, hallmarkColor, null);
							  interactiveGraph.writeUnlock();
						  }
					  }
				  }
				  hallmarkNode_genome_instability=new ArrayList<String>();
			  }
			  if(oldSelectedHallmark.get(c).compareTo(HALLMARK_TUMOR_PROMOTING_INFLAMMATION_DB)==0)
			  {
				  HALLMARK_TUMOR_PROMOTING_INFLAMMATION_VIEW=false;
				  if(hallmarkNode_tumor_promoting_inflammation.size()>0)
				  {
					  for(int i=0; i<hallmarkNode_tumor_promoting_inflammation.size(); i++)
					  {
						  String nodeName=hallmarkNode_tumor_promoting_inflammation.get(i);
						  if(interactiveGraph.getNode(nodeName)!=null)
						  {
							  interactiveGraph.writeLock();
							  interactiveGraph.getNode(nodeName).getNodeData().setColor(originalNode.getR(), originalNode.getG(), originalNode.getB());
							  interactiveGraph.getNode(nodeName).getNodeData().setSize(originalNode.getSize());
							  nodeArr[0]=interactiveGraph.getNode(nodeName);
							  attributeColumnsController.fillNodesColumnWithValue(nodeArr, hallmarkColor, null);
							  interactiveGraph.writeUnlock();
						  }
					  }
				  }
				  hallmarkNode_tumor_promoting_inflammation=new ArrayList<String>();
			  }
		  }
		  }
		  else
		  {
			  HALLMARK_PROLIFERATION_VIEW=false;
			  if(hallmarkNode_proliferation.size()>0)
			  {
				  for(int i=0; i<hallmarkNode_proliferation.size(); i++)
				  {
					  String nodeName=hallmarkNode_proliferation.get(i);
					  if(interactiveGraph.getNode(nodeName)!=null)
					  {
						  interactiveGraph.writeLock();
						  interactiveGraph.getNode(nodeName).getNodeData().setColor(originalNode.getR(), originalNode.getG(), originalNode.getB());
						  interactiveGraph.getNode(nodeName).getNodeData().setSize(originalNode.getSize());
						  nodeArr[0]=interactiveGraph.getNode(nodeName);
						  attributeColumnsController.fillNodesColumnWithValue(nodeArr, hallmarkColor, null);
						  interactiveGraph.writeUnlock();
					  }
				  }
			  }
			  hallmarkNode_proliferation=new ArrayList<String>();
			  
			  HALLMARK_GROWTH_REPRESSOR_VIEW=false;
			  if(hallmarkNode_growth_repressor.size()>0)
			  {
				  for(int i=0; i<hallmarkNode_growth_repressor.size(); i++)
				  {
					  String nodeName=hallmarkNode_growth_repressor.get(i);
					  if(interactiveGraph.getNode(nodeName)!=null)
					  {
						  interactiveGraph.writeLock();
						  interactiveGraph.getNode(nodeName).getNodeData().setColor(originalNode.getR(), originalNode.getG(), originalNode.getB());
						  interactiveGraph.getNode(nodeName).getNodeData().setSize(originalNode.getSize());
						  nodeArr[0]=interactiveGraph.getNode(nodeName);
						  attributeColumnsController.fillNodesColumnWithValue(nodeArr, hallmarkColor, null);
						  interactiveGraph.writeUnlock();
					  }
				  }
			  }
			  hallmarkNode_growth_repressor=new ArrayList<String>();
			  
			  HALLMARK_APOPTOSIS_VIEW=false;
			  if(hallmarkNode_apoptosis.size()>0)
			  {
				  for(int i=0; i<hallmarkNode_apoptosis.size(); i++)
				  {
					  String nodeName=hallmarkNode_apoptosis.get(i);
					  if(interactiveGraph.getNode(nodeName)!=null)
					  {
						  interactiveGraph.writeLock();
						  interactiveGraph.getNode(nodeName).getNodeData().setColor(originalNode.getR(), originalNode.getG(), originalNode.getB());
						  interactiveGraph.getNode(nodeName).getNodeData().setSize(originalNode.getSize());
						  nodeArr[0]=interactiveGraph.getNode(nodeName);
						  attributeColumnsController.fillNodesColumnWithValue(nodeArr, hallmarkColor, null);
						  interactiveGraph.writeUnlock();
					  }
				  }
			  }
			  hallmarkNode_apoptosis=new ArrayList<String>();
			  
			  HALLMARK_REPLICATIVE_IMMORTALITY_VIEW=false;
			  if(hallmarkNode_replicative_immortality.size()>0)
			  {
				  for(int i=0; i<hallmarkNode_replicative_immortality.size(); i++)
				  {
					  String nodeName=hallmarkNode_replicative_immortality.get(i);
					  if(interactiveGraph.getNode(nodeName)!=null)
					  {
						  interactiveGraph.writeLock();
						  interactiveGraph.getNode(nodeName).getNodeData().setColor(originalNode.getR(), originalNode.getG(), originalNode.getB());
						  interactiveGraph.getNode(nodeName).getNodeData().setSize(originalNode.getSize());
						  nodeArr[0]=interactiveGraph.getNode(nodeName);
						  attributeColumnsController.fillNodesColumnWithValue(nodeArr, hallmarkColor, null);
						  interactiveGraph.writeUnlock();
					  }
				  }
			  }
			  hallmarkNode_replicative_immortality=new ArrayList<String>();
			  
			  HALLMARK_ANGIOGENESIS_VIEW=false;
			  if(hallmarkNode_angiogenesis.size()>0)
			  {
				  for(int i=0; i<hallmarkNode_angiogenesis.size(); i++)
				  {
					  String nodeName=hallmarkNode_angiogenesis.get(i);
					  if(interactiveGraph.getNode(nodeName)!=null)
					  {
						  interactiveGraph.writeLock();
						  interactiveGraph.getNode(nodeName).getNodeData().setColor(originalNode.getR(), originalNode.getG(), originalNode.getB());
						  interactiveGraph.getNode(nodeName).getNodeData().setSize(originalNode.getSize());
						  nodeArr[0]=interactiveGraph.getNode(nodeName);
						  attributeColumnsController.fillNodesColumnWithValue(nodeArr, hallmarkColor, null);
						  interactiveGraph.writeUnlock();
					  }
				  }
			  }
			  hallmarkNode_angiogenesis=new ArrayList<String>();
			  
			  HALLMARK_METASTASIS_VIEW=false;
			  if(hallmarkNode_metastasis.size()>0)
			  {
				  for(int i=0; i<hallmarkNode_metastasis.size(); i++)
				  {
					  String nodeName=hallmarkNode_metastasis.get(i);
					  if(interactiveGraph.getNode(nodeName)!=null)
					  {
						  interactiveGraph.writeLock();
						  interactiveGraph.getNode(nodeName).getNodeData().setColor(originalNode.getR(), originalNode.getG(), originalNode.getB());
						  interactiveGraph.getNode(nodeName).getNodeData().setSize(originalNode.getSize());
						  nodeArr[0]=interactiveGraph.getNode(nodeName);
						  attributeColumnsController.fillNodesColumnWithValue(nodeArr, hallmarkColor, null);
						  interactiveGraph.writeUnlock();
					  }
				  }
			  }
			  hallmarkNode_metastasis=new ArrayList<String>();
			  
			  HALLMARK_METABOLISM_VIEW=false;
			  if(hallmarkNode_metabolism.size()>0)
			  {
				  for(int i=0; i<hallmarkNode_metabolism.size(); i++)
				  {
					  String nodeName=hallmarkNode_metabolism.get(i);
					  if(interactiveGraph.getNode(nodeName)!=null)
					  {
						  interactiveGraph.writeLock();
						  interactiveGraph.getNode(nodeName).getNodeData().setColor(originalNode.getR(), originalNode.getG(), originalNode.getB());
						  interactiveGraph.getNode(nodeName).getNodeData().setSize(originalNode.getSize());
						  nodeArr[0]=interactiveGraph.getNode(nodeName);
						  attributeColumnsController.fillNodesColumnWithValue(nodeArr, hallmarkColor, null);
						  interactiveGraph.writeUnlock();
					  }
				  }
			  }
			  hallmarkNode_metabolism=new ArrayList<String>();
			  
			  HALLMARK_IMMUNE_DESTRUCTION_VIEW=false;
			  if(hallmarkNode_immune_destruction.size()>0)
			  {
				  for(int i=0; i<hallmarkNode_immune_destruction.size(); i++)
				  {
					  String nodeName=hallmarkNode_immune_destruction.get(i);
					  if(interactiveGraph.getNode(nodeName)!=null)
					  {
						  interactiveGraph.writeLock();
						  interactiveGraph.getNode(nodeName).getNodeData().setColor(originalNode.getR(), originalNode.getG(), originalNode.getB());
						  interactiveGraph.getNode(nodeName).getNodeData().setSize(originalNode.getSize());
						  nodeArr[0]=interactiveGraph.getNode(nodeName);
						  attributeColumnsController.fillNodesColumnWithValue(nodeArr, hallmarkColor, null);
						  interactiveGraph.writeUnlock();
					  }
				  }
			  }
			  hallmarkNode_immune_destruction=new ArrayList<String>();
			  
			  HALLMARK_GENOME_INSTABILITY_VIEW=false;
			  if(hallmarkNode_genome_instability.size()>0)
			  {
				  for(int i=0; i<hallmarkNode_genome_instability.size(); i++)
				  {
					  String nodeName=hallmarkNode_genome_instability.get(i);
					  if(interactiveGraph.getNode(nodeName)!=null)
					  {
						  interactiveGraph.writeLock();
						  interactiveGraph.getNode(nodeName).getNodeData().setColor(originalNode.getR(), originalNode.getG(), originalNode.getB());
						  interactiveGraph.getNode(nodeName).getNodeData().setSize(originalNode.getSize());
						  nodeArr[0]=interactiveGraph.getNode(nodeName);
						  attributeColumnsController.fillNodesColumnWithValue(nodeArr, hallmarkColor, null);
						  interactiveGraph.writeUnlock();
					  }
				  }
			  }
			  hallmarkNode_genome_instability=new ArrayList<String>();
			  
			  HALLMARK_TUMOR_PROMOTING_INFLAMMATION_VIEW=false;
			  if(hallmarkNode_tumor_promoting_inflammation.size()>0)
			  {
				  for(int i=0; i<hallmarkNode_tumor_promoting_inflammation.size(); i++)
				  {
					  String nodeName=hallmarkNode_tumor_promoting_inflammation.get(i);
					  if(interactiveGraph.getNode(nodeName)!=null)
					  {
						  interactiveGraph.writeLock();
						  interactiveGraph.getNode(nodeName).getNodeData().setColor(originalNode.getR(), originalNode.getG(), originalNode.getB());
						  interactiveGraph.getNode(nodeName).getNodeData().setSize(originalNode.getSize());
						  nodeArr[0]=interactiveGraph.getNode(nodeName);
						  attributeColumnsController.fillNodesColumnWithValue(nodeArr, hallmarkColor, null);
						  interactiveGraph.writeUnlock();
					  }
				  }
			  }
			  hallmarkNode_tumor_promoting_inflammation=new ArrayList<String>();
		  }
		  refreshApplet();
	  }

	  private void setNodeToHallmarkColorSize(String nodeName, String col, float size, float inc_size, boolean isSelected)
	  {
		  AttributeColumn hallmarkColor=attributeModel.getNodeTable().getColumn(GEPHI_COLOR_LIST);
		  float curr_size;

		  setOriginalNode(nodeName);

		  Node node=interactiveGraph.getNode(nodeName);
		  if(node!=null)
		  {
			  interactiveGraph.writeLock();
			  curr_size=interactiveGraph.getNode(nodeName).getNodeData().getSize();
			  node.getNodeData().setSize(size);
			  String currHallmarkColor=(String)node.getNodeData().getAttributes().getValue(hallmarkColor.getIndex());
	 		//set node size
			  if(currHallmarkColor==null || currHallmarkColor.length()==0)
				  interactiveGraph.getNode(nodeName).getNodeData().setSize(size);
			  else
				  interactiveGraph.getNode(nodeName).getNodeData().setSize(curr_size+inc_size);

			  //set node color
			  if(currHallmarkColor==null || currHallmarkColor.length()==0)
				  currHallmarkColor=col; 
			  else
				  currHallmarkColor=currHallmarkColor+","+col;
			  Node[] nodeArr=new Node[1];
			  nodeArr[0]=node;
			  attributeColumnsController.fillNodesColumnWithValue(nodeArr, hallmarkColor, currHallmarkColor);

			  //if node selected, make it opaque
			  if(isSelected)
				  interactiveGraph.getNode(nodeName).getNodeData().setAlpha(1.0f);
			  else
				  interactiveGraph.getNode(nodeName).getNodeData().setAlpha(0.5f);
			  interactiveGraph.writeUnlock();
		  }
	  }

	  public ArrayList<graphNodeData> relabelNodeOfSelectedPathway(ArrayList<String> nodeInPathway)
	  {
		  ArrayList<graphNodeData> list=new ArrayList<graphNodeData>();
		  for(int i=0; i<nodeInPathway.size(); i++)
		  {
			  Node n=interactiveGraph.getNode(nodeInPathway.get(i));
			  if(n!=null)
			  {
				  interactiveGraph.writeLock();
				  graphNodeData d=new graphNodeData(nodeInPathway.get(i), n.getNodeData().r(), n.getNodeData().g(), n.getNodeData().b(),
						  n.getNodeData().alpha(), n.getNodeData().getSize(), n.getNodeData().getLabel());
				  list.add(d);
				  String label=n.getNodeData().getLabel();
				  n.getNodeData().setLabel("*["+label+"]*");
				  interactiveGraph.writeUnlock();
			  }
		  }
		  return list;
	  }

	  public ArrayList<graphNodeData> recolorNodeOfSelectedPathway(ArrayList<String> nodeInPathway)
	  {
		  ArrayList<graphNodeData> list=new ArrayList<graphNodeData>();
		  for(int i=0; i<nodeInPathway.size(); i++)
		  {
			  Node n=interactiveGraph.getNode(nodeInPathway.get(i));
			  if(n!=null)
			  {
				  interactiveGraph.writeLock();
				  graphNodeData d=new graphNodeData(nodeInPathway.get(i), n.getNodeData().r(), n.getNodeData().g(), n.getNodeData().b(),
						  n.getNodeData().alpha(), n.getNodeData().getSize(), n.getNodeData().getLabel());
				  list.add(d);
				  n.getNodeData().setColor(1.0f, 0.0f, 0.0f);
				  interactiveGraph.writeUnlock();
			  }
		  }
		  return list;
	  }

	  public void colorAndResizeHallmarkNode(allHallmark allHallmarkNodeList, ArrayList<String> selectedHallmark, boolean colorAsterixNode)
	  {
		  float HALLMARK_SIZE=10.0f;
		  float HALLMARK_INCREMENT_SIZE=5.0f;

		  HALLMARK_PROLIFERATION_VIEW=false;
		  HALLMARK_GROWTH_REPRESSOR_VIEW=false;
		  HALLMARK_APOPTOSIS_VIEW=false;
		  HALLMARK_REPLICATIVE_IMMORTALITY_VIEW=false;
		  HALLMARK_ANGIOGENESIS_VIEW=false;
		  HALLMARK_METASTASIS_VIEW=false;
		  HALLMARK_METABOLISM_VIEW=false;
		  HALLMARK_IMMUNE_DESTRUCTION_VIEW=false;
		  HALLMARK_GENOME_INSTABILITY_VIEW=false;
		  HALLMARK_TUMOR_PROMOTING_INFLAMMATION_VIEW=false;

		  if(selectedHallmark.size()>0)
		  {
			  hallmarkClusterImpl[] hallmarkClusterArray=new hallmarkClusterImpl[selectedHallmark.size()];
			  interactiveGraph.writeLock();
			  interactivePreviewModel.getProperties().putValue(PreviewProperty.NODE_OPACITY, 50);
			  interactivePreviewModel.getProperties().putValue("MultiColourNode", Boolean.TRUE);
			  interactiveGraph.writeUnlock();

			  //create cluster
			  for(int i=0; i<selectedHallmark.size(); i++)
			  {
				  hallmarkClusterArray[i]=new hallmarkClusterImpl();
				  hallmarkClusterArray[i].print();
				  if(selectedHallmark.get(i).compareTo(HALLMARK_PROLIFERATION_DB)==0)
				  {
					  HALLMARK_PROLIFERATION_VIEW=true;
					  hallmarkClusterArray[i].setName(HALLMARK_PROLIFERATION_DB);
					  hallmarkClusterArray[i].print();
					  System.out.println("Gephi_graph.java colorAndResizeHallmarkNode: HALLMARK_PROLIFERATION_DB true!!");
					  hallmark thisHallmark=allHallmarkNodeList.getProliferation();
					  for(int j=0; j<thisHallmark.getSize(); j++)
					  {
						  String nodeName=thisHallmark.getNodeNameAt(j);
						  Node node=interactiveGraph.getNode(nodeName);
						  hallmarkClusterArray[i].addNode(node);

						  //0x00FF00 green
						  if(prevSelectedNode==null || nodeName.compareTo(prevSelectedNode.getNode())!=0)
							  setNodeToHallmarkColorSize(nodeName, "65280", HALLMARK_SIZE, HALLMARK_INCREMENT_SIZE, false);
						  else if(nodeName.compareTo(prevSelectedNode.getNode())==0)
							  setNodeToHallmarkColorSize(nodeName, "65280", HALLMARK_SIZE, HALLMARK_INCREMENT_SIZE, true);
						  hallmarkNode_proliferation.add(nodeName);
					  }
				  }
				  if(selectedHallmark.get(i).compareTo(HALLMARK_GROWTH_REPRESSOR_DB)==0)
				  {
					  HALLMARK_GROWTH_REPRESSOR_VIEW=true;
					  hallmarkClusterArray[i].setName(HALLMARK_GROWTH_REPRESSOR_DB);
					  hallmarkClusterArray[i].print();
					  System.out.println("Gephi_graph.java colorAndResizeHallmarkNode: HALLMARK_GROWTH_REPRESSOR_DB true!!");
					  hallmark thisHallmark=allHallmarkNodeList.getGrowthRepressor();
					  for(int j=0; j<thisHallmark.getSize(); j++)
					  {
						  String nodeName=thisHallmark.getNodeNameAt(j);
						  Node node=interactiveGraph.getNode(nodeName);
						  hallmarkClusterArray[i].addNode(node);

						  //0xFF8000 orange
						  if(prevSelectedNode==null || nodeName.compareTo(prevSelectedNode.getNode())!=0)
							  setNodeToHallmarkColorSize(nodeName, "16744448", HALLMARK_SIZE, HALLMARK_INCREMENT_SIZE, false);
						  else if(nodeName.compareTo(prevSelectedNode.getNode())==0)
							  setNodeToHallmarkColorSize(nodeName, "16744448", HALLMARK_SIZE, HALLMARK_INCREMENT_SIZE, true);
						  hallmarkNode_growth_repressor.add(nodeName);
					  }
				  }
				  if(selectedHallmark.get(i).compareTo(HALLMARK_APOPTOSIS_DB)==0)
				  {
					  HALLMARK_APOPTOSIS_VIEW=true;
					  hallmarkClusterArray[i].setName(HALLMARK_APOPTOSIS_DB);
					  hallmarkClusterArray[i].print();
					  System.out.println("Gephi_graph.java colorAndResizeHallmarkNode: HALLMARK_APOPTOSIS_DB true!!");
					  hallmark thisHallmark=allHallmarkNodeList.getApoptosis();
					  //if(colorAsterixNode)
					  //	hallmarkNode_apoptosis.add("*");
					  for(int j=0; j<thisHallmark.getSize(); j++)
					  {
						  String nodeName=thisHallmark.getNodeNameAt(j);
						  Node node=interactiveGraph.getNode(nodeName);
						  hallmarkClusterArray[i].addNode(node);

						  //0xFFFF00 yellow
						  if(prevSelectedNode==null || nodeName.compareTo(prevSelectedNode.getNode())!=0)
							  setNodeToHallmarkColorSize(nodeName, "16776960", HALLMARK_SIZE, HALLMARK_INCREMENT_SIZE, false);
						  else if(nodeName.compareTo(prevSelectedNode.getNode())==0)
							  setNodeToHallmarkColorSize(nodeName, "16776960", HALLMARK_SIZE, HALLMARK_INCREMENT_SIZE, true);
						  hallmarkNode_apoptosis.add(nodeName);
					  }
				  }
				  if(selectedHallmark.get(i).compareTo(HALLMARK_REPLICATIVE_IMMORTALITY_DB)==0)
				  {
					  HALLMARK_REPLICATIVE_IMMORTALITY_VIEW=true;
					  hallmarkClusterArray[i].setName(HALLMARK_REPLICATIVE_IMMORTALITY_DB);
					  hallmarkClusterArray[i].print();
					  System.out.println("Gephi_graph.java colorAndResizeHallmarkNode: HALLMARK_REPLICATIVE_IMMORTALITY_DB true!!");
					  hallmark thisHallmark=allHallmarkNodeList.getReplicativeImmortality();
					  for(int j=0; j<thisHallmark.getSize(); j++)
					  {
						  String nodeName=thisHallmark.getNodeNameAt(j);
						  Node node=interactiveGraph.getNode(nodeName);
						  hallmarkClusterArray[i].addNode(node);

						  //0x0080FF blue
						  if(prevSelectedNode==null || nodeName.compareTo(prevSelectedNode.getNode())!=0)
							  setNodeToHallmarkColorSize(nodeName, "33023", HALLMARK_SIZE, HALLMARK_INCREMENT_SIZE, false);
						  else if(nodeName.compareTo(prevSelectedNode.getNode())==0)
							  setNodeToHallmarkColorSize(nodeName, "33023", HALLMARK_SIZE, HALLMARK_INCREMENT_SIZE, true);
						  hallmarkNode_replicative_immortality.add(nodeName);
					  }
				  }
				  if(selectedHallmark.get(i).compareTo(HALLMARK_ANGIOGENESIS_DB)==0)
				  {
					  HALLMARK_ANGIOGENESIS_VIEW=true;
					  hallmarkClusterArray[i].setName(HALLMARK_ANGIOGENESIS_DB);
					  hallmarkClusterArray[i].print();
					  System.out.println("Gephi_graph.java colorAndResizeHallmarkNode: HALLMARK_ANGIOGENESIS_DB true!!");
					  hallmark thisHallmark=allHallmarkNodeList.getAngiogenesis();
					  for(int j=0; j<thisHallmark.getSize(); j++)
					  {
						  String nodeName=thisHallmark.getNodeNameAt(j);
						  Node node=interactiveGraph.getNode(nodeName);
						  hallmarkClusterArray[i].addNode(node);

						  //0xFF80FF pink
						  if(prevSelectedNode==null || nodeName.compareTo(prevSelectedNode.getNode())!=0)
							  setNodeToHallmarkColorSize(nodeName, "16744703", HALLMARK_SIZE, HALLMARK_INCREMENT_SIZE, false);
						  else if(nodeName.compareTo(prevSelectedNode.getNode())==0)
							  setNodeToHallmarkColorSize(nodeName, "16744703", HALLMARK_SIZE, HALLMARK_INCREMENT_SIZE, true);
						  hallmarkNode_angiogenesis.add(nodeName);
					  }
				  }
				  if(selectedHallmark.get(i).compareTo(HALLMARK_METASTASIS_DB)==0)
				  {
					  HALLMARK_METASTASIS_VIEW=true;
					  hallmarkClusterArray[i].setName(HALLMARK_METASTASIS_DB);
					  hallmarkClusterArray[i].print();
					  System.out.println("Gephi_graph.java colorAndResizeHallmarkNode: HALLMARK_METASTASIS_DB true!!");
					  hallmark thisHallmark=allHallmarkNodeList.getMetastasis();
					  for(int j=0; j<thisHallmark.getSize(); j++)
					  {
						  String nodeName=thisHallmark.getNodeNameAt(j);
						  Node node=interactiveGraph.getNode(nodeName);
						  hallmarkClusterArray[i].addNode(node);

						  //0x8080FF purple
						  if(prevSelectedNode==null || nodeName.compareTo(prevSelectedNode.getNode())!=0)
							  setNodeToHallmarkColorSize(nodeName, "8421631", HALLMARK_SIZE, HALLMARK_INCREMENT_SIZE, false);
						  else if(nodeName.compareTo(prevSelectedNode.getNode())==0)
							  setNodeToHallmarkColorSize(nodeName, "8421631", HALLMARK_SIZE, HALLMARK_INCREMENT_SIZE, true);
						  hallmarkNode_metastasis.add(nodeName);
					  }
				  }
				  if(selectedHallmark.get(i).compareTo(HALLMARK_METABOLISM_DB)==0)
				  {
					  HALLMARK_METABOLISM_VIEW=true;
					  hallmarkClusterArray[i].setName(HALLMARK_METABOLISM_DB);
					  hallmarkClusterArray[i].print();
					  System.out.println("Gephi_graph.java colorAndResizeHallmarkNode: HALLMARK_METABOLISM_DB true!!");
					  hallmark thisHallmark=allHallmarkNodeList.getMetabolism();
					  for(int j=0; j<thisHallmark.getSize(); j++)
					  {
						  String nodeName=thisHallmark.getNodeNameAt(j);
						  Node node=interactiveGraph.getNode(nodeName);
						  hallmarkClusterArray[i].addNode(node);

						  //0x808000 brown
						  if(prevSelectedNode==null || nodeName.compareTo(prevSelectedNode.getNode())!=0)
							  setNodeToHallmarkColorSize(nodeName, "8421376", HALLMARK_SIZE, HALLMARK_INCREMENT_SIZE, false);
						  else if(nodeName.compareTo(prevSelectedNode.getNode())==0)
							  setNodeToHallmarkColorSize(nodeName, "8421376", HALLMARK_SIZE, HALLMARK_INCREMENT_SIZE, true);
						  hallmarkNode_metabolism.add(nodeName);
					  }
				  }
				  if(selectedHallmark.get(i).compareTo(HALLMARK_IMMUNE_DESTRUCTION_DB)==0)
				  {
					  HALLMARK_IMMUNE_DESTRUCTION_VIEW=true;
					  hallmarkClusterArray[i].setName(HALLMARK_IMMUNE_DESTRUCTION_DB);
					  hallmarkClusterArray[i].print();
					  System.out.println("Gephi_graph.java colorAndResizeHallmarkNode: HALLMARK_IMMUNE_DESTRUCTION_DB true!!");
					  hallmark thisHallmark=allHallmarkNodeList.getImmuneDestruction();
					  for(int j=0; j<thisHallmark.getSize(); j++)
					  {
						  String nodeName=thisHallmark.getNodeNameAt(j);
						  Node node=interactiveGraph.getNode(nodeName);
						  hallmarkClusterArray[i].addNode(node);

						  //0xFF0000 red
						  if(prevSelectedNode==null || nodeName.compareTo(prevSelectedNode.getNode())!=0)
							  setNodeToHallmarkColorSize(nodeName, "16711680", HALLMARK_SIZE, HALLMARK_INCREMENT_SIZE, false);
						  else if(nodeName.compareTo(prevSelectedNode.getNode())==0)
							  setNodeToHallmarkColorSize(nodeName, "16711680", HALLMARK_SIZE, HALLMARK_INCREMENT_SIZE, true);
						  hallmarkNode_immune_destruction.add(nodeName);
					  }
				  }
				  if(selectedHallmark.get(i).compareTo(HALLMARK_GENOME_INSTABILITY_DB)==0)
				  {
					  HALLMARK_GENOME_INSTABILITY_VIEW=true;
					  hallmarkClusterArray[i].setName(HALLMARK_GENOME_INSTABILITY_DB);
					  hallmarkClusterArray[i].print();
					  System.out.println("Gephi_graph.java colorAndResizeHallmarkNode: HALLMARK_GENOME_INSTABILITY_DB true!!");
					  hallmark thisHallmark=allHallmarkNodeList.getGenomeInstability();
					  for(int j=0; j<thisHallmark.getSize(); j++)
					  {
						  String nodeName=thisHallmark.getNodeNameAt(j);
						  Node node=interactiveGraph.getNode(nodeName);
						  hallmarkClusterArray[i].addNode(node);

						  //0x008080 teal
						  if(prevSelectedNode==null || nodeName.compareTo(prevSelectedNode.getNode())!=0)
							  setNodeToHallmarkColorSize(nodeName, "32896", HALLMARK_SIZE, HALLMARK_INCREMENT_SIZE, false);
						  else if(nodeName.compareTo(prevSelectedNode.getNode())==0)
							  setNodeToHallmarkColorSize(nodeName, "32896", HALLMARK_SIZE, HALLMARK_INCREMENT_SIZE, true);
						  hallmarkNode_genome_instability.add(nodeName);
					  }
				  }
				  if(selectedHallmark.get(i).compareTo(HALLMARK_TUMOR_PROMOTING_INFLAMMATION_DB)==0)
				  {
					  HALLMARK_TUMOR_PROMOTING_INFLAMMATION_VIEW=true;
					  hallmarkClusterArray[i].setName(HALLMARK_TUMOR_PROMOTING_INFLAMMATION_DB);
					  hallmarkClusterArray[i].print();
					  System.out.println("Gephi_graph.java colorAndResizeHallmarkNode: HALLMARK_TUMOR_PROMOTING_INFLAMMATION_DB true!!");
					  hallmark thisHallmark=allHallmarkNodeList.getTumorPromotingInflammation();
					  for(int j=0; j<thisHallmark.getSize(); j++)
					  {
						  String nodeName=thisHallmark.getNodeNameAt(j);
						  Node node=interactiveGraph.getNode(nodeName);
						  hallmarkClusterArray[i].addNode(node);

						  //0xF5DEB3 wheat
						  if(prevSelectedNode==null || nodeName.compareTo(prevSelectedNode.getNode())!=0)
							  setNodeToHallmarkColorSize(nodeName, "16113331", HALLMARK_SIZE, HALLMARK_INCREMENT_SIZE, false);
						  else if(nodeName.compareTo(prevSelectedNode.getNode())==0)
							  setNodeToHallmarkColorSize(nodeName, "16113331", HALLMARK_SIZE, HALLMARK_INCREMENT_SIZE, true);
						  hallmarkNode_tumor_promoting_inflammation.add(nodeName);
					  }
				  }
			  }

			  refreshApplet();
		  }
	  }

	  public void collapsePhysicalInteraction(ArrayList<String> nodeList)
	  {

	  }

	  public void updateEdgeTable_kinetics(ArrayList<edge> kineticsEdges)
	  {
		  ArrayList<Edge> edgeList=new ArrayList<Edge>();
			
			//reset all "weights" values to 0 (no kinetics information)
			interactiveGraph.writeLock();
			AttributeColumn weights=attributeModel.getEdgeTable().getColumn(GEPHI_WEIGHT);
			attributeColumnsController.fillColumnWithValue(attributeModel.getEdgeTable(), weights, "1");
			interactiveGraph.writeUnlock();
			
			//set relevant "hasKinetics" values 
			for(int i=0; i<kineticsEdges.size(); i++)
			{
				Node source=interactiveGraph.getNode(kineticsEdges.get(i).getSourceName());
				Node target=interactiveGraph.getNode(kineticsEdges.get(i).getTargetName());
				Edge edge=interactiveGraph.getEdge(source, target);
				if(edge!=null)
				{
					edgeList.add(edge);
					System.out.println("[updateEdgeTable_kinetics] edge:"+edge);
				}
			}
			Edge[] edgeArr=edgeList.toArray(new Edge[edgeList.size()]);
			System.out.println("[updateEdgeTable_kinetics] edgeArr size="+edgeArr.length);
			if(edgeArr.length>0)
			{
				interactiveGraph.writeLock();
				attributeColumnsController.fillEdgesColumnWithValue(edgeArr, weights, "5");
				interactiveGraph.writeUnlock();
			}
			
	/*		Graph thisGraph=interactiveGraphModel.getGraph(currView);
			PartitionController partitionController = Lookup.getDefault().lookup(PartitionController.class);
			Partition p = partitionController.buildPartition(weights, thisGraph);
			EdgeColorTransformer edgeColorTransformer = new EdgeColorTransformer();
			edgeColorTransformer.randomizeColors(p);
			partitionController.transform(p, edgeColorTransformer);
		*/	
			refreshApplet();
	  }
  }