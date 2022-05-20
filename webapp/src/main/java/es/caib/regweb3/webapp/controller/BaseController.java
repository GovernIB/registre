package es.caib.regweb3.webapp.controller;

import es.caib.regweb3.model.*;
import es.caib.regweb3.persistence.ejb.*;
import es.caib.regweb3.utils.Configuracio;
import es.caib.regweb3.utils.RegwebConstantes;
import es.caib.regweb3.webapp.security.LoginInfo;
import org.fundaciobit.genapp.common.web.i18n.I18NUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.ejb.EJB;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Fundació BIT.
 *
 * @author earrivi
 * Date: 16/01/14
 */
public class BaseController {

    protected final Logger log = LoggerFactory.getLogger(getClass());

    @EJB(mappedName = PermisoOrganismoUsuarioLocal.JNDI_NAME)
    public PermisoOrganismoUsuarioLocal permisoOrganismoUsuarioEjb;

    @EJB(mappedName = UsuarioEntidadLocal.JNDI_NAME)
    public UsuarioEntidadLocal usuarioEntidadEjb;

    @EJB(mappedName = OficinaLocal.JNDI_NAME)
    public OficinaLocal oficinaEjb;

    @EJB(mappedName = EntidadLocal.JNDI_NAME)
    public EntidadLocal entidadEjb;

    @EJB(mappedName = TipoDocumentalLocal.JNDI_NAME)
    public TipoDocumentalLocal tipoDocumentalEjb;

    @EJB(mappedName = RegistroSalidaLocal.JNDI_NAME)
    public RegistroSalidaLocal registroSalidaEjb;

    @EJB(mappedName = RegistroEntradaLocal.JNDI_NAME)
    public RegistroEntradaLocal registroEntradaEjb;

    @EJB(mappedName = RegistroEntradaConsultaLocal.JNDI_NAME)
    public RegistroEntradaConsultaLocal registroEntradaConsultaEjb;

    @EJB(mappedName = RegistroSalidaConsultaLocal.JNDI_NAME)
    public RegistroSalidaConsultaLocal registroSalidaConsultaEjb;

    @EJB(mappedName = OrganismoLocal.JNDI_NAME)
    public OrganismoLocal organismoEjb;

    @EJB(mappedName = NotificacionLocal.JNDI_NAME)
    public NotificacionLocal notificacionEjb;


    /**
     * Retorna la información del UsuarioAutenticado
     * @param request
     * @return
     */
    protected LoginInfo getLoginInfo(HttpServletRequest request){

        HttpSession session = request.getSession();

        return (LoginInfo) session.getAttribute(RegwebConstantes.SESSION_LOGIN_INFO);

    }

    /**
     * Retorna el mensaje traducido según el idioma del usuario
     * @param key
     * @return
     */
    protected String getMessage(String key, String... args){
      return I18NUtils.tradueix(key, args);
    }

    /**
     * Retorna el usuario autenticado en la sesión
     * @param request
     * @return
     */
    protected Usuario getUsuarioAutenticado(HttpServletRequest request){

        return getLoginInfo(request).getUsuarioAutenticado();

    }

    /**
     * Retorna el UsuarioEntidad activo, a partir del UsuarioAutenticado y la EntidadActiva.
     * @param request
     * @return
     */
    protected UsuarioEntidad getUsuarioEntidadActivo(HttpServletRequest request) throws Exception{

        return getLoginInfo(request).getUsuarioEntidadActivo();
    }

    /**
     * Retorna el Rol activo del usuario autenticado
     * @param request
     * @return
     */
    protected Rol getRolActivo(HttpServletRequest request){

        return getLoginInfo(request).getRolActivo();
    }

    /**
     * Devuelve true o false en función de si el ROl Activo es ADMIN
     * @param request
     * @return
     */
    protected Boolean isAdminEntidad(HttpServletRequest request){

        return getRolActivo(request).getNombre().equals(RegwebConstantes.RWE_ADMIN);
    }

    /**
     * Devuelve true o false en función de si el ROl Activo es SUPERADMIN
     * @param request
     * @return
     */
    protected Boolean isSuperAdmin(HttpServletRequest request){

        return getRolActivo(request).getNombre().equals(RegwebConstantes.RWE_SUPERADMIN);
    }

