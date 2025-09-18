package es.caib.regweb3.persistence.utils;

import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.ejb.EJB;
import javax.interceptor.Interceptors;

import org.apache.log4j.Logger;
import org.fundaciobit.genapp.common.i18n.I18NArgumentCode;
import org.fundaciobit.genapp.common.i18n.I18NException;
import org.plugin.geiser.api.AnexoG;
import org.plugin.geiser.api.ApunteRegistro;
import org.plugin.geiser.api.EstadoTramitacion;
import org.plugin.geiser.api.RespuestaBusquedaTramitGeiser;
import org.plugin.geiser.api.RespuestaConsultaGeiser;
import org.plugin.geiser.api.RespuestaRegistroGeiser;
import org.plugin.geiser.api.TipoAsiento;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ejb.interceptor.SpringBeanAutowiringInterceptor;
import org.springframework.stereotype.Component;

import es.caib.regweb3.model.AnexoSir;
import es.caib.regweb3.model.Entidad;
import es.caib.regweb3.model.IRegistro;
import es.caib.regweb3.model.OficioRemision;
import es.caib.regweb3.model.RegistroDetalle;
import es.caib.regweb3.model.RegistroEntrada;
import es.caib.regweb3.model.RegistroSalida;
import es.caib.regweb3.model.RegistroSir;
import es.caib.regweb3.model.Trazabilidad;
import es.caib.regweb3.model.UsuarioEntidad;
import es.caib.regweb3.model.utils.AnexoFull;
import es.caib.regweb3.model.utils.EstadoRegistroSir;
import es.caib.regweb3.persistence.ejb.AnexoLocal;
import es.caib.regweb3.persistence.ejb.HistoricoRegistroEntradaLocal;
import es.caib.regweb3.persistence.ejb.HistoricoRegistroSalidaLocal;
import es.caib.regweb3.persistence.ejb.OficioRemisionLocal;
import es.caib.regweb3.persistence.ejb.RegistroEntradaLocal;
import es.caib.regweb3.persistence.ejb.RegistroSalidaLocal;
import es.caib.regweb3.persistence.ejb.RegistroSirLocal;
import es.caib.regweb3.persistence.ejb.TrazabilidadLocal;
import es.caib.regweb3.utils.Configuracio;
import es.caib.regweb3.utils.EstadoUtils;
import es.caib.regweb3.utils.RegwebConstantes;
import es.caib.regweb3.utils.RegwebUtils;
import es.caib.regweb3.utils.StringUtils;

@Component
@Interceptors(SpringBeanAutowiringInterceptor.class)
public class IntegracionGeiserHelper {
	
	@EJB(mappedName = "regweb3/AnexoEJB/local")
    private AnexoLocal anexoEjb;
	@EJB(mappedName = "regweb3/TrazabilidadEJB/local")
    private TrazabilidadLocal trazabilidadEjb;
	@EJB(mappedName = "regweb3/RegistroSirEJB/local")
    private RegistroSirLocal registroSirEjb;
	@EJB(mappedName = "regweb3/RegistroEntradaEJB/local")
    private RegistroEntradaLocal registroEntradaEjb;
	@EJB(mappedName = "regweb3/RegistroSalidaEJB/local")
    private RegistroSalidaLocal registroSalidaEjb;
	@EJB(mappedName = "regweb3/OficioRemisionEJB/local")
    private OficioRemisionLocal oficioRemisionEjb;
	@EJB(mappedName = "regweb3/HistoricoRegistroEntradaEJB/local")
    private HistoricoRegistroEntradaLocal historicoRegistroEntradaEjb;
	@EJB(mappedName = "regweb3/HistoricoRegistroSalidaEJB/local")
    private HistoricoRegistroSalidaLocal historicoRegistroSalidaEjb;
	
	@Autowired 
	private GeiserPluginHelper pluginHelper;
	
