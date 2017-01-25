package services.categories.skillmap.core.muldiv;

import beans.math.MathBean;
import services.categories.skillmap.Skill;
import services.categories.skillmap.core.addsub.Addition;

public class Multiplication extends Skill {

	public Multiplication(){
		setDerivedSkill(new Addition());
	}
	
	@Override
	public String translateDirect(MathBean mathBean) {
		return "Multiply the number of ";
	}

	@Override
	public String translateDerived(MathBean mathBean) {
		return getDerivedSkill().translateDirect(mathBean); // one level concept for now.
	}

}
