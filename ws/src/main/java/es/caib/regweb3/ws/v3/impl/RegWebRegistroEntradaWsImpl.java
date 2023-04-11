package es.caib.regweb3.ws.v3.impl;

import es.caib.dir3caib.ws.api.unidad.Dir3CaibObtenerUnidadesWs;
import es.caib.dir3caib.ws.api.unidad.UnidadTF;
import es.caib.regweb3.model.*;
import es.caib.regweb3.model.utils.AnexoFull;
import es.caib.regweb3.model.utils.AnexoSimple;
import es.caib.regweb3.persistence.ejb.DistribucionLocal;
import es.caib.regweb3.persistence.ejb.RegistroEntradaConsultaLocal;
import es.caib.regweb3.persistence.ejb.RegistroEntradaLocal;
import es.caib.regweb3.persistence.utils.I18NLogicUtils;
import es.caib.regweb3.persistence.utils.PropiedadGlobalUtil;
import es.caib.regweb3.persistence.utils.RespuestaDistribucion;
import es.caib.regweb3.persistence.validator.RegistroEntradaBeanValidator;
import es.caib.regweb3.persistence.validator.RegistroEntradaValidator;
import es.caib.regweb3.utils.Configuracio;
import es.caib.regweb3.utils.Dir3CaibUtils;
import es.caib.regweb3.utils.RegwebConstantes;
import es.caib.regweb3.utils.StringUtils;
import es.caib.regweb3.ws.converter.RegistroEntradaConverter;
import es.caib.regweb3.ws.model.IdentificadorWs;
import es.caib.regweb3.ws.model.JustificanteWs;
import es.caib.regweb3.ws.model.RegistroEntradaResponseWs;
import es.caib.regweb3.ws.model.RegistroEntradaWs;
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
@Stateless(name = RegWebRegistroEntradaWsImpl.NAME + "Ejb")
@SOAPBinding(style = SOAPBinding.Style.RPC)
@org.apache.cxf.interceptor.InInterceptors(interceptors = {"es.caib.regweb3.ws.utils.RegWebInInterceptor"})
@org.apache.cxf.interceptor.InFaultInterceptors(interceptors = {"es.caib.regweb3.ws.utils.RegWebInInterceptor"})
@WebService(name = RegWebRegistroEntradaWsImpl.NAME_WS, portName = RegWebRegistroEntradaWsImpl.NAME_WS,
   serviceName = RegWebRegistroEntradaWsImpl.NAME_WS + "Service",
   endpointInterface = "es.caib.regweb3.ws.v3.impl.RegWebRegistroEntradaWs")
@WebContext(contextRoot = "/regweb3/ws", urlPattern = "/v3/" + RegWebRegistroEntradaWsImpl.NAME, transportGuarantee = TransportGuarantee.NONE)
public class RegWebRegistroEntradaWsImpl extends AbstractRegistroWsImpl implements RegWebRegistroEntradaWs {

    protected final Logger log = LoggerFactory.getLogger(getClass());

    public static final String NAME = "RegWebRegistroEntrada";

    public static final String NAME_WS = NAME + "Ws";


    RegistroEntradaValidator<RegistroEntrada> registroEntradaValidator = new RegistroEntradaValidator<RegistroEntrada>();


    @EJB(mappedName = RegistroEntradaConsultaLocal.JNDI_NAME)
    private RegistroEntradaConsultaLocal registroEntradaConsultaEjb;

    @EJB(mappedName = RegistroEntradaLocal.JNDI_NAME)
    private RegistroEntradaLocal registroEntradaEjb;

    @EJB(mappedName = DistribucionLocal.JNDI_NAME)
    private DistribucionLocal distribucionEjb;


