package es.caib.regweb.webapp.controller.registro;

import es.caib.regweb.model.*;
import es.caib.regweb.persistence.ejb.*;
import es.caib.regweb.persistence.utils.Paginacion;
import es.caib.regweb.persistence.utils.RegistroUtils;
import es.caib.regweb.utils.RegwebConstantes;
import es.caib.regweb.webapp.controller.BaseController;
import es.caib.regweb.webapp.form.RegistroSalidaBusqueda;
import es.caib.regweb.webapp.validator.RegistroSalidaBusquedaValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.ejb.EJB;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Fundació BIT.
 * Controller para gestionar los Registros de Salida
 * @author earrivi
 * Date: 31/03/14
 */
@Controller
@RequestMapping(value = "/registroSalida")
@SessionAttributes(types = RegistroSalida.class)
public class RegistroSalidaListController extends BaseController {


    @Autowired
    private RegistroSalidaBusquedaValidator registroSalidaBusquedaValidator;
    
    @EJB(mappedName = "regweb/CodigoAsuntoEJB/local")
    public CodigoAsuntoLocal codigoAsuntoEjb;
  
    @EJB(mappedName = "regweb/LopdEJB/local")
    public LopdLocal lopdEjb;
    
    @EJB(mappedName = "regweb/TrazabilidadEJB/local")
    public TrazabilidadLocal trazabilidadEjb;

    @EJB(mappedName = "regweb/ModeloReciboEJB/local")
    public ModeloReciboLocal modeloReciboEjb;
    
    @EJB(mappedName = "regweb/RegistroSalidaEJB/local")
    public RegistroSalidaLocal registroSalidaEjb;
   
    @EJB(mappedName = "regweb/TipoAsuntoEJB/local")
    public TipoAsuntoLocal tipoAsuntoEjb;

    @EJB(mappedName = "regweb/HistoricoRegistroSalidaEJB/local")
    public HistoricoRegistroSalidaLocal historicoRegistroSalidaEjb;

    @EJB(mappedName = "regweb/AnexoEJB/local")
    public AnexoLocal anexoEjb;

    @EJB(mappedName = "regweb/PersonaEJB/local")
    public PersonaLocal personaEjb;

    @EJB(mappedName = "regweb/CatProvinciaEJB/local")
    public CatProvinciaLocal catProvinciaEjb;

    @EJB(mappedName = "regweb/CatPaisEJB/local")
    public CatPaisLocal catPaisEjb;

    @EJB(mappedName = "regweb/CatComunidadAutonomaEJB/local")
    public CatComunidadAutonomaLocal catComunidadAutonomaEjb;

    @EJB(mappedName = "regweb/CatNivelAdministracionEJB/local")
    public CatNivelAdministracionLocal catNivelAdministracionEjb;

    @EJB(mappedName = "regweb/TipoDocumentalEJB/local")
    public TipoDocumentalLocal tipoDocumentalEjb;

    /**
    * Listado de todos los Registros de Salida
    */
   @RequestMapping(value = "", method = RequestMethod.GET)
    public String listado() {
       return "redirect:/registroSalida/list";
    }

