package es.caib.regweb3.persistence.ejb;

import es.caib.regweb3.model.ModeloRecibo;
import org.apache.log4j.Logger;
import org.jboss.ejb3.annotation.SecurityDomain;

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
@SecurityDomain("seycon")
public class ModeloReciboBean extends BaseEjbJPA<ModeloRecibo, Long> implements ModeloReciboLocal{

    protected final Logger log = Logger.getLogger(getClass());

    @PersistenceContext(unitName="regweb3")
    private EntityManager em;


    @Override
    public ModeloRecibo getReference(Long id) throws Exception {

        return em.getReference(ModeloRecibo.class, id);
    }

    @Override
    public ModeloRecibo findById(Long id) throws Exception {

        return em.find(ModeloRecibo.class, id);
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public List<ModeloRecibo> getAll() throws Exception {

        return  em.createQuery("Select modeloRecibo from ModeloRecibo as modeloRecibo order by modeloRecibo.id").getResultList();
    }

    @Override
    public Long getTotal() throws Exception {

        Query q = em.createQuery("Select count(modeloRecibo.id) from ModeloRecibo as modeloRecibo");

        return (Long) q.getSingleResult();
    }

    @Override
    public Long getTotal(Long idEntidad) throws Exception {

         Query q = em.createQuery("Select count(modeloRecibo.id) from ModeloRecibo as modeloRecibo where modeloRecibo.entidad.id = :idEntidad");
         q.setParameter("idEntidad",idEntidad);
        q.setHint("org.hibernate.readOnly", true);

         return (Long) q.getSingleResult();
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public List<ModeloRecibo> getByEntidad(Long idEntidad) throws Exception {

        Query q = em.createQuery("Select modeloRecibo.id, modeloRecibo.nombre from ModeloRecibo as modeloRecibo where modeloRecibo.entidad.id = :idEntidad");
        q.setParameter("idEntidad",idEntidad);
        q.setHint("org.hibernate.readOnly", true);

        List<ModeloRecibo> modelos =  new ArrayList<ModeloRecibo>();

        List<Object[]> result = q.getResultList();

        for (Object[] object : result){

            modelos.add(new ModeloRecibo((Long) object[0], (String) object[1]));
        }

        return modelos;
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public List<ModeloRecibo> getPagination(int inicio) throws Exception {

        Query q = em.createQuery("Select modeloRecibo from ModeloRecibo as modeloRecibo order by modeloRecibo.id");
        q.setFirstResult(inicio);
        q.setMaxResults(RESULTADOS_PAGINACION);
        q.setHint("org.hibernate.readOnly", true);

        return q.getResultList();
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public List<ModeloRecibo> getPagination(int inicio, Long idEntidad) throws Exception {

         Query q = em.createQuery("Select modeloRecibo from ModeloRecibo as modeloRecibo where modeloRecibo.entidad.id = :idEntidad order by modeloRecibo.id");
         q.setParameter("idEntidad",idEntidad);
         q.setFirstResult(inicio);
         q.setMaxResults(RESULTADOS_PAGINACION);
        q.setHint("org.hibernate.readOnly", true);

         return q.getResultList();
    }

    @Override
    public Integer eliminarByEntidad(Long idEntidad) throws Exception{

        List<?> modelos = em.createQuery("Select distinct(id) from ModeloRecibo where entidad.id =:idEntidad").setParameter("idEntidad",idEntidad).getResultList();

        for (Object id : modelos) {
            remove(findById((Long) id));
        }

        return modelos.size();

    }

}
