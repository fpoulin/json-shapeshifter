package la.alsocan.jsonshapeshifter.schemas.visitors;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ContainerNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import la.alsocan.jsonshapeshifter.schemas.SchemaArrayNode;
import la.alsocan.jsonshapeshifter.schemas.SchemaNode;
import la.alsocan.jsonshapeshifter.schemas.SchemaObjectNode;
import java.util.Random;
import java.util.Stack;
import org.apache.commons.lang3.RandomStringUtils;

/**
 * @author Florian Poulin <https://github.com/fpoulin>
 */
public class RandomJsonNodeBuilderVisitor implements ITypedNodeVisitor {

	private final static int RAND_STR_LENGTH = 10;
	private final static int RAND_INT_MAX = 1000;
	private final static Random RAND = new Random();
	private final Stack<ContainerNode> stack;
	private JsonNode node;

	public RandomJsonNodeBuilderVisitor() {
		
		stack = new Stack<>();
		ObjectMapper om = new ObjectMapper();
		stack.push(om.createObjectNode());
	}
	
	@Override
	public void visit(SchemaNode element) {
		switch(element.getType()) {
			case STRING:
				if (element.getName() != null) {
					((ObjectNode)stack.peek()).put(element.getName(), RandomStringUtils.randomAlphanumeric(RAND_STR_LENGTH));
				} else {
					for (int i=0; i<RAND.nextInt(10)+1; i++) {
						((ArrayNode)stack.peek()).add(RandomStringUtils.randomAlphanumeric(RAND_STR_LENGTH));
					}
				}
				break;
			case INTEGER:
				if (element.getName() != null) {
					((ObjectNode)stack.peek()).put(element.getName(), RAND.nextInt(RAND_INT_MAX));
				} else {
					for (int i=0; i<RAND.nextInt(10)+1; i++) {
						((ArrayNode)stack.peek()).add(RAND.nextInt(RAND_INT_MAX));
					}
				}
				break;
		}
	}

	@Override
	public void visitObject(SchemaObjectNode element) {
		if (element.getName() != null) {
			stack.push(((ObjectNode)stack.peek()).putObject(element.getName()));
		} else {
			stack.push(((ArrayNode)stack.peek()).addObject());
		}
	}

	@Override
	public void endVisitObject(SchemaObjectNode element) {
		stack.pop();
	}

	@Override
	public void visitArray(SchemaArrayNode element) {
		if (element.getName() != null) {
			stack.push(((ObjectNode)stack.peek()).putArray(element.getName()));
		} else {
			stack.push(((ArrayNode)stack.peek()).addArray());
		}
	}
	
	@Override
	public void endVisitArray(SchemaArrayNode element) {
		stack.pop();
	}

	@Override
	public void endVisit() {
		node = stack.pop();
	}
	
	public JsonNode getNode() {
		return node;
	}
}
