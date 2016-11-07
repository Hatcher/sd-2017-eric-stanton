package beans.math.service;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import beans.math.MathBean;
import models.math.MathQuestion;

public class PerimeterOfSquare extends QuestionGenerator{

	public static List<MathBean> generateQuestions(int numQuestions){
		List<MathBean> questions = new ArrayList<MathBean>();
		for (int i =0; i < numQuestions; i++){
			questions.add(generateQuestion());
		}
		return questions;
	}
	private static MathBean generateQuestion(){

		MathBean mathBean = new MathBean();
		mathBean.setType("RECTANGLE_PERIMETER");
		Random random = new Random();
		int clauses = 2;
		
		// for each clause, create part of the equation
		boolean firstClause = true;
		for (int i=0; i < clauses; i++){
			if (firstClause){
				int nomial = random.nextInt(9)+1;
				mathBean.getIntegers().add(BigInteger.valueOf(nomial));
				firstClause = false;
			}
			else{
				int nomial = random.nextInt(9)+1;
				mathBean.getIntegers().add(BigInteger.valueOf(nomial));
				mathBean.getOperators().add(ADD);
			}
		}
		if (random.nextInt(2)>0){
			mathBean.getIntegers().add(BigInteger.valueOf(2));
			mathBean.getOperators().add(MULTIPLY);
		}
		else{
			List<BigInteger> replacementIntegers = new ArrayList<BigInteger>();
			for(BigInteger integer : mathBean.getIntegers()){
				replacementIntegers.add(BigInteger.valueOf(integer.intValue()));
				replacementIntegers.add(BigInteger.valueOf(integer.intValue()));
			}
			mathBean.getIntegers().clear();
			mathBean.getIntegers().addAll(replacementIntegers);
			mathBean.getOperators().add(ADD);
			mathBean.getOperators().add(ADD);
		}
		
//		MathQuestion mathQuestion = new MathQuestion();
//		mathQuestion.setQuestionText(mathBean.toString());
		return mathBean;
	}

	private static MathBean multiplyAllBy2(MathBean mathBean){
		MathBean rBean = new MathBean();
		
		
		for (BigInteger integer : mathBean.getIntegers()){
			rBean.getIntegers().add(integer);
		}
		for(String operator : mathBean.getOperators()){
			rBean.getOperators().add(operator);
		}

		return rBean;
	}
	
}