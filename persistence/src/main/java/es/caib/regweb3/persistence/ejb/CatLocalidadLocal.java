package es.caib.regweb3.persistence.ejb;

import es.caib.regweb3.model.CatLocalidad;
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
public interface CatLocalidadLocal extends BaseEjb<CatLocalidad, Long> {

    String JNDI_NAME = "java:app/regweb3-persistence/CatLocalidadEJB";

    /**
     *
     * @param codigoLocalidad
     * @param codigoProvincia
     * @param codigoEntidadGeografica
     * @return
     * @throws I18NException
     */
    CatLocalidad findByCodigo(Long codigoLocalidad, Long codigoProvincia, String codigoEntidadGeografica) throws I18NException;

    /**
     *
     * @param codigoLocalidad
     * @param codigoProvincia
     * @return
     * @throws I18NException
     */
    CatLocalidad findByLocalidadProvincia(Long codigoLocalidad, Long codigoProvincia) throws I18NException;

    /**
     *
     * @param idProvincia
     * @return
     * @throws I18NException
     */
    List<CatLocalidad> getByProvincia(Long idProvincia) throws I18NException;

    /**
     *
     * @param codigoProvincia
     * @return
     * @throws I18NException
     */
    List<Object[]> getByCodigoProvinciaObject(Long codigoProvincia) throws I18NException;

    /**
     *
     * @param codigoProvincia
     * @return
     * @throws I18NException
     */
    List<CatLocalidad> getByCodigoProvincia(Long codigoProvincia) throws I18NException;

    /**
     * Obtiene la Localidad a partir de su nombre
     * @param nombre
     * @return
     * @throws I18NException
     */
    CatLocalidad findByNombre(String nombre) throws I18NException;

}

