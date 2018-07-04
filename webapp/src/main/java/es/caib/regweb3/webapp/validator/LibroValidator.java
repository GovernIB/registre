package es.caib.regweb3.webapp.validator;

import es.caib.regweb3.model.Entidad;
import es.caib.regweb3.model.Libro;
import es.caib.regweb3.persistence.ejb.LibroLocal;
import es.caib.regweb3.persistence.ejb.OficinaLocal;
import es.caib.regweb3.utils.RegwebConstantes;
import es.caib.regweb3.webapp.security.LoginInfo;
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
 * Gestiona las Validaciones del formulario para crear o editar una {@link es.caib.regweb3.model.Entidad}
 * @author earrivi
 * Date: 11/02/14
 */
@Component
public class LibroValidator implements Validator {

    protected final Logger log = Logger.getLogger(getClass());

    @EJB(mappedName = "regweb3/LibroEJB/local")
    private LibroLocal libroEjb;

    @EJB(mappedName = "regweb3/OficinaEJB/local")
    private OficinaLocal oficinaEjb;


    @Override
    public boolean supports(Class<?> clazz) {
        return Libro.class.equals(clazz);
    }

    @Override
    public void validate(Object o, Errors errors) {

        Libro libro = (Libro)o;
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();

        HttpSession session = request.getSession();
        LoginInfo loginInfo = (LoginInfo) session.getAttribute(RegwebConstantes.SESSION_LOGIN_INFO);
        Entidad entidadActiva = loginInfo.getEntidadActiva();

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

            if(libro.getActivo()){
                if(!oficinaEjb.tieneOficinasServicio(libro.getOrganismo().getId(),  RegwebConstantes.OFICINA_VIRTUAL_SI)){
                    log.info("No se puede activar el libro, porque el Organismo al que pertenece el Libro no tiene Oficinas que le den servicio");
                    errors.rejectValue("codigo", "libro.organismo.oficinas","No se puede activar el libro, porque el Organismo al que pertenece el Libro no tiene Oficinas que le den servicio");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
