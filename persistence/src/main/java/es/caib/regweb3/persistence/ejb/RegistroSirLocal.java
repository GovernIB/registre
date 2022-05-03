package es.caib.regweb3.persistence.ejb;

import es.caib.regweb3.model.*;
import es.caib.regweb3.model.utils.CamposNTI;
import es.caib.regweb3.model.utils.EstadoRegistroSir;
import es.caib.regweb3.persistence.utils.Paginacion;
import es.caib.regweb3.sir.core.utils.FicheroIntercambio;
import org.fundaciobit.genapp.common.i18n.I18NException;
import org.fundaciobit.genapp.common.i18n.I18NValidationException;

import javax.annotation.security.RolesAllowed;
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
@RolesAllowed({"RWE_SUPERADMIN","RWE_ADMIN","RWE_USUARI", "RWE_WS_ENTRADA", "RWE_WS_SALIDA"})
public interface RegistroSirLocal extends BaseEjb<RegistroSir, Long> {

     List<Long> getUltimosPendientesProcesarERTE(EstadoRegistroSir estado, String oficinaSir, Date fechaInicio, Date fechaFin, String aplicacion, Integer total) throws Exception;


    /**
     * Obtiene un RegistroSir a partir de los parámetros
     * @param identificadorIntercambio
     * @param codigoEntidadRegistralDestino
     * @return
     * @throws Exception
     */
    RegistroSir getRegistroSir(String identificadorIntercambio, String codigoEntidadRegistralDestino) throws Exception;

    /**
     * Obtiene un RegistroSir incluyendo los anexos almancenados en disco
     * @param idRegistroSir
     * @return
     * @throws Exception
     */
    RegistroSir getRegistroSirConAnexos(Long idRegistroSir) throws Exception;

    /**
     * Crea un RegistroSir
     * @throws Exception
     */
    RegistroSir crearRegistroSir(FicheroIntercambio ficheroIntercambio, Entidad entidad) throws Exception;

    /**
     * Eliminar un RegistroSir y sus trazabilidades
     * @param registroSir
     * @throws Exception
     */
    void eliminarRegistroSir(RegistroSir registroSir) throws Exception;

    /**
     * Marca como eliminado un RegitroSir, creando una TRAZABILIDAD_SIR_ELIMINADO
     * @param registroSir
     * @param usuario
     * @throws Exception
     */
    void marcarEliminado(RegistroSir registroSir, UsuarioEntidad usuario, String observaciones) throws Exception;

    /**
     * Busca los RegistroSir en función de los parámetros, donde sólo mostrará los RegistroSir con codEntidadRegistralDestino = codOficinaActiva
     * @param pageNumber
     * @param fechaInicio
     * @param fechaFin
     * @param registroSir
     * @param oficinaSir
     * @param estado
     * @return
     * @throws Exception
     */
    Paginacion busqueda(Integer pageNumber, Date fechaInicio, Date fechaFin, RegistroSir registroSir, String oficinaSir, String estado,String entidad) throws Exception;

    /**
     *
     * @param pageNumber
     * @param oficinaSir
     * @param estado
     * @return
     * @throws Exception
     */
    Paginacion getRegistrosEstado(Integer pageNumber, String oficinaSir, String estado) throws Exception;

    /**
     * Obtiene los últimos ASR pendientes de procesar destinados a una OficinaSir
     * @param oficinaSir
     * @param total
     * @return
     * @throws Exception
     */
    List<RegistroSir> getUltimosPendientesProcesar(String oficinaSir, Integer total) throws Exception;

    /**
     * Obtiene el total de RegistrosSir pendientes de procesar según la oficina
     * @param oficinaSir
     * @return
     * @throws Exception
     */
    Long getPendientesProcesarCount(String oficinaSir) throws Exception;

    /**
     * Modifica el Estado de un {@link RegistroSir}
     * @param idRegistroSir
     * @param estado
     * @throws Exception
     */
    void modificarEstado(Long idRegistroSir, EstadoRegistroSir estado) throws Exception;

    /**
     * Modifica el Estado de un {@link RegistroSir}, utilizando una transacción nueva
     * @param idRegistroSir
     * @param estado
     * @throws Exception
     */
    void modificarEstadoNuevaTransaccion(Long idRegistroSir, EstadoRegistroSir estado) throws Exception;

    /**
     * Modifica el Estado de un {@link RegistroSir}, a uno con Error, incluyendo código y descripción del Error.
     * @param idRegistroSir
     * @param estado
     * @param codigoError
     * @param descripcionError
     * @throws Exception
     */
    void modificarEstadoError(Long idRegistroSir, EstadoRegistroSir estado, String codigoError, String descripcionError) throws Exception;

        /**
         * Incrementa los reintentos
         * @param idRegistroSir
         * @param reintentos
         * @throws Exception
         */
    void incrementarReintentos(Long idRegistroSir, Integer reintentos) throws Exception;

    /**
     *
     * @param ficheroIntercambio
     * @return
     * @throws Exception
     */
    RegistroSir transformarFicheroIntercambio(FicheroIntercambio ficheroIntercambio, Long idEntidad)throws Exception;


    /**
     *
     * @param registroEntrada
     * @return
     * @throws Exception
     * @throws I18NException
     */
    RegistroSir transformarRegistroEntrada(RegistroEntrada registroEntrada) throws Exception, I18NException;

    /**
     *
     * @param registroSalida
     * @return
     * @throws Exception
     * @throws I18NException
     */
    RegistroSir transformarRegistroSalida(RegistroSalida registroSalida) throws Exception, I18NException;

    /**
     * Obtiene los RegistroSir que han de reintentar su envío al componente CIR
     * @param idEntidad
     * @return
     * @throws Exception
     */
    List<Long> getEnviadosSinAck(Long idEntidad) throws Exception;

    /**
     *
     * @param idEntidad
     * @return
     * @throws Exception
     */
    List<Long> getEnviadosConError(Long idEntidad) throws Exception;

    /**
     * Reinicia el contador de reintentos
     * @param idRegistroSir
     * @throws Exception
     */
    public void reiniciarIntentos(Long idRegistroSir) throws Exception;

    /**
     * Elimina los RegistroSir de una Entidad
     * @param idEntidad
     * @return
     * @throws Exception
     */
    Integer eliminarByEntidad(Long idEntidad) throws Exception;

    /**
     * @param registroSir
     * @param usuario
     * @param oficinaActiva
     * @param idLibro
     * @param idIdioma
     * @param camposNTIs
     * @return
     * @throws Exception
     * @throws I18NException
     * @throws I18NValidationException
     */
    RegistroEntrada aceptarRegistroSirEntrada(RegistroSir registroSir, Entidad entidad, UsuarioEntidad usuario, Oficina oficinaActiva, Long idLibro, Long idIdioma, List<CamposNTI> camposNTIs, Long idOrganismoDestino) throws Exception, I18NException, I18NValidationException;
}
