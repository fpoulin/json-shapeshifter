package la.alsocan.jsonshapeshifter;

import la.alsocan.jsonshapeshifter.bindings.Binding;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.util.ArrayList;
import la.alsocan.jsonshapeshifter.schemas.ENodeType;
import la.alsocan.jsonshapeshifter.schemas.SchemaArrayNode;
import la.alsocan.jsonshapeshifter.schemas.SchemaNode;
import la.alsocan.jsonshapeshifter.schemas.SchemaObjectNode;
import la.alsocan.jsonshapeshifter.schemas.Schema;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.TreeMap;

/**
 * @author Florian Poulin <https://github.com/fpoulin>
 */
public class Transformation {
	
	private final Schema target;
	
	private final Map<SchemaNode, Binding<?>> bindings;
	
	public Transformation (Schema target) {
		this.target = target;
		bindings = new TreeMap<>();
	}
	
	/**
	 * Iterates through the target schema to look for nodes requiring binding. This uses
	 * an iterator on the schema (which traverses the whole structure) but skips nodes 
	 * which already have a binding defined as well as object and null nodes (which do not 
	 * require any binding). The iteration order is based on the Json schema definition.
	 * 
	 * @return An iterator for nodes still requiring a binding
	 */
	public Iterator<SchemaNode> toBindIterator() {
		
		final Iterator<SchemaNode> itNodes = target.iterator();
		return new Iterator<SchemaNode>() {

			private SchemaNode remainingNode = getNextNode();
			
			@Override
			public boolean hasNext() {
				return remainingNode != null;
			}

			@Override
			public SchemaNode next() {
				if (remainingNode == null) {
					throw new NoSuchElementException();
				}
				SchemaNode toReturn = remainingNode;
				remainingNode = getNextNode();
				return toReturn;
			}

			private SchemaNode getNextNode() {
				SchemaNode node = null;
				do {
					if (!itNodes.hasNext()) {
						return null;
					}
					node = itNodes.next();
				} while (bindings.containsKey(node) 
						  || ENodeType.OBJECT.equals(node.getType())
						  || ENodeType.NULL.equals(node.getType()));
				return node;
			}
		};
	}
	
	public void addBinding(SchemaNode node, Binding<?> binding) {
		bindings.put(node, binding);
	}
	
	/**
	 * Temporary method to apply a transformation.
	 * @param payload the payload to transform
	 * @return A transformed Json payload following the transformation definition.
	 */
	public JsonNode apply(JsonNode payload) {
		ObjectMapper om = new ObjectMapper();
		ObjectNode root = om.createObjectNode();
		List<Integer> pointerContext = new ArrayList<>();
		target.getChildren().stream().forEach((tNode) -> {
			resolve(om, tNode, root, payload, pointerContext);
		});
		return root;
	}

	// FIXME factorize this huge method
	public void resolve(ObjectMapper om, SchemaNode node, JsonNode parentNode, JsonNode payload, List<Integer> pointerContext) {
		switch(node.getType()) {
		case OBJECT:
			ObjectNode oNode = om.createObjectNode();
			((SchemaObjectNode)node).getChildren().stream().forEach((tChildNode) -> {
				resolve(om, tChildNode, oNode, payload, pointerContext);
			});
			if (parentNode.isObject()) {
				((ObjectNode)parentNode).set(node.getName(), oNode);
			} else {
				((ArrayNode)parentNode).add(oNode);
			}
			break;
		case ARRAY:
			ArrayNode aNode = om.createArrayNode();
			SchemaNode tChildNode = ((SchemaArrayNode)node).getChild();
			int index = 0;
			pointerContext.add(index);
			Iterator<JsonNode> it = (Iterator<JsonNode>)resolveValue(node, payload, pointerContext);
			while (it.hasNext()) {
				it.next();
				resolve(om, tChildNode, aNode, payload, pointerContext);
				pointerContext.set(pointerContext.size()-1, ++index);
			}
			pointerContext.remove(pointerContext.size()-1);
			if (parentNode.isObject()) {
				((ObjectNode)parentNode).set(node.getName(), aNode);
			} else {
				((ArrayNode)parentNode).add(aNode);
			}
			break;
		case BOOLEAN:
			if (parentNode.isObject()) {
				((ObjectNode)parentNode).put(node.getName(), (Boolean)resolveValue(node, payload, pointerContext));
			} else {
				((ArrayNode)parentNode).add((Boolean)resolveValue(node, payload, pointerContext));
			}
			break;
		case INTEGER:
			if (parentNode.isObject()) {
				((ObjectNode)parentNode).put(node.getName(), (Integer)resolveValue(node, payload, pointerContext));
			} else {
				((ArrayNode)parentNode).add((Integer)resolveValue(node, payload, pointerContext));
			}
			break;
		case NUMBER:
			if (parentNode.isObject()) {
				((ObjectNode)parentNode).put(node.getName(), (Double)resolveValue(node, payload, pointerContext));
			} else {
				((ArrayNode)parentNode).add((Double)resolveValue(node, payload, pointerContext));
			}
			break;
		case NULL:
			if (parentNode.isObject()) {
				((ObjectNode)parentNode).putNull(node.getName());
			} else {
				((ArrayNode)parentNode).addNull();
			}
			break;
		case STRING:
			if (parentNode.isObject()) {
				((ObjectNode)parentNode).put(node.getName(), (String)resolveValue(node, payload, pointerContext));
			} else {
				((ArrayNode)parentNode).add((String)resolveValue(node, payload, pointerContext));
			}
			break;
		}
	}
	
	private Object resolveValue(SchemaNode node, JsonNode payload, List<Integer> pointerContext) {
		Binding<?> b = (Binding<?>)bindings.get(node);
		Object value;
		if (b == null || (value = b.getValue(payload, pointerContext)) == null) {
			switch(node.getType()) {
				case ARRAY:
					return Defaults.DEFAULT_ARRAY;
				case BOOLEAN:
					return Defaults.DEFAULT_BOOLEAN;
				case INTEGER:
					return Defaults.DEFAULT_INTEGER;
				case NUMBER:
					return Defaults.DEFAULT_NUMBER;
				case STRING:
					return Defaults.DEFAULT_STRING;
				default:
					return null;
			}
		}
		return value;
	}
}
