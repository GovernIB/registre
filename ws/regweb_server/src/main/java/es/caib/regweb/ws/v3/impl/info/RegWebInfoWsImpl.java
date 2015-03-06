package es.caib.regweb.ws.v3.impl.info;

import es.caib.regweb.model.*;
import es.caib.regweb.persistence.ejb.*;

import es.caib.regweb.utils.RegwebConstantes;
import es.caib.regweb.ws.model.CodigoAsuntoWs;
import es.caib.regweb.ws.model.LibroWs;
import es.caib.regweb.ws.model.OrganismoWs;
import es.caib.regweb.ws.model.TipoAsuntoWs;
import es.caib.regweb.ws.utils.AuthenticatedBaseWsImpl;
import es.caib.regweb.ws.utils.UsuarioAplicacionCache;
import es.caib.regweb.ws.v3.impl.CommonConverter;
import org.apache.log4j.Logger;
import org.fundaciobit.genapp.common.ws.WsI18NException;
import org.jboss.ejb3.annotation.SecurityDomain;
import org.jboss.wsf.spi.annotation.TransportGuarantee;
import org.jboss.wsf.spi.annotation.WebContext;

//import org.springframework.stereotype.Component;

import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Fundació BIT.
 * 
 * @author anadal
 */
@SecurityDomain(RegwebConstantes.SECURITY_DOMAIN)
@Stateless(name = RegWebInfoWsImpl.NAME + "Ejb")
@RolesAllowed({ RegwebConstantes.ROL_SUPERADMIN })
@SOAPBinding(style = SOAPBinding.Style.RPC)
@org.apache.cxf.interceptor.InInterceptors(interceptors = { "es.caib.regweb.ws.utils.RegWebInInterceptor" })
@org.apache.cxf.interceptor.InFaultInterceptors(interceptors = { "es.caib.regweb.ws.utils.RegWebInInterceptor" })
@WebService(name = RegWebInfoWsImpl.NAME_WS, portName = RegWebInfoWsImpl.NAME_WS, serviceName = RegWebInfoWsImpl.NAME_WS
    + "Service", endpointInterface = "es.caib.regweb.ws.v3.impl.info.RegWebInfoWs")
@WebContext(contextRoot = "/regweb/ws", urlPattern = "/v3/" + RegWebInfoWsImpl.NAME, transportGuarantee = TransportGuarantee.NONE, secureWSDLAccess = false, authMethod = "WSBASIC")
//@Component
public class RegWebInfoWsImpl extends AuthenticatedBaseWsImpl implements RegWebInfoWs {

  protected final Logger log = Logger.getLogger(getClass());

  public static final String NAME = "RegWebInfo";

  public static final String NAME_WS = NAME + "Ws";

  /*
   * @Autowired RegistroSalidaValidator validator = new
   * RegistroSalidaValidator();
   * 
   * @EJB(mappedName = "regweb/OficinaEJB/local") public OficinaLocal
   * oficinaEjb;
   * 
   * @EJB(mappedName = "regweb/OrganismoEJB/local") public OrganismoLocal
   * organismoEjb;
   * 
   * @EJB(mappedName = "regweb/PermisoLibroUsuarioEJB/local") public
   * PermisoLibroUsuarioLocal permisoLibroUsuarioEjb;
   * 
   * @EJB(mappedName = "regweb/UsuarioEntidadEJB/local") public
   * UsuarioEntidadLocal usuarioEntidadEjb;
   * 
   * 
   * 
   * @EJB(mappedName = "regweb/IdiomaRegistroEJB/local") public
   * IdiomaRegistroLocal idiomaRegistroEjb;
   * 
   * 
   * 
   * @EJB(mappedName = "regweb/TransporteEJB/local") public TransporteLocal
   * transporteEjb;
   * 
   * @EJB(mappedName = "regweb/CodigoAsuntoEJB/local") public CodigoAsuntoLocal
   * codigoAsuntoEjb;
   * 
   * @EJB(mappedName = "regweb/TipoDocumentacionFisicaEJB/local") public
   * TipoDocumentacionFisicaLocal tipoDocumentacionFisicaEjb;
   * 
   * @EJB(mappedName = "regweb/RegistroSalidaEJB/local") public
   * RegistroSalidaLocal registroSalidaEjb;
   * 
   * @EJB(mappedName = "regweb/HistoricoRegistroSalidaEJB/local") public
   * HistoricoRegistroSalidaLocal historicoRegistroSalidaEjb;
   * 
   * @EJB(mappedName = "regweb/LopdEJB/local") public LopdLocal lopdEjb;
   */

  @EJB(mappedName = "regweb/EntidadEJB/local")
  public EntidadLocal entidadEjb;

