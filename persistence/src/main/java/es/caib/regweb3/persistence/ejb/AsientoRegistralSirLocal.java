package es.caib.regweb3.persistence.ejb;

import es.caib.regweb3.persistence.utils.Paginacion;
import es.caib.regweb3.sir.core.model.AsientoRegistralSir;
import es.caib.regweb3.sir.core.model.EstadoAsientoRegistralSir;

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
     * Obtiene un AsientoRegistral a partir de los parámetros
     * @param identificadorIntercambio
     * @param codigoEntidadRegistralDestino
     * @return
     * @throws Exception
     */
    public AsientoRegistralSir getAsientoRegistral(String identificadorIntercambio, String codigoEntidadRegistralDestino) throws Exception;

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
     * Elimina los AsientoRegistralSir de una Entidad
     * @param idEntidad
     * @return
     * @throws Exception
     */
    public Integer eliminarByEntidad(Long idEntidad) throws Exception;
}
