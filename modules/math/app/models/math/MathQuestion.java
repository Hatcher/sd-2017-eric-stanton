package models.math;

import com.avaje.ebean.Model;

import net.sourceforge.jeval.EvaluationException;
import net.sourceforge.jeval.Evaluator;
import util.math.constants.ExpressionPatterns;

import javax.persistence.*;

import java.math.BigDecimal;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Entity
public class MathQuestion extends Model {

	@Id
	@GeneratedValue
	public Long questionId;
	public String questionName;
	public String questionText;
	public String equation;
	public String imageUrl;
	public Date createdDate;
	public String userId;
	public String skillId;
	public String treeId;
	@OneToMany(cascade = CascadeType.ALL)
	public List<MathQuestionRule> rules;

	@OneToMany(cascade = CascadeType.ALL)
	public List<MathQuestionLabel> labels;

	@Transient
	public int used = 0;

	public static Finder<String, MathQuestion> finder = new Finder<String, MathQuestion>(MathQuestion.class);
	public static Find<String, MathQuestion> find = new Find<String, MathQuestion>() {
	};

	public static MathQuestion create(MathQuestion mathQuestion) {
		mathQuestion.save();
		return mathQuestion;
	}

	public static MathQuestion find(String treeId, List<String> skillIds, String randomEquation) {
		List<MathQuestion> questionsBySkill = find.where().eq("treeId", treeId).in("skillId", skillIds).findList();

		for (MathQuestion question : questionsBySkill) {
			if (matchesEquation(question.equation, randomEquation)) {
				Map<String, String> variables = getVariables(randomEquation, question.equation);
				if (followsRules(variables, question.rules)) {
					return question;
				}
			}
		}
		return null;
	}

	public static List<MathQuestion> find(String treeId, List<String> skillIds) {
		return find.where().eq("treeId", treeId).in("skillId", skillIds).findList();
	}

	public static boolean matchesEquation(String equationWithVariables, String randomEquation) {
		String tmpEquationWithVariables = equationWithVariables.replaceAll("\\s", "");
		String tmpRandomEquation = randomEquation.replaceAll("\\s", "");

		tmpEquationWithVariables = tmpEquationWithVariables.replaceAll(ExpressionPatterns.VARIABLE_PATTERN, "_");
		tmpRandomEquation = tmpRandomEquation.replaceAll(ExpressionPatterns.INTEGER_PATTERN, "_");

		return tmpEquationWithVariables.equals(tmpRandomEquation);
	}

	public static boolean followsRules(Map<String, String> variables, List<MathQuestionRule> rules) {

		// for each rule, see if the variable fits the rule
		for (MathQuestionRule rule : rules) {
			if (!"".equals(rule.ruleText) && rule.ruleText != null) {
				Iterator<Map.Entry<String, String>> it = variables.entrySet().iterator();
				String evaluate = rule.ruleText;
				while (it.hasNext()) {
					Map.Entry<String, String> pair = (Map.Entry<String, String>) it.next();
					String variableName = pair.getKey();
					String variableValue = pair.getValue();

					evaluate = evaluate.replaceAll(Pattern.quote(variableName), variableValue);

				}
				Evaluator eval = new Evaluator();
				try {
					if (!"1.0".equals(eval.evaluate(evaluate))) {
						return false;
					}
				} catch (EvaluationException e) {
					return false;
				}

			}
		}
		return true;
	}

	public Set<BigDecimal> getConstants() {
		Set<BigDecimal> constants = new HashSet<>();

		for (MathQuestionRule rule : this.rules) {
			String ruleText = rule.ruleText;
			ruleText = ruleText.replaceAll("\\s", "");
			ruleText.replaceAll(ExpressionPatterns.CONSTANT_PATTERN, "");
			if (ruleText.matches(ExpressionPatterns.INTEGER_PATTERN)) {
				int constant = Integer.parseInt(ruleText);
				constants.add(BigDecimal.valueOf(constant));
			}
		}

		return constants;
	}

	public static Map<String, String> getVariables(String randomEquation, String equation) {
		Map<String, String> variables = new HashMap<String, String>();
		Pattern variablePattern = Pattern.compile(ExpressionPatterns.VARIABLE_PATTERN);
		Pattern numbersPattern = Pattern.compile(ExpressionPatterns.INTEGER_PATTERN);

		List<String> variableNames = new ArrayList<String>();
		List<String> variableValues = new ArrayList<String>();

		Matcher variablesMatcher = variablePattern.matcher(equation);
		Matcher numbersMatcher = numbersPattern.matcher(randomEquation);

		while (variablesMatcher.find()) {
			variableNames.add(variablesMatcher.group());
		}

		while (numbersMatcher.find()) {
			variableValues.add(numbersMatcher.group());
		}

		for (int i = 0; i < variableNames.size(); i++) {
			variables.put(variableNames.get(i), variableValues.get(i));
		}
		return variables;
	}

	public Map<String, String> getVariables(String randomEquation) {
		Map<String, String> variables = new HashMap<String, String>();
		Pattern variablePattern = Pattern.compile(ExpressionPatterns.VARIABLE_PATTERN);
		Pattern numbersPattern = Pattern.compile(ExpressionPatterns.INTEGER_PATTERN);

		List<String> variableNames = new ArrayList<String>();
		List<String> variableValues = new ArrayList<String>();

		Matcher variablesMatcher = variablePattern.matcher(equation);
		Matcher numbersMatcher = numbersPattern.matcher(randomEquation);

		while (variablesMatcher.find()) {
			variableNames.add(variablesMatcher.group());
		}

		while (numbersMatcher.find()) {
			variableValues.add(numbersMatcher.group());
		}

		for (int i = 0; i < variableNames.size(); i++) {
			variables.put(variableNames.get(i), variableValues.get(i));
		}
		return variables;
	}

	public List<String> getOperators() {
		List<String> operators = new ArrayList<String>();
		String[] operatorsArray = this.equation.split(ExpressionPatterns.VARIABLE_PATTERN);
		for (String operatorString : operatorsArray) {
			if (!"".equals(operatorString)) {
				operators.add(operatorString);
			}
		}
		return operators;
	}
}
