package es.caib.regweb3.persistence.utils;

import es.caib.regweb3.model.PropiedadGlobal;
import es.caib.regweb3.persistence.ejb.PropiedadGlobalLocal;
import es.caib.regweb3.utils.RegwebConstantes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.naming.InitialContext;
import java.util.List;
import java.util.Properties;

/**
 * Created by earrivi on 03/10/2016.
 */
public class PropiedadGlobalUtil {

    protected static final Logger log = LoggerFactory.getLogger(PropiedadGlobalUtil.class);

    protected static PropiedadGlobalLocal propiedadGlobalEjb;


    /**
     * Retorna el número de organismos que se mostrarán en el select del módulo de Interesados.
     * Propiedad: es.caib.regweb3.interesado.organismos
     * @param idEntidad
     * @return
     */
    public static Integer getTotalOrganismosSelect(Long idEntidad) {
        final String partialPropertyName = "interesado.organismos";
        Integer valor = getIntegerByEntidad(idEntidad, partialPropertyName);

        // Valor global si no existeix el de per entitat
        if (valor == null) {
            valor = 10;
        }
        return valor;
    }

    /**
     * Retorna el valor de la propiedad de la ruta ERTE
     * Propiedad: es.caib.regweb3.urlvalidation.password
     * @param idEntidad
     * @return
     */
    public static String getErtePath(Long idEntidad) {
        final String partialPropertyName = "erte.path";

        return getStringByEntidad(idEntidad, partialPropertyName);
    }

    /**
     * Retorna el valor de la propiedad de la entidad indicada.
     * Propiedad: es.caib.regweb3.sesion.minutosPurgado.iniciadas
     * @param idEntidad
     * @return
     */
    public static Integer getSesionMinutosPurgadoIniciadas(Long idEntidad) {
        final String partialPropertyName = "sesion.minutosPurgado.iniciadas";
        Integer valor = getIntegerByEntidad(idEntidad, partialPropertyName);

        // Valor global si no existeix el de per entitat
        if (valor == null) {
            valor = 60;
        }
        return valor;
    }

    /**
     * Retorna el valor de la propiedad de la entidad indicada.
     * Propiedad: es.caib.regweb3.sesion.minutosPurgado.finalizadas
     * @param idEntidad
     * @return
     */
    public static Integer getSesionMinutosPurgadoFinalizadas(Long idEntidad) {
        final String partialPropertyName = "sesion.minutosPurgado.finalizadas";
        Integer valor = getIntegerByEntidad(idEntidad, partialPropertyName);

        // Valor global si no existeix el de per entitat
        if (valor == null) {
            valor = 60;
        }
        return valor;
    }

    /**
     * Retorna el valor de la propiedad de la entidad indicada.
     * Propiedad: es.caib.regweb3.sesion.minutosPurgado.noIniciadas
     * @param idEntidad
     * @return
     */
    public static Integer getSesionMinutosPurgadoNoIniciadas(Long idEntidad) {
        final String partialPropertyName = "sesion.minutosPurgado.noIniciadas";
        Integer valor = getIntegerByEntidad(idEntidad, partialPropertyName);

        // Valor global si no existeix el de per entitat
        if (valor == null) {
            valor = 60;
        }
        return valor;
    }

    /**
     * Retorna el valor de la propiedad Results per page de la entidad indicada.
     * Propiedad: es.caib.regweb3.resultsperpage
     * @param idEntidad
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
     * Propiedad: es.caib.regweb3.resultsperpage.oficios
     * @param idEntidad
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
     * Propiedad: es.caib.regweb3.resultsperpage.lopd
     * @param idEntidad
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
     * Propiedad: es.caib.regweb3.sir.tamanoMaxTotalAnexos
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
     * Propiedad: es.caib.regweb3.maxuploadsizeinbytes
     * @param idEntidad
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
     * Retorna el valor de la propiedad maximo anexos permitidos de la entidad indicada.
     * Propiedad: es.caib.regweb3.sir.numMaxAnexos
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
     * Propiedad: es.caib.regweb3.sir.tamanoMaximoPorAnexo
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
     * Devuelve el valor de la propiedad que indica si se han de purgar los anexos de los Registros distribuidos
     * Propiedad: es.caib.regweb3.purgarAnexosDistribuidos
     * @param idEntidad
     * @return
     */
    public static Boolean getPurgarAnexosDistribuidos(Long idEntidad) {
        final String partialPropertyName = "anexos.purgarAnexosDistribuidos";
        return getBooleanByEntidad(idEntidad, partialPropertyName);

    }

