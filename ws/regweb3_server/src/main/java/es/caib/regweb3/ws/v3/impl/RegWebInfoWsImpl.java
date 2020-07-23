package es.caib.regweb3.ws.v3.impl;

import es.caib.regweb3.model.*;
import es.caib.regweb3.persistence.ejb.*;
import es.caib.regweb3.utils.RegwebConstantes;
import es.caib.regweb3.utils.StringUtils;
import es.caib.regweb3.ws.model.*;
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
import java.util.LinkedHashSet;
import java.util.List;

//import org.springframework.stereotype.Component;

/**
 * Created by Fundació BIT.
 *
 * @author anadal
 */
@SecurityDomain(RegwebConstantes.SECURITY_DOMAIN)
@Stateless(name = RegWebInfoWsImpl.NAME + "Ejb")
@RolesAllowed({RegwebConstantes.RWE_SUPERADMIN})
@SOAPBinding(style = SOAPBinding.Style.RPC)
@org.apache.cxf.interceptor.InInterceptors(interceptors = {"es.caib.regweb3.ws.utils.RegWebInInterceptor"})
@org.apache.cxf.interceptor.InFaultInterceptors(interceptors = {"es.caib.regweb3.ws.utils.RegWebInInterceptor"})
@WebService(name = RegWebInfoWsImpl.NAME_WS, portName = RegWebInfoWsImpl.NAME_WS, serviceName = RegWebInfoWsImpl.NAME_WS
        + "Service", endpointInterface = "es.caib.regweb3.ws.v3.impl.RegWebInfoWs")
@WebContext(contextRoot = "/regweb3/ws", urlPattern = "/v3/" + RegWebInfoWsImpl.NAME, transportGuarantee = TransportGuarantee.NONE, secureWSDLAccess = false, authMethod = "WSBASIC")
//@Component
public class RegWebInfoWsImpl extends AbstractRegistroWsImpl implements RegWebInfoWs {

    protected final Logger log = Logger.getLogger(getClass());

    public static final String NAME = "RegWebInfo";

    public static final String NAME_WS = NAME + "Ws";


    @EJB(mappedName = "regweb3/TipoDocumentalEJB/local")
    private TipoDocumentalLocal tipoDocumentalEjb;

    @EJB(mappedName = "regweb3/OrganismoEJB/local")
    private OrganismoLocal organismoEjb;

    @EJB(mappedName = "regweb3/CodigoAsuntoEJB/local")
    private CodigoAsuntoLocal codigoAsuntoEjb;

    @EJB(mappedName = "regweb3/UsuarioEntidadEJB/local")
    private UsuarioEntidadLocal usuarioEntidadEjb;

    @EJB(mappedName = "regweb3/OficinaEJB/local")
    private OficinaLocal oficinaEjb;

    @EJB(mappedName = "regweb3/RelacionOrganizativaOfiEJB/local")
    private RelacionOrganizativaOfiLocal relacionOrganizativaOfiLocalEjb;


    /**
     * @param entidadCodigoDir3
     * @return
     * @throws Throwable
     * @throws WsI18NException
     */
    @Override
    @WebMethod
    @RolesAllowed({RegwebConstantes.RWE_USUARI})
    public List<TipoDocumentalWs> listarTipoDocumental(@WebParam(name = "entidadCodigoDir3") String entidadCodigoDir3) throws Throwable,
            WsI18NException {

        // 1.- Comprobaciones de entidad
        Entidad entidad = validarEntidad(entidadCodigoDir3);


        List<TipoDocumental> tipos = tipoDocumentalEjb.getByEntidad(entidad.getId());

        List<TipoDocumentalWs> tiposWs = new ArrayList<TipoDocumentalWs>();


        for (TipoDocumental tipoDocumental : tipos) {
            tiposWs.add(CommonConverter.getTipoDocumentalWs(tipoDocumental));
        }

        return tiposWs;

    }


    /**
     * @param entidadCodigoDir3
     * @return
     * @throws Throwable
     * @throws WsI18NException
     */
    @Override
    @WebMethod
    @RolesAllowed({RegwebConstantes.RWE_USUARI})
    public List<TipoAsuntoWs> listarTipoAsunto(@WebParam(name = "entidadCodigoDir3") String entidadCodigoDir3) throws Throwable,
            WsI18NException {

        // 1.- Comprobaciones de entidad
        Entidad entidad = validarEntidad(entidadCodigoDir3);

        //List<TipoAsunto> tipos = tipoAsuntoEjb.getActivosEntidad(entidad.getId());

        List<TipoAsuntoWs> tiposWs = new ArrayList<TipoAsuntoWs>();

        TipoAsuntoWs tipoAsuntoWs = new TipoAsuntoWs();
        tipoAsuntoWs.setActivo(true);
        tipoAsuntoWs.setCodigo("01");
        tipoAsuntoWs.setNombre("Deprecated");

        tiposWs.add(tipoAsuntoWs);

        //final String idioma = RegwebConstantes.CODIGO_BY_IDIOMA_ID.get(UsuarioAplicacionCache.get().getUsuario().getIdioma());

    /*for (TipoAsunto tipoAsunto : tipos) {
      tiposWs.add(CommonConverter.getTipoAsuntoWs(tipoAsunto, idioma));
    }*/

        return tiposWs;

    }

