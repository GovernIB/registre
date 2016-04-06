package es.caib.regweb3.ws.v3.impl;

import es.caib.dir3caib.ws.api.unidad.Dir3CaibObtenerUnidadesWs;
import es.caib.dir3caib.ws.api.unidad.UnidadTF;
import es.caib.regweb3.model.*;
import es.caib.regweb3.persistence.ejb.*;
import es.caib.regweb3.persistence.utils.AnexoFull;
import es.caib.regweb3.persistence.utils.Dir3CaibUtils;
import es.caib.regweb3.persistence.validator.InteresadoBeanValidator;
import es.caib.regweb3.persistence.validator.InteresadoValidator;
import es.caib.regweb3.persistence.validator.RegistroEntradaBeanValidator;
import es.caib.regweb3.persistence.validator.RegistroEntradaValidator;
import es.caib.regweb3.utils.RegwebConstantes;
import es.caib.regweb3.utils.StringUtils;
import es.caib.regweb3.ws.converter.DatosInteresadoConverter;
import es.caib.regweb3.ws.converter.RegistroEntradaConverter;
import es.caib.regweb3.ws.model.IdentificadorWs;
import es.caib.regweb3.ws.model.InteresadoWs;
import es.caib.regweb3.ws.model.RegistroEntradaResponseWs;
import es.caib.regweb3.ws.model.RegistroEntradaWs;
import es.caib.regweb3.ws.utils.UsuarioAplicacionCache;
import org.apache.log4j.Logger;
import org.fundaciobit.genapp.common.i18n.I18NException;
import org.fundaciobit.genapp.common.i18n.I18NValidationException;
import org.fundaciobit.genapp.common.ws.WsI18NException;
import org.fundaciobit.genapp.common.ws.WsValidationException;
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
@Stateless(name = RegWebRegistroEntradaWsImpl.NAME + "Ejb")
@RolesAllowed({ RegwebConstantes.ROL_SUPERADMIN })
@SOAPBinding(style = SOAPBinding.Style.RPC)
@org.apache.cxf.interceptor.InInterceptors(interceptors = { "es.caib.regweb3.ws.utils.RegWebInInterceptor" })
@org.apache.cxf.interceptor.InFaultInterceptors(interceptors = { "es.caib.regweb3.ws.utils.RegWebInInterceptor" })
@WebService(name = RegWebRegistroEntradaWsImpl.NAME_WS, portName = RegWebRegistroEntradaWsImpl.NAME_WS,
        serviceName = RegWebRegistroEntradaWsImpl.NAME_WS  + "Service",
        endpointInterface = "es.caib.regweb3.ws.v3.impl.RegWebRegistroEntradaWs")
