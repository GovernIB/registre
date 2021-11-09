package es.caib.regweb3.persistence.ejb;

import javax.ejb.Local;

import org.fundaciobit.genapp.common.i18n.I18NException;

/**
 * Created by Fundació BIT.
 *
 * @author earrivi
 * Date: 16/01/14
 */
@Local
/*@RolesAllowed({"RWE_SUPERADMIN","RWE_ADMIN","RWE_USUARI"})*/
public interface SchedulerLocal {

    /**
     *
     * @throws Exception
     */
    void purgarIntegraciones() throws Exception;

    /**
     *
     * @throws Exception
     */
    void purgarAnexosSir() throws Exception;

    /**
     *
     * @throws Exception
     */
    void reintentarIntercambiosSinConfirmacion() throws Exception;

    /**
     *
     * @throws Exception
     */
    void reintentarIntercambiosConError() throws Exception;

    /**
     *
     * @throws Exception
     */
    void reiniciarContadoresEntidad() throws Exception;

    /**
     * Inicia la distribución de los registros en cola de cada entidad.
     *
     * @throws Exception
     */
    void distribuirRegistrosEnCola() throws Exception;

    /**
     * Inicia la custodia de Justificantes en Cola de cada entidad.
     * @throws Exception
     */
    void custodiarJustificantesEnCola() throws Exception;

    /**
     *
     * @throws Exception
     */
    void cerrarExpedientes() throws Exception;

    /**
     * Inicia la purga de los anexos de los registros distribuidos para cada una de las entidades.
     * @throws Exception
     */
    void purgarAnexosDistribuidos() throws Exception;

    /**
     * Método que purga los anexos de los registros que se han enviado via SIR y han sido confirmados
     * en destino.
     * @throws Exception
     */
    void purgarAnexosRegistrosConfirmados() throws Exception;

    /**
     * Genera Comunicaciones automáticas
     * @throws Exception
     */
    void generarComunicaciones() throws Exception;

    /**
     * Purga las sesiones ws
     */
    void purgarSesionesWs() throws Exception;

    /**
     * Envia email a los administradores y propietario de las entidades para notificar
     * que existe un error en la distribución
     * @throws Exception
     */
    void enviarEmailErrorDistribucion() throws Exception;

    /**
     * Elimina los registros que están en estado procesado de la cola de distribución que hace más de X meses que se procesaron
     *
     * @throws Exception
     */
    void purgarProcesadosColaDistribucion() throws Exception;

    /**
     * Actualiza el estado de los envíos SIR con estado no final
     *
     * @throws Exception
     * @throws I18NException 
     */
	void actualizarEnviosSIR() throws Exception, I18NException;
}

