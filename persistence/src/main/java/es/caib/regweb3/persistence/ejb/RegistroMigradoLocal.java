package es.caib.regweb3.persistence.ejb;

import es.caib.regweb3.model.RegistroMigrado;
import es.caib.regweb3.persistence.utils.Paginacion;
import org.fundaciobit.genapp.common.i18n.I18NException;

import javax.ejb.Local;
import java.util.Date;
import java.util.List;

/**
 * Created by Fundació BIT.
 *
 * @author jpernia
 * Date: 11/11/14
 */
@Local
public interface RegistroMigradoLocal extends BaseEjb<RegistroMigrado, Long> {

    String JNDI_NAME = "java:app/regweb3-persistence/RegistroMigradoEJB";


    /**
     * Obtiene una lista de Registros Migrados de una entidad
     *
     * @param entidad código de la entidad
     * @return
     * @throws I18NException
     */
    Boolean tieneRegistrosMigrados(Long entidad) throws I18NException;

    /**
     * Busca los Registros Migrados en función de los parámetros
     *
     * @param pageNumber
     * @param fechaInicio
     * @param fechaFin
     * @param registroMigrado
     * @param numeroRegistro
     * @param anoRegistro
     * @param idEntidad
     * @return
     * @throws I18NException
     */
    Paginacion busqueda(Integer pageNumber, Date fechaInicio, Date fechaFin, Integer numeroRegistro, Integer anoRegistro, RegistroMigrado registroMigrado, Long idEntidad) throws I18NException;

    /**
     * Busca las Oficinas existentes en los Registros Migrados
     *
     * @return
     * @throws I18NException
     */
    List<String[]> getOficinas() throws I18NException;

    /**
     * Inserta la consulta de un Registro Migrado en las tablas de Lopd
     *
     * @param idRegistroMigrado
     * @param idUsuarioEntidad
     * @throws I18NException
     */
    void insertarRegistroLopdMigrado(Long idRegistroMigrado, Long idUsuarioEntidad) throws I18NException;

    /**
     * Inserta la búsqueda de un Registros Migrado en las tablas de Lopd
     *
     * @param paginacion
     * @param idUsuarioEntidad
     * @throws I18NException
     */
    void insertarRegistrosLopdMigrado(Paginacion paginacion, Long idUsuarioEntidad) throws I18NException;

    /**
     * Elimina todos los RegistroMigrado de la Entidad
     *
     * @param idEntidad
     * @return
     * @throws I18NException
     */
    Integer eliminarByEntidad(Long idEntidad) throws I18NException;

}