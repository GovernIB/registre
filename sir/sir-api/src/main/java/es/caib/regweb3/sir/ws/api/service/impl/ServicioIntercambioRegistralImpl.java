package es.caib.regweb3.sir.ws.api.service.impl;

import es.caib.regweb3.model.PreRegistro;
import es.caib.regweb3.persistence.ejb.WebServicesMethodsLocal;
import es.caib.regweb3.sir.core.service.ServicioIntercambioRegistral;
import es.caib.regweb3.sir.ws.api.manager.RecepcionManager;
import es.caib.regweb3.sir.ws.api.manager.impl.RecepcionManagerImpl;
import org.apache.log4j.Logger;
import org.springframework.util.Assert;


/**
 * Created by earrivi on 14/01/2016.
 */
public class ServicioIntercambioRegistralImpl implements ServicioIntercambioRegistral {

    public final Logger log = Logger.getLogger(getClass());

    public RecepcionManager recepcionManager = new RecepcionManagerImpl();

    @Override
    public PreRegistro recibirFicheroIntercambio(String xmlFicheroIntercambio, WebServicesMethodsLocal webServicesMethodsEjb) {

        Assert.hasText(xmlFicheroIntercambio, "'xmlFicheroIntercambio' no puede estar vacio");

        return recepcionManager.recibirFicheroIntercambio(xmlFicheroIntercambio, webServicesMethodsEjb);
    }

}
