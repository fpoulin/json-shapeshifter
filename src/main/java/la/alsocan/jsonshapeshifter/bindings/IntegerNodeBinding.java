package la.alsocan.jsonshapeshifter.bindings;

import com.fasterxml.jackson.databind.JsonNode;
import java.util.List;
import la.alsocan.jsonshapeshifter.schemas.SchemaNode;
import la.alsocan.jsonshapeshifter.utils.JsonPointerUtils;

/**
 * @author Florian Poulin <https://github.com/fpoulin>
 */
public class IntegerNodeBinding extends Binding<Integer> {

	private final SchemaNode source;
	
	public IntegerNodeBinding(SchemaNode source) {
		this.source = source;
	}

	@Override
	public Integer getValue(JsonNode payload, List<Integer> context) {
		return payload.at(JsonPointerUtils.resolvePointer(source.getPath(), context)).asInt();
	}
}
