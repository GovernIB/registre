package es.caib.regweb3.persistence.ejb;

import es.caib.regweb3.model.*;
import es.caib.regweb3.model.utils.CamposNTI;
import es.caib.regweb3.model.utils.EstadoRegistroSir;
import es.caib.regweb3.persistence.utils.Paginacion;
import es.caib.regweb3.sir.core.utils.FicheroIntercambio;
import org.fundaciobit.genapp.common.i18n.I18NException;
import org.fundaciobit.genapp.common.i18n.I18NValidationException;

import javax.ejb.Local;
import java.util.Date;
import java.util.List;

/**
 * Created by Fundació BIT.
 *
 * @author earrivi
 * Date: 16/06/16
 */
@Local
public interface RegistroSirLocal extends BaseEjb<RegistroSir, Long> {

    String JNDI_NAME = "java:app/regweb3-persistence/RegistroSirEJB";


    /**
     * @param estado
     * @param oficinaSir
     * @param fechaInicio
     * @param fechaFin
     * @param aplicacion
     * @param total
     * @return
     * @throws I18NException
     */
    List<Long> getUltimosPendientesProcesarERTE(EstadoRegistroSir estado, String oficinaSir, Date fechaInicio, Date fechaFin, String aplicacion, Integer total) throws I18NException;


    /**
     * Obtiene un RegistroSir a partir de los parámetros
     *
     * @param identificadorIntercambio
     * @param codigoEntidadRegistralDestino
     * @return
     * @throws I18NException
     */
    RegistroSir getRegistroSir(String identificadorIntercambio, String codigoEntidadRegistralDestino) throws I18NException;

    /**
     * Obtiene el Estado de un RegistroSir
     * @param idRegistroSir
     * @return
     * @throws I18NException
     */
    EstadoRegistroSir getEstado(Long idRegistroSir) throws I18NException;

    /**
     * Obtiene un RegistroSir incluyendo los anexos almancenados en disco
     *
     * @param idRegistroSir
     * @return
     * @throws I18NException
     */
    RegistroSir getRegistroSirConAnexos(Long idRegistroSir) throws I18NException;

    /**
     * Obtiene los AnexosSir completos de un RegistroSir
     * @param registroSir
     * @return
     * @throws I18NException
     */
    List<AnexoSir> getAnexos(RegistroSir registroSir) throws I18NException;


    /**
     * Crea un RegistroSir
     *
     * @throws I18NException
     */
    RegistroSir crearRegistroSir(FicheroIntercambio ficheroIntercambio, Entidad entidad) throws I18NException;

    /**
     * Eliminar un RegistroSir y sus trazabilidades
     *
     * @param registroSir
     * @throws I18NException
     */
    void eliminarRegistroSir(RegistroSir registroSir) throws I18NException;

    /**
     * Marca como eliminado un RegitroSir, creando una TRAZABILIDAD_SIR_ELIMINADO
     *
     * @param registroSir
     * @param usuario
     * @throws I18NException
     */
    void marcarEliminado(RegistroSir registroSir, UsuarioEntidad usuario, String observaciones) throws I18NException;

    /**
     * Busca los RegistroSir en función de los parámetros, donde sólo mostrará los RegistroSir con codEntidadRegistralDestino = codOficinaActiva
     *
     * @param pageNumber
     * @param fechaInicio
     * @param fechaFin
     * @param registroSir
     * @param oficinaSir
     * @param estado
     * @return
     * @throws I18NException
     */
    Paginacion busqueda(Integer pageNumber, Date fechaInicio, Date fechaFin, RegistroSir registroSir, String oficinaSir, String estado, String entidad) throws I18NException;

    /**
     * @param pageNumber
     * @param oficinaSir
     * @param estado
     * @return
     * @throws I18NException
     */
    Paginacion getRegistrosEstado(Integer pageNumber, String oficinaSir, String estado) throws I18NException;

    /**
     * Obtiene los últimos ASR pendientes de procesar destinados a una OficinaSir
     *
     * @param oficinaSir
     * @param total
     * @return
     * @throws I18NException
     */
    List<RegistroSir> getUltimosPendientesProcesar(String oficinaSir, Integer total) throws I18NException;

    /**
     * Obtiene el total de RegistrosSir pendientes de procesar según la oficina
     *
     * @param oficinaSir
     * @return
     * @throws I18NException
     */
    Long getPendientesProcesarCount(String oficinaSir) throws I18NException;

    /**
     * Modifica el Estado de un {@link RegistroSir}
     *
     * @param idRegistroSir
     * @param estado
     * @throws I18NException
     */
    void modificarEstado(Long idRegistroSir, EstadoRegistroSir estado) throws I18NException;

    /**
     * Modifica el Estado de un {@link RegistroSir}, utilizando una transacción nueva
     * @param idRegistroSir
     * @param estado
     * @throws I18NException
     */
    void modificarEstadoNuevaTransaccion(Long idRegistroSir, EstadoRegistroSir estado) throws I18NException;

    /**
     * Modifica el Estado de un {@link RegistroSir}, a uno con Error, incluyendo código y descripción del Error.
     * @param idRegistroSir
     * @param estado
     * @param codigoError
     * @param descripcionError
     * @throws I18NException
     */
    void modificarEstadoError(Long idRegistroSir, EstadoRegistroSir estado, String codigoError, String descripcionError) throws I18NException;

    /**
     * Incrementa los reintentos
     * @param idRegistroSir
     * @param reintentos
     * @throws I18NException
     */
    void incrementarReintentos(Long idRegistroSir, Integer reintentos) throws I18NException;

    /**
     * @param ficheroIntercambio
     * @return
     * @throws I18NException
     */
    RegistroSir transformarFicheroIntercambio(FicheroIntercambio ficheroIntercambio, Entidad entidad) throws I18NException;


    /**
     * @param registroEntrada
     * @return
     * @throws I18NException
     * @throws I18NException
     */
    RegistroSir transformarRegistroEntrada(RegistroEntrada registroEntrada) throws I18NException;

    /**
     * @param registroSalida
     * @return
     * @throws I18NException
     * @throws I18NException
     */
    RegistroSir transformarRegistroSalida(RegistroSalida registroSalida) throws I18NException;

    /**
     * Obtiene los RegistroSir que han de reintentar su envío al componente CIR
     *
     * @param idEntidad
     * @return
     * @throws I18NException
     */
    List<Long> getEnviadosSinAck(Long idEntidad) throws I18NException;

    /**
     * @param idEntidad
     * @return
     * @throws I18NException
     */
    List<Long> getEnviadosConError(Long idEntidad) throws I18NException;

    /**
     * Reinicia el contador de reintentos
     *
     * @param idRegistroSir
     * @throws I18NException
     */
    public void reiniciarIntentos(Long idRegistroSir) throws I18NException;

    /**
     * Elimina los RegistroSir de una Entidad
     *
     * @param idEntidad
     * @return
     * @throws I18NException
     */
    Integer eliminarByEntidad(Long idEntidad) throws I18NException;

    /**
     * @param registroSir
     * @param usuario
     * @param oficinaActiva
     * @param idLibro
     * @param idIdioma
     * @param camposNTIs
     * @return
     * @throws I18NException
     * @throws I18NException
     * @throws I18NValidationException
     */
    RegistroEntrada aceptarRegistroSirEntrada(RegistroSir registroSir, Entidad entidad, UsuarioEntidad usuario, Oficina oficinaActiva, Long idLibro, Long idIdioma, List<CamposNTI> camposNTIs, Long idOrganismoDestino, Long codigoSia) throws I18NException, I18NValidationException;
}
