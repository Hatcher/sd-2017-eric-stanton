package services.categories.detector;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

import beans.math.MathBean;
import services.equation.Operators;

// does not include perimeter of circles
public class SurfaceAreaDetectorRightPrism extends CategoryDetector {
	// SA = perimeter of base * height + 2*area of base
	// SA = a*h + b*h + c*h + 2*a*b/2
	// SA = a*h + b*h + c*h + a*b

	@Override
	public boolean isCategory(MathBean mathBean) {
		if (mathBean.getOperators().size() == 7) {
			if (!containsOnlyAdditionAndMultiplication(mathBean)) {
				return false;
			}
			if (mathBean.getOperators().get(1).equals(Operators.ADD)
					&& mathBean.getOperators().get(1).equals(Operators.ADD)
					&& mathBean.getOperators().get(1).equals(Operators.ADD)) {
				MathBean expression1 = getBinomial(mathBean, 0);
				MathBean expression2 = getBinomial(mathBean, 2);
				MathBean expression3 = getBinomial(mathBean, 4);
				MathBean expression4 = getBinomial(mathBean, 6);

				if (containsOnlyMultiplication(expression1) && containsOnlyMultiplication(expression2)
						&& containsOnlyMultiplication(expression3) && containsOnlyMultiplication(expression4)) {
					// last condition is to check if it matches the pattern
					// h must exist in 3 expressions
					// a must exist in 2 expressions
					// b must exist in 2 expressions
					// c must exist in 1 expression

					// looking for h (at least 3 times)
					
					boolean foundH = false;
					int locationH = -1;
					int locationA = -1;
					int locationB = -1;
					int locationC = -1;
					
					int count1 = 1;
					int count2 = 1;

					int singlesAllowed = 1; //c
					boolean first = true;
					for (BigInteger tern : expression1.getIntegers()) {
						if (first) {
							if (expression2.getIntegers().contains(tern)) {
								count1++;
							}
							if (expression3.getIntegers().contains(tern)) {
								count1++;
							}
							if (expression4.getIntegers().contains(tern)) {
								count1++;
							}
							first = false;
						} else {
							if (expression2.getIntegers().contains(tern)) {
								count2++;
							}
							if (expression3.getIntegers().contains(tern)) {
								count2++;
							}
							if (expression4.getIntegers().contains(tern)) {
								count2++;
							}
						}
					}
					if (count1 > 3) {
						singlesAllowed = 0; // (c = h scenario)
						foundH = true;
					} 
					if (count1 == 1)
					{
						if (singlesAllowed > 0){
							locationC = 1;
							singlesAllowed = 0;
						}
						else{
							return false;
						}
					}

					
					
					count1 = 1;
					count2 = 1;
					first = true;
					for (BigInteger tern : expression2.getIntegers()) {
						if (first) {
							if (expression1.getIntegers().contains(tern)) {
								count1++;
							}
							if (expression3.getIntegers().contains(tern)) {
								count1++;
							}
							if (expression4.getIntegers().contains(tern)) {
								count1++;
							}
							first = false;
						} else {
							if (expression1.getIntegers().contains(tern)) {
								count2++;
							}
							if (expression3.getIntegers().contains(tern)) {
								count2++;
							}
							if (expression4.getIntegers().contains(tern)) {
								count2++;
							}
						}
					}
					
					if (count1 > 3) {
						singlesAllowed = 0; // (c = h scenario)
						foundH = true;
					} 
					if (count1 == 1)
					{
						if (singlesAllowed > 0){
							singlesAllowed = 0;
						}
						else{
							return false;
						}
					}
					
					if (count2 > 3) {
						singlesAllowed = 0; // (c = h scenario)
						foundH = true;
					} 
					if (count2 == 1)
					{
						if (singlesAllowed > 0){
							singlesAllowed = 0;
						}
						else{
							return false;
						}
					}
					
					count1 = 1;
					count2 = 1;
					first = true;
					for (BigInteger tern : expression3.getIntegers()) {
						if (first) {
							if (expression1.getIntegers().contains(tern)) {
								count1++;
							}
							if (expression2.getIntegers().contains(tern)) {
								count1++;
							}
							if (expression4.getIntegers().contains(tern)) {
								count1++;
							}
							first = false;
						} else {
							if (expression1.getIntegers().contains(tern)) {
								count2++;
							}
							if (expression2.getIntegers().contains(tern)) {
								count2++;
							}
							if (expression4.getIntegers().contains(tern)) {
								count2++;
							}
						}
					}
					
					if (count1 > 3) {
						singlesAllowed = 0; // (c = h scenario)
						foundH = true;
					} 
					if (count1 == 1)
					{
						if (singlesAllowed > 0){
							singlesAllowed = 0;
						}
						else{
							return false;
						}
					}
					
					if (count2 > 3) {
						singlesAllowed = 0; // (c = h scenario)
						foundH = true;
					} 
					if (count2 == 1)
					{
						if (singlesAllowed > 0){
							singlesAllowed = 0;
						}
						else{
							return false;
						}
					}
					
					count1 = 1;
					count2 = 1;
					first = true;
					for (BigInteger tern : expression4.getIntegers()) {
						if (first) {
							if (expression1.getIntegers().contains(tern)) {
								count1++;
							}
							if (expression2.getIntegers().contains(tern)) {
								count1++;
							}
							if (expression3.getIntegers().contains(tern)) {
								count1++;
							}
							first = false;
						} else {
							if (expression1.getIntegers().contains(tern)) {
								count2++;
							}
							if (expression2.getIntegers().contains(tern)) {
								count2++;
							}
							if (expression3.getIntegers().contains(tern)) {
								count2++;
							}
						}
					}
					
					if (count1 > 3) {
						singlesAllowed = 0; // (c = h scenario)
						foundH = true;
					} 
					if (count1 == 1)
					{
						if (singlesAllowed > 0){
							singlesAllowed = 0;
						}
						else{
							return false;
						}
					}
					
					if (count2 > 3) {
						singlesAllowed = 0; // (c = h scenario)
						foundH = true;
					} 
					if (count2 == 1)
					{
						if (singlesAllowed > 0){
							singlesAllowed = 0;
						}
						else{
							return false;
						}
					}
					// at this point, either more than 1 unique number has been found within an expression
					// and all we need to do is see if we found any triplets (or greater), aka 'h', aka height.
					return foundH;

				}
			}

		}
		return false;
	}

	private MathBean getBinomial(MathBean mathBean, int operatorIndex) {
		MathBean expression = new MathBean();
		expression.getOperators().add(mathBean.getOperators().get(operatorIndex));
		expression.getIntegers().add(mathBean.getIntegers().get(operatorIndex));
		expression.getIntegers().add(mathBean.getIntegers().get(operatorIndex + 1));
		return expression;
	}

}
