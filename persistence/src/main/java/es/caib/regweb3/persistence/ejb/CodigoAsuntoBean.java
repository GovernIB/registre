package es.caib.regweb3.persistence.ejb;

import es.caib.regweb3.model.CodigoAsunto;
import org.apache.log4j.Logger;
import org.jboss.ejb3.annotation.SecurityDomain;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

/**
 * Created 26/03/14 12:36
 *
 * @author mgonzalez
 */
@Stateless(name = "CodigoAsuntoEJB")
@SecurityDomain("seycon")
public class CodigoAsuntoBean extends BaseEjbJPA<CodigoAsunto, Long> implements CodigoAsuntoLocal {

    protected final Logger log = Logger.getLogger(getClass());

    @PersistenceContext(unitName = "regweb3")
    private EntityManager em;

    @EJB  private TipoAsuntoLocal tipoAsuntoEjb;
    @EJB  private EntidadLocal entidadEjb;


    @Override
    public CodigoAsunto getReference(Long id) throws Exception {

        return em.getReference(CodigoAsunto.class, id);
    }

    @Override
    public CodigoAsunto findById(Long id) throws Exception {

        return em.find(CodigoAsunto.class, id);
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public List<CodigoAsunto> getAll() throws Exception {

        return em.createQuery("Select codigoAsunto from CodigoAsunto as codigoAsunto order by codigoAsunto.id").getResultList();
    }

    @Override
    public Long getTotal() throws Exception {

        Query q = em.createQuery("Select count(codigoAsunto.id) from CodigoAsunto as codigoAsunto");

        return (Long) q.getSingleResult();
    }

    @Override
    public Long getTotal(Long idTipoAsunto) throws Exception {

        Query q = em.createQuery("Select count(codigoAsunto.id) from CodigoAsunto as codigoAsunto where codigoAsunto.tipoAsunto.id = :idTipoAsunto");
        q.setParameter("idTipoAsunto", idTipoAsunto);
        q.setHint("org.hibernate.readOnly", true);

        return (Long) q.getSingleResult();
    }


    @Override
    @SuppressWarnings("unchecked")
    public List<CodigoAsunto> getPagination(int inicio) throws Exception {

        Query q = em.createQuery("Select codigoAsunto from CodigoAsunto as codigoAsunto order by codigoAsunto.id");
        q.setFirstResult(inicio);
        q.setMaxResults(RESULTADOS_PAGINACION);
        q.setHint("org.hibernate.readOnly", true);

        return q.getResultList();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<CodigoAsunto> getPagination(int inicio, Long idEntidad) throws Exception {

        Query q = em.createQuery("Select codigoAsunto from CodigoAsunto as codigoAsunto where codigoAsunto.entidad.id = :idEntidad order by codigoAsunto.id");

        q.setParameter("idEntidad", idEntidad);
        q.setFirstResult(inicio);
        q.setMaxResults(RESULTADOS_PAGINACION);
        q.setHint("org.hibernate.readOnly", true);

        return q.getResultList();
    }

    @Override
    public Long getTotalEntidad(Long idEntidad) throws Exception {

        Query q = em.createQuery("Select count(codigoAsunto.id) from CodigoAsunto as codigoAsunto " +
                "where codigoAsunto.entidad.id = :idEntidad");

        q.setParameter("idEntidad",idEntidad);
        q.setHint("org.hibernate.readOnly", true);

        return (Long) q.getSingleResult();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<CodigoAsunto> getByTipoAsunto(Long idTipoAsunto) throws Exception {

        Query q = em.createQuery("Select codigoAsunto from CodigoAsunto as codigoAsunto where codigoAsunto.tipoAsunto.id = :idTipoAsunto order by codigoAsunto.id");

        q.setParameter("idTipoAsunto", idTipoAsunto);
        q.setHint("org.hibernate.readOnly", true);

        return q.getResultList();
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public List<CodigoAsunto> getActivosEntidad(Long idEntidad) throws Exception {

        Query q =  em.createQuery("Select codigoAsunto from CodigoAsunto as codigoAsunto where codigoAsunto.activo = true " +
                "and codigoAsunto.entidad.id = :idEntidad order by codigoAsunto.id ");

        q.setParameter("idEntidad",idEntidad);
        q.setHint("org.hibernate.readOnly", true);
        return q.getResultList();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<CodigoAsunto> getActivosByTipoAsunto(Long idTipoAsunto) throws Exception {

        Query q = em.createQuery("Select codigoAsunto from CodigoAsunto as codigoAsunto where codigoAsunto.tipoAsunto.id = :idTipoAsunto " +
                "and codigoAsunto.activo= true order by codigoAsunto.id");

        q.setParameter("idTipoAsunto", idTipoAsunto);
        q.setHint("org.hibernate.readOnly", true);

        return q.getResultList();
    }

    @Override
    @SuppressWarnings("unchecked")
    public CodigoAsunto findByCodigo(String codigo) throws Exception {

        Query q = em.createQuery("Select codigoAsunto from CodigoAsunto as codigoAsunto where codigoAsunto.codigo = :codigo");

        q.setParameter("codigo", codigo);
        q.setHint("org.hibernate.readOnly", true);

        List<CodigoAsunto> codigoAsunto = q.getResultList();

        if (codigoAsunto.size() == 1) {
            return codigoAsunto.get(0);
        } else {
            return null;
        }
    }

    @Override
    public Boolean existeCodigoEdit(String codigo, Long idCodigoAsunto, Long idEntidad) throws Exception {

        Query q = em.createQuery("Select codigoAsunto.id from CodigoAsunto as codigoAsunto where " +
                "codigoAsunto.id != :idCodigoAsunto and codigoAsunto.codigo = :codigo and codigoAsunto.entidad.id = :idEntidad");

        q.setParameter("codigo", codigo);
        q.setParameter("idCodigoAsunto", idCodigoAsunto);
        q.setParameter("idEntidad", idEntidad);
        q.setHint("org.hibernate.readOnly", true);

        return q.getResultList().size() > 0;

    }

    @Override
    @SuppressWarnings("unchecked")
    public CodigoAsunto findByCodigoEntidad(String codigo, Long idEntidad) throws Exception {

        Query q = em.createQuery("Select codigoAsunto from CodigoAsunto as codigoAsunto where codigoAsunto.codigo = :codigo and codigoAsunto.entidad.id = :idEntidad");

        q.setParameter("codigo", codigo);
        q.setParameter("idEntidad", idEntidad);
        q.setHint("org.hibernate.readOnly", true);

        List<CodigoAsunto> codigoAsunto = q.getResultList();

        if (codigoAsunto.size() == 1) {
            return codigoAsunto.get(0);
        } else {
            return null;
        }
    }

    @Override
    public Integer eliminarByEntidad(Long idEntidad) throws Exception {

        List<?> codigos = em.createQuery("Select distinct(id) from CodigoAsunto where tipoAsunto.entidad.id =:idEntidad").setParameter("idEntidad", idEntidad).getResultList();

        for (Object id : codigos) {
            remove(findById((Long) id));
        }

        return codigos.size();
    }

}
