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
package org.spiderplan;

import org.spiderplan.modules.FinallySolver;
import org.spiderplan.modules.SolverStack;
import org.spiderplan.modules.configuration.ConfigurationManager;
import org.spiderplan.modules.solvers.Core;
import org.spiderplan.modules.solvers.Module;
import org.spiderplan.modules.solvers.Core.State;
import org.spiderplan.modules.tools.ModuleFactory;
import org.spiderplan.representation.ConstraintDatabase;
import org.spiderplan.representation.expressions.Statement;
import org.spiderplan.representation.expressions.cost.Cost;
import org.spiderplan.representation.expressions.interaction.InteractionConstraint;
import org.spiderplan.representation.expressions.misc.Finally;
import org.spiderplan.representation.expressions.optimization.OptimizationTarget;
import org.spiderplan.representation.logic.Term;
import org.spiderplan.representation.types.TypeManager;
import org.spiderplan.tools.statistics.Statistics;

import junit.framework.TestCase;

@SuppressWarnings("javadoc")
public class TestStackSolver extends TestCase {

	@Override
	public void setUp() throws Exception {
	}

	@Override
	public void tearDown() throws Exception {
		Statistics.reset();
	}
	
	/**
	 * Basic test with two solvers and no backtracking. 
	 * It becomes inconsistent in the second module after 
	 * adding to the cost.
	 */
	public void testWithoutBacktracking() {
		/**
		 * Setup Modules
		 */
		ConfigurationManager cM = new ConfigurationManager();		
		cM.add("Solver");
		cM.set("Solver", "class", "StackSolver");
		cM.set("Solver", "solvers", "Domain,Cost");
		cM.add("Cost");
		cM.set("Cost", "class", "CostSolver");
		cM.add("Domain");
		cM.set("Domain", "class", "DomainSolver");

		SolverStack solver = new SolverStack("Solver", cM);
		
		/**
		 * Setup Core
		 */
		Core testCore = new Core();
		testCore.setContext( new ConstraintDatabase() );
		testCore.setTypeManager(new TypeManager());
				
		testCore.getContext().add(new Cost(Term.createComplex("less-than",  Term.createConstant("c"), Term.createInteger(15))));
		testCore.getContext().add(new Cost(Term.createComplex("add", Term.createConstant("c"), Term.createInteger(10))));
		
		testCore = solver.run(testCore);
		assertTrue(testCore.getResultingState("Solver").equals(State.Consistent));
		
		testCore.getContext().add(new Cost(Term.createComplex("add", Term.createConstant("c"), Term.createInteger(10))));
		testCore = solver.run(testCore);
		assertTrue(testCore.getResultingState("Solver").equals(State.Inconsistent));
	}
	
	/**
	 * Five solvers and simple backtracking. 
	 * The second resolver of the interaction
	 * constraint works so there is one backtrack
	 * step.
	 */
	public void testWithBacktracking() {
		/**
		 * Setup Modules
		 */
		ConfigurationManager cM = new ConfigurationManager();		
		cM.add("Solver");
		cM.set("Solver", "class", "StackSolver");
		cM.set("Solver", "solvers", "Domain,Cost,Prolog,IC");
		cM.add("Cost");
		cM.set("Cost", "class", "CostSolver");
		cM.add("Domain");
		cM.set("Domain", "class", "DomainSolver");
		cM.add("Prolog");
		cM.set("Prolog", "class", "PrologSolver");
		cM.add("IC");
		cM.set("IC", "class", "InteractionConstraintSolver");
		cM.set("IC", "consistencyChecker", "Domain");
		SolverStack solver = new SolverStack("Solver", cM);
				
		/**
		 * Setup Core
		 */
		Core testCore = new Core();
		testCore.setContext( new ConstraintDatabase() );
		testCore.setTypeManager(new TypeManager());
		
		testCore.getTypeManager().addSimpleEnumType("t", "a,b,c,d,e,f");
		
		InteractionConstraint ic = new InteractionConstraint(Term.createConstant("ic"));
		ConstraintDatabase r1 = new ConstraintDatabase();
		r1.add(new Cost(Term.createComplex("add", Term.createConstant("c"), Term.createInteger(10))));
		ConstraintDatabase r2 = new ConstraintDatabase();
		r2.add(new Cost(Term.createComplex("add", Term.createConstant("d"), Term.createInteger(3))));
		ic.addResolver(r1);
		ic.addResolver(r2);
		testCore.getContext().add(ic);
				
		testCore.getContext().add(new Cost(Term.createComplex("less-than",  Term.createConstant("c"), Term.createInteger(15))));
		testCore.getContext().add(new Cost(Term.createComplex("less-than",  Term.createConstant("d"), Term.createInteger(5))));
		testCore.getContext().add(new Cost(Term.createComplex("add", Term.createConstant("c"), Term.createInteger(10))));
		
		testCore = solver.run(testCore);
		assertTrue(testCore.getResultingState("Solver").equals(State.Consistent));
	}
	
