package es.caib.regweb.persistence.ejb;

import es.caib.regweb.model.HistoricoRegistroEntrada;
import es.caib.regweb.model.Libro;
import es.caib.regweb.model.RegistroEntrada;
import es.caib.regweb.model.UsuarioEntidad;

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
public interface HistoricoRegistroEntradaLocal extends BaseEjb<HistoricoRegistroEntrada, Long> {

  public List<HistoricoRegistroEntrada> getByRegistroEntrada(Long idRegistro) throws Exception;

    /**
     * Busca los Registros de Entrada Modificada en función de la una fecha inicio, una fecha fin, por Usuario y Libros gestionados
     * @param fechaInicio
     * @param fechaFin
     * @param idUsuario
     * @param libros
     * @return
     * @throws Exception
     */
    public List<HistoricoRegistroEntrada> entradaModificadaPorUsuario(Date fechaInicio, Date fechaFin, Long idUsuario, List<Libro> libros) throws Exception;

    /**
     * Busca los Registros de Entrada Modificada que no sean de 'Creación' en función de la una fecha inicio, una fecha fin, por Usuario y Libro
     * @param fechaInicio
     * @param fechaFin
     * @param idUsuario
     * @param idLibro
     * @return
     * @throws Exception
     */
    public List<HistoricoRegistroEntrada> entradaModificadaPorUsuarioLibro(Date fechaInicio, Date fechaFin, Long idUsuario, Long idLibro) throws Exception;

    /**
     * Crea un HistoricoRegistroEntrada según los parámetros
     * @param registroEntrada
     * @param usuarioEntidad
     * @param modificacion
     * @param serializar
     * @return
     * @throws Exception
     */
    public HistoricoRegistroEntrada crearHistoricoRegistroEntrada(RegistroEntrada registroEntrada, UsuarioEntidad usuarioEntidad, String modificacion, boolean serializar) throws Exception;

  /**
   * Elimina las HistoricoRegistroSalida de una Entidad
   * @param idEntidad
   * @return
   * @throws Exception
   */
  public Integer eliminarByEntidad(Long idEntidad) throws Exception;


}
