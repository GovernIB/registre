package es.caib.regweb3.utils;


import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;
import javax.naming.Context;
import javax.naming.InitialContext;
import java.util.Date;
import java.util.List;

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

     * @throws Exception
     */
    public static void enviaMail(String asunto, String mensajeTexto, InternetAddress addressFrom, Message.RecipientType type, String mailPara) throws Exception {

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


        //Añade el contenido al correo
        msg.setContent(mp);

        // Mandamos el mail
        Transport.send(msg);

    }


    public static void enviarMailConAdjuntos(List<Attachment> attachments, String emailPara, String asunto, String cuerpo) throws Exception{

        InternetAddress addressFrom = new InternetAddress("noresponder@regweb3.com", "REGWEB3");

        Context ctx = new InitialContext();

        Session session = (Session) ctx.lookup("java:/es.caib.regweb3.mail");

        Message message = new MimeMessage(session);


        // Set From: header field of the header.
        message.setFrom(addressFrom);

        // Set To: header field of the header.
        InternetAddress addressTo = new InternetAddress(emailPara);
        message.setRecipient(Message.RecipientType.TO, addressTo);

        // Set Subject: header field
        message.setSubject(asunto);


        // Create the message part
        BodyPart messageBodyPart = new MimeBodyPart();

        // Now set the actual message
        messageBodyPart.setContent(cuerpo, "text/html");

        // Create a multipart message
        Multipart multipart = new MimeMultipart();

        // Set text message part
        multipart.addBodyPart(messageBodyPart);



        //Añadimos los adjuntos que nos pasan
        for (Attachment attachment : attachments) {
            DataSource dataSource = new ByteArrayDataSource(attachment.getData(), attachment.getMime());
            messageBodyPart = new MimeBodyPart();
            messageBodyPart.setDataHandler(new DataHandler(dataSource));
            messageBodyPart.setFileName(attachment.getFilename());
            multipart.addBodyPart(messageBodyPart);
        }



        // Send the complete message parts
        message.setContent(multipart);


        // Send message
        Transport.send(message);

    }
}