    /**
     * @param codigoTipoAsunto
     * @return
     * @throws Throwable
     * @throws WsI18NException
     */
    @Override
    @WebMethod
    @RolesAllowed({RegwebConstantes.RWE_USUARI})
    public List<CodigoAsuntoWs> listarCodigoAsunto(@WebParam(name = "entidadCodigoDir3") String entidadCodigoDir3, @WebParam(name = "codigoTipoAsunto") String codigoTipoAsunto) throws Throwable,
            WsI18NException {

        // 1.- Comprobaciones de entidad
        Entidad entidad = validarEntidad(entidadCodigoDir3);

        // 3.- Comprobaciones de parámetros obligatórios
    /*if(StringUtils.isEmpty(codigoTipoAsunto)){
      throw new I18NException("error.valor.requerido.ws", "codigoTipoAsunto");
    }

    TipoAsunto tipoAsunto = CommonConverter.getTipoAsunto(codigoTipoAsunto, entidad.getId(), tipoAsuntoEjb);

    // 4. Comprobación existencia TipoAsunto
    if(tipoAsunto == null){
      throw new I18NException("error.tipoAsunto.noExiste", codigoTipoAsunto);
    }

    // 5. Comprobación TipoAsunto Activo
    if(!tipoAsunto.getActivo()){
      throw new I18NException("error.tipoAsunto.inactivo", codigoTipoAsunto);
    }*/

        List<CodigoAsunto> codigoAsuntos = codigoAsuntoEjb.getActivosEntidad(entidad.getId());

        List<CodigoAsuntoWs> codigosWs = new ArrayList<CodigoAsuntoWs>();

        final String idioma = RegwebConstantes.CODIGO_BY_IDIOMA_ID.get(UsuarioAplicacionCache.get().getUsuario().getIdioma());

        for (CodigoAsunto codigoAsunto : codigoAsuntos) {
            codigosWs.add(CommonConverter.getCodigoAsuntoWs(codigoAsunto, idioma));
        }

        return codigosWs;

    }

    @Override
    @WebMethod
    @RolesAllowed({RegwebConstantes.RWE_USUARI})
    public List<LibroOficinaWs> obtenerLibrosOficina(@WebParam(name = "entidadCodigoDir3") String entidadCodigoDir3, @WebParam(name = "tipoRegistro") Long tipoRegistro) throws Throwable, WsI18NException {

        // 1.- Comprobaciones de entidad
        Entidad entidad = validarEntidad(entidadCodigoDir3);

        // 2.- Comprobaciones de parámetros obligatórios
        if (tipoRegistro == null) {
            throw new I18NException("error.valor.requerido.ws", "tipoRegistro");
        }

        // 3.- Comprobaciones de parámetros tipoRegistro
        if (!validarAutorizacion(tipoRegistro)) {
            throw new I18NException("error.autorizacion");
        }

        UsuarioEntidad usuarioEntidad = usuarioEntidadEjb.findByIdentificadorCodigoEntidad(UsuarioAplicacionCache.get().getUsuario().getIdentificador(), entidad.getCodigoDir3());

        // 5.- Comprobaciones usuarioEntidad existente
        if (usuarioEntidad == null) {//No existe
            throw new I18NException("registro.usuario.noExiste", UsuarioAplicacionCache.get().getUsuario().getIdentificador(), entidadCodigoDir3);

        }

        // Libro único
        List<Libro> librosRegistro = new ArrayList<Libro>();
        librosRegistro.add(entidad.getLibro());

        ArrayList<LibroOficinaWs> librosOficinas = new ArrayList<LibroOficinaWs>();

        recorrerLibrosRegistro(librosRegistro, librosOficinas);


        return librosOficinas;

    }

