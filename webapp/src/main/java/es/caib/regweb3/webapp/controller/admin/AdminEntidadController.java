package es.caib.regweb3.webapp.controller.admin;

import es.caib.regweb3.model.*;
import es.caib.regweb3.persistence.ejb.*;
import es.caib.regweb3.persistence.utils.Paginacion;
import es.caib.regweb3.persistence.utils.RegistroUtils;
import es.caib.regweb3.utils.Configuracio;
import es.caib.regweb3.utils.RegwebConstantes;
import es.caib.regweb3.utils.StringUtils;
import es.caib.regweb3.utils.TimeUtils;
import es.caib.regweb3.webapp.controller.registro.AbstractRegistroCommonListController;
import es.caib.regweb3.webapp.form.AnularForm;
import es.caib.regweb3.webapp.form.RegistroEntradaBusqueda;
import es.caib.regweb3.webapp.form.RegistroSalidaBusqueda;
import es.caib.regweb3.webapp.utils.Mensaje;
import es.caib.regweb3.webapp.validator.RegistroEntradaBusquedaValidator;
import es.caib.regweb3.webapp.validator.RegistroSalidaBusquedaValidator;
import org.apache.log4j.Logger;
import org.fundaciobit.genapp.common.i18n.I18NException;
import org.fundaciobit.genapp.common.i18n.I18NValidationException;
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
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping(value = "/adminEntidad")
public class AdminEntidadController extends AbstractRegistroCommonListController {

    protected final Logger log = Logger.getLogger(getClass());

    @Autowired
    private RegistroEntradaBusquedaValidator registroEntradaBusquedaValidator;

    @Autowired
    private RegistroSalidaBusquedaValidator registroSalidaBusquedaValidator;

    @EJB(mappedName = "regweb3/LibroEJB/local")
    private LibroLocal libroEjb;

    @EJB(mappedName = "regweb3/HistoricoRegistroEntradaEJB/local")
    private HistoricoRegistroEntradaLocal historicoRegistroEntradaEjb;

    @EJB(mappedName = "regweb3/HistoricoRegistroSalidaEJB/local")
    private HistoricoRegistroSalidaLocal historicoRegistroSalidaEjb;

    @EJB(mappedName = "regweb3/AnexoEJB/local")
    private AnexoLocal anexoEjb;

    @EJB(mappedName = "regweb3/AsientoRegistralEJB/local")
    private AsientoRegistralLocal asientoRegistralEjb;

    @EJB(mappedName = "regweb3/PersonaEJB/local")
    private PersonaLocal personaEjb;

    @EJB(mappedName = "regweb3/InteresadoEJB/local")
    private InteresadoLocal interesadoEjb;