	// Enviar SIR
	public void realizarRegistro(
			IRegistro registro, 
			UsuarioEntidad usuario) throws I18NException, Exception {
		Long entidadId = usuario.getEntidad().getId();
		
		RespuestaRegistroGeiser respuesta = pluginHelper.postProcesoNuevoRegistroGeiser(
				registro, 
				entidadId, 
				false);
		
        if (respuesta != null) {
        	actualizarMetadatosRegistro(
        			null, 
        			registro, 
        			usuario, 
        			respuesta);
        } else {
        	// No s´ha definit cap plugin de Justificant. Consulti amb el seu Administrador.
            throw new I18NException("error.plugin.nodefinit", new I18NArgumentCode("plugin.tipo.11"));
        }
	}
	
	// Enviar SIR
	public void realizarRegistroSir(
			RegistroSir registroSir, 
			IRegistro registro, 
			UsuarioEntidad usuario,
			StringBuilder peticion) throws I18NException, Exception {
		Long entidadId = usuario.getEntidad().getId();
    	registroSir.setDocumentoUsuario(usuario.getUsuario().getDocumento());
    	
        RespuestaRegistroGeiser respuesta = pluginHelper.postProcesoNuevoRegistroSirGeiser(
        		registroSir, 
        		entidadId);
        
        if (respuesta != null) {
        	actualizarMetadatosRegistro(
        			registroSir, 
        			registro, 
        			usuario, 
        			respuesta);
        	
        	consultarActualizarRegistroSalidaGeiser(
        			registroSir,
        			registro, 
        			usuario, 
        			peticion);
        	
        } else {
        	// No s´ha definit cap plugin de Justificant. Consulti amb el seu Administrador.
            throw new I18NException("error.plugin.nodefinit", new I18NArgumentCode("plugin.tipo.11"));
        }
	}
	
	public void actualizarEstadoRegistroSir(
			RegistroSir registroSir, 
			OficioRemision oficioRemision) throws Exception, I18NException {
		Entidad entidad = registroSir.getEntidad();

		if (oficioRemision == null) {
			oficioRemision = oficioRemisionEjb.getByNumeroRegistro(
					registroSir.getNumeroRegistro(),
					entidad.getCodigoDir3());
		}
		
		// Pendiente de persistir
		if (registroSir.getId() == null) {
			registroSirEjb.persist(registroSir);
		}
		
		// Actualizar estado en caso de no ser FINAL
		if (oficioRemision != null && !RegwebUtils.contains(RegwebConstantes.ESTADOS_OFICIO_REMISION_SIR_FINALES, oficioRemision.getEstado())) {
			RespuestaConsultaGeiser responseConsulta = pluginHelper.postProcesoConsultarRegistroSirGeiser(
					registroSir,
					entidad.getId());

			List<ApunteRegistro> apuntes = responseConsulta.getApuntes();
			if (apuntes != null && !apuntes.isEmpty()) {
				sincronizarEstadoGeiser(
						apuntes, 
						registroSir, 
						oficioRemision);
			}
		}
		
	}

