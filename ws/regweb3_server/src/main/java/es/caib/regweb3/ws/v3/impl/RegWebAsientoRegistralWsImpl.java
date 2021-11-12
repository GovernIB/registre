package es.caib.regweb3.ws.v3.impl;

import es.caib.dir3caib.ws.api.unidad.Dir3CaibObtenerUnidadesWs;
import es.caib.dir3caib.ws.api.unidad.UnidadTF;
import es.caib.regweb3.model.*;
import es.caib.regweb3.model.utils.AnexoFull;
import es.caib.regweb3.model.utils.AnexoSimple;
import es.caib.regweb3.persistence.ejb.*;
import es.caib.regweb3.persistence.utils.*;
import es.caib.regweb3.persistence.validator.RegistroEntradaBeanValidator;
import es.caib.regweb3.persistence.validator.RegistroEntradaValidator;
import es.caib.regweb3.persistence.validator.RegistroSalidaBeanValidator;
import es.caib.regweb3.persistence.validator.RegistroSalidaValidator;
import es.caib.regweb3.utils.Configuracio;
import es.caib.regweb3.utils.Dir3CaibUtils;
import es.caib.regweb3.utils.RegwebConstantes;
import es.caib.regweb3.utils.StringUtils;
import es.caib.regweb3.ws.converter.AsientoConverter;
import es.caib.regweb3.ws.converter.AsientoRegistralConverter;
import es.caib.regweb3.ws.model.*;
import es.caib.regweb3.ws.utils.UsuarioAplicacionCache;
import org.apache.commons.io.IOUtils;
import org.fundaciobit.genapp.common.i18n.I18NException;
import org.fundaciobit.genapp.common.i18n.I18NValidationException;
import org.fundaciobit.genapp.common.ws.WsI18NException;
import org.fundaciobit.genapp.common.ws.WsValidationException;
import org.jboss.ejb3.annotation.SecurityDomain;
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

import static es.caib.regweb3.utils.RegwebConstantes.*;

/**
 *
 */
@SecurityDomain(RegwebConstantes.SECURITY_DOMAIN)
@Stateless(name = RegWebAsientoRegistralWsImpl.NAME + "Ejb")
@RolesAllowed({RWE_WS_ENTRADA, RWE_WS_SALIDA, RWE_WS_CIUDADANO})
@SOAPBinding(style = SOAPBinding.Style.RPC)
@org.apache.cxf.interceptor.InInterceptors(interceptors = {"es.caib.regweb3.ws.utils.RegWebInInterceptor"})
@org.apache.cxf.interceptor.InFaultInterceptors(interceptors = {"es.caib.regweb3.ws.utils.RegWebInInterceptor"})
@WebService(name = RegWebAsientoRegistralWsImpl.NAME_WS, portName = RegWebAsientoRegistralWsImpl.NAME_WS,
        serviceName = RegWebAsientoRegistralWsImpl.NAME_WS + "Service",
        endpointInterface = "es.caib.regweb3.ws.v3.impl.RegWebAsientoRegistralWs")
@WebContext(contextRoot = "/regweb3/ws", urlPattern = "/v3/" + RegWebAsientoRegistralWsImpl.NAME, transportGuarantee = TransportGuarantee.NONE, secureWSDLAccess = false, authMethod = "WSBASIC")
public class RegWebAsientoRegistralWsImpl extends AbstractRegistroWsImpl implements RegWebAsientoRegistralWs {

    protected final Logger log = LoggerFactory.getLogger(getClass());

    public static final String NAME = "RegWebAsientoRegistral";

    public static final String NAME_WS = NAME + "Ws";

    RegistroSalidaValidator<RegistroSalida> registroSalidaValidator = new RegistroSalidaValidator<RegistroSalida>();
    RegistroEntradaValidator<RegistroEntrada> registroEntradaValidator = new RegistroEntradaValidator<RegistroEntrada>();

    @EJB(mappedName = "regweb3/RegistroEntradaConsultaEJB/local")
    private RegistroEntradaConsultaLocal registroEntradaConsultaEjb;

    @EJB(mappedName = "regweb3/RegistroSalidaConsultaEJB/local")
    private RegistroSalidaConsultaLocal registroSalidaConsultaEjb;

    @EJB(mappedName = "regweb3/DistribucionEJB/local")
    private DistribucionLocal distribucionEjb;

    @EJB(mappedName = "regweb3/OficioRemisionEJB/local")
    private OficioRemisionLocal oficioRemisionEjb;

    @EJB(mappedName = "regweb3/AsientoRegistralEJB/local")
    private AsientoRegistralLocal asientoRegistralEjb;

    @EJB(mappedName = "regweb3/ModeloOficioRemisionEJB/local")
    private ModeloOficioRemisionLocal modeloOficioRemisionEjb;

    @EJB(mappedName = "regweb3/MultiEntidadEJB/local")
    private MultiEntidadLocal multiEntidadEjb;

    @EJB(mappedName = "regweb3/SesionEJB/local")
    private SesionLocal sesionEjb;


    @RolesAllowed({RWE_WS_ENTRADA, RWE_WS_SALIDA})
    @Override
    @WebMethod
    public Long obtenerSesionRegistro(@WebParam(name = "entidad")String entidad) throws  Throwable, WsI18NException{

        // 1.-Validar la entidad y el usuario que realiza la petición
        Entidad entidadActiva = validarEntidad(entidad);

        // 2.- Obtener el usuario aplicación que ha realizado la petición
        UsuarioEntidad usuarioAplicacion = usuarioEntidadEjb.findByIdentificadorEntidad(UsuarioAplicacionCache.get().getUsuario().getIdentificador(), entidadActiva.getId());

        if (usuarioAplicacion == null) { //No existe
            throw new I18NException("registro.usuario.noExiste", UsuarioAplicacionCache.get().getUsuario().getIdentificador(), entidadActiva.getNombre());
        }

        // 3.- Crear la nueva Sesion
        Sesion sesion = sesionEjb.nuevaSesion(usuarioAplicacion);

        if(sesion != null){
            return sesion.getIdSesion();
        }else{
            throw new I18NException("error.ws.general");
        }

    }

