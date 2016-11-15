package es.caib.regweb3.persistence.ejb;

import es.caib.regweb3.model.*;
import es.caib.regweb3.persistence.utils.Paginacion;
import org.fundaciobit.genapp.common.i18n.I18NException;
import org.fundaciobit.genapp.common.i18n.I18NValidationException;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Local;
import java.util.List;
import java.util.Set;

/**
 * Created by Fundació BIT.
 *
 * @author earrivi
 * Date: 16/01/14
 */
@Local
@RolesAllowed({"RWE_SUPERADMIN","RWE_ADMIN","RWE_USUARI"})
public interface OficioRemisionLocal extends BaseEjb<OficioRemision, Long> {

    /**
     * Realizada una Búsqueda según los parámetros
     * @param pageNumber
     * @param any
     * @param oficioRemision
     * @param libros
     * @param tipoOficioRemision
     * @return
     * @throws Exception
     */
    public Paginacion busqueda(Integer pageNumber, Integer any, OficioRemision oficioRemision, List<Libro> libros, Long tipoOficioRemision, Integer estadoOficioRemision, Long tipoRegistro) throws Exception;

    /**
     * Registra un OficioRemision asignandole número
     * @param oficioRemision
     * @param estado
     * @return
     * @throws Exception
     */
    public OficioRemision registrarOficioRemision(OficioRemision oficioRemision, Long estado, Long tipoOficioRemision)
        throws Exception, I18NException, I18NValidationException;

    /**
     * Anula un Oficio de Remisión
     * @param idOficioRemision
     * @param usuarioEntidad
     * @throws Exception
     */
    public void anularOficioRemision(Long idOficioRemision, UsuarioEntidad usuarioEntidad) throws Exception;

    /**
     * Devuelve los Oficios de Remisión pendientes de procesar de los organismos seleccionados
     * @param organismos
     * @return
     * @throws Exception
     */
    public List<OficioRemision> oficiosPendientesLlegada(Set<Organismo> organismos, Integer total) throws Exception;

    /**
     * Devuelve los Oficios de Remisión pendientes de procesar de los organismos seleccionados paginados
     * @param organismos
     * @param libros
     * @param oficioRemision
     * @return
     * @throws Exception
     */
    public Paginacion oficiosPendientesLlegadaBusqueda(Set<Organismo> organismos, Integer pageNumber,OficioRemision oficioRemision, List<Libro> libros, Long tipoOficioRemision) throws Exception;

    /**
     * Devuelve los Oficios de Remisión pendientes de procesar de los organismos seleccionados
     * @param organismos
     * @return
     * @throws Exception
     */
    public Long oficiosPendientesLlegadaCount(Set<Organismo> organismos) throws Exception;

    /**
     * Busca Registros de Entrada que pertenecen a un OficioRemision
     * @param idOficioRemision
     * @return
     * @throws Exception
     */
    public List<RegistroEntrada> getEntradasByOficioRemision(Long idOficioRemision) throws Exception;

    /**
     * Busca Registros de Salida que pertenecen a un OficioRemision
     * @param idOficioRemision
     * @return
     * @throws Exception
     */
    public List<RegistroSalida> getSalidasByOficioRemision(Long idOficioRemision) throws Exception;

    /**
     * Elimina los Oficios de remisión de una Entidad
     * @param idEntidad
     * @return
     * @throws Exception
     */
    public Integer eliminarByEntidad(Long idEntidad) throws Exception;
}
