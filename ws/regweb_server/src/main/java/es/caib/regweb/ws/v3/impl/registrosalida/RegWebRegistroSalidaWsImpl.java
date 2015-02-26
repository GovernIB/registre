package es.caib.regweb.ws.v3.impl.registrosalida;

import es.caib.regweb.model.*;
import es.caib.regweb.persistence.ejb.*;
import es.caib.regweb.persistence.validator.AnexoBeanValidator;
import es.caib.regweb.persistence.validator.AnexoValidator;
import es.caib.regweb.persistence.validator.InteresadoBeanValidator;
import es.caib.regweb.persistence.validator.InteresadoValidator;
import es.caib.regweb.persistence.validator.RegistroSalidaBeanValidator;
import es.caib.regweb.persistence.validator.RegistroSalidaValidator;
import es.caib.regweb.utils.CompileConstants;
import es.caib.regweb.utils.RegwebConstantes;
import es.caib.regweb.utils.StringUtils;
import es.caib.regweb.ws.converter.AnexoConverter;
import es.caib.regweb.ws.converter.DatosInteresadoConverter;
import es.caib.regweb.ws.converter.RegistroSalidaConverter;
import es.caib.regweb.ws.model.AnexoWs;
import es.caib.regweb.ws.model.IdentificadorWs;
import es.caib.regweb.ws.model.InteresadoWs;
import es.caib.regweb.ws.model.RegistroSalidaWs;
import es.caib.regweb.ws.utils.AuthenticatedBaseWsImpl;

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
 */
@SecurityDomain(RegwebConstantes.SECURITY_DOMAIN)
@Stateless(name = RegWebRegistroSalidaWsImpl.NAME + "Ejb")
@RolesAllowed({ RegwebConstantes.ROL_SUPERADMIN })
@SOAPBinding(style = SOAPBinding.Style.RPC)
@org.apache.cxf.interceptor.InInterceptors(interceptors = { "es.caib.regweb.ws.utils.RegWebInInterceptor" })
@org.apache.cxf.interceptor.InFaultInterceptors(interceptors = { "es.caib.regweb.ws.utils.RegWebInInterceptor" })
@WebService(name = RegWebRegistroSalidaWsImpl.NAME_WS, portName = RegWebRegistroSalidaWsImpl.NAME_WS,
        serviceName = RegWebRegistroSalidaWsImpl.NAME_WS  + "Service",
        endpointInterface = "es.caib.regweb.ws.v3.impl.registrosalida.RegWebRegistroSalidaWs")
@WebContext(contextRoot = "/regweb/ws", urlPattern = "/v3/" + RegWebRegistroSalidaWsImpl.NAME, transportGuarantee = TransportGuarantee.NONE, secureWSDLAccess = false, authMethod = CompileConstants.AUTH_METHOD)
public class RegWebRegistroSalidaWsImpl extends AuthenticatedBaseWsImpl implements RegWebRegistroSalidaWs {

    protected final Logger log = Logger.getLogger(getClass());

    public static final String NAME = "RegWebRegistroSalida";

    public static final String NAME_WS = NAME + "Ws";


    RegistroSalidaValidator<RegistroSalida> validator = new RegistroSalidaValidator<RegistroSalida>();

    
    InteresadoValidator<Interesado> interesadoValidator = new InteresadoValidator<Interesado>();

    
    AnexoValidator<Anexo> anexoValidator = new AnexoValidator<Anexo>();

    @EJB(mappedName = "regweb/OficinaEJB/local")
    public OficinaLocal oficinaEjb;

    @EJB(mappedName = "regweb/OrganismoEJB/local")
    public OrganismoLocal organismoEjb;

    @EJB(mappedName = "regweb/PermisoLibroUsuarioEJB/local")
    public PermisoLibroUsuarioLocal permisoLibroUsuarioEjb;

    @EJB(mappedName = "regweb/UsuarioEntidadEJB/local")
    public UsuarioEntidadLocal usuarioEntidadEjb;

    @EJB(mappedName = "regweb/LibroEJB/local")
    public LibroLocal libroEjb;

