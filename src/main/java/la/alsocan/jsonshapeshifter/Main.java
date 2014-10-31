package la.alsocan.jsonshapeshifter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import la.alsocan.jsonshapeshifter.schemas.SchemaNode;
import la.alsocan.jsonshapeshifter.schemas.Schema;
import java.io.IOException;
import java.util.Iterator;
import la.alsocan.jsonshapeshifter.bindings.IntegerNodeBinding;
import la.alsocan.jsonshapeshifter.bindings.StaticIntegerBinding;
import la.alsocan.jsonshapeshifter.bindings.StaticStringBinding;
import la.alsocan.jsonshapeshifter.bindings.StringNodeBinding;

/**
 * Test application to play with the transformations.
 * 
 * @author Florian Poulin <https://github.com/fpoulin>
 */
public class Main {
	
	private final static String SOURCE_PAYLOAD = "target/classes/payloads/source1.json";
	private final static String SOURCE_SCHEMA = "target/classes/schemas/source.json";
	private final static String TARGET_SCHEMA = "target/classes/schemas/target.json";
	
	/**
	 * An entry point, to test the library in action.
	 * @param args Console args (nothing expected here so far)
	 * @throws IOException if the payload cannot be read
	 */
	public static void main(String[] args) throws IOException {
		
		Schema source;
		Schema target;
		try {
			source = Schema.buildSchema(SOURCE_SCHEMA);
			target = Schema.buildSchema(TARGET_SCHEMA);
		} catch (IOException ex) {
			System.err.println("Oups: " + ex);
			return;
		}
		
		// build the transformation incrementally
		Transformation t = new Transformation(source, target);
		Iterator<SchemaNode> remainings = t.toBindIterator();
		t.addBinding(remainings.next(), new StaticStringBinding("Some string value"));
		t.addBinding(remainings.next(), new StaticIntegerBinding(42));
		t.addBinding(remainings.next(), new StringNodeBinding(source.at("/someSourceString")));
		t.addBinding(remainings.next(), new IntegerNodeBinding(source.at("/someSourceInteger")));

		// produce something
		JsonNode payload = new ObjectMapper().readTree(new File(SOURCE_PAYLOAD));
		JsonNode result = t.apply(payload);
		ObjectMapper om = new ObjectMapper();
		try {
			System.out.println("\nResulting payload:");
			System.out.println(om.writerWithDefaultPrettyPrinter().writeValueAsString(result));
		} catch (JsonProcessingException ex) {
			System.err.println("Oups: " + ex);
		}
	}
}
