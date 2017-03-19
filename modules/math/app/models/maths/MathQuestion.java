package models.maths;

import com.avaje.ebean.Model;

import javax.persistence.*;
import java.util.*;

@Entity
public class MathQuestion extends Model {
	
	@Id
	@GeneratedValue
	public Long questionId;
	public String questionName;
	public String questionText;
	public String equation;
	public String imageUrl;
	public Date createdDate;
	public String userId;
	public String skillId;
	@OneToMany(cascade=CascadeType.ALL)
	public List<RuleEntity> rules;
	
	
	public static Finder<String, MathQuestion> finder = new Finder<String,MathQuestion>(MathQuestion.class);
	public static Find<String,MathQuestion> find = new Find<String,MathQuestion>(){};

	public static MathQuestion create(MathQuestion mathQuestion) {
		mathQuestion.save();
		return mathQuestion;
	}
	
}
	