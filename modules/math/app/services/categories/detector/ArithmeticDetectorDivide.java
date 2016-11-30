package services.categories.detector;

import java.math.BigInteger;

import beans.math.MathBean;
import services.equation.Operators;


public class ArithmeticDetectorDivide extends CategoryDetector {
	@Override
	public boolean isCategory(MathBean mathBean) {
			return containsOnlyDivision(mathBean);
	}

	@Override
	public void label(MathBean mathBean) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void populateQuestion(MathBean mathBean) {
		// TODO Auto-generated method stub
		
	}
	
	
}
