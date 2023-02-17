package es.caib.regweb3.persistence.ejb;

import es.caib.regweb3.model.*;
import es.caib.regweb3.utils.RegwebConstantes;
import org.fundaciobit.genapp.common.i18n.I18NException;
import org.jboss.ejb3.annotation.TransactionTimeout;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Fundació BIT.
 *
 * @author earrivi
 * Date: 16/01/14
 */

@Stateless(name = "PermisoOrganismoUsuarioEJB")
@RolesAllowed({"RWE_SUPERADMIN", "RWE_ADMIN", "RWE_USUARI", "RWE_WS_ENTRADA", "RWE_WS_SALIDA", "RWE_WS_CIUDADANO"})
public class PermisoOrganismoUsuarioBean extends BaseEjbJPA<PermisoOrganismoUsuario, Long>
        implements PermisoOrganismoUsuarioLocal, RegwebConstantes {

    protected final Logger log = LoggerFactory.getLogger(getClass());

    @PersistenceContext(unitName = "regweb3")
    private EntityManager em;

    @EJB private CatEstadoEntidadLocal catEstadoEntidadEjb;
    @EJB private OficinaLocal oficinaEjb;
    @EJB private PermisoLibroUsuarioLocal permisoLibroUsuarioEjb;
    @EJB private OrganismoLocal organismoEjb;
    @EJB private LibroLocal libroEjb;
    @EJB private UsuarioEntidadLocal usuarioEntidadEjb;

    @Override
    public PermisoOrganismoUsuario getReference(Long id) throws I18NException {

        return em.getReference(PermisoOrganismoUsuario.class, id);
    }

    @Override
    public PermisoOrganismoUsuario findById(Long id) throws I18NException {

        return em.find(PermisoOrganismoUsuario.class, id);
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public List<PermisoOrganismoUsuario> getAll() throws I18NException {

        return em.createQuery("Select permisoOrganismoUsuario from PermisoOrganismoUsuario as permisoOrganismoUsuario order by permisoOrganismoUsuario.organismo.id").getResultList();
    }

    @Override
    public Long getTotal() throws I18NException {

        Query q = em.createQuery("Select count(permisoOrganismoUsuario.id) from PermisoOrganismoUsuario as permisoOrganismoUsuario");

        return (Long) q.getSingleResult();
    }


    @Override
    @SuppressWarnings(value = "unchecked")
    public List<PermisoOrganismoUsuario> getPagination(int inicio) throws I18NException {

        Query q = em.createQuery("Select permisoOrganismoUsuario from PermisoOrganismoUsuario as permisoOrganismoUsuario order by permisoOrganismoUsuario.id");
        q.setFirstResult(inicio);
        q.setMaxResults(RESULTADOS_PAGINACION);
        q.setHint("org.hibernate.readOnly", true);

        return q.getResultList();
    }

    @Override
    public void crearPermisosUsuarioOrganismo(UsuarioEntidad usuarioEntidad, Organismo organismo) throws I18NException {

        for (int column = 0; column < RegwebConstantes.PERMISOS.length; column++) {
            PermisoOrganismoUsuario pou = new PermisoOrganismoUsuario();
            pou.setOrganismo(organismo);
            pou.setPermiso(RegwebConstantes.PERMISOS[column]);
            pou.setUsuario(usuarioEntidad);
            persist(pou);
        }

    }

    @Override
    public void crearPermisosUsuarioOARM(Organismo organismo) throws I18NException {

        List<UsuarioEntidad> usuariosOARM = usuarioEntidadEjb.getOAMRByEntidad(organismo.getEntidad().getId());

       for(UsuarioEntidad usuarioEntidad: usuariosOARM){

           for (int column = 0; column < RegwebConstantes.PERMISOS.length; column++) {
               PermisoOrganismoUsuario pou = new PermisoOrganismoUsuario();
               pou.setOrganismo(organismo);
               pou.setPermiso(RegwebConstantes.PERMISOS[column]);
               pou.setUsuario(usuarioEntidad);

               if(PERMISOS[column].equals(PERMISO_CONSULTA_REGISTRO_ENTRADA) || PERMISOS[column].equals(PERMISO_CONSULTA_REGISTRO_SALIDA)){
                   pou.setActivo(true);
               }
               persist(pou);
           }
       }

    }

    @Override
    public void eliminarPermisosOrganismo(Long idOrganismo) throws I18NException {

        em.createQuery("delete from PermisoOrganismoUsuario where organismo.id = :idOrganismo").setParameter("idOrganismo", idOrganismo).executeUpdate();

    }

    @Override
    public void eliminarPermisosUsuarioOrganismo(Long idUsuarioEntidad, Long idOrganismo) throws I18NException {

        em.createQuery("delete from PermisoOrganismoUsuario where usuario.id = :idUsuarioEntidad and organismo.id = :idOrganismo")
                .setParameter("idUsuarioEntidad", idUsuarioEntidad)
                .setParameter("idOrganismo", idOrganismo)
                .executeUpdate();

    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public List<PermisoOrganismoUsuario> findByUsuario(Long idUsuarioEntidad) throws I18NException {

        Query q = em.createQuery("Select pou from PermisoOrganismoUsuario as pou where " +
                "pou.usuario.id = :idUsuarioEntidad order by pou.organismo.id, pou.permiso");

        q.setParameter("idUsuarioEntidad", idUsuarioEntidad);

        return q.getResultList();
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public List<Organismo> getOrganismosByUsuario(Long idUsuarioEntidad) throws I18NException {

        Query q = em.createQuery("Select distinct pou.organismo.id, pou.organismo.codigo, pou.organismo.denominacion from PermisoOrganismoUsuario as pou where " +
                "pou.usuario.id = :idUsuarioEntidad and pou.organismo.permiteUsuarios = true order by pou.organismo.id");

        q.setParameter("idUsuarioEntidad", idUsuarioEntidad);

        List<Organismo> organismos = new ArrayList<Organismo>();

        List<Object[]> result = q.getResultList();

        for (Object[] object : result) {
            Organismo organismo = new Organismo((Long) object[0], (String) object[1], (String) object[2]);

            organismos.add(organismo);
        }

        return organismos;
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public List<PermisoOrganismoUsuario> findByUsuarioOrganismo(Long idUsuarioEntidad, Long idOrganismo) throws I18NException {

        Query q = em.createQuery("Select pou from PermisoOrganismoUsuario as pou where " +
                "pou.usuario.id = :idUsuarioEntidad and pou.organismo.id = :idOrganismo and pou.organismo.permiteUsuarios = true order by pou.permiso");

        q.setParameter("idUsuarioEntidad", idUsuarioEntidad);
        q.setParameter("idOrganismo", idOrganismo);

        return q.getResultList();
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public List<PermisoOrganismoUsuario> findByOrganismo(Long idOrganismo) throws I18NException {

        Query q = em.createQuery("Select pou.id, pou.activo, pou.usuario.id, pou.permiso from PermisoOrganismoUsuario as pou where pou.organismo.id = :idOrganismo " +
                "and pou.organismo.permiteUsuarios = true order by pou.permiso");

        q.setParameter("idOrganismo", idOrganismo);

        List<PermisoOrganismoUsuario> plus = new ArrayList<PermisoOrganismoUsuario>();

        List<Object[]> result = q.getResultList();

        for (Object[] object : result) {
            PermisoOrganismoUsuario pou = new PermisoOrganismoUsuario((Long) object[0], (Boolean) object[1], (Long) object[2], (Long) object[3]);

            plus.add(pou);
        }

        return plus;
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public Boolean tienePermisos(Long idOrganismo) throws I18NException {

        Query q = em.createQuery("Select pou.id from PermisoOrganismoUsuario as pou where pou.organismo.id = :idOrganismo " +
                "and pou.organismo.permiteUsuarios = true order by pou.permiso");

        q.setParameter("idOrganismo", idOrganismo);

        return q.getResultList().size() > 0;

    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public List<UsuarioEntidad> getUsuariosEntidadByOrganismo(Long idOrganismo) throws I18NException {

        Query q = em.createQuery("Select distinct pou.usuario from PermisoOrganismoUsuario as pou where " +
                " pou.organismo.id = :idOrganismo and pou.organismo.permiteUsuarios = true");

        q.setParameter("idOrganismo", idOrganismo);

        return q.getResultList();

    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public List<UsuarioEntidad> getUsuariosEntidadByOrganismos(List<Organismo> organismos) throws I18NException {

        Query q = em.createQuery("Select distinct pou.usuario.id, pou.usuario.usuario from PermisoOrganismoUsuario as pou where " +
                "pou.organismo in (:organismos)");

        q.setParameter("organismos", organismos);

        List<Object[]> result = q.getResultList();
        List<UsuarioEntidad> usuarios = new ArrayList<UsuarioEntidad>();

        for (Object[] object : result) {
            UsuarioEntidad usuarioEntidad = new UsuarioEntidad((Long) object[0], (Usuario) object[1], null);

            usuarios.add(usuarioEntidad);
        }

        return usuarios;
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public List<Organismo> getOrganismosPermiso(Long idUsuarioEntidad, Long idPermiso) throws I18NException {

        Query q = em.createQuery("Select distinct pou.organismo.id, pou.organismo.codigo, pou.organismo.denominacion from PermisoOrganismoUsuario as pou where " +
                "pou.usuario.id = :idUsuarioEntidad and pou.organismo.estado.id = :vigente and pou.organismo.permiteUsuarios = true and (pou.permiso = :idPermiso and pou.activo = true)");

        q.setParameter("idPermiso", idPermiso);
        q.setParameter("idUsuarioEntidad", idUsuarioEntidad);
        q.setParameter("vigente", catEstadoEntidadEjb.findByCodigo(RegwebConstantes.ESTADO_ENTIDAD_VIGENTE).getId());
        q.setHint("org.hibernate.readOnly", true);

        List<Organismo> organismos = new ArrayList<Organismo>();

        List<Object[]> result = q.getResultList();

        for (Object[] object : result) {

            organismos.add(new Organismo((Long) object[0], (String) object[1], (String) object[2]));
        }

        return organismos;
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public LinkedHashSet<Oficina> getOficinasPermiso(Long idUsuarioEntidad, Long idPermiso) throws I18NException {

        // Obtenemos los Organismos con los que el usuario tiene el permiso indicado
        List<Organismo> organismos = getOrganismosPermiso(idUsuarioEntidad, idPermiso);

        // Devolvemos las Oficinas que dan servicio a los Organismos anteriores
        return oficinaEjb.oficinasServicio(organismos, true);
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public List<Organismo> getOrganismosRegistro(Long idUsuarioEntidad) throws I18NException {

        Query q = em.createQuery("Select distinct pou.organismo.id, pou.organismo.codigo, pou.organismo.denominacion from PermisoOrganismoUsuario as pou where " +
                "pou.usuario.id = :idUsuarioEntidad and pou.organismo.estado.id = :vigente and " +
                "pou.organismo.permiteUsuarios = true and pou.activo = true and (pou.permiso=:registrarEntrada or pou.permiso=:registrarSalida or " +
                "pou.permiso=:modificacionEntrada or pou.permiso=:modificacionSalida) " +
                " order by pou.organismo.id");

        q.setParameter("idUsuarioEntidad", idUsuarioEntidad);
        q.setParameter("vigente", catEstadoEntidadEjb.findByCodigo(RegwebConstantes.ESTADO_ENTIDAD_VIGENTE).getId());
        q.setParameter("registrarEntrada", RegwebConstantes.PERMISO_REGISTRO_ENTRADA);
        q.setParameter("registrarSalida", RegwebConstantes.PERMISO_REGISTRO_SALIDA);
        q.setParameter("modificacionEntrada", RegwebConstantes.PERMISO_MODIFICACION_REGISTRO_ENTRADA);
        q.setParameter("modificacionSalida", RegwebConstantes.PERMISO_MODIFICACION_REGISTRO_SALIDA);
        q.setHint("org.hibernate.readOnly", true);

        List<Organismo> organismos = new ArrayList<Organismo>();

        List<Object[]> result = q.getResultList();

        for (Object[] object : result) {

            organismos.add(new Organismo((Long) object[0], (String) object[1], (String) object[2]));
        }

        return organismos;

    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public LinkedHashSet<Oficina> getOficinasRegistro(Long idUsuarioEntidad) throws I18NException {

        List<Organismo> organismos = getOrganismosRegistro(idUsuarioEntidad);

        return oficinaEjb.oficinasServicio(organismos, false);

    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public List<Organismo> getOrganismosConsulta(Long idUsuarioEntidad) throws I18NException {

        Query q = em.createQuery("Select distinct pou.organismo.id, pou.organismo.codigo, pou.organismo.denominacion from PermisoOrganismoUsuario as pou where " +
                "pou.usuario.id = :idUsuarioEntidad and pou.organismo.estado.id = :vigente and pou.organismo.permiteUsuarios = true and " +
                "pou.activo = true and (pou.permiso=:consultaEntrada or pou.permiso=:consultaSalida)" +
                " order by pou.organismo.id");

        q.setParameter("idUsuarioEntidad", idUsuarioEntidad);
        q.setParameter("vigente", catEstadoEntidadEjb.findByCodigo(RegwebConstantes.ESTADO_ENTIDAD_VIGENTE).getId());
        q.setParameter("consultaEntrada", RegwebConstantes.PERMISO_CONSULTA_REGISTRO_ENTRADA);
        q.setParameter("consultaSalida", RegwebConstantes.PERMISO_CONSULTA_REGISTRO_SALIDA);
        q.setHint("org.hibernate.readOnly", true);

        List<Organismo> organismos = new ArrayList<Organismo>();

        // Obtenemos los Organismos con los ue el usuario tiene permisos de consulta de entrada
        List<Object[]> result = q.getResultList();

        for (Object[] object : result) {

            organismos.add(new Organismo((Long) object[0], (String) object[1], (String) object[2]));
        }

        return organismos;
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public LinkedHashSet<Oficina> getOficinasConsulta(Long idUsuarioEntidad) throws I18NException {

        List<Organismo> organismos = getOrganismosConsulta(idUsuarioEntidad);

        return oficinaEjb.oficinasServicio(organismos, false);
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public LinkedHashSet<Oficina> getOficinasResponsable(Long idUsuarioEntidad) throws I18NException {

        List<Organismo> organismos = getOrganismosPermiso(idUsuarioEntidad, RegwebConstantes.PERMISO_RESPONSABLE_OFICINA);

        return oficinaEjb.oficinasServicio(organismos, false);
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public LinkedHashSet<Oficina> getOficinasSir(Long idUsuarioEntidad) throws I18NException {

        List<Organismo> organismos = getOrganismosPermiso(idUsuarioEntidad, RegwebConstantes.PERMISO_SIR);

        return oficinaEjb.oficinasSIR(organismos);
    }


    @Override
    @SuppressWarnings(value = "unchecked")
    public Boolean tienePermiso(Set<Long> organismos, Long idUsuarioEntidad, Long idPermiso) throws I18NException {

        Query q = em.createQuery("Select distinct pou.id from PermisoOrganismoUsuario as pou where " +
                "pou.organismo.id in (:organismos) and pou.usuario.id = :idUsuarioEntidad and (pou.permiso = :idPermiso and pou.activo = true)");

        q.setParameter("organismos", organismos);
        q.setParameter("idUsuarioEntidad", idUsuarioEntidad);
        q.setParameter("idPermiso", idPermiso);
        q.setHint("org.hibernate.readOnly", true);

        List<Long> permisos = q.getResultList();

        return permisos.size() > 0;
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public List<UsuarioEntidad> getUsuariosRegistroEntidad(Long idEntidad) throws I18NException {

        Query q = em.createQuery("Select distinct(pou.usuario) from PermisoOrganismoUsuario as pou where " +
                "pou.usuario.entidad.id = :idEntidad and pou.usuario.activo = true and pou.usuario.usuario.tipoUsuario = 1");

        q.setParameter("idEntidad", idEntidad);
        q.setHint("org.hibernate.readOnly", true);

        return q.getResultList();
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public List<UsuarioEntidad> getUsuariosRegistroOrganismo(List<Long> organismos) throws I18NException {

        Query q = em.createQuery("Select distinct(plu.usuario) from PermisoLibroUsuario as plu where " +
                "plu.libro.organismo.id in (:organismos) and plu.usuario.usuario.tipoUsuario = 1 and " +
                "plu.libro.organismo.permiteUsuarios = true and plu.activo = true and (plu.permiso = :registro_entrada or plu.permiso = :registro_salida)");

        q.setParameter("organismos", organismos);
        q.setParameter("registro_entrada", PERMISO_REGISTRO_ENTRADA);
        q.setParameter("registro_salida", PERMISO_REGISTRO_SALIDA);
        q.setHint("org.hibernate.readOnly", true);

        return q.getResultList();
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public Boolean isAdministradorOrganismo(Long idUsuarioEntidad, Long idOrganismo) throws I18NException {

        CatEstadoEntidad vigente = catEstadoEntidadEjb.findByCodigo(RegwebConstantes.ESTADO_ENTIDAD_VIGENTE);

        Query q = em.createQuery("Select pou.id from PermisoOrganismoUsuario as pou where " +
                "pou.usuario.id = :idUsuarioEntidad and pou.organismo.id= :idOrganismo and pou.organismo.estado.id = :vigente and " +
                "pou.organismo.permiteUsuarios = true and " +
                "(pou.permiso = " + PERMISO_RESPONSABLE_OFICINA + " and pou.activo = true)");

        q.setParameter("idUsuarioEntidad", idUsuarioEntidad);
        q.setParameter("idOrganismo", idOrganismo);
        q.setParameter("vigente", vigente.getId());
        q.setHint("org.hibernate.readOnly", true);

        List<Long> permisos = q.getResultList();

        return permisos.size() == 1;
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public Boolean tienePermiso(Long idUsuarioEntidad, Long idOrganismo, Long idPermiso, Boolean organismoActivo) throws I18NException {

        Query q = em.createQuery("Select pou.id from PermisoOrganismoUsuario as pou where " +
                "pou.usuario.id = :idUsuarioEntidad and pou.organismo.id = :idOrganismo and pou.organismo.permiteUsuarios = true and (pou.permiso = :idPermiso and pou.activo = true)");

        q.setParameter("idUsuarioEntidad", idUsuarioEntidad);
        q.setParameter("idOrganismo", idOrganismo);
        q.setParameter("idPermiso", idPermiso);
        q.setHint("org.hibernate.readOnly", true);

        List<Long> permisos = q.getResultList();

        return permisos.size() == 1;
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public Boolean puedeRegistrar(Long idUsuarioEntidad, Long idOrganismo) throws I18NException {

        Query q = em.createQuery("Select pou.id from PermisoOrganismoUsuario as pou where " +
                "pou.usuario.id = :idUsuarioEntidad and pou.organismo.id = :idOrganismo and pou.organismo.permiteUsuarios = true and pou.activo = true and (pou.permiso=:registrarEntrada or pou.permiso=:registrarSalida)");

        q.setParameter("idUsuarioEntidad", idUsuarioEntidad);
        q.setParameter("idOrganismo", idOrganismo);
        q.setParameter("registrarEntrada", RegwebConstantes.PERMISO_REGISTRO_ENTRADA);
        q.setParameter("registrarSalida", RegwebConstantes.PERMISO_REGISTRO_SALIDA);
        q.setHint("org.hibernate.readOnly", true);

        List<Long> permisos = q.getResultList();

        return permisos.size() >= 1;
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public List<Organismo> getOrganismosAdministrados(Long idUsuarioEntidad) throws I18NException {

        CatEstadoEntidad vigente = catEstadoEntidadEjb.findByCodigo(RegwebConstantes.ESTADO_ENTIDAD_VIGENTE);

        Query q = em.createQuery("Select distinct pou.organismo.id, pou.organismo.codigo, pou.organismo.denominacion from PermisoOrganismoUsuario as pou where " +
                "pou.usuario.id = :idUsuarioEntidad and pou.organismo.estado.id = :vigente and " +
                "pou.permiso = :responsable and pou.activo = true");

        q.setParameter("idUsuarioEntidad", idUsuarioEntidad);
        q.setParameter("vigente", vigente.getId());
        q.setParameter("responsable", PERMISO_RESPONSABLE_OFICINA);
        q.setHint("org.hibernate.readOnly", true);

        List<Organismo> organismos = new ArrayList<Organismo>();

        List<Object[]> result = q.getResultList();

        for (Object[] object : result) {

            organismos.add(new Organismo((Long) object[0], (String) object[1], (String) object[2]));
        }

        return organismos;
    }

    @Override
    public void actualizarPermiso(Long idPermisoOrganismoUsuario, Boolean esActivo) throws I18NException {

        Query q = em.createQuery("UPDATE PermisoOrganismoUsuario SET activo = :esActivo WHERE " +
                "id = :idPermisoOrganismoUsuario");

        q.setParameter("esActivo", esActivo);
        q.setParameter("idPermisoOrganismoUsuario", idPermisoOrganismoUsuario);

        q.executeUpdate();
    }


    @Override
    public Boolean existePermiso(Long idUsuarioEntidad, Long idOrganismo, Long idPermiso) throws I18NException {

        Query q = em.createQuery("Select pou.id from PermisoOrganismoUsuario as pou where " +
                "pou.usuario.id = :idUsuarioEntidad and pou.organismo.id = :idOrganismo and " +
                "pou.permiso = :idPermiso");

        q.setParameter("idUsuarioEntidad", idUsuarioEntidad);
        q.setParameter("idOrganismo", idOrganismo);
        q.setParameter("idPermiso", idPermiso);
        q.setHint("org.hibernate.readOnly", true);

        return q.getResultList().size() == 1;
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public List<UsuarioEntidad> getUsuariosPermiso(Set<Organismo> organismos, Long permiso) throws I18NException {

        Query q = em.createQuery("Select distinct pou.usuario from PermisoOrganismoUsuario as pou where " +
                "pou.usuario.usuario.tipoUsuario = :persona and pou.organismo in (:organismos) and pou.activo = true and pou.permiso=:sir");

        q.setParameter("persona", RegwebConstantes.TIPO_USUARIO_PERSONA);
        q.setParameter("organismos", organismos);
        q.setParameter("sir", permiso);

        return q.getResultList();
    }

    @Override
    public void eliminarByUsuario(Long idUsuarioEntidad) throws I18NException {

        em.createQuery("delete from PermisoOrganismoUsuario where usuario.id=:idUsuarioEntidad ").setParameter("idUsuarioEntidad", idUsuarioEntidad).executeUpdate();

    }

    @Override
    public Integer eliminarByEntidad(Long idEntidad) throws I18NException {

        List<?> plus = em.createQuery("select distinct(pou.id) from PermisoOrganismoUsuario as pou where pou.usuario.entidad.id =:idEntidad").setParameter("idEntidad", idEntidad).getResultList();
        Integer total = plus.size();

        if (plus.size() > 0) {

            // Si hay más de 1000 registros, dividimos las queries (ORA-01795).
            while (plus.size() > RegwebConstantes.NUMBER_EXPRESSIONS_IN) {

                List<?> subList = plus.subList(0, RegwebConstantes.NUMBER_EXPRESSIONS_IN);
                em.createQuery("delete from PermisoOrganismoUsuario where id in (:id) ").setParameter("id", subList).executeUpdate();
                plus.subList(0, RegwebConstantes.NUMBER_EXPRESSIONS_IN).clear();
            }

            em.createQuery("delete from PermisoOrganismoUsuario where id in (:plus) ").setParameter("plus", plus).executeUpdate();
        }

        return total;
    }

    @Override
    @TransactionTimeout(value = 1800)  // 30 minutos
    public Integer migrarPermisos(Libro libro) throws I18NException {

        // Activamos que el organismo pueda tener usuarios
        Organismo organismo = libro.getOrganismo();
        organismo.setPermiteUsuarios(true);
        organismoEjb.merge(organismo);

        // Obtenemos los permisos del libro
        List<PermisoLibroUsuario> permisos = permisoLibroUsuarioEjb.findByLibro(libro.getId());

        for (PermisoLibroUsuario plu : permisos) {

            PermisoOrganismoUsuario pou = new PermisoOrganismoUsuario();
            pou.setOrganismo(organismo);
            pou.setPermiso(plu.getPermiso());
            pou.setUsuario(plu.getUsuario());
            pou.setActivo(plu.getActivo());

            persist(pou);

        }

        // Inactivamos el libro
        libro.setActivo(false);
        libroEjb.merge(libro);

        return permisos.size();

    }

}
