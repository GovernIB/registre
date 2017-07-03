package es.caib.regweb3.persistence.ejb;

import es.caib.regweb3.model.Entidad;
import org.apache.log4j.Logger;

import javax.annotation.security.RunAs;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import java.util.List;

/**
 * Created by Fundació BIT.
 *
 * @author earrivi
 * Date: 16/01/14
 */

@Stateless(name = "SchedulerEJB")
/*@SecurityDomain("seycon")*/
@RunAs("RWE_USUARI")
public class SchedulerBean implements SchedulerLocal{

    protected final Logger log = Logger.getLogger(getClass());

    @EJB(mappedName = "regweb3/SirEJB/local")
    private SirLocal sirEjb;

    @EJB(mappedName = "regweb3/EntidadEJB/local")
    private EntidadLocal entidadEjb;


    @Override
    public void reintentarEnviosSinConfirmacion() throws Exception {

        List<Entidad> entidades = entidadEjb.getEntidadesSir();

        for(Entidad entidad: entidades) {
            log.info(" ");
            log.info("------------- Reintentado envios sin ack de " + entidad.getNombre() + " -------------");
            log.info(" ");
            sirEjb.reintentarEnviosSinConfirmacion(entidad.getId());
        }
    }

    @Override
    public void reintentarEnviosConError() throws Exception {

        List<Entidad> entidades = entidadEjb.getEntidadesSir();

        for(Entidad entidad: entidades) {
            log.info(" ");
            log.info("------------- Reintentado envios con errores de " + entidad.getNombre() + " -------------");
            log.info(" ");
            sirEjb.reintentarEnviosConError(entidad.getId());
        }
    }
}
