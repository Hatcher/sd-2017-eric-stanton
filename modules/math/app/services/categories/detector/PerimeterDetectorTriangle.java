package services.categories.detector;

import java.math.BigInteger;

import beans.math.MathBean;
import services.equation.Operators;

// does not include perimeter of circles
public class PerimeterDetectorTriangle extends CategoryDetector {
// Perimeter = a + b + c
	@Override
	public boolean isCategory(MathBean mathBean) {
		if (mathBean.getOperators().size() == 2){
			return containsOnlyAddition(mathBean);
		}
		
		return false;
	}
	
	
}
