package es.caib.regweb3.persistence.ejb;

import es.caib.regweb3.model.CatNivelAdministracion;
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

@Stateless(name = "CatNivelAdministracionEJB")
@RolesAllowed({"RWE_SUPERADMIN","RWE_ADMIN","RWE_USUARI"})
public class CatNivelAdministracionBean extends BaseEjbJPA<CatNivelAdministracion, Long> implements CatNivelAdministracionLocal{

    protected final Logger log = LoggerFactory.getLogger(getClass());

    @PersistenceContext(unitName="regweb3")
    private EntityManager em;


    @Override
    public CatNivelAdministracion getReference(Long id) throws I18NException {

        return em.getReference(CatNivelAdministracion.class, id);
    }

    @Override
    public CatNivelAdministracion findById(Long id) throws I18NException {

        return em.find(CatNivelAdministracion.class, id);
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public List<CatNivelAdministracion> getAll() throws I18NException {

        return  em.createQuery("Select catNivelAdministracion from CatNivelAdministracion as catNivelAdministracion order by catNivelAdministracion.id")
                .setHint("org.hibernate.cacheable", true)
                .getResultList();
    }

    @Override
    public Long getTotal() throws I18NException {

        Query q = em.createQuery("Select count(catNivelAdministracion.id) from CatNivelAdministracion as catNivelAdministracion");

        return (Long) q.getSingleResult();
    }


    @Override
    @SuppressWarnings(value = "unchecked")
    public List<CatNivelAdministracion> getPagination(int inicio) throws I18NException {

        Query q = em.createQuery("Select catNivelAdministracion from CatNivelAdministracion as catNivelAdministracion order by catNivelAdministracion.id");
        q.setFirstResult(inicio);
        q.setMaxResults(RESULTADOS_PAGINACION);
        q.setHint("org.hibernate.readOnly", true);

        return q.getResultList();
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public CatNivelAdministracion findByCodigo(Long codigo) throws I18NException {
        Query q = em.createQuery("Select catNivelAdministracion from CatNivelAdministracion as catNivelAdministracion where catNivelAdministracion.codigoNivelAdministracion = :codigo");

        q.setParameter("codigo",codigo);
        q.setHint("org.hibernate.readOnly", true);

        List<CatNivelAdministracion> catNivelAdministracion = q.getResultList();
        if(catNivelAdministracion.size() == 1){
            return catNivelAdministracion.get(0);
        }else{
            return  null;
        }
    }
}
