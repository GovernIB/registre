package es.caib.regweb3.persistence.ejb;

import es.caib.regweb3.model.ModeloOficioRemision;
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
 * @author jpernia
 * Date: 2/09/14
 */

@Stateless(name = "ModeloOficioRemisionEJB")
@RolesAllowed({"RWE_SUPERADMIN", "RWE_ADMIN", "RWE_USUARI", "RWE_WS_ENTRADA", "RWE_WS_SALIDA"})
public class ModeloOficioRemisionBean extends BaseEjbJPA<ModeloOficioRemision, Long> implements ModeloOficioRemisionLocal {

    protected final Logger log = LoggerFactory.getLogger(getClass());

    @PersistenceContext(unitName = "regweb3")
    private EntityManager em;


    @Override
    public ModeloOficioRemision getReference(Long id) throws I18NException {

        return em.getReference(ModeloOficioRemision.class, id);
    }

    @Override
    public ModeloOficioRemision findById(Long id) throws I18NException {

        return em.find(ModeloOficioRemision.class, id);
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public List<ModeloOficioRemision> getAll() throws I18NException {

        return em.createQuery("Select modeloOficioRemision from ModeloOficioRemision as modeloOficioRemision order by modeloOficioRemision.id").getResultList();
    }

    @Override
    public Long getTotal() throws I18NException {

        Query q = em.createQuery("Select count(modeloOficioRemision.id) from ModeloOficioRemision as modeloOficioRemision");

        return (Long) q.getSingleResult();
    }

    @Override
    public Long getTotal(Long idEntidad) throws I18NException {

        Query q = em.createQuery("Select count(modeloOficioRemision.id) from ModeloOficioRemision as modeloOficioRemision where modeloOficioRemision.entidad.id = :idEntidad");
        q.setParameter("idEntidad", idEntidad);

        return (Long) q.getSingleResult();
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public List<ModeloOficioRemision> getByEntidad(Long idEntidad) throws I18NException {

        Query q = em.createQuery("Select modelo.id, modelo.nombre from ModeloOficioRemision as modelo where modelo.entidad.id = :idEntidad");
        q.setParameter("idEntidad", idEntidad);
        q.setHint("org.hibernate.readOnly", true);

        List<ModeloOficioRemision> modelos = new ArrayList<ModeloOficioRemision>();

        List<Object[]> result = q.getResultList();

        for (Object[] object : result) {
            modelos.add(new ModeloOficioRemision((Long) object[0], (String) object[1]));
        }

        return modelos;
    }


    @Override
    @SuppressWarnings(value = "unchecked")
    public List<ModeloOficioRemision> getPagination(int inicio) throws I18NException {

        Query q = em.createQuery("Select modeloOficioRemision from ModeloOficioRemision as modeloOficioRemision order by modeloOficioRemision.id");
        q.setFirstResult(inicio);
        q.setMaxResults(RESULTADOS_PAGINACION);
        q.setHint("org.hibernate.readOnly", true);

        return q.getResultList();
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public List<ModeloOficioRemision> getPagination(int inicio, Long idEntidad) throws I18NException {

        Query q = em.createQuery("Select modeloOficioRemision from ModeloOficioRemision as modeloOficioRemision where modeloOficioRemision.entidad.id = :idEntidad order by modeloOficioRemision.id");
        q.setParameter("idEntidad", idEntidad);
        q.setFirstResult(inicio);
        q.setMaxResults(RESULTADOS_PAGINACION);
        q.setHint("org.hibernate.readOnly", true);

        return q.getResultList();
    }

    @Override
    public Integer eliminarByEntidad(Long idEntidad) throws I18NException {

        List<?> modelos = em.createQuery("Select distinct(id) from ModeloOficioRemision where entidad.id =:idEntidad").setParameter("idEntidad", idEntidad).getResultList();

        for (Object id : modelos) {
            remove(findById((Long) id));
        }

        return modelos.size();

    }

}
