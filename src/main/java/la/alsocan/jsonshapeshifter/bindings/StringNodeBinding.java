package la.alsocan.jsonshapeshifter.bindings;

import com.fasterxml.jackson.databind.JsonNode;
import java.util.List;
import la.alsocan.jsonshapeshifter.schemas.SchemaNode;
import la.alsocan.jsonshapeshifter.utils.JsonPointerUtils;

/**
 * @author Florian Poulin <https://github.com/fpoulin>
 */
public class StringNodeBinding extends Binding<String> {

	private final SchemaNode source;
	
	public StringNodeBinding(SchemaNode source) {
		this.source = source;
	}

	@Override
	public String getValue(JsonNode payload, List<Integer> context) {
		return payload.at(JsonPointerUtils.resolvePointer(source.getPath(), context)).asText();
	}
}
