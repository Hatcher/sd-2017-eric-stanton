package services.equation;

import java.math.BigDecimal;
import java.util.ArrayList;
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
		while (numQuestions > questions.size()) {
			System.out.println("generate question: ");
			MathBean question = generateQuestion(questionTypes);
			if ((question.getType() != null) && !"".equals(question.getType())) {
				question.setId(++num + "");
				questions.add(question);
			}
		}
		return questions;
	}

	private MathBean generateQuestion(List<String> skillIds) {
		MathBean mathBean = new MathBean();
		int clauses = random.nextInt(MAX_OPERATORS) + MIN_OPERATORS; // 1-3
																		// operators
		String basicOperator = getRandomOperation();

		int iterations = clauses;
		System.out.println("generating question inside method");
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
				number = BigDecimal.valueOf(random.nextInt(2) + 2);
			}

			mathBean.getIntegers().add(number);
			mathBean.getOperators().add(basicOperator);
		}

		MathQuestion question = MathQuestion.find("", skillIds, mathBean.toString());
		if (question != null) {
			mathBean.setType(question.questionId + "");

			Map<String, String> variables = MathQuestion.getVariables(mathBean.toString(), question.equation);
			String updatedQuestionText = question.questionText;
			Iterator<Map.Entry<String, String>> it = variables.entrySet().iterator();
			while (it.hasNext()) {
				Map.Entry<String, String> pair = (Map.Entry<String, String>) it.next();
				updatedQuestionText = updatedQuestionText.replaceAll(Pattern.quote(pair.getKey()), pair.getValue());
			}

			mathBean.setQuestion(updatedQuestionText);
			mathBean.setImageUrl(question.imageUrl);
			mathBean.getLabels().clear();
			for (MathQuestionLabel mathQuestionLabel : question.labels)
			{
				LabelBean labelBean = new LabelBean();
				labelBean.setName(mathQuestionLabel.variableName);
				labelBean.setX(mathQuestionLabel.x);
				labelBean.setY(mathQuestionLabel.y);
				mathBean.getLabels().add(labelBean);
			}
		}
		return mathBean;
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