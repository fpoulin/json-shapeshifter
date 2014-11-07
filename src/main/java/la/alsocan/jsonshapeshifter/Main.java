package la.alsocan.jsonshapeshifter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import la.alsocan.jsonshapeshifter.schemas.Schema;
import java.io.IOException;
import java.util.Iterator;
import la.alsocan.jsonshapeshifter.bindings.ArrayNodeBinding;
import la.alsocan.jsonshapeshifter.schemas.SchemaNode;

/**
 * Test application to play with the transformations.
 * 
 * @author Florian Poulin <https://github.com/fpoulin>
 */
public class Main {
	
	private final static String SOURCE_PAYLOAD = "target/classes/payloads/source.json";
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
		Iterator<SchemaNode> it = t.toBind();
		t.bind(it.next(), new ArrayNodeBinding(source.at("/rootArray")));
		showPossibilities(t, target.at("/rootArray/{i}/{i}/someArray/{i}"));
		t.bind(it.next(), new ArrayNodeBinding(source.at("/rootArray/{i}")));
		showPossibilities(t, target.at("/rootArray/{i}/{i}/someArray/{i}"));
		it.next();
		it.next();
		t.bind(it.next(), new ArrayNodeBinding(source.at("/rootArray/{i}/{i}/someArray")));
		showPossibilities(t, target.at("/rootArray/{i}/{i}/someArray/{i}"));
		
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
	
	public static void showPossibilities(Transformation t, SchemaNode target) {
		
		t.legalNodesFor(target).stream().forEach((source) -> {
			System.out.println("   " + source.getSchemaPointer());
		});
		System.out.println();
	} 
}
