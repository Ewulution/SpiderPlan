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
package org.spiderplan.representation.types;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.spiderplan.representation.ConstraintDatabase;
import org.spiderplan.representation.expressions.Statement;
import org.spiderplan.representation.expressions.domain.Substitution;
import org.spiderplan.representation.expressions.domain.TypeDomainConstraint;
import org.spiderplan.representation.expressions.domain.TypeSignatureConstraint;
import org.spiderplan.representation.expressions.resources.ReusableResourceCapacity;
import org.spiderplan.representation.logic.Term;
import org.spiderplan.tools.GenericComboBuilder;
import org.spiderplan.tools.UniqueID;

/**
 * Used to register types and their domains and to store the {@link Type}s of arguments of 
 * {@link Term}s, etc. 
 * @author Uwe Köckemann
 */
public class TypeManager {
	
	private ArrayList<Term> typeNames = new ArrayList<Term>();
	private Map<Term,Type> types = new HashMap<Term,Type>();	
	private Map<String,ArrayList<Type>> typeLookUp = new HashMap<String, ArrayList<Type>>();
	private Map<String,String> typeSignatures = new HashMap<String, String>();
	private ArrayList<String> varNames = new ArrayList<String>();
	
	private final static Term BooleanTerm = Term.createConstant("boolean");
	
	/**
	 * Create new type manager.
	 */
	public TypeManager() { 
		this.addSimpleEnumType("boolean", "true,false");
		this.addNewType(new IntervalType());
	}
	
	/**
	 * Collects and adds all type information from a {@link ConstraintDatabase} and 
	 * adds it to this {@link TypeManager}.
	 * @param cDB A {@link ConstraintDatabase}.
	 */
	public void collectTypeInformation( ConstraintDatabase cDB ) {
		for ( TypeDomainConstraint dC : cDB.get(TypeDomainConstraint.class) ) {
			Term r = dC.getRelation(); 
			try {
				if ( r.getName().equals("enum") ) {
					Term typeName = r.getArg(0);
					EnumType t;
					
					if ( this.typeNames.contains(typeName) ) {
						Type tKnown = types.get(typeName);
						if ( tKnown instanceof EnumType ) {
							t = (EnumType)tKnown;
						} else {
							throw new IllegalStateException("Name " + typeName + " from " + dC + " also used for " + tKnown.getClass().getSimpleName());
						}
					} else {
						t = new EnumType();
						t.setName(typeName);
					}
					
					if ( r.getNumArgs() > 1 ) {
						Term memberList = r.getArg(1);
						if ( !memberList.getName().equals("list") ) {
							throw new IllegalArgumentException(dC + " has invalid form. Use (enum type-name { a b c }) or (enum type-name (list a b c)).");
						}
						for ( int i = 0 ; i < memberList.getNumArgs() ; i++ ) {
							if ( !t.D.contains(memberList.getArg(i))) { 
								t.D.add( memberList.getArg(i) );
							}
						}
					}
					
					this.addNewType(t);
					this.attachTypes("(" + t.getName() + " " + t.getName() + ")");
				} else if ( r.getName().equals("int") ) {
					Term typeName = r.getArg(0);
					IntegerType t;
					
					if ( this.typeNames.contains(typeName) ) {
						Type tKnown = types.get(typeName);
						if ( tKnown instanceof IntegerType ) {
							t = (IntegerType)tKnown;
						} else {
							throw new IllegalStateException("Name " + typeName + " from " + dC + " also used for " + tKnown.getClass().getSimpleName());
						}
					} else {
						t = new IntegerType();
						t.setName(typeName);

					}

					if ( r.getNumArgs() > 1 ) {
						Term interval = r.getArg(1);
						if ( !interval.getName().equals("interval") ) {
							throw new IllegalArgumentException(dC + " has invalid form. Use (int type-name [min max]) or (int type-name (interval min max)) where min and max are integers.");
						}
						t.min = Integer.valueOf(interval.getArg(0).toString());
						t.max = Integer.valueOf(interval.getArg(1).toString());
					}
					
					this.addNewType(t);
					this.attachTypes("(" + t.getName() + " " + t.getName() + ")");
				} else if ( r.getName().equals("float") ) {
					Term typeName = r.getArg(0);
					FloatType t;
					
					if ( this.typeNames.contains(typeName) ) {
						Type tKnown = types.get(typeName);
						if ( tKnown instanceof FloatType ) {
							t = (FloatType)tKnown;
						} else {
							throw new IllegalStateException("Name " + typeName + " from " + dC + " also used for " + tKnown.getClass().getSimpleName());
						}
					} else {
						t = new FloatType();
						t.setName(typeName);
					}

					if ( r.getNumArgs() > 1 ) {
						Term interval = r.getArg(1);
						if ( !interval.getName().equals("interval") ) {
							throw new IllegalArgumentException(dC + " has invalid form. Use (float type-name [min max]) or (float type-name (interval min max)) where min and max are float values.");
						}
						t.min = Double.valueOf(interval.getArg(0).toString());
						t.max = Double.valueOf(interval.getArg(1).toString());
					}
					
					this.addNewType(t);
					this.attachTypes("(" + t.getName() + " " + t.getName() + ")");
				} else if ( r.getName().equals("unrestricted") ) {
					Term typeName = r.getArg(0);
					UnrestrictedType t;
					
					if ( this.typeNames.contains(typeName) ) {
						Type tKnown = types.get(typeName);
						if ( tKnown instanceof UnrestrictedType ) {
							t = (UnrestrictedType)tKnown;
						} else {
							throw new IllegalStateException("Name " + typeName + " from " + dC + " also used for " + tKnown.getClass().getSimpleName());
						}
					} else {
						t = new UnrestrictedType();
						t.setName(typeName);
					}
					
					this.addNewType(t);
					this.attachTypes("(" + t.getName() + " " + t.getName() + ")");
				}
			} catch ( Exception e ) {
				e.printStackTrace();
				throw new IllegalArgumentException(dC + " has invalid form.");
			}
		}
		for ( TypeSignatureConstraint tsC : cDB.get(TypeSignatureConstraint.class) ) {
			this.attachTypes(tsC.getVariableSignature(), tsC.getValueTypeName());
		}
//		System.out.println("Updating type domains");
//		this.updateTypeDomains();
	}
	
