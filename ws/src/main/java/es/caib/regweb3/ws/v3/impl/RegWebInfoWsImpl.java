package es.caib.regweb3.ws.v3.impl;

import es.caib.regweb3.model.*;
import es.caib.regweb3.persistence.ejb.*;
import es.caib.regweb3.utils.RegwebConstantes;
import es.caib.regweb3.utils.StringUtils;
import es.caib.regweb3.ws.model.*;
import es.caib.regweb3.ws.utils.UsuarioAplicacionCache;
import es.caib.regweb3.ws.utils.WsUtils;
import org.fundaciobit.genapp.common.ws.WsI18NException;
import org.jboss.ws.api.annotation.TransportGuarantee;
import org.jboss.ws.api.annotation.WebContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

/**
 * Created by Fundació BIT.
 *
 * @author anadal
 */
@Stateless(name = RegWebInfoWsImpl.NAME + "Ejb")
@SOAPBinding(style = SOAPBinding.Style.RPC)
@org.apache.cxf.interceptor.InInterceptors(interceptors = {"es.caib.regweb3.ws.utils.RegWebInInterceptor"})
@org.apache.cxf.interceptor.InFaultInterceptors(interceptors = {"es.caib.regweb3.ws.utils.RegWebInInterceptor"})
@WebService(name = RegWebInfoWsImpl.NAME_WS, portName = RegWebInfoWsImpl.NAME_WS, serviceName = RegWebInfoWsImpl.NAME_WS
        + "Service", endpointInterface = "es.caib.regweb3.ws.v3.impl.RegWebInfoWs")
@WebContext(contextRoot = "/regweb3/ws", urlPattern = "/v3/" + RegWebInfoWsImpl.NAME, transportGuarantee = TransportGuarantee.NONE)
public class RegWebInfoWsImpl extends AbstractRegistroWsImpl implements RegWebInfoWs {

    protected final Logger log = LoggerFactory.getLogger(getClass());

    public static final String NAME = "RegWebInfo";

    public static final String NAME_WS = NAME + "Ws";


    @EJB(mappedName = TipoDocumentalLocal.JNDI_NAME)
    private TipoDocumentalLocal tipoDocumentalEjb;

    @EJB(mappedName = OrganismoLocal.JNDI_NAME)
    private OrganismoLocal organismoEjb;

    @EJB(mappedName = CodigoAsuntoLocal.JNDI_NAME)
    private CodigoAsuntoLocal codigoAsuntoEjb;

    @EJB(mappedName = UsuarioEntidadLocal.JNDI_NAME)
    private UsuarioEntidadLocal usuarioEntidadEjb;

    @EJB(mappedName = OficinaLocal.JNDI_NAME)
    private OficinaLocal oficinaEjb;

    @EJB(mappedName = RelacionOrganizativaOfiLocal.JNDI_NAME)
    private RelacionOrganizativaOfiLocal relacionOrganizativaOfiLocalEjb;


