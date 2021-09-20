package es.caib.regweb3.webapp.validator;

import es.caib.regweb3.model.RegistroSalida;
import es.caib.regweb3.persistence.validator.RegistroSalidaValidator;
import org.fundaciobit.genapp.common.web.validation.WebValidationResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * 
 * @author anadal
 */
@Component
public class RegistroSalidaWebValidator implements Validator {

  protected final Logger log = LoggerFactory.getLogger(getClass());

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