	/**
	 * Adds a type with a domain of non-complex ground terms.
	 * @param name Name of the domain
	 * @param domainString Comma-separated list of elements of the domain (all constants)
	 */
	public void addSimpleEnumType( String name, String domainString ) {
		EnumType newType = new EnumType();
		newType.name = Term.createConstant(name);
		
		for ( String simpleTerm : domainString.split(",")) {
			newType.D.add(Term.parse(simpleTerm));
		}
		
		this.addNewType(newType);
	}
	
	/**
	 * Add a new integer type.
	 * @param name Name of the type.
	 * @param min Minimum value
	 * @param max Maximum value
	 */
	public void addIntegerType( String name, int min, int max ) {
		IntegerType newType = new IntegerType();
		newType.name = Term.createConstant(name);
		newType.min = min;
		newType.max = max;
		
		this.addNewType(newType);
	}
	
	/**
	 * Attaches type definitions to a predicate.
	 * Use form p(typeA,typeB) for relations and p(typeA,typeB) = typeC for state variables.
	 * In case of relations the type "boolean" is set as predicates return value.
	 * @param s String containing predicate and its types (see above example).
	 */
	public void attachTypes( String s ) {
		ArrayList<Type> tList = new ArrayList<Type>();
		ArrayList<Term> tNameList = new ArrayList<Term>();
		
		Term a;
		if ( s.contains("=") ) {
			a = Term.parse(s.split("=")[0]);
		} else {
			a = Term.parse(s);
		}
		
		for ( int i = 0 ; i < a.getNumArgs() ; i++ ) {
			tNameList.add(a.getArg(i));
		}
					
		if ( s.contains("=") ) {
			tNameList.add(Term.createConstant(s.split("=")[1]));
		} else {
			tNameList.add(Term.createConstant("boolean"));
		}
		
		for ( Term tName : tNameList ) {
			Type t = types.get(tName);
			if ( t == null ) {
				throw new IllegalStateException("Type \"" + tName + "\" does not exists! " + s);
			}
			tList.add( t );
		}
		
		varNames.add(a.getUniqueName());
		typeSignatures.put(a.getUniqueName(), s);
		typeLookUp.put(a.getUniqueName(), tList);
	}
	
