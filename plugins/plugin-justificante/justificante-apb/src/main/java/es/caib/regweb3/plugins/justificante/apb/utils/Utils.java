package es.caib.regweb3.plugins.justificante.apb.utils;

import org.apache.commons.codec.binary.Base64;

public class Utils {

	public static String decodeBase64Hash(byte[] hash) {
		// Decodificar el byte array guardado en bbdd
		hash = Base64.decodeBase64(hash);
		
		// Forma de pasar el byte array del hash a String
		StringBuilder hash256 = new StringBuilder();
		for(int i = 0; i < hash.length; i++){
	    	hash256.append(Integer.toString((hash[i] & 0xff) + 0x100, 16).substring(1));
	    }
		return hash256.toString();				
	}
}
