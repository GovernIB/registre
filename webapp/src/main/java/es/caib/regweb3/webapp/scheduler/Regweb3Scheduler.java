package es.caib.regweb3.webapp.scheduler;


import es.caib.regweb3.persistence.ejb.SchedulerLocal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    protected final Logger log = LoggerFactory.getLogger(getClass());

    @EJB(mappedName = SchedulerLocal.JNDI_NAME)
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

        try {
            schedulerEjb.purgarProcesadosColaDistribucion();

        } catch (Exception e) {
            e.printStackTrace();
        }

        /*try {
            schedulerEjb.enviarEmailErrorDistribucion();

        } catch (Exception e) {
            e.printStackTrace();
        }*/
    }

    /**
     * Qué hace: Genera las comunicaciones automáticas a los usuarios
     * Cuando lo hace: Cada primer día de mes
     */
    @Scheduled(cron = "0 0 0 1 * ?")
    public void generarComunicaciones(){

        try {
            schedulerEjb.generarComunicaciones();

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
     * Qué hace: Reintenta enviar los Intercambios SIR sin confirmación
     * Cuando lo hace: cada 30 minutos, a los 5 minutos de iniciarse el servidor.
     */
    @Scheduled(fixedDelay = 1800000, initialDelay = 300000)
    public void reintentarIntercambiosSinConfirmacion(){

        try {
            schedulerEjb.reintentarIntercambiosSinConfirmacion();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * Qué hace: Reintenta enviar los Intercambios SIR con error
     * Cuando lo hace: cada 30 minutos, a los 10 minutos de haberse iniciado el servidor.
     */
    @Scheduled(fixedDelay = 1800000, initialDelay = 600000)
    public void reintentarIntercambiosConError(){

        try {
            schedulerEjb.reintentarIntercambiosConError();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    /**
     * Qué hace: Distribuye los registros que hay en la cola
     * Cuando lo hace: cada 20 minutos, a los 5 minutos de haberse iniciado el servidor.
     */
    @Scheduled(fixedDelay = 1200000, initialDelay = 300000)
    public void distribuirRegistrosEnCola(){

        try {

            schedulerEjb.distribuirRegistrosEnCola();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Qué hace: Custodia los Justificantes que hay en la cola
     * Cuándo lo hace: cada 20 minutos desde las 02:00 hasta las 00:00
     * (de 01:00h a 02:00h hay una parada por Backup de Arxiu)
     */
    @Scheduled(cron = "0 0/20 2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,0 * * *")
    public void custodiarJustificantesEnCola(){

        try {

            schedulerEjb.custodiarJustificantesEnCola();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * Qué hace: purga los anexos de los registros distribuidos
     * Cuando lo hace: Cada 20 minutos a las 00:00, a las 01:00 y a las 02:00
     */
    /*@Scheduled(cron = "0 0/20 0,1,2 * * *") //
    public void purgarAnexosDistribuidos(){

        try {

            schedulerEjb.purgarAnexosDistribuidos();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }*/


    /**
     * Qué hace: purga los anexos de los registros sir que han sido aceptados
     * Cuando lo hace: Cada 20 minutos a las 03:00, 04:00 y 05:00
     */
    /*@Scheduled(cron = "0 0/20 3,4,5 * * *") //
    public void purgarAnexosSir(){

        try {

            schedulerEjb.purgarAnexosSir();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/


    /**
     * Qué hace: purga los anexos de los registros sir que han sido aceptados
     * Cuando lo hace: Cada 20 minutos a las 00:00, 02:00 y 03:00
     */
    /*@Scheduled(cron = "0 0/20 6,7,8 * * *") //
    public void purgarAnexosRegistrosConfirmados(){

        try {

            schedulerEjb.purgarAnexosRegistrosConfirmados();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/
}
