package la.alsocan.jsonshapeshifter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import la.alsocan.jsonshapeshifter.schemas.SchemaNode;
import la.alsocan.jsonshapeshifter.schemas.Schema;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import la.alsocan.jsonshapeshifter.bindings.ArrayConstantBinding;
import la.alsocan.jsonshapeshifter.bindings.Binding;
import la.alsocan.jsonshapeshifter.bindings.StringConstantBinding;
import la.alsocan.jsonshapeshifter.bindings.StringNodeBinding;

/**
 * Test application to play with the transformations.
 * 
 * @author Florian Poulin <https://github.com/fpoulin>
 */
public class Main {
	
	private final static String SOURCE_PAYLOAD = "target/classes/payloads/allTypes.json";
	private final static String SOURCE_SCHEMA = "target/classes/schemas/allTypes.json";
	private final static String TARGET_SCHEMA = "target/classes/schemas/allTypes.json";
	
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
		
		// test template
		String template = "Handlebars says: {{static}} {{node}} '{{nodeIncollection}}'";
		Map<String, Binding> params = new HashMap<>();
		params.put("static", new StringConstantBinding("type"));
		params.put("node", new StringNodeBinding(source.at("/someSourceString")));
		params.put("nodeIncollection", new StringNodeBinding(source.at("/someSourceStringArray/{i}/{i}")));
		
		// build the transformation incrementally
		Transformation t = new Transformation(target);
		Iterator<SchemaNode> remainings = t.toBindIterator();
		t.addBinding(remainings.next(), new ArrayConstantBinding(12));
		
		// produce something
		System.out.println("\nResulting payload:");
		JsonNode payload = new ObjectMapper().readTree(new File(SOURCE_PAYLOAD));
		JsonNode result = t.apply(payload);
		ObjectMapper om = new ObjectMapper();
		try {
			System.out.println(om.writerWithDefaultPrettyPrinter().writeValueAsString(result));
		} catch (JsonProcessingException ex) {
			System.err.println("Oups: " + ex);
		}
	}
}
