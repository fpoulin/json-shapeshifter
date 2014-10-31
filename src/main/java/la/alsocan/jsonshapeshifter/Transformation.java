package la.alsocan.jsonshapeshifter;

import la.alsocan.jsonshapeshifter.bindings.Binding;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import la.alsocan.jsonshapeshifter.schemas.ENodeType;
import la.alsocan.jsonshapeshifter.schemas.SchemaArrayNode;
import la.alsocan.jsonshapeshifter.schemas.SchemaNode;
import la.alsocan.jsonshapeshifter.schemas.SchemaObjectNode;
import la.alsocan.jsonshapeshifter.schemas.Schema;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.TreeMap;

/**
 * @author Florian Poulin <https://github.com/fpoulin>
 */
public class Transformation {
	
	public static final String DEFAULT_STRING = "?";
	public static final Integer DEFAULT_INTEGER = 0;
	
	private final Schema source;
	private final Schema target;
	
	private final Map<SchemaNode, Binding<?>> bindings;
	
	public Transformation (Schema source, Schema target) {
		this.source = source;
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
				if (itNodes.hasNext()) {
					do {
						node = itNodes.next();
					} while (itNodes.hasNext() && 
							  (bindings.containsKey(node) 
							  || ENodeType.OBJECT.equals(node.getType())));
				}
				return node;
			}
		};
	}
	
	public void addBinding(SchemaNode node, Binding<?> binding) {
		bindings.put(node, binding);
	}
	
	/**
	 * Temporary method to apply a transformation. Does not take any payload for now.
	 * 
	 * @return A transformed Json payload following the transformation definition.
	 */
	public JsonNode apply() {
		ObjectMapper om = new ObjectMapper();
		ObjectNode root = om.createObjectNode();
		for (SchemaNode tNode : target.getChildren()) {
			resolve(om, tNode, root);
		}
		return root;
	}

	public void resolve(ObjectMapper om, SchemaNode tNode, JsonNode parent) {
		switch(tNode.getType()) {
			case OBJECT:
				ObjectNode oNode = om.createObjectNode();
				for (SchemaNode tChildNode : ((SchemaObjectNode)tNode).getChildren()) {
					resolve(om, tChildNode, oNode);
				}
				if (parent instanceof ObjectNode) {
					((ObjectNode)parent).set(tNode.getName(), oNode);
				} else {
					((ArrayNode)parent).add(oNode);
				}
				break;
			case ARRAY:
				ArrayNode aNode = om.createArrayNode();
				SchemaNode tChildNode = ((SchemaArrayNode)tNode).getChild();
				for (int i=0; i<5; i++) { // just put 5 elements in array
					resolve(om, tChildNode, aNode);
				}
				if (parent instanceof ObjectNode) {
					((ObjectNode)parent).set(tNode.getName(), aNode);
				} else {
					((ArrayNode)parent).add(aNode);
				}
				break;
			case INTEGER:
				if (parent instanceof ObjectNode) {
					((ObjectNode)parent).put(tNode.getName(), resolveIntegerValue(tNode));
				} else {
					((ArrayNode)parent).add(resolveIntegerValue(tNode));
				}
				break;
			case STRING:
				if (parent instanceof ObjectNode) {
					((ObjectNode)parent).put(tNode.getName(), resolveStringValue(tNode));
				} else {
					((ArrayNode)parent).add(resolveStringValue(tNode));
				}
				break;
		}
	}
	
	private Integer resolveIntegerValue (SchemaNode node) {
		
		Binding<Integer> b = (Binding<Integer>)bindings.get(node);
		Integer value;
		if (b == null) {
			value = DEFAULT_INTEGER;
		} else {
			value = b.getValue();
		}
		return value;
	}
	
	private String resolveStringValue (SchemaNode node) {
		
		Binding<String> b = (Binding<String>)bindings.get(node);
		String value;
		if (b == null) {
			value = DEFAULT_STRING;
		} else {
			value = b.getValue();
		}
		return value;
	}
}
