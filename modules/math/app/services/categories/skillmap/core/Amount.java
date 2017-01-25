package services.categories.skillmap.core;

import beans.math.MathBean;
import services.categories.skillmap.Skill;

public class Amount extends Skill{

	@Override
	public String translateDirect(MathBean mathBean) {
		return "The number of "; // mathbean needs to hold object type for the question (let's say square, cubes, triangles, etc)
	}

	@Override
	public String translateDerived(MathBean mathBean) {
		return getDerivedSkill().translateDirect(mathBean); // one level concept for now.
	}
	

}
