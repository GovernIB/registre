package es.caib.regweb3.webapp.controller.registro;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;

import es.caib.regweb3.persistence.ejb.ScanWebModuleLocal;
import es.caib.regweb3.utils.Configuracio;
import es.caib.regweb3.webapp.scan.TipoScan;



/**
 * 
 * @author anadal migracio a ScanWebApi 2.0.0 (06/07/2016)
 *
 */
// @Controller és només per carrergar els EJB
@Controller
public class ScanRequestServlet extends HttpServlet { 

  @EJB(mappedName = "regweb3/ScanWebModuleEJB/local")
  public ScanWebModuleLocal scanWebModuleEjb;

  protected static final Logger log = Logger.getLogger(ScanRequestServlet.class);
  
  public static final String CONTEXTWEB = "/anexo/scanwebmodule/requestPlugin/";

  
  
  @Override
  public void doGet(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException  {
    processServlet(request, response, false);
  }
  
  
  @Override
  public void doPost(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException  {
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
    
    long scanWebID = Long.parseLong(idStr);
        
    try {
      requestPlugin(request, response, scanWebID, query, isPost);
    } catch (Exception e) {
      throw new IOException(e.getMessage(), e);
    }
  
  }
  
  
  
  
  /* XYZ TODO MOURE A CLASSE D'UTILITAT DE PLUGIN */
  protected String servletRequestInfoToStr(HttpServletRequest request) {
    StringBuffer str = new StringBuffer(
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
        + (String) request.getAttribute("javax.servlet.forward.request_uri")  + "\n");
    str.append(" ===============================================================");
    return str.toString();
  }

  
  
  
  
  

  protected void requestPlugin(HttpServletRequest request, HttpServletResponse response,
      long scanWebID, String query, boolean isPost)
      throws Exception {

    String absoluteRequestPluginBasePath = getAbsoluteRequestPluginBasePath(request,
        CONTEXTWEB, scanWebID);
    String relativeRequestPluginBasePath = getRelativeRequestPluginBasePath(request,
        CONTEXTWEB, scanWebID);

   // Map<String, IUploadedFile> uploadedFiles = getMultipartFiles(request);

    scanWebModuleEjb.requestPlugin(request, response, absoluteRequestPluginBasePath,
        relativeRequestPluginBasePath, scanWebID, query, isPost);

  }




  public static String getAbsoluteRequestPluginBasePath(HttpServletRequest request,
      String webContext, long scanWebID) {

    String absoluteURLBase =   request.getScheme() + "://" + request.getServerName() + ":"
        + request.getServerPort() + request.getContextPath();
    
    return absoluteURLBase + webContext +  scanWebID;
  }

  public static String getRelativeRequestPluginBasePath(HttpServletRequest request,
      String webContext, long scanWebID) {

    return request.getContextPath()  + webContext +  scanWebID;
  }

 

  
  public static List<TipoScan> getTipusScanejat(ScanWebModuleLocal scanWebModuleEjb, Locale locale, String noScanName){
    String[] values = new String[] {"0"};
    try {
      String plugins = Configuracio.getScanPlugins();
      if (plugins != null && !"".equals(plugins))
        values = plugins.split(",");
      
//      log.info("SCAN: Codis de plugins d'escaneig: " + plugins);
    } catch (Exception e) {
//      log.error("SCAN: Error al obtenir els plugins definits al sistema", e);
    }

    List<TipoScan> tiposScan = new ArrayList<TipoScan>();
    for(String value: values) {
      try {
        long codigo = Long.parseLong(value.trim());
        String nombre = (codigo == 0) ? noScanName : scanWebModuleEjb.getName(codigo, locale);
        TipoScan tipoScan = new TipoScan(codigo, nombre);
        tiposScan.add(tipoScan);
//        log.info("SCAN:   " + codigo + " - " + nombre);
      } catch (Exception e){
        log.warn("SCAN: El codi " + value + " no és un codi de tipus d'escanejat válid.");
      }
    }
    return tiposScan;
    //    return Arrays.asList(values);
  }
  

}
