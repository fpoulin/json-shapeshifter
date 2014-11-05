package la.alsocan.jsonshapeshifter.schemas;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.Iterator;
import java.util.NoSuchElementException;
import la.alsocan.jsonshapeshifter.DataSet;
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
public class SchemaIteratorTest {

	@Test
	public void hasNextShouldOnlyReturnFalseForLastNode() throws IOException {
	
		Schema s = Schema.buildSchema(new ObjectMapper().readTree(DataSet.SIMPLE_SCHEMA));
		Iterator<SchemaNode> it = s.iterator();
		for (int i=0; i<5; i++) {
			assertThat(it.hasNext(), is(true));
			it.next();
		}
		assertThat(it.hasNext(), is(false));
	}
	
	@Test
	public void nextShouldOnlyThrowExceptionAfterLastNode() throws IOException {
	
		Schema s = Schema.buildSchema(new ObjectMapper().readTree(DataSet.SIMPLE_SCHEMA));
		Iterator<SchemaNode> it = s.iterator();
		for (int i=0; i<5; i++) {
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
	public void iteratedValuesShouldMatchWithTheSchemaInOrder() throws IOException {
	
		String[] expected = new String [] {
			"/someString", 
			"/someInteger", 
			"/simpleObject", 
			"/simpleObject/stringProperty", 
			"/simpleObject/integerProperty"};
		
		Schema s = Schema.buildSchema(new ObjectMapper().readTree(DataSet.SIMPLE_SCHEMA));
		Iterator<SchemaNode> it = s.iterator();
		for (int i=0; i<5; i++) {
			assertThat(it.next().getPath(), is(equalTo(expected[i])));
		}
	}
}
