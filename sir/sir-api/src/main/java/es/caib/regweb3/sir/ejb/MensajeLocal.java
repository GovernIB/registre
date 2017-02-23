package es.caib.regweb3.sir.ejb;

import es.caib.regweb3.sir.core.model.AsientoRegistralSir;
import es.caib.regweb3.sir.utils.Mensaje;

import javax.ejb.Local;

/**
 * Created by earrivi on 22/02/2017.
 */
@Local
/*@RolesAllowed({"RWE_SUPERADMIN","RWE_ADMIN","RWE_USUARI"})*/
public interface MensajeLocal {

    public void enviarMensaje(Mensaje mensaje);

    public void enviarMensajeConfirmacion(AsientoRegistralSir asientoRegistralSir, String numeroRegistro);
}
