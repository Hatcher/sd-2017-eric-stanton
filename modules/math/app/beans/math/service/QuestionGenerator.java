package beans.math.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import models.math.MathQuestion;

public class QuestionGenerator {

	private static List<String> allOperators = new ArrayList<String>(Arrays.asList("+", "-", "*", "/"));
	private static List<String> addSubOperators = new ArrayList<String>(Arrays.asList("+", "-"));
	
	protected static String MULTIPLY = "*";
	protected static String DIVIDE = "/";
	protected static String ADD = "+";
	protected static String SUBTRACT = "-";
	
	protected static String getRandomOperation(){
		Random random = new Random();
		return allOperators.get(random.nextInt(4));
	}
	
	protected static void multiplyAllPartsBy2(MathQuestion question){
		// TODO implement
	}
	protected static void divideAllBy2(MathQuestion question){
		question.setQuestionText("("+question.getQuestionText()+")/2");
	}
}