    @RolesAllowed({RWE_WS_ENTRADA, RWE_WS_SALIDA})
    @Override
    @WebMethod
    public AsientoRegistralSesionWs verificarAsientoRegistral(@WebParam(name = "entidad") String entidad, @WebParam(name = "idSesion") Long idSesion) throws  Throwable, WsI18NException{

        // 1.-Validar la entidad y el usuario que realiza la petición
        Entidad entidadActiva = validarEntidad(entidad);

        // 2.- Obtener el usuario aplicación que ha realizado la petición
        UsuarioEntidad usuarioAplicacion = usuarioEntidadEjb.findByIdentificadorEntidad(UsuarioAplicacionCache.get().getUsuario().getIdentificador(), entidadActiva.getId());

        if (usuarioAplicacion == null) { //No existe
            throw new I18NException("registro.usuario.noExiste", UsuarioAplicacionCache.get().getUsuario().getIdentificador(), entidadActiva.getNombre());
        }

        // 3.- Validar campo obligatorio idSesion
        if(idSesion == null){
            throw new I18NException("error.valor.requerido.ws", "idSesion");
        }

        // Obtenemos la Sesion
        Sesion sesion = sesionEjb.findByIdSesionUsuario(idSesion, usuarioAplicacion);

        if(sesion == null){
            throw new I18NException("sesion.noExiste", idSesion.toString());
        }

        AsientoRegistralSesionWs asientoRegistralSesionWs = new AsientoRegistralSesionWs();
        asientoRegistralSesionWs.setEstado(sesion.getEstado());

        if(sesion.getEstado().equals(RegwebConstantes.SESION_FINALIZADA)){

            AsientoRegistralWs asientoRegistral = AsientoRegistralConverter.getAsientoRegistral(usuarioAplicacion, sesion.getNumeroRegistro(), sesion.getTipoRegistro(),
                    UsuarioAplicacionCache.get().getIdioma(), false, false, registroEntradaConsultaEjb, registroSalidaConsultaEjb, permisoOrganismoUsuarioEjb, oficioRemisionEjb, lopdEjb);

            asientoRegistralSesionWs.setAsientoRegistralWs(asientoRegistral);
        }

        return asientoRegistralSesionWs;
    }

