package es.caib.regweb3.webapp.controller.asientoRegistralSir;

import es.caib.regweb3.model.*;
import es.caib.regweb3.model.utils.EstadoAsientoRegistralSir;
import es.caib.regweb3.persistence.ejb.*;
import es.caib.regweb3.persistence.utils.Paginacion;
import es.caib.regweb3.sir.ejb.EmisionLocal;
import es.caib.regweb3.sir.ejb.RecepcionLocal;
import es.caib.regweb3.utils.RegwebConstantes;
import es.caib.regweb3.webapp.controller.BaseController;
import es.caib.regweb3.webapp.form.AsientoRegistralSirBusquedaForm;
import es.caib.regweb3.webapp.form.RechazarForm;
import es.caib.regweb3.webapp.form.ReenviarForm;
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
 * Created by Fundació BIT.
 *
 * @author earrivi
 * Date: 16/06/16
 */
@Controller
@RequestMapping(value = "/asientoRegistralSir")
@SessionAttributes(types = AsientoRegistralSir.class)
public class AsientoRegistralSirController extends BaseController {


    @EJB(mappedName = "regweb3/AsientoRegistralSirEJB/local")
    public AsientoRegistralSirLocal asientoRegistralSirEjb;

    @EJB(mappedName = "regweb3/ArchivoEJB/local")
    public ArchivoLocal archivoEjb;

    @EJB(mappedName = "regweb3/TipoAsuntoEJB/local")
    public TipoAsuntoLocal tipoAsuntoEjb;

    @EJB(mappedName = "regweb3/RegistroEntradaEJB/local")
    public RegistroEntradaLocal registroEntradaEjb;

    @EJB(mappedName = "regweb3/RegistroSalidaEJB/local")
    public RegistroSalidaLocal registroSalidaEjb;

    @EJB(mappedName = "regweb3/CatComunidadAutonomaEJB/local")
    public CatComunidadAutonomaLocal catComunidadAutonomaEjb;

    @EJB(mappedName = "regweb3/CatNivelAdministracionEJB/local")
    public CatNivelAdministracionLocal catNivelAdministracionEjb;

    @EJB(mappedName = "regweb3/CatProvinciaEJB/local")
    public CatProvinciaLocal catProvinciaEjb;

    @EJB(mappedName = "regweb3/TipoDocumentalEJB/local")
    public TipoDocumentalLocal tipoDocumentalEjb;

    @EJB(mappedName = "regweb3/SirEJB/local")
    public SirLocal sirEjb;

    @EJB(mappedName = "regweb3/RecepcionEJB/local")
    public RecepcionLocal recepcionEjb;

    @EJB(mappedName = "regweb3/LibroEJB/local")
    public LibroLocal libroEjb;

    @EJB(mappedName = "regweb3/EmisionEJB/local")
    public EmisionLocal emisionEjb;

    @EJB(mappedName = "regweb3/TrazabilidadEJB/local")
    public TrazabilidadLocal trazabilidadEjb;


    /**
     * Listado de todos los AsientoRegistralSirs
     */
    @RequestMapping(value = "", method = RequestMethod.GET)
    public String listado() {
        return "redirect:/asientoRegistralSir/list";
    }

