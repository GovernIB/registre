package es.caib.regweb3.persistence.ejb;

import es.caib.regweb3.model.RegistroEntrada;
import es.caib.regweb3.model.RegistroSalida;
import es.caib.regweb3.model.Trazabilidad;
import es.caib.regweb3.persistence.utils.Paginacion;

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
@RolesAllowed({"RWE_SUPERADMIN","RWE_ADMIN","RWE_USUARI","RWE_WS_ENTRADA","RWE_WS_SALIDA", "RWE_WS_CIUDADANO"})
public interface TrazabilidadLocal extends BaseEjb<Trazabilidad, Long> {

    List<Trazabilidad> oficiosSinREDestino(Long tipoOficio) throws Exception;
    void actualizarTrazabilidad(Long idTrazabilidad, Long idRegistro) throws Exception;

    /**
     * Obtiene todas las Trazabilidades a partir de un Identificador Intercambio
     * @param idIntercambio
     * @return
     * @throws Exception
     */
    List<Trazabilidad> getByIdIntercambio(String idIntercambio, Long idEntidad) throws Exception;

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
     * @param total
     * @return
     * @throws Exception
     */
    List<RegistroEntrada> getPendientesDistribuirSir(Long idOficina, Long idEntidad, Integer total) throws Exception;

    /**
     * Obtiene todos los RegistroEntrada validos que han sido creados a partir de una recepción SIR paginados
     * @param idOficina
     * @param idEntidad
     * @param pageNumber
     * @return
     * @throws Exception
     */
    Paginacion buscarPendientesDistribuirSir(Long idOficina, Long idEntidad, Integer pageNumber) throws Exception;

    /**
     *
     * @param idEntidad
     * @return
     * @throws Exception
     */
    Integer actualizarEstadoSirEntrada(Long idEntidad) throws Exception;

    /**
     *
     * @param idEntidad
     * @return
     * @throws Exception
     */
    Integer actualizarEstadoSirSalida(Long idEntidad) throws Exception;

}
