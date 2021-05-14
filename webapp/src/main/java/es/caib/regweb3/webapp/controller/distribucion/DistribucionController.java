package es.caib.regweb3.webapp.controller.distribucion;

import es.caib.regweb3.model.Cola;
import es.caib.regweb3.model.Entidad;
import es.caib.regweb3.persistence.ejb.ColaLocal;
import es.caib.regweb3.persistence.ejb.DistribucionLocal;
import es.caib.regweb3.persistence.ejb.LibroLocal;
import es.caib.regweb3.utils.RegwebConstantes;
import es.caib.regweb3.webapp.controller.BaseController;
import es.caib.regweb3.webapp.utils.Mensaje;
import es.caib.regweb3.webapp.validator.RegistroEntradaBusquedaValidator;
import org.fundaciobit.genapp.common.i18n.I18NException;
import org.fundaciobit.genapp.common.i18n.I18NValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.ejb.EJB;
import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;

@Controller
@RequestMapping(value = "/distribucion")
public class DistribucionController extends BaseController {

    @EJB(mappedName = "regweb3/LibroEJB/local")
    private LibroLocal libroEjb;

    @EJB(mappedName = "regweb3/DistribucionEJB/local")
    private DistribucionLocal distribucionEjb;

    @EJB(mappedName = "regweb3/ColaEJB/local")
    private ColaLocal colaEjb;

    @Autowired
    private RegistroEntradaBusquedaValidator registroEntradaBusquedaValidator;


    /**
     * Función que se encarga de distribuir un elemento de la cola de distribución de manera individual y
     * sin esperar a la próxima ejecución del scheduler
     * @param idCola
     * @param tipo
     * @param request
     * @return
     * @throws Exception
     * @throws I18NException
     * @throws I18NValidationException
     */
    @RequestMapping(value = "/{idCola}/distribuirelementocola/{tipo}", method = RequestMethod.GET)
    public String distribuirElementoEnCola(@PathVariable Long idCola, @PathVariable Long tipo, HttpServletRequest request) throws Exception, I18NException,I18NValidationException {

        Entidad entidadActiva = getEntidadActiva(request);
        Cola elemento = colaEjb.findById(idCola);

        Boolean distribuido = distribucionEjb.procesarRegistroEnCola(elemento, entidadActiva.getId(),RegwebConstantes.INTEGRACION_DISTRIBUCION);

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