	/**
	 * The same as the previous test case but with a different order
	 * of solver.
	 */
	public void testWithBacktrackingICFirst() {
		/**
		 * Setup Modules
		 */
		ConfigurationManager cM = new ConfigurationManager();		
		cM.add("Solver");
		cM.set("Solver", "class", "StackSolver");
		cM.set("Solver", "solvers", "IC,Domain,Cost,Prolog");
		cM.add("Cost");
		cM.set("Cost", "class", "CostSolver");
		cM.add("Domain");
		cM.set("Domain", "class", "DomainSolver");
		cM.add("Prolog");
		cM.set("Prolog", "class", "PrologSolver");
		cM.add("IC");
		cM.set("IC", "class", "InteractionConstraintSolver");
		cM.set("IC", "consistencyChecker", "Domain");
		SolverStack solver = new SolverStack("Solver", cM);
				
		/**
		 * Setup Core
		 */
		Core testCore = new Core();
		testCore.setContext( new ConstraintDatabase() );
		testCore.setTypeManager(new TypeManager());
		
		testCore.getTypeManager().addSimpleEnumType("t", "a,b,c,d,e,f");
		
		InteractionConstraint ic = new InteractionConstraint(Term.createConstant("ic"));
		ConstraintDatabase r1 = new ConstraintDatabase();
		r1.add(new Cost(Term.createComplex("add", Term.createConstant("c"), Term.createInteger(10))));
		ConstraintDatabase r2 = new ConstraintDatabase();
		r2.add(new Cost(Term.createComplex("add", Term.createConstant("d"), Term.createInteger(3))));
		ic.addResolver(r1);
		ic.addResolver(r2);
		testCore.getContext().add(ic);
				
		testCore.getContext().add(new Cost(Term.createComplex("less-than",  Term.createConstant("c"), Term.createInteger(15))));
		testCore.getContext().add(new Cost(Term.createComplex("less-than",  Term.createConstant("d"), Term.createInteger(5))));
		testCore.getContext().add(new Cost(Term.createComplex("add", Term.createConstant("c"), Term.createInteger(10))));
		
		testCore = solver.run(testCore);
		assertTrue(testCore.getResultingState("Solver").equals(State.Consistent));
	}
	
