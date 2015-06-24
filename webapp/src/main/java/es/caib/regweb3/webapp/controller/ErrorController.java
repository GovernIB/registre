package es.caib.regweb3.webapp.controller;

import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;

/**
 * Created by Fundació BIT.
 *
 * @author earrivi
 * Date: 16/01/14
 */
@ControllerAdvice
public class ErrorController {

    protected final Logger log = Logger.getLogger(getClass());

    @ExceptionHandler(Exception.class)
    public ModelAndView handleException(HttpServletRequest req, Exception exception) {

        log.info("Dentro de handleException: " + req.getRequestURL() + " raised " + exception);
        exception.printStackTrace(); // Mostramos el error por pantalla

        ModelAndView mav = new ModelAndView("error");

        mav.addObject("exception", exception.getMessage());
        mav.addObject("trazaError", getStackTraceComoString(exception));
        mav.addObject("url", req.getRequestURL());

        return mav;
    }

    @ExceptionHandler(RuntimeException.class)
    public ModelAndView handleRuntimeException(HttpServletRequest req, Exception exception) {

        log.info("Dentro de handleRuntimeException: " + req.getRequestURL() + " raised " + exception);
        exception.printStackTrace(); // Mostramos el error por pantalla

        ModelAndView mav = new ModelAndView("error");

        mav.addObject("exception", exception.getMessage());
        mav.addObject("trazaError", getStackTraceComoString(exception));
        mav.addObject("url", req.getRequestURL());

        return mav;
    }

    public static String getStackTraceComoString ( Throwable e )
    {
        if (e == null){
            return "";
        }

        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        try{
            PrintWriter writer = new PrintWriter(bytes, true);
            e.printStackTrace(writer);
        }
        catch (Exception ex){
        }

        return bytes.toString();
    }

    /*@RequestMapping("/error/{error}")
    public ModelAndView error(@PathVariable String error, HttpServletRequest request, HttpServletResponse response) {

        ModelAndView mav =  new ModelAndView("error");

        mav.addObject("error", error);

        log.info("Dentro de ErrorController");

        return mav;

    }*/
}
