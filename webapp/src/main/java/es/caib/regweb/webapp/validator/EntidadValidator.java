package es.caib.regweb.webapp.validator;

import es.caib.regweb.model.Rol;
import es.caib.regweb.persistence.ejb.EntidadLocal;
import es.caib.regweb.utils.RegwebConstantes;
import es.caib.regweb.webapp.form.EntidadForm;
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
 * Gestiona las Validaciones del formulario para crear o editar una {@link es.caib.regweb.model.Entidad}
 * @author earrivi
 * Date: 11/02/14
 */
@Component
public class EntidadValidator implements Validator {


    protected final Logger log = Logger.getLogger(getClass());

    @EJB(mappedName = "regweb/EntidadEJB/local")
    public EntidadLocal entidadEjb;

    @Override
    public boolean supports(Class<?> clazz) {
        return EntidadForm.class.equals(clazz);
    }

    @Override
    public void validate(Object o, Errors errors) {

        EntidadForm entidad = (EntidadForm)o;

        HttpServletRequest request =
                ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
                        .getRequest();

        HttpSession session = request.getSession();
        Rol rolActivo = (Rol) session.getAttribute(RegwebConstantes.SESSION_ROL);

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "entidad.nombre", "error.valor.requerido", "El camp és obligatori");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "entidad.descripcion", "error.valor.requerido", "El camp és obligatori");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "entidad.codigoDir3", "error.valor.requerido", "El camp és obligatori");

        if(rolActivo.getNombre().equals(RegwebConstantes.ROL_ADMIN)){
            ValidationUtils.rejectIfEmptyOrWhitespace(errors, "entidad.colorMenu", "error.valor.requerido", "El camp és obligatori");
            ValidationUtils.rejectIfEmptyOrWhitespace(errors, "entidad.sello", "error.valor.requerido", "El camp Format Segell és obligatori");
            ValidationUtils.rejectIfEmptyOrWhitespace(errors, "entidad.numRegistro", "error.valor.requerido", "El camp Format Número Registre és obligatori");
        }

        /*Validación del campo único CodigoDir3*/
       if(entidad.getEntidad().getCodigoDir3() != null && entidad.getEntidad().getCodigoDir3().length() > 0){
            try {
                if(entidad.getEntidad().getId() != null){ // Es una modificación

                    if(entidadEjb.existeCodigoDir3Edit(entidad.getEntidad().getCodigoDir3(),entidad.getEntidad().getId())){
                        errors.rejectValue("entidad.codigoDir3","entidad.codigoDir3.existe","El código ya existe");
                    }

                }else{
                    if(entidadEjb.findByCodigoDir3(entidad.getEntidad().getCodigoDir3()) != null){
                        errors.rejectValue("entidad.codigoDir3","entidad.codigoDir3.existe","El código ya existe");
                    }

                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }


}
