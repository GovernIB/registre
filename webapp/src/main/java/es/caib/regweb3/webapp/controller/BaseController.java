package es.caib.regweb3.webapp.controller;

import es.caib.dir3caib.ws.api.oficina.ContactoTF;
import es.caib.dir3caib.ws.api.oficina.OficinaTF;
import es.caib.regweb3.model.*;
import es.caib.regweb3.persistence.ejb.*;
import es.caib.regweb3.utils.RegwebConstantes;
import es.caib.regweb3.webapp.security.LoginInfo;
import org.apache.log4j.Logger;
import org.fundaciobit.genapp.common.web.i18n.I18NUtils;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.ejb.EJB;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Fundació BIT.
 *
 * @author earrivi
 * Date: 16/01/14
 */
public class BaseController {

    protected final Logger log = Logger.getLogger(getClass());

    @EJB(mappedName = "regweb3/PermisoLibroUsuarioEJB/local")
    public PermisoLibroUsuarioLocal permisoLibroUsuarioEjb;

    @EJB(mappedName = "regweb3/UsuarioEntidadEJB/local")
    public UsuarioEntidadLocal usuarioEntidadEjb;

    @EJB(mappedName = "regweb3/OrganismoEJB/local")
    public OrganismoLocal organismoEjb;

    @EJB(mappedName = "regweb3/OficinaEJB/local")
    public OficinaLocal oficinaEjb;

    @EJB(mappedName = "regweb3/InteresadoEJB/local")
    public InteresadoLocal interesadoEjb;

    @EJB(mappedName = "regweb3/EntidadEJB/local")
    public EntidadLocal entidadEjb;

    @EJB(mappedName = "regweb3/TipoDocumentalEJB/local")
    public TipoDocumentalLocal tipoDocumentalEjb;

    @EJB(mappedName = "regweb3/RegistroSalidaEJB/local")
    public RegistroSalidaLocal registroSalidaEjb;

    @EJB(mappedName = "regweb3/RegistroEntradaEJB/local")
    public RegistroEntradaLocal registroEntradaEjb;

    @EJB(mappedName = "regweb3/RegistroEntradaConsultaEJB/local")
    public RegistroEntradaConsultaLocal registroEntradaConsultaEjb;

    @EJB(mappedName = "regweb3/RegistroSalidaConsultaEJB/local")
    public RegistroSalidaConsultaLocal registroSalidaConsultaEjb;

