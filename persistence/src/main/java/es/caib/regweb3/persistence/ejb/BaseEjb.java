package es.caib.regweb3.persistence.ejb;

import org.fundaciobit.genapp.common.i18n.I18NException;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Fundació BIT.
 * @author earrivi
 * Date: 16/01/14
 */
public interface BaseEjb<T extends Serializable, E> {

    /**
     * Crear registro
     *
     * @param transientInstance
     * @throws I18NException
     */
    T persist(T transientInstance) throws I18NException;

    /**
     *  Actualizar registro
     * @param instance
     * @return
     * @throws I18NException
     */
    T merge(T instance) throws I18NException;

    /**
     *  Eliminar registro
     * @param persistentInstance
     * @throws I18NException
     */
    void remove(T persistentInstance) throws I18NException;

    /**
     *  Obtener registro por id
     * @param id
     * @return
     * @throws I18NException
     */
    T findById(E id) throws I18NException;

    /**
     *  Obtener una referencia al registro por id
     * @param id
     * @return
     * @throws I18NException
     */
    T getReference(E id) throws I18NException;

    /**
     *  Obtener todos los registros
     * @return
     * @throws I18NException
     */
    List<T> getAll() throws I18NException;

    /**
     * Obtiene el total de registros para la paginacion
     * @return
     * @throws I18NException
     */
    Long getTotal() throws I18NException;

    /**
     * Obtiene X valores comenzando en la posicion pasada por parametro
     * @param inicio
     * @return
     * @throws I18NException
     */
    List<T> getPagination(int inicio) throws I18NException;
}
