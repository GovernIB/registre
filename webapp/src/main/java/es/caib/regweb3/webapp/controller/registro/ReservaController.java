package es.caib.regweb3.webapp.controller.registro;

import es.caib.regweb3.model.*;
import es.caib.regweb3.utils.RegwebConstantes;
import es.caib.regweb3.webapp.controller.BaseController;
import es.caib.regweb3.webapp.utils.Mensaje;
import es.caib.regweb3.webapp.validator.ReservaValidator;
import org.fundaciobit.genapp.common.i18n.I18NException;
import org.fundaciobit.genapp.common.i18n.I18NValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;

/**
 * Created by Fundació BIT.
 * Controller para gestionar las reservas de Registros de Entrada
 * @author earrivi
 * Date: 31/03/14
 */
@Controller
@RequestMapping(value = "/registroEntrada")
@SessionAttributes({"registro"})
public class ReservaController  extends BaseController {

    @Autowired
    private ReservaValidator reservaValidator;

    /**
     * Carga el formulario para un nuevo {@link es.caib.regweb3.model.RegistroEntrada}
     */
    @RequestMapping(value = "/reserva", method = RequestMethod.GET)
    public String reserva(Model model, HttpServletRequest request) throws Exception {

        if(!isOperador(request)){
            Mensaje.saveMessageError(request, getMessage("error.rol.operador"));
            return "redirect:/inici";
        }

        Oficina oficina = getOficinaActiva(request);
        Usuario usuario = getUsuarioAutenticado(request);

        RegistroEntrada registro = new RegistroEntrada();
        registro.setLibro(getLibroEntidad(request));
        registro.setOficina(oficina);
        registro.getRegistroDetalle().setPresencial(true);

        Entidad entidad = getEntidadActiva(request);
        if(oficina == null){
            Mensaje.saveMessageInfo(request, getMessage("oficinaActiva.null"));
            return "redirect:/inici";
        }

        registro.getRegistroDetalle().setOficinaOrigen(oficina);

        model.addAttribute(entidad);
        model.addAttribute(usuario);
        model.addAttribute(oficina);
        model.addAttribute("registro",registro);

        return "registroEntrada/reservaForm";
    }

    /**
     * Guardar un nuevo {@link es.caib.regweb3.model.RegistroEntrada}
     */
    @RequestMapping(value = "/reserva", method = RequestMethod.POST)
    public String reserva(@ModelAttribute("registro") RegistroEntrada registro, BindingResult result, Model model,SessionStatus status,
        HttpServletRequest request) throws Exception, I18NException, I18NValidationException {

        Entidad entidad = getEntidadActiva(request);

        reservaValidator.validate(registro, result);

        if (result.hasErrors()) { // Si hay errores volvemos a la vista del formulario

            model.addAttribute(getEntidadActiva(request));
            model.addAttribute(getUsuarioAutenticado(request));
            model.addAttribute(getOficinaActiva(request));

            return "registroEntrada/reservaForm";
        }else{ // Si no hay errores guardamos el registro

            try {
                UsuarioEntidad usuarioEntidad = getUsuarioEntidadActivo(request);

                registro.setOficina(getOficinaActiva(request));
                registro.setUsuario(usuarioEntidad);

                // Estado Registro entrada
                registro.setEstado(RegwebConstantes.REGISTRO_RESERVA);

                //Opcionales
                registro.getRegistroDetalle().setCodigoAsunto(null);
                registro.getRegistroDetalle().setTransporte(null);
                registro.getRegistroDetalle().setIdioma(null);

                //Guardamos el RegistroEntrada
                registro = registroEntradaEjb.registrarEntrada(registro, entidad, usuarioEntidad, null, null, false);

            }catch (Exception e) {
                Mensaje.saveMessageError(request, getMessage("regweb.error.registro"));
                e.printStackTrace();
            }

            status.setComplete();
            return "redirect:/registroEntrada/"+registro.getId()+"/detalle";
        }
    }

    @InitBinder("registro")
    public void initBinder(WebDataBinder binder) {
        binder.setDisallowedFields("id");
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        CustomDateEditor dateEditor = new CustomDateEditor(sdf, true);
        binder.registerCustomEditor(java.util.Date.class, dateEditor);

        binder.setValidator(this.reservaValidator);
    }

}



