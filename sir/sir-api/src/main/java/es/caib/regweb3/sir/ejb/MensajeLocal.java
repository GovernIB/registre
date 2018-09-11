package es.caib.regweb3.sir.ejb;

import es.caib.regweb3.model.RegistroSir;
import es.caib.regweb3.sir.core.utils.FicheroIntercambio;
import es.caib.regweb3.sir.core.utils.Mensaje;

import javax.ejb.Local;

/**
 * Created by earrivi on 22/02/2017.
 */
@Local
/*@RolesAllowed({"RWE_SUPERADMIN","RWE_ADMIN","RWE_USUARI"})*/
public interface MensajeLocal {

    /**
     *
     * @param registroSir
     * @param numeroRegistro
     */
    void enviarMensajeConfirmacion(RegistroSir registroSir, String numeroRegistro);

    /**
     *
     * @param ficheroIntercambio
     */
    void enviarACK(FicheroIntercambio ficheroIntercambio);

    /**
     *
     * @param mensaje
     */
    void enviarACK(Mensaje mensaje);

    /**
     *
     * @param mensaje
     */
    void enviarMensajeError(Mensaje mensaje);
}
