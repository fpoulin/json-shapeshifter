package la.alsocan.jsonshapeshifter.bindings;

import com.fasterxml.jackson.databind.JsonNode;
import la.alsocan.jsonshapeshifter.schemas.SchemaNode;

/**
 * @author Florian Poulin <https://github.com/fpoulin>
 */
public class NumberNodeBinding extends AbstractNodeBinding<Number> {

	public NumberNodeBinding(SchemaNode source) {
		super(source);
	}

	@Override
	protected Number readValue(JsonNode node) {
		return node.numberValue();
	}
}
