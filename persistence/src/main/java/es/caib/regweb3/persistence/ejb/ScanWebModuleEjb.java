package es.caib.regweb3.persistence.ejb;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.annotation.security.RunAs;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.fundaciobit.genapp.common.i18n.I18NArgumentString;
import org.fundaciobit.genapp.common.i18n.I18NCommonUtils;
import org.fundaciobit.genapp.common.i18n.I18NException;
import org.fundaciobit.plugins.scanweb.api.IScanWebPlugin;
import org.jboss.ejb3.annotation.SecurityDomain;

import es.caib.regweb3.persistence.utils.ScanWebConfigRegWeb;
import es.caib.regweb3.utils.RegwebConstantes;


/**
 *
 * @author anadal migracio a ScanWebApi 2.0.0 (06/07/2016)
 *
 */
@Stateless(name = "ScanWebModuleEJB")
@SecurityDomain("seycon")
@RunAs("RWE_ADMIN") 
//@RolesAllowed({"RWE_SUPERADMIN", "RWE_ADMIN", "RWE_USUARI"})
public class ScanWebModuleEjb implements ScanWebModuleLocal {

  protected Logger log = Logger.getLogger(this.getClass());
  
  @EJB(mappedName = "regweb3/PluginEJB/local")
  private PluginLocal pluginEjb;


  @Override
  public String scanDocument(HttpServletRequest request, String absolutePluginRequestPath,
      String relativePluginRequestPath, long scanWebID) throws Exception, I18NException {

    ScanWebConfigRegWeb scanWebConfig = getScanWebConfig(request, scanWebID);

    long entitatID = scanWebConfig.getEntitatID();

    log.info("SWM :: scanDocument: entitatID = " + entitatID);
    log.info("SWM :: scanDocument: scanWebID = " + scanWebID);

    // El plugin existeix?
    IScanWebPlugin scanWebPlugin;

    scanWebPlugin = getInstanceByEntitatID(entitatID);

    if (scanWebPlugin == null) {
      throw new I18NException("error.plugin.scanweb.noexist", String.valueOf(entitatID));
    }

    String urlToPluginWebPage;
    urlToPluginWebPage = scanWebPlugin.startScanWebTransaction(absolutePluginRequestPath,
        relativePluginRequestPath, request, scanWebConfig);

    return urlToPluginWebPage;

  }

  /**
   * 
   */
  public void requestPlugin(HttpServletRequest request, HttpServletResponse response,
      String absoluteRequestPluginBasePath, String relativeRequestPluginBasePath,
      long scanWebID, String query, boolean isPost) throws Exception, I18NException {

    ScanWebConfigRegWeb ss = getScanWebConfig(request, scanWebID);
    
    if (ss == null) {
      response.sendRedirect("/index.jsp");
      return;
    }
    

    long entitatID = ss.getEntitatID();

    // log.info(" TesterScanWebConfig ss = " + ss);
    // log.info(" ScanWebConfig pluginID = ss.getPluginID(); =>  " + pluginID);

    IScanWebPlugin scanWebPlugin;
    try {
      scanWebPlugin = getInstanceByEntitatID(entitatID);
    } catch (Exception e) {
      throw new I18NException(e, "error.plugin.scanweb.noexist", new I18NArgumentString(String.valueOf(entitatID)));
    }
    if (scanWebPlugin == null) {
      throw new I18NException("error.plugin.scanweb.noexist", String.valueOf(entitatID));
    }

    if (isPost) {
      scanWebPlugin.requestPOST(absoluteRequestPluginBasePath, relativeRequestPluginBasePath,
          scanWebID, query, request, response);
    } else {
      scanWebPlugin.requestGET(absoluteRequestPluginBasePath, relativeRequestPluginBasePath,
          scanWebID, query, request, response);
    }

  }

  // -------------------------------------------------------------------------
  // -------------------------------------------------------------------------
  // ----------------------------- U T I L I T A T S ----------------------
  // -------------------------------------------------------------------------
  // -------------------------------------------------------------------------

  @Override
  public void closeScanWebProcess(HttpServletRequest request, long scanWebID) {

    ScanWebConfigRegWeb pss = getScanWebConfig(request, scanWebID);

    if (pss == null) {
      log.warn("NO Existeix scanWebID igual a " + scanWebID);
      return;
    }

    closeScanWebProcess(request, scanWebID, pss);
  }

  private void closeScanWebProcess(HttpServletRequest request, long scanWebID,
      ScanWebConfigRegWeb pss) {

    Long entitatID = pss.getEntitatID();

    // final String scanWebID = pss.getscanWebID();
    if (entitatID == null) {
      // Encara no s'ha asignat plugin al proces d'escaneig
    } else {

      IScanWebPlugin scanWebPlugin = null;
      try {
        scanWebPlugin = getInstanceByEntitatID(entitatID);
      } catch (Exception e) {
        log.error(I18NCommonUtils.tradueix(new Locale("ca"), 
            "error.plugin.scanweb.noexist", String.valueOf(entitatID)), e);
      }
      if (scanWebPlugin == null) {
        log.error(I18NCommonUtils.tradueix(new Locale("ca"), 
            "error.plugin.scanweb.noexist", String.valueOf(entitatID)));
      }

      try {
        scanWebPlugin.endScanWebTransaction(scanWebID, request);
      } catch (Exception e) {
        log.error(
            "Error borrant dades d'un Proces d'escaneig " + scanWebID + ": " + e.getMessage(),
            e);
      }
    }
    scanWebConfigMap.remove(scanWebID);
  }

