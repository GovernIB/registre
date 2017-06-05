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
    public RegistroBasico findByIdLigero(Long idRegistroEntrada) throws Exception;

    /**
     * Guarda un Registro de Entrada y le asocia un número de registro (con anexos)
     *
     * @param registroEntrada
     * @return
     * @throws Exception
     */
    public RegistroEntrada registrarEntrada(RegistroEntrada registroEntrada,
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
    public Paginacion busqueda(Integer pageNumber, Date fechaInicio, Date fechaFin, RegistroEntrada registroEntrada, String interesadoNom, String interesadoDoc, String interesadoLli1, String interesadoLli2, String organoDest, Boolean anexos, String observaciones, String usuario, Long idEntidad) throws Exception;

    /**
     * Busca los Registros de Entrada de una OficinaActiva en función de su estado.
     *
     * @param idOficina
     * @param idEstado
     * @return
     * @throws Exception
     */
    public List<RegistroBasico> getByOficinaEstado(Long idOficina, Long idEstado, Integer total) throws Exception;


    /**
     *
     * @param pageNumber
     * @param idOficinaActiva
     * @param idEstado
     * @return
     * @throws Exception
     */
    public Paginacion getByOficinaEstadoPaginado(Integer pageNumber, Long idOficinaActiva, Long idEstado) throws Exception;

    /**
     * Busca los Registros de Entrada de una OficinaActiva en función de su estado.
     *
     * @param idOficinaActiva
     * @param idEstado
     * @return
     * @throws Exception
     */
    public Long getByOficinaEstadoCount(Long idOficinaActiva, Long idEstado) throws Exception;

    /**
     * Busca los Registros de Entrada de un listado de Libros en función de su estado.
     *
     * @param inicio
     * @param libros
     * @param idEstado
     * @return
     * @throws Exception
     */
    public List<RegistroEntrada> getByLibrosEstado(int inicio, List<Libro> libros, Long idEstado) throws Exception;

    /**
     * Busca los Registros de Entrada de un listado de Libros en función de su estado.
     *
     * @param libros
     * @param idEstado
     * @return
     * @throws Exception
     */
    public Long getByLibrosEstadoCount(List<Libro> libros, Long idEstado) throws Exception;

    /**
     * Cambia el estado de un RegistroEntrada y el HistoricoModificación correspondiente
     *
     * @param registroEntrada
     * @param idEstado
     * @throws Exception
     */
    public void cambiarEstadoTrazabilidad(RegistroEntrada registroEntrada, Long idEstado, UsuarioEntidad usuarioEntidad) throws Exception;


    /**
     * Obtiene el RegistroEntrada a partir de su numero de registro formateado
     *
     * @param numeroRegistroFormateado
     * @return
     * @throws Exception
     */
    public RegistroEntrada findByNumeroRegistroFormateado(String numeroRegistroFormateado) throws Exception;

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
    public RegistroEntrada findByNumeroAnyoLibro(int numero, int anyo, String libro) throws Exception;

    /**
     * Devuelve el numero de registro formateado a partir de un registro detalle.
     * Se necesita para el plug-in postproceso para pasarselo cuando creamos interesados.
     *
     * @param idRegistroDetalle
     * @return
     * @throws Exception
     */
    public String findNumeroRegistroFormateadoByRegistroDetalle(Long idRegistroDetalle) throws Exception;


    /**
     * Anula un RegistroEntrada, cambiandole el estado a anulado.
     *
     * @param registroEntrada
     * @param usuarioEntidad
     * @throws Exception
     */
    public void anularRegistroEntrada(RegistroEntrada registroEntrada, UsuarioEntidad usuarioEntidad) throws Exception;

    /**
     * Activa un RegistroEntrada, cambiandole el estado a anulado.
     *
     * @param registroEntrada
     * @param usuarioEntidad
     * @throws Exception
     */
    public void activarRegistroEntrada(RegistroEntrada registroEntrada, UsuarioEntidad usuarioEntidad) throws Exception;

    /**
     * Visa un RegistroEntrada, cambiandole el estado a anulado.
     *
     * @param registroEntrada
     * @param usuarioEntidad
     * @throws Exception
     */
    public void visarRegistroEntrada(RegistroEntrada registroEntrada, UsuarioEntidad usuarioEntidad) throws Exception;

    /**
     * Tramita un RegistroEntrada, cambiandole el estado a tramitado.
     *
     * @param registroEntrada
     * @param usuarioEntidad
     * @throws Exception
     */
    public void tramitarRegistroEntrada(RegistroEntrada registroEntrada, UsuarioEntidad usuarioEntidad) throws Exception;

    /**
     * Retorna el identificador del Libro al que pertenece el RegistroEntrada
     *
     * @param idRegistroEntrada
     * @return
     * @throws Exception
     */
    public Long getLibro(Long idRegistroEntrada) throws Exception;

    /**
     * Comprueba si un Registro de Entrada se puede tramitar o no
     *
     * @param idRegistro
     * @param organismos
     * @return
     * @throws Exception
     */
    public Boolean isDistribuir(Long idRegistro, Set<Long> organismos) throws Exception;

    /**
     * Elimina los RegistroEntrada de una Entidad
     *
     * @param idEntidad
     * @return
     * @throws Exception
     */
    public Integer eliminarByEntidad(Long idEntidad) throws Exception;

    /**
     * Busca los Registros de Entrada de un Libro.
     *
     * @param idLibro
     * @return
     * @throws Exception
     */
    public Long getTotalByLibro(Long idLibro) throws Exception;

    /**
     * Comprueba si un usuario tiene RegistroEntrada
     *
     * @param idUsuarioEntidad
     * @return
     * @throws Exception
     */
    public Boolean obtenerPorUsuario(Long idUsuarioEntidad) throws Exception;

    /**
     * Comprueba si un RegistroEntrada tiene un Estado en concreto
     *
     * @param idRegistroEntrada
     * @param idEstado
     * @return
     * @throws Exception
     */
    public Boolean tieneEstado(Long idRegistroEntrada, Long idEstado) throws Exception;

    /**
     * Método que devuelve un registro de entrada completo, con anexoFull pero sin los documentos fisicos.
     *
     * @param id
     * @return
     * @throws Exception
     * @throws I18NException
     */
    public RegistroEntrada getConAnexosFullLigero(Long id) throws Exception, I18NException;

    /**
     * Método que devuelve un registro de entrada completo, con los anexos completos
     *
     * @param id
     * @return
     * @throws Exception
     * @throws I18NException
     */
    public RegistroEntrada getConAnexosFull(Long id) throws Exception, I18NException;

    /**
     * Obtiene un Registro de Entrada a partir de su IdentificadorIntercambio
     *
     * @param identificadorIntercambio
     * @return
     * @throws Exception
     */
    public RegistroEntrada getByIdentificadorIntercambio(String identificadorIntercambio) throws Exception;

    /**
     * Rectificar un Registro de entrada, creando una nuevo informando de ello
     *
     * @param idRegistro
     * @return
     * @throws Exception
     */
    public RegistroEntrada rectificar(Long idRegistro, UsuarioEntidad usuarioEntidad) throws Exception;

    /**
     * Método que obtiene los destinatarios a los que distribuir el registro
     *
     * @param re             registro de entrada a distribuir
     * @param usuarioEntidad
     * @return lista de destinatarios a los que se debe distribuir el registro
     * @throws Exception
     * @throws I18NException
     */
    public RespuestaDistribucion distribuir(RegistroEntrada re, UsuarioEntidad usuarioEntidad) throws Exception, I18NException;

    /**
     * Método que envia un registro de entrada a un conjunto de destinatarios
     *
     * @param re      registro de entrada
     * @param wrapper contiene los destinatarios a los que enviar el registro de entrada
     * @return
     * @throws Exception
     * @throws I18NException
     */
    public Boolean enviar(RegistroEntrada re, DestinatarioWrapper wrapper,
        Long entidadId, String idioma) throws Exception, I18NException;

    /**
     * Método que invoca al plugin post proceso al actualizar un registro entrada.
     *
     * @param re
     * @return
     * @throws Exception
     */
    public void postProcesoActualizarRegistro(RegistroEntrada re, Long entidadId) throws Exception, I18NException;

    /**
     * @param re
     * @return
     * @throws Exception
     */
    public void postProcesoNuevoRegistro(RegistroEntrada re, Long entidadId) throws Exception, I18NException;

}
