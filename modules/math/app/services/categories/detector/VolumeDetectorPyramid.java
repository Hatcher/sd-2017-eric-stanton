package services.categories.detector;

import java.math.BigInteger;

import beans.math.MathBean;
import services.equation.Operators;

// does not include perimeter of circles
public class VolumeDetectorPyramid extends CategoryDetector {
	// Volume = height * base * depth / 2 / 3
	
	@Override
	public boolean isCategory(MathBean mathBean) {
		if (mathBean.getOperators().size() == 3){
			if (containsOnlyMultiplicationAndDivision(mathBean)){
				if (findDivideBy2Operator(mathBean) >= 0){
					if (findDivideByNumberOperator(mathBean, 3) >= 0){
						return true;	
					}
				}
			}
		}
		return false;
	}
}
