package es.caib.regweb3.persistence.validator;

import es.caib.regweb3.model.Anexo;
import org.apache.log4j.Logger;
import org.fundaciobit.genapp.common.validation.IValidatorResult;


/**
 * Created by Fundació BIT.
 * Gestiona las Validaciones del formulario para crear o editar una {@link es.caib.regweb3.model.Anexo}
 * @author earrivi
 * @author anadal (adaptació sense Spring)
 * Date: 11/02/14
 */
public class AnexoValidator<T> extends AbstractRegWebValidator<T> {

  protected final Logger log = Logger.getLogger(getClass());


  /** Constructor */
  public AnexoValidator() {
    super();    
  }
  

  
  public void validate(IValidatorResult<T> errors, T __target__, boolean __isNou__) {

    // Valors Not Null
    /* __vr.rejectIfEmptyOrWhitespace(__target__,PETICIODEFIRMAID, 
        "genapp.validation.required",
        new org.fundaciobit.genapp.common.i18n.I18NArgumentCode(get(PETICIODEFIRMAID)));
    */   
 
    
    /*
    if (__isNou__) { // Creació
      // ================ CREATION
      // Fitxers
      if (__vr.getFieldErrorCount(FITXERID) == 0) { // FITXER
        Object __value = __vr.getFieldValue(__target__, FITXERID);
        if (__value == null || String.valueOf(__value).trim().length() == 0
            || String.valueOf(__value).trim().equals("0")) {
          __vr.rejectValue(FITXERID, "genapp.validation.required",
              new org.fundaciobit.genapp.common.i18n.I18NArgumentCode(get(FITXERID)));
        }
      }

    } else {
      // ================ UPDATE

    }
    */
    
    Anexo anexo = (Anexo)__target__;

    //todo: Verificar NTI catálogo estándares según el tipo mime
    if(anexo.getTipoDocumento()== null || anexo.getTipoDocumento().equals((long) -1)){
       rejectValue(errors, "tipoDocumento", "error.valor.requerido"); // , "El camp és obligatori"
    }

    if(anexo.getTipoDocumental() == null){
        rejectValue(errors, "tipoDocumental", "error.valor.requerido", "El camp és obligatori");
    }else{
      if(anexo.getTipoDocumental().getId()== -1){
          rejectValue(errors, "tipoDocumental", "error.valor.requerido", "El camp és obligatori");
      }
    }


    if(anexo.getOrigenCiudadanoAdmin() == null && anexo.getOrigenCiudadanoAdmin()!= 0 && anexo.getOrigenCiudadanoAdmin() != 1){
       rejectValue(errors,"origenCiudadanoAdmin", "error.valor.requerido", "El camp és obligatori");
    }

    //if (anexo.getTitulo() == null || anexo.getTitulo().trim().length() == 0) {
    //  rejectValue(errors,"titulo", "error.valor.requerido", "El camp és obligatori");
    //}
    
    // TODO NO VA  
    rejectIfEmptyOrWhitespace(errors, __target__, "titulo", "error.valor.requerido", "El camp és obligatori");

    if(anexo.getTitulo() != null && anexo.getTitulo().length()>200 ){
       rejectValue(errors, "titulo", "error.valor.maxlenght", "El camp és massa llarg, màxim 200 caracters");
    }

    
  } // Final de mètode
  
  
  

  

  
  @Override
  public String prefixFieldName() {
      return "anexo.";
  }

  @Override
  public String adjustFieldName(String fieldName) {
      if (fieldName == null ) {
        return null;
      } else {
        if (fieldName.endsWith(".id")) {
          return fieldName.substring(0, fieldName.lastIndexOf('.'));
        }
      }
      return fieldName;
  }
  
  
}