@WebContext(contextRoot = "/regweb3/ws", urlPattern = "/v3/" + RegWebRegistroEntradaWsImpl.NAME, transportGuarantee = TransportGuarantee.NONE, secureWSDLAccess = false, authMethod = "WSBASIC")
public class RegWebRegistroEntradaWsImpl extends AbstractRegistroWsImpl 
    implements RegWebRegistroEntradaWs {

    protected final Logger log = Logger.getLogger(getClass());

    public static final String NAME = "RegWebRegistroEntrada";

    public static final String NAME_WS = NAME + "Ws";

    
    RegistroEntradaValidator<RegistroEntrada> registroEntradaValidator = new RegistroEntradaValidator<RegistroEntrada>();

    InteresadoValidator<Interesado> interesadoValidator = new InteresadoValidator<Interesado>();

   
    

    @EJB(mappedName = "regweb3/OficinaEJB/local")
    public OficinaLocal oficinaEjb;

    @EJB(mappedName = "regweb3/OrganismoEJB/local")
    public OrganismoLocal organismoEjb;

    @EJB(mappedName = "regweb3/PermisoLibroUsuarioEJB/local")
    public PermisoLibroUsuarioLocal permisoLibroUsuarioEjb;

    @EJB(mappedName = "regweb3/UsuarioEntidadEJB/local")
    public UsuarioEntidadLocal usuarioEntidadEjb;

    @EJB(mappedName = "regweb3/LibroEJB/local")
    public LibroLocal libroEjb;

    @EJB(mappedName = "regweb3/TipoAsuntoEJB/local")
    public TipoAsuntoLocal tipoAsuntoEjb;

    @EJB(mappedName = "regweb3/CodigoAsuntoEJB/local")
    public CodigoAsuntoLocal codigoAsuntoEjb;

    @EJB(mappedName = "regweb3/RegistroEntradaEJB/local")
    public RegistroEntradaLocal registroEntradaEjb;

    @EJB(mappedName = "regweb3/HistoricoRegistroEntradaEJB/local")
    public HistoricoRegistroEntradaLocal historicoRegistroEntradaEjb;

    @EJB(mappedName = "regweb3/LopdEJB/local")
    public LopdLocal lopdEjb;

    @EJB(mappedName = "regweb3/InteresadoEJB/local")
    public InteresadoLocal interesadoEjb;

    @EJB(mappedName = "regweb3/PersonaEJB/local")
    public PersonaLocal personaEjb;

    @EJB(mappedName = "regweb3/CatPaisEJB/local")
    public CatPaisLocal catPaisEjb;

    @EJB(mappedName = "regweb3/CatProvinciaEJB/local")
    public CatProvinciaLocal catProvinciaEjb;

    @EJB(mappedName = "regweb3/CatLocalidadEJB/local")
    public CatLocalidadLocal catLocalidadEjb;




    @Override
    @RolesAllowed({ ROL_USUARI })
    @WebMethod
    public IdentificadorWs altaRegistroEntrada(@WebParam(name = "registroEntradaWs") 
       RegistroEntradaWs registroEntradaWs) 
           throws Throwable, WsI18NException, WsValidationException {

        IdentificadorWs identificadorWs = null;

        // 1.- Comprobar que el Organismo destino está vigente
        Organismo destinoInterno = organismoEjb.findByCodigo(registroEntradaWs.getDestino());
        UnidadTF destinoExterno = null;

        if (destinoInterno == null) { // Se trata de un destino externo

            // Lo buscamos en DIR3CAIB
            Dir3CaibObtenerUnidadesWs unidadesService = Dir3CaibUtils.getObtenerUnidadesService();
            destinoExterno = unidadesService.obtenerUnidad(registroEntradaWs.getDestino(), null, null);

            if (destinoExterno == null) {
                throw new I18NException("registro.organismo.noExiste", registroEntradaWs.getDestino());
            } else if (!destinoExterno.getCodigoEstadoEntidad().equals(RegwebConstantes.ESTADO_ENTIDAD_VIGENTE)) {
                throw new I18NException("registro.organismo.extinguido", destinoExterno.getCodigo() + " - " + destinoExterno.getDenominacion());
            }


        } else if (!destinoInterno.getEstado().getCodigoEstadoEntidad().equals(ESTADO_ENTIDAD_VIGENTE)) { //Si está extinguido
            throw new I18NException("registro.organismo.extinguido", destinoInterno.getNombreCompleto());
        }

        // 2.- Comprobar que la Oficina está vigente
        Oficina oficina = oficinaEjb.findByCodigo(registroEntradaWs.getOficina());

        if(oficina == null){ //No existe
            throw new I18NException("registro.oficina.noExiste", registroEntradaWs.getOficina());

        }else if(!oficina.getEstado().getCodigoEstadoEntidad().equals(ESTADO_ENTIDAD_VIGENTE)){ //Si está extinguido
            throw new I18NException("registro.oficina.extinguido", oficina.getNombreCompleto());
        }

        // 3.- Comprobar que el Libro está vigente
        Libro libro = libroEjb.findByCodigoEntidad(registroEntradaWs.getLibro(), oficina.getOrganismoResponsable().getEntidad().getId());

        if(libro == null){ //No existe
            throw new I18NException("registro.libro.noExiste", registroEntradaWs.getLibro());

        }else if(!libro.getActivo()){ //Si está extinguido
            throw new I18NException("registro.libro.inactivo", registroEntradaWs.getLibro());
        }

        // 4.- Comprobar que el usuario tiene permisos para realizar el registro de entrada
        // Nos pueden enviar el username en mayusculas
        UsuarioEntidad usuario = usuarioEntidadEjb.findByIdentificadorEntidad(registroEntradaWs.getCodigoUsuario(), oficina.getOrganismoResponsable().getEntidad().getId());
        
        
        if(usuario == null){//No existe
            throw new I18NException("registro.usuario.noExiste", registroEntradaWs.getCodigoUsuario(), oficina.getOrganismoResponsable().getEntidad().getNombre());

        } else if(!permisoLibroUsuarioEjb.tienePermiso(usuario.getId(), libro.getId(), PERMISO_REGISTRO_ENTRADA)){
            throw new I18NException("registro.usuario.permisos", registroEntradaWs.getCodigoUsuario(), libro.getCodigo());

        }
        
        // Recuperamos el username correcto 
        registroEntradaWs.setCodigoUsuario(usuario.getUsuario().getIdentificador());

        // 5.- Convertir RegistroEntradaWs a RegistroEntrada
        RegistroEntrada registroEntrada = RegistroEntradaConverter.getRegistroEntrada(
                registroEntradaWs, usuario, libro, oficina, destinoInterno, destinoExterno,
             codigoAsuntoEjb, tipoAsuntoEjb);

        // 6.- Validar el RegistroEntrada
        validateRegistroEntrada(registroEntrada);

        // 7.- Validar los Interesados
        if(registroEntradaWs.getInteresados() != null && registroEntradaWs.getInteresados().size() > 0){

            // Procesamos los interesados
            List<Interesado> interesados  = procesarInteresados(registroEntradaWs.getInteresados());

            // Asociamos los Interesados al Registro de Entrada
            registroEntrada.getRegistroDetalle().setInteresados(interesados);

        }else{
            throw new I18NException("interesado.registro.obligatorio");
        }

        // 8.- Validar los Anexos
        List<AnexoFull> anexosFull = null;
        if(registroEntradaWs.getAnexos() != null && registroEntradaWs.getAnexos().size() > 0){

          // /Procesamos los anexos
          anexosFull = procesarAnexos(registroEntradaWs.getAnexos(), usuario.getEntidad().getId());


          //Asociamos los anexos al Registro de Entrada
          registroEntrada.getRegistroDetalle().setAnexos(null);
        }

        // 7.- Creamos el Registro de Entrada
        registroEntrada = registroEntradaEjb.registrarEntrada(registroEntrada, usuario, anexosFull);

        if(registroEntrada.getId() != null){
            // 8.- Creamos el Historico RegistroEntrada
            historicoRegistroEntradaEjb.crearHistoricoRegistroEntrada(registroEntrada, usuario, RegwebConstantes.TIPO_MODIF_ALTA,false);

            // Componemos la respuesta
            identificadorWs = new IdentificadorWs();
            identificadorWs.setFecha(registroEntrada.getFecha());
            identificadorWs.setNumero(registroEntrada.getNumeroRegistro());
            identificadorWs.setNumeroRegistroFormateado(registroEntrada.getNumeroRegistroFormateado());
        }

        return identificadorWs;
    }

    @RolesAllowed({ ROL_USUARI })
    @Override
    @WebMethod
    public void anularRegistroEntrada(
        @WebParam(name = "numeroRegistroFormateado")String numeroRegistroFormateado,
        @WebParam(name = "usuario") String usuario,
        @WebParam(name = "entidad") String entidad,
        @WebParam(name = "anular") boolean anular)
    throws Throwable, WsI18NException, WsValidationException {

        // 1.- Validaciones comunes
        validarObligatorios(numeroRegistroFormateado, usuario, entidad);

        // 2.- Comprobar que el usuario existe en la Entidad proporcionada
        UsuarioEntidad usuarioEntidad = usuarioEntidadEjb.findByIdentificadorCodigoEntidad(usuario, entidad);

        if(usuarioEntidad == null){//No existe
            throw new I18NException("registroEntrada.usuario.noExiste", usuario, entidad);
        }

        // 3.- Obtenemos el RegistroEntrada
        RegistroEntrada registroEntrada = registroEntradaEjb.findByNumeroRegistroFormateado(numeroRegistroFormateado);

        if(registroEntrada == null){
            throw new I18NException("registroEntrada.noExiste", numeroRegistroFormateado);
        }

        // 4.- Comprobamos si el RegistroEntrada se puede anular según su estado.
        final List<Long> estados = new ArrayList<Long>();
        estados.add(RegwebConstantes.ESTADO_PENDIENTE);
        estados.add(RegwebConstantes.ESTADO_VALIDO);
        estados.add(RegwebConstantes.ESTADO_PENDIENTE_VISAR);

        if(!estados.contains(registroEntrada.getEstado())){
            throw new I18NException("registroEntrada.anulado");
        }

        // 5.- Comprobamos que el usuario tiene permisos de modificación para el RegistroEntrada
        if(!permisoLibroUsuarioEjb.tienePermiso(usuarioEntidad.getId(), registroEntrada.getLibro().getId(), PERMISO_MODIFICACION_REGISTRO_ENTRADA)){
            throw new I18NException("registroEntrada.usuario.permisos", usuario);
        }

        // 6.- Anulamos el RegistroEntrada
        // TODO Falta enviar boolean anular
        registroEntradaEjb.anularRegistroEntrada(registroEntrada,usuarioEntidad);

    }

    @RolesAllowed({ ROL_USUARI })
    @Override
    @WebMethod
    public void tramitarRegistroEntrada(@WebParam(name = "numeroRegistroFormateado")String numeroRegistroFormateado, @WebParam(name = "usuario") String usuario, @WebParam(name = "entidad") String entidad) throws Throwable, WsI18NException, WsValidationException {

        // 1.- Validaciones comunes
        validarObligatorios(numeroRegistroFormateado, usuario, entidad);

        // 2.- Comprobar que el usuario existe en la Entidad proporcionada
        UsuarioEntidad usuarioEntidad = usuarioEntidadEjb.findByIdentificadorCodigoEntidad(usuario, entidad);

        if(usuarioEntidad == null){//No existe
            throw new I18NException("registroEntrada.usuario.noExiste", usuario, entidad);
        }

        // 3.- Obtenemos el RegistroEntrada
        RegistroEntrada registroEntrada = registroEntradaEjb.findByNumeroRegistroFormateado(numeroRegistroFormateado);

        if(registroEntrada == null){
            throw new I18NException("registroEntrada.noExiste", numeroRegistroFormateado);
        }

        // 4.- Comprobamos si el RegistroEntrada tiene el estado Válido
        if(!registroEntrada.getEstado().equals(RegwebConstantes.ESTADO_VALIDO)){
            throw new I18NException("registroEntrada.tramitar.error");
        }

        // 5.- Comprobamos que el Organismo destino pertenece a la misma administración
        if(!registroEntrada.getOficina().getOrganismoResponsable().equals(registroEntrada.getDestino())){
            throw new I18NException("registroEntrada.tramitar.error");
        }

        // 6.- Tramitamos el RegistroEntrada
        registroEntradaEjb.tramitarRegistroEntrada(registroEntrada, usuarioEntidad);

    }
    
    
    
    /**
     * 
     * @param anyo
     * @param numeroRegistro
     * @param libro
     * @param usuario
     * @param entidad
     * @return
     * @throws Throwable
     * @throws WsI18NException
     */
    @RolesAllowed({ ROL_USUARI })
    @Override
    @WebMethod
    public IdentificadorWs obtenerRegistroEntradaID(
         @WebParam(name = "anyo")int anyo,
         @WebParam(name = "numeroRegistro")int numeroRegistro,
         @WebParam(name = "libro")String libro,
         @WebParam(name = "usuario")String usuario,
         @WebParam(name = "entidad")String entidad) 
          throws Throwable, WsI18NException {
      
      
      // 1.- Validaciones comunes
      if(anyo <= 0){
        throw new I18NException("error.valor.requerido.ws", "anyo");
      }
      
      if(numeroRegistro <= 0){
        throw new I18NException("error.valor.requerido.ws", "numeroRegistro");
      }
      
      if(StringUtils.isEmpty(libro)){
        throw new I18NException("error.valor.requerido.ws", "libro");
      }
      
      if(StringUtils.isEmpty(usuario)){
        throw new I18NException("error.valor.requerido.ws", "usuario");
      }
  
      if(StringUtils.isEmpty(entidad)){
          throw new I18NException("error.valor.requerido.ws", "entidad");
      }


      // 2.- Comprobar que el usuario existe en la Entidad proporcionada
      UsuarioEntidad usuarioEntidad = usuarioEntidadEjb.findByIdentificadorCodigoEntidad(usuario, entidad);

      if(usuarioEntidad == null){//No existe
          throw new I18NException("registroEntrada.usuario.noExiste", usuario, entidad);
      }
      
      // 3.- Existe libro
      Libro libroObj = libroEjb.findByCodigoEntidad(libro,usuarioEntidad.getEntidad().getId());
      if (libroObj == null) {
        throw new I18NException("registro.libro.noExiste", libro);
      }

      // 4.- Comprobamos que el usuario tiene permisos de lectura para el RegistroEntrada
      if(!permisoLibroUsuarioEjb.tienePermiso(usuarioEntidad.getId(), libroObj.getId(), PERMISO_CONSULTA_REGISTRO_ENTRADA)){
        throw new I18NException("registroEntrada.usuario.permisos", usuario);
      }

      // 3.- Obtenemos el registro
      RegistroEntrada  registro;
      registro = registroEntradaEjb.findByNumeroAnyoLibro(numeroRegistro, anyo, libro);
      if(registro == null){
        throw new I18NException("registroEntrada.noExiste", numeroRegistro 
            + "/" + anyo + " (" + libro + ")");
      }
      
      // LOPD
        lopdEjb.insertarRegistroEntrada(registro.getNumeroRegistro(), registro.getFecha(), registro.getLibro().getId(), usuarioEntidad.getId());

      IdentificadorWs id = new IdentificadorWs();
      id.setFecha(registro.getFecha());
      id.setNumero(numeroRegistro);
      id.setNumeroRegistroFormateado(registro.getNumeroRegistroFormateado());
      
      return id;

    }
    
    
    

    @RolesAllowed({ ROL_USUARI })
    @Override
    @WebMethod
    public RegistroEntradaResponseWs obtenerRegistroEntrada(
        @WebParam(name = "numeroRegistroFormateado")String numeroRegistroFormateado,
        @WebParam(name = "usuario") String usuario,
        @WebParam(name = "entidad") String entidad) 
    throws Throwable, WsI18NException, WsValidationException {

        // 1.- Validaciones comunes
        validarObligatorios(numeroRegistroFormateado, usuario, entidad);

        // 2.- Comprobar que el usuario existe en la Entidad proporcionada
        UsuarioEntidad usuarioEntidad = usuarioEntidadEjb.findByIdentificadorCodigoEntidad(usuario, entidad);

        if(usuarioEntidad == null){//No existe
            throw new I18NException("registroEntrada.usuario.noExiste", usuario, entidad);
        }

        // 3.- Obtenemos el RegistroEntrada
        RegistroEntrada registro = registroEntradaEjb.findByNumeroRegistroFormateado(numeroRegistroFormateado);

        if (registro == null) {
            throw new I18NException("registroEntrada.noExiste", numeroRegistroFormateado);
        }

        // 4.- Comprobamos que el usuario tiene permisos de lectura para el RegistroEntrada
        if (!permisoLibroUsuarioEjb.tienePermiso(usuarioEntidad.getId(), registro.getLibro().getId(), PERMISO_CONSULTA_REGISTRO_ENTRADA)) {
            throw new I18NException("registroEntrada.usuario.permisos", usuario);
        }

        // LOPD
        lopdEjb.insertarRegistroEntrada(registro.getNumeroRegistro(), registro.getFecha(), registro.getLibro().getId(), usuarioEntidad.getId());

        // Retornamos el RegistroEntradaResponseWs
        return RegistroEntradaConverter.getRegistroEntradaResponseWs(registro,
            UsuarioAplicacionCache.get().getIdioma(), anexoEjb);


    }

    /**
     *
     * @param registroEntrada
     * @throws org.fundaciobit.genapp.common.i18n.I18NValidationException
     */
    private void validateRegistroEntrada(RegistroEntrada registroEntrada) throws I18NValidationException, I18NException {
      RegistroEntradaBeanValidator rebv = new RegistroEntradaBeanValidator(registroEntradaValidator);
      rebv.throwValidationExceptionIfErrors(registroEntrada, true);
    }

    /**
     *
     * @param interesado
     * @throws org.fundaciobit.genapp.common.i18n.I18NValidationException
     */
    private void validateInteresado(Interesado interesado) throws I18NValidationException, I18NException {
      InteresadoBeanValidator ibv = new InteresadoBeanValidator(interesadoValidator, interesadoEjb, personaEjb, catPaisEjb);
      ibv.throwValidationExceptionIfErrors(interesado, true);
    }



    /**
     * Procesa los Interesados recibidos
     * @param interesadosWs
     * @return
     * @throws Exception
     * @throws I18NValidationException
     * @throws I18NException
     */
    private List<Interesado> procesarInteresados(List<InteresadoWs> interesadosWs)
        throws Exception, I18NValidationException, I18NException {

        List<Interesado> interesados  = new ArrayList<Interesado>();

        for (InteresadoWs interesadoWs : interesadosWs) {

            Interesado interesado = DatosInteresadoConverter.getInteresado(
                interesadoWs.getInteresado(), 
                catPaisEjb, catProvinciaEjb, catLocalidadEjb);

            validateInteresado(interesado);

            if (interesadoWs.getRepresentante() == null){ // Interesado sin Representante

                // Guardamos el Interesado
                interesado = interesadoEjb.persist(interesado);

                // Lo añadimos al listado
                interesados.add(interesado);

            }else{// Interesado con Representante

                Interesado representante = DatosInteresadoConverter.getInteresado(
                    interesadoWs.getRepresentante(), 
                    catPaisEjb,catProvinciaEjb,catLocalidadEjb);
                validateInteresado(representante);

                representante.setIsRepresentante(true);

                // Guardamos el Interesado
                interesado = interesadoEjb.persist(interesado);

                // Guardamos el Representante
                representante.setRepresentado(interesado);
                representante = interesadoEjb.persist(representante);

                // Asignamos el Representante al Interesado y lo actualizamos
                interesado.setRepresentante(representante);
                interesado = interesadoEjb.merge(interesado);

                // Los añadimos al listado
                interesados.add(interesado);
                interesados.add(representante);

            }

        }

        return interesados;

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
        if(StringUtils.isEmpty(numeroRegistro)){
            throw new I18NException("error.valor.requerido.ws", "identificador");
        }

        if(StringUtils.isEmpty(usuario)){
            throw new I18NException("error.valor.requerido.ws", "usuario");
        }

        if(StringUtils.isEmpty(entidad)){
            throw new I18NException("error.valor.requerido.ws", "entidad");
        }

    }
}
