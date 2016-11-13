package services.categories.detector;

import java.math.BigInteger;

import beans.math.MathBean;
import services.equation.Operators;

// does not include perimeter of circles
public class MedianDetector extends CategoryDetector {

	@Override
	public boolean isCategory(MathBean mathBean) {
		return false;
	}
	
}
