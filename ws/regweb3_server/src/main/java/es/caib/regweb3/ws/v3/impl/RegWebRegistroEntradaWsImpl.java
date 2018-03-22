package es.caib.regweb3.ws.v3.impl;

import es.caib.dir3caib.ws.api.unidad.Dir3CaibObtenerUnidadesWs;
import es.caib.dir3caib.ws.api.unidad.UnidadTF;
import es.caib.regweb3.model.*;
import es.caib.regweb3.model.utils.AnexoFull;
import es.caib.regweb3.persistence.ejb.*;
import es.caib.regweb3.persistence.utils.PropiedadGlobalUtil;
import es.caib.regweb3.persistence.utils.RespuestaDistribucion;
import es.caib.regweb3.persistence.validator.RegistroEntradaBeanValidator;
import es.caib.regweb3.persistence.validator.RegistroEntradaValidator;
import es.caib.regweb3.utils.Dir3CaibUtils;
import es.caib.regweb3.utils.RegwebConstantes;
import es.caib.regweb3.utils.StringUtils;
import es.caib.regweb3.ws.converter.RegistroEntradaConverter;
import es.caib.regweb3.ws.model.IdentificadorWs;
import es.caib.regweb3.ws.model.JustificanteWs;
import es.caib.regweb3.ws.model.RegistroEntradaResponseWs;
import es.caib.regweb3.ws.model.RegistroEntradaWs;
import es.caib.regweb3.ws.utils.UsuarioAplicacionCache;
import org.apache.log4j.Logger;
import org.fundaciobit.genapp.common.i18n.I18NException;
import org.fundaciobit.genapp.common.i18n.I18NValidationException;
import org.fundaciobit.genapp.common.ws.WsI18NException;
import org.fundaciobit.genapp.common.ws.WsValidationException;
import org.fundaciobit.plugins.documentcustody.api.SignatureCustody;
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

/**
 * Created by Fundació BIT.
 *
 * @author earrivi
 * @author anadal
 */
@SecurityDomain(RegwebConstantes.SECURITY_DOMAIN)
@Stateless(name = RegWebRegistroEntradaWsImpl.NAME + "Ejb")
@RolesAllowed({RegwebConstantes.ROL_SUPERADMIN})
@SOAPBinding(style = SOAPBinding.Style.RPC)
@org.apache.cxf.interceptor.InInterceptors(interceptors = {"es.caib.regweb3.ws.utils.RegWebInInterceptor"})
@org.apache.cxf.interceptor.InFaultInterceptors(interceptors = {"es.caib.regweb3.ws.utils.RegWebInInterceptor"})
@WebService(name = RegWebRegistroEntradaWsImpl.NAME_WS, portName = RegWebRegistroEntradaWsImpl.NAME_WS,
        serviceName = RegWebRegistroEntradaWsImpl.NAME_WS + "Service",
        endpointInterface = "es.caib.regweb3.ws.v3.impl.RegWebRegistroEntradaWs")
