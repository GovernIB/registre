package es.caib.regweb3.ws.v3.impl;

import es.caib.regweb3.model.*;
import es.caib.regweb3.model.utils.AnexoFull;
import es.caib.regweb3.model.utils.AnexoSimple;
import es.caib.regweb3.persistence.ejb.RegistroSalidaConsultaLocal;
import es.caib.regweb3.persistence.ejb.RegistroSalidaLocal;
import es.caib.regweb3.persistence.utils.I18NLogicUtils;
import es.caib.regweb3.persistence.validator.RegistroSalidaBeanValidator;
import es.caib.regweb3.persistence.validator.RegistroSalidaValidator;
import es.caib.regweb3.utils.Configuracio;
import es.caib.regweb3.utils.RegwebConstantes;
import es.caib.regweb3.utils.StringUtils;
import es.caib.regweb3.ws.converter.RegistroSalidaConverter;
import es.caib.regweb3.ws.model.IdentificadorWs;
import es.caib.regweb3.ws.model.JustificanteWs;
import es.caib.regweb3.ws.model.RegistroSalidaResponseWs;
import es.caib.regweb3.ws.model.RegistroSalidaWs;
import es.caib.regweb3.ws.utils.UsuarioAplicacionCache;
import org.fundaciobit.genapp.common.i18n.I18NException;
import org.fundaciobit.genapp.common.i18n.I18NValidationException;
import org.fundaciobit.genapp.common.ws.WsI18NException;
import org.fundaciobit.genapp.common.ws.WsValidationException;
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
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by Fundació BIT.
 *
 * @author earrivi
 * @author anadal
 */
@Stateless(name = RegWebRegistroSalidaWsImpl.NAME + "Ejb")
@RolesAllowed({RegwebConstantes.RWE_SUPERADMIN})
@SOAPBinding(style = SOAPBinding.Style.RPC)
@org.apache.cxf.interceptor.InInterceptors(interceptors = {"es.caib.regweb3.ws.utils.RegWebInInterceptor"})
@org.apache.cxf.interceptor.InFaultInterceptors(interceptors = {"es.caib.regweb3.ws.utils.RegWebInInterceptor"})
@WebService(name = RegWebRegistroSalidaWsImpl.NAME_WS, portName = RegWebRegistroSalidaWsImpl.NAME_WS,
        serviceName = RegWebRegistroSalidaWsImpl.NAME_WS + "Service",
        endpointInterface = "es.caib.regweb3.ws.v3.impl.RegWebRegistroSalidaWs")
@WebContext(contextRoot = "/regweb3/ws", urlPattern = "/v3/" + RegWebRegistroSalidaWsImpl.NAME, transportGuarantee = TransportGuarantee.NONE)
public class RegWebRegistroSalidaWsImpl extends AbstractRegistroWsImpl implements RegWebRegistroSalidaWs {

    protected final Logger log = LoggerFactory.getLogger(getClass());

    public static final String NAME = "RegWebRegistroSalida";

    public static final String NAME_WS = NAME + "Ws";


    RegistroSalidaValidator<RegistroSalida> validator = new RegistroSalidaValidator<RegistroSalida>();

    @EJB(mappedName = RegistroSalidaLocal.JNDI_NAME)
    private RegistroSalidaLocal registroSalidaEjb;

    @EJB(mappedName = RegistroSalidaConsultaLocal.JNDI_NAME)
    private RegistroSalidaConsultaLocal registroSalidaConsultaEjb;