    @Override
    @RolesAllowed({RWE_USUARI, RWE_WS_ENTRADA})
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
    @RolesAllowed({RWE_USUARI, RWE_WS_ENTRADA})
    @WebMethod
    @Deprecated
    public IdentificadorWs nuevoRegistroEntrada(@WebParam(name = "entidad") String entidad, @WebParam(name = "registroEntradaWs")
       RegistroEntradaWs registroEntradaWs)
       throws Throwable, WsI18NException, WsValidationException {

        IdentificadorWs identificadorWs = null;

        // Integraciones
        Date inicio = new Date();
        StringBuilder peticion = new StringBuilder();
        
        String numRegFormat = "";

        peticion.append("usuario: ").append(UsuarioAplicacionCache.get().getUsuario().getNombreIdentificador()).append(System.getProperty("line.separator"));

        // 1.- Validar campo obligatorio entidad
        Entidad entidadActiva = validarEntidad(entidad);

        // 2.- Comprobar que el Organismo destino está vigente
        UnidadTF destinoExterno = null;
        Organismo destinoInterno = organismoEjb.findByCodigoByEntidadMultiEntidad(registroEntradaWs.getDestino(),entidadActiva.getId());
        if(destinoInterno == null){ //Externo, lo vamos a buscar a dir3caib
            // Lo buscamos en DIR3CAIB
            Dir3CaibObtenerUnidadesWs unidadesService = Dir3CaibUtils.getObtenerUnidadesService(PropiedadGlobalUtil.getDir3CaibServer(entidadActiva.getId()), PropiedadGlobalUtil.getDir3CaibUsername(entidadActiva.getId()), PropiedadGlobalUtil.getDir3CaibPassword(entidadActiva.getId()));
            destinoExterno = unidadesService.obtenerUnidad(registroEntradaWs.getDestino(), null, null);

            if (destinoExterno == null) { //o no existe o está extinguido
                throw new I18NException("registro.organismo.noExiste", registroEntradaWs.getDestino());
            }
        }else if( !destinoInterno.getEntidad().getId().equals(entidadActiva.getId())){ //No hace falta ir a buscarlo a dir3caib, ya tenemos los datos mínimos.
            destinoExterno = new UnidadTF();
            destinoExterno.setCodigo(destinoInterno.getCodigo());
            destinoExterno.setDenominacion(destinoInterno.getDenominacion());
            destinoInterno = null;
        } else if (!destinoInterno.getEstado().getCodigoEstadoEntidad().equals(ESTADO_ENTIDAD_VIGENTE)) { //Si está extinguido
            throw new I18NException("registro.organismo.extinguido", destinoInterno.getNombreCompleto());
        }

        // 3.- Comprobar que la Oficina está vigente
        Oficina oficina = validarOficina(registroEntradaWs.getOficina(), entidadActiva.getId());

        // 4.- Comprobar que el Libro está vigente
        Libro libro = validarLibroUnico(registroEntradaWs.getLibro(), entidadActiva);

        // 5.- Obtener el usuario aplicación que ha realizado la petición
        UsuarioEntidad usuarioAplicacion = usuarioEntidadEjb.findByIdentificadorEntidad(UsuarioAplicacionCache.get().getUsuario().getIdentificador(), entidadActiva.getId());

        if (usuarioAplicacion == null) { //No existe
            throw new I18NException("registro.usuario.noExiste", UsuarioAplicacionCache.get().getUsuario().getIdentificador(), entidadActiva.getNombre());
        }

        // 6.- Comprobar que el Usuario Entidad persona existe en el sistema, si no existe, se intenta crear
        UsuarioEntidad usuario = usuarioEntidadEjb.comprobarUsuarioEntidad(registroEntradaWs.getCodigoUsuario(), entidadActiva.getId());

        if (usuario == null) {//No existe
            throw new I18NException("registro.usuario.noExiste", registroEntradaWs.getCodigoUsuario(), entidadActiva.getNombre());
        }

        // 7.- Comprobar PERMISO_REGISTRO_ENTRADA de usuario aplicación
        if (!permisoOrganismoUsuarioEjb.tienePermiso(usuarioAplicacion.getId(), oficina.getOrganismoResponsable().getId(), PERMISO_REGISTRO_ENTRADA, true)) {
            throw new I18NException("registro.usuario.permisos", usuarioAplicacion.getNombreCompleto(), libro.getCodigo());
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
            //registroEntrada.getRegistroDetalle().setAnexos(null);

            peticion.append("anexos: ").append(registroEntradaWs.getAnexos().size()).append(System.getProperty("line.separator"));
        }

        // 12.- Creamos el Registro de Entrada
        try{
            registroEntrada = registroEntradaEjb.registrarEntrada(registroEntrada, entidadActiva, usuario, interesados, anexosFull, true);
            numRegFormat = registroEntrada.getNumeroRegistroFormateado();

        }catch (Exception e){
            integracionEjb.addIntegracionError(RegwebConstantes.INTEGRACION_WS, UsuarioAplicacionCache.get().getMethod().getName(), peticion.toString(), e, null,System.currentTimeMillis() - inicio.getTime(), entidadActiva.getId(), numRegFormat);
            throw new I18NException("registroEntrada.nuevo.error");
        }

        if (registroEntrada.getId() != null) {

            // Componemos la respuesta
            identificadorWs = new IdentificadorWs(registroEntrada.getNumeroRegistroFormateado(), registroEntrada.getNumeroRegistro(), registroEntrada.getFecha());
        }

        // Integracion
        peticion.append("oficina: ").append(registroEntrada.getOficina().getDenominacion()).append(System.getProperty("line.separator"));
        peticion.append("registro: ").append(registroEntrada.getNumeroRegistroFormateado()).append(System.getProperty("line.separator"));
        peticion.append("extracto: ").append(registroEntrada.getRegistroDetalle().getExtracto()).append(System.getProperty("line.separator"));
        integracionEjb.addIntegracionOk(inicio, RegwebConstantes.INTEGRACION_WS, UsuarioAplicacionCache.get().getMethod().getName(),peticion.toString(), System.currentTimeMillis() - inicio.getTime(), entidadActiva.getId(), numRegFormat);

        return identificadorWs;
    }


