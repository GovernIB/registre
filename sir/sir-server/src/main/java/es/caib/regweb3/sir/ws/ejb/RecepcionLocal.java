package es.caib.regweb3.sir.ws.ejb;

import es.caib.regweb3.persistence.ejb.WebServicesMethodsLocal;

import javax.ejb.Local;

/**
 * Created by earrivi on 22/02/2017.
 */
@Local
/*@RolesAllowed({"RWE_SUPERADMIN","RWE_ADMIN","RWE_USUARI"})*/
public interface RecepcionLocal {

    String JNDI_NAME = "java:app/regweb3-sir-ws-server/RecepcionEJB";


    /**
     *
     * @param xmlFicheroIntercambio
     * @param webServicesMethodsEjb
     */
    public void recibirFicheroIntercambio(String xmlFicheroIntercambio, WebServicesMethodsLocal webServicesMethodsEjb) throws Exception;

    /**
     *
     * @param xmlMensaje
     * @param webServicesMethodsEjb
     */
    public void recibirMensajeDatosControl(String xmlMensaje, WebServicesMethodsLocal webServicesMethodsEjb);

}
