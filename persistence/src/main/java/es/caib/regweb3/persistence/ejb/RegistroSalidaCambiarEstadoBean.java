package es.caib.regweb3.persistence.ejb;

import es.caib.regweb3.model.RegistroSalida;
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
 * @author anadal
 * Date: 16/01/14
 */

@Stateless(name = "RegistroSalidaCambiarEstadoEJB")
@RolesAllowed({"RWE_SUPERADMIN", "RWE_ADMIN", "RWE_USUARI", "RWE_WS_ENTRADA", "RWE_WS_SALIDA"})
public class RegistroSalidaCambiarEstadoBean extends BaseEjbJPA<RegistroSalida, Long> implements RegistroSalidaCambiarEstadoLocal {

    protected final Logger log = LoggerFactory.getLogger(getClass());

    @PersistenceContext(unitName = "regweb3")
    private EntityManager em;


    @Override
    public RegistroSalida getReference(Long id) throws I18NException {

        return em.getReference(RegistroSalida.class, id);
    }

    @Override
    public RegistroSalida findById(Long id) throws I18NException {

        return em.find(RegistroSalida.class, id);
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public List<RegistroSalida> getAll() throws I18NException {

        return em.createQuery("Select registroSalida from RegistroSalida as registroSalida order by registroSalida.id").getResultList();
    }

    @Override
    public Long getTotal() throws I18NException {

        Query q = em.createQuery("Select count(registroSalida.id) from RegistroSalida as registroSalida");

        return (Long) q.getSingleResult();
    }


    @Override
    @SuppressWarnings(value = "unchecked")
    public List<RegistroSalida> getPagination(int inicio) throws I18NException {

        Query q = em.createQuery("Select registroSalida from RegistroSalida as registroSalida order by registroSalida.id");
        q.setFirstResult(inicio);
        q.setMaxResults(RESULTADOS_PAGINACION);

        return q.getResultList();
    }


    @Override
    public void cambiarEstado(Long idRegistro, Long idEstado) throws I18NException {

        Query q = em.createQuery("update RegistroSalida set estado=:idEstado where id = :idRegistro");
        q.setParameter("idEstado", idEstado);
        q.setParameter("idRegistro", idRegistro);
        q.executeUpdate();
    }


}
