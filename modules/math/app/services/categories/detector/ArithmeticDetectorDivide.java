package services.categories.detector;

import java.math.BigInteger;

import beans.math.MathBean;
import services.equation.Operators;


public class ArithmeticDetectorDivide extends CategoryDetector {
	@Override
	public boolean isCategory(MathBean mathBean) {
			return containsOnlyDivision(mathBean);
	}
	
	
}
