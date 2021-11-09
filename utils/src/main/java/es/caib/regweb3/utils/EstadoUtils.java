package es.caib.regweb3.utils;

public class EstadoUtils {

    public static Long getEstadoTrazabilidad(int estadoGeiser) {
    	switch (estadoGeiser) {
		case 3:
			return RegwebConstantes.TRAZABILIDAD_SIR_PENDIENTE;
		case 4:
			return RegwebConstantes.TRAZABILIDAD_SIR_PENDIENTE;
		case 5:
			return RegwebConstantes.TRAZABILIDAD_SIR_PENDIENTE;
		case 6:
			return RegwebConstantes.TRAZABILIDAD_SIR_PENDIENTE;
		case 7:
			return RegwebConstantes.TRAZABILIDAD_SIR_PENDIENTE;
		case 8:
			return RegwebConstantes.TRAZABILIDAD_SIR_PENDIENTE;
		case 9:
			return RegwebConstantes.TRAZABILIDAD_SIR_CONFIRMADO;
		case 10:
			return RegwebConstantes.TRAZABILIDAD_SIR_RECEPCION;
		case 11:
			return RegwebConstantes.TRAZABILIDAD_SIR_RECHAZO;
		case 12:
			return RegwebConstantes.TRAZABILIDAD_SIR_RECHAZO;
		case 13:
			return RegwebConstantes.TRAZABILIDAD_SIR_REENVIO;
		case 14:
			return RegwebConstantes.TRAZABILIDAD_SIR_PENDIENTE;
		case 15:
			return RegwebConstantes.TRAZABILIDAD_SIR_PENDIENTE;
		case 16:
			return RegwebConstantes.TRAZABILIDAD_SIR_CONFIRMADO;
		case 17:
			return RegwebConstantes.TRAZABILIDAD_SIR_RECHAZO;
		case 18:
			return RegwebConstantes.TRAZABILIDAD_SIR_RECTIFICADO;
		case 19:
			return RegwebConstantes.TRAZABILIDAD_SIR_PENDIENTE;
		case 20:
			return RegwebConstantes.TRAZABILIDAD_SIR_RECHAZO_ORIGEN;
		case 21:
			return RegwebConstantes.TRAZABILIDAD_SIR_ELIMINAR;
		}
		return 0L;
    }
    
	public static int getEstadoOficioRemision(String estadoGeiser) {
    	switch (estadoGeiser) {
		case "SIN_DATOS":
			return RegwebConstantes.OFICIO_SIR_SIN_DATOS;
		case "PENDIENTE_ENVIO":
			return RegwebConstantes.OFICIO_SIR_PENDIENTE_ENVIO;
		case "ENVIADO_PENDIENTE_CONFIRMACION":
			return RegwebConstantes.OFICIO_SIR_ENVIADO_PENDIENTE_CONFIRMACION;
		case "ENVIADO_PENDIENTE_CONFIRMACION_MANUAL":
			return RegwebConstantes.OFICIO_SIR_ENVIADO_PENDIENTE_CONFIRMACION_MANUAL;
		case "RECIBIDO_PENDIENTE_CONFIRMACION":
			return RegwebConstantes.OFICIO_SIR_RECIBIDO_PENDIENTE_CONFIRMACION;
		case "RECIBIDO_PENDIENTE_CONFIRMACION_MANUAL":
			return RegwebConstantes.OFICIO_SIR_RECIBIDO_PENDIENTE_CONFIRMACION_MANUAL;
		case "ENVIADO_CONFIRMADO":
			return RegwebConstantes.OFICIO_SIR_ENVIADO_CONFIRMADO;
		case "RECIBIDO_CONFIRMADO":
			return RegwebConstantes.OFICIO_SIR_RECIBIDO_CONFIRMADO;
		case "ENVIADO_RECHAZADO":
			return RegwebConstantes.OFICIO_SIR_ENVIADO_RECHAZADO;
		case "RECIBIDO_RECHAZADO":
			return RegwebConstantes.OFICIO_SIR_RECIBIDO_RECHAZADO;
		case "REENVIADO":
			return RegwebConstantes.OFICIO_SIR_REENVIADO;
		case "EN_TRAMITE":
			return RegwebConstantes.OFICIO_SIR_EN_TRAMITE;
		case "ASIGNADO":
			return RegwebConstantes.OFICIO_SIR_ASIGNADO;
		case "FINALIZADO":
			return RegwebConstantes.OFICIO_SIR_FINALIZADO;
		case "REENVIADO_RECHAZADO":
			return RegwebConstantes.OFICIO_SIR_REENVIADO_RECHAZADO;
		case "RECTIFICADO":
			return RegwebConstantes.OFICIO_SIR_RECTIFICADO;
		case "ENVIO_PROCESO":
			return RegwebConstantes.OFICIO_SIR_ENVIO_PROCESO;
		case "RECIBIDO_RECHAZADO_CIUDADANO":
			return RegwebConstantes.OFICIO_SIR_RECIBIDO_RECHAZADO_CIUDADANO;
		case "ELIMINADO":
			return RegwebConstantes.OFICIO_SIR_ELIMINADO;
		}
		return 0;
    }
    
}
