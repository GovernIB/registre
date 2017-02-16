package es.caib.regweb3.sir.ws.api.manager;

import es.caib.regweb3.sir.core.excepcion.SIRException;
import es.caib.regweb3.sir.core.model.AsientoRegistralSir;
import es.caib.regweb3.sir.core.model.Errores;
import es.caib.regweb3.sir.core.model.TipoAnotacion;
import es.caib.regweb3.sir.ws.api.utils.FicheroIntercambio;
import es.caib.regweb3.sir.ws.api.wssir6b.RespuestaWS;
import es.caib.regweb3.sir.ws.api.wssir6b.WS_SIR6_BServiceLocator;
import es.caib.regweb3.sir.ws.api.wssir6b.WS_SIR6_B_PortType;
import es.caib.regweb3.utils.Configuracio;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by earrivi on 19/01/2016.
 */
public class EmisionManager {

    public final Logger log = Logger.getLogger(getClass());

    SicresXMLManager sicresXMLManager = new SicresXMLManager();


    /**
     *
     * @param asientoRegistralSir
     * @return
     */
    public String enviarFicheroIntercambio(AsientoRegistralSir asientoRegistralSir){

        try {

            // Comprobar el identificador de intercambio
            if (StringUtils.isBlank(asientoRegistralSir.getIdentificadorIntercambio())) {
                asientoRegistralSir.setIdentificadorIntercambio(generarIdentificadorIntercambio(asientoRegistralSir.getCodigoEntidadRegistralOrigen()));
            }

            // Establecer el tipo de anotación
            asientoRegistralSir.setTipoAnotacion(TipoAnotacion.ENVIO.getValue());
            asientoRegistralSir.setDecodificacionTipoAnotacion(TipoAnotacion.ENVIO.getName());

            String xml = sicresXMLManager.crearXMLFicheroIntercambioSICRES3(asientoRegistralSir);
            log.info("Xml Fichero Intercambio generado: " + xml);

            RespuestaWS respuesta = ws_sir6_b_recepcionFicheroDeAplicacion(xml);

            if (respuesta != null) {
                log.info("Respuesta: " + respuesta.getCodigo() + " - " + respuesta.getDescripcion());

                if (!Errores.OK.getValue().equals(respuesta.getCodigo())) {
                    log.error("Respuesta: " + respuesta.getCodigo() + " - " + respuesta.getDescripcion());
                    throw new SIRException("Error " + respuesta.getCodigo() + " - " + respuesta.getDescripcion());
                }
            }

            return asientoRegistralSir.getIdentificadorIntercambio();

        } catch (Exception e) {
            log.error("Error al enviar el fichero de intercambio: " + e);
            throw new SIRException("Error en la llamada al servicio de recepción de ficheros de datos de intercambio (WS_SIR6_B)");
        }
    }

    /**
     *
     * @param ficheroIntercambio
     */
    public void reenviarFicheroIntercambio(FicheroIntercambio ficheroIntercambio) {

    }

    /**
     *
     * @param ficheroIntercambio
     */
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


    /**
     *
     * @param xml
     * @return
     * @throws Exception
     */
    public static RespuestaWS ws_sir6_b_recepcionFicheroDeAplicacion(String xml) throws Exception {

        /*if (Configuracio.useDirectApiSir()) {
            String url = Configuracio.getSirServerBase() + "/WS_SIR6_B";
            return WS_SIR6_B_DirectApi.recepcionFicheroDeAplicacion(xml, url);
        } else {*/
            WS_SIR6_B_PortType ws_sir6_b = getWS_SIR6_B();
            return ws_sir6_b.recepcionFicheroDeAplicacion(xml);
        //}
    }

    /**
     * Genera el identificador de intercambio a partir del código de la oficina de origen
     *
     * @param codOficinaOrigen
     * @return
     * @throws Exception
     */
    protected String generarIdentificadorIntercambio(String codOficinaOrigen) {

        String identificador = "";
        SimpleDateFormat anyo = new SimpleDateFormat("yy"); // Just the year, with 2 digits

        identificador = codOficinaOrigen + "_" + anyo.format(Calendar.getInstance().getTime()) + "_" + getIdToken(); //todo: Añadir secuencia real


        return identificador;
    }

    /**
     * Calcula una cadena de ocho dígitos a partir del instante de tiempo actual.
     *
     * @return la cadena (String) de ocho digitos
     */
    private static final AtomicLong TIME_STAMP = new AtomicLong();

    private String getIdToken() {
        long now = System.currentTimeMillis();
        while (true) {
            long last = TIME_STAMP.get();
            if (now <= last)
                now = last + 1;
            if (TIME_STAMP.compareAndSet(last, now))
                break;
        }
        long unsignedValue = Long.toString(now).hashCode() & 0xffffffffl;
        String result = Long.toString(unsignedValue);
        if (result.length() > 8) {
            result = result.substring(result.length() - 8, result.length());
        } else {
            result = String.format("%08d", unsignedValue);
        }
        return result;
    }
}