    @RolesAllowed({RWE_USUARI, RWE_WS_ENTRADA})
    @WebMethod
    @Override
    @Deprecated
    public JustificanteWs obtenerJustificante(@WebParam(name = "entidad") String entidad, @WebParam(name = "numeroRegistroFormateado")String numeroRegistroFormateado) throws Throwable, WsI18NException, WsValidationException{

        //1.- Validar obligatorios
        Entidad entidadActiva = validarObligatorios(numeroRegistroFormateado,entidad);

        // Integraciones
        Date inicio = new Date();
        StringBuilder peticion = new StringBuilder();
        
        peticion.append("usuario: ").append(UsuarioAplicacionCache.get().getUsuario().getNombreIdentificador()).append(System.getProperty("line.separator"));
        peticion.append("registro: ").append(numeroRegistroFormateado).append(System.getProperty("line.separator"));
        peticion.append("tipoRegistro: ").append("entrada").append(System.getProperty("line.separator"));


        UsuarioEntidad usuario = usuarioEntidadEjb.findByIdentificadorEntidad(UsuarioAplicacionCache.get().getUsuario().getIdentificador(), entidadActiva.getId());

        // 4.- Obtenemos el RegistroEntrada
        RegistroEntrada registroEntrada = registroEntradaConsultaEjb.findByNumeroRegistroFormateadoCompleto(entidadActiva.getId(), numeroRegistroFormateado);

        if (registroEntrada == null) {
            throw new I18NException("registroEntrada.noExiste", numeroRegistroFormateado);
        }

        // 5.- Generamos o descargamos el Justificante
        AnexoFull justificante = null;
        AnexoSimple anexoSimple = null;

        // Si no tiene Justificante, lo generamos
        if(!registroEntrada.getRegistroDetalle().getTieneJustificante()){

            // Permisos para Modificar el RegistroEntrada?
            if (!permisoOrganismoUsuarioEjb.tienePermiso(usuario.getId(), registroEntrada.getOficina().getOrganismoResponsable().getId(), PERMISO_MODIFICACION_REGISTRO_ENTRADA, true)) {
                throw new I18NException("registroEntrada.usuario.permisos", usuario.getNombreCompleto());
            }

            // Solo se puede generar si el registro es Válido
            if(registroEntrada.getEstado().equals(REGISTRO_VALIDO)){

                try{
                    justificante = justificanteEjb.crearJustificanteWS(entidadActiva, usuario,registroEntrada,RegwebConstantes.REGISTRO_ENTRADA,Configuracio.getDefaultLanguage());
                }catch (I18NException e){
                    log.info("----------------Error generado justificante via WS------------------");
                    integracionEjb.addIntegracionError(RegwebConstantes.INTEGRACION_WS, UsuarioAplicacionCache.get().getMethod().getName(), peticion.toString(), e, null,System.currentTimeMillis() - inicio.getTime(), entidadActiva.getId(), numeroRegistroFormateado);
                    throw new I18NException("registro.justificante.error", numeroRegistroFormateado);
                }

                anexoSimple = anexoEjb.descargarJustificante(justificante.getAnexo(), entidadActiva.getId());
            }else{
                throw new I18NException("registro.justificante.valido");
            }


        }else{ // Tiene Justificante, lo obtenemos

            // Permisos para Consultar el RegistroEntrada?
            if (!permisoOrganismoUsuarioEjb.tienePermiso(usuario.getId(), registroEntrada.getOficina().getOrganismoResponsable().getId(), PERMISO_CONSULTA_REGISTRO_ENTRADA, false)) {
                throw new I18NException("registroEntrada.usuario.permisos", usuario.getNombreCompleto());
            }

            // Obtenemos el Justificante
            try{
                justificante = anexoEjb.getAnexoFullLigero(anexoEjb.getIdJustificante(registroEntrada.getRegistroDetalle().getId()), entidadActiva.getId());
                anexoSimple = anexoEjb.descargarJustificante(justificante.getAnexo(), entidadActiva.getId());
            }catch (Exception e){
                integracionEjb.addIntegracionError(RegwebConstantes.INTEGRACION_WS, UsuarioAplicacionCache.get().getMethod().getName(), peticion.toString(), e, null,System.currentTimeMillis() - inicio.getTime(), entidadActiva.getId(), numeroRegistroFormateado);
                throw new I18NException("registro.justificante.error", numeroRegistroFormateado);
            }

        }

        // Integracion
        integracionEjb.addIntegracionOk(inicio, RegwebConstantes.INTEGRACION_WS, UsuarioAplicacionCache.get().getMethod().getName(),peticion.toString(), System.currentTimeMillis() - inicio.getTime(), entidadActiva.getId(), numeroRegistroFormateado);

        // Alta en la tabla de LOPD
        lopdEjb.altaLopd(registroEntrada.getNumeroRegistro(), registroEntrada.getFecha(), registroEntrada.getLibro().getId(), usuario.getId(), RegwebConstantes.REGISTRO_ENTRADA, RegwebConstantes.LOPD_JUSTIFICANTE);

        return new JustificanteWs(anexoSimple.getData());
    }

