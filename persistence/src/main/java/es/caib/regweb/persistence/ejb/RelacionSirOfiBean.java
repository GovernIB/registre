package es.caib.regweb.persistence.ejb;

import es.caib.regweb.model.RelacionSirOfi;
import es.caib.regweb.model.RelacionSirOfiPK;
import es.caib.regweb.utils.RegwebConstantes;

import org.apache.log4j.Logger;
import org.jboss.ejb3.annotation.SecurityDomain;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

/**
 * Created by Fundació BIT.
 *
 * @author earrivi
 * Date: 16/01/14
 */

@Stateless(name = "RelacionSirOfiEJB")
@SecurityDomain("seycon")
@RolesAllowed("RWE_ADMIN")
public class RelacionSirOfiBean extends BaseEjbJPA<RelacionSirOfi, RelacionSirOfiPK> implements RelacionSirOfiLocal{
  protected final Logger log = Logger.getLogger(getClass());

     @PersistenceContext
     private EntityManager em;

     @Override
     public RelacionSirOfi findById(RelacionSirOfiPK id) throws Exception {

         return em.find(RelacionSirOfi.class, id);
     }

     @Override
     @SuppressWarnings(value = "unchecked")
     public List<RelacionSirOfi> getAll() throws Exception {

         return  em.createQuery("Select relacionSirOfi from RelacionSirOfi as relacionSirOfi ").getResultList();
     }

     @Override
     public Long getTotal() throws Exception {

         Query q = em.createQuery("Select count(relacionSirOfi.id) from RelacionSirOfi as relacionSirOfi");

         return (Long) q.getSingleResult();
     }

     @Override
     public List<RelacionSirOfi> getPagination(int inicio) throws Exception {

         Query q = em.createQuery("Select relacionSirOfi from RelacionSirOfi as relacionSirOfi ");
         q.setFirstResult(inicio);
         q.setMaxResults(RESULTADOS_PAGINACION);

         return q.getResultList();
     }
     @Override
     public void deleteAll() throws Exception {

         em.createQuery("delete from RelacionSirOfi").executeUpdate();

     }

     @Override
     public void deleteByOficina(Long idOficina) throws Exception {

        Query q= em.createQuery("delete from RelacionSirOfi as rso where rso.oficina.id = :idOficina");
        q.setParameter("idOficina", idOficina);
        q.executeUpdate();
     }

    @Override
    public RelacionSirOfi getRelacionSir(Long idOficina, Long idOrganismo) throws Exception {

        Query q = em.createQuery("Select distinct relacionSirOfi.oficina from RelacionSirOfi as relacionSirOfi " +
                "where relacionSirOfi.organismo.id = :idOrganismo and relacionSirOfi.oficina.id = :idOficina " +
                "and relacionSirOfi.estado.codigoEstadoEntidad = :vigente");

        q.setParameter("idOrganismo", idOrganismo);
        q.setParameter("idOficina", idOficina);
        q.setParameter("vigente", RegwebConstantes.ESTADO_ENTIDAD_VIGENTE);

        List<RelacionSirOfi> relaciones =  q.getResultList();

        if(relaciones.size() > 0){
            return  relaciones.get(0);
        }else{
            return null;
        }
    }
}
