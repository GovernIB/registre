package es.caib.regweb3.persistence.validator;


import es.caib.regweb3.model.Interesado;
import es.caib.regweb3.persistence.ejb.CatPaisLocal;
import es.caib.regweb3.persistence.ejb.InteresadoLocal;
import es.caib.regweb3.persistence.ejb.PersonaLocal;
import org.fundaciobit.genapp.common.i18n.I18NException;
import org.fundaciobit.genapp.common.i18n.I18NFieldError;
import org.fundaciobit.genapp.common.validation.BeanValidatorResult;

import java.util.List;

/**
 * @author anadal
 */
public class InteresadoBeanValidator extends AbstractRegWebBeanValidator<Interesado> {

  protected InteresadoLocal interesadoEjb;

  protected PersonaLocal personaEjb;

  protected CatPaisLocal catPaisEjb;
  

  public final InteresadoValidator<Interesado> _validator;


  public InteresadoBeanValidator(InteresadoLocal interesadoEjb,
      PersonaLocal personaEjb, CatPaisLocal catPaisEjb) { 
    this(new InteresadoValidator<Interesado>(), interesadoEjb, personaEjb, catPaisEjb);
  }
  
  

  /**
   * @param _validator
   * @param interesadoEjb
   * @param personaEjb
   * @param catPaisEjb
   */
  public InteresadoBeanValidator(InteresadoValidator<Interesado> _validator,
      InteresadoLocal interesadoEjb, PersonaLocal personaEjb, CatPaisLocal catPaisEjb) {
    super();
    this._validator = _validator;
    this.interesadoEjb = interesadoEjb;
    this.personaEjb = personaEjb;
    this.catPaisEjb = catPaisEjb;
  }




  @Override
  public List<I18NFieldError> validate(Interesado target, boolean isNou) throws I18NException {
    BeanValidatorResult<Interesado> _bvr_ = new BeanValidatorResult<Interesado>();
    _validator.validate(_bvr_, target, isNou, interesadoEjb, personaEjb, catPaisEjb);
    return _bvr_.getErrors();
  }
  
  @Override
  public AbstractRegWebValidator<Interesado> getAbstractValidator() {
    return _validator;
  }
}
