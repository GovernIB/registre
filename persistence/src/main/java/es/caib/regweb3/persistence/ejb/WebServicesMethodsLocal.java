package es.caib.regweb3.persistence.ejb;

import es.caib.dir3caib.ws.api.oficina.Dir3CaibObtenerOficinasWs;
import es.caib.dir3caib.ws.api.unidad.Dir3CaibObtenerUnidadesWs;
import es.caib.regweb3.model.Oficina;
import es.caib.regweb3.model.RegistroSir;
import es.caib.regweb3.model.sir.MensajeControl;
import es.caib.regweb3.persistence.utils.RespuestaRecepcionSir;
import es.caib.regweb3.sir.core.utils.FicheroIntercambio;

import javax.ejb.Local;

/**
 * Created by Fundacio Bit
 *
 * @author earrivi
 */
@Local
public interface WebServicesMethodsLocal {

    /**
     * Recibe un mensaje de control en formato SICRES3 desde un nodo distribuido
     * @param mensaje
     * @throws Exception
     */
    void procesarMensajeDatosControl(MensajeControl mensaje) throws Exception;

    /**
     * Recibe un fichero de intercambio en formato SICRES3 desde un nodo distribuido
     * @param ficheroIntercambio
     * @throws Exception
     */
    RespuestaRecepcionSir procesarFicheroIntercambio(FicheroIntercambio ficheroIntercambio) throws Exception;

    /**
     * Elimina un RegistroSir creado
     * @param registroSir
     * @throws Exception
     */
    void eliminarRegistroSir(RegistroSir registroSir) throws Exception;

    /**
     * Guarda un nuevo Mensaje de Control
     * @param mensajeControl
     * @throws Exception
     */
    void guardarMensajeControl(MensajeControl mensajeControl) throws Exception;

    /**
     *
     * @return
     * @throws Exception
     */
    Dir3CaibObtenerOficinasWs getObtenerOficinasService() throws Exception;

    /**
     *
     * @return
     * @throws Exception
     */
    Dir3CaibObtenerUnidadesWs getObtenerUnidadesService() throws Exception;

    /**
     *
     * @return
     * @throws Exception
     */
    String getFormatosAnexosSir() throws Exception;

    /**
     *
     * @param codigo
     * @return
     * @throws Exception
     */
    Oficina obtenerOficina(String codigo) throws Exception;

    /**
     *
     * @param tipo
     * @param descripcion
     * @param peticion
     * @param th
     * @param tiempo
     * @param idEntidad
     * @param numregformat
     * @throws Exception
     */
    void addIntegracionError(Long tipo, String descripcion, String peticion, Throwable th, String error, Long tiempo, Long idEntidad, String numregformat) throws Exception;
}
