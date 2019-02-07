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
     * Retorna el valor de la propiedad del tamaño máximo de todos los anexos que van a Sir
     * @return
     */
    public static Long getTamanoMaxTotalAnexosSir() {
        final String partialPropertyName = "sir.tamanoMaxTotalAnexos";
        Long valor = getLong(partialPropertyName);

        if(valor == null){//Si no hay ni propiedad global se devuelve por defecto 15 Mb
            return RegwebConstantes.ANEXO_TAMANOMAXTOTAL_SIR;
        }
        return valor;
    }

    /**
     * Retorna el valor de la propiedad del tamaño máximo en bytes permitido al subir un anexo a regweb.
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
    public static String getFormatosAnexosSir() {
        final String partialPropertyName = "sir.formatosAnexos";

        String valor = getString(partialPropertyName);

        if(valor == null){ //si no esta definida la propiedad a nivel global se devuelven los formatos por defecto.
            valor = RegwebConstantes.ANEXO_EXTENSIONES_SIR;
        }
        return valor;
    }

    /**
     * Retorna el valor de la propiedad maximo anexos permitidos de la entidad indicada.
     * @return
     */
    public static Integer getNumeroMaxAnexosSir() {
        final String partialPropertyName = "sir.numMaxAnexos";

        Integer valor = getInteger(partialPropertyName);

        if(valor== null){// si no esta definida la propiedad a nivel global se devuelve por defecto 5.
            valor = RegwebConstantes.ANEXO_NUMEROMAX_SIR;
        }
        return valor;
    }

    /**
     * Devuelve el tamano máximo permitido de un anexo en SIR.
     * @return
     */
    public static Long getTamanoMaximoPorAnexoSir() {
        final String partialPropertyName = "sir.tamanoMaximoPorAnexo";
        Long valor = getLong(partialPropertyName);

        // Valor global si no existeix el de per entitat
        if (valor == null) {
            valor = RegwebConstantes.ANEXO_TAMANOMAX_SIR;
        }
        return valor;
    }

    /**
     * Indicam si volem validar firmes dels annexes introduïts
     * @return
     */
    public static Boolean validarFirmas() {
        final String partialPropertyName = "validarfirmas";

        return getBoolean(partialPropertyName);

    }

    /**
     * Retorna el valor de la propiedad del tamaño máximo en bytes permitido al subir un anexo a regweb.
     * @return
     */
    public static Integer getMaxReintentosSir(Long idEntidad) {
        final String partialPropertyName = "sir.reintentos";
        Integer valor = getIntegerByEntidad(idEntidad, partialPropertyName);

        // Si no está definida retornamos un valor fijo
        if (valor == null) {
            return 10;
        }

        return valor;
    }

    /**
     * Retorna el valor de la propiedad del valor del remitente para el envio de mails
     * @return
     */
    public static String getRemitente() {
        final String partialPropertyName = "mail.remitente";

        return getString(partialPropertyName);
    }

    /**
     * Retorna el valor de la propiedad del valor del nombre del remitente para el envio de mails
     * @return
     */
    public static String getRemitenteNombre() {
        final String partialPropertyName = "mail.remitente.nombre";

        return getString(partialPropertyName);
    }


    /**
     * Retorna el valor de la propiedad DefaultLanguage de la entidad indicada.
     * @return
     */
    public static String getDefaultLanguage() {
        final String partialPropertyName = "defaultlanguage";

        return getString( partialPropertyName);
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

        return getString( partialPropertyName);
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

        return getString( partialPropertyName);
    }


    /**
     * Retorna el valor de la propiedad ArchivosPath de la entidad indicada.
     * @return
     */
    public static String getArchivosPath(Long idEntidad) {
        final String partialPropertyName = "archivos.path";

        return getStringByEntidad(idEntidad, partialPropertyName);
    }

    /**
     * Retorna el valor de la propiedad Dir3CaibServer de la entidad indicada.
     * @return
     */
    public static String getDir3CaibServer() {
        final String partialPropertyName = "dir3caib.server";

        return getString( partialPropertyName);

    }

    /**
     * Retorna el valor de la propiedad Dir3CaibUsername de la entidad indicada.
     * @return
     */
    public static String getDir3CaibUsername() {
        final String partialPropertyName = "dir3caib.username";

        return getString( partialPropertyName);

    }

    /**
     * Retorna el valor de la propiedad Dir3CaibPassword de la entidad indicada.
     * @return
     */
    public static String getDir3CaibPassword() {
        final String partialPropertyName = "dir3caib.password";

        return getString(partialPropertyName);
    }

    /**
     * Retorna el valor de la propiedad SirServerBase de la entidad indicada.
     * @return
     */
    public static String getSirServerBase(Long idEntidad) {
        final String partialPropertyName = "sir.serverbase";

        return  getStringByEntidad(idEntidad, partialPropertyName);
    }

    /**
     * Retorna el valor de la propiedad Fecha Oficio Salida de la entidad indicada.
     * @return
     */
    public static String getFechaOficiosSalida() {
        final String partialPropertyName = "oficioSalida.fecha";

        return getString(partialPropertyName);
    }

    /**
     * Retorna el valor de la propiedad CronExpression para los envios Sir pendientes.
     * Si no está definida devuelve la expresión por defecto
     * @return
     */
    public static String getEnviosSirPendientesCronExpression() {
        final String partialPropertyName = "sir.cronExpression.enviosSirPendientes";
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

        return getString( partialPropertyName);
    }


    /**
     * Devuelve el valor de la propiedad que indica si se permiten adjuntar anexos con firma detached.
     *
     * @return
     */
    public static Boolean getPermitirAnexosDetached(Long idEntidad) {
        final String partialPropertyName = "anexos.permitirfirmadetached";
        return getBooleanByEntidad(idEntidad, partialPropertyName);

    }

    /**
     * Devuelve el valor de la propiedad que indica si se han de cerrar los expedientes de DM
     *
     * @return
     */
    public static Boolean getCerrarExpedientes(Long idEntidad) {
        final String partialPropertyName = "arxiu.cerrarExpedientes";
        return getBooleanByEntidad(idEntidad, partialPropertyName);

    }

    /**
     * Devuelve el valor de la propiedad con la fechaInicio para cerar los expedientes
     *
     * @return
     */
    public static String getFechaInicioCerrarExpedientes(Long idEntidad) {
        final String partialPropertyName = "arxiu.fechaInicio.cerrarExpedientes";
        return getStringByEntidad(idEntidad, partialPropertyName);
    }


    /**
     * Retorna el valor de la propiedad que indica si mostraremos los avisos o no
     *
     * @return
     */
    public static Boolean getMostrarAvisos(Long idEntidad) {
        final String partialPropertyName = "avisos";
        return getBooleanByEntidad(idEntidad, partialPropertyName);

    }

    /**
     * Retorna el valor de la propiedad del Username de la Url de Validación
     * @return
     */
    public static String getUrlValidationUsername(Long idEntidad) {
        final String partialPropertyName = "urlvalidation.username";

        return getStringByEntidad(idEntidad, partialPropertyName);
    }

    /**
     * Retorna el valor de la propiedad del Username de la Url de Validación
     * @return
     */
    public static String getUrlValidationPassword(Long idEntidad) {
        final String partialPropertyName = "urlvalidation.password";

        return getStringByEntidad(idEntidad, partialPropertyName);
    }

    /**
     * Si no está configurado, se devuelven 3 meses
     * @param idEntidad
     * @return
     */
    public static Integer getMesesPurgoAnexos(Long idEntidad) {
        final String partialPropertyName = "anexos.purgo.meses";
        Integer valor = getIntegerByEntidad(idEntidad,partialPropertyName);


        if (valor == null) {
            valor = 3;
        }
        return valor;
    }

    /**
     * Obtenemos el número máximo de reintenso de la cola
     * @param idEntidad
     * @return
     */
    public static Integer getMaxReintentosCola(Long idEntidad) {
        final String partialPropertyName = "cola.maxReintentos";
        Integer valor = getIntegerByEntidad(idEntidad,partialPropertyName);

        if (valor == null) {
            valor = 4;
        }
        return valor;
    }

    /**
     * Devuelve el valor de la propiedad que indica si se generarán Comunicaciones a los usuarios de registro
     *
     * @return
     */
    public static Boolean getGenerarComunicaciones(Long idEntidad) {
        final String partialPropertyName = "comunicaciones.generar";
        return getBooleanByEntidad(idEntidad, partialPropertyName);

    }

    /**
     * Propiedad temporal que nos permite indicar si no queremos distribuir
     * @param idEntidad
     * @return
     */
    public static boolean getNoDistribuir(Long idEntidad){
        final String partialPropertyName = "nodistribuir";
        return getBooleanByEntidad(idEntidad, partialPropertyName);
    }

    /**
     * Propiedad que permite mostrar un enlace en le menú a la instalación de DIR3CAIB
     * @param idEntidad
     * @return
     */
    public static boolean getEnlaceDir3(Long idEntidad){
        final String partialPropertyName = "enlaceDir3";
        return getBooleanByEntidad(idEntidad, partialPropertyName);
    }

    /**
     *
     * @param idEntidad
     * @param partialPropertyName
     * @return
     */
    private static String getStringByEntidad(Long idEntidad, final String partialPropertyName) {
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
    private static String getString(final String partialPropertyName) {
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
    private static Long getLongByEntidad(Long idEntidad, final String partialPropertyName) {
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
    private static Long getLong(final String partialPropertyName) {
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
    private static Boolean getBooleanByEntidad(Long idEntidad, final String partialPropertyName) {
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
    private static Boolean getBoolean(final String partialPropertyName) {
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
    private static Integer getIntegerByEntidad(Long idEntidad, final String partialPropertyName) {
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
    private static Integer getInteger(final String partialPropertyName) {
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
    private static PropiedadGlobalLocal getPropiedadGlobalEJB() throws Exception {

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
