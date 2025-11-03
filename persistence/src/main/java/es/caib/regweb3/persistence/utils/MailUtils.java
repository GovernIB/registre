package es.caib.regweb3.persistence.utils;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;
import javax.naming.Context;
import javax.naming.InitialContext;
import java.util.Date;

/**
 * Created by mgonzalez on 14/05/2018.
 */
public class MailUtils {

	/**
     * Envia un email a un Usuario
     *
     * @param asunto
     *          Asunto del Mensaje
     * @param mensajeTexto
     *          Contenido del cerpo del correo a enviar
     * @param addressFrom
     *          Indica el remitente del mensaje
     * @param type
     *          Indica el con que de que tipo es el destinatario, Copia, Copia Oculta, etc
     * @param mailPara
     *          Email al que va dirigido el mensaje
	 * @param pdf 

     * @throws Exception
     */
	public static void enviaMail(String asunto, String mensajeTexto, InternetAddress addressFrom,
			Message.RecipientType type, String mailPara) throws Exception {
		enviaMail(asunto, mensajeTexto, addressFrom, type, mailPara, false, null);
	}
	
	public static void enviaMail(String asunto, String mensajeTexto, InternetAddress addressFrom,
			Message.RecipientType type, String mailPara, boolean html) throws Exception {
		enviaMail(asunto, mensajeTexto, addressFrom, type, mailPara, html, null);
	}

	public static void enviaMail(String asunto, String mensajeTexto, InternetAddress addressFrom,
			Message.RecipientType type, String mailPara, boolean html, DocumentoDto adjunto) throws Exception {

        Context ctx = new InitialContext();

        Session session = (javax.mail.Session) ctx.lookup("java:/es.caib.regweb3.mail");

        // Creamos el mensaje
        MimeMessage msg = new MimeMessage(session);

        // Indicamos el remitente
        msg.setFrom(addressFrom);

        // Indicamos el destinatario
        InternetAddress addressTo = new InternetAddress(mailPara);
        msg.setRecipient(type, addressTo);

        // Configuramos el asunto
        msg.setSubject(asunto, "UTF-8");
        msg.setSentDate(new Date());


        MimeBodyPart mbp1 = new MimeBodyPart();
        if (html) {
        	msg.setHeader("Content-Type", "text/html; charset=UTF-8");
        	mbp1.setContent(mensajeTexto, "text/html; charset=UTF-8");
        } else {
        	mbp1.setContent(mensajeTexto, "UTF-8");
        }
        
        Multipart mp = new MimeMultipart();
        mp.addBodyPart(mbp1);
        
        if (adjunto != null) {
        	String mimeType = adjunto.getMimeType();
        	if (mimeType == null || !mimeType.contains("/")) {
                mimeType = "application/octet-stream";
            }
        	
            MimeBodyPart mbp2 = new MimeBodyPart();
            DataSource dataSource = new ByteArrayDataSource(adjunto.getContenido(), mimeType);
            mbp2.setDataHandler(new DataHandler(dataSource));
            mbp2.setFileName(adjunto.getFilename());
            mp.addBodyPart(mbp2);
        }

        //Añade el contenido al correo
        msg.setContent(mp);

        // Mandamos el mail
        Transport.send(msg);

    }
}
