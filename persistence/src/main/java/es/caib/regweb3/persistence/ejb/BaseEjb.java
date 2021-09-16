package es.caib.regweb3.persistence.ejb;

import java.io.Serializable;
import java.util.List;

import javax.annotation.security.RolesAllowed;

/**
 * Created by Fundació BIT.
 * @author earrivi
 * Date: 16/01/14
 */
@RolesAllowed({"RWE_SUPERADMIN","RWE_ADMIN","RWE_USUARI"})
public interface BaseEjb<T extends Serializable, E> {

    /**
     * Crear registro
     *
     * @param transientInstance
     * @throws Exception
     */
    T persist(T transientInstance) throws Exception;

    /**
     *  Actualizar registro
     * @param instance
     * @return
     * @throws Exception
     */
    T merge(T instance) throws Exception;

    /**
     *  Eliminar registro
     * @param persistentInstance
     * @throws Exception
     */
    void remove(T persistentInstance) throws Exception;

    /**
     *  Obtener registro por id
     * @param id
     * @return
     * @throws Exception
     */
    T findById(E id) throws Exception;

    /**
     *  Obtener una referencia al registro por id
     * @param id
     * @return
     * @throws Exception
     */
    T getReference(E id) throws Exception;

    /**
     *  Obtener todos los registros
     * @return
     * @throws Exception
     */
    List<T> getAll() throws Exception;

    /**
     * Obtiene el total de registros para la paginacion
     * @return
     * @throws Exception
     */
    Long getTotal() throws Exception;

    /**
     * Obtiene X valores comenzando en la posicion pasada por parametro
     * @param inicio
     * @return
     * @throws Exception
     */
    List<T> getPagination(int inicio) throws Exception;
}
