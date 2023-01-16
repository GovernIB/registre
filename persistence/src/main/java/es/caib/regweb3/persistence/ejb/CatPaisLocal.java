package es.caib.regweb3.persistence.ejb;

import es.caib.regweb3.model.CatPais;
import org.fundaciobit.genapp.common.i18n.I18NException;

import javax.ejb.Local;

/**
 * Created by Fundació BIT.
 *
 * @author earrivi
 * Date: 16/01/14
 */
@Local
public interface CatPaisLocal extends BaseEjb<CatPais, Long> {

  String JNDI_NAME = "java:app/regweb3-persistence/CatPaisEJB";

  CatPais findByCodigo(Long codigo) throws I18NException;

}

