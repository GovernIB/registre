package es.caib.regweb3.persistence.ejb;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Local;

import es.caib.regweb3.model.Remesa;
import es.caib.regweb3.model.RemesaAcuse;

/**
 * Created by Limit Tecnologies S.L.
 * @author Jamal
 */
@Local
@RolesAllowed({"RWE_SUPERADMIN","RWE_ADMIN","RWE_USUARI"})
public interface RemesaAcuseLocal extends BaseEjb<RemesaAcuse, Long> {

	public RemesaAcuse crearReferenciaAcuse(byte[] referencia, String csvResguardo, Remesa remesa) throws Exception;
	
	public void actualizarAcuseRecibo(String nombre, String mimeType, String metadatos);

	public RemesaAcuse findByRemesa(Long remesaId);
	
}
