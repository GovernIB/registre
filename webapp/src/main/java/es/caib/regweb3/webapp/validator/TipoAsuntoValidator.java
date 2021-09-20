package es.caib.regweb3.webapp.validator;

import es.caib.regweb3.model.TipoAsunto;
import es.caib.regweb3.persistence.ejb.TipoAsuntoLocal;
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
 * Gestiona las Validaciones del formulario para crear o editar una {@link es.caib.regweb3.model.TipoAsunto}
 * @author earrivi
 * Date: 11/02/14
 */
@Component
public class TipoAsuntoValidator implements Validator {

    protected final Logger log = LoggerFactory.getLogger(getClass());

    @EJB(mappedName = "regweb3/TipoAsuntoEJB/local")
    public TipoAsuntoLocal tipoAsuntoEjb;

    @Override
    public boolean supports(Class<?> clazz) {
        return TipoAsunto.class.equals(clazz);
    }

    @Override
    public void validate(Object o, Errors errors) {

        TipoAsunto tipoAsunto = (TipoAsunto) o;

        ValidationUtils.rejectIfEmpty(errors, "codigo", "error.valor.requerido", "El camp nom és obligatori");

        
        for (Long idioma :  RegwebConstantes.IDIOMAS_UI) {
          ValidationUtils.rejectIfEmpty(errors, "traducciones[" + RegwebConstantes.CODIGO_BY_IDIOMA_ID.get(idioma) + "].nombre", "error.valor.requerido", "El camp nom és obligatori");
        }
        //ValidationUtils.rejectIfEmpty(errors, "traducciones[" + RegwebConstantes.IDIOMA_DEFAULT + "].nombre", "error.valor.requerido", "El camp nom és obligatori");
        //ValidationUtils.rejectIfEmpty(errors, "traducciones[" + RegwebConstantes.IDIOMA_CASTELLANO + "].nombre", "error.valor.requerido", "El camp nom és obligatori");

        // Identificador único
        try {
            if (tipoAsunto.getCodigo() != null && tipoAsunto.getCodigo().length() > 0) {

                if (tipoAsunto.getId() != null) {  // Se trata de una modificación

                    if (tipoAsuntoEjb.existeCodigoEdit(tipoAsunto.getCodigo(), tipoAsunto.getId(),tipoAsunto.getEntidad().getId())) {
                        errors.rejectValue("codigo", "error.identificador.existe", "L'identificador ja existeix");
                    }

                } else {

                    if (tipoAsuntoEjb.findByCodigoEntidad(tipoAsunto.getCodigo(),tipoAsunto.getEntidad().getId()) != null) {
                        errors.rejectValue("codigo", "error.identificador.existe", "L'identificador ja existeix");
                    }
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}
