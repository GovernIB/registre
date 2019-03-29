package es.caib.regweb3.ws.v3.impl;

import es.caib.dir3caib.ws.api.oficina.OficinaTF;
import es.caib.dir3caib.ws.api.unidad.Dir3CaibObtenerUnidadesWs;
import es.caib.dir3caib.ws.api.unidad.UnidadTF;
import es.caib.regweb3.model.*;
import es.caib.regweb3.model.utils.AnexoFull;
import es.caib.regweb3.persistence.ejb.*;
import es.caib.regweb3.persistence.utils.PropiedadGlobalUtil;
import es.caib.regweb3.persistence.utils.RespuestaDistribucion;
import es.caib.regweb3.persistence.validator.RegistroEntradaBeanValidator;
import es.caib.regweb3.persistence.validator.RegistroEntradaValidator;
import es.caib.regweb3.persistence.validator.RegistroSalidaBeanValidator;
import es.caib.regweb3.persistence.validator.RegistroSalidaValidator;
import es.caib.regweb3.utils.*;
import es.caib.regweb3.ws.converter.AsientoRegistralConverter;
import es.caib.regweb3.ws.model.InteresadoWs;
import es.caib.regweb3.ws.model.JustificanteWs;
import es.caib.regweb3.ws.model.OficioWs;
import es.caib.regweb3.ws.utils.UsuarioAplicacionCache;
import org.apache.commons.io.IOUtils;
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
import java.util.*;

/**
 *
 */
@SecurityDomain(RegwebConstantes.SECURITY_DOMAIN)
@Stateless(name = AsientoRegistralWsImpl.NAME + "Ejb")
@RolesAllowed({RegwebConstantes.ROL_SUPERADMIN})
@SOAPBinding(style = SOAPBinding.Style.RPC)
@org.apache.cxf.interceptor.InInterceptors(interceptors = {"es.caib.regweb3.ws.utils.RegWebInInterceptor"})
@org.apache.cxf.interceptor.InFaultInterceptors(interceptors = {"es.caib.regweb3.ws.utils.RegWebInInterceptor"})
@WebService(name = AsientoRegistralWsImpl.NAME_WS, portName = AsientoRegistralWsImpl.NAME_WS,
        serviceName = AsientoRegistralWsImpl.NAME_WS + "Service",
        endpointInterface = "es.caib.regweb3.ws.v3.impl.AsientoRegistralWs")
@WebContext(contextRoot = "/regweb3/ws", urlPattern = "/v3/" + AsientoRegistralWsImpl.NAME, transportGuarantee = TransportGuarantee.NONE, secureWSDLAccess = false, authMethod = "WSBASIC")
public class AsientoRegistralWsImpl  extends AbstractRegistroWsImpl implements es.caib.regweb3.ws.v3.impl.AsientoRegistralWs {

    protected final Logger log = Logger.getLogger(getClass());

    public static final String NAME = "AsientoRegistral";

    public static final String NAME_WS = NAME + "Ws";

    RegistroSalidaValidator<RegistroSalida> registroSalidaValidator = new RegistroSalidaValidator<RegistroSalida>();
    RegistroEntradaValidator<RegistroEntrada> registroEntradaValidator = new RegistroEntradaValidator<RegistroEntrada>();

    @EJB(mappedName = "regweb3/OrganismoEJB/local")
    private OrganismoLocal organismoEjb;

    @EJB(mappedName = "regweb3/TipoAsuntoEJB/local")
    private TipoAsuntoLocal tipoAsuntoEjb;

    @EJB(mappedName = "regweb3/CodigoAsuntoEJB/local")
    private CodigoAsuntoLocal codigoAsuntoEjb;

    @EJB(mappedName = "regweb3/RegistroEntradaEJB/local")
    private RegistroEntradaLocal registroEntradaEjb;

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

    @EJB(mappedName = "regweb3/IntegracionEJB/local")
    private IntegracionLocal integracionEjb;

    @EJB(mappedName = "regweb3/JustificanteEJB/local")
    private JustificanteLocal justificanteEjb;

    @EJB(mappedName = "regweb3/DistribucionEJB/local")
    private DistribucionLocal distribucionEjb;

    @EJB(mappedName = "regweb3/SirEnvioEJB/local")
    private SirEnvioLocal sirEnvioEjb;

    @EJB(mappedName = "regweb3/OficioRemisionEJB/local")
    private OficioRemisionLocal oficioRemisionEjb;

    @EJB(mappedName = "regweb3/TrazabilidadSirEJB/local")
    private TrazabilidadSirLocal trazabilidadSirEjb;

    @EJB(mappedName = "regweb3/AsientoRegistralEJB/local")
    private AsientoRegistralLocal asientoRegistralEjb;

    @EJB(mappedName = "regweb3/OficioRemisionSalidaUtilsEJB/local")
    private OficioRemisionSalidaUtilsLocal oficioRemisionSalidaUtilsEjb;

    @EJB(mappedName = "regweb3/ModeloOficioRemisionEJB/local")
    private ModeloOficioRemisionLocal modeloOficioRemisionEjb;


