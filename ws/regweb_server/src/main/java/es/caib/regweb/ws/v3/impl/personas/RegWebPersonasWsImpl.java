package es.caib.regweb.ws.v3.impl.personas;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;

import org.apache.log4j.Logger;
import org.fundaciobit.genapp.common.i18n.I18NArgumentCode;
import org.fundaciobit.genapp.common.i18n.I18NException;
import org.fundaciobit.genapp.common.i18n.I18NValidationException;
import org.fundaciobit.genapp.common.ws.WsI18NException;
import org.fundaciobit.genapp.common.ws.WsValidationException;
import org.jboss.ejb3.annotation.SecurityDomain;
import org.jboss.wsf.spi.annotation.TransportGuarantee;
import org.jboss.wsf.spi.annotation.WebContext;

import es.caib.regweb.model.Entidad;
import es.caib.regweb.model.Persona;
import es.caib.regweb.persistence.ejb.CatComunidadAutonomaLocal;
import es.caib.regweb.persistence.ejb.CatLocalidadLocal;
import es.caib.regweb.persistence.ejb.CatPaisLocal;
import es.caib.regweb.persistence.ejb.CatProvinciaLocal;
import es.caib.regweb.persistence.ejb.EntidadLocal;
import es.caib.regweb.persistence.ejb.PersonaLocal;
import es.caib.regweb.persistence.validator.PersonaBeanValidator;
import es.caib.regweb.persistence.validator.PersonaValidator;
import es.caib.regweb.utils.CompileConstants;
import es.caib.regweb.utils.RegwebConstantes;
import es.caib.regweb.ws.utils.AuthenticatedBaseWsImpl;
import es.caib.regweb.ws.utils.UsuarioAplicacionCache;
import es.caib.regweb.ws.utils.UsuarioInfo;

/**
 *
 * @author anadal
 *
 */
@SecurityDomain(RegwebConstantes.SECURITY_DOMAIN)
@Stateless(name = RegWebPersonasWsImpl.NAME + "Ejb")
@RolesAllowed({ RegwebConstantes.ROL_SUPERADMIN })
@SOAPBinding(style = SOAPBinding.Style.RPC)
@org.apache.cxf.interceptor.InInterceptors(interceptors = { "es.caib.regweb.ws.utils.RegWebInInterceptor" })
@org.apache.cxf.interceptor.InFaultInterceptors(interceptors = { "es.caib.regweb.ws.utils.RegWebInInterceptor" })
@WebService(name = RegWebPersonasWsImpl.NAME_WS, portName = RegWebPersonasWsImpl.NAME_WS,
    serviceName = RegWebPersonasWsImpl.NAME_WS  + "Service",
    endpointInterface = "es.caib.regweb.ws.v3.impl.personas.RegWebPersonasWs")
@WebContext(contextRoot = "/regweb/ws", urlPattern = "/v3/" + RegWebPersonasWsImpl.NAME, transportGuarantee = TransportGuarantee.NONE, secureWSDLAccess = false, authMethod = CompileConstants.AUTH_METHOD)
public class RegWebPersonasWsImpl  extends AuthenticatedBaseWsImpl implements RegWebPersonasWs {

  protected final Logger log = Logger.getLogger(getClass());

  public static final String NAME = "RegWebPersonas";

  public static final String NAME_WS = NAME + "Ws";

  @EJB(mappedName = "regweb/PersonaEJB/local")
  public PersonaLocal personaEjb;

  @EJB(mappedName = "regweb/EntidadEJB/local")
  public EntidadLocal entidadEjb;

  @EJB(mappedName = "regweb/CatPaisEJB/local")
  public CatPaisLocal catPaisEjb;

  @EJB(mappedName = "regweb/CatComunidadAutonomaEJB/local")
  public CatComunidadAutonomaLocal catComunidadAutonomaEjb;

  @EJB(mappedName = "regweb/CatProvinciaEJB/local")
  public CatProvinciaLocal catProvinciaEjb;

  @EJB(mappedName = "regweb/CatLocalidadEJB/local")
  public CatLocalidadLocal catLocalidadEjb;

  PersonaValidator<Persona> personaValidator = new PersonaValidator<Persona>();
  
  /**
   * 
   * @param persona
   * @throws I18NValidationException
   */
  private void validate(Persona persona) throws I18NValidationException, I18NException {
    
    PersonaBeanValidator ibv = new PersonaBeanValidator(personaValidator, personaEjb, catPaisEjb);
    ibv.throwValidationExceptionIfErrors(persona, true);

  }

  //@Resource
  //private WebServiceContext wsContext;

