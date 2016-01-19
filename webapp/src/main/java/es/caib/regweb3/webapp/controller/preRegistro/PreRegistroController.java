package es.caib.regweb3.webapp.controller.preRegistro;

import es.caib.regweb3.model.*;
import es.caib.regweb3.persistence.ejb.*;
import es.caib.regweb3.persistence.utils.Paginacion;
import es.caib.regweb3.sir.ws.api.manager.RegistroManager;
import es.caib.regweb3.sir.ws.api.manager.impl.RegistroManagerImpl;
import es.caib.regweb3.utils.RegwebConstantes;
import es.caib.regweb3.webapp.controller.BaseController;
import es.caib.regweb3.webapp.form.PreRegistroBusquedaForm;
import es.caib.regweb3.webapp.form.RegistrarForm;
import es.caib.regweb3.webapp.utils.Mensaje;
import org.fundaciobit.genapp.common.i18n.I18NException;
import org.fundaciobit.genapp.common.i18n.I18NValidationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.ejb.EJB;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created 12/12/14 9:45
 * Controller que gestiona todas las operaciones con {@link es.caib.regweb3.model.PreRegistro}
 * @author jpernia
 */
@Controller
@RequestMapping(value = "/preRegistro")
@SessionAttributes(types = PreRegistro.class)
public class PreRegistroController extends BaseController {

    @EJB(mappedName = "regweb3/PreRegistroUtilsEJB/local")
    private PreRegistroUtilsLocal preRegistroUtils;

    @EJB(mappedName = "regweb3/PreRegistroEJB/local")
    public PreRegistroLocal preRegistroEjb;

    @EJB(mappedName = "regweb3/PersonaEJB/local")
    public PersonaLocal personaEjb;

    @EJB(mappedName = "regweb3/CatPaisEJB/local")
    public CatPaisLocal catPaisEjb;

    @EJB(mappedName = "regweb3/CatProvinciaEJB/local")
    public CatProvinciaLocal catProvinciaEjb;

    @EJB(mappedName = "regweb3/CatComunidadAutonomaEJB/local")
    public CatComunidadAutonomaLocal catComunidadAutonomaEjb;

    @EJB(mappedName = "regweb3/CatNivelAdministracionEJB/local")
    public CatNivelAdministracionLocal catNivelAdministracionEjb;

    @EJB(mappedName = "regweb3/AnexoEJB/local")
    public AnexoLocal anexoEjb;

    @EJB(mappedName = "regweb3/TipoAsuntoEJB/local")
    public TipoAsuntoLocal tipoAsuntoEjb;

    RegistroManager registroManager = new RegistroManagerImpl();

    /**
     * Listado de todos los PreRegistros
     */
    @RequestMapping(value = "", method = RequestMethod.GET)
    public String listado() {
        return "redirect:/preRegistro/list";
    }

    /**
     * Listado de PreRegistros
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public ModelAndView list(Model model, HttpServletRequest request)throws Exception {

        ModelAndView mav = new ModelAndView("preRegistro/preRegistroList");

        PreRegistroBusquedaForm preRegistroBusquedaForm = new PreRegistroBusquedaForm(new PreRegistro(),1);        
        model.addAttribute("estados", RegwebConstantes.ESTADOS_PREREGISTRO);
        model.addAttribute("preRegistroBusqueda", preRegistroBusquedaForm);

        return mav;
    }

    /**
     * Realiza la busqueda de {@link es.caib.regweb3.model.PreRegistro} según los parametros del formulario
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    public ModelAndView list(@ModelAttribute PreRegistroBusquedaForm busqueda, HttpServletRequest request)throws Exception {

        ModelAndView mav = new ModelAndView("preRegistro/preRegistroList");

        PreRegistro preRegistro = busqueda.getPreRegistro();

        Oficina oficinaActiva = getOficinaActiva(request);

        Paginacion paginacion = preRegistroEjb.busqueda(busqueda.getPageNumber(), busqueda.getAnyo(), preRegistro, oficinaActiva.getCodigo(), busqueda.getEstado());

        busqueda.setPageNumber(1);

        mav.addObject("estados", RegwebConstantes.ESTADOS_PREREGISTRO);
        mav.addObject("paginacion", paginacion);
        mav.addObject("preRegistroBusqueda", busqueda);

        return mav;

    }


    /**
     * Realiza la busqueda de Listado de PreRegistro pendientes de procesar
     */
    @RequestMapping(value = "/preRegistrosPendientesProcesar", method = RequestMethod.POST)
    public ModelAndView preRegistrosPendientesProcesar(@ModelAttribute PreRegistroBusquedaForm busqueda, HttpServletRequest request)throws Exception {

        ModelAndView mav = new ModelAndView("preRegistro/preRegistroList");

        PreRegistro preRegistro = busqueda.getPreRegistro();

        Oficina oficinaActiva = getOficinaActiva(request);

        // Obtenemos los PreRegistros, pendientes de procesar
        Paginacion paginacion = preRegistroEjb.busqueda(busqueda.getPageNumber(), busqueda.getAnyo(), preRegistro, oficinaActiva.getCodigo(), busqueda.getEstado());

        busqueda.setPageNumber(1);
        mav.addObject("paginacion", paginacion);
        mav.addObject("preRegistroBusqueda", busqueda);

        return mav;

    }

