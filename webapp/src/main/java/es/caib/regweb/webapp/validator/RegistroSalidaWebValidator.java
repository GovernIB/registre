package es.caib.regweb.webapp.validator;

import org.apache.log4j.Logger;
import org.fundaciobit.genapp.common.web.validation.WebValidationResult;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import es.caib.regweb.model.RegistroSalida;
import es.caib.regweb.persistence.validator.RegistroSalidaValidator;

/**
 * 
 * @author anadal
 */
@Component
public class RegistroSalidaWebValidator implements Validator {

  protected final Logger log = Logger.getLogger(getClass());

  protected RegistroSalidaValidator<Object> validator = new RegistroSalidaValidator<Object>();


  public RegistroSalidaWebValidator() {
    super();    
  }
  
  @Override
  public boolean supports(Class<?> clazz) {
    return RegistroSalida.class.isAssignableFrom(clazz);
  }

  @Override
  public void validate(Object target, Errors errors) {

    WebValidationResult<Object> wvr;
    wvr = new WebValidationResult<Object>(errors);

    final boolean isNou = true;
    /*{
      Boolean nou = (Boolean)errors.getFieldValue("nou");
      boolean isNou =  nou != null && nou.booleanValue();
    } */

    validate(target, errors, wvr, isNou);
  }


  public void validate(Object target, Errors errors,
    WebValidationResult<Object> wvr, boolean isNou) {

    
    validator.validate(wvr, target, isNou);

  } // Final de metode


  public RegistroSalidaValidator<Object> getValidator() {
    return validator;
  }

  public void setValidator(RegistroSalidaValidator<Object> validator) {
    this.validator = validator;
  }

}