package services.s3;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.UUID;

import com.amazonaws.AmazonClientException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import beans.math.tree.RootNode;

public class RemindBucket {
	public boolean saveToS3(RootNode requestBody) {
		boolean success = true;
		
		AmazonS3 s3 = new AmazonS3Client();
		Region usEast1 = Region.getRegion(Regions.US_EAST_1);
		s3.setRegion(usEast1);
		
		File tmpFile;
		try {
			tmpFile = createTmpFile(requestBody);
		} catch (IOException e) {
			e.printStackTrace();
			success = false;
			return false;
		}
		String bucketName = "remind";
		String s3PathAndName = "Math/Trees/CaringTree.json";

		s3.putObject(new PutObjectRequest(bucketName, s3PathAndName, tmpFile).withCannedAcl(CannedAccessControlList.PublicRead));
		return success;
	}
	private File createTmpFile(RootNode requestBody) throws IOException{
		
		
	
			ObjectMapper mapper = new ObjectMapper();
			mapper.setSerializationInclusion(Include.NON_NULL);

			File tempFile = File.createTempFile("prefix", "suffix");
			FileWriter writer = new FileWriter(tempFile);
			writer.write(mapper.writeValueAsString(requestBody));
			writer.close();
	
		return tempFile;
	}
}
