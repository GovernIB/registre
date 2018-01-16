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
@RolesAllowed({"RWE_SUPERADMIN","RWE_ADMIN","RWE_USUARI"})
public interface CatLocalidadLocal extends BaseEjb<CatLocalidad, Long> {

    CatLocalidad findByCodigo(Long codigoLocalidad, Long codigoProvincia, String codigoEntidadGeografica) throws Exception;

    CatLocalidad findByLocalidadProvincia(Long codigoLocalidad, Long codigoProvincia) throws Exception;

    List<CatLocalidad> getByProvincia(Long idProvincia) throws Exception;

    List<Object[]> getByCodigoProvinciaObject(Long codigoProvincia) throws Exception;

    List<CatLocalidad> getByCodigoProvincia(Long codigoProvincia) throws Exception;

    /**
     * Obtiene la Localidad a partir de su nombre
     * @param nombre
     * @return
     * @throws Exception
     */
    CatLocalidad findByNombre(String nombre) throws Exception;

}

