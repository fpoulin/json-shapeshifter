package la.alsocan.jsonshapeshifter.bindings;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import java.util.Iterator;
import la.alsocan.jsonshapeshifter.schemas.SchemaNode;

/**
 * @author Florian Poulin - https://github.com/fpoulin
 */
public class ArrayNodeBinding extends AbstractNodeBinding<Iterator<JsonNode>> {

	public ArrayNodeBinding(SchemaNode source) {
		super(source);
	}

	@Override
	protected Iterator<JsonNode> readValue(JsonNode node) {
		return ((ArrayNode)node).elements();
	}
}
