package es.caib.regweb3.sir.ejb;

import es.caib.regweb3.model.*;

import javax.ejb.Local;

/**
 * Created by earrivi on 22/02/2017.
 */
@Local
/*@RolesAllowed({"RWE_SUPERADMIN","RWE_ADMIN","RWE_USUARI"})*/
public interface EmisionLocal {

    public OficioRemision enviarFicheroIntercambio(String tipoRegistro, Long idRegistro, String codigoEntidadRegistralDestino, String denominacionEntidadRegistralDestino, Oficina oficinaActiva, UsuarioEntidad usuario, Long idLibro);

    public void reenviarFicheroIntercambio(AsientoRegistralSir asientoRegistralSir, Oficina oficinaReenvio, Oficina oficinaActiva, Usuario usuario)  throws Exception;

    public void rechazarFicheroIntercambio(AsientoRegistralSir asientoRegistralSir, Oficina oficinaReenvio, Oficina oficinaActiva, Usuario usuario) throws Exception;
}
