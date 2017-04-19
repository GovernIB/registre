package es.caib.regweb3.sir.ejb;

import es.caib.regweb3.model.*;
import es.caib.regweb3.model.utils.EstadoAsientoRegistralSir;
import es.caib.regweb3.persistence.ejb.*;
import es.caib.regweb3.sir.core.excepcion.SIRException;
import es.caib.regweb3.sir.core.model.Errores;
import es.caib.regweb3.sir.utils.Sicres3XML;
import es.caib.regweb3.sir.ws.api.wssir6b.RespuestaWS;
import es.caib.regweb3.sir.ws.api.wssir6b.WS_SIR6_BServiceLocator;
import es.caib.regweb3.sir.ws.api.wssir6b.WS_SIR6_B_PortType;
import es.caib.regweb3.utils.Configuracio;
import es.caib.regweb3.utils.RegwebConstantes;
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

    @EJB private SirLocal sirEjb;
    @EJB private AsientoRegistralSirLocal asientoRegistralSirEjb;
    @EJB private RegistroEntradaLocal registroEntradaEjb;
    @EJB private RegistroSalidaLocal registroSalidaEjb;
    @EJB private OficioRemisionLocal oficioRemisionEjb;

    private Sicres3XML sicres3XML = new Sicres3XML();


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
    public OficioRemision enviarFicheroIntercambio(String tipoRegistro, Long idRegistro, String codigoEntidadRegistralDestino, String denominacionEntidadRegistralDestino, Oficina oficinaActiva, UsuarioEntidad usuario, Long idLibro)throws Exception{

        OficioRemision oficioRemision;
        AsientoRegistralSir asientoRegistralSir = null;

        try {

            oficioRemision = sirEjb.enviarFicheroIntercambio(tipoRegistro, idRegistro, codigoEntidadRegistralDestino, denominacionEntidadRegistralDestino, oficinaActiva, usuario,idLibro);

            if(tipoRegistro.equals(RegwebConstantes.REGISTRO_ENTRADA_ESCRITO)){

                RegistroEntrada registroEntrada = registroEntradaEjb.getConAnexosFullCompleto(oficioRemision.getRegistrosEntrada().get(0).getId());
                asientoRegistralSir = asientoRegistralSirEjb.transformarRegistroEntrada(registroEntrada);
                log.info("asientoRegistralSir transformado:" + asientoRegistralSir.getIdentificadorIntercambio());
            }else if(tipoRegistro.equals(RegwebConstantes.REGISTRO_SALIDA_ESCRITO)){

                RegistroSalida registroSalida = registroSalidaEjb.getConAnexosFullCompleto(oficioRemision.getRegistrosSalida().get(0).getId());
                asientoRegistralSir = asientoRegistralSirEjb.transformarRegistroSalida(registroSalida);
                log.info("asientoRegistralSir transformado:" + asientoRegistralSir.getIdentificadorIntercambio());
            }

            log.info("Enviando el registro al nodo distribuido: " + asientoRegistralSir.getIdentificadorIntercambio());

            // Enviamos el asiento registral al nodo distribuido.
            enviar(asientoRegistralSir);

            // Modificamos el estado del OficioRemision
            oficioRemisionEjb.modificarEstado(oficioRemision.getId(), RegwebConstantes.OFICIO_SIR_ENVIADO);

        } catch (Exception e) {
            log.error("Error al enviar el fichero de intercambio: " + e);
            e.printStackTrace();
            throw new SIRException("Error en la llamada al servicio de recepción de ficheros de datos de intercambio (WS_SIR6_B)");
        } catch (I18NException e) {
            log.error("Error al enviar el fichero de intercambio: " + e);
            e.printStackTrace();
            throw new SIRException("Error en la llamada al servicio de recepción de ficheros de datos de intercambio (WS_SIR6_B)");
        }

        return oficioRemision;
    }

    /**
     * Reenvío de un AsientoRegistral en formato SICRES3 a un nodo distribuido
     * @param asientoRegistralSir
     */
    public void reenviarFicheroIntercambio(AsientoRegistralSir asientoRegistralSir, Oficina oficinaReenvio, Oficina oficinaActiva, Usuario usuario, String observaciones)  throws Exception {

        //Preparamos el asiento registral para su reenvio
        asientoRegistralSir =  sirEjb.reenviarAsientoRegistralSir(asientoRegistralSir, oficinaReenvio, oficinaActiva, usuario, observaciones);

        log.info("Reenviando el asiento registral al nodo distribuido: " + asientoRegistralSir.getIdentificadorIntercambio());

        // Reenviamos el asiento registral al nodo distribuido.
        enviar(asientoRegistralSir);

        // Modificamos el estado del AsientoRegistralSir
        asientoRegistralSirEjb.modificarEstado(asientoRegistralSir.getId(), EstadoAsientoRegistralSir.REENVIADO);
    }

    /**
     * Rechazo de un AsientoRegistral en formato SICRES3 a un nodo distribuido
     * @param asientoRegistralSir
     * @param oficinaActiva
     * @param usuario
     */
    public void rechazarFicheroIntercambio(AsientoRegistralSir asientoRegistralSir, Oficina oficinaActiva, Usuario usuario, String observaciones) throws Exception{

        //Preparamos el asiento registral para su rechazo
        asientoRegistralSir = sirEjb.rechazarAsientoRegistralSir(asientoRegistralSir, oficinaActiva, usuario, observaciones);

        log.info("Rezhazando el asiento registral al nodo distribuido: " + asientoRegistralSir.getIdentificadorIntercambio());

        // Rechazamos el asiento registral al nodo distribuido.
        enviar(asientoRegistralSir);

        // Modificamos el estado del AsientoRegistralSir
        asientoRegistralSirEjb.modificarEstado(asientoRegistralSir.getId(), EstadoAsientoRegistralSir.RECHAZADO);
    }

    /**
     * Envia un asiento registral a un nodo distribuido creando previamente el fichero de intercambio
     * @param asientoRegistralSir
     */
    private void enviar(AsientoRegistralSir asientoRegistralSir){

        try{
        // Creamos el xml de intercambio
        String xml = sicres3XML.crearXMLFicheroIntercambioSICRES3(asientoRegistralSir);
        log.info("Xml Fichero Intercambio generado: " + xml);

        RespuestaWS respuesta = ws_sir6_b_recepcionFicheroDeAplicacion(xml);

        if (respuesta != null) {
            log.info("Respuesta: " + respuesta.getCodigo() + " - " + respuesta.getDescripcion());

            if (Errores.OK.getValue().equals(respuesta.getCodigo())) {

                log.info("AsientoRegistral enviado correctamente");
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
