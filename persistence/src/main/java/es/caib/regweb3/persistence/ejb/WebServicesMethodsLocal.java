package es.caib.regweb3.persistence.ejb;

import es.caib.regweb3.model.PreRegistro;
import es.caib.regweb3.persistence.utils.Respuesta;

import javax.ejb.Local;

/**
 * Created by Fundacio Bit
 *
 * @author earrivi
 */
@Local
public interface WebServicesMethodsLocal {

    public Respuesta<PreRegistro> crearPreRegistro(String sicres3) throws Exception;
}
