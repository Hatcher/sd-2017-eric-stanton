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

// restful requests for tree updates
public class Tree extends Controller {
	RemindBucket remindBucket = new RemindBucket();
	
	@BodyParser.Of(Json.class)
	public Result update() {
		JsonNode json = request().body().asJson();
		
		ObjectMapper mapper = new ObjectMapper();
		mapper.setSerializationInclusion(Include.NON_NULL);
		RootNode node = null;
		try{
			node = mapper.readValue(json.toString(), RootNode.class);
			return ok("{\"success\": \""+remindBucket.saveToS3(node)+"\"}");
		}
		catch (Exception e){
			e.printStackTrace();
			return badRequest(e.getMessage());
		}
    }    
}