    /**
     * Listado de registros de entrada
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/registroEntrada/list", method = RequestMethod.GET)
    public String list(Model model, HttpServletRequest request)throws Exception {

        // Obtenemos los Libros de la Entidad
        List<Libro> librosConsulta = libroEjb.getLibrosEntidad(getEntidadActiva(request).getId());

        RegistroEntradaBusqueda registroEntradaBusqueda = new RegistroEntradaBusqueda(new RegistroEntrada(),1);
        registroEntradaBusqueda.setFechaInicio(new Date());
        registroEntradaBusqueda.setFechaFin(new Date());

        model.addAttribute("librosConsulta", librosConsulta);
        model.addAttribute("registroEntradaBusqueda", registroEntradaBusqueda);
        model.addAttribute("organosDestino", organismoEjb.getAllByEntidad(getEntidadActiva(request).getId()));
        model.addAttribute("oficinasRegistro",  oficinaEjb.findByEntidadByEstado(getEntidadActiva(request).getId(), RegwebConstantes.ESTADO_ENTIDAD_VIGENTE));

        // Obtenemos los usuarios de la Entidad
        model.addAttribute("usuariosEntidad", usuarioEntidadEjb.findByEntidad(getEntidadActiva(request).getId()));

        return "registroEntrada/registroEntradaListAdmin";
    }

    /**
     * Realiza la busqueda de {@link es.caib.regweb3.model.RegistroEntrada} según los parametros del formulario
     */
    @RequestMapping(value = "/registroEntrada/busqueda", method = RequestMethod.GET)
    public ModelAndView busqueda(@ModelAttribute RegistroEntradaBusqueda busqueda, BindingResult result, HttpServletRequest request, HttpServletResponse response)throws Exception {

        ModelAndView mav = new ModelAndView("registroEntrada/registroEntradaListAdmin", result.getModel());

        Entidad entidadActiva = getEntidadActiva(request);

        // Obtenemos los Libros de la Entidad
        List<Libro> librosConsulta = libroEjb.getLibrosEntidad(entidadActiva.getId());

        List<Organismo> organosDestino = organismoEjb.getAllByEntidad(getEntidadActiva(request).getId());
        List<Oficina> oficinasRegistro = oficinaEjb.findByEntidadByEstado(getEntidadActiva(request).getId(),RegwebConstantes.ESTADO_ENTIDAD_VIGENTE);
        List<UsuarioEntidad> usuariosEntidad = usuarioEntidadEjb.findByEntidad(getEntidadActiva(request).getId());
        UsuarioEntidad usuarioEntidad = getUsuarioEntidadActivo(request);

        registroEntradaBusquedaValidator.validate(busqueda, result);

        // Si hay errores volvemos a la vista del formulario
        if (result.hasErrors()) {
            mav.addObject("errors", result.getAllErrors());
            mav.addObject("usuariosEntidad",usuariosEntidad);
            mav.addObject("librosConsulta", librosConsulta);
            mav.addObject("registroEntradaBusqueda", busqueda);
            mav.addObject("organosDestino", organosDestino);
            mav.addObject("oficinasRegistro",  oficinasRegistro);

            return mav;

        }else { // Si no hay errores realizamos la búsqueda

            RegistroEntrada registroEntrada = busqueda.getRegistroEntrada();

            // Ponemos la hora 23:59 a la fecha fin
            Date fechaFin = RegistroUtils.ajustarHoraBusqueda(busqueda.getFechaFin());

            /* Solución a los problemas de encoding del formulario GET */
            String nombreInteresado = new String(busqueda.getInteressatNom().getBytes("ISO-8859-1"), "UTF-8");
            String apellido1Interesado = new String(busqueda.getInteressatLli1().getBytes("ISO-8859-1"), "UTF-8");
            String apellido2Interesado = new String(busqueda.getInteressatLli2().getBytes("ISO-8859-1"), "UTF-8");
            Paginacion paginacion = registroEntradaConsultaEjb.busqueda(busqueda.getPageNumber(), null,busqueda.getFechaInicio(), fechaFin, registroEntrada, nombreInteresado, apellido1Interesado, apellido2Interesado, busqueda.getInteressatDoc(), busqueda.getOrganDestinatari(), false, null, busqueda.getUsuario(), entidadActiva.getId());

            busqueda.setPageNumber(1);
            mav.addObject("paginacion", paginacion);

            // Alta en tabla LOPD
            lopdEjb.insertarRegistros(paginacion, usuarioEntidad.getId(), RegwebConstantes.REGISTRO_ENTRADA, RegwebConstantes.LOPD_LISTADO);
        }

        // Comprobamos si el Organismo destinatario es externo, para añadirlo a la lista.
        if (StringUtils.isNotEmpty(busqueda.getOrganDestinatari())) {
            Organismo org = organismoEjb.findByCodigoEntidad(busqueda.getOrganDestinatari(), usuarioEntidad.getEntidad().getId());
            if(org== null || !organosDestino.contains(org)){ //Es organismo externo, lo añadimos a la lista
                organosDestino.add(new Organismo(null,busqueda.getOrganDestinatari(),new String(busqueda.getOrganDestinatariNom().getBytes("ISO-8859-1"), "UTF-8") ));
            }
        }

        mav.addObject("organosDestino",  organosDestino);
        mav.addObject("usuariosEntidad",usuariosEntidad);
        mav.addObject("librosConsulta", librosConsulta);
        mav.addObject("oficinasRegistro", oficinasRegistro);
        mav.addObject("registroEntradaBusqueda", busqueda);
        mav.addObject("organDestinatari", busqueda.getOrganDestinatari());

        /* Solucion a los problemas de encoding del formulario GET */
        busqueda.getRegistroEntrada().getRegistroDetalle().setExtracto(new String(busqueda.getRegistroEntrada().getRegistroDetalle().getExtracto().getBytes("ISO-8859-1"), "UTF-8"));
        busqueda.setInteressatNom(new String(busqueda.getInteressatNom().getBytes("ISO-8859-1"), "UTF-8"));
        busqueda.setInteressatLli1(new String(busqueda.getInteressatLli1().getBytes("ISO-8859-1"), "UTF-8"));
        busqueda.setInteressatLli2(new String(busqueda.getInteressatLli2().getBytes("ISO-8859-1"), "UTF-8"));
        busqueda.setOrganDestinatariNom(new String(busqueda.getOrganDestinatariNom().getBytes("ISO-8859-1"), "UTF-8"));

        return mav;
    }

