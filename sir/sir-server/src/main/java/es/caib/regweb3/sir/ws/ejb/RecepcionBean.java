package es.caib.regweb3.sir.ws.ejb;

import es.caib.regweb3.persistence.ejb.WebServicesMethodsLocal;
import es.caib.regweb3.sir.core.excepcion.ServiceException;
import es.caib.regweb3.sir.core.excepcion.ValidacionException;
import es.caib.regweb3.sir.core.model.Errores;
import es.caib.regweb3.sir.core.utils.FicheroIntercambio;
import es.caib.regweb3.sir.core.utils.Mensaje;
import es.caib.regweb3.sir.ejb.MensajeLocal;
import es.caib.regweb3.sir.utils.Sicres3XML;
import es.caib.regweb3.sir.utils.XPathReaderUtil;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.w3c.dom.Node;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.xml.xpath.XPathConstants;

import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;

/**
 * Ejb para la gestión de la Recepción de Ficheros de Intercambios y Mensajes de Dtos de Control
 * en formato SICRES3 desde un nodo distribuido
 */
@Stateless(name = "RecepcionEJB")
public class RecepcionBean implements RecepcionLocal{

    private final Logger log = Logger.getLogger(getClass());

    @EJB(name = "MensajeEJB")
    private MensajeLocal mensajeEjb;

    private Sicres3XML sicres3XML = new Sicres3XML();


    private String errorGenerico = Errores.ERROR_0065.getValue(); // ERROR GENÉRICO: ERROR_EN_EL_ASIENTO
    private final String Codigo_Entidad_Registral_Destino_Xpath = "//Codigo_Entidad_Registral_Destino/text()";
    private final String Codigo_Entidad_Registral_Origen_Xpath = "//Codigo_Entidad_Registral_Origen/text()";
    private final String Identificador_Intercambio_Xpath = "//Identificador_Intercambio/text()";


    /**
     * Recibe un fichero de intercambio en formato SICRES3 desde un nodo distribuido
     *
     * @param xmlFicheroIntercambio
     *
     */
    public void recibirFicheroIntercambio(String xmlFicheroIntercambio, WebServicesMethodsLocal webServicesMethodsEjb) throws Exception {

        FicheroIntercambio ficheroIntercambio = null;
        String descripcionError = null;

        try {

            assert_hasText(xmlFicheroIntercambio, "'xmlFicheroIntercambio' no puede estar vacio");

            // Convertimos y validamos el xml recibido en un FicheroIntercambio mediate xsd FicheroIntercambio.xsd
            ficheroIntercambio = sicres3XML.parseXMLFicheroIntercambio(xmlFicheroIntercambio);

            // Validamos el Fichero de Intercambio creado a partir del xml recibido
            try {
                sicres3XML.validarFicheroIntercambio(ficheroIntercambio);
            } catch (IllegalArgumentException e) {
                log.error("Se produjo un error de validacion del xml recibido: " + e.getMessage());
                throw new ValidacionException(Errores.ERROR_0037, e);
            }

            // Creamos el RegistroSir a partir del xml recibido y validado
            webServicesMethodsEjb.recibirFicheroIntercambio(ficheroIntercambio);

            // Si ha ido bien, enviamos el ACK
            mensajeEjb.enviarACK(ficheroIntercambio);

        } catch (RuntimeException e) {

            // Si el error es de Validación, obtenemos su código de error
            if (e instanceof ValidacionException) {
                Errores errorValidacion = ((ValidacionException) e).getErrorValidacion();
                descripcionError = ((ValidacionException) e).getErrorException().getMessage();
                if (errorValidacion != null) {
                    errorGenerico = errorValidacion.getValue();
                }
                log.info("Error de Valicacion: " + ((ValidacionException) e).getErrorException().getMessage());
            }else{
                log.info("Error al recibir el fichero de intercambio", e);
                descripcionError = e.getMessage();
            }

            // Enviamos el mensaje de error
            try {
                // Intentamos parsear el xml los 3 campos necesarios para mensaje de error
                Mensaje mensaje = parserForError(xmlFicheroIntercambio);

                // Si el código de error es el de duplicado, se vuelve a enviar un ACK
                if (Errores.ERROR_0205.getValue().equals(errorGenerico)) {
                    mensajeEjb.enviarACK(mensaje);

                    // Enviar mensaje de error, sino tiene nada que ver con los campos CodigoEntidad
                } else if (!Errores.ERROR_COD_ENTIDAD_INVALIDO.getValue().equals(errorGenerico)) {
                    mensajeEjb.enviarMensajeError(mensaje, errorGenerico, descripcionError);
                }

            } catch (RuntimeException ex) {
                // Comprobamos una posible excepción al no disponer de los datos necesarios para enviar los mensajes
                log.info("No se ha podido enviar el mensaje de error debido a una excepción producida, podría ser debido a la falta de campos requeridos para el envío del mismo:", ex);
            }

            throw e;

        }
    }

