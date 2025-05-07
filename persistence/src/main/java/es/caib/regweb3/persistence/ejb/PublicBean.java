package es.caib.regweb3.persistence.ejb;

import java.io.StringReader;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.fundaciobit.genapp.common.i18n.I18NArgumentString;
import org.fundaciobit.genapp.common.i18n.I18NException;
import org.jboss.ejb3.annotation.SecurityDomain;
import org.jboss.ejb3.annotation.TransactionTimeout;
import org.plugin.notib.api.INotibPlugin;
import org.plugin.notib.api.NotibPluginException;
import org.springframework.ejb.interceptor.SpringBeanAutowiringInterceptor;

import es.caib.notib.client.domini.EnviamentEstat;
import es.caib.notib.client.domini.NotificacioEstatEnum;
import es.caib.notib.client.domini.RespostaConsultaEstatEnviamentV2;
import es.caib.notib.client.domini.RespostaConsultaEstatNotificacioV2;
import es.caib.regweb3.model.Entidad;
import es.caib.regweb3.model.Plugin;
import es.caib.regweb3.model.Remesa;
import es.caib.regweb3.utils.RegwebConstantes;

/**
 * Created by Limit Tecnologies S.L.
 * 
 * @author Jamal
 */
@Stateless(name = "PublicEJB")
@SecurityDomain("seycon")
@Interceptors(SpringBeanAutowiringInterceptor.class)
public class PublicBean implements PublicLocal {

	protected final Logger log = Logger.getLogger(getClass());

	@PersistenceContext(unitName = "regweb3")
	private EntityManager em;
	
	@Resource
	private javax.ejb.SessionContext ejbContext;

	//TODO: Codi duplicat a tramiteBean (unificar) - la implementació del callback ha de ser pública
	@SuppressWarnings("unchecked")
	@Override
	public Remesa getByIdentificadorAndReferencia(String identificadorNotib, String referencia) {
		Query q = em.createQuery(
				"Select remesa from Remesa as remesa where remesa.identificadorIntern = :identificadorIntern and remesa.referencia = :referencia");

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

	//TODO: Codi duplicat a tramiteBean (unificar) - la implementació del callback ha de ser pública
	@Override
	public void notificacionActualitzarEstado(String identificadorNotib, String referenciaEnviament, Remesa remesa)
			throws Exception, I18NException {
		// Consultar notificació per identificador i referencia i si existeix
		// actualitzar estat, sino, no fer res
		try {
			Entidad entidad = remesa.getEntidad();
			RespostaConsultaEstatEnviamentV2 resposta = consultarEnvio(referenciaEnviament,
					entidad.getId());

			RespostaConsultaEstatNotificacioV2 respostaNotificioEstat = consultarNotificacion(identificadorNotib, entidad.getId());

			if (!respostaNotificioEstat.isError()) {
				String estadoNotificacion = obtenerEstadoNotificacion(respostaNotificioEstat.getEstat());
				String estadoNotifica = obtenerEstadoEnvio(resposta.getEstat());
				Date estadoData = resposta.getEstatData();
				Date fechaCreacion = respostaNotificioEstat.getDataCreada();
				Date fechaEnviada = respostaNotificioEstat.getDataEnviada();
				Date fechaFinalizada = respostaNotificioEstat.getDataFinalitzada();

				actualizarEstadoNotifica(identificadorNotib, referenciaEnviament, estadoNotificacion,
						estadoData, estadoNotifica, fechaCreacion, fechaEnviada, fechaFinalizada);

			} else {
				actualizarMensajeError(remesa.getId(), respostaNotificioEstat.getErrorDescripcio());
			}
			em.flush();
		} catch (NotibPluginException e) {
			throw e;
		}
	}
	
	//TODO: Codi duplicat a tramiteBean (unificar) - la implementació del callback ha de ser pública
	@TransactionTimeout(value = 1200) // 20 minutos
    private void actualizarMensajeError(Long idRemesa, String mensajeError) throws Exception {
        Query q = em.createQuery("update Remesa set mensajeError = :mensajeError where id = :idRemesa");
        q.setParameter("mensajeError", mensajeError);
        q.setParameter("idRemesa", idRemesa);
        q.executeUpdate();
    }
	
	//TODO: Codi duplicat a tramiteBean (unificar) - la implementació del callback ha de ser pública
	@TransactionTimeout(value = 1200) // 20 minutos
	private void actualizarEstadoNotifica(
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
		} else {
			q = em.createQuery(
					"update Remesa set estado=:estado, estadoNotifica=:estadoNotifica where identificador = :identificador");
		}
		
		q.setParameter("estado", estado);
		q.setParameter("estadoNotifica", estadoNotifica);
		
		if (identificadorIntern != null)
			q.setParameter("identificadorIntern", identificadorIntern);
		
		if (referencia != null)
			q.setParameter("referencia", referencia);
		q.executeUpdate();
	}
	
