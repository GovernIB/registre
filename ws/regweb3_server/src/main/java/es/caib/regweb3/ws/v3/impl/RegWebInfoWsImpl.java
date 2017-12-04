package es.caib.regweb3.ws.v3.impl;

import es.caib.regweb3.model.*;
import es.caib.regweb3.persistence.ejb.*;
import es.caib.regweb3.utils.RegwebConstantes;
import es.caib.regweb3.utils.StringUtils;
import es.caib.regweb3.ws.model.*;
import es.caib.regweb3.ws.utils.AuthenticatedBaseWsImpl;
import es.caib.regweb3.ws.utils.UsuarioAplicacionCache;
import org.apache.log4j.Logger;
import org.fundaciobit.genapp.common.i18n.I18NException;
import org.fundaciobit.genapp.common.ws.WsI18NException;
import org.jboss.ejb3.annotation.SecurityDomain;
import org.jboss.wsf.spi.annotation.TransportGuarantee;
import org.jboss.wsf.spi.annotation.WebContext;

import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

//import org.springframework.stereotype.Component;

/**
 * Created by Fundació BIT.
 * 
 * @author anadal
 */
@SecurityDomain(RegwebConstantes.SECURITY_DOMAIN)
@Stateless(name = RegWebInfoWsImpl.NAME + "Ejb")
@RolesAllowed({ RegwebConstantes.ROL_SUPERADMIN })
@SOAPBinding(style = SOAPBinding.Style.RPC)
@org.apache.cxf.interceptor.InInterceptors(interceptors = { "es.caib.regweb3.ws.utils.RegWebInInterceptor" })
@org.apache.cxf.interceptor.InFaultInterceptors(interceptors = { "es.caib.regweb3.ws.utils.RegWebInInterceptor" })
@WebService(name = RegWebInfoWsImpl.NAME_WS, portName = RegWebInfoWsImpl.NAME_WS, serviceName = RegWebInfoWsImpl.NAME_WS
    + "Service", endpointInterface = "es.caib.regweb3.ws.v3.impl.RegWebInfoWs")
@WebContext(contextRoot = "/regweb3/ws", urlPattern = "/v3/" + RegWebInfoWsImpl.NAME, transportGuarantee = TransportGuarantee.NONE, secureWSDLAccess = false, authMethod = "WSBASIC")
//@Component
public class RegWebInfoWsImpl extends AuthenticatedBaseWsImpl implements RegWebInfoWs {

  protected final Logger log = Logger.getLogger(getClass());

  public static final String NAME = "RegWebInfo";

  public static final String NAME_WS = NAME + "Ws";


  @EJB(mappedName = "regweb3/EntidadEJB/local")
  public EntidadLocal entidadEjb;

  @EJB(mappedName = "regweb3/LibroEJB/local")
  public LibroLocal libroEjb;

  @EJB(mappedName = "regweb3/TipoAsuntoEJB/local")
  public TipoAsuntoLocal tipoAsuntoEjb;

  @EJB(mappedName = "regweb3/TipoDocumentalEJB/local")
  public TipoDocumentalLocal tipoDocumentalEjb;
  
  @EJB(mappedName = "regweb3/OrganismoEJB/local")
  public OrganismoLocal organismoEjb;

  @EJB(mappedName = "regweb3/CodigoAsuntoEJB/local")
  public CodigoAsuntoLocal codigoAsuntoEjb;

  @EJB(mappedName = "regweb3/UsuarioEntidadEJB/local")
  public UsuarioEntidadLocal usuarioEntidadEjb;

  @EJB(mappedName = "regweb3/PermisoLibroUsuarioEJB/local")
  public PermisoLibroUsuarioLocal permisoLibroUsuarioEjb;

  @EJB(mappedName = "regweb3/OficinaEJB/local")
  public OficinaLocal oficinaEjb;

  @EJB(mappedName = "regweb3/RelacionOrganizativaOfiEJB/local")
  public RelacionOrganizativaOfiLocal relacionOrganizativaOfiLocalEjb;


