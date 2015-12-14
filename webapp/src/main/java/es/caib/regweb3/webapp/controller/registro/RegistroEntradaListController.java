package es.caib.regweb3.webapp.controller.registro;

import es.caib.regweb3.model.*;
import es.caib.regweb3.persistence.ejb.HistoricoRegistroEntradaLocal;
import es.caib.regweb3.persistence.ejb.RegistroEntradaLocal;
import es.caib.regweb3.persistence.ejb.SirLocal;
import es.caib.regweb3.persistence.utils.Paginacion;
import es.caib.regweb3.persistence.utils.RegistroUtils;
import es.caib.regweb3.utils.RegwebConstantes;
import es.caib.regweb3.webapp.form.RegistroEntradaBusqueda;
import es.caib.regweb3.webapp.utils.Mensaje;
import es.caib.regweb3.webapp.validator.RegistroEntradaBusquedaValidator;
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
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * Created by Fundació BIT.
 * Controller para los listados de los Registros de Entrada
 * @author earrivi
 * @author anadal
 * Date: 31/03/14
 */
@Controller
@RequestMapping(value = "/registroEntrada")
public class RegistroEntradaListController extends AbstractRegistroCommonListController {

    @Autowired
    private RegistroEntradaBusquedaValidator registroEntradaBusquedaValidator;

    @EJB(mappedName = "regweb3/HistoricoRegistroEntradaEJB/local")
    public HistoricoRegistroEntradaLocal historicoRegistroEntradaEjb;
    
    @EJB(mappedName = "regweb3/RegistroEntradaEJB/local")
    public RegistroEntradaLocal registroEntradaEjb;
    
    @EJB(mappedName = "regweb3/SirEJB/local")
    public SirLocal sirEjb;

    

    /**
    * Listado de todos los Registros de Entrada
    */
   @RequestMapping(value = "", method = RequestMethod.GET)
    public String listado() {
       return "redirect:/registroEntrada/list";
    }

   private Set<Organismo> getOrganismosInternosMasExternos(HttpServletRequest request) throws Exception {
	   
	   Set<Organismo> allOrganismos = getOrganismosOficinaActiva(request);
	   /*List<RegistroEntrada> regsEntrada = registroEntradaEjb.getAll();
	   
	   for(int r=0; r<regsEntrada.size(); r++) {
		   if (regsEntrada.get(r).getDestinoExternoCodigo()!=null && !"".equals(regsEntrada.get(r).getDestinoExternoCodigo()) && !"null".equalsIgnoreCase(regsEntrada.get(r).getDestinoExternoCodigo())) {
			   Organismo org = new Organismo();
			   org.setCodigo(regsEntrada.get(r).getDestinoExternoCodigo());
			   org.setDenominacion(regsEntrada.get(r).getDestinoExternoDenominacion());
			   allOrganismos.add(org);
		   }
	   }*/
	   
	   return allOrganismos;
   }

    /**
    * Listado de registros de entrada
    * @return
    * @throws Exception
    */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String list(Model model, HttpServletRequest request)throws Exception {

        // Obtenemos los Libros donde el usuario tiene permisos de Consulta
        List<Libro> librosConsulta = getLibrosConsultaEntradas(request);

        RegistroEntradaBusqueda registroEntradaBusqueda = new RegistroEntradaBusqueda(new RegistroEntrada(),1);
        registroEntradaBusqueda.setFechaInicio(new Date());
        registroEntradaBusqueda.setFechaFin(new Date());

        Oficina oficina = getOficinaActiva(request);
        
        model.addAttribute(oficina);
        model.addAttribute("librosConsulta", librosConsulta);
        model.addAttribute("registroEntradaBusqueda", registroEntradaBusqueda);
        model.addAttribute("organosDestino",  getOrganismosInternosMasExternos(request));
        model.addAttribute("oficinasRegistro",  oficinaEjb.findByEntidadByEstado(getEntidadActiva(request).getId(),RegwebConstantes.ESTADO_ENTIDAD_VIGENTE));

        // Obtenemos los usuarios de la Entidad
        List<UsuarioEntidad> usuariosEntidad = usuarioEntidadEjb.findByEntidad(getEntidadActiva(request).getId());
        model.addAttribute("usuariosEntidad", usuariosEntidad);

        return "registroEntrada/registroEntradaList";
    }

