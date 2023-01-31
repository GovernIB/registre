package es.caib.regweb3.persistence.ejb;

import es.caib.dir3caib.ws.api.oficina.Dir3CaibObtenerOficinasWs;
import es.caib.dir3caib.ws.api.unidad.Dir3CaibObtenerUnidadesWs;
import es.caib.regweb3.model.Oficina;
import es.caib.regweb3.model.RegistroSir;
import es.caib.regweb3.model.sir.MensajeControl;
import es.caib.regweb3.persistence.utils.RespuestaRecepcionSir;
import es.caib.regweb3.sir.core.utils.FicheroIntercambio;
import es.gob.ad.registros.sir.interService.exception.InterException;
import org.fundaciobit.genapp.common.i18n.I18NException;

import javax.ejb.Local;
import java.util.Date;

/**
 * Created by Fundacio Bit
 *
 * @author earrivi
 */
@Local
public interface WebServicesMethodsLocal {

    String JNDI_NAME = "java:app/regweb3-persistence/WebServicesMethodsEJB";

    /**
     *
     * @param registro
     * @param firmaRegistro
     */
    void recibirAsiento(String registro, String firmaRegistro) throws InterException;

    /**
     *
     * @param mensaje
     * @param firma
     * @throws InterException
     */
    void recibirMensajeControl(String mensaje, String firma) throws InterException;

    /**
     * Recibe un mensaje de control en formato SICRES3 desde un nodo distribuido
     *
     * @param mensaje
     * @throws I18NException
     */
    void procesarMensajeDatosControl(MensajeControl mensaje) throws I18NException;

    /**
     * Recibe un fichero de intercambio en formato SICRES3 desde un nodo distribuido
     *
     * @param ficheroIntercambio
     * @throws I18NException
     */
    RespuestaRecepcionSir procesarFicheroIntercambio(FicheroIntercambio ficheroIntercambio) throws I18NException;

    /**
     * Elimina un RegistroSir creado
     *
     * @param registroSir
     * @throws I18NException
     */
    void eliminarRegistroSir(RegistroSir registroSir) throws I18NException;

    /**
     * Guarda un nuevo Mensaje de Control
     *
     * @param mensajeControl
     * @throws I18NException
     */
    void guardarMensajeControl(MensajeControl mensajeControl) throws I18NException;

    /**
     * @return
     * @throws I18NException
     */
    Dir3CaibObtenerOficinasWs getObtenerOficinasService(Long idEntidad) throws I18NException;

    /**
     * @return
     * @throws I18NException
     */
    Dir3CaibObtenerUnidadesWs getObtenerUnidadesService(Long idEntidad) throws I18NException;

    /**
     * @param codigo
     * @return
     * @throws I18NException
     */
    Oficina obtenerOficina(String codigo) throws I18NException;

    /**
     * @param tipo
     * @param descripcion
     * @param peticion
     * @param th
     * @param tiempo
     * @param idEntidad
     * @param numregformat
     * @throws I18NException
     */
    void addIntegracionError(Long tipo, String descripcion, String peticion, Throwable th, String error, Long tiempo, Long idEntidad, String numregformat) throws I18NException;

    /**
     * @param inicio
     * @param tipo
     * @param descripcion
     * @param peticion
     * @param tiempo
     * @param idEntidad
     * @param numregformat
     * @throws I18NException
     */
    void addIntegracionOk(Date inicio, Long tipo, String descripcion, String peticion, Long tiempo, Long idEntidad, String numregformat) throws I18NException;
}
