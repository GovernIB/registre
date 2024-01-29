package es.caib.regweb3.persistence.ejb;

import es.caib.regweb3.model.CatIsla;
import es.caib.regweb3.model.CatProvincia;
import org.fundaciobit.genapp.common.i18n.I18NException;

import javax.ejb.Local;
import java.util.List;

/**
 * Created by DGSMAD.
 *
 * @author earrivi
 * Date: 02/01/24
 */
@Local
public interface CatIslaLocal extends BaseEjb<CatIsla, Long> {

  String JNDI_NAME = "java:app/regweb3-persistence/CatIslaEJB";

  CatIsla findByCodigo(Long codigo) throws I18NException;

  List<CatIsla> getByProvincia(CatProvincia provincia) throws I18NException;

}

