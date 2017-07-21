package es.caib.regweb3.persistence.validator;

import es.caib.regweb3.model.RegistroDetalle;
import es.caib.regweb3.model.RegistroSalida;
import es.caib.regweb3.utils.StringUtils;
import org.apache.log4j.Logger;
import org.fundaciobit.genapp.common.validation.IValidatorResult;


/**
 * Created by Fundació BIT.
 * Gestiona las Validaciones del formulario para crear o editar una {@link es.caib.regweb3.model.RegistroSalida}
 * @author earrivi
 * @author anadal (llevar Spring)
 * Date: 11/02/14
 */

public class RegistroSalidaValidator<T> extends AbstractRegWebValidator<T> {

  protected final Logger log = Logger.getLogger(getClass());



  public void validate(IValidatorResult<T> errors, T __target__, boolean __isNou__) {

        RegistroSalida registroSalida = (RegistroSalida)__target__;
        RegistroDetalle registroDetalle = registroSalida.getRegistroDetalle();

        rejectIfEmptyOrWhitespace(errors, __target__,  "registroDetalle.extracto", "error.valor.requerido", "El camp és obligatori");
        rejectIfEmptyOrWhitespace(errors, __target__,  "registroDetalle.tipoDocumentacionFisica", "error.valor.requerido", "El camp és obligatori");
        rejectIfEmptyOrWhitespace(errors, __target__,  "registroDetalle.idioma", "error.valor.requerido", "El camp és obligatori");

        if(registroDetalle.getExtracto() != null && registroDetalle.getExtracto().length() > 240){
          rejectValue(errors, "registroDetalle.extracto","error.valor.maxlenght","Tamaño demasiado largo");
        }

        if(registroDetalle.getTipoDocumentacionFisica() == null){
          rejectValue(errors,"registroDetalle.tipoDocumentacionFisica","error.valor.requerido","El camp és obligatori");
        }

        if(registroDetalle.getIdioma() == null){
          rejectValue(errors,"registroDetalle.idioma","error.valor.requerido","El camp és obligatori");
        }

        if(registroDetalle.getReferenciaExterna() != null && registroDetalle.getReferenciaExterna().length() > 16){
            rejectValue(errors, "registroDetalle.referenciaExterna","error.valor.maxlenght","Tamaño demasiado largo");
        }

        if(registroDetalle.getExpediente() != null && registroDetalle.getExpediente().length() > 80){
            rejectValue(errors, "registroDetalle.expediente","error.valor.maxlenght","Tamaño demasiado largo");
        }

        if(registroDetalle.getNumeroTransporte() != null && registroDetalle.getNumeroTransporte().length() > 20){
            rejectValue(errors, "registroDetalle.numeroTransporte","error.valor.maxlenght","Tamaño demasiado largo");
        }

        if(registroDetalle.getObservaciones() != null && registroDetalle.getObservaciones().length() > 50){
            rejectValue(errors, "registroDetalle.observaciones","error.valor.maxlenght","Tamaño demasiado largo");
        }

        if(registroDetalle.getNumeroRegistroOrigen() != null && registroDetalle.getNumeroRegistroOrigen().length() > 256){
            rejectValue(errors, "registroDetalle.numeroRegistroOrigen","error.valor.maxlenght","Tamaño demasiado largo");
        }

        if(registroDetalle.getTipoDocumentacionFisica() == null){
            rejectValue(errors, "registroDetalle.tipoDocumentacionFisica", "error.valor.requerido", "El camp és obligatori");
        }

        // TipoAsunto obligatorio
     
        
        if(registroDetalle.getTipoAsunto() == null || registroDetalle.getTipoAsunto().getId() == null || registroDetalle.getTipoAsunto().getId().equals((long) -1) ){
          rejectValue(errors, "registroDetalle.tipoAsunto.id", "error.valor.requerido", "El camp és obligatori");
        }
        
        
        if (registroSalida.getOrigen() == null || registroSalida.getOrigen().getCodigo() == null || "".equals(registroSalida.getOrigen().getCodigo())) {
          rejectValue(errors, "origen.codigo", "error.valor.requerido", "El camp és obligatori");
        }

      if(StringUtils.isNotEmpty(registroDetalle.getExpone())){
          if(StringUtils.isEmpty(registroDetalle.getSolicita())){
              rejectValue(errors, "registroDetalle.solicita", "error.valor.requerido", "El camp és obligatori");
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
        return "registroSalida.";
    }

}
