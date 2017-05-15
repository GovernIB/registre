package es.caib.regweb3.persistence.utils;

import es.caib.regweb3.model.PropiedadGlobal;
import es.caib.regweb3.persistence.ejb.PropiedadGlobalLocal;
import es.caib.regweb3.utils.RegwebConstantes;
import org.apache.log4j.Logger;

import javax.naming.InitialContext;
import java.util.List;
import java.util.Properties;

/**
 * Created by earrivi on 03/10/2016.
 */
public class PropiedadGlobalUtil {

    protected static final Logger log = Logger.getLogger(PropiedadGlobalUtil.class);

    protected static PropiedadGlobalLocal propiedadGlobalEjb;


    /**
     * Retorna el valor de la propiedad Results per page de la entidad indicada.
     * @return
     */
    public static Integer getResultsPerPage(Long idEntidad) {
        final String partialPropertyName = "resultsperpage";
        Integer valor = getIntegerByEntidad(idEntidad, partialPropertyName);

        // Valor global si no existeix el de per entitat
        if (valor == null) {
            valor = 10;
        }
        return valor;
    }

    /**
     * Retorna el valor de la propiedad Results per page Oficios de la entidad indicada.
     * @return
     */
    public static Integer getResultsPerPageOficios(Long idEntidad) {
        final String partialPropertyName = "resultsperpage.oficios";
        Integer valor = getIntegerByEntidad(idEntidad, partialPropertyName);

        // Valor global si no existeix el de per entitat
        if (valor == null) {
            valor = 10;
        }
        return valor;
    }

    /**
     * Retorna el valor de la propiedad Results per page Lopd de la entidad indicada.
     * @return
     */
    public static Integer getResultsPerPageLopd(Long idEntidad) {
        final String partialPropertyName = "resultsperpage.lopd";
        Integer valor = getIntegerByEntidad(idEntidad, partialPropertyName);

        // Valor global si no existeix el de per entitat
        if (valor == null) {
            valor = 10;
        }
        return valor;
    }

    /**
     * Retorna el valor de la propiedad del tamaño máximo en bytes por anexo de la entidad indicada.
     * @return
     */
    public static Long getMaxUploadSizeTotal(Long idEntidad) {
        final String partialPropertyName = "maxuploadsizetotal";
        Long valor = getLongByEntidad(idEntidad, partialPropertyName);

        // Valor global si no existeix el de per entitat
        if (valor == null) {
            valor = getLong(partialPropertyName);
        }
        if(valor == null){//Si no hay ni propiedad global se devuelve por defecto 15 Mb
            return new Long(15728640);
        }
        return valor;
    }

    /**
     * Retorna el valor de la propiedad del tamaño máximo en bytes por anexo de la entidad indicada.
     * @return
     */
    public static Long getMaxUploadSizeInBytes(Long idEntidad) {
        final String partialPropertyName = "maxuploadsizeinbytes";
        Long valor = getLongByEntidad(idEntidad, partialPropertyName);

        // Valor global si no existeix el de per entitat
        if (valor == null) {
            valor = getLong(partialPropertyName);
        }
        return valor;
    }

    /**
     * Retorna el valor de la propiedad de formatos permitidos de la entidad indicada.
     * @return
     */
    public static String getFormatosPermitidos(Long idEntidad) {
        final String partialPropertyName = "formatospermitidos";
        String valor = getStringByEntidad(idEntidad, partialPropertyName);

        // Valor global si no existeix el de per entitat
        if (valor == null) {
            valor = getString(partialPropertyName);
        }
        if(valor == null){ //si no esta definida la propiedad a nivel global se devuelven los formatos por defecto.
            valor = new String(".jpg, .jpeg, .odt, .odp, .ods, .odg, .docx, .xlsx, .pptx, .pdf, .png, .rtf, .svg, .tiff, .txt., .xml, .xsig");
        }
        return valor;
    }