    @RolesAllowed({RWE_WS_ENTRADA, ROL_WS_SALIDA})
    @Override
    @WebMethod
    public es.caib.regweb3.ws.model.AsientoRegistralWs crearAsientoRegistral(
       @WebParam(name = "entidad")String entidad,
       @WebParam(name = "asientoRegistral") es.caib.regweb3.ws.model.AsientoRegistralWs asientoRegistral,
       @WebParam(name = "tipoOperacion") Long tipoOperacion)throws Throwable, WsI18NException, WsValidationException{

        log.info("Dentro de crearAsientoRegitralWs");

        // Definimos la petición que se guardá en el monitor de integración
        StringBuilder peticion = new StringBuilder();
        long tiempo = System.currentTimeMillis();
        String numRegFormat = "";

        peticion.append("usuario: ").append(UsuarioAplicacionCache.get().getUsuario().getNombreIdentificador()).append(System.getProperty("line.separator"));

        // 1.-Validar campo obligatorio entidad
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


        // 4.- Comprobar que la Oficina está vigente
        Oficina oficina = null;
        if(REGISTRO_ENTRADA.equals(asientoRegistral.getTipoRegistro())) {
            oficina = oficinaEjb.findByCodigoEntidad(asientoRegistral.getEntidadRegistralDestinoCodigo(), entidadActiva.getId());
        }else if(REGISTRO_SALIDA.equals(asientoRegistral.getTipoRegistro())){
            oficina = oficinaEjb.findByCodigoEntidad(asientoRegistral.getEntidadRegistralOrigenCodigo(), entidadActiva.getId());
        }

        if (oficina == null) { //No existe
            throw new I18NException("registro.oficina.noExiste", asientoRegistral.getEntidadRegistralDestinoCodigo());

        } else if (!oficina.getEstado().getCodigoEstadoEntidad().equals(ESTADO_ENTIDAD_VIGENTE)) { //Si está extinguido
            throw new I18NException("registro.oficina.extinguido", oficina.getNombreCompleto());
        }


        // 5.- Comprobar que el Libro está vigente
        Libro libro = libroEjb.findByCodigoEntidad(asientoRegistral.getLibroCodigo(), entidadActiva.getId());

        if (libro == null) { //No existe
            throw new I18NException("registro.libro.noExiste", asientoRegistral.getLibroCodigo());

        } else if (!libro.getActivo()) { //Si está inactivo
            throw new I18NException("registro.libro.inactivo", asientoRegistral.getLibroCodigo());
        }


        // 6.- Comprobar que el usuario tiene permisos para realizar el registro de entrada
        // Nos pueden enviar el username en mayusculas
        UsuarioEntidad usuario = usuarioEntidadEjb.findByIdentificadorEntidad(asientoRegistral.getCodigoUsuario(), entidadActiva.getId());

        if (usuario == null) {//No existe
            throw new I18NException("registro.usuario.noExiste", asientoRegistral.getCodigoUsuario(), entidadActiva.getNombre());

        } else if (!permisoLibroUsuarioEjb.tienePermiso(usuario.getId(), libro.getId(), PERMISO_REGISTRO_ENTRADA)) {
            throw new I18NException("registro.usuario.permisos", asientoRegistral.getCodigoUsuario(), libro.getCodigo());

        }


        // 7.- Recuperamos el username correcto
        asientoRegistral.setCodigoUsuario(usuario.getUsuario().getIdentificador());
        asientoRegistral.setAplicacion(CODIGO_APLICACION);
        asientoRegistral.setVersion(Versio.VERSIO);


        // 8.- Validar los Interesados
        List<Interesado> interesados;
        if(tipoOperacion!= null && TIPO_OPERACION_COMUNICACION.equals(tipoOperacion)) { //Si es una comunicación solo se permite un interesado
            if (asientoRegistral.getInteresados() != null && asientoRegistral.getInteresados().size() > 0) {

                if (asientoRegistral.getInteresados().size() == 1) {
                    // Procesamos los interesados
                    interesados = procesarInteresados(asientoRegistral.getInteresados(), interesadoEjb, catPaisEjb, catProvinciaEjb, catLocalidadEjb, personaEjb);
                } else {
                    throw new I18NException("interesado.registro.obligatorio.uno");
                }

            } else {
                throw new I18NException("interesado.registro.obligatorio");
            }
        }else{
            if (asientoRegistral.getInteresados() != null && asientoRegistral.getInteresados().size() > 0) {
                // Procesamos los interesados
                interesados = procesarInteresados(asientoRegistral.getInteresados(), interesadoEjb, catPaisEjb, catProvinciaEjb, catLocalidadEjb, personaEjb);

            } else {
                throw new I18NException("interesado.registro.obligatorio");
            }
        }

        // 9.- Validar los Anexos
        List<AnexoFull> anexosFull = null;
        if (asientoRegistral.getAnexos() != null && asientoRegistral.getAnexos().size() > 0) {
            //Procesamos los anexos
            anexosFull = procesarAnexos(asientoRegistral.getAnexos(), entidadActiva.getId());
        }


        // Convertir AsientoRegistralWs a RegistroEntrada
        if(REGISTRO_ENTRADA.equals(asientoRegistral.getTipoRegistro())){

            // 10.- Comprobar que el Organismo destino está vigente
            Organismo destinoInterno = organismoEjb.findByCodigoEntidad(asientoRegistral.getUnidadTramitacionDestinoCodigo(), entidadActiva.getId());
            UnidadTF destinoExterno = null;

            if (destinoInterno == null) { // Se trata de un destino externo

                // Lo buscamos en DIR3CAIB
                Dir3CaibObtenerUnidadesWs unidadesService = Dir3CaibUtils.getObtenerUnidadesService(PropiedadGlobalUtil.getDir3CaibServer(), PropiedadGlobalUtil.getDir3CaibUsername(), PropiedadGlobalUtil.getDir3CaibPassword());
                destinoExterno = unidadesService.obtenerUnidad(asientoRegistral.getUnidadTramitacionDestinoCodigo(), null, null);

                if (destinoExterno == null) {
                    throw new I18NException("registro.organismo.noExiste", asientoRegistral.getUnidadTramitacionDestinoCodigo());
                } else if (!destinoExterno.getCodigoEstadoEntidad().equals(ESTADO_ENTIDAD_VIGENTE)) {
                    throw new I18NException("registro.organismo.extinguido", destinoExterno.getCodigo() + " - " + destinoExterno.getDenominacion());
                }


            } else if (!destinoInterno.getEstado().getCodigoEstadoEntidad().equals(ESTADO_ENTIDAD_VIGENTE)) { //Si está extinguido
                throw new I18NException("registro.organismo.extinguido", destinoInterno.getNombreCompleto());
            }


            // 11.- Convertimos a Registro de Entrada
            RegistroEntrada registroEntrada = AsientoRegistralConverter.getRegistroEntrada(
               asientoRegistral, usuario, libro, oficina, destinoInterno, destinoExterno,
               codigoAsuntoEjb, tipoAsuntoEjb);

            // 12.- Validar el RegistroEntrada
            validateRegistroEntrada(registroEntrada);


            //13.- Asignamos interesados
            registroEntrada.getRegistroDetalle().setInteresados(null);


            //14.- Asignamos los anexos al Registro de Entrada
            //registroEntrada.getRegistroDetalle().setAnexos(null);


            // 15.- Creamos el Registro de Entrada
            try{
                registroEntrada = registroEntradaEjb.registrarEntrada(registroEntrada, usuario, interesados, anexosFull);
                numRegFormat = registroEntrada.getNumeroRegistroFormateado();
                asientoRegistral.setNumeroRegistro(registroEntrada.getNumeroRegistro());
                asientoRegistral.setNumeroRegistroFormateado(numRegFormat);
                asientoRegistral.setFechaRegistro(registroEntrada.getFecha());


            }catch (Exception e){
                integracionEjb.addIntegracionError(RegwebConstantes.INTEGRACION_WS, UsuarioAplicacionCache.get().getMethod().getName(), peticion.toString(), e, null,System.currentTimeMillis() - tiempo, entidadActiva.getId(), numRegFormat);
                throw new I18NException("registro.nuevo.error");
            }


            // 14.- Integracion
            peticion.append("oficina: ").append(registroEntrada.getOficina().getDenominacion()).append(System.getProperty("line.separator"));
            peticion.append("registro: ").append(registroEntrada.getNumeroRegistroFormateado()).append(System.getProperty("line.separator"));
            integracionEjb.addIntegracionOk(RegwebConstantes.INTEGRACION_WS, UsuarioAplicacionCache.get().getMethod().getName(),peticion.toString(), System.currentTimeMillis() - tiempo, entidadActiva.getId(), numRegFormat);



        }else if(REGISTRO_SALIDA.equals(asientoRegistral.getTipoRegistro())){


                // 10.- Comprobar que el Organismo Origen está vigente
                Organismo origen = organismoEjb.findByCodigoEntidad(asientoRegistral.getUnidadTramitacionOrigenCodigo(), entidadActiva.getId());

                if (origen == null) { //Si no existe todo: Hay que agregar origenes externos?
                    throw new I18NException("registro.organismo.noExiste", asientoRegistral.getUnidadTramitacionOrigenCodigo());

                } else if (!origen.getEstado().getCodigoEstadoEntidad().equals(ESTADO_ENTIDAD_VIGENTE)) { //Si está extinguido
                    throw new I18NException("registro.organismo.extinguido", origen.getNombreCompleto());
                }

                //11.- Convertir a registro de Salida
                RegistroSalida registroSalida = AsientoRegistralConverter.getRegistroSalida(
                   asientoRegistral, usuario, libro, oficina, origen,
                   codigoAsuntoEjb, tipoAsuntoEjb);

                // 12.- Validar el RegistroSalida
                validateRegistroSalida(registroSalida);


                // 13.- Asignamos los interesados
                registroSalida.getRegistroDetalle().setInteresados(null);

                // 14.- Asignamos los anexos
                //registroSalida.getRegistroDetalle().setAnexos(null);


                // 15.- Creamos el Registro de Salida
                try {
                    registroSalida = asientoRegistralEjb.registrarSalida(registroSalida, usuario, interesados, anexosFull);

                    numRegFormat = registroSalida.getNumeroRegistroFormateado();
                    asientoRegistral.setNumeroRegistro(registroSalida.getNumeroRegistro());
                    asientoRegistral.setNumeroRegistroFormateado(numRegFormat);
                    asientoRegistral.setFechaRegistro(registroSalida.getFecha());


                } catch (Exception e) {
                    integracionEjb.addIntegracionError(RegwebConstantes.INTEGRACION_WS, UsuarioAplicacionCache.get().getMethod().getName(), peticion.toString(), e, null, System.currentTimeMillis() - tiempo, entidadActiva.getId(), numRegFormat);
                    throw new I18NException("registro.nuevo.error");
                }


                // Integracion
                peticion.append("oficina: ").append(registroSalida.getOficina().getDenominacion()).append(System.getProperty("line.separator"));
                peticion.append("registro: ").append(registroSalida.getNumeroRegistroFormateado()).append(System.getProperty("line.separator"));
                integracionEjb.addIntegracionOk(RegwebConstantes.INTEGRACION_WS, UsuarioAplicacionCache.get().getMethod().getName(), peticion.toString(), System.currentTimeMillis() - tiempo, entidadActiva.getId(), numRegFormat);

                try{
                    if(tipoOperacion!= null && tipoOperacion.equals(TIPO_OPERACION_NOTIFICACION)) { //Si es una notificación
                        //Creamos el justificante y actualizamos los estados del registroSalida y del Asiento Registral
                        justificanteEstadoAsientoRegistral(registroSalida,asientoRegistral, REGISTRO_DISTRIBUIDO);

                    }else if(tipoOperacion!= null && tipoOperacion.equals(TIPO_OPERACION_COMUNICACION)){
                        //Si es una comunicación dependerá de los interesados destinatarios (solo hay un interesado)
                        for(InteresadoWs interesadoWs: asientoRegistral.getInteresados()){
                            //Si el interesado es una persona física o jurídica es como el caso de notificación
                            if(TIPO_INTERESADO_PERSONA_FISICA.equals(interesadoWs.getInteresado().getTipoInteresado())
                               || TIPO_INTERESADO_PERSONA_JURIDICA.equals(interesadoWs.getInteresado().getTipoInteresado())){
                                //Creamos el justificante y actualizamos los estados del registroSalida y del Asiento Registral
                                justificanteEstadoAsientoRegistral(registroSalida,asientoRegistral,REGISTRO_DISTRIBUIDO);

                            }else if(TIPO_INTERESADO_ADMINISTRACION.equals(interesadoWs.getInteresado().getTipoInteresado())){//Si el interesado es una administración
                                //Obtenemos las oficinas SIR a las que va dirigido el registro de Salida
                                List<OficinaTF> oficinasSIR = oficioRemisionSalidaUtilsEjb.isOficioRemisionSir(registroSalida,getOrganismosOficioRemisionSalida(organismoEjb.getByOficinaActiva(oficina)));
                                //Si el interesado es una administración y no está integrada en SIR
                                if(oficinasSIR.isEmpty()){ //Si no hay oficinas SIR, se marca como oficio externo y el identificador intercambio se marca a -1
                                    //TODO hay que crear el oficio externo???
                                    //Creamos el justificante y actualizamos los estados del registroSalida y del Asiento Registral
                                    justificanteEstadoAsientoRegistral(registroSalida,asientoRegistral, REGISTRO_OFICIO_EXTERNO);
                                    asientoRegistral.setIdentificadorIntercambio("-1");
                                }else{ //Si el interesado es una administración y está integrada en SIR
                                    try {
                                        //Se envia el registro via SIR.
                                        OficioRemision oficioRemision = sirEnvioEjb.enviarFicheroIntercambio(REGISTRO_SALIDA_ESCRITO, registroSalida.getId(),
                                           registroSalida.getOficina(), registroSalida.getUsuario(), oficinasSIR.get(0).getCodigo());
                                        registroSalidaEjb.cambiarEstado(registroSalida.getId(), REGISTRO_OFICIO_SIR);
                                        asientoRegistral.setEstado(REGISTRO_OFICIO_SIR);
                                        asientoRegistral.setIdentificadorIntercambio(oficioRemision.getIdentificadorIntercambio());
                                    }catch (Exception e){
                                        throw new I18NException("registroSir.error.envio");
                                    }

                                }

                            }
                        }
                    }
                }catch (Exception e){
                    integracionEjb.addIntegracionError(RegwebConstantes.INTEGRACION_WS, UsuarioAplicacionCache.get().getMethod().getName(), peticion.toString(), e, null,System.currentTimeMillis() - tiempo, entidadActiva.getId(), numRegFormat);
                    throw new I18NException("registro.nuevo.error");
                }
        }

        return asientoRegistral;
    }




