package es.caib.regweb3.persistence.ejb;

import es.caib.regweb3.model.MetadatoRegistroEntrada;
import es.caib.regweb3.model.RegistroEntrada;
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
@Stateless(name = "MetadatoRegistroEntradaEJB")
@RolesAllowed({"RWE_SUPERADMIN", "RWE_ADMIN", "RWE_USUARI"})
public class MetadatoRegistroEntradaBean extends BaseEjbJPA<MetadatoRegistroEntrada, Long> implements MetadatoRegistroEntradaLocal {

    protected final Logger log = LoggerFactory.getLogger(getClass());

    @PersistenceContext(unitName = "regweb3")
    private EntityManager em;


    @Override
    public MetadatoRegistroEntrada getReference(Long id) throws I18NException {

        return em.getReference(MetadatoRegistroEntrada.class, id);
    }

    @Override
    public MetadatoRegistroEntrada findById(Long id) throws I18NException {

        return em.find(MetadatoRegistroEntrada.class, id);
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public List<MetadatoRegistroEntrada> getAll() throws I18NException {

        return em.createQuery("Select metadatoRegistroEntrada from MetadatoRegistroEntrada as metadatoRegistroEntrada order by metadatoRegistroEntrada.id").getResultList();
    }

    @Override
    public Long getTotal() throws I18NException {

        Query q = em.createQuery("Select count(metadatoRegistroEntrada.id) from MetadatoRegistroEntrada as metadatoRegistroEntrada");
        q.setHint("org.hibernate.readOnly", true);
        return (Long) q.getSingleResult();
    }


    @Override
    @SuppressWarnings(value = "unchecked")
    public List<MetadatoRegistroEntrada> getPagination(int inicio) throws I18NException {

        Query q = em.createQuery("Select metadatoRegistroEntrada from MetadatoRegistroEntrada as metadatoRegistroEntrada order by metadatoRegistroEntrada.id");
        q.setFirstResult(inicio);
        q.setMaxResults(RESULTADOS_PAGINACION);
        q.setHint("org.hibernate.readOnly", true);

        return q.getResultList();
    }

    @Override
    public MetadatoRegistroEntrada guardarMetadatoRegistroEntrada(MetadatoRegistroEntrada metadatoRegistroEntrada, RegistroEntrada registroEntrada) throws I18NException {
        metadatoRegistroEntrada.setRegistroEntrada(registroEntrada);
        return persist(metadatoRegistroEntrada);
    }
}