	/**
	 * Attaches type definitions to a variable.
	 * @param a {@link Term} variable name, where arguments of <code>a</code> are {@link Type} names.
	 * @param v {@link Type} name of value.
	 */
	public void attachTypes( Term a, Term v ) {
		if ( v == null ) {
			v = BooleanTerm;
		}
		ArrayList<Type> tList = new ArrayList<Type>();
		for ( int i = 0 ; i < a.getNumArgs() ; i++ ) {
			Type t = types.get(a.getArg(i));
			if ( t == null ) {
				throw new IllegalStateException("Type \"" + a.getArg(i) + "\" does not exists! (" + a + " = " + v + ")");
			}
			tList.add( t );
		}
			
		tList.add(types.get(v));		
		varNames.add(a.getUniqueName());
		typeSignatures.put(a.getUniqueName(), a+"="+v);
		typeLookUp.put(a.getUniqueName(), tList);
	}
	
	/**
	 * Attaches type definitions to a predicate.
	 * Use unique predicate name (e.g. p/2) and provide list of {@link Type}s.
	 * @param predName Unique predicate name (name/arity)
	 * @param tList List of {@link Type}s
	 */
	public void attachTypes( String predName, ArrayList<Type> tList ) {
		varNames.add(predName);
		typeSignatures.put(predName, tList.toString());
		typeLookUp.put(predName, tList);
	}
	
	/**
	 * Test if a variable exists.
	 * @param name unique name of the variable (i.e., name/arity)
	 * @return <code>true</code> if variable exists, <code>false</code> otherwise
	 */
	public boolean hasVariable( String name ) {
		return this.varNames.contains(name);
	}
	
	/**
	 * Get a type by its name.
	 * @param tName type name
	 * @return the type
	 */
	public Type getTypeByName( Term tName ) {
		if ( !types.containsKey(tName) ) {
			throw new IllegalStateException("Type " + tName + " does not exist.");
		}
		return types.get(tName);
	}
	
//	public Type getTypeByName( String tName ) {
//		if ( !types.containsKey(new Term(tName)) ) {
//			throw new IllegalStateException("Type " + tName + " does not exist.");
//		}
//		return types.get(tName);
//	}
//	
	/**
	 * Check if a type exists.
	 * @param tName name of the type
	 * @return <code>true</code> if type exists, <code>false</code> otherwise
	 */
	public boolean hasTypeWithName( Term tName ) {
		return types.containsKey(tName);
	}
	
	/**
	 * Get all objects of a specific type that are used 
	 * as arguments in a a state-variable.
	 * @param tName the type's name
	 * @param a the state-variable
	 * @return all objects of the given type's domain
	 */
	public Collection<Term> getAllObjectsFromDomains( Term tName, Term a ) {
		Collection<Term> r = new ArrayList<Term>();
		
		for ( int i = 0 ; i < a.getNumArgs() ; i++ ) {
			Type t = this.getPredicateTypes(a.getUniqueName(), i);
			if ( t.getName().equals(tName) ){
				if ( a.getArg(i).isGround() ) {
					r.add(a.getArg(i));
				}
			}
		}		
		return r;
	}
	
	/**
	 * Get types of the arguments and return value of a predicate.
	 * Last element in the list is the return value (boolean in case of relations).
	 * @param predID Predicate ID of the form p/4, where 4 is the arity.
	 * @return List of {@link Type}s of the predicate including return type as last element.
	 */
	public ArrayList<Type> getPredicateTypes( String predID ) {
		if ( !typeLookUp.containsKey(predID) ) {
			throw new IllegalStateException("Type signature of " + predID + " does not exist.");
		}
		return typeLookUp.get(predID);
	}
	
	/**
	 * Check if a unique name (name/arity) has 
	 * a type signature definition.
	 * 
	 * @param uName Unique name of a complex term including the name of the term and the arity. Example: adjacent/2
	 * @return <code>true</code> if the term has a signature, <code>false</code> otherwise.
	 */
	public boolean hasSignature( String uName ) {
		return typeLookUp.containsKey(uName);
	}
	
	/**
	 * 
	 * Get type of a specific argument of a predicate.
	 * Last element in the list is the return value (boolean in case of relations).
	 * @param predID Predicate ID of the form p/4, where 4 is the arity.
	 * @param i Index of the requested {@link Type}. Use -1 for return value.
	 * @return List of {@link Type}s of the predicate including return type as last element.
	 */
	public Type getPredicateTypes( String predID, int i ) {
		if ( !typeLookUp.containsKey(predID) ) {
			System.err.println(typeLookUp.keySet());
			throw new IllegalStateException("Requesting non-existing type of " + predID);
		}
		Type r;
		if ( i == -1 ) {
			r = typeLookUp.get(predID).get(typeLookUp.get(predID).size()-1);
		} else { 
			r = typeLookUp.get(predID).get(i);
		}
		
		if ( r == null ) {
			System.err.println(this);
			System.err.println(predID);
			throw new IllegalStateException(predID  + " has Type signature ("+this.typeSignatures.get(predID)+"), but no type found for argument " + i + " (-1 is legal and used for return/last type in signature). This is probably caused by a misspelled type name.)" );
		}
		
		return r;
	}
	