    @RolesAllowed({RWE_WS_ENTRADA, ROL_WS_SALIDA})
    @Override
    @WebMethod
    public es.caib.regweb3.ws.model.AsientoRegistralWs obtenerAsientoRegistral(
       @WebParam(name = "entidad") String entidad,
       @WebParam(name = "numeroRegistroFormateado") String numeroRegistroFormateado,
       @WebParam(name = "libro") String libro,
       @WebParam(name = "tipoRegistro") Long tipoRegistro,
       @WebParam(name = "conAnexos") boolean conAnexos) throws  Throwable, WsI18NException, WsValidationException{


        StringBuilder peticion = new StringBuilder();
        long tiempo = System.currentTimeMillis();
        peticion.append("usuario: ").append(UsuarioAplicacionCache.get().getUsuario().getNombreIdentificador()).append(System.getProperty("line.separator"));

        //1.- Validar obligatorios
        validarObligatorios(numeroRegistroFormateado,entidad,libro);

        peticion.append("registro: ").append(numeroRegistroFormateado).append(System.getProperty("line.separator"));

        Entidad entidadActiva = entidadEjb.findByCodigoDir3(entidad);
        // 2.- Comprobar que la entidad existe y está activa
        if(entidadActiva == null){
            log.info("La entidad "+entidad+" no existe.");
            throw new I18NException("registro.entidad.noExiste", entidad);
        }else if(!entidadActiva.getActivo()){
            throw new I18NException("registro.entidad.inactiva", entidad);
        }

        // 3.- Comprobar que el usuario existe en la Entidad proporcionada
        if (!UsuarioAplicacionCache.get().getEntidades().contains(entidadActiva)) {
            log.info("El usuario "+UsuarioAplicacionCache.get().getUsuario().getNombreCompleto()+" no pertenece a la entidad " + entidadActiva.getNombre());
            throw new I18NException("registroEntrada.usuario.noExiste", entidad);
        }

        // 4.- Obtenemos el usuario entidad
        UsuarioEntidad usuarioEntidad = usuarioEntidadEjb.findByIdentificadorCodigoEntidad( UsuarioAplicacionCache.get().getUsuario().getIdentificador(), entidad);

        if(usuarioEntidad == null){//No existe
            throw new I18NException("registroEntrada.usuario.noExiste", UsuarioAplicacionCache.get().getUsuario().getIdentificador(), entidad);
        }


        es.caib.regweb3.ws.model.AsientoRegistralWs asientoRegistral = null;


        if(REGISTRO_ENTRADA.equals(tipoRegistro)){
            // 5.- Obtenemos el RegistroEntrada
            RegistroEntrada registro = registroEntradaEjb.findByNumeroRegistroFormateado(entidad, numeroRegistroFormateado, null);
            if (registro == null) {
                throw new I18NException("registroEntrada.noExiste", numeroRegistroFormateado);
            }

            // 6.- Comprobamos que el usuario tiene permisos de lectura para el RegistroEntrada
            if (!permisoLibroUsuarioEjb.tienePermiso(usuarioEntidad.getId(), registro.getLibro().getId(), PERMISO_CONSULTA_REGISTRO_ENTRADA)) {
                throw new I18NException("registroEntrada.usuario.permisos", usuarioEntidad.getUsuario().getNombreCompleto());
            }


            // 7.- Retornamos el Asiento Registral
            try{
                if(conAnexos) {
                    asientoRegistral = AsientoRegistralConverter.getAsientoRegistralBeanConAnexos(registro,
                       UsuarioAplicacionCache.get().getIdioma(),oficioRemisionEjb, anexoEjb, trazabilidadSirEjb);
                }else{
                    asientoRegistral = AsientoRegistralConverter.getAsientoRegistralBean(registro,
                       UsuarioAplicacionCache.get().getIdioma(),oficioRemisionEjb, trazabilidadSirEjb);
                }
            }catch (Exception e){

                integracionEjb.addIntegracionError(RegwebConstantes.INTEGRACION_WS, UsuarioAplicacionCache.get().getMethod().getName(), peticion.toString(), e, null,System.currentTimeMillis() - tiempo, entidadActiva.getId(), numeroRegistroFormateado);
                throw new I18NException("registro.obtener.error");
            }


            // 8.- LOPD
            lopdEjb.altaLopd(registro.getNumeroRegistro(), registro.getFecha(), registro.getLibro().getId(), usuarioEntidad.getId(), RegwebConstantes.REGISTRO_ENTRADA, RegwebConstantes.LOPD_CONSULTA);
        }else if(REGISTRO_SALIDA.equals(tipoRegistro)){
            // 5.- Obtenemos el RegistroSalida
            RegistroSalida registro = registroSalidaEjb.findByNumeroRegistroFormateado(entidad, numeroRegistroFormateado, null);

            if (registro == null) {
                throw new I18NException("registroEntrada.noExiste", numeroRegistroFormateado);
            }

            // 6.- Comprobamos que el usuario tiene permisos de lectura para el RegistroSalida
            if (!permisoLibroUsuarioEjb.tienePermiso(usuarioEntidad.getId(), registro.getLibro().getId(), PERMISO_CONSULTA_REGISTRO_SALIDA)) {
                throw new I18NException("registroSalida.usuario.permisos", usuarioEntidad.getUsuario().getNombreCompleto());
            }

            // 7.- Retornamos el AsientoRegistral
            try{
                if(conAnexos) {
                    asientoRegistral = AsientoRegistralConverter.getAsientoRegistralBeanConAnexos(registro,
                       UsuarioAplicacionCache.get().getIdioma(),oficioRemisionEjb,anexoEjb, trazabilidadSirEjb);
                }else{
                    asientoRegistral = AsientoRegistralConverter.getAsientoRegistralBean(registro,
                       UsuarioAplicacionCache.get().getIdioma(),oficioRemisionEjb, trazabilidadSirEjb);
                }
            }catch (Exception e){

                integracionEjb.addIntegracionError(RegwebConstantes.INTEGRACION_WS, UsuarioAplicacionCache.get().getMethod().getName(), peticion.toString(), e, null,System.currentTimeMillis() - tiempo, entidadActiva.getId(), numeroRegistroFormateado);
                throw new I18NException("registro.obtener.error");
            }

            // 8.- LOPD
            lopdEjb.altaLopd(registro.getNumeroRegistro(), registro.getFecha(), registro.getLibro().getId(), usuarioEntidad.getId(), RegwebConstantes.REGISTRO_SALIDA, RegwebConstantes.LOPD_CONSULTA);
        }

        // 9.- Integracion
        integracionEjb.addIntegracionOk(RegwebConstantes.INTEGRACION_WS, UsuarioAplicacionCache.get().getMethod().getName(),peticion.toString(), System.currentTimeMillis() - tiempo, entidadActiva.getId(), numeroRegistroFormateado);

        return asientoRegistral;

    }

