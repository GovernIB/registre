package es.caib.regweb3.persistence.ejb;

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
     * Obtiene la Trazabilidad de un Registroentrada
     * @param idRegistroEntrada
     * @return
     * @throws Exception
     */
    public List<Trazabilidad> getByRegistroEntrada(Long idRegistroEntrada) throws Exception;

    /**
     * Obtiene el RegistroSalida correspondiente a un OficioRemision y a un RegistroEntrada
     * @param idOficioRemision
     * @param idRegistroEntrada
     * @return
     * @throws Exception
     */
    public Trazabilidad getByOficioRegistroEntrada(Long idOficioRemision, Long idRegistroEntrada) throws Exception;

    /**
     * Elimina las Trazabilidades de una Entidad
     * @param idEntidad
     * @return
     * @throws Exception
     */
    public Integer eliminarByEntidad(Long idEntidad) throws Exception;

}
