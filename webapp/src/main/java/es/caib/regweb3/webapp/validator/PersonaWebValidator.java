package es.caib.regweb3.webapp.validator;

import es.caib.regweb3.model.Persona;
import es.caib.regweb3.persistence.ejb.CatPaisLocal;
import es.caib.regweb3.persistence.ejb.PersonaLocal;
import es.caib.regweb3.persistence.validator.PersonaValidator;
import org.apache.log4j.Logger;
import org.fundaciobit.genapp.common.web.validation.WebValidationResult;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import javax.ejb.EJB;

/**
 * 
 * @author anadal
 */
@Component
public class PersonaWebValidator implements Validator {

  protected final Logger log = Logger.getLogger(getClass());

  protected PersonaValidator<Object> validator = new PersonaValidator<Object>();

  @EJB(mappedName = "regweb3/CatPaisEJB/local")
  protected CatPaisLocal catPaisEjb;

  @EJB(mappedName = "regweb3/PersonaEJB/local")
  protected PersonaLocal personaEjb;


  /**
   * @param _validator
   * @param personaEjb
   * @param personaEjb
   * @param catPaisEjb
   */
  public PersonaWebValidator() {
    super();
  }
  
  @Override
  public boolean supports(Class<?> clazz) {
    return Persona.class.isAssignableFrom(clazz);
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

    
    validator.validate(wvr, target, isNou,personaEjb, catPaisEjb);

  } // Final de metode


  public PersonaValidator<Object> getValidator() {
    return validator;
  }

  public void setValidator(PersonaValidator<Object> validator) {
    this.validator = validator;
  }

}