package es.caib.regweb3.persistence.utils;

/**
 * Clase que controla posibles problemas de concurrencia (scheduled + manual)
 * 
 * @author Limit Tecnologies <limit@limit.es>
 *
 */
public class SemaforoSchedulerLocalizaPendientes {
	
	public static Object lock = new Object();
	
	public static Object getCreacionSemaforo() {
		return lock;
	}
}
