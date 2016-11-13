package services.equation;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import beans.math.MathBean;
import models.math.MathQuestion;
import services.categories.Categorizer;
import services.categories.detector.CategoryDetector;
import services.categories.detector.PerimeterDetectorSquare;

public class EquationGenerator {
	private Categorizer categorizer = new Categorizer();
	private Random random = new Random();
	private static final int MAX_OPERATORS = 2;
	private static final int MIN_OPERATORS = 1;
	
	public List<MathBean> generateQuestions(int numQuestions){
		List<MathBean> questions = new ArrayList<MathBean>();
		for (int i =0; i < numQuestions; i++){
			questions.add(generateQuestion());
		}
		return questions;
	}
	private MathBean generateQuestion(){
		MathBean mathBean = new MathBean();
		int clauses = random.nextInt(MAX_OPERATORS )+MIN_OPERATORS; // 1-3 operators
		String basicOperator = getRandomOperation();

		
		int iterations = clauses;

		boolean firstIteration = true;
		for (int i=0; i < iterations; i++){
			System.out.println(i);
			BigInteger number = getRandomNumber();
			basicOperator = getRandomOperation();
			if (basicOperator.equals(Operators.DIVIDE)){
				number = BigInteger.valueOf(random.nextInt(2)+2); // only divide by 2 or 3
			}
			if (firstIteration){
				mathBean.getIntegers().add(number);
				firstIteration = false;
			}
			mathBean.getIntegers().add(number);
			mathBean.getOperators().add(basicOperator);
		}

		
		
		categorizer.populateCategories(mathBean);
		
		return mathBean;
	}
	
	private String getRandomOperation(){
		Random random = new Random();
		return Operators.ALL_OPERATORS.get(random.nextInt(Operators.ALL_OPERATORS.size()));
	}
	private String getRandomWeightedOperation(){
		Random random = new Random();
		return Operators.WEIGHTED_OPERATORS.get(random.nextInt(Operators.WEIGHTED_OPERATORS.size()));
	}
	private BigInteger getRandomNumber(){
		return BigInteger.valueOf(random.nextInt(9)+1);
	}
	
}