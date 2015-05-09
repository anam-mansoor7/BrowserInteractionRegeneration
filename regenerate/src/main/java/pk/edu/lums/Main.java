package pk.edu.lums;

import java.io.IOException;
import java.util.List;

public class Main {
	public static void main(String[] args) {
		RecordedDataReader r = new RecordedDataReader(Constants.BASE_PATH
				+ "user1\\recorded-trace.txt");
		try {
			List<Node> recordedNodes = r.read();

			System.out.println("Total: " + recordedNodes.size());
			for (Node node : recordedNodes) {
				System.out.println(node.toString());

				Loader loader = new Loader();

				loader.load(node.getUrl());
				List<Node> automatedCalls = loader.getAutomatedCalls();
				List<Node> anchors = loader.getAnchors();

				loader.quit();

			}

		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}
}
