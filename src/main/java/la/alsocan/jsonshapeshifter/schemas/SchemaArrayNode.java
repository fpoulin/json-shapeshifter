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

/**
 * @author Florian Poulin - https://github.com/fpoulin
 */
public class SchemaArrayNode extends SchemaNode {

	private SchemaNode child;
	
	protected SchemaArrayNode(String name, String path, boolean required) {
		super(name, path, ENodeType.ARRAY, required);
	}
	
	protected SchemaArrayNode withResolvedChild(JsonNode node) {
		
		// array of a single type (list)
		JsonNode items = node.get("items");
		if (items != null && items.has("type")) {
			child = buildSchemaNode(items, null, path + "/{i}", false);
			child.setParent(this);
			return this;
		}
		
		// untyped array or tuples are not supported
		throw new UnsupportedJsonSchemaException("Tuples and untyped arrays are currently not supported");
	}
	
	/**
	 * Get the schema node for the elements contained in this array node
	 * @return the schema node for the elements contained in this array node
	 */
	public final SchemaNode getChild(){
		return child;
	}
}
