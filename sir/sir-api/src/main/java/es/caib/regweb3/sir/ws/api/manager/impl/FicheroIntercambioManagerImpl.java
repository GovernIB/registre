package es.caib.regweb3.sir.ws.api.manager.impl;

import es.caib.regweb3.model.RegistroEntrada;
import es.caib.regweb3.sir.core.excepcion.SIRException;
import es.caib.regweb3.sir.core.model.Errores;
import es.caib.regweb3.sir.ws.api.manager.FicheroIntercambioManager;
import es.caib.regweb3.sir.ws.api.manager.SicresXMLManager;
import es.caib.regweb3.sir.ws.api.utils.FicheroIntercambio;
import es.caib.regweb3.sir.ws.api.wssir6b.RespuestaWS;
import es.caib.regweb3.sir.ws.api.wssir6b.WS_SIR6_BServiceLocator;
import es.caib.regweb3.sir.ws.api.wssir6b.WS_SIR6_B_PortType;
import es.caib.regweb3.utils.Configuracio;
import org.apache.log4j.Logger;

import java.net.URL;

/**
 * Created by earrivi on 19/01/2016.
 */
public class FicheroIntercambioManagerImpl implements FicheroIntercambioManager {

    public final Logger log = Logger.getLogger(getClass());

    SicresXMLManager sicresXMLManager = new SicresXMLManagerImpl();


    @Override
    public void enviarFicheroIntercambio(RegistroEntrada registroEntrada) {

        String xml = sicresXMLManager.crearXMLFicheroIntercambioSICRES3(registroEntrada);
        log.info("Xml Fichero Intercambio: " + xml);

        try {
            RespuestaWS respuesta = ws_sir6_b_recepcionFicheroDeAplicacion(xml);

            if (respuesta != null) {
                log.info("Respuesta: " + respuesta.getCodigo() + " - " + respuesta.getDescripcion());

                if (!Errores.OK.getValue().equals(respuesta.getCodigo())) {
                    log.error("Respuesta: " + respuesta.getCodigo() + " - " + respuesta.getDescripcion());
                    throw new SIRException("Error " + respuesta.getCodigo() + " - " + respuesta.getDescripcion());
                }
            }

        } catch (Exception e) {
            log.error("Error al enviar el fichero de intercambio: " + e);
            throw new SIRException("Error en la llamada al servicio de recepción de ficheros de datos de intercambio (WS_SIR6_B)");
        }

    }

    @Override
    public void reenviarFicheroIntercambio(FicheroIntercambio ficheroIntercambio) {

    }

    @Override
    public void rechazarFicheroIntercambio(FicheroIntercambio ficheroIntercambio) {

    }

    /**
     * @return
     * @throws Exception
     */
    public static WS_SIR6_B_PortType getWS_SIR6_B() throws Exception {
        WS_SIR6_BServiceLocator locator = new WS_SIR6_BServiceLocator();
        URL url = new URL(Configuracio.getSirServerBase() + "/WS_SIR6_B");
        WS_SIR6_B_PortType ws_sir6_b = locator.getWS_SIR6_B(url);
        return ws_sir6_b;
    }


    public static RespuestaWS ws_sir6_b_recepcionFicheroDeAplicacion(String xml) throws Exception {

        /*if (Configuracio.useDirectApiSir()) {
            String url = Configuracio.getSirServerBase() + "/WS_SIR6_B";
            return WS_SIR6_B_DirectApi.recepcionFicheroDeAplicacion(xml, url);
        } else {*/
            WS_SIR6_B_PortType ws_sir6_b = getWS_SIR6_B();
            return ws_sir6_b.recepcionFicheroDeAplicacion(xml);
        //}
    }
}
