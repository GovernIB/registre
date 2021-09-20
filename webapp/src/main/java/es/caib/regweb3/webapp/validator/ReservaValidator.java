package es.caib.regweb3.webapp.validator;

import es.caib.regweb3.model.RegistroEntrada;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

/**
 * Created by Fundació BIT.
 * Gestiona las Validaciones del formulario para realizar una reserva de un {@link es.caib.regweb3.model.RegistroEntrada}
 * @author earrivi
 * Date: 11/02/14
 */
@Component
public class ReservaValidator implements Validator {

    protected final Logger log = LoggerFactory.getLogger(getClass());

    @Override
    public boolean supports(Class<?> clazz) {
        return RegistroEntrada.class.equals(clazz);
    }

    @Override
    public void validate(Object o, Errors errors) {

        //RegistroEntrada registroEntrada = (RegistroEntrada)o;

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "registroDetalle.reserva", "error.valor.requerido", "El camp és obligatori");

    }

}
