package beans.math;

import java.math.BigInteger;
import java.util.*;

public class MathBean {

	private String chosenType = "";
	private List<BigInteger> integers = new ArrayList<BigInteger>();
	private List<String> operators = new ArrayList<String>();
	private Set<String> types = new HashSet<String>();

	
	
	public MathBean(){
		this.types.add("Arithmetic");
		this.types.add("WordProblem");
	}
	
	public String getChosenType(){
		return chosenType;
	}
	public void setChosenType(String chosenType){
		this.chosenType=chosenType;
	}
	
	public List<BigInteger> getIntegers() {
		return integers;
	}

	public List<String> getOperators() {
		return operators;
	}

	public Set<String> getTypes() {
		return types;
	}

	public String toString() {
		String rValue = "";
		for (int i = 0; i < getOperators().size(); i++) {
			if (rValue.equals("")){
				rValue += getIntegers().get(i)+" "+getOperators().get(i)+" "+getIntegers().get(i+1);
			}
			else{
				rValue += getOperators().get(i)+" "+getIntegers().get(i+1);
			}
		}
		return rValue;
	}
}
