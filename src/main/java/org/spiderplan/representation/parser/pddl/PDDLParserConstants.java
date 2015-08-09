/* Generated By:JavaCC: Do not edit this line. PDDLParserConstants.java */
package org.spiderplan.representation.parser.pddl;


/**
 * Token literal values and constants.
 * Generated by org.javacc.parser.OtherFilesGen#start()
 */
public interface PDDLParserConstants {

  /** End of File. */
  int EOF = 0;
  /** RegularExpression Id. */
  int DEFINE = 6;
  /** RegularExpression Id. */
  int DOMAIN = 7;
  /** RegularExpression Id. */
  int COLDOMAIN = 8;
  /** RegularExpression Id. */
  int PROBLEM = 9;
  /** RegularExpression Id. */
  int COLPROBLEM = 10;
  /** RegularExpression Id. */
  int REQUIREMENTS = 11;
  /** RegularExpression Id. */
  int FUNCTIONS = 12;
  /** RegularExpression Id. */
  int ACTIONCOSTS = 13;
  /** RegularExpression Id. */
  int TYPING = 14;
  /** RegularExpression Id. */
  int STRIPS = 15;
  /** RegularExpression Id. */
  int DURATIVE_ACTIONS = 16;
  /** RegularExpression Id. */
  int TIMED_INITIAL_LITERALS = 17;
  /** RegularExpression Id. */
  int TYPES = 18;
  /** RegularExpression Id. */
  int PREDICATES = 19;
  /** RegularExpression Id. */
  int ACTION = 20;
  /** RegularExpression Id. */
  int DURATIVE_ACTION = 21;
  /** RegularExpression Id. */
  int PARAMETERS = 22;
  /** RegularExpression Id. */
  int DURATION = 23;
  /** RegularExpression Id. */
  int CONDITION = 24;
  /** RegularExpression Id. */
  int PRECONDITION = 25;
  /** RegularExpression Id. */
  int EFFECT = 26;
  /** RegularExpression Id. */
  int OBJECTS = 27;
  /** RegularExpression Id. */
  int CONSTANTS = 28;
  /** RegularExpression Id. */
  int INIT = 29;
  /** RegularExpression Id. */
  int GOAL = 30;
  /** RegularExpression Id. */
  int METRIC = 31;
  /** RegularExpression Id. */
  int NOT = 32;
  /** RegularExpression Id. */
  int OVER_ALL = 33;
  /** RegularExpression Id. */
  int AT_START = 34;
  /** RegularExpression Id. */
  int AT_END = 35;
  /** RegularExpression Id. */
  int AT = 36;
  /** RegularExpression Id. */
  int MINIMIZE = 37;
  /** RegularExpression Id. */
  int MAXIMIZE = 38;
  /** RegularExpression Id. */
  int EITHER = 39;
  /** RegularExpression Id. */
  int AND = 40;
  /** RegularExpression Id. */
  int INCREASE = 41;
  /** RegularExpression Id. */
  int DECREASE = 42;
  /** RegularExpression Id. */
  int ASSIGN = 43;
  /** RegularExpression Id. */
  int SCALEUP = 44;
  /** RegularExpression Id. */
  int SCALEDOWN = 45;
  /** RegularExpression Id. */
  int OP = 46;
  /** RegularExpression Id. */
  int CP = 47;
  /** RegularExpression Id. */
  int OBRACKET = 48;
  /** RegularExpression Id. */
  int CBRACKET = 49;
  /** RegularExpression Id. */
  int OBRACE = 50;
  /** RegularExpression Id. */
  int CBRACE = 51;
  /** RegularExpression Id. */
  int COMMA = 52;
  /** RegularExpression Id. */
  int DOT = 53;
  /** RegularExpression Id. */
  int ASSIGNMENT = 54;
  /** RegularExpression Id. */
  int SEMICOLON = 55;
  /** RegularExpression Id. */
  int SLASH = 56;
  /** RegularExpression Id. */
  int TO = 57;
  /** RegularExpression Id. */
  int PLUS = 58;
  /** RegularExpression Id. */
  int MINUS = 59;
  /** RegularExpression Id. */
  int TIMES = 60;
  /** RegularExpression Id. */
  int DIVIDED = 61;
  /** RegularExpression Id. */
  int QMARKS = 62;
  /** RegularExpression Id. */
  int EQUALS = 63;
  /** RegularExpression Id. */
  int NEQUALS = 64;
  /** RegularExpression Id. */
  int NEG = 65;
  /** RegularExpression Id. */
  int LESS = 66;
  /** RegularExpression Id. */
  int LESSEQ = 67;
  /** RegularExpression Id. */
  int GREATER = 68;
  /** RegularExpression Id. */
  int GREATEREQ = 69;
  /** RegularExpression Id. */
  int QM = 70;
  /** RegularExpression Id. */
  int HASH = 71;
  /** RegularExpression Id. */
  int DOLLAR = 72;
  /** RegularExpression Id. */
  int UNDERSCORE = 73;
  /** RegularExpression Id. */
  int TERM = 74;
  /** RegularExpression Id. */
  int FLOAT = 75;
  /** RegularExpression Id. */
  int STRING = 76;
  /** RegularExpression Id. */
  int INTVALUE = 77;
  /** RegularExpression Id. */
  int PROLOG_COMMAND = 78;

  /** Lexical state. */
  int DEFAULT = 0;

  /** Literal token values. */
  String[] tokenImage = {
    "<EOF>",
    "\" \"",
    "\"\\r\"",
    "\"\\t\"",
    "\"\\n\"",
    "<token of kind 5>",
    "\"define\"",
    "\"domain\"",
    "\":domain\"",
    "\"problem\"",
    "\":problem\"",
    "\":requirements\"",
    "\":functions\"",
    "\":action-costs\"",
    "\":typing\"",
    "\":strips\"",
    "\":durative-actions\"",
    "\":timed-initial-literals\"",
    "\":types\"",
    "\":predicates\"",
    "\":action\"",
    "\":durative-action\"",
    "\":parameters\"",
    "\":duration\"",
    "\":condition\"",
    "\":precondition\"",
    "\":effect\"",
    "\":objects\"",
    "\":constants\"",
    "\":init\"",
    "\":goal\"",
    "\":metric\"",
    "\"not\"",
    "\"over all\"",
    "\"at start\"",
    "\"at end\"",
    "\"at\"",
    "\"minimize\"",
    "\"maximize\"",
    "\"either\"",
    "\"and\"",
    "\"increase\"",
    "\"decrease\"",
    "\"assign\"",
    "\"scale-up\"",
    "\"scale-down\"",
    "\"(\"",
    "\")\"",
    "\"[\"",
    "\"]\"",
    "\"{\"",
    "\"}\"",
    "\",\"",
    "\".\"",
    "\":=\"",
    "\";\"",
    "\"/\"",
    "\"->\"",
    "\"+\"",
    "\"-\"",
    "\"*\"",
    "\"\\\\\"",
    "\"\\\"\"",
    "\"=\"",
    "\"!=\"",
    "\"!\"",
    "\"<\"",
    "\"<=\"",
    "\">\"",
    "\">=\"",
    "\"?\"",
    "\"#\"",
    "\"$\"",
    "\"_\"",
    "<TERM>",
    "<FLOAT>",
    "<STRING>",
    "<INTVALUE>",
    "<PROLOG_COMMAND>",
  };

}
