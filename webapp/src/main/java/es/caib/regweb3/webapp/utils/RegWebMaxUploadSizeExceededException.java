package es.caib.regweb3.webapp.utils;


/**
 * 
 * @author anadal
 *
 */
public class RegWebMaxUploadSizeExceededException extends org.springframework.web.multipart.MaxUploadSizeExceededException {

  final String msgCode;

  /**
   * @param msg
   */
  public RegWebMaxUploadSizeExceededException(Throwable cause, long maxSize, String msgCode) {
    super(maxSize, cause);    
    this.msgCode = msgCode;
  }

  public String getMsgCode() {
    return msgCode;
  }

}
