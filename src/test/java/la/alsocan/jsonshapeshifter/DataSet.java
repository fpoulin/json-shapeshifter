/*
 * The MIT License
 *
 * Copyright 2014 Florian Poulin - https://github.com/fpoulin.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package la.alsocan.jsonshapeshifter;

/**
 *
 * @author Florian Poulin - https://github.com/fpoulin
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
	
	public static String COMPLEX_EMBEDDED_COLLECTION_SCHEMA = 
		"{\n" +
		"	\"$schema\": \"http://json-schema.org/draft-04/schema#\",\n" +
		"	\"type\": \"object\",\n" +
		"	\"properties\": {\n" +
		"		\"rootString\" : {\n" +
		"			\"type\": \"string\"\n" +
		"		},\n" +
		"		\"rootArray\": {\n" +
		"			\"type\": \"array\",\n" +
		"			\"items\": {\n" +
		"				\"type\": \"array\",\n" +
		"				\"items\": {\n" +
		"					\"type\": \"object\",\n" +
		"					\"properties\": {\n" +
		"						\"someString\" : {\n" +
		"							\"type\": \"string\"\n" +
		"						},\n" +
		"						\"anotherString\" : {\n" +
		"							\"type\": \"string\"\n" +
		"						},\n" +
		"						\"someArray\" : {\n" +
		"							\"type\": \"array\",\n" +
		"							\"items\": {\n" +
		"								\"type\": \"string\"\n" +
		"							}\n" +
		"						}\n" +
		"					},\n" +
		"					\"additionalProperties\" : false,\n" +
		"					\"required\": [\"someString\", \"anotherString\", \"someArray\"]\n" +
		"				}\n" +
		"			}\n" +
		"		}\n" +
		"	},\n" +
		"	\"additionalProperties\" : false,\n" +
		"	\"required\": [\"rootArray\"]\n" +
		"}";
	
	public static String COMPLEX_EMBEDDED_COLLECTION_PAYLOAD = 
		"{\n" +
		"	\"rootString\" : \"rootString\",\n" +
		"	\"rootArray\": [\n" +
		"		[{\n" +
		"			\"someString\": \"value1\",\n" +
		"			\"anotherString\": \"value2\",\n" +
		"			\"someArray\": [\"a\", \"b\", \"c\", \"d\"]\n" +
		"		}],\n" +
		"		[{\n" +
		"			\"someString\": \"value3\",\n" +
		"			\"anotherString\": \"value4\",\n" +
		"			\"someArray\": [\"e\", \"f\"]\n" +
		"		}],\n" +
		"		[{\n" +
		"			\"someString\": \"value5\",\n" +
		"			\"anotherString\": \"value6\",\n" +
		"			\"someArray\": [\"g\"]\n" +
		"		}]\n" +
		"	]\n" +
		"}";
}