	/**
	 * Test with five solvers and backtracking.
	 * Here, testing ICs first is bad because
	 * an obvious inconsistency will be found
	 * too late. 
	 */
	public void testWithMultiStageBacktracking() {
		/**
		 * Setup Modules
		 */
		ConfigurationManager cM = new ConfigurationManager();		
		cM.add("Solver");
		cM.set("Solver", "class", "StackSolver");
		cM.set("Solver", "solvers", "IC,Domain,Cost,Prolog");
		cM.set("Solver", "keepStatistics", "true");
		cM.add("Cost");
		cM.set("Cost", "class", "CostSolver");
		cM.add("Domain");
		cM.set("Domain", "class", "DomainSolver");
		cM.add("Prolog");
		cM.set("Prolog", "class", "PrologSolver");
		cM.add("IC");
		cM.set("IC", "class", "InteractionConstraintSolver");
		cM.set("IC", "consistencyChecker", "Domain");
		SolverStack solver = new SolverStack("Solver", cM);
				
		/**
		 * Setup Core
		 */
		Core testCore = new Core();
		testCore.setContext( new ConstraintDatabase() );
		testCore.setTypeManager(new TypeManager());
		
		testCore.getTypeManager().addSimpleEnumType("t", "a,b,c,d,e,f");
		testCore.getTypeManager().addSimpleEnumType("boolean", "true,false");
		testCore.getTypeManager().attachTypes(Term.parse("(p t)"), Term.createConstant("boolean"));
		
		InteractionConstraint ic = new InteractionConstraint(Term.parse("(ic ?K ?X)"));
		ic.getCondition().add(new Statement("(?K (p ?X) true)"));
		
		ConstraintDatabase r1 = new ConstraintDatabase();
		r1.add(new Cost(Term.createComplex("add", Term.createConstant("c"), Term.createInteger(10))));
		ConstraintDatabase r2 = new ConstraintDatabase();
		r2.add(new Cost(Term.createComplex("add", Term.createConstant("d"), Term.createInteger(3))));
		ic.addResolver(r1);
		ic.addResolver(r2);
		testCore.getContext().add(ic);
				
		testCore.getContext().add(new Statement("(i1 (p a) true)"));
		testCore.getContext().add(new Statement("(i2 (p b) true)"));
		testCore.getContext().add(new Statement("(i3 (p c) true)"));
		testCore.getContext().add(new Cost(Term.createComplex("less-than",  Term.createConstant("c"), Term.createInteger(15))));
		testCore.getContext().add(new Cost(Term.createComplex("less-than",  Term.createConstant("d"), Term.createInteger(10))));
		testCore.getContext().add(new Cost(Term.createComplex("add", Term.createConstant("c"), Term.createInteger(10))));
		
		
		testCore = solver.run(testCore);
		assertTrue(testCore.getResultingState("Solver").equals(State.Consistent));
		
//		assertTrue(Statistics.getCounter("[Solver] Applied resolvers") == 14);
		
		assertTrue(Statistics.getCounter("[Solver] Calling solver 0 IC") == 15);
		assertTrue(Statistics.getCounter("[Solver] Calling solver 1 Domain") == 8);
		assertTrue(Statistics.getCounter("[Solver] Calling solver 2 Cost") == 8);
		assertTrue(Statistics.getCounter("[Solver] Calling solver 3 Prolog") == 1);
		
//		assertTrue(Statistics.getCounter("[Solver] Level 1 peeking") == 2);
//		assertTrue(Statistics.getCounter("[Solver] Level 1 pushing") == 1);
//		
//		assertTrue(Statistics.getCounter("[Solver] Level 2 peeking") == 5);
//		assertTrue(Statistics.getCounter("[Solver] Level 2 pushing") == 2);
//		assertTrue(Statistics.getCounter("[Solver] Level 2 popping") == 1);	
//		
//		assertTrue(Statistics.getCounter("[Solver] Level 3 peeking") == 11);
//		assertTrue(Statistics.getCounter("[Solver] Level 3 pushing") == 4);
//		assertTrue(Statistics.getCounter("[Solver] Level 3 popping") == 3);
	}
	