    /**
     * Retorna el valor de la propiedad del tamaño máximo en bytes permitido al subir un anexo a regweb.
     * Propiedad: es.caib.regweb3.sir.reintentos
     * @param idEntidad
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
     * Propiedad: es.caib.regweb3.mail.remitente
     * @return
     */
    public static String getRemitente(Long idEntidad) {

        final String partialPropertyName = "mail.remitente";

        return getStringByEntidad(idEntidad, partialPropertyName);
    }


    /**
     * Retorna el valor de la propiedad DefaultLanguage de la entidad indicada.
     * Propiedad: es.caib.regweb3.defaultlanguage
     * @return
     */
    public static String getDefaultLanguage() {
        final String partialPropertyName = "defaultlanguage";

        return getString( partialPropertyName);
    }


    /**
     * Retorna el valor de la propiedad IsCaib de la entidad indicada.
     * Propiedad: es.caib.regweb3.iscaib
     * @return
     */
    public static Boolean getIsCaib() {
        final String partialPropertyName = "iscaib";

        return getBoolean(partialPropertyName);

    }

    /**
     * Retorna el valor de la propiedad PreregistreUrl de la entidad indicada.
     * Propiedad: es.caib.regweb3.preregistre
     * @return
     */
    public static String getPreregistreUrl() {
        final String partialPropertyName = "preregistre";

        return getString( partialPropertyName);
    }

    /**
     * Retorna el valor de la propiedad ShowTimestamp de la entidad indicada.
     * Propiedad: es.caib.regweb3.showtimestamp
     * @return
     */
    public static Boolean getShowTimeStamp() {
        final String partialPropertyName = "showtimestamp";

        return getBoolean(partialPropertyName);

    }

    /**
     * Retorna el valor de la propiedad MaxUploadSizeInBytes.
     * Tamaño máximo de subida de ficheros en bytes. No definido significa sin límites.
     * Propiedad: es.caib.regweb3.maxuploadsizeinbytes
     *
     * @return
     */
    public static Long getMaxUploadSizeInBytes() {
        final String partialPropertyName = "maxuploadsizeinbytes";

        return getLong(partialPropertyName);
    }

    /**
     * Retorna el valor de la propiedad HibernateDialect de la entidad indicada.
     * Propiedad: es.caib.regweb3.hibernate.dialect
     * @return
     */
    public static String getHibernateDialect() {
        final String partialPropertyName = "hibernate.dialect";

        return getString( partialPropertyName);
    }


    /**
     * Retorna el valor de la propiedad ArchivosPath de la entidad indicada.
     * Propiedad: es.caib.regweb3.archivos.path
     * @param idEntidad
     * @return
     */
    public static String getArchivosPath(Long idEntidad) {
        final String partialPropertyName = "archivos.path";

        return getStringByEntidad(idEntidad, partialPropertyName);
    }

    /**
     * Retorna el valor de la propiedad Dir3CaibServer de la entidad indicada.
     * Propiedad: es.caib.regweb3.dir3caib.server
     * @return
     */
    public static String getDir3CaibServer() {
        final String partialPropertyName = "dir3caib.server";

        return getString( partialPropertyName);

    }

    /**
     * Retorna el valor de la propiedad Dir3CaibUsername de la entidad indicada.
     * Propiedad: es.caib.regweb3.dir3caib.username
     * @return
     */
    public static String getDir3CaibUsername() {
        final String partialPropertyName = "dir3caib.username";

        return getString( partialPropertyName);

    }

