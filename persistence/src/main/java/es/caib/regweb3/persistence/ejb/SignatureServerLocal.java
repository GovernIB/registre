package es.caib.regweb3.persistence.ejb;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Local;
import java.io.File;

/**
 * Created by jpernia on 04/04/2017.
 */
@Local
@RolesAllowed({"RWE_USUARI"})
public interface SignatureServerLocal {

    /**Método que genera la Firma de un File para una Entidad en concreto
     * @param pdfsource
     * @param languageUI
     * @param idEntidadActiva
     * @return
     * @throws Exception
     */
    public File signFile(File pdfsource, String languageUI, Long idEntidadActiva) throws Exception;

}
