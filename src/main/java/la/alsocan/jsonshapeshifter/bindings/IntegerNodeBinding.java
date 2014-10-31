package la.alsocan.jsonshapeshifter.bindings;

import com.fasterxml.jackson.databind.JsonNode;
import la.alsocan.jsonshapeshifter.schemas.SchemaNode;

/**
 * @author Florian Poulin <https://github.com/fpoulin>
 */
public class IntegerNodeBinding extends Binding<Integer> {

	private final SchemaNode source;
	
	public IntegerNodeBinding(SchemaNode source) {
		this.source = source;
	}

	@Override
	public Integer getValue(JsonNode payload) {
		return payload.at(source.getPath()).asInt();
	}
}
