package la.alsocan.jsonshapeshifter.bindings;

import com.fasterxml.jackson.databind.JsonNode;
import la.alsocan.jsonshapeshifter.schemas.SchemaNode;

/**
 * @author Florian Poulin - https://github.com/fpoulin
 */
public class StringNodeBinding extends AbstractNodeBinding<String> {

	public StringNodeBinding(SchemaNode source) {
		super(source);
	}

	@Override
	protected String readValue(JsonNode node) {
		return node.asText();
	}
}
