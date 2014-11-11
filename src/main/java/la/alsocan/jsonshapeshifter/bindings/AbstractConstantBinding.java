package la.alsocan.jsonshapeshifter.bindings;

import com.fasterxml.jackson.databind.JsonNode;
import java.util.List;

/**
 * @author Florian Poulin - https://github.com/fpoulin
 * @param <T> Type of value for the static binding
 */
public abstract class AbstractConstantBinding<T> extends Binding<T> {

	private final T value;
	
	public AbstractConstantBinding(T value) {
		this.value = value;
	}

	@Override
	public T getValue(JsonNode payload, List<Integer> context) {
		return value;
	}
}
