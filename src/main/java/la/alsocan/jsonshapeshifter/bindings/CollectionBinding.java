package la.alsocan.jsonshapeshifter.bindings;

import com.fasterxml.jackson.databind.JsonNode;
import java.util.List;
import la.alsocan.jsonshapeshifter.schemas.SchemaArrayNode;

/**
 *
 * @author Florian Poulin <https://github.com/fpoulin>
 */
public class CollectionBinding extends Binding<SchemaArrayNode> {

	private SchemaArrayNode node;

	public CollectionBinding(SchemaArrayNode node) {
		this.node = node;
	}
	
	@Override
	public SchemaArrayNode getValue(JsonNode payload, List<Integer> context) {
		return node;
	}
}
