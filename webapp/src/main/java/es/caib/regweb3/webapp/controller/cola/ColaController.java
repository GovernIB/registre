package es.caib.regweb3.webapp.controller.cola;

import es.caib.regweb3.model.Cola;
import es.caib.regweb3.model.Entidad;
import es.caib.regweb3.model.RegistroEntrada;
import es.caib.regweb3.persistence.ejb.ColaLocal;
import es.caib.regweb3.persistence.ejb.CustodiaLocal;
import es.caib.regweb3.persistence.ejb.DistribucionLocal;
import es.caib.regweb3.persistence.utils.Paginacion;
import es.caib.regweb3.persistence.utils.PropiedadGlobalUtil;
import es.caib.regweb3.utils.Configuracio;
import es.caib.regweb3.utils.RegwebConstantes;
import es.caib.regweb3.webapp.controller.BaseController;
import es.caib.regweb3.webapp.utils.Mensaje;
import org.fundaciobit.genapp.common.i18n.I18NException;
import org.fundaciobit.genapp.common.i18n.I18NValidationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.ejb.EJB;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;

/**
 * Created by mgonzalez on 19/04/2018.
 * Controller que gestiona las diferentes colas de objetos definidas en Regweb3
 */
@Controller
@RequestMapping(value = "/cola")
public class ColaController extends BaseController {

    @EJB(mappedName = "regweb3/ColaEJB/local")
    private ColaLocal colaEjb;

    @EJB(mappedName = "regweb3/DistribucionEJB/local")
    private DistribucionLocal distribucionEjb;

    @EJB(mappedName = "regweb3/CustodiaEJB/local")
    private CustodiaLocal custodiaEjb;


    /**
     * Listado de todas las {@link Cola}
     */
    @RequestMapping(value = "/list/{tipo}", method = RequestMethod.GET)
    public String listado(@PathVariable("tipo") Long tipo, Model model, HttpServletRequest request) throws Exception{

        Cola cola = new Cola();
        cola.setTipo(tipo);
        cola.setEstado(RegwebConstantes.COLA_ESTADO_ERROR);

        Entidad entidadActiva = getEntidadActiva(request);

        Paginacion paginacion = colaEjb.busqueda(cola, entidadActiva.getId());

        model.addAttribute("paginacion", paginacion);
        model.addAttribute("colaBusqueda", cola);
        model.addAttribute("tipo", tipo);

        return "cola/colaList";
    }

    /**
     * Listado de todos las {@link Cola}
     */
    @RequestMapping(value = "/list/{tipo}", method = RequestMethod.POST)
    public ModelAndView listado(@PathVariable("tipo") Long tipo, @ModelAttribute Cola busqueda, HttpServletRequest request) throws Exception {


        ModelAndView mav = new ModelAndView("cola/colaList");

        Entidad entidadActiva = getEntidadActiva(request);

        Paginacion paginacion = colaEjb.busqueda(busqueda, entidadActiva.getId());

        mav.addObject("paginacion", paginacion);
        mav.addObject("colaBusqueda", busqueda);
        mav.addObject("tipo", busqueda.getTipo());


        return mav;
    }


    /**
     * Método que vuelve a activar los objetos de la cola para que puedan ser enviados nuevamente.
     * @param idEntidad
     * @param tipo
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/{idEntidad}/reiniciarCola/{tipo}", method = RequestMethod.GET)
    public String reiniciarCola(@PathVariable Long idEntidad, @PathVariable Long tipo, HttpServletRequest request) throws Exception {

        try {

            colaEjb.reiniciarColabyEntidadTipo(idEntidad,tipo);

            Mensaje.saveMessageInfo(request, getMessage("cola.reiniciada"));

        } catch (I18NException | Exception ie) {
            Mensaje.saveMessageError(request, getMessage("cola.error.reiniciar"));
            ie.printStackTrace();
        }

        return "redirect:/cola/list/"+tipo;
    }

    /**
     * Custodia un Justificante de la Cola inmediatamente
     * @param idCola
     * @param request
     * @return
     * @throws Exception
     * @throws I18NException
     * @throws I18NValidationException
     */
    @RequestMapping(value = "/{idCola}/custodiarJustificante", method = RequestMethod.GET)
    public String custodiarJustificante(@PathVariable Long idCola, HttpServletRequest request) throws Exception {

        Entidad entidadActiva = getEntidadActiva(request);
        Cola elemento = colaEjb.findById(idCola);

        Boolean custodiado = custodiaEjb.custodiarJustificanteEnCola(elemento, entidadActiva.getId(), RegwebConstantes.INTEGRACION_CUSTODIA);

        if(custodiado){
            Mensaje.saveMessageInfo(request, getMessage("anexo.custodiar.ok"));
        }else{
            Mensaje.saveMessageError(request, getMessage("anexo.custodiar.error"));
        }

        return "redirect:/cola/list/"+elemento.getTipo();
    }

