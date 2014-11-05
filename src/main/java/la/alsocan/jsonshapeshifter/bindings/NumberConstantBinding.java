package la.alsocan.jsonshapeshifter.bindings;

/**
 * @author Florian Poulin <https://github.com/fpoulin>
 */
public class NumberConstantBinding extends AbstractConstantBinding<Double> {

	public NumberConstantBinding(Number value) {
		super(value.doubleValue());
	}
}