    /**
    * Realiza la busqueda de {@link es.caib.regweb3.model.RegistroEntrada} según los parametros del formulario
    */
    @RequestMapping(value = "/busqueda", method = RequestMethod.GET)
    public ModelAndView list(@ModelAttribute RegistroEntradaBusqueda busqueda, BindingResult result, HttpServletRequest request, HttpServletResponse response)throws Exception {

        ModelAndView mav = new ModelAndView("registroEntrada/registroEntradaList", result.getModel());
        RegistroEntrada registroEntrada = busqueda.getRegistroEntrada();

        UsuarioEntidad usuarioEntidad = getUsuarioEntidadActivo(request);
        List<Libro> librosConsulta = permisoLibroUsuarioEjb.getLibrosPermiso(usuarioEntidad.getId(), RegwebConstantes.PERMISO_CONSULTA_REGISTRO_ENTRADA);

        List<UsuarioEntidad> usuariosEntidad = usuarioEntidadEjb.findByEntidad(getEntidadActiva(request).getId());
        mav.addObject("usuariosEntidad",usuariosEntidad);

        registroEntradaBusquedaValidator.validate(busqueda,result);

        Oficina oficina = getOficinaActiva(request);
        mav.addObject(oficina);

        Set<Organismo> todosOrganismos = getOrganismosInternosMasExternos(request);
        
        if (busqueda.getOrganDestinatari()!=null && !"".equals(busqueda.getOrganDestinatari())) {
		    Organismo org = new Organismo();
		    org.setCodigo(busqueda.getOrganDestinatari());
		    org.setDenominacion(busqueda.getOrganDestinatariNom());
		    todosOrganismos.add(org);
        }
	    
        mav.addObject("organosDestino",  todosOrganismos);
        
        if (result.hasErrors()) { // Si hay errores volvemos a la vista del formulario
            mav.addObject("errors", result.getAllErrors());
            mav.addObject("librosConsulta", librosConsulta);
            mav.addObject("registroEntradaBusqueda", busqueda);
            return mav;
        }else { // Si no hay errores realizamos la búsqueda
            // Ponemos la hora 23:59 a la fecha fin
            Date fechaFin = RegistroUtils.ajustarHoraBusqueda(busqueda.getFechaFin());
            Paginacion paginacion = registroEntradaEjb.busqueda(busqueda.getPageNumber(), busqueda.getFechaInicio(), fechaFin, registroEntrada, librosConsulta, busqueda.getInteressatNom(), busqueda.getInteressatDoc(), busqueda.getOrganDestinatari(), busqueda.getAnexos(), busqueda.getObservaciones(), busqueda.getUsuario());

            // Alta en tabla LOPD
            lopdEjb.insertarRegistrosEntrada(paginacion, usuarioEntidad.getId());
            busqueda.setPageNumber(1);
            mav.addObject("paginacion", paginacion);
            mav.addObject("librosConsulta", librosConsulta);
            mav.addObject("registroEntradaBusqueda", busqueda);            
            mav.addObject("isAdministradorLibro", permisoLibroUsuarioEjb.isAdministradorLibro(getUsuarioEntidadActivo(request).getId(),registroEntrada.getLibro().getId()));
            mav.addObject("puedeEditar", permisoLibroUsuarioEjb.tienePermiso(usuarioEntidad.getId(),registroEntrada.getLibro().getId(),RegwebConstantes.PERMISO_MODIFICACION_REGISTRO_ENTRADA));
            mav.addObject("oficinasRegistro", oficinaEjb.findByEntidadByEstado(getEntidadActiva(request).getId(), RegwebConstantes.ESTADO_ENTIDAD_VIGENTE));
        }


        /* Solucion a los problemas de encoding del formulario GET */
        busqueda.getRegistroEntrada().getRegistroDetalle().setExtracto(new String(busqueda.getRegistroEntrada().getRegistroDetalle().getExtracto().getBytes("ISO-8859-1"), "UTF-8"));
        busqueda.setObservaciones(new String(busqueda.getObservaciones().getBytes("ISO-8859-1"), "UTF-8"));
        busqueda.setInteressatNom(new String(busqueda.getInteressatNom().getBytes("ISO-8859-1"), "UTF-8"));
        return mav;

    }



