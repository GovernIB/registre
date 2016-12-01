package es.caib.regweb3.sir.ws.api.service;

import es.caib.regweb3.persistence.ejb.WebServicesMethodsLocal;
import es.caib.regweb3.sir.ws.api.manager.RecepcionManager;
import org.apache.log4j.Logger;
import org.springframework.util.Assert;


/**
 * Created by earrivi on 14/01/2016.
 */
public class ServicioIntercambioRegistral {

    public final Logger log = Logger.getLogger(getClass());

    public RecepcionManager recepcionManager = new RecepcionManager();

    public void recibirFicheroIntercambio(String xmlFicheroIntercambio, WebServicesMethodsLocal webServicesMethodsEjb) {

        Assert.hasText(xmlFicheroIntercambio, "El xml del ficheroIntercambio no puede estar vacio");

        recepcionManager.recibirFicheroIntercambio(xmlFicheroIntercambio, webServicesMethodsEjb);
    }

}
