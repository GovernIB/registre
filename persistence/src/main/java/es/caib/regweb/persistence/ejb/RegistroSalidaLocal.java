package es.caib.regweb.persistence.ejb;

import es.caib.regweb.model.Libro;
import es.caib.regweb.model.RegistroSalida;
import es.caib.regweb.model.UsuarioEntidad;
import es.caib.regweb.persistence.utils.Paginacion;

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
public interface RegistroSalidaLocal extends BaseEjb<RegistroSalida, Long> {

    /**
     * Obtiene los Registros de Salida de un Usuario.
     * @param idUsuarioEntidad
     * @return
     * @throws Exception
     */
    public List<RegistroSalida> getByUsuario(Long idUsuarioEntidad) throws Exception;

    /**
     * Guarda un Registro de Salida
     * @param registroSalida
     * @return
     * @throws Exception
     */
    public RegistroSalida registrarSalida(RegistroSalida registroSalida) throws Exception;

    /**
     * Busca los Registros de Salida en función de los parametros
     * @param pageNumber
     * @param registroSalida
     * @param libros
     * @return
     * @throws Exception
     */
    public Paginacion busqueda(Integer pageNumber, Date fechaInicio, Date fechaFin, RegistroSalida registroSalida, List<Libro> libros, Boolean anexos) throws Exception;

    
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
    public List<RegistroSalida> buscaIndicadores(Date fechaInicio, Date fechaFin, Long idEntidad) throws Exception;

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
     * Cambiar el estado del registro
     * @param idRegistro
     * @param idEstado
     * @throws Exception
     */
    public void cambiarEstado(Long idRegistro, Long idEstado) throws Exception;

    /**
     * Busca los últimos RegistroSalida de una Oficina
     * @param idOficina
     * @param total
     * @return
     * @throws Exception
     */
    public List<RegistroSalida> getUltimosRegistros(Long idOficina, Integer total) throws Exception;

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

}
