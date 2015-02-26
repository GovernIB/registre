package es.caib.regweb.webapp.validator;

import es.caib.regweb.model.Entidad;
import es.caib.regweb.model.Libro;
import es.caib.regweb.persistence.ejb.LibroLocal;
import es.caib.regweb.utils.RegwebConstantes;
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
public class LibroValidator implements Validator {

    protected final Logger log = Logger.getLogger(getClass());

    @EJB(mappedName = "regweb/LibroEJB/local")
    public LibroLocal libroEjb;


    @Override
    public boolean supports(Class<?> clazz) {
        return Libro.class.equals(clazz);
    }

    @Override
    public void validate(Object o, Errors errors) {

        Libro libro = (Libro)o;
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();

        HttpSession session = request.getSession();
        Entidad entidadActiva = (Entidad) session.getAttribute(RegwebConstantes.SESSION_ENTIDAD);

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "nombre", "error.valor.requerido", "El camp és obligatori");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "codigo", "error.valor.requerido", "El camp és obligatori");

        if(libro.getCodigo() != null && libro.getCodigo().length() > 4){
            errors.rejectValue("codigo", "libro.codigo.largo", "El camp és obligatori");
        }

        try{

            if(libro.getCodigo() != null && libro.getCodigo().length()> 0){

                if(libro.getId() != null){ // Se trata de una modificación

                    if(libroEjb.existeCodigoEdit(libro.getCodigo(), libro.getId(), entidadActiva.getId())){
                        errors.rejectValue("codigo", "error.codigo.existe","El codi ja existeix");
                    }
                }else{
                    if(libroEjb.findByCodigoEntidad(libro.getCodigo(),entidadActiva.getId()) != null){
                        errors.rejectValue("codigo", "error.codigo.existe","El codi ja existeix");
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
