package es.caib.regweb3.sir.ws.api.manager.impl;

import es.caib.regweb3.model.PreRegistro;
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
     * @param preRegistro Información del asiento registral.
     */
    public void enviarMensajeConfirmacion(PreRegistro preRegistro) {

        Mensaje confirmacion = new Mensaje();
        confirmacion.setCodigoEntidadRegistralOrigen(preRegistro.getCodigoEntidadRegistralDestino());
        confirmacion.setCodigoEntidadRegistralDestino(preRegistro.getCodigoEntidadRegistralInicio());
        confirmacion.setIdentificadorIntercambio(preRegistro.getIdIntercambio());
        confirmacion.setTipoMensaje(TipoMensaje.CONFIRMACION);
        confirmacion.setDescripcionMensaje(TipoMensaje.CONFIRMACION.getName());

        //todo Añadir NumeroRegistro confirmacion.setNumeroRegistroEntradaDestino(preRegistro.getNumeroRegistro());
        //confirmacion.setNumeroRegistroEntradaDestino("0000001");
        confirmacion.setFechaEntradaDestino(new Date());

        mensajeManager.enviarMensaje(confirmacion);

        log.info("Mensaje de confirmación  enviado");
    }
}
