package la.alsocan.jsonshapeshifter.bindings;

import com.fasterxml.jackson.databind.JsonNode;
import java.util.List;
import la.alsocan.jsonshapeshifter.schemas.SchemaNode;
import la.alsocan.jsonshapeshifter.utils.JsonPointerUtils;

/**
 * @author Florian Poulin <https://github.com/fpoulin>
 * @param <T> Type of value for the node binding
 */
public abstract class AbstractNodeBinding<T> extends Binding<T> {

	private final SchemaNode source;
	
	public AbstractNodeBinding(SchemaNode source) {
		this.source = source;
	}

	@Override
	public final T getValue(JsonNode payload, List<Integer> context) {
		JsonNode node = payload.at(JsonPointerUtils.resolvePointer(source.getPath(), context));
		return node == null ? null : readValue(node);
	}
	
	protected abstract T readValue(JsonNode node);
}
