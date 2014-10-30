package la.alsocan.jsonshapeshifter.schemas;

import com.fasterxml.jackson.databind.JsonNode;
import la.alsocan.jsonshapeshifter.schemas.visitors.ITypedNodeVisitor;

/**
 * @author Florian Poulin <https://github.com/fpoulin>
 */
public class SchemaNode implements Comparable<SchemaNode> {
	
	protected final String name;
	protected final String path;
	protected final ENodeType type;
	protected final boolean required;
	private SchemaNode parent;
	
	public SchemaNode(String name, String path, ENodeType type, boolean required) {
		this.name = name;
		this.path = path;
		this.type = type;
		this.required = required;
	}
	
	protected static final SchemaNode buildSchemaNode(JsonNode node, String name, String path, boolean required) {
	
		switch (node.get("type") != null ? node.get("type").asText() : "?") {
			case "object":
				return new SchemaObjectNode(name, path, required).withResolvedChildren(node);
			case "array":
				return new SchemaArrayNode(name, path, required).withResolvedChild(node);
			case "string":
				return new SchemaNode(name, path, ENodeType.STRING, required);
			case "integer":
				return new SchemaNode(name, path, ENodeType.INTEGER, required);
			default:
				throw new UnsupportedJsonSchemaException("Unknown type for element '"+path+"'");
		}
	}

	//<editor-fold defaultstate="collapsed" desc="Getter & Setter">
	public SchemaNode getParent() {
		return parent;
	}
	
	public void setParent(SchemaNode parent) {
		this.parent = parent;
	}
	
	public String getName() {
		return name;
	}
	
	public String getPath() {
		return path;
	}
	
	public ENodeType getType() {
		return type;
	}
	
	public boolean isRequired() {
		return required;
	}
	//</editor-fold>
	
	//<editor-fold defaultstate="collapsed" desc="Visitor">
	/**
	 * @param visitor A visitor (from the visitor pattern)
	 */
	protected void accept(ITypedNodeVisitor visitor) {
		visitor.visit(this);
	}
	//</editor-fold>

	@Override
	public int compareTo(SchemaNode o) {
		return path.compareTo(o.path);
	}
}
