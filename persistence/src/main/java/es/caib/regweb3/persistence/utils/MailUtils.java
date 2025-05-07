package es.caib.regweb3.persistence.utils;

import java.io.InputStream;
import java.util.Date;

import javax.activation.DataHandler;
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
	public static void enviaMail(String asunto, String mensajeTexto, InternetAddress addressFrom, Message.RecipientType type, String mailPara) throws Exception {
		enviaMail(asunto, mensajeTexto, addressFrom, type, mailPara, null);
	}
	
    public static void enviaMail(String asunto, String mensajeTexto, InternetAddress addressFrom, Message.RecipientType type, String mailPara, byte[] documento) throws Exception {

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
        mbp1.setText(mensajeTexto, "UTF-8");


        Multipart mp = new MimeMultipart();
        mp.addBodyPart(mbp1);

        if (documento != null) {
        	MimeBodyPart attachmentPart = new MimeBodyPart();
            ByteArrayDataSource source = new ByteArrayDataSource(documento, "application/pdf");
            attachmentPart.setDataHandler(new DataHandler(source));
            attachmentPart.setFileName("documento.pdf");
            mp.addBodyPart(attachmentPart);
        }

        //Añade el contenido al correo
        msg.setContent(mp);

        // Mandamos el mail
        Transport.send(msg);

    }
}
