package es.caib.regweb3.persistence.ejb;

import es.caib.regweb3.model.ModificacionLopdMigrado;
import org.fundaciobit.genapp.common.i18n.I18NException;

import javax.ejb.Local;
import java.util.List;

/**
 * Created by Fundació BIT.
 *
 * @author jpernia
 * Date: 09/12/14
 */
@Local
public interface ModificacionLopdMigradoLocal extends BaseEjb<ModificacionLopdMigrado, Long> {

    String JNDI_NAME = "java:app/regweb3-persistence/ModificacionLopdMigradoEJB";


    /**
     * Devuelve el registro Lopd de un Registro Migrado concreto
     *
     * @param numRegistroMigrado
     * @return
     * @throws I18NException
     */
    List<ModificacionLopdMigrado> getByRegistroMigrado(Long numRegistroMigrado) throws I18NException;

}
