package pk.edu.lums;

public class Node {

	private Boolean automated = false;
	private Boolean done = false;
	private Long timestamp;
	private String url;

	public Node(Boolean automated, Boolean done, Long timestamp, String url) {
		super();
		this.automated = automated;
		this.done = done;
		this.timestamp = timestamp;
		this.url = url;
	}

	public Node(String url, Long timestamp) {
		super();
		this.url = url;
		this.timestamp = timestamp;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Node other = (Node) obj;
		if (url == null) {
			if (other.url != null)
				return false;
		} else if (!url.equals(other.url))
			return false;
		return true;
	}

	public Boolean getAutomated() {
		return automated;
	}

	public Boolean getDone() {
		return done;
	}

	public Long getTimestamp() {
		return timestamp;
	}

	public String getUrl() {
		return url;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((url == null) ? 0 : url.hashCode());
		return result;
	}

	public void setAutomated(Boolean automated) {
		this.automated = automated;
	}

	public void setDone(Boolean done) {
		this.done = done;
	}

	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Node [automated=").append(automated).append(", done=")
				.append(done).append(", timestamp=").append(timestamp)
				.append(", url=").append(url).append("]");
		return builder.toString();
	}

}