  private static final Map<Long, ScanWebConfigRegWeb> scanWebConfigMap = new HashMap<Long, ScanWebConfigRegWeb>();

  private static long lastCheckScanProcessCaducades = 0;

  /**
   * Fa neteja
   * 
   * @param scanWebID
   * @return
   */
  public ScanWebConfigRegWeb getScanWebConfig(HttpServletRequest request, long scanWebID) {
    // Fer net peticions caducades
    // Check si existeix algun proces de escaneig caducat s'ha d'esborrar
    // Com a mínim cada minut es revisa si hi ha caducats
    Long now = System.currentTimeMillis();

    final long un_minut_en_ms = 60 * 60 * 1000;

    if (now + un_minut_en_ms > lastCheckScanProcessCaducades) {
      lastCheckScanProcessCaducades = now;
      Map<Long, ScanWebConfigRegWeb> keysToDelete = new HashMap<Long, ScanWebConfigRegWeb>();

      Set<Long> ids = scanWebConfigMap.keySet();
      for (Long id : ids) {
        ScanWebConfigRegWeb ss = scanWebConfigMap.get(id);
        if (now > ss.getExpiryTransaction()) {
          keysToDelete.put(id, ss);
          SimpleDateFormat sdf = new SimpleDateFormat();
          log.info("Tancant ScanWebConfig amb ID = " + id + " a causa de que està caducat "
              + "( ARA: " + sdf.format(new Date(now)) + " | CADUCITAT: "
              + sdf.format(new Date(ss.getExpiryTransaction())) + ")");
        }
      }

      if (keysToDelete.size() != 0) {
        synchronized (scanWebConfigMap) {

          for (Entry<Long, ScanWebConfigRegWeb> pss : keysToDelete.entrySet()) {
            closeScanWebProcess(request, pss.getKey(), pss.getValue());
          }
        }
      }
    }

    return scanWebConfigMap.get(scanWebID);
  }

  @Override
  public void registerScanWebProcess(HttpServletRequest request, ScanWebConfigRegWeb scanWebConfig) {
    final long scanWebID = scanWebConfig.getScanWebID();
    
    ScanWebConfigRegWeb tmp = getScanWebConfig(request, scanWebID);
    if (tmp != null) {
      closeScanWebProcess(request, scanWebID);
    }
    
    synchronized (scanWebConfigMap) {
      scanWebConfigMap.put(scanWebID, scanWebConfig);
    }

  }
  
  
  @Override
  public Set<String> getDefaultFlags(ScanWebConfigRegWeb ss) throws Exception, I18NException  {
    
    IScanWebPlugin scanWebPlugin = 
        (IScanWebPlugin)pluginEjb.getPlugin(ss.getEntitatID(), RegwebConstantes.PLUGIN_SCAN);
    
    if (scanWebPlugin == null) {
      
      throw new I18NException("error.plugin.scanweb.noexist", String.valueOf(ss.getEntitatID()));
    }
    
    
    List<Set<String>> supFlags = scanWebPlugin.getSupportedFlagsByScanType(ss.getScanType());
    
    return supFlags.get(0);
    
  }

  
  
  


  protected static Map<Long, IScanWebPlugin> pluginsByEntitat = new HashMap<Long, IScanWebPlugin>();
  
  @Override
  public IScanWebPlugin getInstanceByEntitatID(long entitatID) throws Exception {
    
    IScanWebPlugin p = pluginsByEntitat.get(entitatID);
    
    if (p == null) {
      Object obj = pluginEjb.getPlugin(entitatID, RegwebConstantes.PLUGIN_SCAN);
      
      if (obj == null) {
        // No te cap plugin definit
        return null;
      }
      pluginsByEntitat.put(entitatID, (IScanWebPlugin)obj);
      p =  pluginsByEntitat.get(entitatID);
    }      

    return p; 
  }
  
  /**
   * Comprueba si la entidad tiene definido un tipo de escaneo vàlido
   * @param pluginID
   * @return
   */
  @Override
  public boolean entitatTeScan(long entitatID) throws Exception {
    
    IScanWebPlugin plugin = getInstanceByEntitatID(entitatID);
    
    return plugin != null;
    
    /*
    Long count = pluginEjb.getTotalByEntidad(entitatID, RegwebConstantes.PLUGIN_SCAN);
    
    if (count == 0) {
      return false;
    } else {
      return true;
    }
    */
  }
  
  
}
