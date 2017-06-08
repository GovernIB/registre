package es.caib.regweb3.sir.ws.ejb;

import es.caib.regweb3.persistence.ejb.WebServicesMethodsLocal;
import es.caib.regweb3.sir.core.excepcion.ServiceException;
import es.caib.regweb3.sir.core.excepcion.ValidacionException;
import es.caib.regweb3.sir.core.model.Errores;
import es.caib.regweb3.sir.core.utils.Assert;
import es.caib.regweb3.sir.core.utils.FicheroIntercambio;
import es.caib.regweb3.sir.core.utils.Mensaje;
import es.caib.regweb3.sir.ejb.MensajeLocal;
import es.caib.regweb3.sir.utils.Sicres3XML;
import es.caib.regweb3.sir.utils.XPathReaderUtil;
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

        try {

            Assert.hasText(xmlFicheroIntercambio, "El xml del FicheroIntercambio no puede estar vacio");

            // Convertimos y validamos el xml recibido en un FicheroIntercambio mediate xsd FicheroIntercambio.xsd
            ficheroIntercambio = sicres3XML.parseXMLFicheroIntercambio(xmlFicheroIntercambio);

            // Validamos el Fichero de Intercambio creado a partir del xml recibido
            try {
                sicres3XML.validarFicheroIntercambio(ficheroIntercambio, webServicesMethodsEjb.getObtenerOficinasService(), webServicesMethodsEjb.getObtenerUnidadesService());
            } catch (IllegalArgumentException e) {
                log.info("Se produjo un error de validacion del xml recibido: " + e.getMessage());
                throw new ValidacionException(Errores.ERROR_0037, e);
            }

            // Creamos el RegistroSir a partir del xml recibido y validado
            webServicesMethodsEjb.recibirFicheroIntercambio(ficheroIntercambio);

            // Si ha ido bien, enviamos el ACK
            mensajeEjb.enviarACK(ficheroIntercambio);

        }catch (ValidacionException e) {
            log.info("Error de validacion: " + e.getErrorException().getMessage());
            Errores errorValidacion = e.getErrorValidacion();
            String descripcionError = e.getErrorException().getMessage();

            // Si el error de validación no afecta a los campos requeridos para enviar el mensaje de error
            if (!Errores.ERROR_COD_ENTIDAD_INVALIDO.getValue().equals(errorValidacion.getValue())) {

                enviarMensajeError(xmlFicheroIntercambio, errorValidacion.getValue(), descripcionError);

            }else{
                log.info("El error de validacion afecta a campos del segmento De_Destino y no permite componer el mensaje de error");
            }

            throw e;

        } catch (RuntimeException e) {
            //Error inesperado, intentamos enviar el mensaje de error
            //NO ENVIAMOS UN MENSAJE DE ERROR DE CONTROL, PORQUÉ NO SE DEBE A LAS VALIDACIONES
            log.info("Error inesperado recibiendo el Fichero de Intercambio, no enviamos un mensaje de control de error", e);

            //enviarMensajeError(xmlFicheroIntercambio, errorGenerico, e.getMessage());

            throw e;
        }
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
            throw new ServiceException(Errores.ERROR_NO_CONTROLADO,e);
        }

        log.info("Mensaje recibido y procesado correctamente: " + mensaje.getIdentificadorIntercambio());
    }


    /**
     *
     * @param xmlFicheroIntercambio
     * @param codigoError
     * @param descripcionError
     */
    private void enviarMensajeError(String xmlFicheroIntercambio, String codigoError, String descripcionError){

        try {
            // Intentamos parsear el xml los 3 campos necesarios para mensaje de error
            Mensaje mensaje = parserForError(xmlFicheroIntercambio);
            mensajeEjb.enviarMensajeError(mensaje, codigoError, descripcionError);

        }catch (RuntimeException ex) {
            // Comprobamos una posible excepción al no disponer de los datos necesarios para enviar los mensajes
            log.info("No es posible enviar el mensaje de Error, posiblemente por falta de campos minimos requeridos", ex);
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

        mensaje.setCodigoEntidadRegistralDestino(codigoEntidadRegistralDestino.getNodeValue());
        mensaje.setCodigoEntidadRegistralOrigen(codigoEntidadRegistralOrigen.getNodeValue());
        mensaje.setIdentificadorIntercambio(identificadorIntercambio.getNodeValue());

        return mensaje;
    }

}