	/**
	 * Returns the names of all known types.
	 * @return list of type names
	 */
	public ArrayList<Term> getTypeNames() {
		return typeNames;
	}
	
	
	/**
	 * Flatten {@link Type} hierarchy so that each {@link Type}'s domain only contains ground terms.
	 * If there are {@link Type}s that consist of sub-types, the sub-types in the types domains
	 * will be replaced by the sub-types domain values.
	 */	 
	public void updateTypeDomains() {
		for ( Term tName : typeNames ) {
			Type t = types.get(tName);
			for ( Term v : t.generateDomain(this) )  {
				if ( !types.containsKey(v) ) {
					types.put(v, t);
				}
			}
		}
		
		ArrayList<Term> newTypeNames = new ArrayList<Term>();
		for ( Term s : typeNames ) {
			if ( !newTypeNames.contains(s) ) {
				newTypeNames.add(s);
			}
		}
		this.typeNames = newTypeNames;
		for ( Term tName : this.typeNames ) {
			Type t = this.types.get(tName);
			if ( t instanceof EnumType ) {
				EnumType et = (EnumType)t;
				ArrayList<Term> removeList = new ArrayList<Term>();
				ArrayList<Term> addList = new ArrayList<Term>(); 
				for ( Term val : et.D ) {
					Type valType = types.get(val);
					if ( !valType.name.equals(et.name) && typeNames.contains(val) ) {
//						System.out.println("----->removing");
						if ( !removeList.contains(val) ) {
							removeList.add(val);
							for ( Term val2 : valType.generateDomain(this) ) {
								if ( !et.D.contains(val2) ) {
									addList.add(val2);
								}
							}
						}
					}
				}
				et.D.removeAll(removeList);
				et.D.addAll(addList);
				this.types.put(et.name, et);
			}			
		}
		/*
		 * In case of enumeration, go through the domain and expand non-ground objects to collections of ground objects
		 */
		for ( Term tName : typeNames ) {
			Type t = types.get(tName);
			if ( t instanceof EnumType ) {
				EnumType et = (EnumType)types.get(t.name);
				ArrayList<Term> newObjects = new ArrayList<Term>();
				ArrayList<Term> removeObjects = new ArrayList<Term>();
				
				for ( Term s : et.D ) {
					if ( s.isComplex() ) {
						
//						for ( Term tArg : sTerm.getArgs() ) {
//							if ( this.types.get(tArg.toString()).getDomain().isEmpty() ) {
//								throw new IllegalStateException("Error: Trying to enumerate ground combos of " + sTerm + " will fail:" +
//										"\n" + tArg + " has an empty domain." +
//										"\nThis is often caused by a wrong order of domain definitions and can be fixed by adding objects to " + tArg + " before using " + sTerm);
//							}
//						}
						
						List<Term> combos = getAllGroundCombos(s);
						
						if ( !combos.isEmpty() ) {
							newObjects.addAll(combos);
							removeObjects.add(s);		
						}						
					}
				}
							
				removeObjects.removeAll(newObjects);
								
				for ( Term s : newObjects ) {					
					if ( !et.D.contains(s) ) {
						et.D.add(s);
					}
				}
				
//				et.D.addAll(newObjects);
				et.D.removeAll(removeObjects);
				for ( Term s : newObjects ) {
					this.types.put(s, et);
				}
			}
		}
	}
	
