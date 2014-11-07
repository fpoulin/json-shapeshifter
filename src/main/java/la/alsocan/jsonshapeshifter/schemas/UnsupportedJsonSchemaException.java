package la.alsocan.jsonshapeshifter.schemas;

/**
 * Signals that an unsupported feature of Json Schema was detected during the parsing.
 * When thrown, a message giving more information about the unsupported feature is 
 * attached to the exception.
 * @author Florian Poulin <https://github.com/fpoulin>
 */
public class UnsupportedJsonSchemaException extends RuntimeException {

	/**
	 * Default constructor.
	 * @param message A message describing the unsupported feature
	 */
	public UnsupportedJsonSchemaException(String message) {
		super(message);
	}
}
