package la.alsocan.jsonshapeshifter.transformations;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.Iterator;
import java.util.NoSuchElementException;
import la.alsocan.jsonshapeshifter.DataSet;
import la.alsocan.jsonshapeshifter.Transformation;
import la.alsocan.jsonshapeshifter.bindings.IntegerConstantBinding;
import la.alsocan.jsonshapeshifter.bindings.StringConstantBinding;
import la.alsocan.jsonshapeshifter.schemas.Schema;
import la.alsocan.jsonshapeshifter.schemas.SchemaNode;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.fail;
import org.junit.Test;

/**
 * @author Florian Poulin <https://github.com/fpoulin>
 */
public class TransformationToBindIteratorTest {
	
	@Test
	public void hasNextShouldOnlyReturnFalseForLastNode() throws IOException {
	
		Schema s = Schema.buildSchema(new ObjectMapper().readTree(DataSet.SIMPLE_SCHEMA));
		Iterator<SchemaNode> it = new Transformation(s, s).toBind();
		for (int i=0; i<4; i++) {
			assertThat(it.hasNext(), is(true));
			it.next();
		}
		assertThat(it.hasNext(), is(false));
	}
	
	@Test
	public void nextShouldOnlyThrowExceptionAfterLastNode() throws IOException {
	
		Schema s = Schema.buildSchema(new ObjectMapper().readTree(DataSet.SIMPLE_SCHEMA));
		Iterator<SchemaNode> it = new Transformation(s, s).toBind();
		for (int i=0; i<4; i++) {
			assertThat(it.next(), is(not(nullValue())));
		}
		try {
			it.next();
			fail("Expected exception to be thrown here");
		} catch (Exception e) {
			assertThat(e, is(instanceOf(NoSuchElementException.class)));
		}
	}
	
	@Test
	public void iteratorShouldIgnoreObjectNodes() throws IOException {
	
		Schema s = Schema.buildSchema(new ObjectMapper().readTree(DataSet.SIMPLE_SCHEMA));
		Iterator<SchemaNode> it = new Transformation(s, s).toBind();
		while (it.hasNext()) {
			assertThat(it.next().getSchemaPointer(), is(not(equalTo("/simpleObject"))));
		}
	}
	
	@Test
	public void iteratorShouldIgnoreNullNodes() throws IOException {
	
		Schema s = Schema.buildSchema(new ObjectMapper().readTree(DataSet.ALL_TYPES_SCHEMA));
		Iterator<SchemaNode> it = new Transformation(s, s).toBind();
		while (it.hasNext()) {
			assertThat(it.next().getSchemaPointer(), is(not(equalTo("/someArray/{i}/someNull"))));
		}
	}
	
	@Test
	public void iteratorShouldIgnoreNodesWithBindingAndMatchNodesInTheRightOrder() throws IOException {
	
		String[] expected = new String [] {
			"/someInteger", 
			"/simpleObject/integerProperty"};
		
		Schema s = Schema.buildSchema(new ObjectMapper().readTree(DataSet.SIMPLE_SCHEMA));
		Transformation t = new Transformation(s, s);
		t.bind(s.at("/someString"), new StringConstantBinding("someBinding"));
		t.bind(s.at("/simpleObject/stringProperty"), new StringConstantBinding("someBinding"));
		Iterator<SchemaNode> it = t.toBind();
		for (int i=0; i<2; i++) {
			assertThat(it.next().getSchemaPointer(), is(equalTo(expected[i])));
		}
	}
	
	@Test
	public void iteratorShouldIgnoreNodesWithBindingEvenIfLastNode() throws IOException {
	
		Schema s = Schema.buildSchema(new ObjectMapper().readTree(DataSet.SIMPLE_SCHEMA));
		Transformation t = new Transformation(s, s);
		t.bind(s.at("/simpleObject/integerProperty"), new IntegerConstantBinding(12));
		Iterator<SchemaNode> it = t.toBind();
		it.next();
		it.next();
		it.next();
		assertThat(it.hasNext(), is(false));
	}
}
