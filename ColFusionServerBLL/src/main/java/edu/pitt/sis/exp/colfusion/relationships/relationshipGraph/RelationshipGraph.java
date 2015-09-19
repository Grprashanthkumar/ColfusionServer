package edu.pitt.sis.exp.colfusion.relationships.relationshipGraph;

import java.util.List;

public class RelationshipGraph {
	private List<RelationshipGraphNode> nodes;
	private List<RelationshipGraphEdge> edges;

	/**
	 * @return the nodes
	 */
	public List<RelationshipGraphNode> getNodes() {
		return nodes;
	}

	/**
	 * @param nodes the nodes to set
	 */
	public void setNodes(final List<RelationshipGraphNode> nodes) {
		this.nodes = nodes;
	}

	/**
	 * @return the edges
	 */
	public List<RelationshipGraphEdge> getEdges() {
		return edges;
	}

	/**
	 * @param edges the edges to set
	 */
	public void setEdges(final List<RelationshipGraphEdge> edges) {
		this.edges = edges;
	}
}
