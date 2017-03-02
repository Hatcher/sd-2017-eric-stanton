package repositories;

import com.avaje.ebean.Model.Finder;
import models.math.MathQuestion;

public class QuestionRepository extends Repository{
	public static Finder<Long, MathQuestion> find = new Finder<>(MathQuestion.class);

	@Override
	public MathQuestion find(long id) {
		return find.byId(id);
	}
	
}