    /**
     * Listado de registros de salida
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String list(Model model, HttpServletRequest request)throws Exception {

        UsuarioEntidad usuarioEntidad = getUsuarioEntidadActivo(request);

        // Obtenemos los Libros donde el usuario tiene permisos de Consulta
        List<Libro> librosConsulta = permisoLibroUsuarioEjb.getLibrosPermiso(usuarioEntidad.getId(), RegwebConstantes.PERMISO_CONSULTA_REGISTRO_SALIDA);

        RegistroSalidaBusqueda registroSalidaBusqueda = new RegistroSalidaBusqueda(new RegistroSalida(),1);
        registroSalidaBusqueda.setFechaFin(new Date());

        model.addAttribute("librosConsulta", librosConsulta);
        model.addAttribute("registroSalidaBusqueda", registroSalidaBusqueda);

        return "registroSalida/registroSalidaList";

    }

    /**
     * Realiza la busqueda de {@link es.caib.regweb.model.RegistroSalida} según los parametros del formulario
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    public ModelAndView list(@ModelAttribute RegistroSalidaBusqueda busqueda, BindingResult result,HttpServletRequest request)throws Exception {

        ModelAndView mav = new ModelAndView("registroSalida/registroSalidaList", result.getModel());
        RegistroSalida registroSalida = busqueda.getRegistroSalida();

        UsuarioEntidad usuarioEntidad = usuarioEntidadEjb.findByUsuarioEntidad(getUsuarioAutenticado(request).getId(), getEntidadActiva(request).getId());
        List<Libro> librosConsulta = permisoLibroUsuarioEjb.getLibrosPermiso(usuarioEntidad.getId(), RegwebConstantes.PERMISO_CONSULTA_REGISTRO_SALIDA);

        registroSalidaBusquedaValidator.validate(busqueda,result);

        if (result.hasErrors()) { // Si hay errores volvemos a la vista del formulario

            mav.addObject("errors", result.getAllErrors());
            mav.addObject("librosConsulta", librosConsulta);
            mav.addObject("registroSalidaBusqueda", busqueda);
            return mav;
        }else { // Si no hay errores realizamos la búsqueda

            // Ponemos la hora 23:59 a la fecha fin
            Date fechaFin = RegistroUtils.ajustarHoraBusqueda(busqueda.getFechaFin());

            Paginacion paginacion = registroSalidaEjb.busqueda(busqueda.getPageNumber(), busqueda.getFechaInicio(), fechaFin, registroSalida, librosConsulta, busqueda.getAnexos());

            // Alta en tabla LOPD
            lopdEjb.insertarRegistrosSalida(paginacion, usuarioEntidad.getId());

            busqueda.setPageNumber(1);
            mav.addObject("paginacion", paginacion);
            mav.addObject("librosConsulta", librosConsulta);
            mav.addObject("registroSalidaBusqueda", busqueda);
            mav.addObject("isAdministradorLibro", permisoLibroUsuarioEjb.isAdministradorLibro(getUsuarioEntidadActivo(request).getId(),registroSalida.getLibro().getId()));
            mav.addObject("puedeEditar", permisoLibroUsuarioEjb.tienePermiso(usuarioEntidad.getId(),registroSalida.getLibro().getId(),RegwebConstantes.PERMISO_MODIFICACION_REGISTRO_SALIDA));
        }

        return mav;

    }

    /**
     * Carga el formulario para ver el detalle de un {@link es.caib.regweb.model.RegistroSalida}
     */
    @RequestMapping(value = "/{idRegistro}/detalle", method = RequestMethod.GET)
    public String detalleRegistroSalida(@PathVariable Long idRegistro, Model model, HttpServletRequest request) throws Exception {

        RegistroSalida registro = registroSalidaEjb.findById(idRegistro);
        Entidad entidad = getEntidadActiva(request);
        UsuarioEntidad usuarioEntidad = getUsuarioEntidadActivo(request);

        model.addAttribute("registro",registro);

        ModeloRecibo modeloRecibo = new ModeloRecibo();
        model.addAttribute("modeloRecibo", modeloRecibo);
        model.addAttribute("modelosRecibo", modeloReciboEjb.getByEntidad(getEntidadActiva(request).getId()));

        // Permisos
        model.addAttribute("isAdministradorLibro", permisoLibroUsuarioEjb.isAdministradorLibro(getUsuarioEntidadActivo(request).getId(),registro.getLibro().getId()));
        model.addAttribute("puedeEditar", permisoLibroUsuarioEjb.tienePermiso(usuarioEntidad.getId(),registro.getLibro().getId(),RegwebConstantes.PERMISO_MODIFICACION_REGISTRO_ENTRADA));

        // Interesados, solo si el Registro en Válio o Pendiente
        if(registro.getEstado().equals(RegwebConstantes.ESTADO_VALIDO) || registro.getEstado().equals(RegwebConstantes.ESTADO_PENDIENTE)){

            model.addAttribute("personasFisicas",personaEjb.getAllbyEntidadTipo(entidad.getId(), RegwebConstantes.TIPO_PERSONA_FISICA));
            model.addAttribute("personasJuridicas",personaEjb.getAllbyEntidadTipo(entidad.getId(), RegwebConstantes.TIPO_PERSONA_JURIDICA));
            model.addAttribute("tiposInteresado",RegwebConstantes.TIPOS_INTERESADO);
            model.addAttribute("tiposPersona", RegwebConstantes.TIPOS_PERSONA);
            model.addAttribute("paises",catPaisEjb.getAll());
            model.addAttribute("provincias",catProvinciaEjb.getAll());
            model.addAttribute("canalesNotificacion", RegwebConstantes.CANALES_NOTIFICACION);
            model.addAttribute("tiposDocumento",RegwebConstantes.TIPOS_DOCUMENTOID);
            model.addAttribute("nivelesAdministracion",catNivelAdministracionEjb.getAll());
            model.addAttribute("comunidadesAutonomas",catComunidadAutonomaEjb.getAll());
            model.addAttribute("organismosOficinaActiva",organismoEjb.getByOficinaActiva(getOficinaActiva(request).getId()));

        }
        // Anexos
        model.addAttribute("anexos", anexoEjb.getByRegistroSalida(idRegistro));
        model.addAttribute("tiposDocumental", tipoDocumentalEjb.getByEntidad(getEntidadActiva(request).getId()));
        model.addAttribute("tiposDocumentoAnexo", RegwebConstantes.TIPOS_DOCUMENTO);
        model.addAttribute("tiposFirma", RegwebConstantes.TIPOS_FIRMA);
        model.addAttribute("tiposValidezDocumento", RegwebConstantes.TIPOS_VALIDEZDOCUMENTO);

        // Historicos
        model.addAttribute("historicos", historicoRegistroSalidaEjb.getByRegistroSalida(idRegistro));

        // Trazabilidad
        List<Trazabilidad> trazabilidades = trazabilidadEjb.getByRegistroSalida(registro.getId());
        model.addAttribute("trazabilidades", trazabilidades);

        // Alta en tabla LOPD
        lopdEjb.insertarRegistroSalida(idRegistro, usuarioEntidad.getId());

        return "registroSalida/registroSalidaDetalle";
    }

