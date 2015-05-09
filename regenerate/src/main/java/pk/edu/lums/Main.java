package pk.edu.lums;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.WebElement;

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
				List<WebElement> anchors = loader.getAnchors();

				loader.quit();

			}

		} catch (IOException e) {
			System.out.println(e.getMessage());
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
}
