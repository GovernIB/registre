package es.caib.regweb3.webapp.validator;

import es.caib.regweb3.model.Usuario;
import es.caib.regweb3.model.UsuarioEntidad;
import es.caib.regweb3.persistence.ejb.UsuarioLocal;
import es.caib.regweb3.utils.RegwebConstantes;
import es.caib.regweb3.webapp.utils.LoginService;
import org.fundaciobit.genapp.common.i18n.I18NException;
import org.fundaciobit.genapp.common.web.i18n.I18NUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import javax.ejb.EJB;

/**
 * Created by Fundació BIT.
 * Gestiona las Validaciones del formulario para crear o editar una {@link Usuario}
 * @author earrivi
 * Date: 11/02/14
 */
@Component
public class UsuarioEntidadValidator implements Validator {

    protected final Logger log = LoggerFactory.getLogger(getClass());

    @EJB(mappedName = UsuarioLocal.JNDI_NAME)
    private UsuarioLocal usuarioEjb;

    @Autowired
    private LoginService loginService;

    @Override
    public boolean supports(Class<?> clazz) {
        return UsuarioEntidad.class.equals(clazz);
    }

    @Override
    public void validate(Object o, Errors errors) {

        UsuarioEntidad usuarioEntidad = (UsuarioEntidad)o;

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "usuario.nombre", "error.valor.requerido", "El camp és obligatori");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "usuario.email", "error.valor.requerido", "El camp és obligatori");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "usuario.identificador", "error.valor.requerido", "El camp és obligatori");

        //Validaciones si es Usuario Persona
        if(usuarioEntidad.getUsuario().getTipoUsuario() != null && usuarioEntidad.getUsuario().getTipoUsuario().equals(RegwebConstantes.TIPO_USUARIO_PERSONA)) {

            ValidationUtils.rejectIfEmptyOrWhitespace(errors, "usuario.apellido1", "error.valor.requerido", "El camp és obligatori");
            ValidationUtils.rejectIfEmptyOrWhitespace(errors, "usuario.documento", "error.valor.requerido", "El camp és obligatori");

            // Formato DNI
            if (usuarioEntidad.getUsuario().getDocumento() != null && !usuarioEntidad.getUsuario().getDocumento().isEmpty()) {

                String documento = usuarioEntidad.getUsuario().getDocumento();
                String letras = "TRWAGMYFPDXBNJZSQVHLCKE";
                int valor;

                if (documento.length() == 9) {
                    String numeroNif = documento.substring(0, documento.length() - 1);
                    Boolean nifcorrecte = true;
                    for (int i = 0; i < numeroNif.length(); i++) {
                        if (!Character.isDigit(numeroNif.charAt(i))) {
                            errors.rejectValue("usuario.documento", "usuario.dni.incorrecto", "El dni no té format correcte (8 DIGITS + LLETRA)");
                            nifcorrecte = false;
                            break;
                        }
                    }
                    if (nifcorrecte) {
                        valor = Integer.parseInt(documento.substring(0, documento.length() - 1));

                        if (!documento.endsWith("" + letras.charAt(valor % 23))) {
                            errors.rejectValue("usuario.documento", "usuario.documento.letra", "Lletra de document incorrecta");
                        }
                    }
                } else {
                    errors.rejectValue("usuario.documento", "usuario.documento.largo", "Llargària de document incorrecta");
                }
            }


            try {

                // Identificador único
                if (usuarioEntidad.getUsuario().getIdentificador() != null && !usuarioEntidad.getUsuario().getIdentificador().isEmpty()) {

                    if(!loginService.existeIdentificador(usuarioEntidad.getUsuario().getIdentificador())){
                        errors.rejectValue("usuario.identificador", "usuario.identificador.no.existe", "L'identificador no existeix al sistema de usuaris");
                    }

                    if (usuarioEntidad.getUsuario().getId() != null) {  // Se trata de una modificación
                        if (usuarioEjb.existeIdentificadorEdit(usuarioEntidad.getUsuario().getIdentificador(), usuarioEntidad.getUsuario().getId())) {
                            errors.rejectValue("usuario.identificador", "error.identificador.existe", "L'identificador ja existeix");
                        }

                    } else {
                        if (usuarioEjb.findByIdentificador(usuarioEntidad.getUsuario().getIdentificador()) != null) {
                            errors.rejectValue("usuario.identificador", "error.identificador.existe", "L'identificador ja existeix");
                        }
                    }
                }

                // NIF único
                if (usuarioEntidad.getUsuario().getDocumento() != null && usuarioEntidad.getUsuario().getDocumento().length() > 0) {

                    if (usuarioEntidad.getUsuario().getId() != null) { // Se trata de una modificación
                        if (usuarioEjb.existeDocumentioEdit(usuarioEntidad.getUsuario().getDocumento(), usuarioEntidad.getUsuario().getId())) {
                            errors.rejectValue("usuario.documento", "usuario.documento.existe", "El documento ya existe ja existeix");
                        }
                    } else {
                        if (usuarioEjb.findByDocumento(usuarioEntidad.getUsuario().getDocumento()) != null) {
                            errors.rejectValue("usuario.documento", "usuario.documento.existe", "El documento ya existe ja existeix");
                        }
                    }

                }
            } catch(I18NException i18ne) {
              log.error(I18NUtils.getMessage(i18ne), i18ne);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
