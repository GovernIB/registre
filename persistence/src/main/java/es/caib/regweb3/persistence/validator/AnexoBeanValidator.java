package es.caib.regweb3.persistence.validator;


import es.caib.regweb3.model.Anexo;
import org.fundaciobit.genapp.common.i18n.I18NException;
import org.fundaciobit.genapp.common.i18n.I18NFieldError;
import org.fundaciobit.genapp.common.validation.BeanValidatorResult;

import java.util.List;

/**
 * @author anadal
 */
public class AnexoBeanValidator extends AbstractRegWebBeanValidator<Anexo> {



  public final AnexoValidator<Anexo> _validator;


  public AnexoBeanValidator() { 
    _validator = new AnexoValidator<Anexo>();
  }

  public AnexoBeanValidator(AnexoValidator<Anexo> _validator) {
    this._validator = _validator;
  }

  @Override
  public List<I18NFieldError> validate(Anexo target, boolean isNou) throws I18NException {
    BeanValidatorResult<Anexo> _bvr_ = new BeanValidatorResult<Anexo>();
    _validator.validate(_bvr_, target, isNou);
    return _bvr_.getErrors();
  }
  
  @Override
  public AbstractRegWebValidator<Anexo> getAbstractValidator() {
    return _validator;
  }
}
