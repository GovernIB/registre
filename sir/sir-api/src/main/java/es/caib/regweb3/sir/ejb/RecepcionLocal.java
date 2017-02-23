package es.caib.regweb3.sir.ejb;

import es.caib.regweb3.persistence.ejb.WebServicesMethodsLocal;

import javax.ejb.Local;

/**
 * Created by earrivi on 22/02/2017.
 */
@Local
/*@RolesAllowed({"RWE_SUPERADMIN","RWE_ADMIN","RWE_USUARI"})*/
public interface RecepcionLocal {

    public void recibirFicheroIntercambio(String xmlFicheroIntercambio, WebServicesMethodsLocal webServicesMethodsEjb);

    public void recibirMensajeDatosControl(String xmlMensaje);
}