    /**
     * Listado de AsientoRegistralSirs
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public ModelAndView list(Model model, HttpServletRequest request)throws Exception {

        ModelAndView mav = new ModelAndView("asientoRegistralSir/asientoRegistralSirList");

        AsientoRegistralSirBusquedaForm asientoRegistralSirBusquedaForm = new AsientoRegistralSirBusquedaForm(new AsientoRegistralSir(),1);
        model.addAttribute("estados", EstadoAsientoRegistralSir.values());
        model.addAttribute("asientoRegistralSirBusqueda", asientoRegistralSirBusquedaForm);
        model.addAttribute("anys", getAnys());

        return mav;
    }

    /**
     * Realiza la busqueda de {@link AsientoRegistralSir} según los parametros del formulario
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    public ModelAndView list(@ModelAttribute AsientoRegistralSirBusquedaForm busqueda, HttpServletRequest request)throws Exception {

        ModelAndView mav = new ModelAndView("asientoRegistralSir/asientoRegistralSirList");

        AsientoRegistralSir asientoRegistralSir = busqueda.getAsientoRegistralSir();

        Paginacion paginacion = asientoRegistralSirEjb.busqueda(busqueda.getPageNumber(), busqueda.getAnyo(), asientoRegistralSir, getOrganismosSIRCodigo(request), busqueda.getEstado());

        busqueda.setPageNumber(1);

        mav.addObject("estados", EstadoAsientoRegistralSir.values());
        mav.addObject("paginacion", paginacion);
        mav.addObject("asientoRegistralSirBusqueda", busqueda);
        mav.addObject("anys", getAnys());

        return mav;

    }


    /**
     * Realiza la busqueda de Listado de AsientoRegistralSir pendientes de procesar
     */
    @RequestMapping(value = "/asientoRegistralSirsPendientesProcesar", method = RequestMethod.POST)
    public ModelAndView asientoRegistralSirsPendientesProcesar(@ModelAttribute AsientoRegistralSirBusquedaForm busqueda, HttpServletRequest request)throws Exception {

        ModelAndView mav = new ModelAndView("asientoRegistralSir/asientoRegistralSirList");

        AsientoRegistralSir asientoRegistralSir = busqueda.getAsientoRegistralSir();

        // Obtenemos los AsientoRegistralSirs, pendientes de procesar
        Paginacion paginacion = asientoRegistralSirEjb.busqueda(busqueda.getPageNumber(), busqueda.getAnyo(), asientoRegistralSir, getOrganismosSIRCodigo(request), busqueda.getEstado());

        busqueda.setPageNumber(1);
        mav.addObject("paginacion", paginacion);
        mav.addObject("asientoRegistralSirBusqueda", busqueda);

        return mav;

    }

    /**
     * Carga el formulario para ver el detalle de un {@link AsientoRegistralSir}
     */
    @RequestMapping(value = "/{idAsientoRegistralSir}/detalle", method = RequestMethod.GET)
    public String detalleAsientoRegistralSir(@PathVariable Long idAsientoRegistralSir, Model model, HttpServletRequest request) throws Exception {

        AsientoRegistralSir asientoRegistralSir = asientoRegistralSirEjb.findById(idAsientoRegistralSir);

        //si el estado del asiento  es RECIBIDO,DEVUELTO, REENVIADO o REENVIADO_Y_ERROR se puede reenviar
        model.addAttribute("puedeReenviar",  sirEjb.puedeReenviarAsientoRegistralSir(asientoRegistralSir.getEstado()));

        // Comprobamos si el usuario puede gestionar este AsientoRegistralSir según su organismo destino
        if(getOrganismosSIRCodigo(request).contains(asientoRegistralSir.getCodigoUnidadTramitacionDestino())){

            model.addAttribute("asientoRegistralSir",asientoRegistralSir);

            if(asientoRegistralSir.getEstado().equals(EstadoAsientoRegistralSir.RECIBIDO)){
                // Obtenemos los libros del Organismo destinatário del AsientoRegistralSir
                //List<Libro> libros = libroEjb.getLibrosActivosOrganismo(asientoRegistralSir.getCodigoUnidadTramitacionDestino());
                List<Libro> libros = getLibrosRegistroEntrada(request);

                model.addAttribute("libros",libros);
                model.addAttribute("registrarForm", new RegistrarForm());
                model.addAttribute("rechazarForm", new RechazarForm());
                model.addAttribute("reenviarForm", new ReenviarForm());

                model.addAttribute("comunidadesAutonomas", catComunidadAutonomaEjb.getAll());
                model.addAttribute("nivelesAdministracion", catNivelAdministracionEjb.getAll());
                model.addAttribute("comunidad", catNivelAdministracionEjb.getAll());

                // Obtenemos la comunidad de la Entidad para personalizar el buscador de oficinas Sir
                Entidad entidad = getEntidadActiva(request);
                Organismo organismoRaiz = organismoEjb.findByCodigoEntidad(entidad.getCodigoDir3(), entidad.getId());
                if ((organismoRaiz != null) && organismoRaiz.getCodAmbComunidad() != null) {
                    model.addAttribute("comunidad", organismoRaiz.getCodAmbComunidad());
                }else{
                    model.addAttribute("comunidad", new CatComunidadAutonoma());
                }
            }else{
                // Trazabilidad
                model.addAttribute("trazabilidades", trazabilidadEjb.getByAsientoRegistralSir(asientoRegistralSir.getId()));
            }



        }else{
            Mensaje.saveMessageError(request, getMessage("asientoRegistralSir.error.destino"));
            return  "redirect:/asientoRegistralSir/list";
        }



        return "asientoRegistralSir/asientoRegistralSirDetalle";
    }