	// Actualiza el registro de salida generado por entrada SIR (consultamos GEISER ya que no lo tenemos en la respuesta de registrar)
	private void consultarActualizarRegistroSalidaGeiser(
			RegistroSir registroSir, 
			IRegistro registro, 
			UsuarioEntidad usuario, 
			StringBuilder peticion) throws I18NException, Exception {
		Entidad entidad = usuario.getEntidad();
    	RegistroSir registroSirConsulta = new RegistroSir();
    	registroSirConsulta.setNumeroRegistro(registro.getNumeroRegistro());
    	registroSirConsulta.setCodigoEntidadRegistralOrigen(registro.getOficina().getCodigo());
    	
    	RespuestaConsultaGeiser consultaRegistroEntrada = pluginHelper.postProcesoConsultarRegistroSirGeiser(
    			registroSirConsulta, 
    			entidad.getId());

    	// Actualizar registro salida generado en caso de entrada SIR
    	List<ApunteRegistro> apuntesRegistro = consultaRegistroEntrada.getApuntes();
    	if (apuntesRegistro != null && !apuntesRegistro.isEmpty()) {
    		for (ApunteRegistro apunte: apuntesRegistro) {
    			
    			if (apunte.getNuRegistro().equals(registroSir.getNumeroRegistro())) {
    				registroSir.setEstado(EstadoRegistroSir.valueOf(apunte.getEstado().name()));
    			}
    			
    			for (AnexoSir anexoSir : registroSir.getAnexos()) {
    				if (anexoSir.getIdentificadorFichero() == null) {
    					for (AnexoG anexoGeiser : apunte.getAnexos()) {
    						if (anexoGeiser.getHashBase64().equals(anexoSir.getHash()))
    							anexoSir.setIdentificadorFichero(anexoGeiser.getIdentificador());
    					}
    				}
    			}
    			
				if (apunte.getTipoAsiento().equals(TipoAsiento.SALIDA) && apunte.getNuRegistroOrigen().equals(registro.getNumeroRegistro())) {
					List<Trazabilidad> trazabilidadesRegistroSir = trazabilidadEjb.getByRegistroEntradaOrigen(registro.getId());
					if (trazabilidadesRegistroSir != null && !trazabilidadesRegistroSir.isEmpty()) {
						Trazabilidad trazabilidadRegistro = trazabilidadesRegistroSir.get(0);
						if (trazabilidadRegistro.getTipo().equals(RegwebConstantes.TRAZABILIDAD_OFICIO_SIR) || trazabilidadRegistro.getTipo().equals(RegwebConstantes.TRAZABILIDAD_OFICIO)) {
							RegistroSalida registroSalidaSir = trazabilidadRegistro.getRegistroSalida();
							registroSalidaSir.setNumeroRegistro(apunte.getNuRegistro());
							registroSalidaSir.setNumeroRegistroFormateado(apunte.getNuRegistro());
							registroSalidaSir.setFecha(apunte.getFechaRegistro());
						}
					}
				}
			}
    	}
	}
	
