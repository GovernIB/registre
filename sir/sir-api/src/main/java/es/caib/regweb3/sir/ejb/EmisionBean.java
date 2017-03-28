package es.caib.regweb3.sir.ejb;

import es.caib.regweb3.model.*;
import es.caib.regweb3.model.utils.EstadoAsientoRegistralSir;
import es.caib.regweb3.persistence.ejb.AsientoRegistralSirLocal;
import es.caib.regweb3.persistence.ejb.SirLocal;
import es.caib.regweb3.sir.core.excepcion.SIRException;
import es.caib.regweb3.sir.core.model.Errores;
import es.caib.regweb3.sir.core.utils.FicheroIntercambio;
import es.caib.regweb3.sir.utils.Sicres3XML;
import es.caib.regweb3.sir.ws.api.wssir6b.RespuestaWS;
import es.caib.regweb3.sir.ws.api.wssir6b.WS_SIR6_BServiceLocator;
import es.caib.regweb3.sir.ws.api.wssir6b.WS_SIR6_B_PortType;
import es.caib.regweb3.utils.Configuracio;
import org.apache.log4j.Logger;
import org.fundaciobit.genapp.common.i18n.I18NException;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import java.net.URL;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Ejb para la gestión de la Emisión de Ficheros de Intercambios SICRES3 hacia un nodo distribuido
 *
 */
@Stateless(name = "EmisionEJB")
public class EmisionBean implements EmisionLocal{

    public final Logger log = Logger.getLogger(getClass());

    @EJB(mappedName = "regweb3/SirEJB/local")
    public SirLocal sirEjb;

    @EJB public AsientoRegistralSirLocal asientoRegistralSirEjb;

    Sicres3XML sicres3XML = new Sicres3XML();


    /**
     * Envío de un AsientoRegistral en formato SICRES3 a un nodo distribuido
     * @param tipoRegistro
     * @param idRegistro
     * @param codigoEntidadRegistralDestino
     * @param denominacionEntidadRegistralDestino
     * @param oficinaActiva
     * @param usuario
     * @param idLibro
     * @return
     */
    public OficioRemision enviarFicheroIntercambio(String tipoRegistro, Long idRegistro, String codigoEntidadRegistralDestino, String denominacionEntidadRegistralDestino, Oficina oficinaActiva, UsuarioEntidad usuario, Long idLibro){

        OficioRemision oficioRemision = null;

        try {

            oficioRemision = sirEjb.enviarFicheroIntercambio(tipoRegistro, idRegistro, codigoEntidadRegistralDestino, denominacionEntidadRegistralDestino, oficinaActiva, usuario,idLibro);

            AsientoRegistralSir asientoRegistralSir = oficioRemision.getAsientoRegistralSir();

            enviar(asientoRegistralSir, EstadoAsientoRegistralSir.ENVIADO);


        } catch (Exception e) {
            log.error("Error al enviar el fichero de intercambio: " + e);
            throw new SIRException("Error en la llamada al servicio de recepción de ficheros de datos de intercambio (WS_SIR6_B)");
        } catch (I18NException e) {
            log.error("Error al enviar el fichero de intercambio: " + e);
            throw new SIRException("Error en la llamada al servicio de recepción de ficheros de datos de intercambio (WS_SIR6_B)");
        }

        return oficioRemision;
    }

    /**
     * Reenvío de un AsientoRegistral en formato SICRES3 a un nodo distribuido
     * @param asientoRegistralSir
     */
    public void reenviarFicheroIntercambio(AsientoRegistralSir asientoRegistralSir, Oficina oficinaReenvio, Oficina oficinaActiva, Usuario usuario)  throws Exception {

        //Preparamos el asiento registral para su reenvio
        asientoRegistralSir =  sirEjb.reenviarAsientoRegistralSir(asientoRegistralSir, oficinaReenvio, oficinaActiva, usuario);

        //Enviamos el asiento registral al nodo distribuido.
        enviar(asientoRegistralSir, EstadoAsientoRegistralSir.REENVIADO);
    }

    /**
     * Rechazo de un AsientoRegistral en formato SICRES3 a un nodo distribuido
     * @param ficheroIntercambio
     */
    public void rechazarFicheroIntercambio(FicheroIntercambio ficheroIntercambio) {

    }

    /**
     * Envia un asiento registral a un nodo distribuido creando previamente el fichero de intercambio
     * @param asientoRegistralSir
     * @param estado
     */
    public void enviar(AsientoRegistralSir asientoRegistralSir, EstadoAsientoRegistralSir estado){

        try{
        // Creamos el xml de intercambio
        String xml = sicres3XML.crearXMLFicheroIntercambioSICRES3(asientoRegistralSir);
        log.info("Xml Fichero Intercambio generado: " + xml);

        RespuestaWS respuesta = ws_sir6_b_recepcionFicheroDeAplicacion(xml);

        if (respuesta != null) {
            log.info("Respuesta: " + respuesta.getCodigo() + " - " + respuesta.getDescripcion());

            if (Errores.OK.getValue().equals(respuesta.getCodigo())) {

                log.info("AsientoRegistral enviado correctamente");
                asientoRegistralSirEjb.modificarEstado(asientoRegistralSir.getId(), estado);

            }else{
                log.error("Respuesta: " + respuesta.getCodigo() + " - " + respuesta.getDescripcion());
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
    public RespuestaWS ws_sir6_b_recepcionFicheroDeAplicacion(String xml) throws Exception {

        WS_SIR6_B_PortType ws_sir6_b = getWS_SIR6_B();

        return ws_sir6_b.recepcionFicheroDeAplicacion(xml);

    }

    /**
     * @return
     * @throws Exception
     */
    public WS_SIR6_B_PortType getWS_SIR6_B() throws Exception {
        WS_SIR6_BServiceLocator locator = new WS_SIR6_BServiceLocator();
        URL url = new URL(Configuracio.getSirServerBase() + "/WS_SIR6_B");

        return  locator.getWS_SIR6_B(url);
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