    /**
     * Devuelve true o false en función de si el ROl Activo es OPERADOR
     * @param request
     * @return
     */
    protected Boolean isOperador(HttpServletRequest request){

        return getRolActivo(request).getNombre().equals(RegwebConstantes.RWE_USUARI);
    }


    /**
     * Retorna los Roles que tiene asociados el usuario autenticado
     * @param request
     * @return
     */
    @SuppressWarnings(value = "unchecked")
    protected List<Rol> getRolesAutenticado(HttpServletRequest request){

        return getLoginInfo(request).getRolesAutenticado();

    }

    /**
     * Retorna la Entidad activa del usuario autenticado
     * @param request
     * @return
     */
    protected Entidad getEntidadActiva(HttpServletRequest request){

        return getLoginInfo(request).getEntidadActiva();

    }

    /**
     * Actualiza la Entidad activa en la sesion
     * @param request
     * @return
     */
    protected void setEntidadActiva(Entidad entidad, HttpServletRequest request){

        getLoginInfo(request).setEntidadActiva(entidad);

    }

    /**
     * Comprueba si una Entidad está marcada como Sir
     * @param request
     * @return
     * @throws Exception
     */
    protected Boolean isSir(HttpServletRequest request) throws Exception{

        return entidadEjb.isSir(getEntidadActiva(request).getId());
    }

    /**
     * Retorna las Entidades que tiene asociadas el usuario autenticado
     * @param request
     * @return
     */
    @SuppressWarnings(value = "unchecked")
    protected List<Entidad> getEntidadesAutenticado(HttpServletRequest request){

        return getLoginInfo(request).getEntidades();
    }

    /**
     * Retorna la Oficina activa del usuario autenticado
     * @param request
     * @return
     */
    protected Oficina getOficinaActiva(HttpServletRequest request){

        return getLoginInfo(request).getOficinaActiva();
    }

    /**
     * Retorna el Libro de la EntidadActiva
     * @param request
     * @return
     * @throws Exception
     */
    protected Libro getLibroEntidad(HttpServletRequest request) throws Exception {
        return getLoginInfo(request).getEntidadActiva().getLibro();
    }


    /**
     * Retorna las Oficinas a las que el Usuario autenticado tiene acceso
     * @param request
     * @return
     * @throws Exception
     */
    protected LinkedHashSet<Oficina> getOficinasAcceso(HttpServletRequest request) throws Exception {
        return getLoginInfo(request).getOficinasAcceso();
    }

    /**
     * Retorna las Oficinas a las que el Usuario autenticado puede consultar registros de entrada
     * @param request
     * @return
     * @throws Exception
     */
    protected LinkedHashSet<Oficina> getOficinasConsultaEntrada(HttpServletRequest request) throws Exception {
        return getLoginInfo(request).getOficinasConsultaEntrada();
    }

    /**
     * Retorna las Oficinas de un Organismo a las que el Usuario autenticado puede consultar registros de entrada
     * @param request
     * @param idOrganismo
     * @return
     * @throws Exception
     */
    protected LinkedHashSet<Oficina> getOficinasConsultaEntrada(HttpServletRequest request, Long idOrganismo) throws Exception {
        LinkedHashSet<Oficina> oficinas = getLoginInfo(request).getOficinasConsultaEntrada();
        LinkedHashSet<Oficina> oficinasOrganismo = new LinkedHashSet<>();

        Organismo organismo = organismoEjb.findByIdLigero(idOrganismo);

        // Solo almacenamos las que dependen del Organismo
        for(Oficina oficina:oficinas){
            if(oficina.getOrganismoResponsable().equals(organismo)){
                oficinasOrganismo.add(oficina);
            }
        }

        return oficinasOrganismo;
    }

    /**
     * Retorna los Organismos a las que el Usuario autenticado puede consultar registros de entrada
     * @param request
     * @return
     * @throws Exception
     */
    protected List<Organismo> getOrganismosConsultaEntrada(HttpServletRequest request) throws Exception {
        return getLoginInfo(request).getOrganismosConsultaEntrada();
    }

