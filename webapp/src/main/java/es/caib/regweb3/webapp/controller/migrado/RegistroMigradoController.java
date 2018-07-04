package es.caib.regweb3.webapp.controller.migrado;

import es.caib.regweb3.model.*;
import es.caib.regweb3.persistence.ejb.ModificacionLopdMigradoLocal;
import es.caib.regweb3.persistence.ejb.RegistroLopdMigradoLocal;
import es.caib.regweb3.persistence.ejb.RegistroMigradoLocal;
import es.caib.regweb3.persistence.utils.Paginacion;
import es.caib.regweb3.persistence.utils.RegistroUtils;
import es.caib.regweb3.utils.RegwebConstantes;
import es.caib.regweb3.webapp.controller.BaseController;
import es.caib.regweb3.webapp.form.RegistroMigradoBusqueda;
import es.caib.regweb3.webapp.utils.Mensaje;
import es.caib.regweb3.webapp.validator.RegistroMigradoBusquedaValidator;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Fundació BIT.
 * Controller para los listados y detalle de los Registros Migrados
 * @author jpernia
 * Date: 11/11/14
 */
@Controller
@RequestMapping(value = "/registroMigrado")
@SessionAttributes(types = {RegistroMigrado.class})
public class RegistroMigradoController extends BaseController {

    @Autowired
    private RegistroMigradoBusquedaValidator registroMigradoBusquedaValidator;

    @EJB(mappedName = "regweb3/RegistroMigradoEJB/local")
    private RegistroMigradoLocal registroMigradoEjb;

    @EJB(mappedName = "regweb3/RegistroLopdMigradoEJB/local")
    private RegistroLopdMigradoLocal registroLopdMigradoEjb;

    @EJB(mappedName = "regweb3/ModificacionLopdMigradoEJB/local")
    private ModificacionLopdMigradoLocal modificacionLopdMigradoEjb;

    /**
     * Listado de {@link es.caib.regweb3.model.RegistroMigrado}
     */
    @RequestMapping(value = "", method = RequestMethod.GET)
    public String listado() {
        return "redirect:/registroMigrado/list";
    }

    /**
     * Listado de registros migrados
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String list(Model model)throws Exception {

        ArrayList<String[]> oficinasMigrado = (ArrayList<String[]>) registroMigradoEjb.getOficinas();

        RegistroMigradoBusqueda registroMigradoBusqueda = new RegistroMigradoBusqueda(new RegistroMigrado(),1);
        registroMigradoBusqueda.getRegistroMigrado().setTipoRegistro(true);

        model.addAttribute("oficinasMigrado", oficinasMigrado);
        model.addAttribute("registroMigradoBusqueda", registroMigradoBusqueda);

        return "registroMigrado/registroMigradoList";
    }

    /**
     * Realiza la busqueda de {@link es.caib.regweb3.model.RegistroMigrado} según los parametros del formulario
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    public ModelAndView list(@ModelAttribute RegistroMigradoBusqueda busqueda, BindingResult result, HttpServletRequest request)throws Exception {

        ModelAndView mav = new ModelAndView("registroMigrado/registroMigradoList", result.getModel());
        RegistroMigrado registroMigrado = busqueda.getRegistroMigrado();

        Integer numeroRegistro = busqueda.getNumeroRegistro();
        Integer anoRegistro = busqueda.getAnoRegistro();

        ArrayList<String[]> oficinasMigrado = (ArrayList<String[]>) registroMigradoEjb.getOficinas();

        Entidad entidadActiva = getEntidadActiva(request);
        UsuarioEntidad usuarioEntidad = getUsuarioEntidadActivo(request);

        registroMigradoBusquedaValidator.validate(busqueda,result);

        if (result.hasErrors()) { // Si hay errores volvemos a la vista del formulario

            mav.addObject("errors", result.getAllErrors());
            mav.addObject("oficinasMigrado", oficinasMigrado);
            mav.addObject("registroMigradoBusqueda", busqueda);
            return mav;
        }else { // Si no hay errores realizamos la búsqueda

            Paginacion paginacion;

            // Ponemos la hora 23:59 a la fecha fin
            if(busqueda.getFechaFin() != null){
                Date fechaFin = RegistroUtils.ajustarHoraBusqueda(busqueda.getFechaFin());
                paginacion = registroMigradoEjb.busqueda(busqueda.getPageNumber(), busqueda.getFechaInicio(), fechaFin, numeroRegistro, anoRegistro, registroMigrado, entidadActiva.getId());
            } else{
                paginacion = registroMigradoEjb.busqueda(busqueda.getPageNumber(), busqueda.getFechaInicio(), busqueda.getFechaFin(), numeroRegistro, anoRegistro, registroMigrado, entidadActiva.getId());
            }

            // Alta en tabla LOPD
            if(paginacion != null && paginacion.getTotalResults() > 0){
                registroMigradoEjb.insertarRegistrosLopdMigrado(paginacion, usuarioEntidad.getId());
            }

            busqueda.setPageNumber(1);
            mav.addObject("paginacion", paginacion);
            mav.addObject("oficinasMigrado", oficinasMigrado);
            mav.addObject("registroMigradoBusqueda", busqueda);
        }

        return mav;

    }


    /**
     * Carga el formulario para ver el detalle de un {@link es.caib.regweb3.model.RegistroMigrado}
     */
    @RequestMapping(value = "/{idRegistro}/detalle", method = RequestMethod.GET)
    public String detalleRegistroMigrado(@PathVariable Long idRegistro, Model model, HttpServletRequest request) throws Exception {

        HttpSession session = request.getSession();
        Entidad entidadActiva = getEntidadActiva(request);
        RegistroMigrado registroMigrado = registroMigradoEjb.findById(idRegistro);

        // Comprobamos que el Registro Migrado existe
        if (registroMigrado == null) {
            log.info("Aviso: No existeix aquest Registre Migrat");
            Mensaje.saveMessageError(request, getMessage("aviso.registroMigrado.detalle"));
            return "redirect:/registroMigrado/list";
        }

        // Comprueba que el Registro Migrado que consulta es de la Entidad Activa
        if (!registroMigrado.getEntidad().getId().equals(entidadActiva.getId())) {
            log.info("Aviso: No existe este registro Migrado en esta Entidad");
            Mensaje.saveMessageError(request, getMessage("aviso.registroMigrado.detalle"));
            return "redirect:/registroMigrado/list";
        }

        UsuarioEntidad usuarioEntidad = getUsuarioEntidadActivo(request);

        model.addAttribute("registroMigrado",registroMigrado);

        // Alta en tabla LOPD
        registroMigradoEjb.insertarRegistroLopdMigrado(idRegistro, usuarioEntidad.getId());

        return "registroMigrado/registroMigradoDetalle";
    }


