package graph;


import org.gephi.data.attributes.api.AttributeModel;
import org.gephi.graph.api.Graph;
import org.gephi.preview.api.Item;
import org.gephi.preview.api.PreviewController;
import org.gephi.preview.api.PreviewProperties;
import org.gephi.preview.spi.ItemBuilder;
import org.gephi.project.api.Workspace;
import org.openide.util.Lookup;
import org.openide.util.lookup.ServiceProvider;

@ServiceProvider(service = ItemBuilder.class)
public class Gephi_graph_previewMouseItemBuilder implements ItemBuilder {

    @Override
    public Item[] getItems(Graph graph, AttributeModel attributeModel) {
        Workspace workspace = attributeModel.getWorkspace();
        PreviewProperties properties = Lookup.getDefault().lookup(PreviewController.class).getModel(workspace).getProperties();

        if (properties.hasProperty("display-label.node.id")) {
            String nodeId = properties.getStringValue("display-label.node.id");
            return new Item[]{new Gephi_graph_previewMouseLabelItem(graph.getNode(nodeId))};
        } else {
            return new Item[0];
        }
    }

    @Override
    public String getType() {
        return "some.type-label";
    }
}