    /**
     * Retorna el valor de la propiedad Dir3CaibPassword de la entidad indicada.
     * Propiedad: es.caib.regweb3.dir3caib.password
     * @return
     */
    public static String getDir3CaibPassword() {
        final String partialPropertyName = "dir3caib.password";

        return getString(partialPropertyName);
    }

    /**
     * Retorna el valor de la propiedad Dir3CaibServer de la entidad indicada.
     * Propiedad: es.caib.regweb3.dir3caib.server
     * @param idEntidad
     * @return
     */
    public static String getDir3CaibServer(Long idEntidad) {
        final String partialPropertyName = "dir3caib.server";

        return getStringByEntidad(idEntidad, partialPropertyName);

    }

    /**
     * Retorna el valor de la propiedad Dir3CaibUsername de la entidad indicada.
     * Propiedad: es.caib.regweb3.dir3caib.username
     * @param idEntidad
     * @return
     */
    public static String getDir3CaibUsername(Long idEntidad) {
        final String partialPropertyName = "dir3caib.username";

        return getStringByEntidad(idEntidad, partialPropertyName);
    }

    /**
     * Retorna el valor de la propiedad Dir3CaibPassword de la entidad indicada.
     * Propiedad: es.caib.regweb3.dir3caib.password
     * @param idEntidad
     * @return
     */
    public static String getDir3CaibPassword(Long idEntidad) {
        final String partialPropertyName = "dir3caib.password";

        return getStringByEntidad(idEntidad, partialPropertyName);
    }

    /**
     * Retorna el valor de la propiedad CarpetaEnviarMEnsajes de la entidad indicada.
     * Propiedad: es.caib.regweb3.carpeta.enviarMensajes
     * @param idEntidad
     * @return
     */
    public static Boolean getCarpetaEnviarMensajes(Long idEntidad) {
        final String partialPropertyName = "carpeta.enviarMensajes";

        return getBooleanByEntidad(idEntidad, partialPropertyName);

    }

    /**
     * Retorna el valor de la propiedad CarpetaServer de la entidad indicada.
     * Propiedad: es.caib.regweb3.carpeta.server
     * @param idEntidad
     * @return
     */
    public static String getCarpetaServer(Long idEntidad) {
        final String partialPropertyName = "carpeta.server";

        return getStringByEntidad(idEntidad, partialPropertyName);

    }

    /**
     * Retorna el valor de la propiedad CarpetaUsername de la entidad indicada.
     * Propiedad: es.caib.regweb3.carpeta.username
     * @param idEntidad
     * @return
     */
    public static String getCarpetaUsername(Long idEntidad) {
        final String partialPropertyName = "carpeta.username";

        return getStringByEntidad(idEntidad, partialPropertyName);
    }

    /**
     * Retorna el valor de la propiedad CarpetaPassword de la entidad indicada.
     * Propiedad: es.caib.regweb3.carpeta.password
     * @param idEntidad
     * @return
     */
    public static String getCarpetaPassword(Long idEntidad) {
        final String partialPropertyName = "carpeta.password";

        return getStringByEntidad(idEntidad, partialPropertyName);
    }

    /**
     * Retorna el valor de la propiedad CarpetaMotificacionCode de la entidad indicada.
     * Propiedad: es.caib.regweb3.carpeta.notificationCode
     * @param idEntidad
     * @return
     */
    public static String getCarpetaNotificationCode(Long idEntidad) {
        final String partialPropertyName = "carpeta.notificationCode";

        return getStringByEntidad(idEntidad, partialPropertyName);
    }

    /**
     * Retorna el valor de la propiedad SirServerBase de la entidad indicada.
     * Propiedad: es.caib.regweb3.sir.serverbase
     * @param idEntidad
     * @return
     */
    public static String getSirServerBase(Long idEntidad) {
        final String partialPropertyName = "sir.serverbase";

        return getStringByEntidad(idEntidad, partialPropertyName);
    }