    /**
     * Función que se encarga de distribuir un elemento de la cola de distribución de manera individual y
     * sin esperar a la próxima ejecución del scheduler
     * @param idCola
     * @param request
     * @return
     * @throws Exception
     * @throws I18NException
     * @throws I18NValidationException
     */
    @RequestMapping(value = "/{idCola}/distribuirRegistro", method = RequestMethod.GET)
    public String distribuirRegistro(@PathVariable Long idCola, HttpServletRequest request) throws Exception {

        Entidad entidadActiva = getEntidadActiva(request);
        Cola elemento = colaEjb.findById(idCola);

        Boolean distribuido = distribucionEjb.distribuirRegistroEnCola(elemento, entidadActiva.getId(),RegwebConstantes.INTEGRACION_DISTRIBUCION);

        if(distribuido){
            Mensaje.saveMessageInfo(request, getMessage("registroEntrada.distribuir.ok"));
        }else{
            Mensaje.saveMessageError(request, getMessage("registroEntrada.distribuir.error.noEnviado"));
        }

        return "redirect:/cola/list/"+elemento.getTipo();
    }

    /**
     * Marcar como procesado un elemento de la {@link Cola}
     */
    @RequestMapping(value = "/{colaId}/procesar")
    public String procesarCola(@PathVariable Long colaId, HttpServletRequest request) {

        try {

            Cola elemento = colaEjb.findById(colaId);

            if(elemento.getTipo().equals(RegwebConstantes.COLA_DISTRIBUCION)){

                // Marcamos el elemento como procesado
                colaEjb.procesarElementoDistribucion(elemento);

                // Marcamos como distribuido el Registro
                RegistroEntrada registroEntrada = registroEntradaEjb.findById(elemento.getIdObjeto());
                registroEntradaEjb.marcarDistribuido(registroEntrada);

            }else if(elemento.getTipo().equals(RegwebConstantes.COLA_CUSTODIA)){
                // Marcamos el elemento como procesado
                colaEjb.procesarElemento(elemento);
            }


            Mensaje.saveMessageInfo(request, getMessage("cola.procesar.ok"));

            return "redirect:/cola/list/"+elemento.getTipo();

        } catch (Exception e) {
            Mensaje.saveMessageError(request, getMessage("cola.error.eliminar"));
            e.printStackTrace();
        }

        return "redirect:/cola/list";
    }

    /**
     * Reiniciar un {@link Cola} y le pone el contador a 0.
     */
    @RequestMapping(value = "/{colaId}/reiniciar/{tipo}")
    public String reiniciarElementoCola(@PathVariable Long colaId, @PathVariable Long tipo,  HttpServletRequest request) {

        try {

            colaEjb.reiniciarElementoCola(colaId);

            Mensaje.saveMessageInfo(request, getMessage("cola.reiniciado"));

        } catch (Exception e) {
            Mensaje.saveMessageError(request, getMessage("cola.reiniciar.elemento.error"));
            e.printStackTrace();
        }

        return "redirect:/cola/list/"+tipo;
    }


    @ModelAttribute("tiposCola")
    public HashMap<Long, Boolean> tiposCola(HttpServletRequest request) {

        HashMap<Long, Boolean> tiposCola =  new HashMap<>();
        Entidad entidadActiva = getEntidadActiva(request);

        if(Configuracio.isCAIB()){
            for(Long tipoCola:RegwebConstantes.COLA_TIPOS_CAIB){

                if(tipoCola.equals(RegwebConstantes.COLA_DISTRIBUCION)){
                    tiposCola.put(tipoCola, PropiedadGlobalUtil.pararColaDistribucion(entidadActiva.getId()));
                }

                if(tipoCola.equals(RegwebConstantes.COLA_CUSTODIA)){
                    tiposCola.put(tipoCola, PropiedadGlobalUtil.pararColaCustodia(entidadActiva.getId()));
                }
            }

        }else{
            for(Long tipoCola:RegwebConstantes.COLA_TIPOS){
                tiposCola.put(tipoCola, PropiedadGlobalUtil.pararColaDistribucion(entidadActiva.getId()));
            }
        }
        return tiposCola;
    }

    @ModelAttribute("estados")
    public Long[] estados() {
        return RegwebConstantes.COLA_ESTADOS;
    }
}
