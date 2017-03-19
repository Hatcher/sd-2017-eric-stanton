package services.question.detector;

import java.util.ArrayList;
import java.util.List;

// detects one kind of question
public class QuestionDetector {
	private String treeId = "";
	private String skillId = "";
	
	private String equation = "";
	private List<String> rules = new ArrayList<String>();
	
	public String getTreeId() {
		return treeId;
	}
	public void setTreeId(String treeId) {
		this.treeId = treeId;
	}
	public String getSkillId() {
		return skillId;
	}
	public void setSkillId(String skillId) {
		this.skillId = skillId;
	}
	public String getEquation() {
		return equation;
	}
	public void setEquation(String equation) {
		this.equation = equation;
	}
	public List<String> getRules() {
		return rules;
	}
	public void setRules(List<String> rules) {
		this.rules = rules;
	}
	
	public boolean detected (String randomEquation){
		return true;
	}
	
}
