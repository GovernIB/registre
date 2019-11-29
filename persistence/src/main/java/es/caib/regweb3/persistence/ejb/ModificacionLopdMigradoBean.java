package es.caib.regweb3.persistence.ejb;

import es.caib.regweb3.model.ModificacionLopdMigrado;
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
 * @author jpernia
 * Date: 09/12/14
 */

@Stateless(name = "ModificacionLopdMigradoEJB")
@SecurityDomain("seycon")
public class ModificacionLopdMigradoBean extends BaseEjbJPA<ModificacionLopdMigrado, Long> implements ModificacionLopdMigradoLocal{

    protected final Logger log = Logger.getLogger(getClass());

    @PersistenceContext(unitName="regweb3")
    private EntityManager em;


    @Override
    public ModificacionLopdMigrado getReference(Long id) throws Exception {

        return em.getReference(ModificacionLopdMigrado.class, id);
    }

    @Override
    public ModificacionLopdMigrado findById(Long id) throws Exception {

        return em.find(ModificacionLopdMigrado.class, id);

    }


    @Override
    @SuppressWarnings(value = "unchecked")
    public List<ModificacionLopdMigrado> getAll() throws Exception {

        return  em.createQuery("Select modificacionLopdMigrado from ModificacionLopdMigrado as modificacionLopdMigrado order by modificacionLopdMigrado.id").getResultList();
    }

    @Override
    public Long getTotal() throws Exception {

        Query q = em.createQuery("Select count(modificacionLopdMigrado.id) from ModificacionLopdMigrado as modificacionLopdMigrado");

        return (Long) q.getSingleResult();
    }


    @Override
    @SuppressWarnings(value = "unchecked")
    public List<ModificacionLopdMigrado> getPagination(int inicio) throws Exception {

        Query q = em.createQuery("Select modificacionLopdMigrado from ModificacionLopdMigrado as modificacionLopdMigrado order by modificacionLopdMigrado.id");
        q.setFirstResult(inicio);
        q.setMaxResults(RESULTADOS_PAGINACION);
        q.setHint("org.hibernate.readOnly", true);

        return q.getResultList();
    }


    @Override
    @SuppressWarnings(value = "unchecked")
    public List<ModificacionLopdMigrado> getByRegistroMigrado(Long numRegistroMigrado) throws Exception {

        Query q = em.createQuery("Select modificacionLopdMigrado from ModificacionLopdMigrado as modificacionLopdMigrado where " +
                "modificacionLopdMigrado.registroMigrado.id = :numRegistroMigrado order by modificacionLopdMigrado.fecha asc");

        q.setParameter("numRegistroMigrado", numRegistroMigrado);
        q.setHint("org.hibernate.readOnly", true);

        return q.getResultList();

    }
}
