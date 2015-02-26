package es.caib.regweb.persistence.validator;


import org.fundaciobit.genapp.common.validation.BeanValidatorResult;

import java.util.List;

import org.fundaciobit.genapp.common.i18n.I18NFieldError;
import org.fundaciobit.genapp.common.i18n.I18NException;

import es.caib.regweb.model.RegistroSalida;


/**
 * @author anadal
 */
public class RegistroSalidaBeanValidator extends AbstractRegWebBeanValidator<RegistroSalida> {

 

  public final RegistroSalidaValidator<RegistroSalida> _validator;


  public RegistroSalidaBeanValidator() { 
    this(new RegistroSalidaValidator<RegistroSalida>());
  }
  
  

  /**
   * @param _validator
   * 
   */
  public RegistroSalidaBeanValidator(RegistroSalidaValidator<RegistroSalida> _validator) {
    super();
    this._validator = _validator;
  }




  @Override
  public List<I18NFieldError> validate(RegistroSalida target, boolean isNou) throws I18NException {
    BeanValidatorResult<RegistroSalida> _bvr_ = new BeanValidatorResult<RegistroSalida>();
    _validator.validate(_bvr_, target, isNou);
    return _bvr_.getErrors();
  }
  
  @Override
  public AbstractRegWebValidator<RegistroSalida> getAbstractValidator() {
    return _validator;
  }
}
