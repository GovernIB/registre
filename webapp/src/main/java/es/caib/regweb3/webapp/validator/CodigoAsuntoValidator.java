package es.caib.regweb3.webapp.validator;

import es.caib.regweb3.model.CodigoAsunto;
import es.caib.regweb3.persistence.ejb.CodigoAsuntoLocal;
import es.caib.regweb3.utils.RegwebConstantes;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import javax.ejb.EJB;

/**
 * Created by Fundació BIT.
 * Gestiona las Validaciones del formulario para crear o editar una {@link CodigoAsunto}
 * @author earrivi
 * Date: 11/02/14
 */
@Component
public class CodigoAsuntoValidator implements Validator {

    protected final Logger log = Logger.getLogger(getClass());

    @EJB(mappedName = "regweb3/CodigoAsuntoEJB/local")
    public CodigoAsuntoLocal codigoAsuntoEjb;

    @Override
    public boolean supports(Class<?> clazz) {
        return CodigoAsunto.class.equals(clazz);
    }

    @Override
    public void validate(Object o, Errors errors) {

        CodigoAsunto codigoAsunto = (CodigoAsunto) o;

        ValidationUtils.rejectIfEmpty(errors, "codigo", "error.valor.requerido", "El camp nom és obligatori");

        
        for (Long idioma :  RegwebConstantes.IDIOMAS_UI) {
          ValidationUtils.rejectIfEmpty(errors, "traducciones[" + RegwebConstantes.CODIGO_BY_IDIOMA_ID.get(idioma) + "].nombre", "error.valor.requerido", "El camp nom és obligatori");
        }

        // Identificador único
        try {
            if (codigoAsunto.getCodigo() != null && codigoAsunto.getCodigo().length() > 0) {

                if (codigoAsunto.getId() != null) {  // Se trata de una modificación

                    if (codigoAsuntoEjb.existeCodigoEdit(codigoAsunto.getCodigo(), codigoAsunto.getId(),codigoAsunto.getEntidad().getId())) {
                        errors.rejectValue("codigo", "error.identificador.existe", "L'identificador ja existeix");
                    }

                } else {

                    if (codigoAsuntoEjb.findByCodigoEntidad(codigoAsunto.getCodigo(),codigoAsunto.getEntidad().getId()) != null) {
                        errors.rejectValue("codigo", "error.identificador.existe", "L'identificador ja existeix");
                    }
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}
