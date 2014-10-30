package la.alsocan.jsonshapeshifter.schemas;

import com.fasterxml.jackson.databind.JsonNode;
import la.alsocan.jsonshapeshifter.schemas.visitors.ITypedNodeVisitor;
import com.github.fge.jackson.JsonLoader;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

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
		return schema;
	}

	//<editor-fold defaultstate="collapsed" desc="Visitor">
	@Override
	public final void accept(ITypedNodeVisitor visitor) {
		for (SchemaNode node : getChildren()) {
			node.accept(visitor);
		}
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
			Queue rootNodes = new LinkedList(schema.getChildren());
			buffer.push(rootNodes);
			nextNode = readFromBuffer();
		}
		
		@Override
		public SchemaNode next() {
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
			Queue<SchemaNode> queue = buffer.pop();
			SchemaNode node = queue.poll();
			if (!queue.isEmpty()) {
				buffer.push(queue);
			}
			return node;
		}
		
		private SchemaNode calculateNext() {
			if (nextNode == null) {
				return null;
			}
			if (ENodeType.OBJECT.equals(nextNode.type)) {
				Queue childrenNodes = new LinkedList(((SchemaObjectNode)nextNode).getChildren());
				buffer.push(childrenNodes);
			} else if (ENodeType.ARRAY.equals(nextNode.type)) {
				Queue childNode = new LinkedList();
				childNode.add(((SchemaArrayNode)nextNode).getChild());
				buffer.push(childNode);
			}
			return readFromBuffer();
		}
	}
	//</editor-fold>
}
