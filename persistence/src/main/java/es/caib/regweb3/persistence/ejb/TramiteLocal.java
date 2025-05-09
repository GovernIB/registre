package es.caib.regweb3.persistence.ejb;

import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Local;

import org.fundaciobit.genapp.common.i18n.I18NException;

import es.caib.regweb3.model.Entidad;
import es.caib.regweb3.model.RegistroEntrada;
import es.caib.regweb3.model.Remesa;
import es.caib.regweb3.model.UsuarioEntidad;
import es.caib.regweb3.model.utils.ClasificacionDto;
import es.caib.regweb3.model.utils.TramiteDto;
import es.caib.regweb3.persistence.utils.DocumentoDto;

@Local
@RolesAllowed({ "RWE_USUARI" })
public interface TramiteLocal {

	public List<TramiteDto> getTramitesRolsac(String lang) throws Exception;

	public void clasificarRegistro(RegistroEntrada registroEntrada, UsuarioEntidad usuarioEntidad,
			ClasificacionDto clasificacionForm, Entidad entidad) throws Exception, I18NException;

	public DocumentoDto descargarCertificacion(String identificadorNotib, String referenciaEnviament, Entidad entidad)
			throws I18NException, Exception;

	public void notificacionActualitzarEstado(String identificadorNotib, String referenciaEnviament, Remesa remesa)
			throws Exception, I18NException;

//  public DocumentoDto descargarCertificacion(String identificadorNotib, String referenciaEnviament, Entidad entidad) throws I18NException, Exception;

}