	/**
	 * Same as before but better ordering:
	 * Testing ICs last eliminates inconsistent resolvers early.
	 */
	public void testWithMultiStageBacktrackingBetterOrdering() {
		/**
		 * Setup Modules
		 */
		ConfigurationManager cM = new ConfigurationManager();		
		cM.add("Solver");
		cM.set("Solver", "class", "StackSolver");
		cM.set("Solver", "solvers", "Domain,Cost,Prolog,IC");
		cM.set("Solver", "keepStatistics", "true");
		cM.add("Cost");
		cM.set("Cost", "class", "CostSolver");
		cM.add("Domain");
		cM.set("Domain", "class", "DomainSolver");
		cM.add("Prolog");
		cM.set("Prolog", "class", "PrologSolver");
		cM.add("IC");
		cM.set("IC", "class", "InteractionConstraintSolver");
		cM.set("IC", "consistencyChecker", "Domain");

		SolverStack solver = new SolverStack("Solver", cM);
		
		/**
		 * Setup Core
		 */
		Core testCore = new Core();
		testCore.setContext( new ConstraintDatabase() );
		testCore.setTypeManager(new TypeManager());
		
		testCore.getTypeManager().addSimpleEnumType("t", "a,b,c,d,e,f");
		testCore.getTypeManager().addSimpleEnumType("boolean", "true,false");
		testCore.getTypeManager().attachTypes(Term.parse("(p t)"), Term.createConstant("boolean"));
		
		InteractionConstraint ic = new InteractionConstraint(Term.parse("(ic ?K ?X)"));
		ic.getCondition().add(new Statement("(?K (p ?X) true)"));
		
		ConstraintDatabase r1 = new ConstraintDatabase();
		r1.add(new Cost(Term.createComplex("add", Term.createConstant("c"), Term.createInteger(10))));
		ConstraintDatabase r2 = new ConstraintDatabase();
		r2.add(new Cost(Term.createComplex("add", Term.createConstant("d"), Term.createInteger(3))));
		ic.addResolver(r1);
		ic.addResolver(r2);
		testCore.getContext().add(ic);
				
		testCore.getContext().add(new Statement("(i1 (p a) true)"));
		testCore.getContext().add(new Statement("(i2 (p b) true)"));
		testCore.getContext().add(new Statement("(i3 (p c) true)"));
		testCore.getContext().add(new Cost(Term.createComplex("less-than",  Term.createConstant("c"), Term.createInteger(15))));
		testCore.getContext().add(new Cost(Term.createComplex("less-than",  Term.createConstant("d"), Term.createInteger(10))));
		testCore.getContext().add(new Cost(Term.createComplex("add", Term.createConstant("c"), Term.createInteger(10))));
		
		testCore = solver.run(testCore);
		assertTrue(testCore.getResultingState("Solver").equals(State.Consistent));

//		assertTrue(Statistics.getCounter("[Solver] Applied resolvers") == 6);
		
		assertTrue(Statistics.getCounter("[Solver] Calling solver 0 Domain") == 7);
		assertTrue(Statistics.getCounter("[Solver] Calling solver 1 Cost") == 7);
		assertTrue(Statistics.getCounter("[Solver] Calling solver 2 Prolog") == 4);
		assertTrue(Statistics.getCounter("[Solver] Calling solver 3 IC") == 4);
		
//		assertTrue(Statistics.getCounter("[Solver] Level 1 peeking") == 2);
//		assertTrue(Statistics.getCounter("[Solver] Level 1 pushing") == 1);
//		assertTrue(Statistics.getCounter("[Solver] Level 2 peeking") == 2);
//		assertTrue(Statistics.getCounter("[Solver] Level 2 pushing") == 1);
//		assertTrue(Statistics.getCounter("[Solver] Level 3 peeking") == 2);
//		assertTrue(Statistics.getCounter("[Solver] Level 3 pushing") == 1);
	}
	
	/**
	 * Same as before but contains a {@link Finally} constraint
	 * and uses {@link FinallySolver}.
	 */
	public void testWithFinallySimple() {
		/**
		 * Setup Modules
		 */
		ConfigurationManager cM = new ConfigurationManager();		
		cM.add("Solver");
		cM.set("Solver", "class", "StackSolver");
		cM.set("Solver", "solvers", "Cost,Finally");
		cM.set("Solver", "keepStatistics", "true");
		cM.add("Cost");
		cM.set("Cost", "class", "CostSolver");
		cM.add("Finally");
		cM.set("Finally", "class", "FinallySolver");
		
		SolverStack solver = new SolverStack("Solver", cM);
		
		/**
		 * Setup Core
		 */
		Core testCore = new Core();
		testCore.setContext( new ConstraintDatabase() );
		testCore.setTypeManager(new TypeManager());
		
		testCore.getTypeManager().addSimpleEnumType("t", "a,b,c,d,e,f");
		testCore.getTypeManager().addSimpleEnumType("boolean", "true,false");
		testCore.getTypeManager().attachTypes(Term.parse("(p t)"), Term.createConstant("boolean"));
		
					
		testCore.getContext().add(new Cost(Term.createComplex("less-than",  Term.createConstant("c"), Term.createInteger(15))));
		testCore.getContext().add(new Cost(Term.createComplex("add", Term.createConstant("c"), Term.createInteger(10))));
		
		ConstraintDatabase finallyDB = new ConstraintDatabase();
		finallyDB.add(new Cost(Term.createComplex("add", Term.createConstant("c"), Term.createInteger(1))));
		testCore.getContext().add(new Finally(Term.createConstant("finally-1"), finallyDB));
		
		
		testCore = solver.run(testCore);
			
		assertTrue(testCore.getResultingState("Solver").equals(State.Consistent));
	}
	
