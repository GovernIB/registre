package es.caib.regweb3.persistence.ejb;

import es.caib.regweb3.model.PropiedadGlobal;
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
@Stateless(name = "PropiedadGlobalEJB")
@SecurityDomain("seycon")
public class PropiedadGlobalBean extends BaseEjbJPA<PropiedadGlobal, Long> implements PropiedadGlobalLocal {


    protected final Logger log = Logger.getLogger(getClass());

    @PersistenceContext(unitName = "regweb3")
    private EntityManager em;


    @Override
    public PropiedadGlobal findById(Long id) throws Exception {

        return em.find(PropiedadGlobal.class, id);
    }


    @Override
    @SuppressWarnings(value = "unchecked")
    public List<PropiedadGlobal> getAll() throws Exception {

        return em.createQuery("Select propiedadGlobal from PropiedadGlobal as propiedadGlobal order by propiedadGlobal.id").getResultList();
    }

    @Override
    public Long getTotal() throws Exception {

        Query q = em.createQuery("Select count(propiedadGlobal.id) from PropiedadGlobal as propiedadGlobal");

        return (Long) q.getSingleResult();
    }


    @Override
    public List<PropiedadGlobal> getPagination(int inicio) throws Exception {

        Query q = em.createQuery("Select propiedadGlobal from PropiedadGlobal as propiedadGlobal order by propiedadGlobal.id");
        q.setFirstResult(inicio);
        q.setMaxResults(RESULTADOS_PAGINACION);

        return q.getResultList();
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public List<PropiedadGlobal> findByEntidad(Long idEntidad) throws Exception {

        Query q = em.createQuery("Select propiedadGlobal from PropiedadGlobal as propiedadGlobal where propiedadGlobal.entidad = :idEntidad order by propiedadGlobal.id");
        q.setParameter("idEntidad", idEntidad);

        return q.getResultList();

    }

    @Override
    public PropiedadGlobal findByClaveEntidad(String clave, Long idEntidad, Long idPropiedadGlobal) throws Exception {

        String entidadQuery = "";
        String propiedadQuery = "";

        if (idEntidad != null) {
            entidadQuery = "and propiedadGlobal.entidad = :idEntidad ";
        } else {
            entidadQuery = "and propiedadGlobal.entidad is null ";
        }

        if (idPropiedadGlobal != null) {
            propiedadQuery = " and propiedadGlobal.id != :idPropiedadGlobal ";
        }


        Query q = em.createQuery("Select propiedadGlobal from PropiedadGlobal as propiedadGlobal where propiedadGlobal.clave = :clave " +
                entidadQuery + propiedadQuery + " order by propiedadGlobal.id");

        q.setParameter("clave", clave);
        if (idEntidad != null) {
            q.setParameter("idEntidad", idEntidad);
        }
        if (idPropiedadGlobal != null) {
            q.setParameter("idPropiedadGlobal", idPropiedadGlobal);
        }

        List<PropiedadGlobal> p = q.getResultList();

        return p.size() == 1 ? p.get(0) : null;

    }

    @Override
    public Long getTotalByEntidad(Long idEntidad) throws Exception {

        Query q = em.createQuery("Select count(propiedadGlobal.id) from PropiedadGlobal as propiedadGlobal where propiedadGlobal.entidad = :idEntidad");
        q.setParameter("idEntidad", idEntidad);

        return (Long) q.getSingleResult();
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public List<PropiedadGlobal> getPaginationByEntidad(int inicio, Long idEntidad) throws Exception {

        Query q = em.createQuery("Select propiedadGlobal from PropiedadGlobal as propiedadGlobal where propiedadGlobal.entidad = :idEntidad order by propiedadGlobal.id");
        q.setParameter("idEntidad", idEntidad);
        q.setFirstResult(inicio);
        q.setMaxResults(RESULTADOS_PAGINACION);

        return q.getResultList();
    }

    @Override
    public Long getTotalREGWEB3() throws Exception {

        Query q = em.createQuery("Select count(propiedadGlobal.id) from PropiedadGlobal as propiedadGlobal where propiedadGlobal.entidad is null");

        return (Long) q.getSingleResult();
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public List<PropiedadGlobal> getPaginationREGWEB3(int inicio) throws Exception {

        Query q = em.createQuery("Select propiedadGlobal from PropiedadGlobal as propiedadGlobal where propiedadGlobal.entidad is null order by propiedadGlobal.id");
        q.setFirstResult(inicio);
        q.setMaxResults(RESULTADOS_PAGINACION);

        return q.getResultList();
    }

    @Override
    public Integer eliminarByEntidad(Long idEntidad) throws Exception {

        List<?> propiedades = em.createQuery("Select distinct(id) from PropiedadGlobal where entidad =:idEntidad").setParameter("idEntidad", idEntidad).getResultList();

        for (Object id : propiedades) {
            remove(findById((Long) id));
        }

        return propiedades.size();
    }
}
