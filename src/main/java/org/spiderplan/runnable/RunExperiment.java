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
package org.spiderplan.runnable;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.spiderplan.modules.configuration.ConfigurationManager;
import org.spiderplan.modules.solvers.Core;
import org.spiderplan.modules.solvers.Module;
import org.spiderplan.modules.tools.ModuleFactory;
import org.spiderplan.representation.ConstraintDatabase;
import org.spiderplan.representation.parser.Compile;
import org.spiderplan.representation.parser.ConsistencyChecker;
import org.spiderplan.representation.parser.experiment.ExperimentParser;
import org.spiderplan.tools.Global;
import org.spiderplan.tools.statistics.Statistics;
import org.spiderplan.tools.stopWatch.StopWatch;

/**
 * Run an experiment by providing a file defining it.
 * 
 * @author Uwe Köckemann
 *
 */
public class RunExperiment {
	
////		domain = 		"./data/domains/benchmark/social-acceptablility/domain-2vars-02res.uddl";
////		problemDirName = "./data/domains/benchmark/social-acceptablility/SA-FF-2INT";
////		run("SA-FF-2INT-02RES", domain, problemDirName, planner);
////		Statistics.reset();
////		
////		domain = 		"./data/domains/benchmark/social-acceptablility/domain-2vars-03res.uddl";
////		problemDirName = "./data/domains/benchmark/social-acceptablility/SA-FF-2INT";
////		run("SA-FF-2INT-03RES", domain, problemDirName, planner);
////		Statistics.reset();
////		
////		domain = 		"./data/domains/benchmark/social-acceptablility/domain-2vars-04res.uddl";
////		problemDirName = "./data/domains/benchmark/social-acceptablility/SA-FF-2INT";
////		run("SA-FF-2INT-04RES", domain, problemDirName, planner);
////		Statistics.reset();
////		
////		domain = 		"./data/domains/benchmark/social-acceptablility/domain-2vars-05res.uddl";
////		problemDirName = "./data/domains/benchmark/social-acceptablility/SA-FF-2INT";
////		run("SA-FF-2INT-05RES", domain, problemDirName, planner);
////		Statistics.reset();
////		
////		domain = 		"./data/domains/benchmark/social-acceptablility/domain-2vars-06res.uddl";
////		problemDirName = "./data/domains/benchmark/social-acceptablility/SA-FF-2INT";
////		run("SA-FF-2INT-06RES", domain, problemDirName, planner);
////		Statistics.reset();
////		
////		domain = 		"./data/domains/benchmark/social-acceptablility/domain-2vars-07res.uddl";
////		problemDirName = "./data/domains/benchmark/social-acceptablility/SA-FF-2INT";
////		run("SA-FF-2INT-07RES", domain, problemDirName, planner);
////		Statistics.reset();
////		
////		domain = 		"./data/domains/benchmark/social-acceptablility/domain-2vars-08res.uddl";
////		problemDirName = "./data/domains/benchmark/social-acceptablility/SA-FF-2INT";
////		run("SA-FF-2INT-08RES", domain, problemDirName, planner);
////		Statistics.reset();
////		
////		domain = 		"./data/domains/benchmark/social-acceptablility/domain-2vars-09res.uddl";
////		problemDirName = "./data/domains/benchmark/social-acceptablility/SA-FF-2INT";
////		run("SA-FF-2INT-09RES", domain, problemDirName, planner);
////		Statistics.reset();
////		
////		domain = 		"./data/domains/benchmark/social-acceptablility/domain-2vars-10res.uddl";
////		problemDirName = "./data/domains/benchmark/social-acceptablility/SA-FF-2INT";
////		run("SA-FF-2INT-10RES", domain, problemDirName, planner);
////		Statistics.reset();
//				
//		domain = 		"./data/domains/benchmark/social-acceptablility/domain-3vars-02res.uddl";
//		problemDirName = "./data/domains/benchmark/social-acceptablility/SA-FF-3INT";
//		run("SA-FF-3INT-02RES", domain, problemDirName, planner);
//		Statistics.reset();
//		
//		domain = 		"./data/domains/benchmark/social-acceptablility/domain-3vars-03res.uddl";
//		problemDirName = "./data/domains/benchmark/social-acceptablility/SA-FF-3INT";
//		run("SA-FF-3INT-03RES", domain, problemDirName, planner);
//		Statistics.reset();
//		
//		domain = 		"./data/domains/benchmark/social-acceptablility/domain-3vars-04res.uddl";
//		problemDirName = "./data/domains/benchmark/social-acceptablility/SA-FF-3INT";
//		run("SA-FF-3INT-04RES", domain, problemDirName, planner);
//		Statistics.reset();
//		
//		domain = 		"./data/domains/benchmark/social-acceptablility/domain-3vars-05res.uddl";
//		problemDirName = "./data/domains/benchmark/social-acceptablility/SA-FF-3INT";
//		run("SA-FF-3INT-05RES", domain, problemDirName, planner);
//		Statistics.reset();
//		
//		domain = 		"./data/domains/benchmark/social-acceptablility/domain-3vars-06res.uddl";
//		problemDirName = "./data/domains/benchmark/social-acceptablility/SA-FF-3INT";
//		run("SA-FF-3INT-06RES", domain, problemDirName, planner);
//		Statistics.reset();
//		
//		domain = 		"./data/domains/benchmark/social-acceptablility/domain-3vars-07res.uddl";
//		problemDirName = "./data/domains/benchmark/social-acceptablility/SA-FF-3INT";
//		run("SA-FF-3INT-07RES", domain, problemDirName, planner);
//		Statistics.reset();
//		
//		domain = 		"./data/domains/benchmark/social-acceptablility/domain-3vars-08res.uddl";
//		problemDirName = "./data/domains/benchmark/social-acceptablility/SA-FF-3INT";
//		run("SA-FF-3INT-08RES", domain, problemDirName, planner);
//		Statistics.reset();
//		
//		domain = 		"./data/domains/benchmark/social-acceptablility/domain-3vars-09res.uddl";
//		problemDirName = "./data/domains/benchmark/social-acceptablility/SA-FF-3INT";
//		run("SA-FF-3INT-09RES", domain, problemDirName, planner);
//		Statistics.reset();
//		
//		domain = 		"./data/domains/benchmark/social-acceptablility/domain-3vars-10res.uddl";
//		problemDirName = "./data/domains/benchmark/social-acceptablility/SA-FF-3INT";
//		run("SA-FF-3INT-10RES", domain, problemDirName, planner);
//		Statistics.reset();
//		
//		
//		
//		
////		
////		domain = 		"./data/domains/benchmark/context-awareness/domain-2-ints.uddl";
////		problemDirName = "./data/domains/benchmark/context-awareness/2-ints";
////		run("CA-2INT", domain, problemDirName, planner);
////		Statistics.reset();
////		
////		domain = 		"./data/domains/benchmark/context-awareness/domain-3-ints.uddl";
////		problemDirName = "./data/domains/benchmark/context-awareness/3-ints";
////		run("CA-3INT", domain, problemDirName, planner);	
////		Statistics.reset();
////				
////		domain = 		"./data/domains/benchmark/context-awareness/domain-4-ints.uddl";
////		problemDirName = "./data/domains/benchmark/context-awareness/4-ints";
////		run("CA-4INT", domain, problemDirName, planner);	
////		Statistics.reset();
////		
//		
////		domain = 		"./data/domains/benchmark/social-acceptablility/domain-vars-scale.uddl";
////		problemDirName = "./data/domains/benchmark/social-acceptablility/var-scale-test";
////		run("SA-VS", domain, problemDirName, planner);
////		Statistics.reset();	
//		
//		
////		/**
////		 * Change planner and run again!
////		 */
////		planner = 		"./data/domains/benchmark/planner-experiment-bruteforce.spider";
//////		
////		domain = 		"./data/domains/benchmark/social-acceptablility/domain-2vars.uddl";
////		problemDirName = "./data/domains/benchmark/social-acceptablility/SA-NC-2INT";		
////		run("SA-NC-2INT-BF", domain, problemDirName, planner);
////		Statistics.reset();
////		
////		domain = 		"./data/domains/benchmark/social-acceptablility/domain-3vars.uddl";
////		problemDirName = "./data/domains/benchmark/social-acceptablility/SA-NC-3INT";
////		run("SA-NC-3INT-BF", domain, problemDirName, planner);
////		Statistics.reset();
//		
////		domain = 		"./data/domains/benchmark/social-acceptablility/domain-2vars.uddl";
////		problemDirName = "./data/domains/benchmark/social-acceptablility/SA-FF-2INT";
////		run("SA-FF-2INT-BF", domain, problemDirName, planner);
////		Statistics.reset();
////		
////		domain = 		"./data/domains/benchmark/social-acceptablility/domain-3vars.uddl";
////		problemDirName = "./data/domains/benchmark/social-acceptablility/SA-FF-3INT";
////		run("SA-FF-3INT-BF", domain, problemDirName, planner);
////		Statistics.reset();	
////		
////		domain = 		"./data/domains/benchmark/social-acceptablility/domain-2vars-02res.uddl";
////		problemDirName = "./data/domains/benchmark/social-acceptablility/SA-FF-2INT";
////		run("SA-FF-2INT-02RES-BF", domain, problemDirName, planner);
////		Statistics.reset();
////		
////		domain = 		"./data/domains/benchmark/social-acceptablility/domain-2vars-03res.uddl";
////		problemDirName = "./data/domains/benchmark/social-acceptablility/SA-FF-2INT";
////		run("SA-FF-2INT-03RES-BF", domain, problemDirName, planner);
////		Statistics.reset();
////		
////		domain = 		"./data/domains/benchmark/social-acceptablility/domain-2vars-04res.uddl";
////		problemDirName = "./data/domains/benchmark/social-acceptablility/SA-FF-2INT";
////		run("SA-FF-2INT-04RES-BF", domain, problemDirName, planner);
////		Statistics.reset();
////		
////		domain = 		"./data/domains/benchmark/social-acceptablility/domain-2vars-05res.uddl";
////		problemDirName = "./data/domains/benchmark/social-acceptablility/SA-FF-2INT";
////		run("SA-FF-2INT-05RES-BF", domain, problemDirName, planner);
////		Statistics.reset();
////		
////		domain = 		"./data/domains/benchmark/social-acceptablility/domain-2vars-06res.uddl";
////		problemDirName = "./data/domains/benchmark/social-acceptablility/SA-FF-2INT";
////		run("SA-FF-2INT-06RES-BF", domain, problemDirName, planner);
////		Statistics.reset();
////		
////		domain = 		"./data/domains/benchmark/social-acceptablility/domain-2vars-07res.uddl";
////		problemDirName = "./data/domains/benchmark/social-acceptablility/SA-FF-2INT";
////		run("SA-FF-2INT-07RES-BF", domain, problemDirName, planner);
////		Statistics.reset();
////		
////		domain = 		"./data/domains/benchmark/social-acceptablility/domain-2vars-08res.uddl";
////		problemDirName = "./data/domains/benchmark/social-acceptablility/SA-FF-2INT";
////		run("SA-FF-2INT-08RES-BF", domain, problemDirName, planner);
////		Statistics.reset();
////		
////		domain = 		"./data/domains/benchmark/social-acceptablility/domain-2vars-09res.uddl";
////		problemDirName = "./data/domains/benchmark/social-acceptablility/SA-FF-2INT";
////		run("SA-FF-2INT-09RES-BF", domain, problemDirName, planner);
////		Statistics.reset();
////		
////		domain = 		"./data/domains/benchmark/social-acceptablility/domain-2vars-10res.uddl";
////		problemDirName = "./data/domains/benchmark/social-acceptablility/SA-FF-2INT";
////		run("SA-FF-2INT-10RES-BF", domain, problemDirName, planner);
////		Statistics.reset();
////		
////		domain = 		"./data/domains/benchmark/context-awareness/domain-2-ints.uddl";
////		problemDirName = "./data/domains/benchmark/context-awareness/2-ints";
////		run("CA-2INT-BF", domain, problemDirName, planner);
////		Statistics.reset();
////		
////		domain = 		"./data/domains/benchmark/context-awareness/domain-3-ints.uddl";
////		problemDirName = "./data/domains/benchmark/context-awareness/3-ints";
////		run("CA-3INT-BF", domain, problemDirName, planner);	
////		Statistics.reset();
//		
//		
//		//Skipping for now
//		
////		domain = 		"./data/domains/benchmark/social-acceptablility/domain-4vars.uddl";
////		problemDirName = "./data/domains/benchmark/social-acceptablility/SA-NC-4INT";
////		run("SA-NC-4INT-BF", domain, problemDirName, planner);
////		Statistics.reset();
////		domain = 		"./data/domains/benchmark/context-awareness/domain-4-ints.uddl";
////		problemDirName = "./data/domains/benchmark/context-awareness/4-ints";
////		run("CA-4INT-BF", domain, problemDirName, planner);	
////		Statistics.reset();		
//		
//		//Continue here
//
//		
////		domain = 		"./data/domains/benchmark/social-acceptablility/domain-vars-scale.uddl";
////		problemDirName = "./data/domains/benchmark/social-acceptablility/var-scale-test";
////		run("SA-VS-BF", domain, problemDirName, planner);
////		Statistics.reset();	
////
////		
////		domain = 		 "./data/domains/benchmark/social-acceptablility/domain-2vars.uddl";
////		problemDirName = "./data/domains/benchmark/social-acceptablility/SA-BC-2INT";
////		run("SA-BC-2INT", domain, problemDirName, planner);
////		Statistics.reset();
//
//		
//		
//		/**
//		 * Offline proactivity tests
//		 */
////		planner = "./data/domains/human-aware planning/planner-ic-first-experiment.spider";
////		domain = "./data/domains/human-aware planning/domain-v5.uddl";
////		problemDirName = "/home/uwe/icaps-2015-exp/";
////		run("proactivity", domain, problemDirName, planner);
////		Statistics.reset();
//		
//		
//	}
	
