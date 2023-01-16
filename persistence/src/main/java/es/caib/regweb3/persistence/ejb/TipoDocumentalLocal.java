package es.caib.regweb3.persistence.ejb;

import es.caib.regweb3.model.TipoDocumental;
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
public interface TipoDocumentalLocal extends BaseEjb<TipoDocumental, Long> {

    String JNDI_NAME = "java:app/regweb3-persistence/TipoDocumentalEJB";


    /**
     * @param idEntidad
     * @return
     * @throws I18NException
     */
    Long getTotal(Long idEntidad) throws I18NException;

    /**
     * @param inicio
     * @param idEntidad
     * @return
     * @throws I18NException
     */
    List<TipoDocumental> getPagination(int inicio, Long idEntidad) throws I18NException;

    /**
     * Retorna el {@link es.caib.regweb3.model.TipoDocumental} asociado a un codigo.
     *
     * @param codigoNTI
     * @param idEntidad
     * @return
     * @throws I18NException
     */
    TipoDocumental findByCodigoEntidad(String codigoNTI, Long idEntidad) throws I18NException;

    /**
     * Comprueba la existencia del codigo en algún TipoDocumental
     *
     * @param codigoNTI
     * @param idTipoDocumental
     * @param idEntidad
     * @return
     * @throws I18NException
     */
    Boolean existeCodigoEdit(String codigoNTI, Long idTipoDocumental, Long idEntidad) throws I18NException;

    /**
     * Obtiene los tipos documentales de una entidad
     *
     * @param idEntidad
     * @return
     * @throws I18NException
     */
    List<TipoDocumental> getByEntidad(Long idEntidad) throws I18NException;

    /**
     * Elimina los TipoDocumental de una Entidad
     *
     * @param idEntidad
     * @return
     * @throws I18NException
     */
    Integer eliminarByEntidad(Long idEntidad) throws I18NException;

    /**
     * Crea un TipoDocumental con sus traducciones en Catalán y Castellano
     *
     * @param codigo
     * @param idEntidad
     * @param nombreCa
     * @param nombreES
     * @return
     * @throws I18NException
     */
    TipoDocumental nuevoTraduccion(String codigo, Long idEntidad, String nombreCa, String nombreES) throws I18NException;


}
