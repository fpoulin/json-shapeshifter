package la.alsocan.jsonshapeshifter.transformations;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import java.io.IOException;
import java.util.Iterator;
import la.alsocan.jsonshapeshifter.DataSet;
import la.alsocan.jsonshapeshifter.Transformation;
import la.alsocan.jsonshapeshifter.bindings.CollectionBinding;
import la.alsocan.jsonshapeshifter.bindings.IntegerNodeBinding;
import la.alsocan.jsonshapeshifter.bindings.StaticIntegerBinding;
import la.alsocan.jsonshapeshifter.bindings.StringNodeBinding;
import la.alsocan.jsonshapeshifter.schemas.Schema;
import la.alsocan.jsonshapeshifter.schemas.SchemaArrayNode;
import la.alsocan.jsonshapeshifter.schemas.SchemaNode;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import org.junit.Test;

/**
 * @author Florian Poulin <https://github.com/fpoulin>
 */
public class SimpleCollectionTransformationTest {
	
	public static final String DEFAULT_STRING = "?";
	
	@Test
	public void collectionBindingShouldProduceTheRightNumberOfElements() throws IOException {
	
		Schema source = Schema.buildSchema(new ObjectMapper().readTree(DataSet.SIMPLE_COLLECTION_SCHEMA));
		Schema target = Schema.buildSchema(new ObjectMapper().readTree(DataSet.SIMPLE_COLLECTION_SCHEMA));
		Transformation t = new Transformation(target);
		
		Iterator<SchemaNode> it = t.toBindIterator();
		it.next();
		t.addBinding(it.next(), new CollectionBinding((SchemaArrayNode)source.at("/someStringArray")));
		it.next();
		t.addBinding(it.next(), new CollectionBinding((SchemaArrayNode)source.at("/someIntegerArray")));
		
		JsonNode payload = new ObjectMapper().readTree(DataSet.SIMPLE_COLLECTION_PAYLOAD);
		JsonNode result = t.apply(payload);
		
		ArrayNode stringCollection = (ArrayNode)result.at("/someStringArray");
		assertThat(stringCollection.size(), is(5));
		
		ArrayNode integerCollection = (ArrayNode)result.at("/someIntegerArray");
		assertThat(integerCollection.size(), is(8));
	}
	
	@Test
	public void collectionBindingShouldProduceTheRightValues() throws IOException {
	
		Schema source = Schema.buildSchema(new ObjectMapper().readTree(DataSet.SIMPLE_COLLECTION_SCHEMA));
		Schema target = Schema.buildSchema(new ObjectMapper().readTree(DataSet.SIMPLE_COLLECTION_SCHEMA));
		Transformation t = new Transformation(target);
		
		Iterator<SchemaNode> it = t.toBindIterator();
		it.next();
		t.addBinding(it.next(), new CollectionBinding((SchemaArrayNode)source.at("/someStringArray")));
		t.addBinding(it.next(), new StringNodeBinding(source.at("/someStringArray/{i}")));
		t.addBinding(it.next(), new CollectionBinding((SchemaArrayNode)source.at("/someIntegerArray")));
		t.addBinding(it.next(), new IntegerNodeBinding(source.at("/someIntegerArray/{i}")));
		
		JsonNode payload = new ObjectMapper().readTree(DataSet.SIMPLE_COLLECTION_PAYLOAD);
		JsonNode result = t.apply(payload);
		
		int index = 0;
		String[] expectedStrings = new String [] {"a", "b", "c", "d", "e"};
		for (JsonNode node : (ArrayNode)result.at("/someStringArray")) {
			assertThat(node.asText(), is(equalTo(expectedStrings[index++])));
		}

		index = 0;
		Integer[] expectedIntegers = new Integer [] {1, 2, 3, 4, 5, 6, 7, 8};
		for (JsonNode node : (ArrayNode)result.at("/someIntegerArray")) {
			assertThat(node.asInt(), is(equalTo(expectedIntegers[index++])));
		}
	}
	
	@Test
	public void collectionBindingShouldWorkWithBindingOnNonCollectionNodes() throws IOException {
	
		Schema source = Schema.buildSchema(new ObjectMapper().readTree(DataSet.SIMPLE_COLLECTION_SCHEMA));
		Schema target = Schema.buildSchema(new ObjectMapper().readTree(DataSet.SIMPLE_COLLECTION_SCHEMA));
		Transformation t = new Transformation(target);
		
		Iterator<SchemaNode> it = t.toBindIterator();
		it.next();
		t.addBinding(it.next(), new CollectionBinding((SchemaArrayNode)source.at("/someStringArray")));
		t.addBinding(it.next(), new StringNodeBinding(source.at("/someString")));
		t.addBinding(it.next(), new CollectionBinding((SchemaArrayNode)source.at("/someIntegerArray")));
		t.addBinding(it.next(), new StaticIntegerBinding(12));
		
		JsonNode payload = new ObjectMapper().readTree(DataSet.SIMPLE_COLLECTION_PAYLOAD);
		JsonNode result = t.apply(payload);
		
		for (JsonNode node : (ArrayNode)result.at("/someStringArray")) {
			assertThat(node.asText(), is(equalTo("string1")));
		}

		for (JsonNode node : (ArrayNode)result.at("/someIntegerArray")) {
			assertThat(node.asInt(), is(equalTo(12)));
		}
	}
	
	@Test
	public void partialCollectionTransformationShouldAssignDefaultValues() throws IOException {
	
		Schema source = Schema.buildSchema(new ObjectMapper().readTree(DataSet.SIMPLE_COLLECTION_SCHEMA));
		Schema target = Schema.buildSchema(new ObjectMapper().readTree(DataSet.SIMPLE_COLLECTION_SCHEMA));
		Transformation t = new Transformation(target);
		
		Iterator<SchemaNode> it = t.toBindIterator();
		it.next();
		t.addBinding(it.next(), new CollectionBinding((SchemaArrayNode)source.at("/someStringArray")));
		// not binding /someIntegerArray at all (should produce empty array)
		
		JsonNode payload = new ObjectMapper().readTree(DataSet.SIMPLE_COLLECTION_PAYLOAD);
		
		JsonNode result;
		try {
			result = t.apply(payload);
		} catch (Exception e) {
			fail("Got the following exception: " + e);
			return;
		}
		
		for (int i=0; i<5; i++) {
			JsonNode node = result.at("/someStringArray/" + i);
			assertThat(node, is(not(nullValue())));
			assertThat(node.asText(), is(equalTo(DEFAULT_STRING)));
		}

		JsonNode node = result.at("/someIntegerArray");
		assertThat(node, is(not(nullValue())));
		assertThat(node, is(instanceOf(ArrayNode.class)));
		assertThat(((ArrayNode)node).size(), is(equalTo(0)));
	}
}
