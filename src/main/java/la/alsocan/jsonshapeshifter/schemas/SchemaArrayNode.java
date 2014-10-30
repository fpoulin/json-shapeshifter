package la.alsocan.jsonshapeshifter.schemas;

import com.fasterxml.jackson.databind.JsonNode;
import la.alsocan.jsonshapeshifter.schemas.visitors.ITypedNodeVisitor;

/**
 * @author Florian Poulin <https://github.com/fpoulin>
 */
public class SchemaArrayNode extends SchemaNode {

	private SchemaNode child;
	
	public SchemaArrayNode(String name, String path, boolean required) {
		super(name, path, ENodeType.ARRAY, required);
	}
	
	public SchemaArrayNode withResolvedChild(JsonNode node) {
		
		// array of a single type (list)
		JsonNode items = node.get("items");
		if (items != null && items.has("type")) {
			child = buildSchemaNode(items, null, path + "/{i}", false);
			setChild(child);
			child.setParent(this);
			return this;
		}
		
		// untyped array or tuples are not supported
		throw new UnsupportedJsonSchemaException("Tuples and untyped arrays are currently not supported");
	}
	
	//<editor-fold defaultstate="collapsed" desc="Getter & setter">
	public final void setChild(SchemaNode element) {
		child = element;
	}
	
	public final SchemaNode getChild(){
		return child;
	}
	//</editor-fold>
	
	//<editor-fold defaultstate="collapsed" desc="Visitor">
	@Override
	protected void accept(ITypedNodeVisitor visitor) {
		visitor.visitArray(this);
		child.accept(visitor);
		visitor.endVisitArray(this);
	}
	//</editor-fold>
}
