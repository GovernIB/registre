package es.caib.regweb3.persistence.validator;

import org.fundaciobit.genapp.common.i18n.I18NArgument;
import org.fundaciobit.genapp.common.query.Field;
import org.fundaciobit.genapp.common.query.StringField;
import org.fundaciobit.genapp.common.validation.IValidatorResult;

/**
 * 
 * @author anadal
 * 
 */
public abstract class AbstractRegWebValidator<T> {


  public AbstractRegWebValidator() {

  }

  public abstract String prefixFieldName();

  public abstract String adjustFieldName(String fieldName);

  public boolean isNullOrEmpty(String value) {
    return (value == null) || (value.trim().length() == 0);
  }

  public String getTranslationCode(Field<?> f) {
    final String field = f.getJavaName();

    return prefixFieldName() + adjustFieldName(field);
  }

  public void rejectIfEmptyOrWhitespace(IValidatorResult<T> errors, T __target__, String field, String errorCode) {
    StringField f =  getField(field);

    errors.rejectIfEmptyOrWhitespace(__target__, f, errorCode,
        new org.fundaciobit.genapp.common.i18n.I18NArgumentCode(getTranslationCode(f)));
  }

  public void rejectValue(IValidatorResult<T> errors, String field, String errorCode, I18NArgument ... args) {

    errors.rejectValue( getField(field), errorCode, args);
  }
  
  public boolean hasFieldErrors(IValidatorResult<T> errors, String field) {
    return errors.getFieldErrorCount(getField(field)) != 0;
  }
  
  public StringField getField(String field) {
    return new StringField(null, field, field);
  }

}
