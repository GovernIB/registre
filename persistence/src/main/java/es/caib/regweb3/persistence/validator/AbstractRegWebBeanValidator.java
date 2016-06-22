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
 * 
 * @author anadal
 *
 * @param <T>
 */
public abstract  class AbstractRegWebBeanValidator <T> extends AbstractBeanValidator<T> {

  public abstract AbstractRegWebValidator<T> getAbstractValidator();
  
  
  @Override
  public void throwValidationExceptionIfErrors(T target, boolean isNou)
      throws I18NValidationException, I18NException {
    
       
     
      List<I18NFieldError> fieldErrorList;
      fieldErrorList = this.validate(target, isNou);
      
      if (!fieldErrorList.isEmpty()) {
        
          List<I18NFieldError> fieldErrors = new ArrayList<I18NFieldError>(); 
          
          for(I18NFieldError error : fieldErrorList) {
              System.out.println("Error: " + error.getField().getCodeLabel());
              System.out.println("Error: " + error.getTranslation().getCode());


            /*
            System.out.println(" ============== INICI  =====================");

            
            
            System.out.println(" Class: " + error.getClass().getName());
            System.out.println(" Code: " + error.getCode());
            System.out.println(" DefMsg: " + error.getDefaultMessage());
            System.out.println(" ObjName: " + error.getObjectName());
            System.out.println(" Arguments: " + Arrays.toString(error.getArguments()));
            System.out.println(" Codes: " + Arrays.toString(error.getCodes()));
            */
            
            
            /*
            
            final String defaultMessage = error.getDefaultMessage();
            if (StringUtils.hasText(defaultMessage)) {          
              errorDescription.append(defaultMessage);
            } else {
              errorDescription.append(error.getCode());
            }
            errorDescription.append(']');
            */
            
            //String error = errorDescription.toString();
            // Nom del camp on hi ha l'error (ja traduit)
            
            //final String field = error.getField().getJavaName();
            
            
            // Taula
            final String table = null; //this.persistentClass.getSimpleName();
            String javaName = getAbstractValidator().getTranslationCode(error.getField());
            final String sqlName = javaName;
            
            IntegerField f = new IntegerField(table, javaName, sqlName);
            
            I18NTranslation trans = new I18NTranslation(error.getTranslation().getCode());

            I18NFieldError i18nErrorField = new I18NFieldError(f, trans);
            
            fieldErrors.add(i18nErrorField);
          }


          I18NValidationException valError = new I18NValidationException(fieldErrors);
          
          // it's an exception, so you can throw it if needed
          throw valError;
        
        
        
        //throw new I18NValidationException(fieldErrors);
      }
      
    }
  
  
}
