package es.caib.regweb3.webapp.validator;

import es.caib.regweb3.model.Usuario;
import es.caib.regweb3.persistence.ejb.PluginLocal;
import es.caib.regweb3.persistence.ejb.UsuarioLocal;
import es.caib.regweb3.utils.RegwebConstantes;
import org.fundaciobit.genapp.common.web.i18n.I18NUtils;
import org.fundaciobit.pluginsib.userinformation.IUserInformationPlugin;
import org.fundaciobit.pluginsib.userinformation.UserInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
public class UsuarioDocumentoValidator implements Validator {

    protected final Logger log = LoggerFactory.getLogger(getClass());

    @EJB(mappedName = PluginLocal.JNDI_NAME)
    private PluginLocal pluginEjb;

    @EJB(mappedName = UsuarioLocal.JNDI_NAME)
    private UsuarioLocal usuarioEjb;


    @Override
    public boolean supports(Class<?> clazz) {
        return Usuario.class.equals(clazz);
    }

    @Override
    public void validate(Object o, Errors errors) {

        Usuario usuario = (Usuario)o;

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "identificador", "error.valor.requerido", "El camp és obligatori");

        try{
            IUserInformationPlugin loginPlugin = (IUserInformationPlugin) pluginEjb.getPlugin(null, RegwebConstantes.PLUGIN_USER_INFORMATION, true);
            UserInfo regwebUserInfo = loginPlugin.getUserInfoByUserName(usuario.getIdentificador());

            boolean existe;
            try {

                if (usuario.getId() != null) { // Se trata de una modificación
                    existe = usuarioEjb.existeDocumentoEdit(regwebUserInfo.getAdministrationID().toUpperCase(), usuario.getId());

                } else {
                    existe = usuarioEjb.existeDocumentoNew(regwebUserInfo.getAdministrationID().toUpperCase());
                }
            }catch (Exception e){
                log.error("Error validando documento de usuario: ", e);
                existe = true;
            }

            if (existe) {
                errors.rejectValue("identificador", "error.document.identificador.existe", new Object[]{regwebUserInfo.getAdministrationID()}, I18NUtils.tradueix("error.document.identificador.existe",regwebUserInfo.getAdministrationID()));
            }

        }catch(Exception e){
            e.printStackTrace();
        }

//        // Formato DNI
//        if(usuario.getDocumento()!= null && usuario.getDocumento().length()>0){
//
//            String documento = usuario.getDocumento();
//            String letras = "TRWAGMYFPDXBNJZSQVHLCKE";
//            int valor = 0;
//
//            if(documento.length() == 9){
//                String numeroNif = documento.substring(0,documento.length()-1);
//                Boolean nifcorrecte = true;
//                for(int i = 0; i<numeroNif.length(); i++){
//                    if( !Character.isDigit(numeroNif.charAt(i))){
//                        errors.rejectValue("documento","usuario.dni.incorrecto","El dni no té format correcte (8 DIGITS + LLETRA)");
//                        nifcorrecte = false;
//                        break;
//                    }
//                }
//                if(nifcorrecte){
//                    valor=Integer.parseInt(documento.substring(0,documento.length()-1));
//
//                    if (!documento.endsWith("" + letras.charAt(valor % 23))){
//                        errors.rejectValue("documento","usuario.documento.letra","Lletra de document incorrecta");
//                    }
//                }
//            } else {
//                errors.rejectValue("documento","usuario.documento.largo","Llargària de document incorrecta");
//            }
//        }

    }


}
