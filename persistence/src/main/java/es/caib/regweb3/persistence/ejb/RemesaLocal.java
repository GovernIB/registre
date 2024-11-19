package es.caib.regweb3.persistence.ejb;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Local;

import org.fundaciobit.genapp.common.i18n.I18NException;
import org.plugin.lema.api.ConsultaAcuseReciboResponse;
import org.plugin.lema.api.Envio;
import org.plugin.lema.api.PeticionAccesoResponse;

import es.caib.regweb3.model.Entidad;
import es.caib.regweb3.model.Remesa;

/**
 * Created by Limit Tecnologies S.L.
 * @author Jamal
 */
@Local
@RolesAllowed({"RWE_SUPERADMIN","RWE_ADMIN","RWE_USUARI"})
public interface RemesaLocal extends BaseEjb<Remesa, Long> {

//	public void localizaGuardaNotificaciones(Entidad entidad) throws I18NException, Exception;
	
	public void guardaNotificacion(Envio envio, Entidad entidad) throws I18NException, Exception;

	public PeticionAccesoResponse lecturaNotificacion(String identificador, Entidad entidad) throws I18NException, Exception;

//	public Remesa findByIdentificadorWithDocumentos(String identificador, Entidad entidad) throws I18NException, Exception;

	public ConsultaAcuseReciboResponse consultaGuardaAcuseRecibo(String identificador, Entidad entidad) throws I18NException, Exception;

	public void actualizarEstadoRegistrada(String identificador, Long registroId, String estado) throws Exception;

	public Remesa findByRegistroEntrada(Long registroId);
	
	public Remesa findByIdentificador(String identificador);

}
