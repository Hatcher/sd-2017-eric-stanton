package beans.math;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;

import net.sourceforge.jeval.EvaluationException;
import net.sourceforge.jeval.Evaluator;
import services.equation.Operators;

public class MathBean {

	private String chosenType = "";
	private List<BigInteger> integers = new ArrayList<BigInteger>();
	private List<String> operators = new ArrayList<String>();
	private Set<String> types = new HashSet<String>();

	public MathBean() {
		this.types.add("Arithmetic");
		this.types.add("WordProblem");
	}

	public String getChosenType() {
		return chosenType;
	}

	public void setChosenType(String chosenType) {
		this.chosenType = chosenType;
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

	public String getAnswer() {
		Evaluator eval = new Evaluator();
		try {
			return eval.evaluate(this.toString());
		} catch (EvaluationException e) {
			return "";
		}
	}

	public Set<String> getAnswerChoices(String answer) {
		Set<String> answers = new HashSet<String>();
		answers.add(answer);
		Evaluator eval = new Evaluator();
		Random random = new Random();
		
		while (answers.size() < 3) {
			List<BigInteger> wrongIntegers = cloneIntegers();
			List<String> wrongOperators = cloneOperators();
			
			try {
				if (random.nextInt(2) >0){
					wrongIntegers.set(random.nextInt(wrongIntegers.size()), BigInteger.valueOf(random.nextInt(9)+1));
				}
				else{
					wrongOperators.set(random.nextInt(wrongOperators.size()), Operators.ALL_OPERATORS.get(random.nextInt(Operators.ALL_OPERATORS.size())));
				}
				answers.add(eval.evaluate(this.toString()));
			} catch (EvaluationException e) {
				return new HashSet<String>();
			}
		}
		return answers;

	}

	private List<BigInteger> cloneIntegers(){
		List<BigInteger> wrongIntegers = new ArrayList<BigInteger>();
		for (BigInteger tmp : getIntegers()){
			wrongIntegers.add(BigInteger.valueOf(tmp.intValue()));
		}
		return wrongIntegers;
	}

	private List<String> cloneOperators(){
		List<String> wrongOperators = new ArrayList<String>();
		for (String tmp : getOperators()){
			wrongOperators.add(tmp);
		}
		return wrongOperators;
	}
	
	public String toString() {
		String rValue = "";
		for (int i = 0; i < getOperators().size(); i++) {
			if (rValue.equals("")) {
				rValue += getIntegers().get(i) + " " + getOperators().get(i) + " " + getIntegers().get(i + 1);
			} else {
				rValue += getOperators().get(i) + " " + getIntegers().get(i + 1);
			}
		}
		return rValue;
	}
}
