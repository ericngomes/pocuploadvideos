package br.com.sambatech.poc.pocsambavideo.model.builder;

import br.com.sambatech.poc.pocsambavideo.model.VideoFile;

/**
 * Builder <code>VideoFileBuilder</code> contem o carregamento de cada atributo do objeto VideoFIle
 * Builder for the {@link VideoFile}
 * @author ericgomes
 *
 */
public class VideoFileBuilder {
	
	private VideoFile videoFile = new VideoFile();
	
	public VideoFileBuilder videoFileKey(String key) {
		videoFile.setVideoFileKey(key);
		return this;
	}
	
	public VideoFileBuilder videoFileTamanho(Double size) {
		videoFile.setVideoFileTamanho(size);
		return this;
	}
	
	public VideoFileBuilder videoFileUrl(String key) {
		videoFile.setVideoFileUrl(key);
		return this;
	}
	
	public VideoFileBuilder defaultScenario() {
		this.videoFileKey("123");
		this.videoFileTamanho(1000000D);
		this.videoFileUrl("http://aws.pocsambavideo.com:8080/index.xhtml/video1");
		return this;
	}

	
	public VideoFile build() {
		return videoFile;
	}
}
