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
    public void enviarMensajeConfirmacion(RegistroSir registroSir, String numeroRegistro);

    /**
     *
     * @param ficheroIntercambio
     */
    public void enviarACK(FicheroIntercambio ficheroIntercambio);

    /**
     *
     * @param mensaje
     */
    public void enviarACK(Mensaje mensaje);

    /**
     *
     * @param mensaje
     */
    public void enviarMensajeError(Mensaje mensaje);
}
