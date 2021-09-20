package es.caib.regweb3.persistence.ejb;

import es.caib.regweb3.persistence.utils.ScanWebConfigRegWeb;
import org.fundaciobit.genapp.common.i18n.I18NException;
import org.fundaciobit.pluginsib.scanweb.api.IScanWebPlugin;
import org.fundaciobit.pluginsib.scanweb.api.ScanWebPlainFile;

import javax.ejb.Local;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Set;


/**
 * @author anadal migracio a ScanWebApi 2.0.0 (06/07/2016)
 */
@Local
public interface ScanWebModuleLocal {

    String JNDI_NAME = "java:app/regweb3-persistence/ScanWebModuleEJB";


    void closeScanWebProcess(HttpServletRequest request, String scanWebID);


    void registerScanWebProcess(HttpServletRequest request, ScanWebConfigRegWeb ess);


    String scanDocument(
            HttpServletRequest request, String absoluteRequestPluginBasePath,
            String relativeRequestPluginBasePath,
            String scanWebID) throws Exception, I18NException;


    void requestPlugin(HttpServletRequest request, HttpServletResponse response,
                       String absoluteRequestPluginBasePath, String relativeRequestPluginBasePath,
                       String scanWebID, String query, boolean isPost) throws Exception, I18NException;


    ScanWebConfigRegWeb getScanWebConfig(HttpServletRequest request,
                                         String scanWebID);

    Set<String> getDefaultFlags(ScanWebConfigRegWeb ss) throws Exception, I18NException;

    boolean entitatTeScan(long entitatID) throws I18NException;

    boolean entitatPermetScanMasiu(long entitatID) throws I18NException;

    public ScanWebPlainFile obtenerDocumentoSeparador(long entitatID, String languageUI) throws I18NException;

    IScanWebPlugin getInstanceByEntitatID(long entitatID) throws I18NException;

}