    /**
     * Carga el formulario para ver el detalle de un {@link es.caib.regweb.model.RegistroSalida}
     */
    @RequestMapping(value = "/{idRegistro}/sello", method = RequestMethod.GET)
    public ModelAndView sello(@PathVariable Long idRegistro, HttpServletRequest request) throws Exception {

        ModelAndView mav = new ModelAndView("sello");

        RegistroSalida registroSalida = registroSalidaEjb.findById(idRegistro);

        mav.addObject("registro", registroSalida);
        mav.addObject("x", request.getParameter("x"));
        mav.addObject("y", request.getParameter("y"));
        mav.addObject("orientacion", request.getParameter("orientacion"));

        return mav;
    }


    /**
     * Procesa las opciones de comunes de un RegistroEntrada, lo utilizamos en la creación y modificación.
     * @param registroEntrada
     * @return
     * @throws Exception
     */
    public RegistroEntrada procesarRegistroEntrada(RegistroEntrada registroEntrada) throws Exception{

        Organismo organismoDestino = registroEntrada.getDestino();

        // Gestionamos el Organismo, determinando si es Interno o Externo
        Organismo orgDestino = organismoEjb.findByCodigoVigente(organismoDestino.getCodigo());
        if(orgDestino != null){ // es interno
            log.info("orgDestino interno: " + orgDestino.getDenominacion());
            registroEntrada.setDestino(orgDestino);
            registroEntrada.setDestinoExternoCodigo(null);
            registroEntrada.setDestinoExternoDenominacion(null);
        } else { // es externo
            log.info("orgDestino externo: " + registroEntrada.getDestino().getDenominacion());
            registroEntrada.setDestinoExternoCodigo(registroEntrada.getDestino().getCodigo());
            registroEntrada.setDestinoExternoDenominacion(registroEntrada.getDestino().getDenominacion());
            registroEntrada.setDestino(null);
        }

        // Cogemos los dos posibles campos
        Oficina oficinaOrigen = registroEntrada.getRegistroDetalle().getOficinaOrigen();
        
        // Si no han indicado ni externa ni interna, se establece la oficina en la que se realiza el registro.
        if(oficinaOrigen == null){
            registroEntrada.getRegistroDetalle().setOficinaOrigen(registroEntrada.getOficina());
        } else {
          if(!oficinaOrigen.getCodigo().equals("-1")){ // si han indicado oficina origen
              Oficina ofiOrigen = oficinaEjb.findByCodigo(oficinaOrigen.getCodigo());
              if(ofiOrigen != null){ // Es interna
                  // log.info("oficina interna");
                  registroEntrada.getRegistroDetalle().setOficinaOrigen(ofiOrigen);
                  registroEntrada.getRegistroDetalle().setOficinaOrigenExternoCodigo(null);
                  registroEntrada.getRegistroDetalle().setOficinaOrigenExternoDenominacion(null);
              } else {  // es interna
                  // log.info("oficina externo");
                  registroEntrada.getRegistroDetalle().setOficinaOrigenExternoCodigo(registroEntrada.getRegistroDetalle().getOficinaOrigen().getCodigo());
                  registroEntrada.getRegistroDetalle().setOficinaOrigenExternoDenominacion(registroEntrada.getRegistroDetalle().getOficinaOrigen().getDenominacion());
                  registroEntrada.getRegistroDetalle().setOficinaOrigen(null);
              }
          }else { // No han indicado oficina de origen
              registroEntrada.getRegistroDetalle().setOficinaOrigen(null);
              registroEntrada.getRegistroDetalle().setOficinaOrigenExternoCodigo(null);
              registroEntrada.getRegistroDetalle().setOficinaOrigenExternoDenominacion(null);
          }
        }




        // Solo se comprueba si es una modificación de RegistroEntrada
        if(registroEntrada.getId() != null){
            // Si no ha introducido ninguna fecha de Origen, se establece la fecha actual
            if(registroEntrada.getRegistroDetalle().getFechaOrigen() == null){
                registroEntrada.getRegistroDetalle().setFechaOrigen(new Date());
            }

            // Si no ha introducido ningún número de registro de Origen, le ponemos el actual.
            if(registroEntrada.getRegistroDetalle().getNumeroRegistroOrigen() == null || registroEntrada.getRegistroDetalle().getNumeroRegistroOrigen().length() == 0){
                registroEntrada.getRegistroDetalle().setNumeroRegistroOrigen(registroEntrada.getNumeroRegistroFormateado());
            }
        }

        // No han especificado Codigo Asunto
        if( registroEntrada.getRegistroDetalle().getCodigoAsunto().getId() == null || registroEntrada.getRegistroDetalle().getCodigoAsunto().getId() == -1){
            registroEntrada.getRegistroDetalle().setCodigoAsunto(null);
        }

        // No han especificadoTransporte
        if( registroEntrada.getRegistroDetalle().getTransporte() == -1){
            registroEntrada.getRegistroDetalle().setTransporte(null);
        }

        // Organimo Interesado



        return registroEntrada;
    }