    @RolesAllowed({RWE_USUARI})
    @Override
    @WebMethod
    @Deprecated
    public void anularRegistroEntrada(
       @WebParam(name = "numeroRegistroFormateado") String numeroRegistroFormateado,
       @WebParam(name = "entidad") String entidad,
       @WebParam(name = "anular") boolean anular)
       throws Throwable, WsI18NException, WsValidationException {

        // Integraciones
        Date inicio = new Date();
        StringBuilder peticion = new StringBuilder();

        peticion.append("usuario: ").append(UsuarioAplicacionCache.get().getUsuario().getNombreIdentificador()).append(System.getProperty("line.separator"));

        //1.- Validar obligatorios
        Entidad entidadActiva = validarObligatorios(numeroRegistroFormateado,entidad);

        //2.- Comprobar que el usuario existe en la Entidad proporcionada
        UsuarioEntidad usuarioEntidad = usuarioEntidadEjb.findByIdentificadorCodigoEntidad( UsuarioAplicacionCache.get().getUsuario().getIdentificador(), entidad);

        if (usuarioEntidad == null) {//No existe
            throw new I18NException("registroEntrada.usuario.noExiste", UsuarioAplicacionCache.get().getUsuario().getIdentificador(), entidad);
        }

        // 3.- Obtenemos el RegistroEntrada
        RegistroEntrada registroEntrada = registroEntradaConsultaEjb.findByNumeroRegistroFormateado(usuarioEntidad.getEntidad().getId(), numeroRegistroFormateado);

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
        if (!permisoOrganismoUsuarioEjb.tienePermiso(usuarioEntidad.getId(), registroEntrada.getOficina().getOrganismoResponsable().getId(), PERMISO_MODIFICACION_REGISTRO_ENTRADA, true)) {
            throw new I18NException("registroEntrada.usuario.permisos", usuarioEntidad.getUsuario().getNombreCompleto());
        }

        // 6.- Anulamos el RegistroEntrada
        Locale locale = new Locale(UsuarioAplicacionCache.get().getIdioma());
        registroEntradaEjb.anularRegistroEntrada(registroEntrada, usuarioEntidad, I18NLogicUtils.tradueix(locale, "registro.anulado.ws"));

        // Integracion
        peticion.append("registro: ").append(registroEntrada.getNumeroRegistroFormateado()).append(System.getProperty("line.separator"));
        integracionEjb.addIntegracionOk(inicio, RegwebConstantes.INTEGRACION_WS, UsuarioAplicacionCache.get().getMethod().getName(),peticion.toString(), System.currentTimeMillis() - inicio.getTime(), entidadActiva.getId(), numeroRegistroFormateado);
    }