	/**
	 * Generate all ground substitutions for a functor/literal by reading
	 * domain of all {@link Type}s in arguments
	 * 
	 * Example: (ground location) and location = { kitchen bathroom }
	 * returns list: { (ground kitchen) (ground bathroom) }
	 * @param s term to be expanded.
	 * @return List of combinations of all ground {@link Term}s or {@link Term} substituting all {@link Type} names
	 * in s by their domains.
	 */
	public List<Term> getAllGroundCombos( Term s ) {
		ArrayList<Term> r = new ArrayList<Term>();
		
		if ( !s.isComplex() ) {
			if ( !typeNames.contains(s) ) {
				r.add(s);
				return r;
			} else {
				return types.get(s).generateDomain(this);
			}
		} else {
			ArrayList<List<Term>> allDomains = new ArrayList<List<Term>>();
			
			for ( int i = 0 ; i < s.getNumArgs() ; i++ ) {
				allDomains.add(getAllGroundCombos(s.getArg(i)));
			}
			
			GenericComboBuilder<Term> cBuilder = new GenericComboBuilder<Term>();
			
			List<List<Term>> combos = cBuilder.getCombos(allDomains);
			
			for ( List<Term> combo : combos ) {
				Term[] args = combo.toArray(new Term[combo.size()]);
				r.add(Term.createComplex(s.getName(),args));
			}
			return r;
		}
	}
	
	/**
	 * Get all substitutions that make a given state-variable ground.
	 * This method only considers values in the domains of each argument's
	 * type.
	 * @param a the state-variable
	 * @return list of substitutions
	 */
	public ArrayList<Substitution> getAllGroundSubstitutions( Term a ) {
		ArrayList<Substitution> r = new ArrayList<Substitution>();

		if ( a.isGround() ) {
			return r;
		}

		Map<Term,List<Term>> varsAndDomains = getAllVariablesAndDomains(a);
		
		ArrayList<Term> vars = new ArrayList<Term>();
		List<List<Term>> domains = new ArrayList<List<Term>>();
		
		for ( Term k : varsAndDomains.keySet() ) {
			vars.add(k);
			domains.add(varsAndDomains.get(k));
		}
		
		GenericComboBuilder<Term> cB = new GenericComboBuilder<Term>(); 
		
		List<List<Term>> allCombos = cB.getCombos(domains);
		
		
		for ( List<Term> assignment : allCombos ) {
			Substitution theta = new Substitution();
			for ( int i = 0 ; i < vars.size() ; i++ ) {
				theta.add(vars.get(i), assignment.get(i));
			}
			
			r.add(theta);
		}
			
		
		return r;
	}
	
	/**
	 * Return all possible ground instances of a state-variable given the types 
	 * of all arguments.
	 * 
	 * @param v state-variable
	 * @return ground versions of the state-variable
	 */
	public Collection<Term> getAllGroundAtomics( Term v ) {
		Set<Term> r = new HashSet<Term>();
		
		if ( v.isGround() ) {
			r.add(v);
			return r;
		}
		
		ArrayList<Substitution> groundSubst = this.getAllGroundSubstitutions(v);
		for ( Substitution theta : groundSubst ) {
			r.add(v.substitute(theta));
		}			

		return r;
	}
	
	/**
	 * Returns a lookup of all variables used in a state-variable
	 * to the domains of these variables.
	 *  
	 * @param a the state-variable
	 * @return map from variables to their domains 
	 */
//	public Map<Term,ArrayList<Term>> getAllVariablesAndDomains( Term a ) {
//		Map<Term,ArrayList<Term>> r = new HashMap<Term, ArrayList<Term>>();
//		
//		for ( int i = 0 ; i < a.getNumArgs() ; i++ ) {
//			Term arg = a.getArg(i);
//			if ( arg.isVariable() ) {
//				r.put( arg , this.getPredicateTypes(a.getUniqueName(), i).getDomain());
//			} else if ( arg.isComplex() ) {
//				r.putAll(getAllVariablesAndDomains(arg));
//			}
//		}
//		return r;
//	}
	
	/**
	 * Returns a lookup of all variables used in a term
	 * to the domains of these variables. Complex terms 
	 * may contain multiple variables.
	 *  
	 * @param t the the term
	 * @return map from variables to their domains 
	 */
	public Map<Term,List<Term>> getAllVariablesAndDomains( Term t ) {
		Map<Term,List<Term>> r = new HashMap<Term, List<Term>>();
		
		if ( !t.isComplex() ) {
			return r;
		}
		
		for ( int i = 0 ; i < t.getNumArgs() ; i++ ) {
			Term arg = t.getArg(i);
			if ( arg.isVariable() ) {
				r.put( arg , this.getPredicateTypes( t.getUniqueName(), i).generateDomain(this) );
			} else if ( arg.isComplex() ) {
				r.putAll( getAllVariablesAndDomains(arg) );
			}
		}
	
		return r;
	}

