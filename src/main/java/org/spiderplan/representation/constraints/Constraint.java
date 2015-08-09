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
package org.spiderplan.representation.constraints;

import java.util.Collection;

import org.spiderplan.representation.constraints.constraintInterfaces.Matchable;
import org.spiderplan.representation.constraints.constraintInterfaces.Substitutable;
import org.spiderplan.representation.logic.Atomic;
import org.spiderplan.representation.logic.Term;
import org.spiderplan.tools.statistics.Statistics;

/**
 * Superclass to all constraints. The only information kept in this (mostly) abstract class
 * is a flag that is set when a constraint is asserted.
 * An asserted constraint can not be violated (unless other 
 * constraints are removed) and can thus be treated as knowledge.
 * <p>
 * Example: A {@link AllenConstraint} <i>A before B</i> in non-asserted state is
 * a requirement. Once it is propagated it becomes asserted and
 * can thus be treated as given.
 * <p>
 * Note: Constraints should only be removed if it can be assured that constraints
 * that depend on them are set to non-asserted. This is possible, for instance,
 * when {@link AllenConstraint}s are added to resolve resource conflicts, but
 * removed again before another action is added by the planner so that the scheduler
 * does not need to commit to a certain resolver.  
 * <p>
 * Often a sub-class to this class implements 
 * {@link Substitutable} and/or {@link Matchable}
 * 
 * @author Uwe Köckemann
 *
 */
public abstract class Constraint {
	
	private final Term type;
	
	protected  boolean simpleConsistency = false;
	private boolean isAsserted = false;

	public static Statistics stats = new Statistics();
	
	public Constraint( Term type ) { this.type = type; };

	public Term getType() { return type; }
	
//	public boolean isAsserted() { return isAsserted; }
//	public void setAsserted( boolean asserted ) { this.isAsserted = asserted; }
	
	/**
	 * Get all variable {@link Term}s in this {@link Constraint}.
	 * @return
	 */
	public abstract Collection<Term> getVariableTerms();
	public abstract Collection<Term> getGroundTerms();
	public abstract Collection<Atomic> getAtomics();
	
	/**
	 * Whether or not isConsistent() method is overwritten.
	 * @return <i>true</i> iff <i>isConsistent()</i> was overwritten by sub-class.
	 */
	public boolean supportsSimpleConsistency() {
		return simpleConsistency;
	}
	
	/**
	 * Some constraints are self-sufficient to determine consistency and can overwrite
	 * this function to decide whether or not they are satisfied.
	 * @return <i>true</i> iff the constraint is satisfied.
	 */
	public boolean isConsistent() {
		throw new IllegalAccessError("Not supported by this class. Check support with ");
	}
	
	/**
	 * Returns an exact copy of this instance that
	 * can be changed without changing the original.
	 * @return A copy of this constraint.
	 */
//	public abstract Constraint copy();

	@Override
	public abstract boolean equals( Object o );
	
	@Override
	public abstract int hashCode( );
	
	@Override
	public abstract String toString();
	
}