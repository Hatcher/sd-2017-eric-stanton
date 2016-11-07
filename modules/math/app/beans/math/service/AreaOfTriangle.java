package beans.math.service;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import beans.math.MathBean;
import models.math.MathQuestion;

public class AreaOfTriangle extends QuestionGenerator{

	public static List<MathBean> generateQuestions(int numQuestions){
		List<MathBean> questions = new ArrayList<MathBean>();
		for (int i =0; i < numQuestions; i++){
			questions.add(generateQuestion());
		}
		return questions;
	}
	private static MathBean generateQuestion(){

		MathBean mathBean = new MathBean();
		mathBean.setType("TRIANGLE_AREA");
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
				mathBean.getOperators().add(MULTIPLY);
			}
		}

		mathBean = multiplyAllBy2(mathBean);
		mathBean.getIntegers().add(BigInteger.valueOf(2));
		mathBean.getOperators().add(DIVIDE);
		
		return mathBean;
	}
	private static MathBean multiplyAllBy2(MathBean mathBean){
		MathBean rBean = new MathBean();
		
		for (BigInteger integer : mathBean.getIntegers()){
			rBean.getIntegers().add(integer.multiply(BigInteger.valueOf(2)));
		}
		for(String operator : mathBean.getOperators()){
			rBean.getOperators().add(operator);
		}

		rBean.setType(mathBean.getType());
		return rBean;
	}
	
}