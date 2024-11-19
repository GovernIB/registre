package es.caib.regweb3.persistence.ejb;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
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
import org.apache.log4j.Logger;
import org.fundaciobit.genapp.common.i18n.I18NException;
import org.jboss.ejb3.annotation.SecurityDomain;
import org.jboss.ejb3.annotation.TransactionTimeout;
import org.plugin.lema.api.Anexos;
import org.plugin.lema.api.ConsultaAcuseReciboRequest;
import org.plugin.lema.api.ConsultaAcuseReciboResponse;
import org.plugin.lema.api.ConsultaAnexoRequest;
import org.plugin.lema.api.ConsultaAnexoResponse;
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

import es.caib.regweb3.model.Entidad;
import es.caib.regweb3.model.RegistroEntrada;
import es.caib.regweb3.model.Remesa;
import es.caib.regweb3.model.RemesaAcuse;
import es.caib.regweb3.persistence.utils.DehuDocumentManager;
import es.caib.regweb3.persistence.utils.LemaPluginHelper;
import es.caib.regweb3.persistence.utils.LemaUtils;
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
	public void guardaNotificacion(Envio envio, Entidad entidad) throws I18NException, Exception {
		try {
			Remesa remesa = new Remesa(
					envio.getConcepto(), 
					envio.getDescripcion(), 
					envio.getIdentificador(), 
					envio.getCodigoOrigen().intValue(),
					envio.getTipoEnvio().intValue(), 
					envio.getOrganismoEmisor().getCodigoOrganismo(), 
					envio.getOrganismoEmisor().getNombreOrganismo(), 
					LemaUtils.xmlGregorianCalendarToDate(envio.getFechaPuestaDisposicion()), 
					envio.getTitular().getNifTitular(), 
					envio.getTitular().getNombreTitular(),
					envio.getTitular().getCodigoDIR3(),
					envio.getTitular().getDescripcionEntidad(),
					RegwebConstantes.REMESA_ESTADO_REG_PENDIENTE, 
					RegwebConstantes.REMESA_ENV_ESTADO_PENDIENTE,
					null,
					3,
					entidad);

			persist(remesa);
			
			em.flush();
		} catch (Exception ex) {
			log.error("Ha habido un error guardando la notificación (identificador=" + envio.getIdentificador() + ")");
			ex.printStackTrace();
            ejbContext.setRollbackOnly();
            throw ex;
		}
		
//		try {
//			LocalizaRealizadasRequest requestRealizadas = new LocalizaRealizadasRequest();
//			requestRealizadas.setFechaDesde(LemaUtils.convertStringToDate(fechaDesde));
//			
//			// localizaRealizadas no permite hora: 4207 La fecha actual enviada se encuentra fuera del margen permitido
//			Calendar calendar = Calendar.getInstance();
//			calendar.setTime(fechaHasta);
//			calendar.set(Calendar.HOUR_OF_DAY, 0);
//			calendar.set(Calendar.MINUTE, 0);
//			calendar.set(Calendar.SECOND, 0);
//			calendar.set(Calendar.MILLISECOND, 0);
//			requestRealizadas.setFechaHasta(calendar.getTime());
//			
//			responseRealizadas = pluginHelper.localizaRealizadas(requestRealizadas, entidad);
//			if (responseRealizadas != null && RegwebConstantes.LEMA_RESPUESTA_OK.equals(responseRealizadas.getCodigoRespuesta())) {
//				for (EnvioRealizada envio : responseRealizadas.getEnvios()) {
//					
//					boolean existeEnvio = findByIdentificador(envio.getIdentificador()) != null;
//					
//					if (! existeEnvio) {
//						Remesa remesa = new Remesa(
//								envio.getConcepto(), 
//								envio.getDescripcion(), 
//								envio.getIdentificador(), 
//								envio.getCodigoOrigen().intValue(),
//								Integer.valueOf(envio.getTipoEnvio()), 
//								envio.getOrganismoEmisor().getCodigoOrganismo(), 
//								envio.getOrganismoEmisor().getNombreOrganismo(), 
//								LemaUtils.xmlGregorianCalendarToDate(envio.getFechaPuestaDisposicion()), 
//								envio.getTitular().getNifTitular(), 
//								envio.getTitular().getNombreTitular(), 
//								envio.getTitular().getCodigoDIR3(),
//								envio.getTitular().getDescripcionEntidad(),
//								RegwebConstantes.REMESA_ESTADO_REG_LEIDA, 
//								envio.getEstado().name(),
//								envio.getCodigoProcedimiento().getCodigo(),
//								3,
//								entidad);
//	
//						persist(remesa);
//						
//						em.flush();
//					}
//				}
//				
//			}
//			
//		} catch (LemaPluginException | I18NException i18ne) {
//			log.error("Ha habido un error lozalizando las notificaciones en DEHú");
//			i18ne.printStackTrace();
////            ejbContext.setRollbackOnly();
////            throw i18ne;
//		} catch (Exception ex) {
//			log.error("Ha habido un error lozalizando las notificaciones en DEHú");
//			ex.printStackTrace();
////            ejbContext.setRollbackOnly();
////            throw ex;
//		}
		
	}
	
