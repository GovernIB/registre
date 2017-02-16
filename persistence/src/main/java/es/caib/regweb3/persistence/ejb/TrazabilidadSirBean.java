package es.caib.regweb3.persistence.ejb;

import es.caib.regweb3.sir.core.model.TrazabilidadSir;
import org.apache.log4j.Logger;
import org.jboss.ejb3.annotation.SecurityDomain;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

/**
 * Created by Fundació BIT.
 *
 * @author earrivi
 *         Date: 05/05/16
 */
@Stateless(name = "TrazabilidadSirEJB")
@SecurityDomain("seycon")
public class TrazabilidadSirBean extends BaseEjbJPA<TrazabilidadSir, Long> implements TrazabilidadSirLocal {


    protected final Logger log = Logger.getLogger(getClass());

    @PersistenceContext(unitName = "regweb3")
    private EntityManager em;


    @Override
    public TrazabilidadSir getReference(Long id) throws Exception {

        return em.getReference(TrazabilidadSir.class, id);
    }

    @Override
    public TrazabilidadSir findById(Long id) throws Exception {

        return em.find(TrazabilidadSir.class, id);
    }


    @Override
    @SuppressWarnings(value = "unchecked")
    public List<TrazabilidadSir> getAll() throws Exception {

        return em.createQuery("Select trazabilidadSir from TrazabilidadSir as trazabilidadSir order by trazabilidadSir.id").getResultList();
    }

    @Override
    public Long getTotal() throws Exception {

        Query q = em.createQuery("Select count(trazabilidadSir.id) from TrazabilidadSir as trazabilidadSir");

        return (Long) q.getSingleResult();
    }


    @Override
    public List<TrazabilidadSir> getPagination(int inicio) throws Exception {

        Query q = em.createQuery("Select trazabilidadSir from TrazabilidadSir as trazabilidadSir order by trazabilidadSir.id");
        q.setFirstResult(inicio);
        q.setMaxResults(RESULTADOS_PAGINACION);

        return q.getResultList();
    }


    @Override
    public Integer eliminarByEntidad(Long idEntidad) throws Exception {

        List<?> propiedades = em.createQuery("Select distinct(id) from TrazabilidadSir  where asientoRegistralSir.entidad.id =:idEntidad").setParameter("idEntidad", idEntidad).getResultList();

        for (Object id : propiedades) {
            remove(findById((Long) id));
        }

        return propiedades.size();
    }




}
