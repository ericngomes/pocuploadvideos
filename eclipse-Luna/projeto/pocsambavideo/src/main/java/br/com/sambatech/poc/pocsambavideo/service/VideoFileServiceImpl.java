package br.com.sambatech.poc.pocsambavideo.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.faces.context.FacesContext;

import org.apache.commons.lang.StringUtils;
import org.primefaces.model.UploadedFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import br.com.sambatech.poc.pocsambavideo.model.VideoFile;
import br.com.sambatech.poc.pocsambavideo.utils.FormataParametrosUtil;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ListObjectsV2Request;
import com.amazonaws.services.s3.model.ListObjectsV2Result;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.amazonaws.services.s3.model.S3ObjectSummary;
/**
 * 
 * @author eric.gomes - (13/07/2016)
 *
 */
@Service
public class VideoFileServiceImpl implements VideoFileService {

	private static final Logger logger = LoggerFactory.getLogger(VideoFileServiceImpl.class);
	
	static final String READ_PERMISSION = "READ";
	static final Integer ONE_MB = 1000000;
	static final String PARAM_BUCKET_NAME = "bucket";
	static final String PARAM_BUCKET_URL = "urlbucket";
	static final String PARAM_ACCESS_KEY = "accesskey";
	static final String PARAM_SECRET_KEY = "secretkey";
	static final String PARAM_ZENCODER_URL = "zencoderurl";
	static final String PARAM_ZENCODER_KEY = "zencoderkey";
	static final String PARAM_ZENCODER_GRANTEE = "zencodergrantee";

	private OutputStream outputStream;
	
	
	/**
	 * Salva o video no Storage Service (Amazon S3)
	 */
	public PutObjectResult saveVideoOnAmazonS3(File videoFile) {
		
		AmazonS3 amazonClientS3 = buildClientOnAmazonS3();

		PutObjectRequest objetoRequest = new PutObjectRequest(
				FormataParametrosUtil.recupera(PARAM_BUCKET_NAME), videoFile.getName(),videoFile);
		
		objetoRequest.setCannedAcl(CannedAccessControlList.PublicRead);
		PutObjectResult result = amazonClientS3.putObject(objetoRequest);
		
		return result;
	}
	
	/**
	 * Cria o objeto <code>amazonS3Client</code> e suas configuracoes de acesso.
	 * @return
	 * 
	 */
	private AmazonS3Client buildClientOnAmazonS3() {
		BasicAWSCredentials awsCredenciais = new BasicAWSCredentials(
				FormataParametrosUtil.recupera(PARAM_ACCESS_KEY),
				FormataParametrosUtil.recupera(PARAM_SECRET_KEY));
		return new AmazonS3Client(awsCredenciais);
	}
	
	/**
	 * 
	 * Recupera o direotiro temp.
	 * 
	 */
	public String getTempDir() {
		logger.info("Rodando o metodo getTempDir()");
		
		String urlTemporaria = FacesContext.getCurrentInstance().getExternalContext().getRealPath(StringUtils.EMPTY);
		String token = "tmp";
		int valorToken = urlTemporaria.indexOf(token);
		String diretorio = urlTemporaria.substring(0, valorToken) + token;
		
		return diretorio + File.separator;
	}
	
	public void uploadVideoFile(UploadedFile videoFile) throws IOException {
		logger.info("Rodando o metodo uploadVideoFile(). URL: "+getTempDir() +" Arquivo de video: " + videoFile);
		
		File novoArquivoVideo = new File(getTempDir() + videoFile.getFileName());

		// Gera arquivo temporario com os dados do arquivo uploaded
		InputStream inputStream = videoFile.getInputstream();
		
		outputStream = new FileOutputStream(novoArquivoVideo);
		
		byte[] buffer = new byte[10 * 1024];
		for (int length; (length = inputStream.read(buffer)) != -1;) {
			outputStream.write(buffer, 0, length);
			outputStream.flush();
		}

		saveVideoOnAmazonS3(novoArquivoVideo);
	}
	
	/**
	 * Converte o objeto AmazonS3 em um objeto VideoFile.
	 * 
	 */
	private VideoFile buildFileVideo(S3ObjectSummary objectSummary) {
		logger.info("Rodando o metodo buildFileVideo(). S3ObjectSummary key: "+objectSummary.getKey());
		
		VideoFile videoFile = new VideoFile();
		videoFile.setVideoFileKey(objectSummary.getKey());

		long tamanhoMG = objectSummary.getSize();
		
		if (tamanhoMG > 0) {
			videoFile.setVideoFileTamanho((double) (tamanhoMG / ONE_MB));
		} else {
			videoFile.setVideoFileTamanho((double) tamanhoMG);
		}

		return videoFile;
	}



	public List<VideoFile> findAll() {
		List<VideoFile> listaVideo = new ArrayList<VideoFile>();
		
		AmazonS3 amazonS3 = buildClientOnAmazonS3();

		try {
			ListObjectsV2Request listaObjetoV2 = new ListObjectsV2Request()
					.withBucketName(FormataParametrosUtil.recupera(PARAM_BUCKET_NAME)).withMaxKeys(2);
			
			ListObjectsV2Result result = new ListObjectsV2Result();
			
			do {
				result = addVideoFormatted(listaVideo, amazonS3, listaObjetoV2);
				
			} while (result.isTruncated() == true);
	
		} catch (Exception e) {
			e.printStackTrace();
		}

		return listaVideo;
	}

	/**
	 * Cria uma lista de videos e retorna o objeto do tipo <code>ListObjectsV2Result</code>
	 * @param listaVideo
	 * @param amazonS3
	 * @param listaObjetoV2
	 * @return
	 */
	private ListObjectsV2Result addVideoFormatted(List<VideoFile> listaVideo,
			AmazonS3 amazonS3, ListObjectsV2Request listaObjetoV2) {
		ListObjectsV2Result objetosV2;
		objetosV2 = amazonS3.listObjectsV2(listaObjetoV2);

		for (S3ObjectSummary objectSummary : objetosV2.getObjectSummaries()) {
			listaVideo.add(buildFileVideo(objectSummary));
		}
		listaObjetoV2.setContinuationToken(objetosV2.getNextContinuationToken());
		
		return objetosV2;
	}

	/**
	 * Retorna uma lista especifica de videos
	 */
	public List<VideoFile> find(String chave) {
		
		List<VideoFile> listaVideos = findAll();
		
		for (Iterator<VideoFile> iterator = listaVideos.iterator(); iterator.hasNext();) {
			VideoFile videoFile = (VideoFile) iterator.next();
			
			if (!videoFile.getVideoFileKey().contains(chave)) {
				iterator.remove();
			}
			
		}

		return listaVideos;
	}


	/**
	 * Exclui o video selecionado da Store
	 */
	public void excluirVideoFile(VideoFile objetoSelecionado) {
		AmazonS3 amazonS3Client = buildClientOnAmazonS3();
		amazonS3Client.deleteObject(new DeleteObjectRequest(FormataParametrosUtil
				.recupera(PARAM_BUCKET_NAME), objetoSelecionado.getVideoFileKey()));

	}

	
	public String getUrlFile(VideoFile file) {
		// TODO Auto-generated method stub
		return null;
	}
}
