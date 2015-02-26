package es.caib.regweb.persistence.ejb;

import es.caib.regweb.model.Entidad;
import org.apache.log4j.Logger;
import org.jboss.ejb3.annotation.SecurityDomain;

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

@Stateless(name = "EntidadEJB")
@SecurityDomain("seycon")
public class EntidadBean extends BaseEjbJPA<Entidad, Long> implements EntidadLocal{

    protected final Logger log = Logger.getLogger(getClass());

    @PersistenceContext(unitName="regweb")
    private EntityManager em;


    @Override
    public Entidad findById(Long id) throws Exception {

        return em.find(Entidad.class, id);
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public List<Entidad> getAll() throws Exception {

        return  em.createQuery("Select entidad from Entidad as entidad order by entidad.id").getResultList();
    }

    @Override
    public Long getTotal() throws Exception {

        Query q = em.createQuery("Select count(entidad.id) from Entidad as entidad");

        return (Long) q.getSingleResult();
    }


    @Override
    public List<Entidad> getPagination(int inicio) throws Exception {

        Query q = em.createQuery("Select entidad from Entidad as entidad order by entidad.id");
        q.setFirstResult(inicio);
        q.setMaxResults(RESULTADOS_PAGINACION);

        return q.getResultList();
    }

    @Override
    public Entidad findByCodigoDir3(String codigo) throws Exception {

        Query q = em.createQuery("Select entidad from Entidad as entidad where entidad.codigoDir3 = :codigo ");

        q.setParameter("codigo",codigo);

        List<Entidad> entidad =  q.getResultList();

        if(entidad.size() == 1){
            return entidad.get(0);
        }else{
            return  null;
        }

    }

    @Override
    public Boolean tieneOrganismos(Long idEntidad) throws Exception{
        Query q = em.createQuery("Select organismo.id from Organismo as organismo where organismo.entidad.id = :idEntidad");

        q.setParameter("idEntidad",idEntidad);

        List<Long> organismos = q.getResultList();

        return organismos.size() > 0;
    }

    @Override
    public List<Entidad> getEntidadesAdministrador(Long idUsuarioEntidad) throws Exception{

        Query q = em.createQuery("Select entidad from Entidad as entidad, UsuarioEntidad as usuarioEntidad where usuarioEntidad in elements(entidad.administradores) " +
                "and usuarioEntidad.id = :idUsuarioEntidad and entidad.activo = true");

        q.setParameter("idUsuarioEntidad",idUsuarioEntidad);

        return q.getResultList();
    }

    @Override
    public List<Entidad> getEntidadesPropietario(Long idUsuario) throws Exception {

        Query q = em.createQuery("Select entidad from Entidad as entidad where entidad.propietario.id = :idUsuario " +
                "and entidad.activo = true");

        q.setParameter("idUsuario",idUsuario);

        return q.getResultList();
    }

    @Override
    public Boolean existeCodigoDir3Edit(String codigo, Long idEntidad) throws Exception {

        Query q = em.createQuery("Select entidad from Entidad as entidad where " +
                "entidad.id != :idEntidad and entidad.codigoDir3 = :codigo");

        q.setParameter("codigo",codigo);
        q.setParameter("idEntidad",idEntidad);

        return q.getResultList().size() > 0;
    }

    @Override
    public Boolean esAutorizado(Long idEntidad, Long idUsuario) throws Exception {

        Query q = em.createQuery("Select entidad from Entidad as entidad, UsuarioEntidad as usuarioEntidad where (entidad.propietario.id = :idUsuario or usuarioEntidad in elements(entidad.administradores) ) and entidad.id=:idEntidad");

        q.setParameter("idUsuario",idUsuario);
        q.setParameter("idEntidad",idEntidad);

        return q.getResultList().size() > 0;

    }

}