    /**
     * Procesa {@link AsientoRegistralSir}, creando un RegistroEntrada
     */
    @RequestMapping(value = "/aceptar/{idAsientoRegistralSir}", method = RequestMethod.POST)
    public String confirmarAsientoRegistralSir(@PathVariable Long idAsientoRegistralSir, @ModelAttribute RegistrarForm registrarForm , HttpServletRequest request)
            throws Exception, I18NException, I18NValidationException {

        AsientoRegistralSir asientoRegistralSir = asientoRegistralSirEjb.findById(idAsientoRegistralSir);
        Oficina oficinaActiva = getOficinaActiva(request);
        UsuarioEntidad usuarioEntidad = getUsuarioEntidadActivo(request);
        String variableReturn = "redirect:/asientoRegistralSir/"+idAsientoRegistralSir+"/detalle";

        // Comprobamos si ya ha sido confirmado
        if(asientoRegistralSir.getEstado().equals(EstadoAsientoRegistralSir.ACEPTADO)){
            Mensaje.saveMessageError(request, getMessage("asientoRegistralSir.procesado.error"));
            return variableReturn;
        }

        // Procesa el AsientoRegistralSir
        try{
            //idRegistro = sirEjb.aceptarAsientoRegistralSir(asientoRegistralSir, usuarioEntidad, oficinaActiva, registrarForm.getIdLibro(), registrarForm.getIdIdioma(), registrarForm.getIdTipoAsunto(), registrarForm.getCamposNTIs());
            RegistroEntrada registroEntrada = recepcionEjb.aceptarAsientoRegistralSir(asientoRegistralSir, usuarioEntidad, oficinaActiva, registrarForm.getIdLibro(), registrarForm.getIdIdioma(), registrarForm.getIdTipoAsunto(), registrarForm.getCamposNTIs());

            variableReturn = "redirect:/registroEntrada/" + registroEntrada.getId() + "/detalle";

            Mensaje.saveMessageInfo(request, getMessage("asientoRegistralSir.aceptar.ok"));

        }catch (Exception e){
            Mensaje.saveMessageError(request, getMessage("asientoRegistralSir.error.aceptar"));
            e.printStackTrace();
            return variableReturn;
        }

        return variableReturn;
    }