    @Override
    @RolesAllowed({RWE_USUARI})
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
    @RolesAllowed({RWE_USUARI})
    @WebMethod
    @Deprecated
    public IdentificadorWs nuevoRegistroSalida(@WebParam(name = "entidad") String entidad, @WebParam(name = "registroSalidaWs") RegistroSalidaWs registroSalidaWs) throws Throwable, WsI18NException, WsValidationException {

        IdentificadorWs identificadorWs = null;

        // Integraciones
        Date inicio = new Date();
        StringBuilder peticion = new StringBuilder();
        
        String numRegFormat = "";

        peticion.append("usuario: ").append(UsuarioAplicacionCache.get().getUsuario().getNombreIdentificador()).append(System.getProperty("line.separator"));

        // 1.- Comprobaciones de parámetros obligatórios
        Entidad entidadActiva = validarEntidad(entidad);

        // 2.- Comprobar que el Organismo destino está vigente
        Organismo origen = organismoEjb.findByCodigoEntidadLigero(registroSalidaWs.getOrigen(), entidadActiva.getId());

        if (origen == null) { //Si no existe todo: Hay que agregar origenes externos?
            throw new I18NException("registro.organismo.noExiste", registroSalidaWs.getOrigen());

        } else if (!origen.getEstado().getCodigoEstadoEntidad().equals(ESTADO_ENTIDAD_VIGENTE)) { //Si está extinguido
            throw new I18NException("registro.organismo.extinguido", origen.getNombreCompleto());
        }

        // 3.- Comprobar que la Oficina está vigente
        Oficina oficina = validarOficina(registroSalidaWs.getOficina(), entidadActiva.getId());

        // 4.- Comprobar que el Libro está vigente
        Libro libro = validarLibroUnico(registroSalidaWs.getLibro(), entidadActiva);

        // 5.- Obtener el usuario aplicación que ha realizado la petición
        UsuarioEntidad usuarioAplicacion = usuarioEntidadEjb.findByIdentificadorEntidad(UsuarioAplicacionCache.get().getUsuario().getIdentificador(), entidadActiva.getId());

        if (usuarioAplicacion == null) { //No existe
            throw new I18NException("registro.usuario.noExiste", UsuarioAplicacionCache.get().getUsuario().getIdentificador(), entidadActiva.getNombre());
        }

        // 6.- Comprobar que el Usuario Entidad persona existe en el sistema, si no existe, se intenta crear
        UsuarioEntidad usuario = usuarioEntidadEjb.comprobarUsuarioEntidad(registroSalidaWs.getCodigoUsuario(), entidadActiva.getId());

        if (usuario == null) {//No existe
            throw new I18NException("registro.usuario.noExiste", registroSalidaWs.getCodigoUsuario(), entidadActiva.getNombre());
        }

        // 7.- Comprobar PERMISO_REGISTRO_ENTRADA de usuario aplicación
        if (!permisoOrganismoUsuarioEjb.tienePermiso(usuarioAplicacion.getId(), oficina.getOrganismoResponsable().getId(), PERMISO_REGISTRO_SALIDA, true)) {
            throw new I18NException("registro.usuario.permisos", usuarioAplicacion.getNombreCompleto(), libro.getCodigo());
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

            //registroSalida.getRegistroDetalle().setAnexos(null);

            peticion.append("anexos: ").append(registroSalidaWs.getAnexos().size()).append(System.getProperty("line.separator"));

        }

        // 7.- Creamos el Registro de Salida
        try{
            registroSalida = registroSalidaEjb.registrarSalida(registroSalida, usuario, interesados, anexosFull, true);

            numRegFormat = registroSalida.getNumeroRegistroFormateado();

        }catch (Exception e){
            integracionEjb.addIntegracionError(RegwebConstantes.INTEGRACION_WS, UsuarioAplicacionCache.get().getMethod().getName(), peticion.toString(), e, null,System.currentTimeMillis() - inicio.getTime(), entidadActiva.getId(), numRegFormat);
            throw new I18NException("registro.nuevo.error");
        }


        if (registroSalida.getId() != null) {
            // Componemos la respuesta
            identificadorWs = new IdentificadorWs(registroSalida.getNumeroRegistroFormateado(), registroSalida.getNumeroRegistro(), registroSalida.getFecha());
        }

        // Integracion
        peticion.append("oficina: ").append(registroSalida.getOficina().getDenominacion()).append(System.getProperty("line.separator"));
        peticion.append("registro: ").append(registroSalida.getNumeroRegistroFormateado()).append(System.getProperty("line.separator"));
        peticion.append("extracto: ").append(registroSalida.getRegistroDetalle().getExtracto()).append(System.getProperty("line.separator"));
        integracionEjb.addIntegracionOk(inicio, RegwebConstantes.INTEGRACION_WS, UsuarioAplicacionCache.get().getMethod().getName(),peticion.toString(), System.currentTimeMillis() - inicio.getTime(), entidadActiva.getId(), numRegFormat);

        return identificadorWs;
    }

