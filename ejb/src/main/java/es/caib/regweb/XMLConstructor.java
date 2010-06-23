package es.caib.regweb;

import java.util.Properties;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import java.io.FileWriter;
import java.io.*;
import java.util.Vector;
import java.util.Iterator;
import sun.misc.BASE64Encoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


import javax.naming.InitialContext;
import javax.naming.Context;
import javax.naming.NamingException;


/**
 * @version 0.0
 * @autor Jesús Reyes (3dígits)
 * 
 * Construye un acuse de recibo de entrada en el registro en formato XML a partir de los 
 * datos básicos de la entrada
 */

class XMLConstructor 
{
	protected XMLConstructor()
	{
	}
	
	
	
	/**
	 * Construye un documento XML (de la clase Document de dom4j) a partir de los parámetros de entrada
	 * @return documento XML obtenido al construir el recibo
	 * @param documentName Vector que contiene los nombres de los documentos electrónicos presentados en el registro. Puede ser null
	 * @param documentHash Vector que contiene el hash de cada uno de los documentos electrónicos presentados en el registro. Puede ser null
	 * @param hashingAlg Nombre del algoritmo de hashing utilizado al crear el hash de los documentos electrónicos presentados en el resigstro. Puede ser null o cadena vacía
	 * @param oficina Nombre de la oficina de registro
	 * @param data Fecha de la entrada del registro
	 * @param num Número de entrada del registro
	 */
	protected Document createDocument(String num, String data, String oficina, String hashingAlg, Vector documentHash, Vector documentName) {
		Document document = DocumentHelper.createDocument();
		boolean hashingAlgExists = false;
		Element root = document.addElement( "registreEntradaCAIB" );
		
		Element eNum = root.addElement( "num" )
		.addText( num );        
		Element eData = root.addElement( "data" )
		.addText(data);
		Element eOficina = root.addElement ("oficina")
		.addText(oficina);
		if (hashingAlg!=null)
		{
			if (!hashingAlg.equalsIgnoreCase(""))
			{
				Element eHashingAlg = root.addElement ("algoritmeHashing")
				.addText (hashingAlg);
				hashingAlgExists = true;
			}
		}
		
		byte[] hashDoc = null;
		String docName = "";
		if (documentHash!=null && hashingAlgExists)
		{
			Iterator itr = documentHash.iterator();
			Iterator itr2 = null;
			if  (documentName!=null)
			{
				itr2 = documentName.iterator();
			}
			while (itr.hasNext())
			{
				hashDoc = (byte[]) itr.next();
				docName = (itr2==null) ? "": (String) itr2.next();
				Element eHashingDoc = root.addElement("document")
				.addAttribute("nom",docName)
				.addText(new BASE64Encoder().encode(hashDoc));   
			}
		}
		return document;
	}
	
	
	/**
	 * Construye un documento XML a partir d los parámetros de entrada y lo devuelve a través de un OutputStream
	 * @param xmlDocument stream conteniendo el documento XML construido
	 * @param documentName Vector que contiene los nombres de los documentos electrónicos presentados en el registro
	 * @param documentHash Vector que contiene el hash de cada uno de los documentos electrónicos presentados en el registro
	 * @param hashingAlg Nombre del algoritmo de hashing utilizado al crear el hash de los documentos electrónicos presentados en el resigstro
	 * @param oficina Nombre de la oficina de registro
	 * @param data Fecha de la entrada del registro
	 * @param num Número de entrada del registro
	 */
	protected void createDocument(String num, String data, String oficina, String hashingAlg, Vector documentHash, Vector documentName, OutputStream xmlDocument) 
	{
		Document document =  createDocument( num,  data,  oficina,  hashingAlg,  documentHash,  documentName);
		try
		{
			XMLWriter output = new XMLWriter(xmlDocument,new OutputFormat("   ", true, "UTF-8") );
			output.write( document );
			output.close();
		}
		catch (UnsupportedEncodingException ex)
		{
			
		}
		catch (IOException io)
		{
			
		}
		
	}
	
	
	
	
	
	
	
}