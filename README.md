# json-shapeshifter

An incremental Json-to-Json transformation library written in Java.

It provides a step-by-step API to specify a **transformation** from a **source schema** to a **target schema**, both defined using [Json Schema](http://json-schema.org/).
The resulting transformation can be applied on Json instances of the source schema, to produce instances of the target schema.
Although it *does transform* Json to Json, its primary focus is on the *process of specifying* this transformation.

If you are looking for a pure Json-to-Json transformation tool in Java (or compatible), have a look at [JQ](http://stedolan.github.io/jq/), [JOLT](https://github.com/bazaarvoice/jolt) or [JsonIQ](http://www.jsoniq.org/) which are probably faster and more approriate. Or you can probably hack something with [JsonPath](https://github.com/jayway/JsonPath) too.

Anyway, so far it's a wannabe. :sweat_smile:

# Try it

* Checkout out the sources
* Open the project in Netbeans (yeah, that's the easiest for now)
* In the `Projects` view, right-click on project and `Custom` --> `Do the damn thing`
* Something (useless, so far) will happen in the console

# License

Under [MIT License](http://opensource.org/licenses/MIT).