    /**
     * Retorna el valor de la propiedad maximo anexos permitidos de la entidad indicada.
     * @return
     */
    public static Integer getMaxAnexosPermitidos(Long idEntidad) {
        final String partialPropertyName = "maxanexospermitidos";
        Integer valor = getIntegerByEntidad(idEntidad, partialPropertyName);

        // Valor global si no existeix el de per entitat
        if (valor == null) {
            valor = getInteger(partialPropertyName);
        }
        if(valor== null){// si no esta definida la propiedad a nivel global se devuelve por defecto 5.
            valor = 5;
        }
        return valor;
    }



    /**
     * Retorna el valor de la propiedad DefaultLanguage de la entidad indicada.
     * @return
     */
    public static String getDefaultLanguage() {
        final String partialPropertyName = "defaultlanguage";
        String valor = getString( partialPropertyName);

        return valor != null ? valor : null;
    }


    /**
     * Retorna el valor de la propiedad de MIME permitidos de la entidad indicada.
     * Basado en el catalogo de estandares de ENI
     * @return
     */
    public static String getMIMEPermitidos() {
        final String partialPropertyName = "mimepermitidos";
        String valor = getString(partialPropertyName);

        if(valor == null){ //si no esta definida la propiedad a nivel global se devuelven los formatos por defecto.
            valor = new String("image/jpeg, image/pjpeg, application/vnd.oasis.opendocument.text, application/vnd.oasis.opendocument.spreadsheet, application/vnd.openxmlformats-officedocument.wordprocessingml.document, application/vnd.openxmlformats-officedocument.spreadsheetml.sheet, application/mspowerpoint, application/powerpoint, application/x-mspowerpoint, application/pdf, image/png, text/rtf, application/rtf, application/x-rtf, image/svg+xml, image/tiff, image/x-tiff, text/plain, application/xml");
        }
        return valor;
    }

    /**
     * Retorna el valor de la propiedad IsCaib de la entidad indicada.
     * @return
     */
    public static Boolean getIsCaib() {
        final String partialPropertyName = "iscaib";

        return getBoolean(partialPropertyName);

    }

    /**
     * Retorna el valor de la propiedad PreregistreUrl de la entidad indicada.
     * @return
     */
    public static String getPreregistreUrl() {
        final String partialPropertyName = "preregistre";
        String valor = getString( partialPropertyName);

        return valor != null ? valor : null;
    }

    /**
     * Retorna el valor de la propiedad ShowTimestamp de la entidad indicada.
     * @return
     */
    public static Boolean getShowTimeStamp() {
        final String partialPropertyName = "showtimestamp";

        return getBoolean(partialPropertyName);

    }

    /**
     * Retorna el valor de la propiedad ShowAnnexes de la entidad indicada.
     * @return
     */
    public static Boolean getShowAnnexes() {
        final String partialPropertyName = "showannexes";

        return getBoolean(partialPropertyName);

    }


    /**
     * Retorna la clase del plugin de distribución de la entidad
     * @return
     */

    public static String getPluginDistribucion(Long idEntidad, String basePlugin) {

        String valor = getStringByEntidad(idEntidad, basePlugin);

        if(valor == null){
            return RegwebConstantes.PLUGIN_DISTRIBUCION_CLASS_MOCK;
        }

        return valor;
    }


    /**
     * Retorna la clase del plugin de distribución de la entidad
     * @return
     */
    public static String getPluginPostProceso(Long idEntidad) {
        final String partialPropertyName = "postproceso.plugin";
        String valor = getStringByEntidad(idEntidad, partialPropertyName);

        // Valor global si no existeix el de per entitat
        if (valor == null) {
            valor = getString(partialPropertyName);
        }
        return valor;
    }


    /**
     * Retorna el valor de la propiedad MaxUploadSizeInBytes.
     * Tamaño máximo de subida de ficheros en bytes. No definido significa sin límites.
     *
     * @return
     */
    public static Long getMaxUploadSizeInBytes() {
        final String partialPropertyName = "maxuploadsizeinbytes";
        return getLong(partialPropertyName);
    }

    /**
     * Retorna el valor de la propiedad HibernateDialect de la entidad indicada.
     * @return
     */
    public static String getHibernateDialect() {
        final String partialPropertyName = "hibernate.dialect";
        String valor = getString( partialPropertyName);

        return valor != null ? valor : null;
    }