	// Actualiza el registro con la información de GEISER
	private void sincronizarEstadoGeiser(
			List<ApunteRegistro> apuntes, 
			RegistroSir registroSir, 
			OficioRemision oficioRemision) throws Exception, I18NException {
		int estadoOficioSirActual = 0;
		for (ApunteRegistro apunteRegistro : apuntes) {
			if (apunteRegistro.getNuRegistro().equals(registroSir.getNumeroRegistro())) {
				registroSir.setEstado(EstadoRegistroSir.valueOf(apunteRegistro.getEstado().name()));

				estadoOficioSirActual = EstadoUtils.getEstadoOficioRemision(apunteRegistro.getEstado().name());

				if (oficioRemision != null && oficioRemision.getEstado() != estadoOficioSirActual && estadoOficioSirActual != 0) {
					oficioRemisionEjb.modificarEstado(oficioRemision.getId(), estadoOficioSirActual);

				}
			}
			
			if ((estadoOficioSirActual == RegwebConstantes.OFICIO_SIR_ENVIADO_CONFIRMADO || estadoOficioSirActual == RegwebConstantes.OFICIO_SIR_ENVIADO_RECHAZADO)
					&& !apunteRegistro.getOrganoDestino().equals(registroSir.getCodigoUnidadTramitacionDestino())) {
				actualizarRegistroSirReenviado(
						registroSir, 
						oficioRemision, 
						apunteRegistro);
			}
			
			// Actualizar anexos sir con identificador fichero de geiser
			for (AnexoSir anexoSir : registroSir.getAnexos()) {
				if (anexoSir.getIdentificadorFichero() == null) {
					for (AnexoG anexoGeiser : apunteRegistro.getAnexos()) {
						if (anexoGeiser.getHashBase64().equals(anexoSir.getHash()))
							anexoSir.setIdentificadorFichero(anexoGeiser.getIdentificador());
					}
				}
			}
			
			// Actualizar estado registro
			if (RegwebUtils.contains(RegwebConstantes.ESTADOS_OFICIO_REMISION_SIR_ENVIADOS_FINALES, estadoOficioSirActual)) {
				for (RegistroEntrada registroEntrada : oficioRemision.getRegistrosEntrada()) {
					registroEntradaEjb.cambiarEstado(
							registroEntrada.getId(),
							RegwebConstantes.REGISTRO_OFICIO_ACEPTADO);
					
					registroEntrada.setEstado(RegwebConstantes.REGISTRO_OFICIO_ACEPTADO);
					historicoRegistroEntradaEjb.crearHistoricoRegistroEntrada(
							registroEntrada,
			                oficioRemision.getUsuarioResponsable(), 
			                I18NLogicUtils.tradueix(new Locale(Configuracio.getDefaultLanguage()), "registro.modificacion.estado"), 
			                false);
				}
				for (RegistroSalida registroSalida : oficioRemision.getRegistrosSalida()) {
					registroSalidaEjb.cambiarEstado(
							registroSalida.getId(),
							RegwebConstantes.REGISTRO_OFICIO_ACEPTADO);
					
					registroSalida.setEstado(RegwebConstantes.REGISTRO_OFICIO_ACEPTADO);
					historicoRegistroSalidaEjb.crearHistoricoRegistroSalida(
							registroSalida,
			                oficioRemision.getUsuarioResponsable(), 
			                I18NLogicUtils.tradueix(new Locale(Configuracio.getDefaultLanguage()), "registro.modificacion.estado"), 
			                false);
				}
			}
			
			// Ver si rectificar registro
			if (RegwebUtils.contains(RegwebConstantes.ESTADOS_OFICIO_REMISION_SIR_RECTIFICAR, estadoOficioSirActual)) {
				for (RegistroEntrada registroEntrada : oficioRemision.getRegistrosEntrada()) {
					registroEntradaEjb.cambiarEstado(
							registroEntrada.getId(),
							RegwebConstantes.REGISTRO_RECHAZADO);
					
					registroEntrada.setEstado(RegwebConstantes.REGISTRO_RECHAZADO);
					historicoRegistroEntradaEjb.crearHistoricoRegistroEntrada(
							registroEntrada,
			                oficioRemision.getUsuarioResponsable(), 
			                I18NLogicUtils.tradueix(new Locale(Configuracio.getDefaultLanguage()), "registro.modificacion.estado"), 
			                false);
				}
				for (RegistroSalida registroSalida : oficioRemision.getRegistrosSalida()) {
					registroSalidaEjb.cambiarEstado(
							registroSalida.getId(),
							RegwebConstantes.REGISTRO_RECHAZADO);
					
					registroSalida.setEstado(RegwebConstantes.REGISTRO_RECHAZADO);
					historicoRegistroSalidaEjb.crearHistoricoRegistroSalida(
							registroSalida,
			                oficioRemision.getUsuarioResponsable(), 
			                I18NLogicUtils.tradueix(new Locale(Configuracio.getDefaultLanguage()), "registro.modificacion.estado"), 
			                false);
				}
			}
			
			actualizarIdentificadorIntercambio(
					registroSir, 
					oficioRemision);
		}
	}
	
