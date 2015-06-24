package es.caib.regweb3.persistence.ejb;


import es.caib.dir3caib.ws.api.oficina.OficinaTF;
import es.caib.dir3caib.ws.api.unidad.UnidadTF;
import es.caib.regweb3.model.Organismo;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Local;
import java.sql.Timestamp;

/**
 * Created by Fundacio Bit
 *
 * @author earrivi
 * Date: 6/03/13
 */
@Local
@RolesAllowed({"RWE_ADMIN"})
public interface SincronizadorDir3Local {

  public int sincronizarActualizar(Long entidadId, Timestamp fechaActualizacion, Timestamp fechaSincronizacion) throws Exception;
  public Organismo sincronizarOrganismo(UnidadTF unidadTF, Long idEntidad) throws Exception;
  public void sincronizarOficina(OficinaTF oficinaTF) throws Exception;
  public void sincronizarHistoricosOrganismo(Organismo organismo, UnidadTF unidadTF) throws Exception;
  //TODO borrar, solo se emplea para testear
  //public void obtenerOrganismosDestinatarios(String codigo) throws Exception;
}
