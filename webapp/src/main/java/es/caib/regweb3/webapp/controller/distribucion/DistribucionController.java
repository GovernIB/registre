package es.caib.regweb3.webapp.controller.distribucion;

import es.caib.regweb3.model.Cola;
import es.caib.regweb3.model.Entidad;
import es.caib.regweb3.persistence.ejb.ColaLocal;
import es.caib.regweb3.persistence.ejb.DistribucionLocal;
import es.caib.regweb3.utils.RegwebConstantes;
import es.caib.regweb3.webapp.controller.BaseController;
import es.caib.regweb3.webapp.utils.Mensaje;
import org.fundaciobit.genapp.common.i18n.I18NException;
import org.fundaciobit.genapp.common.i18n.I18NValidationException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.ejb.EJB;
import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping(value = "/distribucion")
public class DistribucionController extends BaseController {

    @EJB(mappedName = DistribucionLocal.JNDI_NAME)
    private DistribucionLocal distribucionEjb;

    @EJB(mappedName = ColaLocal.JNDI_NAME)
    private ColaLocal colaEjb;


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
    @RequestMapping(value = "/{idCola}/distribuirRegistro/{tipo}", method = RequestMethod.GET)
    public String distribuirRegistro(@PathVariable Long idCola, @PathVariable Long tipo, HttpServletRequest request) throws Exception, I18NException,I18NValidationException {

        Entidad entidadActiva = getEntidadActiva(request);
        Cola elemento = colaEjb.findById(idCola);

        Boolean distribuido = distribucionEjb.distribuirRegistroEnCola(elemento, entidadActiva,RegwebConstantes.INTEGRACION_DISTRIBUCION);

        if(distribuido){
            Mensaje.saveMessageInfo(request, getMessage("registroEntrada.distribuir.ok"));
        }else{
            Mensaje.saveMessageError(request, getMessage("registroEntrada.distribuir.error.noEnviado"));
        }

        return "redirect:/cola/list/"+tipo;
    }

}
