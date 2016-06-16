package es.caib.regweb3.sir.ws.api.manager;

import es.caib.regweb3.persistence.ejb.WebServicesMethodsLocal;

/**
 *
 */

public interface RecepcionManager {

    public void recibirFicheroIntercambio(String xmlFicheroIntercambio, WebServicesMethodsLocal webServicesMethodsEjb);

}
