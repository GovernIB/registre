package es.caib.regweb3.persistence.utils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.ejb.EJB;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.interceptor.Interceptors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ejb.interceptor.SpringBeanAutowiringInterceptor;
import org.springframework.stereotype.Component;

import es.caib.regweb3.model.Entidad;
import es.caib.regweb3.model.RegistroSalida;
import es.caib.regweb3.model.RegistroSir;
import es.caib.regweb3.model.sir.TipoAnotacion;
import es.caib.regweb3.persistence.ejb.IntegracionLocal;
import es.caib.regweb3.persistence.ejb.RegistroSalidaConsultaLocal;
import es.caib.regweb3.persistence.ejb.RegistroSirLocal;
import es.caib.regweb3.utils.RegwebConstantes;

/**
 * 
 * @author Limit Tecnologies <limit@limit.es>
 *
 */
@Component
@Interceptors(SpringBeanAutowiringInterceptor.class)
public class NotibHelper {

	@EJB(mappedName = "regweb3/RegistroSalidaConsultaEJB/local")
	private RegistroSalidaConsultaLocal registroSalidaConsultaEjb;
	@EJB(mappedName = "regweb3/RegistroSirEJB/local")
	private RegistroSirLocal registroSirEjb;
	@EJB(mappedName = "regweb3/IntegracionEJB/local")
	private IntegracionLocal integracionEjb;
	
	@Autowired
	private GenericApiClient api;
	@Autowired
	private NotibPluginHelper notibHelper;

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void comunicarCambioEstadoSir(RegistroSir registroSir) throws Exception {
		Entidad entidad = registroSir.getEntidad();
		StringBuilder peticion = new StringBuilder();
		Date inicio = new Date();
		long tiempo = System.currentTimeMillis();
		String descripcion = "Callback cambio estado del registro SIR: " + registroSir.getNumeroRegistro();
		try {
			RegistroSalida registroSalida = registroSalidaConsultaEjb.findByNumeroRegistroFormateado(
					entidad.getCodigoDir3(),
					registroSir.getNumeroRegistro());
			String aplicacionTelematica = registroSalida.getRegistroDetalle().getAplicacionTelematica();
			boolean presencial = registroSalida.getRegistroDetalle().getPresencial();
			
			peticion.append("TipoRegistro: ").append("Salida").append(System.getProperty("line.separator"));
		    peticion.append("TipoAnotación: ").append(TipoAnotacion.ENVIO.getName()).append(System.getProperty("line.separator"));
		    peticion.append("Aplicación telemática: ").append(aplicacionTelematica).append(System.getProperty("line.separator"));
		    peticion.append("Número registro: ").append(registroSir.getNumeroRegistro()).append(System.getProperty("line.separator"));

			String urlNotib = PropiedadGlobalUtil.getNotibCallbackUrl(entidad.getId());

			if (!presencial && aplicacionTelematica.contains("NOTIB") && urlNotib != null) {
				Map<String, String> request = new HashMap<>();
				String codigoEmisor = PropiedadGlobalUtil.getEmisorForzadoNotib();
				request.put("registreNumero", registroSalida.getNumeroRegistroFormateado());
				request.put("entitatDir3Codi", codigoEmisor != null ? codigoEmisor : entidad.getCodigoDir3());

				RespuestaCallbackSir respuesta = api.postRequest(urlNotib, request, RespuestaCallbackSir.class);
				
				if (respuesta.isOk()) {
					integracionEjb.addIntegracionOk(
							inicio, 
							RegwebConstantes.INTEGRACION_SIR, 
							descripcion, 
							peticion.toString(), 
							System.currentTimeMillis() - tiempo, 
							entidad.getId(), 
							registroSir.getIdentificadorIntercambio());
					
					registroSirEjb.actualizarEstadoComunicacionAdviser(registroSir.getId(), true);
				} else {
					throw new RuntimeException(respuesta.getErrorDescripcio());
				}
			}
		} catch (Exception e) {
			integracionEjb.addIntegracionError(
					RegwebConstantes.INTEGRACION_SIR, 
					descripcion, 
					peticion.toString(), 
					e, 
					null, 
					System.currentTimeMillis() - tiempo, 
					entidad.getId(), 
					registroSir.getIdentificadorIntercambio());

			registroSirEjb.actualizarEstadoComunicacionAdviser(registroSir.getId(), false);
			throw e;
		}
	}

}