  /**
   *
   * @param entidadCodigoDir3
   * @return
   * @throws Throwable
   * @throws WsI18NException
   */
  @Override
  @WebMethod
  @RolesAllowed({ RegwebConstantes.ROL_USUARI })
  public List<TipoDocumentalWs> listarTipoDocumental(@WebParam(name = "entidadCodigoDir3") String entidadCodigoDir3) throws Throwable,
          WsI18NException {

    // 1.- Comprobaciones de parámetros obligatórios
    if(StringUtils.isEmpty(entidadCodigoDir3)){
      throw new I18NException("error.valor.requerido.ws", "entidadCodigoDir3");
    }

    Entidad entidad = CommonConverter.getEntidad(entidadCodigoDir3, entidadEjb);

    // 4.- Comprobaciones entidad existente
    if(entidad == null){
      throw new I18NException("registro.entidad.noExiste", entidadCodigoDir3);
    }


    List<TipoDocumental> tipos = tipoDocumentalEjb.getByEntidad(entidad.getId());

    List<TipoDocumentalWs> tiposWs = new ArrayList<TipoDocumentalWs>();


    for (TipoDocumental tipoDocumental : tipos) {
      tiposWs.add(CommonConverter.getTipoDocumentalWs(tipoDocumental));
    }

    return tiposWs;

  }


  /**
   *
   * @param entidadCodigoDir3
   * @return
   * @throws Throwable
   * @throws WsI18NException
   */
  @Override
  @WebMethod
  @RolesAllowed({ RegwebConstantes.ROL_USUARI })
  public List<TipoAsuntoWs> listarTipoAsunto(@WebParam(name = "entidadCodigoDir3") String entidadCodigoDir3) throws Throwable,
      WsI18NException {

    // 1.- Comprobaciones de parámetros obligatórios
    if(StringUtils.isEmpty(entidadCodigoDir3)){
      throw new I18NException("error.valor.requerido.ws", "entidadCodigoDir3");
    }

    Entidad entidad = CommonConverter.getEntidad(entidadCodigoDir3, entidadEjb);

    // 2.- Comprobaciones entidad existente
    if(entidad == null){
      throw new I18NException("registro.entidad.noExiste", entidadCodigoDir3);
    }

    List<TipoAsunto> tipos = tipoAsuntoEjb.getActivosEntidad(entidad.getId());

    List<TipoAsuntoWs> tiposWs = new ArrayList<TipoAsuntoWs>();

    final String idioma = RegwebConstantes.CODIGO_BY_IDIOMA_ID.get(UsuarioAplicacionCache.get().getUsuario().getIdioma());

    for (TipoAsunto tipoAsunto : tipos) {
      tiposWs.add(CommonConverter.getTipoAsuntoWs(tipoAsunto, idioma));
    }

    return tiposWs;

  }