    /**
     * Carga el formulario para ver el detalle de un {@link es.caib.regweb3.model.RegistroEntrada}
     */
    @RequestMapping(value = "/{idRegistro}/detalle", method = RequestMethod.GET)
    public String detalleRegistroEntrada(@PathVariable Long idRegistro, Model model, HttpServletRequest request) throws Exception {

        RegistroEntrada registro = registroEntradaEjb.findById(idRegistro);
        Entidad entidad = getEntidadActiva(request);
        UsuarioEntidad usuarioEntidad = getUsuarioEntidadActivo(request);
        Oficina oficinaActiva = getOficinaActiva(request);

        model.addAttribute("registro",registro);

        // Modelo Recibo
        model.addAttribute("modeloRecibo", new ModeloRecibo());
        model.addAttribute("modelosRecibo", modeloReciboEjb.getByEntidad(getEntidadActiva(request).getId()));

        // Permisos
        Boolean oficinaRegistral = registro.getOficina().getId().equals(oficinaActiva.getId()) || (registro.getOficina().getOficinaResponsable() != null && registro.getOficina().getOficinaResponsable().getId().equals(oficinaActiva.getId()));
        model.addAttribute("oficinaRegistral", oficinaRegistral);
        model.addAttribute("isAdministradorLibro", permisoLibroUsuarioEjb.isAdministradorLibro(getUsuarioEntidadActivo(request).getId(),registro.getLibro().getId()));
        model.addAttribute("puedeEditar", permisoLibroUsuarioEjb.tienePermiso(usuarioEntidad.getId(),registro.getLibro().getId(),RegwebConstantes.PERMISO_MODIFICACION_REGISTRO_ENTRADA));

        // OficioRemision
        model.addAttribute("isOficioRemision",registroEntradaEjb.isOficioRemisionInterno(idRegistro));

        // Interesados, solo si el Registro en Válido o Estamos en la Oficina donde se registró, o en su Oficina Responsable
        if(registro.getEstado().equals(RegwebConstantes.ESTADO_VALIDO) && oficinaRegistral){

            model.addAttribute("personasFisicas",personaEjb.getAllbyEntidadTipo(entidad.getId(), RegwebConstantes.TIPO_PERSONA_FISICA));
            model.addAttribute("personasJuridicas",personaEjb.getAllbyEntidadTipo(entidad.getId(), RegwebConstantes.TIPO_PERSONA_JURIDICA));
            model.addAttribute("tiposInteresado", RegwebConstantes.TIPOS_INTERESADO);
            model.addAttribute("tiposPersona",RegwebConstantes.TIPOS_PERSONA);
            model.addAttribute("paises",catPaisEjb.getAll());
            model.addAttribute("provincias",catProvinciaEjb.getAll());
            model.addAttribute("canalesNotificacion",RegwebConstantes.CANALES_NOTIFICACION);
            model.addAttribute("tiposDocumento", RegwebConstantes.TIPOS_DOCUMENTOID);
            model.addAttribute("nivelesAdministracion",catNivelAdministracionEjb.getAll());
            model.addAttribute("comunidadesAutonomas",catComunidadAutonomaEjb.getAll());
            model.addAttribute("organismosOficinaActiva",organismoEjb.getByOficinaActiva(getOficinaActiva(request)));
        }
        // Anexos
        model.addAttribute("anexos", anexoEjb.getByRegistroEntrada(idRegistro));
        initAnexos(entidad, model, request, registro.getId());

        // Historicos
        model.addAttribute("historicos", historicoRegistroEntradaEjb.getByRegistroEntrada(idRegistro));

        // Trazabilidad
        List<Trazabilidad> trazabilidades = trazabilidadEjb.getByRegistroEntrada(registro.getId());
        model.addAttribute("trazabilidades", trazabilidades);

        // Posicion sello
        if(entidad.getPosXsello()!=null && entidad.getPosYsello()!=null){
            model.addAttribute("posXsello",entidad.getPosXsello());
            model.addAttribute("posYsello",entidad.getPosYsello());
        }

        // Alta en tabla LOPD
        lopdEjb.insertarRegistroEntrada(idRegistro, usuarioEntidad.getId());
        
        return "registroEntrada/registroEntradaDetalle";
    }
    
    
    

    
    

