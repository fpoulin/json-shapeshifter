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
