package es.caib.regweb3.persistence.ejb;

import es.caib.regweb3.model.TipoDocumental;
import es.caib.regweb3.model.TraduccionTipoDocumental;
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

@Stateless(name = "TipoDocumentalEJB")
@SecurityDomain("seycon")
public class TipoDocumentalBean extends BaseEjbJPA<TipoDocumental, Long> implements TipoDocumentalLocal{

    protected final Logger log = Logger.getLogger(getClass());

    @PersistenceContext(unitName="regweb3")
    private EntityManager em;


    @Override
    public TipoDocumental getReference(Long id) throws Exception {

        return em.getReference(TipoDocumental.class, id);
    }

    @Override
    public TipoDocumental findById(Long id) throws Exception {

        return em.find(TipoDocumental.class, id);
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public List<TipoDocumental> getAll() throws Exception {

        return  em.createQuery("Select tipoDocumental from TipoDocumental as tipoDocumental order by tipoDocumental.id").getResultList();
    }

    @Override
    public Long getTotal() throws Exception {

        Query q = em.createQuery("Select count(tipoDocumental.id) from TipoDocumental as tipoDocumental");

        return (Long) q.getSingleResult();
    }

    @Override
    public Long getTotal(Long idEntidad) throws Exception {

        Query q = em.createQuery("Select count(tipoDocumental.id) from TipoDocumental as tipoDocumental where tipoDocumental.entidad.id = :idEntidad");
        q.setParameter("idEntidad",idEntidad);

        return (Long) q.getSingleResult();
    }


    @Override
    @SuppressWarnings(value = "unchecked")
    public List<TipoDocumental> getPagination(int inicio) throws Exception {

        Query q = em.createQuery("Select tipoDocumental from TipoDocumental as tipoDocumental order by tipoDocumental.id");
        q.setFirstResult(inicio);
        q.setMaxResults(RESULTADOS_PAGINACION);
        q.setHint("org.hibernate.readOnly", true);

        return q.getResultList();
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public List<TipoDocumental> getPagination(int inicio, Long idEntidad) throws Exception {

        Query q = em.createQuery("Select tipoDocumental from TipoDocumental as tipoDocumental where tipoDocumental.entidad.id = :idEntidad order by tipoDocumental.id");
        q.setParameter("idEntidad",idEntidad);
        q.setFirstResult(inicio);
        q.setMaxResults(RESULTADOS_PAGINACION);
        q.setHint("org.hibernate.readOnly", true);

        return q.getResultList();
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public TipoDocumental findByCodigoEntidad(String codigoNTI,Long idEntidad) throws Exception{

        Query q = em.createQuery("Select tipoDocumental from TipoDocumental as tipoDocumental where tipoDocumental.codigoNTI = :codigoNTI " +
                "and tipoDocumental.entidad.id = :idEntidad");

        q.setParameter("codigoNTI",codigoNTI);
        q.setParameter("idEntidad",idEntidad);
        q.setHint("org.hibernate.readOnly", true);

        List<TipoDocumental> tipoDocumental = q.getResultList();

        if(tipoDocumental.size() == 1){
            return tipoDocumental.get(0);
        }else{
            return  null;
        }
    }

    @Override
    public Boolean existeCodigoEdit(String codigoNTI, Long idTipoDocumental, Long idEntidad) throws Exception {

        Query q = em.createQuery("Select tipoDocumental.id from TipoDocumental as tipoDocumental where " +
                "tipoDocumental.id != :idTipoDocumental and tipoDocumental.codigoNTI = :codigoNTI and tipoDocumental.entidad.id = :idEntidad");

        q.setParameter("codigoNTI",codigoNTI);
        q.setParameter("idTipoDocumental",idTipoDocumental);
        q.setParameter("idEntidad",idEntidad);
        q.setHint("org.hibernate.readOnly", true);

        return q.getResultList().size() > 0;
    }


    @Override
    @SuppressWarnings(value = "unchecked")
    public List<TipoDocumental> getByEntidad(Long idEntidad) throws Exception {

        Query q = em.createQuery("Select tipoDocumental from TipoDocumental as tipoDocumental where tipoDocumental.entidad.id = :idEntidad");
        q.setParameter("idEntidad",idEntidad);
        q.setHint("org.hibernate.readOnly", true);

        return q.getResultList();
    }

    @Override
    public Integer eliminarByEntidad(Long idEntidad) throws Exception{

        List<?> tipos = em.createQuery("Select distinct(id) from TipoDocumental where entidad.id =:idEntidad").setParameter("idEntidad",idEntidad).getResultList();

        for (Object id : tipos) {
            remove(findById((Long) id));
        }

        return tipos.size();
    }

    @Override
    public TipoDocumental nuevoTraduccion(String codigo, Long idEntidad, String nombreCa, String nombreES) throws Exception {

        TipoDocumental tipoDocumental = new TipoDocumental(codigo, idEntidad);
        TraduccionTipoDocumental tra1 = new TraduccionTipoDocumental(nombreCa);
        TraduccionTipoDocumental tra2 = new TraduccionTipoDocumental(nombreES);
        tipoDocumental.setTraduccion("ca", tra1);
        tipoDocumental.setTraduccion("es", tra2);

        return  persist(tipoDocumental);

    }

}
