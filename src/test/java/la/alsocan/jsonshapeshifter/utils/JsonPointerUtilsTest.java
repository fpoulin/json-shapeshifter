package la.alsocan.jsonshapeshifter.utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import org.junit.Test;

/**
 * @author Florian Poulin <https://github.com/fpoulin>
 */
public class JsonPointerUtilsTest {
	
	@Test
	public void resolutionShouldWorkWithEmptyContext() throws IOException {
		List<Integer> context = new ArrayList();
		String path = JsonPointerUtils.resolvePointer("/something", context);
		assertThat(path, is(equalTo("/something")));
	}
	
	@Test
	public void resolutionShouldWorkWithNonEmptyContext() throws IOException {
		List<Integer> context = new ArrayList();
		context.add(1);
		context.add(2);
		String path = JsonPointerUtils.resolvePointer("/something/{i}/something/{i}/something", context);
		assertThat(path, is(equalTo("/something/1/something/2/something")));
	}
}
