package controllers.math;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import beans.math.tree.RootNode;
import play.mvc.BodyParser;
import play.mvc.BodyParser.Json;
import play.mvc.Controller;
import play.mvc.Result;
import services.s3.RemindBucket;

public class Tree extends Controller {
	RemindBucket remindBucket = new RemindBucket();
	
	@BodyParser.Of(Json.class)
	public Result update() {
		System.out.println("ATTEMPTED AT LEAST");
		JsonNode json = request().body().asJson();
		
		ObjectMapper mapper = new ObjectMapper();
		mapper.setSerializationInclusion(Include.NON_NULL);
		RootNode node = null;
		try{
			node = mapper.readValue(json.toString(), RootNode.class);
			return ok("{\"success\": \""+remindBucket.saveToS3(node)+"\"}");
		}
		catch (Exception e){
			System.out.println("method: "+request().method());
			System.out.println("method: "+request().body().asJson());
			System.out.println("input: "+request().body().asJson().toString());
			e.printStackTrace();
			return badRequest(e.getMessage());
		}
    }    
}
