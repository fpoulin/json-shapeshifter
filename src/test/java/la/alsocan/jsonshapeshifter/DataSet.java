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
	
	public final static String SIMPLE_COLLECTION_SCHEMA = 
		"{\n" +
		"	\"$schema\": \"http://json-schema.org/draft-04/schema#\",\n" +
		"	\"type\": \"object\",\n" +
		"	\"properties\": {\n" +
		"		\"someString\": {\n" +
		"			\"type\": \"string\"\n" +
		"		},\n" +
		"		\"someStringArray\": {\n" +
		"			\"type\": \"array\",\n" +
		"			\"items\": {\n" +
		"				\"type\": \"string\"\n" +
		"			}\n" +
		"		},\n" +
		"		\"someIntegerArray\": {\n" +
		"			\"type\": \"array\",\n" +
		"			\"items\": {\n" +
		"				\"type\": \"integer\"\n" +
		"			}\n" +
		"		}\n" +
		"	},\n" +
		"	\"additionalProperties\" : false,\n" +
		"	\"required\": [\"someString\", \"someStringArray\", \"someIntegerArray\"]\n" +
		"}";
	
	public final static String SIMPLE_COLLECTION_PAYLOAD = 
		"{\n" +
		"	\"someString\": \"string1\",\n" +
		"	\"someStringArray\": [\"a\", \"b\", \"c\", \"d\", \"e\"],\n" +
		"	\"someIntegerArray\": [1, 2, 3, 4, 5, 6, 7, 8]\n" +
		"}";
	
	public final static String EMBEDDED_COLLECTION_SCHEMA = 
		"{\n" +
		"	\"$schema\": \"http://json-schema.org/draft-04/schema#\",\n" +
		"	\"type\": \"object\",\n" +
		"	\"properties\": {\n" +
		"		\"someArrayOfArray\": {\n" +
		"			\"type\": \"array\",\n" +
		"			\"items\": {\n" +
		"				\"type\": \"array\",\n" +
		"				\"items\": {\n" +
		"					\"type\": \"string\"\n" +
		"				}\n" +
		"			}\n" +
		"		}\n" +
		"	},\n" +
		"	\"additionalProperties\" : false,\n" +
		"	\"required\": [\"someArrayOfArray\"]\n" +
		"}";

	public final static String EMBEDDED_COLLECTION_PAYLOAD = 
		"{\n" +
		"	\"someArrayOfArray\": [[\"a\", \"b\", \"c\"], [\"d\", \"e\"], [\"f\"]]\n" +
		"}";
	
	public final static String ALL_TYPES_SCHEMA = 
		"{\n" +
		"	\"$schema\": \"http://json-schema.org/draft-04/schema#\",\n" +
		"	\"type\": \"object\",\n" +
		"	\"properties\": {\n" +
		"		\"someArray\": {\n" +
		"			\"type\": \"array\",\n" +
		"			\"items\": {\n" +
		"				\"type\": \"object\",\n" +
		"				\"properties\": {\n" +
		"					\"someBoolean\": {\n" +
		"						\"type\": \"boolean\"\n" +
		"					},\n" +
		"					\"someInteger\": {\n" +
		"						\"type\": \"integer\"\n" +
		"					},\n" +
		"					\"someNumber\": {\n" +
		"						\"type\": \"number\"\n" +
		"					},\n" +
		"					\"someNull\": {\n" +
		"						\"type\": \"null\"\n" +
		"					},\n" +
		"					\"someString\": {\n" +
		"						\"type\": \"string\"\n" +
		"					}\n" +
		"				},\n" +
		"				\"additionalProperties\" : false,\n" +
		"				\"required\": [\"someBoolean\", \"someInteger\", \"someNumber\", \"someNull\", \"someString\"]\n" +
		"			}\n" +
		"		}\n" +
		"	},\n" +
		"  \"additionalProperties\" : false,\n" +
		"	\"required\": [\"someArray\"]\n" +
		"}";
}
