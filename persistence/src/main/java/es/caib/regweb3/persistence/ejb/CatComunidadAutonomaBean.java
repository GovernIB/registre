package es.caib.regweb3.persistence.ejb;

import es.caib.regweb3.model.CatComunidadAutonoma;
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
 * Date: 16/01/14
 */

@Stateless(name = "CatComunidadAutonomaEJB")
@RolesAllowed({"RWE_SUPERADMIN","RWE_ADMIN","RWE_USUARI"})
public class CatComunidadAutonomaBean extends BaseEjbJPA<CatComunidadAutonoma, Long> implements CatComunidadAutonomaLocal{

    protected final Logger log = LoggerFactory.getLogger(getClass());

    @PersistenceContext(unitName="regweb3")
    private EntityManager em;


    @Override
    public CatComunidadAutonoma getReference(Long id) throws I18NException {

        return em.getReference(CatComunidadAutonoma.class, id);
    }

    @Override
    public CatComunidadAutonoma findById(Long id) throws I18NException {

        return em.find(CatComunidadAutonoma.class, id);
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public List<CatComunidadAutonoma> getAll() throws I18NException {

        return  em.createQuery("Select catComunidadAutonoma from CatComunidadAutonoma as catComunidadAutonoma order by catComunidadAutonoma.id")
                .setHint("org.hibernate.cacheable", true)
                .getResultList();
    }

    @Override
    public Long getTotal() throws I18NException {

        Query q = em.createQuery("Select count(catComunidadAutonoma.id) from CatComunidadAutonoma as catComunidadAutonoma");

        return (Long) q.getSingleResult();
    }


    @Override
    @SuppressWarnings(value = "unchecked")
    public List<CatComunidadAutonoma> getPagination(int inicio) throws I18NException {

        Query q = em.createQuery("Select catComunidadAutonoma from CatComunidadAutonoma as catComunidadAutonoma order by catComunidadAutonoma.id");
        q.setFirstResult(inicio);
        q.setMaxResults(RESULTADOS_PAGINACION);

        return q.getResultList();
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public CatComunidadAutonoma findByCodigo(Long codigo) throws I18NException {
        Query q = em.createQuery("Select catComunidadAutonoma from CatComunidadAutonoma as catComunidadAutonoma where catComunidadAutonoma.codigoComunidad = :codigo");

        q.setParameter("codigo",codigo);
        q.setHint("org.hibernate.readOnly", true);

        List<CatComunidadAutonoma> catComunidadAutonoma = q.getResultList();
        if(catComunidadAutonoma.size() == 1){
            return catComunidadAutonoma.get(0);
        }else{
            return  null;
        }

    }
}
