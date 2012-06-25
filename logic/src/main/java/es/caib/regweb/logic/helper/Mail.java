package es.caib.regweb.logic.helper;


import javax.ejb.SessionContext;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.log4j.Logger;



import javax.mail.Message;
import javax.mail.Session;
import javax.mail.SendFailedException;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;


import org.apache.log4j.Logger;

public class Mail {

	private static Logger log = Logger.getLogger(Helper.class);
	
	public static int FORMATO_TEXTO = 1;
	public static int FORMATO_HTML = 2;
	
	private static String SERVICIO_MAIL="java:/es.caib.regweb.mail";


    public static boolean enviarCorreu ( String emailTo, String subject, String msg) {
    	return enviarCorreu ( SERVICIO_MAIL,  emailTo,  subject,  msg, FORMATO_HTML); 
    }
    
    public static boolean enviarCorreu (String mailService, String emailTo, String subject, String msg, int formato) {
    	
    	try {    		
    		InitialContext ctx = new InitialContext();
    		Session sesion = (Session) ctx.lookup(mailService);
    		MimeMessage mensaje = new MimeMessage(sesion);    		
    		
    		mensaje.addRecipient(Message.RecipientType.TO, new InternetAddress(emailTo));      		
    		mensaje.setSubject(subject); 
    		
       		if(formato==FORMATO_TEXTO)
       			mensaje.setText(msg);
       		else
       			mensaje.setContent(msg, "text/html; charset=utf-8");
       		
			try {   
				Transport.send(mensaje); 
			} catch (SendFailedException ex) {
				log.error("Error enviant correu (To: " + emailTo + ") " + ex.toString()); 
				return false;
			} catch (Exception ex) {
    			log.error("Error enviant correu (To: " + emailTo + ") " + ex.toString()); 
    			return false;
    		} 

    	} catch (Exception ex) {
    		log.error("Error enviant correu (To: " + emailTo + ") " + ex.toString()); 
    		return false;
    	} 
    	log.debug("Correo enviado correctamente (To: '" + emailTo + "', subject: '"+subject+"')");
    	return true; 
    }
}
