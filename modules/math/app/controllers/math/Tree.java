package controllers.math;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import beans.math.MathBean;
import models.math.MathQuestion;
import models.math.Test;
import beans.math.TestBean;
import beans.math.tree.NodeStructure;
import beans.math.tree.RootNode;
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
		
		ObjectMapper mapper = new ObjectMapper();
		mapper.setSerializationInclusion(Include.NON_NULL);
		RootNode node = null;
		try{
			node = mapper.readValue(request().body().asJson().toString(), RootNode.class);
			return ok(mapper.writeValueAsString(node));
		}
		catch (Exception e){
			System.out.println("method: "+request().method());
			System.out.println("method: "+request().body().asJson());
			System.out.println("input: "+request().body().asJson().toString());
			e.printStackTrace();
			// return exception status 500
		}
		return ok();

    }    
}
