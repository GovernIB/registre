package es.caib.regweb3.sir.ejb;

import es.caib.regweb3.model.RegistroSir;
import org.fundaciobit.genapp.common.i18n.I18NException;

import javax.ejb.Local;

/**
 * Created by earrivi on 22/02/2017.
 */
@Local
/*@RolesAllowed({"RWE_SUPERADMIN","RWE_ADMIN","RWE_USUARI"})*/
public interface EmisionLocal {

    String JNDI_NAME = "java:app/regweb3-sir-api/EmisionEJB";

    /**
     * @param registroSir
     * @throws Exception
     */
    void enviarFicheroIntercambio(RegistroSir registroSir) throws I18NException;

    /**
     * @param registroSir
     * @throws Exception
     */
    void reenviarFicheroIntercambio(RegistroSir registroSir) throws I18NException;

    /**
     * @param registroSir
     * @throws Exception
     */
    void rechazarFicheroIntercambio(RegistroSir registroSir) throws I18NException;
}
