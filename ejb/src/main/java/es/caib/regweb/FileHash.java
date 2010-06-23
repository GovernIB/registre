package es.caib.regweb;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;



/**
 * Clase de utillidad para obtener el código hash de un fichero
 * @author Jesús Reyes (3dígits)
 * @version 1.0
 */
public class FileHash 
{
	
	/**
	 * Obtiene un array de bytes con el código hash de un InputStream
	 * @throws java.security.NoSuchAlgorithmException
	 * @throws java.io.IOException
	 * @return byte[] con el hash de la entrada
	 * @param algName nombre del algoritmo hashing
	 * @param inputStream <code>InputStream</code> del que se quiere obtener el hashing
	 */
	public static byte[] makeHashing(InputStream inputStream, String algName) throws IOException,NoSuchAlgorithmException
	{
		MessageDigest md = null;
		byte[] buffer = new byte[1024];
		
		md = MessageDigest.getInstance(algName); 
		int n;
		while ((n=inputStream.read(buffer))!=-1)
		{
			md.update(buffer,0,n);
		}
		inputStream.close();
		byte raw[] = md.digest(); // Obtención del resumen de mensaje
		return raw;
		
	}
	
	/**
	 * Obtiene un array de bytes con el código hash de un fichero
	 * @throws java.security.NoSuchAlgorithmException
	 * @throws java.io.IOException
	 * @return byte[] con el hash de la entrada
	 * @param algName nombre del algoritmo hashing
	 * @param fileName nombre del fichero del que se quiere obtener el hashing
	 */
	
	public static byte[] makeHashing(String fileName, String algName) throws IOException,NoSuchAlgorithmException
	{
		return makeHashing(new FileInputStream(fileName),algName);
	}
}