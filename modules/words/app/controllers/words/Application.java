package controllers.words;

import play.mvc.Controller;
import play.mvc.Result;

public class Application extends Controller {

    public static Result getIndex() {
        return ok( views.html.words.index.render( "Hello, " + models.words.MyLibrary.getName() + ". Welcome to Play " +
		        "Framework." ) );
    }

}
