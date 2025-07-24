package es.caib.regweb3.persistence.ejb;

import java.io.UnsupportedEncodingException;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Local;

import org.fundaciobit.genapp.common.i18n.I18NException;
import org.fundaciobit.genapp.common.i18n.I18NValidationException;

import es.caib.regweb3.model.Entidad;
import es.caib.regweb3.model.IRegistro;
import es.caib.regweb3.model.UsuarioEntidad;
import es.caib.regweb3.model.utils.AnexoFull;

/**
 * Created by Fundació BIT.
 *
 * @author earrivi
 * Date: 22/06/16
 */
@Local
@RolesAllowed({"RWE_SUPERADMIN", "RWE_ADMIN", "RWE_USUARI", "RWE_WS_ENTRADA", "RWE_WS_SALIDA", "RWE_WS_CIUDADANO"})
public interface JustificanteLocal {

    /**
     * Crea el Justificante en función del plugin definido
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
     * Crea el Justificante en Filesystem si la Custodia en diferido está activa, sino lo hace normalmente
     * @param usuarioEntidad
     * @param registro
     * @param tipoRegistro
     * @param idioma
     * @return
     * @throws I18NException
     * @throws I18NValidationException
     */
    AnexoFull crearJustificanteWS(UsuarioEntidad usuarioEntidad, IRegistro registro, Long tipoRegistro, String idioma) throws I18NException, I18NValidationException;

    /**
     * Envía el justificante del registro por correo al interesado y representante
     * @param registro
     * @throws I18NException 
     * @throws UnsupportedEncodingException 
     * @throws Exception 
     */
	void enviarJustificantePorEmail(Entidad entidadActiva, IRegistro registro) throws I18NException, Exception;
}

