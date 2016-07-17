package br.com.sambatech.poc.pocsambavideo.model.builder;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import br.com.sambatech.poc.pocsambavideo.business.VideoBusiness;
import br.com.sambatech.poc.pocsambavideo.model.VideoFile;
import br.com.sambatech.poc.pocsambavideo.service.VideoFileService;
import br.com.sambatech.poc.pocsambavideo.utils.FormataParametrosUtil;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;

@RunWith(MockitoJUnitRunner.class)
public class VideoFileServiceTest {
	
	static final String PARAM_BUCKET_NAME = "bucket"; 
	static final String PARAM_BUCKET_URL = "urlbucket";
	static final String PARAM_ACCESS_KEY = "accesskey";
	static final String PARAM_SECRET_KEY = "secretkey";
	static final String PARAM_ZENCODER_URL = "zencoderurl";
	static final String PARAM_ZENCODER_KEY = "zencoderkey";
	static final String PARAM_ZENCODER_GRANTEE = "zencodergrantee";	
	static final String READ_PERMISSION = "READ"; 
	static final String ZENCODER_TEST = "zencodertestjob";
	static final String ERROR_CODE = "NoSuchKey";
	
	@InjectMocks
	private VideoFileService service;
	
	@Mock
	private VideoBusiness business;
	
	@Test
	public void test_findAll() {

		VideoFile video1 = new VideoFileBuilder().defaultScenario().videoFileKey("123").build();
		VideoFile video2 = new VideoFileBuilder().defaultScenario().videoFileKey("2ab").build();
		
		List<VideoFile> videoListMock = new ArrayList<VideoFile>();
		videoListMock.add(video1);
		videoListMock.add(video2);
		
		String keyName = "findJunitKey";
		File tempFile = buildFile(keyName);

		AmazonS3 s3Client = buildS3Client();
		PutObjectResult result = s3Client.putObject(new PutObjectRequest(
				FormataParametrosUtil.consulta(PARAM_BUCKET_NAME), keyName, tempFile));
		
		Assert.assertNotNull(result);
		
		Mockito.when(service.find(keyName)).thenReturn(videoListMock);

	}
	private AmazonS3Client buildS3Client() {
		BasicAWSCredentials awsCredenciais = new BasicAWSCredentials(FormataParametrosUtil.consulta(PARAM_ACCESS_KEY),
				FormataParametrosUtil.consulta(PARAM_SECRET_KEY));
		return new AmazonS3Client(awsCredenciais);
	}

	private File buildFile(String fileName)  {
		File tempFile = new File(getTempDir() + fileName);
		try {
			tempFile.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return tempFile;
	}

	public String getTempDir() {
		return System.getProperty("java.io.tmpdir") + File.separator;
	}
}
