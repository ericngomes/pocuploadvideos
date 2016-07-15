package br.com.sambatech.poc.pocsambavideo.business;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.primefaces.model.UploadedFile;
import org.springframework.stereotype.Service;

import br.com.sambatech.poc.pocsambavideo.model.VideoFile;

import com.amazonaws.services.s3.model.PutObjectResult;

/**
 * Responsible interface for making the bridge between the business layer and persistence
 * Responsavel pela interface que faz a ponte entre o controle
 *
 * @author ericgomes
 *
 */
@Service
public interface VideoBusiness {

	/**
	 * Salva o video no Storage Service (Amazon S3)
	 */
	public PutObjectResult saveVideoOnAmazonS3(File videoFile);
	
	/**
	 * Faz o upload e conversao para Stream do video
	 * @param videoFile
	 * @throws IOException
	 */
	public void uploadVideoFile(UploadedFile videoFile) throws IOException;
	
	/**
	 * Retorna uma lista de videos
	 * @return
	 */
	public List<VideoFile> findAll();
	
	/**
	 * Retorna uma lista especifica de video 
	 * @param chave
	 * @return
	 */
	public List<VideoFile> find(String chave);
	
	/**
	 * Retorna a URL do video
	 * @param file
	 * @return
	 */
	public String getUrlVideoOnAmazonS3(VideoFile videoFile);
	
	/**
	 * Exclui o video 
	 * @param objetoSelecionado
	 */
	public void excluirVideoFile(VideoFile objetoSelecionado);
}
