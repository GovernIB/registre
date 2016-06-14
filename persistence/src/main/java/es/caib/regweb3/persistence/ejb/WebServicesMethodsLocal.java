package es.caib.regweb3.persistence.ejb;

import es.caib.regweb3.model.CatLocalidad;
import es.caib.regweb3.model.CatPais;
import es.caib.regweb3.model.CatProvincia;
import es.caib.regweb3.model.PreRegistro;

import javax.ejb.Local;

/**
 * Created by Fundacio Bit
 *
 * @author earrivi
 */
@Local
public interface WebServicesMethodsLocal {

    public PreRegistro crearPreRegistro(PreRegistro preRegistro) throws Exception;

    public CatPais findByCodigoPais(Long codigo) throws Exception;

    public CatProvincia findByCodigoProvincia(Long codigo) throws Exception;

    public CatLocalidad findByLocalidadProvincia(Long codigoLocalidad, Long codigoProvincia) throws Exception;
}
