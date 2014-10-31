package la.alsocan.jsonshapeshifter.bindings;

import com.fasterxml.jackson.databind.JsonNode;
import la.alsocan.jsonshapeshifter.schemas.SchemaNode;

/**
 *
 * @author Florian Poulin <https://github.com/fpoulin>
 */
public class StringNodeBinding extends Binding<String> {

	private final SchemaNode source;
	
	public StringNodeBinding(SchemaNode source) {
		this.source = source;
	}

	@Override
	public String getValue(JsonNode payload) {
		return payload.at(source.getPath()).asText();
	}
}