    @EJB(mappedName = "regweb3/NotificacionEJB/local")
    private NotificacionLocal notificacionBean;



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
     * Retorna los Libros Administrados del UsuarioEntidad Avtico
     * @param request
     * @return
     */
    @SuppressWarnings(value = "unchecked")
    protected List<Libro> getLibrosAdministrados(HttpServletRequest request) throws Exception{

        return getLoginInfo(request).getLibrosAdministrados();

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
     * Retorna las Oficinas que tiene asociadas el usuario autenticado
     * @param request
     * @return
     */
    @SuppressWarnings(value = "unchecked")
    protected LinkedHashSet<Oficina> getOficinasAutenticado(HttpServletRequest request){

        return getLoginInfo(request).getOficinasRegistro();

    }
    
    
    /**
     * Obtenemos los Libros de la EntidadActiva en los que el UsuarioEntidad actual tiene permisos para consultar registros de entrada
     * @param request
     * @return
     * @throws Exception
     */
    protected List<Libro> getLibrosConsultaEntradas(HttpServletRequest request) throws Exception {

        UsuarioEntidad usuarioEntidad = getUsuarioEntidadActivo(request);

        return permisoLibroUsuarioEjb.getLibrosPermiso(usuarioEntidad.getId(), RegwebConstantes.PERMISO_CONSULTA_REGISTRO_ENTRADA, false);
    }

    /**
     * Obtenemos los Libros de los Organismos a los que la OficinaActiva da servicio
     * y en los que el UsuarioEntidad actual tiene permisos para registrar entradas
     * @param request
     * @return
     * @throws Exception
     */
    protected List<Libro> getLibrosRegistroEntrada(HttpServletRequest request) throws Exception {

        UsuarioEntidad usuarioEntidad = getUsuarioEntidadActivo(request);
        Oficina oficinaActiva = getOficinaActiva(request);

        // Obtenemos los Organismos a los que da servicio una Oficina
        Set<Long> organismos = oficinaActiva.getOrganismosFuncionalesId();

        return permisoLibroUsuarioEjb.getLibrosOrganismoPermiso(organismos, usuarioEntidad.getId(), RegwebConstantes.PERMISO_REGISTRO_ENTRADA);
    }
    

    /**
     * Obtenemos los Libros de los Organismos a los que la OficinaActiva da servicio
     * y en los que el UsuarioEntidad actual tiene permisos para registrar salidas
     * @param request
     * @return
     * @throws Exception
     */
    protected List<Libro> getLibrosRegistroSalida(HttpServletRequest request) throws Exception {

        UsuarioEntidad usuarioEntidad = getUsuarioEntidadActivo(request);
        Oficina oficinaActiva = getOficinaActiva(request);

        // Obtenemos los Organismos a los que da servicio una Oficina
        Set<Long> organismos = oficinaActiva.getOrganismosFuncionalesId();

        return permisoLibroUsuarioEjb.getLibrosOrganismoPermiso(organismos, usuarioEntidad.getId(), RegwebConstantes.PERMISO_REGISTRO_SALIDA);
    }

    /**
     * Obtenemos los Libros de los Organismos a los que la OficinaActiva da servicio
     * y en los que el UsuarioEntidad actual tiene permisos para administrar
     * @param request
     * @return
     * @throws Exception
     */
    protected List<Libro> getLibrosAdministradosOficina(HttpServletRequest request) throws Exception {

        UsuarioEntidad usuarioEntidad = getUsuarioEntidadActivo(request);
        Oficina oficinaActiva = getOficinaActiva(request);

        // Obtenemos los Organismos a los que da servicio una Oficina
        Set<Long> organismos = oficinaActiva.getOrganismosFuncionalesId();

        return permisoLibroUsuarioEjb.getLibrosOrganismoPermiso(organismos, usuarioEntidad.getId(), RegwebConstantes.PERMISO_ADMINISTRACION_LIBRO);
    }



    /**
     * Obtenemos los Libros de la Entidad Activa en los que el UsuarioEntidad actual tiene permisos para consultar registros de salida
     * @param request
     * @return
     * @throws Exception
     */
    protected List<Libro> getLibrosConsultaSalidas(HttpServletRequest request) throws Exception {

        UsuarioEntidad usuarioEntidad = getUsuarioEntidadActivo(request);

        return permisoLibroUsuarioEjb.getLibrosPermiso(usuarioEntidad.getId(), RegwebConstantes.PERMISO_CONSULTA_REGISTRO_SALIDA, false);
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
     * Obtiene los unicamente los códigos de los Organismos de la OficinaActiva que puede gestionar el usuario
     * @param request
     * @return
     * @throws Exception
     */
    public Set<String> getOrganismosOficinaActivaCodigo(HttpServletRequest request) throws Exception {

        LinkedHashSet<Organismo> organismos = getOrganismosOficinaActiva(request);

        // Creamos un Set solo con los codigos
        Set<String> organismosCodigo = new HashSet<String>();

        for (Organismo organismo : organismos) {
            organismosCodigo.add(organismo.getCodigo());
        }

        return organismosCodigo;
    }

    /**
     * Obtiene el Id de los Organismos de la OficinaActiva en los cuales no se generará OficioRemisión.
     * Se eliminando los Organismos que están marcados como Entidad de Derecho Público o
     * a los que la OficiaActiva da servicio.
     *
     * @param request
     * @return
     * @throws Exception
     */
    public Set<Long> getOrganismosOficioRemision(HttpServletRequest request, Set<Organismo> organismos) throws Exception {

        Oficina oficinaActiva = getOficinaActiva(request);

        // Creamos un Set solo con los identificadores
        Set<Long> organismosId = new HashSet<Long>();

        for (Organismo organismo : organismos) {
            organismosId.add(organismo.getId());
            /*// Eliminamos el Organismo si está marcado como Entidad de Derecho Público o si
            // la OficinaActiva le da servicio.
            if (!organismo.getEdp() || oficinaActiva.getOrganismosFuncionales().contains(organismo)) {
                organismosId.add(organismo.getId());
            }*/
        }
        return organismosId;
    }

    /**
     * Obtiene el Id de los Organismos de la OficinaActiva en los cuales no se generará OficioRemisión.
     * Se eliminando los Organismos que están marcados como Entidad de Derecho Público o
     * a los que la OficiaActiva da servicio.
     *
     * @return
     * @throws Exception
     */
    public Set<String> getOrganismosOficioRemisionSalida(Set<Organismo> organismos) throws Exception {

        // Creamos un Set solo con los codigos
        Set<String> organismosCodigo = new HashSet<String>();

        for (Organismo organismo : organismos) {
            organismosCodigo.add(organismo.getCodigo());

        }
        return organismosCodigo;
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
     * Obtenemos y procesamos los Interesados almacenados en la Session
     * @param interesadosSesion
     * @return
     * @throws Exception
     */
    public List<Interesado> procesarInteresados(List<Interesado> interesadosSesion, Long idRegistroDetalle) throws Exception{

        List<Interesado> interesados  = new ArrayList<Interesado>();

        RegistroDetalle registroDetalle = null;

        if(idRegistroDetalle != null){
            registroDetalle = new RegistroDetalle(idRegistroDetalle);
        }

        if(interesadosSesion != null){

            for(Interesado interesado:interesadosSesion){

                if(!interesado.getIsRepresentante()){ // No es representante

                    // Comprobamos si tiene un representante
                    if(interesado.getRepresentante() != null){
                        try{

                            log.info(interesado.getNombre() + " tiene representante");

                            Interesado representante = interesadosSesion.get(interesadosSesion.indexOf(interesado.getRepresentante()));

                            //Guardamos el Interesado
                            interesado.setId(null); // ponemos su id a null
                            interesado.setRegistroDetalle(registroDetalle);
                            interesado.setRepresentante(null);
                            interesado = interesadoEjb.persist(interesado);

                            // Guardamos el Representante
                            representante.setId(null);
                            representante.setRegistroDetalle(registroDetalle);
                            representante.setRepresentado(interesado);
                            representante = interesadoEjb.persist(representante);

                            // Le asignamos su representado y actualizamos
                            //representante.setRepresentado(interesado);
                            //representante = interesadoEjb.merge(representante);

                            // Lo asigamos al interesado y actualizamos
                            interesado.setRepresentante(representante);
                            log.info("id representante : " + representante.getId());
                            interesado = interesadoEjb.merge(interesado);

                            // Lo añadimos al Array
                            interesados.add(interesado);
                            interesados.add(representante);

                        }catch (Exception e){                 // TODO NO!!!!!!!!!!!!!!!!!
                            e.printStackTrace();
                        }
                    }else{
                        // Guardamos el nuevo Interesado
                        interesado.setId(null); // ponemos su id a null
                        interesado.setRegistroDetalle(registroDetalle);
                        interesado = interesadoEjb.persist(interesado);

                        // Lo añadimos al Array
                        interesados.add(interesado);

                    }

                }
            }
        }

        return interesados;

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
     * Retorna el libro de cuyo OrganismoRespnsable coincide con el de la OficinaActiva
     * @param request
     * @param libros
     * @return
     * @throws Exception
     */
    public Libro seleccionarLibroOficinaActiva(HttpServletRequest request,List<Libro> libros) throws Exception{

        Oficina oficinaActiva = getOficinaActiva(request);
        for (Libro libro:libros){
            if(libro.getOrganismo().equals(oficinaActiva.getOrganismoResponsable())){
                return  libro;
            }
        }
        return null;
    }
    
    
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

    /**
     * Método que obtiene los contactos de la oficina Sir de destino
     * @param oficinaSir
     * @return
     * @throws Exception
     */
    public String getContactosOficinaSir(OficinaTF oficinaSir) throws Exception {
        StringBuilder stb = new StringBuilder();
        for(ContactoTF contactoTF: oficinaSir.getContactos()){
            String scontactoTF = "<b>" + contactoTF.getTipoContacto()+"</b>: "+ contactoTF.getValorContacto();
            stb.append(scontactoTF);
            stb.append("<br>");
        }

        return stb.toString();

    }

    @ModelAttribute("notificacionesPendientes")
    public Long notificacionesPendientes(HttpServletRequest request) throws Exception {
        UsuarioEntidad usuarioEntidad = getUsuarioEntidadActivo(request);

        if (usuarioEntidad != null && (isAdminEntidad(request) || isOperador(request))) {

            return notificacionBean.notificacionesPendientes(usuarioEntidad.getId());
        }

        return 0L;
    }
}
