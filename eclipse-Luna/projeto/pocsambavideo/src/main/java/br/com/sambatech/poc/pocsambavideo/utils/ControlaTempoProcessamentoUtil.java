package br.com.sambatech.poc.pocsambavideo.utils;

/**
 * Classe responsavel pelo controle de processamento na geração do url do video
 * @author eric.gomes - (14/07/2016)
 *
 */
public class ControlaTempoProcessamentoUtil {

	static final Long SECONDS_TO_WAIT = 10000L;
	
	/**
	 * Aguarda processamento
	 * Solucao paleativa
	 */
	public static void esperandoProcessamento() {
		try {
    		Thread.sleep(SECONDS_TO_WAIT);			
		
		} catch (InterruptedException e) {		
			e.printStackTrace();
		}		
	}
}
