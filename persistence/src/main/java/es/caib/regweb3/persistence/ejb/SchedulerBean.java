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

    @EJB(mappedName = "regweb3/LibroEJB/local")
    private LibroLocal libroEjb;

    @EJB(mappedName = "regweb3/ContadorEJB/local")
    private ContadorLocal contadorEjb;

    @EJB(mappedName = "regweb3/IntegracionEJB/local")
    private IntegracionLocal integracionEjb;


    @Override
    public void purgarIntegraciones() throws Exception{

        List<Entidad> entidades = entidadEjb.getAll();

        for(Entidad entidad: entidades) {
            log.info(" ");
            log.info("------------- Purgando integraciones de " + entidad.getNombre() + " -------------");
            log.info(" ");
            integracionEjb.purgarIntegraciones(entidad.getId());
        }
    }

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

    @Override
    public void reiniciarContadoresEntidad() throws Exception {
        try{

            List<Entidad> entidades = entidadEjb.getAll();

            for(Entidad entidad: entidades) {
                log.info("Inicializando contadores de:" + entidad.getNombre());
                libroEjb.reiniciarContadoresEntidadTask(entidad.getId());
                contadorEjb.reiniciarContador(entidad.getContadorSir().getId());
            }

        } catch (Throwable e) {
            log.error("Error Inicializando contadores entidad ...", e);
        }

    }
}
