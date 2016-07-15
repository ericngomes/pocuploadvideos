package br.com.sambatech.poc.pocsambavideo.controll;
import org.springframework.stereotype.Controller;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.RequestScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import org.primefaces.model.UploadedFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;

import br.com.sambatech.poc.pocsambavideo.model.VideoFile;
import br.com.sambatech.poc.pocsambavideo.service.VideoFileService;
import br.com.sambatech.poc.pocsambavideo.utils.ControlaTempoProcessamentoUtil;

/**
 * Classe de controle da orquestração da tela com o servicoo de video
 * 
 * @author eric.gomes - (14/07/2016)
 *
 */
@Controller
@RequestScoped
public class VideoFileController implements Serializable {

	private static final Logger logger = LoggerFactory.getLogger(VideoFileController.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = -4428740370717617514L;
	
	@Autowired
	private VideoFileService serviceVideo;	
	
	private VideoFile videoFile;
	
	private List<VideoFile> listaVideosFile;
	
	private UploadedFile uploadVideoFile;
	
	private String chaveVideo;
	
	private VideoFile videoSelecionado;	
	
	
	
	public void abrirArquivo(VideoFile selectedArquivo){
		
		loadVideoSelecionado(selectedArquivo);
		
		logger.info("URL do video: " + videoSelecionado.getVideoFileUrl());
		
		ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
		ControlaTempoProcessamentoUtil.esperandoProcessamento();  	
		
		try {
		
			externalContext.redirect(videoSelecionado.getVideoFileUrl());
		
		} catch (IOException e) {
			showMessage("Falha ao redirecionar para vídeo!");
		}		
	}

	private void loadVideoSelecionado(VideoFile selectedArquivo) {
		setVideoSelecionado(selectedArquivo);
		getVideoSelecionado().setVideoFileUrl(serviceVideo.getUrlFile(videoSelecionado));
	}
	
	/**
	 * Submete o arquivo de video
	 */
	public void upload(){
		try{
			serviceVideo.uploadVideoFile(uploadVideoFile);
			find();
			showMessage("Upload realizado com sucesso!");
		}
		catch(Exception e){
			
			showMessage("Falha ao enviar o arquivo!");
		}
		
	}
	
	/**
	 * Realiza uma busca dos videos
	 */
	public void find(){
		if(StringUtils.hasText(chaveVideo)){
			setArquivosList(serviceVideo.find(chaveVideo));
		}
		else{
			setArquivosList(serviceVideo.findAll());			
		}
	}
	
	private void showMessage(String message){
		FacesContext.getCurrentInstance().addMessage(
				"informacao",
				new FacesMessage(FacesMessage.SEVERITY_INFO, message,
						org.apache.commons.lang.StringUtils.EMPTY));
	}
	
	/**
	 * Exclui o arquivo selecionado e atualiza a listagem.
	 */
	public void excluir(VideoFile selectedArquivo){
		serviceVideo.excluirVideoFile(selectedArquivo);
		find();
		showMessage("Video excluido com sucesso!");
	}
	
	

	public VideoFile getVideoSelecionado() {
		return videoSelecionado;
	}

	public void setVideoSelecionado(VideoFile videoSelecionado) {
		this.videoSelecionado = videoSelecionado;
	}

	public List<VideoFile> getArquivosList() {
		return listaVideosFile;
	}

	public void setArquivosList(List<VideoFile> arquivosList) {
		this.listaVideosFile = arquivosList;
	}

	public VideoFileService getServiceVideo() {
		return serviceVideo;
	}

	public void setServiceVideo(VideoFileService serviceVideo) {
		this.serviceVideo = serviceVideo;
	}

	public VideoFile getVideoFile() {
		return videoFile;
	}

	public void setVideoFile(VideoFile videoFile) {
		this.videoFile = videoFile;
	}

	public List<VideoFile> getListaVideosFile() {
		return listaVideosFile;
	}

	public void setListaVideosFile(List<VideoFile> listaVideosFile) {
		this.listaVideosFile = listaVideosFile;
	}

	public UploadedFile getUploadVideoFile() {
		return uploadVideoFile;
	}

	public void setUploadVideoFile(UploadedFile uploadVideoFile) {
		this.uploadVideoFile = uploadVideoFile;
	}

	public String getChaveVideo() {
		return chaveVideo;
	}

	public void setChaveVideo(String chaveVideo) {
		this.chaveVideo = chaveVideo;
	}


}
