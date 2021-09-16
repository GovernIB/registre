package es.caib.regweb3.persistence.ejb;

import es.caib.regweb3.model.CatNivelAdministracion;
import org.apache.log4j.Logger;
import org.jboss.ejb3.annotation.SecurityDomain;

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
@SecurityDomain("seycon")
@RolesAllowed({"RWE_SUPERADMIN","RWE_ADMIN","RWE_USUARI"})
public class CatNivelAdministracionBean extends BaseEjbJPA<CatNivelAdministracion, Long> implements CatNivelAdministracionLocal{

    protected final Logger log = Logger.getLogger(getClass());

    @PersistenceContext(unitName="regweb3")
    private EntityManager em;


    @Override
    public CatNivelAdministracion getReference(Long id) throws Exception {

        return em.getReference(CatNivelAdministracion.class, id);
    }

    @Override
    public CatNivelAdministracion findById(Long id) throws Exception {

        return em.find(CatNivelAdministracion.class, id);
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public List<CatNivelAdministracion> getAll() throws Exception {

        return  em.createQuery("Select catNivelAdministracion from CatNivelAdministracion as catNivelAdministracion order by catNivelAdministracion.id").getResultList();
    }

    @Override
    public Long getTotal() throws Exception {

        Query q = em.createQuery("Select count(catNivelAdministracion.id) from CatNivelAdministracion as catNivelAdministracion");

        return (Long) q.getSingleResult();
    }


    @Override
    @SuppressWarnings(value = "unchecked")
    public List<CatNivelAdministracion> getPagination(int inicio) throws Exception {

        Query q = em.createQuery("Select catNivelAdministracion from CatNivelAdministracion as catNivelAdministracion order by catNivelAdministracion.id");
        q.setFirstResult(inicio);
        q.setMaxResults(RESULTADOS_PAGINACION);
        q.setHint("org.hibernate.readOnly", true);

        return q.getResultList();
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public CatNivelAdministracion findByCodigo(Long codigo) throws Exception {
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