    @EJB(mappedName = "regweb/IdiomaRegistroEJB/local")
    public IdiomaRegistroLocal idiomaRegistroEjb;

    @EJB(mappedName = "regweb/TipoAsuntoEJB/local")
    public TipoAsuntoLocal tipoAsuntoEjb;

    @EJB(mappedName = "regweb/CodigoAsuntoEJB/local")
    public CodigoAsuntoLocal codigoAsuntoEjb;

    @EJB(mappedName = "regweb/RegistroSalidaEJB/local")
    public RegistroSalidaLocal registroSalidaEjb;

    @EJB(mappedName = "regweb/HistoricoRegistroSalidaEJB/local")
    public HistoricoRegistroSalidaLocal historicoRegistroSalidaEjb;

    @EJB(mappedName = "regweb/LopdEJB/local")
    public LopdLocal lopdEjb;

    @EJB(mappedName = "regweb/InteresadoEJB/local")
    public InteresadoLocal interesadoEjb;

    @EJB(mappedName = "regweb/PersonaEJB/local")
    public PersonaLocal personaEjb;

    @EJB(mappedName = "regweb/CatPaisEJB/local")
    public CatPaisLocal catPaisEjb;

    @EJB(mappedName = "regweb/CatProvinciaEJB/local")
    public CatProvinciaLocal catProvinciaEjb;

    @EJB(mappedName = "regweb/CatLocalidadEJB/local")
    public CatLocalidadLocal catLocalidadEjb;

    @EJB(mappedName = "regweb/AnexoEJB/local")
    public AnexoLocal anexoEjb;

    @EJB(mappedName = "regweb/TipoDocumentalEJB/local")
    public TipoDocumentalLocal tipoDocumentalEjb;

