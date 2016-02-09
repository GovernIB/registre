package es.caib.regweb3.persistence.ejb;

import es.caib.regweb3.model.*;
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

    @PersistenceContext(unitName="regweb3")
    private EntityManager em;

    @EJB(mappedName = "regweb3/CatEstadoEntidadEJB/local")
    public CatEstadoEntidadLocal catEstadoEntidadEjb;

    @EJB(mappedName = "regweb3/UsuarioEntidadEJB/local")
    public UsuarioEntidadLocal usuarioEntidadEjb;

    @EJB(mappedName = "regweb3/LibroEJB/local")
    public LibroLocal libroEjb;

    @EJB(mappedName = "regweb3/PermisoLibroUsuarioEJB/local")
    public PermisoLibroUsuarioLocal permisoLibroUsuarioEjb;

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
                "plu.usuario.id = :idUsuarioEntidad order by plu.libro.id, plu.permiso");

        q.setParameter("idUsuarioEntidad", idUsuarioEntidad);

        return q.getResultList();
    }

    @Override
    public List<PermisoLibroUsuario> findByUsuarioLibros(Long idUsuarioEntidad, List<Libro> libros) throws Exception {

        Query q = em.createQuery("Select plu from PermisoLibroUsuario as plu where " +
                "plu.usuario.id = :idUsuarioEntidad and plu.libro in(:libros) order by plu.libro.id, plu.permiso");

        q.setParameter("idUsuarioEntidad", idUsuarioEntidad);
        q.setParameter("libros", libros);

        return q.getResultList();
    }

    @Override
    public List<PermisoLibroUsuario> findByUsuarioLibro(Long idUsuarioEntidad, Long idLibro) throws Exception{

        Query q = em.createQuery("Select plu from PermisoLibroUsuario as plu where " +
                "plu.usuario.id = :idUsuarioEntidad and plu.libro.id = :idLibro order by plu.permiso");

        q.setParameter("idUsuarioEntidad",idUsuarioEntidad);
        q.setParameter("idLibro",idLibro);

        return q.getResultList();
    }

    @Override
    public List<PermisoLibroUsuario> findByLibro(Long idLibro) throws Exception {

        Query q = em.createQuery("Select plu.id, plu.activo, plu.usuario.id from PermisoLibroUsuario as plu where plu.libro.id = :idLibro order by plu.permiso");

        q.setParameter("idLibro",idLibro);

        List<PermisoLibroUsuario> plus = new ArrayList<PermisoLibroUsuario>();

        List<Object[]> result = q.getResultList();

        for (Object[] object : result) {
            PermisoLibroUsuario plu = new PermisoLibroUsuario((Long) object[0], (Boolean) object[1], (Long) object[2]);

            plus.add(plu);
        }

        return plus;
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

        Query q = em.createQuery("Select distinct plu.libro.id, plu.libro.nombre, plu.libro.organismo.id from PermisoLibroUsuario as plu where " +
                "plu.usuario.id = :idUsuarioEntidad and plu.libro.organismo.estado.id = :vigente and " +
                "plu.libro.activo = true and plu.activo = true");

        q.setParameter("idUsuarioEntidad",idUsuarioEntidad);
        q.setParameter("vigente",vigente.getId());

        List<Libro> libros =  new ArrayList<Libro>();

        List<Object[]> result = q.getResultList();

        for (Object[] object : result){
            Libro libro = new Libro((Long)object[0],(String)object[1],(Long)object[2]);

            libros.add(libro);
        }

        return libros;
    }

    @Override
    public List<Libro> getLibrosAdministrados(Long idUsuarioEntidad) throws Exception {

        CatEstadoEntidad vigente = catEstadoEntidadEjb.findByCodigo(RegwebConstantes.ESTADO_ENTIDAD_VIGENTE);

        Query q = em.createQuery("Select distinct plu.libro.id, plu.libro.nombre, plu.libro.organismo.id, plu.libro.organismo.denominacion from PermisoLibroUsuario as plu where " +
                "plu.usuario.id = :idUsuarioEntidad and plu.libro.organismo.estado.id = :vigente and " +
                "plu.libro.activo = true and " +
                "(plu.permiso = " + PERMISO_ADMINISTRACION_LIBRO + " and plu.activo = true)");

        q.setParameter("idUsuarioEntidad",idUsuarioEntidad);
        q.setParameter("vigente",vigente.getId());

        List<Libro> libros =  new ArrayList<Libro>();

        List<Object[]> result = q.getResultList();

        for (Object[] object : result){
            Libro libro = new Libro((Long)object[0],(String)object[1],(Long)object[2],(String)object[3]);

            libros.add(libro);
        }

        return libros;
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
    public List<Libro> getLibrosOrganismoPermiso(Set<Long> organismos, Long idUsuarioEntidad, Long idPermiso) throws Exception {

        Query q = em.createQuery("Select distinct plu.libro from PermisoLibroUsuario as plu where " +
                "plu.libro.organismo.id in (:organismos) and plu.usuario.id = :idUsuarioEntidad and " +
                "plu.libro.activo = true and " +
                "(plu.permiso = :idPermiso and plu.activo = true)");

        q.setParameter("organismos",organismos);
        q.setParameter("idUsuarioEntidad",idUsuarioEntidad);
        q.setParameter("idPermiso",idPermiso);

        return q.getResultList();
    }

    @Override
    public List<Libro> getLibrosEntidadPermiso(Long idEntidad, Long idUsuarioEntidad, Long idPermiso) throws Exception {

        Query q = em.createQuery("Select distinct plu.libro from PermisoLibroUsuario as plu where " +
                "plu.libro.organismo.entidad.id = :idEntidad and plu.usuario.id = :idUsuarioEntidad and " +
                "plu.libro.activo = true and " +
                "(plu.permiso = :idPermiso and plu.activo = true)");

        q.setParameter("idEntidad",idEntidad);
        q.setParameter("idUsuarioEntidad",idUsuarioEntidad);
        q.setParameter("idPermiso",idPermiso);

        return q.getResultList();
    }

    @Override
    public Boolean isAdministradorLibro(Long idUsuarioEntidad, Long idLibro) throws Exception {

        CatEstadoEntidad vigente = catEstadoEntidadEjb.findByCodigo(RegwebConstantes.ESTADO_ENTIDAD_VIGENTE);

        Query q = em.createQuery("Select plu.id from PermisoLibroUsuario as plu where " +
                "plu.usuario.id = :idUsuarioEntidad and plu.libro.id= :idLibro and plu.libro.organismo.estado.id = :vigente and " +
                "plu.libro.activo = true and " +
                "(plu.permiso = " + PERMISO_ADMINISTRACION_LIBRO + " and plu.activo = true)");

        q.setParameter("idUsuarioEntidad",idUsuarioEntidad);
        q.setParameter("idLibro",idLibro);
        q.setParameter("vigente",vigente.getId());

        List<Long> permisos = q.getResultList();

        return permisos.size() == 1;
    }

    @Override
    public Boolean tienePermiso(Long idUsuarioEntidad, Long idLibro, Long idPermiso) throws Exception {

        Query q = em.createQuery("Select plu.id from PermisoLibroUsuario as plu where " +
                "plu.usuario.id = :idUsuarioEntidad and plu.libro.id = :idLibro and " +
                "plu.libro.activo = true and " +
                "(plu.permiso = :idPermiso and plu.activo = true)");

        q.setParameter("idUsuarioEntidad",idUsuarioEntidad);
        q.setParameter("idLibro",idLibro);
        q.setParameter("idPermiso",idPermiso);

        List<Long> permisos = q.getResultList();

        return permisos.size() == 1;
    }

    @Override
    public List<UsuarioEntidad> getUsuariosEntidadEnLibros(List<Libro> libros) throws Exception{

        Query q = em.createQuery("Select distinct permisoLibroUsuario.usuario from PermisoLibroUsuario as permisoLibroUsuario where " +
                "permisoLibroUsuario.libro in (:libros)");

        q.setParameter("libros", libros);

        return q.getResultList();
    }

    @Override
    public void actualizarPermiso(Long idPermisoLibroUsuario, Boolean esActivo) throws Exception {

        Query q = em.createQuery("UPDATE PermisoLibroUsuario SET activo = :esActivo WHERE " +
                "id = :idPermisoLibroUsuario");

        q.setParameter("esActivo",esActivo);
        q.setParameter("idPermisoLibroUsuario",idPermisoLibroUsuario);

        q.executeUpdate();
    }

    @Override
    public void crearPermisosUsuarioNuevo(UsuarioEntidad usuarioEntidad, Long idEntidad) throws Exception {

        List<Libro> librosEntidad = libroEjb.getTodosLibrosEntidad(idEntidad);
        for (Libro libroEntidad : librosEntidad) {
            for (int column = 0; column < RegwebConstantes.PERMISOS.length; column++) {
                PermisoLibroUsuario plu = new PermisoLibroUsuario();
                plu.setLibro(libroEntidad);
                plu.setPermiso(RegwebConstantes.PERMISOS[column]);
                plu.setUsuario(usuarioEntidad);
                permisoLibroUsuarioEjb.persist(plu);
            }
        }

    }

    @Override
    public void crearPermisosLibroNuevo(Libro libro, Long idEntidad) throws Exception {

        List<UsuarioEntidad> usuariosEntidad = usuarioEntidadEjb.findOperadoresByEntidad(idEntidad);
        for (UsuarioEntidad usuario : usuariosEntidad) {
            for ( int column = 0; column < RegwebConstantes.PERMISOS.length; column++ ){
                PermisoLibroUsuario plu = new PermisoLibroUsuario();
                plu.setLibro(libro);
                plu.setPermiso(RegwebConstantes.PERMISOS[column]);
                plu.setUsuario(usuario);
                permisoLibroUsuarioEjb.persist(plu);
            }
        }

    }

    /*@Override
    public void crearPermisosNoExistentes() throws Exception {

        List<Entidad> entidades = em.createQuery("Select entidad from Entidad as entidad order by entidad.id").getResultList();

        for (Entidad entidad : entidades) {
            log.info("ENTITAT: " + entidad.getDescripcion());

            List<UsuarioEntidad> usuariosEntidad = usuarioEntidadEjb.findOperadoresByEntidad(entidad.getId());
            List<Libro> libros = libroEjb.getTodosLibrosEntidad(entidad.getId());

            for (UsuarioEntidad usuario : usuariosEntidad) {
                for (Libro libro : libros) {
                    for (int column = 0; column < RegwebConstantes.PERMISOS.length; column++) {

                        if (!permisoLibroUsuarioEjb.existePermiso(usuario.getId(),libro.getId(),Long.valueOf(column + 1))) {
                            log.info("CREAT_PERMIS (LLIBRE/USUARI/PERMIS): " + libro.getCodigo() + " / " + usuario.getNombreCompleto() + " / " + RegwebConstantes.PERMISOS[column]);
                            PermisoLibroUsuario plu = new PermisoLibroUsuario();
                            plu.setLibro(libro);
                            plu.setPermiso(RegwebConstantes.PERMISOS[column]);
                            plu.setUsuario(usuario);
                            permisoLibroUsuarioEjb.persist(plu);
                        }

                    }
                }
            }

        }
    }*/

    @Override
    public Boolean existePermiso(Long idUsuarioEntidad, Long idLibro, Long idPermiso) throws Exception{

        Query q = em.createQuery("Select plu.id from PermisoLibroUsuario as plu where " +
                "plu.usuario.id = :idUsuarioEntidad and plu.libro.id = :idLibro and " +
                "plu.permiso = :idPermiso");

        q.setParameter("idUsuarioEntidad",idUsuarioEntidad);
        q.setParameter("idLibro",idLibro);
        q.setParameter("idPermiso",idPermiso);

        return q.getResultList().size() == 1;
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
