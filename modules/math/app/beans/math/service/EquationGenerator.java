package beans.math.service;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import beans.math.MathBean;
import models.math.MathQuestion;

public class EquationGenerator {

	private static List<String> allOperators = new ArrayList<String>(Arrays.asList("+", "-", "*", "/"));
	private static List<String> addSubOperators = new ArrayList<String>(Arrays.asList("+", "-"));

	
	
	public static List<MathBean> generateQuestions(int numQuestions){
		List<MathBean> questions = new ArrayList<MathBean>();
		for (int i =0; i < numQuestions; i++){
			questions.add(generateQuestion());
		}
		return questions;
	}
	private static MathBean generateQuestion(){
		String question = "";
		Random random = new Random();
		int clauses = random.nextInt(2)+2;
		MathBean mathBean = new MathBean();
		// for each clause, create part of the equation
		boolean firstClause = true;
		for (int i=0; i < clauses; i++){
			
			
			if (firstClause){
				mathBean.getIntegers().add(BigInteger.valueOf(random.nextInt(9)+1));
				firstClause = false;
			}
			else{
				mathBean.getIntegers().add(BigInteger.valueOf(random.nextInt(9)+1));
				mathBean.getOperators().add(getRandomOperation());
			}
		}
		mathBean.setType("EQUATION");
		return mathBean;
	}
	
	private static String getRandomOperation(){
		Random random = new Random();
		return allOperators.get(random.nextInt(4));
	}
	// will be necessary for some geometry probelms (example area of a triangle)
	private static void divideAllBy2(MathQuestion question){
		question.setQuestionText("("+question.getQuestionText()+")/2");
	}
}