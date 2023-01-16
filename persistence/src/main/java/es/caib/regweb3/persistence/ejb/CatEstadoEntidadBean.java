package es.caib.regweb3.persistence.ejb;

import es.caib.regweb3.model.CatEstadoEntidad;
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

@Stateless(name = "CatEstadoEntidadEJB")
@RolesAllowed({"RWE_SUPERADMIN","RWE_ADMIN","RWE_USUARI","RWE_WS_ENTRADA","RWE_WS_SALIDA"})
public class CatEstadoEntidadBean extends BaseEjbJPA<CatEstadoEntidad, Long> implements CatEstadoEntidadLocal{

    protected final Logger log = LoggerFactory.getLogger(getClass());

    @PersistenceContext(unitName="regweb3")
    private EntityManager em;


    @Override
    public CatEstadoEntidad getReference(Long id) throws I18NException {

        return em.getReference(CatEstadoEntidad.class, id);
    }

    @Override
    public CatEstadoEntidad findById(Long id) throws I18NException {

        return em.find(CatEstadoEntidad.class, id);
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public List<CatEstadoEntidad> getAll() throws I18NException {

        return  em.createQuery("Select catEstadoEntidad from CatEstadoEntidad as catEstadoEntidad order by catEstadoEntidad.id").getResultList();
    }

    @Override
    public Long getTotal() throws I18NException {

        Query q = em.createQuery("Select count(catEstadoEntidad.id) from CatEstadoEntidad as catEstadoEntidad");

        return (Long) q.getSingleResult();
    }


    @Override
    @SuppressWarnings("unchecked")
    public List<CatEstadoEntidad> getPagination(int inicio) throws I18NException {

        Query q = em.createQuery("Select catEstadoEntidad from CatEstadoEntidad as catEstadoEntidad order by catEstadoEntidad.id");
        q.setFirstResult(inicio);
        q.setMaxResults(RESULTADOS_PAGINACION);
        q.setHint("org.hibernate.readOnly", true);

        return q.getResultList();
    }

    @Override
    @SuppressWarnings("unchecked")
    public CatEstadoEntidad findByCodigo(String codigo) throws I18NException {
        Query q = em.createQuery("Select catEstadoEntidad from CatEstadoEntidad as catEstadoEntidad where catEstadoEntidad.codigoEstadoEntidad = :codigo");

        q.setParameter("codigo",codigo);
        q.setHint("org.hibernate.readOnly", true);

        List<CatEstadoEntidad> catEstadoEntidad = q.getResultList();
        if(catEstadoEntidad.size() == 1){
            return catEstadoEntidad.get(0);
        }else{
            return  null;
        }

    }
}
