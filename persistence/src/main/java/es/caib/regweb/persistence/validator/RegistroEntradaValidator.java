package es.caib.regweb.persistence.validator;

import es.caib.regweb.model.RegistroDetalle;
import es.caib.regweb.model.RegistroEntrada;

import org.apache.log4j.Logger;
import org.fundaciobit.genapp.common.query.Field;
import org.fundaciobit.genapp.common.validation.IValidatorResult;

/**
 * Created by Fundació BIT.
 * Gestiona las Validaciones del formulario para crear o editar una {@link es.caib.regweb.model.RegistroEntrada}
 * @author earrivi
 * @author Adaptar a NO-String
 * Date: 11/02/14
 */
public class RegistroEntradaValidator<T> extends AbstractRegWebValidator<T> {

    protected final Logger log = Logger.getLogger(getClass());



    public void validate(IValidatorResult<T> errors, T __target__, boolean __isNou__) {

        RegistroEntrada registroEntrada = (RegistroEntrada)__target__;
        RegistroDetalle registroDetalle = registroEntrada.getRegistroDetalle();

        rejectIfEmptyOrWhitespace(errors, __target__, "registroDetalle.extracto", "error.valor.requerido", "El camp és obligatori");
        rejectIfEmptyOrWhitespace(errors, __target__, "registroDetalle.tipoDocumentacionFisica", "error.valor.requerido", "El camp és obligatori");
        rejectIfEmptyOrWhitespace(errors, __target__, "registroDetalle.idioma", "error.valor.requerido", "El camp és obligatori");

        /*if(registroEntrada.getDestino() == null && registroEntrada.getDestinoExternoCodigo() == null){
           if(registroEntrada.getDestino() == null){
              rejectValue(errors,"destino","error.valor.requerido", "El camp és obligatori");
           }
           if(registroEntrada.getDestinoExternoCodigo() == null){
              rejectValue(errors,"destinoExternoCodigo","error.valor.requerido", "El camp és obligatori");
           }
        }*/

        if(registroDetalle.getExtracto() != null && registroDetalle.getExtracto().length() > 240){
            rejectValue(errors,"registroDetalle.extracto","error.valor.maxlenght","Tamaño demasiado largo");
        }

        if(registroDetalle.getReferenciaExterna() != null && registroDetalle.getReferenciaExterna().length() > 16){
            rejectValue(errors,"registroDetalle.referenciaExterna","error.valor.maxlenght","Tamaño demasiado largo");
        }

        if(registroDetalle.getExpediente() != null && registroDetalle.getExpediente().length() > 80){
            rejectValue(errors,"registroDetalle.expediente","error.valor.maxlenght","Tamaño demasiado largo");
        }

        if(registroDetalle.getNumeroTransporte() != null && registroDetalle.getNumeroTransporte().length() > 20){
            rejectValue(errors,"registroDetalle.numeroTransporte","error.valor.maxlenght","Tamaño demasiado largo");
        }

        if(registroDetalle.getObservaciones() != null && registroDetalle.getObservaciones().length() > 50){
            rejectValue(errors,"registroDetalle.observaciones","error.valor.maxlenght","Tamaño demasiado largo");
        }

        if(registroDetalle.getNumeroRegistroOrigen() != null && registroDetalle.getNumeroRegistroOrigen().length() > 256){
            rejectValue(errors,"registroDetalle.numeroRegistroOrigen","error.valor.maxlenght","Tamaño demasiado largo");
        }

        if(registroDetalle.getTipoDocumentacionFisica() == null){
            rejectValue(errors, "registroDetalle.tipoDocumentacionFisica", "error.valor.requerido", "El camp és obligatori");
        }

        // TipoAsunto obligatorio
        if(registroDetalle.getTipoAsunto() == null || registroDetalle.getTipoAsunto().getId() == null || registroDetalle.getTipoAsunto().getId().equals((long) -1) ){
          rejectValue(errors, "registroDetalle.tipoAsunto.id", "error.valor.requerido", "El camp és obligatori");
        }
        
        
        if (registroEntrada.getDestino() == null || registroEntrada.getDestino().getCodigo() == null || "".equals(registroEntrada.getDestino().getCodigo())) {
          rejectValue(errors, "destino.codigo", "error.valor.requerido", "El camp és obligatori");
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
