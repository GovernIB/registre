package es.caib.regweb3.persistence.ejb;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.fundaciobit.genapp.common.i18n.I18NException;
import org.jboss.ejb3.annotation.SecurityDomain;
import org.plugin.lema.api.Envio;
import org.plugin.lema.api.LemaPluginException;
import org.plugin.lema.api.LocalizaRequest;
import org.plugin.lema.api.LocalizaResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ejb.interceptor.SpringBeanAutowiringInterceptor;
import org.springframework.util.DefaultPropertiesPersister;

import es.caib.regweb3.model.Entidad;
import es.caib.regweb3.model.Remesa;
import es.caib.regweb3.model.Remesa.TipoRemesa;
import es.caib.regweb3.persistence.utils.LemaPluginHelper;
import es.caib.regweb3.persistence.utils.LemaUtils;
import es.caib.regweb3.persistence.utils.Paginacion;
import es.caib.regweb3.persistence.utils.PropiedadGlobalUtil;
import es.caib.regweb3.persistence.utils.RemesasProgress;
import es.caib.regweb3.utils.RegwebConstantes;
import es.caib.regweb3.utils.StringUtils;

/**
 * Created by Limit Tecnologies S.L.
 * 
 * @author Jamal
 */
@Stateless(name = "RemesaConsultaEJB")
@SecurityDomain("seycon")
@Interceptors(SpringBeanAutowiringInterceptor.class)
public class RemesaConsultaBean extends BaseEjbJPA<Remesa, Long> implements RemesaConsultaLocal {

	protected final Logger log = Logger.getLogger(getClass());
	
	@PersistenceContext(unitName = "regweb3")
	private EntityManager em;
	
	@EJB
	private RemesaLocal remesaEjb;

	@Resource
	private javax.ejb.SessionContext ejbContext;

	@Autowired
	private LemaPluginHelper pluginHelper;

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

		Query q = em.createQuery("Select remesa from Remesa as remesa where remesa.tipoRemesa = :tipoRemesa order by remesa.id");
		
		q.setParameter("tipoRemesa", TipoRemesa.RECIBIDA);
		
