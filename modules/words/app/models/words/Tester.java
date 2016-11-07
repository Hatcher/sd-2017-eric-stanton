package models.words;
import com.avaje.ebean.Model;

import java.util.List;


public class Tester extends Model {

	/********************************
	 FIELDS
	 ********************************/
	/* Universal */
	/*===========*/
	public String name;

	/********************************
	 CONSTRUCTORS
	 ********************************/
	public Tester(String s) {
		this.name = s;
	}


	public static Finder<Long, Tester> find = new Finder<Long, Tester>(Tester.class);

	public static List<Tester> getAll() {
		return find
				.findList();
	}
}
