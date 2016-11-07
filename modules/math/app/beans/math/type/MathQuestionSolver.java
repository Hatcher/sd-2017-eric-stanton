package beans.math.type;

import java.math.BigDecimal;
import java.math.BigInteger;

public class MathQuestionSolver extends BigDecimal{
	public BigDecimal answer;
	
	public MathQuestionSolver(long val) {
		super(val);
		answer = new BigDecimal(val);
	}

	public void test(){
		//answer.
	}
	
}
