package la.alsocan.jsonshapeshifter;

import com.fasterxml.jackson.databind.JsonNode;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * @author Florian Poulin <https://github.com/fpoulin>
 */
public class Defaults {
	
	public static final Boolean DEFAULT_BOOLEAN = false;
	public static final Integer DEFAULT_INTEGER = 0;
	public static final Number DEFAULT_NUMBER = 0.0;
	public static final String DEFAULT_STRING = "?";
	public static final Iterator<JsonNode> DEFAULT_ARRAY = 
		new Iterator<JsonNode>() {
			@Override public boolean hasNext() { return false; }
			@Override public JsonNode next()   { throw new NoSuchElementException(); }
		};
	
}
