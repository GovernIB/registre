package es.caib.regweb.webapp.controller.catalogoDatos;

import es.caib.regweb.model.*;
import es.caib.regweb.persistence.ejb.ArchivoLocal;
import es.caib.regweb.persistence.ejb.BaseEjbJPA;
import es.caib.regweb.persistence.ejb.IdiomaLocal;
import es.caib.regweb.persistence.ejb.ModeloReciboLocal;
import es.caib.regweb.persistence.ejb.RegistroEntradaLocal;
import es.caib.regweb.persistence.ejb.RegistroSalidaLocal;
import es.caib.regweb.persistence.utils.FileSystemManager;
import es.caib.regweb.persistence.utils.Paginacion;
import es.caib.regweb.utils.RegwebConstantes;
import es.caib.regweb.webapp.controller.BaseController;
import es.caib.regweb.webapp.form.ModeloReciboForm;
import es.caib.regweb.webapp.utils.ArchivoFormManager;
import es.caib.regweb.webapp.utils.Mensaje;
import es.caib.regweb.webapp.validator.ModeloReciboValidator;
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
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;

/**
 * Created 8/04/14 14:49
 *
 * @author mgonzalez
 */
@Controller
@SessionAttributes(types = ModeloReciboForm.class )
@RequestMapping(value = "/modeloRecibo")
public class ModeloReciboController extends BaseController {

   @Autowired
   private ModeloReciboValidator modeloReciboValidator;
   
   @EJB(mappedName = "regweb/ArchivoEJB/local")
   public ArchivoLocal archivoEjb;
   
   @EJB(mappedName = "regweb/ModeloReciboEJB/local")
   public ModeloReciboLocal modeloReciboEjb;
   
   @EJB(mappedName = "regweb/RegistroSalidaEJB/local")
   public RegistroSalidaLocal registroSalidaEjb;

   @EJB(mappedName = "regweb/RegistroEntradaEJB/local")
   public RegistroEntradaLocal registroEntradaEjb;
   
   @EJB(mappedName = "regweb/IdiomaEJB/local")
   public IdiomaLocal idiomaEjb;

  /**
   * Listado de todos los Modelos de Recibo
   */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String listado() {
      return "redirect:/modeloRecibo/list/1";
    }

   /**
   * Listado de Modelo de Recibo
   * @param pageNumber
   * @return
   * @throws Exception
   */
   @RequestMapping(value = "/list/{pageNumber}", method = RequestMethod.GET)
    public ModelAndView list(@PathVariable Integer pageNumber, HttpServletRequest request)throws Exception {

      ModelAndView mav = new ModelAndView("catalogoDatos/modeloReciboList");

      Entidad entidad = getEntidadActiva(request);

      List<ModeloRecibo> listado = modeloReciboEjb.getPagination((pageNumber-1)* BaseEjbJPA.RESULTADOS_PAGINACION, entidad.getId());
      Long total = modeloReciboEjb.getTotal(entidad.getId());

      Paginacion paginacion = new Paginacion(total.intValue(), pageNumber);

      mav.addObject("paginacion", paginacion);
      mav.addObject("listado", listado);

      return mav;
    }


  /**
     * Carga el formulario para un nuevo {@link es.caib.regweb.model.ModeloRecibo}
     */
    @RequestMapping(value = "/new", method = RequestMethod.GET)
    public String nuevoModeloRecibo(Model model, HttpServletRequest request) throws Exception {

        ModeloReciboForm modeloReciboForm = new ModeloReciboForm();
        Entidad entidad = getEntidadActiva(request);
        ModeloRecibo modeloRecibo = new ModeloRecibo();
        modeloRecibo.setEntidad(entidad);

        modeloReciboForm.setModeloRecibo(modeloRecibo);

        model.addAttribute(modeloReciboForm);

        return "/catalogoDatos/modeloReciboForm";
    }

    /**
     * Guardar un nuevo {@link es.caib.regweb.model.ModeloRecibo}
     */
    @RequestMapping(value = "new", method = RequestMethod.POST)
    public String nuevoModeloRecibo(@ModelAttribute ModeloReciboForm modeloReciboForm, BindingResult result, SessionStatus status, HttpServletRequest request) {

        modeloReciboValidator.validate(modeloReciboForm, result);

        if (result.hasErrors()) { // Si hay errores volvemos a la vista del formulario
            return "catalogoDatos/modeloReciboForm";
        }else{ // Si no hay errores guardamos el registro
          ModeloRecibo modeloRecibo = modeloReciboForm.getModeloRecibo();
          ArchivoFormManager afm = null;

          try {

             if(modeloReciboForm.getModelo() != null){

                 afm = new ArchivoFormManager(archivoEjb,modeloReciboForm.getModelo(), RegwebConstantes.ARCHIVOS_LOCATION_PROPERTY);

                 // Asociamos el nuevo archivo
                 modeloRecibo.setModelo(afm.prePersist());
             }

             modeloReciboEjb.persist(modeloRecibo);
             Mensaje.saveMessageInfo(request, getMessage("regweb.guardar.registro"));
             status.setComplete();

            }catch (Exception e) {
                Mensaje.saveMessageError(request, getMessage("regweb.error.registro"));
                e.printStackTrace();
            }

            return "redirect:/modeloRecibo/list";
        }
    }

