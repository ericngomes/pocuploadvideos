/**
 * 
 */
package br.com.sambatech.poc.pocsambavideo.utils;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Paramentros da Configuracao
 * 
 * @author eric.gomes - (14/07/2016)
 *
 */
public class FormataParametrosUtil {

	/** The bundle. */
	private static ResourceBundle bundle;

	static{
		bundle = ResourceBundle.getBundle("application", new Locale("pt_br"));		
	}
	
	private static String formata(String mensagem, Object... parametros){
		return MessageFormat.format(mensagem, parametros); 
	}
	/**
	 * Recupera.
	 *
	 * @param chaveVideo the chave
	 * @param parametros the parametros
	 * @return the string
	 */
	public static String recupera(String chaveVideo, Object... parametros){
		return formata(bundle.getString(chaveVideo), parametros);
	}
	
}