    /**
     * @param entidadCodigoDir3
     * @return
     * @throws Throwable
     * @throws WsI18NException
     */
    @Override
    @WebMethod
    @RolesAllowed({RWE_USUARI, RWE_WS_ENTRADA, RWE_WS_SALIDA})
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
    @RolesAllowed({RWE_USUARI, RWE_WS_ENTRADA, RWE_WS_SALIDA})
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
    @RolesAllowed({RWE_USUARI, RWE_WS_ENTRADA, RWE_WS_SALIDA})
    public List<CodigoAsuntoWs> listarCodigoAsunto(@WebParam(name = "entidadCodigoDir3") String entidadCodigoDir3, @WebParam(name = "codigoTipoAsunto") String codigoTipoAsunto) throws Throwable,
            WsI18NException {

        // 1.- Comprobaciones de entidad
        Entidad entidad = validarEntidad(entidadCodigoDir3);

        // 3.- Comprobaciones de parámetros obligatórios
    /*if(StringUtils.isEmpty(codigoTipoAsunto)){
      throw WsUtils.createWsI18NException("error.valor.requerido.ws", "codigoTipoAsunto");
    }

    TipoAsunto tipoAsunto = CommonConverter.getTipoAsunto(codigoTipoAsunto, entidad.getId(), tipoAsuntoEjb);

    // 4. Comprobación existencia TipoAsunto
    if(tipoAsunto == null){
      throw WsUtils.createWsI18NException("error.tipoAsunto.noExiste", codigoTipoAsunto);
    }

    // 5. Comprobación TipoAsunto Activo
    if(!tipoAsunto.getActivo()){
      throw WsUtils.createWsI18NException("error.tipoAsunto.inactivo", codigoTipoAsunto);
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
    @RolesAllowed({RWE_USUARI, RWE_WS_ENTRADA, RWE_WS_SALIDA})
    public List<LibroOficinaWs> obtenerLibrosOficina(@WebParam(name = "entidadCodigoDir3") String entidadCodigoDir3, @WebParam(name = "tipoRegistro") Long tipoRegistro) throws Throwable, WsI18NException {

        // 1.- Comprobaciones de entidad
        Entidad entidad = validarEntidad(entidadCodigoDir3);

        // 2.- Comprobaciones de parámetros obligatórios
        if (tipoRegistro == null) {
            throw WsUtils.createWsI18NException("error.valor.requerido.ws", "tipoRegistro");
        }

        // 3.- Comprobaciones de parámetros tipoRegistro
        if (!validarAutorizacion(tipoRegistro)) {
            throw WsUtils.createWsI18NException("error.autorizacion");
        }

        UsuarioEntidad usuarioEntidad = usuarioEntidadEjb.findByIdentificadorCodigoEntidad(UsuarioAplicacionCache.get().getUsuario().getIdentificador(), entidad.getCodigoDir3());

        // 5.- Comprobaciones usuarioEntidad existente
        if (usuarioEntidad == null) {//No existe
            throw WsUtils.createWsI18NException("registro.usuario.noExiste", UsuarioAplicacionCache.get().getUsuario().getIdentificador(), entidadCodigoDir3);

        }

        //Obtenemos los Organismos donde el usuario puede registrar
        List<Organismo> organismos = permisoOrganismoUsuarioEjb.getOrganismosPermiso(usuarioEntidad.getId(), tipoRegistro);


        List<LibroOficinaWs> librosOficinas = recorrerLibrosRegistro(organismos, entidad.getLibro());


        return librosOficinas;

    }

    @Override
    @WebMethod
    @RolesAllowed({RWE_USUARI, RWE_WS_ENTRADA, RWE_WS_SALIDA})
    public List<LibroOficinaWs> obtenerLibrosOficinaUsuario(@WebParam(name = "entidadCodigoDir3") String entidadCodigoDir3, @WebParam(name = "usuario") String usuario, @WebParam(name = "tipoRegistro") Long tipoRegistro) throws Throwable, WsI18NException {

        // 1.- Comprobaciones de entidad
        Entidad entidad = validarEntidad(entidadCodigoDir3);

        // 2.- Comprobaciones de parámetros obligatórios
        if (tipoRegistro == null) {
            throw WsUtils.createWsI18NException("error.valor.requerido.ws", "tipoRegistro");
        }

        // 3.- Comprobaciones de parámetros obligatórios
        if (StringUtils.isEmpty(usuario)) {
            throw WsUtils.createWsI18NException("error.valor.requerido.ws", "usuario");
        }

        UsuarioEntidad usuarioEntidad = usuarioEntidadEjb.findByIdentificadorCodigoEntidad(usuario, entidad.getCodigoDir3());

        // 5.- Comprobaciones usuarioEntidad existente
        if (usuarioEntidad == null) {//No existe
            throw WsUtils.createWsI18NException("registro.usuario.noExiste", usuario, entidadCodigoDir3);

        }

        //Obtenemos los Organismos donde el usuario puede registrar
        List<Organismo> organismos = permisoOrganismoUsuarioEjb.getOrganismosPermiso(usuarioEntidad.getId(), tipoRegistro);

        List<LibroOficinaWs> librosOficinas = recorrerLibrosRegistro(organismos, entidad.getLibro());

        return librosOficinas;

    }

    @Override
    @WebMethod
    @RolesAllowed({RWE_USUARI, RWE_WS_ENTRADA, RWE_WS_SALIDA})
    public List<OficinaWs> listarOficinas(@WebParam(name = "entidadCodigoDir3") String entidadCodigoDir3,
                                          @WebParam(name = "autorizacion") Long autorizacion) throws Throwable, WsI18NException {

        // 1.- Comprobaciones de entidad
        Entidad entidad = validarEntidad(entidadCodigoDir3);

        if (autorizacion == null) {
            throw WsUtils.createWsI18NException("error.valor.requerido.ws", "autorizacion");
        }

        if (!validarAutorizacion(autorizacion)) {
            throw WsUtils.createWsI18NException("error.autorizacion");
        }

        UsuarioEntidad usuarioEntidad = usuarioEntidadEjb.findByIdentificadorCodigoEntidad(UsuarioAplicacionCache.get().getUsuario().getIdentificador(), entidad.getCodigoDir3());

        // 1.- Comprobaciones usuarioEntidad existente
        if (usuarioEntidad == null) {//No existe
            throw WsUtils.createWsI18NException("registro.usuario.noExiste", UsuarioAplicacionCache.get().getUsuario().getIdentificador(), entidadCodigoDir3);

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
    @RolesAllowed({RWE_USUARI, RWE_WS_ENTRADA, RWE_WS_SALIDA})
    public List<LibroWs> listarLibros(@WebParam(name = "entidadCodigoDir3") String entidadCodigoDir3,
                                      @WebParam(name = "oficinaCodigoDir3") String oficinaCodigoDir3, @WebParam(name = "autorizacion") Long autorizacion) throws Throwable, WsI18NException {

        // 1.- Comprobaciones de entidad
        Entidad entidad = validarEntidad(entidadCodigoDir3);

        if (StringUtils.isEmpty(oficinaCodigoDir3)) {
            throw WsUtils.createWsI18NException("error.valor.requerido.ws", "oficinaCodigoDir3");
        }

        if (autorizacion == null) {
            throw WsUtils.createWsI18NException("error.valor.requerido.ws", "autorizacion");
        }

        if (!validarAutorizacion(autorizacion)) {
            throw WsUtils.createWsI18NException("error.autorizacion");
        }

        UsuarioEntidad usuarioEntidad = usuarioEntidadEjb.findByIdentificadorCodigoEntidad(UsuarioAplicacionCache.get().getUsuario().getIdentificador(), entidad.getCodigoDir3());

        // 2.- Comprobar que el usuarioEntidad existe
        if (usuarioEntidad == null) {//No existe
            throw WsUtils.createWsI18NException("registro.usuario.noExiste", UsuarioAplicacionCache.get().getUsuario().getIdentificador(), entidadCodigoDir3);

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
    @RolesAllowed({RWE_USUARI, RWE_WS_ENTRADA, RWE_WS_SALIDA})
    public LibroWs listarLibroOrganismo(@WebParam(name = "entidad") String entidadCodigo,
                                        @WebParam(name = "organismo") String organismo) throws Throwable, WsI18NException {

        // 1.- Comprobaciones de parámetros obligatórios
        Entidad entidad = validarEntidad(entidadCodigo);

        if (StringUtils.isEmpty(organismo)) {
            throw WsUtils.createWsI18NException("error.valor.requerido.ws", "organismo");
        }

        UsuarioEntidad usuarioEntidad = usuarioEntidadEjb.findByIdentificadorCodigoEntidad(UsuarioAplicacionCache.get().getUsuario().getIdentificador(), entidad.getCodigoDir3());

        // 2.- Comprobar que el usuarioEntidad existe
        if (usuarioEntidad == null) {
            throw WsUtils.createWsI18NException("registro.usuario.noExiste", UsuarioAplicacionCache.get().getUsuario().getIdentificador(), entidadCodigo);

        }

        Organismo organismoActivo = organismoEjb.findByCodigoEntidadLigero(organismo, entidad.getId());

        // 3. Comprobar que el Organismo existe y está vigente
        if (organismoActivo == null) { //No existe
            throw WsUtils.createWsI18NException("registro.organismo.noExiste", organismo);
        }

        // Retornamos el Libro de la entidad
        if (entidad.getLibro() != null) {
            return CommonConverter.getLibroWs(entidad.getLibro());
        }

        throw WsUtils.createWsI18NException("organismo.no.libroRegistro", organismoActivo.getNombreCompleto());
    }


    @Override
    @WebMethod
    @RolesAllowed({RWE_USUARI, RWE_WS_ENTRADA, RWE_WS_SALIDA})
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
     * Recorremos los organismos y  obtenemos las Oficinas que pueden Registrar en ellos.
     *
     * @param organismos
     * @param libroUnico
     * @throws Exception
     */
    private List<LibroOficinaWs> recorrerLibrosRegistro(List<Organismo> organismos, Libro libroUnico) throws Exception {

        List<LibroOficinaWs> librosOficinas = new ArrayList<LibroOficinaWs>();

        for (Organismo organismo : organismos) {
            LibroWs libroWs = new LibroWs(libroUnico.getCodigo(), libroUnico.getNombre(), libroUnico.getNombreCompleto(), null);

            // Obtenemos las Oficinas cuyo Organismo responsable es al que pertenece el Libro
            for (Oficina oficina : oficinaEjb.findByOrganismoResponsable(organismo.getId())) {
                OficinaWs oficinaWs = new OficinaWs(oficina.getCodigo(), oficina.getDenominacion());

                LibroOficinaWs libroOficinaWs = new LibroOficinaWs(libroWs, oficinaWs);
                librosOficinas.add(libroOficinaWs);
            }

            // Obtenemos las Oficinas que dan servicio al Organismo que pertenece el Libro
            for (Oficina oficina : relacionOrganizativaOfiLocalEjb.getOficinasByOrganismo(organismo.getId())) {
                OficinaWs oficinaWs = new OficinaWs(oficina.getCodigo(), oficina.getDenominacion());

                LibroOficinaWs libroOficinaWs = new LibroOficinaWs(libroWs, oficinaWs);
                librosOficinas.add(libroOficinaWs);
            }

        }

        return librosOficinas;
    }


}
