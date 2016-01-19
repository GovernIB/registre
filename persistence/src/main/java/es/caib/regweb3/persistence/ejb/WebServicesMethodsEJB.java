package es.caib.regweb3.persistence.ejb;

import es.caib.regweb3.model.PreRegistro;
import org.jboss.ejb3.annotation.SecurityDomain;

import javax.annotation.security.RunAs;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 * Created by Fundacio Bit
 *
 * @author earrivi
 */
@Stateless(name = "WebServicesMethodsEJB")
@SecurityDomain("seycon")
@RunAs("RWE_USUARI") //todo Revisar si se puede eliminar
public class WebServicesMethodsEJB implements WebServicesMethodsLocal {

    @EJB(mappedName = "regweb3/PreRegistroEJB/local")
    public PreRegistroLocal preRegistroEjb;


    @Override
    public PreRegistro crearPreRegistro(PreRegistro preRegistro) throws Exception {
        return preRegistroEjb.preRegistrar(preRegistro);
    }
}
