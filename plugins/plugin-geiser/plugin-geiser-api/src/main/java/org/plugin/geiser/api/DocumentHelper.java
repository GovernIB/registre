package org.plugin.geiser.api;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.codec.binary.Base64;

public class DocumentHelper {

	public static String getHash512Document(byte[] fitxerByte) {
		
		StringBuilder hash256 = new StringBuilder();
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-512");
		    byte[] data = md.digest(fitxerByte);
		    for(int i = 0; i < data.length; i++){
		    	hash256.append(Integer.toString((data[i] & 0xff) + 0x100, 16).substring(1));
		    }
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
	    return hash256.toString();
	}
	
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
