package es.caib.regweb3.webapp.scheduler;


import es.caib.regweb3.persistence.ejb.SchedulerLocal;
import es.caib.regweb3.utils.Configuracio;
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
     * Qué hace: Purga las sesiones ws
     * Cuando lo hace: cada 60 minutos
     */
    @Scheduled(cron = "0 0 * * * *") //  {*/60 * * * * * cada 60 secs }
    public void purgarSesionesWs(){

        try {

            schedulerEjb.purgarSesionesWs();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

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
            schedulerEjb.enviarEmailErrorDistribucion();

        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            schedulerEjb.purgarProcesadosColaDistribucion();

        } catch (Exception e) {
            e.printStackTrace();
        }
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
     * Cuando lo hace: cada 25 minutos
     */
    @Scheduled(cron = "0 0/25 * * * *")
    public void reintentarIntercambiosSinConfirmacion(){

        try {
            schedulerEjb.reintentarIntercambiosSinConfirmacion();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * Qué hace: Reintenta enviar los Intercambios SIR con error
     * Cuando lo hace: en cada hora, se ejecuta a los 30 minutos despues de iniciada la hora y cada 60 minutos
     * que al coincidir con la hora en punto y tener un desplazamiento de 30 minutos, conseguimos que se ejecute a y 30 en cada hora).
     */
    @Scheduled(cron = "0 30/60 * * * *")
    public void reintentarIntercambiosConError(){

        try {
            schedulerEjb.reintentarIntercambiosConError();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    /**
     * Qué hace: Distribuye los registros que hay en la cola
     * Cuando lo hace: cada 30 minutos
     */
    @Scheduled(cron = "0 0/30 * * * *") // {0 0 * * * * Cada hora, cada día} -  {*/60 * * * * * cada 60 secs }
    public void distribuirRegistrosEnCola(){

        try {

            schedulerEjb.distribuirRegistrosEnCola();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Qué hace: Custodia los Justificantes que hay en la cola
     * Cuándo lo hace: cada 30 minutos
     */
    @Scheduled(cron = "0 0/30 * * * *") // {0 0 * * * * Cada hora, cada día} -  {*/60 * * * * * cada 60 secs }
    public void custodiarJustificantesEnCola(){

        try {

            schedulerEjb.custodiarJustificantesEnCola();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * Qué hace: purga los anexos de los registros distribuidos
     * Cuando lo hace: Cada 10 minutos a las 00:00, a las 02:00 y a las 03:00
     */
    @Scheduled(cron = "0 0/10 0,2,3 * * *") //
    public void purgarAnexosDistribuidos(){

        try {

            schedulerEjb.purgarAnexosDistribuidos();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    /**
     * Qué hace: purga los anexos de los registros sir que han sido aceptados
     * Cuando lo hace: Cada 13 minutos a las 05:00, 06:00 y 07:00
     */
    @Scheduled(cron = "0 0/13 5,6,7 * * *") //
    public void purgarAnexosSir(){

        try {

            schedulerEjb.purgarAnexosSir();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    /**
     * Qué hace: purga los anexos de los registros sir que han sido aceptados
     * Cuando lo hace: Cada 25 minutos a las 00:00, 02:00 y 03:00
     */
    @Scheduled(cron = "0 0/25 0,2,3 * * *") //
    public void purgarAnexosRegistrosConfirmados(){

        try {

            schedulerEjb.purgarAnexosRegistrosConfirmados();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    /**
     * Qué hace: Cierra los expedientes que están en DM del Arxiu del GOIB
     * Cuando lo hace: Desde las 00:00 hasta las 07:00 y desde las 15:00 hasta las 00:00 cada 15 minutos
     */
    @Scheduled(cron = "0 0/15 0,1,2,3,4,5,6,7,15,16,17,18,19,20,21,22,23 * * *") // 0 0/30 15-7 * * *   0 0/30 * * * *
    public void cerrarExpedientes(){
        try {

            if(Configuracio.isCAIB()){ // Solo si es una instalación GOIB
                schedulerEjb.cerrarExpedientes();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * Scheduler para realizar pruebas que se ejecutará cada 60 segundos
     */
   // @Scheduled(cron = "*/60 * * * * *") // **60 * * * * * cada 60 secs
    public void pruebas(){
        try {

        } catch (Exception e) {
            log.info("-- Error pruebas --");
            e.printStackTrace();
        }
    }

}
