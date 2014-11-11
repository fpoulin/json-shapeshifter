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
package la.alsocan.jsonshapeshifter.bindings;

import com.fasterxml.jackson.databind.JsonNode;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import la.alsocan.jsonshapeshifter.schemas.SchemaNode;

/**
 * @author Florian Poulin - https://github.com/fpoulin
 * @param <T> Type of value for the node binding
 */
public abstract class AbstractNodeBinding<T> extends Binding<T> {

	private final SchemaNode source;
	
	public AbstractNodeBinding(SchemaNode source) {
		this.source = source;
		if (source == null) {
			throw new IllegalBindingException();
		}
	}
	
	@Override
	public Set<SchemaNode> getSourceNodes() {
		Set<SchemaNode> nodes = new HashSet<>();
		nodes.add(source);
		return nodes;
	}

	@Override
	public final T getValue(JsonNode payload, List<Integer> context) {
		JsonNode node = payload.at(jsonPointer(source.getSchemaPointer(), context));
		return node == null ? null : readValue(node);
	}
	
	protected abstract T readValue(JsonNode node);
	
	private static final String PATH_DELIMITER_REGEX = "\\{i\\}";
	static String jsonPointer(String schemaPointer, List<Integer> context) {
		for (Integer index : context) {
			schemaPointer = schemaPointer.replaceFirst(PATH_DELIMITER_REGEX, String.valueOf(index));
		}
		return schemaPointer;
	}
}
