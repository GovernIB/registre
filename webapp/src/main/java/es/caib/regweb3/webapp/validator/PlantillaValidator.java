package es.caib.regweb3.webapp.validator;

import es.caib.regweb3.model.Plantilla;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

/**
 * Created by Fundació BIT.
 * Gestiona las Validaciones del formulario para crear o editar una {@link Plantilla}
 *
 * @author earrivi
 *         Date: 11/02/14
 */
@Component
public class PlantillaValidator implements Validator {

    protected final Logger log = Logger.getLogger(getClass());

    @Override
    public boolean supports(Class<?> clazz) {
        return Plantilla.class.equals(clazz);
    }

    @Override
    public void validate(Object o, Errors errors) {


        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "nombre", "error.valor.requerido", "El camp és obligatori");


    }


}
