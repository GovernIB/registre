package es.caib.regweb3.persistence.ejb;


import javax.ejb.Local;
import java.sql.Timestamp;

/**
 * Created by Fundacio Bit
 *
 * @author earrivi
 * Date: 6/03/13
 */
@Local
public interface SincronizadorDir3Local {


    String JNDI_NAME = "java:app/regweb3-persistence/SincronizadorDir3EJB";


    /**
     * @param entidadId
     * @param fechaActualizacion
     * @param fechaSincronizacion
     * @return
     * @throws Exception
     */
    int sincronizarActualizar(Long entidadId, Timestamp fechaActualizacion, Timestamp fechaSincronizacion) throws Exception;

}
