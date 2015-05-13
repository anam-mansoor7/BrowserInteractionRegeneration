package pk.edu.lums;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import org.jgrapht.Graph;
import org.jgrapht.ext.DOTExporter;
import org.jgrapht.ext.EdgeNameProvider;
import org.jgrapht.ext.IntegerNameProvider;
import org.jgrapht.ext.VertexNameProvider;
import org.jgrapht.graph.DefaultDirectedGraph;

public class SampleGraph {

	public static void writeDot(String filename, Graph<Node, Edge> graph) {
		DOTExporter<Node, Edge> export = new DOTExporter<Node, Edge>(
				new IntegerNameProvider<Node>(),
				new VertexNameProvider<Node>() {
					@Override
					public String getVertexName(Node arg0) {
						return arg0.toString();
					}
				}, new EdgeNameProvider<Edge>() {
					@Override
					public String getEdgeName(Edge arg0) {
						return arg0.toString();
					}
				});
		try {
			BufferedWriter out = new BufferedWriter(new FileWriter(filename));
			export.export(out, graph);
			out.close();
		} catch (IOException e) {
			System.err.println("Error: could not write spanning tree dot file");
		}
	}

	public static void main(String[] args) {

		Node a = new Node("a", 1l);
		Node b = new Node("b", 1l);
		Node c = new Node("c", 1l);
		Node d = new Node("d", 1l);
		Node e = new Node("e", 1l);
		Node f = new Node("f", 1l);
		Node g = new Node("g", 1l);

		Graph<Node, Edge> graph = new DefaultDirectedGraph<Node, Edge>(
				Edge.class);
		graph.addVertex(a);
		graph.addVertex(b);
		graph.addVertex(c);
		graph.addVertex(d);
		graph.addVertex(e);
		graph.addVertex(f);
		graph.addVertex(g);

		graph.addEdge(a, b);
		graph.addEdge(b, c);
		graph.addEdge(c, d);
		graph.addEdge(b, d);

		graph.addEdge(e, d, new Edge(null, 0.1f, EdgeType.confirmed));
		graph.addEdge(d, f);

		writeDot("graph.dot", graph);
		System.out.println("Completed!");
	}

}
