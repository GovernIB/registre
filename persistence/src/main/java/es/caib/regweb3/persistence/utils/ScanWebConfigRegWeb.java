package es.caib.regweb3.persistence.utils;

import java.util.List;
import java.util.Set;

import org.fundaciobit.plugins.scanweb.api.ScanWebConfig;
import org.fundaciobit.plugins.scanweb.api.ScanWebMode;
import org.fundaciobit.plugins.utils.Metadata;

/**
 *
 * @author anadal
 *
 */
public class ScanWebConfigRegWeb extends ScanWebConfig {

  protected Long entitatID = null;

  protected final long expiryTransaction;

  public ScanWebConfigRegWeb(long scanWebID, String scanType, Set<String> flags,
      List<Metadata> metadades, ScanWebMode mode, String languageUI, String urlFinal,
      long expiryTransaction) {
    super(scanWebID, scanType, flags, metadades, mode, languageUI, urlFinal);
    this.expiryTransaction = expiryTransaction;
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
