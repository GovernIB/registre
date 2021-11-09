package es.caib.regweb3.webapp.scheduler;


import es.caib.regweb3.persistence.ejb.PropiedadGlobalLocal;
import es.caib.regweb3.persistence.ejb.SchedulerLocal;
import es.caib.regweb3.persistence.utils.PropiedadGlobalUtil;
import es.caib.regweb3.utils.Configuracio;
import es.caib.regweb3.utils.RegwebConstantes;

import org.apache.log4j.Logger;
import org.fundaciobit.genapp.common.i18n.I18NException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.TriggerContext;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.support.PeriodicTrigger;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import javax.ejb.EJB;

/**
 * Created by Fundació BIT,  Limit Tecnologies.
 *
 * @author earrivi, Limit Tecnologies <limit@limit.es>
 * 
 */
@Service
@Configuration
@EnableScheduling
public class Regweb3Scheduler implements SchedulingConfigurer {

    protected final Logger log = Logger.getLogger(getClass());

    @EJB(mappedName = "regweb3/SchedulerEJB/local")
    private SchedulerLocal schedulerEjb;
    @EJB(mappedName = "regweb3/PropiedadGlobalEJB/local")
    private PropiedadGlobalLocal propiedadGlobalEjb;
    
    @Autowired
    TaskScheduler taskScheduler;
    
    private Boolean[] primeraVez = {Boolean.TRUE};

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
     * Cuando lo hace: en cada hora, se ejecuta a los 15 minutos despues de iniciada la hora y cada 45 minutos
     * que al coincidir con la hora en punto y tener un desplazamiento de 15 minutos, conseguimos que se ejecute a y 15 en cada hora).
     */
    @Scheduled(cron = "0 15/45 * * * *")
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
//    @Scheduled(cron = "*/60 * * * * *") // **60 * * * * * cada 60 secs
    public void pruebas(){
        try {        	
        	log.info("test");
        } catch (Exception e) {
            log.info("-- Error pruebas --");
            e.printStackTrace();
        }
    }
    
    /**
     * Qué hace: Actualiza el estado de los envíos SIR con el nuevo estaado de GEISER. Solo actualiza estado envíos con estado no final.
     * Cuando lo hace: cada 15 minutos
     */
//    @Scheduled(cron = "0 0/10 * * * *")
//    public void actualizarEstadoEnviosSir(){
//        try {
//            schedulerEjb.actualizarEnviosSIR();
//        } catch (Exception e) {
//            e.printStackTrace();
//        } catch (I18NException e) {
//			e.printStackTrace();
//		}
//    }
 
    /**
     * Qué hace: Consulta a GEISER los registros SIR recibidos y los crea en Regweb.
     * Cuando lo hace: cada 20 minutos
     */
//    @Scheduled(cron = "0 0/10 * * * *")
//    public void consultarICrearRegistrosRecibidos(){
//        try {
//            schedulerEjb.consultarICrearRegistrosRecibidos();
//        } catch (Exception e) {
//            e.printStackTrace();
//        } catch (I18NException e) {
//			e.printStackTrace();
//		}
//    }
    
    /**
     * Qué hace: Consulta el identificador de intercambio de los registros recibidos.
     * Cuando lo hace: cada 25 minutos
     */
