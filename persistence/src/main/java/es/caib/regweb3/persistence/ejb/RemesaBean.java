package es.caib.regweb3.persistence.ejb;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;

import javax.activation.DataHandler;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipFile;
import org.apache.log4j.Logger;
import org.fundaciobit.genapp.common.i18n.I18NException;
import org.jboss.ejb3.annotation.SecurityDomain;
import org.jboss.ejb3.annotation.TransactionTimeout;
import org.plugin.lema.api.Anexos;
import org.plugin.lema.api.ConsultaAcuseReciboRequest;
import org.plugin.lema.api.ConsultaAcuseReciboResponse;
import org.plugin.lema.api.ConsultaAnexoRequest;
import org.plugin.lema.api.ConsultaAnexoResponse;
import org.plugin.lema.api.ConsultaRealizadaRequest;
import org.plugin.lema.api.ConsultaRealizadaResponse;
import org.plugin.lema.api.Contenido;
import org.plugin.lema.api.DetalleDocumento;
import org.plugin.lema.api.DocumentoAnexo;
import org.plugin.lema.api.EnlaceDocumento;
import org.plugin.lema.api.Envio;
import org.plugin.lema.api.IdentificadorAcuseRecibo;
import org.plugin.lema.api.LemaPluginException;
import org.plugin.lema.api.PeticionAccesoRequest;
import org.plugin.lema.api.PeticionAccesoResponse;
import org.plugin.lema.api.ReferenciaDocumento;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ejb.interceptor.SpringBeanAutowiringInterceptor;

import es.caib.notib.client.domini.EnviamentReferencia;
import es.caib.notib.client.domini.EnviamentTipus;
import es.caib.notib.client.domini.EnviamentV2;
import es.caib.notib.client.domini.NotificacioV2;
import es.caib.notib.client.domini.RespostaAlta;
import es.caib.regweb3.model.Entidad;
import es.caib.regweb3.model.RegistroEntrada;
import es.caib.regweb3.model.Remesa;
import es.caib.regweb3.model.Remesa.TipoRemesa;
import es.caib.regweb3.model.RemesaAcuse;
import es.caib.regweb3.model.UsuarioEntidad;
import es.caib.regweb3.persistence.utils.DehuDocumentManager;
import es.caib.regweb3.persistence.utils.LemaPluginHelper;
import es.caib.regweb3.persistence.utils.LemaUtils;
import es.caib.regweb3.persistence.utils.PropiedadGlobalUtil;
import es.caib.regweb3.utils.MimeTypeUtils;
import es.caib.regweb3.utils.RegwebConstantes;

/**
 * Created by Limit Tecnologies S.L.
 * 
 * @author Jamal
 */
@Stateless(name = "RemesaEJB")
@SecurityDomain("seycon")
@Interceptors(SpringBeanAutowiringInterceptor.class)
public class RemesaBean extends BaseEjbJPA<Remesa, Long> implements RemesaLocal {

	protected final Logger log = Logger.getLogger(getClass());

	@PersistenceContext(unitName = "regweb3")
	private EntityManager em;
	
	@Resource
    private javax.ejb.SessionContext ejbContext;
	
	@EJB private RemesaAcuseLocal remesaAcuseEjb;
	@EJB private RemesaAnexoLocal remesaAnexoEjb;
	@EJB private RegistroEntradaLocal registroEntradaEjb;
	@EJB private EntidadLocal entidadEjb;
	
	@Autowired 
	private LemaPluginHelper pluginHelper;

	@Autowired
	private DehuDocumentManager documentManager;
	
	@Override
	public Remesa getReference(Long id) throws Exception {

		return em.getReference(Remesa.class, id);
	}

	@Override
	public Remesa findById(Long id) throws Exception {

		return em.find(Remesa.class, id);
	}

	@Override
	@SuppressWarnings(value = "unchecked")
	public List<Remesa> getAll() throws Exception {

		return em.createQuery("Select remesa from Remesa as remesa order by remesa.id").getResultList();
	}

