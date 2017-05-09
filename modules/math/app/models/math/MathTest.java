package models.math;

import com.avaje.ebean.Model;

import net.sourceforge.jeval.EvaluationException;
import net.sourceforge.jeval.Evaluator;
import util.math.constants.ExpressionPatterns;

import javax.persistence.*;

import java.math.BigDecimal;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Entity
public class MathTest extends Model {

	@Id
	@GeneratedValue
	public Long testId;
	public String testJson;

	public static Finder<String, MathTest> finder = new Finder<String, MathTest>(MathTest.class);
	public static Find<String, MathTest> find = new Find<String, MathTest>() {};

	public static MathTest create(MathTest mathQuestion) {
		mathQuestion.save();
		return mathQuestion;
	}



	public static MathTest find(Long id) {
		return find.where().eq("testId", id).findUnique();
	}

}