	//TODO: Codi duplicat a notibPluginHelper (unificar) - la implementació del callback ha de ser pública
	public RespostaConsultaEstatEnviamentV2 consultarEnvio(String referenciaEnviament, Long entidadId) throws I18NException {
		RespostaConsultaEstatEnviamentV2 respuesta = null;
    	INotibPlugin notibPlugin = getINotibPlugin(entidadId);
        if (notibPlugin != null) {
	        respuesta = notibPlugin.consultarEnvio(referenciaEnviament);
        }
		return respuesta;
	}
	
	//TODO: Codi duplicat a notibPluginHelper (unificar) - la implementació del callback ha de ser pública
	public RespostaConsultaEstatNotificacioV2 consultarNotificacion(String identificador, Long entidadId) throws I18NException {
		RespostaConsultaEstatNotificacioV2  respuesta = null;
    	INotibPlugin notibPlugin = getINotibPlugin(entidadId);
        if (notibPlugin != null) {
	        respuesta = notibPlugin.consultarNotificacion(identificador);
        }
		return respuesta;
	}

	//TODO: Codi duplicat a pluginBean (unificar) - la implementació del callback ha de ser pública
    private INotibPlugin getINotibPlugin(Long entidadId) throws I18NException {
    	return (INotibPlugin) getPlugin(entidadId, RegwebConstantes.PLUGIN_NOTIB);
    }
    
    //TODO: Codi duplicat a pluginBean (unificar) - la implementació del callback ha de ser pública
    private Object getPlugin(Long idEntidad, Long tipoPlugin) throws I18NException {

        try {
            List<Plugin> plugins;

            plugins = findByEntidadTipo(idEntidad, tipoPlugin);

            if (plugins.size() > 0) {
                return cargarPlugin(plugins.get(0));
            }
        } catch (Exception e) {
            throw new I18NException(e, "error.desconegut", new I18NArgumentString(e.getMessage()));
        }

        return null;
    }
    
    //TODO: Codi duplicat a pluginBean (unificar) - la implementació del callback ha de ser pública
    private List<Plugin> findByEntidadTipo(Long idEntidad, Long tipo) throws Exception {

        String entidadQuery;

        if (idEntidad != null) {
            entidadQuery = "p.entidad = :idEntidad";
        } else {
            entidadQuery = "p.entidad is null";
        }

        Query q = em.createQuery("Select p from Plugin as p where "+entidadQuery+" and p.tipo = :tipo order by p.id");

        if (idEntidad != null) {
            q.setParameter("idEntidad", idEntidad);
        }
        q.setParameter("tipo", tipo);
        q.setHint("org.hibernate.readOnly", true);

        return q.getResultList();
    }
    
    //TODO: Codi duplicat a pluginBean (unificar) - la implementació del callback ha de ser pública
    private Object cargarPlugin(Plugin plugin) throws Exception {

        String BASE_PACKAGE = RegwebConstantes.REGWEB3_PROPERTY_BASE;

        // Si no existe el plugin, retornamos null
        if (plugin == null) {
            log.info("No existe ningun plugin de este tipo definido en el sistema", new Exception());
            return null;
        }

        // Obtenemos la clase del Plugin
        String className = plugin.getClase().trim();

        // Obtenemos sus propiedades
        Properties prop = new Properties();

        if (plugin.getPropiedadesEntidad() != null && plugin.getPropiedadesEntidad().trim().length() != 0) {
            prop.load(new StringReader(plugin.getPropiedadesEntidad()));
        }

        if (plugin.getPropiedadesAdmin() != null && plugin.getPropiedadesAdmin().trim().length() != 0) {
            prop.load(new StringReader(plugin.getPropiedadesAdmin()));
        }

        // Carregant la classe
        return org.fundaciobit.pluginsib.core.utils.PluginsManager.instancePluginByClassName(className, BASE_PACKAGE, prop);
    }
    
