package es.caib.regweb3.webapp.controller;

import es.gob.ad.registros.sir.interService.exception.InterException;
import es.gob.ad.registros.sir.interService.service.IEntradaService;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * Created by Fundació BIT.
 *
 * @author earrivi
 * Date: 16/01/14
 */
@Controller
public class ComunController {

    protected final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    IEntradaService entradaService;


    @RequestMapping(value = "/libsir")
    public ModelAndView libsir(HttpServletRequest request, HttpServletResponse response) {

        try {
            log.info("----------------------------------------- LIBSIR: RECIBIR ASIENTO -----------------------------------------");
            String registro = FileUtils.readFileToString(new File("C:\\Users\\mgonzalez.TIC\\Documents\\OTAE\\REGWEB3-4.0\\repositorio\\registre\\sir\\sir-server\\src\\test\\java\\es\\caib\\regweb3\\sir\\ws\\apiaxis\\sircall.xml"), StandardCharsets.UTF_8);


            entradaService.recibirAsiento(registro, "");
        } catch (InterException | IOException e) {
            e.printStackTrace();
        }

        return new ModelAndView("noAutorizado");
    }


    @RequestMapping(value = "/noAutorizado")
    public ModelAndView noautorizado(HttpServletRequest request, HttpServletResponse response) {
        return new ModelAndView("noAutorizado");
    }

    @RequestMapping(value = "/accesoDenegado")
    public ModelAndView accesoDenegado(HttpServletRequest request, HttpServletResponse response) {
        return new ModelAndView("accesoDenegado");
    }

    @RequestMapping(value = "/error/404")
    public ModelAndView error(HttpServletRequest request) throws Exception{

        log.info("Dentro de error404 : " + request.getRequestURL());

        ModelAndView mav = new ModelAndView("error");

        mav.addObject("exception", "Error 404, la página solicitada no existe");
        mav.addObject("trazaError", "La página solicitada no existe");
        mav.addObject("url", request.getRequestURL());

        return mav;
    }

    @RequestMapping(value = "/aviso")
    public ModelAndView aviso(HttpServletRequest request) throws Exception{

        return new ModelAndView("aviso");
    }

    @RequestMapping(value = "/sesion")
    public ModelAndView sesion(HttpServletRequest request, HttpServletResponse response) throws Exception{

        ModelAndView mav = new ModelAndView("sesion");

        request.getSession();

        return mav;
    }

}
