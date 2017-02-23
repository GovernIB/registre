package es.caib.regweb3.sir.ejb;

import es.caib.regweb3.model.Oficina;
import es.caib.regweb3.model.UsuarioEntidad;
import es.caib.regweb3.sir.core.model.AsientoRegistralSir;

import javax.ejb.Local;

/**
 * Created by earrivi on 22/02/2017.
 */
@Local
/*@RolesAllowed({"RWE_SUPERADMIN","RWE_ADMIN","RWE_USUARI"})*/
public interface EmisionLocal {

    public AsientoRegistralSir enviarFicheroIntercambio(Long idRegistroEntrada, String codigoEntidadRegistralDestino, String denominacionEntidadRegistralDestino, Oficina oficinaActiva, UsuarioEntidad usuario, Long idLibro);
}
