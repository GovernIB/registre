package es.caib.regweb3.persistence.ejb;

import es.caib.regweb3.model.Configuracion;
import org.fundaciobit.genapp.common.i18n.I18NException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;


/**
 * Created by Fundació BIT.
 *
 * @author jpernia
 * Date: 07/07/15
 */

@Stateless(name = "ConfiguracionEJB")
@RolesAllowed({"RWE_SUPERADMIN"})
public class ConfiguracionBean extends BaseEjbJPA<Configuracion, Long> implements ConfiguracionLocal {

    protected final Logger log = LoggerFactory.getLogger(getClass());

    @PersistenceContext(unitName = "regweb3")
    private EntityManager em;


    @Override
    public Configuracion getReference(Long id) throws I18NException {

        return em.getReference(Configuracion.class, id);
    }

    @Override
    public Configuracion findById(Long id) throws I18NException {

        return em.find(Configuracion.class, id);
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public List<Configuracion> getAll() throws I18NException {

        return em.createQuery("Select configuracion from Configuracion as configuracion order by configuracion.id").getResultList();
    }

    @Override
    public Long getTotal() throws I18NException {

        Query q = em.createQuery("Select count(configuracion.id) from Configuracion as configuracion");

        return (Long) q.getSingleResult();
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public List<Configuracion> getPagination(int inicio) throws I18NException {

        Query q = em.createQuery("Select configuracion from Configuracion as configuracion order by configuracion.id");
        q.setFirstResult(inicio);
        q.setMaxResults(RESULTADOS_PAGINACION);
        q.setHint("org.hibernate.readOnly", true);

        return q.getResultList();
    }

}
