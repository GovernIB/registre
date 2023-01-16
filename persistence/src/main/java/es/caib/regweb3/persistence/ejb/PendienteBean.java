package es.caib.regweb3.persistence.ejb;

import es.caib.regweb3.model.Pendiente;
import org.fundaciobit.genapp.common.i18n.I18NException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;

/**
 * Created 14/10/14 9:55
 *
 * @author mgonzalez
 */
@Stateless(name = "PendienteEJB")
@RolesAllowed({"RWE_SUPERADMIN", "RWE_ADMIN", "RWE_USUARI", "RWE_WS_ENTRADA", "RWE_WS_SALIDA", "RWE_WS_CIUDADANO"})
public class PendienteBean extends BaseEjbJPA<Pendiente, Long> implements PendienteLocal {


    protected final Logger log = LoggerFactory.getLogger(getClass());

    @PersistenceContext(unitName = "regweb3")
    private EntityManager em;

    @EJB private OrganismoLocal organismoEjb;


    @Override
    public Pendiente getReference(Long id) throws I18NException {

        return em.getReference(Pendiente.class, id);
    }

    @Override
    public Pendiente findById(Long id) throws I18NException {

        return em.find(Pendiente.class, id);
    }


    public Pendiente findByIdOrganismo(Long idOrganismo) throws I18NException {

        Query q = em.createQuery("Select pendiente from Pendiente as pendiente where pendiente.idOrganismo=:idOrganismo and pendiente.procesado = false ");
        q.setParameter("idOrganismo", idOrganismo);
        return (Pendiente) q.getSingleResult();

    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public List<Pendiente> findByEstadoProcesado(String estado, Boolean procesado) throws I18NException {

        Query q = em.createQuery("Select pendiente from Pendiente as pendiente where pendiente.estado=:estado and pendiente.procesado=:procesado");
        q.setParameter("estado", estado);
        q.setParameter("procesado", procesado);
        q.setHint("org.hibernate.readOnly", true);

        return q.getResultList();

    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public List<Pendiente> findPendientesProcesar(Long idEntidad) throws I18NException {

        //TODO Habría que poner el campo identidad en la tabla RWE_PENDIENTE

        Query q = em.createQuery("Select pendiente from Pendiente as pendiente where pendiente.procesado = false");

        List<Pendiente> pendientes = q.getResultList();
        List<Pendiente> pendientesEntidad = new ArrayList<Pendiente>();
        for (Pendiente pendiente : pendientes) {
            Long entidadOrganismo = organismoEjb.getEntidad(pendiente.getIdOrganismo());
            if (idEntidad.equals(entidadOrganismo)) {
                pendientesEntidad.add(pendiente);
            }
        }
        return pendientesEntidad;

    }


    @Override
    @SuppressWarnings(value = "unchecked")
    public List<Pendiente> getAll() throws I18NException {

        return em.createQuery("Select pendiente from Pendiente as pendiente order by pendiente.id").getResultList();
    }

    @Override
    public Long getTotal() throws I18NException {

        Query q = em.createQuery("Select count(pendiente.id) from Pendiente as pendiente");
        q.setHint("org.hibernate.readOnly", true);

        return (Long) q.getSingleResult();
    }


    @Override
    @SuppressWarnings(value = "unchecked")
    public List<Pendiente> getPagination(int inicio) throws I18NException {

        Query q = em.createQuery("Select pendiente from Pendiente as pendiente order by pendiente.id");
        q.setFirstResult(inicio);
        q.setMaxResults(RESULTADOS_PAGINACION);
        q.setHint("org.hibernate.readOnly", true);

        return q.getResultList();
    }
}
