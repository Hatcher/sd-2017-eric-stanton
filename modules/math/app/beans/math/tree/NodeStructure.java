package beans.math.tree;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class NodeStructure {
	private String HTMLid;
	private boolean pseudo;
	private List<NodeStructure> children;
	private String image;
	private TextNode text;
	
	@JsonProperty("HTMLid")
	public String getHTMLid() {
		return HTMLid;
	}
	@JsonProperty("HTMLid")
	public void setHTMLid(String hTMLid) {
		HTMLid = hTMLid;
	}
	public boolean isPseudo() {
		return pseudo;
	}
	public void setPseudo(boolean pseudo) {
		this.pseudo = pseudo;
	}
	public List<NodeStructure> getChildren() {
		return children;
	}
	public void setChildren(List<NodeStructure> children) {
		this.children = children;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public TextNode getText() {
		return text;
	}
	public void setText(TextNode text) {
		this.text = text;
	}
	
	
}
