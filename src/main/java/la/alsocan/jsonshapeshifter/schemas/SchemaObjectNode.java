package la.alsocan.jsonshapeshifter.schemas;

import com.fasterxml.jackson.databind.JsonNode;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * @author Florian Poulin - https://github.com/fpoulin
 */
public class SchemaObjectNode extends SchemaNode {

	private final List<SchemaNode> children = new LinkedList<>();
	
	protected SchemaObjectNode(String name, String path, boolean required) {
		super(name, path, ENodeType.OBJECT, required);
	}

	protected SchemaObjectNode withResolvedChildren(JsonNode node) {
		JsonNode props = node.get("properties");
		
		// untyped objects not supported
		if (props == null) {
			throw new UnsupportedJsonSchemaException("Untyped objects are currently not supported");
		}
		
		// collect required element names
		Set<String> reqSet = new HashSet<>();
		JsonNode reqs = node.get("required");
		if (reqs != null) {
			Iterator<JsonNode> itReqs = reqs.elements();
			while(itReqs.hasNext()) {
				reqSet.add(itReqs.next().asText());
			}
		}
		
		// build children elements
		Iterator<String> itName = props.fieldNames();
		Iterator<JsonNode> itNode = props.elements();
		while (itNode.hasNext()) {
			String childName = itName.next();
			SchemaNode typedChild = buildSchemaNode(itNode.next(), childName, path + "/" + childName, reqSet.contains(childName));
			children.add(typedChild);
			typedChild.setParent(this);
		}
		return this;
	}

	/**
	 * Get the schema nodes for the elements contained in this object node
	 * @return the schema nodes for the elements contained in this object node
	 */
	public List<SchemaNode> getChildren() {
		return children;
	}
}
