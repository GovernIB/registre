package es.caib.regweb3.persistence.ejb;

import es.caib.regweb3.model.RegistroLopdMigrado;
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
 * @author jpernia
 * Date: 19/11/14
 */

@Stateless(name = "RegistroLopdMigradoEJB")
@SecurityDomain("seycon")
@RolesAllowed({"RWE_SUPERADMIN", "RWE_ADMIN", "RWE_USUARI"})
public class RegistroLopdMigradoBean extends BaseEjbJPA<RegistroLopdMigrado, Long> implements RegistroLopdMigradoLocal{

    protected final Logger log = Logger.getLogger(getClass());

    @PersistenceContext(unitName="regweb3")
    private EntityManager em;


    @Override
    public RegistroLopdMigrado getReference(Long id) throws Exception {

        return em.getReference(RegistroLopdMigrado.class, id);
    }

    @Override
    public RegistroLopdMigrado findById(Long id) throws Exception {

        return em.find(RegistroLopdMigrado.class, id);
    }


    @Override
    @SuppressWarnings(value = "unchecked")
    public List<RegistroLopdMigrado> getAll() throws Exception {

        return  em.createQuery("Select registroLopdMigrado from RegistroLopdMigrado as registroLopdMigrado order by registroLopdMigrado.id").getResultList();
    }

    @Override
    public Long getTotal() throws Exception {

        Query q = em.createQuery("Select count(registroLopdMigrado.id) from RegistroLopdMigrado as registroLopdMigrado");

        return (Long) q.getSingleResult();
    }


    @Override
    @SuppressWarnings(value = "unchecked")
    public List<RegistroLopdMigrado> getPagination(int inicio) throws Exception {

        Query q = em.createQuery("Select registroLopdMigrado from RegistroLopdMigrado as registroLopdMigrado order by registroLopdMigrado.id");
        q.setFirstResult(inicio);
        q.setMaxResults(RESULTADOS_PAGINACION);

        return q.getResultList();
    }


    @Override
    @SuppressWarnings(value = "unchecked")
    public List<RegistroLopdMigrado> getByRegistroMigrado(Long numRegistro, String accion) throws Exception {

        Query q = em.createQuery("Select registroLopdMigrado from RegistroLopdMigrado as registroLopdMigrado where " +
                "registroLopdMigrado.registroMigrado.id = :numRegistro and " +
                "registroLopdMigrado.tipoAcceso like :accion order by registroLopdMigrado.fecha desc");

        q.setParameter("numRegistro", numRegistro);
        q.setParameter("accion", accion);

        return q.getResultList();
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public RegistroLopdMigrado getCreacion(Long numRegistro, String accion) throws Exception {

        Query q = em.createQuery("Select registroLopdMigrado from RegistroLopdMigrado as registroLopdMigrado where " +
                "registroLopdMigrado.registroMigrado.id = :numRegistro and " +
                "registroLopdMigrado.tipoAcceso like :accion order by registroLopdMigrado.fecha desc");

        q.setParameter("numRegistro", numRegistro);
        q.setParameter("accion", accion);

        List<RegistroLopdMigrado> registroLopdMigrado = q.getResultList();
        if(registroLopdMigrado.size() == 1){
            return registroLopdMigrado.get(0);
        }else{
            return  null;
        }
    }


    @Override
    public Integer eliminarByEntidad(Long idEntidad) throws Exception{

        List<?> tipos = em.createQuery("Select distinct(id) from RegistroLopdMigrado where registroMigrado.entidad.id =:idEntidad").setParameter("idEntidad",idEntidad).getResultList();

        for (Object id : tipos) {
            remove(findById((Long) id));
        }

        return tipos.size();

    }

}
