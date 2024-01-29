package es.caib.regweb3.persistence.ejb;

import es.caib.regweb3.model.CatIsla;
import es.caib.regweb3.model.CatProvincia;
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
 * Created by DGSMAD.
 *
 * @author earrivi
 * Date: 02/01/24
 */

@Stateless(name = "CatIslaEJB")
@RolesAllowed({"RWE_SUPERADMIN","RWE_ADMIN","RWE_USUARI","RWE_WS_ENTRADA","RWE_WS_SALIDA"})
public class CatIslaBean extends BaseEjbJPA<CatIsla, Long> implements CatIslaLocal{

    protected final Logger log = LoggerFactory.getLogger(getClass());

    @PersistenceContext(unitName="regweb3")
    private EntityManager em;


    @Override
    public CatIsla getReference(Long id) throws I18NException {

        return em.getReference(CatIsla.class, id);
    }

    @Override
    public CatIsla findById(Long id) throws I18NException {

        return em.find(CatIsla.class, id);
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public List<CatIsla> getAll() throws I18NException {

        return em.createQuery("Select catIsla from CatIsla as catIsla order by catIsla.descripcionIsla").getResultList();
    }

    @Override
    public Long getTotal() throws I18NException {

        Query q = em.createQuery("Select count(catIsla.id) from CatIsla as catIsla");

        return (Long) q.getSingleResult();
    }


    @Override
    @SuppressWarnings(value = "unchecked")
    public List<CatIsla> getPagination(int inicio) throws I18NException {

        Query q = em.createQuery("Select catIsla from CatIsla as catIsla order by catIsla.id");
        q.setFirstResult(inicio);
        q.setMaxResults(RESULTADOS_PAGINACION);
        q.setHint("org.hibernate.readOnly", true);

        return q.getResultList();
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public CatIsla findByCodigo(Long codigo) throws I18NException {
        Query q = em.createQuery("Select catIsla from CatIsla as catIsla where catIsla.codigoIsla = :codigo");

        q.setParameter("codigo",codigo);
        q.setHint("org.hibernate.readOnly", true);

        List<CatIsla> catIsla = q.getResultList();
        if(catIsla.size() == 1){
            return catIsla.get(0);
        }else{
            return  null;
        }
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public List<CatIsla> getByProvincia(CatProvincia provincia) throws I18NException {
        return em.createQuery("Select catIsla from CatIsla as catIsla where catIsla.provincia = :provincia order by catIsla.descripcionIsla").setParameter("provincia", provincia).getResultList();
    }
}
