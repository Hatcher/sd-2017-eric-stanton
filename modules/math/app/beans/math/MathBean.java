package beans.math;

import java.math.BigInteger;
import java.util.*;

public class MathBean {

	private List<BigInteger> integers = new ArrayList<BigInteger>();
	private List<String> operators = new ArrayList<String>();
	private String type = "EQUATION";

	public List<BigInteger> getIntegers() {
		return integers;
	}

	public List<String> getOperators() {
		return operators;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String toString() {
		String rValue = "";
		Random random = new Random();
		boolean appendToEnd = true;
		for (int i = 0; i < integers.size(); i++) {
			if (appendToEnd){
				rValue += " " + integers.get(i);
			}
			else{
				rValue = integers.get(i)+" "+rValue;
			}
			appendToEnd = true;
			if (i < integers.size() - 1) {
				if (i > 0) {
					if ("*".equals(operators.get(i))
							&& ("+".equals(operators.get(i - 1)) || "-".equals(operators.get(i - 1)))) {
						
						if (random.nextInt(2)>0){
							rValue = "("+rValue+")"+operators.get(i);
						}
						else{
							rValue = operators.get(i)+"("+rValue+")";
							appendToEnd= false;
						}
					} else {
						rValue += " " + operators.get(i);
					}
				}
				else{
					rValue += " " + operators.get(i);
				}

			}
		}
		return rValue.trim();

	}
}