    /**
     * Retorna el valor de la propiedad ArchivosPath de la entidad indicada.
     * @return
     */
    public static String getArchivosPath(Long idEntidad) {
        final String partialPropertyName = "archivos.path";
        String valor = getStringByEntidad(idEntidad, partialPropertyName);

        return valor != null ? valor : null;
    }

    /**
     * Retorna el valor de la propiedad UserInformationPlugin de la entidad indicada.
     * @return
     */
    public static String getUserInformationPlugin(Long idEntidad) {
        final String partialPropertyName = "userinformationplugin";
        String valor = getStringByEntidad(idEntidad, partialPropertyName);

        return valor != null ? valor : null;
    }

    /**
     * Retorna el valor de la propiedad DocumentCustodyPlugin de la entidad indicada.
     * @return
     */
    public static String getDocumentCustodyPlugin(Long idEntidad) {
        final String partialPropertyName = "annex.documentcustodyplugin";
        String valor = getStringByEntidad(idEntidad, partialPropertyName);

        return valor != null ? valor : null;
    }

    /**
     * Retorna el valor de la propiedad Dir3CaibServer de la entidad indicada.
     * @param idEntidad
     * @return
     */
    public static String getDir3CaibServer(Long idEntidad) {
        final String partialPropertyName = "dir3caib.server";
        String valor = getStringByEntidad(idEntidad, partialPropertyName);

        return valor != null ? valor : null;
    }

    /**
     * Retorna el valor de la propiedad Dir3CaibUsername de la entidad indicada.
     * @param idEntidad
     * @return
     */
    public static String getDir3CaibUsername(Long idEntidad) {
        final String partialPropertyName = "dir3caib.username";
        String valor = getStringByEntidad(idEntidad, partialPropertyName);

        return valor != null ? valor : null;
    }

    /**
     * Retorna el valor de la propiedad Dir3CaibPassword de la entidad indicada.
     * @param idEntidad
     * @return
     */
    public static String getDir3CaibPassword(Long idEntidad) {
        final String partialPropertyName = "dir3caib.password";
        String valor = getStringByEntidad(idEntidad, partialPropertyName);

        return valor != null ? valor : null;
    }

    /**
     * Retorna el valor de la propiedad SirServerBase de la entidad indicada.
     * @return
     */
    public static String getSirServerBase(Long idEntidad) {
        final String partialPropertyName = "sir.serverbase";
        String valor = getStringByEntidad(idEntidad, partialPropertyName);

        return valor != null ? valor : null;
    }

    /**
     * Retorna el valor de la propiedad SirServerBase de la entidad indicada.
     * @return
     */
    public static Boolean getSirUseDirectApi(Long idEntidad) {
        final String partialPropertyName = "sir.usedirectapi";

        return getBooleanByEntidad(idEntidad, partialPropertyName);
    }


    /**
     * Retorna el valor de la propiedad JustificantePlugin de la entidad indicada.
     * @return
     */
    public static String getJustificantePlugin(Long idEntidad, String basePlugin) {

        String valor = getStringByEntidad(idEntidad, basePlugin);

        if(valor == null){
            return RegwebConstantes.PLUGIN_JUSTIFICANTE_CLASS_MOCK;
        }

        return valor;
    }

    /**
     //     * Retorna el valor de la propiedad Mensaje Estampacion Justificante de la entidad indicada.
     //     * @return
     //     */
    public static String getMensajeEstampacionJustificante(Long idEntidad) {
        final String partialPropertyName = "justificante.mensaje.estampacion";
        String valor = getStringByEntidad(idEntidad, partialPropertyName);

        return valor != null ? valor : null;
    }


    /**
     * Retorna el valor de la propiedad CronExpression para inicializar contadores libro.
     * Si no está definida devuelve la expresión por defecto
     * @return
     */
    public static String getInicializarContadoresCronExpression() {
        final String partialPropertyName = "cronExpression.inicializarContadores";
        String valor = getString( partialPropertyName);

        return valor != null ? valor : RegwebConstantes.CRON_INICIALIZAR_CONTADORES;
    }

