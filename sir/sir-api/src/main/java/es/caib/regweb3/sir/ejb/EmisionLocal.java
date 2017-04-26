package es.caib.regweb3.sir.ejb;

import es.caib.regweb3.model.RegistroSir;

import javax.ejb.Local;

/**
 * Created by earrivi on 22/02/2017.
 */
@Local
/*@RolesAllowed({"RWE_SUPERADMIN","RWE_ADMIN","RWE_USUARI"})*/
public interface EmisionLocal {

    public void enviarFicheroIntercambio(RegistroSir registroSir) throws Exception;

    public void reenviarFicheroIntercambio(RegistroSir registroSir)  throws Exception;

    public void rechazarFicheroIntercambio(RegistroSir registroSir) throws Exception;
}
