package es.caib.regweb3.webapp.controller.dir3;

import es.caib.regweb3.model.Descarga;
import es.caib.regweb3.persistence.ejb.DescargaLocal;
import es.caib.regweb3.persistence.ejb.SincronizadorCatalogoLocal;
import es.caib.regweb3.utils.RegwebConstantes;
import es.caib.regweb3.webapp.controller.BaseController;
import es.caib.regweb3.webapp.utils.JsonResponse;
import es.caib.regweb3.webapp.utils.Mensaje;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.ejb.EJB;
import javax.servlet.http.HttpServletRequest;

/**
 * Created by Fundació BIT.
 * Controller que gestiona todas las operaciones con el CatalogoDir3
 * @author earrivi
 * Date: 11/02/14
 */
@Controller
@RequestMapping(value = "/dir3")
public class Dir3Controller extends BaseController {

    //protected final Logger log = LoggerFactory.getLogger(getClass());

    @EJB(mappedName = "regweb3/SincronizadorCatalogoEJB/local")
    private SincronizadorCatalogoLocal sincronizadorCatalogoEjb;
    
    @EJB(mappedName = "regweb3/DescargaEJB/local")
    private DescargaLocal descargaEjb;


     /*
     * Devuelve la ultima sincronización.
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/datosCatalogo", method = RequestMethod.GET)
    public ModelAndView datosCatalogo(HttpServletRequest request)throws Exception {

        if(request.getParameter("result")!=null) {
            if (request.getParameter("result").equals("ok")) {
                Mensaje.saveMessageInfo(request, getMessage("catalogoDir3.sincronizar.ok"));
            }
        }

        ModelAndView mav = new ModelAndView("catalogoDir3/catalogoDir3List");
        Descarga ultimaDescarga = descargaEjb.findByTipo(RegwebConstantes.CATALOGO);

        mav.addObject("descarga", ultimaDescarga);

        return mav;
    }



    /**
     * Sincronizar el CatalogoDIR3
     */
    @ResponseBody
    @RequestMapping(value = "/sincronizarCatalogo", method = RequestMethod.POST)
    public JsonResponse sincronizar(HttpServletRequest request) throws Exception {

        log.info("Inicio sincronización catalogo DIR3");

        JsonResponse jsonResponse = new JsonResponse();

        try {
            sincronizadorCatalogoEjb.sincronizarCatalogo();
            jsonResponse.setStatus("SUCCESS");

        } catch (Exception e) {
            jsonResponse.setStatus("FAIL");
            jsonResponse.setError(getMessage("catalogoDir3.sincronizar.error"));
            e.printStackTrace();
        }

        return jsonResponse;
    }


     /**
     * Actualizar el CatalogoDIR3
     */
     @ResponseBody
     @RequestMapping(value = "/actualizarCatalogo", method = RequestMethod.POST)
     public JsonResponse actualizar(HttpServletRequest request){

        log.info("Inicio actualizacion catalogo DIR3");

        JsonResponse jsonResponse = new JsonResponse();

        try {
            sincronizadorCatalogoEjb.actualizarCatalogo();
            jsonResponse.setStatus("SUCCESS");

        } catch (Exception e) {
            jsonResponse.setStatus("FAIL");
            jsonResponse.setError(getMessage("catalogoDir3.actualizar.error"));
            e.printStackTrace();
        }

        return jsonResponse;
     }

}