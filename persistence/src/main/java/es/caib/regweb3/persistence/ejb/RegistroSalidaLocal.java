package es.caib.regweb3.persistence.ejb;

import es.caib.regweb3.model.Interesado;
import es.caib.regweb3.model.Libro;
import es.caib.regweb3.model.RegistroSalida;
import es.caib.regweb3.model.UsuarioEntidad;
import es.caib.regweb3.model.utils.AnexoFull;
import es.caib.regweb3.model.utils.RegistroBasico;
import es.caib.regweb3.persistence.utils.Paginacion;
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
 * Date: 16/01/14
 */
@Local
@RolesAllowed({"RWE_SUPERADMIN","RWE_ADMIN","RWE_USUARI","RWE_WS_ENTRADA","RWE_WS_SALIDA", "RWE_WS_CIUDADANO"})
public interface RegistroSalidaLocal extends RegistroSalidaCambiarEstadoLocal {

    /**
     * Obtiene un RegistroSalida con un mínimo de campos
     * @param idRegistroSalida
     * @return
     * @throws Exception
     */
    RegistroBasico findByIdLigero(Long idRegistroSalida) throws Exception;

    /**
     * Guarda un Registro de Salida (con anexos)
     * @param registroSalida
     * @return
     * @throws Exception
     */
    RegistroSalida registrarSalida(RegistroSalida registroSalida,
                                   UsuarioEntidad usuarioEntidad, List<Interesado> interesados, List<AnexoFull> anexos)
            throws Exception, I18NException, I18NValidationException;
    

    /**
     * Busca los Registros de Salida en función de los parametros
     * @param pageNumber
     * @param registroSalida
     * @return
     * @throws Exception
     */
    Paginacion busqueda(Integer pageNumber, Date fechaInicio, Date fechaFin, RegistroSalida registroSalida, String interesadoNom, String interesadoDoc, String interesadoLli1, String interesadoLli2, String organoOrigen, Boolean anexos, String observaciones, String usuario, Long idEntidad) throws Exception;

    
    /**
     * Busca los Registros de Salida en función de los parametros
     * @param numero
     * @param anyo
     * @param libro
     * @return
     * @throws Exception
     */
    RegistroSalida findByNumeroAnyoLibro(int numero, int anyo, String libro) throws Exception;

    /**
     * Obtiene el RegistroSalida a partir de su numero de registro formateado, la entidad y el libro.
     * @param numeroRegistroFormateado
     * @return
     * @throws Exception
     */
    public RegistroSalida findByNumeroRegistroFormateado(String codigoEntidad, String numeroRegistroFormateado) throws Exception;


    /**
     * Obtiene el RegistroSalida a partir de su numero de registro formateado, la entidad y el libro y carga los anexos
     * @param codigoEntidad
     * @param numeroRegistroFormateado
     * @return
     * @throws Exception
     * @throws I18NException
     */
    public RegistroSalida findByNumeroRegistroFormateadoConAnexos(String codigoEntidad, String numeroRegistroFormateado) throws Exception, I18NException;


    /**
     * Devuelve el numero de registro formateado a partir de un registro detalle.
     * Se necesita para el plug-in postproceso para pasarselo cuando creamos interesados.
     * @param idRegistroDetalle
     * @return
     * @throws Exception
     */
    String findNumeroRegistroFormateadoByRegistroDetalle(Long idRegistroDetalle) throws Exception;

    /**
     * Anula un RegistroSalida, cambiandole el estado a anulado.
     * @param registroSalida
     * @param usuarioEntidad
     * @param observacionesAnulacion
     * @throws Exception
     */
    void anularRegistroSalida(RegistroSalida registroSalida, UsuarioEntidad usuarioEntidad, String observacionesAnulacion) throws Exception;

    /**
     * Activa un RegistroSalida, cambiandole el estado a anulado.
     * @param registroSalida
     * @param usuarioEntidad
     * @throws Exception
     */
    void activarRegistroSalida(RegistroSalida registroSalida, UsuarioEntidad usuarioEntidad) throws Exception;

    /**
     * Visa un RegistroSalida, cambiandole el estado a anulado.
     * @param registroSalida
     * @param usuarioEntidad
     * @throws Exception
     */
    void visarRegistroSalida(RegistroSalida registroSalida, UsuarioEntidad usuarioEntidad) throws Exception;