@WebContext(contextRoot = "/regweb3/ws", urlPattern = "/v3/" + RegWebRegistroEntradaWsImpl.NAME, transportGuarantee = TransportGuarantee.NONE, secureWSDLAccess = false, authMethod = "WSBASIC")
public class RegWebRegistroEntradaWsImpl extends AbstractRegistroWsImpl
        implements RegWebRegistroEntradaWs {

    protected final Logger log = Logger.getLogger(getClass());

    public static final String NAME = "RegWebRegistroEntrada";

    public static final String NAME_WS = NAME + "Ws";


    RegistroEntradaValidator<RegistroEntrada> registroEntradaValidator = new RegistroEntradaValidator<RegistroEntrada>();

    private StringBuilder peticion = new StringBuilder();


    @EJB(mappedName = "regweb3/OficinaEJB/local")
    private OficinaLocal oficinaEjb;

    @EJB(mappedName = "regweb3/OrganismoEJB/local")
    private OrganismoLocal organismoEjb;

    @EJB(mappedName = "regweb3/PermisoLibroUsuarioEJB/local")
    private PermisoLibroUsuarioLocal permisoLibroUsuarioEjb;

    @EJB(mappedName = "regweb3/UsuarioEntidadEJB/local")
    private UsuarioEntidadLocal usuarioEntidadEjb;

    @EJB(mappedName = "regweb3/LibroEJB/local")
    private LibroLocal libroEjb;

    @EJB(mappedName = "regweb3/TipoAsuntoEJB/local")
    private TipoAsuntoLocal tipoAsuntoEjb;

    @EJB(mappedName = "regweb3/CodigoAsuntoEJB/local")
    private CodigoAsuntoLocal codigoAsuntoEjb;

    @EJB(mappedName = "regweb3/RegistroEntradaEJB/local")
    private RegistroEntradaLocal registroEntradaEjb;

    @EJB(mappedName = "regweb3/LopdEJB/local")
    private LopdLocal lopdEjb;

    @EJB(mappedName = "regweb3/InteresadoEJB/local")
    private InteresadoLocal interesadoEjb;

    @EJB(mappedName = "regweb3/PersonaEJB/local")
    private PersonaLocal personaEjb;

    @EJB(mappedName = "regweb3/CatPaisEJB/local")
    private CatPaisLocal catPaisEjb;

    @EJB(mappedName = "regweb3/CatProvinciaEJB/local")
    private CatProvinciaLocal catProvinciaEjb;

    @EJB(mappedName = "regweb3/CatLocalidadEJB/local")
    private CatLocalidadLocal catLocalidadEjb;

    @EJB(mappedName = "regweb3/EntidadEJB/local")
    private EntidadLocal entidadEjb;

    @EJB(mappedName = "regweb3/AnexoEJB/local")
    private AnexoLocal anexoEjb;

    @EJB(mappedName = "regweb3/IntegracionEJB/local")
    private IntegracionLocal integracionEjb;

    @EJB(mappedName = "regweb3/JustificanteEJB/local")
    private JustificanteLocal justificanteEjb;


    @Override
    @RolesAllowed({ROL_USUARI})
    @WebMethod
    @Deprecated
    public IdentificadorWs altaRegistroEntrada(@WebParam(name = "registroEntradaWs")
            RegistroEntradaWs registroEntradaWs)
            throws Throwable, WsI18NException, WsValidationException {

        Entidad entidad = null;

        // Obtenemos la Entidad a la que se realiza el RegistroEntrada
        if(UsuarioAplicacionCache.get().getEntidades().size() > 1){
            log.info("Usuario asociado a varias Entidades");

            Libro libro = libroEjb.findByCodigo(registroEntradaWs.getLibro());
            // todo: Podría darse el hipotético caso que un mismo código de Libro esté presente en dos Entidades
            if(libro != null){
                entidad = libro.getOrganismo().getEntidad();
            }
        }else{
            entidad = UsuarioAplicacionCache.get().getEntidades().get(0);
        }

        if(entidad != null){
            return nuevoRegistroEntrada(entidad.getCodigoDir3(), registroEntradaWs);
        }

        throw new I18NException("error.valor.requerido.ws", "entidad");

    }

    @Override
    @RolesAllowed({ROL_USUARI})
    @WebMethod
    public IdentificadorWs nuevoRegistroEntrada(@WebParam(name = "entidad") String entidad, @WebParam(name = "registroEntradaWs")
            RegistroEntradaWs registroEntradaWs)
            throws Throwable, WsI18NException, WsValidationException {

        IdentificadorWs identificadorWs = null;
        long tiempo = System.currentTimeMillis();

        peticion.append("usuario: ").append(UsuarioAplicacionCache.get().getUsuario().getNombreCompleto()).append(System.getProperty("line.separator"));

        // 1.- Validar campo obligatorio entidad
        if(StringUtils.isEmpty(entidad)){
            throw new I18NException("error.valor.requerido.ws", "entidad");
        }

        Entidad entidadActiva = entidadEjb.findByCodigoDir3(entidad);

        // 2.- Comprobar que la entidad existe y está activa
        if(entidadActiva == null){
            log.info("La entidad "+entidad+" no existe.");
            throw new I18NException("registro.entidad.noExiste", entidad);
        }else if(!entidadActiva.getActivo()){
            throw new I18NException("registro.entidad.inactiva", entidad);
        }

        // 3.- Comprobamos que el Usuario pertenece a la Entidad indicada
        if (!UsuarioAplicacionCache.get().getEntidades().contains(entidadActiva)) {
            log.info("El usuario "+UsuarioAplicacionCache.get().getUsuario().getNombreCompleto()+" no pertenece a la entidad.");
            throw new I18NException("registro.entidad.noExiste", entidad);
        }

        // 4.- Comprobar que el Organismo destino está vigente
        Organismo destinoInterno = organismoEjb.findByCodigoEntidad(registroEntradaWs.getDestino(), entidadActiva.getId());
        UnidadTF destinoExterno = null;

        if (destinoInterno == null) { // Se trata de un destino externo

            // Lo buscamos en DIR3CAIB
            Dir3CaibObtenerUnidadesWs unidadesService = Dir3CaibUtils.getObtenerUnidadesService(PropiedadGlobalUtil.getDir3CaibServer(), PropiedadGlobalUtil.getDir3CaibUsername(), PropiedadGlobalUtil.getDir3CaibPassword());
            destinoExterno = unidadesService.obtenerUnidad(registroEntradaWs.getDestino(), null, null);

            if (destinoExterno == null) {
                throw new I18NException("registro.organismo.noExiste", registroEntradaWs.getDestino());
            } else if (!destinoExterno.getCodigoEstadoEntidad().equals(RegwebConstantes.ESTADO_ENTIDAD_VIGENTE)) {
                throw new I18NException("registro.organismo.extinguido", destinoExterno.getCodigo() + " - " + destinoExterno.getDenominacion());
            }


        } else if (!destinoInterno.getEstado().getCodigoEstadoEntidad().equals(ESTADO_ENTIDAD_VIGENTE)) { //Si está extinguido
            throw new I18NException("registro.organismo.extinguido", destinoInterno.getNombreCompleto());
        }

        // 5.- Comprobar que la Oficina está vigente
        Oficina oficina = oficinaEjb.findByCodigoEntidad(registroEntradaWs.getOficina(), entidadActiva.getId());

        if (oficina == null) { //No existe
            throw new I18NException("registro.oficina.noExiste", registroEntradaWs.getOficina());

        } else if (!oficina.getEstado().getCodigoEstadoEntidad().equals(ESTADO_ENTIDAD_VIGENTE)) { //Si está extinguido
            throw new I18NException("registro.oficina.extinguido", oficina.getNombreCompleto());
        }

        // 6.- Comprobar que el Libro está vigente
        Libro libro = libroEjb.findByCodigoEntidad(registroEntradaWs.getLibro(), entidadActiva.getId());

        if (libro == null) { //No existe
            throw new I18NException("registro.libro.noExiste", registroEntradaWs.getLibro());

        } else if (!libro.getActivo()) { //Si está extinguido
            throw new I18NException("registro.libro.inactivo", registroEntradaWs.getLibro());
        }

        // 7.- Comprobar que el usuario tiene permisos para realizar el registro de entrada
        // Nos pueden enviar el username en mayusculas
        UsuarioEntidad usuario = usuarioEntidadEjb.findByIdentificadorEntidad(registroEntradaWs.getCodigoUsuario(), entidadActiva.getId());

        if (usuario == null) {//No existe
            throw new I18NException("registro.usuario.noExiste", registroEntradaWs.getCodigoUsuario(), entidadActiva.getNombre());

        } else if (!permisoLibroUsuarioEjb.tienePermiso(usuario.getId(), libro.getId(), PERMISO_REGISTRO_ENTRADA)) {
            throw new I18NException("registro.usuario.permisos", registroEntradaWs.getCodigoUsuario(), libro.getCodigo());

        }

        // Recuperamos el username correcto 
        registroEntradaWs.setCodigoUsuario(usuario.getUsuario().getIdentificador());

        // 8.- Convertir RegistroEntradaWs a RegistroEntrada
        RegistroEntrada registroEntrada = RegistroEntradaConverter.getRegistroEntrada(
                registroEntradaWs, usuario, libro, oficina, destinoInterno, destinoExterno,
                codigoAsuntoEjb, tipoAsuntoEjb, oficinaEjb);

        // 9.- Validar el RegistroEntrada
        validateRegistroEntrada(registroEntrada);

        // 10.- Validar los Interesados
        List<Interesado> interesados = null;
        if (registroEntradaWs.getInteresados() != null && registroEntradaWs.getInteresados().size() > 0) {

            // Procesamos los interesados
            interesados = procesarInteresados(registroEntradaWs.getInteresados(), interesadoEjb, catPaisEjb, catProvinciaEjb, catLocalidadEjb, personaEjb);

            registroEntrada.getRegistroDetalle().setInteresados(null);

        } else {
            throw new I18NException("interesado.registro.obligatorio");
        }

        // 11.- Validar los Anexos
        List<AnexoFull> anexosFull = null;
        if (registroEntradaWs.getAnexos() != null && registroEntradaWs.getAnexos().size() > 0) {

            //Procesamos los anexos
            anexosFull = procesarAnexos(registroEntradaWs.getAnexos(), entidadActiva.getId());

            //Asociamos los anexos al Registro de Entrada
            registroEntrada.getRegistroDetalle().setAnexos(null);
        }

        // 12.- Creamos el Registro de Entrada
        try{
            registroEntrada = registroEntradaEjb.registrarEntrada(registroEntrada, usuario, interesados, anexosFull);

        }catch (Exception e){
            integracionEjb.addIntegracionError(RegwebConstantes.INTEGRACION_WS, UsuarioAplicacionCache.get().getMethod().getName(), peticion.toString(), e, System.currentTimeMillis() - tiempo, entidadActiva.getId());
            throw new I18NException("registro.nuevo.error");
        }

        if (registroEntrada.getId() != null) {

            // Componemos la respuesta
            identificadorWs = new IdentificadorWs(registroEntrada.getNumeroRegistroFormateado(), registroEntrada.getNumeroRegistro(), registroEntrada.getFecha());
        }

        // Integracion
        integracionEjb.addIntegracionOk(RegwebConstantes.INTEGRACION_WS, UsuarioAplicacionCache.get().getMethod().getName(),peticion.toString(), System.currentTimeMillis() - tiempo, entidadActiva.getId());

        return identificadorWs;
    }


    @RolesAllowed({ROL_USUARI})
    @WebMethod
    @Override
    public JustificanteWs obtenerJustificante(@WebParam(name = "entidad") String entidad, @WebParam(name = "numeroRegistroFormateado")String numeroRegistroFormateado) throws Throwable, WsI18NException, WsValidationException{


        //1.- Validar obligatorios
        validarObligatorios(numeroRegistroFormateado,entidad);

        // 2.- Comprobar que la entidad existe y está activa
        Entidad entidadActiva = entidadEjb.findByCodigoDir3(entidad);

        if(entidadActiva == null){
            log.info("La entidad "+entidad+" no existe.");
            throw new I18NException("registro.entidad.noExiste", entidad);
        }else if(!entidadActiva.getActivo()){
            throw new I18NException("registro.entidad.inactiva", entidad);
        }

        // 3.- Comprobamos que el Usuario pertenece a la Entidad indicada
        if (!UsuarioAplicacionCache.get().getEntidades().contains(entidadActiva)) {
            log.info("El usuario "+UsuarioAplicacionCache.get().getUsuario().getNombreCompleto()+" no pertenece a la entidad.");
            throw new I18NException("registro.usuario.entidad",UsuarioAplicacionCache.get().getUsuario().getNombreCompleto(), entidad);
        }


        UsuarioEntidad usuario = usuarioEntidadEjb.findByIdentificadorEntidad(UsuarioAplicacionCache.get().getUsuario().getIdentificador(), entidadActiva.getId());

        // 4.- Obtenemos el RegistroEntrada
        RegistroEntrada registroEntrada = registroEntradaEjb.findByNumeroRegistroFormateadoConAnexos(entidad, numeroRegistroFormateado, null);


        if (registroEntrada == null) {
            throw new I18NException("registroEntrada.noExiste", numeroRegistroFormateado);
        }

        // 5.- Generamos o descargamos el Justificante
        AnexoFull justificante = null;
        SignatureCustody sc = null;

        // Si no tiene Justificante, lo generamos
        if(!registroEntrada.getRegistroDetalle().getTieneJustificante()){

            // Permisos para Modificar el RegistroEntrada?
            if (!permisoLibroUsuarioEjb.tienePermiso(usuario.getId(), registroEntrada.getLibro().getId(), PERMISO_MODIFICACION_REGISTRO_ENTRADA)) {
                throw new I18NException("registroEntrada.usuario.permisos", usuario.getNombreCompleto());
            }

            // Solo se puede generar si el registro es Válido
            if(registroEntrada.getEstado().equals(REGISTRO_VALIDO)){
                justificante = justificanteEjb.crearJustificante(usuario,registroEntrada,RegwebConstantes.REGISTRO_ENTRADA_ESCRITO.toLowerCase(),"ca");
                sc = justificante.getSignatureCustody();
                // Alta en la tabla de LOPD
                lopdEjb.altaLopd(registroEntrada.getNumeroRegistro(), registroEntrada.getFecha(), registroEntrada.getLibro().getId(), usuario.getId(), RegwebConstantes.REGISTRO_ENTRADA, RegwebConstantes.LOPD_JUSTIFICANTE);
            }else{
                throw new I18NException("registro.justificante.valido");
            }


        }else{ // Tiene Justificante, lo obtenemos

            // Permisos para Consultar el RegistroEntrada?
            if (!permisoLibroUsuarioEjb.tienePermiso(usuario.getId(), registroEntrada.getLibro().getId(), PERMISO_CONSULTA_REGISTRO_ENTRADA)) {
                throw new I18NException("registroEntrada.usuario.permisos", usuario.getNombreCompleto());
            }

            justificante = anexoEjb.getAnexoFullLigero(anexoEjb.getIdJustificante(registroEntrada.getRegistroDetalle().getId()), entidadActiva.getId());
            sc = anexoEjb.getFirma(justificante.getAnexo().getCustodiaID(), true, entidadActiva.getId());
            // Alta en la tabla de LOPD
            lopdEjb.altaLopd(registroEntrada.getNumeroRegistro(), registroEntrada.getFecha(), registroEntrada.getLibro().getId(), usuario.getId(), RegwebConstantes.REGISTRO_ENTRADA, RegwebConstantes.LOPD_JUSTIFICANTE);
        }

        return new JustificanteWs(sc.getData());
    }

    @RolesAllowed({ROL_USUARI})
    @Override
    @WebMethod
    public void anularRegistroEntrada(
            @WebParam(name = "numeroRegistroFormateado") String numeroRegistroFormateado,
            @WebParam(name = "usuario") String usuario,
            @WebParam(name = "entidad") String entidad,
            @WebParam(name = "anular") boolean anular)
            throws Throwable, WsI18NException, WsValidationException {


        //1.- Validar obligatorios
        validarObligatorios(numeroRegistroFormateado,entidad);

        //2.- Comprobar que el usuario existe en la Entidad proporcionada
        UsuarioEntidad usuarioEntidad = usuarioEntidadEjb.findByIdentificadorCodigoEntidad( UsuarioAplicacionCache.get().getUsuario().getIdentificador(), entidad);

        if (usuarioEntidad == null) {//No existe
            throw new I18NException("registroEntrada.usuario.noExiste", UsuarioAplicacionCache.get().getUsuario().getIdentificador(), entidad);
        }

        // 3.- Obtenemos el RegistroEntrada
        RegistroEntrada registroEntrada = registroEntradaEjb.findByNumeroRegistroFormateado(entidad, numeroRegistroFormateado,null);

        if (registroEntrada == null) {
            throw new I18NException("registroEntrada.noExiste", numeroRegistroFormateado);
        }

        // 4.- Comprobamos si el RegistroEntrada se puede anular según su estado.
        final List<Long> estados = new ArrayList<Long>();
        estados.add(RegwebConstantes.REGISTRO_RESERVA);
        estados.add(RegwebConstantes.REGISTRO_VALIDO);
        estados.add(RegwebConstantes.REGISTRO_PENDIENTE_VISAR);

        if (!estados.contains(registroEntrada.getEstado())) {
            throw new I18NException("registroEntrada.anulado");
        }

        // 5.- Comprobamos que el usuario tiene permisos de modificación para el RegistroEntrada
        if (!permisoLibroUsuarioEjb.tienePermiso(usuarioEntidad.getId(), registroEntrada.getLibro().getId(), PERMISO_MODIFICACION_REGISTRO_ENTRADA)) {
            throw new I18NException("registroEntrada.usuario.permisos", usuarioEntidad.getUsuario().getNombreCompleto());
        }

        // 6.- Anulamos el RegistroEntrada
        // TODO Falta enviar boolean anular
        registroEntradaEjb.anularRegistroEntrada(registroEntrada, usuarioEntidad);
    }



    @RolesAllowed({ROL_USUARI})
    @Override
    @WebMethod
    public void tramitarRegistroEntrada(@WebParam(name = "numeroRegistroFormateado") String numeroRegistroFormateado, @WebParam(name = "usuario") String usuario, @WebParam(name = "entidad") String entidad) throws Throwable, WsI18NException, WsValidationException {

        //1.- Validar obligatorios
        validarObligatorios(numeroRegistroFormateado,entidad);


        // 2.- Comprobar que el usuario existe en la Entidad proporcionada
        UsuarioEntidad usuarioEntidad = usuarioEntidadEjb.findByIdentificadorCodigoEntidad( UsuarioAplicacionCache.get().getUsuario().getIdentificador(), entidad);

        if (usuarioEntidad == null) {//No existe
            throw new I18NException("registroEntrada.usuario.noExiste", UsuarioAplicacionCache.get().getUsuario().getIdentificador(), entidad);
        }

        // 3.- Obtenemos el RegistroEntrada
        RegistroEntrada registroEntrada = registroEntradaEjb.findByNumeroRegistroFormateado(entidad, numeroRegistroFormateado, null);

        if (registroEntrada == null) {
            throw new I18NException("registroEntrada.noExiste", numeroRegistroFormateado);
        }

        // 4.- Comprobamos si el RegistroEntrada tiene el estado Válido
        if (!registroEntrada.getEstado().equals(RegwebConstantes.REGISTRO_VALIDO)) {
            throw new I18NException("registroEntrada.tramitar.error");
        }

        // 5.- Comprobamos que el Organismo destino pertenece a la misma administración
        if (!registroEntrada.getOficina().getOrganismoResponsable().equals(registroEntrada.getDestino())) {
            throw new I18NException("registroEntrada.tramitar.error");
        }

        // 6.- Generamos el justificante del RegistroEntrada
        registroEntrada = registroEntradaEjb.generarJustificanteRegistroEntrada(registroEntrada, usuarioEntidad);


        // 7.- Tramitamos el RegistroEntrada
        registroEntradaEjb.tramitarRegistroEntrada(registroEntrada, usuarioEntidad);

    }


    @RolesAllowed({ROL_USUARI})
    @Override
    @WebMethod
    public void distribuirRegistroEntrada(@WebParam(name = "numeroRegistroFormateado") String numeroRegistroFormateado, @WebParam(name = "entidad") String entidad) throws Throwable, WsI18NException, WsValidationException {

        //1.- Validar obligatorios
        validarObligatorios(numeroRegistroFormateado,entidad);

        // 2.- Comprobar que la entidad existe y está activa
        Entidad entidadActiva = entidadEjb.findByCodigoDir3(entidad);

        if(entidadActiva == null){
            log.info("La entidad "+entidad+" no existe.");
            throw new I18NException("registro.entidad.noExiste", entidad);
        }else if(!entidadActiva.getActivo()){
            throw new I18NException("registro.entidad.inactiva", entidad);
        }

        // 3.- Comprobamos que el Usuario pertenece a la Entidad indicada
        if (!UsuarioAplicacionCache.get().getEntidades().contains(entidadActiva)) {
            log.info("El usuario "+UsuarioAplicacionCache.get().getUsuario().getNombreCompleto()+" no pertenece a la entidad.");
            throw new I18NException("registro.usuario.entidad",UsuarioAplicacionCache.get().getUsuario().getNombreCompleto(), entidad);
        }

        UsuarioEntidad usuario = usuarioEntidadEjb.findByIdentificadorEntidad(UsuarioAplicacionCache.get().getUsuario().getIdentificador(), entidadActiva.getId());

        // 4.- Obtenemos el RegistroEntrada
        RegistroEntrada registroEntrada = registroEntradaEjb.findByNumeroRegistroFormateadoConAnexos(entidad, numeroRegistroFormateado,null);


        if (registroEntrada == null) {
            throw new I18NException("registroEntrada.noExiste", numeroRegistroFormateado);
        }

        // 5.- Comprobamos que el usuario tiene permisos para Distribuir el registro
        if(!permisoLibroUsuarioEjb.tienePermiso(usuario.getId(), registroEntrada.getLibro().getId(), RegwebConstantes.PERMISO_DISTRIBUCION_REGISTRO)){
            throw new I18NException("registroEntrada.distribuir.error.permiso");
        }

        //6.- Obtenemos los organismos de la oficina en la que se ha realizado el registro que hace de oficinaActiva
        LinkedHashSet<Organismo> organismosOficinaRegistro = new LinkedHashSet<Organismo>(organismoEjb.getByOficinaActiva(registroEntrada.getOficina()));

        // Comprobamos que el RegistroEntrada se puede Distribuir
        if (!registroEntradaEjb.isDistribuir(registroEntrada.getId(), getOrganismosOficioRemision(organismosOficinaRegistro))) {
            throw new I18NException("registroEntrada.distribuir.noPermitido");
        }

        try{
            // 7.- Distribuimos el registro de entrada
            AnexoFull justificante = null;
            if(!registroEntrada.getRegistroDetalle().getTieneJustificante()) {
                justificante = justificanteEjb.crearJustificante(registroEntrada.getUsuario(), registroEntrada, RegwebConstantes.REGISTRO_ENTRADA_ESCRITO.toLowerCase(), RegwebConstantes.IDIOMA_CATALAN_CODIGO);
                registroEntrada.getRegistroDetalle().getAnexosFull().add(justificante);
            }
            RespuestaDistribucion respuestaDistribucion = registroEntradaEjb.distribuir(registroEntrada, usuario);

            // Si el Plugin permite seleccionar Destinatarios, no se puede distribuir automaticamente
            if(respuestaDistribucion.getDestinatarios() != null){
                throw new I18NException("registroEntrada.distribuir.destinatarios");
            }

            // Si no se ha enviado, es porque algo ha fallado
            if(!respuestaDistribucion.getEnviado()){
                throw new I18NException("registroEntrada.distribuir.error");
            }

        }catch (Exception e){
            throw new I18NException("registroEntrada.distribuir.error");
        }

    }


    /**
     * @param anyo
     * @param numeroRegistro
     * @param libro
     * @param usuario
     * @param entidad
     * @return
     * @throws Throwable
     * @throws WsI18NException
     */
    @RolesAllowed({ROL_USUARI})
    @Override
    @WebMethod
    public IdentificadorWs obtenerRegistroEntradaID(
            @WebParam(name = "anyo") int anyo,
            @WebParam(name = "numeroRegistro") int numeroRegistro,
            @WebParam(name = "libro") String libro,
            @WebParam(name = "usuario") String usuario,
            @WebParam(name = "entidad") String entidad)
            throws Throwable, WsI18NException {


        // 1.- Validaciones comunes
        if (anyo <= 0) {
            throw new I18NException("error.valor.requerido.ws", "anyo");
        }

        if (numeroRegistro <= 0) {
            throw new I18NException("error.valor.requerido.ws", "numeroRegistro");
        }

        if (StringUtils.isEmpty(libro)) {
            throw new I18NException("error.valor.requerido.ws", "libro");
        }

        if (StringUtils.isEmpty(usuario)) {
            throw new I18NException("error.valor.requerido.ws", "usuario");
        }

        if (StringUtils.isEmpty(entidad)) {
            throw new I18NException("error.valor.requerido.ws", "entidad");
        }


        // 2.- Comprobar que el usuario existe en la Entidad proporcionada
        UsuarioEntidad usuarioEntidad = usuarioEntidadEjb.findByIdentificadorCodigoEntidad(usuario, entidad);

        if (usuarioEntidad == null) {//No existe
            throw new I18NException("registroEntrada.usuario.noExiste", usuario, entidad);
        }

        // 3.- Existe libro
        Libro libroObj = libroEjb.findByCodigoEntidad(libro, usuarioEntidad.getEntidad().getId());
        if (libroObj == null) {
            throw new I18NException("registro.libro.noExiste", libro);
        }

        // 4.- Comprobamos que el usuario tiene permisos de lectura para el RegistroEntrada
        if (!permisoLibroUsuarioEjb.tienePermiso(usuarioEntidad.getId(), libroObj.getId(), PERMISO_CONSULTA_REGISTRO_ENTRADA)) {
            throw new I18NException("registroEntrada.usuario.permisos", usuario);
        }

        // 3.- Obtenemos el registro
        RegistroEntrada registro;
        registro = registroEntradaEjb.findByNumeroAnyoLibro(numeroRegistro, anyo, libro);
        if (registro == null) {
            throw new I18NException("registroEntrada.noExiste", numeroRegistro
                    + "/" + anyo + " (" + libro + ")");
        }

        // LOPD
        lopdEjb.altaLopd(registro.getNumeroRegistro(), registro.getFecha(), registro.getLibro().getId(), usuarioEntidad.getId(), RegwebConstantes.REGISTRO_ENTRADA, RegwebConstantes.LOPD_CONSULTA);

        return new IdentificadorWs(registro.getNumeroRegistroFormateado(), numeroRegistro, registro.getFecha());

    }


    @RolesAllowed({ROL_USUARI})
    @Override
    @WebMethod
    public RegistroEntradaResponseWs obtenerRegistroEntrada(
            @WebParam(name = "numeroRegistroFormateado") String numeroRegistroFormateado,
            @WebParam(name = "usuario") String usuario,
            @WebParam(name = "entidad") String entidad)
            throws Throwable, WsI18NException, WsValidationException {

        //1.- Validar obligatorios
        validarObligatorios(numeroRegistroFormateado,entidad);

        // 2.- Comprobar que el usuario existe en la Entidad proporcionada
        UsuarioEntidad usuarioEntidad = usuarioEntidadEjb.findByIdentificadorCodigoEntidad( UsuarioAplicacionCache.get().getUsuario().getIdentificador(), entidad);

        if(usuarioEntidad == null){//No existe
            throw new I18NException("registroEntrada.usuario.noExiste", UsuarioAplicacionCache.get().getUsuario().getIdentificador(), entidad);
        }

        // 3.- Obtenemos el RegistroEntrada
        RegistroEntrada registro = registroEntradaEjb.findByNumeroRegistroFormateado(entidad, numeroRegistroFormateado,null);

        if (registro == null) {
            throw new I18NException("registroEntrada.noExiste", numeroRegistroFormateado);
        }

        // 4.- Comprobamos que el usuario tiene permisos de lectura para el RegistroEntrada
        if (!permisoLibroUsuarioEjb.tienePermiso(usuarioEntidad.getId(), registro.getLibro().getId(), PERMISO_CONSULTA_REGISTRO_ENTRADA)) {
            throw new I18NException("registroEntrada.usuario.permisos", usuarioEntidad.getUsuario().getNombreCompleto());
        }

        // LOPD
        lopdEjb.altaLopd(registro.getNumeroRegistro(), registro.getFecha(), registro.getLibro().getId(), usuarioEntidad.getId(), RegwebConstantes.REGISTRO_ENTRADA, RegwebConstantes.LOPD_CONSULTA);

        // Retornamos el RegistroEntradaResponseWs
        return RegistroEntradaConverter.getRegistroEntradaResponseWs(registro,
                UsuarioAplicacionCache.get().getIdioma(), anexoEjb);



    }

    /**
     * @param registroEntrada
     * @throws org.fundaciobit.genapp.common.i18n.I18NValidationException
     */
    private void validateRegistroEntrada(RegistroEntrada registroEntrada) throws I18NValidationException, I18NException {
        RegistroEntradaBeanValidator rebv = new RegistroEntradaBeanValidator(registroEntradaValidator);
        rebv.throwValidationExceptionIfErrors(registroEntrada, true);
    }



}
