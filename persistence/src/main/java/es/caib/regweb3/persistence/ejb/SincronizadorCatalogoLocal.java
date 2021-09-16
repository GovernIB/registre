package es.caib.regweb3.persistence.ejb;


import es.caib.regweb3.model.Descarga;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Local;

/**
 * Created by Fundacio Bit
 *
 * @author earrivi
 * Date: 6/03/13
 */
@Local
public interface SincronizadorCatalogoLocal {

  Descarga sincronizarCatalogo() throws Exception;
  Descarga actualizarCatalogo() throws Exception;
}