    @Override
    @WebMethod
    @RolesAllowed({RegwebConstantes.RWE_USUARI})
    public List<LibroOficinaWs> obtenerLibrosOficinaUsuario(@WebParam(name = "entidadCodigoDir3") String entidadCodigoDir3, @WebParam(name = "usuario") String usuario, @WebParam(name = "tipoRegistro") Long tipoRegistro) throws Throwable, WsI18NException {

        // 1.- Comprobaciones de entidad
        Entidad entidad = validarEntidad(entidadCodigoDir3);

        // 2.- Comprobaciones de parámetros obligatórios
        if (tipoRegistro == null) {
            throw new I18NException("error.valor.requerido.ws", "tipoRegistro");
        }

        // 3.- Comprobaciones de parámetros obligatórios
        if (StringUtils.isEmpty(usuario)) {
            throw new I18NException("error.valor.requerido.ws", "usuario");
        }

        UsuarioEntidad usuarioEntidad = usuarioEntidadEjb.findByIdentificadorCodigoEntidad(usuario, entidad.getCodigoDir3());

        // 5.- Comprobaciones usuarioEntidad existente
        if (usuarioEntidad == null) {//No existe
            throw new I18NException("registro.usuario.noExiste", usuario, entidadCodigoDir3);

        }

        // Libro único
        List<Libro> librosRegistro = new ArrayList<Libro>();
        librosRegistro.add(entidad.getLibro());

        ArrayList<LibroOficinaWs> librosOficinas = new ArrayList<LibroOficinaWs>();

        recorrerLibrosRegistro(librosRegistro, librosOficinas);

        return librosOficinas;

    }

    @Override
    @WebMethod
    @RolesAllowed({RegwebConstantes.RWE_USUARI})
    public List<OficinaWs> listarOficinas(@WebParam(name = "entidadCodigoDir3") String entidadCodigoDir3,
                                          @WebParam(name = "autorizacion") Long autorizacion) throws Throwable, WsI18NException {

        // 1.- Comprobaciones de entidad
        Entidad entidad = validarEntidad(entidadCodigoDir3);

        if (autorizacion == null) {
            throw new I18NException("error.valor.requerido.ws", "autorizacion");
        }

        if (!validarAutorizacion(autorizacion)) {
            throw new I18NException("error.autorizacion");
        }

        UsuarioEntidad usuarioEntidad = usuarioEntidadEjb.findByIdentificadorCodigoEntidad(UsuarioAplicacionCache.get().getUsuario().getIdentificador(), entidad.getCodigoDir3());

        // 1.- Comprobaciones usuarioEntidad existente
        if (usuarioEntidad == null) {//No existe
            throw new I18NException("registro.usuario.noExiste", UsuarioAplicacionCache.get().getUsuario().getIdentificador(), entidadCodigoDir3);

        }


        LinkedHashSet<Oficina> oficinas = permisoOrganismoUsuarioEjb.getOficinasPermiso(usuarioEntidad.getId(), autorizacion);

        // Convertimos el Listado de Oficinas en un Listado de OficinaWs
        List<OficinaWs> listOficinaWs = new ArrayList<OficinaWs>(oficinas.size());
        for (Oficina oficina : oficinas) {
            listOficinaWs.add(CommonConverter.getOficinaWs(oficina));
        }

        return listOficinaWs;

    }

    @Override
    @WebMethod
    @RolesAllowed({RegwebConstantes.RWE_USUARI})
    public List<LibroWs> listarLibros(@WebParam(name = "entidadCodigoDir3") String entidadCodigoDir3,
                                      @WebParam(name = "oficinaCodigoDir3") String oficinaCodigoDir3, @WebParam(name = "autorizacion") Long autorizacion) throws Throwable, WsI18NException {

        // 1.- Comprobaciones de entidad
        Entidad entidad = validarEntidad(entidadCodigoDir3);

        if (StringUtils.isEmpty(oficinaCodigoDir3)) {
            throw new I18NException("error.valor.requerido.ws", "oficinaCodigoDir3");
        }

        if (autorizacion == null) {
            throw new I18NException("error.valor.requerido.ws", "autorizacion");
        }

        if (!validarAutorizacion(autorizacion)) {
            throw new I18NException("error.autorizacion");
        }

        UsuarioEntidad usuarioEntidad = usuarioEntidadEjb.findByIdentificadorCodigoEntidad(UsuarioAplicacionCache.get().getUsuario().getIdentificador(), entidad.getCodigoDir3());

        // 2.- Comprobar que el usuarioEntidad existe
        if (usuarioEntidad == null) {//No existe
            throw new I18NException("registro.usuario.noExiste", UsuarioAplicacionCache.get().getUsuario().getIdentificador(), entidadCodigoDir3);

        }

        // 2.- Comprobar que la Oficina está vigente
        Oficina oficina = validarOficina(oficinaCodigoDir3, entidad.getId());

        // Comprobamos si tiene permiso para registrar en el Organismo al que pertenece la oficina
        if (permisoOrganismoUsuarioEjb.tienePermiso(usuarioEntidad.getId(), oficina.getOrganismoResponsable().getId(), autorizacion, true)) {

            //Libro único
            List<LibroWs> listLibroWs = new ArrayList<LibroWs>();
            listLibroWs.add(CommonConverter.getLibroWs(entidad.getLibro()));

            return listLibroWs;

        } else {
            return null;
        }

    }

