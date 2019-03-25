package es.caib.regweb3.persistence.ejb;

import es.caib.regweb3.model.Interesado;
import es.caib.regweb3.model.RegistroSalida;
import es.caib.regweb3.model.UsuarioEntidad;
import es.caib.regweb3.model.utils.AnexoFull;
import org.fundaciobit.genapp.common.i18n.I18NException;
import org.fundaciobit.genapp.common.i18n.I18NValidationException;
import org.jboss.ejb3.annotation.SecurityDomain;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import java.util.List;

/**
 * Created by mgonzalez on 06/03/2019.
 * Clase que representa un asiento Registral
 */
@Stateless(name = "AsientoRegistralEJB")
@SecurityDomain("seycon")
@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
public class AsientoRegistralBean implements AsientoRegistralLocal {

   @EJB(mappedName = "regweb3/RegistroSalidaEJB/local")
   public RegistroSalidaLocal registroSalidaEjb;




   @Override
   //TODO se deberá refactorizar para trabajar con asiento registral.
   public RegistroSalida registrarSalida(RegistroSalida registroSalida,
         UsuarioEntidad usuarioEntidad, List<Interesado> interesados, List<AnexoFull> anexos)
      throws Exception, I18NException, I18NValidationException {

      registroSalida = registroSalidaEjb.registrarSalida(registroSalida,usuarioEntidad,interesados,anexos);
      return registroSalida;
   }

}
