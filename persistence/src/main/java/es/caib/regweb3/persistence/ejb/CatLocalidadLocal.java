package es.caib.regweb3.persistence.ejb;

import es.caib.regweb3.model.CatLocalidad;

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
public interface CatLocalidadLocal extends BaseEjb<CatLocalidad, Long> {

    /**
     *
     * @param codigoLocalidad
     * @param codigoProvincia
     * @param codigoEntidadGeografica
     * @return
     * @throws Exception
     */
    CatLocalidad findByCodigo(Long codigoLocalidad, Long codigoProvincia, String codigoEntidadGeografica) throws Exception;

    /**
     *
     * @param codigoLocalidad
     * @param codigoProvincia
     * @return
     * @throws Exception
     */
    CatLocalidad findByLocalidadProvincia(Long codigoLocalidad, Long codigoProvincia) throws Exception;

    /**
     *
     * @param idProvincia
     * @return
     * @throws Exception
     */
    List<CatLocalidad> getByProvincia(Long idProvincia) throws Exception;

    /**
     *
     * @param codigoProvincia
     * @return
     * @throws Exception
     */
    List<Object[]> getByCodigoProvinciaObject(Long codigoProvincia) throws Exception;

    /**
     *
     * @param codigoProvincia
     * @return
     * @throws Exception
     */
    List<CatLocalidad> getByCodigoProvincia(Long codigoProvincia) throws Exception;

    /**
     * Obtiene la Localidad a partir de su nombre
     * @param nombre
     * @return
     * @throws Exception
     */
    CatLocalidad findByNombre(String nombre) throws Exception;

}

