package es.caib.regweb3.persistence.ejb;


import es.caib.regweb3.model.AnexoSir;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Local;

/**
 * Created by Fundació BIT.
 *
 * @author earrivi
 * Date: 16/01/14
 */
@Local
@RolesAllowed({"RWE_SUPERADMIN","RWE_ADMIN","RWE_USUARI"})
public interface AnexoSirLocal extends BaseEjb<AnexoSir, Long> {

    /**
     * Elimina los Archivos del sistema de archivos de los RegistrosSir Aceptados, Rechazados o Reenviados
     * @param idEntidad
     * @param numElementos numero máximo de archivos que se van a purgar en esta iteracion
     * @throws Exception
     */
    int purgarArchivos(Long idEntidad, Integer numElementos) throws Exception;

    /**
     * Elimina los AnexoSir de una Entidad
     * @param idEntidad
     * @return
     * @throws Exception
     */
    Integer eliminarByEntidad(Long idEntidad) throws Exception;
}

