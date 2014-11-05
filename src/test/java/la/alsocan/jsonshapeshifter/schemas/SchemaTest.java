package la.alsocan.jsonshapeshifter.schemas;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import la.alsocan.jsonshapeshifter.DataSet;
import la.alsocan.jsonshapeshifter.schemas.Schema;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import org.junit.Test;

/**
 * @author Florian Poulin <https://github.com/fpoulin>
 */
public class SchemaTest {
	
	@Test
	public void schemaCanBeBuiltFromAJsonNode() throws IOException {
	
		Schema s = Schema.buildSchema(new ObjectMapper().readTree(DataSet.SIMPLE_SCHEMA));
		assertThat(s, is(not(nullValue())));
	}
}
