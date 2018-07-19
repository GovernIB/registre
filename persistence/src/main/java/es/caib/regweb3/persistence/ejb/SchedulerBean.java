package es.caib.regweb3.persistence.ejb;

import es.caib.regweb3.model.Entidad;
import es.caib.regweb3.persistence.utils.PropiedadGlobalUtil;
import org.apache.log4j.Logger;
import org.fundaciobit.genapp.common.i18n.I18NException;
import org.fundaciobit.genapp.common.i18n.I18NValidationException;
import org.jboss.ejb3.annotation.SecurityDomain;

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
@SecurityDomain("seycon")
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

    @EJB(mappedName = "regweb3/RegistroEntradaEJB/local")
    private RegistroEntradaLocal registroEntradaEjb;

    @EJB(mappedName = "regweb3/IntegracionEJB/local")
    private IntegracionLocal integracionEjb;

    @EJB(mappedName = "regweb3/ArxiuEJB/local")
    private ArxiuLocal arxiuEjb;

    @EJB(mappedName = "regweb3/AnexoEJB/local")
    private AnexoLocal anexoEjb;


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
            log.info("------------- SIR: Reintentado envios sin ack de " + entidad.getNombre() + " -------------");
            log.info(" ");
            sirEjb.reintentarEnviosSinConfirmacion(entidad.getId());
        }
    }

    @Override
    public void reintentarEnviosConError() throws Exception {

        List<Entidad> entidades = entidadEjb.getEntidadesSir();

        for(Entidad entidad: entidades) {
            log.info(" ");
            log.info("------------- SIR: Reintentado envios con errores de " + entidad.getNombre() + " -------------");
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


    /**
     * Inicia la distribución de los registros en cola de cada entidad.
     * @throws Exception
     */
    @Override
    public void distribuirRegistrosEnCola() throws Exception{

        try {
            List<Entidad> entidades = entidadEjb.getEntidadesActivas();

            for(Entidad entidad: entidades) {
                log.info(" ");
                log.info("------------- Distribucion: Procesando registros En Cola " + entidad.getNombre() + " -------------");
                log.info(" ");

                registroEntradaEjb.distribuirRegistrosEnCola(entidad.getId());
            }
        } catch (I18NException e) {
            log.error("Error Distribuyendo los registros de la entidad ...", e);
        }catch (I18NValidationException e) {
            log.error("Error Distribuyendo los registros de la entidad ...", e);
        }

    }

    /**
     * Cierra los expedientes que están en DM del Arxiu Digital del GOIB
     */
    @Override
    public void cerrarExpedientes(){

        try {
            List<Entidad> entidades = entidadEjb.getAll();

            for(Entidad entidad: entidades) {

                if(PropiedadGlobalUtil.getCerrarExpedientes(entidad.getId())){
                    log.info(" ");
                    log.info("------------- Cerrando expedientes en DM de la entidad: " + entidad.getNombre() + " -------------");
                    log.info(" ");

                    arxiuEjb.cerrarExpedientesScheduler(entidad.getId());

                    log.info(" ");
                    log.info("------------- FIN expedientes en DM de la entidad: " + entidad.getNombre() + " -------------");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