	/**
	 * Run experiment by providing filename of an experiment description.
	 * 
	 * @param expFilename filename of experiment definition
	 */
	public static void run( String expFilename ) {
		ConsistencyChecker.ignoreWarnings = true;
		int expCount = 0;
		
		Map<String,String> optionsMap = new HashMap<String,String>();
		
		InputStream inStream;
		try {
			inStream = new java.io.FileInputStream(expFilename);
			ExperimentParser expParser = new ExperimentParser(inStream);
			expParser.CompilationUnit(optionsMap);
			inStream.close();
		} catch ( FileNotFoundException e ) {
			e.printStackTrace();
			System.exit(1);
		} catch ( IOException e ) {
			e.printStackTrace();
			System.exit(1);
		} catch ( org.spiderplan.representation.parser.experiment.ParseException e ) {
			e.printStackTrace();
			System.exit(1);
		}
		
		/**
		 * TODO: 
		 * 	- Add standard attributes (problem, domain, result, time)
		 * 	- Try to fetch all non-standard attributes from Profiler, StopWatch and Statistics
		 * 	- Exception and help in case of unknown attributes (list all available and explain how to create new ones)
		 * 	- Warning if verbose true on any Module
		 *  - Create experiment files for all existing experiments
		 *  - Use shell scripts to run multiple experiments
		 *  - Experiments with specific combinations of planner domain and problems (rather than fixed domain and planner files with problem directory)
		 */
		
				
		StopWatch.keepAllTimes = false;
		
		
		long maxTimeMillis = -1;
		
		String expName = null;
		String domainFilename = null;
		String problemsDirectoryName = null;
		String plannerFilename = null;
		String attributesString = null;
		List<String> attributeNames = new ArrayList<String>();
		Map<String,String> attributeAliases = new HashMap<String, String>();
		
		System.out.println("Experiment:");
				
		/**
		 * Required information
		 */		
		if ( optionsMap.containsKey("name") ) {
			System.out.println("Name: " + optionsMap.get("name"));
			expName = optionsMap.get("name");
			Global.UniqueFilenamePart = expName;			
		} else {
			throw new IllegalArgumentException("Missing information in file " + expFilename + ": name");
		}
		if ( optionsMap.containsKey("problems") ) {
			System.out.println("Problem Directory: " + optionsMap.get("problems"));
			problemsDirectoryName = optionsMap.get("problems");
		} else {
			throw new IllegalArgumentException("Missing information in file " + expFilename + ": problems");
		}
		if ( optionsMap.containsKey("planner") ) {
			System.out.println("Planner: " + optionsMap.get("planner"));
			plannerFilename = optionsMap.get("planner");
		} else {
			throw new IllegalArgumentException("Missing information in file " + expFilename + ": planner");
		}
		if ( optionsMap.containsKey("attributes") ) {
			System.out.println("Attributes:");
			attributesString = optionsMap.get("attributes");
			
			String[] atts = attributesString.split(",");
			
			for ( String attDesc : atts ) {
				if ( !attDesc.contains("/") ) {
					System.out.println("\t" + attDesc.trim());
					attributeNames.add(attDesc.trim());
				} else {
					String[] attAndAlias = attDesc.split("/");
					attributeNames.add(attAndAlias[0].trim());
					attributeAliases.put(attAndAlias[0].trim(), attAndAlias[1].trim());
					System.out.println("\t" + attAndAlias[0].trim() + " as " + attAndAlias[1].trim());
				}
			}
		} else {
			throw new IllegalArgumentException("Missing information in file " + expFilename + ": attributes");
		}
		
		/**
		 * Optional information
		 */		
		if ( optionsMap.containsKey("domain") ) {
			System.out.println("Domain: " + optionsMap.get("domain"));
			domainFilename = optionsMap.get("domain");
		} 
		if ( optionsMap.containsKey("timeout") ) {
			System.out.println("Timeout: " + optionsMap.get("timeout"));
			long timeout = Long.valueOf(optionsMap.get("timeout"));
			maxTimeMillis = 1000*60*timeout;
		}
		
		class TimeOutThread extends Thread {
			
			long startTimeMillis;
			long maxTimeMillis;
			boolean stopped = false;
			
			public TimeOutThread( long maxTimeMillis ) {
				this.maxTimeMillis = maxTimeMillis;
			}
			
			public void stopThread() {
				this.stopped = true;
			}
			
			@Override
			public void run() {
				startTimeMillis = System.currentTimeMillis();
				
				while ( !stopped && (System.currentTimeMillis()-startTimeMillis) < maxTimeMillis ) {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				if ( !stopped ) {
					Module.setKillFlag(true);
				}
			}
		};
						
		File problemDir = new File(problemsDirectoryName);

		System.out.println("..." + problemDir.getAbsolutePath());
		
		ArrayList<String> problemList = new ArrayList<String>();
	
		for ( File f : problemDir.listFiles() ) {
			if ( f.isFile() && f.getName().contains(".uddl") && !f.getName().contains("~")) {
				problemList.add(f.getAbsolutePath());
			}
		}
		
		Collections.sort(problemList);
		
		ArrayList<Long> times = new ArrayList<Long>();
				
		System.out.println("Running:");
		System.out.println("\t" + problemsDirectoryName);
				
		for ( String problem : problemList ) {
			int lastIndex = problem.split("/").length-1;
			String pName = problem.split("/")[lastIndex].split("\\.")[0];
			int lastIndexDomain = domainFilename.split("/").length-1;
			String dName = domainFilename.split("/")[lastIndexDomain].split("\\.")[0];
			expCount++;
							
			Compile.verbose = false;
			
			ModuleFactory.forgetStaticModules();
			
			ArrayList<String> domainFilenames = new ArrayList<>();
			domainFilenames.add(domainFilename);
			domainFilenames.add(problem);
			
			Compile.compile( domainFilenames, plannerFilename);
				
			ConfigurationManager oM = Compile.getPlannerConfig();
			
			Module main = ModuleFactory.initModule("main", oM);
					
			Core initCore = Compile.getCore();
			
	        Calendar cal = Calendar.getInstance();
	        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
					
			System.out.print("Starting " +pName+ " at " + sdf.format(cal.getTime()) + "... ");

			StopWatch.start("[main] Running... " + pName );
			
			TimeOutThread tOutThread = null;
			if ( maxTimeMillis != -1 ) {
				tOutThread = new TimeOutThread(maxTimeMillis);
				tOutThread.start();
			}
			
			ConstraintDatabase initDB = initCore.getContext().copy();
			
			Core r = main.run(initCore);
			
			System.out.println(r.getRootCore().getContext().equals(initDB));
			
			if ( maxTimeMillis != -1 ) {
				tOutThread.stopThread();
			}
						

			StopWatch.stop("[main] Running... " + pName );
			
			if ( Module.getKillFlag() ) {
				System.out.println("Time for "+pName+" ("+expCount+"): TIMEOUT");
				Statistics.setString("Time", "TIMEOUT");
			} else {			
				System.out.println("Time for "+pName+" ("+expCount+"): " +  String.format("%.3f", StopWatch.getAvg("[main] Running... " + pName)/1000.0) + " (" + r.getResultingState("main") + ")");
				Statistics.setDouble("Time", StopWatch.getAvg("[main] Running... " + pName)/1000.0);
			}
			Module.setKillFlag(false);
						
			times.add( StopWatch.getLast("[main] Running... " + pName ));
			
			for ( String attName : attributeAliases.keySet() ) {
				Statistics.renameKey(attName, attributeAliases.get(attName));
			}
						
			Statistics.setString("result", r.getResultingState("main").toString());
			Statistics.setString("problem", pName+".uddl");
			Statistics.setString("domain", dName);
			Statistics.setLong("time", StopWatch.getLast("[main] Running... " + pName ));
			
			Statistics.store();
			Statistics.dumpCSV("/home/uwe/results/" + expName + ".csv", attributeNames);
			
			StopWatch.reset();			
			Global.resetStatics();
			
			System.gc();
			
			try {
				Thread.sleep(2000);
			} catch ( Exception e) {
				
			}
		}
	}
}