    /**
     * Carga el formulario para ver el detalle de un {@link es.caib.regweb3.model.RegistroEntrada}
     */
    @RequestMapping(value = "/registroEntrada/{idRegistro}/detalle", method = RequestMethod.GET)
    public String detalleRegistroEntrada(@PathVariable Long idRegistro, Model model, HttpServletRequest request, HttpServletResponse response) throws Exception, I18NException, I18NValidationException {

        RegistroEntrada registro = registroEntradaEjb.findById(idRegistro);

        Entidad entidadActiva = getEntidadActiva(request);
        UsuarioEntidad usuarioEntidad = getUsuarioEntidadActivo(request);

        model.addAttribute("registro",registro);
        model.addAttribute("entidadActiva", entidadActiva);
        model.addAttribute("anularForm", new AnularForm());

        // Permisos
        Boolean tieneJustificante = registro.getRegistroDetalle().getTieneJustificante();
        model.addAttribute("tieneJustificante", tieneJustificante);

        // Solo si no es una reserva de número
        if(!registro.getEstado().equals(RegwebConstantes.REGISTRO_RESERVA)){

            // Justificante
            if(tieneJustificante){

                model.addAttribute("idJustificante", anexoEjb.getIdJustificante(registro.getRegistroDetalle().getId()));
                String urlValidacion = anexoEjb.getUrlValidation(registro.getRegistroDetalle().getJustificante(),entidadActiva.getId());
                model.addAttribute("tieneUrlValidacion", StringUtils.isNotEmpty(urlValidacion));
            }

            // Historicos
            model.addAttribute("historicos", historicoRegistroEntradaEjb.getByRegistroEntrada(idRegistro));

            // Trazabilidad
            model.addAttribute("trazabilidades", trazabilidadEjb.getByRegistroEntrada(registro.getId()));
        }

        // Alta en tabla LOPD
        lopdEjb.altaLopd(registro.getNumeroRegistro(), registro.getFecha(), registro.getLibro().getId(), usuarioEntidad.getId(), RegwebConstantes.REGISTRO_ENTRADA, RegwebConstantes.LOPD_CONSULTA);

        return "registroEntrada/registroEntradaDetalleAdmin";
    }

