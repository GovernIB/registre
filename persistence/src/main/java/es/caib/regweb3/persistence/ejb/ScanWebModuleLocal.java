package es.caib.regweb3.persistence.ejb;

import es.caib.regweb3.persistence.utils.ScanWebConfigRegWeb;
import org.fundaciobit.genapp.common.i18n.I18NException;
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


    /**
     *
     * @param request
     * @param scanWebID
     */
    void closeScanWebProcess(HttpServletRequest request, String scanWebID);

    /**
     *
     * @param request
     * @param ess
     */
    void registerScanWebProcess(HttpServletRequest request, ScanWebConfigRegWeb ess);

    /**
     *
     * @param request
     * @param absoluteRequestPluginBasePath
     * @param relativeRequestPluginBasePath
     * @param scanWebID
     * @return
     * @throws I18NException
     * @throws I18NException
     */
    String scanDocument(HttpServletRequest request, String absoluteRequestPluginBasePath, String relativeRequestPluginBasePath, String scanWebID) throws Exception;

    /**
     *
     * @param request
     * @param response
     * @param absoluteRequestPluginBasePath
     * @param relativeRequestPluginBasePath
     * @param scanWebID
     * @param query
     * @param isPost
     * @throws I18NException
     * @throws I18NException
     */
    void requestPlugin(HttpServletRequest request, HttpServletResponse response, String absoluteRequestPluginBasePath, String relativeRequestPluginBasePath,
                       String scanWebID, String query, boolean isPost) throws Exception;

    /**
     *
     * @param request
     * @param scanWebID
     * @return
     */
    ScanWebConfigRegWeb getScanWebConfig(HttpServletRequest request, String scanWebID);

    /**
     *
     * @param ss
     * @return
     * @throws I18NException
     * @throws I18NException
     */
    Set<String> getDefaultFlags(ScanWebConfigRegWeb ss) throws I18NException;

    /**
     *
     * @param entitatID
     * @return
     * @throws I18NException
     */
    boolean entitatTeScan(long entitatID) throws I18NException;

    /**
     *
     * @param entitatID
     * @return
     * @throws I18NException
     */
    boolean entitatPermetScanMasiu(long entitatID) throws I18NException;

    /**
     *
     * @param entitatID
     * @param languageUI
     * @return
     * @throws I18NException
     */
    ScanWebPlainFile obtenerDocumentoSeparador(long entitatID, String languageUI) throws I18NException;

}
