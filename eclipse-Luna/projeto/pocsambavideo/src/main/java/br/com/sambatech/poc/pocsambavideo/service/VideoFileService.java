package br.com.sambatech.poc.pocsambavideo.service;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.primefaces.model.UploadedFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.sambatech.poc.pocsambavideo.business.VideoBusiness;
import br.com.sambatech.poc.pocsambavideo.model.VideoFile;

import com.amazonaws.services.s3.model.PutObjectResult;

/**
 * Classe de serviço resposavel por que mantem o desacoplamento entre o controle
 * com o negocio, no caso as funcionalidades de acesso aos videos
 * 
 * @author eric.gomes - (13/07/2016)
 *
 */
@Service
public class VideoFileService {

	@Autowired
	private VideoBusiness videoBusiness;

	/**
	 * Salva o video no Storage Service (Amazon S3)
	 */
	public PutObjectResult saveVideoOnAmazonS3(File videoFile) {

		return videoBusiness.saveVideoOnAmazonS3(videoFile);
	}

	public void uploadVideoFile(UploadedFile videoFile) throws IOException {
		videoBusiness.uploadVideoFile(videoFile);

	}

	public List<VideoFile> findAll() {
		return videoBusiness.findAll();
	}

	/**
	 * Retorna uma lista especifica de videos
	 */
	public List<VideoFile> find(String chave) {
		return videoBusiness.find(chave);
	}

	/**
	 * Exclui o video selecionado da Store
	 */
	public void excluirVideoFile(VideoFile objetoSelecionado) {
		videoBusiness.excluirVideoFile(objetoSelecionado);

	}

	public String getUrlVideoOnAmazonS3(VideoFile arquivoVideo) {
		return videoBusiness.getUrlVideoOnAmazonS3(arquivoVideo);
	}

}
