package es.caib.regweb3.webapp.controller.registro;

import org.fundaciobit.genapp.common.i18n.I18NException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by Fundació BIT.
 *
 * @author mgonzalez
 * Date: 01/06/2021
 *
 * Controller público que hace de puente para la vuelta del plugin de scanweb.
 * Solventa el error del Chrome 90 i DTWAIN version 17(APB) con el request.getSession().getAttribute();
 *
 * Incidencia Escaneo Anexos con Chrome 90 y Dynamic Web Twain #460
 */
@Controller
public class PublicScanWebController {

   protected final Logger log = LoggerFactory.getLogger(getClass());

   public static final String CONTEXT_WEB= "/public/returnscanweb/";



   @RequestMapping(value = "/public/returnscanweb/{scanwebID}", method = RequestMethod.GET)
   public ModelAndView callbackScanWeb(HttpServletRequest request,
                               HttpServletResponse response, @PathVariable String scanwebID) throws I18NException, Exception {

      ModelAndView mav = new ModelAndView("registro/callbackscanweb");

      mav.addObject("urlFinal", request.getContextPath()+"/anexoScan/new");

      return mav;

   }
}