    /**
     * Obtiene del xml de intercambio los campos necesarios para crear un mensaje de error
     * @param xml
     * @return
     */
    private Mensaje parserForError(String xml) {

        log.debug("Intentamos parsear el xml recibido para enviar el mensaje de Error");

        Mensaje mensaje = new Mensaje();

        XPathReaderUtil reader = null;
        // procesamos el xml para procesar las peticiones xpath
        try {
            reader = new XPathReaderUtil(new ByteArrayInputStream(xml.getBytes("UTF-8")));
        } catch (UnsupportedEncodingException e) {
            log.error("Imposible parsear el xml recibido:"+xml+" excepción "+e.getLocalizedMessage());
            throw new RuntimeException(e);
        }

        Node codigoEntidadRegistralDestino = (Node)reader.read(Codigo_Entidad_Registral_Destino_Xpath, XPathConstants.NODE);
        Node codigoEntidadRegistralOrigen = (Node)reader.read(Codigo_Entidad_Registral_Origen_Xpath, XPathConstants.NODE);
        Node identificadorIntercambio = (Node)reader.read(Identificador_Intercambio_Xpath, XPathConstants.NODE);

        //log.info("CodigoEntidadRegistralDestino: " + codigoEntidadRegistralDestino.getNodeValue());
        //log.info("CodigoEntidadRegistralOrigen: " + codigoEntidadRegistralOrigen.getNodeValue());
        //log.info("IdentificadorIntercambio: " + identificadorIntercambio.getNodeValue());

        mensaje.setCodigoEntidadRegistralDestino(codigoEntidadRegistralDestino.getNodeValue());
        mensaje.setCodigoEntidadRegistralOrigen(codigoEntidadRegistralOrigen.getNodeValue());
        mensaje.setIdentificadorIntercambio(identificadorIntercambio.getNodeValue());

        return mensaje;
    }

    /**
     * Recibe un fichero de datos de control del nodo distribuido asociado.
     *
     * @param xmlMensaje XML con la información del mensaje en formato SICRES 3.0.
     */
    public void recibirMensajeDatosControl(String xmlMensaje, WebServicesMethodsLocal webServicesMethodsEjb){

        // Parseamos el mensaje xml
        Mensaje mensaje = sicres3XML.parseXMLMensaje(xmlMensaje);

        // Validamos el mensaje recibido
        sicres3XML.validarMensaje(mensaje);

        log.info("Recibiendo mensade de control: " + mensaje.getTipoMensaje() + " - " + mensaje.getIdentificadorIntercambio());

        try {

            webServicesMethodsEjb.recibirMensajeDatosControl(mensaje);

        } catch (Exception e) {
            e.printStackTrace();
            throw new ServiceException(Errores.ERROR_INESPERADO,e);
        }

        log.info("Mensaje recibido y procesado correctamente: " + mensaje.getIdentificadorIntercambio());

    }
    
    
    
    /**
     * Assert that the given String has valid text content; that is, it must not
     * be {@code null} and must contain at least one non-whitespace character.
     * <pre class="code">assert_hasText(name, "'name' must not be empty");</pre>
     * @param text the String to check
     * @param message the exception message to use if the assertion fails
     * @see StringUtils#hasText
     */
    public static void assert_hasText(String text, String message) {
      if (!hasText(text)) {
        throw new IllegalArgumentException(message);
      }
    }
    
    
    public static boolean hasText(String str) {
      if (!(str != null && str.length() > 0)) {
        return false;
      }
      int strLen = str.length();
      for (int i = 0; i < strLen; i++) {
        if (!Character.isWhitespace(str.charAt(i))) {
          return true;
        }
      }
      return false;
    }
    
    
}
