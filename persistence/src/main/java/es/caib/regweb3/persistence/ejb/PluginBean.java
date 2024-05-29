package es.caib.regweb3.persistence.ejb;

import es.caib.regweb3.model.Entidad;
import es.caib.regweb3.model.Plugin;
import es.caib.regweb3.utils.RegwebConstantes;
import org.fundaciobit.genapp.common.i18n.I18NArgumentCode;
import org.fundaciobit.genapp.common.i18n.I18NArgumentString;
import org.fundaciobit.genapp.common.i18n.I18NException;
import org.fundaciobit.pluginsib.core.IPlugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;


/**
 * Created by Fundació BIT.
 *
 * @author earrivi
 * Date: 05/05/16
 */
@Stateless(name = "PluginEJB")
@RolesAllowed({"RWE_SUPERADMIN", "RWE_ADMIN", "RWE_USUARI", "RWE_WS_ENTRADA", "RWE_WS_SALIDA", "RWE_WS_CIUDADANO"})
public class PluginBean extends BaseEjbJPA<Plugin, Long> implements PluginLocal {


    protected final Logger log = LoggerFactory.getLogger(getClass());

    private static final Map<Long, IPlugin> pluginsCache = new HashMap<Long, IPlugin>();

    @PersistenceContext(unitName = "regweb3")
    private EntityManager em;


    @Override
    public Plugin getReference(Long id) throws I18NException {

        return em.getReference(Plugin.class, id);
    }

    @Override
    public Plugin findById(Long id) throws I18NException {

        return em.find(Plugin.class, id);
    }

    @Override
    public Plugin merge(Plugin plugin) throws I18NException{

        deleteOfCache(plugin.getId());

        return super.merge(plugin);
    }

