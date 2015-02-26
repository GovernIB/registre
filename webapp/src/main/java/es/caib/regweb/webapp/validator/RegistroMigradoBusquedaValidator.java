package es.caib.regweb.webapp.validator;

import es.caib.regweb.webapp.form.RegistroMigradoBusqueda;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by Fundació BIT.
 * Gestiona las Validaciones del formulario para buscar Registros Migrados
 * @author jpernia
 * Date: 11/11/14
 */
@Component
public class RegistroMigradoBusquedaValidator implements Validator {

    protected final Logger log = Logger.getLogger(getClass());


    @Override
    public boolean supports(Class<?> clazz) {
        return RegistroMigradoBusqueda.class.equals(clazz);
    }

    @Override
    public void validate(Object o, Errors errors) {

        RegistroMigradoBusqueda busqueda = (RegistroMigradoBusqueda)o;

        if(busqueda.getFechaInicio() != null && busqueda.getFechaFin() != null){

            Calendar fechaInicio = new GregorianCalendar();
            Calendar fechaFin = new GregorianCalendar();

            fechaInicio.setTime(busqueda.getFechaInicio());
            fechaFin.setTime(busqueda.getFechaFin());

            if(fechaFin.before(fechaInicio)){
                errors.rejectValue("fechaFin","error.fecha.anterior","La data de finalització ha de ser major que la d'inici");
            }
        }

    }

}