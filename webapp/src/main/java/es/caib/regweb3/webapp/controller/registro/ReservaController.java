package es.caib.regweb3.webapp.controller.registro;

import es.caib.regweb3.model.*;
import es.caib.regweb3.persistence.ejb.RegistroEntradaLocal;
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

import javax.ejb.EJB;
import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

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

    @EJB(mappedName = "regweb3/RegistroEntradaEJB/local")
    public RegistroEntradaLocal registroEntradaEjb;

    /**
     * Carga el formulario para un nuevo {@link es.caib.regweb3.model.RegistroEntrada}
     */
    @RequestMapping(value = "/reserva", method = RequestMethod.GET)
    public String reserva(Model model, HttpServletRequest request) throws Exception {

        if(!isOperador(request)){
            Mensaje.saveMessageError(request, getMessage("error.rol.operador"));
            return "redirect:/inici";
        }

        RegistroEntrada registro = new RegistroEntrada();
        RegistroDetalle registroDetalle = new RegistroDetalle();
        registro.setRegistroDetalle(registroDetalle);

        Oficina oficina = getOficinaActiva(request);
        Usuario usuario = getUsuarioAutenticado(request);

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
        model.addAttribute("libros", getLibrosRegistroEntrada(request));

        return "registroEntrada/reservaForm";
    }

    /**
     * Guardar un nuevo {@link es.caib.regweb3.model.RegistroEntrada}
     */
    @RequestMapping(value = "/reserva", method = RequestMethod.POST)
    public String reserva(@ModelAttribute("registro") RegistroEntrada registro, 
        BindingResult result, Model model,SessionStatus status,
        HttpServletRequest request) throws Exception, I18NException, I18NValidationException {

        reservaValidator.validate(registro, result);

        if (result.hasErrors()) { // Si hay errores volvemos a la vista del formulario

            model.addAttribute(getEntidadActiva(request));
            model.addAttribute(getUsuarioAutenticado(request));
            model.addAttribute(getOficinaActiva(request));
            model.addAttribute("libros", getLibrosRegistroEntrada(request));

            return "registroEntrada/reservaForm";
        }else{ // Si no hay errores guardamos el registro

            try {
                // Guardar el nuevo registro
                // Incrementar el contador del Libro
                // Generar número de registro

                registro.setOficina(getOficinaActiva(request));
                registro.setUsuario(usuarioEntidadEjb.findByUsuarioEntidad(getUsuarioAutenticado(request).getId(), getEntidadActiva(request).getId()));

                // Estado Registro entrada
                registro.setEstado(RegwebConstantes.ESTADO_PENDIENTE);

                //Opcionales
                registro.getRegistroDetalle().setCodigoAsunto(null);
                registro.getRegistroDetalle().setTransporte(null);
                registro.getRegistroDetalle().setIdioma(null);

                //Guardamos el RegistroEntrada
                synchronized (this){
                    registro = registroEntradaEjb.registrarEntrada(registro);
                }

                status.setComplete();
            }catch (Exception e) {
                Mensaje.saveMessageError(request, getMessage("regweb.error.registro"));
                e.printStackTrace();
            }

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



