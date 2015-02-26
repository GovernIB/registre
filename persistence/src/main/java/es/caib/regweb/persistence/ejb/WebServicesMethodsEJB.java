package es.caib.regweb.persistence.ejb;

import es.caib.regweb.model.PreRegistro;
import es.caib.regweb.persistence.utils.Respuesta;

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

    @EJB(mappedName = "regweb/PreRegistroEJB/local")
    public PreRegistroLocal preRegistroEjb;


    @Override
    public Respuesta<PreRegistro> crearPreRegistro(String sicres3) throws Exception {
        return preRegistroEjb.crearPreRegistro(sicres3);
    }
}
