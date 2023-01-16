package es.caib.regweb3.persistence.ejb;

import es.caib.regweb3.model.RegistroEntrada;
import es.caib.regweb3.model.RegistroSalida;
import es.caib.regweb3.model.Trazabilidad;
import es.caib.regweb3.persistence.utils.Paginacion;
import org.fundaciobit.genapp.common.i18n.I18NException;

import javax.ejb.Local;
import java.util.List;

/**
 * Created by Fundació BIT.
 *
 * @author earrivi
 * Date: 16/01/14
 */
@Local
public interface TrazabilidadLocal extends BaseEjb<Trazabilidad, Long> {

    String JNDI_NAME = "java:app/regweb3-persistence/TrazabilidadEJB";


    /**
     * @param tipoOficio
     * @return
     * @throws I18NException
     */
    List<Trazabilidad> oficiosSinREDestino(Long tipoOficio) throws I18NException;

    /**
     * @param idTrazabilidad
     * @param idRegistro
     * @throws I18NException
     */
    void actualizarTrazabilidad(Long idTrazabilidad, Long idRegistro) throws I18NException;

    /**
     * Obtiene todas las Trazabilidades a partir de un Identificador Intercambio
     *
     * @param idIntercambio
     * @return
     * @throws I18NException
     */
    List<Trazabilidad> getByIdIntercambio(String idIntercambio, Long idEntidad) throws I18NException;

    /**
     * Obtiene la Trazabilidad de un RegistroSalida
     *
     * @param idRegistroSalida
     * @return
     * @throws I18NException
     */
    List<Trazabilidad> getByRegistroSalida(Long idRegistroSalida) throws I18NException;

    /**
     * Obtiene la Trazabilidad de un RegistroEntrada
     *
     * @param idRegistroEntrada
     * @return
     * @throws I18NException
     */
    List<Trazabilidad> getByRegistroEntrada(Long idRegistroEntrada) throws I18NException;

    /**
     * Obtiene las Trazabilidades de un OficioRemision
     *
     * @param idOficioRemision
     * @return
     * @throws I18NException
     */
    List<Trazabilidad> getByOficioRemision(Long idOficioRemision) throws I18NException;

    /**
     * Obtiene la Trazabilidad correspondiente a un OficioRemision y a un RegistroEntrada
     *
     * @param idOficioRemision
     * @param idRegistroEntrada
     * @return
     * @throws I18NException
     */
    Trazabilidad getByOficioRegistroEntrada(Long idOficioRemision, Long idRegistroEntrada) throws I18NException;

    /**
     * Obtiene la Trazabilidad correspondiente a un OficioRemision y a un RegistroSalida
     *
     * @param idOficioRemision
     * @param idRegistroSalida
     * @return
     * @throws I18NException
     */
    Trazabilidad getByOficioRegistroSalida(Long idOficioRemision, Long idRegistroSalida) throws I18NException;

    /**
     * Obtiene las Trazabilidades de un RegistroSir
     *
     * @param idRegistroSir
     * @return
     * @throws I18NException
     */
    List<Trazabilidad> getByRegistroSir(Long idRegistroSir) throws I18NException;

    /**
     * Obtiene el Registro de Entrada generado a partir de la aceptación de un Registro Sir
     *
     * @param idRegistroSir
     * @return
     * @throws I18NException
     */
    RegistroEntrada getRegistroAceptado(Long idRegistroSir) throws I18NException;

    /**
     * Elimina las Trazabilidades de una Entidad
     *
     * @param idEntidad
     * @return
     * @throws I18NException
     */
    Integer eliminarByEntidad(Long idEntidad) throws I18NException;

    /**
     * Obtiene los RegistroSalida generados en un OficioRemision
     *
     * @param idOficioRemision
     * @return
     */
    List<RegistroSalida> obtenerRegistrosSalida(Long idOficioRemision) throws I18NException;

    /**
     * Obtiene los RegistroEntrada validos que han sido creados a partir de una recepción SIR
     *
     * @param idOficina
     * @param idEntidad
     * @param total
     * @return
     * @throws I18NException
     */
    List<RegistroEntrada> getPendientesDistribuirSir(Long idOficina, Long idEntidad, Integer total) throws I18NException;

    /**
     * Obtiene todos los RegistroEntrada validos que han sido creados a partir de una recepción SIR paginados
     *
     * @param idOficina
     * @param idEntidad
     * @param pageNumber
     * @return
     * @throws I18NException
     */
    Paginacion buscarPendientesDistribuirSir(Long idOficina, Long idEntidad, Integer pageNumber) throws I18NException;

    /**
     * @param idEntidad
     * @return
     * @throws I18NException
     */
    Integer actualizarEstadoSirEntrada(Long idEntidad) throws I18NException;

    /**
     * @param idEntidad
     * @return
     * @throws I18NException
     */
    Integer actualizarEstadoSirSalida(Long idEntidad) throws I18NException;

}
