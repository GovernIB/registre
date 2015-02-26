package es.caib.regweb.webapp.controller.catalogoDatos;

import es.caib.regweb.model.*;
import es.caib.regweb.persistence.ejb.ArchivoLocal;
import es.caib.regweb.persistence.ejb.BaseEjbJPA;
import es.caib.regweb.persistence.ejb.IdiomaLocal;
import es.caib.regweb.persistence.ejb.ModeloOficioRemisionLocal;
import es.caib.regweb.persistence.utils.FileSystemManager;
import es.caib.regweb.persistence.utils.Paginacion;
import es.caib.regweb.utils.RegwebConstantes;
import es.caib.regweb.webapp.controller.BaseController;
import es.caib.regweb.webapp.form.ModeloOficioRemisionForm;
import es.caib.regweb.webapp.utils.ArchivoFormManager;
import es.caib.regweb.webapp.utils.Mensaje;
import es.caib.regweb.webapp.validator.ModeloOficioRemisionValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.ModelAndView;

import javax.ejb.EJB;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

/**
 * Created 2/09/14 13:56
 *
 * @author jpernia
 */
@Controller
@SessionAttributes(types = ModeloOficioRemisionForm.class )
@RequestMapping(value = "/modeloOficioRemision")
public class ModeloOficioRemisionController extends BaseController {

    @Autowired
    private ModeloOficioRemisionValidator modeloOficioRemisionValidator;
    
    @EJB(mappedName = "regweb/ModeloOficioRemisionEJB/local")
    public ModeloOficioRemisionLocal modeloOficioRemisionEjb;
    
    @EJB(mappedName = "regweb/ArchivoEJB/local")
    public ArchivoLocal archivoEjb;
    
    @EJB(mappedName = "regweb/IdiomaEJB/local")
    public IdiomaLocal idiomaEjb;
    

    /**
     * Listado de todos los Modelos de Oficio de Remisión
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String listado() {
        return "redirect:/modeloOficioRemision/list/1";
    }

    /**
     * Listado de Modelo de Oficio de Remisión
     * @param pageNumber
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/list/{pageNumber}", method = RequestMethod.GET)
    public ModelAndView list(@PathVariable Integer pageNumber, HttpServletRequest request)throws Exception {

        ModelAndView mav = new ModelAndView("catalogoDatos/modeloOficioRemisionList");

        Entidad entidad = getEntidadActiva(request);

        List<ModeloOficioRemision> listado = modeloOficioRemisionEjb.getPagination((pageNumber-1)* BaseEjbJPA.RESULTADOS_PAGINACION, entidad.getId());
        Long total = modeloOficioRemisionEjb.getTotal(entidad.getId());

        Paginacion paginacion = new Paginacion(total.intValue(), pageNumber);

        mav.addObject("paginacion", paginacion);
        mav.addObject("listado", listado);

        return mav;
    }


    /**
     * Carga el formulario para un nuevo {@link es.caib.regweb.model.ModeloOficioRemision}
     */
    @RequestMapping(value = "/new", method = RequestMethod.GET)
    public String nuevoModeloOficioRemision(Model model, HttpServletRequest request) throws Exception {

        ModeloOficioRemisionForm modeloOficioRemisionForm = new ModeloOficioRemisionForm();
        Entidad entidad = getEntidadActiva(request);
        ModeloOficioRemision modeloOficioRemision = new ModeloOficioRemision();
        modeloOficioRemision.setEntidad(entidad);

        modeloOficioRemisionForm.setModeloOficioRemision(modeloOficioRemision);
        model.addAttribute(modeloOficioRemisionForm);

        return "/catalogoDatos/modeloOficioRemisionForm";
    }

    /**
     * Guardar un nuevo {@link es.caib.regweb.model.ModeloOficioRemision}
     */
    @RequestMapping(value = "new", method = RequestMethod.POST)
    public String nuevoModeloOficioRemision(@ModelAttribute ModeloOficioRemisionForm modeloOficioRemisionForm, BindingResult result, SessionStatus status, HttpServletRequest request) {

        modeloOficioRemisionValidator.validate(modeloOficioRemisionForm, result);

        if (result.hasErrors()) { // Si hay errores volvemos a la vista del formulario
            return "catalogoDatos/modeloOficioRemisionForm";
        }else{ // Si no hay errores guardamos el registro

            ArchivoFormManager afm = null;

            try {

                ModeloOficioRemision modeloOficioRemision = modeloOficioRemisionForm.getModeloOficioRemision();

                if(modeloOficioRemisionForm.getModelo() != null){

                    afm = new ArchivoFormManager(archivoEjb,modeloOficioRemisionForm.getModelo(), RegwebConstantes.ARCHIVOS_LOCATION_PROPERTY);

                    // Asociamos el nuevo archivo
                    modeloOficioRemision.setModelo(afm.prePersist());
                }

                modeloOficioRemisionEjb.persist(modeloOficioRemision);
                Mensaje.saveMessageInfo(request, getMessage("regweb.actualizar.registro"));
                status.setComplete();

            }catch (Exception e) {
                e.printStackTrace();
                Mensaje.saveMessageError(request, getMessage("regweb.error.registro"));
            }

            return "redirect:/modeloOficioRemision/list";
        }
    }

