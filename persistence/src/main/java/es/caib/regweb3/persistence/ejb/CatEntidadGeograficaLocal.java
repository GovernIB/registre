package es.caib.regweb3.persistence.ejb;

import es.caib.regweb3.model.CatEntidadGeografica;
import org.fundaciobit.genapp.common.i18n.I18NException;

import javax.ejb.Local;

/**
 * Created by Fundació BIT.
 *
 * @author earrivi
 * Date: 16/01/14
 */
@Local
public interface CatEntidadGeograficaLocal extends BaseEjb<CatEntidadGeografica, Long> {

  String JNDI_NAME = "java:app/regweb3-persistence/CatEntidadGeograficaEJB";

  /**
       * Retorna los {@link es.caib.regweb3.model.CatEntidadGeografica} a partir de un código
       * @param codigo
       * @return
       * @throws I18NException
       */
  CatEntidadGeografica findByCodigo(String codigo) throws I18NException;
}