    /**
     * Retorna el valor de la propiedad InterDocServer.
     * Propiedad: es.caib.regweb3.dir3caib.server
     *
     * @return
     */
    public static String getInterDocServer() {
        final String partialPropertyName = "interdoc.server";

        return getString(partialPropertyName);

    }

    /**
     * Retorna el valor de la propiedad InterDocUsername.
     * Propiedad: es.caib.regweb3.dir3caib.username
     *
     * @return
     */
    public static String getInterDocUsername() {
        final String partialPropertyName = "interdoc.username";

        return getString(partialPropertyName);

    }

    /**
     * Retorna el valor de la propiedad InterDocPassword.
     * Propiedad: es.caib.regweb3.dir3caib.password
     *
     * @return
     */
    public static String getInterDocPassword() {
        final String partialPropertyName = "interdoc.password";

        return getString(partialPropertyName);
    }

    /**
     * Retorna el valor de la propiedad InterDocServer de la entidad indicada.
     * Propiedad: es.caib.regweb3.dir3caib.server
     *
     * @param idEntidad
     * @return
     */
    public static String getInterDocServer(Long idEntidad) {
        final String partialPropertyName = "interdoc.server";

        return getStringByEntidad(idEntidad, partialPropertyName);

    }

    /**
     * Retorna el valor de la propiedad InterDocUsername de la entidad indicada.
     * Propiedad: es.caib.regweb3.dir3caib.username
     *
     * @param idEntidad
     * @return
     */
    public static String getInterDocUsername(Long idEntidad) {
        final String partialPropertyName = "interdoc.username";

        return getStringByEntidad(idEntidad, partialPropertyName);
    }

    /**
     * Retorna el valor de la propiedad InterDocPassword de la entidad indicada.
     * Propiedad: es.caib.regweb3.dir3caib.password
     *
     * @param idEntidad
     * @return
     */
    public static String getInterDocPassword(Long idEntidad) {
        final String partialPropertyName = "interdoc.password";

        return getStringByEntidad(idEntidad, partialPropertyName);
    }

    /**
     * Retorna el valor de la propiedad Fecha Oficio Salida de la entidad indicada.
     * Propiedad: es.caib.regweb3.oficioSalida.fecha
     *
     * @return
     */
    public static String getFechaOficiosSalida() {
        final String partialPropertyName = "oficioSalida.fecha";

        return getString(partialPropertyName);
    }


    /**
     * Devuelve el valor de la propiedad que indica si se permiten adjuntar anexos con firma detached.
     * Propiedad: es.caib.regweb3.anexos.permitirfirmadetached
     * @param idEntidad
     * @return
     */
    public static Boolean getPermitirAnexosDetached(Long idEntidad) {
        final String partialPropertyName = "anexos.permitirfirmadetached";
        return getBooleanByEntidad(idEntidad, partialPropertyName);

    }

    /**
     * Devuelve el valor de la propiedad que indica si se han de cerrar los expedientes de DM
     * Propiedad: es.caib.regweb3.arxiu.cerrarExpedientes
     * @param idEntidad
     * @return
     */
    public static Boolean getCerrarExpedientes(Long idEntidad) {
        final String partialPropertyName = "arxiu.cerrarExpedientes";
        return getBooleanByEntidad(idEntidad, partialPropertyName);

    }

    /**
     * Devuelve el valor de la propiedad con la fechaInicio para cerar los expedientes
     * Propiedad: es.caib.regweb3.arxiu.fechaInicio.cerrarExpedientes
     * @param idEntidad
     * @return
     */
    public static String getFechaInicioCerrarExpedientes(Long idEntidad) {
        final String partialPropertyName = "arxiu.fechaInicio.cerrarExpedientes";
        return getStringByEntidad(idEntidad, partialPropertyName);
    }


