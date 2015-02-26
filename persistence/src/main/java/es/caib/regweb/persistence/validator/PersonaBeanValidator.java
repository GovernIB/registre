package es.caib.regweb.persistence.validator;


import org.fundaciobit.genapp.common.validation.BeanValidatorResult;

import java.util.List;

import org.fundaciobit.genapp.common.i18n.I18NFieldError;
import org.fundaciobit.genapp.common.i18n.I18NException;

import es.caib.regweb.model.Persona;
import es.caib.regweb.persistence.ejb.CatPaisLocal;
import es.caib.regweb.persistence.ejb.PersonaLocal;

/**
 * @author anadal
 */
public class PersonaBeanValidator extends AbstractRegWebBeanValidator<Persona> {

  protected PersonaLocal personaEjb;

  protected CatPaisLocal catPaisEjb;
  

  public final PersonaValidator<Persona> _validator;


  public PersonaBeanValidator(PersonaLocal personaEjb, CatPaisLocal catPaisEjb) { 
    this(new PersonaValidator<Persona>(), personaEjb, catPaisEjb);
  }
  
  

  /**
   * @param _validator
   * @param personaEjb
   * @param personaEjb
   * @param catPaisEjb
   */
  public PersonaBeanValidator(PersonaValidator<Persona> _validator,
      PersonaLocal personaEjb, CatPaisLocal catPaisEjb) {
    super();
    this._validator = _validator;
    this.personaEjb = personaEjb;
    this.catPaisEjb = catPaisEjb;
  }




  @Override
  public List<I18NFieldError> validate(Persona target, boolean isNou) throws I18NException {
    BeanValidatorResult<Persona> _bvr_ = new BeanValidatorResult<Persona>();
    _validator.validate(_bvr_, target, isNou, personaEjb, catPaisEjb);
    return _bvr_.getErrors();
  }
  
  @Override
  public AbstractRegWebValidator<Persona> getAbstractValidator() {
    return _validator;
  }
}