    @RolesAllowed({RWE_USUARI, RWE_WS_ENTRADA})
    @Override
    @WebMethod
    @Deprecated
    public void tramitarRegistroEntrada(@WebParam(name = "numeroRegistroFormateado") String numeroRegistroFormateado, @WebParam(name = "usuario") String usuario, @WebParam(name = "entidad") String entidad) throws Throwable, WsI18NException, WsValidationException {

        //1.- Validar obligatorios
        validarObligatorios(numeroRegistroFormateado,entidad);


        // 2.- Comprobar que el usuario existe en la Entidad proporcionada
        UsuarioEntidad usuarioEntidad = usuarioEntidadEjb.findByIdentificadorCodigoEntidad( UsuarioAplicacionCache.get().getUsuario().getIdentificador(), entidad);

        if (usuarioEntidad == null) {//No existe
            throw new I18NException("registroEntrada.usuario.noExiste", UsuarioAplicacionCache.get().getUsuario().getIdentificador(), entidad);
        }

        // 3.- Obtenemos el RegistroEntrada
        RegistroEntrada registroEntrada = registroEntradaConsultaEjb.findByNumeroRegistroFormateado(usuarioEntidad.getEntidad().getId(), numeroRegistroFormateado);

        if (registroEntrada == null) {
            throw new I18NException("registroEntrada.noExiste", numeroRegistroFormateado);
        }

        // 5.- Comprobamos que el Organismo destino pertenece a la misma administración
        if (!registroEntrada.getOficina().getOrganismoResponsable().equals(registroEntrada.getDestino())) {
            throw new I18NException("registroEntrada.tramitar.error");
        }

        //  Comprobamos que el usuario tiene permisos para Distribuir el registro
        if(!permisoOrganismoUsuarioEjb.tienePermiso(usuarioEntidad.getId(), registroEntrada.getOficina().getOrganismoResponsable().getId(), RegwebConstantes.PERMISO_DISTRIBUCION_REGISTRO, true)){
            throw new I18NException("registroEntrada.distribuir.error.permiso");
        }

        // Comprobamos que el RegistroEntrada se puede Distribuir
        if (!registroEntradaConsultaEjb.isDistribuir(registroEntrada.getId())) {
            throw new I18NException("registroEntrada.distribuir.noPermitido");
        }

        try{
            // Distribuimos el registro de entrada
            distribucionEjb.distribuir(registroEntrada, usuarioEntidad, "Distribución desde WS",null,null);

        }catch (I18NValidationException e){
            e.printStackTrace();
            throw new I18NException("registroEntrada.distribuir.error");
        }

    }


