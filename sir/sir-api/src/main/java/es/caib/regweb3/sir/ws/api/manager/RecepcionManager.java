package es.caib.regweb3.sir.ws.api.manager;

import es.caib.regweb3.model.PreRegistro;
import es.caib.regweb3.persistence.ejb.WebServicesMethodsLocal;

/**
 *
 */

public interface RecepcionManager {

    public PreRegistro recibirFicheroIntercambio(String xmlFicheroIntercambio, WebServicesMethodsLocal webServicesMethodsEjb);

}