    /**
     * Marca como Distribuido y genera el Justificante del {@link es.caib.regweb3.model.RegistroEntrada}
     */
    @RequestMapping(value = "/registroEntrada/{idRegistro}/procesar", method = RequestMethod.GET)
    public String procesarRegistroEntrada(@PathVariable Long idRegistro, Model model, HttpServletRequest request, HttpServletResponse response) throws Exception, I18NException, I18NValidationException {

        RegistroEntrada registroEntrada = registroEntradaEjb.findById(idRegistro);

        UsuarioEntidad usuarioEntidad = getUsuarioEntidadActivo(request);

        try{

            if(registroEntrada.getEstado().equals(RegwebConstantes.REGISTRO_VALIDO) ||
                    registroEntrada.getEstado().equals(RegwebConstantes.REGISTRO_DISTRIBUYENDO)){

                //Cola elemento = colaEjb.findByIdObjetoEstado(registroEntrada.getId(), usuarioEntidad.getEntidad().getId(), RegwebConstantes.COLA_ESTADO_PROCESADO);

                //if(elemento != null){ todo: Añadir esta validación al solucionar el bug de Distribuyendo

                    if (!registroEntrada.getRegistroDetalle().getTieneJustificante()) {
                        asientoRegistralEjb.crearJustificante(registroEntrada.getUsuario(),registroEntrada, RegwebConstantes.REGISTRO_ENTRADA, Configuracio.getDefaultLanguage());
                    }

                    registroEntradaEjb.marcarDistribuido(registroEntrada, usuarioEntidad);

                    Mensaje.saveMessageInfo(request, getMessage("registroEntrada.procesar.ok"));
               // }
            }

        }catch (Exception e){
            e.printStackTrace();
            Mensaje.saveMessageInfo(request, getMessage("registroEntrada.procesar.error"));
        }

        return "redirect:/adminEntidad/registroEntrada/"+idRegistro+"/detalle";
    }

