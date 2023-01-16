package es.caib.regweb3.persistence.ejb;

import es.caib.regweb3.model.ModeloRecibo;
import org.fundaciobit.genapp.common.i18n.I18NException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Fundació BIT.
 *
 * @author earrivi
 * Date: 16/01/14
 */

@Stateless(name = "ModeloReciboEJB")
@RolesAllowed({"RWE_SUPERADMIN", "RWE_ADMIN", "RWE_USUARI"})
public class ModeloReciboBean extends BaseEjbJPA<ModeloRecibo, Long> implements ModeloReciboLocal {

    protected final Logger log = LoggerFactory.getLogger(getClass());

    @PersistenceContext(unitName = "regweb3")
    private EntityManager em;


    @Override
    public ModeloRecibo getReference(Long id) throws I18NException {

        return em.getReference(ModeloRecibo.class, id);
    }

    @Override
    public ModeloRecibo findById(Long id) throws I18NException {

        return em.find(ModeloRecibo.class, id);
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public List<ModeloRecibo> getAll() throws I18NException {

        return em.createQuery("Select modeloRecibo from ModeloRecibo as modeloRecibo order by modeloRecibo.id").getResultList();
    }

    @Override
    public Long getTotal() throws I18NException {

        Query q = em.createQuery("Select count(modeloRecibo.id) from ModeloRecibo as modeloRecibo");

        return (Long) q.getSingleResult();
    }

    @Override
    public Long getTotal(Long idEntidad) throws I18NException {

        Query q = em.createQuery("Select count(modeloRecibo.id) from ModeloRecibo as modeloRecibo where modeloRecibo.entidad.id = :idEntidad");
        q.setParameter("idEntidad", idEntidad);
        q.setHint("org.hibernate.readOnly", true);

        return (Long) q.getSingleResult();
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public List<ModeloRecibo> getByEntidad(Long idEntidad) throws I18NException {

        Query q = em.createQuery("Select modeloRecibo.id, modeloRecibo.nombre from ModeloRecibo as modeloRecibo where modeloRecibo.entidad.id = :idEntidad");
        q.setParameter("idEntidad", idEntidad);
        q.setHint("org.hibernate.readOnly", true);

        List<ModeloRecibo> modelos = new ArrayList<ModeloRecibo>();

        List<Object[]> result = q.getResultList();

        for (Object[] object : result) {

            modelos.add(new ModeloRecibo((Long) object[0], (String) object[1]));
        }

        return modelos;
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public List<ModeloRecibo> getPagination(int inicio) throws I18NException {

        Query q = em.createQuery("Select modeloRecibo from ModeloRecibo as modeloRecibo order by modeloRecibo.id");
        q.setFirstResult(inicio);
        q.setMaxResults(RESULTADOS_PAGINACION);
        q.setHint("org.hibernate.readOnly", true);

        return q.getResultList();
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public List<ModeloRecibo> getPagination(int inicio, Long idEntidad) throws I18NException {

        Query q = em.createQuery("Select modeloRecibo from ModeloRecibo as modeloRecibo where modeloRecibo.entidad.id = :idEntidad order by modeloRecibo.id");
        q.setParameter("idEntidad", idEntidad);
        q.setFirstResult(inicio);
        q.setMaxResults(RESULTADOS_PAGINACION);
        q.setHint("org.hibernate.readOnly", true);

        return q.getResultList();
    }

    @Override
    public Integer eliminarByEntidad(Long idEntidad) throws I18NException {

        List<?> modelos = em.createQuery("Select distinct(id) from ModeloRecibo where entidad.id =:idEntidad").setParameter("idEntidad", idEntidad).getResultList();

        for (Object id : modelos) {
            remove(findById((Long) id));
        }

        return modelos.size();

    }

}
