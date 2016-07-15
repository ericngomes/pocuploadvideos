package br.com.sambatech.poc.pocsambavideo.model;

import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * Objeto arquivo do usuario
 * 
 * @author eric.gomes - (12/07/2016)
 *
 */
@ToString(of="videoFileKey")
@EqualsAndHashCode(of="videoFileKey")
public class VideoFile {

	private String videoFileKey;
	private String videoFileUrl;
	private Double videoFileTamanho;
	
	
	public String getVideoFileKey() {
		return videoFileKey;
	}
	public void setVideoFileKey(String videoFileKey) {
		this.videoFileKey = videoFileKey;
	}
	public String getVideoFileUrl() {
		return videoFileUrl;
	}
	public void setVideoFileUrl(String videoFileUrl) {
		this.videoFileUrl = videoFileUrl;
	}
	public Double getVideoFileTamanho() {
		return videoFileTamanho;
	}
	public void setVideoFileTamanho(Double videoFileTamanho) {
		this.videoFileTamanho = videoFileTamanho;
	}
}
