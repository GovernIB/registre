package es.caib.regweb3.sir.ws.api.service;

import es.caib.regweb3.persistence.ejb.WebServicesMethodsLocal;

/**
 * Created by earrivi on 14/01/2016.
 */
public interface ServicioIntercambioRegistral {

    public void recibirFicheroIntercambio(String xmlFicheroIntercambio, WebServicesMethodsLocal webServicesMethodsEjb);
}
