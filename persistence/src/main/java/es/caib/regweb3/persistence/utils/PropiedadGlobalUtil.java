package es.caib.regweb3.persistence.utils;

import es.caib.regweb3.persistence.ejb.PropiedadGlobalLocal;
import es.caib.regweb3.utils.RegwebConstantes;
import org.apache.log4j.Logger;

import javax.naming.InitialContext;

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
            log.error("Error obteniendo la propiedad ]" + RegwebConstantes.REGWEB3_PROPERTY_BASE + partialPropertyName, e);
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
            return null;
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
            return null;
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

}
