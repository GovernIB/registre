package es.caib.regweb3.webapp.validator;

import es.caib.regweb3.webapp.form.ModeloReciboForm;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

/**
 * Created by Fundació BIT.
 * Gestiona las Validaciones del formulario para crear o editar una {@link es.caib.regweb3.model.ModeloRecibo}
 * @author earrivi
 * @author anadal
 * Date: 11/02/14
 */
@Component
public class ModeloReciboValidator implements Validator {

    protected final Logger log = Logger.getLogger(getClass());


    @Override
    public boolean supports(Class<?> clazz) {
        return ModeloReciboForm.class.equals(clazz);
    }

    @Override
    public void validate(Object o, Errors errors) {

        ModeloReciboForm modeloReciboForm = (ModeloReciboForm) o;

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "modeloRecibo.nombre", "error.valor.requerido", "El camp és obligatori");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "modeloRecibo.entidad", "error.valor.requerido", "El camp és obligatori");

        if(modeloReciboForm.getModeloRecibo().getId()==null){
            ValidationUtils.rejectIfEmptyOrWhitespace(errors, "modelo", "error.valor.requerido", "El camp és obligatori");
        }


    }


}
