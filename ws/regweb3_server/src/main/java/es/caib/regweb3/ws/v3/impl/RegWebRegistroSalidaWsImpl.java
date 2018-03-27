package es.caib.regweb3.ws.v3.impl;

import es.caib.regweb3.model.*;
import es.caib.regweb3.model.utils.AnexoFull;
import es.caib.regweb3.persistence.ejb.*;
import es.caib.regweb3.persistence.validator.RegistroSalidaBeanValidator;
import es.caib.regweb3.persistence.validator.RegistroSalidaValidator;
import es.caib.regweb3.utils.RegwebConstantes;
import es.caib.regweb3.utils.StringUtils;
import es.caib.regweb3.ws.converter.RegistroSalidaConverter;
import es.caib.regweb3.ws.model.IdentificadorWs;
import es.caib.regweb3.ws.model.JustificanteWs;
import es.caib.regweb3.ws.model.RegistroSalidaResponseWs;
import es.caib.regweb3.ws.model.RegistroSalidaWs;
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
import java.util.List;

/**
 * Created by Fundació BIT.
 *
 * @author earrivi
 * @author anadal
 */
@SecurityDomain(RegwebConstantes.SECURITY_DOMAIN)
@Stateless(name = RegWebRegistroSalidaWsImpl.NAME + "Ejb")
@RolesAllowed({RegwebConstantes.ROL_SUPERADMIN})
@SOAPBinding(style = SOAPBinding.Style.RPC)
@org.apache.cxf.interceptor.InInterceptors(interceptors = {"es.caib.regweb3.ws.utils.RegWebInInterceptor"})
@org.apache.cxf.interceptor.InFaultInterceptors(interceptors = {"es.caib.regweb3.ws.utils.RegWebInInterceptor"})
@WebService(name = RegWebRegistroSalidaWsImpl.NAME_WS, portName = RegWebRegistroSalidaWsImpl.NAME_WS,
        serviceName = RegWebRegistroSalidaWsImpl.NAME_WS + "Service",
        endpointInterface = "es.caib.regweb3.ws.v3.impl.RegWebRegistroSalidaWs")
@WebContext(contextRoot = "/regweb3/ws", urlPattern = "/v3/" + RegWebRegistroSalidaWsImpl.NAME, transportGuarantee = TransportGuarantee.NONE, secureWSDLAccess = false, authMethod = "WSBASIC")
public class RegWebRegistroSalidaWsImpl extends AbstractRegistroWsImpl implements RegWebRegistroSalidaWs {

    protected final Logger log = Logger.getLogger(getClass());

    public static final String NAME = "RegWebRegistroSalida";

    public static final String NAME_WS = NAME + "Ws";


    RegistroSalidaValidator<RegistroSalida> validator = new RegistroSalidaValidator<RegistroSalida>();

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

    @EJB(mappedName = "regweb3/RegistroSalidaEJB/local")
    private RegistroSalidaLocal registroSalidaEjb;

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

    @EJB(mappedName = "regweb3/IntegracionEJB/local")
    private IntegracionLocal integracionEjb;

    @EJB(mappedName = "regweb3/JustificanteEJB/local")
    private JustificanteLocal justificanteEjb;


    @Override
    @RolesAllowed({ROL_USUARI})
    @WebMethod
    @Deprecated
    public IdentificadorWs altaRegistroSalida(@WebParam(name = "registroSalidaWs") RegistroSalidaWs registroSalidaWs) throws Throwable, WsI18NException, WsValidationException {

        Entidad entidad = null;

        // Obtenemos la Entidad a la que se realiza el RegistroSalida
        if(UsuarioAplicacionCache.get().getEntidades().size() > 1){
            log.info("Usuario asociado a varias Entidades");

            Libro libro = libroEjb.findByCodigo(registroSalidaWs.getLibro());
            // todo: Podría darse el hipotético caso que un mismo código de Libro esté presente en dos Entidades
            if(libro != null){
                entidad = libro.getOrganismo().getEntidad();
            }
        }else{
            entidad = UsuarioAplicacionCache.get().getEntidades().get(0);
        }

        if(entidad != null){
            return nuevoRegistroSalida(entidad.getCodigoDir3(), registroSalidaWs);
        }

        throw new I18NException("error.valor.requerido.ws", "entidad");
    }

