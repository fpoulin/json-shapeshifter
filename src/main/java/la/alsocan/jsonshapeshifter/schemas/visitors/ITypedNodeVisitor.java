package la.alsocan.jsonshapeshifter.schemas.visitors;

import la.alsocan.jsonshapeshifter.schemas.SchemaArrayNode;
import la.alsocan.jsonshapeshifter.schemas.SchemaNode;
import la.alsocan.jsonshapeshifter.schemas.SchemaObjectNode;

/**
 * @author Florian Poulin <https://github.com/fpoulin>
 */
public interface ITypedNodeVisitor {
	
	void visit(SchemaNode element);
	void visitObject(SchemaObjectNode element);
	void endVisitObject(SchemaObjectNode element);
	void visitArray(SchemaArrayNode element);
	void endVisitArray(SchemaArrayNode element);
	void endVisit();
}
