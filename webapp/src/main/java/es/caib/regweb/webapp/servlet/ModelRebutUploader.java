/*
 * ModelOficioUploader.java
 *
 * Created on 3 de agosto de 2009, 11:36
 */

package es.caib.regweb.webapp.servlet;

import org.apache.commons.fileupload.*;

import org.apache.log4j.Logger;

import java.io.InputStream;
import java.sql.SQLException;

import es.caib.regweb.logic.interfaces.ModeloReciboFacade;
import es.caib.regweb.logic.util.ModeloReciboFacadeUtil;

/**
 *
 * @author  AROGEL
 */
public class ModelRebutUploader {
	
	private Logger log = null;
	
	public static int MAX_MB=3;
	private static int MAX_FILE_SIZE=MAX_MB*1024*1024;
	private boolean borrado=false;
	private boolean grabado=false;
	
	/** Creates a new instance of FileUploader */
	public ModelRebutUploader(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws SQLException, Exception {      
		log = Logger.getLogger(this.getClass());
		FileUpload fu = new FileUpload(new DefaultFileItemFactory());
		fu.setSizeMax(MAX_FILE_SIZE);// 40 kB
		//fu.setSizeThreshold(MAX_FILE_SIZE);// 40 kB
		
		//Cercam el EJB d'accés al repositori de models de rebuts
        ModeloReciboFacade mod = ModeloReciboFacadeUtil.getHome().create();

		
		java.util.List fileItems = fu.parseRequest(request);
		java.util.Iterator i = fileItems.iterator();		
		boolean borrar = false;
		String nombre = null;
		FileItem datos = null;
		while (i.hasNext()) {
			FileItem item = (FileItem) i.next();
			if (item.getFieldName().equals("borra_model") && item.getString().equals("s")){ 
				borrar=true;
			} else if (item.getFieldName().equals("nombre")) {
				nombre = item.getString();
			} else if (item.getSize() > 0) {
				datos = item;
			}
		}
		
		if (borrar) {
			try {
				mod.eliminar(nombre);
				borrado=true;
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else if (datos.getFieldName().equals("fitxer")) {
			try {
				InputStream uploadedStream = datos.getInputStream();
				java.io.ByteArrayOutputStream baos = new java.io.ByteArrayOutputStream() ;
				int iRead=0 ;
				byte[] baChunk = new byte[4096] ;
				while ( (iRead= uploadedStream.read(baChunk))>0)
					baos.write(baChunk,0,iRead) ;
				byte[] baResult = baos.toByteArray() ;
			    uploadedStream.close();

				mod.grabar(nombre, datos.getContentType(), baResult);
				grabado=true;
			} catch (SQLException e) {
				log.error("Excepci\u00f3 pujant fitxer model rebut");
				e.printStackTrace();
			}
		}
	
	} //Fin public ModelRebutUploader

	public boolean getBorrado() {
		return borrado;
	}

	public boolean getGrabado() {
		return grabado;
	}
}