    /**
     * Anular un {@link es.caib.regweb3.model.RegistroEntrada}
     */
    @RequestMapping(value = "/{idRegistro}/anular")
    public String anularRegistroEntrada(@PathVariable Long idRegistro, HttpServletRequest request) {

        try {

            RegistroEntrada registroEntrada = registroEntradaEjb.findById(idRegistro);
            UsuarioEntidad usuarioEntidad = getUsuarioEntidadActivo(request);

            final List<Long> estados = new ArrayList<Long>();
            estados.add(RegwebConstantes.ESTADO_PENDIENTE);
            estados.add(RegwebConstantes.ESTADO_VALIDO);
            estados.add(RegwebConstantes.ESTADO_PENDIENTE_VISAR);

            // Comprobamos si el RegistroEntrada se puede anular según su estado.
            if(!estados.contains(registroEntrada.getEstado())){
                Mensaje.saveMessageError(request, getMessage("registroEntrada.anulado"));
                return "redirect:/registroEntrada/list";
            }

            // Comprobamos que el usuario dispone del permiso para Modificar el Registro
            if(!permisoLibroUsuarioEjb.tienePermiso(usuarioEntidad.getId(),registroEntrada.getLibro().getId(),RegwebConstantes.PERMISO_MODIFICACION_REGISTRO_ENTRADA)){
                log.info("Aviso: No dispone de los permisos necesarios para editar el registro");
                Mensaje.saveMessageAviso(request, getMessage("aviso.registro.editar"));

                return "redirect:/registroEntrada/list";
            }

            // Anulamos el RegistroEntrada
            registroEntradaEjb.anularRegistroEntrada(registroEntrada,usuarioEntidad);

            Mensaje.saveMessageInfo(request, getMessage("registroEntrada.anular"));

        } catch (Exception e) {
            Mensaje.saveMessageError(request, getMessage("regweb.error.registro"));
            e.printStackTrace();
        }

        return "redirect:/registroEntrada/list";
    }

    /**
     * Activar un {@link es.caib.regweb3.model.RegistroEntrada}
     */
    @RequestMapping(value = "/{idRegistro}/activar")
    public String activarRegistroEntrada(@PathVariable Long idRegistro, HttpServletRequest request) {

        try {

            RegistroEntrada registroEntrada = registroEntradaEjb.findById(idRegistro);
            UsuarioEntidad usuarioEntidad = getUsuarioEntidadActivo(request);

            // Comprobamos si el RegistroEntrada tiene el estado anulado
            if(!registroEntrada.getEstado().equals(RegwebConstantes.ESTADO_ANULADO)){

                Mensaje.saveMessageError(request, getMessage("registro.activar.error"));
                return "redirect:/registroEntrada/list";
            }

            // Comprobamos que el usuario dispone del permiso para Modificar el Registro
            if(!permisoLibroUsuarioEjb.tienePermiso(usuarioEntidad.getId(), registroEntrada.getLibro().getId(),RegwebConstantes.PERMISO_MODIFICACION_REGISTRO_ENTRADA)){
                log.info("Aviso: No dispone de los permisos necesarios para editar el registro");
                Mensaje.saveMessageAviso(request, getMessage("aviso.registro.editar"));

                return "redirect:/registroEntrada/list";
            }

            // Activamos el RegistroEntrada
            registroEntradaEjb.activarRegistroEntrada(registroEntrada, usuarioEntidad);

            Mensaje.saveMessageInfo(request, getMessage("registroEntrada.activar"));

        } catch (Exception e) {
            Mensaje.saveMessageError(request, getMessage("regweb.error.registro"));
            e.printStackTrace();
        }

        return "redirect:/registroEntrada/list";
    }

    /**
     * Visar un {@link es.caib.regweb3.model.RegistroEntrada}
     */
    @RequestMapping(value = "/{idRegistro}/visar")
    public String visarRegistroEntrada(@PathVariable Long idRegistro, HttpServletRequest request) {

        try {

            RegistroEntrada registroEntrada = registroEntradaEjb.findById(idRegistro);
            UsuarioEntidad usuarioEntidad = getUsuarioEntidadActivo(request);

            // Comprobamos si el RegistroEntrada tiene el estado Pendiente de Visar
            if(!registroEntrada.getEstado().equals(RegwebConstantes.ESTADO_PENDIENTE_VISAR)){

                Mensaje.saveMessageError(request, getMessage("registro.visar.error"));
                return "redirect:/registroEntrada/list";
            }

            // Comprobamos si el UsuarioEntidad tiene permisos para visar el Registro Entrada
            if(!permisoLibroUsuarioEjb.isAdministradorLibro(usuarioEntidad.getId(), registroEntrada.getLibro().getId())){

                Mensaje.saveMessageError(request, getMessage("aviso.usuario.visar"));
                return "redirect:/registroEntrada/list";
            }

            // Visar el RegistroEntrada
            registroEntradaEjb.visarRegistroEntrada(registroEntrada,usuarioEntidad);

            Mensaje.saveMessageInfo(request, getMessage("registroEntrada.visar.ok"));

        } catch (Exception e) {
            Mensaje.saveMessageError(request, getMessage("regweb.error.registro"));
            e.printStackTrace();
        }

        return "redirect:/avisos/pendientesVisar/Entrada";
    }

