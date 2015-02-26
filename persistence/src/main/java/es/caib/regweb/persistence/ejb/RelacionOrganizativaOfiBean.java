package es.caib.regweb.persistence.ejb;


import es.caib.regweb.model.Oficina;
import es.caib.regweb.model.Organismo;
import es.caib.regweb.model.RelacionOrganizativaOfi;
import es.caib.regweb.model.RelacionOrganizativaOfiPK;
import es.caib.regweb.utils.RegwebConstantes;
import org.apache.log4j.Logger;
import org.hibernate.Hibernate;
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
 * Date: 10/10/13
 */
@Stateless(name = "RelacionOrganizativaOfiEJB")
@SecurityDomain("seycon")
public class RelacionOrganizativaOfiBean extends BaseEjbJPA<RelacionOrganizativaOfi, RelacionOrganizativaOfiPK> implements RelacionOrganizativaOfiLocal{

    protected final Logger log = Logger.getLogger(getClass());

    @PersistenceContext
    private EntityManager em;

    @Override
    public RelacionOrganizativaOfi findById(RelacionOrganizativaOfiPK id) throws Exception {

        return em.find(RelacionOrganizativaOfi.class, id);
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public List<RelacionOrganizativaOfi> getAll() throws Exception {

        return  em.createQuery("Select relacionOrganizativaOfi from RelacionOrganizativaOfi as relacionOrganizativaOfi ").getResultList();
    }

    @Override
    public Long getTotal() throws Exception {

        Query q = em.createQuery("Select count(relacionOrganizativaOfi.id) from RelacionOrganizativaOfi as relacionOrganizativaOfi");

        return (Long) q.getSingleResult();
    }

    @Override
    public List<RelacionOrganizativaOfi> getPagination(int inicio) throws Exception {

        Query q = em.createQuery("Select relacionOrganizativaOfi from RelacionOrganizativaOfi as relacionOrganizativaOfi ");
        q.setFirstResult(inicio);
        q.setMaxResults(RESULTADOS_PAGINACION);

        return q.getResultList();
    }

    @Override
    public void deleteAll() throws Exception {

        em.createQuery("delete from RelacionOrganizativaOfi").executeUpdate();
        
    }

    @Override
    public void deleteByOficina(Long idOficina) throws Exception {

      Query q= em.createQuery("delete from RelacionOrganizativaOfi as roo where roo.oficina.id = :idOficina");
      q.setParameter("idOficina", idOficina);
      q.executeUpdate();
    }

    @Override
    public List<Oficina> getOficinasByOrganismo(Long idOrganismo) throws Exception {

        Query q = em.createQuery("Select distinct relacionOrganizativaOfi.oficina from RelacionOrganizativaOfi as relacionOrganizativaOfi " +
                "where relacionOrganizativaOfi.organismo.id = :idOrganismo and relacionOrganizativaOfi.estado.codigoEstadoEntidad = :vigente");

        q.setParameter("idOrganismo", idOrganismo);
        q.setParameter("vigente", RegwebConstantes.ESTADO_ENTIDAD_VIGENTE);

        List<Oficina> oficinas = q.getResultList();
        for(Oficina oficina:oficinas){
            Hibernate.initialize(oficina.getOrganizativasOfi());
        }

        return oficinas;
    }

    public List<Organismo> getOrganismosByOficina(Long idOficina) throws Exception{

        Query q = em.createQuery("Select distinct relacionOrganizativaOfi.organismo from RelacionOrganizativaOfi as relacionOrganizativaOfi " +
                "where relacionOrganizativaOfi.oficina.id = :idOficina and relacionOrganizativaOfi.estado.codigoEstadoEntidad = :vigente");

        q.setParameter("idOficina", idOficina);
        q.setParameter("vigente", RegwebConstantes.ESTADO_ENTIDAD_VIGENTE);

        return q.getResultList();
    }

    @Override
    public RelacionOrganizativaOfi getRelacionOrganizativa(Long idOficina, Long idOrganismo) throws Exception {

        Query q = em.createQuery("Select distinct relacionOrganizativaOfi.oficina from RelacionOrganizativaOfi as relacionOrganizativaOfi " +
                "where relacionOrganizativaOfi.organismo.id = :idOrganismo and relacionOrganizativaOfi.oficina.id = :idOficina " +
                "and relacionOrganizativaOfi.estado.codigoEstadoEntidad = :vigente");

        q.setParameter("idOrganismo", idOrganismo);
        q.setParameter("idOficina", idOficina);
        q.setParameter("vigente", RegwebConstantes.ESTADO_ENTIDAD_VIGENTE);

        List<RelacionOrganizativaOfi> relaciones =  q.getResultList();

        if(relaciones.size() > 0){
            return  relaciones.get(0);
        }else{
            return null;
        }
    }

    @Override
    public List<RelacionOrganizativaOfi> funcionalByEntidadEstado(Long idEntidad, String estado) throws Exception{
        Query q = em.createQuery("Select relacionOrganizativaOfi from RelacionOrganizativaOfi as relacionOrganizativaOfi where " +
                "relacionOrganizativaOfi.organismo.entidad.id =:idEntidad and relacionOrganizativaOfi.estado.codigoEstadoEntidad =:estado");

        q.setParameter("idEntidad",idEntidad);
        q.setParameter("estado",estado);

        return q.getResultList();
    }

}
