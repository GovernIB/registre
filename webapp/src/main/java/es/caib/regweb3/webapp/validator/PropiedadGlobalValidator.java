package es.caib.regweb3.webapp.validator;

import es.caib.regweb3.model.Entidad;
import es.caib.regweb3.model.PropiedadGlobal;
import es.caib.regweb3.persistence.ejb.PropiedadGlobalLocal;
import es.caib.regweb3.utils.RegwebConstantes;
import es.caib.regweb3.utils.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.ejb.EJB;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Created by Fundació BIT.
 * Gestiona las Validaciones del formulario para crear o editar una {@link es.caib.regweb3.model.PropiedadGlobal}
 *
 * @author earrivi
 *         Date: 05/05/15
 */
@Component
public class PropiedadGlobalValidator implements Validator {

    protected final Logger log = Logger.getLogger(getClass());

    @EJB(mappedName = "regweb3/PropiedadGlobalEJB/local")
    public PropiedadGlobalLocal propiedadGlobalEjb;

    @Override
    public boolean supports(Class<?> clazz) {
        return PropiedadGlobal.class.equals(clazz);
    }

    @Override
    public void validate(Object o, Errors errors) {

        PropiedadGlobal propiedadGlobal = (PropiedadGlobal) o;

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "clave", "error.valor.requerido", "El camp és obligatori");

        // Comprobamos la duplicidad de la Clave
        if (StringUtils.isNotEmpty(propiedadGlobal.getClave())) {

            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
            HttpSession session = request.getSession();
            Entidad entidadActiva = (Entidad) session.getAttribute(RegwebConstantes.SESSION_ENTIDAD);

            try {
                Long idEntidad = null;
                if (entidadActiva != null) {// PropiedadGlobal de REGWEB3
                    idEntidad = entidadActiva.getId();
                }

                PropiedadGlobal p = null;

                if (propiedadGlobal.getId() == null) { // Nueva propiedad
                    p = propiedadGlobalEjb.findByClaveEntidad(propiedadGlobal.getClave(), idEntidad, null);
                } else { // Editar propiedad
                    p = propiedadGlobalEjb.findByClaveEntidad(propiedadGlobal.getClave(), idEntidad, propiedadGlobal.getId());
                }


                if (p != null) {
                    errors.rejectValue("clave", "error.clave.existe", "Ja existeix aquesta Clau al sistema");
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }


    }
}
