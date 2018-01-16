package es.caib.regweb3.persistence.ejb;

import es.caib.regweb3.model.TipoAsunto;
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
 * Date: 16/01/14
 */

@Stateless(name = "TipoAsuntoEJB")
@SecurityDomain("seycon")
public class TipoAsuntoBean extends BaseEjbJPA<TipoAsunto, Long> implements TipoAsuntoLocal{

    protected final Logger log = Logger.getLogger(getClass());

    @PersistenceContext(unitName="regweb3")
    private EntityManager em;


    @Override
    public TipoAsunto getReference(Long id) throws Exception {

        return em.getReference(TipoAsunto.class, id);
    }

    @Override
    public TipoAsunto findById(Long id) throws Exception {

        return em.find(TipoAsunto.class, id);
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public List<TipoAsunto> getAll() throws Exception {

        return  em.createQuery("Select tipoAsunto from TipoAsunto as tipoAsunto  order by tipoAsunto.id ").getResultList();
    }

    @Override
    public Long getTotal() throws Exception {

        Query q = em.createQuery("Select count(tipoAsunto.id) from TipoAsunto as tipoAsunto");

        return (Long) q.getSingleResult();
    }

    @Override
    public Long getTotal(Long idEntidad) throws Exception {

        Query q = em.createQuery("Select count(tipoAsunto.id) from TipoAsunto as tipoAsunto where tipoAsunto.entidad.id = :idEntidad");
        q.setParameter("idEntidad",idEntidad);

        return (Long) q.getSingleResult();
    }


    @Override
    @SuppressWarnings(value = "unchecked")
    public List<TipoAsunto> getPagination(int inicio) throws Exception {

        Query q = em.createQuery("Select tipoAsunto from TipoAsunto as tipoAsunto order by tipoAsunto.id");
        q.setFirstResult(inicio);
        q.setMaxResults(RESULTADOS_PAGINACION);

        return q.getResultList();
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public List<TipoAsunto> getPagination(int inicio, Long idEntidad) throws Exception {

        Query q = em.createQuery("Select tipoAsunto from TipoAsunto as tipoAsunto where tipoAsunto.entidad.id = :idEntidad order by tipoAsunto.activo desc, tipoAsunto.codigo");
        q.setParameter("idEntidad",idEntidad);
        q.setFirstResult(inicio);
        q.setMaxResults(RESULTADOS_PAGINACION);

        return q.getResultList();
    }


    @Override
    @SuppressWarnings(value = "unchecked")
    public List<TipoAsunto> getAll(Long idEntidad) throws Exception {

        Query q = em.createQuery("Select tipoAsunto from TipoAsunto as tipoAsunto where tipoAsunto.entidad.id = :idEntidad order by tipoAsunto.id");
        q.setParameter("idEntidad",idEntidad);

        return q.getResultList();
    }
    
    

    @Override
    @SuppressWarnings(value = "unchecked")
    public List<TipoAsunto> getActivosEntidad(Long idEntidad) throws Exception {

        Query q =  em.createQuery("Select tipoAsunto from TipoAsunto as tipoAsunto where tipoAsunto.activo= true " +
                "and tipoAsunto.entidad.id = :idEntidad order by tipoAsunto.id ");

        q.setParameter("idEntidad",idEntidad);
        return q.getResultList();


    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public TipoAsunto findByCodigo(String codigo) throws Exception {

        Query q = em.createQuery("Select tipoAsunto from TipoAsunto as tipoAsunto where tipoAsunto.codigo = :codigo");

        q.setParameter("codigo",codigo);

        List<TipoAsunto> tipoAsunto = q.getResultList();

        if(tipoAsunto.size() == 1){
            return tipoAsunto.get(0);
        }else{
            return  null;
        }
    }

    @Override
    public Boolean existeCodigoEdit(String codigo, Long idTipoAsunto, Long idEntidad) throws Exception {

        Query q = em.createQuery("Select tipoAsunto.id from TipoAsunto as tipoAsunto where " +
                "tipoAsunto.id != :idTipoAsunto and tipoAsunto.codigo = :codigo and tipoAsunto.entidad.id = :idEntidad");

        q.setParameter("codigo",codigo);
        q.setParameter("idTipoAsunto",idTipoAsunto);
        q.setParameter("idEntidad",idEntidad);

        return q.getResultList().size() > 0;

    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public TipoAsunto findByCodigoEntidad(String codigo,Long idEntidad) throws Exception{

        Query q = em.createQuery("Select tipoAsunto from TipoAsunto as tipoAsunto where tipoAsunto.codigo = :codigo " +
                "and tipoAsunto.entidad.id = :idEntidad");

        q.setParameter("codigo",codigo);
        q.setParameter("idEntidad",idEntidad);

        List<TipoAsunto> tipoAsunto = q.getResultList();

        if(tipoAsunto.size() == 1){
            return tipoAsunto.get(0);
        }else{
            return  null;
        }
    }

    @Override
    public Long getTotalEntidad(Long idEntidad) throws Exception {

        Query q = em.createQuery("Select count(tipoAsunto.id) from TipoAsunto as tipoAsunto " +
                "where tipoAsunto.entidad.id = :idEntidad");

        q.setParameter("idEntidad",idEntidad);

        return (Long) q.getSingleResult();
    }

    @Override
    public Integer eliminarByEntidad(Long idEntidad) throws Exception{

        List<?> tipos = em.createQuery("Select distinct(id) from TipoAsunto where entidad.id =:idEntidad").setParameter("idEntidad",idEntidad).getResultList();

        for (Object id : tipos) {
            remove(findById((Long) id));
        }

        return tipos.size();

    }

}
