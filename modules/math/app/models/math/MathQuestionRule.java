package models.math;

import com.avaje.ebean.Model;

import javax.persistence.*;
import java.util.*;

@Entity
public class MathQuestionRule extends Model {
	
	@Id
	@GeneratedValue
	public Long ruleId;
	public Long questionId;
	public String ruleText;

	public static Finder<String, MathQuestionRule> finder = new Finder<String,MathQuestionRule>(MathQuestionRule.class);
	public static Find<String,MathQuestionRule> find = new Find<String,MathQuestionRule>(){};

	public static MathQuestionRule create(MathQuestionRule rule) {
		rule.save();
		return rule;
	}
	
}
	