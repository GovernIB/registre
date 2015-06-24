package es.caib.regweb3.persistence.validator;


import es.caib.regweb3.model.RegistroEntrada;
import org.fundaciobit.genapp.common.i18n.I18NException;
import org.fundaciobit.genapp.common.i18n.I18NFieldError;
import org.fundaciobit.genapp.common.validation.BeanValidatorResult;

import java.util.List;


/**
 * @author anadal
 */
public class RegistroEntradaBeanValidator extends AbstractRegWebBeanValidator<RegistroEntrada> {

 

  public final RegistroEntradaValidator<RegistroEntrada> _validator;


  public RegistroEntradaBeanValidator() { 
    this(new RegistroEntradaValidator<RegistroEntrada>());
  }
  
  

  /**
   * @param _validator
   * @param registroEntradaEjb
   * @param registroEntradaEjb
   * @param catPaisEjb
   */
  public RegistroEntradaBeanValidator(RegistroEntradaValidator<RegistroEntrada> _validator) {
    super();
    this._validator = _validator;
  }




  @Override
  public List<I18NFieldError> validate(RegistroEntrada target, boolean isNou) throws I18NException {
    BeanValidatorResult<RegistroEntrada> _bvr_ = new BeanValidatorResult<RegistroEntrada>();
    _validator.validate(_bvr_, target, isNou);
    return _bvr_.getErrors();
  }
  
  @Override
  public AbstractRegWebValidator<RegistroEntrada> getAbstractValidator() {
    return _validator;
  }
}
