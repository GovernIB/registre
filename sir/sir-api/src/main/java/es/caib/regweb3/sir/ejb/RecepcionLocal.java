package es.caib.regweb3.sir.ejb;

import es.caib.regweb3.model.AsientoRegistralSir;
import es.caib.regweb3.model.Oficina;
import es.caib.regweb3.model.RegistroEntrada;
import es.caib.regweb3.model.UsuarioEntidad;
import es.caib.regweb3.model.utils.CamposNTI;
import es.caib.regweb3.persistence.ejb.WebServicesMethodsLocal;

import javax.ejb.Local;
import java.util.List;

/**
 * Created by earrivi on 22/02/2017.
 */
@Local
/*@RolesAllowed({"RWE_SUPERADMIN","RWE_ADMIN","RWE_USUARI"})*/
public interface RecepcionLocal {

    public void recibirFicheroIntercambio(String xmlFicheroIntercambio, WebServicesMethodsLocal webServicesMethodsEjb);

    public void recibirMensajeDatosControl(String xmlMensaje);

    /**
     *
     * @param asientoRegistralSir
     * @param usuario
     * @param oficinaActiva
     * @param idLibro
     * @param idIdioma
     * @param idTipoAsunto
     * @param camposNTIs
     * @return
     */
    public RegistroEntrada aceptarAsientoRegistralSir(AsientoRegistralSir asientoRegistralSir, UsuarioEntidad usuario, Oficina oficinaActiva, Long idLibro, Long idIdioma, Long idTipoAsunto, List<CamposNTI> camposNTIs) throws Exception;
}