    /**
     * Elimina los RegistroSalida de una Entidad
     * @param idEntidad
     * @return
     * @throws Exception
     */
    Integer eliminarByEntidad(Long idEntidad) throws Exception;

    /**
     * Retorna el identificador del Libro al que pertenece el RegistroSalida
     * @param idRegistroSalida
     * @return
     * @throws Exception
     */
    Long getLibro(Long idRegistroSalida) throws Exception;

    /**
     * Busca los Registros de Salida de un listado de Libros en función de su estado.
     * @param libros
     * @param idEstado
     * @return
     * @throws Exception
     */
    Long getByLibrosEstadoCount(List<Libro> libros, Long idEstado) throws Exception;

    /**
     * Busca los Registros de Salida de un listado de Libros en función de su estado.
     * @param inicio
     * @param libros
     * @param idEstado
     * @return
     * @throws Exception
     */
    List<RegistroSalida> getByLibrosEstado(int inicio, List<Libro> libros, Long idEstado) throws Exception;

    /**
     * Busca los Registros de Salida de un Libro.
     * @param idLibro
     * @return
     * @throws Exception
     */
    Long getTotalByLibro(Long idLibro) throws Exception;

    /**
     * Comprueba si un usuario tiene RegistroSalida
     *
     * @param idUsuarioEntidad
     * @return
     * @throws Exception
     */
    Boolean obtenerPorUsuario(Long idUsuarioEntidad) throws Exception;

    /**
     * Cambia el estado de un RegistroSalida
     * @param registroSalida
     * @param idEstado
     * @param usuarioEntidad
     * @throws Exception
     */
    void cambiarEstadoHistorico(RegistroSalida registroSalida, Long idEstado, UsuarioEntidad usuarioEntidad) throws Exception;

    /**
     * Cambia el estado de un RegistroEntrada y el HistoricoModificación correspondiente
     *
     * @param registroSalida
     * @param idEstado
     * @param observacionesAnulacion
     * @throws Exception
     */
    void cambiarEstadoAnuladoHistorico(RegistroSalida registroSalida, Long idEstado, UsuarioEntidad usuarioEntidad, String observacionesAnulacion) throws Exception;


    /**
     * Método que devuelve un registro de salida completo, con los anexosFull pero sin los documentos fisicos.
     * @param id
     * @return
     * @throws Exception
     * @throws I18NException
     */
    RegistroSalida getConAnexosFullLigero(Long id) throws Exception, I18NException;
    /**
     * Método que devuelve un registro de salida completo, con los anexos completos
     *
     * @param id
     * @return
     * @throws Exception
     * @throws I18NException
     */
    RegistroSalida getConAnexosFull(Long id) throws Exception, I18NException;

    /**
     * Rectificar Registro de Salida
     * @param idRegistro
     * @param usuarioEntidad
     * @return
     * @throws Exception
     */
    RegistroSalida rectificar(Long idRegistro, UsuarioEntidad usuarioEntidad) throws Exception;

    /**
     *
     * @param pageNumber
     * @param idOficina
     * @return
     * @throws Exception
     */
    Paginacion getSirRechazadosReenviadosPaginado(Integer pageNumber, Long idOficina) throws Exception;

    /**
     * Busca los Registros de Salida de una OficinaActiva Rechazados o Reenviados por SIR
     * @param idOficina
     * @param total
     * @return
     * @throws Exception
     */
    List<RegistroSalida> getSirRechazadosReenviados(Long idOficina, Integer total) throws Exception;

    /**
     * Total de Registros de Salida de una OficinaActiva Rechazados o Reenviados por SIR
     * @param idOficina
     * @return
     * @throws Exception
     */
    Long getSirRechazadosReenviadosCount(Long idOficina) throws Exception;

    /**
     *
     * @param idEntidad
     * @param documento
     * @return
     * @throws Exception
     */
    List<RegistroSalida> getByDocumento(Long idEntidad, String documento) throws Exception;

    /**
     * Metodo que llama al plugin de postproceso cuando creamos un registro de salida.
     * @param rs
     * @return
     * @throws Exception
     */
    void postProcesoNuevoRegistro(RegistroSalida rs, Long entidadId) throws Exception, I18NException;

    /**
     * Metodo que llama al plugin de postproceso cuando actualizamos un registro de salida
     * @param rs
     * @return
     * @throws Exception
     */
    void postProcesoActualizarRegistro(RegistroSalida rs, Long entidadId) throws Exception, I18NException;

}
