package es.caib.regweb3.persistence.ejb;

import es.caib.regweb3.model.Interesado;
import es.caib.regweb3.model.RegistroSalida;
import es.caib.regweb3.model.UsuarioEntidad;
import es.caib.regweb3.model.utils.AnexoFull;
import org.fundaciobit.genapp.common.i18n.I18NException;
import org.fundaciobit.genapp.common.i18n.I18NValidationException;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Local;
import java.util.List;

/**
 * Created by Fundació BIT.
 *
 * @author mgonzalez
 * Date: 06/03/2019
 */
@Local
@RolesAllowed({"RWE_SUPERADMIN", "RWE_ADMIN", "RWE_USUARI","RWE_WS_ENTRADA","RWE_WS_SALIDA"})
public interface AsientoRegistralLocal {

    /**
     * Método que crea un registro de Salida a partir de un Asiento Registral.
     * @param registroSalida
     * @param usuarioEntidad
     * @param interesados
     * @param anexos
     * @return
     * @throws Exception
     * @throws I18NException
     * @throws I18NValidationException
     */
    RegistroSalida registrarSalida(RegistroSalida registroSalida,
                                   UsuarioEntidad usuarioEntidad, List<Interesado> interesados, List<AnexoFull> anexos)
       throws Exception, I18NException, I18NValidationException;
}

