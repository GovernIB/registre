package es.caib.regweb3.persistence.ejb;

import es.caib.regweb3.persistence.utils.RespuestaRecepcionSir;
import es.caib.regweb3.sir.core.utils.FicheroIntercambio;

import javax.ejb.Local;

/**
 * Created by Fundació BIT.
 *
 * @author earrivi
 * Date: 22/06/16
 */
@Local
public interface FicheroIntercambioLocal {

    String JNDI_NAME = "java:app/regweb3-persistence/FicheroIntercambioEJB";

    /**
     * Recibe un fichero de intercambio en formato SICRES3 desde un nodo distribuido
     *
     * @param ficheroIntercambio
     * @throws Exception
     */
    RespuestaRecepcionSir procesarFicheroIntercambio(FicheroIntercambio ficheroIntercambio) throws Exception;

}

