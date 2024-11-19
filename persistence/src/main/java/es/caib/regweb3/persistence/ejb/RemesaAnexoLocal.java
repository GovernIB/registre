package es.caib.regweb3.persistence.ejb;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Local;

import es.caib.regweb3.model.Remesa;
import es.caib.regweb3.model.RemesaAnexo;

/**
 * Created by Limit Tecnologies S.L.
 * @author Jamal
 */
@Local
@RolesAllowed({"RWE_SUPERADMIN","RWE_ADMIN","RWE_USUARI"})
public interface RemesaAnexoLocal extends BaseEjb<RemesaAnexo, Long> {

	public RemesaAnexo crearReferenciaAnexo(String enlace, byte[] referencia, Remesa remesa) throws Exception;

	public RemesaAnexo findByRemesa(Long remesaId);
	
}
