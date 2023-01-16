package es.caib.regweb3.persistence.ejb;

import es.caib.regweb3.model.CatServicio;
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
 * @author earrivi
 * Date: 16/01/14
 */

@Stateless(name = "CatServicioEJB")
@RolesAllowed({"RWE_SUPERADMIN", "RWE_ADMIN", "RWE_USUARI", "RWE_WS_ENTRADA", "RWE_WS_SALIDA"})
public class CatServicioBean extends BaseEjbJPA<CatServicio, Long> implements CatServicioLocal {

    protected final Logger log = LoggerFactory.getLogger(getClass());

    @PersistenceContext(unitName = "regweb3")
    private EntityManager em;


    @Override
    public CatServicio getReference(Long id) throws I18NException {

        return em.getReference(CatServicio.class, id);
    }

    @Override
    public CatServicio findById(Long id) throws I18NException {

        return em.find(CatServicio.class, id);
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public List<CatServicio> getAll() throws I18NException {

        return em.createQuery("Select catServicio from CatServicio as catServicio order by catServicio.descServicio").getResultList();
    }

    @Override
    public Long getTotal() throws I18NException {

        Query q = em.createQuery("Select count(catServicio.id) from CatServicio as catServicio");

        return (Long) q.getSingleResult();
    }


    @Override
    @SuppressWarnings(value = "unchecked")
    public List<CatServicio> getPagination(int inicio) throws I18NException {

        Query q = em.createQuery("Select catServicio from CatServicio as catServicio order by catServicio.id");
        q.setFirstResult(inicio);
        q.setMaxResults(RESULTADOS_PAGINACION);
        q.setHint("org.hibernate.readOnly", true);

        return q.getResultList();
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public CatServicio findByCodigo(Long codigo) throws I18NException {
        Query q = em.createQuery("Select catServicio from CatServicio as catServicio where catServicio.codServicio = :codigo");

        q.setParameter("codigo", codigo);
        q.setHint("org.hibernate.readOnly", true);

        List<CatServicio> catServicio = q.getResultList();

        if (catServicio.size() == 1) {
            return catServicio.get(0);
        } else {
            return null;
        }
    }
}
