package es.caib.regweb3.persistence.ejb;

import es.caib.regweb3.model.Entidad;
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
 * @author mgonzalez
 * Date: 07/07/2021
 */

@Stateless(name = "MultiEntidadEJB")
@SecurityDomain("seycon")
public class MultiEntidadBean extends BaseEjbJPA<Entidad, Long>
   implements MultiEntidadLocal{

   protected final Logger log = Logger.getLogger(getClass());

   @PersistenceContext(unitName="regweb3")
   private EntityManager em;


   @Override
   public boolean isMultiEntidad() throws Exception{
      return em.createQuery("Select entidad from Entidad as entidad where entidad.sir = true order by entidad.id").getResultList().size()>1;
   }


   @Override
   public Entidad getReference(Long id) throws Exception {

      return em.getReference(Entidad.class, id);
   }

   @Override
   public Entidad findById(Long id) throws Exception {

      return em.find(Entidad.class, id);
   }


   @Override
   @SuppressWarnings(value = "unchecked")
   public List<Entidad> getAll() throws Exception {

      return  em.createQuery("Select entidad from Entidad as entidad order by entidad.id").getResultList();
   }

   @Override
   public Long getTotal() throws Exception {

      Query q = em.createQuery("Select count(entidad.id) from Entidad as entidad");

      return (Long) q.getSingleResult();
   }


   @Override
   @SuppressWarnings(value = "unchecked")
   public List<Entidad> getPagination(int inicio) throws Exception {

      Query q = em.createQuery("Select entidad from Entidad as entidad order by entidad.id");
      q.setFirstResult(inicio);
      q.setMaxResults(RESULTADOS_PAGINACION);

      return q.getResultList();
   }
}


