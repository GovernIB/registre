package es.caib.regweb3.persistence.ejb;

import es.caib.regweb3.model.CatEntidadGeografica;
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

@Stateless(name = "CatEntidadGeograficaEJB")
@RolesAllowed({"RWE_SUPERADMIN","RWE_ADMIN","RWE_USUARI"})
public class CatEntidadGeograficaBean extends BaseEjbJPA<CatEntidadGeografica, Long> implements CatEntidadGeograficaLocal{

    protected final Logger log = LoggerFactory.getLogger(getClass());

    @PersistenceContext(unitName="regweb3")
    private EntityManager em;


    @Override
    public CatEntidadGeografica getReference(Long id) throws I18NException {

        return em.getReference(CatEntidadGeografica.class, id);
    }

    @Override
    public CatEntidadGeografica findById(Long id) throws I18NException {

        return em.find(CatEntidadGeografica.class, id);
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public List<CatEntidadGeografica> getAll() throws I18NException {

        return  em.createQuery("Select catEntidadGeografica from CatEntidadGeografica as catEntidadGeografica order by catEntidadGeografica.id").getResultList();
    }

    @Override
    public Long getTotal() throws I18NException {

        Query q = em.createQuery("Select count(catEntidadGeografica.id) from CatEntidadGeografica as catEntidadGeografica");

        return (Long) q.getSingleResult();
    }


    @Override
    @SuppressWarnings("unchecked")
    public List<CatEntidadGeografica> getPagination(int inicio) throws I18NException {

        Query q = em.createQuery("Select catEntidadGeografica from CatEntidadGeografica as catEntidadGeografica order by catEntidadGeografica.id");
        q.setFirstResult(inicio);
        q.setMaxResults(RESULTADOS_PAGINACION);
        q.setHint("org.hibernate.readOnly", true);

        return q.getResultList();
    }

    @Override
    @SuppressWarnings("unchecked")
    public CatEntidadGeografica findByCodigo(String codigo) throws I18NException {
        Query q = em.createQuery("Select catEntidadGeografica from CatEntidadGeografica as catEntidadGeografica where catEntidadGeografica.codigoEntidadGeografica = :codigo");

        q.setParameter("codigo",codigo);
        q.setHint("org.hibernate.readOnly", true);

        List<CatEntidadGeografica> catEntidadGeografica = q.getResultList();
        if(catEntidadGeografica.size() == 1){
            return catEntidadGeografica.get(0);
        }else{
            return  null;
        }

    }

}