    @Override
    @RolesAllowed({ ROL_USUARI })
    @WebMethod
    public IdentificadorWs altaRegistroSalida(@WebParam(name = "registroSalidaWs") RegistroSalidaWs registroSalidaWs) throws Throwable, WsI18NException, WsValidationException {

        IdentificadorWs identificadorWs = null;

        // 1.- Comprobar que el Organismo destino está vigente
        Organismo origen = organismoEjb.findByCodigo(registroSalidaWs.getOrigen());

        if(origen == null){ //Si no existe todo: Hay que agregar origenes externos?
            throw new I18NException("registro.organismo.noExiste", registroSalidaWs.getOrigen());

        }else if(!origen.getEstado().getCodigoEstadoEntidad().equals(ESTADO_ENTIDAD_VIGENTE)){ //Si está extinguido
            throw new I18NException("registro.organismo.extinguido", origen.getNombreCompleto());
        }

        // 2.- Comprobar que la Oficina está vigente
        Oficina oficina = oficinaEjb.findByCodigo(registroSalidaWs.getOficina());

        if(oficina == null){ //No existe
            throw new I18NException("registro.oficina.noExiste", registroSalidaWs.getOrigen());

        }else if(!oficina.getEstado().getCodigoEstadoEntidad().equals(ESTADO_ENTIDAD_VIGENTE)){ //Si está extinguido
            throw new I18NException("registro.oficina.extinguido", oficina.getNombreCompleto());
        }

        // 3.- Comprobar que el Libro está vigente
        Libro libro = libroEjb.findByCodigo(registroSalidaWs.getLibro());

        if(libro == null){ //No existe
            throw new I18NException("registro.libro.noExiste", registroSalidaWs.getLibro());

        }else if(!libro.getActivo()){ //Si está extinguido
            throw new I18NException("registro.libro.inactivo", registroSalidaWs.getLibro());
        }

        // 4.- Comprobar que el usuario tiene permisos para realizar el registro de entrada
        UsuarioEntidad usuario = usuarioEntidadEjb.findByIdentificadorEntidad(registroSalidaWs.getCodigoUsuario(), oficina.getOrganismoResponsable().getEntidad().getId());

        if(usuario == null){//No existe
            throw new I18NException("registro.usuario.noExiste", registroSalidaWs.getCodigoUsuario(), oficina.getOrganismoResponsable().getEntidad().getNombre());

        }else if(!permisoLibroUsuarioEjb.tienePermiso(usuario.getId(), libro.getId(), PERMISO_REGISTRO_SALIDA)){
            throw new I18NException("registro.usuario.permisos", registroSalidaWs.getCodigoUsuario(), libro.getCodigo());

        }

        // 5.- Convertir RegistroEntradaWs a RegistroEntrada
        RegistroSalida registroSalida = RegistroSalidaConverter.getRegistroSalida(registroSalidaWs, usuario, libro, oficina, origen,
                idiomaRegistroEjb, codigoAsuntoEjb, tipoAsuntoEjb);

        // 6.- Validar el RegistroEntrada
        validateRegistroSalida(registroSalida);

        // 7.- Validar los Interesados
        if(registroSalidaWs.getInteresados() != null && registroSalidaWs.getInteresados().size() > 0){

            // Procesamos los interesados
            List<Interesado> interesados  = procesarInteresados(registroSalidaWs.getInteresados());

            // Asociamos los Interesados al Registro de Entrada
            registroSalida.getRegistroDetalle().setInteresados(interesados);

        }else{
            throw new I18NException("interesado.registro.obligatorio");
        }

        // 8.- Validar los Anexos
        if(registroSalidaWs.getAnexos() != null && registroSalidaWs.getAnexos().size() > 0){
          // /Procesamos los anexos
          List<Anexo> anexos = procesarAnexos(registroSalidaWs.getAnexos());

          //Asociamos los anexos al Registro de Entrada
          registroSalida.getRegistroDetalle().setAnexos(anexos);
        }

        // 7.- Creamos el Registro de Entrada
        registroSalida = registroSalidaEjb.registrarSalida(registroSalida);

        if(registroSalida.getId() != null){
            // 8.- Creamos el Historico RegistroEntrada
            historicoRegistroSalidaEjb.crearHistoricoRegistroSalida(registroSalida, usuario, RegwebConstantes.TIPO_MODIF_ALTA, false);

            // Componemos la respuesta
            identificadorWs = new IdentificadorWs();
            identificadorWs.setFecha(registroSalida.getFecha());
            identificadorWs.setNumero(registroSalida.getNumeroRegistro());
            identificadorWs.setNumeroRegistroFormateado(registroSalida.getNumeroRegistroFormateado());
        }

        return identificadorWs;
    }

    @RolesAllowed({ ROL_USUARI })
    @Override
    @WebMethod
    public void anularRegistroSalida(@WebParam(name = "numeroRegistro")String numeroRegistro,
        @WebParam(name = "usuario") String usuario, 
        @WebParam(name = "entidad") String entidad,
        @WebParam(name = "anular") boolean anular)
      throws Throwable, WsI18NException, WsValidationException {

        // 1.- Validaciones comunes
        validarObligatorios(numeroRegistro, usuario, entidad);

        // 2.- Comprobar que el usuario existe en la Entidad proporcionada
        UsuarioEntidad usuarioEntidad = usuarioEntidadEjb.findByIdentificadorCodigoEntidad(usuario, entidad);

        if(usuarioEntidad == null){//No existe
            throw new I18NException("registroEntrada.usuario.noExiste", usuario, entidad);
        }

        // 3.- Obtenemos el RegistroSalida
        RegistroSalida registroSalida = registroSalidaEjb.findByNumeroRegistroFormateado(numeroRegistro);

        if(registroSalida == null){
            throw new I18NException("registroEntrada.noExiste", numeroRegistro);
        }

        // 4.- Comprobamos si el RegistroSalida se puede anular según su estado.
        final List<Long> estados = new ArrayList<Long>();
        estados.add(RegwebConstantes.ESTADO_PENDIENTE);
        estados.add(RegwebConstantes.ESTADO_VALIDO);
        estados.add(RegwebConstantes.ESTADO_PENDIENTE_VISAR);

        if(!estados.contains(registroSalida.getEstado())){
            throw new I18NException("registroEntrada.anulado");
        }

        // 5.- Comprobamos que el usuario tiene permisos de modificación para el RegistroSalida
        if(!permisoLibroUsuarioEjb.tienePermiso(usuarioEntidad.getId(), registroSalida.getLibro().getId(), PERMISO_MODIFICACION_REGISTRO_SALIDA)){
            throw new I18NException("registroEntrada.usuario.permisos", usuario);
        }

        // 6.- Anulamos el RegistroSalida
        // TODO Falta Afegir paràmetre
        registroSalidaEjb.anularRegistroSalida(registroSalida, usuarioEntidad);

    }

