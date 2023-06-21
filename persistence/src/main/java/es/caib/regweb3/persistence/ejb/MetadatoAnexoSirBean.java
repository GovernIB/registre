package es.caib.regweb3.persistence.ejb;

import es.caib.regweb3.model.MetadatoAnexoSir;
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
@Stateless(name = "MetadatoAnexoSirEJB")
@RolesAllowed({"RWE_SUPERADMIN", "RWE_ADMIN", "RWE_USUARI"})
public class MetadatoAnexoSirBean extends BaseEjbJPA<MetadatoAnexoSir, Long> implements MetadatoAnexoSirLocal {

    protected final Logger log = LoggerFactory.getLogger(getClass());

    @PersistenceContext(unitName = "regweb3")
    private EntityManager em;


    @Override
    public MetadatoAnexoSir getReference(Long id) throws I18NException {

        return em.getReference(MetadatoAnexoSir.class, id);
    }

    @Override
    public MetadatoAnexoSir findById(Long id) throws I18NException {

        return em.find(MetadatoAnexoSir.class, id);
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public List<MetadatoAnexoSir> getAll() throws I18NException {

        return em.createQuery("Select metadatoAnexoSir from MetadatoAnexoSir as metadatoAnexoSir order by metadatoAnexoSir.id").getResultList();
    }

    @Override
    public Long getTotal() throws I18NException {

        Query q = em.createQuery("Select count(metadatoAnexoSir.id) from metadatoAnexoSir as metadatoAnexoSir");
        q.setHint("org.hibernate.readOnly", true);
        return (Long) q.getSingleResult();
    }


    @Override
    @SuppressWarnings(value = "unchecked")
    public List<MetadatoAnexoSir> getPagination(int inicio) throws I18NException {

        Query q = em.createQuery("Select metadatoAnexoSir from MetadatoAnexoSir as metadatoAnexoSir order by metadatoAnexoSir.id");
        q.setFirstResult(inicio);
        q.setMaxResults(RESULTADOS_PAGINACION);
        q.setHint("org.hibernate.readOnly", true);

        return q.getResultList();
    }
}
