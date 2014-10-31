package la.alsocan.jsonshapeshifter;

/**
 *
 * @author Florian Poulin <https://github.com/fpoulin>
 */
public class DataSet {
	
	public final static String SIMPLE_SCHEMA = 
		"{\n" +
		"	\"$schema\": \"http://json-schema.org/draft-04/schema#\",\n" +
		"	\"type\": \"object\",\n" +
		"	\"properties\": {\n" +
		"		\"someString\": {\n" +
		"			\"type\": \"string\"\n" +
		"		},\n" +
		"		\"someInteger\": {\n" +
		"			\"type\": \"integer\"\n" +
		"		},\n" +
		"		\"simpleObject\": {\n" +
		"			\"type\": \"object\",\n" +
		"			\"properties\": {\n" +
		"				\"stringProperty\": {\n" +
		"					\"type\": \"string\"\n" +
		"				},\n" +
		"				\"integerProperty\": {\n" +
		"					\"type\": \"integer\"\n" +
		"				}\n" +
		"			},\n" +
		"			\"additionalProperties\" : false,\n" +
		"			\"required\": [\"stringProperty\", \"integerProperty\"]\n" +
		"		}\n" +
		"	},\n" +
		"	\"additionalProperties\" : false,\n" +
		"	\"required\": [\"someString\", \"someInteger\", \"simpleObject\"]\n" +
		"}";
	
	public final static String SIMPLE_PAYLOAD = 
		"{\n" +
		"	\"someString\": \"string1\",\n" +
		"	\"someInteger\": 1,\n" +
		"	\"simpleObject\": {\n" +
		"		\"stringProperty\": \"string2\",\n" +
		"		\"integerProperty\": 2\n" +
		"	}\n" +
		"}";
}
