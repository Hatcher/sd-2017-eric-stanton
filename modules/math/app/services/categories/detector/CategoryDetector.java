package services.categories.detector;

import java.math.BigInteger;

import beans.math.MathBean;
import services.equation.Operators;

public abstract class CategoryDetector {

	public abstract boolean isCategory(MathBean mathBean);
	
	public abstract void label(MathBean mathBean);
	
	public abstract void populateQuestion(MathBean mathBean);
	
	protected int findDivideBy2Operator(MathBean mathBean){
		return findDivideByNumberOperator(mathBean, 2);
	}
	
	protected int findDivideByNumberOperator(MathBean mathBean, int number){
		for	(int i =0; i < mathBean.getOperators().size(); i++){
			String operator = mathBean.getOperators().get(i);
			if (operator.equals(Operators.DIVIDE)){
				if (mathBean.getIntegers().get(i+1).equals(BigInteger.valueOf(number))){
					return i;
				}
			}
		}
		return -1;
	}
	
	protected int find2(MathBean mathBean){
		//for (String operator : mathBean.getOperators()){
		for	(int i =0; i < mathBean.getIntegers().size(); i++){
			if (mathBean.getIntegers().get(i).equals(BigInteger.valueOf(2))){
				return i;
			}
		}
		return -1;
	}
	
	protected boolean containsOnlyMultiplicationAndDivision(MathBean mathBean){
		for (String operator : mathBean.getOperators()){
			if (!(operator.equals(Operators.MULTIPLY) || operator.equalsIgnoreCase(Operators.DIVIDE))){
				return false;
			}
		}
		return true;
	}
	protected boolean containsOnlyAdditionAndSubtraction(MathBean mathBean){
		for (String operator : mathBean.getOperators()){
			if (!(operator.equals(Operators.SUBTRACT) || operator.equalsIgnoreCase(Operators.ADD))){
				return false;
			}
		}
		return true;
	}
	
	protected boolean containsOnlyAdditionAndMultiplication(MathBean mathBean){
		for (String operator : mathBean.getOperators()){
			if (!(operator.equals(Operators.MULTIPLY) || operator.equalsIgnoreCase(Operators.ADD))){
				return false;
			}
		}
		return true;
	}
	
	protected boolean containsOnlyMultiplication(MathBean mathBean){
		for (String operator : mathBean.getOperators()){
			if (!(operator.equalsIgnoreCase(Operators.MULTIPLY))){
				return false;
			}
		}
		return true;
	}
	
	protected boolean containsOnlyAddition(MathBean mathBean){
		for (String operator : mathBean.getOperators()){
			if (!(operator.equalsIgnoreCase(Operators.ADD))){
				return false;
			}
		}
		return true;
	}
	protected boolean containsOnlySubtraction(MathBean mathBean){
		for (String operator : mathBean.getOperators()){
			if (!(operator.equalsIgnoreCase(Operators.SUBTRACT))){
				return false;
			}
		}
		return true;
	}
	protected boolean containsOnlyDivision(MathBean mathBean){
		for (String operator : mathBean.getOperators()){
			if (!(operator.equalsIgnoreCase(Operators.DIVIDE))){
				return false;
			}
		}
		return true;
	}
	
}
