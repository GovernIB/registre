package es.caib.regweb3.persistence.ejb;

import es.caib.regweb3.model.RegistroMigrado;
import es.caib.regweb3.persistence.utils.Paginacion;

import javax.annotation.security.RolesAllowed;
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
@RolesAllowed({"RWE_USUARI"})
public interface RegistroMigradoLocal extends BaseEjb<RegistroMigrado, Long> {

    /**
     * Obtiene una lista de Registros Migrados de una entidad
     * @param entidad código de la entidad
     * @return
     * @throws Exception
     */
    public Boolean tieneRegistrosMigrados(Long entidad) throws Exception;

    /**
     * Busca los Registros Migrados en función de los parámetros
     * @param pageNumber
     * @param fechaInicio
     * @param fechaFin
     * @param registroMigrado
     * @param numeroRegistro
     * @param anoRegistro
     * @param idEntidad
     * @return
     * @throws Exception
     */
    public Paginacion busqueda(Integer pageNumber, Date fechaInicio, Date fechaFin, Integer numeroRegistro, Integer anoRegistro, RegistroMigrado registroMigrado, Long idEntidad) throws Exception;

    /**
     * Busca las Oficinas existentes en los Registros Migrados
     * @return
     * @throws Exception
     */
    public List<String[]> getOficinas() throws Exception;

    /**
     * Inserta la consulta de un Registro Migrado en las tablas de Lopd
     * @param idRegistroMigrado
     * @param idUsuarioEntidad
     * @throws Exception
     */
    public void insertarRegistroLopdMigrado(Long idRegistroMigrado, Long idUsuarioEntidad) throws Exception;

    /**
     * Inserta la búsqueda de un Registros Migrado en las tablas de Lopd
     * @param paginacion
     * @param idUsuarioEntidad
     * @throws Exception
     */
    public void insertarRegistrosLopdMigrado(Paginacion paginacion, Long idUsuarioEntidad) throws Exception;

    /**
     * Elimina todos los RegistroMigrado de la Entidad
     * @param idEntidad
     * @return
     * @throws Exception
     */
    public Integer eliminarByEntidad(Long idEntidad) throws Exception;

}