    /**
     * Rechaza un {@link AsientoRegistralSir}
     */
    @RequestMapping(value = "/rechazar/{idAsientoRegistralSir}", method = RequestMethod.POST)
    public String rechazarAsientoRegistralSir(@PathVariable Long idAsientoRegistralSir, @ModelAttribute RechazarForm rechazarForm , HttpServletRequest request)
            throws Exception, I18NException, I18NValidationException {

        log.info("Observaciones: " + rechazarForm.getObservacionesRechazo());

        AsientoRegistralSir asientoRegistralSir = asientoRegistralSirEjb.findById(idAsientoRegistralSir);
        Oficina oficinaActiva = getOficinaActiva(request);
        UsuarioEntidad usuarioEntidad = getUsuarioEntidadActivo(request);

        String variableReturn = "redirect:/asientoRegistralSir/"+idAsientoRegistralSir+"/detalle";

        // Comprobamos si ya ha sido confirmado
        if(asientoRegistralSir.getEstado().equals(EstadoAsientoRegistralSir.RECHAZADO)){
            Mensaje.saveMessageError(request, getMessage("asientoRegistralSir.procesado.error"));
            return variableReturn;
        }

        // Rechaza el AsientoRegistralSir
        try{
            emisionEjb.rechazarFicheroIntercambio(asientoRegistralSir, oficinaActiva, usuarioEntidad.getUsuario(), rechazarForm.getObservacionesRechazo());

            Mensaje.saveMessageInfo(request, getMessage("asientoRegistralSir.rechazo.ok"));

        }catch (Exception e){
            Mensaje.saveMessageError(request, getMessage("asientoRegistralSir.error.rechazo"));
            e.printStackTrace();
        }

        return "redirect:/asientoRegistralSir/"+idAsientoRegistralSir+"/detalle";
    }

    /**
     * Reenvia un {@link AsientoRegistralSir}
     */
    @RequestMapping(value = "/reenviar/{idAsientoRegistralSir}", method = RequestMethod.POST)
    public String reenviarAsientoRegistralSir(@PathVariable Long idAsientoRegistralSir, @ModelAttribute ReenviarForm reenviarForm , HttpServletRequest request)
            throws Exception, I18NException, I18NValidationException {

        log.info("Oficina Destino reenvio: " + reenviarForm.getCodigoOficina());

        //Montamos la oficina de reenvio seleccionada por el usuario
        Oficina oficinaReenvio = reenviarForm.oficinaReenvio();
        AsientoRegistralSir asientoRegistralSir = asientoRegistralSirEjb.findById(idAsientoRegistralSir);
        Oficina oficinaActiva = getOficinaActiva(request);
        UsuarioEntidad usuarioEntidad = getUsuarioEntidadActivo(request);
        Long idRegistro;
        String variableReturn = "redirect:/asientoRegistralSir/"+idAsientoRegistralSir+"/detalle";

        // Comprobamos si ya ha sido reenviado
        if(asientoRegistralSir.getEstado().equals(EstadoAsientoRegistralSir.REENVIADO)){
            Mensaje.saveMessageError(request, getMessage("asientoRegistralSir.error.reenvio"));
            return variableReturn;
        }

        // Reenvia el AsientoRegistralSir
        try{
            if(oficinaReenvio != null){//Si han seleccionado oficina de reenvio
                //Reenviamos
                emisionEjb.reenviarFicheroIntercambio(asientoRegistralSir, oficinaReenvio, oficinaActiva,usuarioEntidad.getUsuario());
            }

            Mensaje.saveMessageInfo(request, getMessage("asientoRegistralSir.reenvio.ok"));

        }catch (Exception e){
            Mensaje.saveMessageError(request, getMessage("asientoRegistralSir.error.reenvio"));
            e.printStackTrace();
        }

        return "redirect:/asientoRegistralSir/"+idAsientoRegistralSir+"/detalle";
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

    @ModelAttribute("tiposValidezDocumento")
    public Long[] validezDocumento() throws Exception {

        return RegwebConstantes.TIPOS_VALIDEZDOCUMENTO;
    }

    @ModelAttribute("tiposDocumentales")
    public List<TipoDocumental> tiposDocumentales(HttpServletRequest request) throws Exception {
        Entidad entidadActiva = getEntidadActiva(request);
        return tipoDocumentalEjb.getByEntidad(getEntidadActiva(request).getId());
    }

}


