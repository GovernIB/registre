package es.caib.regweb3.webapp.scheduler;


import es.caib.regweb3.persistence.ejb.SchedulerLocal;
import org.apache.log4j.Logger;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.ejb.EJB;

/**
 * Created by Fundació BIT.
 *
 * @author earrivi
 * Date: 20/03/2018
 */
@Service
public class Regweb3Scheduler {

    protected final Logger log = Logger.getLogger(getClass());

    @EJB(mappedName = "regweb3/SchedulerEJB/local")
    private SchedulerLocal schedulerEjb;


    /**
     * Qué hace: Realiza tareas administrativas generales de la aplicación
     * Cuando lo hace: Todos días, a las 01:00 h.
     */
    @Scheduled(cron = "0 0 1 * * *") // 0 0 1 * * * Cada día a las 01:00h
    public void tareasAdministrativas(){
        try {
            schedulerEjb.purgarIntegraciones();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Qué hace: Inicializa a 0 los contadores de todos los libros de todas las entidades
     * Cuando lo hace: Todos los 1 de Enero a las 00:00:00 h.
     */
    @Scheduled(cron = "0 0 0 1 1 ?")
    public void inicializarContadores(){

        try {
            schedulerEjb.reiniciarContadoresEntidad();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * Qué hace: Reintenta enviar los Registros sin confirmación o con error
     * Cuando lo hace: Todos días, cada hora.
     */
    @Scheduled(cron = "0 0 * * * *") // {0 0 * * * * Cada hora, cada día} -  {*/60 * * * * * cada 60 secs }
    public void reintentarEnvioSir(){

        try {
            schedulerEjb.reintentarEnviosSinConfirmacion();
            schedulerEjb.reintentarEnviosConError();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    /**
     * Qué hace: Distribuye los registros que hay en la cola
     * Cuando lo hace: cada 5 minutos
     */
    @Scheduled(cron = "0 0/5 * * * *") // {0 0 * * * * Cada hora, cada día} -  {*/60 * * * * * cada 60 secs }
    public void distribuirRegistrosEnCola(){

        try {

            schedulerEjb.distribuirRegistros();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * Qué hace: Cierra los expedientes que están en DM del Arxiu del GOIB
     * Cuando lo hace: Cada 30 minutos
     */
    @Scheduled(cron = "0 0/30 15-7 * * *") // 0 0/30 * * * *
    public void cerrarExpedientes(){
        try {
            schedulerEjb.cerrarExpedientes();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