  @EJB(mappedName = "regweb/LibroEJB/local")
  public LibroLocal libroEjb;

  @EJB(mappedName = "regweb/TipoAsuntoEJB/local")
  public TipoAsuntoLocal tipoAsuntoEjb;
  
  @EJB(mappedName = "regweb/OrganismoEJB/local")
  public OrganismoLocal organismoEjb;

  @EJB(mappedName = "regweb/CodigoAsuntoEJB/local")
  public CodigoAsuntoLocal codigoAsuntoEjb;

  /**
   * 
   * @param usuario
   * @param entidadCodigoDir3
   * @return
   * @throws Throwable
   * @throws WsI18NException
   */
  @Override
  @WebMethod
  @RolesAllowed({ RegwebConstantes.ROL_USUARI })
  public List<TipoAsuntoWs> listarTipoAsunto(@WebParam(name = "usuario") String usuario,
      @WebParam(name = "entidadCodigoDir3") String entidadCodigoDir3) throws Throwable,
      WsI18NException {

    // TODO Checks
    Entidad entidadObj = CommonConverter.getEntidad(entidadCodigoDir3, entidadEjb);

    List<TipoAsunto> tipos = tipoAsuntoEjb.getAll(entidadObj.getId());

    List<TipoAsuntoWs> tiposWs = new ArrayList<TipoAsuntoWs>();

    final String idioma = RegwebConstantes.CODIGO_BY_IDIOMA_ID.get(UsuarioAplicacionCache.get().getUsuario().getIdioma());

    for (TipoAsunto tipoAsunto : tipos) {
      tiposWs.add(CommonConverter.getTipoAsuntoWs(tipoAsunto, idioma));
    }

    return tiposWs;

  }

  /**
   *
   * @param usuario
   * @param codigoTipoAsunto
   * @return
   * @throws Throwable
   * @throws WsI18NException
   */
  @Override
  @WebMethod
  @RolesAllowed({ RegwebConstantes.ROL_USUARI })
  public List<CodigoAsuntoWs> listarCodigoAsunto(@WebParam(name = "usuario") String usuario,
      @WebParam(name = "codigoTipoAsunto") String codigoTipoAsunto) throws Throwable,
      WsI18NException {

    // TODO Checks
    TipoAsunto tipoAsuntoObj = CommonConverter.getTipoAsunto(codigoTipoAsunto,tipoAsuntoEjb);

    List<CodigoAsunto> codigoAsuntos = codigoAsuntoEjb.getByTipoAsunto(tipoAsuntoObj.getId());

    List<CodigoAsuntoWs> codigosWs = new ArrayList<CodigoAsuntoWs>();

    final String idioma = RegwebConstantes.CODIGO_BY_IDIOMA_ID.get(UsuarioAplicacionCache.get().getUsuario().getIdioma());

    for (CodigoAsunto codigoAsunto : codigoAsuntos) {
      codigosWs.add(CommonConverter.getCodigoAsuntoWs(codigoAsunto, idioma));
    }

    return codigosWs;

  }

  @Override
  @WebMethod
  @RolesAllowed({ RegwebConstantes.ROL_USUARI })
  public List<LibroWs> listarLibros(@WebParam(name = "usuario") String usuario,
      @WebParam(name = "entidadCodigoDir3") String entidadCodigoDir3,
      @WebParam(name = "autorizacion") String autorizacion) throws Throwable, WsI18NException {

    // TODO com proces lo d'AUTORIZACION: CE, CS , CV 
    
    // TODO Checks
    Entidad entidadObj = CommonConverter.getEntidad(entidadCodigoDir3, entidadEjb);

    List<Libro> listLibro = libroEjb.getLibrosEntidad(entidadObj.getId());

    List<LibroWs> listLibroWs = new ArrayList<LibroWs>(listLibro.size());

    for (Libro libro : listLibro) {
      listLibroWs.add(CommonConverter.getLibroWs(libro));
    }

    return listLibroWs;

  }
  
  
  @Override
  @WebMethod
  @RolesAllowed({ RegwebConstantes.ROL_USUARI })
  public List<OrganismoWs> listarOrganismos(
      @WebParam(name = "entidadCodigoDir3") String entidadCodigoDir3)
      throws Throwable, WsI18NException {

    // TODO Checks
    Entidad entidadObj = CommonConverter.getEntidad(entidadCodigoDir3, entidadEjb);

    List<Organismo> listOrganismo = organismoEjb.getAllByEntidad(entidadObj.getId());

    List<OrganismoWs> listOrganismoWs = new ArrayList<OrganismoWs>(listOrganismo.size());

    for (Organismo libro : listOrganismo) {
      listOrganismoWs.add(CommonConverter.getOrganismoWs(libro));
    }

    return listOrganismoWs;
  }
  

}
