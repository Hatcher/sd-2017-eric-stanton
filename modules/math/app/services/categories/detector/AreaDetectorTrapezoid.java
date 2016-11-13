package services.categories.detector;

import java.math.BigInteger;

import beans.math.MathBean;
import services.equation.Operators;

// does not include perimeter of circles
public class AreaDetectorTrapezoid extends CategoryDetector {
// Scenario 1 : Area = height*(base + top) / 2
// Scenario 2 : Area = (height * base + height * top) / 2
// Scenario 3 : Area = height * base / 2 + height * top / 2
	
	
	
	@Override
	public boolean isCategory(MathBean mathBean) {
// do not have ability to handle parentheses yet.  Only handling scenario 3

		if (mathBean.getOperators().size() == 5){
			// candidate for Scenario 3
			if (mathBean.getOperators().get(2).equals(Operators.ADD)){
				MathBean expression1 = new MathBean();
				expression1.getOperators().add(mathBean.getOperators().get(0));
				expression1.getOperators().add(mathBean.getOperators().get(1));
				expression1.getIntegers().add(mathBean.getIntegers().get(0));
				expression1.getIntegers().add(mathBean.getIntegers().get(1));
				expression1.getIntegers().add(mathBean.getIntegers().get(2));				
				
				MathBean expression2 = new MathBean();
				expression2.getOperators().add(mathBean.getOperators().get(3));
				expression2.getOperators().add(mathBean.getOperators().get(4));
				expression2.getIntegers().add(mathBean.getIntegers().get(3));
				expression2.getIntegers().add(mathBean.getIntegers().get(4));
				expression2.getIntegers().add(mathBean.getIntegers().get(5));
				
				int ignoreIndex1 = findDivideBy2Operator(expression1);
				int ignoreIndex2 = findDivideBy2Operator(expression2);
				if ( ignoreIndex1 >=0 && ignoreIndex2 >=0){ // has div by 2 condition
					if (containsOnlyMultiplicationAndDivision(expression1) && containsOnlyMultiplicationAndDivision(expression2)){
						return containsMirrorTern(expression1, ignoreIndex1, expression2, ignoreIndex2);
					}
				}
			}
		}
		return false;
	}
	private boolean containsMirrorTern(MathBean mathBean1, int ignoreIndex1, MathBean mathBean2, int ignoreIndex2){
		int i;
		for (i = 0; i < mathBean1.getOperators().size(); i++){
			if (i != ignoreIndex1){
				if (containsIntegerExcluding(mathBean1.getIntegers().get(i), mathBean2, ignoreIndex2+1)){
					return true;
				}
					
			}
		}
		
		return false;
	}
	private boolean containsIntegerExcluding(BigInteger needle, MathBean mathBean, int ignoreIndex){
		for (int i = 0; i < mathBean.getIntegers().size(); i++){
			if (i != ignoreIndex){
				if (needle.equals(mathBean.getIntegers().get(i))){
					return true;
				}
			}
		}
		
		return false;
	}
}
