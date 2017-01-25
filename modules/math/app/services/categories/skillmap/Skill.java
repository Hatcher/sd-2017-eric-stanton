package services.categories.skillmap;

import beans.math.MathBean;

public abstract class Skill{
	
	Skill derivedSkill;
	
	public abstract String translateDirect(MathBean mathBean);
	public abstract String translateDerived(MathBean mathBean);
	
	public Skill getDerivedSkill(){
		return derivedSkill;
	}
	public void setDerivedSkill(Skill skill){
		this.derivedSkill = skill;
	}
	
	
	
	
}
