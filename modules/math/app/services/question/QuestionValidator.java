package services.question;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import beans.math.question.MathQuestionBean;
import beans.math.question.QuestionRootNode;
import beans.math.question.Rule;
import net.sourceforge.jeval.EvaluationException;
import net.sourceforge.jeval.Evaluator;
import util.math.constants.ExpressionPatterns;

public class QuestionValidator {
	public List<String> validate(QuestionRootNode questionNode) {
		List<String> errors = new ArrayList<String>();
		errors = validateRequiredFields(questionNode, errors);
		errors = validateNoDecimals(questionNode, errors);
		errors = validateNoParenthesis(questionNode, errors);

		if (!errors.isEmpty()) {
			return errors;
		} else {
			MathQuestionBean question = questionNode.getQuestion();
			String evaluableEquation = findAndReplaceConstants(question.getEquation(), question.getRules());
			if (!isValidEquation(evaluableEquation)) {
				errors.add("equation is not solveable");
			}

			for (Rule rule : question.getRules()) {
				if ((rule.getRule() != null) && !"".equals(rule.getRule())) {
					if (!isValidEquation(rule.getRule())) {
						errors.add("equation is not solveable");
					}
				}
			}
			if (!ruleVariablesInEquation(question)) {
				errors.add("rule contains variable not found in equation");
			}
		}
		return errors;
	}

	private boolean ruleVariablesInEquation(MathQuestionBean question) {
		Pattern pattern = Pattern.compile(ExpressionPatterns.VARIABLE_PATTERN);

		for (Rule rule : question.getRules()) {
			Matcher matcher = pattern.matcher(rule.getRule());
			while (matcher.find()) {
				if (!question.getEquation().contains(matcher.group())) {
					return false;
				}
			}
		}
		return true;
	}

	private boolean isValidEquation(String equation) {
		Evaluator eval = new Evaluator();
		try {
			String evaluableEquation = equation.replaceAll(ExpressionPatterns.VARIABLE_PATTERN, "1");
			String result = eval.evaluate(evaluableEquation);
			if (result.equals("Infinity")){
				return false; // divide by zero
			}
			return true;
		} catch (EvaluationException e) {
			return false;
		}
	}

	private String findAndReplaceConstants(String equation, List<Rule> rules) {
		String constantsReplaced = equation;
		Pattern variablePattern = Pattern.compile(ExpressionPatterns.VARIABLE_PATTERN);
		
		for (Rule rule : rules) {
			Matcher variableMatcher = variablePattern.matcher(rule.getRule().replaceAll("\\s", ""));
			String variableName = "${z}";
			if (variableMatcher.find()){
				variableName = variableMatcher.group();
			}
				
			
			String ruleText = rule.getRule().replaceAll(ExpressionPatterns.CONSTANT_PATTERN, "");
			if (ruleText.matches(ExpressionPatterns.INTEGER_PATTERN)) {
				int constant = Integer.parseInt(ruleText);
				constantsReplaced = constantsReplaced.replaceAll(Pattern.quote(variableName), constant+"");
			}
			
		}

		return constantsReplaced;
	}

	private List<String> validateNoDecimals(QuestionRootNode questionNode, List<String> errors) {
		if (questionNode.getQuestion().getEquation().contains(".")) {
			errors.add("No Decimals Allowed in Question Creation.  Use Fractions instead");
		}
		return errors;
	}

	private List<String> validateNoParenthesis(QuestionRootNode questionNode, List<String> errors) {
		if (questionNode.getQuestion().getEquation().contains("(")
				|| questionNode.getQuestion().getEquation().contains(")")) {
			errors.add("Parentheses are not supported.  Please restructure the formula to not include parenthesese.");
		}
		return errors;
	}

	private List<String> validateRequiredFields(QuestionRootNode questionNode, List<String> errors) {
		if (questionNode == null) {
			errors.add("question cannot be empty");
		} else {
			MathQuestionBean question = questionNode.getQuestion();
			if (question == null) {
				errors.add("question cannot be empty");
			} else {
				if ((question.getEquation() == null) || ("".equals(question.getEquation()))) {
					errors.add("equation cannot be empty");
				}
				if ((question.getQuestionName() == null) || ("".equals(question.getQuestionName()))) {
					errors.add("question name cannot be empty");
				}
				if ((question.getQuestionText() == null) || ("".equals(question.getQuestionText()))) {
					errors.add("question text cannot be empty");
				}
				if ((question.getSkillId() == null) || ("".equals(question.getSkillId()))) {
					errors.add("skill id cannot be empty");
				}
			}
		}
		return errors;
	}
}
