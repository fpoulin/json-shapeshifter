package la.alsocan.jsonshapeshifter.schemas;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * @author Florian Poulin <https://github.com/fpoulin>
 */
public class SchemaArrayNode extends SchemaNode {

	private SchemaNode child;
	
	protected SchemaArrayNode(String name, String path, boolean required) {
		super(name, path, ENodeType.ARRAY, required);
	}
	
	protected SchemaArrayNode withResolvedChild(JsonNode node) {
		
		// array of a single type (list)
		JsonNode items = node.get("items");
		if (items != null && items.has("type")) {
			child = buildSchemaNode(items, null, path + "/{i}", false);
			child.setParent(this);
			return this;
		}
		
		// untyped array or tuples are not supported
		throw new UnsupportedJsonSchemaException("Tuples and untyped arrays are currently not supported");
	}
	
	/**
	 * Get the schema node for the elements contained in this array node
	 * @return the schema node for the elements contained in this array node
	 */
	public final SchemaNode getChild(){
		return child;
	}
}
