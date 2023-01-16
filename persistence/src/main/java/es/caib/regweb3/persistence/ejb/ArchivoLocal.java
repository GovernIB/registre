package es.caib.regweb3.persistence.ejb;


import es.caib.regweb3.model.Archivo;
import org.fundaciobit.genapp.common.i18n.I18NException;

import javax.ejb.Local;
import java.util.List;

/**
 * Created by Fundacio Bit
 *
 * @author earrivi
 * Date: 6/03/13
 */
@Local
public interface ArchivoLocal extends BaseEjb<Archivo, Long> {

    String JNDI_NAME = "java:app/regweb3-persistence/ArchivoEJB";

    /**
     * Obtiene los id's de todos los Archivos de la aplicación
     * @return
     * @throws I18NException
     */
    List<Long> getAllLigero() throws I18NException;

    /**
     *
     * @param archivo
     * @return
     * @throws I18NException
     */
    boolean borrarArchivo(Archivo archivo) throws I18NException;
}
