package la.alsocan.jsonshapeshifter.bindings;

import com.fasterxml.jackson.databind.JsonNode;
import la.alsocan.jsonshapeshifter.schemas.SchemaNode;

/**
 * @author Florian Poulin - https://github.com/fpoulin
 */
public class IntegerNodeBinding extends AbstractNodeBinding<Integer> {

	public IntegerNodeBinding(SchemaNode source) {
		super(source);
	}

	@Override
	protected Integer readValue(JsonNode node) {
		return node.asInt();
	}
}
