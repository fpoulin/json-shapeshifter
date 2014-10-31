package la.alsocan.jsonshapeshifter.bindings;

/**
 * @author Florian Poulin <https://github.com/fpoulin>
 * @param <T> Type of target value for the binding
 */
public abstract class Binding<T> {
	
	public abstract T getValue();
}