		return q.getResultList();
	}

	@Override
	public Long getTotal() throws Exception {

		Query q = em.createQuery("Select count(remesa.id) from Remesa as remesa where remesa.tipoRemesa = :tipoRemesa ");
		q.setHint("org.hibernate.readOnly", true);
		q.setParameter("tipoRemesa", TipoRemesa.RECIBIDA);

		return (Long) q.getSingleResult();
	}

	@Override
	@SuppressWarnings(value = "unchecked")
	public List<Remesa> getPagination(int inicio) throws Exception {

		Query q = em.createQuery("Select remesa from Remesa as remesa where remesa.tipoRemesa = :tipoRemesa order by remesa.id");
		q.setFirstResult(inicio);
		q.setMaxResults(RESULTADOS_PAGINACION);
		q.setParameter("tipoRemesa", TipoRemesa.RECIBIDA);
		q.setHint("org.hibernate.readOnly", true);

		return q.getResultList();
	}

	@Override
	@SuppressWarnings(value = "unchecked")
	public List<Remesa> getByEntidad(Long idEntidad) throws Exception {

		Query q = em.createQuery(
				"Select remesa from Remesa as remesa where remesa.destinatario.entidad.id = :idEntidad and remesa.tipoRemesa = :tipoRemesa order by remesa.id");
		q.setParameter("idEntidad", idEntidad);
		q.setParameter("tipoRemesa", TipoRemesa.RECIBIDA);
		q.setHint("org.hibernate.readOnly", true);

		return q.getResultList();

	}
	
	@Override
	@SuppressWarnings(value = "unchecked")
	public List<Remesa> findByEntidadAndEstado(Long idEntidad, String estado) throws Exception {

		Query q = em.createQuery(
				"Select remesa from Remesa as remesa where remesa.entidad.id = :idEntidad and remesa.estado = :estado and remesa.tipoRemesa = :tipoRemesa order by remesa.id");
		q.setParameter("idEntidad", idEntidad);
		q.setParameter("tipoRemesa", TipoRemesa.RECIBIDA);
		q.setParameter("estado", estado);
		q.setHint("org.hibernate.readOnly", true);

		return q.getResultList();

	}

	@Override
	public Paginacion busqueda(Integer pageNumber, List<String> estados, Remesa remesa, String emisor, Date fechaPuestaDisposicionDesde,
			Date fechaPuestaDisposicionHasta, Long idEntidad) throws Exception {

		Query q;
		Query q2;
		Map<String, Object> parametros = new HashMap<String, Object>();
		List<String> where = new ArrayList<String>();

		StringBuilder query = new StringBuilder("Select remesa from Remesa as remesa ");

		// Entidad
		where.add("remesa.entidad.id = :idEntidad ");
		parametros.put("idEntidad", idEntidad);

		// Identificador
		if (StringUtils.isNotEmpty(remesa.getIdentificador())) {
			where.add("remesa.identificador = :identificador ");
			parametros.put("identificador", remesa.getIdentificador());
		}

		if (StringUtils.isNotEmpty(emisor)) {
			where.add("(LOWER(remesa.organoEmisorCodigo) like :emisor or LOWER(remesa.organoEmisorNombre) like :emisor) ");
			parametros.put("emisor", '%' + emisor.toLowerCase() + '%');
		}

		// Concepto
		if (StringUtils.isNotEmpty(remesa.getConcepto())) {
			where.add("LOWER(remesa.concepto) like :concepto ");
			parametros.put("concepto", '%' + remesa.getConcepto().toLowerCase() + '%');
		}

		// Estado
		if (estados != null && ! estados.isEmpty()) {
			where.add("remesa.estado in (:estados) ");
			parametros.put("estados", estados);
		}

		// Fecha puesta disposición desde
		if (fechaPuestaDisposicionDesde != null) {
			where.add(" remesa.fechaPuestaDisposicion >= :fechaPuestaDisposicionDesde ");
			parametros.put("fechaPuestaDisposicionDesde", fechaPuestaDisposicionDesde);
		}

		// Fecha puesta disposición hasta
		if (fechaPuestaDisposicionHasta != null) {
			where.add(" remesa.fechaPuestaDisposicion <= :fechaPuestaDisposicionHasta ");
			parametros.put("fechaPuestaDisposicionHasta", fechaPuestaDisposicionHasta);
		}
		
		where.add(" remesa.tipoRemesa = :tipoRemesa  ");
		parametros.put("tipoRemesa", TipoRemesa.RECIBIDA);

		// Añadimos los parámetros a la query
		if (parametros.size() != 0) {
			query.append("where ");
			int count = 0;
			for (String w : where) {
				if (count != 0) {
					query.append(" and ");
				}
				query.append(w);
				count++;
			}
			q2 = em.createQuery(query.toString().replaceAll("Select remesa from Remesa as remesa ",
					"Select count(remesa.id) from Remesa as remesa "));
			query.append("order by remesa.fechaPuestaDisposicion desc");
			q = em.createQuery(query.toString());

			for (Map.Entry<String, Object> param : parametros.entrySet()) {
				q.setParameter(param.getKey(), param.getValue());
				q2.setParameter(param.getKey(), param.getValue());
			}

		} else {
			q2 = em.createQuery(query.toString().replaceAll("Select remesa from Remesa as remesa ",
					"Select count(remesa.id) from Remesa as remesa "));
			query.append("order by remesa.fechaPuestaDisposicion desc");
			q = em.createQuery(query.toString());
		}

		Paginacion paginacion;

		if (pageNumber != null) { // Comprobamos si es una busqueda paginada o no
			q2.setHint("org.hibernate.readOnly", true);
			Long total = (Long) q2.getSingleResult();
			paginacion = new Paginacion(total.intValue(), pageNumber);
			int inicio = (pageNumber - 1) * BaseEjbJPA.RESULTADOS_PAGINACION;
			q.setFirstResult(inicio);
			q.setMaxResults(BaseEjbJPA.RESULTADOS_PAGINACION);
			q.setHint("org.hibernate.readOnly", true);
		} else {
			paginacion = new Paginacion(0, 0);
		}

		paginacion.setListado(q.getResultList());

		return paginacion;

	}

	@Override
	public Long remesasPendientes(Long idUsuarioEntidad) throws Exception {

		Query q = em.createQuery("Select count(n.id) from Remesa as n where n.destinatario.id = :idUsuarioEntidad and remesa.tipoRemesa = :tipoRemesa  "
				+ "and n.estado = :nueva");

		q.setParameter("idUsuarioEntidad", idUsuarioEntidad);
		q.setParameter("tipoRemesa", TipoRemesa.RECIBIDA);
		q.setParameter("nueva", RegwebConstantes.NOTIFICACION_ESTADO_NUEVA);
		q.setHint("org.hibernate.readOnly", true);

		return (Long) q.getSingleResult();
	}

	@Override
	public RemesasProgress localizaGuardaNotificaciones(Entidad entidad, Date fechaDesde, Date fechaHasta) throws I18NException, Exception {
		RemesasProgress progreso = null;
		try {
			LocalizaResponse response = null;
			
			if (fechaDesde == null) {
				String fechaDesdeStr = getFechaInicioProximaLocalizacion(entidad.getId());
				fechaDesde = LemaUtils.convertStringToDate(fechaDesdeStr);
			}
			
			if (fechaHasta == null)
				fechaHasta = new Date();
			
			LocalizaRequest request = new LocalizaRequest();
			Integer notificacionesError = 0;
			request.setFechaDesde(fechaDesde);
			request.setFechaHasta(fechaHasta);

			response = pluginHelper.localizaPendientes(request, entidad);

			if (response != null && RegwebConstantes.LEMA_RESPUESTA_OK.equals(response.getCodigoRespuesta())) {
				List<Envio> envios = response.getEnvios();
				progreso = new RemesasProgress(envios.size());
				
				for (Envio envio : envios) {

					boolean existeEnvio = getByIdentificador(envio.getIdentificador()) != null;

					if (!existeEnvio) {
						try {
							remesaEjb.guardarNotificacionRecibida(envio, entidad);
							progreso.incrementarProcesadas();
						} catch (Exception e) {
							notificacionesError++;
						}
					} else {
						progreso.incrementarDuplicadas();
					}
				}

				if (notificacionesError == 0)
					updateFechaInicioProximaLocalizacion(entidad.getId(), LemaUtils.convertDateToString(fechaHasta));
				
			} else if (response != null) {
				throw new LemaPluginException(response.getCodigoRespuesta() + " " + response.getDescripcionRespuesta());
			}
			
		} catch (LemaPluginException | I18NException i18ne) {
			log.error("Ha habido un error lozalizando las notificaciones en DEHú");
			i18ne.printStackTrace();
			ejbContext.setRollbackOnly();
			throw i18ne;
		} catch (Exception ex) {
			log.error("Ha habido un error lozalizando las notificaciones en DEHú");
			ex.printStackTrace();
			ejbContext.setRollbackOnly();
			throw ex;
		}
		
		return progreso;
	}
	
	// Estaba en remesabean
	@SuppressWarnings("unchecked")
	@Override
	public Remesa getByIdentificador(String identificador) {
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
	public Remesa getByIdentificadorAndReferencia(String identificadorNotib, String referencia) {
		Query q = em.createQuery("Select remesa from Remesa as remesa where remesa.identificadorIntern = :identificadorIntern and remesa.referencia = :referencia");

        q.setParameter("identificadorIntern", identificadorNotib);
        q.setParameter("referencia", referencia);
        q.setHint("org.hibernate.readOnly", true);

        List<Remesa> remesa = q.getResultList();

        if (remesa.size() > 0) {
            return remesa.get(0);
        } else {
            return null;
        }
	}   

	// Estaba en remesabean
	@SuppressWarnings("unchecked")
	@Override
	public List<Remesa> findByRegistroEntrada(Long registroId) {
		Query q = em.createQuery("Select remesa from Remesa as remesa where remesa.registro.id = :registroId");

        q.setParameter("registroId", registroId);
        q.setHint("org.hibernate.readOnly", true);

        List<Remesa> remesas = q.getResultList();

        if (remesas.size() > 0) {
            return remesas;
        } else {
            return null;
        }
	}

	@Override
	public Remesa getById(Long remesaId) throws Exception {
		return findById(remesaId);
	}
	
	private String getFechaInicioProximaLocalizacion(Long idEntidad) {
		String path = PropiedadGlobalUtil.getFechaInicioBusquedaNotificacionesPath(idEntidad);
		Properties properties = new Properties();
		String fechaInicio = null;
		try {
			log.debug("Recuperando fecha próxima inicio búsqueda notificaciones y comunicaciones pendientes...");
			if (path == null || path == "") {
				path = "/opt/files/";
			}
			File file = new File(path + "/lema.properties");
			properties.load(new FileInputStream(file));
			fechaInicio = properties.getProperty("busqueda.notificaciones.fecha.fechaInicio");
		} catch (Exception ex) {
			log.error("Error en la lectura de la fecha de inicio de búsqueda registros SIR", ex);
			throw new RuntimeException(
					"Error en la lectura de la fecha de inicio de búsqueda registros SIR: " + ex.getMessage());
		}
		return fechaInicio;
	}

	private void updateFechaInicioProximaLocalizacion(Long idEntidad, String fechaFinActual) {
		String path = PropiedadGlobalUtil.getFechaInicioBusquedaNotificacionesPath(idEntidad);
		Properties properties = new Properties();
		File file;
		try {
			properties.setProperty("busqueda.notificaciones.fecha.fechaInicio", fechaFinActual);
			if (path == null || path == "") {
				path = "/opt/files/";
			}
			file = new File(path + "/lema.properties");
			OutputStream out = new FileOutputStream(file);

			DefaultPropertiesPersister p = new DefaultPropertiesPersister();
			p.store(properties, out, "Fecha inicio próximo búsqueda notificaciones y comunicaciones pendientes.");
		} catch (Exception ex) {
			log.error("Error a la hora de escribir el fichero config", ex);
			ex.printStackTrace();
		}
	}
	
//    private void enviarEmailResumen(
//    		int totalNotificaciones, 
//    		int totalNotificacionesError, 
//    		String fechaDesde, 
//    		String fechaHasta,
//			Entidad entidad) {
//    	Locale locale = new Locale(RegwebConstantes.IDIOMA_CATALAN_CODIGO);
//		// Obtenemos los usuarios a los que hay que enviarles el mail
//        List<Usuario> usuariosANotificar = new ArrayList<Usuario>();
//        String entorno = PropiedadGlobalUtil.getEntorno();
//    	try {
//
//            // Propietario Entidad
//            usuariosANotificar.add(entidad.getPropietario());
//
//            // Administradores Entidad
//            for (UsuarioEntidad usuarioEntidad : entidad.getAdministradores()) {
//                usuariosANotificar.add(usuarioEntidad.getUsuario());
//            }
//
//            // Asunto
//            String[] argsEntorno = {entorno != null ? entorno : "PRO"};
//            String asunto = I18NLogicUtils.tradueix(locale, "registro.lema.resultado.consulta.mail.asunto", argsEntorno);
//
//            String[] args = {
//            		String.valueOf(totalNotificaciones), 
//            		String.valueOf(totalNotificaciones - totalNotificacionesError), 
//            		fechaDesde,
//            		fechaHasta,
//            		getPeriodoConsultaNotificaciones(),
//            		entidad.getNombre()};
//            String mensajeTexto = I18NLogicUtils.tradueix(locale, "registro.lema.resultado.consulta.mail.cuerpo", args);
//
//            //Enviamos el mail a todos los usuarios
//            InternetAddress addressFrom = new InternetAddress(RegwebConstantes.APLICACION_EMAIL, RegwebConstantes.APLICACION_NOMBRE);
//
//    		String usuariosAvisoAdicionales = pluginHelper.getUsuariosAdicionales("usuarios.aviso", entidad);
//    		if (StringUtils.isNotEmpty(usuariosAvisoAdicionales)) {
//    			String[] usuariosAvisoAdicionalesArr = usuariosAvisoAdicionales.split(",");
//    			
//    			for (String usuarioAviso : usuariosAvisoAdicionalesArr) {
//        			MailUtils.enviaMail(asunto, mensajeTexto, addressFrom, Message.RecipientType.TO, usuarioAviso);
//				}
//    		
//    		}
//    		
//    		for (Usuario usuario : usuariosANotificar) {
//
//                if (StringUtils.isNotEmpty(usuario.getEmail())) {
//                	MailUtils.enviaMail(asunto, mensajeTexto, addressFrom, Message.RecipientType.TO, usuario.getEmail());
//                }
//    		}
//    		
//        } catch (Exception e) {
//            log.error("Se ha producido una excepcion enviando email informando de un error recepción SIR");
//            e.printStackTrace();
//        } catch (I18NException e) {
//        	log.error("Ha habido un error recuperando los destinatarios del plugin de LEMA");
//			e.printStackTrace();
//		}
//    }
    
    private String getPeriodoConsultaNotificaciones () {
    	Long periodoConsultaNotificaciones = PropiedadGlobalUtil.getCronTareaPeriodoConsultaNotificacionesDehu();
    	
    	return String.valueOf(periodoConsultaNotificaciones != null ? (periodoConsultaNotificaciones / (1000 * 60)) : "");
    }
}
