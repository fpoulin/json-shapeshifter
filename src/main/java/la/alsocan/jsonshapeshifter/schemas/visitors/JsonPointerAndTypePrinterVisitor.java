package la.alsocan.jsonshapeshifter.schemas.visitors;

import la.alsocan.jsonshapeshifter.schemas.SchemaArrayNode;
import la.alsocan.jsonshapeshifter.schemas.SchemaNode;
import la.alsocan.jsonshapeshifter.schemas.SchemaObjectNode;

/**
 * Basic visitor which prints the schema.
 * 
 * @author Florian Poulin <https://github.com/fpoulin>
 */
public class JsonPointerAndTypePrinterVisitor implements ITypedNodeVisitor {

	@Override
	public void visit(SchemaNode element) {
		displayElement(element);
	}

	@Override
	public void visitObject(SchemaObjectNode element) {
		displayElement(element);
	}

	@Override
	public void endVisitObject(SchemaObjectNode element) {}

	@Override
	public void visitArray(SchemaArrayNode element) {
		displayElement(element);
	}

	@Override
	public void endVisitArray(SchemaArrayNode element) {}
	
	@Override
	public void endVisit() {
	}
	
	private void displayElement(SchemaNode element) {
		System.out.println(element.getPath()+ " [" + element.getType() + (element.isRequired()?"*":"") + "]");
	}
}