    @Override
    @RolesAllowed({ROL_USUARI})
    @WebMethod
    public IdentificadorWs nuevoRegistroSalida(@WebParam(name = "entidad") String entidad, @WebParam(name = "registroSalidaWs") RegistroSalidaWs registroSalidaWs) throws Throwable, WsI18NException, WsValidationException {

        IdentificadorWs identificadorWs = null;
        StringBuilder peticion = new StringBuilder();
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
            log.info("El usuario "+UsuarioAplicacionCache.get().getUsuario().getNombreCompleto()+" no pertenece a la entidad " + entidadActiva.getNombre());
            throw new I18NException("registro.entidad.noExiste", entidad);
        }

        // 1.- Comprobar que el Organismo destino está vigente
        Organismo origen = organismoEjb.findByCodigoEntidad(registroSalidaWs.getOrigen(), entidadActiva.getId());

        if (origen == null) { //Si no existe todo: Hay que agregar origenes externos?
            throw new I18NException("registro.organismo.noExiste", registroSalidaWs.getOrigen());

        } else if (!origen.getEstado().getCodigoEstadoEntidad().equals(ESTADO_ENTIDAD_VIGENTE)) { //Si está extinguido
            throw new I18NException("registro.organismo.extinguido", origen.getNombreCompleto());
        }

        // 2.- Comprobar que la Oficina está vigente
        Oficina oficina = oficinaEjb.findByCodigoEntidad(registroSalidaWs.getOficina(), entidadActiva.getId());

        if (oficina == null) { //No existe
            throw new I18NException("registro.oficina.noExiste", registroSalidaWs.getOrigen());

        } else if (!oficina.getEstado().getCodigoEstadoEntidad().equals(ESTADO_ENTIDAD_VIGENTE)) { //Si está extinguido
            throw new I18NException("registro.oficina.extinguido", oficina.getNombreCompleto());
        }

        // 3.- Comprobar que el Libro está vigente
        Libro libro = libroEjb.findByCodigoEntidad(registroSalidaWs.getLibro(), entidadActiva.getId());

        if (libro == null) { //No existe
            throw new I18NException("registro.libro.noExiste", registroSalidaWs.getLibro());

        } else if (!libro.getActivo()) { //Si está extinguido
            throw new I18NException("registro.libro.inactivo", registroSalidaWs.getLibro());
        }

        // 4.- Comprobar que el usuario tiene permisos para realizar el registro de salida
        UsuarioEntidad usuario = usuarioEntidadEjb.findByIdentificadorEntidad(registroSalidaWs.getCodigoUsuario(), entidadActiva.getId());

        if (usuario == null) {//No existe
            throw new I18NException("registro.usuario.noExiste", registroSalidaWs.getCodigoUsuario(), entidadActiva.getNombre());

        } else if (!permisoLibroUsuarioEjb.tienePermiso(usuario.getId(), libro.getId(), PERMISO_REGISTRO_SALIDA)) {
            throw new I18NException("registro.usuario.permisos", registroSalidaWs.getCodigoUsuario(), libro.getCodigo());
        }

        // Recuperamos el username correcto
        registroSalidaWs.setCodigoUsuario(usuario.getUsuario().getIdentificador());

        // 5.- Convertir RegistroSalidaWs a RegistroSalida
        RegistroSalida registroSalida = RegistroSalidaConverter.getRegistroSalida(
                registroSalidaWs, usuario, libro, oficina, origen,
                codigoAsuntoEjb, tipoAsuntoEjb, oficinaEjb);

        // 6.- Validar el RegistroSalida
        validateRegistroSalida(registroSalida);

        // 7.- Validar los Interesados
        List<Interesado> interesados = null;
        if (registroSalidaWs.getInteresados() != null && registroSalidaWs.getInteresados().size() > 0) {

            // Procesamos los interesados
            interesados = procesarInteresados(registroSalidaWs.getInteresados(), interesadoEjb, catPaisEjb, catProvinciaEjb, catLocalidadEjb, personaEjb);

            registroSalida.getRegistroDetalle().setInteresados(null);

        } else {
            throw new I18NException("interesado.registro.obligatorio");
        }


