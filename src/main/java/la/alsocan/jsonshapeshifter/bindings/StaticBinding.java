package la.alsocan.jsonshapeshifter.bindings;

import com.fasterxml.jackson.databind.JsonNode;
import java.util.List;

/**
 * @author Florian Poulin <https://github.com/fpoulin>
 * @param <T> Type of value for the static binding
 */
public abstract class StaticBinding<T> extends Binding<T> {

	private final T value;
	
	public StaticBinding(T value) {
		this.value = value;
	}

	@Override
	public T getValue(JsonNode payload, List<Integer> context) {
		return value;
	}
	
}
