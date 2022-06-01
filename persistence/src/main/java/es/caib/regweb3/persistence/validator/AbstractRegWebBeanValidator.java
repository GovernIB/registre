package es.caib.regweb3.persistence.validator;

import org.fundaciobit.genapp.common.i18n.I18NException;
import org.fundaciobit.genapp.common.i18n.I18NFieldError;
import org.fundaciobit.genapp.common.i18n.I18NTranslation;
import org.fundaciobit.genapp.common.i18n.I18NValidationException;
import org.fundaciobit.genapp.common.query.IntegerField;
import org.fundaciobit.genapp.common.validation.AbstractBeanValidator;

import java.util.ArrayList;
import java.util.List;

/**
 * @param <T>
 * @author anadal
 */
public abstract class AbstractRegWebBeanValidator<T> extends AbstractBeanValidator<T> {

    public abstract AbstractRegWebValidator<T> getAbstractValidator();


    @Override
    public void throwValidationExceptionIfErrors(T target, boolean isNou)
            throws I18NValidationException, I18NException {


        List<I18NFieldError> fieldErrorList;
        fieldErrorList = this.validate(target, isNou);

        if (!fieldErrorList.isEmpty()) {

            List<I18NFieldError> fieldErrors = new ArrayList<I18NFieldError>();

            for (I18NFieldError error : fieldErrorList) {
                System.out.println("Error: " + error.getField().getCodeLabel());
                System.out.println("Error: " + error.getTranslation().getCode());


                // Taula
                final String table = null; //this.persistentClass.getSimpleName();
                String javaName = getAbstractValidator().getTranslationCode(error.getField());
                final String sqlName = javaName;

                IntegerField f = new IntegerField(table, javaName, sqlName);

                I18NTranslation trans = new I18NTranslation(error.getTranslation().getCode(), error.getTranslation().getArgs());

                I18NFieldError i18nErrorField = new I18NFieldError(f, trans);

                fieldErrors.add(i18nErrorField);
            }

            I18NValidationException valError = new I18NValidationException(fieldErrors);

            // it's an exception, so you can throw it if needed
            throw valError;
        }
    }
}
