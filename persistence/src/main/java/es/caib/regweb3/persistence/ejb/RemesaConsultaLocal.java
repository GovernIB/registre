package es.caib.regweb3.persistence.ejb;

import java.util.Date;
import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Local;

import org.fundaciobit.genapp.common.i18n.I18NException;

import es.caib.regweb3.model.Entidad;
import es.caib.regweb3.model.Remesa;
import es.caib.regweb3.persistence.utils.Paginacion;

/**
 * Created by Limit Tecnologies S.L.
 * 
 * @author Jamal
 */
@Local
@RolesAllowed({ "RWE_SUPERADMIN", "RWE_ADMIN", "RWE_USUARI" })
public interface RemesaConsultaLocal {

	public List<Remesa> getByEntidad(Long idEntidad) throws Exception;

	public Paginacion busqueda(Integer pageNumber, Remesa remesa, String emisor, Date fechaPuestaDisposicionDesde,
			Date fechaPuestaDisposicionHasta, Long idEntidad) throws Exception;

	public Long remesasPendientes(Long idEntidad) throws Exception;

	public void localizaGuardaNotificaciones(Entidad entidad) throws I18NException, Exception;

	public List<Remesa> getByEntidadAndEstado(Long idEntidad, String estado) throws Exception;
}