  @RolesAllowed({ ROL_SUPERADMIN })
  @WebMethod
  @Override
  public List<PersonaWs> listarPersonas(String entidadCodigoDir3) throws Throwable, WsI18NException {

    // Check si pot llegir info d'aquest entitat
    UsuarioInfo usuariApp = UsuarioAplicacionCache.get();
    usuariApp.checkPerteneceAEntidad(entidadCodigoDir3);
    
    Entidad entidad = entidadEjb.findByCodigoDir3(entidadCodigoDir3);
    
    if (entidad == null) {
      throw new I18NException("entidad.noexisteix", entidadCodigoDir3);
    }
    
    Long entidadID = entidad.getId();

    List<Persona> all = personaEjb.getAllbyEntidadTipo(entidadID, null);
    List<PersonaWs> allws = new ArrayList<PersonaWs>(all.size());
    for (Persona persona : all) {
      allws.add(PersonaConverter.getPersonaWs(persona));
    }
    return allws;

  }

  @RolesAllowed({ ROL_SUPERADMIN })
  @WebMethod
  @Override
  public Long crearPersona(@WebParam(name = "personaWs")PersonaWs personaWs) throws Throwable,WsI18NException, WsValidationException {

    if (personaWs == null) {
      return null;
    }

    // Check s'ha definit el camp
    String entidadCodigoDir3 = personaWs.getEntidadDir3ID();
    if (entidadCodigoDir3 == null || entidadCodigoDir3.trim().length() == 0) {
      // No ha definit el camp entidadCodigoDir3
      throw new I18NException("error.valor.requerido.ws",
          new I18NArgumentCode("personaws.entidadCodigoDir3"));
      
      //String msg = "No ha definit el camp entidadCodigoDir3";
      //WsUtils.throwException(msg);
    }

    // Check si pot llegir info d'aquest entitat
    UsuarioInfo usuariApp = UsuarioAplicacionCache.get();
    usuariApp.checkPerteneceAEntidad(entidadCodigoDir3);
    
    // Convertir Persona WS a Bean
    Persona persona = PersonaConverter.getPersona(personaWs, 
        catPaisEjb, catProvinciaEjb, catLocalidadEjb, entidadEjb);

    // Validar    
    validate(persona);
    

    persona = personaEjb.persist(persona);

    return persona.getId();

  }


  @RolesAllowed({ ROL_SUPERADMIN })
  @WebMethod
  @Override
  public void borrarPersona(@WebParam(name = "personaID")Long personaID) throws Throwable, WsI18NException, WsI18NException {
    Persona persona = personaEjb.findById(personaID);
    
    if (persona != null) {
      // Check si pot llegir info de l'entitat de la persona
      UsuarioInfo usuariApp = UsuarioAplicacionCache.get();
      usuariApp.checkPerteneceAEntidad(persona.getEntidad().getCodigoDir3());

      personaEjb.remove(persona);
    }
  }



  @RolesAllowed({ ROL_SUPERADMIN })
  @WebMethod
  @Override
  public void actualizarPersona(@WebParam(name = "personaWs")PersonaWs personaWs) throws Throwable, WsI18NException, WsValidationException {

    if (personaWs == null || personaWs.getId() == null) {
      // No existeix cap persona amb ID = NULL
      throw new I18NException("persona.error.noexisteix", "[NULL]");
    }
    
    Persona persona = personaEjb.findById(personaWs.getId());
    
    if (persona == null) {
      // No existeix cap persona amb ID = {0}
      throw new I18NException("persona.error.noexisteix", String.valueOf(personaWs.getId()));
    }

    // ENTITAT ACTUAL = Check si pot llegir info de l'entitat de la persona
    UsuarioInfo usuariApp = UsuarioAplicacionCache.get();
    usuariApp.checkPerteneceAEntidad(persona.getEntidad().getCodigoDir3());

    // Check s'ha definit el camp
    String entidadCodigoDir3 = personaWs.getEntidadDir3ID();
    if (entidadCodigoDir3 == null || entidadCodigoDir3.trim().length() == 0) {
      // "No ha definit el camp entidadCodigoDir3";
      throw new I18NException("error.valor.requerido.ws",
          new I18NArgumentCode("personaws.entidadCodigoDir3"));
    }
    
    // NOVA ENTITAT = Check si pot llegir info d'aquesta entitat    
    usuariApp.checkPerteneceAEntidad(entidadCodigoDir3);

    // Validar
    validate(persona);
    
    personaEjb.merge(PersonaConverter.getPersona(personaWs, 
        catPaisEjb, catProvinciaEjb, catLocalidadEjb, entidadEjb));

  }


  
}