    @RolesAllowed({RWE_USUARI})
    @WebMethod
    @Override
    @Deprecated
    public JustificanteWs obtenerJustificante(@WebParam(name = "entidad") String entidad, @WebParam(name = "numeroRegistroFormateado") String numeroRegistroFormateado) throws Throwable, WsI18NException, WsValidationException{

        //1.- Validar obligatorios
        Entidad entidadActiva = validarObligatorios(numeroRegistroFormateado,entidad);

        // Integraciones
        Date inicio = new Date();
        StringBuilder peticion = new StringBuilder();
        
        peticion.append("usuario: ").append(UsuarioAplicacionCache.get().getUsuario().getNombreIdentificador()).append(System.getProperty("line.separator"));

        peticion.append("registro: ").append(numeroRegistroFormateado).append(System.getProperty("line.separator"));
        peticion.append("tipoRegistro: ").append("salida").append(System.getProperty("line.separator"));

        UsuarioEntidad usuario = usuarioEntidadEjb.findByIdentificadorEntidad(UsuarioAplicacionCache.get().getUsuario().getIdentificador(), entidadActiva.getId());

        // 4.- Obtenemos el RegistroSalida
        RegistroSalida registroSalida = registroSalidaConsultaEjb.findByNumeroRegistroFormateadoCompleto(entidad, numeroRegistroFormateado);

        if (registroSalida == null) {
            throw new I18NException("registroEntrada.noExiste", numeroRegistroFormateado);
        }

        // 5.- Generamos o descargamos el Justificante
        AnexoFull justificante = null;
        AnexoSimple anexoSimple = null;

        // Si no tiene Justificante, lo generamos
        if(!registroSalida.getRegistroDetalle().getTieneJustificante()){

            // Permisos para Modificar el RegistroSalida?
            if (!permisoOrganismoUsuarioEjb.tienePermiso(usuario.getId(), registroSalida.getOficina().getOrganismoResponsable().getId(), PERMISO_MODIFICACION_REGISTRO_SALIDA, true)) {
                throw new I18NException("registroEntrada.usuario.permisos", usuario.getNombreCompleto());
            }

            // Solo se puede generar si el registro es Válido
            if(registroSalida.getEstado().equals(REGISTRO_VALIDO)) {

                try{
                    justificante = justificanteEjb.crearJustificanteWS(usuario,registroSalida,RegwebConstantes.REGISTRO_SALIDA,Configuracio.getDefaultLanguage());
                }catch (I18NException e){
                    integracionEjb.addIntegracionError(RegwebConstantes.INTEGRACION_WS, UsuarioAplicacionCache.get().getMethod().getName(), peticion.toString(), e, null,System.currentTimeMillis() - inicio.getTime(), entidadActiva.getId(), numeroRegistroFormateado);
                    throw new I18NException("registro.justificante.error", numeroRegistroFormateado);
                }

                anexoSimple = anexoEjb.descargarJustificante(justificante.getAnexo(), entidadActiva.getId());
            }else{
                throw new I18NException("registro.justificante.valido");
            }

        }else{ // Tiene Justificante, lo obtenemos

            // Permisos para Consultar el RegistroSalida?
            if (!permisoOrganismoUsuarioEjb.tienePermiso(usuario.getId(), registroSalida.getOficina().getOrganismoResponsable().getId(), PERMISO_CONSULTA_REGISTRO_SALIDA, false)) {
                throw new I18NException("registroEntrada.usuario.permisos", usuario.getNombreCompleto());
            }

            // Obtenemos el Justificante
            try{
                justificante = anexoEjb.getAnexoFullLigero(anexoEjb.getIdJustificante(registroSalida.getRegistroDetalle().getId()), entidadActiva.getId());
                anexoSimple = anexoEjb.descargarJustificante(justificante.getAnexo(), entidadActiva.getId());
            }catch (Exception e){
                integracionEjb.addIntegracionError(RegwebConstantes.INTEGRACION_WS, UsuarioAplicacionCache.get().getMethod().getName(), peticion.toString(), e, null,System.currentTimeMillis() - inicio.getTime(), entidadActiva.getId(), numeroRegistroFormateado);
                throw new I18NException("registro.justificante.error", numeroRegistroFormateado);
            }

        }

        // Integracion
        integracionEjb.addIntegracionOk(inicio, RegwebConstantes.INTEGRACION_WS, UsuarioAplicacionCache.get().getMethod().getName(),peticion.toString(), System.currentTimeMillis() - inicio.getTime(), entidadActiva.getId(), numeroRegistroFormateado);

        // Alta en la tabla de LOPD
        lopdEjb.altaLopd(registroSalida.getNumeroRegistro(), registroSalida.getFecha(), registroSalida.getLibro().getId(), usuario.getId(), RegwebConstantes.REGISTRO_SALIDA, RegwebConstantes.LOPD_JUSTIFICANTE);

        return new JustificanteWs(anexoSimple.getData());
    }

