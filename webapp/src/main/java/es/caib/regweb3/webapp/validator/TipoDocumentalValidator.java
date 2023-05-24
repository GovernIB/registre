package es.caib.regweb3.webapp.validator;

import es.caib.regweb3.model.TipoDocumental;
import es.caib.regweb3.persistence.ejb.TipoDocumentalLocal;
import es.caib.regweb3.utils.RegwebConstantes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import javax.ejb.EJB;

/**
 * Created by Fundació BIT.
 * Gestiona las Validaciones del formulario para crear o editar una {@link es.caib.regweb3.model.TipoDocumental}
 *
 * @author earrivi
 * Date: 11/02/14
 */
@Component
public class TipoDocumentalValidator implements Validator {

    protected final Logger log = LoggerFactory.getLogger(getClass());

    @EJB(mappedName = TipoDocumentalLocal.JNDI_NAME)
    private TipoDocumentalLocal tipoDocumentalEjb;

    @Override
    public boolean supports(Class<?> clazz) {
        return TipoDocumental.class.equals(clazz);
    }

    @Override
    public void validate(Object o, Errors errors) {

        TipoDocumental tipoDocumental = (TipoDocumental) o;

        for (Long idioma : RegwebConstantes.IDIOMAS_UI) {
            ValidationUtils.rejectIfEmpty(errors, "traducciones[" + RegwebConstantes.CODIGO_BY_IDIOMA_ID.get(idioma) + "].nombre", "error.valor.requerido", "El camp nom és obligatori");
        }

        try {
            if (tipoDocumental.getCodigoNTI() != null && tipoDocumental.getCodigoNTI().length() > 0) {

                if (tipoDocumental.getId() != null) {  // Se trata de una modificación

                    if (tipoDocumentalEjb.existeCodigoEdit(tipoDocumental.getCodigoNTI(), tipoDocumental.getId(), tipoDocumental.getEntidad().getId())) {
                        errors.rejectValue("codigoNTI", "error.codigo.existe", "El codi ja existeix");
                    }

                } else {

                    if (tipoDocumentalEjb.findByCodigoEntidad(tipoDocumental.getCodigoNTI(), tipoDocumental.getEntidad().getId()) != null) {
                        errors.rejectValue("codigoNTI", "error.codigo.existe", "El codi ja existeix");
                    }
                }

            } else {
                errors.rejectValue("codigoNTI", "error.codigo.obligatorio", "El codi NTI és obligatori");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
