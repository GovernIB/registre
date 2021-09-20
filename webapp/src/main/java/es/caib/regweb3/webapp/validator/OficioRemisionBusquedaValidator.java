package es.caib.regweb3.webapp.validator;

import es.caib.regweb3.webapp.form.OficioRemisionBusquedaForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class OficioRemisionBusquedaValidator implements Validator {

   protected final Logger log = LoggerFactory.getLogger(getClass());


   @Override
   public boolean supports(Class<?> clazz) {
      return OficioRemisionBusquedaForm.class.equals(clazz);
   }

   @Override
   public void validate(Object o, Errors errors) {

   }



}
