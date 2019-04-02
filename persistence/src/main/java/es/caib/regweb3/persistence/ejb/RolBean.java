package es.caib.regweb3.persistence.ejb;

import es.caib.regweb3.model.Rol;
import es.caib.regweb3.utils.RegwebConstantes;
import org.apache.log4j.Logger;
import org.fundaciobit.genapp.common.i18n.I18NException;
import org.fundaciobit.plugins.userinformation.IUserInformationPlugin;
import org.fundaciobit.plugins.userinformation.RolesInfo;
import org.jboss.ejb3.annotation.SecurityDomain;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Fundació BIT.
 *
 * @author earrivi
 * Date: 16/01/14
 */

@Stateless(name = "RolEJB")
@SecurityDomain("seycon")
public class RolBean extends BaseEjbJPA<Rol, Long> implements RolLocal{

    protected final Logger log = Logger.getLogger(getClass());

    @PersistenceContext(unitName="regweb3")
    private EntityManager em;

    @EJB private PluginLocal pluginEjb;


    @Override
    public Rol getReference(Long id) throws Exception {

        return em.getReference(Rol.class, id);
    }

    @Override
    public Rol findById(Long id) throws Exception {

        return em.find(Rol.class, id);
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public List<Rol> getAll() throws Exception {

        return  em.createQuery("Select rol from Rol as rol order by rol.id").getResultList();
    }

    @Override
    public Long getTotal() throws Exception {

        Query q = em.createQuery("Select count(rol.id) from Rol as rol");

        return (Long) q.getSingleResult();
    }


    @Override
    @SuppressWarnings(value = "unchecked")
    public List<Rol> getPagination(int inicio) throws Exception {

        Query q = em.createQuery("Select rol from Rol as rol order by rol.id");
        q.setFirstResult(inicio);
        q.setMaxResults(RESULTADOS_PAGINACION);

        return q.getResultList();
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public List<Rol> getByRol(List<String> roles) throws Exception {

        Query q = em.createQuery("Select rol from Rol as rol where rol.nombre IN (:roles) order by rol.orden");

        q.setParameter("roles",roles);

        return q.getResultList();

    }

    @Override
    public List<Rol> obtenerRolesUserPlugin(String identificador) throws Exception, I18NException {

        IUserInformationPlugin loginPlugin = (IUserInformationPlugin) pluginEjb.getPlugin(null,RegwebConstantes.PLUGIN_USER_INFORMATION);
        RolesInfo rolesInfo = loginPlugin.getRolesByUsername(identificador);

        List<String> roles = new ArrayList<String>();
        List<Rol> rolesUsuario = null;

        if(rolesInfo != null && rolesInfo.getRoles().length > 0){

            Collections.addAll(roles, rolesInfo.getRoles());
            if(roles.size() > 0){
                rolesUsuario = getByRol(roles);
            }
        }else{
            log.info("El usuario " + identificador + " no dispone de ningun Rol de REGWEB3 en el sistema de autentificacion");
        }

        return rolesUsuario;
    }
}
