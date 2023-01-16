package es.caib.regweb3.persistence.ejb;

import es.caib.regweb3.model.CatPais;
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

@Stateless(name = "CatPaisEJB")
@RolesAllowed({"RWE_SUPERADMIN","RWE_ADMIN","RWE_USUARI","RWE_WS_ENTRADA","RWE_WS_SALIDA"})
public class CatPaisBean extends BaseEjbJPA<CatPais, Long> implements CatPaisLocal{

    protected final Logger log = LoggerFactory.getLogger(getClass());

    @PersistenceContext(unitName="regweb3")
    private EntityManager em;


    @Override
    public CatPais getReference(Long id) throws I18NException {

        return em.getReference(CatPais.class, id);
    }

    @Override
    public CatPais findById(Long id) throws I18NException {

        return em.find(CatPais.class, id);
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public List<CatPais> getAll() throws I18NException {

        return  em.createQuery("Select catPais from CatPais as catPais order by catPais.descripcionPais").getResultList();
    }

    @Override
    public Long getTotal() throws I18NException {

        Query q = em.createQuery("Select count(catPais.id) from CatPais as catPais");

        return (Long) q.getSingleResult();
    }


    @Override
    @SuppressWarnings(value = "unchecked")
    public List<CatPais> getPagination(int inicio) throws I18NException {

        Query q = em.createQuery("Select catPais from CatPais as catPais order by catPais.id");
        q.setFirstResult(inicio);
        q.setMaxResults(RESULTADOS_PAGINACION);
        q.setHint("org.hibernate.readOnly", true);

        return q.getResultList();
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public CatPais findByCodigo(Long codigo) throws I18NException {
        Query q = em.createQuery("Select catPais from CatPais as catPais where catPais.codigoPais = :codigo");

        q.setParameter("codigo",codigo);
        q.setHint("org.hibernate.readOnly", true);

        List<CatPais> catPais = q.getResultList();
        if(catPais.size() == 1){
            return catPais.get(0);
        }else{
            return  null;
        }
    }
}
