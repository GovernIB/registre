package es.caib.regweb3.persistence.ejb;

import es.caib.regweb3.model.MetadatoAnexo;
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
@Stateless(name = "MetadatoAnexoEJB")
@RolesAllowed({"RWE_SUPERADMIN", "RWE_ADMIN", "RWE_USUARI"})
public class MetadatoAnexoBean extends BaseEjbJPA<MetadatoAnexo, Long> implements MetadatoAnexoLocal {

    protected final Logger log = LoggerFactory.getLogger(getClass());

    @PersistenceContext(unitName = "regweb3")
    private EntityManager em;


    @Override
    public MetadatoAnexo getReference(Long id) throws I18NException {

        return em.getReference(MetadatoAnexo.class, id);
    }

    @Override
    public MetadatoAnexo findById(Long id) throws I18NException {

        return em.find(MetadatoAnexo.class, id);
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public List<MetadatoAnexo> getAll() throws I18NException {

        return em.createQuery("Select metadatoAnexo from MetadatoAnexo as metadatoAnexo order by metadatoAnexo.id").getResultList();
    }

    @Override
    public Long getTotal() throws I18NException {

        Query q = em.createQuery("Select count(metadatoAnexo.id) from MetadatoAnexo as metadatoAnexo");
        q.setHint("org.hibernate.readOnly", true);
        return (Long) q.getSingleResult();
    }


    @Override
    @SuppressWarnings(value = "unchecked")
    public List<MetadatoAnexo> getPagination(int inicio) throws I18NException {

        Query q = em.createQuery("Select metadatoAnexo from MetadatoAnexo as metadatoAnexo order by metadatoAnexo.id");
        q.setFirstResult(inicio);
        q.setMaxResults(RESULTADOS_PAGINACION);
        q.setHint("org.hibernate.readOnly", true);

        return q.getResultList();
    }
}
