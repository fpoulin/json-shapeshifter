package la.alsocan.jsonshapeshifter.schemas;

import com.fasterxml.jackson.databind.JsonNode;
import la.alsocan.jsonshapeshifter.schemas.visitors.ITypedNodeVisitor;
import com.github.fge.jackson.JsonLoader;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.Queue;
import java.util.Stack;
import java.util.TreeMap;

/**
 * @author Florian Poulin <https://github.com/fpoulin>
 */
public class Schema extends SchemaObjectNode implements Iterable<SchemaNode> {
	
	public Schema(String name, String path, boolean required) {
		super(name, path, required);
	}
	
	public final static Schema buildSchema(String schemaPath) throws IOException {
		return Schema.buildSchema(new File(schemaPath));
	}
	
	public final static Schema buildSchema(File schemaFile) throws IOException {
		return buildSchema(JsonLoader.fromFile(schemaFile));
	}
	
	public final static Schema buildSchema(JsonNode schemaNode) {
		Schema schema = new Schema("", "", true);
		schema.withResolvedChildren(schemaNode);
		schema.buildIndex();
		return schema;
	}
	
	// FIXME: replace this with something more elegant
	private final TreeMap<String, SchemaNode> nodeIndex = new TreeMap<>();
	private void buildIndex(){
		for (SchemaNode node : this) {
			nodeIndex.put(node.getPath(), node);
		}
	}
	public SchemaNode at(String path) {
		return nodeIndex.get(path);
	}

	//<editor-fold defaultstate="collapsed" desc="Visitor">
	@Override
	public final void accept(ITypedNodeVisitor visitor) {
		getChildren().stream().forEach((node) -> {
			node.accept(visitor);
		});
		visitor.endVisit();
	}
	//</editor-fold>

	//<editor-fold defaultstate="collapsed" desc="Iterator">
	@Override
	public Iterator<SchemaNode> iterator() {
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
