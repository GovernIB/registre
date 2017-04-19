package es.caib.regweb3.persistence.ejb;

import es.caib.regweb3.sir.core.utils.FicheroIntercambio;
import es.caib.regweb3.sir.core.utils.Mensaje;
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

    @EJB(mappedName = "regweb3/SirEJB/local")
    private SirLocal sirEjb;

    @Override
    public void recibirMensajeDatosControl(Mensaje mensaje) throws Exception{
        sirEjb.recibirMensajeDatosControl(mensaje);
    }

    @Override
    public void recibirFicheroIntercambio(FicheroIntercambio ficheroIntercambio) throws Exception{
        sirEjb.recibirFicheroIntercambio(ficheroIntercambio);
    }
}
