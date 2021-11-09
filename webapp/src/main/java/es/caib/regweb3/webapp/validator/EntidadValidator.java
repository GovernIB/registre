package es.caib.regweb3.webapp.validator;

import es.caib.regweb3.model.Entidad;
import es.caib.regweb3.model.Rol;
import es.caib.regweb3.persistence.ejb.EntidadLocal;
import es.caib.regweb3.utils.RegwebConstantes;
import es.caib.regweb3.webapp.form.EntidadForm;
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
public class EntidadValidator implements Validator {


    protected final Logger log = Logger.getLogger(getClass());

    @EJB(mappedName = "regweb3/EntidadEJB/local")
    public EntidadLocal entidadEjb;

    @Override
    public boolean supports(Class<?> clazz) {
        return EntidadForm.class.equals(clazz);
    }

    @Override
    public void validate(Object o, Errors errors) {

        EntidadForm entidadForm = (EntidadForm)o;
        Entidad entidad = entidadForm.getEntidad();

        HttpServletRequest request =
                ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
                        .getRequest();

        HttpSession session = request.getSession();
        LoginInfo loginInfo = (LoginInfo) session.getAttribute(RegwebConstantes.SESSION_LOGIN_INFO);
        Rol rolActivo = loginInfo.getRolActivo();

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "entidad.nombre", "error.valor.requerido", "El camp és obligatori");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "entidad.descripcion", "error.valor.requerido", "El camp és obligatori");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "entidad.codigoDir3", "error.valor.requerido", "El camp és obligatori");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "entidad.libro.nombre", "error.valor.requerido", "El camp és obligatori");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "entidad.libro.codigo", "error.valor.requerido", "El camp és obligatori");

        // Tamaño campo Libro
        if(entidad.getLibro().getCodigo() != null && entidad.getLibro().getCodigo().length() > 4){
            errors.rejectValue("codigo", "libro.codigo.largo", "El camp és obligatori");
        }

        // Validaciones solo si es Administrador de Entidad
        if(rolActivo.getNombre().equals(RegwebConstantes.RWE_ADMIN)){
            ValidationUtils.rejectIfEmptyOrWhitespace(errors, "entidad.colorMenu", "error.valor.requerido", "El camp és obligatori");
            ValidationUtils.rejectIfEmptyOrWhitespace(errors, "entidad.sello", "error.valor.requerido", "El camp Format Segell és obligatori");
//            ValidationUtils.rejectIfEmptyOrWhitespace(errors, "entidad.numRegistro", "error.valor.requerido", "El camp Format Número Registre és obligatori");
        }

        /*Validación del campo único CodigoDir3*/
        if(entidad.getCodigoDir3() != null && entidad.getCodigoDir3().length() > 0){
            try {
                if(entidad.getId() != null){ // Es una modificación

                    if(entidadEjb.existeCodigoDir3Edit(entidad.getCodigoDir3(),entidad.getId())){
                        errors.rejectValue("entidad.codigoDir3","entidad.codigoDir3.existe","El código ya existe");
                    }

                }else{
                    if(entidadEjb.findByCodigoDir3(entidad.getCodigoDir3()) != null){
                        errors.rejectValue("entidad.codigoDir3","entidad.codigoDir3.existe","El código ya existe");
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        /*Validación de la Posición del Sello: (X, Y)*/
        if(entidad.getPosXsello() != null){ //Comprueba que PosX tiene valor
            try {
                if(entidad.getPosYsello() != null){ //PosY también tiene valor
                    if(entidad.getPosXsello() > 155){ //Valor máximo de PosX = 155
                        errors.rejectValue("entidad.posXsello","entidad.sello.valorXmax","Valor máximo = 155");
                    }else if(entidad.getPosXsello() < 10){ //Valor mínimo de PosX = 10
                        errors.rejectValue("entidad.posXsello","entidad.sello.valorXmin","Valor mínimo = 10");
                    }
                    if(entidad.getPosYsello() > 264){ //Valor máximo de PosY = 264
                        errors.rejectValue("entidad.posYsello","entidad.sello.valorYmax","Valor máximo = 264");
                    }else if(entidad.getPosYsello() < 10){ //Valor mínimo de PosY = 10
                        errors.rejectValue("entidad.posYsello","entidad.sello.valorYmin","Valor mínimo = 10");
                    }
                }else{ // Si PosY no tiene valor, indica error
                    errors.rejectValue("entidad.posYsello","entidad.posicion.necesario","Debe completar las dos posiciones");
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }else{  // PosX no tiene valor
            if(entidad.getPosYsello() != null){ // Si PosY tiene valor, indica error
                errors.rejectValue("entidad.posXsello","entidad.posicion.necesario","Debe completar las dos posiciones");
            }
        }

        /*Validación de Format Num.Registre
        if(entidad.getNumRegistro() != null){
            try {
                if(!entidad.getNumRegistro().contains("${tipusRegistre}")){ //Si no conté el camp tipusRegistre
                    errors.rejectValue("entidad.numRegistro","error.numRegistre.incorrecto","error.numRegistre.tipusRegistre");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }*/

    }


}