    /**
     * Retorna el valor de la propiedad del Username de la Url de Validación
     * Propiedad: es.caib.regweb3.urlvalidation.username
     * @param idEntidad
     * @return
     */
    public static String getUrlValidationUsername(Long idEntidad) {
        final String partialPropertyName = "urlvalidation.username";

        return getStringByEntidad(idEntidad, partialPropertyName);
    }

    /**
     * Retorna el valor de la propiedad del Username de la Url de Validación
     * Propiedad: es.caib.regweb3.urlvalidation.password
     * @param idEntidad
     * @return
     */
    public static String getUrlValidationPassword(Long idEntidad) {
        final String partialPropertyName = "urlvalidation.password";

        return getStringByEntidad(idEntidad, partialPropertyName);
    }

    /**
     * Propiedad: es.caib.regweb3.anexos.purgo.meses
     * @param idEntidad
     * @return
     */
    public static Integer getMesesPurgoAnexos(Long idEntidad) {
        final String partialPropertyName = "anexos.purgo.meses";
        Integer valor = getIntegerByEntidad(idEntidad,partialPropertyName);

        // Valor global si no existeix el de per entitat
        if (valor == null) {
            valor = 3;
        }

        return valor;

    }


    /**
     * Propiedad: es.caib.regweb3.anexos.purgo.numelementos
     * @param idEntidad
     * @return
     */
    public static Integer getNumElementosPurgoAnexos(Long idEntidad) {
        final String partialPropertyName = "anexos.purgo.numelementos";
        Integer valor =  getIntegerByEntidad(idEntidad,partialPropertyName);

        // Valor global si no existeix el de per entitat
        if (valor == null) {
            valor = 100;
        }

        return valor;

    }

    /**
     * Propiedad: es.caib.regweb3.procesados.purgo.meses
     * @param idEntidad
     * @return
     */
    public static Integer getMesesPurgoProcesadosCola(Long idEntidad) {
        final String partialPropertyName = "cola.procesados.purgo.meses";
        Integer valor =  getIntegerByEntidad(idEntidad,partialPropertyName);

        if (valor == null) {
            valor = 2;
        }
        return valor;
    }

    /**
     * Obtenemos el número de elementos de cada iteración en la Cola
     * Propiedad: es.caib.regweb3.cola.elementos
     * @param idEntidad
     * @return
     */
    public static Integer getElementosCola(Long idEntidad) {
        final String partialPropertyName = "cola.elementos";
        Integer valor = getIntegerByEntidad(idEntidad,partialPropertyName);

        if (valor == null) {
            valor = 15;
        }
        return valor;
    }

    /**
     * Devuelve el valor de la propiedad que indica si la Custodia de ls Justificantes será en diferido
     * Propiedad: es.caib.regweb3.comunicaciones.generar
     * @param idEntidad
     * @return
     */
    public static Boolean getCustodiaDiferida(Long idEntidad) {
        final String partialPropertyName = "custodia.diferida.justificantes";
        return getBooleanByEntidad(idEntidad, partialPropertyName);

    }

    /**
     * Obtenemos el número de elementos de cada iteración en la Cola de Custodia
     * Propiedad: es.caib.regweb3.cola.custodia.elementos
     * @param idEntidad
     * @return
     */
    public static Integer getElementosColaCustodia(Long idEntidad) {
        final String partialPropertyName = "cola.custodia.elementos";
        Integer valor = getIntegerByEntidad(idEntidad,partialPropertyName);

        if (valor == null) {
            valor = 15;
        }
        return valor;
    }

    /**
     * Obtenemos el número máximo de reintentos de la cola
     * Propiedad: es.caib.regweb3.cola.maxReintentos
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
     * Propiedad: es.caib.regweb3.comunicaciones.generar
     * @param idEntidad
     * @return
     */
    public static Boolean getGenerarComunicaciones(Long idEntidad) {
        final String partialPropertyName = "comunicaciones.generar";
        return getBooleanByEntidad(idEntidad, partialPropertyName);

    }

