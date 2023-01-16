package es.caib.regweb3.persistence.ejb;

import es.caib.regweb3.model.Contador;
import es.caib.regweb3.model.Entidad;
import es.caib.regweb3.model.Libro;
import org.fundaciobit.genapp.common.i18n.I18NException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.security.RolesAllowed;
import javax.annotation.security.RunAs;
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

@Stateless(name = "LibroEJB")
@RolesAllowed({"RWE_SUPERADMIN", "RWE_ADMIN", "RWE_USUARI", "RWE_WS_ENTRADA", "RWE_WS_SALIDA"})
@RunAs("RWE_SUPERADMIN")
public class LibroBean extends BaseEjbJPA<Libro, Long> implements LibroLocal {

    protected final Logger log = LoggerFactory.getLogger(getClass());

    @PersistenceContext(unitName = "regweb3")
    private EntityManager em;

    @EJB private ContadorLocal contadorEjb;


    @Override
    public Libro getReference(Long id) throws I18NException {

        return em.getReference(Libro.class, id);
    }

    @Override
    public Libro findById(Long id) throws I18NException {

        return em.find(Libro.class, id);
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public List<Libro> getAll() throws I18NException {

        return em.createQuery("Select libro from Libro as libro order by libro.id").getResultList();
    }

    @Override
    public Long getTotal() throws I18NException {

        Query q = em.createQuery("Select count(libro.id) from Libro as libro");

        return (Long) q.getSingleResult();
    }


    @Override
    @SuppressWarnings(value = "unchecked")
    public List<Libro> getPagination(int inicio) throws I18NException {

        Query q = em.createQuery("Select libro from Libro as libro order by libro.id");
        q.setFirstResult(inicio);
        q.setMaxResults(RESULTADOS_PAGINACION);
        q.setHint("org.hibernate.readOnly", true);

        return q.getResultList();
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public List<Libro> getLibrosEntidad(Long idEntidad) throws I18NException {

        Query q = em.createQuery("Select libro.id, libro.nombre,libro.codigo, libro.organismo.id,libro.organismo.denominacion from Libro as libro where libro.activo = true and libro.organismo.entidad.id = :idEntidad order by libro.id");
        q.setParameter("idEntidad", idEntidad);
        q.setHint("org.hibernate.readOnly", true);

        List<Libro> libros = new ArrayList<Libro>();

        List<Object[]> result = q.getResultList();

        for (Object[] object : result) {
            Libro libro = new Libro((Long) object[0], (String) object[1], (String) object[2], (Long) object[3], (String) object[4]);

            libros.add(libro);
        }

        return libros;
    }


    @Override
    public Boolean existeCodigoEdit(String codigo, Long idLibro, Long idEntidad) throws I18NException {

        Query q = em.createQuery("Select libro.id from Libro as libro where " +
                "libro.id != :idLibro and libro.codigo = :codigo and libro.organismo.entidad.id = :idEntidad");

        q.setParameter("codigo", codigo);
        q.setParameter("idLibro", idLibro);
        q.setParameter("idEntidad", idEntidad);
        q.setHint("org.hibernate.readOnly", true);

        return q.getResultList().size() > 0;

    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public Libro findByCodigo(String codigo) throws I18NException {

        Query q = em.createQuery("Select libro from Libro as libro where libro.codigo = :codigo");

        q.setParameter("codigo", codigo);
        q.setHint("org.hibernate.readOnly", true);

        List<Libro> libro = q.getResultList();
        if (libro.size() == 1) {
            return libro.get(0);
        } else {
            return null;
        }

    }


    @Override
    @SuppressWarnings(value = "unchecked")
    public Libro findByCodigoEntidad(String codigo, Long idEntidad) throws I18NException {

        Query q = em.createQuery("Select libro from Libro as libro where libro.codigo = :codigo " +
                "and libro.organismo.entidad.id = :idEntidad");

        q.setParameter("codigo", codigo);
        q.setParameter("idEntidad", idEntidad);
        q.setHint("org.hibernate.readOnly", true);

        List<Libro> libro = q.getResultList();

        if (libro.size() == 1) {
            return libro.get(0);
        } else {
            return null;
        }
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public Boolean tieneLibro(Long idOrganismo) throws I18NException {

        Query q = em.createQuery("Select libro.id from Libro as libro where " +
                "libro.organismo.id = :idOrganismo and libro.activo = true");

        q.setParameter("idOrganismo", idOrganismo);
        q.setHint("org.hibernate.readOnly", true);

        return q.getResultList().size() > 0;
    }


    @Override
    @SuppressWarnings(value = "unchecked")
    public List<Libro> getLibrosActivosOrganismo(Long idOrganismo) throws I18NException {

        Query q = em.createQuery("Select libro.id,libro.codigo, libro.nombre from Libro as libro where " +
                "libro.organismo.id = :idOrganismo and libro.activo = true");

        q.setParameter("idOrganismo", idOrganismo);
        q.setHint("org.hibernate.readOnly", true);

        List<Object[]> result = q.getResultList();
        List<Libro> libros = new ArrayList<Libro>();

        for (Object[] object : result) {
            Libro libro = new Libro((Long) object[0], (String) object[1], (String) object[2]);

            libros.add(libro);
        }

        return libros;
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public List<Libro> getLibrosActivosOrganismoDiferente(String codigoOrganismo, Long idEntidad) throws I18NException {

        Query q = em.createQuery("Select libro.id,libro.codigo, libro.nombre from Libro as libro where " +
                "libro.organismo.codigo = :codigoOrganismo and libro.activo = true and organismo.entidad.id != :idEntidad");

        q.setParameter("codigoOrganismo", codigoOrganismo);
        q.setParameter("idEntidad", idEntidad);
        q.setHint("org.hibernate.readOnly", true);

        List<Object[]> result = q.getResultList();
        List<Libro> libros = new ArrayList<Libro>();

        for (Object[] object : result) {
            Libro libro = new Libro((Long) object[0], (String) object[1], (String) object[2]);

            libros.add(libro);
        }

        return libros;
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public List<Libro> getLibrosOrganismo(Long idOrganismo) throws I18NException {

        Query q = em.createQuery("Select libro from Libro as libro where " +
                "libro.organismo.id = :idOrganismo");

        q.setParameter("idOrganismo", idOrganismo);
        q.setHint("org.hibernate.readOnly", true);

        return q.getResultList();
    }


    @Override
    @SuppressWarnings(value = "unchecked")
    public List<Libro> getTodosLibrosEntidad(Long idEntidad) throws I18NException {

        Query q = em.createQuery("Select libro from Libro as libro where libro.organismo.entidad.id = :idEntidad order by libro.id");
        q.setParameter("idEntidad", idEntidad);
        q.setHint("org.hibernate.readOnly", true);

        return q.getResultList();
    }

    @Override
    public void reiniciarContadores(Long idLibro) throws I18NException {

        Libro libro = findById(idLibro);

        contadorEjb.reiniciarContador(libro.getContadorEntrada().getId());
        contadorEjb.reiniciarContador(libro.getContadorSalida().getId());
        contadorEjb.reiniciarContador(libro.getContadorOficioRemision().getId());
        contadorEjb.reiniciarContador(libro.getContadorSir().getId());

    }

    @Override
    public Libro crearLibro(Libro libro) throws I18NException {

        Contador contadorEntrada = contadorEjb.persist(new Contador());
        Contador contadorSalida = contadorEjb.persist(new Contador());
        Contador contadorOficio = contadorEjb.persist(new Contador());
        Contador contadorSir = contadorEjb.persist(new Contador());

        libro.setContadorEntrada(contadorEntrada);
        libro.setContadorSalida(contadorSalida);
        libro.setContadorOficioRemision(contadorOficio);
        libro.setContadorSir(contadorSir);

        return persist(libro);
    }

    @Override
    public Integer eliminarByEntidad(Entidad entidad) throws I18NException{

        List<?> libros = em.createQuery("Select distinct(o.id) from Libro as o where o.organismo.entidad.id =:idEntidad").setParameter("idEntidad", entidad.getId()).getResultList();
        List<Long> contadores = new ArrayList<>();

        for (Object idLibro : libros) {
            Long id = (Long) idLibro;

            Libro libro = findById(id);

            if(!libro.getId().equals(entidad.getLibro().getId())){

                if (libro.getContadorEntrada() != null) {
                    contadores.add(libro.getContadorEntrada().getId());
                }
                if (libro.getContadorSalida() != null) {
                    contadores.add(libro.getContadorSalida().getId());
                }
                if (libro.getContadorOficioRemision() != null) {
                    contadores.add(libro.getContadorOficioRemision().getId());
                }
                if (libro.getContadorSir() != null) {
                    contadores.add(libro.getContadorSir().getId());
                }

                em.createQuery("delete from Libro where id = :id ").setParameter("id", id).executeUpdate();
            }else{
                em.createQuery("update from Libro set organismo = null where id = :id ").setParameter("id", id).executeUpdate();

            }
        }

        // Eliminamos los contadores
        for(Long idContador:contadores){
            em.createQuery("delete from Contador where id = :id ").setParameter("id", idContador).executeUpdate();

        }

        return libros.size();
    }

}