    /**
     * Listado de registros de salida
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/registroSalida/list", method = RequestMethod.GET)
    public String listRegistroSalida(Model model, HttpServletRequest request)throws Exception {

        // Obtenemos los Libros de la Entidad
        List<Libro> librosConsulta = libroEjb.getLibrosEntidad(getEntidadActiva(request).getId());

        RegistroSalidaBusqueda registroSalidaBusqueda = new RegistroSalidaBusqueda(new RegistroSalida(),1);
        registroSalidaBusqueda.setFechaInicio(new Date());
        registroSalidaBusqueda.setFechaFin(new Date());

        model.addAttribute("librosConsulta", librosConsulta);
        model.addAttribute("registroSalidaBusqueda", registroSalidaBusqueda);
        model.addAttribute("organosOrigen", organismoEjb.getAllByEntidad(getEntidadActiva(request).getId()));
        model.addAttribute("oficinasRegistro",  oficinaEjb.findByEntidadByEstado(getEntidadActiva(request).getId(), RegwebConstantes.ESTADO_ENTIDAD_VIGENTE));

        // Obtenemos los usuarios de la Entidad
        model.addAttribute("usuariosEntidad", usuarioEntidadEjb.findByEntidad(getEntidadActiva(request).getId()));

        return "registroSalida/registroSalidaListAdmin";
    }

    /**
     * Realiza la busqueda de {@link es.caib.regweb3.model.RegistroSalida} según los parametros del formulario
     */
    @RequestMapping(value = "/registroSalida/busqueda", method = RequestMethod.GET)
    public ModelAndView busquedaRegistroSalida(@ModelAttribute RegistroSalidaBusqueda busqueda, BindingResult result, HttpServletRequest request)throws Exception {

        ModelAndView mav = new ModelAndView("registroSalida/registroSalidaListAdmin", result.getModel());

        Entidad entidadActiva = getEntidadActiva(request);

        // Obtenemos los Libros de la Entidad
        List<Libro> librosConsulta = libroEjb.getLibrosEntidad(entidadActiva.getId());

        List<Organismo> organoOrigen = organismoEjb.getAllByEntidad(getEntidadActiva(request).getId());
        List<Oficina> oficinasRegistro = oficinaEjb.findByEntidadByEstado(getEntidadActiva(request).getId(),RegwebConstantes.ESTADO_ENTIDAD_VIGENTE);
        List<UsuarioEntidad> usuariosEntidad = usuarioEntidadEjb.findByEntidad(getEntidadActiva(request).getId());
        UsuarioEntidad usuarioEntidad = getUsuarioEntidadActivo(request);

        registroSalidaBusquedaValidator.validate(busqueda, result);

        // Si hay errores volvemos a la vista del formulario
        if (result.hasErrors()) {
            mav.addObject("errors", result.getAllErrors());
            mav.addObject("usuariosEntidad",usuariosEntidad);
            mav.addObject("librosConsulta", librosConsulta);
            mav.addObject("registroSalidaBusqueda", busqueda);
            mav.addObject("organoOrigen", organoOrigen);
            mav.addObject("oficinasRegistro",  oficinasRegistro);

            return mav;

        }else { // Si no hay errores realizamos la búsqueda

            RegistroSalida registroSalida = busqueda.getRegistroSalida();

            // Ponemos la hora 23:59 a la fecha fin
            Date fechaFin = RegistroUtils.ajustarHoraBusqueda(busqueda.getFechaFin());

            /* Solución a los problemas de encoding del formulario GET */
            String nombreInteresado = new String(busqueda.getInteressatNom().getBytes("ISO-8859-1"), "UTF-8");
            String apellido1Interesado = new String(busqueda.getInteressatLli1().getBytes("ISO-8859-1"), "UTF-8");
            String apellido2Interesado = new String(busqueda.getInteressatLli2().getBytes("ISO-8859-1"), "UTF-8");
            Paginacion paginacion = registroSalidaConsultaEjb.busqueda(busqueda.getPageNumber(),null, busqueda.getFechaInicio(), fechaFin, registroSalida, nombreInteresado, apellido1Interesado, apellido2Interesado, busqueda.getInteressatDoc(), busqueda.getOrganOrigen(), false, null, busqueda.getUsuario(), entidadActiva.getId());

            busqueda.setPageNumber(1);
            mav.addObject("paginacion", paginacion);

            // Alta en tabla LOPD
            lopdEjb.insertarRegistros(paginacion, usuarioEntidad.getId(), RegwebConstantes.REGISTRO_SALIDA, RegwebConstantes.LOPD_LISTADO);
        }

        mav.addObject("organoOrigen",  organoOrigen);
        mav.addObject("usuariosEntidad",usuariosEntidad);
        mav.addObject("librosConsulta", librosConsulta);
        mav.addObject("oficinasRegistro", oficinasRegistro);
        mav.addObject("registroEntradaBusqueda", busqueda);

        /* Solucion a los problemas de encoding del formulario GET */
        busqueda.getRegistroSalida().getRegistroDetalle().setExtracto(new String(busqueda.getRegistroSalida().getRegistroDetalle().getExtracto().getBytes("ISO-8859-1"), "UTF-8"));
        busqueda.setInteressatNom(new String(busqueda.getInteressatNom().getBytes("ISO-8859-1"), "UTF-8"));
        busqueda.setInteressatLli1(new String(busqueda.getInteressatLli1().getBytes("ISO-8859-1"), "UTF-8"));
        busqueda.setInteressatLli2(new String(busqueda.getInteressatLli2().getBytes("ISO-8859-1"), "UTF-8"));

        return mav;
    }

