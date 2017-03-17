package es.caib.regweb3.sir.ejb;

import es.caib.regweb3.model.AsientoRegistralSir;
import es.caib.regweb3.sir.utils.FicheroIntercambio;
import es.caib.regweb3.sir.utils.Mensaje;

import javax.ejb.Local;

/**
 * Created by earrivi on 22/02/2017.
 */
@Local
/*@RolesAllowed({"RWE_SUPERADMIN","RWE_ADMIN","RWE_USUARI"})*/
public interface MensajeLocal {

    /**
     *
     * @param asientoRegistralSir
     * @param numeroRegistro
     */
    public void enviarMensajeConfirmacion(AsientoRegistralSir asientoRegistralSir, String numeroRegistro);

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
     * @param codigoError
     * @param descError
     */
    public void enviarMensajeError(Mensaje mensaje, String codigoError, String descError);
}
