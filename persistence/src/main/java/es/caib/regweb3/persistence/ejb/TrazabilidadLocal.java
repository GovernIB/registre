package es.caib.regweb3.persistence.ejb;

import es.caib.regweb3.model.RegistroEntrada;
import es.caib.regweb3.model.RegistroSalida;
import es.caib.regweb3.model.Trazabilidad;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Local;
import java.util.List;
import java.util.Set;

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
    List<Trazabilidad> getByRegistroSalida(Long idRegistroSalida) throws Exception;

    /**
     * Obtiene la Trazabilidad de un RegistroEntrada
     * @param idRegistroEntrada
     * @return
     * @throws Exception
     */
    List<Trazabilidad> getByRegistroEntrada(Long idRegistroEntrada) throws Exception;

    /**
     * Obtiene las Trazabilidades de un OficioRemision
     * @param idOficioRemision
     * @return
     * @throws Exception
     */
    List<Trazabilidad> getByOficioRemision(Long idOficioRemision) throws Exception;

    /**
     * Obtiene la Trazabilidad correspondiente a un OficioRemision y a un RegistroEntrada
     * @param idOficioRemision
     * @param idRegistroEntrada
     * @return
     * @throws Exception
     */
    Trazabilidad getByOficioRegistroEntrada(Long idOficioRemision, Long idRegistroEntrada) throws Exception;

    /**
     * Obtiene la Trazabilidad correspondiente a un OficioRemision y a un RegistroSalida
     * @param idOficioRemision
     * @param idRegistroSalida
     * @return
     * @throws Exception
     */
    Trazabilidad getByOficioRegistroSalida(Long idOficioRemision, Long idRegistroSalida) throws Exception;

    /**
     * Obtiene las Trazabilidades de un RegistroSir
     * @param idRegistroSir
     * @return
     * @throws Exception
     */
    List<Trazabilidad> getByRegistroSir(Long idRegistroSir) throws Exception;

    /**
     * Obtiene el Registro de Entrada generado a partir de la aceptación de un Registro Sir
     * @param idRegistroSir
     * @return
     * @throws Exception
     */
    RegistroEntrada getRegistroAceptado(Long idRegistroSir) throws Exception;

    /**
     * Elimina las Trazabilidades de una Entidad
     * @param idEntidad
     * @return
     * @throws Exception
     */
    Integer eliminarByEntidad(Long idEntidad) throws Exception;

    /**
     * Obtiene los RegistroSalida generados en un OficioRemision
     * @param idOficioRemision
     * @return
     */
    List<RegistroSalida> obtenerRegistrosSalida(Long idOficioRemision) throws Exception;

    /**
     * Obtiene los RegistroEntrada validos que han sido creados a partir de una recepción SIR
     * @param idOficina
     * @param idEntidad
     * @param organismos
     * @param total
     * @return
     * @throws Exception
     */
    List<RegistroEntrada> getPendientesDistribuirSir(Long idOficina, Long idEntidad, Set<Long> organismos, Integer total) throws Exception;

}