    @ModelAttribute("libros")
    public List<Libro> libros(HttpServletRequest request) throws Exception {

        UsuarioEntidad usuarioEntidad = usuarioEntidadEjb.findByUsuarioEntidad(getUsuarioAutenticado(request).getId(),getEntidadActiva(request).getId());
        Oficina oficinaActiva = getOficinaActiva(request);

        Set<Organismo> organismos = new HashSet<Organismo>();

        Organismo organismoResponsable = oficinaActiva.getOrganismoResponsable();

        // Añadimos el Organismo responsable
        organismos.add(organismoResponsable);

        // Añadimos los Organismos a los que la Oficina da servicio
        Set<RelacionOrganizativaOfi> organismosFuncionales = oficinaActiva.getOrganizativasOfi();
        for(RelacionOrganizativaOfi relacionOrganizativaOfi:organismosFuncionales){
            organismos.add(relacionOrganizativaOfi.getOrganismo());
        }

        return permisoLibroUsuarioEjb.getLibrosRegistroOficina(organismos, usuarioEntidad);
    }


    @ModelAttribute("estados")
    public Long[] estados(HttpServletRequest request) throws Exception {
        if(getEntidadActiva(request).getSir()){
            return RegwebConstantes.ESTADOS_REGISTRO_SIR;
        }else {
            return RegwebConstantes.ESTADOS_REGISTRO;
        }
    }

    @InitBinder("registroSalidaBusqueda")
    public void registroSalidaBusqueda(WebDataBinder binder) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        CustomDateEditor dateEditor = new CustomDateEditor(sdf, true);
        binder.registerCustomEditor(java.util.Date.class,dateEditor);
    }

    /**
     * Eliminamos los posibles interesados de la Sesion
     * @param request
     * @throws Exception
     */
    public void eliminarInteresadosSesion(HttpServletRequest request) throws Exception{
        HttpSession session = request.getSession();

        session.setAttribute("interesados", null);
    }

}