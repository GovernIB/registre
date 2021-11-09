package es.caib.regweb3.persistence.ejb;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Local;

import org.fundaciobit.genapp.common.i18n.I18NException;
import org.plugin.geiser.api.ApunteRegistro;

import es.caib.regweb3.model.RegistroSir;
import es.caib.regweb3.persistence.utils.ProgresoActualitzacion;

/**
 *
 * @author Limit Tecnologies
 */
@Local
@RolesAllowed({"RWE_SUPERADMIN","RWE_ADMIN","RWE_USUARI", "RWE_WS_ENTRADA", "RWE_WS_SALIDA"})
public interface RegistroSirHelperLocal extends BaseEjb<RegistroSir, Long> {

	void crearRegistroSirRecibido(ApunteRegistro apunteRegistroBusquedaFiltrado, Long entidadId, Integer total,
			ProgresoActualitzacion progreso) throws Exception, I18NException;
}