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
import com.fasterxml.jackson.databind.node.ArrayNode;
import java.io.IOException;
import java.util.Iterator;
import la.alsocan.jsonshapeshifter.DataSet;
import la.alsocan.jsonshapeshifter.Transformation;
import la.alsocan.jsonshapeshifter.bindings.ArrayNodeBinding;
import la.alsocan.jsonshapeshifter.bindings.StringConstantBinding;
import la.alsocan.jsonshapeshifter.bindings.StringNodeBinding;
import la.alsocan.jsonshapeshifter.schemas.Schema;
import la.alsocan.jsonshapeshifter.schemas.SchemaArrayNode;
import la.alsocan.jsonshapeshifter.schemas.SchemaNode;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import org.junit.Test;

/**
 * @author Florian Poulin - https://github.com/fpoulin
 */
public class AdvancedCollectionTransformationTest {
	
	@Test
	public void embeddedCollectionBindingShouldProduceTheRightNumberOfElements() throws IOException {
	
		Schema source = Schema.buildSchema(new ObjectMapper().readTree(DataSet.EMBEDDED_COLLECTION_SCHEMA));
		Schema target = Schema.buildSchema(new ObjectMapper().readTree(DataSet.EMBEDDED_COLLECTION_SCHEMA));
		Transformation t = new Transformation(source, target);
		
		Iterator<SchemaNode> it = t.toBind();
		t.bind(it.next(), new ArrayNodeBinding((SchemaArrayNode)source.at("/someArrayOfArray")));
		t.bind(it.next(), new ArrayNodeBinding((SchemaArrayNode)source.at("/someArrayOfArray/{i}")));
		t.bind(it.next(), new StringNodeBinding(source.at("/someArrayOfArray/{i}/{i}")));
		
		JsonNode payload = new ObjectMapper().readTree(DataSet.EMBEDDED_COLLECTION_PAYLOAD);
		JsonNode result = t.apply(payload);
		
		ArrayNode arrayCollection = (ArrayNode)result.at("/someArrayOfArray");
		assertThat(arrayCollection.size(), is(3));
		
		arrayCollection = (ArrayNode)result.at("/someArrayOfArray/0");
		assertThat(arrayCollection.size(), is(3));
		
		arrayCollection = (ArrayNode)result.at("/someArrayOfArray/1");
		assertThat(arrayCollection.size(), is(2));
		
		arrayCollection = (ArrayNode)result.at("/someArrayOfArray/2");
		assertThat(arrayCollection.size(), is(1));
	}
	
	@Test
	public void embeddedCollectionBindingShouldProduceTheRightValues() throws IOException {
	
		Schema source = Schema.buildSchema(new ObjectMapper().readTree(DataSet.EMBEDDED_COLLECTION_SCHEMA));
		Schema target = Schema.buildSchema(new ObjectMapper().readTree(DataSet.EMBEDDED_COLLECTION_SCHEMA));
		Transformation t = new Transformation(source, target);
		
		Iterator<SchemaNode> it = t.toBind();
		t.bind(it.next(), new ArrayNodeBinding((SchemaArrayNode)source.at("/someArrayOfArray")));
		t.bind(it.next(), new ArrayNodeBinding((SchemaArrayNode)source.at("/someArrayOfArray/{i}")));
		t.bind(it.next(), new StringNodeBinding(source.at("/someArrayOfArray/{i}/{i}")));
		
		JsonNode payload = new ObjectMapper().readTree(DataSet.EMBEDDED_COLLECTION_PAYLOAD);
		JsonNode result = t.apply(payload);
		
		int index = 0;
		String[] expectedStrings = new String [] {"a", "b", "c"};
		for (JsonNode node : (ArrayNode)result.at("/someArrayOfArray/0")) {
			assertThat(node.asText(), is(equalTo(expectedStrings[index++])));
		}

		index = 0;
		expectedStrings = new String [] {"d", "e"};
		for (JsonNode node : (ArrayNode)result.at("/someArrayOfArray/1")) {
			assertThat(node.asText(), is(equalTo(expectedStrings[index++])));
		}
		
		index = 0;
		expectedStrings = new String [] {"f"};
		for (JsonNode node : (ArrayNode)result.at("/someArrayOfArray/2")) {
			assertThat(node.asText(), is(equalTo(expectedStrings[index++])));
		}
	}
	
	@Test
	public void embeddedCollectionBindingShouldWithStaticValues() throws IOException {
	
		Schema source = Schema.buildSchema(new ObjectMapper().readTree(DataSet.EMBEDDED_COLLECTION_SCHEMA));
		Schema target = Schema.buildSchema(new ObjectMapper().readTree(DataSet.EMBEDDED_COLLECTION_SCHEMA));
		Transformation t = new Transformation(source, target);
		
		Iterator<SchemaNode> it = t.toBind();
		t.bind(it.next(), new ArrayNodeBinding((SchemaArrayNode)source.at("/someArrayOfArray")));
		t.bind(it.next(), new ArrayNodeBinding((SchemaArrayNode)source.at("/someArrayOfArray/{i}")));
		t.bind(it.next(), new StringConstantBinding("someString"));
		
		JsonNode payload = new ObjectMapper().readTree(DataSet.EMBEDDED_COLLECTION_PAYLOAD);
		JsonNode result = t.apply(payload);
		
		for (JsonNode iNode : (ArrayNode)result.at("/someArrayOfArray")) {
			for (JsonNode jNode : iNode) {
				assertThat(jNode.asText(), is(equalTo("someString")));
			}
		}
	}
}
