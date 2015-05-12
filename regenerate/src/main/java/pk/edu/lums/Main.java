package pk.edu.lums;

import info.debatty.java.stringsimilarity.Jaccard;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.FileAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.jgrapht.Graph;
import org.jgrapht.ext.DOTExporter;
import org.jgrapht.ext.EdgeNameProvider;
import org.jgrapht.ext.IntegerNameProvider;
import org.jgrapht.ext.VertexNameProvider;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.openqa.selenium.WebElement;

public class Main {
	private static Logger logger = Logger.getLogger(Main.class);

	static {
		System.out.println("In static block");
		String runPath = Constants.BASE_PATH + "user1\\run";
		Integer run = getRunNumber(runPath);
		Constants.BASE_USER_PATH = runPath + run + "\\";
		System.out.println(Constants.BASE_USER_PATH);
		File f = new File(Constants.BASE_USER_PATH);
		f.mkdirs();

		ConsoleAppender console = new ConsoleAppender(); // create appender
		// configure the appender
		String PATTERN = "%d [%p|%c|%C{1}] %m%n";
		console.setLayout(new PatternLayout(PATTERN));
		console.setThreshold(Level.FATAL);
		console.activateOptions();
		// add appender to any Logger (here is root)
		Logger.getRootLogger().addAppender(console);

		FileAppender fa = new FileAppender();
		fa.setName("FileLogger");
		fa.setFile(Constants.BASE_USER_PATH + "mylog.log");
		fa.setLayout(new PatternLayout("%d %-5p [%c{1}] %m%n"));
		fa.setThreshold(Level.DEBUG);
		fa.setAppend(true);
		fa.activateOptions();

		// add appender to any Logger (here is root)
		Logger.getRootLogger().addAppender(fa);

	}

	private static Integer getRunNumber(String runPath) {
		int run = 1;
		for (;; run++) {
			File f = new File(runPath + run);
			if (!f.exists()) {
				break;
			}
		}
		return run;
	}

	public static void main(String[] args) {

		Graph<Node, Edge> graph = new DefaultDirectedGraph<Node, Edge>(
				Edge.class);
		RecordedDataReader recodedData = new RecordedDataReader(
				Constants.BASE_PATH + "user1\\recorded-trace.txt");
		try {
			List<Node> recordedNodes = recodedData.read();

			logger.info("Total: " + recordedNodes.size());

			for (int currentNodeIndex = 0; currentNodeIndex < recordedNodes
					.size(); currentNodeIndex++) {

				Node currentNode = recordedNodes.get(currentNodeIndex);

				logger.info(currentNode.toString());
				if (!currentNode.getDone()) {

					Loader loader = new Loader();
					try {

						loader.load(currentNode.getUrl());

						graph.addVertex(currentNode);

						List<Node> automatedCalls = loader.getAutomatedCalls();
						markAndDoneAutomatedCalls(currentNodeIndex,
								recordedNodes, automatedCalls);

						List<WebElement> anchors = loader.getAnchors();
						List<WebElement> clickables = loader.getClickables();

						currentNode.setDone(true);

					} catch (Exception e) {
						logger.info(e.getMessage());
					} finally {
						loader.quit();
					}
				}
			}

		} catch (IOException e) {
			logger.info(e.getMessage());
		}

		writeDot(Constants.BASE_USER_PATH + "mined-graph.dot", graph);
	}

	private static void markAndDoneAutomatedCalls(Integer currentNodeIndex,
			List<Node> recordedNodes, List<Node> automatedCalls) {
		// check equality
		markDone(currentNodeIndex, recordedNodes, automatedCalls, true, true);
		// check similarity
		markDone(currentNodeIndex, recordedNodes, automatedCalls, false, true);

	}

	private static void markDone(Integer currentNodeIndex,
			List<Node> recordedNodes, List<Node> automatedCalls,
			boolean checkEquality, boolean markAutomated) {

		if (recordedNodes == null || automatedCalls == null) {
			return;
		}

		for (int nextNodeIndex = currentNodeIndex + 1; nextNodeIndex < Constants.REQUESTS_TO_CHECK
				&& nextNodeIndex < recordedNodes.size(); nextNodeIndex++) {

			Node currentNode = recordedNodes.get(nextNodeIndex);
			String currentUrl = currentNode.getUrl();

			if (currentUrl == null || "".equals(currentUrl.trim())) {
				continue;
			}

			for (Node autoNode : automatedCalls) {

				String autoUrl = autoNode.getUrl() == null ? null : autoNode
						.getUrl().trim();

				boolean stringEqual = checkEquality
						&& currentUrl.equals(autoUrl);
				boolean stringSimilar = !checkEquality
						&& new Jaccard().similarity(currentUrl, autoUrl) > Constants.REQUESTS_SIMILARITY;

				if (stringEqual || stringSimilar) {
					currentNode.setAutomated(markAutomated);
					currentNode.setDone(true);
					break;
				}
			}
		}
	}

	private static List<Node> anchorsToNodes(List<WebElement> anchors,
			String currentUrl) {
		List<Node> nodes = new ArrayList<Node>(anchors.size());

		Long timestamp = 0l;
		for (WebElement anchor : anchors) {
			String url = anchor.getAttribute("href");
			if (url != null && currentUrl != null && !url.startsWith("http")) {
				String mid = currentUrl.endsWith("/") || url.startsWith("/") ? ""
						: "/";
				url = currentUrl + mid + url;
			}

			nodes.add(new Node(url, ++timestamp));
		}
		return nodes;
	}

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
}