    @RolesAllowed({RWE_WS_ENTRADA, ROL_WS_SALIDA})
    @Override
    @WebMethod
    public JustificanteWs obtenerJustificante(
       @WebParam(name = "entidad") String entidad,
       @WebParam(name = "numeroRegistroFormateado")String numeroRegistroFormateado,
       @WebParam(name = "libro") String libro,
       @WebParam(name = "tipoRegistro") Long tipoRegistro) throws Throwable, WsI18NException, WsValidationException{


        StringBuilder peticion = new StringBuilder();
        long tiempo = System.currentTimeMillis();
        peticion.append("usuario: ").append(UsuarioAplicacionCache.get().getUsuario().getNombreIdentificador()).append(System.getProperty("line.separator"));

        //1.- Validar obligatorios
        validarObligatorios(numeroRegistroFormateado,entidad,libro);

        peticion.append("registro: ").append(numeroRegistroFormateado).append(System.getProperty("line.separator"));
        if(REGISTRO_ENTRADA.equals(tipoRegistro)) {
            peticion.append("tipoRegistro: ").append("entrada").append(System.getProperty("line.separator"));
        }else if(REGISTRO_SALIDA.equals(tipoRegistro)){
            peticion.append("tipoRegistro: ").append("salida").append(System.getProperty("line.separator"));
        }

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


        AnexoFull justificante = null;
        SignatureCustody sc = null;
        if(REGISTRO_ENTRADA.equals(tipoRegistro)){
            // 4.- Obtenemos el RegistroEntrada
            RegistroEntrada registroEntrada = registroEntradaEjb.findByNumeroRegistroFormateadoConAnexos(entidad, numeroRegistroFormateado, null);

            if (registroEntrada == null) {
                throw new I18NException("registroEntrada.noExiste", numeroRegistroFormateado);
            }



            // Si no tiene Justificante, lo generamos
            if(!registroEntrada.getRegistroDetalle().getTieneJustificante()){

                // Permisos para Modificar el RegistroEntrada?
                if (!permisoLibroUsuarioEjb.tienePermiso(usuario.getId(), registroEntrada.getLibro().getId(), PERMISO_MODIFICACION_REGISTRO_ENTRADA)) {
                    throw new I18NException("registroEntrada.usuario.permisos", usuario.getNombreCompleto());
                }

                // Solo se puede generar si el registro es Válido
                if(registroEntrada.getEstado().equals(REGISTRO_VALIDO)){

                    try{
                        justificante = justificanteEjb.crearJustificante(usuario,registroEntrada,RegwebConstantes.REGISTRO_ENTRADA_ESCRITO.toLowerCase(), Configuracio.getDefaultLanguage());
                    }catch (I18NException e){
                        log.info("----------------Error generado justificante via WS------------------");
                        integracionEjb.addIntegracionError(RegwebConstantes.INTEGRACION_WS, UsuarioAplicacionCache.get().getMethod().getName(), peticion.toString(), e, null,System.currentTimeMillis() - tiempo, entidadActiva.getId(), numeroRegistroFormateado);
                        throw new I18NException("registro.justificante.error", numeroRegistroFormateado);
                    }

                    sc = anexoEjb.descargarFirmaDesdeUrlValidacion(justificante.getAnexo().getCustodiaID(), true, entidadActiva.getId());
                }else{
                    throw new I18NException("registro.justificante.valido");
                }


            }else{ // Tiene Justificante, lo obtenemos

                // Permisos para Consultar el RegistroEntrada?
                if (!permisoLibroUsuarioEjb.tienePermiso(usuario.getId(), registroEntrada.getLibro().getId(), PERMISO_CONSULTA_REGISTRO_ENTRADA)) {
                    throw new I18NException("registroEntrada.usuario.permisos", usuario.getNombreCompleto());
                }

                // Obtenemos el Justificante
                try{
                    justificante = anexoEjb.getAnexoFullLigero(anexoEjb.getIdJustificante(registroEntrada.getRegistroDetalle().getId()), entidadActiva.getId());
                    sc = anexoEjb.descargarFirmaDesdeUrlValidacion(justificante.getAnexo().getCustodiaID(), true, entidadActiva.getId());
                }catch (Exception e){
                    integracionEjb.addIntegracionError(RegwebConstantes.INTEGRACION_WS, UsuarioAplicacionCache.get().getMethod().getName(), peticion.toString(), e, null,System.currentTimeMillis() - tiempo, entidadActiva.getId(), numeroRegistroFormateado);
                    throw new I18NException("registro.justificante.error", numeroRegistroFormateado);
                }

            }

            // Integracion
            integracionEjb.addIntegracionOk(RegwebConstantes.INTEGRACION_WS, UsuarioAplicacionCache.get().getMethod().getName(),peticion.toString(), System.currentTimeMillis() - tiempo, entidadActiva.getId(), numeroRegistroFormateado);

            // Alta en la tabla de LOPD
            lopdEjb.altaLopd(registroEntrada.getNumeroRegistro(), registroEntrada.getFecha(), registroEntrada.getLibro().getId(), usuario.getId(), RegwebConstantes.REGISTRO_ENTRADA, RegwebConstantes.LOPD_JUSTIFICANTE);
        } else if(REGISTRO_SALIDA.equals(tipoRegistro)){

            // 4.- Obtenemos el RegistroSalida
            RegistroSalida registroSalida = registroSalidaEjb.findByNumeroRegistroFormateadoConAnexos(entidad, numeroRegistroFormateado,null);

            if (registroSalida == null) {
                throw new I18NException("registroEntrada.noExiste", numeroRegistroFormateado);
            }

            // Si no tiene Justificante, lo generamos
            if(!registroSalida.getRegistroDetalle().getTieneJustificante()){

                // Permisos para Modificar el RegistroSalida?
                if (!permisoLibroUsuarioEjb.tienePermiso(usuario.getId(), registroSalida.getLibro().getId(), PERMISO_MODIFICACION_REGISTRO_SALIDA)) {
                    throw new I18NException("registroEntrada.usuario.permisos", usuario.getNombreCompleto());
                }

                // Solo se puede generar si el registro es Válido
                if(registroSalida.getEstado().equals(REGISTRO_VALIDO)) {

                    try{
                        justificante = justificanteEjb.crearJustificante(usuario,registroSalida,RegwebConstantes.REGISTRO_SALIDA_ESCRITO.toLowerCase(),Configuracio.getDefaultLanguage());
                    }catch (I18NException e){
                        integracionEjb.addIntegracionError(RegwebConstantes.INTEGRACION_WS, UsuarioAplicacionCache.get().getMethod().getName(), peticion.toString(), e, null,System.currentTimeMillis() - tiempo, entidadActiva.getId(), numeroRegistroFormateado);
                        throw new I18NException("registro.justificante.error", numeroRegistroFormateado);
                    }

                    sc = anexoEjb.descargarFirmaDesdeUrlValidacion(justificante.getAnexo().getCustodiaID(), true, entidadActiva.getId());
                }else{
                    throw new I18NException("registro.justificante.valido");
                }

            }else{ // Tiene Justificante, lo obtenemos

                // Permisos para Consultar el RegistroSalida?
                if (!permisoLibroUsuarioEjb.tienePermiso(usuario.getId(), registroSalida.getLibro().getId(), PERMISO_CONSULTA_REGISTRO_SALIDA)) {
                    throw new I18NException("registroEntrada.usuario.permisos", usuario.getNombreCompleto());
                }

                // Obtenemos el Justificante
                try{
                    justificante = anexoEjb.getAnexoFullLigero(anexoEjb.getIdJustificante(registroSalida.getRegistroDetalle().getId()), entidadActiva.getId());
                    sc = anexoEjb.descargarFirmaDesdeUrlValidacion(justificante.getAnexo().getCustodiaID(), true, entidadActiva.getId());
                }catch (Exception e){
                    integracionEjb.addIntegracionError(RegwebConstantes.INTEGRACION_WS, UsuarioAplicacionCache.get().getMethod().getName(), peticion.toString(), e, null,System.currentTimeMillis() - tiempo, entidadActiva.getId(), numeroRegistroFormateado);
                    throw new I18NException("registro.justificante.error", numeroRegistroFormateado);
                }

            }

            // Integracion
            integracionEjb.addIntegracionOk(RegwebConstantes.INTEGRACION_WS, UsuarioAplicacionCache.get().getMethod().getName(),peticion.toString(), System.currentTimeMillis() - tiempo, entidadActiva.getId(), numeroRegistroFormateado);

            // Alta en la tabla de LOPD
            lopdEjb.altaLopd(registroSalida.getNumeroRegistro(), registroSalida.getFecha(), registroSalida.getLibro().getId(), usuario.getId(), RegwebConstantes.REGISTRO_SALIDA, RegwebConstantes.LOPD_JUSTIFICANTE);

        }


        return new JustificanteWs(sc.getData());

    }


