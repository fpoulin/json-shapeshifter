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
package la.alsocan.jsonshapeshifter.schemas;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonschema.core.exceptions.ProcessingException;
import com.github.fge.jsonschema.core.report.ProcessingReport;
import com.github.fge.jsonschema.main.JsonSchema;
import com.github.fge.jsonschema.main.JsonSchemaFactory;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.Queue;
import java.util.Stack;
import java.util.TreeMap;

/**
 * @author Florian Poulin - https://github.com/fpoulin
 */
public class Schema extends SchemaObjectNode implements Iterable<SchemaNode> {
	
	private final JsonNode schemaNode;
	
	private Schema(JsonNode schemaNode, String name, String schemaPointer, boolean required) {
		super(name, schemaPointer, required);
		this.schemaNode = schemaNode;
	}
	
	/**
	 * Build a {@link Schema} instance.
	 * @param schemaPath Path to a valid Json schema file
	 * @return A {@link Schema} instance
	 * @throws IOException the schema file cannot be read
	 */
	public final static Schema buildSchema(String schemaPath) throws IOException {
		return Schema.buildSchema(new File(schemaPath));
	}
	
	/**
	 * Build a {@link Schema} instance.
	 * @param schemaFile A valid Json schema file
	 * @return A {@link Schema} instance
	 * @throws IOException the schema file cannot be read
	 */
	public final static Schema buildSchema(File schemaFile) throws IOException {
		return Schema.buildSchema(new ObjectMapper().readTree(schemaFile));
	}
	
	/**
	 * Build a {@link Schema} instance.
	 * @param schemaNode A {@link JsonNode} corresponding to a valid Json schema
	 * @return A {@link Schema} instance
	 */
	public final static Schema buildSchema(JsonNode schemaNode) {
		Schema schema = new Schema(schemaNode, "", "", true);
		schema.withResolvedChildren(schemaNode);
		schema.buildIndex();
		return schema;
	}
	
	/**
	 * Validate a {@link Schema} instance (payload) and return a processing report.
	 * @param payload Any {@link JsonNode} (a Json payload)
	 * @return A {@link ProcessingReport}
	 * @throws ProcessingException a processing error occurred during validation
	 * @see ProcessingReport#isSuccess() 
	 */
	public ProcessingReport validate(JsonNode payload) throws ProcessingException {
		final JsonSchemaFactory factory = JsonSchemaFactory.byDefault();
		final JsonSchema schema = factory.getJsonSchema(schemaNode);
		return schema.validate(payload);
	}
	
	/**
	 * Get the {@link SchemaNode} corresponding to the given <i>schema pointer</i>.
	 * 
	 * A <i>schema pointer</i> is equivalent to a 
	 * <a href="http://tools.ietf.org/html/rfc6901" target="_blank">Json pointer</a> with 
	 * the exception that elements within an array are indexed with the <code>{i}</code>
	 * string (instead of a numeric value).<br>
	 * <br>
	 * Examples of valid <i>schema pointers</i>:
	 * <ul>
	 * <li><code>/anObject</code></li>
	 * <li><code>/anObject/anArray</code></li>
	 * <li><code>/anObject/anArray/{i}</code></li>
	 * <li><code>/anObject/anArray/{i}/{i}</code></li>
	 * <li><code>/anObject/anArray/{i}/{i}/someProp</code></li>
	 * </ul>
	 * 
	 * @param schemaPointer A schema pointer
	 * @return The {@link SchemaNode} corresponding to the given <i>schema pointer</i>, or
	 * <code>null</code> if the node cannot be found
	 */
	public final SchemaNode at(String schemaPointer) {
		return nodeIndex.get(schemaPointer);
	}
	
	// FIXME: replace this with something more elegant
	private final TreeMap<String, SchemaNode> nodeIndex = new TreeMap<>();
	private void buildIndex(){
		for (SchemaNode node : this) {
			nodeIndex.put(node.getSchemaPointer(), node);
		}
	}

	//<editor-fold defaultstate="collapsed" desc="Iterator">
	/**
	 * Returns an iterator over the {@link SchemaNode} elements of the schema.
	 * @return an iterator over the {@link SchemaNode} elements of the schema
	 */
	@Override
	public final Iterator<SchemaNode> iterator() {
		return new SchemaNodesIterator(this);
	}
	
	private class SchemaNodesIterator implements Iterator<SchemaNode> {
		
		private final Stack<Queue<SchemaNode>> buffer;
		private SchemaNode nextNode;
		
		public SchemaNodesIterator(Schema schema) {
			buffer = new Stack<>();
			Queue<SchemaNode> rootNodes = new LinkedList<>(schema.getChildren());
			buffer.push(rootNodes);
			nextNode = readFromBuffer();
		}
		
		@Override
		public SchemaNode next() {
			if (nextNode == null) {
				throw new NoSuchElementException();
			}
			SchemaNode toReturn = nextNode;
			nextNode = calculateNext();
			return toReturn;
		}
		
		@Override
		public boolean hasNext() {
			return nextNode != null;
		}
		
		private SchemaNode readFromBuffer() {
			if (buffer.isEmpty()) {
				return null;
			}
			Queue<SchemaNode> queue = buffer.peek();
			SchemaNode node = queue.poll();
			if (queue.isEmpty()) {
				buffer.pop();
			}
			return node;
		}
		
		private SchemaNode calculateNext() {
			if (nextNode == null) {
				return null;
			}
			if (ENodeType.OBJECT.equals(nextNode.type)) {
				Queue<SchemaNode> childrenNodes = new LinkedList<>(((SchemaObjectNode)nextNode).getChildren());
				buffer.push(childrenNodes);
			} else if (ENodeType.ARRAY.equals(nextNode.type)) {
				Queue<SchemaNode> childNode = new LinkedList<>();
				childNode.add(((SchemaArrayNode)nextNode).getChild());
				buffer.push(childNode);
			}
			return readFromBuffer();
		}
	}
	//</editor-fold>
}
