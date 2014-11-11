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
