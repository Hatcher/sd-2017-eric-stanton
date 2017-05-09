package util.math.constants;

public class ExpressionPatterns {
	public static final String VARIABLE_PATTERN = "\\$\\{[a-zA-Z]+\\}";
	public static final String INTEGER_PATTERN = "[0-9]+";
	public static final String CONSTANT_PATTERN = "^"+VARIABLE_PATTERN+"==";
}
