package la.alsocan.jsonshapeshifter.bindings;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Template;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Florian Poulin <https://github.com/fpoulin>
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
	public String getValue(JsonNode payload, List context) {
		
		final Map<String, Object> values = new HashMap<>();
		for (String key : params.keySet()) {
			values.put(key, params.get(key).getValue(payload, context));
		}
		
		String result;
		try {
			result = template.apply(values);
		} catch (IOException e) {
			result = "?";
		}
		return result;
	}
}