//	@Override
//	public Remesa findByIdentificadorWithDocumentos(String identificador, Entidad entidad) throws I18NException, Exception {
//		try {
//			Remesa remesa = findByIdentificador(identificador);
//			
////			request.setIdentificador(identificador);
////			request.setCodigoOrigen(remesa.getCodigoOrigen().intValue());
////			request.setConcepto(remesa.getConcepto());
////			request.setNifPeticion(remesa.getTitularNif());
////			request.setNombrePeticion(remesa.getTitularNombre());
////			
////			response = pluginHelper.consultaRealizada(request, entidad);
//
//			// Guardar anexos notificación
//	    	RemesaAnexo remesaAnexo = remesaAnexoEjb.findByRemesa(remesa.getId());
//	    	
//	    	// Consulta anexo referenciado
//			ConsultaAnexoRequest request = new ConsultaAnexoRequest();
//			request.setIdentificador(remesa.getIdentificador());
//			request.setCodigoOrigen(remesa.getCodigoOrigen());
//			request.setReferenciaAnexo(remesaAnexo.getReferencia());
//			
//			ConsultaAnexoResponse response = pluginHelper.consultaAnexo(request, entidad);
//			
//			DocumentoAnexo anexo = response.getDocumentoAnexo();
//			if (anexo != null) {
//				guardarDocumento(
//						remesa.getIdentificador(), 
//						anexo.getNombre(), 
//						anexo.getContenido());
//			}
//			
//		} catch (LemaPluginException | I18NException i18ne) {
//			log.error("Error consulta de la notificación con identificador: " + identificador);
//			i18ne.printStackTrace();
//            ejbContext.setRollbackOnly();
//            throw i18ne;
//		} catch (Exception ex) {
//			log.error("Error consulta de la notificación con identificador: " + identificador);
//			ex.printStackTrace();
//            ejbContext.setRollbackOnly();
//            throw ex;
//		}
//		
//		return response;
//	}
	
	@Override
	public ConsultaAcuseReciboResponse consultaGuardaAcuseRecibo(String identificador, Entidad entidad) throws I18NException, Exception {
		ConsultaAcuseReciboRequest request = new ConsultaAcuseReciboRequest();
		ConsultaAcuseReciboResponse response = new ConsultaAcuseReciboResponse();
		try {
			Remesa remesa = findByIdentificador(identificador);
			RemesaAcuse acuseInfo = remesaAcuseEjb.findByRemesa(remesa.getId());
					
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
	
	@Override
	public PeticionAccesoResponse lecturaNotificacion(String identificador, Entidad entidad) throws I18NException, Exception {
		PeticionAccesoRequest request = new PeticionAccesoRequest();
		PeticionAccesoResponse response = new PeticionAccesoResponse();
		try {
			Remesa remesa = findByIdentificador(identificador);
			
			request.setIdentificador(identificador);
			request.setCodigoOrigen(remesa.getCodigoOrigen());
			request.setConcepto(remesa.getConcepto());
			request.setNifReceptor(remesa.getTitularNif());
			request.setNombreReceptor(remesa.getTitularNombre());
			
			response = pluginHelper.peticionAcceso(request, entidad);
			
			if (response != null && RegwebConstantes.LEMA_RESPUESTA_OK.equals(response.getCodigoRespuesta())) {
				actualizarEstadoNotifica(
						identificador, 
						RegwebConstantes.REMESA_ESTADO_REG_LEIDA, 
						RegwebConstantes.REMESA_ENV_ESTADO_ACEPTADA);
				
				if (response.getDocumento() != null) {
					DetalleDocumento detalle = response.getDocumento();
					remesaAcuseEjb.crearReferenciaAcuse(
							detalle.getReferenciaPdfAcuse(), 
							detalle.getCsvResguardo(), 
							remesa);
					// Guardar documento notificación
					Contenido contenido = detalle.getContenido();
					if (contenido != null) {
						String nombreDocumento = "Notificación_" + identificador + "." + MimeTypeUtils.getExtensionFileName(detalle.getNombre());
						guardarDocumento(identificador, nombreDocumento, contenido);
						
						response.getDocumento().setContenido(null);
					}
				}
				
				// Guardar anexos notificación
				try {
					consultaGuardaAnexos(entidad, response.getAnexos(), remesa);
				} catch (Exception e) {
					log.error("Ha habido un error guardando los anexos de la notificación con identificador: " + identificador);
				}
				
				// Guardar acuse recibo notificación
				try {
					consultaGuardaAcuseRecibo(identificador, entidad);
				} catch (Exception e) {
					log.error("Ha habido un error consultando el acuse de recibo de la notificación con identificador: " + identificador);
				}
				
				actualizarReintentosLectura(remesa.getId());
				
				em.flush();
			} 
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
	
	@SuppressWarnings("unchecked")
	@Override
	public Remesa findByIdentificador(String identificador) {
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

	@SuppressWarnings("unchecked")
	@Override
	public Remesa findByRegistroEntrada(Long registroId) {
		Query q = em.createQuery("Select remesa from Remesa as remesa where remesa.registro.id = :registroId");

        q.setParameter("registroId", registroId);
        q.setHint("org.hibernate.readOnly", true);

        List<Remesa> remesa = q.getResultList();

        if (remesa.size() > 0) {
            return remesa.get(0);
        } else {
            return null;
        }
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

	@TransactionTimeout(value = 1200) // 20 minutos
	public void actualizarEstadoNotifica(String identificador, String estado, String estadoNotifica) {

		Query q = em.createQuery(
				"update Remesa set estado=:estado, estadoNotifica=:estadoNotifica where identificador = :identificador");
		q.setParameter("estado", estado);
		q.setParameter("estadoNotifica", estadoNotifica);
		q.setParameter("identificador", identificador);
		q.executeUpdate();
	}
    
    public void actualizarReintentosLectura(Long idRemesa) throws Exception {
        Query q = em.createQuery("update Remesa set reintentosLectura = reintentosLectura-1 where id = :idRemesa");
        q.setParameter("idRemesa", idRemesa);
        q.executeUpdate();
    }
    
    private void guardarDocumento(String identificador, String nombre, Contenido contenido) {
    	byte[] contenidoBytes = null;
    	boolean documentExists = documentManager.documentExists(identificador, nombre);
		
		if (! documentExists) {
			if (contenido.getHref() != null)
				contenidoBytes = getBytesFromDataHandler(contenido.getHref());
			else
				contenidoBytes = serializeToBase64(contenido.getContenido().getContent());
			
			documentManager.saveDocument(
					identificador, 
					nombre, 
					contenidoBytes);
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
					guardarDocumento(
							remesa.getIdentificador(), 
							anexo.getNombre(), 
							anexo.getContenido());
				}
			}
		}
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
