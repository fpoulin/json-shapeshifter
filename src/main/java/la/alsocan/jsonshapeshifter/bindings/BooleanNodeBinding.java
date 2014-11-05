package la.alsocan.jsonshapeshifter.bindings;

import com.fasterxml.jackson.databind.JsonNode;
import la.alsocan.jsonshapeshifter.schemas.SchemaNode;

/**
 * @author Florian Poulin <https://github.com/fpoulin>
 */
public class BooleanNodeBinding extends AbstractNodeBinding<Boolean> {

	public BooleanNodeBinding(SchemaNode source) {
		super(source);
	}

	@Override
	protected Boolean readValue(JsonNode node) {
		return node.asBoolean();
	}
}
