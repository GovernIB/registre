package es.caib.regweb3.sir.ws.manager;

import es.caib.regweb3.persistence.ejb.WebServicesMethodsLocal;

/**
 * Interfaz
 * @author earrivi
 */
public interface EnvioManager {

    public void envioFichero(String ficheroIntercambio, WebServicesMethodsLocal webServicesMethodsEjb);
}
