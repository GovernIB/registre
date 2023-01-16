package es.caib.regweb3.persistence.ejb;

import es.caib.regweb3.model.Descarga;
import es.caib.regweb3.utils.RegwebConstantes;
import org.fundaciobit.genapp.common.i18n.I18NException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
 * Date: 10/10/13
 */
@Stateless(name = "DescargaEJB")
@RolesAllowed({"RWE_SUPERADMIN", "RWE_ADMIN"})
public class DescargaBean extends BaseEjbJPA<Descarga, Long> implements DescargaLocal {

    protected final Logger log = LoggerFactory.getLogger(getClass());

    @PersistenceContext(unitName = "regweb3")
    private EntityManager em;


    @Override
    public Descarga getReference(Long id) throws I18NException {

        return em.getReference(Descarga.class, id);
    }

    @Override
    public Descarga findById(Long id) throws I18NException {

        return em.find(Descarga.class, id);
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public Descarga findByTipo(String tipo) throws I18NException {
        Query q = em.createQuery("select descarga from Descarga as descarga where descarga.tipo=:tipo order by descarga.id desc");
        q.setParameter("tipo", tipo);
        q.setHint("org.hibernate.readOnly", true);

        List<Descarga> descargas = q.getResultList();
        if (!descargas.isEmpty()) {
            return (Descarga) q.getResultList().get(0);
        } else {
            return null;
        }

    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public Descarga ultimaDescarga(String tipo, Long idEntidad) throws I18NException {

        Query q = em.createQuery("select descarga from Descarga as descarga where descarga.tipo = :tipo and descarga.entidad.id = :idEntidad order by descarga.id desc");

        q.setParameter("tipo", tipo);
        q.setParameter("idEntidad", idEntidad);
        q.setHint("org.hibernate.readOnly", true);

        List<Descarga> descargas = q.getResultList();
        if (!descargas.isEmpty()) {
            return (Descarga) q.getResultList().get(0);
        } else {
            return null;
        }
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public Descarga findByTipoEntidadInverse(String tipo, Long idEntidad) throws I18NException {

        Query q = em.createQuery("select descarga from Descarga as descarga where descarga.tipo=:tipo and descarga.entidad.id = :idEntidad order by descarga.id asc");

        q.setParameter("tipo", tipo);
        q.setParameter("idEntidad", idEntidad);
        q.setHint("org.hibernate.readOnly", true);

        List<Descarga> descargas = q.getResultList();
        if (!descargas.isEmpty()) {
            return (Descarga) q.getResultList().get(0);
        } else {
            return null;
        }
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public List<Descarga> getAll() throws I18NException {

        return em.createQuery("Select descarga from Descarga as descarga order by descarga.id").getResultList();
    }

    @Override
    public Long getTotal() throws I18NException {

        Query q = em.createQuery("Select count(descarga.id) from Descarga as descarga");
        q.setHint("org.hibernate.readOnly", true);

        return (Long) q.getSingleResult();
    }


    public Long getTotalByEntidad(Long idEntidad) throws I18NException {

        Query q = em.createQuery("Select count(descarga.id) from Descarga as descarga where descarga.entidad.id=:idEntidad").setParameter("idEntidad", idEntidad);
        q.setHint("org.hibernate.readOnly", true);

        return (Long) q.getSingleResult();
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public List<Descarga> getPagination(int inicio) throws I18NException {

        Query q = em.createQuery("Select descarga from Descarga as descarga order by descarga.id");
        q.setFirstResult(inicio);
        q.setMaxResults(RESULTADOS_PAGINACION);
        q.setHint("org.hibernate.readOnly", true);

        return q.getResultList();
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public List<Descarga> getPaginationByEntidad(int inicio, Long idEntidad) throws I18NException {

        Query q = em.createQuery("Select descarga from Descarga as descarga where descarga.entidad.id=:idEntidad order by descarga.id desc").setParameter("idEntidad", idEntidad);
        q.setFirstResult(inicio);
        q.setMaxResults(RESULTADOS_PAGINACION);
        q.setHint("org.hibernate.readOnly", true);

        return q.getResultList();
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public void deleteByTipo(String tipo) throws I18NException {

        Query query = em.createQuery("delete from Descarga as descarga where descarga.tipo=:tipo");
        query.setParameter("tipo", tipo);
        query.executeUpdate();

    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public List<Descarga> findByEntidad(Long idEntidad) throws I18NException {

        return em.createQuery("Select descarga from Descarga as descarga where descarga.entidad.id =:idEntidad order by descarga.id").setParameter("idEntidad", idEntidad).getResultList();

    }

    @Override
    public Integer eliminarByEntidad(Long idEntidad) throws I18NException {

        List<?> descargas = em.createQuery("Select distinct(id) from Descarga where entidad.id =:idEntidad").setParameter("idEntidad", idEntidad).getResultList();
        Integer total = descargas.size();


        if (descargas.size() > 0) {

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
    @SuppressWarnings(value = "unchecked")
    public List<Descarga> getPagination(int inicio, Long idEntidad) throws I18NException {

        Query q = em.createQuery("Select descarga from Descarga as descarga where descarga.entidad.id = :idEntidad order by descarga.id desc");
        q.setParameter("idEntidad", idEntidad);
        q.setFirstResult(inicio);
        q.setMaxResults(RESULTADOS_PAGINACION);
        q.setHint("org.hibernate.readOnly", true);

        return q.getResultList();
    }

    @Override
    public Long getTotalEntidad(Long idEntidad) throws I18NException {

        Query q = em.createQuery("Select count(descarga.id) from Descarga as descarga " +
                "where descarga.entidad.id = :idEntidad");

        q.setParameter("idEntidad", idEntidad);
        q.setHint("org.hibernate.readOnly", true);

        return (Long) q.getSingleResult();
    }

}