    @RolesAllowed({ ROL_USUARI })
    @Override
    @WebMethod
    public RegistroSalidaWs obtenerRegistroSalida(@WebParam(name = "numeroRegistro")String numeroRegistro, @WebParam(name = "usuario") String usuario, @WebParam(name = "entidad") String entidad) throws Throwable, WsI18NException, WsValidationException {

        // 1.- Validaciones comunes
        validarObligatorios(numeroRegistro, usuario, entidad);

        // 2.- Comprobar que el usuario existe en la Entidad proporcionada
        UsuarioEntidad usuarioEntidad = usuarioEntidadEjb.findByIdentificadorCodigoEntidad(usuario, entidad);

        if(usuarioEntidad == null){//No existe
            throw new I18NException("registroEntrada.usuario.noExiste", usuario, entidad);
        }

        // 3.- Obtenemos el RegistroSalida
        RegistroSalida registroSalida = registroSalidaEjb.findByNumeroRegistroFormateado(numeroRegistro);

        if(registroSalida == null){
            throw new I18NException("registroEntrada.noExiste", numeroRegistro);
        }

        // 4.- Comprobamos que el usuario tiene permisos de lectura para el RegistroSalida
        if(!permisoLibroUsuarioEjb.tienePermiso(usuarioEntidad.getId(), registroSalida.getLibro().getId(), PERMISO_CONSULTA_REGISTRO_SALIDA)){
            throw new I18NException("registroEntrada.usuario.permisos", usuario);
        }

        // LOPD
        lopdEjb.insertarRegistroSalida(registroSalida.getId(), usuarioEntidad.getId());

        // Retornamos el RegistroSalidaWs
        return RegistroSalidaConverter.getRegistroSalidaWs(registroSalida);


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
    public IdentificadorWs obtenerRegistroSalidaID(
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
      Libro libroObj = libroEjb.findByCodigo(libro);
      if (libroObj == null) {
        throw new I18NException("registro.libro.noExiste", libro);
      }

      // 4.- Comprobamos que el usuario tiene permisos de lectura para el RegistroEntrada
      if(!permisoLibroUsuarioEjb.tienePermiso(usuarioEntidad.getId(), libroObj.getId(), PERMISO_CONSULTA_REGISTRO_ENTRADA)){
        throw new I18NException("registroSalida.usuario.permisos", usuario);
      }

      // 3.- Obtenemos el registro
      RegistroSalida  registro;
      registro = registroSalidaEjb.findByNumeroAnyoLibro(numeroRegistro, anyo, libro);
      if(registro == null){
        throw new I18NException("registroSalida.noExiste", numeroRegistro 
            + "/" + anyo + " (" + libro + ")");
      }
      
      // LOPD
      lopdEjb.insertarRegistroSalida(registro.getId(), usuarioEntidad.getId());

      IdentificadorWs id = new IdentificadorWs();
      id.setFecha(registro.getFecha());
      id.setNumero(numeroRegistro);
      id.setNumeroRegistroFormateado(registro.getNumeroRegistroFormateado());
      
      return id;
    }
    

    /**
     *
     * @param registroSalida
     * @throws org.fundaciobit.genapp.common.i18n.I18NValidationException
     */
    private void validateRegistroSalida(RegistroSalida registroSalida) throws I18NValidationException, I18NException {
       
      RegistroSalidaBeanValidator rsbv = new RegistroSalidaBeanValidator(validator);
      
      rsbv.validate(registroSalida, true);
    }

    /**
     *
     * @param interesado
     * @throws org.fundaciobit.genapp.common.i18n.I18NValidationException
     */
    private void validateInteresado(Interesado interesado) throws I18NValidationException, I18NException {
      
      InteresadoBeanValidator ibv = new InteresadoBeanValidator(interesadoValidator, interesadoEjb, personaEjb, catPaisEjb);
      
      final boolean isNou = true;
      ibv.throwValidationExceptionIfErrors(interesado, isNou);
    }

     /**
     *
     * @param anexo
     * @throws org.fundaciobit.genapp.common.i18n.I18NValidationException
     */
    private void validateAnexo(Anexo anexo, boolean isNou) throws I18NValidationException, I18NException {
        //anexoValidator.validateStandalone(anexo);
      AnexoBeanValidator pfbv = new AnexoBeanValidator(anexoValidator);
      
      //final boolean isNou = true;
      pfbv.throwValidationExceptionIfErrors(anexo, isNou);
    }

    /**
     * Procesa los Interesados recibidos
     * @param interesadosWs
     * @return
     * @throws Exception
     * @throws I18NValidationException
     * @throws I18NException
     */
    private List<Interesado> procesarInteresados(List<InteresadoWs> interesadosWs) throws Exception, I18NValidationException, I18NException {

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
     * Procesa los Anexos recibidos
     * @param anexosWs
     * @return
     * @throws Exception
     * @throws I18NValidationException
     * @throws I18NException
     */
    private List<Anexo> procesarAnexos(List<AnexoWs> anexosWs) throws Exception, I18NValidationException, I18NException {

        List<Anexo> anexos  = new ArrayList<Anexo>();

        for (AnexoWs anexoWs : anexosWs) {
            //Convertimos a anexo
            Anexo anexo = AnexoConverter.getAnexo(anexoWs, tipoDocumentalEjb);

            //validamos el anexo
            validateAnexo(anexo, true);

            // Guardamos el Anexo datos básicos
            anexo = anexoEjb.persist(anexo);
            // Actualizamos el anexo y le asociamos los archivos
            if(ANEXO_TIPOVALIDEZDOCUMENTO_COPIA.equals(anexoWs.getValidezDocumento())){ // Si validez Documento --> Copia no se admite firma
                anexoWs.setNombreFirmaAnexada("");
                anexoWs.setFirmaAnexada(null);
                anexoWs.setTamanoFirmaAnexada(null);
                anexoWs.setTipoMIMEFirmaAnexada(null);
                anexo = anexoEjb.actualizarAnexoConArchivos(anexo.getId(),anexoWs.getFicheroAnexado(), anexoWs.getNombreFicheroAnexado(),anexoWs.getTipoMIMEFicheroAnexado(),
                                             anexoWs.getTamanoFicheroAnexado(), anexoWs.getFirmaAnexada(), anexoWs.getNombreFirmaAnexada(), anexoWs.getTipoMIMEFirmaAnexada(),
                                             anexoWs.getTamanoFirmaAnexada(), anexoWs.getModoFirma(), anexoWs.getFechaCaptura().getTime());
            } else {
                anexo = anexoEjb.actualizarAnexoConArchivos(anexo.getId(),anexoWs.getFicheroAnexado(), anexoWs.getNombreFicheroAnexado(),anexoWs.getTipoMIMEFicheroAnexado(),
                                             anexoWs.getTamanoFicheroAnexado(), anexoWs.getFirmaAnexada(), anexoWs.getNombreFirmaAnexada(), anexoWs.getTipoMIMEFirmaAnexada(),
                                             anexoWs.getTamanoFirmaAnexada(), anexoWs.getModoFirma(), anexoWs.getFechaCaptura().getTime());
            }
            // Lo añadimos al listado
            anexos.add(anexo);

        }

        return anexos;

    }

    /**
     * Valida la obligatoriedad de los campos
     * @param numeroRegistro
     * @param usuario
     * @param entidad
     * @throws org.fundaciobit.genapp.common.i18n.I18NException
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
