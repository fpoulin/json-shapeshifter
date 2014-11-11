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
package la.alsocan.jsonshapeshifter.transformations;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import la.alsocan.jsonshapeshifter.DataSet;
import la.alsocan.jsonshapeshifter.Transformation;
import la.alsocan.jsonshapeshifter.bindings.Binding;
import la.alsocan.jsonshapeshifter.bindings.ArrayNodeBinding;
import la.alsocan.jsonshapeshifter.bindings.StringHandlebarsBinding;
import la.alsocan.jsonshapeshifter.bindings.StringConstantBinding;
import la.alsocan.jsonshapeshifter.bindings.StringNodeBinding;
import la.alsocan.jsonshapeshifter.schemas.Schema;
import la.alsocan.jsonshapeshifter.schemas.SchemaArrayNode;
import la.alsocan.jsonshapeshifter.schemas.SchemaNode;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import org.junit.Test;

/**
 * @author Florian Poulin - https://github.com/fpoulin
 */
public class HandlebarsBindingTransformationTest {
	
	@Test
	public void staticHandlebarsBindingShouldProduceRightResult() throws IOException {
		
		Schema source = Schema.buildSchema(new ObjectMapper().readTree(DataSet.SIMPLE_COLLECTION_SCHEMA));
		Schema target = Schema.buildSchema(new ObjectMapper().readTree(DataSet.SIMPLE_COLLECTION_SCHEMA));
		Transformation t = new Transformation(source, target);
		
		String template = "Hello {{world}}";
		Map<String, Binding> params = new HashMap<>();
		params.put("world", new StringConstantBinding("world"));
		
		Iterator<SchemaNode> it = t.toBind();
		t.bind(it.next(), new StringHandlebarsBinding(template, params));
		
		JsonNode payload = new ObjectMapper().readTree(DataSet.SIMPLE_COLLECTION_PAYLOAD);
		JsonNode result = t.apply(payload);
		
		JsonNode node = result.at("/someString");
		assertThat(node.asText(), is(equalTo("Hello world")));
	}
	
	@Test
	public void nodeHandlebarsBindingShouldProduceRightResult() throws IOException {
		
		Schema source = Schema.buildSchema(new ObjectMapper().readTree(DataSet.SIMPLE_COLLECTION_SCHEMA));
		Schema target = Schema.buildSchema(new ObjectMapper().readTree(DataSet.SIMPLE_COLLECTION_SCHEMA));
		Transformation t = new Transformation(source, target);
		
		String template = "{{someString}} {{stringInArray}}";
		Map<String, Binding> params = new HashMap<>();
		params.put("someString", new StringNodeBinding(source.at("/someString")));
		params.put("stringInArray", new StringNodeBinding(source.at("/someStringArray/{i}")));
		
		Iterator<SchemaNode> it = t.toBind();
		it.next();
		t.bind(it.next(), new ArrayNodeBinding((SchemaArrayNode)source.at("/someStringArray")));
		t.bind(it.next(), new StringHandlebarsBinding(template, params));
		
		JsonNode payload = new ObjectMapper().readTree(DataSet.SIMPLE_COLLECTION_PAYLOAD);
		JsonNode result = t.apply(payload);
		
		String[] expectedStrings = new String [] {"string1 a", "string1 b", "string1 c", "string1 d", "string1 e"};
		for (int i=0; i<5; i++) {
			JsonNode node = result.at("/someStringArray/" + i);
			assertThat(node, is(not(nullValue())));
			assertThat(node.asText(), is(equalTo(expectedStrings[i])));
		}
	}
}
