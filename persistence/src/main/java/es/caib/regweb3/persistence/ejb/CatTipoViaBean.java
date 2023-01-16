package es.caib.regweb3.persistence.ejb;

import es.caib.regweb3.model.CatTipoVia;
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

@Stateless(name = "CatTipoViaEJB")
@RolesAllowed({"RWE_SUPERADMIN", "RWE_ADMIN", "RWE_USUARI"})
public class CatTipoViaBean extends BaseEjbJPA<CatTipoVia, Long> implements CatTipoViaLocal {

    protected final Logger log = LoggerFactory.getLogger(getClass());

    @PersistenceContext(unitName = "regweb3")
    private EntityManager em;


    @Override
    public CatTipoVia getReference(Long id) throws I18NException {

        return em.getReference(CatTipoVia.class, id);
    }

    @Override
    public CatTipoVia findById(Long id) throws I18NException {

        return em.find(CatTipoVia.class, id);
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public List<CatTipoVia> getAll() throws I18NException {

        return em.createQuery("Select catTipoVia from CatTipoVia as catTipoVia order by catTipoVia.codigoTipoVia").getResultList();
    }

    @Override
    public Long getTotal() throws I18NException {

        Query q = em.createQuery("Select count(catTipoVia.id) from CatTipoVia as catTipoVia");

        return (Long) q.getSingleResult();
    }


    @Override
    @SuppressWarnings(value = "unchecked")
    public List<CatTipoVia> getPagination(int inicio) throws I18NException {

        Query q = em.createQuery("Select catTipoVia from CatTipoVia as catTipoVia order by catTipoVia.id");
        q.setFirstResult(inicio);
        q.setMaxResults(RESULTADOS_PAGINACION);
        q.setHint("org.hibernate.readOnly", true);

        return q.getResultList();
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public CatTipoVia findByCodigo(Long codigo) throws I18NException {

        Query q = em.createQuery("Select catTipoVia from CatTipoVia as catTipoVia where catTipoVia.codigoTipoVia = :codigo");

        q.setParameter("codigo", codigo);
        q.setHint("org.hibernate.readOnly", true);

        List<CatTipoVia> catTipoVia = q.getResultList();
        if (catTipoVia.size() == 1) {
            return catTipoVia.get(0);
        } else {
            return null;
        }

    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public CatTipoVia findByDescripcion(String descripcion) throws I18NException {

        Query q = em.createQuery("Select catTipoVia from CatTipoVia as catTipoVia where catTipoVia.descripcionTipoVia = :descripcion");

        q.setParameter("descripcion", descripcion);
        q.setHint("org.hibernate.readOnly", true);

        List<CatTipoVia> catTipoVia = q.getResultList();
        if (catTipoVia.size() == 1) {
            return catTipoVia.get(0);
        } else {
            return null;
        }
    }
}