    @RolesAllowed({RWE_USUARI, RWE_WS_ENTRADA})
    @Override
    @WebMethod
    @Deprecated
    public void distribuirRegistroEntrada(@WebParam(name = "numeroRegistroFormateado") String numeroRegistroFormateado, @WebParam(name = "entidad") String entidad) throws Throwable, WsI18NException, WsValidationException {

        //1.- Validar obligatorios
        Entidad entidadActiva = validarObligatorios(numeroRegistroFormateado,entidad);

        UsuarioEntidad usuario = usuarioEntidadEjb.findByIdentificadorEntidad(UsuarioAplicacionCache.get().getUsuario().getIdentificador(), entidadActiva.getId());

        // 4.- Obtenemos el RegistroEntrada
        RegistroEntrada registroEntrada = registroEntradaConsultaEjb.findByNumeroRegistroFormateadoCompleto(entidadActiva.getId(), numeroRegistroFormateado);


        if (registroEntrada == null) {
            throw new I18NException("registroEntrada.noExiste", numeroRegistroFormateado);
        }

        // 5.- Comprobamos que el usuario tiene permisos para Distribuir el registro
        if(!permisoOrganismoUsuarioEjb.tienePermiso(usuario.getId(), registroEntrada.getOficina().getOrganismoResponsable().getId(), RegwebConstantes.PERMISO_DISTRIBUCION_REGISTRO, true)){
            throw new I18NException("registroEntrada.distribuir.error.permiso");
        }

        // Comprobamos que el RegistroEntrada se puede Distribuir
        if (!registroEntradaConsultaEjb.isDistribuir(registroEntrada.getId())) {
            throw new I18NException("registroEntrada.distribuir.noPermitido");
        }

        try{
            // 7.- Distribuimos el registro de entrada
            RespuestaDistribucion respuestaDistribucion = distribucionEjb.distribuir(registroEntrada, usuario, "Distribución desde WS",null,null);

            // Si el Plugin permite seleccionar Destinatarios, no se puede distribuir automaticamente
            if(respuestaDistribucion.getDestinatarios() != null){
                throw new I18NException("registroEntrada.distribuir.destinatarios");
            }

            if(!respuestaDistribucion.getEncolado()  && !respuestaDistribucion.getDistribuido()){ //Cuando hay plugin y no ha llegado a destino

                throw new I18NException(("registroEntrada.distribuir.error.noEnviado"));
            }

        }catch (Exception e){
            throw new I18NException("registroEntrada.distribuir.error");
        }

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
    @RolesAllowed({RWE_USUARI, RWE_WS_ENTRADA})
    @Override
    @WebMethod
    @Deprecated
    public IdentificadorWs obtenerRegistroEntradaID(
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
        RegistroEntrada registro;
        registro = registroEntradaConsultaEjb.findByNumeroAnyoLibro(numeroRegistro, anyo, codigoLibro);
        if (registro == null) {
            throw new I18NException("registroEntrada.noExiste", numeroRegistro
               + "/" + anyo + " (" + codigoLibro + ")");
        }

        // 5.- Comprobamos que el usuario tiene permisos de lectura para el RegistroEntrada
        if (!permisoOrganismoUsuarioEjb.tienePermiso(usuarioEntidad.getId(), registro.getOficina().getOrganismoResponsable().getId(), PERMISO_CONSULTA_REGISTRO_ENTRADA, false)) {
            throw new I18NException("registroEntrada.usuario.permisos", usuario);
        }

        // LOPD
        lopdEjb.altaLopd(registro.getNumeroRegistro(), registro.getFecha(), registro.getLibro().getId(), usuarioEntidad.getId(), RegwebConstantes.REGISTRO_ENTRADA, RegwebConstantes.LOPD_CONSULTA);

        return new IdentificadorWs(registro.getNumeroRegistroFormateado(), numeroRegistro, registro.getFecha());

    }


    @RolesAllowed({RWE_USUARI, RWE_WS_ENTRADA})
    @Override
    @WebMethod
    @Deprecated
    public RegistroEntradaResponseWs obtenerRegistroEntrada(
       @WebParam(name = "numeroRegistroFormateado") String numeroRegistroFormateado,
       @WebParam(name = "usuario") String usuario,
       @WebParam(name = "entidad") String entidad)
       throws Throwable, WsI18NException, WsValidationException {

        //1.- Validar obligatorios
        Entidad entidadActiva = validarObligatorios(numeroRegistroFormateado,entidad);

        // Integraciones
        Date inicio = new Date();
        StringBuilder peticion = new StringBuilder();
        
        peticion.append("usuario: ").append(UsuarioAplicacionCache.get().getUsuario().getNombreIdentificador()).append(System.getProperty("line.separator"));
        peticion.append("registro: ").append(numeroRegistroFormateado).append(System.getProperty("line.separator"));

        UsuarioEntidad usuarioEntidad = usuarioEntidadEjb.findByIdentificadorCodigoEntidad( UsuarioAplicacionCache.get().getUsuario().getIdentificador(), entidad);

        if(usuarioEntidad == null){//No existe
            throw new I18NException("registroEntrada.usuario.noExiste", UsuarioAplicacionCache.get().getUsuario().getIdentificador(), entidad);
        }

        // 4.- Obtenemos el RegistroEntrada
        RegistroEntrada registro = registroEntradaConsultaEjb.findByNumeroRegistroFormateadoCompleto(entidadActiva.getId(), numeroRegistroFormateado);

        if (registro == null) {
            throw new I18NException("registroEntrada.noExiste", numeroRegistroFormateado);
        }

        // 5.- Comprobamos que el usuario tiene permisos de lectura para el RegistroEntrada
        if (!permisoOrganismoUsuarioEjb.tienePermiso(usuarioEntidad.getId(), registro.getOficina().getOrganismoResponsable().getId(), PERMISO_CONSULTA_REGISTRO_ENTRADA, false)) {
            throw new I18NException("registroEntrada.usuario.permisos", usuarioEntidad.getUsuario().getNombreCompleto());
        }

        // Retornamos el RegistroEntradaResponseWs
        RegistroEntradaResponseWs responseWs = null;
        try{
            responseWs = RegistroEntradaConverter.getRegistroEntradaResponseWs(registro, UsuarioAplicacionCache.get().getIdioma());
        }catch (Exception e){

            integracionEjb.addIntegracionError(RegwebConstantes.INTEGRACION_WS, UsuarioAplicacionCache.get().getMethod().getName(), peticion.toString(), e, null,System.currentTimeMillis() - inicio.getTime(), entidadActiva.getId(), numeroRegistroFormateado);
            throw new I18NException("registro.obtener.error");
        }

        // LOPD
        lopdEjb.altaLopd(registro.getNumeroRegistro(), registro.getFecha(), registro.getLibro().getId(), usuarioEntidad.getId(), RegwebConstantes.REGISTRO_ENTRADA, RegwebConstantes.LOPD_CONSULTA);

        // Integracion
        integracionEjb.addIntegracionOk(inicio, RegwebConstantes.INTEGRACION_WS, UsuarioAplicacionCache.get().getMethod().getName(),peticion.toString(), System.currentTimeMillis() - inicio.getTime(), entidadActiva.getId(), numeroRegistroFormateado);

        return responseWs;

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