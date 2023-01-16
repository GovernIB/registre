package es.caib.regweb3.persistence.ejb;

import es.caib.regweb3.model.RegistroLopdMigrado;
import org.fundaciobit.genapp.common.i18n.I18NException;

import javax.ejb.Local;
import java.util.List;

/**
 * Created by Fundació BIT.
 *
 * @author jpernia
 * Date: 19/11/14
 */
@Local
public interface RegistroLopdMigradoLocal extends BaseEjb<RegistroLopdMigrado, Long> {

    String JNDI_NAME = "java:app/regweb3-persistence/RegistroLopdMigradoEJB";


    /**
     * Devuelve los registros Lopd de un Registro Migrado concreto, pasando la Accion
     *
     * @param numRegistro
     * @param accion
     * @return
     * @throws I18NException
     */
    List<RegistroLopdMigrado> getByRegistroMigrado(Long numRegistro, String accion) throws I18NException;

    /**
     * Devuelve el registro Lopd de un Registro Migrado concreto
     *
     * @param numRegistro
     * @param accion
     * @return
     * @throws I18NException
     */
    RegistroLopdMigrado getCreacion(Long numRegistro, String accion) throws I18NException;

    /**
     * Elimina todos los Registros LOPD de una entidad
     *
     * @param idEntidad
     * @return
     * @throws I18NException
     */
    Integer eliminarByEntidad(Long idEntidad) throws I18NException;

}