	/**
	 * Same as before but contains a {@link Finally} constraint
	 * and uses {@link FinallySolver}.
	 */
	public void testWithFinally() {
		/**
		 * Setup Core
		 */
		Core testCore = new Core();
		testCore.setContext( new ConstraintDatabase() );
		testCore.setTypeManager(new TypeManager());
		
		testCore.getTypeManager().addSimpleEnumType("t", "a,b,c,d,e,f");
		testCore.getTypeManager().addSimpleEnumType("boolean", "true,false");
		testCore.getTypeManager().attachTypes(Term.parse("(p t)"), Term.createConstant("boolean"));
		
		InteractionConstraint ic = new InteractionConstraint(Term.parse("(ic ?K ?X)"));
		ic.getCondition().add(new Statement("(?K (p ?X) true)"));
		
		ConstraintDatabase r1 = new ConstraintDatabase();
		r1.add(new Cost(Term.createComplex("add", Term.createConstant("c"), Term.createInteger(10))));
		ConstraintDatabase r2 = new ConstraintDatabase();
		r2.add(new Cost(Term.createComplex("add", Term.createConstant("d"), Term.createInteger(3))));
		ic.addResolver(r1);
		ic.addResolver(r2);
		testCore.getContext().add(ic);
				
		testCore.getContext().add(new Statement("(i1 (p a) true)"));
		testCore.getContext().add(new Statement("(i2 (p b) true)"));
		testCore.getContext().add(new Statement("(i3 (p c) true)"));
		testCore.getContext().add(new Cost(Term.createComplex("less-than",  Term.createConstant("c"), Term.createInteger(15))));
		testCore.getContext().add(new Cost(Term.createComplex("less-than-or-equals", Term.createConstant("d"), Term.createInteger(10))));
		testCore.getContext().add(new Cost(Term.createComplex("add", Term.createConstant("c"), Term.createInteger(10))));
		
		ConstraintDatabase finallyDB = new ConstraintDatabase();
		finallyDB.add(new Cost(Term.createComplex("add", Term.createConstant("d"), Term.createInteger(1))));
		testCore.getContext().add(new Finally(Term.createConstant("finally-1"), finallyDB));
		
//		System.out.println(testCore.getContext());
		
		/**
		 * Setup Modules
		 */
		ConfigurationManager cM = new ConfigurationManager();		
		cM.add("Solver");
		cM.set("Solver", "class", "SolverStack");
		cM.set("Solver", "solvers", "Domain,Cost,Prolog,IC,Finally");
		cM.set("Solver", "keepStatistics", "true");
		cM.add("Cost");
		cM.set("Cost", "class", "CostSolver");
		cM.add("Domain");
		cM.set("Domain", "class", "DomainSolver");
		cM.add("Prolog");
		cM.set("Prolog", "class", "PrologSolver");
		cM.add("Finally");
		cM.set("Finally", "class", "FinallySolver");
		cM.add("IC");
		cM.set("IC", "class", "InteractionConstraintSolver");
		cM.set("IC", "consistencyChecker", "Domain");
		
//		cM.set("Solver", "verbose", "true");
//		cM.set("Solver", "verbosity", "5");
//		cM.set("IC", "verbose", "true");
//		cM.set("IC", "verbosity", "5");
//		cM.set("Cost", "verbose", "true");
//		cM.set("Cost", "verbosity", "5");
//		Logger.addPrintStream("Solver", System.out);
//		Logger.addPrintStream("Cost", System.out);
//		Logger.addPrintStream("IC", System.out);

		Module solver = ModuleFactory.initModule("Solver", cM);
						
		testCore = solver.run(testCore);
						
		assertTrue(testCore.getResultingState("Solver").equals(State.Consistent));	
//		assertTrue(Statistics.getCounter("[Solver] Applied resolvers") == 7);
		
//		System.out.println(Statistics.getCounter("[Solver] Calling solver 0 Domain"));
		
		assertTrue(Statistics.getCounter("[Solver] Calling solver 0 Domain") == 8);
		assertTrue(Statistics.getCounter("[Solver] Calling solver 1 Cost") == 8);
		assertTrue(Statistics.getCounter("[Solver] Calling solver 2 Prolog") == 5);
		assertTrue(Statistics.getCounter("[Solver] Calling solver 3 IC") == 5);
		
//		assertTrue(Statistics.getCounter("[Solver] Level 1 peeking") == 2);
//		assertTrue(Statistics.getCounter("[Solver] Level 1 pushing") == 1);
//		assertTrue(Statistics.getCounter("[Solver] Level 2 peeking") == 2);
//		assertTrue(Statistics.getCounter("[Solver] Level 2 pushing") == 1);
//		assertTrue(Statistics.getCounter("[Solver] Level 3 peeking") == 2);
//		assertTrue(Statistics.getCounter("[Solver] Level 3 pushing") == 1);
	}
	
