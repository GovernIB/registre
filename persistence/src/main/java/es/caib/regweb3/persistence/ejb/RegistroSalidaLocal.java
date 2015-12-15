package es.caib.regweb3.persistence.ejb;

import es.caib.regweb3.model.Libro;
import es.caib.regweb3.model.RegistroSalida;
import es.caib.regweb3.model.UsuarioEntidad;
import es.caib.regweb3.model.utils.RegistroBasico;
import es.caib.regweb3.persistence.utils.AnexoFull;
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
    public RegistroSalida registrarSalida(RegistroSalida registroSalida) 
        throws Exception, I18NException, I18NValidationException;
    
    /**
     * Guarda un Registro de Salida (con anexos)
     * @param registroSalida
     * @return
     * @throws Exception
     */
    public RegistroSalida registrarSalida(RegistroSalida registroSalida,
        UsuarioEntidad usuarioEntidad, List<AnexoFull> anexos) 
            throws Exception, I18NException, I18NValidationException;
    

    /**
     * Busca los Registros de Salida en función de los parametros
     * @param pageNumber
     * @param registroSalida
     * @param libros
     * @return
     * @throws Exception
     */
    public Paginacion busqueda(Integer pageNumber, Date fechaInicio, Date fechaFin, RegistroSalida registroSalida, List<Libro> libros, String interesadoNom, String interesadoDoc, String organoOrig, Boolean anexos, String observaciones, String usuario) throws Exception;

    
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
     * Busca los Registros de Salida en función de la una fecha inicio, una fecha fin y el libro
     * @param fechaInicio
     * @param fechaFin
     * @param libros
     * @return
     * @throws Exception
     */
    public List<RegistroSalida> buscaLibroRegistro(Date fechaInicio, Date fechaFin, List<Libro> libros) throws Exception;

    /**
     * Busca los Registros de Salida que no estén anulados ni pendientes en función de la una fecha inicio, una fecha fin, de la Entidad Activa
     * @param fechaInicio
     * @param fechaFin
     * @param idEntidad
     * @return
     * @throws Exception
     */
    public Long buscaIndicadoresTotal(Date fechaInicio, Date fechaFin, Long idEntidad) throws Exception;

    /**
     * Busca los Registros de Salida que no estén anulados ni pendientes en función de la una fecha inicio, una fecha fin, de una Oficina
     * @param fechaInicio
     * @param fechaFin
     * @param idOficina
     * @return
     * @throws Exception
     */
    public Long buscaIndicadoresOficinaTotal(Date fechaInicio, Date fechaFin, Long idOficina) throws Exception;


    /**
     * Busca los Registros de Salida que no estén anulados ni pendientes en función de la una fecha inicio, una fecha fin, por Conselleria
     * @param fechaInicio
     * @param fechaFin
     * @param conselleria
     * @return
     * @throws Exception
     */
    public Long buscaSalidaPorConselleria(Date fechaInicio, Date fechaFin, Long conselleria) throws Exception;

    /**
     * Busca los Registros de Salida que no estén anulados ni pendientes en función de la una fecha inicio, una fecha fin, por Tipo de Asunto, de la Entidad Activa
     * @param fechaInicio
     * @param fechaFin
     * @param tipoAsunto
     * @param idEntidad
     * @return
     * @throws Exception
     */
    public Long buscaSalidaPorAsunto(Date fechaInicio, Date fechaFin, Long tipoAsunto, Long idEntidad) throws Exception;

    /**
     * Busca los Registros de Salida que no estén anulados ni pendientes en función de la una fecha inicio, una fecha fin, por Idiomas, de la Entidad Activa
     * @param fechaInicio
     * @param fechaFin
     * @param idioma
     * @param idOficina
     * @return
     * @throws Exception
     */
    public Long buscaSalidaPorIdiomaOficina(Date fechaInicio, Date fechaFin, Long idioma, Long idOficina) throws Exception;

    /**
     * Busca los Registros de Salida que no estén anulados ni pendientes en función de la una fecha inicio, una fecha fin, por Idiomas, de la Entidad Activa
     * @param fechaInicio
     * @param fechaFin
     * @param idioma
     * @param idEntidad
     * @return
     * @throws Exception
     */
    public Long buscaSalidaPorIdioma(Date fechaInicio, Date fechaFin, Long idioma, Long idEntidad) throws Exception;



    /**
     * Busca los Registros de Salida que no estén anulados ni pendientes en función de la una fecha inicio, una fecha fin, por Libros
     * @param fechaInicio
     * @param fechaFin
     * @param libro
     * @return
     * @throws Exception
     */
    public Long buscaSalidaPorLibro(Date fechaInicio, Date fechaFin, Long libro) throws Exception;

    /**
     * Busca los Registros de Salida que no estén anulados ni pendientes en función de la una fecha inicio, una fecha fin, por Oficinas
     * @param fechaInicio
     * @param fechaFin
     * @param oficina
     * @return
     * @throws Exception
     */
    public Long buscaSalidaPorOficina(Date fechaInicio, Date fechaFin, Long oficina) throws Exception;

    /**
     * Busca los Registros de Salida en función de la una fecha inicio, una fecha fin, por Usuario y Libros gestionados
     * @param fechaInicio
     * @param fechaFin
     * @param idUsuario
     * @param libros
     * @return
     * @throws Exception
     */
    public List<RegistroSalida> buscaSalidaPorUsuario(Date fechaInicio, Date fechaFin, Long idUsuario, List<Libro> libros) throws Exception;

    /**
     * Busca los Registros de Salida que no estén pendientes en función de la una fecha inicio, una fecha fin, por Usuario y Libro
     * @param fechaInicio
     * @param fechaFin
     * @param idUsuario
     * @param idLibro
     * @return
     * @throws Exception
     */
    public List<RegistroSalida> buscaSalidaPorUsuarioLibro(Date fechaInicio, Date fechaFin, Long idUsuario, Long idLibro) throws Exception;

    /**
     * Busca los Registros de Salida en función de la una fecha inicio, una fecha fin, Libro y Número de registro
     * @param fechaInicio
     * @param fechaFin
     * @param idLibro
     * @param numeroRegistro
     * @return
     * @throws Exception
     */
    public List<RegistroSalida> buscaPorLibroTipoNumero(Date fechaInicio, Date fechaFin, Long idLibro, Integer numeroRegistro) throws Exception;

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

}
