package es.caib.regweb3.webapp.controller.dir3;


import es.caib.regweb3.model.Descarga;
import es.caib.regweb3.persistence.ejb.*;
import es.caib.regweb3.utils.RegwebConstantes;
import es.caib.regweb3.webapp.controller.BaseController;
import es.caib.regweb3.webapp.utils.Mensaje;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.ejb.EJB;
import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;

/**
 * Created by Fundació BIT.
 * Controller que gestiona todas las operaciones con el CatalogoDir3
 * @author earrivi
 * Date: 11/02/14
 */
@Controller
@RequestMapping(value = "/dir3")
public class Dir3Controller extends BaseController {

    //protected final Logger log = Logger.getLogger(getClass());

    @EJB(mappedName = "regweb3/SincronizadorCatalogoEJB/local")
    public SincronizadorCatalogoLocal sincronizadorCatalogoEjb;
    
    @EJB(mappedName = "regweb3/DescargaEJB/local")
    public DescargaLocal descargaEjb;
    
    @EJB(mappedName = "regweb3/CatLocalidadEJB/local")
    public CatLocalidadLocal catLocalidadEjb;
    
    @EJB(mappedName = "regweb3/CatEntidadGeograficaEJB/local")
    public CatEntidadGeograficaLocal catEntidadGeograficaEjb;
    
    @EJB(mappedName = "regweb3/CatComunidadAutonomaEJB/local")
    public CatComunidadAutonomaLocal catComunidadAutonomaEjb;
    
    @EJB(mappedName = "regweb3/CatPaisEJB/local")
    public CatPaisLocal catPaisEjb;
    
    @EJB(mappedName = "regweb3/CatNivelAdministracionEJB/local")
    public CatNivelAdministracionLocal catNivelAdministracionEjb;
    
    @EJB(mappedName = "regweb3/CatEstadoEntidadEJB/local")
    public CatEstadoEntidadLocal catEstadoEntidadEjb;


    public static final SimpleDateFormat formatoFecha = new SimpleDateFormat(RegwebConstantes.FORMATO_FECHA);

    @EJB(mappedName = "regweb3/CatProvinciaEJB/local")
    public CatProvinciaLocal catProvinciaEjb;

     /*
     * Devuelve la ultima sincronización.
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/datosCatalogo", method = RequestMethod.GET)
    public ModelAndView datosCatalogo()throws Exception {

        ModelAndView mav = new ModelAndView("catalogoDir3/catalogoDir3List");
        Descarga ultimaDescarga = descargaEjb.findByTipo(RegwebConstantes.CATALOGO);

        mav.addObject("descarga", ultimaDescarga);

        return mav;
    }



    /**
     * Sincronizar el CatalogoDIR3
     */
    @RequestMapping(value = "/sincronizarCatalogo", method = RequestMethod.GET)
    public ModelAndView sincronizar( HttpServletRequest request) {

        ModelAndView mav = new ModelAndView("/catalogoDir3/catalogoDir3List");

        log.info("Inicio sincronización catalogo DIR3");

        try {

            Descarga descarga = sincronizadorCatalogoEjb.sincronizarCatalogo();

            Mensaje.saveMessageInfo(request, getMessage("catalogoDir3.sincronizar.ok"));
            mav.addObject("descarga", descarga);

        } catch (Exception e) {
            Mensaje.saveMessageError(request, getMessage("catalogoDir3.sincronizar.error"));
            e.printStackTrace();
        }

        return mav;
    }


     /**
     * Actualizar el CatalogoDIR3
     */
    @RequestMapping(value = "/actualizarCatalogo", method = RequestMethod.GET)
     public ModelAndView actualizar(HttpServletRequest request){

        ModelAndView mav = new ModelAndView("/catalogoDir3/catalogoDir3List");

        log.info("Inicio actualizacion catalogo DIR3");

        try {

            Descarga descarga = sincronizadorCatalogoEjb.actualizarCatalogo();
            Mensaje.saveMessageInfo(request, getMessage("catalogoDir3.actualizar.ok"));

            mav.addObject("descarga", descarga);

        } catch (Exception e) {
            Mensaje.saveMessageError(request, getMessage("catalogoDir3.actualizar.error"));
            e.printStackTrace();
        }

        return mav;

     }


}
