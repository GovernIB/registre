package es.caib.regweb3.webapp.controller.sir;

import es.caib.regweb3.model.Entidad;
import es.caib.regweb3.model.sir.MensajeControl;
import es.caib.regweb3.persistence.ejb.MensajeControlLocal;
import es.caib.regweb3.persistence.ejb.SirEnvioLocal;
import es.caib.regweb3.persistence.utils.Paginacion;
import es.caib.regweb3.persistence.utils.RegistroUtils;
import es.caib.regweb3.utils.RegwebConstantes;
import es.caib.regweb3.webapp.controller.BaseController;
import es.caib.regweb3.webapp.form.MensajeControlBusquedaForm;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.ejb.EJB;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Controller
@RequestMapping(value = "/sir/mensajeControl")
public class MensajeControlController extends BaseController {


    @EJB(mappedName = "regweb3/MensajeControlEJB/local")
    private MensajeControlLocal mensajeControlEjb;

    @EJB(mappedName = "regweb3/SirEnvioEJB/local")
    private SirEnvioLocal sirEnvioEjb;

    /**
     * Realiza la busqueda de {@link es.caib.regweb3.model.sir.MensajeControl} según los parametros del formulario
     *
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public ModelAndView enviados(Model model, HttpServletRequest request) throws Exception {

        ModelAndView mav = new ModelAndView("sir/mensajesControlList");

        MensajeControlBusquedaForm mensajeControlBusquedaForm = new MensajeControlBusquedaForm(new MensajeControl(), 1);

        model.addAttribute("tiposMensajeControl", RegwebConstantes.TIPOS_MENSAJE_CONTROL);
        model.addAttribute("tiposComunicacion", RegwebConstantes.TIPOS_COMUNICACION_MENSAJE);
        model.addAttribute("mensajeControlBusqueda", mensajeControlBusquedaForm);

        return mav;
    }

    /**
     * Realiza la busqueda de {@link es.caib.regweb3.model.sir.MensajeControl} según los parametros del formulario
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    public ModelAndView enviados(@ModelAttribute MensajeControlBusquedaForm busqueda, HttpServletRequest request) throws Exception {

        ModelAndView mav = new ModelAndView("sir/mensajesControlList");

        Entidad entidad = getEntidadActiva(request);

        MensajeControl mensajeControl = busqueda.getMensajeControl();

        // Ajustam la dataFi per a que ens trobi els oficis del mateix dia
        Date dataFi = RegistroUtils.ajustarHoraBusqueda(busqueda.getFechaFin());

        Paginacion paginacion = mensajeControlEjb.busqueda(busqueda.getPageNumber(), busqueda.getFechaInicio(), dataFi,mensajeControl,  entidad);

        busqueda.setPageNumber(1);
        mav.addObject("paginacion", paginacion);
        mav.addObject("tiposMensajeControl", RegwebConstantes.TIPOS_MENSAJE_CONTROL);
        mav.addObject("tiposComunicacion", RegwebConstantes.TIPOS_COMUNICACION_MENSAJE);
        mav.addObject("mensajeControlBusqueda", busqueda);

        return mav;
    }

    /**
     * Reenvia un mensaje de control
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/reenviar", method = RequestMethod.GET)
    @ResponseBody
    public Boolean reenviarMensajeControl(@RequestParam Long idMensaje)throws Exception {

//        try{
//
//            MensajeControl mensajeControl = mensajeControlEjb.findById(idMensaje);
//
//            if(mensajeControl.getTipoComunicacion().equals(RegwebConstantes.TIPO_COMUNICACION_ENVIADO)){
//                return sirEnvioEjb.reenviarMensaje(mensajeControl);
//            }else{
//                return  false;
//            }
//
//        }catch (Exception e){
//            e.printStackTrace();
//        }

        return false;
    }

}
