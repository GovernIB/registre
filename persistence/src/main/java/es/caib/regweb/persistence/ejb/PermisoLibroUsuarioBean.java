package es.caib.regweb.persistence.ejb;

import es.caib.regweb.model.*;
import es.caib.regweb.utils.RegwebConstantes;
import org.apache.log4j.Logger;
import org.jboss.ejb3.annotation.SecurityDomain;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;
import java.util.Set;

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

    @PersistenceContext(unitName="regweb")
    private EntityManager em;

    @EJB(mappedName = "regweb/CatEstadoEntidadEJB/local")
    public CatEstadoEntidadLocal catEstadoEntidadEjb;

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
    public List<PermisoLibroUsuario> getPagination(int inicio) throws Exception {

        Query q = em.createQuery("Select permisoLibroUsuario from PermisoLibroUsuario as permisoLibroUsuario order by permisoLibroUsuario.id");
        q.setFirstResult(inicio);
        q.setMaxResults(RESULTADOS_PAGINACION);

        return q.getResultList();
    }


    @Override
    public List<PermisoLibroUsuario> findByUsuario(Long idUsuarioEntidad) throws Exception {

        Query q = em.createQuery("Select plu from PermisoLibroUsuario as plu where " +
                "plu.usuario.id = :idUsuarioEntidad");

        q.setParameter("idUsuarioEntidad",idUsuarioEntidad);

        return q.getResultList();
    }

    @Override
    public List<PermisoLibroUsuario> findByUsuarioLibro(Long idUsuarioEntidad, Long idLibro) throws Exception{

        Query q = em.createQuery("Select plu from PermisoLibroUsuario as plu where " +
                "plu.usuario.id = :idUsuarioEntidad and plu.libro.id = :idLibro");

        q.setParameter("idUsuarioEntidad",idUsuarioEntidad);
        q.setParameter("idLibro",idLibro);

        return q.getResultList();
    }

    @Override
    public List<PermisoLibroUsuario> findByLibro(Long idLibro) throws Exception {

        Query q = em.createQuery("Select plu from PermisoLibroUsuario as plu where plu.libro.id = :idLibro");

        q.setParameter("idLibro",idLibro);

        return q.getResultList();
    }

    @Override
    public List<UsuarioEntidad> getUsuariosEntidadByLibro(Long idLibro) throws Exception {

        Query q = em.createQuery("Select distinct plu.usuario from PermisoLibroUsuario as plu where " +
                " plu.libro.id = :idLibro");

        q.setParameter("idLibro",idLibro);

        return q.getResultList();
    }

    @Override
    public List<Libro> getLibrosRegistro(Long idUsuarioEntidad) throws Exception {

        CatEstadoEntidad vigente = catEstadoEntidadEjb.findByCodigo(RegwebConstantes.ESTADO_ENTIDAD_VIGENTE);

        Query q = em.createQuery("Select distinct plu.libro from PermisoLibroUsuario as plu where " +
                "plu.usuario.id = :idUsuarioEntidad and plu.libro.organismo.estado.id = :vigente and " +
                "plu.libro.activo = true and " +
                "(plu.permiso = " + PERMISO_REGISTRO_ENTRADA + " or plu.permiso = "  + PERMISO_REGISTRO_SALIDA + ") and plu.activo = true");

        q.setParameter("idUsuarioEntidad",idUsuarioEntidad);
        q.setParameter("vigente",vigente.getId());

        return q.getResultList();
    }

    @Override
    public List<Libro> getLibrosAdministrados(Long idUsuarioEntidad) throws Exception {

        CatEstadoEntidad vigente = catEstadoEntidadEjb.findByCodigo(RegwebConstantes.ESTADO_ENTIDAD_VIGENTE);

        Query q = em.createQuery("Select distinct plu.libro from PermisoLibroUsuario as plu where " +
                "plu.usuario.id = :idUsuarioEntidad and plu.libro.organismo.estado.id = :vigente and " +
                "plu.libro.activo = true and " +
                "(plu.permiso = " + PERMISO_ADMINISTRACION_LIBRO + " and plu.activo = true)");

        q.setParameter("idUsuarioEntidad",idUsuarioEntidad);
        q.setParameter("vigente",vigente.getId());

        return q.getResultList();
    }

    @Override
    public List<Libro> getLibrosRegistroEntrada(Long idUsuarioEntidad) throws Exception{

        Query q = em.createQuery("Select distinct plu.libro from PermisoLibroUsuario as plu where " +
                "plu.usuario.id = :idUsuarioEntidad and plu.libro.activo = true and "
                + "(plu.permiso = " + PERMISO_REGISTRO_ENTRADA + " and plu.activo = true)");

        q.setParameter("idUsuarioEntidad", idUsuarioEntidad);

        return q.getResultList();
    }

    @Override
    public List<Libro> getLibrosRegistroSalida(Long idUsuarioEntidad) throws Exception{

        Query q = em.createQuery("Select distinct plu.libro from PermisoLibroUsuario as plu where " +
                "plu.usuario.id = :idUsuarioEntidad and plu.libro.activo = true and (plu.permiso = " + PERMISO_REGISTRO_SALIDA + " and plu.activo = true)");

        q.setParameter("idUsuarioEntidad", idUsuarioEntidad);

        return q.getResultList();
    }

    @Override
    public List<Libro> getLibrosConsultaEntrada(Long idUsuarioEntidad) throws Exception{

        Query q = em.createQuery("Select distinct plu.libro from PermisoLibroUsuario as plu where " +
                "plu.usuario.id = :idUsuarioEntidad and plu.libro.activo = true and (plu.permiso = " + PERMISO_CONSULTA_REGISTRO_ENTRADA + " and plu.activo = true)");

        q.setParameter("idUsuarioEntidad",idUsuarioEntidad);

        return q.getResultList();
    }

    @Override
    public List<Libro> getLibrosConsultaSalida(Long idUsuarioEntidad) throws Exception{

        Query q = em.createQuery("Select distinct plu.libro from PermisoLibroUsuario as plu where " +
                "plu.usuario.id = :idUsuarioEntidad and plu.libro.activo = true and (plu.permiso = " + PERMISO_CONSULTA_REGISTRO_SALIDA + " and plu.activo = true)");

        q.setParameter("idUsuarioEntidad",idUsuarioEntidad);

        return q.getResultList();
    }

    @Override
    public List<Libro> getLibrosPermiso(Long idUsuarioEntidad, Long idPermiso) throws Exception{

        Query q = em.createQuery("Select distinct plu.libro from PermisoLibroUsuario as plu where " +
                "plu.usuario.id = :idUsuarioEntidad and plu.libro.activo = true and (plu.permiso = :idPermiso and plu.activo = true)");

        q.setParameter("idPermiso",idPermiso);
        q.setParameter("idUsuarioEntidad",idUsuarioEntidad);

        return q.getResultList();
    }

    @Override
    public List<Libro> getLibrosRegistroOficina(Set<Organismo> organismos, UsuarioEntidad usuario) throws Exception{

        Query q = em.createQuery("Select distinct plu.libro from PermisoLibroUsuario as plu where " +
                "plu.libro.organismo in (:organismos) and plu.usuario.id = :idUsuarioEntidad and " +
                "plu.libro.activo = true and " +
                "(plu.permiso = " +PERMISO_REGISTRO_ENTRADA + " or plu.permiso = " + PERMISO_REGISTRO_SALIDA +" and plu.activo = true)");

        q.setParameter("organismos",organismos);
        q.setParameter("idUsuarioEntidad",usuario.getId());

        return q.getResultList();
    }

    @Override
    public List<Libro> getLibrosOrganismoPermiso(Set<Organismo> organismos, UsuarioEntidad usuario, Long idPermiso) throws Exception{

        Query q = em.createQuery("Select distinct plu.libro from PermisoLibroUsuario as plu where " +
                "plu.libro.organismo in (:organismos) and plu.usuario.id = :idUsuarioEntidad and " +
                "plu.libro.activo = true and " +
                "(plu.permiso = :idPermiso and plu.activo = true)");

        q.setParameter("organismos",organismos);
        q.setParameter("idUsuarioEntidad",usuario.getId());
        q.setParameter("idPermiso",idPermiso);

        return q.getResultList();
    }

    @Override
    public List<Libro> getLibrosEntidadPermiso(Long idEntidad, UsuarioEntidad usuario, Long idPermiso) throws Exception {

        Query q = em.createQuery("Select distinct plu.libro from PermisoLibroUsuario as plu where " +
                "plu.libro.organismo.entidad.id = :idEntidad and plu.usuario.id = :idUsuarioEntidad and " +
                "plu.libro.activo = true and " +
                "(plu.permiso = :idPermiso and plu.activo = true)");

        q.setParameter("idEntidad",idEntidad);
        q.setParameter("idUsuarioEntidad",usuario.getId());
        q.setParameter("idPermiso",idPermiso);

        return q.getResultList();
    }

    @Override
    public Boolean isAdministradorLibro(Long idUsuarioEntidad, Long idLibro) throws Exception {

        CatEstadoEntidad vigente = catEstadoEntidadEjb.findByCodigo(RegwebConstantes.ESTADO_ENTIDAD_VIGENTE);

        Query q = em.createQuery("Select plu from PermisoLibroUsuario as plu where " +
                "plu.usuario.id = :idUsuarioEntidad and plu.libro.id= :idLibro and plu.libro.organismo.estado.id = :vigente and " +
                "plu.libro.activo = true and " +
                "(plu.permiso = " + PERMISO_ADMINISTRACION_LIBRO + " and plu.activo = true)");

        q.setParameter("idUsuarioEntidad",idUsuarioEntidad);
        q.setParameter("idLibro",idLibro);
        q.setParameter("vigente",vigente.getId());

        List<PermisoLibroUsuario> permisos = q.getResultList();

        return permisos.size() == 1;
    }

    @Override
    public Boolean tienePermiso(Long idUsuarioEntidad, Long idLibro, Long idPermiso) throws Exception {

        Query q = em.createQuery("Select plu from PermisoLibroUsuario as plu where " +
                "plu.usuario.id = :idUsuarioEntidad and plu.libro.id = :idLibro and " +
                "plu.libro.activo = true and " +
                "(plu.permiso = :idPermiso and plu.activo = true)");

        q.setParameter("idUsuarioEntidad",idUsuarioEntidad);
        q.setParameter("idLibro",idLibro);
        q.setParameter("idPermiso",idPermiso);

        List<PermisoLibroUsuario> permisos = q.getResultList();

        return permisos.size() == 1;
    }

    @Override
    public List<UsuarioEntidad> getUsuariosEntidadEnLibros(List<Libro> libros) throws Exception{

        Query q = em.createQuery("Select distinct permisoLibroUsuario.usuario from PermisoLibroUsuario as permisoLibroUsuario where " +
                "permisoLibroUsuario.libro in (:libros)");

        q.setParameter("libros",libros);

        return q.getResultList();
    }
}
