package es.caib.regweb3.persistence.ejb;

import es.gob.ad.registros.sir.interService.exception.InterException;

import javax.ejb.Local;

/**
 * Created by DGMAD
 *
 * @author earrivi
 * Date: 21/10/22
 */
@Local
public interface LibSirLocal {

    String JNDI_NAME = "java:app/regweb3-persistence/LibSirEJB";

    /**
     *
     * @param registro
     * @param firmaRegistro
     */
    void recibirAsiento(String registro, String firmaRegistro) throws InterException;

    /**
     *
     * @param mensaje
     * @param firma
     * @throws InterException
     */
    void recibirMensajeControl(String mensaje, String firma) throws InterException;
}
