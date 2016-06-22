package es.caib.regweb3.webapp.controller.asientoRegistralSir;

import es.caib.regweb3.model.*;
import es.caib.regweb3.persistence.ejb.ArchivoLocal;
import es.caib.regweb3.persistence.ejb.AsientoRegistralSirLocal;
import es.caib.regweb3.persistence.ejb.TipoAsuntoLocal;
import es.caib.regweb3.persistence.utils.Paginacion;
import es.caib.regweb3.sir.core.model.AsientoRegistralSir;
import es.caib.regweb3.sir.core.model.EstadoAsientoRegistralSir;
import es.caib.regweb3.sir.core.model.TipoRegistro;
import es.caib.regweb3.sir.ws.api.manager.RegistroManager;
import es.caib.regweb3.sir.ws.api.manager.impl.RegistroManagerImpl;
import es.caib.regweb3.utils.RegwebConstantes;
import es.caib.regweb3.webapp.controller.BaseController;
import es.caib.regweb3.webapp.form.AsientoRegistralSirBusquedaForm;
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

    RegistroManager registroManager = new RegistroManagerImpl();

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

        Oficina oficinaActiva = getOficinaActiva(request);

        Paginacion paginacion = asientoRegistralSirEjb.busqueda(busqueda.getPageNumber(), busqueda.getAnyo(), asientoRegistralSir, oficinaActiva.getCodigo(), busqueda.getEstado());

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

        Oficina oficinaActiva = getOficinaActiva(request);

        // Obtenemos los AsientoRegistralSirs, pendientes de procesar
        Paginacion paginacion = asientoRegistralSirEjb.busqueda(busqueda.getPageNumber(), busqueda.getAnyo(), asientoRegistralSir, oficinaActiva.getCodigo(), busqueda.getEstado());

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

        // Comprobamos que el AsientoRegistralSir tiene como destino nuestra Oficina Activa
        if(asientoRegistralSir.getCodigoEntidadRegistralDestino().equals(getOficinaActiva(request).getCodigo())){
            model.addAttribute("asientoRegistralSir",asientoRegistralSir);

            //Obtenemos los libros de Registro según si el AsientoRegistralSir es de Entrada o de Salida
            List<Libro> libros = null;
            if(asientoRegistralSir.getTipoRegistro().equals(TipoRegistro.ENTRADA)){
                 libros = getLibrosRegistroEntrada(request);
            }
            if(asientoRegistralSir.getTipoRegistro().equals(TipoRegistro.SALIDA)){
                libros = getLibrosRegistroSalida(request);
            }
            model.addAttribute("libros",libros);

            RegistrarForm registrarForm = new RegistrarForm();
            model.addAttribute("registrarForm", registrarForm);

            // Anexos
           // model.addAttribute("anexos", anexoEjb.getByRegistroDetalle(asientoRegistralSir.getRegistroDetalle().getId()));
        }

        return "asientoRegistralSir/asientoRegistralSirDetalle";
    }


    /**
     * Procesa {@link AsientoRegistralSir}, creando un RegistroEntrada
     */
    @RequestMapping(value = "/{idAsientoRegistralSir}/aceptar/{idLibro}/{idIdioma}/{idTipoAsunto}", method = RequestMethod.GET)
    public String confirmarAsientoRegistralSir(@PathVariable Long idAsientoRegistralSir,
        @PathVariable Long idLibro, @PathVariable Long idIdioma, @PathVariable Long idTipoAsunto, Model model, HttpServletRequest request)
            throws Exception, I18NException, I18NValidationException {

        AsientoRegistralSir asientoRegistralSir = asientoRegistralSirEjb.findById(idAsientoRegistralSir);
        Oficina oficinaActiva = getOficinaActiva(request);
        UsuarioEntidad usuarioEntidad = getUsuarioEntidadActivo(request);
        Long idRegistro;

        // Comprobamos si ya ha sido confirmado
        if(asientoRegistralSir.getEstado().equals(EstadoAsientoRegistralSir.ACEPTADO)){
            Mensaje.saveMessageError(request, getMessage("asientoRegistralSir.procesado.error"));
            return "redirect:/asientoRegistralSir/asientoRegistralSirProcesar";
        }

        // Procesa el AsientoRegistralSir
        String variableReturn = "redirect:/asientoRegistralSir/"+idAsientoRegistralSir+"/detalle";
        try{
            idRegistro = asientoRegistralSirEjb.aceptarAsientoRegistralSir(asientoRegistralSir, usuarioEntidad, oficinaActiva, idLibro, idIdioma, idTipoAsunto);

            if(asientoRegistralSir.getTipoRegistro().equals(TipoRegistro.ENTRADA)) {
                variableReturn = "redirect:/registroEntrada/" + idRegistro + "/detalle";
            }
            if(asientoRegistralSir.getTipoRegistro().equals(TipoRegistro.SALIDA)) {
                variableReturn = "redirect:/registroSalida/" + idRegistro + "/detalle";
            }

            //todo: Crear función genérica para enviar mensajes DeMensaje
            registroManager.enviarMensajeConfirmacion(asientoRegistralSir);

        }catch (Exception e){
            Mensaje.saveMessageError(request, getMessage("asientoRegistralSir.error.confirmacion"));
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


