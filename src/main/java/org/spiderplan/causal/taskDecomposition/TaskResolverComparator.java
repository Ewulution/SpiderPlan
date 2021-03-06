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
package org.spiderplan.causal.taskDecomposition;

import java.util.Comparator;

import org.spiderplan.modules.solvers.Resolver;
import org.spiderplan.representation.expressions.causal.Task;

/**
 * Compares two resolvers based on the number of open tasks. 
 * 
 * @author Uwe Köckemann
 *
 */
public class TaskResolverComparator implements Comparator<Resolver> {
		
	@Override
	public int compare(Resolver o1, Resolver o2) {
		int subTasksO1 = 0;
		int subTasksO2 = 0;
		
		for ( Task c : o1.getConstraintDatabase().get(Task.class) ) {
			if ( !c.isAsserted() ) {
				subTasksO1++;
			}
		}
		for ( Task c : o2.getConstraintDatabase().get(Task.class) ) {
			if ( !c.isAsserted() ) {
				subTasksO2++;
			}
		}
		
		return (subTasksO1-subTasksO2);
	}
}
