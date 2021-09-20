package es.caib.regweb3.webapp.validator;

import es.caib.regweb3.model.Plugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;



/**
 * Created by Fundació BIT.
 * Gestiona las Validaciones del formulario para crear o editar una {@link Plugin}
 *
 * @author earrivi
 *         Date: 05/05/15
 */
@Component
public class PluginValidator implements Validator {

    protected final Logger log = LoggerFactory.getLogger(getClass());


    @Override
    public boolean supports(Class<?> clazz) {
        return Plugin.class.equals(clazz);
    }

    @Override
    public void validate(Object o, Errors errors) {

        Plugin plugin = (Plugin) o;

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "clase", "error.valor.requerido", "El camp és obligatori");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "nombre", "error.valor.requerido", "El camp és obligatori");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "descripcion", "error.valor.requerido", "El camp és obligatori");

    }
}
