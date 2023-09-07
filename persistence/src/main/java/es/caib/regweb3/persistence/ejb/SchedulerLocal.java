package es.caib.regweb3.persistence.ejb;

import org.fundaciobit.genapp.common.i18n.I18NException;

import javax.ejb.Local;

/**
 * Created by Fundació BIT.
 *
 * @author earrivi
 * Date: 16/01/14
 */
@Local
public interface SchedulerLocal {

    String JNDI_NAME = "java:app/regweb3-persistence/SchedulerEJB";


    /**
     *
     * @throws I18NException
     */
    void purgarIntegraciones() throws I18NException;

    /**
     *
     * @throws I18NException
     */
    void purgarAnexosSir() throws I18NException;

    /**
     *
     * @throws I18NException
     */
    void reintentarIntercambiosSinAck() throws I18NException;

    /**
     *
     * @throws I18NException
     */
    void reintentarReenviosRechazosSinAck() throws I18NException;

    /**
     *
     * @throws I18NException
     */
    void reintentarIntercambiosConError() throws I18NException;

    /**
     *
     * @throws I18NException
     */
    void reintentarReenviosRechazosConError() throws I18NException;

    /**
     *
     * @throws I18NException
     */
    void reiniciarContadoresEntidad() throws I18NException;

    /**
     * Inicia la distribución de los registros en cola de cada entidad.
     *
     * @throws I18NException
     */
    void distribuirRegistrosEnCola() throws I18NException;

    /**
     * Inicia la custodia de Justificantes en Cola de cada entidad.
     * @throws I18NException
     */
    void custodiarJustificantesEnCola() throws I18NException;

    /**
     * Segundo hilo de custodia de justificantes
     * @throws I18NException
     */
    void custodiarJustificantesEnCola2() throws I18NException;

    /**
     *
     * @throws I18NException
     */
    void cerrarExpedientes() throws I18NException;

    /**
     * Inicia la purga de los anexos de los registros distribuidos para cada una de las entidades.
     * @throws I18NException
     */
    void purgarAnexosDistribuidos() throws I18NException;

    /**
     * Método que purga los anexos de los registros que se han enviado via SIR y han sido confirmados
     * en destino.
     * @throws I18NException
     */
    void purgarAnexosRegistrosConfirmados() throws I18NException;

    /**
     * Genera Comunicaciones automáticas
     * @throws I18NException
     */
    void generarComunicaciones() throws I18NException;

    /**
     * Purga las sesiones ws
     */
    void purgarSesionesWs() throws I18NException;

    /**
     * Elimina los registros que están en estado procesado de las diferentes COLAS que hace más de X meses que se procesaron
     *
     * @throws I18NException
     */
    void purgarProcesadosColas() throws I18NException;

    void consultarAsientosPendientesSIR() throws I18NException;

    void reencolarAsientos() throws I18NException;
}

