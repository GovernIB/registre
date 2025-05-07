package es.caib.regweb3.persistence.ejb;

import java.util.Date;
import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Local;

import org.fundaciobit.genapp.common.i18n.I18NException;
import org.plugin.lema.api.ConsultaAcuseReciboResponse;
import org.plugin.lema.api.Envio;
import org.plugin.lema.api.PeticionAccesoResponse;

import es.caib.notib.client.domini.NotificacioV2;
import es.caib.notib.client.domini.RespostaAlta;
import es.caib.regweb3.model.Entidad;
import es.caib.regweb3.model.RegistroEntrada;
import es.caib.regweb3.model.Remesa;
import es.caib.regweb3.model.UsuarioEntidad;

/**
 * Created by Limit Tecnologies S.L.
 * @author Jamal
 */
@Local
@RolesAllowed({"RWE_SUPERADMIN","RWE_ADMIN","RWE_USUARI"})
public interface RemesaLocal extends BaseEjb<Remesa, Long> {

//	public void localizaGuardaNotificaciones(Entidad entidad) throws I18NException, Exception;
	
	public List<Remesa> guardarNotificacion(NotificacioV2 envio, Entidad entidad, RegistroEntrada registroEntrada, UsuarioEntidad usuario) throws I18NException, Exception;
	
	public void actualizarNotificacionEnviada(List<Remesa> remesas, RespostaAlta respuesta) throws I18NException, Exception;
	
	public void guardarNotificacionRecibida(Envio envio, Entidad entidad) throws I18NException, Exception;

	public PeticionAccesoResponse lecturaNotificacion(String identificador, UsuarioEntidad usuarioEntidad, Entidad entidad) throws I18NException, Exception;

//	public Remesa findByIdentificadorWithDocumentos(String identificador, Entidad entidad) throws I18NException, Exception;

	public ConsultaAcuseReciboResponse consultaGuardaAcuseRecibo(String identificador, Entidad entidad) throws I18NException, Exception;

	public void actualizarEstadoRegistrada(String identificador, Long registroId, String estado) throws Exception;

	public void actualizarEstado(String identificador, String estado) throws Exception;

	public void actualizarEstadoNotifica(String identificador, String referencia, String estado, Date fechaEstado, String estadoNotifica, Date fechaCreacion, Date fechaEnviada, Date fechaFinalizada);

	public void notificacionActualitzarEstado(String identificadorNotib, String referenciaEnviament, Entidad entidad);

}
