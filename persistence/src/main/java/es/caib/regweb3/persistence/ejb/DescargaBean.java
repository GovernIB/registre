package es.caib.regweb3.persistence.ejb;

import es.caib.regweb3.model.Descarga;
import es.caib.regweb3.utils.RegwebConstantes;
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
 * Date: 10/10/13
 */
@Stateless(name = "DescargaEJB")
@SecurityDomain("seycon")
public class DescargaBean extends BaseEjbJPA<Descarga, Long> implements DescargaLocal{

    protected final Logger log = Logger.getLogger(getClass());

    @PersistenceContext
    private EntityManager em;


    @Override
    public Descarga getReference(Long id) throws Exception {

        return em.getReference(Descarga.class, id);
    }

    @Override
    public Descarga findById(Long id) throws Exception {

        return em.find(Descarga.class, id);
    }

     public Descarga findByTipo(String tipo) throws Exception {
        Query query = em.createQuery( "select descarga from Descarga as descarga where descarga.tipo=? order by descarga.id desc");
        query.setParameter(1, tipo);

        List<Descarga> descargas = query.getResultList();
        if(!descargas.isEmpty()){
          return (Descarga) query.getResultList().get(0);
        } else {
          return null;
        }

     }
    
    public Descarga findByTipoEntidad(String tipo, Long idEntidad) throws Exception {

        Query query = em.createQuery( "select descarga from Descarga as descarga where descarga.tipo=? and descarga.entidad.id =? order by descarga.id desc");
        query.setParameter(1, tipo);
        query.setParameter(2, idEntidad);

        List<Descarga> descargas = query.getResultList();
        if(!descargas.isEmpty()){
          return (Descarga) query.getResultList().get(0);
        } else {
          return null;
        } 
    }

    public Descarga findByTipoEntidadInverse(String tipo, Long idEntidad) throws Exception {

        Query query = em.createQuery( "select descarga from Descarga as descarga where descarga.tipo=? and descarga.entidad.id = ? order by descarga.id asc");
        query.setParameter(1, tipo);
        query.setParameter(2, idEntidad);
        List<Descarga> descargas = query.getResultList();
        if(!descargas.isEmpty()){
          return (Descarga) query.getResultList().get(0);
        } else {
          return null;
        }
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public List<Descarga> getAll() throws Exception {

        return  em.createQuery("Select descarga from Descarga as descarga order by descarga.id").getResultList();
    }

    @Override
    public Long getTotal() throws Exception {

        Query q = em.createQuery("Select count(descarga.id) from Descarga as descarga");

        return (Long) q.getSingleResult();
    }


    public Long getTotalByEntidad(Long idEntidad) throws Exception {

        Query q = em.createQuery("Select count(descarga.id) from Descarga as descarga where descarga.entidad.id=:idEntidad").setParameter("idEntidad", idEntidad);

        return (Long) q.getSingleResult();
    }

    @Override
    public List<Descarga> getPagination(int inicio) throws Exception {

        Query q = em.createQuery("Select descarga from Descarga as descarga order by descarga.id");
        q.setFirstResult(inicio);
        q.setMaxResults(RESULTADOS_PAGINACION);

        return q.getResultList();
    }


    public List<Descarga> getPaginationByEntidad(int inicio,Long idEntidad) throws Exception {

        Query q = em.createQuery("Select descarga from Descarga as descarga where descarga.entidad.id=:idEntidad order by descarga.id desc").setParameter("idEntidad", idEntidad);
        q.setFirstResult(inicio);
        q.setMaxResults(RESULTADOS_PAGINACION);

        return q.getResultList();
    }
    
    public void deleteByTipo(String tipo) throws Exception {

        Query query = em.createQuery("delete from Descarga as descarga where descarga.tipo=?");
        query.setParameter(1, tipo);
        query.executeUpdate();
         
    }

    @Override
    public List<Descarga> findByEntidad(Long idEntidad) throws Exception{

       return em.createQuery("Select descarga from Descarga as descarga where descarga.entidad.id =:idEntidad order by descarga.id").setParameter("idEntidad",idEntidad).getResultList();

    }

    @Override
    public Integer eliminarByEntidad(Long idEntidad) throws Exception{

        List<?> descargas = em.createQuery("Select distinct(id) from Descarga where entidad.id =:idEntidad").setParameter("idEntidad",idEntidad).getResultList();
        Integer total = descargas.size();


        if(descargas.size() > 0){

            // Si hay más de 1000 registros, dividimos las queries (ORA-01795).
            while (descargas.size() > RegwebConstantes.NUMBER_EXPRESSIONS_IN) {

                List<?> subList = descargas.subList(0, RegwebConstantes.NUMBER_EXPRESSIONS_IN);
                em.createQuery("delete from Descarga where id in (:descargas) ").setParameter("descargas", subList).executeUpdate();
                descargas.subList(0, RegwebConstantes.NUMBER_EXPRESSIONS_IN).clear();
            }

            em.createQuery("delete from Descarga where id in (:descargas) ").setParameter("descargas", descargas).executeUpdate();
        }

        return total;
    }

    @Override
    public List<Descarga> getPagination(int inicio, Long idEntidad) throws Exception {

        Query q = em.createQuery("Select descarga from Descarga as descarga where descarga.entidad.id = :idEntidad order by descarga.id desc");
        q.setParameter("idEntidad", idEntidad);
        q.setFirstResult(inicio);
        q.setMaxResults(RESULTADOS_PAGINACION);

        return q.getResultList();
    }

    @Override
    public Long getTotalEntidad(Long idEntidad) throws Exception {

        Query q = em.createQuery("Select count(descarga.id) from Descarga as descarga " +
                "where descarga.entidad.id = :idEntidad");

        q.setParameter("idEntidad", idEntidad);

        return (Long) q.getSingleResult();
    }
    
}
