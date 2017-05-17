package es.caib.regweb3.persistence.ejb;

import java.util.Set;

import javax.ejb.Local;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.fundaciobit.genapp.common.i18n.I18NException;
import org.fundaciobit.plugins.scanweb.api.IScanWebPlugin;

import es.caib.regweb3.persistence.utils.ScanWebConfigRegWeb;



/**
 * 
 * @author anadal migracio a ScanWebApi 2.0.0 (06/07/2016)
 *
 */
@Local
public interface ScanWebModuleLocal {
  
  public static final String JNDI_NAME = "scanweb/ScanWebModuleEJB/local";

  public void closeScanWebProcess(HttpServletRequest request, long scanWebID);
  
  
  public void registerScanWebProcess(HttpServletRequest request, ScanWebConfigRegWeb ess);
  
  
  public String scanDocument(
      HttpServletRequest request, String absoluteRequestPluginBasePath,
      String relativeRequestPluginBasePath,      
      long scanWebID) throws Exception, I18NException;
  
  
  public void requestPlugin(HttpServletRequest request, HttpServletResponse response,
      String absoluteRequestPluginBasePath, String relativeRequestPluginBasePath,
      long scanWebID, String query, boolean isPost)  throws  Exception, I18NException;
  
  
  public ScanWebConfigRegWeb getScanWebConfig(HttpServletRequest request,
      long scanWebID);

  public Set<String> getDefaultFlags(ScanWebConfigRegWeb ss) throws Exception, I18NException;
  
  public boolean entitatTeScan(long entitatID) throws Exception;
  
  public IScanWebPlugin getInstanceByEntitatID(long entitatID) throws Exception;
  
}
