package es.caib.regweb3.persistence.ejb;

import es.caib.regweb3.model.RegistroSalida;
import es.caib.regweb3.model.Trazabilidad;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Local;
import java.util.List;

/**
 * Created by Fundació BIT.
 *
 * @author earrivi
 * Date: 16/01/14
 */
@Local
@RolesAllowed({"RWE_SUPERADMIN","RWE_ADMIN","RWE_USUARI"})
public interface TrazabilidadLocal extends BaseEjb<Trazabilidad, Long> {

    /**
     * Obtiene la Trazabilidad de un RegistroSalida
     * @param idRegistroSalida
     * @return
     * @throws Exception
     */
    public List<Trazabilidad> getByRegistroSalida(Long idRegistroSalida) throws Exception;

    /**
     * Obtiene la Trazabilidad de un RegistroEntrada
     * @param idRegistroEntrada
     * @return
     * @throws Exception
     */
    public List<Trazabilidad> getByRegistroEntrada(Long idRegistroEntrada) throws Exception;

    /**
     * Obtiene las Trazabilidades de un OficioRemision
     * @param idOficioRemision
     * @return
     * @throws Exception
     */
    public List<Trazabilidad> getByOficioRemision(Long idOficioRemision) throws Exception;

    /**
     * Obtiene la Trazabilidad correspondiente a un OficioRemision y a un RegistroEntrada
     * @param idOficioRemision
     * @param idRegistroEntrada
     * @return
     * @throws Exception
     */
    public Trazabilidad getByOficioRegistroEntrada(Long idOficioRemision, Long idRegistroEntrada) throws Exception;

    /**
     * Obtiene la Trazabilidad correspondiente a un OficioRemision y a un RegistroSalida
     * @param idOficioRemision
     * @param idRegistroSalida
     * @return
     * @throws Exception
     */
    public Trazabilidad getByOficioRegistroSalida(Long idOficioRemision, Long idRegistroSalida) throws Exception;

    /**
     * Obtiene las Trazabilidades de un AsientoRegistralSir
     * @param idAsientoRegistralSir
     * @return
     * @throws Exception
     */
    public List<Trazabilidad> getByAsientoRegistralSir(Long idAsientoRegistralSir) throws Exception;

    /**
     * Elimina las Trazabilidades de una Entidad
     * @param idEntidad
     * @return
     * @throws Exception
     */
    public Integer eliminarByEntidad(Long idEntidad) throws Exception;

    /**
     * Obtiene los RegistroSalida generados en un OficioRemision
     * @param idOficioRemision
     * @return
     */
    public List<RegistroSalida> obtenerRegistrosSalida(Long idOficioRemision) throws Exception;

}
