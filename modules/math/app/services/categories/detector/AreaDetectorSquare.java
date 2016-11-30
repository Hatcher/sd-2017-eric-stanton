package services.categories.detector;

import java.math.BigInteger;

import beans.math.MathBean;
import services.equation.Operators;

public class AreaDetectorSquare extends CategoryDetector {
// Area = Length * Width (if length and Width are the same
// Area = Side^2
	@Override
	public boolean isCategory(MathBean mathBean) {
		if (mathBean.getOperators().size()==1){
			if (mathBean.getOperators().get(0).equals(Operators.MULTIPLY)){
				if (mathBean.getIntegers().get(0).equals(mathBean.getIntegers().get(1))){
					return true;
				}
			}
			else if(mathBean.getOperators().get(0).equals(Operators.EXPONENT)){
				if (mathBean.getIntegers().get(1).equals(BigInteger.valueOf(2))){
					return true;
				}
			}
			else{
				return false;
			}
		}
		return false;
	}

	@Override
	public void label(MathBean mathBean) {
		mathBean.getLabels().clear();
		mathBean.getLabels().put("side", mathBean.getIntegers().get(0)+"");
		
	}

	@Override
	public void populateQuestion(MathBean mathBean) {
		mathBean.setQuestion("Calculate the area of the following square.");
	}
}
