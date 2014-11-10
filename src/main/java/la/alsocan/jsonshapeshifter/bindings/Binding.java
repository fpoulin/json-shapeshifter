package la.alsocan.jsonshapeshifter.bindings;

import com.fasterxml.jackson.databind.JsonNode;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import la.alsocan.jsonshapeshifter.schemas.SchemaNode;

/**
 * @author Florian Poulin <https://github.com/fpoulin>
 * @param <T> Type of target value for the binding
 */
public abstract class Binding<T> {
	
	public abstract T getValue(JsonNode payload, List<Integer> context);
	
	public Set<SchemaNode> getSourceNodes() {
		return Collections.EMPTY_SET;
	}
}
