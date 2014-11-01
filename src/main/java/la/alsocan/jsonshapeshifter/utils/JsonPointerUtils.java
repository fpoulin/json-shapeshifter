package la.alsocan.jsonshapeshifter.utils;

import java.util.List;

/**
 * @author Florian Poulin <https://github.com/fpoulin>
 */
public class JsonPointerUtils {
	
	public static final String PATH_DELIMITER_REGEX = "\\{i\\}";
	
	public static String resolvePointer(String path, List<Integer> context) {
		for (Integer index : context) {
			path = path.replaceFirst(PATH_DELIMITER_REGEX, String.valueOf(index));
		}
		return path;
	}
}