    @Override
    public void remove(Plugin plugin) throws I18NException{

        deleteOfCache(plugin.getId());

        super.remove(plugin);
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public List<Plugin> getAll() throws I18NException {

        return em.createQuery("Select plugin from Plugin as plugin order by plugin.id").getResultList();
    }

    @Override
    public Long getTotal() throws I18NException {

        Query q = em.createQuery("Select count(plugin.id) from Plugin as plugin");
        q.setHint("org.hibernate.readOnly", true);

        return (Long) q.getSingleResult();
    }


    @Override
    @SuppressWarnings(value = "unchecked")
    public List<Plugin> getPagination(int inicio) throws I18NException {

        Query q = em.createQuery("Select plugin from Plugin as plugin order by plugin.id");
        q.setFirstResult(inicio);
        q.setMaxResults(RESULTADOS_PAGINACION);
        q.setHint("org.hibernate.readOnly", true);

        return q.getResultList();
    }

    @Override
    public Long getTotalByEntidad(Long idEntidad, Long tipo) throws I18NException {

        String tipoWhere = "";
        if (tipo != null) {
            tipoWhere = "and p.tipo = :tipo ";
        }

        Query q = em.createQuery("Select count(p.id) from Plugin as p where p.entidad = :idEntidad " + tipoWhere);
        q.setParameter("idEntidad", idEntidad);

        if (tipo != null) {
            q.setParameter("tipo", tipo);
        }
        q.setHint("org.hibernate.readOnly", true);

        return (Long) q.getSingleResult();
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public List<Plugin> getPaginationByEntidad(int inicio, Long idEntidad, Long tipo) throws I18NException {

        String tipoWhere = "";
        if (tipo != null) {
            tipoWhere = "and p.tipo = :tipo ";
        }

        Query q = em.createQuery("Select p from Plugin as p where p.entidad = :idEntidad " + tipoWhere + " order by p.id");
        q.setParameter("idEntidad", idEntidad);
        q.setFirstResult(inicio);
        q.setMaxResults(RESULTADOS_PAGINACION);
        q.setHint("org.hibernate.readOnly", true);

        if (tipo != null) {
            q.setParameter("tipo", tipo);
        }

        return q.getResultList();
    }

    @Override
    public Long getTotalREGWEB3(Long tipo) throws I18NException {

        String tipoWhere = "";
        if (tipo != null) {
            tipoWhere = "and p.tipo = :tipo ";
        }

        Query q = em.createQuery("Select count(p.id) from Plugin as p where p.entidad is null " + tipoWhere);

        if (tipo != null) {
            q.setParameter("tipo", tipo);
        }
        q.setHint("org.hibernate.readOnly", true);

        return (Long) q.getSingleResult();
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public List<Plugin> getPaginationREGWEB3(int inicio, Long tipo) throws I18NException {

        String tipoWhere = "";
        if (tipo != null) {
            tipoWhere = "and p.tipo = :tipo ";
        }

        Query q = em.createQuery("Select p from Plugin as p where p.entidad is null " + tipoWhere + " order by p.id");
        q.setFirstResult(inicio);
        q.setMaxResults(RESULTADOS_PAGINACION);
        q.setHint("org.hibernate.readOnly", true);

        if (tipo != null) {
            q.setParameter("tipo", tipo);
        }

        return q.getResultList();
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public Plugin findByEntidadTipo(Long idEntidad, Long tipo) throws I18NException {

        String entidadQuery;

        if (idEntidad != null) {
            entidadQuery = "p.entidad = :idEntidad";
        } else {
            entidadQuery = "p.entidad is null";
        }

        Query q = em.createQuery("Select p from Plugin as p where " + entidadQuery + " and p.tipo = :tipo");

        if (idEntidad != null) {
            q.setParameter("idEntidad", idEntidad);
        }
        q.setParameter("tipo", tipo);
        q.setHint("org.hibernate.readOnly", true);

        List<Plugin> plugins = q.getResultList();

        return plugins != null && !plugins.isEmpty() ? plugins.get(0) : null;

    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public Long findIdByEntidadTipo(Long idEntidad, Long tipo) throws I18NException {

        String entidadQuery;

        if (idEntidad != null) {
            entidadQuery = "p.entidad = :idEntidad";
        } else {
            entidadQuery = "p.entidad is null";
        }

        Query q = em.createQuery("Select p.id from Plugin as p where " + entidadQuery + " and p.tipo = :tipo");

        if (idEntidad != null) {
            q.setParameter("idEntidad", idEntidad);
        }
        q.setParameter("tipo", tipo);
        q.setHint("org.hibernate.readOnly", true);

        List<Long> plugins = q.getResultList();

        return plugins != null && !plugins.isEmpty() ? plugins.get(0) : null;

    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public List<Long> getTiposPluginDefinidos(Entidad entidad) throws I18NException {

        String entidadQuery = "p.entidad = :idEntidad";

        if (entidad == null) {
            entidadQuery = "p.entidad is null";
        }

        Query q = em.createQuery("Select p.tipo from Plugin as p where " + entidadQuery);

        if (entidad != null) {
            q.setParameter("idEntidad", entidad.getId());
        }

        q.setHint("org.hibernate.readOnly", true);

        return q.getResultList();
    }

    @Override
    public Object getPlugin(Long idEntidad, Long tipoPlugin, Boolean obligatorio) throws I18NException {

        // Obtenemos el id del Plugin
        Long idPlugin = findIdByEntidadTipo(idEntidad, tipoPlugin);

        if(idPlugin == null && obligatorio){
            throw new I18NException("error.plugin.nodefinit", new I18NArgumentCode("plugin.tipo."+tipoPlugin));
        }else if(idPlugin == null){
            return null;
        }

        // Lo buscamos en la cache de plugins
        IPlugin pluginInstance = getPluginFromCache(idPlugin);

        // Si no está lo cargamos y guardamos en la cache
        if (pluginInstance == null) {
            Plugin plugin = findByEntidadTipo(idEntidad, tipoPlugin);

            pluginInstance = cargarPlugin(plugin);

            if (pluginInstance == null) {
                throw new I18NException("error.plugin.instanciando", plugin.getClase());
            }

            addPluginToCache(idPlugin, pluginInstance);
        }

        return pluginInstance;
    }

    @Override
    public Properties getPropertiesPlugin(Long idEntidad, Long tipoPlugin) throws I18NException {

        try {
            Plugin plugin = findByEntidadTipo(idEntidad, tipoPlugin);

            if (plugin != null) {
                return cargarPropiedades(plugin);
            }
        } catch (Exception e) {
            throw new I18NException(e, "error.desconegut", new I18NArgumentString(e.getMessage()));
        }

        return null;
    }

    @Override
    public boolean existPlugin(Long idEntidad, Long tipoPlugin) throws I18NException {

        try {
            Plugin plugin = findByEntidadTipo(idEntidad, tipoPlugin);

            return (plugin != null);

        } catch (Exception e) {
            throw new I18NException(e, "error.desconegut", new I18NArgumentString(e.getMessage()));
        }

    }

    @Override
    public Integer eliminarByEntidad(Long idEntidad) throws I18NException {

        List<Long> plugins = em.createQuery("Select id from Plugin where entidad = :idEntidad").setParameter("idEntidad", idEntidad).getResultList();

        for (Long id : plugins) {
            remove(findById(id));
            deleteOfCache(id);
        }

        return plugins.size();
    }

    /**
     * @param plugin
     * @return
     * @throws I18NException
     */
    private IPlugin cargarPlugin(Plugin plugin) throws I18NException {

        String BASE_PACKAGE = RegwebConstantes.REGWEB3_PROPERTY_BASE;

        // Obtenemos la clase del Plugin
        String className = plugin.getClase().trim();

        // Obtenemos sus propiedades
        Properties prop = new Properties();

        if (plugin.getPropiedadesEntidad() != null && plugin.getPropiedadesEntidad().trim().length() != 0) {
            try {
                prop.load(new StringReader(plugin.getPropiedadesEntidad()));
            } catch (IOException e) {
                throw new I18NException("error.plugin.propiedades.entidad");
            }
        }

        if (plugin.getPropiedadesAdmin() != null && plugin.getPropiedadesAdmin().trim().length() != 0) {
            try {
                prop.load(new StringReader(plugin.getPropiedadesAdmin()));
            } catch (IOException e) {
                throw new I18NException("error.plugin.propiedades.aplicacion");
            }
        }

        // Carregant la classe
        return (IPlugin) org.fundaciobit.pluginsib.core.utils.PluginsManager.instancePluginByClassName(className, BASE_PACKAGE, prop);
    }

    /**
     * @param plugin
     * @return
     * @throws I18NException
     */
    private Properties cargarPropiedades(Plugin plugin) throws I18NException {

        // Si no existe el plugin, retornamos null
        if (plugin == null) {
            log.info("No existe ningun plugin de este tipo definido en el sistema", new Exception());
            return null;
        }

        // Obtenemos sus propiedades
        Properties prop = new Properties();

        if (plugin.getPropiedadesEntidad() != null && plugin.getPropiedadesEntidad().trim().length() != 0) {
            try {
                prop.load(new StringReader(plugin.getPropiedadesEntidad()));
            } catch (IOException e) {
                throw new I18NException("error.plugin.propiedades.entidad");
            }
        }

        if (plugin.getPropiedadesAdmin() != null && plugin.getPropiedadesAdmin().trim().length() != 0) {
            try {
                prop.load(new StringReader(plugin.getPropiedadesAdmin()));
            } catch (IOException e) {
                throw new I18NException("error.plugin.propiedades.aplicacion");
            }
        }

        return prop;
    }

    /**
     * Añade una instancia de Plugin a la cache
     * @param pluginID
     * @param pluginInstance
     */
    private void addPluginToCache(Long pluginID, IPlugin pluginInstance) {
        synchronized (pluginsCache) {
            pluginsCache.put(pluginID, pluginInstance);
        }
    }

    /**
     * Obtiene una instancia de Plugin de la cache
     * @param pluginID
     * @return
     */
    private IPlugin getPluginFromCache(Long pluginID) {
        synchronized (pluginsCache) {
            return pluginsCache.get(pluginID);
        }
    }

    /**
     * Elimina una instancia de Plugin de la cache
     * @param pluginID
     * @return
     */
    private boolean deleteOfCache(Long pluginID) {
        synchronized (pluginsCache) {
            IPlugin p = pluginsCache.remove(pluginID);
            return p != null;
        }
    }
}
