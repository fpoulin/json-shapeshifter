package la.alsocan.jsonshapeshifter.bindings;

import com.fasterxml.jackson.databind.JsonNode;
import la.alsocan.jsonshapeshifter.schemas.SchemaNode;

/**
 * @author Florian Poulin <https://github.com/fpoulin>
 */
public class NumberNodeBinding extends AbstractNodeBinding<Double> {

	public NumberNodeBinding(SchemaNode source) {
		super(source);
	}

	@Override
	protected Double readValue(JsonNode node) {
		return node.doubleValue();
	}
}
