package es.caib.regweb3.sir.ejb;

import es.caib.regweb3.model.RegistroSir;
import es.caib.regweb3.model.sir.MensajeControl;
import es.caib.regweb3.sir.core.utils.FicheroIntercambio;

import javax.ejb.Local;
import java.util.Date;

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
    MensajeControl enviarMensajeConfirmacion(RegistroSir registroSir, String numeroRegistro, Date fechaRegistro);

    /**
     *
     * @param ficheroIntercambio
     */
    MensajeControl enviarACK(FicheroIntercambio ficheroIntercambio);

    /**
     *
     * @param mensaje
     */
    MensajeControl enviarACK(MensajeControl mensaje);

    /**
     *
     * @param mensaje
     */
    MensajeControl enviarMensajeError(MensajeControl mensaje);

    /**
     * Reenvia un mensaje de control
     * @param mensaje
     * @return
     */
    MensajeControl reenviarMensajeControl(MensajeControl mensaje);
}