	@Override
	public Long getTotal() throws Exception {

		Query q = em.createQuery("Select count(remesa.id) from Remesa as remesa");
		q.setHint("org.hibernate.readOnly", true);

		return (Long) q.getSingleResult();
	}

	@Override
	@SuppressWarnings(value = "unchecked")
	public List<Remesa> getPagination(int inicio) throws Exception {

		Query q = em.createQuery("Select remesa from Remesa as remesa order by remesa.id");
		q.setFirstResult(inicio);
		q.setMaxResults(RESULTADOS_PAGINACION);
		q.setHint("org.hibernate.readOnly", true);

		return q.getResultList();
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	@Override
	public void guardarNotificacionRecibida(Envio envio, Entidad entidad) throws I18NException, Exception {
		try {
			// Caso especial PRE (APB): Forzar emisor para poder registrar (EA0001301 caducado/anulado)
			String codigoDir3 = PropiedadGlobalUtil.getForzarEmisorDehu();
			
			Remesa remesa = new Remesa(
					envio.getConcepto(), 
					envio.getDescripcion(), 
					envio.getIdentificador(), 
					null,
					envio.getCodigoOrigen().intValue(),
					envio.getTipoEnvio().intValue(), 
					codigoDir3 != null ? codigoDir3 : envio.getOrganismoEmisor().getCodigoOrganismo(), 
					envio.getOrganismoEmisor().getNifOrganismo(),
					envio.getOrganismoEmisor().getNombreOrganismo(), 
					LemaUtils.xmlGregorianCalendarToDate(envio.getFechaPuestaDisposicion()), 
					envio.getTitular().getNifTitular(), 
					envio.getTitular().getNombreTitular(),
					null,
					envio.getTitular().getCodigoDIR3(),
					envio.getTitular().getDescripcionEntidad(),
					RegwebConstantes.REMESA_ESTADO_REG_PENDIENTE, 
					RegwebConstantes.REMESA_ENV_ESTADO_PENDIENTE_SEDE,
					null,
					3,
					entidad,
					null,
					TipoRemesa.RECIBIDA);

			persist(remesa);
			
			em.flush();
		} catch (Exception ex) {
			log.error("Ha habido un error guardando la notificación (identificador=" + envio.getIdentificador() + ")");
			ex.printStackTrace();
            ejbContext.setRollbackOnly();
            throw ex;
		}
	}

//	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	@Override
	public List<Remesa> guardarNotificacion(
			NotificacioV2 notificacio, 
			Entidad entidad, 
			RegistroEntrada registroEntrada,
			UsuarioEntidad usuario) throws I18NException, Exception {
		try {
			List<Remesa> remesas = new ArrayList<Remesa>();
			List<EnviamentV2> enviaments = notificacio.getEnviaments();
			Remesa remesa = null;
			
			for (EnviamentV2 enviament : enviaments) {		
				EnviamentTipus tipo = notificacio.getEnviamentTipus();
				
				remesa = new Remesa(
						notificacio.getConcepte(), 
						notificacio.getDescripcio(), 
						null,
						null,
						null,
						EnviamentTipus.NOTIFICACIO.equals(tipo) ? 2 : 1, 
						entidad.getCodigoDir3(), 
						null,
						entidad.getNombre(), 
						null, 
						enviament.getTitular().getNif(), 
						enviament.getTitular().getNom() != null ? enviament.getTitular().getNom() : enviament.getTitular().getRaoSocial(),
						enviament.getTitular().getDir3Codi(),
						null,
						null,
						RegwebConstantes.REMESA_ESTADO_REG_PENDIENTE, 
						null,
						notificacio.getProcedimentCodi(),
						3,
						entidad,
						usuario,
						TipoRemesa.ENVIADA);

				persist(remesa);
				
				actualizarRegistroEntrada(remesa.getId(), registroEntrada.getId());
								
				remesas.add(remesa);
			}
			
			return remesas;
		} catch (Exception ex) {
			log.error("Ha habido un error guardando la notificación (concepte=" + notificacio.getConcepte() + ")");
			ex.printStackTrace();
            ejbContext.setRollbackOnly();
            throw ex;
		}
	}
	
//	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	@Override
	public void actualizarNotificacionEnviada(
			List<Remesa> remesas, 
			RespostaAlta respuesta) throws I18NException, Exception {
		try {
			if (! respuesta.isError()) {
				for (Remesa remesa : remesas) {
					actualizarIdentificadorIntern(remesa.getId(), respuesta.getIdentificador());
					
					for (EnviamentReferencia enviamentReferencia : respuesta.getReferencies()) {
						if (remesa.getTitularNif() != null && remesa.getTitularNif().equals(enviamentReferencia.getTitularNif())
								|| remesa.getTitularDir3codi() != null && remesa.getTitularDir3codi().equals(enviamentReferencia.getTitularNif())) {
							actualizarReferencia(remesa.getId(), enviamentReferencia.getReferencia());
						}
					}
				}
				
				
			} else {
				for (Remesa remesa : remesas) {
					actualizarMensajeError(remesa.getId(), respuesta.getErrorDescripcio());
				}
			}
			
			em.flush();
		} catch (Exception ex) {
			log.error("Ha habido un error actualizando las remesas (identificador=" + respuesta.getIdentificador() + ")");
			ex.printStackTrace();
            ejbContext.setRollbackOnly();
            throw ex;
		}
	}
	
	@Override
	public ConsultaAcuseReciboResponse consultaGuardaAcuseRecibo(String identificador, Entidad entidad) throws I18NException, Exception {
		ConsultaAcuseReciboRequest request = new ConsultaAcuseReciboRequest();
		ConsultaAcuseReciboResponse response = new ConsultaAcuseReciboResponse();
		try {
			Remesa remesa = getByIdentificador(identificador);
			RemesaAcuse acuseInfo = remesaAcuseEjb.findByRemesa(remesa.getId());
					
			if (acuseInfo != null) {
				request.setIdentificador(identificador);
				request.setCodigoOrigen(remesa.getCodigoOrigen());
				IdentificadorAcuseRecibo identificadorAcuse = new IdentificadorAcuseRecibo();
				identificadorAcuse.setCsvResguardo(acuseInfo.getCsvResguardo());
				identificadorAcuse.setReferencia(acuseInfo.getReferencia());
				request.setIdentificadorAcuse(identificadorAcuse);
				
				response = pluginHelper.consultaAcuseRecibo(request, entidad);
				
				Contenido contenido = response.getAcuseRecibo().getContenido();
				if (contenido != null) {
					guardarDocumento(identificador, response.getAcuseRecibo().getNombreAcuse(), contenido);
				}
			}
		} catch (LemaPluginException | I18NException i18ne) {
			log.error("Error consulta acuse de recibo de la notificación con identificador: " + identificador);
			i18ne.printStackTrace();
            ejbContext.setRollbackOnly();
            throw i18ne;
		} catch (Exception ex) {
			log.error("Error consulta acuse de recibo de la notificación con identificador: " + identificador);
			ex.printStackTrace();
            ejbContext.setRollbackOnly();
            throw ex;
		}
		
		return response;
	}
	
	@SuppressWarnings("unchecked")
	private Remesa getByIdentificador(String identificador) {
		Query q = em.createQuery("Select remesa from Remesa as remesa where remesa.identificador = :identificador");

        q.setParameter("identificador", identificador);
        q.setHint("org.hibernate.readOnly", true);

        List<Remesa> remesa = q.getResultList();

        if (remesa.size() > 0) {
            return remesa.get(0);
        } else {
            return null;
        }
	}

	
	@Override
	public PeticionAccesoResponse lecturaNotificacion(String identificador, UsuarioEntidad usuarioEntidad, Entidad entidad) throws I18NException, Exception {
		PeticionAccesoRequest request = new PeticionAccesoRequest();
		PeticionAccesoResponse response = new PeticionAccesoResponse();
		try {
			boolean documentoRecuperado = false;
			Remesa remesa = getByIdentificador(identificador);
			
			request.setIdentificador(identificador);
			request.setCodigoOrigen(remesa.getCodigoOrigen());
			request.setConcepto(remesa.getConcepto());
			request.setNifReceptor(remesa.getTitularNif());
			request.setNombreReceptor(remesa.getTitularNombre());
			
			try {
				response = pluginHelper.peticionAcceso(request, entidad);
			} catch (Exception ex) {
				documentoRecuperado = intentarRecuperarDocumento(
						remesa, 
						entidad, 
						usuarioEntidad);
			}
			
			if (response != null && RegwebConstantes.LEMA_RESPUESTA_OK.equals(response.getCodigoRespuesta()) && ! documentoRecuperado) {
				actualizarEstadoYGuardaDocumentoLegal(
						remesa, 
						entidad, 
						response.getDocumento(), 
						null);
				
				// Guardar anexos remesa
				try {
					consultaGuardaAnexos(entidad, response.getAnexos(), remesa);
				} catch (Exception e) {
					log.error("Ha habido un error guardando los anexos de la notificación con identificador: " + identificador);
				}
				
				// Guardar acuse recibo remesa
				try {
					consultaGuardaAcuseRecibo(identificador, entidad);
				} catch (Exception e) {
					log.error("Ha habido un error consultando el acuse de recibo de la notificación con identificador: " + identificador);
				}
				
				actualizarUsuario(usuarioEntidad, remesa.getId());
			} 
			
			em.flush();
		} catch (LemaPluginException | I18NException i18ne) {
			log.error("Error en la lectura de la notificación con identificador: " + identificador);
			i18ne.printStackTrace();
            ejbContext.setRollbackOnly();
            throw i18ne;
		} catch (Exception ex) {
			log.error("Error en la lectura de la notificación con identificador: " + identificador);
			ex.printStackTrace();
            ejbContext.setRollbackOnly();
            throw ex;
		}
		
		return response;
	}

	@Override
	@TransactionTimeout(value = 1200) // 20 minutos
	public void actualizarEstado(String identificador, String estado) throws Exception {

		Query q = em.createQuery("update Remesa set estado=:estado where identificador = :identificador");
		q.setParameter("estado", estado);
		q.setParameter("identificador", identificador);
		q.executeUpdate();
		
	}
	
	@Override
	@TransactionTimeout(value = 1200) // 20 minutos
	public void actualizarEstadoRegistrada(String identificador, Long registroId, String estado) throws Exception {

		RegistroEntrada registro = registroEntradaEjb.findById(registroId);
		
		Query q = em.createQuery("update Remesa set estado=:estado, registro = :registro where identificador = :identificador");
		q.setParameter("estado", estado);
		q.setParameter("identificador", identificador);
		q.setParameter("registro", registro);
		q.executeUpdate();
		
	}

	@Override
	@TransactionTimeout(value = 1200) // 20 minutos
	public void actualizarEstadoNotifica(
			String identificador, 
			String identificadorIntern, 
			String referencia, 
			String estado, 
			Date fechaEstado,
			String estadoNotifica, 
			Date fechaCreacion,
			Date fechaEnvio, 
			Date fechaFinalizacion) {

		Query q = null;
		
		if (referencia != null) {
			q = em.createQuery(
					"update Remesa " + 
					"set estado=:estado, " + 
					"mensajeError=null, " +
					"estadoNotifica=:estadoNotifica, " +
					"fechaEstado=:fechaEstado, " +
					"fechaCreacion=:fechaCreacion, " +
					"fechaEnvio=:fechaEnvio, " +
					"fechaFinalizacion=:fechaFinalizacion " +
				"where identificadorIntern = :identificadorIntern and referencia = :referencia");
			q.setParameter("fechaEstado", fechaEstado);
			q.setParameter("fechaCreacion", fechaCreacion);
			q.setParameter("fechaEnvio", fechaEnvio);
			q.setParameter("fechaFinalizacion", fechaFinalizacion);
			q.setParameter("identificadorIntern", identificadorIntern);
		} else {
			q = em.createQuery(
					"update Remesa set estado=:estado, estadoNotifica=:estadoNotifica where identificador = :identificador");
			q.setParameter("identificador", identificador);
		}
		
		q.setParameter("estado", estado);
		q.setParameter("estadoNotifica", estadoNotifica);
		
		if (referencia != null)
			q.setParameter("referencia", referencia);
		q.executeUpdate();
	}
	
	@TransactionTimeout(value = 1200) // 20 minutos
	public void actualizarRegistroEntrada(Long remesaId, Long registroId) throws Exception {

		RegistroEntrada registro = registroEntradaEjb.findById(registroId);
		
		Query q = em.createQuery(
				"update Remesa set registro = :registro where id = :remesaId");
		q.setParameter("remesaId", remesaId);
		q.setParameter("registro", registro);
		q.executeUpdate();
	}
	
	@TransactionTimeout(value = 1200) // 20 minutos
	public void actualizarEnviada(Long remesaId, Date enviadaDate) throws Exception {
		
		Query q = em.createQuery(
				"update Remesa set enviadaDate = :enviadaDate, estado = :estado where id = :remesaId");
		q.setParameter("remesaId", remesaId);
		q.setParameter("estado", RegwebConstantes.REMESA_ESTADO_REG_ENVIADA);
		q.setParameter("enviadaDate", enviadaDate);
		q.executeUpdate();
	}
	
	@TransactionTimeout(value = 1200) // 20 minutos
	public void actualizarProcesada(Long remesaId, Date procesadaDate) throws Exception {

		Query q = em.createQuery(
				"update Remesa set procesadaDate = :procesadaDate, estado = :estado where id = :remesaId");
		q.setParameter("remesaId", remesaId);
		q.setParameter("estado", RegwebConstantes.REMESA_ESTADO_REG_PROCESADA);
		q.setParameter("procesadaDate", procesadaDate);
		q.executeUpdate();
	}
    
	@TransactionTimeout(value = 1200) // 20 minutos
    public void actualizarReintentosLectura(Long idRemesa, Integer reintentos) throws Exception {
        Query q = em.createQuery("update Remesa set reintentosLectura = reintentosLectura-1 where id = :idRemesa");
        
        if (reintentos != null) {
        	q = em.createQuery("update Remesa set reintentosLectura = :reintentos where id = :idRemesa");
        	q.setParameter("reintentos", reintentos);
        }
        q.setParameter("idRemesa", idRemesa);
        q.executeUpdate();
    }
	
	@TransactionTimeout(value = 1200) // 20 minutos
    public void actualizarUsuario(UsuarioEntidad usuario, Long idRemesa) throws Exception {
        Query q = em.createQuery("update Remesa set usuario = :usuario where id = :idRemesa");
        q.setParameter("usuario", usuario);
        q.setParameter("idRemesa", idRemesa);
        q.executeUpdate();
    }
	
	@TransactionTimeout(value = 1200) // 20 minutos
    public void actualizarIdentificadorIntern(Long idRemesa, String identificadorIntern) throws Exception {
        Query q = em.createQuery("update Remesa set identificadorIntern = :identificadorIntern where id = :idRemesa");
        q.setParameter("identificadorIntern", identificadorIntern);
        q.setParameter("idRemesa", idRemesa);
        q.executeUpdate();
    }
	
	@TransactionTimeout(value = 1200) // 20 minutos
    public void actualizarReferencia(Long idRemesa, String referencia) throws Exception {
        Query q = em.createQuery("update Remesa set referencia = :referencia where id = :idRemesa");
        q.setParameter("referencia", referencia);
        q.setParameter("idRemesa", idRemesa);
        q.executeUpdate();
    }
    
	@TransactionTimeout(value = 1200) // 20 minutos
	@Override
    public void actualizarMensajeError(Long idRemesa, String mensajeError) throws Exception {
        Query q = em.createQuery("update Remesa set mensajeError = :mensajeError where id = :idRemesa");
        q.setParameter("mensajeError", mensajeError);
        q.setParameter("idRemesa", idRemesa);
        q.executeUpdate();
    }
    
	@TransactionTimeout(value = 1200) // 20 minutos
    private void actualizarDescomprimido(String identificador) {
		Query q = em.createQuery("update Remesa set documentoDescomprimido = true where identificador = :identificador");
		q.setParameter("identificador", identificador);
		q.executeUpdate();
	}
    
    private boolean intentarRecuperarDocumento(Remesa remesa, Entidad entidad, UsuarioEntidad usuarioEntidad) throws I18NException, Exception {
    	// Si falla la lectura, intenta obtenir el document en una segona crida
    	boolean documentoRecuperado = false;
    	String identificador = remesa.getIdentificador();
		ConsultaRealizadaRequest requestRealizada = new ConsultaRealizadaRequest();
		requestRealizada.setIdentificador(identificador);
		requestRealizada.setCodigoOrigen(remesa.getCodigoOrigen());
		requestRealizada.setConcepto(remesa.getConcepto());
		requestRealizada.setNifPeticion(remesa.getTitularNif());
		requestRealizada.setNombrePeticion(remesa.getTitularNombre());
		
		try {
			ConsultaRealizadaResponse responseRealizada = pluginHelper.consultaRealizada(requestRealizada, entidad);
		
			if (responseRealizada != null && RegwebConstantes.LEMA_RESPUESTA_OK.equals(responseRealizada.getCodigoRespuesta()) && responseRealizada.getDocumento() != null) {
				actualizarEstadoYGuardaDocumentoLegal(
						remesa, 
						entidad, 
						responseRealizada.getDocumento(),
						null);
				
				responseRealizada.getDocumento().setContenido(null);
				
				documentoRecuperado = true;
			}
			
			actualizarUsuario(usuarioEntidad, remesa.getId());
		} catch (Exception ex) {
			if (ex.getMessage() != null && ex.getMessage().contains("4225")) { //HA arribat al màxim de reintents (3)
				actualizarEstadoYGuardaDocumentoLegal(
						remesa, 
						entidad, 
						null, 
						0);
				
				actualizarMensajeError(remesa.getId(), ex.getMessage());
				em.flush();
			} else {
				throw ex;
			}
		} catch (I18NException ex) {
			throw ex;
		}
		
		return documentoRecuperado;
    }
    
    private void actualizarEstadoYGuardaDocumentoLegal(
    		Remesa remesa, 
    		Entidad entidad, 
    		DetalleDocumento documento, 
    		Integer reintentos) throws Exception, I18NException {
    	actualizarReintentosLectura(remesa.getId(), reintentos);
    	
    	actualizarEstadoNotifica(
    			remesa.getIdentificador(), 
				null, 
				null,
				RegwebConstantes.REMESA_ESTADO_REG_LEIDA, 
				null,
				RegwebConstantes.REMESA_ENV_ESTADO_LEIDA,
				null,
				null,
				null);
    	
    	if (documento != null) {
    		guardaDocumentoRemesa(entidad, documento, remesa);
    	}
    }
    
    private void guardaDocumentoRemesa(Entidad entidad, DetalleDocumento detalle, Remesa remesa) throws Exception, I18NException {
    	String identificador = remesa.getIdentificador();
    	
		// Guardar datos acceso obtención certificación remesa
		if (detalle.getCsvResguardo() != null || detalle.getReferenciaPdfAcuse() != null)
			remesaAcuseEjb.crearReferenciaAcuse(
					detalle.getReferenciaPdfAcuse(), 
					detalle.getCsvResguardo(), 
					remesa);
		// Guardar documento remesa
		Contenido contenido = detalle.getContenido();
		String mimeType = detalle.getMimeType();
		
		if (contenido != null) {
			String nombreDocumento = "Notificación_" + identificador + "." + MimeTypeUtils.getExtensionFileName(detalle.getNombre());
			
			procesarDocumento(
					remesa.getIdentificador(), 
					contenido, 
					nombreDocumento, 
					mimeType);
		}
    }
    
    private void consultaGuardaAnexos(Entidad entidad, Anexos anexos, Remesa remesa) throws Exception, I18NException {
		if (anexos != null && anexos.getEnlaceDocumentos() != null) {
			for (EnlaceDocumento enlace : anexos.getEnlaceDocumentos()) {
				remesaAnexoEjb.crearReferenciaAnexo(enlace.getEnlaceDocumento(), null, remesa);
			}
			for (ReferenciaDocumento referencia : anexos.getReferenciaDocumentos()) {
				
				remesaAnexoEjb.crearReferenciaAnexo(null, referencia.getReferenciaDocumento(), remesa);
				
				// Consulta anexo referenciado
				ConsultaAnexoRequest request = new ConsultaAnexoRequest();
				request.setIdentificador(remesa.getIdentificador());
				request.setCodigoOrigen(remesa.getCodigoOrigen());
				request.setReferenciaAnexo(referencia.getReferenciaDocumento());
				
				ConsultaAnexoResponse response = pluginHelper.consultaAnexo(request, entidad);
				
				DocumentoAnexo anexo = response.getDocumentoAnexo();
				if (anexo != null) {
					procesarDocumento(
							remesa.getIdentificador(), 
							anexo.getContenido(), 
							anexo.getNombre(), 
							anexo.getMimeType());
				}
			}
		}
    }
    
    private void procesarDocumento(String identificador, Contenido contenido, String nombreOriginal, String mimeTypeOriginal) throws IOException {
    	List<DocumentoAnexo> documentosZip = extraerDocumentosZip(contenido, mimeTypeOriginal);
		
		if (! documentosZip.isEmpty()) {
			for (DocumentoAnexo documentoZip : documentosZip) {
				guardarDocumento(
						identificador, 
						documentoZip.getNombre(), 
						documentoZip.getContenido());
			}
			
			actualizarDescomprimido(identificador);
		} else {
			guardarDocumento(
					identificador, 
					nombreOriginal, 
					contenido);
		}
    }

	private void guardarDocumento(String identificador, String nombre, Contenido contenido) throws IOException {
    	byte[] contenidoBytes = null;
    	boolean documentExists = documentManager.documentExists(identificador, nombre);
		
		if (! documentExists) {
			if (contenido.getBase64() != null)
				contenidoBytes = Base64.decodeBase64(contenido.getBase64()); //getBytesFromDataHandler(contenido.getHref());
			else
				contenidoBytes = serializeToBase64(contenido.getContenido().getContent());
			
			documentManager.saveDocument(
					identificador, 
					nombre, 
					contenidoBytes);
		}
    }
	
    private List<DocumentoAnexo> extraerDocumentosZip(Contenido contenido, String mimeType) throws IOException {
    	List<DocumentoAnexo> documentos = new ArrayList<DocumentoAnexo>();
    	
    	if (mimeType != null && 
                (mimeType.equalsIgnoreCase("application/zip") ||
                 mimeType.equalsIgnoreCase("application/x-zip-compressed") ||
                 mimeType.equalsIgnoreCase("multipart/x-zip"))) {
    		byte[] contenidoBytes = null;
    		
    		if (contenido.getBase64() != null)
				contenidoBytes = Base64.decodeBase64(contenido.getBase64());
			else
				contenidoBytes = serializeToBase64(contenido.getContenido().getContent());

			File tempZip = null;
			try {
				// Guardar el zip en un archivo temporal
				tempZip = File.createTempFile("upload", ".zip");
				try (FileOutputStream fos = new FileOutputStream(tempZip)) {
					fos.write(contenidoBytes);
				}

				// Intentar con distintos charsets
				String[] charsets = new String[]{"UTF-8", "Cp437", "ISO-8859-1", "Windows-1252"};
				boolean success = false;

				for (String cs : charsets) {
					ZipFile zip = null;
	                try {
	                    zip = new ZipFile(tempZip, cs);
	                    Enumeration<ZipArchiveEntry> entries = zip.getEntries();

	                    while (entries.hasMoreElements()) {
	                        ZipArchiveEntry entry = entries.nextElement();
	                        if (!entry.isDirectory()) {
	                            InputStream is = zip.getInputStream(entry);
	                            ByteArrayOutputStream baos = new ByteArrayOutputStream();

	                            byte[] buffer = new byte[4096];
	                            int len;
	                            while ((len = is.read(buffer)) > 0) {
	                                baos.write(buffer, 0, len);
	                            }
	                            is.close();
	                            baos.close();

	                            // Crear DocumentoAnexo hijo
	                            String base64Extraido = Base64.encodeBase64String(baos.toByteArray());
	                            Contenido contenidoExtraido = new Contenido();
	                            contenidoExtraido.setBase64(base64Extraido);

	                            String mimeTypeZip = MimeTypeUtils.getExtensionFileName(entry.getName());

	                            byte[] rawName = entry.getRawName();
		                        String nombre;
		                        if (entry.getGeneralPurposeBit().usesUTF8ForNames()) {
		                            nombre = new String(rawName, "UTF-8");
		                        } else {
		                            nombre = new String(rawName, "Cp437");
		                        }
		                        
		                        // Assegurar que no contengui caracters no permesos
		                        nombre = revisarCaractersArxiu(nombre);
		                        
	                            DocumentoAnexo hijo = new DocumentoAnexo();
	                            hijo.setNombre(nombre);
	                            hijo.setContenido(contenidoExtraido);
	                            hijo.setMimeType(mimeTypeZip);

	                            documentos.add(hijo);
	                        }
	                    }

	                    zip.close();
	                    success = true;
	                    break;
	                } catch (IOException e) {
	                    if (zip != null) try { zip.close(); } catch (IOException ignored) {}
	                }
				}

				if (!success) {
					throw new IOException("No se pudo leer el ZIP con ningún charset válido.");
				}

			} catch (IOException e) {
				throw e;
			} finally {
				if (tempZip != null && tempZip.exists()) {
					tempZip.delete();
				}
			}
    	}
    	
		return documentos;
	}

	private String revisarCaractersArxiu(String nombre) {
		StringBuilder sb = new StringBuilder(nombre.length());

		outer:
		for (char c : nombre.toCharArray()) {
			for (char noPermitido : RegwebConstantes.CARACTERES_NO_PERMITIDOS_ARXIU) {
				if (c == noPermitido) {
					sb.append('_');
					continue outer;
				}
			}
			sb.append(c);
		}
		
		return sb.toString();
	}

	public static byte[] getBytesFromDataHandler(DataHandler dataHandler) {
        try (InputStream inputStream = dataHandler.getInputStream();
             ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {

            byte[] buffer = new byte[4096];
            int bytesRead;

            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }

            return outputStream.toByteArray();
        } catch (IOException e) {
            System.err.println("Error al obtener bytes de DataHandler: " + e.getMessage());
            return null;
        }
    }
    
	public static byte[] serializeToBase64(List<Serializable> contenido) {
		Serializable contenidoBase64 = contenido.get(0);
		return Base64.decodeBase64(contenidoBase64.toString());
	}

}
