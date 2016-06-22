package es.caib.regweb3.persistence.ejb;

import es.caib.regweb3.model.Oficina;
import es.caib.regweb3.model.RegistroEntrada;
import es.caib.regweb3.model.RegistroSalida;
import es.caib.regweb3.model.UsuarioEntidad;
import es.caib.regweb3.sir.core.model.AsientoRegistralSir;
import org.fundaciobit.genapp.common.i18n.I18NException;
import org.fundaciobit.genapp.common.i18n.I18NValidationException;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Local;

/**
 * Created by Fundació BIT.
 *
 * @author earrivi
 * Date: 22/06/16
 */
@Local
@RolesAllowed({"RWE_SUPERADMIN","RWE_ADMIN","RWE_USUARI"})
public interface SirLocal {

  /**
   *
   * @param asientoRegistralSir
   * @param usuario
   * @param oficinaActiva
   * @param idLibro
   * @param idIdioma
   * @param idTipoAsunto
   * @return
     * @throws Exception
     */
  public RegistroEntrada transformarRegistroEntrada(AsientoRegistralSir asientoRegistralSir, UsuarioEntidad usuario, Oficina oficinaActiva, Long idLibro, Long idIdioma, Long idTipoAsunto) throws Exception, I18NException, I18NValidationException;

  /**
   *
   * @param asientoRegistralSir
   * @param usuario
   * @param oficinaActiva
   * @param idLibro
   * @param idIdioma
   * @param idTipoAsunto
   * @return
   * @throws Exception
   */
  public RegistroSalida transformarRegistroSalida(AsientoRegistralSir asientoRegistralSir, UsuarioEntidad usuario, Oficina oficinaActiva, Long idLibro, Long idIdioma, Long idTipoAsunto) throws Exception, I18NException, I18NValidationException;

}