    @RolesAllowed({RWE_WS_ENTRADA, RWE_WS_SALIDA})
    @Override
    @WebMethod
    public AsientoRegistralWs crearAsientoRegistral(
            @WebParam(name = "idSesion")Long idSesion,
            @WebParam(name = "entidad")String entidad,
            @WebParam(name = "asientoRegistral") AsientoRegistralWs asientoRegistral,
            @WebParam(name = "tipoOperacion") Long tipoOperacion,
            @WebParam(name = "justificante") Boolean justificante,
            @WebParam(name = "distribuir") Boolean distribuir) throws Throwable, WsI18NException, WsValidationException{

        UsuarioEntidad usuarioAplicacion = null;
        Entidad entidadActiva = null;
        AsientoRegistralWs asiento = new AsientoRegistralWs();

        // Definimos la petición que se guardá en el monitor de integración
        Date inicio = new Date();
        StringBuilder peticion = new StringBuilder();
        
        String numRegFormat = "";

        peticion.append("usuario: ").append(UsuarioAplicacionCache.get().getUsuario().getNombreIdentificador()).append(System.getProperty("line.separator"));
        peticion.append("justificante: ").append(justificante).append(System.getProperty("line.separator"));
        peticion.append("distribuir: ").append(distribuir).append(System.getProperty("line.separator"));

        try{

            // Validar la entidad y el usuario que realiza la petición
            entidadActiva = validarEntidad(entidad);

            // Obtener el usuario aplicación que ha realizado la petición
            usuarioAplicacion = usuarioEntidadEjb.findByIdentificadorEntidad(UsuarioAplicacionCache.get().getUsuario().getIdentificador(), entidadActiva.getId());

            if (usuarioAplicacion == null) { //No existe
                throw new I18NException("registro.usuario.noExiste", UsuarioAplicacionCache.get().getUsuario().getIdentificador(), entidadActiva.getNombre());
            }

            // Comprobar que la Oficina está vigente
            Oficina oficina = validarOficina(asientoRegistral.getEntidadRegistralOrigenCodigo(), entidadActiva.getId());

            // Comprobar que el Libro está vigente
            Libro libro = validarLibroUnico(asientoRegistral.getLibroCodigo(), entidadActiva);

            // Comprobar que el Usuario Entidad persona existe en el sistema, si no existe, se intenta crear
            UsuarioEntidad usuario = asientoRegistralEjb.comprobarUsuarioEntidad(asientoRegistral.getCodigoUsuario(), entidadActiva.getId());

            if (usuario == null) {//No existe
                throw new I18NException("registro.usuario.noExiste", asientoRegistral.getCodigoUsuario(), entidadActiva.getNombre());
            }

            // Recuperamos el username correcto
            asientoRegistral.setCodigoUsuario(usuario.getUsuario().getIdentificador());

            //asientoRegistral.setAplicacion(CODIGO_APLICACION); todo REPENSAR setAplicacionTelematica
            //asientoRegistral.setVersion(Versio.VERSIO);

            // Validar los Interesados
            List<Interesado> interesados;
            if (asientoRegistral.getInteresados() != null && asientoRegistral.getInteresados().size() > 0) {

                if(TIPO_OPERACION_COMUNICACION.equals(tipoOperacion)) { //Si es una comunicación

                    if (asientoRegistral.getInteresados().size() != 1) { // solo se permite un interesado
                        throw new I18NException("interesado.registro.obligatorio.uno");
                    }
                }
                // Procesamos los interesados
                interesados = procesarInteresados(asientoRegistral.getInteresados(), interesadoEjb, catPaisEjb, catProvinciaEjb, catLocalidadEjb, personaEjb);

            }else{
                throw new I18NException("interesado.registro.obligatorio");
            }

            // Validar los Anexos
            List<AnexoFull> anexosFull = null;
            if (asientoRegistral.getAnexos() != null && asientoRegistral.getAnexos().size() > 0) {
                anexosFull = procesarAnexos(asientoRegistral.getAnexos(), entidadActiva.getId());
                peticion.append("anexos: ").append(asientoRegistral.getAnexos().size()).append(System.getProperty("line.separator"));
            }

            // Se trata a de un Registro de Entrada
            if(REGISTRO_ENTRADA.equals(asientoRegistral.getTipoRegistro())){

                peticion.append("tipoRegistro: ").append(REGISTRO_ENTRADA_ESCRITO).append(System.getProperty("line.separator"));

                // Comprobar ROL RWE_WS_ENTRADA
                if(!UsuarioAplicacionCache.get().getUsuario().getRwe_ws_entrada()){
                    throw new I18NException("registro.usuario.rol", UsuarioAplicacionCache.get().getUsuario().getIdentificador());
                }

                // Comprobar PERMISO_REGISTRO_ENTRADA de usuario aplicación
                if (!permisoOrganismoUsuarioEjb.tienePermiso(usuarioAplicacion.getId(), oficina.getOrganismoResponsable().getId(), PERMISO_REGISTRO_ENTRADA, true)) {
                    throw new I18NException("registro.usuario.permisos", usuarioAplicacion.getNombreCompleto(), libro.getCodigo());
                }

                // Comprobar que el Organismo destino está vigente
                UnidadTF destinoExterno = null;
                Organismo destinoInterno = organismoEjb.findByCodigoByEntidadMultiEntidad(asientoRegistral.getUnidadTramitacionDestinoCodigo(),entidadActiva.getId());
                if(destinoInterno == null){ //Externo, lo vamos a buscar a dir3caib
                    // Lo buscamos en DIR3CAIB
                    Dir3CaibObtenerUnidadesWs unidadesService = Dir3CaibUtils.getObtenerUnidadesService(PropiedadGlobalUtil.getDir3CaibServer(), PropiedadGlobalUtil.getDir3CaibUsername(), PropiedadGlobalUtil.getDir3CaibPassword());
                    destinoExterno = unidadesService.obtenerUnidad(asientoRegistral.getUnidadTramitacionDestinoCodigo(), null, null);

                    if (destinoExterno == null) { //o no existe o está extinguido
                        throw new I18NException("registro.organismo.noExiste", asientoRegistral.getUnidadTramitacionDestinoCodigo());
                    }
                }else if( !destinoInterno.getEntidad().getId().equals(entidadActiva.getId())){ //No hace falta ir a buscarlo a dir3caib, ya tenemos los datos mínimos.
                    destinoExterno = new UnidadTF();
                    destinoExterno.setCodigo(destinoInterno.getCodigo());
                    destinoExterno.setDenominacion(destinoInterno.getDenominacion());
                    destinoInterno = null;
                } else if (!destinoInterno.getEstado().getCodigoEstadoEntidad().equals(ESTADO_ENTIDAD_VIGENTE)) { //Si está extinguido
                    throw new I18NException("registro.organismo.extinguido", destinoInterno.getNombreCompleto());
                }


                // Convertimos a Registro de Entrada
                RegistroEntrada registroEntrada = AsientoRegistralConverter.getRegistroEntrada(
                        asientoRegistral, usuario, libro, oficina, destinoInterno, destinoExterno,
                        codigoAsuntoEjb, tipoAsuntoEjb);

                // Validar el RegistroEntrada
                validateRegistroEntrada(registroEntrada);

                // Creamos el Registro de Entrada
                try{

                    // Si hay idSesion, gestionar el nuevo AsientoRegistral mediante este sistema
                    if(idSesion != null){
                        try{
                            // Iniciamos la sesión
                            sesionEjb.iniciarSesion(idSesion, usuarioAplicacion);
                        }catch (Exception e){
                            throw new I18NException("sesion.noExiste", idSesion.toString());
                        }
                    }

                    registroEntrada = asientoRegistralEjb.registrarEntrada(registroEntrada, usuario, interesados, anexosFull, true);
                    numRegFormat = registroEntrada.getNumeroRegistroFormateado();

                    asiento.setNumeroRegistro(registroEntrada.getNumeroRegistro());
                    asiento.setNumeroRegistroFormateado(registroEntrada.getNumeroRegistroFormateado());
                    asiento.setFechaRegistro(registroEntrada.getFecha());

                    // Distribuir / Generar justificante
                    if(justificante && distribuir){

                        if(PropiedadGlobalUtil.getCustodiaDiferida(entidadActiva.getId())){ // Si la Custodia en diferido está activa, generamos el  justificante
                            asientoRegistralEjb.crearJustificante(usuario, registroEntrada, REGISTRO_ENTRADA, RegistroUtils.getIdiomaJustificante(registroEntrada));
                        }

                        // Distribuimos, si la Custodia en diferido no está activa, se generará el justificante antes de Distribuir
                        asientoRegistralEjb.distribuirRegistroEntrada(registroEntrada, usuarioAplicacion);

                    }else if(justificante){
                        asientoRegistralEjb.crearJustificante(usuario, registroEntrada, REGISTRO_ENTRADA, RegistroUtils.getIdiomaJustificante(registroEntrada));
                    }else if(distribuir){
                        asientoRegistralEjb.distribuirRegistroEntrada(registroEntrada, usuarioAplicacion);
                    }

                    //Integracion OK
                    peticion.append("oficina: ").append(registroEntrada.getOficina().getDenominacion()).append(System.getProperty("line.separator"));
                    peticion.append("registro: ").append(registroEntrada.getNumeroRegistroFormateado()).append(System.getProperty("line.separator"));
                    peticion.append("extracto: ").append(registroEntrada.getRegistroDetalle().getExtracto()).append(System.getProperty("line.separator"));
                    integracionEjb.addIntegracionOk(inicio, RegwebConstantes.INTEGRACION_WS, UsuarioAplicacionCache.get().getMethod().getName(),peticion.toString(), System.currentTimeMillis() - inicio.getTime(), entidadActiva.getId(), numRegFormat);

                }catch (Exception e){
                    e.printStackTrace();
                    throw new I18NException(e, "registro.nuevo.error");
                }

             // Se trata a de un Registro de Salida
            }else if(REGISTRO_SALIDA.equals(asientoRegistral.getTipoRegistro())){

                peticion.append("tipoRegistro: ").append(REGISTRO_SALIDA_ESCRITO).append(System.getProperty("line.separator"));
                if(tipoOperacion != null){
                    peticion.append("tipoOperacion: ").append(tipoOperacion).append(System.getProperty("line.separator"));
                }

                // Comprobar ROL RWE_WS_SALIDA
                if(!UsuarioAplicacionCache.get().getUsuario().getRwe_ws_salida()){
                    throw new I18NException("registro.usuario.rol", UsuarioAplicacionCache.get().getUsuario().getIdentificador());
                }

                // Comprobar PERMISO_REGISTRO_SALIDA de usuario aplicación
                if (!permisoOrganismoUsuarioEjb.tienePermiso(usuarioAplicacion.getId(), oficina.getOrganismoResponsable().getId(), PERMISO_REGISTRO_SALIDA, true)) {
                    throw new I18NException("registro.usuario.permisos", usuarioAplicacion.getNombreCompleto(), libro.getCodigo());
                }

                // Comprobar que el Organismo Origen está vigente
                Organismo origen = organismoEjb.findByCodigoEntidadLigero(asientoRegistral.getUnidadTramitacionOrigenCodigo(), entidadActiva.getId());

                if (origen == null) {
                    throw new I18NException("registro.organismo.noExiste", asientoRegistral.getUnidadTramitacionOrigenCodigo());

                } else if (!origen.getEstado().getCodigoEstadoEntidad().equals(ESTADO_ENTIDAD_VIGENTE)) { //Si está extinguido
                    throw new I18NException("registro.organismo.extinguido", origen.getNombreCompleto());
                }

                // Convertir a registro de Salida
                RegistroSalida registroSalida = AsientoRegistralConverter.getRegistroSalida(
                        asientoRegistral, usuario, libro, oficina, origen,
                        codigoAsuntoEjb, tipoAsuntoEjb);

                // Validar el RegistroSalida
                validateRegistroSalida(registroSalida);

                // Registrar el Registro de Salida y lo procesamos
                try {

                    // Si hay idSesion, gestionar el nuevo AsientoRegistral mediante este sistema
                    if(idSesion != null){
                        try{
                            // Iniciamos la sesión
                            sesionEjb.iniciarSesion(idSesion, usuarioAplicacion);
                        }catch (Exception e){
                            throw new I18NException("sesion.noExiste", idSesion.toString());
                        }
                    }

                    // Registrar la salida
                    registroSalida = asientoRegistralEjb.registrarSalida(registroSalida, usuario, interesados, anexosFull, true);
                    numRegFormat = registroSalida.getNumeroRegistroFormateado();

                    asiento.setNumeroRegistro(registroSalida.getNumeroRegistro());
                    asiento.setNumeroRegistroFormateado(registroSalida.getNumeroRegistroFormateado());
                    asiento.setFechaRegistro(registroSalida.getFecha());

                    // Procesar el Registro de Salida según el Tipo Operación
                    registroSalida = asientoRegistralEjb.procesarRegistroSalida(tipoOperacion, registroSalida);

                    //Actualizamos el AsientoRegistral
                    asiento.setEstado(registroSalida.getEstado());
                    asiento.setIdentificadorIntercambio(registroSalida.getRegistroDetalle().getIdentificadorIntercambio());

                    // Justificante
                    if(tipoOperacion == null && justificante){
                        asientoRegistralEjb.crearJustificante(usuario, registroSalida, REGISTRO_SALIDA, RegistroUtils.getIdiomaJustificante(registroSalida));
                    }

                    // Integracion OK
                    peticion.append("oficina: ").append(registroSalida.getOficina().getDenominacion()).append(System.getProperty("line.separator"));
                    peticion.append("registro: ").append(registroSalida.getNumeroRegistroFormateado()).append(System.getProperty("line.separator"));
                    peticion.append("extracto: ").append(registroSalida.getRegistroDetalle().getExtracto()).append(System.getProperty("line.separator"));
                    integracionEjb.addIntegracionOk(inicio, RegwebConstantes.INTEGRACION_WS, UsuarioAplicacionCache.get().getMethod().getName(), peticion.toString(), System.currentTimeMillis() - inicio.getTime(), entidadActiva.getId(), numRegFormat);

                } catch (Exception e) {
                    e.printStackTrace();
                    throw new I18NException(e, "registro.nuevo.error");
                }
            }

            // Ha funcionado bien, finalizamos la sesion
            if(idSesion != null){
                sesionEjb.finalizarSesion(idSesion, usuarioAplicacion, asientoRegistral.getTipoRegistro(), asiento.getNumeroRegistroFormateado());
            }

        }catch (I18NException | Exception e){

            if(entidadActiva != null){
                integracionEjb.addIntegracionError(RegwebConstantes.INTEGRACION_WS, UsuarioAplicacionCache.get().getMethod().getName(), peticion.toString(), e, null, System.currentTimeMillis() - inicio.getTime(), entidadActiva.getId(), numRegFormat);
            }

            // Marcamos la sesión como ERROR
            if(idSesion != null){
                sesionEjb.cambiarEstado(idSesion, usuarioAplicacion, RegwebConstantes.SESION_ERROR);
            }

            throw e;
        }

        return asiento;
    }



