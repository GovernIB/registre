package es.caib.regweb3.ws.utils;

import es.caib.regweb3.persistence.utils.I18NLogicUtils;
import org.apache.cxf.binding.soap.SoapFault;
import org.fundaciobit.genapp.common.i18n.*;
import org.fundaciobit.genapp.common.query.Field;
import org.fundaciobit.genapp.common.ws.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.handler.MessageContext;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


/**
 * @author anadal
 */
public class WsUtils {

    protected static final Logger log = LoggerFactory.getLogger(WsUtils.class);


    public static final javax.xml.namespace.QName QNAME = new javax.xml.namespace.QName("-1");


    public static void throwException(String msg) throws org.apache.cxf.binding.soap.SoapFault {
        SoapFault sf = new SoapFault(msg, QNAME);
        throw sf;
    }


    public static org.apache.cxf.binding.soap.SoapFault mountException(String msg) {
        SoapFault sf = new SoapFault(msg, QNAME);
        return sf;
    }


    /**
     *
     * @param ve
     * @param locale
     * @return
     */
    public static WsValidationException convertToWsValidationException(I18NValidationException ve, Locale locale) {
        if (ve == null) {
            return null;
        }

        StringBuffer str = new StringBuffer();
        List<WsFieldValidationError> list = new ArrayList<WsFieldValidationError>();

        for (I18NFieldError fe : ve.getFieldErrorList()) {
            I18NTranslation trans = fe.getTranslation();
            String code = trans.getCode();
            String[] args = I18NLogicUtils.tradueixArguments(locale, trans.getArgs());
            String error = I18NLogicUtils.tradueix(locale, code, args);
            Field<?> field = fe.getField();
            String fieldLabel = I18NLogicUtils.tradueix(locale, field.fullName);

            list.add(new WsFieldValidationError(field.javaName, fieldLabel,
                    error, convertToWsTranslation(trans)));

            if (str.length() != 0) {
                str.append("\n");
            }
            str.append(fieldLabel + "(" + field.javaName + "): " + error);

        }

        return new WsValidationException(str.toString(), list);

    }


    /**
     * @param translation
     * @return
     */
    public static WsI18NTranslation convertToWsTranslation(I18NTranslation translation) {
        if (translation == null) {
            return null;
        }
        List<WsI18NArgument> args = null;
        I18NArgument[] origArgs = translation.getArgs();
        if (origArgs != null && origArgs.length != 0) {
            args = new ArrayList<WsI18NArgument>(origArgs.length);
            for (I18NArgument i18nArgument : origArgs) {
                args.add(new WsI18NArgument(i18nArgument.getValue(),
                        i18nArgument instanceof I18NArgumentCode));
            }
        }
        return new WsI18NTranslation(translation.getCode(), args);

    }

    /**
     * Crea una Excepción del tipo WsI18NException
     * @param code
     * @param args
     * @return
     */
    public static WsI18NException createWsI18NException(String code, String... args) {
        return new WsI18NException(WsUtils.convertToWsTranslation(new I18NTranslation(code, args)), I18NLogicUtils.tradueix(new Locale("ca"), code, args));
    }

    /**
     * Crea una Excepción del tipo WsI18NException
     * @param cause
     * @param code
     * @param args
     * @return
     */
    public static WsI18NException createWsI18NException(Throwable cause, String code, String... args) {
        return new WsI18NException(WsUtils.convertToWsTranslation(new I18NTranslation(code, args)), I18NLogicUtils.tradueix(new Locale("ca"),code, args), cause);
    }


    public static void printInfoUserApp(WebServiceContext wsContext) {

        try {

            log.info("RegWeb_Wsutils::printInfoUserApp()  Thread = "
                    + Thread.currentThread().getId());


            HttpServletRequest servRequest = (HttpServletRequest) wsContext.getMessageContext().get(
                    MessageContext.SERVLET_REQUEST);

            log.info("User = " + servRequest.getUserPrincipal().getName());

            // HttpSession session = (HttpSession) servRequest.getSession();
            // servletContext = session.getServletContext();

            /*
             * usuariAplicacioLogicaEjb
             *
             * UsuariAplicacioJPA usuariAplicacio = (UsuariAplicacioJPA)
             * au.getDetails();
             *
             * log.error("USUARI APLICACIO = " + usuariAplicacio);
             *
             * log.error("USUARI APLICACIO FIND = " +
             * usuariAplicacioEjb.findByPrimaryKeyFull
             * (usuariAplicacio.getUsuariAplicacioID()));
             */
        } catch (Throwable e) {
            log.error("Error executant printInfoUserApp", e);
        }

    }

}
