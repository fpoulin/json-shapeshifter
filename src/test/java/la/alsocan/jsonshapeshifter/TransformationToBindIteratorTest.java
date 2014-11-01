package la.alsocan.jsonshapeshifter;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.Iterator;
import java.util.NoSuchElementException;
import la.alsocan.jsonshapeshifter.bindings.StaticIntegerBinding;
import la.alsocan.jsonshapeshifter.bindings.StaticStringBinding;
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
		Iterator<SchemaNode> it = new Transformation(s).toBindIterator();
		for (int i=0; i<4; i++) {
			assertThat(it.hasNext(), is(true));
			it.next();
		}
		assertThat(it.hasNext(), is(false));
	}
	
	@Test
	public void nextShouldOnlyThrowExceptionAfterLastNode() throws IOException {
	
		Schema s = Schema.buildSchema(new ObjectMapper().readTree(DataSet.SIMPLE_SCHEMA));
		Iterator<SchemaNode> it = new Transformation(s).toBindIterator();
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
		Iterator<SchemaNode> it = new Transformation(s).toBindIterator();
		for (int i=0; i<4; i++) {
			assertThat(it.next().getPath(), is(not(equalTo("/simpleObject"))));
		}
	}
	
	@Test
	public void iteratorShouldIgnoreNodesWithBindingAndMatchNodesInTheRightOrder() throws IOException {
	
		String[] expected = new String [] {
			"/someInteger", 
			"/simpleObject/integerProperty"};
		
		Schema s = Schema.buildSchema(new ObjectMapper().readTree(DataSet.SIMPLE_SCHEMA));
		Transformation t = new Transformation(s);
		t.addBinding(s.at("/someString"), new StaticStringBinding("someBinding"));
		t.addBinding(s.at("/simpleObject/stringProperty"), new StaticStringBinding("someBinding"));
		Iterator<SchemaNode> it = t.toBindIterator();
		for (int i=0; i<2; i++) {
			assertThat(it.next().getPath(), is(equalTo(expected[i])));
		}
	}
	
	@Test
	public void iteratorShouldIgnoreNodesWithBindingEvenIfLastNode() throws IOException {
	
		Schema s = Schema.buildSchema(new ObjectMapper().readTree(DataSet.SIMPLE_SCHEMA));
		Transformation t = new Transformation(s);
		t.addBinding(s.at("/simpleObject/integerProperty"), new StaticIntegerBinding(12));
		Iterator<SchemaNode> it = t.toBindIterator();
		it.next();
		it.next();
		it.next();
		assertThat(it.hasNext(), is(false));
	}
}
