/*******************************************************************************
 * Copyright (c) 2015-2017 Uwe Köckemann <uwe.kockemann@oru.se>
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 ******************************************************************************/
package org.spiderplan.prolog;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.spiderplan.representation.ConstraintDatabase;
import org.spiderplan.representation.Operator;
import org.spiderplan.representation.expressions.Expression;
import org.spiderplan.representation.expressions.domain.Substitution;
import org.spiderplan.representation.expressions.prolog.PrologConstraint;
import org.spiderplan.representation.logic.Term;
import org.spiderplan.tools.GenericComboBuilder;


/**
 * Static class for some tool methods.
 * @author Uwe Köckemann
 *
 */
public class PrologTools {

	/**
	 * Get all Prolog queries of this operators in a map with a program ID
	 * as a key.
	 * @param o An {@link Operator}.
	 * @return A map from program IDs to queries.
	 */
	public static Map<Term,Collection<PrologConstraint>> getQueries( Operator o ) {
		Map<Term,Collection<PrologConstraint>> queries = new HashMap<Term, Collection<PrologConstraint>>();
		
		for ( Expression c : o.getConstraints() ) {
			if ( c instanceof PrologConstraint ) {
				PrologConstraint rC = (PrologConstraint)c;
				if ( !queries.containsKey(rC.getSubProblemID()) ) {
					queries.put(rC.getSubProblemID(), new ArrayList<PrologConstraint>());
				}
				queries.get(rC.getSubProblemID()).add(rC);
			}
		}
		return queries;
	} 
	
	/**
	 * Get all {@link Substitution} of an {@link Operator} that satisfy a set of
	 * {@link PrologConstraint}s.
	 * @param o An {@link Operator}.
	 * @param C A {@link ConstraintDatabase}.
	 * @return A {@link Collection} of {@link Substitution}s that satisfy
	 * the {@link PrologConstraint}s of <i>o</i>. 
	 */
	public static Collection<Substitution> getSubstitutionsSatisfyingRelationalConstraints( Operator o, ConstraintDatabase C ) {
		Collection<Substitution> r = new ArrayList<Substitution>();
		
		Collection<PrologConstraint> cRels = C.get(PrologConstraint.class);
		
		ConstraintDatabase thisConstColl = new ConstraintDatabase();
		thisConstColl.addAll(o.getConstraints());
		Collection<PrologConstraint> thisRels = thisConstColl.get(PrologConstraint.class);
				
		if ( thisRels.isEmpty() ) {
			return r;
		}
		
		List<List<Substitution>> matches = new ArrayList<List<Substitution>>();
		int i = 0;
		for ( PrologConstraint thisRel : thisRels ) {
			matches.add( new ArrayList<Substitution>() );
			
			for ( PrologConstraint cRel : cRels ) {
				Substitution theta = thisRel.match(cRel);
				if ( theta != null ) {
					matches.get(i).add(theta);
				}
			}

			if( matches.get(i).isEmpty()) {
				return null;
			}
			i++;
		}
		
		GenericComboBuilder<Substitution> cB = new GenericComboBuilder<Substitution>();
		
		List<List<Substitution>> combos = cB.getCombos(matches);
		
		for ( List<Substitution> combo : combos ) {
			Substitution theta = new Substitution();
			
			boolean works = true;
			for ( Substitution theta1 : combo ) {
				works = theta.add(theta1);
				if ( !works ) {
					break;
				}
			}
			if ( works ) {
				r.add(theta);
			}
		}
		/**
		 * Here empty r means no way to satisfy all relational constraints.
		 */
		if ( r.isEmpty() ) {
			return null;
		} else {
			return r;
		}
	}
	
	
}
