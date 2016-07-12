package es.caib.regweb3.persistence.ejb;

import es.caib.regweb3.model.Oficina;
import es.caib.regweb3.model.UsuarioEntidad;
import es.caib.regweb3.persistence.utils.Paginacion;
import es.caib.regweb3.sir.core.model.AsientoRegistralSir;
import es.caib.regweb3.sir.core.model.EstadoAsientoRegistralSir;
import org.fundaciobit.genapp.common.i18n.I18NException;
import org.fundaciobit.genapp.common.i18n.I18NValidationException;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Local;
import java.util.List;

/**
 * Created by Fundació BIT.
 *
 * @author earrivi
 * Date: 16/06/16
 */
@Local
@RolesAllowed({"RWE_SUPERADMIN","RWE_ADMIN","RWE_USUARI"})
public interface AsientoRegistralSirLocal extends BaseEjb<AsientoRegistralSir, Long> {

    /**
     * Crea un AsientoRegistralSir
     * @throws Exception
     */
    public AsientoRegistralSir crearAsientoRegistralSir(AsientoRegistralSir asientoRegistralSir) throws Exception;

    /**
     * Comrueba si una Oficina tiene AsientoRegistralSir
     * @param codigoOficinaActiva código de la Oficina Activa
     * @return
     * @throws Exception
     */
    public Boolean tieneAsientoRegistralSir(String codigoOficinaActiva) throws Exception;

    /**
     * Busca los AsientoRegistralSir en función de los parámetros, donde sólo mostrará los AsientoRegistralSir con codEntidadRegistralDestino = codOficinaActiva
     * @param pageNumber
     * @param any
     * @param asientoRegistralSir
     * @param codigoOficinaActiva
     * @param estado
     * @return
     * @throws Exception
     */
    public Paginacion busqueda(Integer pageNumber, Integer any, AsientoRegistralSir asientoRegistralSir, String codigoOficinaActiva, String estado) throws Exception;

    /**
     *
     * @param codigoOficinaActiva
     * @param total
     * @return
     * @throws Exception
     */
    public List<AsientoRegistralSir> getUltimosARSPendientesProcesar(String codigoOficinaActiva, Integer total) throws Exception;

    /**
     * Modifica el Estado de un {@link es.caib.regweb3.sir.core.model.AsientoRegistralSir}
     * @param idAsientoRegistralSir
     * @param estado
     * @throws Exception
     */
    public void modificarEstado(Long idAsientoRegistralSir, EstadoAsientoRegistralSir estado) throws Exception;

    /**
     *
     * @param asientoRegistralSir
     * @param usuario
     * @param oficinaActiva
     * @param idLibro
     * @param idIdioma
     * @param idTipoAsunto
     * @return
     * @throws Exception
     * @throws I18NException
     * @throws I18NValidationException
     */
    public Long aceptarAsientoRegistralSir(AsientoRegistralSir asientoRegistralSir, UsuarioEntidad usuario, Oficina oficinaActiva, Long idLibro, Long idIdioma, Long idTipoAsunto)
            throws Exception, I18NException, I18NValidationException;


}