        // 8.- Validar los Anexos
        List<AnexoFull> anexosFull = null;
        if (registroSalidaWs.getAnexos() != null && registroSalidaWs.getAnexos().size() > 0) {

            //Procesamos los anexos
            anexosFull = procesarAnexos(registroSalidaWs.getAnexos(), entidadActiva.getId());

            registroSalida.getRegistroDetalle().setAnexos(null);
        }

        // 7.- Creamos el Registro de Salida
        try{
            registroSalida = registroSalidaEjb.registrarSalida(registroSalida, usuario, interesados, anexosFull);

        }catch (Exception e){
            integracionEjb.addIntegracionError(RegwebConstantes.INTEGRACION_WS, UsuarioAplicacionCache.get().getMethod().getName(), peticion.toString(), e, System.currentTimeMillis() - tiempo, entidadActiva.getId());
            throw new I18NException("registro.nuevo.error");
        }


        if (registroSalida.getId() != null) {
            // Componemos la respuesta
            identificadorWs = new IdentificadorWs(registroSalida.getNumeroRegistroFormateado(), registroSalida.getNumeroRegistro(), registroSalida.getFecha());
        }

        // Integracion
        peticion.append("oficina: ").append(registroSalida.getOficina().getDenominacion()).append(System.getProperty("line.separator"));
        peticion.append("registro: ").append(registroSalida.getNumeroRegistroFormateado()).append(System.getProperty("line.separator"));
        integracionEjb.addIntegracionOk(RegwebConstantes.INTEGRACION_WS, UsuarioAplicacionCache.get().getMethod().getName(),peticion.toString(), System.currentTimeMillis() - tiempo, entidadActiva.getId());