    /**
     * Retorna los Organismos a las que el Usuario autenticado puede consultar registros de entrada
     * @param request
     * @return
     * @throws Exception
     */
    protected List<Long> getOrganismosConsultaEntradaId(HttpServletRequest request) throws Exception {
        List<Organismo> organismos = getOrganismosConsultaEntrada(request);
        List<Long> organismosId = new ArrayList<>();

        for(Organismo organismo:organismos){
            organismosId.add(organismo.getId());
        }
        return organismosId;
    }

    /**
     * Retorna las Oficinas a las que el Usuario autenticado puede consultar registros de salida
     * @param request
     * @return
     * @throws Exception
     */
    protected LinkedHashSet<Oficina> getOficinasConsultaSalida(HttpServletRequest request) throws Exception {
        return getLoginInfo(request).getOficinasConsultaSalida();
    }

    /**
     * Retorna las Oficinas de un Organismo a las que el Usuario autenticado puede consultar registros de salida
     * @param request
     * @param idOrganismo
     * @return
     * @throws Exception
     */
    protected LinkedHashSet<Oficina> getOficinasConsultaSalida(HttpServletRequest request, Long idOrganismo) throws Exception {
        LinkedHashSet<Oficina> oficinas = getLoginInfo(request).getOficinasConsultaSalida();
        LinkedHashSet<Oficina> oficinasOrganismo = new LinkedHashSet<>();

        Organismo organismo = organismoEjb.findByIdLigero(idOrganismo);

        // Solo almacenamos las que dependen del Organismo
        for(Oficina oficina:oficinas){
            if(oficina.getOrganismoResponsable().equals(organismo)){
                oficinasOrganismo.add(oficina);
            }
        }

        return oficinasOrganismo;
    }

    /**
     * Retorna los Organismos a las que el Usuario autenticado puede consultar registros de salida
     * @param request
     * @return
     * @throws Exception
     */
    protected List<Organismo> getOrganismosConsultaSalida(HttpServletRequest request) throws Exception {
        return getLoginInfo(request).getOrganismosConsultaSalida();
    }

    /**
     * Retorna los Organismos a las que el Usuario autenticado puede consultar registros de salida
     * @param request
     * @return
     * @throws Exception
     */
    protected List<Long> getOrganismosConsultaSalidaId(HttpServletRequest request) throws Exception {
        List<Organismo> organismos = getOrganismosConsultaSalida(request);
        List<Long> organismosId = new ArrayList<>();

        for(Organismo organismo:organismos){
            organismosId.add(organismo.getId());
        }
        return organismosId;
    }

    /**
     * Retorna las Oficinas a las que el Usuario autenticado es responsable
     * @param request
     * @return
     * @throws Exception
     */
    protected LinkedHashSet<Oficina> getOficinasResponsable(HttpServletRequest request) throws Exception {
        return getLoginInfo(request).getOficinasResponsable();
    }

    /**
     * Obtiene los Organismos del UsuarioActivo de los que es responsable
     * @param request
     * @return
     * @throws Exception
     */
    protected  List<Organismo> getOrganismosResponsable(HttpServletRequest request) throws Exception{

        UsuarioEntidad usuarioEntidad = getUsuarioEntidadActivo(request);
        return permisoOrganismoUsuarioEjb.getOrganismosAdministrados(usuarioEntidad.getId());
    }

    /**
     * Obtiene los Organismos de la OficinaActiva en los que puede registrar,
     * sin generar OficioRemisión
     * @param request
     * @return
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public LinkedHashSet<Organismo> getOrganismosOficinaActiva(HttpServletRequest request) throws Exception {

        return getLoginInfo(request).getOrganismosOficinaActiva();
    }


    /**
     *  Obtiene todas las oficinas de la entidad activa vigentes
     * @param request
     * @return
     * @throws Exception
     */
    public Set<Oficina> getOficinasOrigen(HttpServletRequest request ) throws  Exception {

        return new HashSet<Oficina>(oficinaEjb.findByEntidadByEstado(getEntidadActiva(request).getId(), RegwebConstantes.ESTADO_ENTIDAD_VIGENTE));
    }


