package la.alsocan.jsonshapeshifter.bindings;

/**
 * @author Florian Poulin <https://github.com/fpoulin>
 * @param <T> Type of value for the static binding
 */
public abstract class StaticBinding<T> extends Binding {

	private final T value;
	
	public StaticBinding(T value) {
		this.value = value;
	}

	@Override
	public T getValue() {
		return value;
	}
}
