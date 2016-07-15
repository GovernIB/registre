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
import javax.ejb.Stateless;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.fundaciobit.plugins.scanweb.api.IScanWebPlugin;
import org.fundaciobit.plugins.utils.PluginsManager;
import org.jboss.ejb3.annotation.SecurityDomain;

import es.caib.regweb3.persistence.utils.ScanWebConfigRegWeb;
import es.caib.regweb3.utils.RegwebConstantes;


/**
 *
 *@author anadal migracio a ScanWebApi 2.0.0 (06/07/2016)
 *
 */
@Stateless(name = "ScanWebModuleEJB")
@SecurityDomain("seycon")
@RunAs("RWE_ADMIN") 
//@RolesAllowed({"RWE_SUPERADMIN", "RWE_ADMIN", "RWE_USUARI"})
public class ScanWebModuleEjb implements ScanWebModuleLocal {

  protected static Logger log = Logger.getLogger(ScanWebModuleEjb.class);

  /*
  @Override
  public List<Plugin> getAllPluginsFiltered(HttpServletRequest request, long scanWebID)
      throws Exception {

    ScanWebConfigTester scanWebConfig = getScanWebConfig(request, scanWebID);

    // TODO CHECK scanWebConfig
    List<Plugin> plugins = ScanWebPluginManager.getAllPlugins();
    if (plugins == null || plugins.size() == 0) {
      String msg = "S'ha produit un error llegint els plugins o no se n'han definit.";
      throw new Exception(msg);
    }

    List<Plugin> pluginsFiltered = new ArrayList<Plugin>();

    IScanWebPlugin scanWebPlugin;

    for (Plugin pluginDeScanWeb : plugins) {
      // 1.- Es pot instanciar el plugin ?
      scanWebPlugin = ScanWebPluginManager
          .getInstanceByPluginID(pluginDeScanWeb.getPluginID());

      if (scanWebPlugin == null) {
        throw new Exception("No s'ha pogut instanciar Plugin amb ID "
            + pluginDeScanWeb.getPluginID());
      }

      // 2.- Passa el filtre ...

      if (scanWebPlugin.filter(request, scanWebConfig)) {
        pluginsFiltered.add(pluginDeScanWeb);
      } else {
        // Exclude Plugin
        log.info("Exclos plugin [" + pluginDeScanWeb.getNom() + "]: NO PASSA FILTRE");
      }

    }

    return pluginsFiltered;

  }
  */