	public void actualizarIdentificadorIntercambio(
			RegistroSir registroSir, 
			OficioRemision oficioRemision) throws I18NException, Exception {
		Entidad entidad = registroSir.getEntidad();
		int estadoActualSir = Integer.valueOf(registroSir.getEstado().getValue());
		
		if (oficioRemision == null) {
			oficioRemision = oficioRemisionEjb.getByNumeroRegistro(
					registroSir.getNumeroRegistro(),
					entidad.getCodigoDir3());
		}
		
		// registroSir.getIdentificadorIntercambio() == null || (oficioRemision != null && oficioRemision.getIdentificadorIntercambio() == null)
		// No aplicamos validacion anterior ya que podría tratarse de un registro sir rechazado en destino y habría que recuperar motivo
		if (! RegwebUtils.contains(RegwebConstantes.ESTADOS_REGISTRO_SIR_FINALES, estadoActualSir)) {
			Long entidadId = registroSir.getEntidad().getId();

			RespuestaBusquedaTramitGeiser response = pluginHelper.postProcesoBuscarEstadoTRegistroSirGeiser(
					null,
					registroSir,
					entidadId);

			List<EstadoTramitacion> estadoTramitacion = response.getEstadosTramitacionRegistro();
			// Identificador intercambio a veces no disponible al momento (consultar con una scheduled)
			if (estadoTramitacion != null && !estadoTramitacion.isEmpty() && !estadoTramitacion.get(0).getIdentificadorIntercambioSIR().isEmpty()) {
				String identificadorIntercambio = estadoTramitacion.get(0).getIdentificadorIntercambioSIR().get(0);
				String motivoRechazo = estadoTramitacion.get(0).getMotivoRechazo();

				if (identificadorIntercambio != null) {
					registroSir.setIdentificadorIntercambio(identificadorIntercambio);
					registroSir.setMotivoRechazo(motivoRechazo);
				}

				if (oficioRemision != null && identificadorIntercambio != null) {
					oficioRemisionEjb.actualizarIdentificadorIntercambio(
							oficioRemision.getId(),
							identificadorIntercambio);

					oficioRemisionEjb.actualizarMotivoRechazo(
							oficioRemision.getId(), 
							motivoRechazo);

					if (oficioRemision.getRegistrosEntrada() != null) {
						for (RegistroEntrada registroEntrada : oficioRemision.getRegistrosEntrada()) {
							if (registroEntrada.getNumeroRegistro().equals(registroSir.getNumeroRegistro()))
								registroEntrada.getRegistroDetalle().setIdentificadorIntercambio(identificadorIntercambio);
						}
					}

					if (oficioRemision.getRegistrosSalida() != null) {
						for (RegistroSalida registroSalida : oficioRemision.getRegistrosSalida()) {
							if (registroSalida.getNumeroRegistro().equals(registroSir.getNumeroRegistro()))
								registroSalida.getRegistroDetalle().setIdentificadorIntercambio(identificadorIntercambio);
						}
					}

					if (estadoTramitacion.get(0).getFechaEstado() != null) {
						Date fechaEstado = estadoTramitacion.get(0).getFechaEstado();
						oficioRemisionEjb.modificarFechaEstado(oficioRemision.getId(), fechaEstado);
						registroSir.setFechaEstado(fechaEstado);
					} else {
						oficioRemisionEjb.modificarFechaEstado(oficioRemision.getId(), null); // No mostrar fecha estado si no la devuelve GEISER
					}
				}
			}
		}
	}
	
	private void actualizarRegistroSirReenviado(
			RegistroSir registroSir, 
			OficioRemision oficioRemision, 
			ApunteRegistro apunteRegistro) throws Exception {
		// Si se hace un reenvío el destinatario cambia
		registroSir.setCodigoEntidadRegistral(apunteRegistro.getCdAmbitoActual());
		registroSir.setCodigoEntidadRegistralDestino(apunteRegistro.getCdAmbitoActual());
		registroSir.setDecodificacionEntidadRegistralDestino(apunteRegistro.getNombreAmbitoActual());
		registroSir.setCodigoUnidadTramitacionDestino(apunteRegistro.getOrganoDestino());
		registroSir.setDecodificacionUnidadTramitacionDestino(apunteRegistro.getOrganoDestinoDenominacion());
		
		// Destino oficio remisión
		oficioRemisionEjb.actualizarDestinoExterno(
				oficioRemision.getId(),
				apunteRegistro.getCdAmbitoActual(),
				apunteRegistro.getNombreAmbitoActual(),
				apunteRegistro.getOrganoDestino(),
				apunteRegistro.getOrganoDestinoDenominacion());
		
		for (RegistroEntrada registroEntrada : oficioRemision.getRegistrosEntrada()) {
			// Destino registro entrada
			RegistroDetalle registroDetalle = registroEntrada.getRegistroDetalle();
			registroDetalle.setCodigoEntidadRegistralDestino(apunteRegistro.getCdAmbitoActual());
			registroDetalle.setDecodificacionEntidadRegistralDestino(apunteRegistro.getNombreAmbitoActual());
			registroEntrada.setRegistroDetalle(registroDetalle);
			
			registroEntradaEjb.actualizarDestinoExterno(
					registroEntrada.getId(),
					apunteRegistro.getOrganoDestino(),
					apunteRegistro.getOrganoDestinoDenominacion());
		}
		for (RegistroSalida registroSalida : oficioRemision.getRegistrosSalida()) {
			// Destino registro salida
			RegistroDetalle registroDetalle = registroSalida.getRegistroDetalle();
			registroDetalle.setCodigoEntidadRegistralDestino(apunteRegistro.getCdAmbitoActual());
			registroDetalle.setDecodificacionEntidadRegistralDestino(apunteRegistro.getNombreAmbitoActual());
			registroSalida.setRegistroDetalle(registroDetalle);
			
			registroSalidaEjb.actualizarDestinoExterno(
					registroSalida.getId(),
					apunteRegistro.getOrganoDestino(),
					apunteRegistro.getOrganoDestinoDenominacion());
		}
	}
	
