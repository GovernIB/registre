package es.caib.regweb3.persistence.ejb;

import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Local;

import org.fundaciobit.genapp.common.i18n.I18NException;
import org.plugin.geiser.api.ApunteRegistro;

import es.caib.regweb3.model.IRegistro;
import es.caib.regweb3.model.RegistroSir;
import es.caib.regweb3.model.UsuarioEntidad;
import es.caib.regweb3.model.utils.CamposNTI;
import es.caib.regweb3.persistence.utils.ProgresoActualitzacion;

/**
 *
 * @author Limit Tecnologies
 */
@Local
@RolesAllowed({"RWE_SUPERADMIN","RWE_ADMIN","RWE_USUARI", "RWE_WS_ENTRADA", "RWE_WS_SALIDA"})
public interface RegistroSirHelperLocal extends BaseEjb<RegistroSir, Long> {

	Integer crearRegistroSirRecibido(ApunteRegistro apunteRegistroBusquedaFiltrado, Long entidadId, Integer total,
			ProgresoActualitzacion progreso) throws Exception, I18NException;

	IRegistro actualizarMetadatosRegistro(Long anexoId, String codigoEntidad, IRegistro registro,
			UsuarioEntidad usuario, List<CamposNTI> camposNTIs);
}