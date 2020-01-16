package es.caib.regweb3.webapp.controller.distribucion;

import es.caib.regweb3.model.Entidad;
import es.caib.regweb3.model.RegistroEntrada;
import es.caib.regweb3.model.UsuarioEntidad;
import es.caib.regweb3.persistence.ejb.DistribucionLocal;
import es.caib.regweb3.persistence.ejb.LibroLocal;
import es.caib.regweb3.persistence.utils.Paginacion;
import es.caib.regweb3.persistence.utils.RegistroUtils;
import es.caib.regweb3.persistence.utils.RespuestaDistribucion;
import es.caib.regweb3.utils.RegwebConstantes;
import es.caib.regweb3.webapp.controller.BaseController;
import es.caib.regweb3.webapp.form.RegistroEntradaBusqueda;
import es.caib.regweb3.webapp.utils.JsonResponse;
import es.caib.regweb3.webapp.utils.Mensaje;
import es.caib.regweb3.webapp.validator.RegistroEntradaBusquedaValidator;
import org.fundaciobit.genapp.common.i18n.I18NException;
import org.fundaciobit.genapp.common.i18n.I18NValidationException;
import org.fundaciobit.genapp.common.web.i18n.I18NUtils;
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
import java.net.SocketTimeoutException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Controller
@RequestMapping(value = "/distribucion")
public class DistribucionController extends BaseController {

    @EJB(mappedName = "regweb3/LibroEJB/local")
    private LibroLocal libroEjb;

    @EJB(mappedName = "regweb3/DistribucionEJB/local")
    private DistribucionLocal distribucionEjb;

    @Autowired
    private RegistroEntradaBusquedaValidator registroEntradaBusquedaValidator;

    /**
     * Listado de Registros distribuidos
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/distribuidos/list", method = RequestMethod.GET)
    public String pendientesDistribuir(Model model, HttpServletRequest request)throws Exception {

        Entidad entidad = getEntidadActiva(request);

        RegistroEntradaBusqueda registroEntradaBusqueda = new RegistroEntradaBusqueda(new RegistroEntrada(),1);
        registroEntradaBusqueda.setFechaInicio(new Date());
        registroEntradaBusqueda.setFechaFin(new Date());
        registroEntradaBusqueda.getRegistroEntrada().getRegistroDetalle().setExtracto("");
        registroEntradaBusqueda.getRegistroEntrada().setEstado(RegwebConstantes.REGISTRO_DISTRIBUIDO);

        model.addAttribute("registroEntradaBusqueda", registroEntradaBusqueda);
        model.addAttribute("librosConsulta", libroEjb.getLibrosEntidad(entidad.getId()));

        return "distribucion/registrosDistribuidos";
    }

    /**
     * Listado de Registros distribuidos
     */
    @RequestMapping(value = "/distribuidos/list", method = RequestMethod.POST)
    public ModelAndView pendientesDistribuir(@ModelAttribute RegistroEntradaBusqueda busqueda, BindingResult result, HttpServletRequest request)throws Exception {

        ModelAndView mav = new ModelAndView("distribucion/registrosDistribuidos");
        Entidad entidad = getEntidadActiva(request);

        registroEntradaBusquedaValidator.validate(busqueda, result);

        if (result.hasErrors()) {
            mav.addObject("errors", result.getAllErrors());
            mav.addObject("librosConsulta", libroEjb.getLibrosEntidad(entidad.getId()));
            mav.addObject("registroEntradaBusqueda", busqueda);

            return mav;

        }else {

            RegistroEntrada registroEntrada = busqueda.getRegistroEntrada();

            // Ponemos la hora 23:59 a la fecha fin
            Date fechaFin = RegistroUtils.ajustarHoraBusqueda(busqueda.getFechaFin());

            Paginacion paginacion = registroEntradaConsultaEjb.busqueda(busqueda.getPageNumber(), busqueda.getFechaInicio(), fechaFin, registroEntrada, "", "", "", "", null, false, "", null, entidad.getId());

            busqueda.setPageNumber(1);

            mav.addObject("librosConsulta", libroEjb.getLibrosEntidad(entidad.getId()));
            mav.addObject("paginacion", paginacion);
            mav.addObject("pendientesDistribuirBusqueda", busqueda);
        }

        return mav;

    }

