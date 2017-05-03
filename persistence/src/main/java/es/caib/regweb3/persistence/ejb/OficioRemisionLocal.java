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
    public OficioRemision registrarOficioRemision(OficioRemision oficioRemision, Long estado)
        throws Exception, I18NException, I18NValidationException;

    /**
     * Anula un Oficio de Remisión
     * @param idOficioRemision
     * @param usuarioEntidad
     * @throws Exception
     */
    public void anularOficioRemision(Long idOficioRemision, UsuarioEntidad usuarioEntidad) throws Exception;

    /**
     *
     * @param oficioRemision
     * @return
     * @throws Exception
     * @throws I18NException
     * @throws I18NValidationException
     */
    public OficioRemision registrarOficioRemisionSIR(OficioRemision oficioRemision) throws Exception, I18NException, I18NValidationException;

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
     * @param oficioRemision
     * @return
     * @throws Exception
     */
    public Paginacion oficiosPendientesLlegadaBusqueda(Set<Organismo> organismos, Integer pageNumber,OficioRemision oficioRemision, Long tipoOficioRemision) throws Exception;

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
     * Obtiene los Oficios de Remisión de un determinado Estado y Entidad
     * @param idEstado
     * @param idOficina
     * @return
     * @throws Exception
     */
    public List<OficioRemision> getByEstadoOficina(int idEstado, Long idOficina) throws Exception;

    /**
     * Obtiene los Oficios de Remisión de un determinado Estado y Entidad
     * @param idEstado
     * @param idEntidad
     * @return
     * @throws Exception
     */
    public List<OficioRemision> getByEstadoEntidad(int idEstado, Long idEntidad) throws Exception;

    /**
     * Busca Registros de Salida que pertenecen a un OficioRemision
     * @param idOficioRemision
     * @return
     * @throws Exception
     */
    public List<RegistroSalida> getSalidasByOficioRemision(Long idOficioRemision) throws Exception;

    /**
     * Obtiene un OficioRemisionSir a partir del identificadorIntercambio del envío
     * @param identificadorIntercambio
     * @return
     * @throws Exception
     */
    public OficioRemision getByIdentificadorIntercambio(String identificadorIntercambio) throws Exception;

    /**
     * Modifica el Estado de un OficioRemision
     * @param idOficioRemision
     * @param estado
     * @throws Exception
     */
    public void modificarEstado(Long idOficioRemision, int estado) throws Exception;

    /**
     * Busca los Números de Registro Formateados de los Registros de Entrada que pertenecen a un OficioRemision
     * @param idOficioRemision
     * @return
     * @throws Exception
     */
    public List<String> getNumerosRegistroEntradaFormateadoByOficioRemision(Long idOficioRemision) throws Exception;

    /**
     * Busca los Números de Registro Formateados de los Registros de Salida que pertenecen a un OficioRemision
     * @param idOficioRemision
     * @return
     * @throws Exception
     */
    public List<String> getNumerosRegistroSalidaFormateadoByOficioRemision(Long idOficioRemision) throws Exception;

    /**
     * Elimina los Oficios de remisión de una Entidad
     * @param idEntidad
     * @return
     * @throws Exception
     */
    public Integer eliminarByEntidad(Long idEntidad) throws Exception;
}
