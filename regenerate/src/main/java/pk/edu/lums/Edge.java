package pk.edu.lums;

import org.jgraph.graph.DefaultEdge;
import org.openqa.selenium.WebElement;

public class Edge extends DefaultEdge {
	private static final long serialVersionUID = -539034690558435065L;

	private WebElement element;
	private Float similarity;
	private EdgeType type = EdgeType.unconfirmed;
	
	public Edge()
	{
		super();
	}

	public Edge(EdgeType type, Float similarity) {
		super();
		this.type = type;
		this.similarity = similarity;
	}

	public WebElement getElement() {
		return element;
	}

	public Float getSimilarity() {
		return similarity;
	}

	public EdgeType getType() {
		return type;
	}

	public void setElement(WebElement element) {
		this.element = element;
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
				.append(", type=").append(type).append(", element=")
				.append(element).append("]");
		return builder.toString();
	}

}
