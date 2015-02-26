package es.caib.regweb.webapp.controller.migrado;

import es.caib.regweb.model.*;
import es.caib.regweb.persistence.ejb.ModificacionLopdMigradoLocal;
import es.caib.regweb.persistence.ejb.RegistroLopdMigradoLocal;
import es.caib.regweb.persistence.ejb.RegistroMigradoLocal;
import es.caib.regweb.persistence.utils.Paginacion;
import es.caib.regweb.persistence.utils.RegistroUtils;
import es.caib.regweb.utils.RegwebConstantes;
import es.caib.regweb.webapp.controller.BaseController;
import es.caib.regweb.webapp.form.RegistroMigradoBusqueda;
import es.caib.regweb.webapp.utils.Mensaje;
import es.caib.regweb.webapp.validator.RegistroMigradoBusquedaValidator;
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

    @EJB(mappedName = "regweb/RegistroMigradoEJB/local")
    public RegistroMigradoLocal registroMigradoEjb;

    @EJB(mappedName = "regweb/RegistroLopdMigradoEJB/local")
    public RegistroLopdMigradoLocal registroLopdMigradoEjb;

    @EJB(mappedName = "regweb/ModificacionLopdMigradoEJB/local")
    public ModificacionLopdMigradoLocal modificacionLopdMigradoEjb;

    /**
     * Listado de todos los Registros Migrados
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
     * Realiza la busqueda de {@link es.caib.regweb.model.RegistroMigrado} según los parametros del formulario
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    public ModelAndView list(@ModelAttribute RegistroMigradoBusqueda busqueda, BindingResult result, HttpServletRequest request)throws Exception {

        ModelAndView mav = new ModelAndView("registroMigrado/registroMigradoList", result.getModel());
        RegistroMigrado registroMigrado = busqueda.getRegistroMigrado();

        Integer numeroRegistro = busqueda.getNumeroRegistro();
        Integer anoRegistro = busqueda.getAnoRegistro();

        ArrayList<String[]> oficinasMigrado = (ArrayList<String[]>) registroMigradoEjb.getOficinas();

        Entidad entidadActiva = getEntidadActiva(request);
        UsuarioEntidad usuarioEntidad = usuarioEntidadEjb.findByUsuarioEntidad(getUsuarioAutenticado(request).getId(), entidadActiva.getId());

        registroMigradoBusquedaValidator.validate(busqueda,result);

        if (result.hasErrors()) { // Si hay errores volvemos a la vista del formulario

            mav.addObject("errors", result.getAllErrors());
            mav.addObject("oficinasMigrado", oficinasMigrado);
            mav.addObject("registroMigradoBusqueda", busqueda);
            return mav;
        }else { // Si no hay errores realizamos la búsqueda

            Paginacion paginacion = null;

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
     * Carga el formulario para ver el detalle de un {@link es.caib.regweb.model.RegistroMigrado}
     */
    @RequestMapping(value = "/{idRegistro}/detalle", method = RequestMethod.GET)
    public String detalleRegistroMigrado(@PathVariable Long idRegistro, Model model, HttpServletRequest request) throws Exception {

        RegistroMigrado registroMigrado = registroMigradoEjb.findById(idRegistro);

        // Comprueba que el Registro Migrado que consulta es de la Entidad Activa
        if(!registroMigrado.getEntidad().getId().equals(getEntidadActiva(request).getId())){
            log.info("Aviso: No existe este registro en esta Entidad");
            Mensaje.saveMessageError(request, getMessage("aviso.registromigrado.detalle"));

            return "redirect:/registroMigrado/list";
        }

        UsuarioEntidad usuarioEntidad = getUsuarioEntidadActivo(request);

        model.addAttribute("registroMigrado",registroMigrado);

        // Alta en tabla LOPD
        registroMigradoEjb.insertarRegistroLopdMigrado(idRegistro, usuarioEntidad.getId());

        return "registroMigrado/registroMigradoDetalle";
    }


    /**
     * Realiza el informe Lopd del Registro Migrado seleccionado
     */
    @RequestMapping(value = "/{idRegistroMigrado}/lopd", method = RequestMethod.GET)
    public ModelAndView informeRegistroLopd(@PathVariable Long idRegistroMigrado, HttpServletRequest request)throws Exception {

        ModelAndView mav = new ModelAndView("registroMigrado/registroLopdMigrado");

        // Añade la información del Registro Migrado
        RegistroMigrado registroMigrado = registroMigradoEjb.findById(idRegistroMigrado);

        // Comprueba que el Registro Migrado que consulta es de la Entidad Activa
        if(!registroMigrado.getEntidad().getId().equals(getEntidadActiva(request).getId())){
            log.info("Aviso: No existe este registro en esta Entidad");
            Mensaje.saveMessageError(request, getMessage("aviso.registromigrado.detalle"));
            mav = new ModelAndView("redirect:/registroMigrado/list");
            return mav;
        }

        mav.addObject("registroMigrado", registroMigrado);

        // Registros Migrados Listados y Consultados
        Long numRegistroMigrado = Long.valueOf(idRegistroMigrado);

        List<ModificacionLopdMigrado> visados = modificacionLopdMigradoEjb.getByRegistroMigrado(numRegistroMigrado);
        mav.addObject("visados", visados);

        RegistroLopdMigrado registroCreado = registroLopdMigradoEjb.getCreacion(numRegistroMigrado, RegwebConstantes.LOPDMIGRADO_CREACION);
        mav.addObject("registroCreado", registroCreado);

        List<RegistroLopdMigrado> modificaciones = registroLopdMigradoEjb.getByRegistroMigrado(numRegistroMigrado, RegwebConstantes.LOPDMIGRADO_MODIFICACION);
        mav.addObject("modificaciones", modificaciones);

        List<RegistroLopdMigrado> consultas = registroLopdMigradoEjb.getByRegistroMigrado(numRegistroMigrado, RegwebConstantes.LOPDMIGRADO_CONSULTA);
        mav.addObject("consultas", consultas);

        List<RegistroLopdMigrado> listados = registroLopdMigradoEjb.getByRegistroMigrado(numRegistroMigrado, RegwebConstantes.LOPDMIGRADO_LISTADO);
        mav.addObject("listados", listados);

        mav.addObject("numRegistro", numRegistroMigrado);

        return mav;
    }



    @InitBinder("registroMigradoBusqueda")
    public void registroMigradoBusqueda(WebDataBinder binder) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        CustomDateEditor dateEditor = new CustomDateEditor(sdf, true);
        binder.registerCustomEditor(java.util.Date.class,dateEditor);
    }

}