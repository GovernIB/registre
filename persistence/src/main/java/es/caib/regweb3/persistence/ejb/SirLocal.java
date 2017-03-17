package es.caib.regweb3.persistence.ejb;

import es.caib.regweb3.model.*;
import es.caib.regweb3.model.utils.CamposNTI;
import org.fundaciobit.genapp.common.i18n.I18NException;
import org.fundaciobit.genapp.common.i18n.I18NValidationException;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Local;
import java.util.List;

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
   * @param tipoRegistro
   * @param idRegistro
   * @param codigoEntidadRegistralDestino
   * @param denominacionEntidadRegistralDestino
   * @param oficinaActiva
   * @param usuario
   * @param idLibro
   * @throws Exception
     * @throws I18NException
     */
  public OficioRemision enviarFicheroIntercambio(String tipoRegistro, Long idRegistro, String codigoEntidadRegistralDestino, String denominacionEntidadRegistralDestino, Oficina oficinaActiva, UsuarioEntidad usuario, Long idLibro) throws Exception, I18NException;

  /**
   *
   * @param asientoRegistralSir
   * @param usuario
   * @param oficinaActiva
   * @param idLibro
   * @param idIdioma
   * @param idTipoAsunto
   * @param camposNTIs
     * @return
     */
  public RegistroEntrada aceptarAsientoRegistralSir(AsientoRegistralSir asientoRegistralSir, UsuarioEntidad usuario, Oficina oficinaActiva, Long idLibro, Long idIdioma, Long idTipoAsunto, List<CamposNTI> camposNTIs) throws Exception;

  /**
   * Transforma un {@link es.caib.regweb3.model.RegistroEntrada} en un {@link AsientoRegistralSir}
   * @param registroEntrada
   * @param codigoEntidadRegistralDestino
   * @param denominacionEntidadRegistralDestino
   * @return
   * @throws Exception
   * @throws I18NException
   * @throws I18NValidationException
     */
  public AsientoRegistralSir transformarRegistroEntrada(RegistroEntrada registroEntrada, String codigoEntidadRegistralDestino, String denominacionEntidadRegistralDestino)
          throws Exception, I18NException;

  /**
   *
   * @param asientoRegistralSir
   * @param usuario
   * @param oficinaActiva
   * @param idLibro
   * @param idIdioma
   * @param idTipoAsunto
   * @param camposNTIs
   * @return
   * @throws Exception
   * @throws I18NException
   * @throws I18NValidationException
     */
  public RegistroEntrada transformarAsientoRegistralEntrada(AsientoRegistralSir asientoRegistralSir, UsuarioEntidad usuario, Oficina oficinaActiva, Long idLibro, Long idIdioma, Long idTipoAsunto, List<CamposNTI> camposNTIs) throws Exception, I18NException, I18NValidationException;

  /**
   * Transforma un {@link es.caib.regweb3.model.RegistroEntrada} en un {@link AsientoRegistralSir}
   * @param registroSalida
   * @param codigoEntidadRegistralDestino
   * @param denominacionEntidadRegistralDestino
   * @return
   * @throws Exception
   * @throws I18NException
   * @throws I18NValidationException
   */
  public AsientoRegistralSir transformarRegistroSalida(RegistroSalida registroSalida, String codigoEntidadRegistralDestino, String denominacionEntidadRegistralDestino)
          throws Exception, I18NException;

  /**
   *
   * @param asientoRegistralSir
   * @param usuario
   * @param oficinaActiva
   * @param idLibro
   * @param idIdioma
   * @param idTipoAsunto
   * @param camposNTIs
   * @return
   * @throws Exception
   * @throws I18NException
   * @throws I18NValidationException
   */
  public RegistroSalida transformarAsientoRegistralSalida(AsientoRegistralSir asientoRegistralSir, UsuarioEntidad usuario, Oficina oficinaActiva, Long idLibro, Long idIdioma, Long idTipoAsunto, List<CamposNTI> camposNTIs) throws Exception, I18NException, I18NValidationException;

}

