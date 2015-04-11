# json-shapeshifter 
[![Travis branch](https://img.shields.io/travis/fpoulin/json-shapeshifter/master.svg)](https://travis-ci.org/fpoulin/json-shapeshifter)
[![Coveralls branch](https://img.shields.io/coveralls/fpoulin/json-shapeshifter/master.svg)](https://coveralls.io/r/fpoulin/json-shapeshifter?branch=master)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/la.alsocan/json-shapeshifter/badge.svg)](https://maven-badges.herokuapp.com/maven-central/la.alsocan/json-shapeshifter/)

An incremental Json-to-Json transformation library written in Java.

It provides a step-by-step API to specify a **transformation** from a **source schema** to a **target schema**, both defined using [Json Schema](http://json-schema.org/).
The resulting transformation can be applied on Json instances of the source schema, to produce instances of the target schema. Got it?

Although it *does transform* Json to Json, its primary focus is on the *process of specifying* this transformation. If you are looking for a pure Json-to-Json transformation tool in Java (or compatible), have a look at [JQ](http://stedolan.github.io/jq/), [JOLT](https://github.com/bazaarvoice/jolt) or [JsonIQ](http://www.jsoniq.org/) which are probably faster and more approriate.

### TL;DR --> Code

```java
Schema source = Schema.buildSchema("source.json");
Schema target = Schema.buildSchema("target.json");

Transformation t = new Transformation (source, target);

// iterate through all target nodes needing a binding
Iterator<SchemaNode> it = t.toBind();
while (it.hasNext()) {

	SchemaNode targetNode = it.next();

	// fetch all possible source nodes for this node
	Set<SchemaNode> sources = t.legalNodesFor(targetNode);

	// show this list to your user and let her choose amongst the
	// various Binding options (constant, node, handlebars, etc.) This
	// is where the value is: make this transformation process interactive,
	// let your user decide how to transform, at runtime!
	Binding b = new StringNodeBinding(source.at("/some/string/field"));

	// use the selected binding for the target node
	t.bind(targetNode, b);
}

// load a Json instance of the source schema (using Jackson here)
ObjectMapper om = new ObjectMapper();
JsonNode payload = om.readTree(new File("payload.json"));
if (!source.validate(payload).isSuccess()) {
	...
}

// transform it
JsonNode result = t.apply(payload);
target.validate(result).isSuccess(); // returns true, yeah baby!

// display it (using Jackson again)
String s = om.writerWithDefaultPrettyPrinter().writeValueAsString(result)
System.out.println(s);
```

# Try it

### Get it from Maven Central

That's the easiest. Current version is `0.1.1` (it's a beta but it does things)

```xml
<dependency>
   <groupId>la.alsocan</groupId>
   <artifactId>json-shapeshifter</artifactId>
   <version>0.1.1</version>
</dependency>
```

### Build from the source

* Checkout out the sources
* Open the project in Netbeans (yeah, that's the easiest for now)
* In the `Projects` view, right-click on project and `Custom` --> `Do the damn thing`
* The project shall be built and the artifact installed in your local repository
* For convenience, a `Main` class is executed

# Credits

This project uses [Jackson](https://github.com/FasterXML/jackson) massively, a bit of [json-schema-validator](https://github.com/fge/json-schema-validator) and [Handlebars](https://github.com/jknack/handlebars.java) for one particularly useful [binding class](https://github.com/fpoulin/json-shapeshifter/blob/master/src/main/java/la/alsocan/jsonshapeshifter/bindings/StringHandlebarsBinding.java). Of course [Maven](http://maven.apache.org/) and [JUnit](http://junit.org/) / [Hamcrest](https://github.com/hamcrest/JavaHamcrest) are there to support the build and tests.

# License

Under [MIT License](http://opensource.org/licenses/MIT).
