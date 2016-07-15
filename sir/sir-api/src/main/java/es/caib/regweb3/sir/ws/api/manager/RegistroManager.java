package es.caib.regweb3.sir.ws.api.manager;

import es.caib.regweb3.sir.core.model.AsientoRegistralSir;

/**
 * Created by earrivi on 14/01/2016.
 */
public interface RegistroManager {

    public void enviarMensajeConfirmacion(AsientoRegistralSir asientoRegistralSir, String numeroRegistro);
}