    @RolesAllowed({RWE_USUARI})
    @Override
    @WebMethod
    @Deprecated
    public void anularRegistroSalida(@WebParam(name = "numeroRegistro") String numeroRegistro,
                                     @WebParam(name = "entidad") String entidad,
                                     @WebParam(name = "anular") boolean anular)
            throws Throwable, WsI18NException, WsValidationException {

        //1.- Validar obligatorios
        validarObligatorios(numeroRegistro,entidad);

        // 4.- Comprobar que el usuario existe en la Entidad proporcionada
        UsuarioEntidad usuarioEntidad = usuarioEntidadEjb.findByIdentificadorCodigoEntidad( UsuarioAplicacionCache.get().getUsuario().getIdentificador(), entidad);


        // 5.- Obtenemos el RegistroSalida
        RegistroSalida registroSalida = registroSalidaConsultaEjb.findByNumeroRegistroFormateado(entidad, numeroRegistro);

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
        if (!permisoOrganismoUsuarioEjb.tienePermiso(usuarioEntidad.getId(), registroSalida.getOficina().getOrganismoResponsable().getId(), PERMISO_MODIFICACION_REGISTRO_SALIDA, true)) {
            throw new I18NException("registroEntrada.usuario.permisos", usuarioEntidad.getNombreCompleto());
        }

        // 8.- Anulamos el RegistroSalida
        // TODO Falta Afegir paràmetre
        Locale locale = new Locale(UsuarioAplicacionCache.get().getIdioma());
        registroSalidaEjb.anularRegistroSalida(registroSalida, usuarioEntidad, I18NLogicUtils.tradueix(locale, "registro.anulado.ws"));

    }

