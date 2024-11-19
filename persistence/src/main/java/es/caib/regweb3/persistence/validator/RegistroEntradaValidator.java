package es.caib.regweb3.persistence.validator;

import es.caib.regweb3.model.Anexo;
import es.caib.regweb3.model.RegistroDetalle;
import es.caib.regweb3.model.RegistroEntrada;
import es.caib.regweb3.utils.RegwebConstantes;
import es.caib.regweb3.utils.StringUtils;

import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;
import org.fundaciobit.genapp.common.i18n.I18NArgumentString;
import org.fundaciobit.genapp.common.query.Field;
import org.fundaciobit.genapp.common.validation.IValidatorResult;

/**
 * Created by Fundació BIT.
 * Gestiona las Validaciones del formulario para crear o editar una {@link es.caib.regweb3.model.RegistroEntrada}
 * @author earrivi
 * @author Adaptar a NO-String
 * Date: 11/02/14
 */
public class RegistroEntradaValidator<T> extends AbstractRegWebValidator<T> {

    protected final Logger log = Logger.getLogger(getClass());



    public void validate(IValidatorResult<T> errors, T __target__, boolean __isNou__) {

        RegistroEntrada registroEntrada = (RegistroEntrada)__target__;
        RegistroDetalle registroDetalle = registroEntrada.getRegistroDetalle();

        rejectIfEmptyOrWhitespace(errors, __target__, "registroDetalle.extracto", "error.valor.requerido");
        rejectIfEmptyOrWhitespace(errors, __target__, "registroDetalle.tipoDocumentacionFisica", "error.valor.requerido");
        rejectIfEmptyOrWhitespace(errors, __target__, "registroDetalle.idioma", "error.valor.requerido");


        if(registroDetalle.getExtracto() != null && registroDetalle.getExtracto().length() > 240){
            rejectValue(errors,"registroDetalle.extracto","error.valor.maxlenght");
        }

        if(registroDetalle.getReferenciaExterna() != null && registroDetalle.getReferenciaExterna().length() > 16){
            rejectValue(errors,"registroDetalle.referenciaExterna","error.valor.maxlenght");
        }

        if(registroDetalle.getExpediente() != null && registroDetalle.getExpediente().length() > 80){
            rejectValue(errors,"registroDetalle.expediente","error.valor.maxlenght");
        }

        if(registroDetalle.getNumeroTransporte() != null && registroDetalle.getNumeroTransporte().length() > 20){
            rejectValue(errors,"registroDetalle.numeroTransporte","error.valor.maxlenght");
        }

        if(registroDetalle.getObservaciones() != null && registroDetalle.getObservaciones().length() > 50){
            rejectValue(errors,"registroDetalle.observaciones","error.valor.maxlenght");
        }
        
        // Anexos LEMA
        List<Anexo> anexos = registroDetalle.getAnexos();
        if (anexos != null) {
        	for (int i = 0; i < anexos.size(); i++) {
        		
        		if (anexos.get(i).getValidezDocumento() == null) {
                    rejectValue(errors, "registroDetalle.anexos[" + i + "].validezDocumento", "error.valor.requerido"); // , "El camp és obligatori"
                }

                if (anexos.get(i).getTipoDocumento() == null || anexos.get(i).getTipoDocumento().equals((long) -1)) {
                    rejectValue(errors, "registroDetalle.anexos[" + i + "].tipoDocumento", "error.valor.requerido"); // , "El camp és obligatori"
                }
                
        		if (anexos.get(i).getTipoDocumental() == null || anexos.get(i).getTipoDocumental().getId() == null ) {
    	            rejectValue(errors, "registroDetalle.anexos[" + i + "].tipoDocumental", "error.valor.requerido");
    	        } else {
    	            if (anexos.get(i).getTipoDocumental().getId() == -1) {
    	                rejectValue(errors, "registroDetalle.anexos[" + i + "].tipoDocumental", "error.valor.requerido");
    	            }
    	        }
        		
        		if (anexos.get(i).getOrigenCiudadanoAdmin() == null) {
    	            rejectValue(errors, "registroDetalle.anexos[" + i + "].origenCiudadanoAdmin", "error.valor.requerido");
    	        } else if (anexos.get(i).getOrigenCiudadanoAdmin() != 0 && anexos.get(i).getOrigenCiudadanoAdmin() != 1) {
    	            rejectValue(errors, "registroDetalle.anexos[" + i + "].origenCiudadanoAdmin", "error.valor.inesperado.origen");
    	        }
        		
        		rejectIfEmptyOrWhitespace(errors, __target__, "registroDetalle.anexos[" + i + "].titulo", "error.valor.requerido");

        		rejectIfEmptyOrWhitespace(errors, __target__, "registroDetalle.anexos[" + i + "].nombreFichero", "error.valor.requerido");
        		
                if (anexos.get(i).getTitulo() != null && anexos.get(i).getTitulo().length() > 200) {
                    rejectValue(errors, "registroDetalle.anexos[" + i + "].titulo", "error.valor.maxlenght");
                }
                
                if (anexos.get(i).getNombreFichero() != null && anexos.get(i).getNombreFichero().length() > 200) {
                    rejectValue(errors, "registroDetalle.anexos[" + i + "].nombreFichero", "error.valor.maxlenght");
                }
			}
        }

        if(registroDetalle.getNumeroRegistroOrigen() != null && registroDetalle.getNumeroRegistroOrigen().length() > 256){
            rejectValue(errors,"registroDetalle.numeroRegistroOrigen","error.valor.maxlenght");
        }


        if (StringUtils.isEmpty(registroEntrada.getDestinoExternoCodigo())) {
            if (registroEntrada.getDestino() == null || registroEntrada.getDestino().getCodigo() == null || "".equals(registroEntrada.getDestino().getCodigo())) {
                rejectValue(errors, "destino.codigo", "error.valor.requerido");
            }
        }
        //Si el campo expone esta informado, el campo solicita se debe informar también
        if(StringUtils.isNotEmpty(registroDetalle.getExpone())){
            if(StringUtils.isEmpty(registroDetalle.getSolicita())){
                rejectValue(errors, "registroDetalle.solicita", "error.valor.requerido");
            }
        }


    }


    @Override
    public String adjustFieldName(String fieldName) {
        if (fieldName == null ) {
            return null;
        } else {
          
            
            if (fieldName.startsWith("registroDetalle.")) {
                fieldName =  fieldName.replace("registroDetalle.", "");
            }

            if (fieldName.endsWith(".id")) {
                return fieldName.substring(0, fieldName.lastIndexOf('.'));
            }

        }
        return fieldName;
    }

    @Override
    public String prefixFieldName() {
        return "registroEntrada.";
    }
    
    
    @Override
    public String  getTranslationCode(Field<?> f) {
      if ("registroEntrada.destino.codigo".equals(f.getJavaName())) {
         return "registroEntrada.organismoDestino";
      } else {
        return super.getTranslationCode(f);
      }
    }

}
