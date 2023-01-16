package es.caib.regweb3.persistence.ejb;

import es.caib.regweb3.model.CatProvincia;
import es.caib.regweb3.model.utils.ObjetoBasico;
import org.fundaciobit.genapp.common.i18n.I18NException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Fundació BIT.
 *
 * @author earrivi
 * Date: 16/01/14
 */

@Stateless(name = "CatProvinciaEJB")
@RolesAllowed({"RWE_SUPERADMIN","RWE_ADMIN","RWE_USUARI","RWE_WS_ENTRADA","RWE_WS_SALIDA"})
public class CatProvinciaBean extends BaseEjbJPA<CatProvincia, Long> implements CatProvinciaLocal{

    protected final Logger log = LoggerFactory.getLogger(getClass());

    @PersistenceContext(unitName="regweb3")
    private EntityManager em;


    @Override
    public CatProvincia getReference(Long id) throws I18NException {

        return em.getReference(CatProvincia.class, id);
    }

    @Override
    public CatProvincia findById(Long id) throws I18NException {

        return em.find(CatProvincia.class, id);
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public List<CatProvincia> getAll() throws I18NException {

        return  em.createQuery("Select catProvincia from CatProvincia as catProvincia order by catProvincia.descripcionProvincia").getResultList();
    }

    @Override
    public Long getTotal() throws I18NException {

        Query q = em.createQuery("Select count(catProvincia.id) from CatProvincia as catProvincia");

        return (Long) q.getSingleResult();
    }


    @Override
    @SuppressWarnings("unchecked")
    public List<CatProvincia> getPagination(int inicio) throws I18NException {

        Query q = em.createQuery("Select catProvincia from CatProvincia as catProvincia order by catProvincia.id");
        q.setFirstResult(inicio);
        q.setMaxResults(RESULTADOS_PAGINACION);
        q.setHint("org.hibernate.readOnly", true);

        return q.getResultList();
    }

    @Override
    @SuppressWarnings("unchecked")
    public CatProvincia findByCodigo(Long codigo) throws I18NException {
        Query q = em.createQuery("Select catProvincia from CatProvincia as catProvincia where catProvincia.codigoProvincia = :codigo");

        q.setParameter("codigo",codigo);
        q.setHint("org.hibernate.readOnly", true);

        List<CatProvincia> catProvincia = q.getResultList();
        if(catProvincia.size() == 1){
            return catProvincia.get(0);
        }else{
            return  null;
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<CatProvincia> getByComunidad(Long codigoComunidad) throws I18NException {

        Query q = em.createQuery("Select catProvincia from CatProvincia as catProvincia where catProvincia.comunidadAutonoma.codigoComunidad = :codigoComunidad order by catProvincia.descripcionProvincia");

        q.setParameter("codigoComunidad", codigoComunidad);
        q.setHint("org.hibernate.readOnly", true);

        return q.getResultList();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<ObjetoBasico> getByComunidadObject(Long codigoComunidad) throws I18NException {

        Query q = em.createQuery("Select catProvincia.codigoProvincia, catProvincia.descripcionProvincia from CatProvincia as catProvincia where catProvincia.comunidadAutonoma.codigoComunidad = :codigoComunidad order by catProvincia.descripcionProvincia");

        q.setParameter("codigoComunidad", codigoComunidad);
        q.setHint("org.hibernate.readOnly", true);

        List<Object[]> provincias = q.getResultList();

        List<ObjetoBasico> provinciasob = new ArrayList<ObjetoBasico>();
        for (Object[] object : provincias) {

            provinciasob.add(new ObjetoBasico((Long) object[0], (String) object[1]));
        }
        return provinciasob;
    }
}