    /**
     * Retorna el valor de la propiedad CronExpression para los envios Sir pendientes.
     * Si no está definida devuelve la expresión por defecto
     * @return
     */
    public static String getEnviosSirPendientesCronExpression() {
        final String partialPropertyName = "cronExpression.enviosSirPendientes";
        String valor = getString( partialPropertyName);

        return valor != null ? valor : RegwebConstantes.CRON_ENVIOS_SIR_PENDIENTES;
    }
    
    /**
     * Els ApACHes de la CAIB usen la IP de destí enlloc de la URL de cridada, d'aqui 
     * que s'hagi de sobreescriure a mà la URL real.
     * @return
     */
    public static String getScanWebAbsoluteURL() {
      final String partialPropertyName = "scanweb.absoluteurl";
      String valor = getString( partialPropertyName);
      return valor;
    }



    /**
     *
     * @param idEntidad
     * @param partialPropertyName
     * @return
     */
    protected static String getStringByEntidad(Long idEntidad, final String partialPropertyName) {
        try {
            PropiedadGlobalLocal propiedadGlobalEjb = getPropiedadGlobalEJB();
            return propiedadGlobalEjb.getPropertyByEntidad(idEntidad,RegwebConstantes.REGWEB3_PROPERTY_BASE + partialPropertyName);
        } catch (Exception e) {
            log.error("Error obteniendo la propiedad " + RegwebConstantes.REGWEB3_PROPERTY_BASE + partialPropertyName, e);
            return null;
        }
    }


    /**
     *
     * @param partialPropertyName
     * @return
     */
    protected static String getString(final String partialPropertyName) {
        try {
            PropiedadGlobalLocal propiedadGlobalEjb = getPropiedadGlobalEJB();
            return propiedadGlobalEjb.getProperty(RegwebConstantes.REGWEB3_PROPERTY_BASE + partialPropertyName);
        } catch (Exception e) {
            log.error("Error obteniendo la propiedad ]" + RegwebConstantes.REGWEB3_PROPERTY_BASE + partialPropertyName, e);
            return null;
        }
    }

    /**
     *
     * @param idEntidad
     * @param partialPropertyName
     * @return
     */
    protected static Long getLongByEntidad(Long idEntidad, final String partialPropertyName) {
        try {
            PropiedadGlobalLocal propiedadGlobalEjb = getPropiedadGlobalEJB();
            return propiedadGlobalEjb.getLongPropertyByEntitat(idEntidad,RegwebConstantes.REGWEB3_PROPERTY_BASE + partialPropertyName);
        } catch (Exception e) {
            log.error("Error obteniendo la propiedad ]" + RegwebConstantes.REGWEB3_PROPERTY_BASE + partialPropertyName, e);
            return null;
        }
    }

    /**
     *
     * @param partialPropertyName
     * @return
     */
    protected static Long getLong(final String partialPropertyName) {
        try {
            PropiedadGlobalLocal propiedadGlobalEjb = getPropiedadGlobalEJB();
            return propiedadGlobalEjb.getLongProperty(RegwebConstantes.REGWEB3_PROPERTY_BASE + partialPropertyName);
        } catch (Exception e) {
            log.error("Error obteniendo la propiedad ]" + RegwebConstantes.REGWEB3_PROPERTY_BASE + partialPropertyName, e);
            return null;
        }
    }

    /**
     *
     * @param idEntidad
     * @param partialPropertyName
     * @return
     */
    protected static Boolean getBooleanByEntidad(Long idEntidad, final String partialPropertyName) {
        try {
            PropiedadGlobalLocal propiedadGlobalEjb = getPropiedadGlobalEJB();
            return propiedadGlobalEjb.getBooleanPropertyByEntidad(idEntidad,RegwebConstantes.REGWEB3_PROPERTY_BASE + partialPropertyName);
        } catch (Exception e) {
            log.error("Error obteniendo la propiedad ]" + RegwebConstantes.REGWEB3_PROPERTY_BASE + partialPropertyName, e);
            return false;
        }
    }

