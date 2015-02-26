package es.caib.regweb.persistence.ejb;

import es.caib.regweb.model.HistoricoRegistroSalida;
import es.caib.regweb.model.Libro;
import es.caib.regweb.model.RegistroSalida;
import es.caib.regweb.model.UsuarioEntidad;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Local;
import java.util.Date;
import java.util.List;

/**
 * Created by Fundació BIT.
 *
 * @author jpernia
 * Date: 30/10/14
 */
@Local
@RolesAllowed({"RWE_SUPERADMIN","RWE_ADMIN","RWE_USUARI"})
public interface HistoricoRegistroSalidaLocal extends BaseEjb<HistoricoRegistroSalida, Long> {

    public List<HistoricoRegistroSalida> getByRegistroSalida(Long idRegistro) throws Exception;

    /**
     * Busca los Registros de Salida Modificada en función de la una fecha inicio, una fecha fin, por Usuario y Libros gestionados
     * @param fechaInicio
     * @param fechaFin
     * @param idUsuario
     * @param libros
     * @return
     * @throws Exception
     */
    public List<HistoricoRegistroSalida> salidaModificadaPorUsuario(Date fechaInicio, Date fechaFin, Long idUsuario, List<Libro> libros) throws Exception;

    /**
     * Busca los Registros de Salida Modificada que no sean de 'Creación' en función de la una fecha inicio, una fecha fin, por Usuario y Libro
     * @param fechaInicio
     * @param fechaFin
     * @param idUsuario
     * @param idLibro
     * @return
     * @throws Exception
     */
    public List<HistoricoRegistroSalida> salidaModificadaPorUsuarioLibro(Date fechaInicio, Date fechaFin, Long idUsuario, Long idLibro) throws Exception;


    /**
     * Crea un HistoricoRegistroSalida según los parámetros
     * @param registroSalida
     * @param usuarioEntidad
     * @param modificacion
     * @param serializar
     * @return
     * @throws Exception
     */
    public HistoricoRegistroSalida crearHistoricoRegistroSalida(RegistroSalida registroSalida, UsuarioEntidad usuarioEntidad, String modificacion, boolean serializar) throws Exception;


}