    /**
     * Función que se encarga de obtener los destinatarios a los que se debe distribuir el registro de entrada.
     * La obtención de esos destinatarios se realiza a través del plugin
     *
     * @param idRegistro identificador del registro
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/{idRegistro}/redistribuir", method = RequestMethod.GET)
    public
    @ResponseBody
    JsonResponse redistribuirRegistro(@PathVariable Long idRegistro, HttpServletRequest request) throws Exception, I18NException,I18NValidationException {

        RegistroEntrada registroEntrada = registroEntradaEjb.getConAnexosFull(idRegistro);
        UsuarioEntidad usuarioEntidad = getUsuarioEntidadActivo(request);
        RespuestaDistribucion respuestaDistribucion = new RespuestaDistribucion();

        JsonResponse respuesta = new JsonResponse();

        // Comprobamos si el RegistroEntrada tiene el estado REGISTRO_DISTRIBUIDO
        if (!registroEntrada.getEstado().equals(RegwebConstantes.REGISTRO_DISTRIBUIDO)) {
            respuesta.setStatus("FAIL_NOVALIDO");
            respuesta.setError(getMessage("registroEntrada.distribuir.error.novalido"));
            respuesta.setResult(respuestaDistribucion);
        }

        try {

            //Distribuimos el registro
            respuestaDistribucion = distribucionEjb.distribuir(registroEntrada, usuarioEntidad, true);

            if(respuestaDistribucion.getHayPlugin() && !respuestaDistribucion.getListadoDestinatariosModificable()){// Si no es modificable,
                if(respuestaDistribucion.getEnviadoCola()){ //Si se ha enviado a la cola
                    respuesta.setStatus("ENVIADO_COLA");
                    Mensaje.saveMessageInfo(request, getMessage("registroEntrada.enviocola"));
                }else if ((respuestaDistribucion.getHayPlugin() && respuestaDistribucion.getEnviado())){ //Cuando hay plugin y ha llegado a destino
                    Mensaje.saveMessageInfo(request, getMessage("registroEntrada.distribuir.ok"));
                    respuesta.setStatus("SUCCESS");
                }else if(respuestaDistribucion.getHayPlugin() && !respuestaDistribucion.getEnviado()){ //Cuando hay plugin y no ha llegado a destino
                    respuesta.setStatus("FAIL");
                    respuesta.setError(getMessage("registroEntrada.distribuir.error.noEnviado"));
                }
            }else {
                if(!respuestaDistribucion.getHayPlugin()){ //Si no ha plugin se cambia estado a tramitado.
                    Mensaje.saveMessageInfo(request, getMessage("registroEntrada.distribuir.ok"));
                    respuesta.setStatus("SUCCESS");
                }
            }

            respuesta.setResult(respuestaDistribucion);

        } catch (I18NValidationException e) {
            e.printStackTrace();
            respuesta.setStatus("FAIL");
            respuesta.setError(I18NUtils.getMessage(e));
            respuesta.setResult(respuestaDistribucion);
            return respuesta;
        } catch (I18NException ie) {
            ie.printStackTrace();
            respuesta.setStatus("FAIL");
            respuesta.setError(I18NUtils.getMessage(ie));
            respuesta.setResult(respuestaDistribucion);
            return respuesta;
        } catch(SocketTimeoutException ste){
            ste.printStackTrace();
            respuesta.setStatus("FAIL");
            respuesta.setError(ste.getMessage());
            respuesta.setResult(respuestaDistribucion);
            return respuesta;
        } catch (Exception iie){
            iie.printStackTrace();
            respuesta.setStatus("FAIL");
            respuesta.setError(iie.getMessage());
            respuesta.setResult(respuestaDistribucion);
            return respuesta;
        }

        return respuesta;
    }

    /**
     * Función que se encarga de distribuir un elemento de la cola de distribución de manera individual y
     * sin esperar a la próxima ejecución del scheduler
     * @param idRegistro
     * @param tipo
     * @param request
     * @return
     * @throws Exception
     * @throws I18NException
     * @throws I18NValidationException
     */
    @RequestMapping(value = "/{idRegistro}/distribuirelementocola/{tipo}", method = RequestMethod.GET)
    public String distribuirElementoEnCola(@PathVariable Long idRegistro, @PathVariable Long tipo, HttpServletRequest request) throws Exception, I18NException,I18NValidationException {

        Entidad entidadActiva = getEntidadActiva(request);

        Boolean distribuido = distribucionEjb.distribuirRegistro(idRegistro, entidadActiva.getId());

        if(distribuido){
            Mensaje.saveMessageInfo(request, getMessage("registroEntrada.distribuir.ok"));
        }else{
            Mensaje.saveMessageError(request, getMessage("registroEntrada.distribuir.error.noEnviado"));
        }

        return "redirect:/cola/list/"+tipo;
    }


    @InitBinder("registroEntradaBusqueda")
    public void registroEntradaBusqueda(WebDataBinder binder) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        CustomDateEditor dateEditor = new CustomDateEditor(sdf, true);
        binder.registerCustomEditor(java.util.Date.class,dateEditor);
    }

}
