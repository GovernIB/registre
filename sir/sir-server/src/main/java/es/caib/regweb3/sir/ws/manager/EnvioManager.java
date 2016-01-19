package es.caib.regweb3.sir.ws.manager;

import es.caib.regweb3.persistence.ejb.WebServicesMethodsLocal;

/**
 * Created by earrivi on 14/01/2016.
 */
public interface EnvioManager {

    public void envioFichero(String ficheroIntercambio, WebServicesMethodsLocal webServicesMethodsEjb);
}