  @Override
  public String scanDocument(HttpServletRequest request, String absolutePluginRequestPath,
      String relativePluginRequestPath, long scanWebID) throws Exception {

    ScanWebConfigRegWeb scanWebConfig = getScanWebConfig(request, scanWebID);

    Long pluginID = scanWebConfig.getPluginID();

    log.info("SMC :: scanDocument: PluginID = " + pluginID);
    log.info("SMC :: scanDocument: scanWebID = " + scanWebID);

    // El plugin existeix?
    IScanWebPlugin scanWebPlugin;

    scanWebPlugin = getInstanceByPluginID(pluginID);

    if (scanWebPlugin == null) {
      String msg = "plugin.scanweb.noexist: " + String.valueOf(pluginID);
      throw new Exception(msg);
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
      long scanWebID, String query, boolean isPost) throws Exception {

    ScanWebConfigRegWeb ss = getScanWebConfig(request, scanWebID);
    
    if (ss == null) {
      response.sendRedirect("/index.jsp");
      return;
    }
    

    long pluginID = ss.getPluginID();

    // log.info(" TesterScanWebConfig ss = " + ss);
    // log.info(" ScanWebConfig pluginID = ss.getPluginID(); =>  " + pluginID);

    IScanWebPlugin scanWebPlugin;
    try {
      scanWebPlugin = getInstanceByPluginID(pluginID);
    } catch (Exception e) {

      String msg = "plugin.scanweb.noexist: " + String.valueOf(pluginID);
      throw new Exception(msg);
    }
    if (scanWebPlugin == null) {
      String msg = "plugin.scanweb.noexist: " + String.valueOf(pluginID);
      throw new Exception(msg);
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

    Long pluginID = pss.getPluginID();

    // final String scanWebID = pss.getscanWebID();
    if (pluginID == null) {
      // Encara no s'ha asignat plugin al proces d'escaneig
    } else {

      IScanWebPlugin scanWebPlugin = null;
      try {
        scanWebPlugin = getInstanceByPluginID(pluginID);
      } catch (Exception e) {
        log.error(e.getMessage(), e);
        return;
      }
      if (scanWebPlugin == null) {
        log.error("plugin.scanweb.noexist: " + String.valueOf(pluginID));
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
  public Set<String> getDefaultFlags(es.caib.regweb3.persistence.utils.ScanWebConfigRegWeb ss) throws Exception  {
    
    long pluginID = ss.getPluginID();
    
    IScanWebPlugin scanWebPlugin;
    try {
      scanWebPlugin = getInstanceByPluginID(pluginID);
    } catch (Exception e) {

      String msg = "plugin.scanweb.noexist: " + String.valueOf(pluginID);
      throw new Exception(msg);
    }
    if (scanWebPlugin == null) {
      String msg = "plugin.scanweb.noexist: " + String.valueOf(pluginID);
      throw new Exception(msg);
    }
    
    List<Set<String>> supFlags = scanWebPlugin.getSupportedFlagsByScanType(ss.getScanType());
    
    return supFlags.get(0);
    
  }

  
  
  


  protected static Map<Long, IScanWebPlugin> plugins = new HashMap<Long, IScanWebPlugin>();
  
  @Override
  public IScanWebPlugin getInstanceByPluginID(long pluginID) throws Exception {
    
    if (plugins.get(pluginID) == null) {
      // Valor de la Clau
      final String propertyName = RegwebConstantes.REGWEB3_PROPERTY_BASE + "scan.plugin." + pluginID;
      String className = System.getProperty(propertyName);
//      String className = "es.limit.plugins.scanweb.dynamicwebtwain.DynamicWebTwainScanWebPlugin";
//      log.info("SCAN: Classe del plugin " + tipusScan + " = " + className);
      if (className == null || className.trim().length()<=0) {
        log.error("No hi ha cap propietat " + propertyName + " definint la classe que gestiona el plugin de scanner");
        throw new Exception("No hi ha cap propietat " + propertyName + " definint la classe que gestiona el plugin de scanner");
      }
      // Carregant la classe
      Object obj;
      obj = PluginsManager.instancePluginByClassName(className, propertyName + ".");
//      log.info("SCAN: Obtinguda instància de classe -> " + obj.toString());
      plugins.put(pluginID, (IScanWebPlugin)obj);
//      Class<?> clazz = Class.forName(className);
//      IScanWebPlugin plugin = (IScanWebPlugin)clazz.newInstance();
//      plugins.put(tipusScan, plugin);
    }      

    return plugins.get(pluginID); 
  }
  
  /**
   * Comprueba si la entidad tiene definido un tipo de escaneo vàlido
   * @param pluginID
   * @return
   */
  @Override
  public boolean teScan(Long pluginID) {
    IScanWebPlugin plugin = null;
    if (pluginID != null && pluginID > 0) {
      try {
        plugin = getInstanceByPluginID(pluginID);
      } catch (Exception e) {
        // En cas d'error el plugin serà null
        log.error("SCAN: Error al obtenir el plugin d'escaneig " + pluginID, e);
      }
    }
//    log.info("SCAN: TeScan de " + tipusScan + " = " + plugin != null);
    return plugin != null;
  }
  
  /**
   * Obtiene el nombre del tipo de scaneo
   * @param pluginID
   * @param locale
   * @return
   */
  @Override
  public String getName(Long pluginID, Locale locale) throws Exception {
//    log.info("Obtenint nom del tipus d'escaneig " + tipusScan);
    return getInstanceByPluginID(pluginID).getName(locale);
  }


  
}
