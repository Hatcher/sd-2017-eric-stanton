package models.maths;

import com.avaje.ebean.Model;

import javax.persistence.*;

import java.math.BigDecimal;
import java.util.*;

@Entity
public class MathQuestionLabel extends Model {
	
	@Id
	@GeneratedValue
	public Long ruleId;
	public String variableName;
	public BigDecimal x;
	public BigDecimal y;

	public static Finder<String, MathQuestionLabel> finder = new Finder<String,MathQuestionLabel>(MathQuestionLabel.class);
	public static Find<String,MathQuestionLabel> find = new Find<String,MathQuestionLabel>(){};

	public static MathQuestionLabel create(MathQuestionLabel rule) {
		rule.save();
		return rule;
	}
	
}
	