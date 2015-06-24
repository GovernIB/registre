package es.caib.regweb3.persistence.ejb;


import es.caib.regweb3.model.Oficina;
import es.caib.regweb3.model.OficioRemision;
import es.caib.regweb3.model.RegistroEntrada;
import es.caib.regweb3.model.UsuarioEntidad;
import es.caib.regweb3.model.utils.OficioPendienteLlegada;
import org.fundaciobit.genapp.common.i18n.I18NException;
import org.fundaciobit.genapp.common.i18n.I18NValidationException;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Local;
import java.util.List;


/**
 * Created by Fundació BIT.
 *
 * @author earrivi
 * @author anadal (EJB)
 * Date: 16/01/14
 */
@Local
@RolesAllowed({"RWE_SUPERADMIN","RWE_ADMIN","RWE_USUARI"})
public interface OficioRemisionUtilsLocal  {
  
  public OficioRemision crearOficioRemisionInterno(List<RegistroEntrada> registrosEntrada,
      Oficina oficinaActiva, UsuarioEntidad usuarioEntidad,
      Long idOrganismo, Long idLibro) throws Exception, I18NException, I18NValidationException;

  
  public OficioRemision crearOficioRemisionExterno(List<RegistroEntrada> registrosEntrada,
      Oficina oficinaActiva, UsuarioEntidad usuarioEntidad, String organismoExterno,
      String organismoExternoDenominacion, Long idLibro,
      String identificadorIntercambioSir) throws Exception, I18NException, I18NValidationException;
  
  
  public List<RegistroEntrada> procesarOficioRemision(OficioRemision oficioRemision,
      UsuarioEntidad usuario, Oficina oficinaActiva,
      List<OficioPendienteLlegada> oficios) throws Exception, I18NException, I18NValidationException;
  
}
