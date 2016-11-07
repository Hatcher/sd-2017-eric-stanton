package beans.math.service;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import beans.math.MathBean;
import models.math.MathQuestion;

public class AreaOfSquare extends QuestionGenerator{

	public static List<MathBean> generateQuestions(int numQuestions){
		List<MathBean> questions = new ArrayList<MathBean>();
		for (int i =0; i < numQuestions; i++){
			questions.add(generateQuestion());
		}
		return questions;
	}
	private static MathBean generateQuestion(){

		MathBean mathBean = new MathBean();
		mathBean.setType("RECTANGLE_AREA");
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

		
		return mathBean;
	}


	
}