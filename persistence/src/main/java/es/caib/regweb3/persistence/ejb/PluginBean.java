package es.caib.regweb3.persistence.ejb;

import es.caib.regweb3.model.Plugin;
import es.caib.regweb3.utils.RegwebConstantes;
import org.apache.log4j.Logger;
import org.fundaciobit.genapp.common.i18n.I18NArgumentString;
import org.fundaciobit.genapp.common.i18n.I18NException;
import org.jboss.ejb3.annotation.SecurityDomain;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

//import org.fundaciobit.plugins.utils.PluginsManager;

/**
 * Created by Fundació BIT.
 *
 * @author earrivi
 *         Date: 05/05/16
 */
@Stateless(name = "PluginEJB")
@SecurityDomain("seycon")
@RolesAllowed({"RWE_SUPERADMIN", "RWE_ADMIN", "RWE_USUARI","RWE_WS_ENTRADA","RWE_WS_SALIDA","RWE_WS_CIUDADANO"})
public class PluginBean extends BaseEjbJPA<Plugin, Long> implements PluginLocal {


    protected final Logger log = Logger.getLogger(getClass());

    @PersistenceContext(unitName = "regweb3")
    private EntityManager em;


    @Override
    public Plugin getReference(Long id) throws Exception {

        return em.getReference(Plugin.class, id);
    }

    @Override
    public Plugin findById(Long id) throws Exception {

        return em.find(Plugin.class, id);
    }


    @Override
    @SuppressWarnings(value = "unchecked")
    public List<Plugin> getAll() throws Exception {

        return em.createQuery("Select plugin from Plugin as plugin order by plugin.id").getResultList();
    }

    @Override
    public Long getTotal() throws Exception {

        Query q = em.createQuery("Select count(plugin.id) from Plugin as plugin");
        q.setHint("org.hibernate.readOnly", true);

        return (Long) q.getSingleResult();
    }


    @Override
    @SuppressWarnings(value = "unchecked")
    public List<Plugin> getPagination(int inicio) throws Exception {

        Query q = em.createQuery("Select plugin from Plugin as plugin order by plugin.id");
        q.setFirstResult(inicio);
        q.setMaxResults(RESULTADOS_PAGINACION);
        q.setHint("org.hibernate.readOnly", true);

        return q.getResultList();
    }

    @Override
    public Long getTotalByEntidad(Long idEntidad, Long tipo) throws Exception {

        String tipoWhere = "";
        if(tipo != null){
            tipoWhere = "and p.tipo = :tipo ";
        }

        Query q = em.createQuery("Select count(p.id) from Plugin as p where p.entidad = :idEntidad "+tipoWhere);
        q.setParameter("idEntidad", idEntidad);

        if(tipo != null){
            q.setParameter("tipo", tipo);
        }
        q.setHint("org.hibernate.readOnly", true);

        return (Long) q.getSingleResult();
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public List<Plugin> getPaginationByEntidad(int inicio, Long idEntidad, Long tipo) throws Exception {

        String tipoWhere = "";
        if(tipo != null){
            tipoWhere = "and p.tipo = :tipo ";
        }

        Query q = em.createQuery("Select p from Plugin as p where p.entidad = :idEntidad "+tipoWhere+" order by p.id");
        q.setParameter("idEntidad", idEntidad);
        q.setFirstResult(inicio);
        q.setMaxResults(RESULTADOS_PAGINACION);
        q.setHint("org.hibernate.readOnly", true);

        if(tipo != null){
            q.setParameter("tipo", tipo);
        }

        return q.getResultList();
    }

    @Override
    public Long getTotalREGWEB3(Long tipo) throws Exception {

        String tipoWhere = "";
        if(tipo != null){
            tipoWhere = "and p.tipo = :tipo ";
        }

        Query q = em.createQuery("Select count(p.id) from Plugin as p where p.entidad is null "+tipoWhere);

        if(tipo != null){
            q.setParameter("tipo", tipo);
        }
        q.setHint("org.hibernate.readOnly", true);

        return (Long) q.getSingleResult();
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public List<Plugin> getPaginationREGWEB3(int inicio, Long tipo) throws Exception {

        String tipoWhere = "";
        if(tipo != null){
            tipoWhere = "and p.tipo = :tipo ";
        }

        Query q = em.createQuery("Select p from Plugin as p where p.entidad is null "+tipoWhere+" order by p.id");
        q.setFirstResult(inicio);
        q.setMaxResults(RESULTADOS_PAGINACION);
        q.setHint("org.hibernate.readOnly", true);

        if(tipo != null){
            q.setParameter("tipo", tipo);
        }

        return q.getResultList();
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public List<Plugin> findByEntidadTipo(Long idEntidad, Long tipo) throws Exception {

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

    @Override
    public Object getPlugin(Long idEntidad, Long tipoPlugin) throws I18NException {

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

    @Override
    public Properties getPropertiesPlugin(Long idEntidad, Long tipoPlugin) throws I18NException{

        try {
            List<Plugin> plugins;

            plugins = findByEntidadTipo(idEntidad, tipoPlugin);

            if (plugins.size() > 0) {
                return cargarPropiedades(plugins.get(0));
            }
        } catch (Exception e) {
            throw new I18NException(e, "error.desconegut", new I18NArgumentString(e.getMessage()));
        }

        return null;
    }

    @Override
    public boolean existPlugin(Long idEntidad, Long tipoPlugin) throws I18NException {

        try {
            List<Plugin> plugins;

            plugins = findByEntidadTipo(idEntidad, tipoPlugin);

            return (plugins.size()>0);
        } catch (Exception e) {
            throw new I18NException(e, "error.desconegut", new I18NArgumentString(e.getMessage()));
        }

    }

    @Override
    public List<Object> getPlugins(Long idEntidad, Long tipoPlugin) throws Exception{

        List<Plugin> plugins = findByEntidadTipo(idEntidad, tipoPlugin);

        if (plugins.size() > 0) {

            List<Object> pluginsCargados = new ArrayList<Object>();
            for (Plugin plugin : plugins) {
                pluginsCargados.add(cargarPlugin(plugin));
            }

            return pluginsCargados;
        }

        return null;
    }

    @Override
    public Integer eliminarByEntidad(Long idEntidad) throws Exception {

        List<?> plugins = em.createQuery("Select id from Plugin where entidad = :idEntidad").setParameter("idEntidad", idEntidad).getResultList();

        for (Object id : plugins) {
            remove(findById((Long) id));
        }

        return plugins.size();
    }

    /**
     *
     * @param plugin
     * @return
     * @throws Exception
     */
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

    /**
     *
     * @param plugin
     * @return
     * @throws Exception
     */
    private Properties cargarPropiedades(Plugin plugin) throws Exception {

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

        return prop;
    }
}
