package es.caib.regweb.persistence.ejb;

import es.caib.regweb.model.Idioma;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Local;

/**
 * Created by Fundació BIT.
 *
 * @author earrivi
 *         Date: 16/01/14
 */
@Local
@RolesAllowed({"RWE_SUPERADMIN","RWE_ADMIN","RWE_USUARI"})
public interface IdiomaLocal extends BaseEjb<Idioma, Long> {
}
