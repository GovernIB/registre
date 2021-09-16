package es.caib.regweb3.persistence.ejb;

import es.caib.regweb3.model.CatLocalidad;
import es.caib.regweb3.utils.RegwebConstantes;
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

@Stateless(name = "CatLocalidadEJB")
@SecurityDomain("seycon")
@RolesAllowed({"RWE_SUPERADMIN","RWE_ADMIN","RWE_USUARI","RWE_WS_ENTRADA","RWE_WS_SALIDA"})
public class CatLocalidadBean extends BaseEjbJPA<CatLocalidad, Long> implements CatLocalidadLocal{

    protected final Logger log = Logger.getLogger(getClass());

    @PersistenceContext(unitName="regweb3")
    private EntityManager em;


    @Override
    public CatLocalidad getReference(Long id) throws Exception {

        return em.getReference(CatLocalidad.class, id);
    }

    @Override
    public CatLocalidad findById(Long id) throws Exception {

        return em.find(CatLocalidad.class, id);
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public List<CatLocalidad> getAll() throws Exception {

        return  em.createQuery("Select catLocalidad from CatLocalidad as catLocalidad order by catLocalidad.id").getResultList();
    }

    @Override
    public Long getTotal() throws Exception {

        Query q = em.createQuery("Select count(catLocalidad.id) from CatLocalidad as catLocalidad");

        return (Long) q.getSingleResult();
    }


    @Override
    @SuppressWarnings(value = "unchecked")
    public List<CatLocalidad> getPagination(int inicio) throws Exception {

        Query q = em.createQuery("Select catLocalidad from CatLocalidad as catLocalidad order by catLocalidad.id");
        q.setFirstResult(inicio);
        q.setMaxResults(RESULTADOS_PAGINACION);
        q.setHint("org.hibernate.readOnly", true);

        return q.getResultList();
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public CatLocalidad findByCodigo(Long codigoLocalidad, Long codigoProvincia, String codigoEntidadGeografica) throws Exception {
        Query q = em.createQuery("Select catLocalidad from CatLocalidad as catLocalidad where catLocalidad.codigoLocalidad =:codigoLocalidad and catLocalidad.provincia.codigoProvincia =:codigoProvincia and catLocalidad.entidadGeografica.codigoEntidadGeografica =:codigoEntidadGeografica");

        q.setParameter("codigoLocalidad", codigoLocalidad);
        q.setParameter("codigoProvincia", codigoProvincia);
        q.setParameter("codigoEntidadGeografica",codigoEntidadGeografica);
        q.setHint("org.hibernate.readOnly", true);

        List<CatLocalidad> catLocalidad = q.getResultList();
        if(catLocalidad.size() == 1){
            return catLocalidad.get(0);
        }else{
            return  null;
        }

    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public CatLocalidad findByLocalidadProvincia(Long codigoLocalidad, Long codigoProvincia) throws Exception{
        Query q = em.createQuery("Select catLocalidad from CatLocalidad as catLocalidad where " +
                "catLocalidad.codigoLocalidad =:codigoLocalidad " +
                "and catLocalidad.provincia.codigoProvincia =:codigoProvincia " +
                "and catLocalidad.entidadGeografica.codigoEntidadGeografica = :entidadGeografica");

        q.setParameter("codigoLocalidad",codigoLocalidad);
        q.setParameter("codigoProvincia",codigoProvincia);
        q.setParameter("entidadGeografica", RegwebConstantes.ENTIDAD_GEOGRAFICA_MUNICIPIO);
        q.setHint("org.hibernate.readOnly", true);

        List<CatLocalidad> catLocalidad = q.getResultList();
        if(catLocalidad.size() > 0){
            return catLocalidad.get(0);
        }else{
            return null;
        }
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public List<CatLocalidad> getByProvincia(Long idProvincia) throws Exception {

        Query q = em.createQuery("Select catLocalidad from CatLocalidad as catLocalidad where " +
                "catLocalidad.provincia.id = :idProvincia and " +
                "catLocalidad.entidadGeografica.codigoEntidadGeografica = :entidadGeografica " +
                "order by catLocalidad.nombre");

        q.setParameter("idProvincia",idProvincia);
        q.setParameter("entidadGeografica", RegwebConstantes.ENTIDAD_GEOGRAFICA_MUNICIPIO);
        q.setHint("org.hibernate.readOnly", true);

        return  q.getResultList();
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public List<CatLocalidad> getByCodigoProvincia(Long codigoProvincia) throws Exception {
        log.info("inicio getByProvincia");
        Query q = em.createQuery("Select catLocalidad from CatLocalidad as catLocalidad where catLocalidad.provincia.codigoProvincia = :codigoProvincia order by catLocalidad.nombre");

        q.setParameter("codigoProvincia", codigoProvincia);
        q.setHint("org.hibernate.readOnly", true);

        return q.getResultList();

    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public List<Object[]> getByCodigoProvinciaObject(Long codigoProvincia) throws Exception {

        Query q = em.createQuery("Select catLocalidad.codigoLocalidad, catLocalidad.nombre, catLocalidad.entidadGeografica.codigoEntidadGeografica from CatLocalidad as catLocalidad where catLocalidad.provincia.codigoProvincia = :codigoProvincia order by catLocalidad.nombre");

        q.setParameter("codigoProvincia", codigoProvincia);
        q.setHint("org.hibernate.readOnly", true);

        return q.getResultList();

    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public CatLocalidad findByNombre(String nombre) throws Exception{

        Query q = em.createQuery("Select catLocalidad from CatLocalidad as catLocalidad where catLocalidad.nombre =:nombre");

        q.setParameter("nombre",nombre);
        q.setHint("org.hibernate.readOnly", true);

        List<CatLocalidad> catLocalidad = q.getResultList();
        if(catLocalidad.size() == 1){
            return catLocalidad.get(0);
        }else{
            return  null;
        }
    }

}