//    @Scheduled(cron = "0 0/20 * * * *")
//    public void actualizarIdEnviosSirRecibidos(){
//        try {
//            schedulerEjb.actualizarIdEnviosSirRecibidos();
//        } catch (Exception e) {
//            e.printStackTrace();
//        } catch (I18NException e) {
//			e.printStackTrace();
//		}
//    }
    
	@Override
	public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
		taskRegistrar.setScheduler(taskScheduler);
		
		// Actualiza el estado de los envíos SIR con el nuevo estaado de GEISER. Solo actualiza estado envíos con estado no final.
        ////////////////////////////////////////////////////////////////
        taskRegistrar.addTriggerTask(
                new Runnable() {
                    @Override
                    public void run() {
                    	try {
							schedulerEjb.actualizarEnviosSIR();
						} catch (Exception e) {
							e.printStackTrace();
						} catch (I18NException e) {
							e.printStackTrace();
						}
                    }
                },
                new Trigger() {
                    @Override
                    public Date nextExecutionTime(TriggerContext triggerContext) {
						Long periodo = schedulerEjb.getCronTareaPeriodoActualizacionEnviosSir();
						if (periodo != null) {
							PeriodicTrigger trigger = new PeriodicTrigger(periodo, TimeUnit.MILLISECONDS);
							trigger.setFixedRate(true);
							// Només la primera vegada que s'executa
							Long registrarEnviamentsPendentsInitialDelayLong = 0L;
							if (primeraVez[0]) {
								registrarEnviamentsPendentsInitialDelayLong = schedulerEjb.getCronTareaRetardoActualizacionEnviosSir();
								primeraVez[0] = false;
							}
							trigger.setInitialDelay(registrarEnviamentsPendentsInitialDelayLong);
							Date nextExecution = trigger.nextExecutionTime(triggerContext);
							return nextExecution;
						}
						return null;
                    }
                }
        );
        // Consulta a GEISER los registros SIR recibidos y los crea en Regweb.
        ////////////////////////////////////////////////////////////////
        taskRegistrar.addTriggerTask(
                new Runnable() {
                    @Override
                    public void run() {
                    	try {
                    		schedulerEjb.consultarICrearRegistrosRecibidos();
						} catch (Exception e) {
							e.printStackTrace();
						} catch (I18NException e) {
							e.printStackTrace();
						}
                    }
                },
                new Trigger() {
                    @Override
                    public Date nextExecutionTime(TriggerContext triggerContext) {
						Long periodo = schedulerEjb.getCronTareaPeriodoActualizacionEnviosRecibidosSir();
						if (periodo != null) {
							PeriodicTrigger trigger = new PeriodicTrigger(periodo, TimeUnit.MILLISECONDS);
							trigger.setFixedRate(true);
							// Només la primera vegada que s'executa
							Long registrarEnviamentsPendentsInitialDelayLong = 0L;
							if (primeraVez[0]) {
								registrarEnviamentsPendentsInitialDelayLong = schedulerEjb.getCronTareaRetardoActualizacionEnviosRecibidosSir();
								primeraVez[0] = false;
							}
							trigger.setInitialDelay(registrarEnviamentsPendentsInitialDelayLong);
							Date nextExecution = trigger.nextExecutionTime(triggerContext);
							return nextExecution;
						}
						return null;
                    }
                }
        );
        // Consulta el identificador de intercambio de los registros recibidos.
        ////////////////////////////////////////////////////////////////
        taskRegistrar.addTriggerTask(
                new Runnable() {
                    @Override
                    public void run() {
                    	try {
                    		schedulerEjb.actualizarIdEnviosSirRecibidos();
						} catch (Exception e) {
							e.printStackTrace();
						} catch (I18NException e) {
							e.printStackTrace();
						}
                    }
                },
                new Trigger() {
                    @Override
                    public Date nextExecutionTime(TriggerContext triggerContext) {
						Long periodo = schedulerEjb.getCronTareaPeriodoActualizacionIdEnviosRecibidosSir();
						if (periodo != null) {
							PeriodicTrigger trigger = new PeriodicTrigger(periodo, TimeUnit.MILLISECONDS);
							trigger.setFixedRate(true);
							// Només la primera vegada que s'executa
							Long registrarEnviamentsPendentsInitialDelayLong = 0L;
							if (primeraVez[0]) {
								registrarEnviamentsPendentsInitialDelayLong = schedulerEjb.getCronTareaRetardoActualizacionIdEnviosRecibidosSir();
								primeraVez[0] = false;
							}
							trigger.setInitialDelay(registrarEnviamentsPendentsInitialDelayLong);
							Date nextExecution = trigger.nextExecutionTime(triggerContext);
							return nextExecution;
						}
						return null;
                    }
                }
        );
	}
}
