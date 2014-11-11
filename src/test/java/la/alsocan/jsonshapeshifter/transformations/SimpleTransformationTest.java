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
import la.alsocan.jsonshapeshifter.bindings.IntegerNodeBinding;
import la.alsocan.jsonshapeshifter.bindings.IntegerConstantBinding;
import la.alsocan.jsonshapeshifter.bindings.StringConstantBinding;
import la.alsocan.jsonshapeshifter.bindings.StringNodeBinding;
import la.alsocan.jsonshapeshifter.schemas.Schema;
import la.alsocan.jsonshapeshifter.schemas.SchemaNode;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import org.junit.Test;

/**
 * @author Florian Poulin - https://github.com/fpoulin
 */
public class SimpleTransformationTest {
	
	public static final String DEFAULT_STRING = "?";
	public static final Integer DEFAULT_INTEGER = 0;
	
	@Test
	public void transformationShouldSetRightStaticValues() throws IOException {
	
		Schema source = Schema.buildSchema(new ObjectMapper().readTree(DataSet.SIMPLE_SCHEMA));
		Schema target = Schema.buildSchema(new ObjectMapper().readTree(DataSet.SIMPLE_SCHEMA));
		Transformation t = new Transformation(source, target);
		
		Iterator<SchemaNode> it = t.toBind();
		t.bind(it.next(), new StringConstantBinding("firstString"));
		t.bind(it.next(), new IntegerConstantBinding(1));
		t.bind(it.next(), new StringConstantBinding("secondString"));
		t.bind(it.next(), new IntegerConstantBinding(2));
		
		JsonNode result = t.apply(null);
		
		assertThat(result.at("/someString").asText(), is(equalTo("firstString")));
		assertThat(result.at("/someInteger").asInt(), is(equalTo(1)));
		assertThat(result.at("/simpleObject/stringProperty").asText(), is(equalTo("secondString")));
		assertThat(result.at("/simpleObject/integerProperty").asInt(), is(equalTo(2)));
	}
	
	@Test
	public void partialTransformationShouldAssignDefaultValues() throws IOException {
	
		Schema source = Schema.buildSchema(new ObjectMapper().readTree(DataSet.SIMPLE_SCHEMA));
		Schema target = Schema.buildSchema(new ObjectMapper().readTree(DataSet.SIMPLE_SCHEMA));
		Transformation t = new Transformation(source, target);
		
		Iterator<SchemaNode> it = t.toBind();
		t.bind(it.next(), new StringConstantBinding("firstString"));
		t.bind(it.next(), new IntegerConstantBinding(1));
		
		JsonNode result = t.apply(null);
		
		assertThat(result.at("/simpleObject/stringProperty").asText(), is(equalTo(DEFAULT_STRING)));
		assertThat(result.at("/simpleObject/integerProperty").asInt(), is(equalTo(DEFAULT_INTEGER)));
	}
	
	@Test
	public void transformationShouldSetRightNodeValues() throws IOException {
	
		Schema source = Schema.buildSchema(new ObjectMapper().readTree(DataSet.SIMPLE_SCHEMA));
		Schema target = Schema.buildSchema(new ObjectMapper().readTree(DataSet.SIMPLE_SCHEMA));
		Transformation t = new Transformation(source, target);
		
		Iterator<SchemaNode> it = t.toBind();
		t.bind(it.next(), new StringNodeBinding(source.at("/simpleObject/stringProperty")));
		t.bind(it.next(), new IntegerNodeBinding(source.at("/simpleObject/integerProperty")));
		t.bind(it.next(), new StringNodeBinding(source.at("/someString")));
		t.bind(it.next(), new IntegerNodeBinding(source.at("/someInteger")));
		
		JsonNode payload = new ObjectMapper().readTree(DataSet.SIMPLE_PAYLOAD);
		JsonNode result = t.apply(payload);
		
		assertThat(result.at("/someString").asText(), is(equalTo("string2")));
		assertThat(result.at("/someInteger").asInt(), is(equalTo(2)));
		assertThat(result.at("/simpleObject/stringProperty").asText(), is(equalTo("string1")));
		assertThat(result.at("/simpleObject/integerProperty").asInt(), is(equalTo(1)));
	}
}
