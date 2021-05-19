package es.caib.regweb3.persistence.utils;

import org.fundaciobit.pluginsib.scanweb.api.ScanWebRequest;
import org.fundaciobit.pluginsib.scanweb.api.ScanWebResult;

/**
 *
 * @author anadal
 *
 */
public class ScanWebConfigRegWeb {

  protected final ScanWebRequest scanWebRequest;
  protected ScanWebResult scanWebResult;


  protected Long entitatID = null;

  protected final long expiryTransaction;

  public ScanWebConfigRegWeb(ScanWebRequest scanWebRequest, long expiryTransaction) {
    this.scanWebRequest=scanWebRequest;
    this.expiryTransaction = expiryTransaction;
  }

  public ScanWebRequest getScanWebRequest() {
    return scanWebRequest;
  }

  public ScanWebResult getScanWebResult() {
    return scanWebResult;
  }

  public void setScanWebResult(ScanWebResult scanWebResult) {
    this.scanWebResult = scanWebResult;
  }

  public Long getEntitatID() {
    return entitatID;
  }

  public void setEntitatID(Long entitatID) {
    this.entitatID = entitatID;
  }

  public long getExpiryTransaction() {
    return expiryTransaction;
  }

}
