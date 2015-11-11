package es.caib.regweb3.persistence.ejb;

import es.caib.regweb3.model.RegistroLopdMigrado;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Local;
import java.util.Date;
import java.util.List;

/**
 * Created by Fundació BIT.
 *
 * @author jpernia
 * Date: 19/11/14
 */
@Local
@RolesAllowed({"RWE_USUARI"})
public interface RegistroLopdMigradoLocal extends BaseEjb<RegistroLopdMigrado, Long> {

    /**
     * Devuelve los registros Lopd de un Registro Migrado concreto, pasando la Accion
     * @param numRegistro
     * @param accion
     * @return
     * @throws Exception
     */
    public List<RegistroLopdMigrado> getByRegistroMigrado(Long numRegistro, String accion) throws Exception;

    /**
     * Devuelve el registro Lopd de un Registro Migrado concreto
     * @param numRegistro
     * @param accion
     * @return
     * @throws Exception
     */
    public RegistroLopdMigrado getCreacion(Long numRegistro, String accion) throws Exception;


    /**
     * Devuelve los registros Lopd de un Registro Migrado que ha realizado un Usuario entre dos fechas, pasando la Accion
     * @param dataInici
     * @param dataFi
     * @param usuario
     * @param accion
     * @return
     * @throws Exception
     */
    public List<RegistroLopdMigrado> getByUsuario(Date dataInici, Date dataFi, String usuario, String accion) throws Exception;

    /**
     * Elimina todos los Registros LOPD de una entidad
     * @param idEntidad
     * @return
     * @throws Exception
     */
    public Integer eliminarByEntidad(Long idEntidad) throws Exception;

}