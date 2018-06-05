package es.caib.regweb3.persistence.ejb;

import es.caib.regweb3.model.Interesado;
import es.caib.regweb3.model.Libro;
import es.caib.regweb3.model.RegistroEntrada;
import es.caib.regweb3.model.UsuarioEntidad;
import es.caib.regweb3.model.utils.AnexoFull;
import es.caib.regweb3.model.utils.RegistroBasico;
import es.caib.regweb3.persistence.utils.DestinatarioWrapper;
import es.caib.regweb3.persistence.utils.Paginacion;
import es.caib.regweb3.persistence.utils.RespuestaDistribucion;
import org.fundaciobit.genapp.common.i18n.I18NException;
import org.fundaciobit.genapp.common.i18n.I18NValidationException;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Local;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * Created by Fundació BIT.
 *
 * @author earrivi
 *         Date: 16/01/14
 */
@Local
@RolesAllowed({"RWE_SUPERADMIN", "RWE_ADMIN", "RWE_USUARI"})
public interface RegistroEntradaLocal extends RegistroEntradaCambiarEstadoLocal {


    /**
     * Obtiene un RegistroEntrada con un mínimo de campos
     * @param idRegistroEntrada
     * @return
     * @throws Exception
     */
    RegistroBasico findByIdLigero(Long idRegistroEntrada) throws Exception;

    /**
     * Guarda un Registro de Entrada y le asocia un número de registro (con anexos)
     *
     * @param registroEntrada
     * @return
     * @throws Exception
     */
    RegistroEntrada registrarEntrada(RegistroEntrada registroEntrada,
                                     UsuarioEntidad usuarioEntidad, List<Interesado> interesados, List<AnexoFull> anexosFull)
            throws Exception, I18NException, I18NValidationException;

    /**
     * Busca los Registros de Entrada en función de los parámetros
     *
     * @param pageNumber
     * @param fechaInicio
     * @param fechaFin
     * @param registroEntrada
     * @return
     * @throws Exception
     */
    Paginacion busqueda(Integer pageNumber, Date fechaInicio, Date fechaFin, RegistroEntrada registroEntrada, String interesadoNom, String interesadoDoc, String interesadoLli1, String interesadoLli2, String organoDest, Boolean anexos, String observaciones, String usuario, Long idEntidad) throws Exception;

    /**
     * Busca los Registros de Entrada de una OficinaActiva en función de su estado.
     *
     * @param idOficina
     * @param idEstado
     * @return
     * @throws Exception
     */
    List<RegistroBasico> getByOficinaEstado(Long idOficina, Long idEstado, Integer total) throws Exception;

    /**
     *
     * @param pageNumber
     * @param idOficinaActiva
     * @param idEstado
     * @return
     * @throws Exception
     */
    Paginacion getByOficinaEstadoPaginado(Integer pageNumber, Long idOficinaActiva, Long idEstado) throws Exception;

    /**
     * Busca los Registros de Entrada de una OficinaActiva en función de su estado.
     *
     * @param idOficinaActiva
     * @param idEstado
     * @return
     * @throws Exception
     */
    Long getByOficinaEstadoCount(Long idOficinaActiva, Long idEstado) throws Exception;

    /**
     * Busca los Registros de Entrada de un listado de Libros en función de su estado.
     *
     * @param inicio
     * @param libros
     * @param idEstado
     * @return
     * @throws Exception
     */
    List<RegistroEntrada> getByLibrosEstado(int inicio, List<Libro> libros, Long idEstado) throws Exception;

    /**
     * Busca los Registros de Entrada de un listado de Libros en función de su estado.
     *
     * @param libros
     * @param idEstado
     * @return
     * @throws Exception
     */
    Long getByLibrosEstadoCount(List<Libro> libros, Long idEstado) throws Exception;

    /**
     * Cambia el estado de un RegistroEntrada y el HistoricoModificación correspondiente
     *
     * @param registroEntrada
     * @param idEstado
     * @throws Exception
     */
    void cambiarEstadoHistorico(RegistroEntrada registroEntrada, Long idEstado, UsuarioEntidad usuarioEntidad) throws Exception;


    /**
     * Obtiene el RegistroEntrada a partir de su numero de registro formateado, entidad y el libro
     * @param codigoEntidad
     * @param numeroRegistroFormateado
     * @param codigoLibro
     * @return
     * @throws Exception
     */
    RegistroEntrada findByNumeroRegistroFormateado(String codigoEntidad, String numeroRegistroFormateado, String codigoLibro) throws Exception;