    /**
     * Carga el formulario para modificar un {@link es.caib.regweb.model.ModeloOficioRemision}
     */
    @RequestMapping(value = "/{modeloOficioRemisionId}/edit", method = RequestMethod.GET)
    public String editarModeloOficioRemision(@PathVariable("modeloOficioRemisionId") Long modeloOficioRemisionId, Model model) {

        ModeloOficioRemisionForm modeloOficioRemisionForm= new ModeloOficioRemisionForm();
        try {

            ModeloOficioRemision modeloOficioRemision = modeloOficioRemisionEjb.findById(modeloOficioRemisionId);
            modeloOficioRemisionForm.setModeloOficioRemision(modeloOficioRemision);

        }catch (Exception e) {
            e.printStackTrace();
        }

        model.addAttribute(modeloOficioRemisionForm);
        return "catalogoDatos/modeloOficioRemisionForm";
    }


    /**
     * Editar un {@link es.caib.regweb.model.ModeloOficioRemision}
     */
    @RequestMapping(value = "/{modeloOficioRemisionId}/edit", method = RequestMethod.POST)
    public String editarModeloOficioRemision(@ModelAttribute @Valid ModeloOficioRemisionForm modeloOficioRemisionForm,
                                     BindingResult result, SessionStatus status, HttpServletRequest request) {

        modeloOficioRemisionValidator.validate(modeloOficioRemisionForm, result);


        if (result.hasErrors()) {
            return "catalogoDatos/modeloOficioRemisionForm";
        }else {

            ArchivoFormManager afm = null;

            try {

                ModeloOficioRemision modeloOficioRemision = modeloOficioRemisionForm.getModeloOficioRemision();

                if((modeloOficioRemisionForm.getModelo() != null)){ //Modificación con archivo Modelo

                    Archivo eliminarModelo = null;

                    if(modeloOficioRemisionForm.getModelo() != null){

                        afm = new ArchivoFormManager(archivoEjb,modeloOficioRemisionForm.getModelo(), RegwebConstantes.ARCHIVOS_LOCATION_PROPERTY);

                        ModeloOficioRemision modeloOficioRemisionGuardado = modeloOficioRemisionEjb.findById(modeloOficioRemisionForm.getModeloOficioRemision().getId());
                        eliminarModelo = modeloOficioRemisionGuardado.getModelo();

                        // Asociamos el nuevo archivo
                        modeloOficioRemision.setModelo(afm.prePersist());
                    }

                    modeloOficioRemisionEjb.merge(modeloOficioRemision);
                    Mensaje.saveMessageInfo(request, getMessage("regweb.actualizar.registro"));
                    status.setComplete();

                    // Eliminamos el anterior Modelo
                    if(eliminarModelo != null){
                        es.caib.regweb.persistence.utils.FileSystemManager.eliminarArchivo(eliminarModelo.getId());
                        archivoEjb.remove(eliminarModelo);
                    }


                }else{

                    if(modeloOficioRemisionForm.getModeloOficioRemision().getModelo() != null){
                        modeloOficioRemision.setModelo(archivoEjb.findById(modeloOficioRemisionForm.getModeloOficioRemision().getModelo().getId()));
                    }

                    modeloOficioRemisionEjb.merge(modeloOficioRemisionForm.getModeloOficioRemision());
                    Mensaje.saveMessageInfo(request, getMessage("regweb.actualizar.registro"));
                    status.setComplete();
                }


            }catch (Exception e) {
                e.printStackTrace();
                Mensaje.saveMessageError(request, getMessage("regweb.error.registro"));
            }

            status.setComplete();
            return "redirect:/modeloOficioRemision/list";
        }
    }

    /**
     * Eliminar un {@link es.caib.regweb.model.ModeloOficioRemision}
     */
    @RequestMapping(value = "/{modeloOficioRemisionId}/delete")
    public String eliminarModeloOficioRemision(@PathVariable Long modeloOficioRemisionId, HttpServletRequest request) {

        try {

            ModeloOficioRemision modeloOficioRemision = modeloOficioRemisionEjb.findById(modeloOficioRemisionId);

            FileSystemManager.eliminarArchivo(modeloOficioRemision.getModelo().getId());

            modeloOficioRemisionEjb.remove(modeloOficioRemision);

            Mensaje.saveMessageInfo(request, getMessage("regweb.eliminar.registro"));

        } catch (Exception e) {
            Mensaje.saveMessageError(request, getMessage("regweb.relaciones.registro"));
            e.printStackTrace();
        }

        return "redirect:/modeloOficioRemision/list";
    }


    @ModelAttribute("idiomas")
    public List<Idioma> idiomas() throws Exception {
        return idiomaEjb.getAll();
    }

    @InitBinder("modeloOficioRemisionForm")
    public void initBinder(WebDataBinder binder) {
        binder.setDisallowedFields("id");
        binder.setValidator(this.modeloOficioRemisionValidator);

    }
}
