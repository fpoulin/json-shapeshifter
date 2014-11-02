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
import la.alsocan.jsonshapeshifter.bindings.CollectionBinding;

/**
 * @author Florian Poulin <https://github.com/fpoulin>
 */
public class Transformation {
	
	public static final String DEFAULT_STRING = "?";
	public static final Integer DEFAULT_INTEGER = 0;
	
	private final Schema target;
	
	private final Map<SchemaNode, Binding<?>> bindings;
	
	public Transformation (Schema target) {
		this.target = target;
		bindings = new TreeMap<>();
	}
	
	/**
	 * Iterates through the target schema to look for nodes requiring binding. This uses
	 * an iterator on the schema (which traverses the whole structure) but skips nodes 
	 * which already have a binding defined as well as object nodes (which do not require 
	 * any binding). The iteration order is based on the Json schema definition.
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
						  || ENodeType.OBJECT.equals(node.getType()));
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
		for (SchemaNode tNode : target.getChildren()) {
			resolve(om, tNode, root, payload, pointerContext);
		}
		return root;
	}

	public void resolve(ObjectMapper om, SchemaNode targetSchemaNode, JsonNode parentNode, JsonNode payload, List<Integer> pointerContext) {
		switch(targetSchemaNode.getType()) {
			case OBJECT:
				ObjectNode oNode = om.createObjectNode();
				for (SchemaNode tChildNode : ((SchemaObjectNode)targetSchemaNode).getChildren()) {
					resolve(om, tChildNode, oNode, payload, pointerContext);
				}
				if (parentNode instanceof ObjectNode) {
					((ObjectNode)parentNode).set(targetSchemaNode.getName(), oNode);
				} else {
					((ArrayNode)parentNode).add(oNode);
				}
				break;
			case ARRAY:
				ArrayNode aNode = om.createArrayNode();
				SchemaNode tChildNode = ((SchemaArrayNode)targetSchemaNode).getChild();
				CollectionBinding binding = (CollectionBinding)bindings.get(targetSchemaNode);
				int index = 0;
				pointerContext.add(index);
				for (JsonNode node : binding.getValue(payload, pointerContext)) {
					resolve(om, tChildNode, aNode, payload, pointerContext);
					pointerContext.set(pointerContext.size()-1, ++index);
				}
				pointerContext.remove(pointerContext.size()-1);
				if (parentNode instanceof ObjectNode) {
					((ObjectNode)parentNode).set(targetSchemaNode.getName(), aNode);
				} else {
					((ArrayNode)parentNode).add(aNode);
				}
				break;
			case INTEGER:
				if (parentNode instanceof ObjectNode) {
					((ObjectNode)parentNode).put(targetSchemaNode.getName(), resolveIntegerValue(targetSchemaNode, payload, pointerContext));
				} else {
					((ArrayNode)parentNode).add(resolveIntegerValue(targetSchemaNode, payload, pointerContext));
				}
				break;
			case STRING:
				if (parentNode instanceof ObjectNode) {
					((ObjectNode)parentNode).put(targetSchemaNode.getName(), resolveStringValue(targetSchemaNode, payload, pointerContext));
				} else {
					((ArrayNode)parentNode).add(resolveStringValue(targetSchemaNode, payload, pointerContext));
				}
				break;
		}
	}
	
	private Integer resolveIntegerValue (SchemaNode node, JsonNode payload, List<Integer> pointerContext) {
		
		Binding<Integer> b = (Binding<Integer>)bindings.get(node);
		Integer value;
		if (b == null) {
			value = DEFAULT_INTEGER;
		} else {
			value = b.getValue(payload, pointerContext);
		}
		return value;
	}
	
	private String resolveStringValue (SchemaNode node, JsonNode payload, List<Integer> pointerContext) {
		
		Binding<String> b = (Binding<String>)bindings.get(node);
		String value;
		if (b == null) {
			value = DEFAULT_STRING;
		} else {
			value = b.getValue(payload, pointerContext);
		}
		return value;
	}
}