    /**
     * Carga el formulario para ver el detalle de un {@link es.caib.regweb3.model.PreRegistro}
     */
    @RequestMapping(value = "/{idPreRegistro}/detalle", method = RequestMethod.GET)
    public String detallePreRegistro(@PathVariable Long idPreRegistro, Model model, HttpServletRequest request) throws Exception {

        PreRegistro preRegistro = preRegistroEjb.findById(idPreRegistro);

        // Comprobamos que el PreRegistro tiene como destino nuestra Oficina Activa
        if(preRegistro.getCodigoEntidadRegistralDestino().equals(getOficinaActiva(request).getCodigo())){
            model.addAttribute("preRegistro",preRegistro);

            Entidad entidad = getEntidadActiva(request);

            //Obtenemos los libros de Registro según si el PreRegistro es de Entrada o de Salida
            List<Libro> libros = null;
            if(preRegistro.getTipoRegistro().equals(RegwebConstantes.PREREGISTRO_ENTRADA.toString())){
                 libros = getLibrosRegistroEntrada(request);
            }
            if(preRegistro.getTipoRegistro().equals(RegwebConstantes.PREREGISTRO_SALIDA.toString())){
                libros = getLibrosRegistroSalida(request);
            }
            model.addAttribute("libros",libros);

            RegistrarForm registrarForm = new RegistrarForm();
            model.addAttribute("registrarForm", registrarForm);

            // Interesados
            model.addAttribute("personasFisicas",personaEjb.getAllbyEntidadTipo(entidad.getId(), RegwebConstantes.TIPO_PERSONA_FISICA));
            model.addAttribute("personasJuridicas",personaEjb.getAllbyEntidadTipo(entidad.getId(), RegwebConstantes.TIPO_PERSONA_JURIDICA));
            model.addAttribute("tiposInteresado",RegwebConstantes.TIPOS_INTERESADO);
            model.addAttribute("tiposPersona",RegwebConstantes.TIPOS_PERSONA);
            model.addAttribute("paises",catPaisEjb.getAll());
            model.addAttribute("provincias",catProvinciaEjb.getAll());
            model.addAttribute("canalesNotificacion",RegwebConstantes.CANALES_NOTIFICACION);
            model.addAttribute("tiposDocumento",RegwebConstantes.TIPOS_DOCUMENTOID);
            model.addAttribute("nivelesAdministracion",catNivelAdministracionEjb.getAll());
            model.addAttribute("comunidadesAutonomas",catComunidadAutonomaEjb.getAll());

            // Anexos
            model.addAttribute("anexos", anexoEjb.getByRegistroDetalle(preRegistro.getRegistroDetalle().getId()));
        }

        return "preRegistro/preRegistroDetalle";
    }


    /**
     * Procesa {@link es.caib.regweb3.model.PreRegistro}, creando un RegistroEntrada
     */
    @RequestMapping(value = "/{idPreRegistro}/registrar/{idLibro}/{idIdioma}/{idTipoAsunto}", method = RequestMethod.GET)
    public String confirmarPreRegistro(@PathVariable Long idPreRegistro,
        @PathVariable Long idLibro, @PathVariable Long idIdioma, @PathVariable Long idTipoAsunto, Model model, HttpServletRequest request)
            throws Exception, I18NException, I18NValidationException {

        PreRegistro preRegistro = preRegistroEjb.findById(idPreRegistro);
        Oficina oficinaActiva = getOficinaActiva(request);
        UsuarioEntidad usuarioEntidad = getUsuarioEntidadActivo(request);
        RegistroEntrada registroEntrada = new RegistroEntrada();
        RegistroSalida registroSalida = new RegistroSalida();

        // Comprobamos si ya ha sido confirmado
        if(preRegistro.getEstado().equals(RegwebConstantes.ESTADO_PREREGISTRO_PROCESADO)){
            Mensaje.saveMessageError(request, getMessage("preRegistro.procesado.error"));
            return "redirect:/preRegistro/preRegistroProcesar";
        }

        // Procesa el PreRegistro
        String variableReturn = "redirect:/preRegistro/"+idPreRegistro+"/detalle";
        try{
            if(preRegistro.getTipoRegistro().equals(RegwebConstantes.PREREGISTRO_ENTRADA.toString())) {
                registroEntrada = preRegistroUtils.procesarPreRegistroEntrada(preRegistro, usuarioEntidad, oficinaActiva, idLibro, idIdioma, idTipoAsunto);
                model.addAttribute("registroEntrada",registroEntrada);
                variableReturn = "redirect:/registroEntrada/" + registroEntrada.getId() + "/detalle";
            }
            if(preRegistro.getTipoRegistro().equals(RegwebConstantes.PREREGISTRO_SALIDA.toString())) {
                registroSalida = preRegistroUtils.procesarPreRegistroSalida(preRegistro, usuarioEntidad, oficinaActiva, idLibro, idIdioma, idTipoAsunto);
                model.addAttribute("registroSalida",registroSalida);
                variableReturn = "redirect:/registroSalida/" + registroSalida.getId() + "/detalle";
            }

            //todo: Crear función genérica para enviar mensajes DeMensaje
            registroManager.enviarMensajeConfirmacion(preRegistro);

        }catch (Exception e){
            Mensaje.saveMessageError(request, getMessage("preRegistro.error.confirmacion"));
            e.printStackTrace();
        }

        return variableReturn;
    }

    @ModelAttribute("idiomas")
    public Long[] idiomas() throws Exception {
        return RegwebConstantes.IDIOMAS_REGISTRO;
    }

    @ModelAttribute("tiposAsunto")
    public List<TipoAsunto> tiposAsunto(HttpServletRequest request) throws Exception {

        Entidad entidadActiva = getEntidadActiva(request);
        return tipoAsuntoEjb.getActivosEntidad(entidadActiva.getId());
    }

}


