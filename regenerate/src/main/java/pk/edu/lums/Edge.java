package pk.edu.lums;

import org.jgraph.graph.DefaultEdge;

public class Edge extends DefaultEdge {
	private static final long serialVersionUID = -539034690558435065L;

	private Float similarity;
	private EdgeType type = EdgeType.unconfirmed;

	public Edge(EdgeType type, Float similarity) {
		super();
		this.type = type;
		this.similarity = similarity;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Edge other = (Edge) obj;
		if (similarity == null) {
			if (other.similarity != null)
				return false;
		} else if (!similarity.equals(other.similarity))
			return false;
		if (type != other.type)
			return false;
		return true;
	}

	public Float getSimilarity() {
		return similarity;
	}

	public EdgeType getType() {
		return type;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((similarity == null) ? 0 : similarity.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		return result;
	}

	public void setSimilarity(Float similarity) {
		this.similarity = similarity;
	}

	public void setType(EdgeType type) {
		this.type = type;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Edge [similarity=").append(similarity)
				.append(", type=").append(type).append("]");
		return builder.toString();
	}

}
