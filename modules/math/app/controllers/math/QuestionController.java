package controllers.math;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import beans.math.question.QuestionRootNode;
import play.mvc.BodyParser;
import play.mvc.BodyParser.Json;
import play.mvc.Controller;
import play.mvc.Result;
import services.question.QuestionService;
import services.question.QuestionValidator;
import services.s3.RemindBucket;

public class QuestionController extends Controller {
	QuestionService service = new QuestionService();
	QuestionValidator validator = new QuestionValidator();
	
	@BodyParser.Of(Json.class)
	public Result create() {
		JsonNode json = request().body().asJson();
		
		ObjectMapper mapper = new ObjectMapper();
		mapper.setSerializationInclusion(Include.NON_NULL);
		QuestionRootNode node = null;

		
		
		
		
		try{
			node = mapper.readValue(json.toString(), QuestionRootNode.class);
			
			// validate
			List<String> errors = validator.validate(node);
			if (errors.isEmpty()){
				// save to database
				return ok("{\"response\":{\"success\": \""+service.create(node)+"\"}}");
			}
			else{
				boolean first = true;
				String errorsString = "\"errors\": [";
				for (String error : errors){
					if (!first){
						errorsString+=",";
					}
					else{
						first = false;
					}
					errorsString+="\""+error+"\"";
				}
				errorsString +="]";
				
				return ok("{\"response\":{\"success\": \"false\","
						+ errorsString
						+ "}}");
				
			}
			
		}
		catch (Exception e){
			e.printStackTrace();
			return badRequest(e.getMessage());
		}
    }    
}
