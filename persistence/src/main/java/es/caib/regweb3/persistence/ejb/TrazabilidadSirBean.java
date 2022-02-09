package es.caib.regweb3.persistence.ejb;

import es.caib.regweb3.model.TrazabilidadSir;
import es.caib.regweb3.utils.RegwebConstantes;
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
 * Date: 16/01/14
 */

@Stateless(name = "TrazabilidadSirEJB")
@RolesAllowed({"RWE_SUPERADMIN", "RWE_ADMIN", "RWE_USUARI", "RWE_WS_ENTRADA", "RWE_WS_SALIDA", "RWE_WS_CIUDADANO"})
public class TrazabilidadSirBean extends BaseEjbJPA<TrazabilidadSir, Long> implements TrazabilidadSirLocal {

    protected final Logger log = LoggerFactory.getLogger(getClass());

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
    @SuppressWarnings(value = "unchecked")
    public List<TrazabilidadSir> getPagination(int inicio) throws Exception {

        Query q = em.createQuery("Select trazabilidadSir from TrazabilidadSir as trazabilidadSir order by trazabilidadSir.id");
        q.setFirstResult(inicio);
        q.setMaxResults(RESULTADOS_PAGINACION);
        q.setHint("org.hibernate.readOnly", true);

        return q.getResultList();
    }


    @Override
    @SuppressWarnings(value = "unchecked")
    public List<TrazabilidadSir> getByRegistroSir(Long idRegistroSir) throws Exception {

        Query q = em.createQuery("Select DISTINCT trazabilidadSir from TrazabilidadSir as trazabilidadSir " +
                "where trazabilidadSir.registroSir.id = :registroSir order by trazabilidadSir.fecha");

        q.setParameter("registroSir", idRegistroSir);
        q.setHint("org.hibernate.readOnly", true);

        return q.getResultList();
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public List<TrazabilidadSir> getByIdIntercambio(String idIntercambio, Long idEntidad) throws Exception {

        Query q = em.createQuery("Select DISTINCT trazabilidadSir from TrazabilidadSir as trazabilidadSir " +
                "where trazabilidadSir.registroSir.identificadorIntercambio = :idIntercambio and registroSir.entidad.id = :idEntidad order by trazabilidadSir.fecha");

        q.setParameter("idIntercambio", idIntercambio);
        q.setParameter("idEntidad", idEntidad);
        q.setHint("org.hibernate.readOnly", true);

        return q.getResultList();
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public TrazabilidadSir getByRegistroSirAceptado(Long idRegistroSir) throws Exception {

        Query q = em.createQuery("Select DISTINCT trazabilidadSir from TrazabilidadSir as trazabilidadSir " +
                "where trazabilidadSir.registroSir.id = :registroSir and trazabilidadSir.tipo = :aceptado");

        q.setParameter("registroSir", idRegistroSir);
        q.setParameter("aceptado", RegwebConstantes.TRAZABILIDAD_SIR_ACEPTADO);
        q.setHint("org.hibernate.readOnly", true);

        List<TrazabilidadSir> result = q.getResultList();

        if (result.size() >= 1) {
            return (TrazabilidadSir) result.get(0);
        }

        return null;
    }

    @Override
    public Integer eliminarByEntidad(Long idEntidad) {

        List<?> trazabilidadesSir = em.createQuery("Select id from TrazabilidadSir where registroSir.entidad.id=:idEntidad").setParameter("idEntidad", idEntidad).getResultList();
        Integer total = trazabilidadesSir.size();

        if (trazabilidadesSir.size() > 0) {

            // Si hay más de 1000 registros, dividimos las queries (ORA-01795).
            while (trazabilidadesSir.size() > RegwebConstantes.NUMBER_EXPRESSIONS_IN) {

                List<?> subList = trazabilidadesSir.subList(0, RegwebConstantes.NUMBER_EXPRESSIONS_IN);
                em.createQuery("delete from TrazabilidadSir where id in (:id)").setParameter("id", subList).executeUpdate();
                trazabilidadesSir.subList(0, RegwebConstantes.NUMBER_EXPRESSIONS_IN).clear();
            }

            em.createQuery("delete from TrazabilidadSir where id in (:id)").setParameter("id", trazabilidadesSir).executeUpdate();
        }

        return total;
    }
}
