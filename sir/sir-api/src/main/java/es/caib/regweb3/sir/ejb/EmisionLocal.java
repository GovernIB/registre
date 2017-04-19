package es.caib.regweb3.sir.ejb;

import es.caib.regweb3.model.AsientoRegistralSir;

import javax.ejb.Local;

/**
 * Created by earrivi on 22/02/2017.
 */
@Local
/*@RolesAllowed({"RWE_SUPERADMIN","RWE_ADMIN","RWE_USUARI"})*/
public interface EmisionLocal {

    public void enviarFicheroIntercambio(AsientoRegistralSir asientoRegistralSir) throws Exception;

    public void reenviarFicheroIntercambio(AsientoRegistralSir asientoRegistralSir)  throws Exception;

    public void rechazarFicheroIntercambio(AsientoRegistralSir asientoRegistralSir) throws Exception;
}
