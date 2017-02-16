package es.caib.regweb3.sir.core.service;

import es.caib.regweb3.sir.core.model.AsientoRegistralSir;

/**
 * Created by earrivi on 14/01/2016.
 */

public interface ServicioIntercambioRegistral {


    public void enviarAsientoRegistralSir(AsientoRegistralSir asientoRegistralSir);

    //public AsientoRegistralSir recibirFicheroIntercambio(String xmlFicheroIntercambio, WebServicesMethodsLocal webServicesMethodsEjb);
}
