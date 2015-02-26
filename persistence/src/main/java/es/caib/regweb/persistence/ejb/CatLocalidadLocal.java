package es.caib.regweb.persistence.ejb;

import es.caib.regweb.model.CatLocalidad;

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

    public CatLocalidad findByCodigo(Long codigoLocalidad, Long codigoProvincia, String codigoEntidadGeografica) throws Exception;

    public CatLocalidad findByLocalidadProvincia(Long codigoLocalidad, Long codigoProvincia) throws Exception;

    public List<CatLocalidad> getByProvincia(Long idProvincia) throws Exception;

}

