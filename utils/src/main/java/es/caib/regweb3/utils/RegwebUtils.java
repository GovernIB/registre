package es.caib.regweb3.utils;

import org.apache.commons.codec.binary.Base64;

import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * @author anadal
 */
public class RegwebUtils {


    public static <V, K> Map<V, K> invert(Map<K, V> map) {

        Map<V, K> inv = new HashMap<V, K>();

        for (Entry<K, V> entry : map.entrySet())
            inv.put(entry.getValue(), entry.getKey());

        return inv;
    }

    /**
     * Genera el Hash mediante SHA-256 del contenido del documento y lo codifica en base64
     *
     * @param documentoData
     * @return
     * @throws Exception
     */
    public static byte[] obtenerHash(byte[] documentoData) throws Exception {

        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] digest = md.digest(documentoData);

        return Base64.encodeBase64(digest);

    }
    
    /**
     * Convierte un hash en string
     *
     * @param documentoData
     * @return
     * @throws Exception
     */
    public static String obtenerStringHash(byte[] hash) throws Exception {
		// Decodificar el byte array guardado en bbdd
		hash = Base64.decodeBase64(hash);

		// Forma de pasar el byte array del hash a String
		StringBuilder hash256 = new StringBuilder();
		for (int i = 0; i < hash.length; i++) {
			hash256.append(Integer.toString((hash[i] & 0xff) + 0x100, 16).substring(1));
		}
		return hash256.toString();

    }
    
    /**
     * Comprueba si un array contiene la llave pasada por parámetro
     * 
     * @param <T>
     * @param array
     * @param key
     * 
     * @return
     */
    @SuppressWarnings("unchecked")
	public static <T> boolean contains(T[] array, T key) {
    	boolean exists = false;
    	if (array instanceof Integer[]) {
    		List<Integer> intList = (List<Integer>) new ArrayList<>(Arrays.asList(array));
    		if (intList.contains(key)) {
    			exists = true;
    		}
    	}
		return exists;
    }
    
}