	private String obtenerEstadoEnvio(EnviamentEstat estat) {
		String estado = null;
		
		switch (estat) {
		case ABSENT:
			estado = RegwebConstantes.REMESA_ENV_ESTADO_AUSENTE;
			break;
		case DESCONEGUT:
			estado = RegwebConstantes.REMESA_ENV_ESTADO_DESCONOCIDO;
			break;
		case ADRESA_INCORRECTA:
			estado = RegwebConstantes.REMESA_ENV_ESTADO_DIRECCION_INCO;
			break;
		case ENVIADA_DEH:
			estado = RegwebConstantes.REMESA_ENV_ESTADO_ENVIADO_DEH;
			break;
		case ENVIADA_CI:
			estado = RegwebConstantes.REMESA_ENV_ESTADO_ENVIADO_CI;
			break;
		case ENTREGADA_OP:
			estado = RegwebConstantes.REMESA_ENV_ESTADO_ENTREGADO_OP;
			break;
		case LLEGIDA:
			estado = RegwebConstantes.REMESA_ENV_ESTADO_LEIDA;
			break;
		case ERROR_ENTREGA:
			estado = RegwebConstantes.REMESA_ENV_ESTADO_ERROR;
			break;
		case EXTRAVIADA:
			estado = RegwebConstantes.REMESA_ENV_ESTADO_EXTRAVIADA;
			break;
		case MORT:
			estado = RegwebConstantes.REMESA_ENV_ESTADO_FALLECIDO;
			break;
		case NOTIFICADA:
			estado = RegwebConstantes.REMESA_ENV_ESTADO_NOTIFICADA;
			break;
		case PENDENT_ENVIAMENT:
			estado = RegwebConstantes.REMESA_ENV_ESTADO_PENDIENTE_ENVIO;
			break;
		case PENDENT_CIE:
			estado = RegwebConstantes.REMESA_ENV_ESTADO_PENDIENTE_CIE;
			break;
		case PENDENT_DEH:
			estado = RegwebConstantes.REMESA_ENV_ESTADO_PENDIENTE_DEH;
			break;
		case PENDENT_SEU:
			estado = RegwebConstantes.REMESA_ENV_ESTADO_PENDIENTE_SEDE;
			break;
		case REBUTJADA:
			estado = RegwebConstantes.REMESA_ENV_ESTADO_REHUSADA;
			break;
		case EXPIRADA:
			estado = RegwebConstantes.REMESA_ENV_ESTADO_EXPIRADA;
			break;
		case ENVIAMENT_PROGRAMAT:
			estado = RegwebConstantes.REMESA_ENV_ESTADO_ENVIO_PROGRAM;
			break;
		case SENSE_INFORMACIO:
			estado = RegwebConstantes.REMESA_ENV_ESTADO_SIN_INFO;
			break;
		case ANULADA:
			estado = RegwebConstantes.REMESA_ENV_ESTADO_ANULADA;
			break;
		default:
			break;
		}
		
		return estado;
	}
	
	private String obtenerEstadoNotificacion(NotificacioEstatEnum estat) {
		String estado = null;
		
		switch (estat) {
		case PENDENT:
			estado = RegwebConstantes.REMESA_ESTADO_REG_PENDIENTE;
			break;
		case REGISTRADA:
			estado = RegwebConstantes.REMESA_ESTADO_REG_REGISTRADA;
			break;
		case ENVIADA:
			estado = RegwebConstantes.REMESA_ESTADO_REG_ENVIADA;
			break;
		case ENVIADA_AMB_ERRORS:
			estado = RegwebConstantes.REMESA_ESTADO_REG_ENVIADA;
			break;
		case FINALITZADA:
			estado = RegwebConstantes.REMESA_ESTADO_REG_FINALIZADA;
			break;
		case FINALITZADA_AMB_ERRORS:
			estado = RegwebConstantes.REMESA_ESTADO_REG_FINALIZADA;
			break;
		case PROCESSADA:
			estado = RegwebConstantes.REMESA_ESTADO_REG_PROCESADA;
			break;
		default:
			break;
		}
		
		return estado;
	}
}
