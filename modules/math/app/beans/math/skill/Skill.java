package beans.math.skill;
import java.util.List;
import java.util.ArrayList;

public class Skill {
	private String name = "";
	private List<Skill> requiredSkills = new ArrayList();
	
	public Skill(){
		
	}
	public String getName(){
		return name;
	}
	public void setName(String name){
		this.name = name;
	}
	public List<Skill> getRequiredSkills(){
		return requiredSkills;
	}
	
}
