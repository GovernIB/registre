package es.caib.regweb3.persistence.ejb;

import es.caib.regweb3.model.MetadatoRegistroSir;
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
 * @author mgonzalez
 * @version 1
 * 18/11/2022
 */
@Stateless(name = "MetadatoRegistroSirEJB")
@RolesAllowed({"RWE_SUPERADMIN", "RWE_ADMIN", "RWE_USUARI"})
public class MetadatoRegistroSirBean extends BaseEjbJPA<MetadatoRegistroSir, Long> implements MetadatoRegistroSirLocal {

    protected final Logger log = LoggerFactory.getLogger(getClass());

    @PersistenceContext(unitName = "regweb3")
    private EntityManager em;


    @Override
    public MetadatoRegistroSir getReference(Long id) throws I18NException {

        return em.getReference(MetadatoRegistroSir.class, id);
    }

    @Override
    public MetadatoRegistroSir findById(Long id) throws I18NException {

        return em.find(MetadatoRegistroSir.class, id);
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public List<MetadatoRegistroSir> getAll() throws I18NException {

        return em.createQuery("Select metadatoRegistroSir from MetadatoRegistroSir as metadatoRegistroSir order by metadatoRegistroSir.id").getResultList();
    }

    @Override
    public Long getTotal() throws I18NException {

        Query q = em.createQuery("Select count(metadatoRegistroSir.id) from MetadatoRegistroSir as metadatoRegistroSir");
        q.setHint("org.hibernate.readOnly", true);
        return (Long) q.getSingleResult();
    }


    @Override
    @SuppressWarnings(value = "unchecked")
    public List<MetadatoRegistroSir> getPagination(int inicio) throws I18NException {

        Query q = em.createQuery("Select metadatoRegistroSir from MetadatoRegistroSir as metadatoRegistroSir order by metadatoRegistroSir.id");
        q.setFirstResult(inicio);
        q.setMaxResults(RESULTADOS_PAGINACION);
        q.setHint("org.hibernate.readOnly", true);

        return q.getResultList();
    }
}