    @RolesAllowed({RWE_USUARI})
    @Override
    @WebMethod
    @Deprecated
    public RegistroSalidaResponseWs obtenerRegistroSalida(
            @WebParam(name = "numeroRegistro") String numeroRegistro,
            @WebParam(name = "usuario") String usuario,
            @WebParam(name = "entidad") String entidad) throws Throwable, WsI18NException, WsValidationException {

        //1.- Validar obligatorios
        Entidad entidadActiva = validarObligatorios(numeroRegistro,entidad);

        // Integraciones
        Date inicio = new Date();
        StringBuilder peticion = new StringBuilder();
        
        peticion.append("usuario: ").append(UsuarioAplicacionCache.get().getUsuario().getNombreIdentificador()).append(System.getProperty("line.separator"));
        peticion.append("registro: ").append(numeroRegistro).append(System.getProperty("line.separator"));

        UsuarioEntidad usuarioEntidad = usuarioEntidadEjb.findByIdentificadorCodigoEntidad(UsuarioAplicacionCache.get().getUsuario().getIdentificador(), entidad);

        if(usuarioEntidad == null){//No existe
            throw new I18NException("registroEntrada.usuario.noExiste", UsuarioAplicacionCache.get().getUsuario().getIdentificador(), entidad);
        }

        // 4.- Obtenemos el RegistroSalida
        RegistroSalida registro = registroSalidaConsultaEjb.findByNumeroRegistroFormateadoCompleto(entidad, numeroRegistro);

        if (registro == null) {
            throw new I18NException("registroEntrada.noExiste", numeroRegistro);
        }

        // 5.- Comprobamos que el usuario tiene permisos de lectura para el RegistroSalida
        if (!permisoOrganismoUsuarioEjb.tienePermiso(usuarioEntidad.getId(), registro.getOficina().getOrganismoResponsable().getId(), PERMISO_CONSULTA_REGISTRO_SALIDA, false)) {
            throw new I18NException("registroEntrada.usuario.permisos", usuarioEntidad.getUsuario().getNombreCompleto());
        }

        // Retornamos el RegistroSalidaResponseWs
        RegistroSalidaResponseWs responseWs = null;
        try{
            responseWs = RegistroSalidaConverter.getRegistroSalidaResponseWs(registro, UsuarioAplicacionCache.get().getIdioma());
        }catch (Exception e){

            integracionEjb.addIntegracionError(RegwebConstantes.INTEGRACION_WS, UsuarioAplicacionCache.get().getMethod().getName(), peticion.toString(), e, null,System.currentTimeMillis() - inicio.getTime(), entidadActiva.getId(), numeroRegistro);
            throw new I18NException("registro.obtener.error");
        }

        // LOPD
        lopdEjb.altaLopd(registro.getNumeroRegistro(), registro.getFecha(), registro.getLibro().getId(), usuarioEntidad.getId(), RegwebConstantes.REGISTRO_SALIDA, RegwebConstantes.LOPD_CONSULTA);

        // Integracion
        integracionEjb.addIntegracionOk(inicio, RegwebConstantes.INTEGRACION_WS, UsuarioAplicacionCache.get().getMethod().getName(),peticion.toString(), System.currentTimeMillis() - inicio.getTime(), entidadActiva.getId(), numeroRegistro);

        return responseWs;

    }


    /**
     * @param anyo
     * @param numeroRegistro
     * @param codigoLibro
     * @param usuario
     * @param entidad
     * @return
     * @throws Throwable
     * @throws WsI18NException
     */
    @RolesAllowed({RWE_USUARI})
    @Override
    @WebMethod
    @Deprecated
    public IdentificadorWs obtenerRegistroSalidaID(
            @WebParam(name = "anyo") int anyo,
            @WebParam(name = "numeroRegistro") int numeroRegistro,
            @WebParam(name = "libro") String codigoLibro,
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

        if (StringUtils.isEmpty(codigoLibro)) {
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
        //Libro libro = validarLibro(codigoLibro, usuarioEntidad.getEntidad()); DEJAMOS DE VALIDARLO PORQUE CON LIBRO ÚNICO SE HAN ANULADO TODOS LOS LIBROS

        // 4.- Obtenemos el registro
        RegistroSalida registro;
        registro = registroSalidaConsultaEjb.findByNumeroAnyoLibro(numeroRegistro, anyo, codigoLibro);
        if (registro == null) {
            throw new I18NException("registroEntrada.noExiste", numeroRegistro
                    + "/" + anyo + " (" + codigoLibro + ")");
        }

        // 5.- Comprobamos que el usuario tiene permisos de lectura para el RegistroEntrada
        if (!permisoOrganismoUsuarioEjb.tienePermiso(usuarioEntidad.getId(), registro.getOficina().getOrganismoResponsable().getId(), PERMISO_CONSULTA_REGISTRO_SALIDA, false)) {
            throw new I18NException("registroEntrada.usuario.permisos", usuario);
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