    /**
     * Tramitar un {@link es.caib.regweb3.model.RegistroEntrada}
     */
    @RequestMapping(value = "/{idRegistro}/tramitar")
    public String tramitarRegistroEntrada(@PathVariable Long idRegistro, HttpServletRequest request) {

        try {

            RegistroEntrada registroEntrada = registroEntradaEjb.findById(idRegistro);
            UsuarioEntidad usuarioEntidad = getUsuarioEntidadActivo(request);

            // Comprobamos si el RegistroEntrada tiene el estado Válido
            if(!registroEntrada.getEstado().equals(RegwebConstantes.ESTADO_VALIDO)){

                Mensaje.saveMessageError(request, getMessage("registroEntrada.tramitar.error"));
                return "redirect:/registroEntrada/list";
            }

            // Comprobamos que el RegistroEntrada es un OficioRemision
            if(registroEntradaEjb.isOficioRemisionInterno(idRegistro)){
                Mensaje.saveMessageError(request, getMessage("registroEntrada.tramitar.error"));
                return "redirect:/registroEntrada/list";
            }


            //TODO aquí hay que hacer la gestión con el plugin que tengamos configurado



            registroEntradaEjb.tramitarRegistroEntrada(registroEntrada, usuarioEntidad);

            Mensaje.saveMessageInfo(request, getMessage("registroEntrada.tramitar.ok"));

        } catch (Exception e) {
            Mensaje.saveMessageError(request, getMessage("regweb.error.registro"));
            e.printStackTrace();
        }

        return "redirect:/registroEntrada/list";
    }


    /**
     * Carga el formulario para ver el detalle de un {@link es.caib.regweb3.model.RegistroEntrada}
     */
    @RequestMapping(value = "/{idRegistro}/sello", method = RequestMethod.GET)
    public ModelAndView sello(@PathVariable Long idRegistro, HttpServletRequest request) throws Exception {

        ModelAndView mav = new ModelAndView("sello");

        RegistroEntrada registroEntrada = registroEntradaEjb.findById(idRegistro);

        mav.addObject("registro", registroEntrada);
        mav.addObject("x", request.getParameter("x"));
        mav.addObject("y", request.getParameter("y"));
        mav.addObject("orientacion", request.getParameter("orientacion"));
        mav.addObject("tipoRegistro", getMessage("informe.entrada"));

        return mav;
    }


    /**
     * Crea el xml de un {@link es.caib.regweb3.model.RegistroEntrada}
     */
    /*@RequestMapping(value = "/{idRegistro}/xml", method = RequestMethod.GET)
    public String xmlRegistroEntrada(@PathVariable Long idRegistro) throws Exception {

        RegistroEntrada registroEntrada = registroEntradaEjb.findById(idRegistro);
        
        FicheroIntercambioSICRES3 fiSICRES3 =  sirEjb.writeFicheroIntercambioSICRES3(registroEntrada);
        String xml = SirUtils.marshallObject(fiSICRES3);

        Archivo archivo = new Archivo();
        archivo.setMime("application/xml");
        archivo.setNombre(registroEntrada.getRegistroDetalle().getExtracto());
        archivo.setTamano(Long.valueOf(xml.getBytes().length));

        archivo = archivoEjb.persist(archivo);
        FileSystemManager.crearArchivo(xml.getBytes(), archivo.getId());

        return "redirect:/registroEntrada/list";
    }*/





    @InitBinder("registroEntradaBusqueda")
    public void registroEntradaBusqueda(WebDataBinder binder) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        CustomDateEditor dateEditor = new CustomDateEditor(sdf, true);
        binder.registerCustomEditor(java.util.Date.class,dateEditor);
    }

    
   

}