	/**
	 * Add a new type.
	 * Will also add new types domain to type look-up
	 * @param t - The type to be added.
	 */
	public void addNewType( Type t ) {	
		if ( !typeNames.contains(t.name) ) {
			typeNames.add(t.name);
			types.put(t.name, t);	
		}
		/*
		 * In case of enumeration, go through the domain and expand non-ground objects to collections of ground objects
		 */
//		if ( t instanceof EnumType ) {
//			EnumType et = (EnumType)types.get(t.name);
//			ArrayList<Term> newObjects = new ArrayList<Term>();
//			ArrayList<Term> removeObjects = new ArrayList<Term>();
//			
//			for ( Term s : et.D ) {
//				if ( s.isComplex() ) {
//					ArrayList<Term> combos = getAllGroundCombos(s);
//					if ( !combos.isEmpty() ) {
//						newObjects.addAll(getAllGroundCombos(s));
//						removeObjects.add(s);
//					}
//				}
//			}
//			et.D.addAll(newObjects);
//			et.D.removeAll(removeObjects);
//		}
//		
//		/*
//		 * Add all values in domain to type lookup:
//		 */
//		for ( Term s : t.getDomain() ) {
//			if ( !types.containsKey(s) ) {
//				types.put(s, t);
//			}
//		}
	}
		
	private HashSet<String> resourcesVars = new HashSet<String>();
	private HashSet<String> nonResourcesVars = new HashSet<String>();
	
	/**
	 * Check if a state-variable and value pair is consistent with its
	 * type's domains.
	 * 
	 * @param var the variable 
	 * @param val the value
	 * @return <code>true</code> if all assignments are consistent with their domains, <code>false</code> otherwise
	 */
	public boolean isConsistentVariableTermAssignment( Term var, Term val ) {
		for ( int i = 0 ; i < var.getNumArgs(); i++ ) {
			Term t = var.getArg(i);
			if ( t.isGround() && !this.getPredicateTypes(var.getUniqueName(), i).contains(t, this)) {
				return false;
			}
		}
		if ( val != null && val.isGround() && !this.getPredicateTypes(var.getUniqueName(), -1).contains(val, this)) {
			return false;
		}		
		return true;
	}
	
	public Term replaceTypesNamesWithVariables( Term t, TypeManager tM ) {
		if ( !t.isComplex() ) {
			if ( tM.hasTypeWithName(t) ) {
				long idx = UniqueID.getID();
				return Term.createVariable("?Type"+idx+"_"+t.getName());
			} else {
				return t;
			}
		} else {
			Term[] argList = new Term[t.getNumArgs()];
			for ( int i = 0 ; i < t.getNumArgs() ; i++ ) {
				argList[i] = replaceTypesNamesWithVariables(t.getArg(i), tM);
			}
			return Term.createComplex(t.getName(), argList);
		}
	}
	
	/**
	 * Check if resource capacity is attached to {@link Statement} s.
	 * @param s A {@link Statement}.
	 * @param cdb A {@link ConstraintDatabase} that may contain {@link ReusableResourceCapacity} constraints.
	 * @return <code>true</code> iff there is a resource constraints on <code>s</code>, false otherwise.
	 */
	public boolean isResourceAssignment( Statement s, ConstraintDatabase cdb ) {
		
		if ( resourcesVars.contains( s.getVariable().getUniqueName() ) ) {
			return true;
		}
		
		for ( ReusableResourceCapacity rrC : cdb.get(ReusableResourceCapacity.class) ) {
			resourcesVars.add(rrC.getVariable().getUniqueName());			
			if ( rrC.getVariable().getUniqueName().equals(s.getVariable().getUniqueName()) ) {
				return true;
			}
		}
		nonResourcesVars.add(s.getVariable().getUniqueName());
		return false;
	}
	
	@Override
	public String toString() {
		String s = "";
		
		for ( Term tName : this.typeNames ) {
			s += types.get(tName).toString() + "\n";
		}
		
		for ( String p : typeLookUp.keySet() ) {
			ArrayList<Type> tList = typeLookUp.get(p);
			if ( tList.size() > 1 ) {
				s += p.split("/")[0] + "( ";
				for ( int i = 0 ; i < tList.size()-1; i++ ) {
					s += tList.get(i).name + ", ";
				}
				s = s.substring(0, s.length()-2) + " ) = " + tList.get(tList.size()-1).name + "\n" ;
			} else {
				s += p.split("/")[0] + " = " + tList.get(tList.size()-1).name + "\n" ;
			}
		}
		return s;	
	}
}
