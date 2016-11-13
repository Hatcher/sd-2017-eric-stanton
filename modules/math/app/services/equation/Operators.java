package services.equation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Operators {
	public static final String ADD = "+";
	public static final String SUBTRACT = "-";
	public static final String MULTIPLY = "*";
	public static final String DIVIDE = "/";
	public static final String EXPONENT = "^";
	
	
	public static final List<String> ALL_OPERATORS = new ArrayList<String>(Arrays.asList(ADD, SUBTRACT, MULTIPLY, DIVIDE));
	public static final List<String> ADD_AND_SUB = new ArrayList<String>(Arrays.asList(ADD, SUBTRACT));
	
	public static final List<String> WEIGHTED_OPERATORS = new ArrayList<String>(Arrays.asList(ADD, MULTIPLY, ADD,ADD,ADD,ADD,ADD,ADD,MULTIPLY,MULTIPLY,MULTIPLY,MULTIPLY,MULTIPLY,MULTIPLY,MULTIPLY));
	
	
}
