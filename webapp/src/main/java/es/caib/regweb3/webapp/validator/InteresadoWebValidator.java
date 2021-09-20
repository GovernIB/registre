package es.caib.regweb3.webapp.validator;

import es.caib.regweb3.model.Interesado;
import es.caib.regweb3.persistence.ejb.CatPaisLocal;
import es.caib.regweb3.persistence.ejb.InteresadoLocal;
import es.caib.regweb3.persistence.ejb.PersonaLocal;
import es.caib.regweb3.persistence.validator.InteresadoValidator;
import org.fundaciobit.genapp.common.web.validation.WebValidationResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import javax.ejb.EJB;

/**
 * 
 * @author anadal
 */
@Component
public class InteresadoWebValidator implements Validator {

  protected final Logger log = LoggerFactory.getLogger(getClass());

  protected InteresadoValidator<Object> validator = new InteresadoValidator<Object>();

  @EJB(mappedName = CatPaisLocal.JNDI_NAME)
  protected CatPaisLocal catPaisEjb;

  @EJB(mappedName = InteresadoLocal.JNDI_NAME)
  protected InteresadoLocal interesadoEjb;

  @EJB(mappedName = PersonaLocal.JNDI_NAME)
  protected PersonaLocal personaEjb;


  /**
   *
   */
  public InteresadoWebValidator() {
    super();
  }
  
  @Override
  public boolean supports(Class<?> clazz) {
    return Interesado.class.isAssignableFrom(clazz);
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

    
    validator.validate(wvr, target, isNou,interesadoEjb, personaEjb, catPaisEjb);

  } // Final de metode


  public InteresadoValidator<Object> getValidator() {
    return validator;
  }

  public void setValidator(InteresadoValidator<Object> validator) {
    this.validator = validator;
  }

}