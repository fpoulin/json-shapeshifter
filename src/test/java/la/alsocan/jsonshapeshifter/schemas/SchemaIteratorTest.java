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
package la.alsocan.jsonshapeshifter.schemas;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.Iterator;
import java.util.NoSuchElementException;
import la.alsocan.jsonshapeshifter.DataSet;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.fail;
import org.junit.Test;

/**
 * @author Florian Poulin - https://github.com/fpoulin
 */
public class SchemaIteratorTest {

	@Test
	public void hasNextShouldOnlyReturnFalseForLastNode() throws IOException {
	
		Schema s = Schema.buildSchema(new ObjectMapper().readTree(DataSet.SIMPLE_SCHEMA));
		Iterator<SchemaNode> it = s.iterator();
		for (int i=0; i<5; i++) {
			assertThat(it.hasNext(), is(true));
			it.next();
		}
		assertThat(it.hasNext(), is(false));
	}
	
	@Test
	public void nextShouldOnlyThrowExceptionAfterLastNode() throws IOException {
	
		Schema s = Schema.buildSchema(new ObjectMapper().readTree(DataSet.SIMPLE_SCHEMA));
		Iterator<SchemaNode> it = s.iterator();
		for (int i=0; i<5; i++) {
			assertThat(it.next(), is(not(nullValue())));
		}
		try {
			it.next();
			fail("Expected exception to be thrown here");
		} catch (Exception e) {
			assertThat(e, is(instanceOf(NoSuchElementException.class)));
		}
	}
	
	@Test
	public void iteratedValuesShouldMatchWithTheSchemaInOrder() throws IOException {
	
		String[] expected = new String [] {
			"/someString", 
			"/someInteger", 
			"/simpleObject", 
			"/simpleObject/stringProperty", 
			"/simpleObject/integerProperty"};
		
		Schema s = Schema.buildSchema(new ObjectMapper().readTree(DataSet.SIMPLE_SCHEMA));
		Iterator<SchemaNode> it = s.iterator();
		for (int i=0; i<5; i++) {
			assertThat(it.next().getSchemaPointer(), is(equalTo(expected[i])));
		}
	}
}