    /**
     *  Obtiene todas las oficinas de la entidad activa vigentes
     * @param request
     * @return
     * @throws Exception
     */
    public Set<Oficina> getOficinasOrigenMultiEntidad(HttpServletRequest request ) throws  Exception {

        return new HashSet<Oficina>(oficinaEjb.findByEntidadByEstadoMultiEntidad(getEntidadActiva(request).getId(), RegwebConstantes.ESTADO_ENTIDAD_VIGENTE));
    }

    /**
     * Retorna los años para un select de búsqueda
     *
     * @return
     */
    protected List<Integer> getAnys() {
        List<Integer> anys = new ArrayList<Integer>();

        Date hoy = new Date();
        SimpleDateFormat formatYear = new SimpleDateFormat("yyyy");

        for (int i = Integer.valueOf(formatYear.format(hoy)); i >= RegwebConstantes.ANY; i--) {
            anys.add(i);
        }

        return anys;

    }


    /**
     * Eliminamos la variable especificada de la Sesion
     * @param request
     * @throws Exception
     */
    public void eliminarVariableSesion(HttpServletRequest request, String variable) throws Exception{
        HttpSession session = request.getSession();

        session.setAttribute(variable, null);
    }

    /**
     * Retorna el Organismo al que pertenece la OficinaActiva
     * @param request
     * @return
     * @throws Exception
     */
    public Long seleccionarOrganismoActivo(HttpServletRequest request, List<Organismo> organismos) throws Exception{

        Oficina oficinaActiva = getOficinaActiva(request);

        for (Organismo organismo:organismos){

            if(oficinaActiva.getOrganismoResponsable().equals(organismo)){
                return organismo.getId();
            }

        }
        return null;
    }

    /**
     *
     * @param errores
     * @param bean
     * @return
     */
    public List<FieldError> setDefaultMessageToErrors(List<FieldError> errores, String bean) {

      if (errores == null) {
        log.warn("Variable errores val null !!!!" , new Exception());
      }

      
      List<FieldError> nousErrors = new ArrayList<FieldError>(errores.size());
      
      for (FieldError error : errores) {
        if (error.getDefaultMessage() == null) {
          
          final String code = error.getCode();
          Object[] args = error.getArguments(); 
          String defMsg;
          if (args == null || args.length == 0) {
            defMsg = I18NUtils.tradueix(code);
          } else {
          
            String[] stringArray = Arrays.copyOf(args, args.length, String[].class);
            defMsg = I18NUtils.tradueix(code,  stringArray);
          }
          
          FieldError fe = new FieldError(error.getObjectName(),error.getField(),
              error.getRejectedValue(), false, error.getCodes(), error.getArguments(),
              defMsg);
          nousErrors.add(fe);
        } else {
          nousErrors.add(error);
        }
      }
      
      // TODO Passar a DEBUG
      log.info("====== Hi ha errors en " + bean + " ==========");
      for(FieldError error:nousErrors){
          log.info("+ ObjectName: " + error.getField());
          log.info("    - Code: " + error.getCode());
          log.info("    - DefaultMessage: " + error.getDefaultMessage());
      }

      return nousErrors;
      
    }

    @ModelAttribute("tiposIntegracion")
    public Long[] tiposIntegracion() {
        if(Configuracio.isCAIB()){
            return RegwebConstantes.INTEGRACION_TIPOS_CAIB;
        }else{
            return RegwebConstantes.INTEGRACION_TIPOS;
        }
    }

    /**
     * Método que extrae la url Base(protocol+hostname+port) de una url Completa
     * @param request
     * @param baseUrlFull
     * @return
     */
    public  String getUrlBaseFromFullUrl(HttpServletRequest request, String baseUrlFull) {
        URL url;
        try {
            url = new URL(baseUrlFull);
        } catch (MalformedURLException e) {
            log.error(e.getMessage(),e);
            return null;
        }

        String port;
        if (url.getPort() == -1) {
            port = "";
        } else {
            port = ":" + url.getPort();
        }

        String baseUrl = url.getProtocol() + "://" + url.getHost() + port
                + request.getContextPath();
        return baseUrl;
    }

}