    /**
     * Obtiene el RegistroEntrada a partir de su numero de registro formateado, entidad y el libro cargando sus anexos.
     * @param codigoEntidad
     * @param numeroRegistroFormateado
     * @param codigoLibro
     * @return
     * @throws Exception
     * @throws I18NException
     */
    public RegistroEntrada findByNumeroRegistroFormateadoConAnexos(String codigoEntidad, String numeroRegistroFormateado, String codigoLibro) throws Exception, I18NException;

    /**
     * Obtiene el numero de registro formateado de un RegistroEntrada
     * a partir de su numero de registro, año y libro.
     *
     * @param numero
     * @param anyo
     * @param libro
     * @return
     * @throws Exception
     */
    RegistroEntrada findByNumeroAnyoLibro(int numero, int anyo, String libro) throws Exception;

    /**
     * Devuelve el numero de registro formateado a partir de un registro detalle.
     * Se necesita para el plug-in postproceso para pasarselo cuando creamos interesados.
     *
     * @param idRegistroDetalle
     * @return
     * @throws Exception
     */
    String findNumeroRegistroFormateadoByRegistroDetalle(Long idRegistroDetalle) throws Exception;


    /**
     * Anula un RegistroEntrada, cambiandole el estado a anulado.
     *
     * @param registroEntrada
     * @param usuarioEntidad
     * @throws Exception
     */
    void anularRegistroEntrada(RegistroEntrada registroEntrada, UsuarioEntidad usuarioEntidad) throws Exception;

    /**
     * Activa un RegistroEntrada, cambiandole el estado a anulado.
     *
     * @param registroEntrada
     * @param usuarioEntidad
     * @throws Exception
     */
    void activarRegistroEntrada(RegistroEntrada registroEntrada, UsuarioEntidad usuarioEntidad) throws Exception;

    /**
     * Visa un RegistroEntrada, cambiandole el estado a anulado.
     *
     * @param registroEntrada
     * @param usuarioEntidad
     * @throws Exception
     */
    void visarRegistroEntrada(RegistroEntrada registroEntrada, UsuarioEntidad usuarioEntidad) throws Exception;

    /**
     * Tramita un RegistroEntrada, cambiandole el estado a tramitado.
     *
     * @param registroEntrada
     * @param usuarioEntidad
     * @throws Exception
     */
    void tramitarRegistroEntrada(RegistroEntrada registroEntrada, UsuarioEntidad usuarioEntidad) throws Exception, I18NValidationException, I18NException;


    /**
     * Genera el justificante del registro de entrada en el caso que no esté generado.
     * Se usa para generarlo antes de distribuir el registro
     * @param registroEntrada
     * @param usuarioEntidad
     * @throws Exception
     * @throws I18NValidationException
     * @throws I18NException
     */
    RegistroEntrada generarJustificanteRegistroEntrada(RegistroEntrada registroEntrada,
                                                       UsuarioEntidad usuarioEntidad) throws Exception, I18NValidationException, I18NException;

    /**
     * Retorna el identificador del Libro al que pertenece el RegistroEntrada
     *
     * @param idRegistroEntrada
     * @return
     * @throws Exception
     */
    Long getLibro(Long idRegistroEntrada) throws Exception;

    /**
     * Comprueba si un Registro de Entrada se puede tramitar o no
     *
     * @param idRegistro
     * @param organismos
     * @return
     * @throws Exception
     */
    Boolean isDistribuir(Long idRegistro, Set<Long> organismos) throws Exception;

    /**
     * Elimina los RegistroEntrada de una Entidad
     *
     * @param idEntidad
     * @return
     * @throws Exception
     */
    Integer eliminarByEntidad(Long idEntidad) throws Exception;

    /**
     * Busca los Registros de Entrada de un Libro.
     *
     * @param idLibro
     * @return
     * @throws Exception
     */
    Long getTotalByLibro(Long idLibro) throws Exception;

    /**
     * Comprueba si un usuario tiene RegistroEntrada
     *
     * @param idUsuarioEntidad
     * @return
     * @throws Exception
     */
    Boolean obtenerPorUsuario(Long idUsuarioEntidad) throws Exception;

    /**
     * Comprueba si un RegistroEntrada tiene un Estado en concreto
     *
     * @param idRegistroEntrada
     * @param idEstado
     * @return
     * @throws Exception
     */
    Boolean tieneEstado(Long idRegistroEntrada, Long idEstado) throws Exception;

