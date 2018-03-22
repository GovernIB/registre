package es.caib.regweb3.persistence.ejb;

import es.caib.regweb3.model.IRegistro;
import es.caib.regweb3.model.UsuarioEntidad;
import es.caib.regweb3.model.utils.AnexoFull;
import org.fundaciobit.genapp.common.i18n.I18NException;
import org.fundaciobit.genapp.common.i18n.I18NValidationException;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Local;

/**
 * Created by Fundació BIT.
 *
 * @author earrivi
 * Date: 22/06/16
 */
@Local
@RolesAllowed({"RWE_SUPERADMIN", "RWE_ADMIN", "RWE_USUARI"})
public interface JustificanteLocal {

    AnexoFull crearJustificante(UsuarioEntidad usuarioEntidad, IRegistro registro, String tipoRegistro, String idioma) throws I18NException, I18NValidationException;
}

