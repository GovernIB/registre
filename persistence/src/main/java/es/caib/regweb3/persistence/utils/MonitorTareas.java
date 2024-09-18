package es.caib.regweb3.persistence.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.ejb.EJB;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.util.DefaultPropertiesPersister;

import es.caib.regweb3.persistence.ejb.SchedulerLocal;

@Component
public class MonitorTareas {

	@EJB(mappedName = "regweb3/SchedulerEJB/local")
    private SchedulerLocal schedulerEjb;
	
	protected final Logger log = Logger.getLogger(getClass());
	
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
	
	private ScheduledExecutorService monitorExecutor = Executors.newScheduledThreadPool(1);
	
	public MonitorTareas() {
		monitorExecutor.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
            	try {
            		verificarUltimaEjecucionEntidades();
            	} catch (Exception e) {
            		e.printStackTrace();
            		log.error("Advertencia: Error durante la verificación de la última ejecución: " + e.getMessage());
				}
            }
        }, 0, 30, TimeUnit.MINUTES);
	}
	
	public void verificarUltimaEjecucionEntidades() throws Exception {
		Date ahora = new Date();
		long intervaloMaximo = TimeUnit.MINUTES.toMillis(15); // Alerta si han pasado más de 15 minutos
				
		log.debug("Verificando última ejecución...");
		
		Date ultimaEjecucionExitosa = obtenerFechaUltimaEjecucion();
			
		if (ultimaEjecucionExitosa != null && (ahora.getTime() - ultimaEjecucionExitosa.getTime() > intervaloMaximo)) {
			schedulerEjb.enviarCorreoVerificacionAnexosBloqueada();
		}
	}
	
	public void actualizarUltimaEjecucion() {
		String fechaUltimaActualizacion = sdf.format(new Date());
    	Properties properties = new Properties();
		File file;
		try {
			String path = PropiedadGlobalUtil.getFechaUltimaVerificacionFirmaAnexosPath();
			properties.setProperty("verificacion.anexos.fecha.ultima.ejecucion", fechaUltimaActualizacion);
			if (path == null || path == "") {
				path = "/opt/files/";
			}
			file = new File(path + "/fecha_verificacion.properties");
			OutputStream out = new FileOutputStream(file);
			
			DefaultPropertiesPersister p = new DefaultPropertiesPersister();
			p.store(properties, out, "Fecha última ejecución proceso validación firma anexos.");
		} catch (Exception ex) {
			log.error("Error a la hora de escribir el fichero fecha_verificacion", ex);
			ex.printStackTrace();
		}
	}

	private Date obtenerFechaUltimaEjecucion() {
		String path = PropiedadGlobalUtil.getFechaUltimaVerificacionFirmaAnexosPath();
		Properties properties = new Properties();
		Date fechaEjecucionDate = null;
		try {
			log.debug("Recuperando fecha última ejecución proceso validación firmas...");
			if (path == null || path == "") {
				path = "/opt/files/";
			}
			File file = new File(path + "/fecha_verificacion.properties");
			properties.load(new FileInputStream(file));
			String fechaEjecucion = properties.getProperty("verificacion.anexos.fecha.ultima.ejecucion");
			
			if (fechaEjecucion != null)
				fechaEjecucionDate = sdf.parse(fechaEjecucion);
		} catch (Exception ex) {
			log.error("Error en la lectura de la fecha última ejeción validación firmas", ex);
			throw new RuntimeException(
					"Error en la lectura de la fecha última ejeción validación firmas: " + ex.getMessage());
		}
		return fechaEjecucionDate;
	}

}