        return identificadorWs;
    }

    @RolesAllowed({ROL_USUARI})
    @WebMethod
    @Override
    public JustificanteWs obtenerJustificante(@WebParam(name = "entidad") String entidad, @WebParam(name = "numeroRegistroFormateado") String numeroRegistroFormateado) throws Throwable, WsI18NException, WsValidationException{

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

        // 4.- Obtenemos el RegistroSalida
        RegistroSalida registroSalida = registroSalidaEjb.findByNumeroRegistroFormateadoConAnexos(entidad, numeroRegistroFormateado,null);

        if (registroSalida == null) {
            throw new I18NException("registroEntrada.noExiste", numeroRegistroFormateado);
        }

        // 5.- Generamos o descargamos el Justificante
        AnexoFull justificante = null;
        SignatureCustody sc = null;

        // Si no tiene Justificante, lo generamos
        if(!registroSalida.getRegistroDetalle().getTieneJustificante()){

            // Permisos para Modificar el RegistroSalida?
            if (!permisoLibroUsuarioEjb.tienePermiso(usuario.getId(), registroSalida.getLibro().getId(), PERMISO_MODIFICACION_REGISTRO_SALIDA)) {
                throw new I18NException("registroEntrada.usuario.permisos", usuario.getNombreCompleto());
            }

            // Solo se puede generar si el registro es Válido
            if(registroSalida.getEstado().equals(REGISTRO_VALIDO)) {
                justificante = justificanteEjb.crearJustificante(usuario,registroSalida,RegwebConstantes.REGISTRO_SALIDA_ESCRITO.toLowerCase(),"ca");
                sc = justificante.getSignatureCustody();
                // Alta en la tabla de LOPD
                lopdEjb.altaLopd(registroSalida.getNumeroRegistro(), registroSalida.getFecha(), registroSalida.getLibro().getId(), usuario.getId(), RegwebConstantes.REGISTRO_SALIDA, RegwebConstantes.LOPD_JUSTIFICANTE);
            }else{
                throw new I18NException("registro.justificante.valido");
            }

        }else{ // Tiene Justificante, lo obtenemos

            // Permisos para Consultar el RegistroSalida?
            if (!permisoLibroUsuarioEjb.tienePermiso(usuario.getId(), registroSalida.getLibro().getId(), PERMISO_CONSULTA_REGISTRO_SALIDA)) {
                throw new I18NException("registroEntrada.usuario.permisos", usuario.getNombreCompleto());
            }

            justificante = anexoEjb.getAnexoFullLigero(anexoEjb.getIdJustificante(registroSalida.getRegistroDetalle().getId()), entidadActiva.getId());
            sc = anexoEjb.getFirma(justificante.getAnexo().getCustodiaID(), true, entidadActiva.getId());
            // Alta en la tabla de LOPD
            lopdEjb.altaLopd(registroSalida.getNumeroRegistro(), registroSalida.getFecha(), registroSalida.getLibro().getId(), usuario.getId(), RegwebConstantes.REGISTRO_SALIDA, RegwebConstantes.LOPD_JUSTIFICANTE);
        }

        return new JustificanteWs(sc.getData());
    }

    @RolesAllowed({ROL_USUARI})
    @Override
    @WebMethod
    public void anularRegistroSalida(@WebParam(name = "numeroRegistro") String numeroRegistro,
                                     @WebParam(name = "usuario") String usuario,
                                     @WebParam(name = "entidad") String entidad,
                                     @WebParam(name = "anular") boolean anular)
            throws Throwable, WsI18NException, WsValidationException {

        //1.- Validar obligatorios
        validarObligatorios(numeroRegistro,entidad);


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
            log.info("El usuario "+UsuarioAplicacionCache.get().getUsuario().getNombreCompleto()+" no pertenece a la entidad " + entidadActiva.getNombre());
            throw new I18NException("registro.entidad.noExiste", entidad);
        }

        // 4.- Comprobar que el usuario existe en la Entidad proporcionada
        UsuarioEntidad usuarioEntidad = usuarioEntidadEjb.findByIdentificadorCodigoEntidad( UsuarioAplicacionCache.get().getUsuario().getIdentificador(), entidad);


        // 5.- Obtenemos el RegistroSalida
        RegistroSalida registroSalida = registroSalidaEjb.findByNumeroRegistroFormateado(entidad, numeroRegistro,null);

        if (registroSalida == null) {
            throw new I18NException("registroEntrada.noExiste", numeroRegistro);
        }

        // 6.- Comprobamos si el RegistroSalida se puede anular según su estado.
        final List<Long> estados = new ArrayList<Long>();
        estados.add(RegwebConstantes.REGISTRO_RESERVA);
        estados.add(RegwebConstantes.REGISTRO_VALIDO);
        estados.add(RegwebConstantes.REGISTRO_PENDIENTE_VISAR);

        if (!estados.contains(registroSalida.getEstado())) {
            throw new I18NException("registroSalida.anulado");
        }

        // 7.- Comprobamos que el usuario tiene permisos de modificación para el RegistroSalida
        if (!permisoLibroUsuarioEjb.tienePermiso(usuarioEntidad.getId(), registroSalida.getLibro().getId(), PERMISO_MODIFICACION_REGISTRO_SALIDA)) {
            throw new I18NException("registroEntrada.usuario.permisos", usuarioEntidad.getNombreCompleto());
        }

        // 8.- Anulamos el RegistroSalida
        // TODO Falta Afegir paràmetre
        registroSalidaEjb.anularRegistroSalida(registroSalida, usuarioEntidad);

    }

    @RolesAllowed({ROL_USUARI})
    @Override
    @WebMethod
    public RegistroSalidaResponseWs obtenerRegistroSalida(
            @WebParam(name = "numeroRegistro") String numeroRegistro,
            @WebParam(name = "usuario") String usuario,
            @WebParam(name = "entidad") String entidad) throws Throwable, WsI18NException, WsValidationException {

        StringBuilder peticion = new StringBuilder();
        long tiempo = System.currentTimeMillis();
        peticion.append("usuario: ").append(UsuarioAplicacionCache.get().getUsuario().getNombreCompleto()).append(System.getProperty("line.separator"));

        //1.- Validar obligatorios
        validarObligatorios(numeroRegistro,entidad);

        peticion.append("numeroRegistro: ").append(numeroRegistro).append(System.getProperty("line.separator"));

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
            log.info("El usuario "+UsuarioAplicacionCache.get().getUsuario().getNombreCompleto()+" no pertenece a la entidad " + entidadActiva.getNombre());
            throw new I18NException("registroEntrada.usuario.noExiste", entidad);
        }

        UsuarioEntidad usuarioEntidad = usuarioEntidadEjb.findByIdentificadorCodigoEntidad(UsuarioAplicacionCache.get().getUsuario().getIdentificador(), entidad);

        if(usuarioEntidad == null){//No existe
            throw new I18NException("registroEntrada.usuario.noExiste", UsuarioAplicacionCache.get().getUsuario().getIdentificador(), entidad);
        }

        // 4.- Obtenemos el RegistroSalida
        RegistroSalida registro = registroSalidaEjb.findByNumeroRegistroFormateado(entidad, numeroRegistro,null);

        if (registro == null) {
            throw new I18NException("registroEntrada.noExiste", numeroRegistro);
        }

        // 5.- Comprobamos que el usuario tiene permisos de lectura para el RegistroSalida
        if (!permisoLibroUsuarioEjb.tienePermiso(usuarioEntidad.getId(), registro.getLibro().getId(), PERMISO_CONSULTA_REGISTRO_SALIDA)) {
            throw new I18NException("registroSalida.usuario.permisos", usuarioEntidad.getUsuario().getNombreCompleto());
        }

        // Retornamos el RegistroSalidaResponseWs
        RegistroSalidaResponseWs responseWs = null;
        try{
            responseWs = RegistroSalidaConverter.getRegistroSalidaResponseWs(registro,
                    UsuarioAplicacionCache.get().getIdioma(), anexoEjb);
        }catch (Exception e){

            integracionEjb.addIntegracionError(RegwebConstantes.INTEGRACION_WS, UsuarioAplicacionCache.get().getMethod().getName(), peticion.toString(), e, System.currentTimeMillis() - tiempo, entidadActiva.getId());
            throw new I18NException("registro.obtener.error");
        }

        // LOPD
        lopdEjb.altaLopd(registro.getNumeroRegistro(), registro.getFecha(), registro.getLibro().getId(), usuarioEntidad.getId(), RegwebConstantes.REGISTRO_SALIDA, RegwebConstantes.LOPD_CONSULTA);

        // Integracion
        integracionEjb.addIntegracionOk(RegwebConstantes.INTEGRACION_WS, UsuarioAplicacionCache.get().getMethod().getName(),peticion.toString(), System.currentTimeMillis() - tiempo, entidadActiva.getId());

        return responseWs;

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
    public IdentificadorWs obtenerRegistroSalidaID(
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

        // 4.- Comprobamos que el usuario tiene permisos de lectura para el RegistroSalida
        if (!permisoLibroUsuarioEjb.tienePermiso(usuarioEntidad.getId(), libroObj.getId(), PERMISO_CONSULTA_REGISTRO_SALIDA)) {
            throw new I18NException("registroEntrada.usuario.permisos", usuario);
        }

        // 3.- Obtenemos el registro
        RegistroSalida registro;
        registro = registroSalidaEjb.findByNumeroAnyoLibro(numeroRegistro, anyo, libro);
        if (registro == null) {
            throw new I18NException("registroEntrada.noExiste", numeroRegistro
                    + "/" + anyo + " (" + libro + ")");
        }

        // LOPD
        lopdEjb.altaLopd(registro.getNumeroRegistro(), registro.getFecha(), registro.getLibro().getId(), usuarioEntidad.getId(), RegwebConstantes.REGISTRO_SALIDA, RegwebConstantes.LOPD_CONSULTA);

        return new IdentificadorWs(registro.getNumeroRegistroFormateado(), numeroRegistro, registro.getFecha());
    }


    /**
     * @param registroSalida
     * @throws org.fundaciobit.genapp.common.i18n.I18NValidationException
     */
    private void validateRegistroSalida(RegistroSalida registroSalida) throws I18NValidationException, I18NException {

        RegistroSalidaBeanValidator rsbv = new RegistroSalidaBeanValidator(validator);

        rsbv.throwValidationExceptionIfErrors(registroSalida, true);
    }

}
