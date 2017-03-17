package es.caib.regweb3.sir.ejb;

import es.caib.regweb3.model.Oficina;
import es.caib.regweb3.model.OficioRemision;
import es.caib.regweb3.model.UsuarioEntidad;

import javax.ejb.Local;

/**
 * Created by earrivi on 22/02/2017.
 */
@Local
/*@RolesAllowed({"RWE_SUPERADMIN","RWE_ADMIN","RWE_USUARI"})*/
public interface EmisionLocal {

    public OficioRemision enviarFicheroIntercambio(String tipoRegistro, Long idRegistro, String codigoEntidadRegistralDestino, String denominacionEntidadRegistralDestino, Oficina oficinaActiva, UsuarioEntidad usuario, Long idLibro);
}
