package es.caib.regweb3.persistence.ejb;

import java.util.Locale;
import java.util.Set;

import javax.ejb.Local;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
      long scanWebID) throws Exception;
  
  
  public void requestPlugin(HttpServletRequest request, HttpServletResponse response,
      String absoluteRequestPluginBasePath, String relativeRequestPluginBasePath,
      long scanWebID, String query, boolean isPost)  throws Exception;
  
  
  public ScanWebConfigRegWeb getScanWebConfig(HttpServletRequest request,
      long scanWebID);
  
  //public List<Plugin> getAllPluginsFiltered(HttpServletRequest request, long scanWebID) throws Exception;
  
  
  public Set<String> getDefaultFlags(ScanWebConfigRegWeb ss) throws Exception;
  
  
  public String getName(Long pluginID, Locale locale) throws Exception;
  
  public boolean teScan(Long pluginID);
  
  public IScanWebPlugin getInstanceByPluginID(long pluginID) throws Exception;
  
}
