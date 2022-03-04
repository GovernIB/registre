package es.caib.regweb3.webapp.validator;

import es.caib.regweb3.webapp.form.ModeloOficioRemisionForm;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

/**
 * Created by Fundació BIT.
 * Gestiona las Validaciones del formulario para crear o editar una {@link es.caib.regweb3.model.ModeloOficioRemision}
 * @author jpernia
 * Date: 2/09/14
 */
@Component
public class ModeloOficioRemisionValidator implements Validator {

    protected final Logger log = Logger.getLogger(getClass());

    @Override
    public boolean supports(Class<?> clazz) {
        return ModeloOficioRemisionForm.class.equals(clazz);
    }

    @Override
    public void validate(Object o, Errors errors) {

        ModeloOficioRemisionForm modeloOficioRemisionForm = (ModeloOficioRemisionForm) o;

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "modeloOficioRemision.nombre", "error.valor.requerido", "El camp és obligatori");

        if(modeloOficioRemisionForm.getModeloOficioRemision().getId()==null) {
            ValidationUtils.rejectIfEmptyOrWhitespace(errors, "modelo", "error.valor.requerido", "El camp és obligatori");
        }
    }
}