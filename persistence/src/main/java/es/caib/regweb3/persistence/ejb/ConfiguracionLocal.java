package es.caib.regweb3.persistence.ejb;

import es.caib.regweb3.model.Configuracion;

import javax.ejb.Local;

/**
 * Created by Fundació BIT.
 *
 * @author jpernia
 * Date: 07/07/15
 */
@Local
public interface ConfiguracionLocal extends BaseEjb<Configuracion, Long> {

    String JNDI_NAME = "java:app/regweb3-persistence/ConfiguracionEJB";


}