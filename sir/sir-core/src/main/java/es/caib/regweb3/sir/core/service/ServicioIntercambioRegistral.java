package es.caib.regweb3.sir.core.service;

import es.caib.regweb3.model.PreRegistro;
import es.caib.regweb3.persistence.ejb.WebServicesMethodsLocal;

/**
 * Created by earrivi on 14/01/2016.
 */
public interface ServicioIntercambioRegistral {

    public PreRegistro recibirFicheroIntercambio(String xmlFicheroIntercambio, WebServicesMethodsLocal webServicesMethodsEjb);
}
