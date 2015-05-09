package pk.edu.lums;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RecordedDataReader {

	private static final int DEFAULT_NODES = 200;
	private String file;

	public RecordedDataReader(String file) {
		this.file = file;

	}

	List<Node> read() throws IOException {
		List<Node> nodes = new ArrayList<Node>(DEFAULT_NODES);

		BufferedReader br = new BufferedReader(new FileReader(new File(file)));

		String line = null;
		Long timestamp = 0l;
		while ((line = br.readLine()) != null) {

			line = line.trim();
			if ("".equals(line)) {
				continue;
			}

			nodes.add(new Node(line, ++timestamp));
		}

		br.close();
		return nodes;
	}
}
