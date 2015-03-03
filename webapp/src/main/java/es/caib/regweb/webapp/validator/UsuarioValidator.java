package es.caib.regweb.webapp.validator;

import es.caib.regweb.model.Usuario;
import es.caib.regweb.persistence.ejb.UsuarioLocal;
import es.caib.regweb.utils.RegwebConstantes;
import es.caib.regweb.webapp.utils.UsuarioService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import javax.ejb.EJB;

/**
 * Created by Fundació BIT.
 * Gestiona las Validaciones del formulario para crear o editar una {@link es.caib.regweb.model.Usuario}
 * @author earrivi
 * Date: 11/02/14
 */
@Component
public class UsuarioValidator implements Validator {

    protected final Logger log = Logger.getLogger(getClass());

    @EJB(mappedName = "regweb/UsuarioEJB/local")
    public UsuarioLocal usuarioEjb;

    @Autowired
    private UsuarioService usuarioService;

    @Override
    public boolean supports(Class<?> clazz) {
        return Usuario.class.equals(clazz);
    }

    @Override
    public void validate(Object o, Errors errors) {

        Usuario usuario = (Usuario)o;

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "nombre", "error.valor.requerido", "El camp és obligatori");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "email", "error.valor.requerido", "El camp és obligatori");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "identificador", "error.valor.requerido", "El camp és obligatori");

        //Validaciones si es Usuario Persona
        if(usuario.getTipoUsuario() != null && usuario.getTipoUsuario().equals(RegwebConstantes.TIPO_USUARIO_PERSONA)) {

            ValidationUtils.rejectIfEmptyOrWhitespace(errors, "apellido1", "error.valor.requerido", "El camp és obligatori");
            ValidationUtils.rejectIfEmptyOrWhitespace(errors, "documento", "error.valor.requerido", "El camp és obligatori");

            // Formato DNI
            if (usuario.getDocumento() != null && usuario.getDocumento().length() > 0) {

                String documento = usuario.getDocumento();
                String letras = "TRWAGMYFPDXBNJZSQVHLCKE";
                int valor = 0;

                if (documento.length() == 9) {
                    String numeroNif = documento.substring(0, documento.length() - 1);
                    Boolean nifcorrecte = true;
                    for (int i = 0; i < numeroNif.length(); i++) {
                        if (!Character.isDigit(numeroNif.charAt(i))) {
                            errors.rejectValue("documento", "usuario.dni.incorrecto", "El dni no té format correcte (8 DIGITS + LLETRA)");
                            nifcorrecte = false;
                            break;
                        }
                    }
                    if (nifcorrecte) {
                        valor = Integer.parseInt(documento.substring(0, documento.length() - 1));

                        if (!documento.endsWith("" + letras.charAt(valor % 23))) {
                            errors.rejectValue("documento", "usuario.documento.letra", "Lletra de document incorrecta");
                        }
                    }
                } else {
                    errors.rejectValue("documento", "usuario.documento.largo", "Llargària de document incorrecta");
                }
            }


            try {

                // Identificador único
                if (usuario.getIdentificador() != null && usuario.getIdentificador().length() > 0) {

                    if(!usuarioService.existeIdentificador(usuario.getIdentificador())){
                        errors.rejectValue("identificador", "usuario.identificador.no.existe", "L'identificador no existeix al sistema de usuaris");
                    }

                    if (usuario.getId() != null) {  // Se trata de una modificación
                        if (usuarioEjb.existeIdentificadorEdit(usuario.getIdentificador(), usuario.getId())) {
                            errors.rejectValue("identificador", "error.identificador.existe", "L'identificador ja existeix");
                        }

                    } else {
                        if (usuarioEjb.findByIdentificador(usuario.getIdentificador()) != null) {
                            errors.rejectValue("identificador", "error.identificador.existe", "L'identificador ja existeix");
                        }
                    }

                }

                // NIF único
                if (usuario.getDocumento() != null && usuario.getDocumento().length() > 0) {

                    if (usuario.getId() != null) { // Se trata de una modificación
                        if (usuarioEjb.existeDocumentioEdit(usuario.getDocumento(), usuario.getId())) {
                            errors.rejectValue("documento", "usuario.documento.existe", "El documento ya existe ja existeix");
                        }
                    } else {
                        if (usuarioEjb.findByDocumento(usuario.getDocumento()) != null) {
                            errors.rejectValue("documento", "usuario.documento.existe", "El documento ya existe ja existeix");
                        }
                    }

                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }


}