    @RolesAllowed({RWE_WS_ENTRADA, ROL_WS_SALIDA})
    @Override
    @WebMethod
    public void distribuirAsientoRegistral(
       @WebParam(name = "entidad") String entidad,
       @WebParam(name = "numeroRegistroFormateado") String numeroRegistroFormateado,
       @WebParam(name = "libro") String libro) throws Throwable, WsI18NException, WsValidationException{

        //1.- Validar obligatorios
        validarObligatorios(numeroRegistroFormateado,entidad,libro);

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
            RespuestaDistribucion respuestaDistribucion = distribucionEjb.distribuir(registroEntrada, usuario, false);

            // Si el Plugin permite seleccionar Destinatarios, no se puede distribuir automaticamente
            if(respuestaDistribucion.getDestinatarios() != null){
                throw new I18NException("registroEntrada.distribuir.destinatarios");
            }

            if(respuestaDistribucion.getHayPlugin() && !respuestaDistribucion.getListadoDestinatariosModificable()){// Si no es modificable,
                if(!respuestaDistribucion.getEnviadoCola()  && !respuestaDistribucion.getEnviado()){ //Cuando hay plugin y no ha llegado a destino

                    throw new I18NException(("registroEntrada.distribuir.error.noEnviado"));
                }
            }

        }catch (Exception e){
            throw new I18NException("registroEntrada.distribuir.error");
        }

    }

    /**
     * Obtiene el documento generado del oficioExterno del asiento indicado por numeroRegistroFormateado.
     * Este asiento debe formar parte de un Oficio Externo.
     * @param entidad
     * @param numeroRegistroFormateado
     * @param libro
     * @return
     * @throws Throwable
     * @throws WsI18NException
     * @throws WsValidationException
     */
    @RolesAllowed({RWE_WS_ENTRADA, ROL_WS_SALIDA})
    @Override
    @WebMethod
    public OficioWs obtenerOficioExterno(
       @WebParam(name = "entidad") String entidad,
       @WebParam(name = "numeroRegistroFormateado")String numeroRegistroFormateado,
       @WebParam(name = "libro") String libro) throws Throwable, WsI18NException, WsValidationException{

        validarObligatorios(numeroRegistroFormateado,entidad,libro);

        //Averiguamos si existe un oficio de remisión para el número de registro indicado.
        OficioRemision oficio = oficioRemisionEjb.getByNumeroRegistroFormateado(numeroRegistroFormateado,entidad,libro);
        if(oficio == null){
            throw new I18NException("oficio.noExiste", numeroRegistroFormateado);
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
        List<ModeloOficioRemision> modelos = modeloOficioRemisionEjb.getByEntidad(entidadEjb.findByCodigoDir3(entidad).getId());


        if(modelos.size()>0) {// Si la entidad tiene modelo de remisión
            //Obtenemos el modelo asociado
            ModeloOficioRemision modeloOficioRemision = modeloOficioRemisionEjb.findById( modelos.get(0).getId());
            //Generamos el documento del oficio de remisión

            return new OficioWs(IOUtils.toByteArray(oficioRemisionEjb.generarOficioRemisionRtf(oficio, modeloOficioRemision, registrosEntrada, registrosSalida)));

        }else{
            throw new Exception("La entidad no tiene ningún modelo de oficio de remisión y no se ha podido generar.");
        }

    }

    @RolesAllowed({ROL_WS_CIUDADANO})
    @Override
    @WebMethod
    public List<es.caib.regweb3.ws.model.AsientoRegistralWs> obtenerRegistrosCiudadano(@WebParam(name = "entidad") String entidad,  @WebParam(name = "documento") String documento) throws Throwable, WsI18NException, WsValidationException{

        // Definimos la petición que se guardá en el monitor de integración
        StringBuilder peticion = new StringBuilder();
        long tiempo = System.currentTimeMillis();
        String numRegFormat = "";

        peticion.append("usuario: ").append(UsuarioAplicacionCache.get().getUsuario().getNombreIdentificador()).append(System.getProperty("line.separator"));

        // 1.- Validar campo obligatorio entidad
        if(StringUtils.isEmpty(entidad)){
            throw new I18NException("error.valor.requerido.ws", "entidad");
        }

        // 2.- Validar campo obligatorio documento
        if(StringUtils.isEmpty(documento)){
            throw new I18NException("error.valor.requerido.ws", "documento");
        }

        peticion.append("documento: ").append(documento).append(System.getProperty("line.separator"));

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
            log.info("El usuario " + UsuarioAplicacionCache.get().getUsuario().getNombreCompleto() + " no pertenece a la entidad.");
            throw new I18NException("registro.entidad.noExiste", entidad);
        }

        List<es.caib.regweb3.ws.model.AsientoRegistralWs> asientos = new ArrayList<es.caib.regweb3.ws.model.AsientoRegistralWs>();

        try{

            List<RegistroEntrada> entradas = registroEntradaEjb.getByDocumento(entidadActiva.getId(),documento);
            List<RegistroSalida> salidas = registroSalidaEjb.getByDocumento(entidadActiva.getId(),documento);


            for (RegistroEntrada entrada : entradas) {

                asientos.add(AsientoRegistralConverter.getAsientoRegistralBean(entrada,
                        UsuarioAplicacionCache.get().getIdioma(),oficioRemisionEjb, trazabilidadSirEjb));

            }

            for (RegistroSalida salida : salidas) {

                asientos.add(AsientoRegistralConverter.getAsientoRegistralBean(salida,
                        UsuarioAplicacionCache.get().getIdioma(),oficioRemisionEjb, trazabilidadSirEjb));

            }

            log.info("Asientos totales: " + asientos.size());

            // Alta en la tabla de LOPD
//            lopdEjb.altaLopd(registroSalida.getNumeroRegistro(), registroSalida.getFecha(), registroSalida.getLibro().getId(), usuario.getId(), RegwebConstantes.REGISTRO_SALIDA, RegwebConstantes.LOPD_JUSTIFICANTE);

            integracionEjb.addIntegracionOk(RegwebConstantes.INTEGRACION_WS, UsuarioAplicacionCache.get().getMethod().getName(),peticion.toString(), System.currentTimeMillis() - tiempo, entidadActiva.getId(), numRegFormat);

        }catch (Exception e){
            integracionEjb.addIntegracionError(RegwebConstantes.INTEGRACION_WS, UsuarioAplicacionCache.get().getMethod().getName(), peticion.toString(), e, null,System.currentTimeMillis() - tiempo, entidadActiva.getId(), numRegFormat);
            throw new I18NException("error.ws.general");
        }


        return asientos;
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



    /**
     * Transforma un conjunto de organismos a un conjunto de strings con los códigos de los organismos
     *
     * @return
     * @throws Exception
     */
   private Set<String> getOrganismosOficioRemisionSalida(Set<Organismo> organismos) throws Exception {

        // Creamos un Set solo con los codigos
        Set<String> organismosCodigo = new HashSet<String>();

        for (Organismo organismo : organismos) {
            organismosCodigo.add(organismo.getCodigo());

        }
        return organismosCodigo;
    }


    /*
      Método que crea el justiifcante del asiento registral y actualizar el estado del registro de salida y del asiento registral
     */
    private void justificanteEstadoAsientoRegistral(RegistroSalida registroSalida, es.caib.regweb3.ws.model.AsientoRegistralWs asientoRegistral, Long estado) throws Exception, I18NValidationException, I18NException {
        //Crear Justificante
        justificanteEjb.crearJustificante(registroSalida.getUsuario(),registroSalida,RegwebConstantes.REGISTRO_SALIDA_ESCRITO_CASTELLANO,"ca" );
        //Marcar como distribuido
        registroSalidaEjb.cambiarEstado(registroSalida.getId(),estado);
        //Actualizar datos asiento registral
        asientoRegistral.setEstado(estado);
    }


}
