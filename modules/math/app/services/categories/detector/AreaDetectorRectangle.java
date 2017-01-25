package services.categories.detector;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;

import beans.math.MathBean;
import services.equation.Operators;

// does not include perimeter of circles
public class AreaDetectorRectangle extends CategoryDetector {
//Area = Length * Width
	
	@Override
	public boolean isCategory(MathBean mathBean) {
		if (mathBean.getOperators().size() == 1){
			if (containsOnlyMultiplicationAndDivision(mathBean)){
				return true;
			}
		}
		return false;
	}

	@Override
	public void label(MathBean mathBean) {
		mathBean.getLabels().put("width", getLongest(mathBean)+"");
		mathBean.getLabels().put("length", getShortest(mathBean)+"");
		
	}

	@Override
	public void populateQuestion(MathBean mathBean) {
		mathBean.setQuestion("Calculate the area of the rectangle.");
	}
	
	private BigDecimal getShortest(MathBean mathBean){
		BigDecimal shortest = mathBean.getIntegers().get(0);
		if (mathBean.getOperators().get(0).equals(Operators.DIVIDE)){
			shortest =  BigDecimal.ONE.divide(mathBean.getIntegers().get(1), 2, RoundingMode.HALF_UP);
		}
		return shortest;
	}
	private BigDecimal getLongest(MathBean mathBean){
		BigDecimal longest = mathBean.getIntegers().get(0);
		if (!mathBean.getOperators().get(0).equals(Operators.DIVIDE)){
			longest =  mathBean.getIntegers().get(1);
		}
		return longest;
	}
	
}
