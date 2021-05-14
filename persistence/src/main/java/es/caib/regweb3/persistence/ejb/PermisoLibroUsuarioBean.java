package es.caib.regweb3.persistence.ejb;

import es.caib.regweb3.model.PermisoLibroUsuario;
import es.caib.regweb3.utils.RegwebConstantes;
import org.apache.log4j.Logger;
import org.jboss.ejb3.annotation.SecurityDomain;

import javax.ejb.EJB;
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

@Stateless(name = "PermisoLibroUsuarioEJB")
@SecurityDomain("seycon")
public class PermisoLibroUsuarioBean extends BaseEjbJPA<PermisoLibroUsuario, Long> 
   implements PermisoLibroUsuarioLocal, RegwebConstantes {

    protected final Logger log = Logger.getLogger(getClass());

    @PersistenceContext(unitName="regweb3")
    private EntityManager em;

    @EJB private CatEstadoEntidadLocal catEstadoEntidadEjb;
    @EJB private UsuarioEntidadLocal usuarioEntidadEjb;
    @EJB private LibroLocal libroEjb;


    @Override
    public PermisoLibroUsuario getReference(Long id) throws Exception {

        return em.getReference(PermisoLibroUsuario.class, id);
    }

    @Override
    public PermisoLibroUsuario findById(Long id) throws Exception {

        return em.find(PermisoLibroUsuario.class, id);
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public List<PermisoLibroUsuario> getAll() throws Exception {

        return  em.createQuery("Select permisoLibroUsuario from PermisoLibroUsuario as permisoLibroUsuario order by permisoLibroUsuario.libro.id").getResultList();
    }

    @Override
    public Long getTotal() throws Exception {

        Query q = em.createQuery("Select count(permisoLibroUsuario.id) from PermisoLibroUsuario as permisoLibroUsuario");

        return (Long) q.getSingleResult();
    }


    @Override
    @SuppressWarnings(value = "unchecked")
    public List<PermisoLibroUsuario> getPagination(int inicio) throws Exception {

        Query q = em.createQuery("Select permisoLibroUsuario from PermisoLibroUsuario as permisoLibroUsuario order by permisoLibroUsuario.id");
        q.setFirstResult(inicio);
        q.setMaxResults(RESULTADOS_PAGINACION);
        q.setHint("org.hibernate.readOnly", true);

        return q.getResultList();
    }


    @Override
    @SuppressWarnings(value = "unchecked")
    public List<PermisoLibroUsuario> findByLibro(Long idLibro) throws Exception {

        Query q = em.createQuery("Select plu.id, plu.permiso, plu.activo, plu.usuario.id from PermisoLibroUsuario as plu where plu.libro.id = :idLibro order by plu.usuario.id");

        q.setParameter("idLibro",idLibro);

        List<PermisoLibroUsuario> plus = new ArrayList<PermisoLibroUsuario>();

        List<Object[]> result = q.getResultList();

        for (Object[] object : result) {
            PermisoLibroUsuario plu = new PermisoLibroUsuario((Long) object[0],(Long) object[1], (Boolean) object[2], (Long) object[3]);

            plus.add(plu);
        }

        return plus;
    }

    @Override
    public Integer eliminarByEntidad(Long idEntidad) throws Exception{

        List<?> plus = em.createQuery("select distinct(plu.id) from PermisoLibroUsuario as plu where plu.usuario.entidad.id =:idEntidad").setParameter("idEntidad",idEntidad).getResultList();
        Integer total = plus.size();

        if(plus.size() > 0){

            // Si hay más de 1000 registros, dividimos las queries (ORA-01795).
            while (plus.size() > RegwebConstantes.NUMBER_EXPRESSIONS_IN) {

                List<?> subList = plus.subList(0, RegwebConstantes.NUMBER_EXPRESSIONS_IN);
                em.createQuery("delete from PermisoLibroUsuario where id in (:id) ").setParameter("id", subList).executeUpdate();
                plus.subList(0, RegwebConstantes.NUMBER_EXPRESSIONS_IN).clear();
            }

            em.createQuery("delete from PermisoLibroUsuario where id in (:plus) ").setParameter("plus", plus).executeUpdate();
        }

        return total;
    }

}
