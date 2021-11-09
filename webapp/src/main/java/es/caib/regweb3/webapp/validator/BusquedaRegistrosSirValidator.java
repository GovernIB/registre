package es.caib.regweb3.webapp.validator;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import es.caib.regweb3.webapp.form.RangoFechasBusqueda;

/**
 * Created by Fundació BIT.
 * Gestiona las Validaciones del formulario para crear o editar una {@link es.caib.regweb3.model.Entidad}
 * @author earrivi
 * Date: 11/02/14
 */
@Component
public class BusquedaRegistrosSirValidator implements Validator {


    protected final Logger log = Logger.getLogger(getClass());

    @Override
    public boolean supports(Class<?> clazz) {
        return RangoFechasBusqueda.class.equals(clazz);
    }

    @Override
    public void validate(Object o, Errors errors) {
    	RangoFechasBusqueda rangoFechasForm = (RangoFechasBusqueda)o;
        
        // Tamaño campo Libro
        if(rangoFechasForm.getFechaInicioImportacion() == null) {
            errors.rejectValue("fechaInicioImportacion", null, "El camp és obligatori");
        }

    }
}