    /**
     * Realiza el informe Lopd del {@link es.caib.regweb3.model.RegistroMigrado} seleccionado
     */
    @RequestMapping(value = "/{idRegistroMigrado}/lopd", method = RequestMethod.GET)
    public String informeRegistroLopd(Model model, @PathVariable Long idRegistroMigrado, HttpServletRequest request)throws Exception {

        HttpSession session = request.getSession();
        Entidad entidadActiva = getEntidadActiva(request);

        // Añade la información del Registro Migrado
        RegistroMigrado registroMigrado = registroMigradoEjb.findById(idRegistroMigrado);

        // Comprobamos que el Registro Migrado existe
        if(registroMigrado == null){
            log.info("Aviso: No existeix aquest Registre Migrat");
            Mensaje.saveMessageError(request, getMessage("aviso.registroMigrado.detalle"));
            return "redirect:/registroMigrado/list";
        }

        // Comprueba que el Registro Migrado que consulta es de la Entidad Activa
        if(!registroMigrado.getEntidad().getId().equals(entidadActiva.getId())){
            log.info("Aviso: No existe este registro Migrado en esta Entidad");
            Mensaje.saveMessageError(request, getMessage("aviso.registroMigrado.detalle"));
            return "redirect:/registroMigrado/list";
        }

        model.addAttribute("registroMigrado", registroMigrado);

        // Registros Migrados Listados y Consultados
        Long numRegistroMigrado = idRegistroMigrado;

        List<ModificacionLopdMigrado> visados = modificacionLopdMigradoEjb.getByRegistroMigrado(numRegistroMigrado);
        model.addAttribute("visados", visados);

        RegistroLopdMigrado registroCreado = registroLopdMigradoEjb.getCreacion(numRegistroMigrado, RegwebConstantes.LOPDMIGRADO_CREACION);
        model.addAttribute("registroCreado", registroCreado);

        List<RegistroLopdMigrado> modificaciones = registroLopdMigradoEjb.getByRegistroMigrado(numRegistroMigrado, RegwebConstantes.LOPDMIGRADO_MODIFICACION);
        model.addAttribute("modificaciones", modificaciones);

        List<RegistroLopdMigrado> consultas = registroLopdMigradoEjb.getByRegistroMigrado(numRegistroMigrado, RegwebConstantes.LOPDMIGRADO_CONSULTA);
        model.addAttribute("consultas", consultas);

        List<RegistroLopdMigrado> listados = registroLopdMigradoEjb.getByRegistroMigrado(numRegistroMigrado, RegwebConstantes.LOPDMIGRADO_LISTADO);
        model.addAttribute("listados", listados);

        model.addAttribute("numRegistro", numRegistroMigrado);

        return "registroMigrado/registroLopdMigrado";
    }



    @InitBinder("registroMigradoBusqueda")
    public void registroMigradoBusqueda(WebDataBinder binder) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        CustomDateEditor dateEditor = new CustomDateEditor(sdf, true);
        binder.registerCustomEditor(java.util.Date.class,dateEditor);
    }

}