  /**
   *
   * @param codigoTipoAsunto
   * @return
   * @throws Throwable
   * @throws WsI18NException
   */
  @Override
  @WebMethod
  @RolesAllowed({ RegwebConstantes.ROL_USUARI })
  public List<CodigoAsuntoWs> listarCodigoAsunto(@WebParam(name = "entidadCodigoDir3") String entidadCodigoDir3, @WebParam(name = "codigoTipoAsunto") String codigoTipoAsunto) throws Throwable,
      WsI18NException {

    // 1.- Comprobaciones de parámetros obligatórios
    if(StringUtils.isEmpty(entidadCodigoDir3)){
      throw new I18NException("error.valor.requerido.ws", "entidadCodigoDir3");
    }

    Entidad entidad = CommonConverter.getEntidad(entidadCodigoDir3, entidadEjb);

    // 2.- Comprobaciones entidad existente
    if(entidad == null){
      throw new I18NException("registro.entidad.noExiste", entidadCodigoDir3);
    }

    // 3.- Comprobaciones de parámetros obligatórios
    if(StringUtils.isEmpty(codigoTipoAsunto)){
      throw new I18NException("error.valor.requerido.ws", "codigoTipoAsunto");
    }

    TipoAsunto tipoAsunto = CommonConverter.getTipoAsunto(codigoTipoAsunto, entidad.getId(), tipoAsuntoEjb);

    // 4. Comprobación TipoAsunto Activo
    if(!tipoAsunto.getActivo()){
      throw new I18NException("error.tipoAsunto.inactivo", codigoTipoAsunto);
    }

    List<CodigoAsunto> codigoAsuntos = codigoAsuntoEjb.getByTipoAsunto(tipoAsunto.getId());

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
  public List<LibroOficinaWs> obtenerLibrosOficina(@WebParam(name = "entidadCodigoDir3") String entidadCodigoDir3,@WebParam(name = "tipoRegistro") Long tipoRegistro) throws Throwable, WsI18NException {

    // 1.- Comprobaciones de parámetros obligatórios
    if(StringUtils.isEmpty(entidadCodigoDir3)){
      throw new I18NException("error.valor.requerido.ws", "entidadCodigoDir3");
    }

    // 2.- Comprobaciones de parámetros obligatórios
    if(tipoRegistro == null){
      throw new I18NException("error.valor.requerido.ws", "tipoRegistro");
    }

    // 3.- Comprobaciones de parámetros tipoRegistro
    if(!validarAutorizacion(tipoRegistro)){
      throw new I18NException("error.autorizacion");
    }

    Entidad entidad = CommonConverter.getEntidad(entidadCodigoDir3, entidadEjb);
    UsuarioEntidad usuarioEntidad = usuarioEntidadEjb.findByIdentificadorCodigoEntidad(UsuarioAplicacionCache.get().getUsuario().getIdentificador(), entidadCodigoDir3);

    // 4.- Comprobaciones entidad existente
    if(entidad == null){
      throw new I18NException("registro.entidad.noExiste", entidadCodigoDir3);
    }

    // 5.- Comprobaciones usuarioEntidad existente
    if(usuarioEntidad == null){//No existe
      throw new I18NException("registro.usuario.noExiste", UsuarioAplicacionCache.get().getUsuario().getIdentificador(), entidadCodigoDir3);

    }

    // Obtenemos los Libros de Registro donde el usuario puede registrar
    List<Libro> librosRegistro = permisoLibroUsuarioEjb.getLibrosPermiso(usuarioEntidad.getId(), tipoRegistro);

    ArrayList<LibroOficinaWs> librosOficinas = new ArrayList<LibroOficinaWs>();

    // Recorremos los Libros y a partir del Organismo al que pertenecen, obtenemos las Oficinas que pueden Registrar en el.
    for (Libro libro : librosRegistro) {
      LibroWs libroWs = new LibroWs(libro.getCodigo(),libro.getNombre(), libro.getNombreCompleto() ,null);

      Long idOrganismo = libro.getOrganismo().getId();

      // Obtenemos las Oficinas cuyo Organismo responsable es al que pertenece el Libro
      for(Oficina oficina: oficinaEjb.findByOrganismoResponsable(idOrganismo) ){
        OficinaWs oficinaWs = new OficinaWs(oficina.getCodigo(),oficina.getDenominacion());

        LibroOficinaWs libroOficinaWs = new LibroOficinaWs(libroWs,oficinaWs);
        librosOficinas.add(libroOficinaWs);
      }

      // Obtenemos las Oficinas que dan servicio al Organismo que pertenece el Libro
      for(Oficina oficina: relacionOrganizativaOfiLocalEjb.getOficinasByOrganismo(idOrganismo) ){
        OficinaWs oficinaWs = new OficinaWs(oficina.getCodigo(),oficina.getDenominacion());

        LibroOficinaWs libroOficinaWs = new LibroOficinaWs(libroWs,oficinaWs);
        librosOficinas.add(libroOficinaWs);
      }

    }


    return librosOficinas;

  }

  @Override
  @WebMethod
  @RolesAllowed({ RegwebConstantes.ROL_USUARI })
  public List<LibroOficinaWs> obtenerLibrosOficinaUsuario(@WebParam(name = "entidadCodigoDir3") String entidadCodigoDir3, @WebParam(name = "usuario") String usuario, @WebParam(name = "tipoRegistro") Long tipoRegistro) throws Throwable, WsI18NException {

    // 1.- Comprobaciones de parámetros obligatórios
    if(StringUtils.isEmpty(entidadCodigoDir3)){
      throw new I18NException("error.valor.requerido.ws", "entidadCodigoDir3");
    }

    // 2.- Comprobaciones de parámetros obligatórios
    if(tipoRegistro == null){
      throw new I18NException("error.valor.requerido.ws", "tipoRegistro");
    }

    // 3.- Comprobaciones de parámetros obligatórios
    if(StringUtils.isEmpty(usuario)){
      throw new I18NException("error.valor.requerido.ws", "usuario");
    }

    Entidad entidad = CommonConverter.getEntidad(entidadCodigoDir3, entidadEjb);
    UsuarioEntidad usuarioEntidad = usuarioEntidadEjb.findByIdentificadorCodigoEntidad(usuario, entidadCodigoDir3);

    // 4.- Comprobaciones entidad existente
    if(entidad == null){
      throw new I18NException("registro.entidad.noExiste", entidadCodigoDir3);
    }

    // 5.- Comprobaciones usuarioEntidad existente
    if(usuarioEntidad == null){//No existe
      throw new I18NException("registro.usuario.noExiste", usuario, entidadCodigoDir3);

    }

    // Obtenemos los Libros de Registro donde el usuario puede registrar
    List<Libro> librosRegistro = permisoLibroUsuarioEjb.getLibrosPermiso(usuarioEntidad.getId(), tipoRegistro);

    ArrayList<LibroOficinaWs> librosOficinas = new ArrayList<LibroOficinaWs>();

    // Recorremos los Libros y a partir del Organismo al que pertenecen, obtenemos las Oficinas que pueden Registrar en el.
    for (Libro libro : librosRegistro) {
      LibroWs libroWs = new LibroWs(libro.getCodigo(),libro.getNombre(), libro.getNombreCompleto() ,null);

      Long idOrganismo = libro.getOrganismo().getId();

      // Obtenemos las Oficinas cuyo Organismo responsable es al que pertenece el Libro
      for(Oficina oficina: oficinaEjb.findByOrganismoResponsable(idOrganismo) ){
        OficinaWs oficinaWs = new OficinaWs(oficina.getCodigo(),oficina.getDenominacion());

        LibroOficinaWs libroOficinaWs = new LibroOficinaWs(libroWs,oficinaWs);
        librosOficinas.add(libroOficinaWs);
      }

      // Obtenemos las Oficinas que dan servicio al Organismo que pertenece el Libro
      for(Oficina oficina: relacionOrganizativaOfiLocalEjb.getOficinasByOrganismo(idOrganismo) ){
        OficinaWs oficinaWs = new OficinaWs(oficina.getCodigo(),oficina.getDenominacion());

        LibroOficinaWs libroOficinaWs = new LibroOficinaWs(libroWs,oficinaWs);
        librosOficinas.add(libroOficinaWs);
      }

    }


    return librosOficinas;

  }

  @Override
    @WebMethod
    @RolesAllowed({ RegwebConstantes.ROL_USUARI })
    public List<OficinaWs> listarOficinas(@WebParam(name = "entidadCodigoDir3") String entidadCodigoDir3,
                                          @WebParam(name = "autorizacion") Long autorizacion) throws Throwable, WsI18NException {

      // 1.- Comprobaciones de parámetros obligatórios
      if(StringUtils.isEmpty(entidadCodigoDir3)){
        throw new I18NException("error.valor.requerido.ws", "entidadCodigoDir3");
      }

      if(autorizacion == null){
        throw new I18NException("error.valor.requerido.ws", "autorizacion");
      }

      if(!validarAutorizacion(autorizacion)){
        throw new I18NException("error.autorizacion");
      }

        Entidad entidad = CommonConverter.getEntidad(entidadCodigoDir3, entidadEjb);
        UsuarioEntidad usuarioEntidad = usuarioEntidadEjb.findByIdentificadorCodigoEntidad(UsuarioAplicacionCache.get().getUsuario().getIdentificador(), entidad.getCodigoDir3());

        List<Libro> librosRegistro = permisoLibroUsuarioEjb.getLibrosPermiso(usuarioEntidad.getId(), autorizacion);

        Set<Oficina> oficinasRegistro = new HashSet<Oficina>();  // Utilizamos un Set porque no permite duplicados

        // Recorremos los Libros y a partir del Organismo al que pertenecen, obtenemos las Oficinas que pueden Registrar en el.
        for (Libro libro : librosRegistro) {
          Long idOrganismo = libro.getOrganismo().getId();
          oficinasRegistro.addAll(oficinaEjb.findByOrganismoResponsable(idOrganismo));
          oficinasRegistro.addAll(relacionOrganizativaOfiLocalEjb.getOficinasByOrganismo(idOrganismo));
        }

        // Convertimos el Listado de Oficinas en un Listado de OficinaWs
        List<OficinaWs> listOficinaWs = new ArrayList<OficinaWs>(oficinasRegistro.size());
        for (Oficina oficina : oficinasRegistro) {
          listOficinaWs.add(CommonConverter.getOficinaWs(oficina));
        }

        return listOficinaWs;

    }

    @Override
    @WebMethod
    @RolesAllowed({ RegwebConstantes.ROL_USUARI })
    public List<LibroWs> listarLibros(@WebParam(name = "entidadCodigoDir3") String entidadCodigoDir3,
        @WebParam(name = "oficinaCodigoDir3") String oficinaCodigoDir3, @WebParam(name = "autorizacion") Long autorizacion) throws Throwable, WsI18NException {

      // 1.- Comprobaciones de parámetros obligatórios
      if(StringUtils.isEmpty(entidadCodigoDir3)){
        throw new I18NException("error.valor.requerido.ws", "entidadCodigoDir3");
      }

      if(StringUtils.isEmpty(oficinaCodigoDir3)){
        throw new I18NException("error.valor.requerido.ws", "oficinaCodigoDir3");
      }

      if(autorizacion == null){
        throw new I18NException("error.valor.requerido.ws", "autorizacion");
      }

      if(!validarAutorizacion(autorizacion)){
        throw new I18NException("error.autorizacion");
      }

      Entidad entidad = CommonConverter.getEntidad(entidadCodigoDir3, entidadEjb);
      UsuarioEntidad usuarioEntidad = usuarioEntidadEjb.findByIdentificadorCodigoEntidad(UsuarioAplicacionCache.get().getUsuario().getIdentificador(), entidad.getCodigoDir3());
      Oficina oficina = CommonConverter.getOficina(oficinaCodigoDir3, oficinaEjb);

      // Comprobar que la Oficina está vigente
      if(oficina == null){ //No existe
        throw new I18NException("registro.oficina.noExiste", oficinaCodigoDir3);

      }else if(!oficina.getEstado().getCodigoEstadoEntidad().equals(ESTADO_ENTIDAD_VIGENTE)){ //Si está extinguido
        throw new I18NException("registro.oficina.extinguido", oficina.getNombreCompleto());
      }

      // Obtenemos los organismos funcionales a la que da servicio al Oficina seleccionada
      Set<Long> organismos = oficina.getOrganismosFuncionalesId();

      // Obtenemos los libros a los que el usuario puede registrar
      List<Libro> listLibro = permisoLibroUsuarioEjb.getLibrosOrganismoPermiso(organismos, usuarioEntidad.getId(), autorizacion);

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

    // 1.- Comprobaciones de parámetros obligatórios
    if(StringUtils.isEmpty(entidadCodigoDir3)){
      throw new I18NException("error.valor.requerido.ws", "entidadCodigoDir3");
    }

    Entidad entidadObj = CommonConverter.getEntidad(entidadCodigoDir3, entidadEjb);

    List<Organismo> listOrganismo = organismoEjb.getAllByEntidad(entidadObj.getId());

    List<OrganismoWs> listOrganismoWs = new ArrayList<OrganismoWs>(listOrganismo.size());

    for (Organismo libro : listOrganismo) {
      listOrganismoWs.add(CommonConverter.getOrganismoWs(libro));
    }

    return listOrganismoWs;
  }

  /**
   * Valida la obligatoriedad de los campos
   * @param numeroRegistro
   * @param usuario
   * @param entidad
   * @throws I18NException
   */
  private void validarObligatorios(String numeroRegistro, String usuario, String entidad) throws  I18NException, Exception{

    // 1.- Comprobaciones de parámetros obligatórios
    if(StringUtils.isEmpty(entidad)){
      throw new I18NException("error.valor.requerido.ws", "entidad");
    }

  }

  /**
   * Valida la tipoRegistro recibida entre las disponibles
   * @param tipoRegistro
   * @return
   * @throws Exception
   */
  private Boolean validarAutorizacion(Long tipoRegistro) throws Exception {

    return tipoRegistro.equals(PERMISO_REGISTRO_ENTRADA) || tipoRegistro.equals(PERMISO_REGISTRO_SALIDA);

  }



  

}
