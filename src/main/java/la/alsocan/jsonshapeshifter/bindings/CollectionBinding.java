package la.alsocan.jsonshapeshifter.bindings;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import java.util.List;
import la.alsocan.jsonshapeshifter.schemas.SchemaArrayNode;
import la.alsocan.jsonshapeshifter.utils.JsonPointerUtils;

/**
 * @author Florian Poulin <https://github.com/fpoulin>
 */
public class CollectionBinding extends Binding<ArrayNode> {
	
	private final SchemaArrayNode source;

	public CollectionBinding(SchemaArrayNode source) {
		this.source = source;
	}
	
	@Override
	public ArrayNode getValue(JsonNode payload, List<Integer> context) {
		return (ArrayNode)payload.at(JsonPointerUtils.resolvePointer(source.getPath(), context));
	}
}
