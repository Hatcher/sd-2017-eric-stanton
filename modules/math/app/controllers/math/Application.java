package controllers.math;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import beans.math.MathBean;
import models.math.MathQuestion;
import models.math.Test;
import beans.math.TestBean;
import play.mvc.Controller;
import play.mvc.Result;
import services.categories.Categorizer;
import services.equation.EquationGenerator;


public class Application extends Controller {

	private EquationGenerator equationGenerator = new EquationGenerator();
	
    public Result getIndex() {
    	TestBean test = new TestBean();
    	test.getMathBeans().addAll(equationGenerator.generateQuestions(30));
    	return ok( views.html.math.index.render( test ) );
    }

    public Result generateTest() {
    	TestBean test = new TestBean();
    	test.getMathBeans().addAll(equationGenerator.generateQuestions(30));
    	return ok( views.html.math.generatetest.render( test ) );
    }
    
    public static Result generateRectangle() {
    	MathBean bean = new MathBean();
    	bean.getIntegers().add(BigInteger.valueOf(2));
    	bean.getIntegers().add(BigInteger.valueOf(3));
    	bean.getTypes().add("RECTANGLE_AREA");
    	
    	return ok( views.html.math.question.templates.rectangle.render( bean ) );
    }
    
}