    /**
     * Carga el formulario para modificar un {@link es.caib.regweb.model.ModeloRecibo}
     */
    @RequestMapping(value = "/{modeloReciboId}/edit", method = RequestMethod.GET)
    public String editarModeloRecibo(@PathVariable("modeloReciboId") Long modeloReciboId, Model model) {

        ModeloReciboForm modeloReciboForm= new ModeloReciboForm();
        try {

            ModeloRecibo modeloRecibo = modeloReciboEjb.findById(modeloReciboId);
            modeloReciboForm.setModeloRecibo(modeloRecibo);

        }catch (Exception e) {
            e.printStackTrace();
        }

        model.addAttribute( modeloReciboForm);
        return "catalogoDatos/modeloReciboForm";
    }


   /**
    * Editar un {@link es.caib.regweb.model.ModeloRecibo}
    */
   @RequestMapping(value = "/{modeloReciboId}/edit", method = RequestMethod.POST)
   public String editarModeloRecibo(@ModelAttribute @Valid ModeloReciboForm modeloReciboForm,
       BindingResult result, SessionStatus status, HttpServletRequest request) {

       modeloReciboValidator.validate(modeloReciboForm, result);


       if (result.hasErrors()) {
           return "catalogoDatos/modeloReciboForm";
       }else {

           ArchivoFormManager afm = null;

           try {

               ModeloRecibo modeloRecibo = modeloReciboForm.getModeloRecibo();

               if((modeloReciboForm.getModelo() != null)){ //Modificación con archivo Modelo

                   Archivo eliminarModelo = null;

                   if(modeloReciboForm.getModelo() != null){

                       afm = new ArchivoFormManager(archivoEjb,modeloReciboForm.getModelo(), RegwebConstantes.ARCHIVOS_LOCATION_PROPERTY);

                       ModeloRecibo modeloReciboGuardado = modeloReciboEjb.findById(modeloReciboForm.getModeloRecibo().getId());
                       eliminarModelo = modeloReciboGuardado.getModelo();

                       // Asociamos el nuevo archivo
                       modeloRecibo.setModelo(afm.prePersist());
                   }

                   modeloReciboEjb.merge(modeloRecibo);
                   Mensaje.saveMessageInfo(request, getMessage("regweb.actualizar.registro"));
                   status.setComplete();

                   // Eliminamos el anterior Modelo
                   if(eliminarModelo != null){
                       es.caib.regweb.persistence.utils.FileSystemManager.eliminarArchivo(eliminarModelo.getId());
                       archivoEjb.remove(eliminarModelo);
                   }


               }else{

                   if(modeloReciboForm.getModeloRecibo().getModelo() != null){
                       modeloRecibo.setModelo(archivoEjb.findById(modeloReciboForm.getModeloRecibo().getModelo().getId()));
                   }

                   modeloReciboEjb.merge(modeloReciboForm.getModeloRecibo());
                   Mensaje.saveMessageInfo(request, getMessage("regweb.actualizar.registro"));
                   status.setComplete();
               }


           }catch (Exception e) {
               e.printStackTrace();
               Mensaje.saveMessageError(request, getMessage("regweb.error.registro"));
           }

           status.setComplete();
           return "redirect:/modeloRecibo/list";
          }
    }

    /**
     * Eliminar un {@link es.caib.regweb.model.ModeloRecibo}
     */
    @RequestMapping(value = "/{modeloReciboId}/delete")
    public String eliminarModeloRecibo(@PathVariable Long modeloReciboId, HttpServletRequest request) {

        try {

            ModeloRecibo modeloRecibo = modeloReciboEjb.findById(modeloReciboId);


            FileSystemManager.eliminarArchivo(modeloRecibo.getModelo().getId());

            modeloReciboEjb.remove(modeloRecibo);

            Mensaje.saveMessageInfo(request, getMessage("regweb.eliminar.registro"));

        } catch (Exception e) {
            Mensaje.saveMessageError(request, getMessage("regweb.relaciones.registro"));
            e.printStackTrace();
        }

        return "redirect:/modeloRecibo/list";
    }


    /**
     * Seleccionar un {@link es.caib.regweb.model.ModeloRecibo}
     */
    @RequestMapping(value = "/{registroId}/{tipoRegistro}/imprimir/{idModelo}", method = RequestMethod.GET)
    public ModelAndView imprimirModeloRecibo(@PathVariable Long registroId, @PathVariable String tipoRegistro, @PathVariable Long idModelo, HttpServletRequest request, HttpServletResponse response) throws Exception {

        ModelAndView mav = new ModelAndView("recibo");
        ModeloRecibo modeloRecibo = modeloReciboEjb.findById(idModelo);

        if(tipoRegistro.equals("RE")){

            RegistroEntrada registro = registroEntradaEjb.findById(registroId);
            mav.addObject("registro", registro);
        }else if (tipoRegistro.equals("RS")){

            RegistroSalida registro = registroSalidaEjb.findById(registroId);
            mav.addObject("registro", registro);
        }

        mav.addObject("modeloRecibo", modeloRecibo);

        return mav;

    }

    @ModelAttribute("idiomas")
    public List<Idioma> idiomas() throws Exception {
        return idiomaEjb.getAll();
    }

    @InitBinder("modeloReciboForm")
    public void initBinder(WebDataBinder binder) {
       binder.setDisallowedFields("id");
       binder.setValidator(this.modeloReciboValidator);

    }
}
