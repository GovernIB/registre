package es.caib.regweb3.sir.ws.api.manager.impl;

import es.caib.regweb3.sir.core.model.AsientoRegistralSir;
import es.caib.regweb3.sir.core.model.TipoMensaje;
import es.caib.regweb3.sir.ws.api.manager.MensajeManager;
import es.caib.regweb3.sir.ws.api.manager.RegistroManager;
import es.caib.regweb3.sir.ws.api.utils.Mensaje;
import org.apache.log4j.Logger;

import java.util.Date;

/**
 * Created by earrivi on 14/01/2016.
 */
public class RegistroManagerImpl implements RegistroManager {

    public final Logger log = Logger.getLogger(getClass());

    public MensajeManager mensajeManager = new MensajeManagerImpl();


    /**
     * Envía un mensaje de control de confirmación.
     *
     * @param asientoRegistralSir Información del asiento registral.
     */
    public void enviarMensajeConfirmacion(AsientoRegistralSir asientoRegistralSir, String numeroRegistro) {

        Mensaje confirmacion = new Mensaje();
        confirmacion.setCodigoEntidadRegistralOrigen(asientoRegistralSir.getCodigoEntidadRegistralDestino());
        confirmacion.setCodigoEntidadRegistralDestino(asientoRegistralSir.getCodigoEntidadRegistralInicio());
        confirmacion.setIdentificadorIntercambio(asientoRegistralSir.getIdentificadorIntercambio());
        confirmacion.setTipoMensaje(TipoMensaje.CONFIRMACION);
        confirmacion.setDescripcionMensaje(TipoMensaje.CONFIRMACION.getName());
        confirmacion.setNumeroRegistroEntradaDestino(numeroRegistro);
        confirmacion.setFechaEntradaDestino(new Date());

        mensajeManager.enviarMensaje(confirmacion);

        log.info("Mensaje de confirmación  enviado");
    }
}