    /**
     * Carga el formulario para ver el detalle de un {@link es.caib.regweb3.model.RegistroSalida}
     */
    @RequestMapping(value = "/registroSalida/{idRegistro}/detalle", method = RequestMethod.GET)
    public String detalleRegistroSalida(@PathVariable Long idRegistro, Model model, HttpServletRequest request) throws Exception, I18NException, I18NValidationException {

        RegistroSalida registro = registroSalidaEjb.findById(idRegistro);

        Entidad entidadActiva = getEntidadActiva(request);
        UsuarioEntidad usuarioEntidad = getUsuarioEntidadActivo(request);

        model.addAttribute("registro",registro);
        model.addAttribute("entidadActiva", entidadActiva);
        model.addAttribute("anularForm", new AnularForm());

        // Permisos
        Boolean tieneJustificante = registro.getRegistroDetalle().getTieneJustificante();
        model.addAttribute("tieneJustificante", tieneJustificante);

        // Justificante
        if(tieneJustificante){
            Anexo justificante = registro.getRegistroDetalle().getJustificante();

            model.addAttribute("idJustificante", justificante.getId());
            String urlValidacion = anexoEjb.getUrlValidation(justificante,entidadActiva.getId());
            model.addAttribute("tieneUrlValidacion", StringUtils.isNotEmpty(urlValidacion));
        }

        // Historicos
        model.addAttribute("historicos", historicoRegistroSalidaEjb.getByRegistroSalida(idRegistro));

        // Trazabilidad
        model.addAttribute("trazabilidades", trazabilidadEjb.getByRegistroSalida(registro.getId()));


        // Alta en tabla LOPD
        lopdEjb.altaLopd(registro.getNumeroRegistro(), registro.getFecha(), registro.getLibro().getId(), usuarioEntidad.getId(), RegwebConstantes.REGISTRO_SALIDA, RegwebConstantes.LOPD_CONSULTA);

        return "registroSalida/registroSalidaDetalleAdmin";
    }

    /**
     * Capitalizar todas las {@link es.caib.regweb3.model.Persona} de un tipo
     */
    @RequestMapping(value = "/capitalizarPersonas/{tipoPersona}")
    public String capitalizarPersonas(@PathVariable Long tipoPersona, HttpServletRequest request) {

        try {

            long inicio = System.currentTimeMillis();

            Entidad entidad = getEntidadActiva(request);

            if(tipoPersona.equals(RegwebConstantes.TIPO_PERSONA_JURIDICA)){
                personaEjb.capitalizarPersonasJuridicas(entidad.getId());

            }else if(tipoPersona.equals(RegwebConstantes.TIPO_PERSONA_FISICA)){
                personaEjb.capitalizarPersonasFisicas(entidad.getId());
            }

            Mensaje.saveMessageInfo(request, "Se han capitalizado las personas correctamente en " + TimeUtils.formatElapsedTime(System.currentTimeMillis() - inicio));

        } catch (Exception e) {
            Mensaje.saveMessageError(request, "Error al capitalizar personas");
            e.printStackTrace();
        }

        return "redirect:/inici";
    }

    /**
     * Capitalizar todas las {@link es.caib.regweb3.model.Interesado} de un tipo
     */
    @RequestMapping(value = "/capitalizarInteresados/{tipoInteresado}")
    public String capitalizarInteresados(@PathVariable Long tipoInteresado, HttpServletRequest request) {

        try {

            long inicio = System.currentTimeMillis();

            if(tipoInteresado.equals(RegwebConstantes.TIPO_INTERESADO_PERSONA_JURIDICA)){
                interesadoEjb.capitalizarInteresadosJuridicos();

            }else if(tipoInteresado.equals(RegwebConstantes.TIPO_INTERESADO_PERSONA_FISICA)){
                interesadoEjb.capitalizarInteresadosFisicas();
            }

            Mensaje.saveMessageInfo(request, "Se han capitalizado los interesados correctamente en " + TimeUtils.formatElapsedTime(System.currentTimeMillis() - inicio));

        } catch (Exception e) {
            Mensaje.saveMessageError(request, "Error al capitalizar interesados");
            e.printStackTrace();
        }

        return "redirect:/inici";
    }


    @InitBinder("registroEntradaBusqueda")
    public void registroEntradaBusqueda(WebDataBinder binder) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        CustomDateEditor dateEditor = new CustomDateEditor(sdf, true);
        binder.registerCustomEditor(java.util.Date.class,dateEditor);
    }

    @InitBinder("registroSalidaBusqueda")
    public void registroSalidaBusqueda(WebDataBinder binder) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        CustomDateEditor dateEditor = new CustomDateEditor(sdf, true);
        binder.registerCustomEditor(java.util.Date.class,dateEditor);
    }
}
