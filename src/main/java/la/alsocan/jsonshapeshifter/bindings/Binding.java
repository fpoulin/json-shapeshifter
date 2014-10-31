package la.alsocan.jsonshapeshifter.bindings;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * @author Florian Poulin <https://github.com/fpoulin>
 * @param <T> Type of target value for the binding
 */
public abstract class Binding<T> {
	
	public abstract T getValue(JsonNode payload);
}