    @RolesAllowed({RWE_WS_ENTRADA, RWE_WS_SALIDA})
    @Override
    @WebMethod
    public AsientoRegistralWs obtenerAsientoRegistral(
       @WebParam(name = "entidad") String entidad,
       @WebParam(name = "numeroRegistroFormateado") String numeroRegistroFormateado,
       @WebParam(name = "tipoRegistro") Long tipoRegistro,
       @WebParam(name = "conAnexos") boolean conAnexos) throws  Throwable, WsI18NException, WsValidationException{

        // Validar obligatorios
        Entidad entidadActiva = validarObligatorios(numeroRegistroFormateado,entidad);

        // Integraciones
        Date inicio = new Date();
        StringBuilder peticion = new StringBuilder();
        
        peticion.append("usuario: ").append(UsuarioAplicacionCache.get().getUsuario().getNombreIdentificador()).append(System.getProperty("line.separator"));
        peticion.append("registro: ").append(numeroRegistroFormateado).append(System.getProperty("line.separator"));

        //  Obtenemos el usuario entidad
        UsuarioEntidad usuarioEntidad = usuarioEntidadEjb.findByIdentificadorCodigoEntidad( UsuarioAplicacionCache.get().getUsuario().getIdentificador(), entidad);

        if(usuarioEntidad == null){//No existe
            throw new I18NException("registroEntrada.usuario.noExiste", UsuarioAplicacionCache.get().getUsuario().getIdentificador(), entidad);
        }

        // Obtenemos el AsientoRegistral
        AsientoRegistralWs asientoRegistralWs;

        try{

            asientoRegistralWs = AsientoRegistralConverter.getAsientoRegistral(usuarioEntidad, numeroRegistroFormateado, tipoRegistro,
                    UsuarioAplicacionCache.get().getIdioma(), conAnexos, true, registroEntradaConsultaEjb, registroSalidaConsultaEjb, permisoOrganismoUsuarioEjb, oficioRemisionEjb, lopdEjb);

        }catch (Exception e){

            integracionEjb.addIntegracionError(RegwebConstantes.INTEGRACION_WS, UsuarioAplicacionCache.get().getMethod().getName(), peticion.toString(), e, null,System.currentTimeMillis() - inicio.getTime(), entidadActiva.getId(), numeroRegistroFormateado);
            throw new I18NException("asientoRegistral.obtener.error", e.getLocalizedMessage());
        }

        // Integracion
        integracionEjb.addIntegracionOk(inicio, RegwebConstantes.INTEGRACION_WS, UsuarioAplicacionCache.get().getMethod().getName(),peticion.toString(), System.currentTimeMillis() - inicio.getTime(), entidadActiva.getId(), numeroRegistroFormateado);

        return asientoRegistralWs;

    }

