package es.caib.regweb3.sir.ejb;

import es.caib.regweb3.model.RegistroSir;
import es.caib.regweb3.model.sir.Errores;
import es.caib.regweb3.sir.core.excepcion.SIRException;
import es.caib.regweb3.sir.utils.Sicres3XML;
import es.caib.regweb3.sir.ws.api.wssir6b.RespuestaWS;
import es.caib.regweb3.sir.ws.api.wssir6b.WS_SIR6_BServiceLocator;
import es.caib.regweb3.sir.ws.api.wssir6b.WS_SIR6_B_PortType;
import es.caib.regweb3.utils.Configuracio;
import org.dom4j.Document;
import org.fundaciobit.genapp.common.i18n.I18NException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import java.net.URL;

/**
 * Ejb para la gestión de la Emisión de Ficheros de Intercambios SICRES3 hacia un nodo distribuido
 *
 */
@Stateless(name = "EmisionEJB")
@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
public class EmisionBean implements EmisionLocal{

    public final Logger log = LoggerFactory.getLogger(getClass());

    private Sicres3XML sicres3XML = new Sicres3XML();


    /**
     * Envío de un RegistroSir en formato SICRES3 a un nodo distribuido
     * @param registroSir

     * @return
     */
    public void enviarFicheroIntercambio(RegistroSir registroSir) throws I18NException {

        try {
            log.info("Enviando el Registro " + registroSir.getIdentificadorIntercambio()+ " al nodo distribuido: " + registroSir.getCodigoEntidadRegistralDestino() +" - " + registroSir.getDecodificacionEntidadRegistralDestino());

            // Enviamos el RegistroSir al nodo distribuido.
            enviar(registroSir);


        } catch (Exception e) {
            log.error("Error al enviar el fichero de intercambio: " + e);
            e.printStackTrace();
            throw new SIRException("Error en la llamada al servicio de recepción de ficheros de datos de intercambio (WS_SIR6_B)");
        }

    }

    /**
     * Reenvío de un RegistroSir en formato SICRES3 a un nodo distribuido
     * @param registroSir
     */
    public void reenviarFicheroIntercambio(RegistroSir registroSir)  throws I18NException {

        log.info("Reenviando el Registro " + registroSir.getIdentificadorIntercambio()+ " al nodo distribuido: " + registroSir.getCodigoEntidadRegistralDestino() +" - " + registroSir.getDecodificacionEntidadRegistralDestino());

        // Reenviamos el RegistroSir al nodo distribuido.
        enviar(registroSir);

    }

    /**
     * Rechazo de un RegistroSir en formato SICRES3 a un nodo distribuido
     * @param registroSir
     */
    public void rechazarFicheroIntercambio(RegistroSir registroSir) throws I18NException{

        log.info("Rechazando el Registro " + registroSir.getIdentificadorIntercambio()+ " al nodo distribuido: " + registroSir.getCodigoEntidadRegistralDestino() +" - " + registroSir.getDecodificacionEntidadRegistralDestino());

        // Rechazamos el RegistroSir al nodo distribuido.
        enviar(registroSir);

    }

    /**
     * Envia un RegistroSir a un nodo distribuido creando previamente el fichero de intercambio
     * @param registroSir
     */
    private void enviar(RegistroSir registroSir){

        try{
        // Creamos el xml de intercambio
        Document doc = sicres3XML.crearXMLFicheroIntercambioSICRES3(registroSir);
        /*log.debug("-----------------------------------------------------------------------------------------");
        log.debug("");
        log.debug("Xml Fichero Intercambio generado: " + doc.asXML());
        log.debug("");
        log.debug("-----------------------------------------------------------------------------------------");*/

        // Enviamos el Fichero de intercambio
        RespuestaWS respuesta = ws_sir6_b_recepcionFicheroDeAplicacion(doc.asXML());

        if (respuesta != null) {

            if (Errores.OK.getValue().equals(respuesta.getCodigo())) {

                //log.info("RegistroSir enviado correctamente: " + respuesta.getCodigo() + " - " + respuesta.getDescripcion());
            }else{
                log.error("Errro en el envio: " + respuesta.getCodigo() + " - " + respuesta.getDescripcion());
                throw new SIRException("Error " + respuesta.getCodigo() + " - " + respuesta.getDescripcion());
            }
        }

        } catch (Exception e) {
            log.error("Error al enviar el fichero de intercambio: " + e);
            throw new SIRException("Error en la llamada al servicio de recepción de ficheros de datos de intercambio (WS_SIR6_B)");
        }
    }

    /**
     *
     * @param xml
     * @return
     * @throws Exception
     */
    private RespuestaWS ws_sir6_b_recepcionFicheroDeAplicacion(String xml) throws Exception {

        WS_SIR6_B_PortType ws_sir6_b = getWS_SIR6_B();

        return ws_sir6_b.recepcionFicheroDeAplicacion(xml);

    }

    /**
     * @return
     * @throws Exception
     */
    private WS_SIR6_B_PortType getWS_SIR6_B() throws Exception {
        WS_SIR6_BServiceLocator locator = new WS_SIR6_BServiceLocator();
        URL url = new URL(Configuracio.getSirServerBase() + "/WS_SIR6_B");

        return  locator.getWS_SIR6_B(url);
    }

}
