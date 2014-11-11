package la.alsocan.jsonshapeshifter.bindings;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Template;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import la.alsocan.jsonshapeshifter.Default;
import la.alsocan.jsonshapeshifter.schemas.SchemaNode;

/**
 * @author Florian Poulin - https://github.com/fpoulin
 */
public class StringHandlebarsBinding extends Binding<String> {

	private final Template template;
	private final Map<String, Binding> params;
	
	public StringHandlebarsBinding(String template, Map<String, Binding> params) throws IOException {
		
		Handlebars handlebars = new Handlebars();
		this.template = handlebars.compileInline(template);
		this.params = params;
	}

	@Override
	public Set<SchemaNode> getSourceNodes() {
		Set<SchemaNode> nodes = new HashSet<>();
		params.values().stream().forEach((b) -> {
			nodes.addAll(b.getSourceNodes());
		});
		return nodes;
	}

	@Override
	public String getValue(JsonNode payload, List context) {
		
		final Map<String, Object> values = new HashMap<>();
		params.keySet().stream().forEach((key) -> {
			values.put(key, params.get(key).getValue(payload, context));
		});
		
		String result;
		try {
			result = template.apply(values);
		} catch (IOException e) {
			result = Default.DEFAULT_STRING;
		}
		return result;
	}
}
