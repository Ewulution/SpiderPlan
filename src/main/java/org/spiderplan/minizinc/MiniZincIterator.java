/*******************************************************************************
 * Copyright (c) 2015 Uwe Köckemann <uwe.kockemann@oru.se>
 *  
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the
 * "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 * WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *******************************************************************************/
package org.spiderplan.minizinc;

import java.util.Collection;
import org.spiderplan.modules.configuration.ConfigurationManager;
import org.spiderplan.modules.solvers.Resolver;
import org.spiderplan.modules.solvers.ResolverIterator;
import org.spiderplan.representation.constraints.ConstraintCollection;
import org.spiderplan.representation.logic.Substitution;

public class MiniZincIterator extends ResolverIterator {

	private int solutionIndex = 1;
	
	private String program;
	private String data;
	
	public MiniZincIterator( String program, String data, String name, ConfigurationManager cM) {
		super(name, cM);
		this.program  = program;
		this.data = data;
	}

	@Override
	public Resolver next( ConstraintCollection C ) {
		Collection<Substitution> solutions = MiniZincAdapter.runMiniZinc(program, data, false, solutionIndex++);
		if ( solutions != null ) {			
			return new Resolver(solutions.iterator().next());
		} else {
			return null;
		}
	}
	
}
