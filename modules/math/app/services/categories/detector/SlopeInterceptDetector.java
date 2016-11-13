package services.categories.detector;

import java.math.BigInteger;

import beans.math.MathBean;
import services.equation.Operators;

// does not include perimeter of circles
public class SlopeInterceptDetector extends CategoryDetector {
// y =mx + b
	@Override
	public boolean isCategory(MathBean mathBean) {
		if (mathBean.getOperators().size()==2){
			if (operatorPlusOrMinus(mathBean.getOperators().get(0))){
				if (operatorMultiplyOrDivide(mathBean.getOperators().get(1))){
					return true;
				}
			}
			else{
				if (operatorPlusOrMinus(mathBean.getOperators().get(1))){
					if (operatorMultiplyOrDivide(mathBean.getOperators().get(0))){
						return true;
					}
				}
			}
		}
		return false;
	}
	
	private boolean operatorPlusOrMinus(String operator){
		return Operators.ADD.equals(operator) || Operators.SUBTRACT.equals(operator);
		
	}
	private boolean operatorMultiplyOrDivide(String operator){
		return Operators.MULTIPLY.equals(operator) || Operators.DIVIDE.equals(operator);
	}
}