    /**
     *
     * @param partialPropertyName
     * @return
     */
    protected static Boolean getBoolean(final String partialPropertyName) {
        try {
            PropiedadGlobalLocal propiedadGlobalEjb = getPropiedadGlobalEJB();
            return propiedadGlobalEjb.getBooleanProperty(RegwebConstantes.REGWEB3_PROPERTY_BASE + partialPropertyName);
        } catch (Exception e) {
            log.error("Error obteniendo la propiedad ]" + RegwebConstantes.REGWEB3_PROPERTY_BASE + partialPropertyName, e);
            return false;
        }
    }

    /**
     *
     * @param idEntidad
     * @param partialPropertyName
     * @return
     */
    protected static Integer getIntegerByEntidad(Long idEntidad, final String partialPropertyName) {
        try {
            PropiedadGlobalLocal propiedadGlobalEjb = getPropiedadGlobalEJB();
            return propiedadGlobalEjb.getIntegerPropertyByEntitat(idEntidad,RegwebConstantes.REGWEB3_PROPERTY_BASE + partialPropertyName);
        } catch (Exception e) {
            log.error("Error obteniendo la propiedad ]" + RegwebConstantes.REGWEB3_PROPERTY_BASE + partialPropertyName, e);
            return null;
        }
    }

    /**
     *
     * @param partialPropertyName
     * @return
     */
    protected static Integer getInteger(final String partialPropertyName) {
        try {
            PropiedadGlobalLocal propiedadGlobalEjb = getPropiedadGlobalEJB();
            return propiedadGlobalEjb.getIntegerProperty(RegwebConstantes.REGWEB3_PROPERTY_BASE + partialPropertyName);
        } catch (Exception e) {
            log.error("Error obteniendo la propiedad ]" + RegwebConstantes.REGWEB3_PROPERTY_BASE + partialPropertyName, e);
            return null;
        }
    }


    /**
     * Obtiene la referencia al ejb de PropiedadGlobalLocal
     * @return
     * @throws Exception
     */
    public static PropiedadGlobalLocal getPropiedadGlobalEJB() throws Exception {

        if (propiedadGlobalEjb == null) {
            try {
                propiedadGlobalEjb = (PropiedadGlobalLocal) new InitialContext().lookup("regweb3/PropiedadGlobalEJB/local");
            } catch (Throwable e) {
                log.error("No se ha podido instanciar PropiedadGlobalEJB");
                throw new Exception(e);

            }
        }
        return propiedadGlobalEjb;
    }


    /**
     * Retorna la base del plugin de SignatureServer de la entidad
     * @return
     */
    public static String getBasePluginSignatureServer(Long idEntidad) {
        final String partialPropertyName = "signatureserver.plugin.base";
        String valor = getStringByEntidad(idEntidad, partialPropertyName);

        // Valor global si no existeix el de per entitat
        if (valor == null) {
            valor = getString(partialPropertyName);
        }
        return valor;

    }

    /**
     * Retorna totes les Properties d'una Entitat
     * @return
     */
    public static Properties getAllPropertiesByEntity(Long idEntidad) {
        try {
            PropiedadGlobalLocal propiedadGlobalEjb = getPropiedadGlobalEJB();
            List<PropiedadGlobal> list = propiedadGlobalEjb.getAllPropertiesByEntidad(idEntidad);
            Properties p = new Properties();
            for(PropiedadGlobal pg : list){
                p.setProperty(pg.getClave(), pg.getValor());
            }
            return p;
        } catch (Exception e) {
            log.error("Error obteniendo las propiedades de la Entidad " + idEntidad + ": " + e.getMessage(), e);
            return null;
        }
    }
    
    
    /**
     * Retorna totes les Properties d'una Entitat
     * @return
     */
    public static Properties getAllProperties() {
        try {
            PropiedadGlobalLocal propiedadGlobalEjb = getPropiedadGlobalEJB();
            List<PropiedadGlobal> list = propiedadGlobalEjb.getAllProperties();
            Properties p = new Properties();
            for(PropiedadGlobal pg : list){
                p.setProperty(pg.getClave(), pg.getValor());
            }
            return p;
        } catch (Exception e) {
            log.error("Error obteniendo las propiedades no asociadas a alguna Entidad: " + e.getMessage(), e);
            return null;
        }
    }

}