    /**
     * Método que devuelve un registro de entrada completo, con anexoFull pero sin los documentos fisicos.
     *
     * @param id
     * @return
     * @throws Exception
     * @throws I18NException
     */
    RegistroEntrada getConAnexosFullLigero(Long id) throws Exception, I18NException;

    /**
     * Método que devuelve un registro de entrada completo, con los anexos completos
     *
     * @param id
     * @return
     * @throws Exception
     * @throws I18NException
     */
    RegistroEntrada getConAnexosFull(Long id) throws Exception, I18NException;


    /**
     * Cargamos los anexos del registro de entrada que nos pasan.
     * @param registroEntrada
     * @return
     * @throws Exception
     * @throws I18NException
     */
    RegistroEntrada cargarAnexosFull(RegistroEntrada registroEntrada) throws Exception, I18NException;

    /**
     * Obtiene un Registro de Entrada a partir de su IdentificadorIntercambio
     *
     * @param identificadorIntercambio
     * @return
     * @throws Exception
     */
    RegistroEntrada getByIdentificadorIntercambio(String identificadorIntercambio) throws Exception;

    /**
     * Rectificar un Registro de entrada, creando una nuevo informando de ello
     *
     * @param idRegistro
     * @return
     * @throws Exception
     */
    RegistroEntrada rectificar(Long idRegistro, UsuarioEntidad usuarioEntidad) throws Exception, I18NException;


    /**
     * Método que realiza la distribución de los elementos de la cola pendientes de distribuir de una entidad
     * @param entidadId
     * @throws Exception
     * @throws I18NException
     */
    public void iniciarDistribucionLista(Long entidadId, List<UsuarioEntidad> administradores) throws Exception, I18NException, I18NValidationException;


    /**
     * Método que envia un registro a la cola de Distribución
     * @param re
     * @throws Exception
     * @throws I18NException
     * @throws I18NValidationException
     */
     void enviarAColaDistribucion(RegistroEntrada re, int maxReintentos) throws Exception, I18NException, I18NValidationException;

    /**
     * Método que obtiene los destinatarios a los que distribuir el registro
     *
     * @param re             registro de entrada a distribuir
     * @param usuarioEntidad
     * @return lista de destinatarios a los que se debe distribuir el registro
     * @throws Exception
     * @throws I18NException
     */
    RespuestaDistribucion distribuir(RegistroEntrada re, UsuarioEntidad usuarioEntidad) throws Exception, I18NException, I18NValidationException;

    /**
     * Método que envia un registro de entrada a un conjunto de destinatarios
     *
     * @param re      registro de entrada
     * @param wrapper contiene los destinatarios a los que enviar el registro de entrada
     * @return
     * @throws Exception
     * @throws I18NException
     */
    Boolean enviar(RegistroEntrada re, DestinatarioWrapper wrapper,
                   Long entidadId, String idioma) throws Exception, I18NException, I18NValidationException;

    /**
     *
     * @param pageNumber
     * @param idOficina
     * @return
     * @throws Exception
     */
    Paginacion getSirRechazadosReenviadosPaginado(Integer pageNumber, Long idOficina) throws Exception;

    /**
     * Busca los Registros de Entrada de una OficinaActiva Rechazados o Reenviados por SIR
     * @param idOficina
     * @param total
     * @return
     * @throws Exception
     */
    List<RegistroEntrada> getSirRechazadosReenviados(Long idOficina, Integer total) throws Exception;

    /**
     * Total de Registros de Entrada de una OficinaActiva Rechazados o Reenviados por SIR
     * @param idOficina
     * @return
     * @throws Exception
     */
    Long getSirRechazadosReenviadosCount(Long idOficina) throws Exception;

    /**
     * Actualiza el Destino extinguido por el que le sustituye
     * @param idOrganismoExtinguido
     * @param idOrganismo
     * @throws Exception
     */
    void actualizarDestinoExtinguido(Long idOrganismoExtinguido, Long idOrganismo) throws Exception;

    /**
     * Método que invoca al plugin post proceso al actualizar un registro entrada.
     *
     * @param re
     * @return
     * @throws Exception
     */
    void postProcesoActualizarRegistro(RegistroEntrada re, Long entidadId) throws Exception, I18NException;

    /**
     * @param re
     * @return
     * @throws Exception
     */
    void postProcesoNuevoRegistro(RegistroEntrada re, Long entidadId) throws Exception, I18NException;

}