	private void actualizarMetadatosRegistro(
			RegistroSir registroSir,
			IRegistro registro,
			UsuarioEntidad usuario, 
			RespuestaRegistroGeiser respuesta) throws Exception, I18NException {
		
		if (registroSir != null) {
			registroSir.setNumeroRegistro(respuesta.getNuRegistro());
			registroSir.setFechaRegistro(respuesta.getFechaRegistro());
		}
		
    	if (registro instanceof RegistroEntrada) {
    		RegistroEntrada registroEntrada = (RegistroEntrada) registro;
    		if (registroEntrada.getDestino() == null) {
            	// Si es un registro a una adm externa recuperar justifcante de GEISER
                registroEntrada.getRegistroDetalle().setJustificanteGeiser(true);
    		}
    		registroEntrada.setNumeroRegistro(respuesta.getNuRegistro());
    		registroEntrada.setNumeroRegistroFormateado(respuesta.getNuRegistro());
    		registroEntrada.setFecha(respuesta.getFechaRegistro());
    		
            // Si no ha introducido ninguna fecha de Origen
            if (registroEntrada.getRegistroDetalle().getFechaOrigen() == null)
            	registroEntrada.getRegistroDetalle().setFechaOrigen(registroEntrada.getFecha());
                
            //Si no se ha espeficicado un NumeroRegistroOrigen, le asignamos el propio
            if (StringUtils.isEmpty(registroEntrada.getRegistroDetalle().getNumeroRegistroOrigen()))
            	registroEntrada.getRegistroDetalle().setNumeroRegistroOrigen(registroEntrada.getNumeroRegistroFormateado());
            
    	} else {
    		RegistroSalida registroSalida = (RegistroSalida) registro;
			registroSalida.setNumeroRegistro(respuesta.getNuRegistro());
			registroSalida.setNumeroRegistroFormateado(respuesta.getNuRegistro());
			registroSalida.setFecha(respuesta.getFechaRegistro());

            // Si no ha introducido ninguna fecha de Origen
            if (registroSalida.getRegistroDetalle().getFechaOrigen() == null)
                registroSalida.getRegistroDetalle().setFechaOrigen(registroSalida.getFecha());
                
            //Si no se ha espeficicado un NumeroRegistroOrigen, le asignamos el propio
            if (StringUtils.isEmpty(registroSalida.getRegistroDetalle().getNumeroRegistroOrigen())) 
                registroSalida.getRegistroDetalle().setNumeroRegistroOrigen(registroSalida.getNumeroRegistroFormateado());
    	}
		
		// Actualizamos metadatos anexos en custodia
		actualizarMetadatosAnexosArxiu(registro, usuario);
		
	}
    
    private void actualizarMetadatosAnexosArxiu(
    		IRegistro registro, 
    		UsuarioEntidad usuario) throws I18NException {	
    	// Actualizar información anexos SGD
    	try {
    		for (AnexoFull anexoFull: registro.getRegistroDetalle().getAnexosFull()) {
        		anexoEjb.actualizarMetadatosAnexo(registro, anexoFull, usuario, false);
			}
    	} catch (I18NException i18n) {
    		log.error("Ha habido un error actualizando los metadatos de registro del anexo.");
    		i18n.printStackTrace();
		} catch (Exception e) {
			log.error("Ha habido un error actualizando los metadatos de registro del anexo.");
			e.printStackTrace();
		}
    }
    
    protected final Logger log = Logger.getLogger(getClass());
	
}
