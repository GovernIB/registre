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

    public CatLocalidad findByCodigo(Long codigoLocalidad, Long codigoProvincia, String codigoEntidadGeografica) throws Exception;

    public CatLocalidad findByLocalidadProvincia(Long codigoLocalidad, Long codigoProvincia) throws Exception;

    public List<CatLocalidad> getByProvincia(Long idProvincia) throws Exception;

    /**
     * Obtiene la Localidad a partir de su nombre
     * @param nombre
     * @return
     * @throws Exception
     */
    public CatLocalidad findByNombre(String nombre) throws Exception;

}

