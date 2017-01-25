package services.categories.skillmap.core.addsub;

import beans.math.MathBean;
import services.categories.skillmap.Skill;
import services.categories.skillmap.core.Amount;

public class Addition extends Skill{

	public Addition(){
		setDerivedSkill(new Amount());
	}
	
	@Override
	public String translateDirect(MathBean mathBean) {

		return " added to "; // add the different object types together
	}

	@Override
	public String translateDerived(MathBean mathBean) {
		return getDerivedSkill().translateDirect(mathBean); // one level concept for now.
	}

}
