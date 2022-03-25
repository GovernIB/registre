package es.caib.regweb3.persistence.validator;

import es.caib.regweb3.model.Anexo;
import es.caib.regweb3.utils.RegwebConstantes;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.fundaciobit.genapp.common.i18n.I18NArgumentString;
import org.fundaciobit.genapp.common.validation.IValidatorResult;

import java.util.Arrays;


/**
 * Created by Fundació BIT.
 * Gestiona las Validaciones del formulario para crear o editar una {@link es.caib.regweb3.model.Anexo}
 *
 * @author earrivi
 * @author anadal (adaptació sense Spring)
 * Date: 11/02/14
 */
public class AnexoValidator<T> extends AbstractRegWebValidator<T> {

    protected final Logger log = Logger.getLogger(getClass());


    /**
     * Constructor
     */
    public AnexoValidator() {
        super();
    }


    public void validate(IValidatorResult<T> errors, T __target__, boolean __isNou__) {


        Anexo anexo = (Anexo) __target__;


        if (anexo.getValidezDocumento() == null) {
            rejectValue(errors, "validezDocumento", "error.valor.requerido"); // , "El camp és obligatori"
        }

        if (anexo.getTipoDocumento() == null || anexo.getTipoDocumento().equals((long) -1)) {
            rejectValue(errors, "tipoDocumento", "error.valor.requerido"); // , "El camp és obligatori"
        }

        if (anexo.getValidarNtiOrigen() && !anexo.getTipoDocumento().equals(RegwebConstantes.TIPO_DOCUMENTO_JUSTIFICANTE)) {
	        if (anexo.getTipoDocumental() == null || anexo.getTipoDocumental().getId() == null ) {
	            rejectValue(errors, "tipoDocumental", "error.valor.requerido");
	        } else {
	            if (anexo.getTipoDocumental().getId() == -1) {
	                rejectValue(errors, "tipoDocumental", "error.valor.requerido");
	            }
	        }
	
	        if (anexo.getOrigenCiudadanoAdmin() == null) {
	            rejectValue(errors, "origenCiudadanoAdmin", "error.valor.requerido");
	        } else if (anexo.getOrigenCiudadanoAdmin() != 0 && anexo.getOrigenCiudadanoAdmin() != 1) {
	            rejectValue(errors, "origenCiudadanoAdmin", "error.valor.inesperado.origen");
	        }
        }
        
        rejectIfEmptyOrWhitespace(errors, __target__, "titulo", "error.valor.requerido");

        if (StringUtils.isNotEmpty(anexo.getTitulo())) {
            if (StringUtils.indexOfAny(anexo.getTitulo(), RegwebConstantes.CARACTERES_NO_PERMITIDOS_ARXIU) != -1) {
                rejectValue(errors, "titulo", "error.caracteres.noPermitidos", new I18NArgumentString(Arrays.toString(RegwebConstantes.CARACTERES_NO_PERMITIDOS_ARXIU)));
            }
        }

        if (anexo.getTitulo() != null && anexo.getTitulo().length() > 200) {
            rejectValue(errors, "titulo", "error.valor.maxlenght");
        }


    } // Final de mètode


    @Override
    public String prefixFieldName() {
        return "anexo.";
    }

    @Override
    public String adjustFieldName(String fieldName) {
        if (fieldName == null) {
            return null;
        } else {
            if (fieldName.endsWith(".id")) {
                return fieldName.substring(0, fieldName.lastIndexOf('.'));
            }
        }
        return fieldName;
    }


}


