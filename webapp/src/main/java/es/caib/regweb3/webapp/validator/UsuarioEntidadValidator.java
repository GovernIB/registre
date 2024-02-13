package es.caib.regweb3.webapp.validator;

import es.caib.regweb3.model.Usuario;
import es.caib.regweb3.model.UsuarioEntidad;
import es.caib.regweb3.persistence.ejb.UsuarioLocal;
import es.caib.regweb3.utils.DocumentoUtils;
import es.caib.regweb3.utils.RegwebConstantes;
import es.caib.regweb3.utils.StringUtils;
import es.caib.regweb3.utils.Validacion;
import es.caib.regweb3.webapp.utils.LoginService;
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
            if (StringUtils.isNotEmpty(usuarioEntidad.getUsuario().getDocumento())) {

                String documento = usuarioEntidad.getUsuario().getDocumento().toUpperCase();
                Validacion validacionDocumento;

                try {
                    Long tipoDocumento;

                    // Solo permitimos DNI o NIE
                    if(documento.startsWith("Y") || documento.startsWith("Z") || documento.startsWith("X")){
                        tipoDocumento = RegwebConstantes.TIPODOCUMENTOID_NIE_ID;
                    }else{
                        tipoDocumento = RegwebConstantes.TIPODOCUMENTOID_NIF_ID;
                    }

                    validacionDocumento = DocumentoUtils.comprobarDocumento(documento, tipoDocumento);

                } catch (Exception e) {
                    e.printStackTrace();
                    validacionDocumento = new Validacion(Boolean.FALSE, "error.documento", "El document es erroni");
                }

                // Si es válido, coprobamos si ya existe previamente
                if (validacionDocumento.getValido()) {
                    boolean existe;
                    try {

                        if (usuarioEntidad.getUsuario().getId() != null) { // Se trata de una modificación
                            existe = usuarioEjb.existeDocumentoEdit(documento, usuarioEntidad.getUsuario().getId());

                        } else {
                            existe = usuarioEjb.existeDocumentoNew(documento);
                        }
                    }catch (Exception e){
                        log.error("Error comprobando si usuario entidad ya existe: ", e);
                        existe = true;
                    }

                    if (existe) {
                        errors.rejectValue("usuario.documento", "error.document.existe");
                    }

                }else{
                    errors.rejectValue("usuario.documento", validacionDocumento.getCodigoError(), new String[]{documento}, "El dni no té format correcte (8 DIGITS + LLETRA)");
                }
            }

        }
    }
}
