package models.maths;

import com.avaje.ebean.Model;

import javax.persistence.*;
import java.util.*;

@Entity
public class RuleEntity extends Model {
	
	@Id
	@GeneratedValue
	public Long ruleId;
	public Long questionId;
	public String ruleText;

	public static Finder<String, RuleEntity> finder = new Finder<String,RuleEntity>(RuleEntity.class);
	public static Find<String,RuleEntity> find = new Find<String,RuleEntity>(){};

	public static RuleEntity create(RuleEntity rule) {
		rule.save();
		return rule;
	}
	
}
	