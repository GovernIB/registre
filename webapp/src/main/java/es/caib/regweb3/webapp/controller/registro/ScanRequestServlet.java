package es.caib.regweb3.webapp.controller.registro;

import es.caib.regweb3.persistence.ejb.ScanWebModuleLocal;
import org.fundaciobit.genapp.common.i18n.I18NException;
import org.fundaciobit.genapp.common.web.i18n.I18NUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;

import javax.annotation.security.RunAs;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


/**
 * @author anadal migracio a ScanWebApi 2.0.0 (06/07/2016)
 * @author anadal migracio a Plugins 2.0.0 (17/05/2015)
 */
// @Controller és només per carrergar els EJB
@Controller
@RunAs("RWE_ADMIN")
public class ScanRequestServlet extends HttpServlet {

    @EJB(mappedName = ScanWebModuleLocal.JNDI_NAME)
    private ScanWebModuleLocal scanWebModuleEjb;

    protected static final Logger log = LoggerFactory.getLogger(ScanRequestServlet.class);

    public static final String CONTEXTWEB = "/anexo/scanwebmodule/requestPlugin/";


    @Override
    public void doGet(HttpServletRequest request,
                      HttpServletResponse response) throws ServletException, IOException {
        processServlet(request, response, false);
    }


    @Override
    public void doPost(HttpServletRequest request,
                       HttpServletResponse response) throws ServletException, IOException {
        processServlet(request, response, true);
    }


    protected void processServlet(HttpServletRequest request,
                                  HttpServletResponse response, boolean isPost) throws ServletException, IOException {

        final boolean debug = log.isDebugEnabled();

        if (debug) {
            log.debug(servletRequestInfoToStr(request));
        }

        // uri = /scanweb/common/scanwebmodule/requestPlugin/1466408733012148444/index.html
        String uri = request.getRequestURI();
        if (debug) {
            log.info(" uri = " + uri);
        }

        int index = uri.indexOf(CONTEXTWEB);

        if (index == -1) {
            String msg = "URL base incorrecte !!!! Esperat " + CONTEXTWEB + ". URI = " + uri;
            throw new IOException(msg);
        }

        //  idAndQuery = 1466408733012148444/index.html
        String idAndQuery = uri.substring(index + CONTEXTWEB.length());
        if (debug) {
            log.info("idAndQuery = " + idAndQuery);
        }

        index = idAndQuery.indexOf('/');

        String idStr = idAndQuery.substring(0, index);
        String query = idAndQuery.substring(index + 1, idAndQuery.length());

        if (debug) {
            log.info("idStr = " + idStr);
            log.info("query = " + query);
        }

        String scanWebID = idStr;

        try {
            requestPlugin(request, response, scanWebID, query, isPost);
        } catch (Exception e) {
            throw new IOException(e.getMessage(), e);
        } catch (I18NException i18ne) {
            String msg = I18NUtils.getMessage(i18ne);
            throw new IOException(msg, i18ne);
        }

    }


    public static String servletRequestInfoToStr(HttpServletRequest request) {
        StringBuilder str = new StringBuilder(
                " +++++++++++++++++ SERVLET REQUEST INFO ++++++++++++++++++++++\n");
        str.append(" ++++ Scheme: " + request.getScheme() + "\n");
        str.append(" ++++ ServerName: " + request.getServerName() + "\n");
        str.append(" ++++ ServerPort: " + request.getServerPort() + "\n");
        str.append(" ++++ PathInfo: " + request.getPathInfo() + "\n");
        str.append(" ++++ PathTrans: " + request.getPathTranslated() + "\n");
        str.append(" ++++ ContextPath: " + request.getContextPath() + "\n");
        str.append(" ++++ ServletPath: " + request.getServletPath() + "\n");
        str.append(" ++++ getRequestURI: " + request.getRequestURI() + "\n");
        str.append(" ++++ getRequestURL: " + request.getRequestURL() + "\n");
        str.append(" ++++ getQueryString: " + request.getQueryString() + "\n");
        str.append(" ++++ javax.servlet.forward.request_uri: "
                + (String) request.getAttribute("javax.servlet.forward.request_uri") + "\n");
        str.append(" ===============================================================");
        return str.toString();
    }


    protected void requestPlugin(HttpServletRequest request, HttpServletResponse response,
                                 String scanWebID, String query, boolean isPost)
            throws Exception, I18NException {

        String scanWebAbsoluteUrl = (String) request.getSession().getAttribute("scanwebAbsoluteurlBase");

        String absoluteRequestPluginBasePath = getAbsoluteRequestPluginBasePath(request,
                CONTEXTWEB, scanWebID, scanWebAbsoluteUrl);
        String relativeRequestPluginBasePath = getRelativeRequestPluginBasePath(request,
                CONTEXTWEB, scanWebID);

        // Map<String, IUploadedFile> uploadedFiles = getMultipartFiles(request);

        scanWebModuleEjb.requestPlugin(request, response, absoluteRequestPluginBasePath,
                relativeRequestPluginBasePath, scanWebID, query, isPost);

    }


    public static String getAbsoluteRequestPluginBasePath(HttpServletRequest request,
                                                          String webContext, String scanWebID, String scanWebAbsoluteUrl) {

        String absoluteURLBase = scanWebAbsoluteUrl;
        if (absoluteURLBase == null || absoluteURLBase.trim().isEmpty()) {
            absoluteURLBase = request.getScheme() + "://" + request.getServerName() + ":"
                    + request.getServerPort() + request.getContextPath();
        }

        return absoluteURLBase + webContext + scanWebID;
    }

    public static String getRelativeRequestPluginBasePath(HttpServletRequest request,
                                                          String webContext, String scanWebID) {

        return request.getContextPath() + webContext + scanWebID;
    }

}
