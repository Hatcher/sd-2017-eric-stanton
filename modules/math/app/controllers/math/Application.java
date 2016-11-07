package controllers.math;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import beans.math.MathBean;
import beans.math.service.AreaOfSquare;
import beans.math.service.AreaOfTriangle;
import beans.math.service.PerimeterOfSquare;
import beans.math.service.QuestionGenerator;
import beans.math.service.EquationGenerator;
import models.math.MathQuestion;
import models.math.Test;
import beans.math.TestBean;
import play.mvc.Controller;
import play.mvc.Result;


public class Application extends Controller {

    public static Result getIndex() {
        return ok( views.html.words.index.render( "Hello, " + models.words.MyLibrary.getName() + ". Welcome to Play " +
		        "Framework." ) );
    }

    public static Result generateTest() {
    	TestBean test = new TestBean();
    	test.getMathBeans().addAll(EquationGenerator.generateQuestions(4));
    	test.getMathBeans().addAll(AreaOfSquare.generateQuestions(3));
    	test.getMathBeans().addAll(AreaOfTriangle.generateQuestions(5));
    	test.getMathBeans().addAll(PerimeterOfSquare.generateQuestions(3));
    	Collections.shuffle(test.getMathBeans());
    	return ok( views.html.math.generatetest.render( test ) );
    }
    
    public static Result generateRectangle() {
    	MathBean bean = new MathBean();
    	bean.getIntegers().add(BigInteger.valueOf(2));
    	bean.getIntegers().add(BigInteger.valueOf(3));
    	bean.setType("RECTANGLE_AREA");
    	
    	return ok( views.html.math.question.templates.rectangle.render( bean ) );
    }
    
}
