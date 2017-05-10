package services.equation;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.regex.Pattern;

import beans.math.LabelBean;
import beans.math.MathBean;
import models.math.MathQuestion;
import models.math.MathQuestionLabel;

public class EquationGenerator {
	private Random random = new Random();

	public List<MathBean> generateQuestions(int numQuestionsRequested, List<String> questionTypes) {
		if (questionTypes.isEmpty()) {
			return new ArrayList<MathBean>();
		}

		List<MathBean> questions = new ArrayList<MathBean>();
		List<MathQuestion> questionsFound = MathQuestion.find("", questionTypes);

		int num = 0;
		int numRepeats;

		if (questionsFound.size() < 1) {
			return new ArrayList<MathBean>();
		} else if (numQuestionsRequested < questionsFound.size()) {
			numRepeats = 1;
		} else {
			numRepeats = numQuestionsRequested / questionsFound.size() + 1;
		}

		int maxOperators = getMaxOperators(questionsFound);
		int minOperators = getMinOperators(questionsFound);
		Set<String> operators = getOperators(questionsFound);
		Set<BigDecimal> constants = getConstants(questionsFound);

		while (numQuestionsRequested > questions.size()) {
			int clauses = random.nextInt(maxOperators) + minOperators;

			MathBean question = generateQuestion(questionsFound, numRepeats, clauses, operators, constants);
			if ((question.getType() != null) && !"".equals(question.getType())) {
				question.setId(++num + "");
				questions.add(question);
			}
		}
		return questions;
	}

	private MathBean generateQuestion(List<MathQuestion> questionsFound, int numRepeats, int clauses,
			Set<String> operators, Set<BigDecimal> constants) {
		MathBean mathBean = new MathBean();
		String basicOperator = null;

		int iterations = clauses;
		boolean firstIteration = true;

		for (int i = 0; i < iterations; i++) {
			BigDecimal number = getRandomNumber(constants);
			basicOperator = getRandomOperation(operators);

			if (firstIteration) {
				mathBean.getIntegers().add(number);
				number = getRandomNumber(constants);
				firstIteration = false;
			}

			mathBean.getIntegers().add(number);
			mathBean.getOperators().add(basicOperator);
		}

		// categorize equation
		for (MathQuestion question : questionsFound) {
			if (question != null) {
				if (followsRules(question, mathBean.toString(), numRepeats)) {

					mathBean.setType(question.questionId + "");

					Map<String, String> variables = MathQuestion.getVariables(mathBean.toString(), question.equation);
					String updatedQuestionText = question.questionText;
					Iterator<Map.Entry<String, String>> it = variables.entrySet().iterator();
					mathBean.getLabels().clear();

					while (it.hasNext()) {
						Map.Entry<String, String> pair = (Map.Entry<String, String>) it.next();
						updatedQuestionText = updatedQuestionText.replaceAll(Pattern.quote(pair.getKey()),
								pair.getValue());
						for (MathQuestionLabel mathQuestionLabel : question.labels) {
							if (mathQuestionLabel.variableName != null) {
								String matchableVariable = "${" + mathQuestionLabel.variableName + "}";
								if (matchableVariable.equals(pair.getKey())) {
									LabelBean labelBean = new LabelBean();
									labelBean.setValue(pair.getValue());
									labelBean.setX(mathQuestionLabel.x);
									labelBean.setY(mathQuestionLabel.y);
									mathBean.getLabels().add(labelBean);
								}
							}
						}
					}

					mathBean.setQuestion(updatedQuestionText);
					mathBean.setImageUrl(question.imageUrl);
					question.used++;
					break;
				}
			}
		}
		return mathBean;
	}

	private boolean followsRules(MathQuestion question, String randomEquation, int maxRepeats) {
		if ((question.used < maxRepeats) && MathQuestion.matchesEquation(question.equation, randomEquation)) {
			Map<String, String> variables = MathQuestion.getVariables(randomEquation, question.equation);
			if (MathQuestion.followsRules(variables, question.rules)) {
				return true;
			}
		}
		return false;

	}

	private String getRandomOperation(Set<String> operators) {
		if (!operators.isEmpty()) {
			int index = random.nextInt(operators.size());
			int i = 0;
			for (String operator : operators) {
				if (i >= index) {
					return operator;
				}
				i++;
			}

		} else {
			return null;
		}
		return null; // should be unreachable
	}

	private BigDecimal getRandomNumber(Set<BigDecimal> constants) {
		if (!constants.isEmpty()) {
			if (random.nextInt(2) > 0) {
				return BigDecimal.valueOf(random.nextInt(9) + 1);
			} else {
				int index = random.nextInt(constants.size());
				int i = 0;
				for (BigDecimal constant : constants) {
					if (i >= index) {
						return constant;
					}
					i++;
				}
			}
		} else {
			return BigDecimal.valueOf(random.nextInt(9) + 1);
		}
		return BigDecimal.ONE; // should be unreachable
	}

	private int getMinOperators(List<MathQuestion> questionsFound) {
		boolean firstClause = true;
		int minOperators = -1;
		for (MathQuestion question : questionsFound) {
			if (firstClause) {
				minOperators = question.getOperators().size(); // requires
																// strict
																// operator
																// usage (no
																// parenthesis,
																// no implied
																// multiplication)
				firstClause = false;
			} else {
				int possibleMin = question.getOperators().size();
				if (minOperators > possibleMin) {
					minOperators = possibleMin;
				}
			}
		}
		return minOperators;
	}

	private int getMaxOperators(List<MathQuestion> questionsFound) {
		boolean firstClause = true;
		int maxOperators = -1;
		for (MathQuestion question : questionsFound) {
			if (firstClause) {
				maxOperators = question.getOperators().size(); // requires
																// strict
																// operator
																// usage (no
																// parenthesis,
																// no implied
																// multiplication)
				firstClause = false;
			} else {
				int possibleMax = question.getOperators().size();
				if (maxOperators < possibleMax) {
					maxOperators = possibleMax;
				}
			}
		}
		return maxOperators;
	}

	private Set<BigDecimal> getConstants(List<MathQuestion> questionsFound) {
		Set<BigDecimal> constants = new HashSet<>();
		for (MathQuestion question : questionsFound) {
			constants.addAll(question.getConstants());
		}

		return constants;
	}

	private Set<String> getOperators(List<MathQuestion> questionsFound) {
		Set<String> operators = new HashSet<>();
		for (MathQuestion question : questionsFound) {
			operators.addAll(question.getOperators());
		}
		return operators;
	}
}