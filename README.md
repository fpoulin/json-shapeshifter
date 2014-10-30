json-shapeshifter
=================

Json Shapeshifter is an incremental Json-to-Json transformation library written in Java.

It provides a step-by-step API to specify a **transformation** from a **source schema** to a **target schema**, both defined using [Json Schema] [0].
The resulting transformation can be applied on Json instances of the source schema, to produce instances of the target schema.

In other words, although it *does transform* Json to Json, its primary focus is on the *process of specifying* this transformation.

If you are looking for a pure Json-to-Json transformation tool in Java (or compatible), have a look at [JQ] [1], [JOLT] [2] or [JsonIQ] [3] which are probably faster and more approriate.
Or you can probably hack something with [JsonPath] [4] too.

Anyway, so far it's a wannabe. :sweat_smile:

[0] http://json-schema.org/               Json Schema
[1] http://stedolan.github.io/jq/         JQ
[2] https://github.com/bazaarvoice/jolt   JOLT
[3] http://www.jsoniq.org/                JsonIQ
[4] https://github.com/jayway/JsonPath    JsonPath
