package la.alsocan.jsonshapeshifter.bindings;

import com.fasterxml.jackson.databind.JsonNode;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import la.alsocan.jsonshapeshifter.schemas.SchemaNode;

/**
 * @author Florian Poulin <https://github.com/fpoulin>
 * @param <T> Type of value for the node binding
 */
public abstract class AbstractNodeBinding<T> extends Binding<T> {

	private final SchemaNode source;
	
	public AbstractNodeBinding(SchemaNode source) {
		this.source = source;
		if (source == null) {
			throw new IllegalBindingException();
		}
	}
	
	@Override
	public Set<SchemaNode> getSourceNodes() {
		Set<SchemaNode> nodes = new HashSet<>();
		nodes.add(source);
		return nodes;
	}

	@Override
	public final T getValue(JsonNode payload, List<Integer> context) {
		JsonNode node = payload.at(jsonPointer(source.getSchemaPointer(), context));
		return node == null ? null : readValue(node);
	}
	
	protected abstract T readValue(JsonNode node);
	
	private static final String PATH_DELIMITER_REGEX = "\\{i\\}";
	static String jsonPointer(String schemaPointer, List<Integer> context) {
		for (Integer index : context) {
			schemaPointer = schemaPointer.replaceFirst(PATH_DELIMITER_REGEX, String.valueOf(index));
		}
		return schemaPointer;
	}
}
