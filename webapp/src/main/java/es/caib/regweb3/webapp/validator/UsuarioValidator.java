package es.caib.regweb3.webapp.validator;

import es.caib.regweb3.model.Usuario;
import es.caib.regweb3.persistence.ejb.UsuarioLocal;
import es.caib.regweb3.utils.DocumentoUtils;
import es.caib.regweb3.utils.RegwebConstantes;
import es.caib.regweb3.utils.Validacion;
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
 * Gestiona las Validaciones del formulario para crear o editar una {@link es.caib.regweb3.model.Usuario}
 * @author earrivi
 * Date: 11/02/14
 */
@Component
public class UsuarioValidator implements Validator {

    protected final Logger log = LoggerFactory.getLogger(getClass());

    @EJB(mappedName = UsuarioLocal.JNDI_NAME)
    private UsuarioLocal usuarioEjb;

    @Autowired
    private LoginService loginService;

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
            if (usuario.getDocumento() != null && !usuario.getDocumento().isEmpty()) {

                String documento = usuario.getDocumento().toUpperCase();
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

                        if (usuario.getId() != null) { // Se trata de una modificación
                            existe = usuarioEjb.existeDocumentoEdit(documento, usuario.getId());

                        } else {
                            existe = usuarioEjb.existeDocumentoNew(documento);
                        }
                    }catch (Exception e){
                        log.error("Error comprobando si usuario entidad ya existe: ", e);
                        existe = true;
                    }

                    if (existe) {
                        errors.rejectValue("documento", "error.document.existe");
                    }

                }else{
                    errors.rejectValue("documento", validacionDocumento.getCodigoError(), "El dni no té format correcte (8 DIGITS + LLETRA)");
                    log.info("El formato del documento NO es correcto");
                }
            }


            try {

                // Identificador único
                if (usuario.getIdentificador() != null && !usuario.getIdentificador().isEmpty()) {

                    if(!loginService.existeIdentificador(usuario.getIdentificador())){
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

            } catch(I18NException i18ne) {
              log.error(I18NUtils.getMessage(i18ne), i18ne);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }


}
