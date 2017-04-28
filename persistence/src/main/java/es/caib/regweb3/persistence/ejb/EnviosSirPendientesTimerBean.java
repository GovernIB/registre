package es.caib.regweb3.persistence.ejb;

import es.caib.regweb3.model.Entidad;
import es.caib.regweb3.persistence.utils.PropiedadGlobalUtil;
import es.caib.regweb3.utils.RegwebConstantes;
import org.apache.log4j.Logger;
import org.jboss.ejb3.annotation.SecurityDomain;

import javax.annotation.security.RunAs;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import java.util.List;

/**
 * Created by earrivi on 20/04/2017.
 */
@Stateless(name = "EnviosSirPendientesTimerEJB")
@SecurityDomain("seycon")
@RunAs("RWE_SUPERADMIN")
public class EnviosSirPendientesTimerBean extends AbstractTimerEJB implements EnviosSirPendientesTimerLocal{

    protected final Logger log = Logger.getLogger(getClass());

    @EJB(mappedName = "regweb3/EntidadEJB/local")
    public EntidadLocal entidadEjb;

    @EJB(mappedName = "regweb3/SirEJB/local")
    private SirLocal sirEjb;


    @Override
    public String getTimerName() {
        return "EnviosSirPendientesTimer";
    }

    @Override
    public String getCronExpression() {
        return PropiedadGlobalUtil.getEnviosSirPendientesCronExpression();
    }

    @Override
    public String getDefaultCronExpression() {
        return RegwebConstantes.CRON_ENVIOS_SIR_PENDIENTES;
    }

    @Override
    public void executeTask() {

        try{

            log.info(" -- ExecuteTask() de " + getTimerName() + " --------------");

            List<Entidad> entidades = entidadEjb.getEntidadesSir();

            for(Entidad entidad: entidades) {
                log.info("------------- Reintentado envios de " + entidad.getNombre() + " -------------");
                sirEjb.reintentarEnvios(entidad.getId());
            }

            log.info("Fin " + getTimerName());
        }catch (Throwable e) {
            log.error("Error enviando EnviosSirPendientesTimer: " + e.getMessage(), e);
        }

    }
}
