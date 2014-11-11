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
package la.alsocan.jsonshapeshifter.bindings;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import static la.alsocan.jsonshapeshifter.bindings.AbstractNodeBinding.jsonPointer;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import org.junit.Test;

/**
 * @author Florian Poulin - https://github.com/fpoulin
 */
public class JsonPointerResolutionTest {
	
	@Test
	public void resolutionShouldWorkWithEmptyContext() throws IOException {
		List<Integer> context = new ArrayList();
		String path = jsonPointer("/something", context);
		assertThat(path, is(equalTo("/something")));
	}
	
	@Test
	public void resolutionShouldWorkWithNonEmptyContext() throws IOException {
		List<Integer> context = new ArrayList();
		context.add(1);
		context.add(2);
		String path = jsonPointer("/something/{i}/something/{i}/something", context);
		assertThat(path, is(equalTo("/something/1/something/2/something")));
	}
}
