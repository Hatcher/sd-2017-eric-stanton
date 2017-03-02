package controllers.math;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.fasterxml.jackson.databind.JsonNode;

import beans.math.MathBean;
import models.math.MathQuestion;
import models.math.Test;
import beans.math.TestBean;
import play.mvc.BodyParser;
import play.mvc.BodyParser.Json;
import play.mvc.Controller;
import play.mvc.Result;
import services.categories.Categorizer;
import services.equation.EquationGenerator;
import services.s3.RemindBucket;

public class Tree extends Controller {
	RemindBucket remindBucket = new RemindBucket();
	
	@BodyParser.Of(Json.class)
	public Result update() {
		JsonNode json = request().body().asJson();
		
		for (JsonNode node : json.elements()){
			
		}
		
		
		return ok(views.html.math.demo.json.index.render());
    }    
}
