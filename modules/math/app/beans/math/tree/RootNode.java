package beans.math.tree;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
@JsonIgnoreProperties(ignoreUnknown = true)
public class RootNode {
	private NodeStructure nodeStructure;

	public NodeStructure getNodeStructure() {
		return nodeStructure;
	}

	public void setNodeStructure(NodeStructure nodeStructure) {
		this.nodeStructure = nodeStructure;
	}
	
}
