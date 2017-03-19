package controllers.math;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import beans.math.question.QuestionRootNode;
import play.mvc.BodyParser;
import play.mvc.BodyParser.Json;
import play.mvc.Controller;
import play.mvc.Result;
import services.question.QuestionService;
import services.s3.RemindBucket;

public class QuestionController extends Controller {
	QuestionService service = new QuestionService();
	
	@BodyParser.Of(Json.class)
	public Result create() {
		JsonNode json = request().body().asJson();
		
		ObjectMapper mapper = new ObjectMapper();
		mapper.setSerializationInclusion(Include.NON_NULL);
		QuestionRootNode node = null;

		try{
			node = mapper.readValue(json.toString(), QuestionRootNode.class);
			// save to database
			return ok("{\"success\": \""+service.create(node)+"\"}");
			// return ok("{\"success\": \""+remindBucket.saveToS3(node)+"\"}");
		}
		catch (Exception e){
			e.printStackTrace();
			return badRequest(e.getMessage());
		}
    }    
}
