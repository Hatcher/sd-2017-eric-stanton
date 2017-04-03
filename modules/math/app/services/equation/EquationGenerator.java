package services.equation;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.regex.Pattern;

import beans.math.LabelBean;
import beans.math.MathBean;
import models.maths.MathQuestion;
import models.maths.MathQuestionLabel;

public class EquationGenerator {
	private Random random = new Random();
	private static final int MAX_OPERATORS = 2;
	private static final int MIN_OPERATORS = 1;

	public List<MathBean> generateQuestions(int numQuestions, List<String> questionTypes) {
		if (questionTypes.isEmpty()) {
			return new ArrayList<MathBean>();
		}

		List<MathBean> questions = new ArrayList<MathBean>();
		int num = 0;
		List<MathQuestion> questionsOfTypes = MathQuestion.find("", questionTypes);

		int numRepeats = numQuestions / questionsOfTypes.size() + 1;

		while (numQuestions > questions.size()) {
			Collections.shuffle(questionsOfTypes);
			MathBean question = generateQuestion(questionsOfTypes, numRepeats);
			if ((question.getType() != null) && !"".equals(question.getType())) {
				question.setId(++num + "");
				questions.add(question);
			}
		}
		return questions;
	}

	private MathBean generateQuestion(List<MathQuestion> questionsOfTypes, int numRepeats) {

		// generate equation
		MathBean mathBean = new MathBean();
		int clauses = random.nextInt(MAX_OPERATORS) + MIN_OPERATORS; // 1-3
																		// operators
		String basicOperator = getRandomOperation();

		int iterations = clauses;
		boolean firstIteration = true;
		for (int i = 0; i < iterations; i++) {
			BigDecimal number = getRandomNumber();
			basicOperator = getRandomOperation();

			if (firstIteration) {
				mathBean.getIntegers().add(number);
				number = getRandomNumber();
				firstIteration = false;
			}
			if (basicOperator.equals(Operators.DIVIDE)) {
				// only divide by 2 or 3
				//number = BigDecimal.valueOf(random.nextInt(2) + 2);
				number = BigDecimal.valueOf(2);
			}

			mathBean.getIntegers().add(number);
			mathBean.getOperators().add(basicOperator);
		}

		// categorize equation
		for  (MathQuestion question : questionsOfTypes){
			if (question != null) {
				if (followsRules(question,mathBean.toString(), numRepeats)){
				
					mathBean.setType(question.questionId + "");
		
					Map<String, String> variables = MathQuestion.getVariables(mathBean.toString(), question.equation);
					String updatedQuestionText = question.questionText;
					Iterator<Map.Entry<String, String>> it = variables.entrySet().iterator();
					mathBean.getLabels().clear();
		
					while (it.hasNext()) {
						Map.Entry<String, String> pair = (Map.Entry<String, String>) it.next();
						updatedQuestionText = updatedQuestionText.replaceAll(Pattern.quote(pair.getKey()), pair.getValue());
						for (MathQuestionLabel mathQuestionLabel : question.labels) {
							if (mathQuestionLabel.variableName != null) {
								String matchableVariable = "${"+mathQuestionLabel.variableName+"}";
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
	
	private boolean followsRules(MathQuestion question, String randomEquation, int maxRepeats){
			if ((question.used < maxRepeats) && MathQuestion.matchesEquation(question.equation, randomEquation)){
				Map<String, String> variables = MathQuestion.getVariables(randomEquation, question.equation);
				if (MathQuestion.followsRules(variables, question.rules)) {
					return true;
				}
			}
			return false;

	}

	private String getRandomOperation(List<MathQuestion> questions){
		Random random = new Random();
		MathQuestion randomQuestion = questions.get(random.nextInt(questions.size()));
//		randomQuestion.get
		return "*";
		
	}
	
	private String getRandomOperation() {
		Random random = new Random();
		return Operators.ALL_OPERATORS.get(random.nextInt(Operators.ALL_OPERATORS.size()));
	}

	private String getRandomWeightedOperation() {
		Random random = new Random();
		return Operators.WEIGHTED_OPERATORS.get(random.nextInt(Operators.WEIGHTED_OPERATORS.size()));
	}

	private BigDecimal getRandomNumber() {
		return BigDecimal.valueOf(random.nextInt(9) + 1);
	}

}