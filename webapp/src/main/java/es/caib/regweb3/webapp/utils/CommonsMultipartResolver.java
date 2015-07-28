package es.caib.regweb3.webapp.utils;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import es.caib.regweb3.utils.Configuracio;

/**
 * 
 * @author anadal
 * 
 */
@Component
public class CommonsMultipartResolver extends
    org.springframework.web.multipart.commons.CommonsMultipartResolver {

  protected final Logger log = Logger.getLogger(getClass());

  public CommonsMultipartResolver() {
    super();
  }

  @Override
  public MultipartHttpServletRequest resolveMultipart(HttpServletRequest request)
      throws MultipartException {
    
      log.info(" ++++ Scheme: " + request.getScheme());
      log.info(" ++++ PathInfo: " + request.getPathInfo());
      log.info(" ++++ PathTrans: " + request.getPathTranslated());
      log.info(" ++++ ContextPath: " + request.getContextPath());
      log.info(" ++++ ServletPath: " + request.getServletPath());
      log.info(" ++++ getRequestURI: " + request.getRequestURI());
      log.info(" ++++ getRequestURL: " + request.getRequestURL().toString());
      log.info(" ++++ getQueryString: " + request.getQueryString());
     

    Long maxUploadSize;
    String msgCode;

    // Pujada d'un fitxer
    // Es fa una mescla entre el màxim global i màxim per entitat
    maxUploadSize = getMaxUploadSize();
    msgCode = "tamanyfitxerpujatsuperat";

    if (maxUploadSize == null) {
      this.setMaxUploadSize(-1); // -1 = No Limit
    } else {
      this.setMaxUploadSize(maxUploadSize);
    }
    if (log.isDebugEnabled()) {
      log.debug("Tamany de pujada de Fitxers: "
        + (maxUploadSize == null ? "Sense limit" : maxUploadSize));
    }

    try {
      return super.resolveMultipart(request);
    } catch (MaxUploadSizeExceededException musee) {
      throw new RegWebMaxUploadSizeExceededException(musee.getCause(), musee.getMaxUploadSize(), msgCode);
    }

  }

  private Long getMaxUploadSize() {
    Long maxUploadSizeGlobal = Configuracio.getMaxUploadSizeInBytes();
    if (log.isDebugEnabled()) {
      if (maxUploadSizeGlobal == null) {
        log.debug("No s'ha definit limit de tamany global en la pujada de Fitxers");
      } else {
        log.info("S'ha definit un tamany màxim de pujada global de Fitxers a "
            + maxUploadSizeGlobal + " bytes");
      }
    }
    
    return maxUploadSizeGlobal;
    
    /*  Si algun dia 
    Long maxUploadSizeEntitat;
    try {
      maxUploadSizeEntitat = LoginInfo.getInstance().getEntitat().getMaxUploadSize();
    } catch (Throwable th) {
      maxUploadSizeEntitat = null;
    }

    Long maxUploadSize = PdfUtils.selectMin(maxUploadSizeGlobal, maxUploadSizeEntitat);
    return maxUploadSize;
    */
  }

  

}
