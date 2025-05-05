package es.caib.regweb3.webapp.scheduler;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import javax.ejb.EJB;

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
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.scheduling.support.PeriodicTrigger;
import org.springframework.stereotype.Service;

import es.caib.regweb3.persistence.ejb.PropiedadGlobalLocal;
import es.caib.regweb3.persistence.ejb.SchedulerLocal;
import es.caib.regweb3.persistence.utils.MonitorTareas;
import es.caib.regweb3.utils.Configuracio;

/**
 * Created by Fundació BIT,  Limit Tecnologies.
 *
 * @author earrivi, Limit Tecnologies <limit@limit.es>
 * 
 *  InitializingBean, DisposableBean
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
    private TaskScheduler taskScheduler;
    @Autowired
    private MonitorTareas monitorTareas;
    
    private Boolean primeraVez = Boolean.TRUE;

    private Map<String, Runnable> taskMap = new HashMap<>();
    
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

	@Override
	public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
		taskRegistrar.setScheduler(taskScheduler);
		
		// Actualiza el estado de los envíos SIR con el nuevo estaado de GEISER. Solo actualiza estado envíos con estado no final.
        ////////////////////////////////////////////////////////////////
		
		addScheduledTask(taskRegistrar, "tareaActualizarEnviosSir", 
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
                    	Long periodo = 600000L;
                    	try {
                    		periodo = schedulerEjb.getCronTareaPeriodoActualizacionEnviosSir();
                    	} catch (Exception e) {
                    		log.warn("Hi ha hagut un error recuperant el periode getCronTareaPeriodoActualizacionEnviosSir. Agafam valor per defecte 600000L.");
                    	}
                    	
						if (periodo != null) {
							PeriodicTrigger trigger = new PeriodicTrigger(periodo, TimeUnit.MILLISECONDS);
							trigger.setFixedRate(true);
							// Només la primera vegada que s'executa
							Long actualizarEnviosSirInitialDelayLong = 0L;
							if (primeraVez) {
								actualizarEnviosSirInitialDelayLong = schedulerEjb.getCronTareaRetardoActualizacionEnviosSir();
								primeraVez = false;
							}
							trigger.setInitialDelay(actualizarEnviosSirInitialDelayLong);
							Date nextExecution = trigger.nextExecutionTime(triggerContext);
							return nextExecution;
						}
						return null;
                    }
                }
        );
        // Consulta a GEISER los registros SIR recibidos y los crea en Regweb.
        ////////////////////////////////////////////////////////////////
		addScheduledTask(taskRegistrar, "tareaConsultarRegistrosRecibidos", 
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
                    	Long periodo = 600000L;
                    	try {
                    		periodo = schedulerEjb.getCronTareaPeriodoActualizacionEnviosRecibidosSir();
                    	} catch (Exception e) {
                    		log.warn("Hi ha hagut un error recuperant el periode getCronTareaPeriodoActualizacionEnviosRecibidosSir. Agafam valor per defecte 600000L.");
                    	}
						if (periodo != null) {
							PeriodicTrigger trigger = new PeriodicTrigger(periodo, TimeUnit.MILLISECONDS);
							trigger.setFixedRate(true);
							// Només la primera vegada que s'executa
							Long registrarEnviosPendientesInitialDelayLong = 0L;
							if (primeraVez) {
								registrarEnviosPendientesInitialDelayLong = schedulerEjb.getCronTareaRetardoActualizacionEnviosRecibidosSir();
								primeraVez = false;
							}
							trigger.setInitialDelay(registrarEnviosPendientesInitialDelayLong);
							Date nextExecution = trigger.nextExecutionTime(triggerContext);
							return nextExecution;
						}
						return null;
                    }
                }
        );
        // Consulta el identificador de intercambio de los registros recibidos.
        ////////////////////////////////////////////////////////////////
		addScheduledTask(taskRegistrar, "tareaActualizarIdEnviosSir", 
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
                    	Long periodo = 900000L;
                    	try {
                    		periodo = schedulerEjb.getCronTareaPeriodoActualizacionIdEnviosRecibidosSir();
                    	} catch (Exception e) {
                    		log.warn("Hi ha hagut un error recuperant el periode getCronTareaPeriodoActualizacionEnviosRecibidosSir. Agafam valor per defecte 900000L.");
                    	}
						if (periodo != null) {
							PeriodicTrigger trigger = new PeriodicTrigger(periodo, TimeUnit.MILLISECONDS);
							trigger.setFixedRate(true);
							// Només la primera vegada que s'executa
							Long actualizarIdEnvioSirInitialDelayLong = 0L;
							if (primeraVez) {
								actualizarIdEnvioSirInitialDelayLong = schedulerEjb.getCronTareaRetardoActualizacionIdEnviosRecibidosSir();
								primeraVez = false;
							}
							trigger.setInitialDelay(actualizarIdEnvioSirInitialDelayLong);
							Date nextExecution = trigger.nextExecutionTime(triggerContext);
							return nextExecution;
						}
						return null;
                    }
                }
        );
        
        // Consulta anexos pendientes verificación firma electrónica
        ////////////////////////////////////////////////////////////////
		addScheduledTask(taskRegistrar, "tareaActualizarAnexosPendientes", 
                new Runnable() {
                    @Override
                    public void run() {
                    	try {
                    		schedulerEjb.actualizarAnexosPendientesVerificacionFirma();
                    		

                    		log.info("------------- ANEXOS: Actualización fecha última ejecución -------------");

                    		monitorTareas.actualizarUltimaEjecucion();
                    		
                    		log.info("------------- ANEXOS: Fecha última ejecución finalizada " + " -------------");
						} catch (Exception ex) {
							log.error("Error en la ejecución de la tarea: ", ex);
						} catch (I18NException I18N) {
							log.error("Error en la ejecución de la tarea: ", I18N);
						} catch (Throwable t) {
							log.error("Error crítico en la tarea, posible detención: ", t);
						}
                    }
                },
                new Trigger() {
                    @Override
                    public Date nextExecutionTime(TriggerContext triggerContext) {
                    	Long periodo = 300000L;
                    	try {
                    		periodo = schedulerEjb.getCronTareaPeriodoActualizacionAnexosPendientesVerificacionFirma();
                    	} catch (Exception e) {
                    		log.warn("Hi ha hagut un error recuperant el periode getCronTareaPeriodoActualizacionAnexosPendientesVerificacionFirma. Agafam valor per defecte 300000L.");
                    	}
						if (periodo != null) {
							PeriodicTrigger trigger = new PeriodicTrigger(periodo, TimeUnit.MILLISECONDS);
							trigger.setFixedRate(true);
							// Només la primera vegada que s'executa
							Long actualizarFirmaAnexosPendientesVerInitialDelayLong = 0L;
							if (primeraVez) {
								actualizarFirmaAnexosPendientesVerInitialDelayLong = schedulerEjb.getCronTareaRetardoActualizacionAnexosPendientesVerificacionFirma();
								primeraVez = false;
							}
							trigger.setInitialDelay(actualizarFirmaAnexosPendientesVerInitialDelayLong);
							Date nextExecution = trigger.nextExecutionTime(triggerContext);
							return nextExecution;
						}
						return null;
                    }
                }
        );
        
        // Localiza notificaciones y comunicacione pendientes
        ////////////////////////////////////////////////////////////////
		addScheduledTask(taskRegistrar, "tareaLocalizarNotificaciones",
                new Runnable() {
                    @Override
                    public void run() {
                    	try {
                    		schedulerEjb.localizarIGuardarNotificaciones();
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
                    	Long periodo = 1800000L;
                    	try {
                    		periodo = schedulerEjb.getCronTareaPeriodoConsultaNotificacionesDehu();
                    	} catch (Exception e) {
                    		log.warn("Hi ha hagut un error recuperant el periode getCronTareaPeriodoActualizacionAnexosPendientesVerificacionFirma. Agafam valor per defecte 1800000L.");
                    	}
						if (periodo != null) {
							PeriodicTrigger trigger = new PeriodicTrigger(periodo, TimeUnit.MILLISECONDS);
							trigger.setFixedRate(true);
							// Només la primera vegada que s'executa
							Long localizaNotificacionesPendientesInitialDelayLong = 0L;
							if (primeraVez) {
								localizaNotificacionesPendientesInitialDelayLong = schedulerEjb.getCronTareaRetardoConsultaNotificacionesDehu();
								primeraVez = false;
							}
							trigger.setInitialDelay(localizaNotificacionesPendientesInitialDelayLong);
							Date nextExecution = trigger.nextExecutionTime(triggerContext);
							return nextExecution;
						}
						return null;
                    }
                }
        );
        
		addScheduledTask(taskRegistrar, "tareaEnvioCorreoPendientes", 
                new Runnable() {
                    @Override
                    public void run() {
                    	try {
                    		log.info("------------- LEMA: Envío correo remesas pendientes de procesar -------------");
                    		
                    		schedulerEjb.enviarCorreoInformandoPendientes();
                    		
                    		log.info("------------- LEMA: Envío correo remesas pendientes de procesar finalizado -------------");
						} catch (Exception e) {
							e.printStackTrace();
						}
                    }
                },
                new Trigger() {
                    @Override
                    public Date nextExecutionTime(TriggerContext triggerContext) {
                    	String horaEjecucionStr = "14:00";
                    	try {
                    		horaEjecucionStr = schedulerEjb.getHoraEnvioCorreoNotificacionesDehu();
                    	} catch (Exception e) {
                    		log.warn("Hi ha hagut un error recuperant el periode getHoraEnvioCorreoNotificacionesDehu. Agafam valor per defecte 14:00.");
                    	}
                        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
                        Calendar calendar = Calendar.getInstance();
                        try {
                        	if (horaEjecucionStr != null)
                        		calendar.setTime(sdf.parse(horaEjecucionStr));
                        	else
                        		calendar.setTime(sdf.parse("14:00"));
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

                        // Generamos la expresión cron
                        int hour = calendar.get(Calendar.HOUR_OF_DAY);
                        int minute = calendar.get(Calendar.MINUTE);
                        
                        String horaExecucio = String.format("0 %d %d * * ?", minute, hour); // Expresión cron
                        
                    	CronTrigger cronTrigger = new CronTrigger(horaExecucio, TimeZone.getTimeZone("Europe/Madrid"));
                        return cronTrigger.nextExecutionTime(triggerContext);
                    }
                }
		);

	}
	
    // Método para agregar una tarea programada al map
    private void addScheduledTask(ScheduledTaskRegistrar taskRegistrar, String taskName, Runnable task, Trigger trigger) {
        taskRegistrar.addTriggerTask(task, trigger);
        taskMap.put(taskName, task); // Guardamos la tarea con su nombre en el map
    }

    // Método para reiniciar una tarea usando su nombre
    public void reiniciarTarea(String taskName) {
        if (taskMap.containsKey(taskName)) {
            Runnable task = taskMap.get(taskName);
            taskScheduler.schedule(task, new Date());  // Reinicia la tarea
            log.info("Tarea " + taskName + " reiniciada.");
        } else {
            log.warn("No se encontró la tarea con el nombre: " + taskName);
        }
    }
    
}
