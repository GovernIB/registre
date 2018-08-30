package es.caib.regweb3.persistence.ejb;

import es.caib.regweb3.sir.core.utils.FicheroIntercambio;
import es.caib.regweb3.sir.core.utils.Mensaje;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Local;

/**
 * Created by Fundació BIT.
 *
 * @author earrivi
 * Date: 22/06/16
 */
@Local
@RolesAllowed({"RWE_SUPERADMIN", "RWE_ADMIN", "RWE_USUARI"})
public interface SirRecepcionLocal {

    /**
     * Recibe un fichero de intercambio en formato SICRES3 desde un nodo distribuido
     * @param ficheroIntercambio
     * @throws Exception
     */
    Boolean recibirFicheroIntercambio(FicheroIntercambio ficheroIntercambio) throws Exception;

    /**
     * Realiza las acciones pertinentes cuando se recibie un mensaje de control
     *
     * @param mensaje
     * @throws Exception
     */
    void recibirMensajeDatosControl(Mensaje mensaje) throws Exception;

}

