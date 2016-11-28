package beans.math.skill;
import java.util.List;

import services.categories.Categorizer;
import services.categories.detector.CategoryDetector;

import java.util.ArrayList;

public class SkillBuilder {
	
	
	public Skill buildSkills(){
		
//		Categorizer categorizer = new Categorizer();
//		Skill skill = new Skill();
//		skill.setName("Area");
//		for(CategoryDetector detector : categorizer.getDetectors()){
//			String type = detector.getClass().getSimpleName().replaceAll("Detector", "");
//			if (type.startsWith(skill.getName())){
//				Skill requiredSkill = new Skill();
//				requiredSkill.setName(detector.getClass().getSimpleName().replaceAll("Detector", ""));
//				skill.getRequiredSkills().add(requiredSkill);
//			}
//		}
//		
//			
//		skill.setName("Perimeter");
//		
//		
//		
//		return skill;
		return new Skill();
	}
	
}
