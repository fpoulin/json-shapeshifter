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
package la.alsocan.jsonshapeshifter.transformations;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.Iterator;
import java.util.Set;
import la.alsocan.jsonshapeshifter.DataSet;
import la.alsocan.jsonshapeshifter.Transformation;
import la.alsocan.jsonshapeshifter.bindings.ArrayNodeBinding;
import la.alsocan.jsonshapeshifter.bindings.IllegalBindingException;
import la.alsocan.jsonshapeshifter.bindings.StringNodeBinding;
import la.alsocan.jsonshapeshifter.schemas.Schema;
import la.alsocan.jsonshapeshifter.schemas.SchemaNode;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import org.junit.Test;

/**
 * @author Florian Poulin - https://github.com/fpoulin
 */
public class AllowedNodeSourcesTest {
	
	@Test
	public void allowedNodeSourceShouldWorkForComplexEmbeddedCollections() throws IOException {

		Schema source = Schema.buildSchema(new ObjectMapper().readTree(DataSet.COMPLEX_EMBEDDED_COLLECTION_SCHEMA));
		Schema target = Schema.buildSchema(new ObjectMapper().readTree(DataSet.COMPLEX_EMBEDDED_COLLECTION_SCHEMA));
		Transformation t = new Transformation(source, target);
		
		SchemaNode observedNode = target.at("/rootArray/{i}/{i}/someArray/{i}");
		
		Iterator<SchemaNode> it = t.toBind();
		it.next();
		t.bind(it.next(), new ArrayNodeBinding(source.at("/rootArray")));
		
		Set<SchemaNode> allowedNodes = t.legalNodesFor(observedNode);
		assertThat(allowedNodes.size(), is(equalTo(1)));
		assertThat(allowedNodes, hasItem(source.at("/rootString")));
		
		t.bind(it.next(), new ArrayNodeBinding(source.at("/rootArray/{i}")));
		
		allowedNodes = t.legalNodesFor(observedNode);
		assertThat(allowedNodes.size(), is(equalTo(3)));
		assertThat(allowedNodes, hasItem(source.at("/rootString")));
		assertThat(allowedNodes, hasItem(source.at("/rootArray/{i}/{i}/someString")));
		assertThat(allowedNodes, hasItem(source.at("/rootArray/{i}/{i}/anotherString")));
		
		it.next();
		it.next();
		t.bind(it.next(), new ArrayNodeBinding(source.at("/rootArray/{i}/{i}/someArray")));
		
		allowedNodes = t.legalNodesFor(observedNode);
		assertThat(allowedNodes.size(), is(equalTo(4)));
		assertThat(allowedNodes, hasItem(source.at("/rootString")));
		assertThat(allowedNodes, hasItem(source.at("/rootArray/{i}/{i}/someString")));
		assertThat(allowedNodes, hasItem(source.at("/rootArray/{i}/{i}/anotherString")));
		assertThat(allowedNodes, hasItem(source.at("/rootArray/{i}/{i}/someArray/{i}")));
	}
	
	@Test
	public void onlyLegalNodesShouldBeAllowedForBinding() throws IOException {
	
		Schema source = Schema.buildSchema(new ObjectMapper().readTree(DataSet.COMPLEX_EMBEDDED_COLLECTION_SCHEMA));
		Schema target = Schema.buildSchema(new ObjectMapper().readTree(DataSet.COMPLEX_EMBEDDED_COLLECTION_SCHEMA));
		Transformation t = new Transformation(source, target);
		
		SchemaNode observedNode = target.at("/rootArray/{i}/{i}/someArray/{i}");
		
		Iterator<SchemaNode> it = t.toBind();
		it.next();
		t.bind(it.next(), new ArrayNodeBinding(source.at("/rootArray")));
		t.bind(it.next(), new ArrayNodeBinding(source.at("/rootArray/{i}")));
		it.next();
		it.next();
		
		try{
			t.bind(observedNode, new StringNodeBinding(source.at("/rootArray/{i}/{i}/someArray/{i}")));
			fail("Expecting an IllegalBindingException to be thrown here");
		} catch(IllegalBindingException e) {}
		
		t.bind(it.next(), new ArrayNodeBinding(source.at("/rootArray/{i}/{i}/someArray")));
		try{
			t.bind(observedNode, new StringNodeBinding(source.at("/rootArray/{i}/{i}/someArray/{i}")));
		} catch(IllegalBindingException e) {
			fail("Expecting an IllegalBindingException to be thrown here");
		}
	}
}
