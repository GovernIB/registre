package es.caib.regweb3.persistence.ejb;

import es.caib.regweb3.model.Interesado;
import es.caib.regweb3.model.Libro;
import es.caib.regweb3.model.RegistroSalida;
import es.caib.regweb3.model.UsuarioEntidad;
import es.caib.regweb3.model.utils.AnexoFull;
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
@RolesAllowed({"RWE_SUPERADMIN","RWE_ADMIN","RWE_USUARI"})
public interface RegistroSalidaLocal extends RegistroSalidaCambiarEstadoLocal {
    
    /**
     * Guarda un Registro de Salida (con anexos)
     * @param registroSalida
     * @return
     * @throws Exception
     */
    public RegistroSalida registrarSalida(RegistroSalida registroSalida,
                                          UsuarioEntidad usuarioEntidad, List<Interesado> interesados, List<AnexoFull> anexos)
            throws Exception, I18NException, I18NValidationException;
    

    /**
     * Busca los Registros de Salida en función de los parametros
     * @param pageNumber
     * @param registroSalida
     * @return
     * @throws Exception
     */
    public Paginacion busqueda(Integer pageNumber, Date fechaInicio, Date fechaFin, RegistroSalida registroSalida, String interesadoNom, String interesadoDoc, String interesadoLli1, String interesadoLli2, String organoOrigen, Boolean anexos, String observaciones, String usuario, Long idEntidad) throws Exception;

    
    /**
     * Busca los Registros de Salida en función de los parametros
     * @param numero
     * @param anyo
     * @param libro
     * @return
     * @throws Exception
     */
    public RegistroSalida findByNumeroAnyoLibro(int numero, int anyo, String libro) throws Exception;

    /**
     * Obtiene el RegistroSalida a partir de su numero de registro formateado
     * @param numeroRegistroFormateado
     * @return
     * @throws Exception
     */
    public RegistroSalida findByNumeroRegistroFormateado(String numeroRegistroFormateado) throws Exception;


    /**
     * Devuelve el numero de registro formateado a partir de un registro detalle.
     * Se necesita para el plug-in postproceso para pasarselo cuando creamos interesados.
     * @param idRegistroDetalle
     * @return
     * @throws Exception
     */
    public String findNumeroRegistroFormateadoByRegistroDetalle(Long idRegistroDetalle) throws Exception;

    /**
     * Anula un RegistroSalida, cambiandole el estado a anulado.
     * @param registroSalida
     * @param usuarioEntidad
     * @throws Exception
     */
    public void anularRegistroSalida(RegistroSalida registroSalida, UsuarioEntidad usuarioEntidad) throws Exception;

    /**
     * Activa un RegistroSalida, cambiandole el estado a anulado.
     * @param registroSalida
     * @param usuarioEntidad
     * @throws Exception
     */
    public void activarRegistroSalida(RegistroSalida registroSalida, UsuarioEntidad usuarioEntidad) throws Exception;

    /**
     * Visa un RegistroSalida, cambiandole el estado a anulado.
     * @param registroSalida
     * @param usuarioEntidad
     * @throws Exception
     */
    public void visarRegistroSalida(RegistroSalida registroSalida, UsuarioEntidad usuarioEntidad) throws Exception;

    /**
     * Elimina los RegistroSalida de una Entidad
     * @param idEntidad
     * @return
     * @throws Exception
     */
    public Integer eliminarByEntidad(Long idEntidad) throws Exception;

    /**
     * Retorna el identificador del Libro al que pertenece el RegistroSalida
     * @param idRegistroSalida
     * @return
     * @throws Exception
     */
    public Long getLibro(Long idRegistroSalida) throws Exception;

    /**
     * Busca los Registros de Salida de un listado de Libros en función de su estado.
     * @param libros
     * @param idEstado
     * @return
     * @throws Exception
     */
    public Long getByLibrosEstadoCount(List<Libro> libros, Long idEstado) throws Exception;

    /**
     * Busca los Registros de Salida de un listado de Libros en función de su estado.
     * @param inicio
     * @param libros
     * @param idEstado
     * @return
     * @throws Exception
     */
    public List<RegistroSalida> getByLibrosEstado(int inicio, List<Libro> libros, Long idEstado) throws Exception;

    /**
     * Busca los Registros de Salida de un Libro.
     * @param idLibro
     * @return
     * @throws Exception
     */
    public Long getTotalByLibro(Long idLibro) throws Exception;

    /**
     * Comprueba si un usuario tiene RegistroSalida
     *
     * @param idUsuarioEntidad
     * @return
     * @throws Exception
     */
    public Boolean obtenerPorUsuario(Long idUsuarioEntidad) throws Exception;

    /**
     * Cambia el estado de un RegistroSalida
     * @param registroSalida
     * @param idEstado
     * @param usuarioEntidad
     * @throws Exception
     */
    public void cambiarEstadoTrazabilidad(RegistroSalida registroSalida, Long idEstado, UsuarioEntidad usuarioEntidad) throws Exception;

    /**
     * Método que devuelve un registro de salida completo, con los anexosFull pero sin los documentos fisicos.
     * @param id
     * @return
     * @throws Exception
     * @throws I18NException
     */
    public RegistroSalida getConAnexosFullLigero(Long id) throws Exception, I18NException;
    /**
     * Método que devuelve un registro de salida completo, con los anexos completos
     *
     * @param id
     * @return
     * @throws Exception
     * @throws I18NException
     */
    public RegistroSalida getConAnexosFull(Long id) throws Exception, I18NException;

    /**
     * Rectificar Registro de Salida
     * @param idRegistro
     * @param usuarioEntidad
     * @return
     * @throws Exception
     */
    public RegistroSalida rectificar(Long idRegistro, UsuarioEntidad usuarioEntidad) throws Exception;


    /**
     * Metodo que llama al plugin de postproceso cuando creamos un registro de salida.
     * @param rs
     * @return
     * @throws Exception
     */
    public void postProcesoNuevoRegistro(RegistroSalida rs, Long entidadId) throws Exception;

    /**
     * Metodo que llama al plugin de postproceso cuando actualizamos un registro de salida
     * @param rs
     * @return
     * @throws Exception
     */
    public void postProcesoActualizarRegistro(RegistroSalida rs, Long entidadId) throws Exception;

}
