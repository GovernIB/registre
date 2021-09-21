package es.caib.regweb3.persistence.ejb;

import es.caib.regweb3.model.IRegistro;
import es.caib.regweb3.model.UsuarioEntidad;
import es.caib.regweb3.model.utils.AnexoFull;
import org.fundaciobit.genapp.common.i18n.I18NException;
import org.fundaciobit.genapp.common.i18n.I18NValidationException;

import javax.ejb.Local;

/**
 * Created by Fundació BIT.
 *
 * @author earrivi
 * Date: 22/06/16
 */
@Local
public interface JustificanteLocal {

    String JNDI_NAME = "java:app/regweb3-persistence/JustificanteEJB";

    /**
     * @param usuarioEntidad
     * @param registro
     * @param tipoRegistro
     * @param idioma
     * @return
     * @throws I18NException
     * @throws I18NValidationException
     */
    AnexoFull crearJustificante(UsuarioEntidad usuarioEntidad, IRegistro registro, Long tipoRegistro, String idioma) throws I18NException, I18NValidationException;

    /**
     * @param usuarioEntidad
     * @param registro
     * @param tipoRegistro
     * @param idioma
     * @return
     * @throws I18NException
     * @throws I18NValidationException
     */
    AnexoFull crearJustificanteWS(UsuarioEntidad usuarioEntidad, IRegistro registro, Long tipoRegistro, String idioma) throws I18NException, I18NValidationException;
}

