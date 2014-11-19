/*
 * The MIT License
 *
 * Copyright 2014 Florian Poulin - https://github.com/fpoulin.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
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
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import la.alsocan.jsonshapeshifter.bindings.ArrayNodeBinding;
import la.alsocan.jsonshapeshifter.bindings.BooleanNodeBinding;
import la.alsocan.jsonshapeshifter.bindings.IllegalBindingException;
import la.alsocan.jsonshapeshifter.bindings.IntegerNodeBinding;
import la.alsocan.jsonshapeshifter.bindings.NumberNodeBinding;
import la.alsocan.jsonshapeshifter.bindings.StringHandlebarsBinding;
import la.alsocan.jsonshapeshifter.bindings.StringNodeBinding;

/**
 * @author Florian Poulin - https://github.com/fpoulin
 */
public class Transformation {
	
	private final Schema source;
	private final Schema target;
	
	private final Map<SchemaNode, Binding<?>> bindings;
	
	/**
	 * Build a new transformation.
	 * 
	 * @param source The source schema
	 * @param target The target schema
	 */
	public Transformation (Schema source, Schema target) {
		this.source = source;
		this.target = target;
		bindings = new TreeMap<>();
	}

	/**
	 * Get the {@link Binding}s defined in this transformation.
	 * @return All bindings defined in this transformation
	 */
	public Map<SchemaNode, Binding<?>> getBindings() {
		return bindings;
	}

	/**
	 * Get the source {@link Schema} used in this transformation.
	 * @return The source schema used in this transformation
	 */
	public Schema getSource() {
		return source;
	}

	/**
	 * Get the target {@link Schema} used in this transformation.
	 * @return The target schema used in this transformation
	 */
	public Schema getTarget() {
		return target;
	}
	
	/**
	 * Iterates through the target schema to look for {@link SchemaNode} still requiring a 
	 * binding. This iterates through the nodes of the target schema but skips those which 
	 * already have a binding defined as well as <i>object</i> and <i>null</i> nodes (which
	 * do not require any binding).<br>
	 * <br>
	 * When {@link Iterator#hasNext()} returns <code>false</code>, the transformation can 
	 * be considered fully defined (and invoking {@link #apply(JsonNode)} is safe).
	 * 
	 * @return An iterator for all target {@link SchemaNode} still requiring a binding
	 */
	public Iterator<SchemaNode> toBind() {
		
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
	
	/**
	 * Define a {@link Binding} for the given target schema node. If the binding
	 * references one or several nodes (i.e. it is not a <i>constant</i> binding), the 
	 * source node(s) have to be compatible with the target schema node (otherwise an 
	 * {@link IllegalBindingException} will be thrown).
	 * 
	 * @param targetNode The target node for which a binding must be added
	 * @param binding A binding for the target node
	 * @throws IllegalBindingException The binding provided is not allowed for the given
	 * target node (or is <code>null</code>)
	 * @see #legalNodesFor(la.alsocan.jsonshapeshifter.schemas.SchemaNode) 
	 */
	public void bind(SchemaNode targetNode, Binding binding) {
		if (binding == null || (!binding.getSourceNodes().isEmpty() 
			&& !legalNodesFor(targetNode).containsAll(binding.getSourceNodes()))) {
			throw new IllegalBindingException();
		}
		bindings.put(targetNode, binding);
	}
	
	/**
	 * Returns a set of {@link SchemaNode} which can be used as source for the given target
	 * node. This method inspects the current state of the transformation (i.e. the set of 
	 * bindings already defined) and tells you which nodes from the source schema can be 
	 * used in a node binding for the given target node.<br>
	 * <br>
	 * Use this method before invoking {@link #bind(SchemaNode, Binding)} with a node 
	 * binding (refer to the <i>see</i> section below for the list of node bindings).
	 * 
	 * @param targetNode The target node to inspect
	 * @return A set of source nodes which can be used in a binding for this target node
	 * @see ArrayNodeBinding
	 * @see BooleanNodeBinding
	 * @see IntegerNodeBinding
	 * @see NumberNodeBinding
	 * @see StringHandlebarsBinding
	 * @see StringNodeBinding
	 */
	public Set<SchemaNode> legalNodesFor(SchemaNode targetNode) {
		
		// explore from the root of the source schema (top to bottom, no array traversal)
		Set<SchemaNode> sources = new TreeSet<>();
		source.getChildren().stream().forEach((sourceNode) -> {
			collectDown(sources, sourceNode, targetNode.getType());
		});
		
		// explore from the target node (bottom to top, traverse arrays with binding)
		SchemaNode current = targetNode;
		while ((current = current.getParent()) != null) {
			if (bindings.get(current) instanceof ArrayNodeBinding) {
				SchemaArrayNode sourceNode = (SchemaArrayNode)((ArrayNodeBinding)bindings.get(current)).getSourceNodes().iterator().next();
				collectDown(sources, sourceNode, targetNode.getType());
				collectDown(sources, sourceNode.getChild(), targetNode.getType());
			}
		}
		return sources;
	}
	private void collectDown(Set<SchemaNode> sources, SchemaNode sourceNode, ENodeType type) {
		
		// add compatible nodes (ignore null and object nodes)
		switch(type) {
			case ARRAY:
			case BOOLEAN:
			case INTEGER:
				if (sourceNode.getType().equals(type)) {
					sources.add(sourceNode);
				}
				break;
			case NUMBER:
				switch(sourceNode.getType()) {
					case INTEGER:
					case NUMBER:
						sources.add(sourceNode);
						break;
				}
				break;
			case STRING:
				switch(sourceNode.getType()) {
					case BOOLEAN:
					case INTEGER:
					case NUMBER:
					case STRING:
						sources.add(sourceNode);
						break;
				}
		}

		// explore object children nodes
		if (ENodeType.OBJECT.equals(sourceNode.getType())) {
			((SchemaObjectNode)sourceNode).getChildren().stream().forEach((child) -> {
				collectDown(sources, child, type);
			});
		}
	}
	
	/**
	 * Transforms the given Json instance of the source schema to a Json instance of the 
	 * target schema. The transformation applies all bindings defined in the 
	 * transformation. Missing bindings will produce {@link Default} values (therefore
	 * the transformation always produces a result, even if there are missing bindings).
	 * 
	 * @param payload the payload to transform
	 * @return A transformed Json payload following the transformation definition.
	 */
	public JsonNode apply(JsonNode payload) {
		ObjectMapper om = new ObjectMapper();
		ObjectNode root = om.createObjectNode();
		List<Integer> pointerContext = new ArrayList<>();
		target.getChildren().stream().forEach((tNode) -> {
			resolveNode(om, tNode, root, payload, pointerContext);
		});
		return root;
	}
	private void resolveNode(ObjectMapper om, SchemaNode node, JsonNode parentNode, JsonNode payload, List<Integer> pointerContext) {
		switch(node.getType()) {
		case OBJECT:
			ObjectNode oNode = om.createObjectNode();
			((SchemaObjectNode)node).getChildren().stream().forEach((tChildNode) -> {
				resolveNode(om, tChildNode, oNode, payload, pointerContext);
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
				resolveNode(om, tChildNode, aNode, payload, pointerContext);
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
					return Default.DEFAULT_ARRAY;
				case BOOLEAN:
					return Default.DEFAULT_BOOLEAN;
				case INTEGER:
					return Default.DEFAULT_INTEGER;
				case NUMBER:
					return Default.DEFAULT_NUMBER;
				case STRING:
					return Default.DEFAULT_STRING;
				default:
					return null;
			}
		}
		return value;
	}
}
