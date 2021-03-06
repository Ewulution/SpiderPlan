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
package org.spiderplan.causal.forwardPlanning.fastForward;

import java.util.ArrayList;

import org.spiderplan.representation.logic.Term;


/**
 * Data structure for quick Planning Graph generation.
 * 
 * Only used by FastForwardHeuristic
 * @author Uwe Köckemann
 *
 */
class EarliestLayerOperator {
	public int earliestLayer = -1;
	
	public Term name;
	
	public ArrayList<EarliestLayerSVA> preconditions = new ArrayList<EarliestLayerSVA>();
	public ArrayList<EarliestLayerSVA> effects = new ArrayList<EarliestLayerSVA>();
	
	public boolean isFirstTimeApplicable( int l ) {
		if ( earliestLayer != -1 ) {
			return false;
		}
	
		for ( EarliestLayerSVA p : preconditions ) {
			if ( p.earliestLayer == -1 || p.earliestLayer > l ) {
				return false;
			}
		}
		this.earliestLayer = l;
		
		return true;
	}
	
	@Override
	public int hashCode() {
		return name.hashCode();
	}
	
	@Override
	public boolean equals( Object o ) {
		if ( !(o instanceof EarliestLayerOperator) ) {
			return false;
		}
		EarliestLayerOperator a = (EarliestLayerOperator)o;
		
		return a.name.equals(this.name);
	}
}