	public void testOptimize() {
		/**
		 * Setup Modules
		 */
		ConfigurationManager cM = new ConfigurationManager();		
		cM.add("Solver");
		cM.set("Solver", "class", "SolverStack");
		cM.set("Solver", "solvers", "Cost,ICSolver");
		cM.set("Solver", "optimize", "true");
		cM.add("Cost");
		cM.set("Cost", "class", "CostSolver");
		cM.add("ICSolver");
		cM.set("ICSolver", "class", "InteractionConstraintSolver");
		cM.set("ICSolver", "consistencyChecker", "Cost");
		
//		cM.add("ICConChecker");
//		cM.set("ICConChecker", "class", "SolverStack");
//		cM.set("ICConChecker", "consistencyChecker", "ICConChecker");
		
//		cM.set("Solver", "verbose", "true");
//		cM.set("Solver", "verbosity", "5");
//		cM.set("ICSolver", "verbose", "true");
//		cM.set("ICSolver", "verbosity", "5");
//		cM.set("Cost", "verbose", "true");
//		cM.set("Cost", "verbosity", "5");
//		Logger.addPrintStream("Solver", System.out);
//		Logger.addPrintStream("Cost", System.out);
//		Logger.addPrintStream("IC", System.out);

		SolverStack solver = new SolverStack("Solver", cM);

		InteractionConstraint ic = new InteractionConstraint(Term.parse("(simple-ic)"));
		ConstraintDatabase r1 = new ConstraintDatabase();
		r1.add(new Cost(Term.parse("(add x 10)")));
		ic.addResolver(r1);
		
		ConstraintDatabase r2 = new ConstraintDatabase();
		r2.add(new Cost(Term.parse("(add x 8)")));
		ic.addResolver(r2);
		
		ConstraintDatabase r3 = new ConstraintDatabase();
		r3.add(new Cost(Term.parse("(add x 6)")));
		ic.addResolver(r3);
		
		/**
		 * Setup Core
		 */
		ConstraintDatabase init = new ConstraintDatabase();
		init.add(new OptimizationTarget(Term.parse("(min x)")));
		init.add(ic);
		Core testCore = new Core();
		testCore.setContext( init );
		testCore.setTypeManager(new TypeManager());
				
		testCore = solver.run(testCore);
		
		
//		assertTrue(testCore.getResultingState("Solver").equals(State.Consistent));
		
//		System.out.println(testCore.getContext());
		
		
	}
}


