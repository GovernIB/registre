package es.caib.regweb3.persistence.validator;

import org.fundaciobit.genapp.common.query.Field;
import org.fundaciobit.genapp.common.query.StringField;
import org.fundaciobit.genapp.common.validation.IValidatorResult;

/**
 * 
 * @author anadal
 * 
 */
public abstract class AbstractRegWebValidator<T> {

  // private Class<T> persistentClass;

  public AbstractRegWebValidator() {
    /*
     * this.persistentClass = (Class<T>) ((ParameterizedType) getClass()
     * .getGenericSuperclass()).getActualTypeArguments()[0];
     */
  }

  /*
   * protected void validateStandalone(T object) throws I18NValidationException
   * {
   * 
   * 
   * PeticioDeFirmaBeanValidator pfbv = new
   * PeticioDeFirmaBeanValidator(validator, algorismeDeFirmaEjb,
   * custodiaInfoEjb, fluxDeFirmesLogicaEjb, idiomaEjb, this,
   * posicioTaulaFirmesEjb, prioritatEjb, tipusDocumentEjb,
   * tipusEstatPeticioDeFirmaEjb, tipusFirmaEjb, usuariAplicacioEjb,
   * usuariEntitatEjb);
   * 
   * final boolean isNou = true;
   * pfbv.throwValidationExceptionIfErrors(peticioDeFirma, isNou);
   * 
   * 
   * }
   */

  public abstract String prefixFieldName();

  public abstract String adjustFieldName(String fieldName);

  public boolean isNullOrEmpty(String value) {
    return (value == null) || (value.trim().length() == 0);
  }

  public String getTranslationCode(Field<?> f) {
    final String field = f.getJavaName();

    return prefixFieldName() + adjustFieldName(field);
  }

  public void rejectIfEmptyOrWhitespace(IValidatorResult<T> errors, T __target__,
      String field, String errorCode, String defaultValue) {
    StringField f =  getField(field);

    errors.rejectIfEmptyOrWhitespace(__target__, f, errorCode,
        new org.fundaciobit.genapp.common.i18n.I18NArgumentCode(getTranslationCode(f)));
  }

  public void rejectValue(IValidatorResult<T> errors, String field, String errorCode) {
    rejectValue(errors, field, errorCode, null);
  }

  public void rejectValue(IValidatorResult<T> errors, String field, String errorCode,
      String defaultValue) {

    errors.rejectValue( getField(field), errorCode);
  }
  
  public boolean hasFieldErrors(IValidatorResult<T> errors, String field) {
    return errors.getFieldErrorCount(getField(field)) != 0;
  }
  
  public StringField getField(String field) {
    return new StringField(null, field, field);
  }

}
