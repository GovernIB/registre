package es.caib.regweb.persistence.ejb;

import es.caib.regweb.model.PreRegistro;
import es.caib.regweb.model.RegistroEntrada;
import es.caib.regweb.persistence.utils.Respuesta;
import es.caib.regweb.persistence.utils.sir.FicheroIntercambioSICRES3;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Local;

/**
 * Created by Fundacio Bit
 *
 * @author earrivi
 */
@Local
@RolesAllowed({"RWE_SUPERADMIN","RWE_ADMIN","RWE_USUARI"})
public interface SirLocal {

    public FicheroIntercambioSICRES3 writeFicheroIntercambioSICRES3(RegistroEntrada re) throws Exception;

    public Respuesta<PreRegistro> readFicheroIntercambioSICRES3(String sicres3) throws Exception;

}
