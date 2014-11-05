package la.alsocan.jsonshapeshifter.bindings;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.NullNode;
import java.util.Iterator;
import java.util.List;

/**
 * @author Florian Poulin <https://github.com/fpoulin>
 */
public class ArrayConstantBinding extends Binding<Iterator<JsonNode>> {

	private final int nbIterations;

	public ArrayConstantBinding(int nbIterations) {
		this.nbIterations = nbIterations;
	}
	
	@Override
	public Iterator<JsonNode> getValue(JsonNode payload, List<Integer> context) {
		return new Iterator<JsonNode>() {
			
			private int count = 0;
			
			@Override
			public boolean hasNext() {
				return count < nbIterations;
			}

			@Override
			public JsonNode next() {
				count++;
				return NullNode.instance;
			}
		};
	}
}
