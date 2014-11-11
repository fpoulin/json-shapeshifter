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
import java.util.Iterator;
import la.alsocan.jsonshapeshifter.DataSet;
import la.alsocan.jsonshapeshifter.Transformation;
import la.alsocan.jsonshapeshifter.bindings.ArrayConstantBinding;
import la.alsocan.jsonshapeshifter.schemas.Schema;
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
public class TypesTransformationsTest {
	
	public static final Boolean DEFAULT_BOOLEAN = false;
	public static final Integer DEFAULT_INTEGER = 0;
	public static final Number DEFAULT_NUMBER = 0.0;
	public static final String DEFAULT_STRING = "?";
	
	@Test
	public void transformationShouldProduceDefaultsForAllTypes() throws IOException {
	
		Schema source = Schema.buildSchema(new ObjectMapper().readTree(DataSet.ALL_TYPES_SCHEMA));
		Schema target = Schema.buildSchema(new ObjectMapper().readTree(DataSet.ALL_TYPES_SCHEMA));
		Transformation t = new Transformation(source, target);
		
		Iterator<SchemaNode> it = t.toBind();
		t.bind(it.next(), new ArrayConstantBinding(1));
		
		JsonNode result = t.apply(null);
		
		JsonNode node = result.at("/someArray/0/someBoolean");
		assertThat(node, is(not(nullValue())));
		assertThat(node.asBoolean(), is(equalTo(DEFAULT_BOOLEAN)));
		
		node = result.at("/someArray/0/someInteger");
		assertThat(node, is(not(nullValue())));
		assertThat(node.asInt(), is(equalTo(DEFAULT_INTEGER)));
		
		node = result.at("/someArray/0/someNumber");
		assertThat(node, is(not(nullValue())));
		assertThat(node.numberValue(), is(equalTo(DEFAULT_NUMBER)));
		
		node = result.at("/someArray/0/someNull");
		assertThat(node, is(not(nullValue())));
		assertThat(node.isNull(), is(equalTo(true)));
		
		node = result.at("/someArray/0/someString");
		assertThat(node, is(not(nullValue())));
		assertThat(node.asText(), is(equalTo(DEFAULT_STRING)));
	}
}
