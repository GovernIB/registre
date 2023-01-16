package es.caib.regweb3.persistence.ejb;

import es.caib.regweb3.model.ModificacionLopdMigrado;
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
 * @author jpernia
 * Date: 09/12/14
 */

@Stateless(name = "ModificacionLopdMigradoEJB")
@RolesAllowed({"RWE_USUARI"})
public class ModificacionLopdMigradoBean extends BaseEjbJPA<ModificacionLopdMigrado, Long> implements ModificacionLopdMigradoLocal {

    protected final Logger log = LoggerFactory.getLogger(getClass());

    @PersistenceContext(unitName = "regweb3")
    private EntityManager em;


    @Override
    public ModificacionLopdMigrado getReference(Long id) throws I18NException {

        return em.getReference(ModificacionLopdMigrado.class, id);
    }

    @Override
    public ModificacionLopdMigrado findById(Long id) throws I18NException {

        return em.find(ModificacionLopdMigrado.class, id);

    }


    @Override
    @SuppressWarnings(value = "unchecked")
    public List<ModificacionLopdMigrado> getAll() throws I18NException {

        return em.createQuery("Select modificacionLopdMigrado from ModificacionLopdMigrado as modificacionLopdMigrado order by modificacionLopdMigrado.id").getResultList();
    }

    @Override
    public Long getTotal() throws I18NException {

        Query q = em.createQuery("Select count(modificacionLopdMigrado.id) from ModificacionLopdMigrado as modificacionLopdMigrado");

        return (Long) q.getSingleResult();
    }


    @Override
    @SuppressWarnings(value = "unchecked")
    public List<ModificacionLopdMigrado> getPagination(int inicio) throws I18NException {

        Query q = em.createQuery("Select modificacionLopdMigrado from ModificacionLopdMigrado as modificacionLopdMigrado order by modificacionLopdMigrado.id");
        q.setFirstResult(inicio);
        q.setMaxResults(RESULTADOS_PAGINACION);
        q.setHint("org.hibernate.readOnly", true);

        return q.getResultList();
    }


    @Override
    @SuppressWarnings(value = "unchecked")
    public List<ModificacionLopdMigrado> getByRegistroMigrado(Long numRegistroMigrado) throws I18NException {

        Query q = em.createQuery("Select modificacionLopdMigrado from ModificacionLopdMigrado as modificacionLopdMigrado where " +
                "modificacionLopdMigrado.registroMigrado.id = :numRegistroMigrado order by modificacionLopdMigrado.fecha asc");

        q.setParameter("numRegistroMigrado", numRegistroMigrado);
        q.setHint("org.hibernate.readOnly", true);

        return q.getResultList();

    }
}
