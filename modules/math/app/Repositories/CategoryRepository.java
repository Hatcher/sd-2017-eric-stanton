package repositories;

import com.avaje.ebean.Model.Finder;

import models.math.Category;

public class CategoryRepository extends Repository{
	public static Finder<Long, Category> find = new Finder<>(Category.class);

	@Override
	public Category find(long id) {
		return find.byId(id);
	}
	
}
