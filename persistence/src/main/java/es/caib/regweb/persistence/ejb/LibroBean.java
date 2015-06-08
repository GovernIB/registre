package es.caib.regweb.persistence.ejb;

import es.caib.regweb.model.Contador;
import es.caib.regweb.model.Libro;
import org.apache.log4j.Logger;
import org.jboss.ejb3.annotation.SecurityDomain;

import javax.ejb.EJB;
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

@Stateless(name = "LibroEJB")
@SecurityDomain("seycon")
public class LibroBean extends BaseEjbJPA<Libro, Long> implements LibroLocal{

    protected final Logger log = Logger.getLogger(getClass());

    @PersistenceContext(unitName="regweb")
    private EntityManager em;

    @EJB
    public ContadorLocal contadorEjb;


    @Override
    public Libro findById(Long id) throws Exception {

        return em.find(Libro.class, id);
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public List<Libro> getAll() throws Exception {

        return  em.createQuery("Select libro from Libro as libro order by libro.id").getResultList();
    }

    @Override
    public Long getTotal() throws Exception {

        Query q = em.createQuery("Select count(libro.id) from Libro as libro");

        return (Long) q.getSingleResult();
    }


    @Override
    public List<Libro> getPagination(int inicio) throws Exception {

        Query q = em.createQuery("Select libro from Libro as libro order by libro.id");
        q.setFirstResult(inicio);
        q.setMaxResults(RESULTADOS_PAGINACION);

        return q.getResultList();
    }

    @Override
    public List<Libro> getLibrosEntidad(Long idEntidad) throws Exception{

        Query q = em.createQuery("Select libro from Libro as libro where libro.activo = true and libro.organismo.entidad.id = :idEntidad order by libro.id");
        q.setParameter("idEntidad",idEntidad);

        return q.getResultList();
    }


    @Override
    public Boolean existeCodigoEdit(String codigo, Long idLibro, Long idEntidad) throws Exception {

        Query q = em.createQuery("Select libro from Libro as libro where " +
                "libro.id != :idLibro and libro.codigo = :codigo and libro.organismo.entidad.id = :idEntidad");

        q.setParameter("codigo",codigo);
        q.setParameter("idLibro",idLibro);
        q.setParameter("idEntidad",idEntidad);

        return q.getResultList().size() > 0;

    }

    @Override
    public Libro findByCodigo(String codigo) throws Exception {

        Query q = em.createQuery("Select libro from Libro as libro where libro.codigo = :codigo");

        q.setParameter("codigo",codigo);

        List<Libro> libro = q.getResultList();
        if(libro.size() == 1){
            return libro.get(0);
        }else{
            return  null;
        }

    }


    @Override
    public Libro findByCodigoEntidad(String codigo, Long idEntidad) throws Exception{

        Query q = em.createQuery("Select libro from Libro as libro where libro.codigo = :codigo " +
                "and libro.organismo.entidad.id = :idEntidad");

        q.setParameter("codigo",codigo);
        q.setParameter("idEntidad",idEntidad);

        List<Libro> libro = q.getResultList();

        if(libro.size() == 1){
            return libro.get(0);
        }else{
            return  null;
        }
    }


    @Override
    public List<Libro> getLibrosActivosOrganismo(Long idOrganismo) throws Exception{

        Query q = em.createQuery("Select libro from Libro as libro where " +
                "libro.organismo.id = :idOrganismo and libro.activo = true");

        q.setParameter("idOrganismo",idOrganismo);

        return  q.getResultList();
    }

    @Override
    public List<Libro> getLibrosOrganismo(Long idOrganismo) throws Exception{

        Query q = em.createQuery("Select libro from Libro as libro where " +
                "libro.organismo.id = :idOrganismo");

        q.setParameter("idOrganismo",idOrganismo);

        return  q.getResultList();
    }

    @Override
    public List<Libro> getTodosLibrosEntidad(Long idEntidad) throws Exception{

        Query q = em.createQuery("Select libro from Libro as libro where libro.organismo.entidad.id = :idEntidad order by libro.id");
        q.setParameter("idEntidad",idEntidad);

        return q.getResultList();
    }

    @Override
    public void reiniciarContadores(Long idLibro) throws Exception{

        Libro libro = findById(idLibro);

        contadorEjb.reiniciarContador(libro.getContadorEntrada().getId());
        contadorEjb.reiniciarContador(libro.getContadorSalida().getId());
        contadorEjb.reiniciarContador(libro.getContadorOficioRemision().getId());

    }

    public Libro crearLibro(Libro libro) throws Exception{

        Contador contadorEntrada = contadorEjb.persist(new Contador());
        Contador contadorSalida = contadorEjb.persist(new Contador());
        Contador contadorOficio = contadorEjb.persist(new Contador());

        libro.setContadorEntrada(contadorEntrada);
        libro.setContadorSalida(contadorSalida);
        libro.setContadorOficioRemision(contadorOficio);

        return persist(libro);
    }

}
