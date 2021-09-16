package es.caib.regweb3.persistence.ejb;

import es.caib.regweb3.model.PreRegistro;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Local;

/**
 * Created by Fundació BIT.
 *
 * @author jpernia
 * Date: 09/12/14
 */
@Local
public interface PreRegistroLocal extends BaseEjb<PreRegistro, Long> {



}
