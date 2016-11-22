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
@RolesAllowed({"RWE_SUPERADMIN","RWE_ADMIN","RWE_USUARI"})
public interface RegistroSalidaLocal extends RegistroSalidaCambiarEstadoLocal {

    /**
     * Obtiene el Numero RegistroSalida Formateado
     * @param idRegistroSalida
     * @return
     * @throws Exception
     */
    public String getNumeroRegistroSalida(Long idRegistroSalida) throws Exception;

    /**
     * Obtiene los Registros de Salida de un Usuario.
     * @param idUsuarioEntidad
     * @return
     * @throws Exception
     */
    public List<RegistroSalida> getByUsuario(Long idUsuarioEntidad) throws Exception;

    /**
     * Guarda un Registro de Salida (sin anexos)
     * @param registroSalida
     * @return
     * @throws Exception
     */
    public RegistroSalida registrarSalida(RegistroSalida registroSalida,
                                          UsuarioEntidad usuarioEntidad, List<Interesado> interesados)
        throws Exception, I18NException, I18NValidationException;
    
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
     * Busca los últimos RegistroSalida de una Oficina
     * @param idOficina
     * @param total
     * @return
     * @throws Exception
     */
    public List<RegistroBasico> getUltimosRegistros(Long idOficina, Integer total) throws Exception;

    /**
     * Obtiene el RegistroSalida a partir de su numero de registro formateado
     * @param numeroRegistroFormateado
     * @return
     * @throws Exception
     */
    public RegistroSalida findByNumeroRegistroFormateado(String numeroRegistroFormateado) throws Exception;

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
     * @param libros
     * @param idEstado
     * @return
     * @throws Exception
     */
    public List<RegistroSalida> getByLibrosEstado(List<Libro> libros, Long idEstado) throws Exception;

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
    public void cambiarEstado(RegistroSalida registroSalida, Long idEstado, UsuarioEntidad usuarioEntidad) throws Exception;

    /**
     * Método que devuelve un registro de salida completo, con los anexos completos
     *
     * @param id
     * @return
     * @throws Exception
     * @throws I18NException
     */
    public RegistroSalida getConAnexosFull(Long id) throws Exception, I18NException;

}