    @RolesAllowed({RWE_WS_ENTRADA, RWE_WS_SALIDA, RWE_WS_CIUDADANO})
    @Override
    @WebMethod
    public JustificanteWs obtenerJustificante(
       @WebParam(name = "entidad") String entidad,
       @WebParam(name = "numeroRegistroFormateado")String numeroRegistroFormateado,
       @WebParam(name = "tipoRegistro") Long tipoRegistro) throws Throwable, WsI18NException, WsValidationException{

        //1.- Validar obligatorios
        Entidad entidadActiva = validarObligatorios(numeroRegistroFormateado,entidad);

        // Integraciones
        Date inicio = new Date();
        StringBuilder peticion = new StringBuilder();
        
        peticion.append("usuario: ").append(UsuarioAplicacionCache.get().getUsuario().getNombreIdentificador()).append(System.getProperty("line.separator"));
        peticion.append("registro: ").append(numeroRegistroFormateado).append(System.getProperty("line.separator"));

        if(REGISTRO_ENTRADA.equals(tipoRegistro)) {
            peticion.append("tipoRegistro: ").append("entrada").append(System.getProperty("line.separator"));
        }else if(REGISTRO_SALIDA.equals(tipoRegistro)){
            peticion.append("tipoRegistro: ").append("salida").append(System.getProperty("line.separator"));
        }

        UsuarioEntidad usuario = usuarioEntidadEjb.findByIdentificadorEntidad(UsuarioAplicacionCache.get().getUsuario().getIdentificador(), entidadActiva.getId());

        AnexoFull justificante = null;
        AnexoSimple anexoSimple = null;

        if(REGISTRO_ENTRADA.equals(tipoRegistro)){
            // 4.- Obtenemos el RegistroEntrada
            RegistroEntrada registroEntrada = registroEntradaConsultaEjb.findByNumeroRegistroFormateadoCompleto(entidad, numeroRegistroFormateado);

            if (registroEntrada == null) {
                throw new I18NException("registroEntrada.noExiste", numeroRegistroFormateado);
            }

            // Si no tiene Justificante, lo generamos
            if(!registroEntrada.getRegistroDetalle().getTieneJustificante()){

                // Solo se puede generar si el registro es Válido o está en la Cola de distribución
                if(registroEntrada.getEstado().equals(REGISTRO_VALIDO) || registroEntrada.getEstado().equals(REGISTRO_DISTRIBUYENDO)){

                    try{
                        justificante = justificanteEjb.crearJustificanteWS(usuario,registroEntrada,RegwebConstantes.REGISTRO_ENTRADA, RegistroUtils.getIdiomaJustificante(registroEntrada));
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

        } else if(REGISTRO_SALIDA.equals(tipoRegistro)){

            // 4.- Obtenemos el RegistroSalida
            RegistroSalida registroSalida = registroSalidaConsultaEjb.findByNumeroRegistroFormateadoCompleto(entidad, numeroRegistroFormateado);

            if (registroSalida == null) {
                throw new I18NException("registroEntrada.noExiste", numeroRegistroFormateado);
            }

            // Si no tiene Justificante, lo generamos
            if(!registroSalida.getRegistroDetalle().getTieneJustificante()){

                // Solo se puede generar si el registro es Válido
                if(registroSalida.getEstado().equals(REGISTRO_VALIDO)) {

                    try{
                        justificante = justificanteEjb.crearJustificanteWS(usuario,registroSalida,RegwebConstantes.REGISTRO_SALIDA, RegistroUtils.getIdiomaJustificante(registroSalida));
                    }catch (I18NException e){
                        integracionEjb.addIntegracionError(RegwebConstantes.INTEGRACION_WS, UsuarioAplicacionCache.get().getMethod().getName(), peticion.toString(), e, null,System.currentTimeMillis() - inicio.getTime(), entidadActiva.getId(), numeroRegistroFormateado);
                        throw new I18NException("registro.justificante.error", numeroRegistroFormateado);
                    }

                    anexoSimple = anexoEjb.descargarJustificante(justificante.getAnexo(), entidadActiva.getId());
                }else{
                    throw new I18NException("registro.justificante.valido");
                }

            }else{ // Tiene Justificante, lo obtenemos

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
        }

        return new JustificanteWs(anexoSimple.getData());

    }

    @RolesAllowed({RWE_WS_ENTRADA, RWE_WS_SALIDA, RWE_WS_CIUDADANO})
    @Override
    @WebMethod
    public JustificanteReferenciaWs obtenerReferenciaJustificante(
            @WebParam(name = "entidad") String entidad,
            @WebParam(name = "numeroRegistroFormateado")String numeroRegistroFormateado) throws Throwable, WsI18NException, WsValidationException{

        //1.- Validar obligatorios
        Entidad entidadActiva =  validarObligatorios(numeroRegistroFormateado,entidad);

        // Integraciones
        Date inicio = new Date();
        StringBuilder peticion = new StringBuilder();
        
        peticion.append("usuario: ").append(UsuarioAplicacionCache.get().getUsuario().getNombreIdentificador()).append(System.getProperty("line.separator"));
        peticion.append("registro: ").append(numeroRegistroFormateado).append(System.getProperty("line.separator"));


        try{
            JustificanteReferencia jref = asientoRegistralEjb.obtenerReferenciaJustificante(numeroRegistroFormateado, entidadActiva);

            // Integracion
            integracionEjb.addIntegracionOk(inicio, RegwebConstantes.INTEGRACION_WS, UsuarioAplicacionCache.get().getMethod().getName(),peticion.toString(), System.currentTimeMillis() - inicio.getTime(), entidadActiva.getId(), numeroRegistroFormateado);

            return new JustificanteReferenciaWs(jref.getCsv(), jref.getUrl());

        }catch (Exception e){
            integracionEjb.addIntegracionError(RegwebConstantes.INTEGRACION_WS, UsuarioAplicacionCache.get().getMethod().getName(), peticion.toString(), e, null,System.currentTimeMillis() - inicio.getTime(), entidadActiva.getId(), numeroRegistroFormateado);
            throw new I18NException(e, "error.ws.general");
        }

    }


    @RolesAllowed({RWE_WS_ENTRADA, RWE_WS_SALIDA})
    @Override
    @WebMethod
    public void distribuirAsientoRegistral(
       @WebParam(name = "entidad") String entidad,
       @WebParam(name = "numeroRegistroFormateado") String numeroRegistroFormateado) throws Throwable, WsI18NException, WsValidationException{

        //1.- Validar obligatorios
        Entidad entidadActiva= validarObligatorios(numeroRegistroFormateado,entidad);

        UsuarioEntidad usuario = usuarioEntidadEjb.findByIdentificadorEntidad(UsuarioAplicacionCache.get().getUsuario().getIdentificador(), entidadActiva.getId());

        // 2.- Obtenemos el RegistroEntrada
        RegistroEntrada registroEntrada = registroEntradaConsultaEjb.findByNumeroRegistroFormateadoCompleto(entidad, numeroRegistroFormateado);


        if (registroEntrada == null) {
            throw new I18NException("registroEntrada.noExiste", numeroRegistroFormateado);
        }

        // 3.- Comprobamos que el usuario tiene permisos para Distribuir el registro
        if(!permisoOrganismoUsuarioEjb.tienePermiso(usuario.getId(), registroEntrada.getOficina().getOrganismoResponsable().getId(), RegwebConstantes.PERMISO_DISTRIBUCION_REGISTRO, true)){
            throw new I18NException("registroEntrada.distribuir.error.permiso");
        }

        // 4.- Comprobamos que el RegistroEntrada se puede Distribuir
        if (!registroEntradaConsultaEjb.isDistribuir(registroEntrada.getId())) {
            throw new I18NException("registroEntrada.distribuir.noPermitido");
        }

        try{
            // 5.- Distribuimos el registro de entrada
            RespuestaDistribucion respuestaDistribucion = distribucionEjb.distribuir(registroEntrada, usuario);

            if(!respuestaDistribucion.getEnviadoCola() && !respuestaDistribucion.getEnviado()){ //Cuando hay plugin y no ha llegado a destino

                throw new I18NException(("registroEntrada.distribuir.error.noEnviado"));
            }

        }catch (Exception e){
            throw new I18NException(e, "registroEntrada.distribuir.error");
        }

    }

    /**
     * Obtiene el documento generado del oficioExterno del asiento indicado por numeroRegistroFormateado.
     * Este asiento debe formar parte de un Oficio Externo.
     * @param entidad
     * @param numeroRegistroFormateado
     * @return
     * @throws Throwable
     * @throws WsI18NException
     * @throws WsValidationException
     */
    @RolesAllowed({RWE_WS_ENTRADA, RWE_WS_SALIDA})
    @Override
    @WebMethod
    public OficioWs obtenerOficioExterno(
       @WebParam(name = "entidad") String entidad,
       @WebParam(name = "numeroRegistroFormateado")String numeroRegistroFormateado) throws Throwable, WsI18NException, WsValidationException{

        Entidad entidadActiva = validarObligatorios(numeroRegistroFormateado,entidad);

        //Averiguamos si existe un oficio de remisión para el número de registro indicado.
        OficioRemision oficio = oficioRemisionEjb.getByNumeroRegistroFormateado(numeroRegistroFormateado,entidad);
        if(oficio == null){
            throw new I18NException("oficioRemision.noExiste", numeroRegistroFormateado);
        }

        List<String> registrosEntrada = new ArrayList<String>();
        List<String> registrosSalida = new ArrayList<String>();

        //Obtenemos los diferentes registros de entrada o de salida que forman parte del oficio de Remisión en cuestión
        if (RegwebConstantes.TIPO_OFICIO_REMISION_ENTRADA.equals(oficio.getTipoOficioRemision())) {
            registrosEntrada = oficioRemisionEjb.getNumerosRegistroEntradaFormateadoByOficioRemision(oficio.getId());

        } else if (RegwebConstantes.TIPO_OFICIO_REMISION_SALIDA.equals(oficio.getTipoOficioRemision())) {
            registrosSalida = oficioRemisionEjb.getNumerosRegistroSalidaFormateadoByOficioRemision(oficio.getId());

        }

        //Obtenemos los modelos de oficio de remisión de la entidad indicada.
        List<ModeloOficioRemision> modelos = modeloOficioRemisionEjb.getByEntidad(entidadActiva.getId());


        if(modelos.size()>0) {// Si la entidad tiene modelo de remisión
            //Obtenemos el modelo asociado
            ModeloOficioRemision modeloOficioRemision = modeloOficioRemisionEjb.findById( modelos.get(0).getId());
            //Generamos el documento del oficio de remisión

            return new OficioWs(IOUtils.toByteArray(oficioRemisionEjb.generarOficioRemisionRtf(oficio, modeloOficioRemision, registrosEntrada, registrosSalida)));

        }else{
            throw new Exception("La entidad no tiene ningún modelo de oficio de remisión y no se ha podido generar.");
        }

    }

    @RolesAllowed({RWE_WS_CIUDADANO})
    @Override
    @WebMethod
    public ResultadoBusquedaWs obtenerAsientosCiudadano(@WebParam(name = "entidad") String entidad,  @WebParam(name = "documento") String documento, @WebParam(name = "pageNumber") Integer pageNumber) throws Throwable, WsI18NException, WsValidationException{

        // Definimos la petición que se guardá en el monitor de integración
        Date inicio = new Date();
        StringBuilder peticion = new StringBuilder();
        
        String numRegFormat = "";

        peticion.append("usuario: ").append(UsuarioAplicacionCache.get().getUsuario().getNombreIdentificador()).append(System.getProperty("line.separator"));

        // 1.- Validar campo obligatorio entidad
        Entidad entidadActiva = validarEntidad(entidad);

        // 2.- Obtener el usuario aplicación que ha realizado la petición
        UsuarioEntidad usuarioAplicacion = usuarioEntidadEjb.findByIdentificadorEntidad(UsuarioAplicacionCache.get().getUsuario().getIdentificador(), entidadActiva.getId());

        if (usuarioAplicacion == null) { //No existe
            throw new I18NException("registro.usuario.noExiste", UsuarioAplicacionCache.get().getUsuario().getIdentificador(), entidadActiva.getNombre());
        }

        // 3.- Validar campo obligatorio documento
        if(StringUtils.isEmpty(documento)){
            throw new I18NException("error.valor.requerido.ws", "documento");
        }

        // 4.- Validar obligatorio pageNumber
        if(pageNumber == null){
            pageNumber = 0;
        }

        peticion.append("documento: ").append(documento).append(System.getProperty("line.separator"));

        ResultadoBusquedaWs<AsientoRegistralWs> resultado = new ResultadoBusquedaWs<AsientoRegistralWs>();

        try{

            // Obtenemos los Registros de Entrada de un ciudadano
            List<RegistroEntrada> entradas = registroEntradaConsultaEjb.getByDocumento(entidadActiva.getId(),documento);
            resultado.setTotalResults(entradas.size());
            resultado.setPageNumber(pageNumber);

            // Transformamos los Registros de Entrada en AsientoRegistralWs
            List<AsientoRegistralWs> asientos = new ArrayList<AsientoRegistralWs>();
            for (RegistroEntrada entrada : entradas) {

                asientos.add(AsientoRegistralConverter.transformarRegistro(entrada, REGISTRO_ENTRADA, entidadActiva,
                        UsuarioAplicacionCache.get().getIdioma(),  oficioRemisionEjb));

            }
            resultado.setResults(asientos);

            peticion.append("asientos: ").append(asientos.size()).append(System.getProperty("line.separator"));
            integracionEjb.addIntegracionOk(inicio, RegwebConstantes.INTEGRACION_WS, UsuarioAplicacionCache.get().getMethod().getName(),peticion.toString(), System.currentTimeMillis() - inicio.getTime(), entidadActiva.getId(), numRegFormat);

        }catch (Exception e){
            e.printStackTrace();
            integracionEjb.addIntegracionError(RegwebConstantes.INTEGRACION_WS, UsuarioAplicacionCache.get().getMethod().getName(), peticion.toString(), e, null,System.currentTimeMillis() - inicio.getTime(), entidadActiva.getId(), numRegFormat);
            throw new I18NException(e, "error.ws.general");
        }

        return resultado;
    }

    @RolesAllowed({RWE_WS_CIUDADANO})
    @Override
    @WebMethod
    public AsientoRegistralWs obtenerAsientoCiudadano(@WebParam(name = "entidad") String entidad, @WebParam(name = "documento") String documento, @WebParam(name = "numeroRegistroFormateado") String numeroRegistroFormateado) throws Throwable{

        // Definimos la petición que se guardá en el monitor de integración
        Date inicio = new Date();
        StringBuilder peticion = new StringBuilder();
        
        String numRegFormat = "";

        peticion.append("usuario: ").append(UsuarioAplicacionCache.get().getUsuario().getNombreIdentificador()).append(System.getProperty("line.separator"));

        //  Validar campo obligatorio entidad
        Entidad entidadActiva = validarEntidad(entidad);

        // Obtener el usuario aplicación que ha realizado la petición
        UsuarioEntidad usuarioAplicacion = usuarioEntidadEjb.findByIdentificadorEntidad(UsuarioAplicacionCache.get().getUsuario().getIdentificador(), entidadActiva.getId());

        // Validar campo obligatorio documento
        if(StringUtils.isEmpty(documento)){
            throw new I18NException("error.valor.requerido.ws", "documento");
        }

        // Validar obligatorio numeroRegistroFormateado
        if(StringUtils.isEmpty(numeroRegistroFormateado)){
            throw new I18NException("error.valor.requerido.ws", "numeroRegistroFormateado");
        }

        peticion.append("documento: ").append(documento).append(System.getProperty("line.separator"));
        peticion.append("registro: ").append(numeroRegistroFormateado).append(System.getProperty("line.separator"));

        try{

            RegistroEntrada registroEntrada = registroEntradaConsultaEjb.getByDocumentoNumeroRegistro(entidadActiva.getId(), documento, numeroRegistroFormateado);

            if (registroEntrada == null) {
                throw new I18NException("registroEntrada.noExiste", numeroRegistroFormateado);
            }

            AsientoRegistralWs asiento = AsientoRegistralConverter.transformarRegistro(registroEntrada, REGISTRO_ENTRADA, entidadActiva,
                    UsuarioAplicacionCache.get().getIdioma(),  oficioRemisionEjb);

            integracionEjb.addIntegracionOk(inicio, RegwebConstantes.INTEGRACION_WS, UsuarioAplicacionCache.get().getMethod().getName(),peticion.toString(), System.currentTimeMillis() - inicio.getTime(), entidadActiva.getId(), numRegFormat);

            lopdEjb.altaLopd(registroEntrada.getNumeroRegistro(), registroEntrada.getFecha(), registroEntrada.getLibro().getId(), usuarioAplicacion.getId(), RegwebConstantes.REGISTRO_ENTRADA, RegwebConstantes.LOPD_CONSULTA);

            return asiento;

        }catch (Exception e){
            e.printStackTrace();
            integracionEjb.addIntegracionError(RegwebConstantes.INTEGRACION_WS, UsuarioAplicacionCache.get().getMethod().getName(), peticion.toString(), e, null,System.currentTimeMillis() - inicio.getTime(), entidadActiva.getId(), numRegFormat);
            throw new I18NException("asientoRegistral.obtener.error", e.getLocalizedMessage());
        }

    }

    @RolesAllowed({RWE_WS_CIUDADANO})
    @Override
    @WebMethod
    public ResultadoBusquedaWs obtenerAsientosCiudadanoCarpeta(@WebParam(name = "entidad") String entidad,  @WebParam(name = "documento") String documento, @WebParam(name = "pageNumber") Integer pageNumber, @WebParam(name = "idioma") String idioma, @WebParam(name = "fechaInicio") Date fechaInicio, @WebParam(name = "fechaFin") Date fechaFin, @WebParam(name = "numeroRegistroFormateado") String numeroRegistroFormateado, @WebParam(name = "estados") List<Integer> estados, @WebParam(name = "extracto") String extracto, @WebParam(name = "resultPorPagina") Integer resultPorPagina) throws Throwable, WsI18NException, WsValidationException{

        // Definimos la petición que se guardá en el monitor de integración
        Date inicio = new Date();
        StringBuilder peticion = new StringBuilder();
        
        String numRegFormat = "";

        peticion.append("usuario: ").append(UsuarioAplicacionCache.get().getUsuario().getNombreIdentificador()).append(System.getProperty("line.separator"));

        // Validar campo obligatorio entidad
        Entidad entidadActiva = validarEntidad(entidad);

        // Obtener el usuario aplicación que ha realizado la petición
        UsuarioEntidad usuarioAplicacion = usuarioEntidadEjb.findByIdentificadorEntidad(UsuarioAplicacionCache.get().getUsuario().getIdentificador(), entidadActiva.getId());

        if (usuarioAplicacion == null) { //No existe
            throw new I18NException("registro.usuario.noExiste", UsuarioAplicacionCache.get().getUsuario().getIdentificador(), entidadActiva.getNombre());
        }

        // Validar campo obligatorio documento
        if(StringUtils.isEmpty(documento)){
            throw new I18NException("error.valor.requerido.ws", "documento");
        }

        // Validar obligatorio pageNumber
        if(pageNumber == null){
            pageNumber = 0;
        }

        // Validar campo idioma
        if(StringUtils.isEmpty(idioma)){
            idioma = Configuracio.getDefaultLanguage();
        }

        peticion.append("documento: ").append(documento).append(System.getProperty("line.separator"));

        ResultadoBusquedaWs<AsientoWs> resultado = new ResultadoBusquedaWs<AsientoWs>();

        try{

            // Obtenemos los Registros de Entrada de un ciudadano
            Paginacion entradas = registroEntradaConsultaEjb.getByDocumento(entidadActiva.getId(),documento, pageNumber, fechaInicio, fechaFin,numeroRegistroFormateado,estados,extracto, resultPorPagina);
            resultado.setTotalResults(entradas.getTotalResults());
            resultado.setPageNumber(pageNumber);

            // Transformamos los Registros de Entrada en AsientoRegistralWs
            List<AsientoWs> asientos = new ArrayList<AsientoWs>();
            for (RegistroEntrada entrada : (List<RegistroEntrada>) entradas.getListado()) {

                asientos.add(AsientoConverter.transformarRegistro(entrada, REGISTRO_ENTRADA, idioma));
            }
            resultado.setResults(asientos);

            peticion.append("asientos: ").append(asientos.size()).append(System.getProperty("line.separator"));
            integracionEjb.addIntegracionOk(inicio, RegwebConstantes.INTEGRACION_WS, UsuarioAplicacionCache.get().getMethod().getName(),peticion.toString(), System.currentTimeMillis() - inicio.getTime(), entidadActiva.getId(), numRegFormat);

        }catch (Exception e){
            e.printStackTrace();
            integracionEjb.addIntegracionError(RegwebConstantes.INTEGRACION_WS, UsuarioAplicacionCache.get().getMethod().getName(), peticion.toString(), e, null,System.currentTimeMillis() - inicio.getTime(), entidadActiva.getId(), numRegFormat);
            throw new I18NException(e, "error.ws.general");
        }

        return resultado;
    }


    @RolesAllowed({RWE_WS_CIUDADANO})
    @Override
    @WebMethod
    public AsientoWs obtenerAsientoCiudadanoCarpeta(@WebParam(name = "entidad") String entidad, @WebParam(name = "documento") String documento, @WebParam(name = "numeroRegistroFormateado") String numeroRegistroFormateado, @WebParam(name = "idioma") String idioma) throws Throwable{

        // Definimos la petición que se guardá en el monitor de integración
        Date inicio = new Date();
        StringBuilder peticion = new StringBuilder();
        
        String numRegFormat = "";

        peticion.append("usuario: ").append(UsuarioAplicacionCache.get().getUsuario().getNombreIdentificador()).append(System.getProperty("line.separator"));

        // Validar campo obligatorio entidad
        Entidad entidadActiva = validarEntidad(entidad);

        // Obtener el usuario aplicación que ha realizado la petición
        UsuarioEntidad usuarioAplicacion = usuarioEntidadEjb.findByIdentificadorEntidad(UsuarioAplicacionCache.get().getUsuario().getIdentificador(), entidadActiva.getId());

        // Validar campo obligatorio documento
        if(StringUtils.isEmpty(documento)){
            throw new I18NException("error.valor.requerido.ws", "documento");
        }

        // Validar obligatorio numeroRegistroFormateado
        if(StringUtils.isEmpty(numeroRegistroFormateado)){
            throw new I18NException("error.valor.requerido.ws", "numeroRegistroFormateado");
        }

        // Validar campo idioma
        if(StringUtils.isEmpty(idioma)){
            idioma = Configuracio.getDefaultLanguage();
        }

        peticion.append("documento: ").append(documento).append(System.getProperty("line.separator"));
        peticion.append("registro: ").append(numeroRegistroFormateado).append(System.getProperty("line.separator"));

        try{

            RegistroEntrada registroEntrada = registroEntradaConsultaEjb.getByDocumentoNumeroRegistro(entidadActiva.getId(), documento, numeroRegistroFormateado);

            if (registroEntrada == null) {
                throw new I18NException("registroEntrada.noExiste", numeroRegistroFormateado);
            }

            AsientoWs asiento = AsientoConverter.transformarRegistro(registroEntrada, REGISTRO_ENTRADA, idioma);

            integracionEjb.addIntegracionOk(inicio, RegwebConstantes.INTEGRACION_WS, UsuarioAplicacionCache.get().getMethod().getName(),peticion.toString(), System.currentTimeMillis() - inicio.getTime(), entidadActiva.getId(), numRegFormat);

            lopdEjb.altaLopd(registroEntrada.getNumeroRegistro(), registroEntrada.getFecha(), registroEntrada.getLibro().getId(), usuarioAplicacion.getId(), RegwebConstantes.REGISTRO_ENTRADA, RegwebConstantes.LOPD_CONSULTA);

            return asiento;

        }catch (Exception e){
            e.printStackTrace();
            integracionEjb.addIntegracionError(RegwebConstantes.INTEGRACION_WS, UsuarioAplicacionCache.get().getMethod().getName(), peticion.toString(), e, null,System.currentTimeMillis() - inicio.getTime(), entidadActiva.getId(), numRegFormat);
            throw new I18NException("asientoRegistral.obtener.error", e.getLocalizedMessage());
        }
    }

    @RolesAllowed({RWE_WS_CIUDADANO})
    @Override
    @WebMethod
    public FileContentWs obtenerAnexoCiudadano(@WebParam(name = "entidad") String entidad, @WebParam(name = "idAnexo") Long idAnexo, @WebParam(name = "idioma") String idioma) throws Throwable{

        // Definimos la petición que se guardá en el monitor de integración
        Date inicio = new Date();
        StringBuilder peticion = new StringBuilder();
        
        String numRegFormat = "";

        peticion.append("usuario: ").append(UsuarioAplicacionCache.get().getUsuario().getNombreIdentificador()).append(System.getProperty("line.separator"));

        //  Validar campo obligatorio entidad
        Entidad entidadActiva = validarEntidad(entidad);

        // Validar obligatorio idAnexo
        if(idAnexo == null){
            throw new I18NException("error.valor.requerido.ws", "idAnexo");
        }

        // Validar campo idioma
        if(StringUtils.isEmpty(idioma)){
            idioma = Configuracio.getDefaultLanguage();
        }

        peticion.append("idAnexo: ").append(idAnexo).append(System.getProperty("line.separator"));

        try{

            AnexoFull anexo = anexoEjb.getAnexoFull(idAnexo, entidadActiva.getId());

            if (anexo == null) {
                throw new I18NException("anexo.noExiste", String.valueOf(idAnexo));
            }

            FileContentWs fileContentWs = AsientoConverter.transformarFileContentWs(anexo, anexoEjb, entidadActiva, idioma);

            integracionEjb.addIntegracionOk(inicio, RegwebConstantes.INTEGRACION_WS, UsuarioAplicacionCache.get().getMethod().getName(),peticion.toString(), System.currentTimeMillis() - inicio.getTime(), entidadActiva.getId(), numRegFormat);

            return fileContentWs;

        }catch (Exception e){
            e.printStackTrace();
            integracionEjb.addIntegracionError(RegwebConstantes.INTEGRACION_WS, UsuarioAplicacionCache.get().getMethod().getName(), peticion.toString(), e, null,System.currentTimeMillis() - inicio.getTime(), entidadActiva.getId(), numRegFormat);
            throw new I18NException("asientoRegistral.obtener.error", e.getLocalizedMessage());
        }

    }


    /**
     * @param registroEntrada
     * @throws org.fundaciobit.genapp.common.i18n.I18NValidationException
     */
    private void validateRegistroEntrada(RegistroEntrada registroEntrada) throws I18NValidationException, I18NException {
        RegistroEntradaBeanValidator rebv = new RegistroEntradaBeanValidator(registroEntradaValidator);
        rebv.throwValidationExceptionIfErrors(registroEntrada, true);
    }

    /**
     * @param registroSalida
     * @throws org.fundaciobit.genapp.common.i18n.I18NValidationException
     */
    private void validateRegistroSalida(RegistroSalida registroSalida) throws I18NValidationException, I18NException {

        RegistroSalidaBeanValidator rsbv = new RegistroSalidaBeanValidator(registroSalidaValidator);
        rsbv.throwValidationExceptionIfErrors(registroSalida, true);
    }


}