    /**
     * Propiedad que indica si se validan los anexos con firma antes de distribuir.
     * Propiedad: es.caib.regweb3.nodistribuir
     * @param idEntidad
     * @return
     */
    public static boolean getNoDistribuir(Long idEntidad){
        final String partialPropertyName = "nodistribuir";
        return getBooleanByEntidad(idEntidad, partialPropertyName);
    }

    /**
     * Propiedad que para la cola de Distribución
     * Propiedad: es.caib.regweb3.parar.cola.distribucion
     * @param idEntidad
     * @return
     */
    public static boolean pararColaDistribucion(Long idEntidad){
        final String partialPropertyName = "cola.parar.distribucion";
        return getBooleanByEntidad(idEntidad, partialPropertyName);
    }

    /**
     * Propiedad que para la cola de Cutodia
     * Propiedad: es.caib.regweb3.cola.parar.custodia
     * @param idEntidad
     * @return
     */
    public static boolean pararColaCustodia(Long idEntidad){
        final String partialPropertyName = "cola.parar.custodia";
        return getBooleanByEntidad(idEntidad, partialPropertyName);
    }

    /**
     * Propiedad que configura un segundo hilo de procesamiento de la cola de Custodia
     * Propiedad: es.caib.regweb3.cola.2hilo.custodia
     * @param idEntidad
     * @return
     */
    public static boolean segundoHiloCustodia(Long idEntidad){
        final String partialPropertyName = "cola.2hilo.custodia";
        return getBooleanByEntidad(idEntidad, partialPropertyName);
    }

    /**
     * Propiedad que activa/desactiva la Distribución automática
     * Propiedad: es.caib.regweb3.parar.cola.distribucion
     * @param idEntidad
     * @return
     */
    public static boolean distribucionAutomatica(Long idEntidad){
        final String partialPropertyName = "cola.distribucionAutomatica";
        return getBooleanByEntidad(idEntidad, partialPropertyName);
    }

    /**
     * Propiedad que retorna los días de antigüedad de los registros para ser distribuidos automáticamente
     * Propiedad: es.caib.regweb3.cola.distribucionAutomatic.dias
     * @param idEntidad
     * @return
     */
    public static Integer getDiasDistribucionAutomatica(Long idEntidad) {
        final String partialPropertyName = "cola.distribucionAutomatica.dias";
        Integer valor =  getIntegerByEntidad(idEntidad,partialPropertyName);

        if (valor == null) {
            valor = 2;
        }
        return valor;
    }

    /**
     * Propiedad que permite mostrar un enlace en le menú a la instalación de DIR3CAIB
     * Propiedad: es.caib.regweb3.enlaceDir3
     * @param idEntidad
     * @return
     */
    public static boolean getEnlaceDir3(Long idEntidad){
        final String partialPropertyName = "enlaceDir3";
        return getBooleanByEntidad(idEntidad, partialPropertyName);
    }

    /**
     * Retorna el valor de la url de ayuda para acceder al microsite de Oficinas de Assitencia
     * Propiedad: es.caib.regweb3.ayuda.url
     * @param idEntidad
     * @return
     */
    public static String getAyudaUrl(Long idEntidad) {
        final String partialPropertyName = "ayuda.url";
        String valor = getStringByEntidad(idEntidad, partialPropertyName);

        // Valor global si no existeix el de per entitat
        if (valor == null) {
            valor = getString(partialPropertyName);
        }
        return valor;
    }

    /**
     * Retorna la base del plugin de SignatureServer de la entidad
     * Propiedad: es.caib.regweb3.signatureserver.plugin.base
     * @param idEntidad
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
                propiedadGlobalEjb = (PropiedadGlobalLocal) new InitialContext().lookup(PropiedadGlobalLocal.JNDI_NAME);
            } catch (Throwable e) {
                log.error("No se ha podido instanciar PropiedadGlobalEJB");
                throw new Exception(e);

            }
        }
        return propiedadGlobalEjb;
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