    @Override
    @WebMethod
    @RolesAllowed({RegwebConstantes.RWE_USUARI, RWE_WS_ENTRADA, RWE_WS_SALIDA})
    public LibroWs listarLibroOrganismo(@WebParam(name = "entidad") String entidadCodigo,
                                        @WebParam(name = "organismo") String organismo) throws Throwable, WsI18NException {

        // 1.- Comprobaciones de parámetros obligatórios
        Entidad entidad = validarEntidad(entidadCodigo);

        if (StringUtils.isEmpty(organismo)) {
            throw new I18NException("error.valor.requerido.ws", "organismo");
        }

        UsuarioEntidad usuarioEntidad = usuarioEntidadEjb.findByIdentificadorCodigoEntidad(UsuarioAplicacionCache.get().getUsuario().getIdentificador(), entidad.getCodigoDir3());

        // 2.- Comprobar que el usuarioEntidad existe
        if (usuarioEntidad == null) {
            throw new I18NException("registro.usuario.noExiste", UsuarioAplicacionCache.get().getUsuario().getIdentificador(), entidadCodigo);

        }

        Organismo organismoActivo = organismoEjb.findByCodigoEntidad(organismo, entidad.getId());

        // 3. Comprobar que el Organismo existe y está vigente
        if (organismoActivo == null) { //No existe
            throw new I18NException("registro.organismo.noExiste", organismo);
        }

        // Comprobamos que el usuario tiene permisos para registrar en el Organismo indicado
        if (permisoOrganismoUsuarioEjb.puedeRegistrar(usuarioEntidad.getId(), organismoActivo.getId())) {
            return CommonConverter.getLibroWs(entidad.getLibro());
        }


        throw new I18NException("organismo.no.libroRegistro", organismoActivo.getNombreCompleto());
    }


    @Override
    @WebMethod
    @RolesAllowed({RegwebConstantes.RWE_USUARI})
    public List<OrganismoWs> listarOrganismos(
            @WebParam(name = "entidadCodigoDir3") String entidadCodigoDir3)
            throws Throwable, WsI18NException {

        // 1.- Comprobaciones de parámetros obligatórios
        Entidad entidad = validarEntidad(entidadCodigoDir3);

        List<Organismo> listOrganismo = organismoEjb.getAllByEntidad(entidad.getId());

        List<OrganismoWs> listOrganismoWs = new ArrayList<OrganismoWs>(listOrganismo.size());

        for (Organismo libro : listOrganismo) {
            listOrganismoWs.add(CommonConverter.getOrganismoWs(libro));
        }

        return listOrganismoWs;
    }

    /**
     * Valida la tipoRegistro recibida entre las disponibles
     *
     * @param tipoRegistro
     * @return
     * @throws Exception
     */
    private Boolean validarAutorizacion(Long tipoRegistro) throws Exception {

        return tipoRegistro.equals(PERMISO_REGISTRO_ENTRADA) || tipoRegistro.equals(PERMISO_REGISTRO_SALIDA);

    }

    /**
     * Recorremos los Libros y a partir del Organismo al que pertenecen, obtenemos las Oficinas que pueden Registrar en el.
     *
     * @param librosRegistro
     * @param librosOficinas
     * @throws Exception
     */
    private void recorrerLibrosRegistro(List<Libro> librosRegistro, List<LibroOficinaWs> librosOficinas) throws Exception {

        for (Libro libro : librosRegistro) {
            LibroWs libroWs = new LibroWs(libro.getCodigo(), libro.getNombre(), libro.getNombreCompleto(), null);

            Long idOrganismo = libro.getOrganismo().getId();

            // Obtenemos las Oficinas cuyo Organismo responsable es al que pertenece el Libro
            for (Oficina oficina : oficinaEjb.findByOrganismoResponsable(idOrganismo)) {
                OficinaWs oficinaWs = new OficinaWs(oficina.getCodigo(), oficina.getDenominacion());

                LibroOficinaWs libroOficinaWs = new LibroOficinaWs(libroWs, oficinaWs);
                librosOficinas.add(libroOficinaWs);
            }

            // Obtenemos las Oficinas que dan servicio al Organismo que pertenece el Libro
            for (Oficina oficina : relacionOrganizativaOfiLocalEjb.getOficinasByOrganismo(idOrganismo)) {
                OficinaWs oficinaWs = new OficinaWs(oficina.getCodigo(), oficina.getDenominacion());

                LibroOficinaWs libroOficinaWs = new LibroOficinaWs(libroWs, oficinaWs);
                librosOficinas.add(libroOficinaWs);
            }

        }

    }


}
