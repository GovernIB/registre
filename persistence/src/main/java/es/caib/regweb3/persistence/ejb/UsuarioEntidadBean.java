package es.caib.regweb3.persistence.ejb;

import es.caib.regweb3.model.Entidad;
import es.caib.regweb3.model.Usuario;
import es.caib.regweb3.model.UsuarioEntidad;
import es.caib.regweb3.persistence.utils.DataBaseUtils;
import es.caib.regweb3.persistence.utils.Paginacion;
import es.caib.regweb3.utils.RegwebConstantes;
import es.caib.regweb3.utils.StringUtils;
import org.fundaciobit.genapp.common.i18n.I18NException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Fundació BIT.
 *
 * @author earrivi
 * Date: 16/01/14
 */

@Stateless(name = "UsuarioEntidadEJB")
@RolesAllowed({"RWE_SUPERADMIN", "RWE_ADMIN", "RWE_USUARI", "RWE_WS_ENTRADA", "RWE_WS_SALIDA", "RWE_WS_CIUDADANO"})
public class UsuarioEntidadBean extends BaseEjbJPA<UsuarioEntidad, Long> implements UsuarioEntidadLocal {

    protected final Logger log = LoggerFactory.getLogger(getClass());

    @PersistenceContext(unitName = "regweb3")
    private EntityManager em;

    @EJB private UsuarioLocal usuarioEjb;


    @Override
    public UsuarioEntidad getReference(Long id) throws I18NException {

        return em.getReference(UsuarioEntidad.class, id);
    }

    @Override
    public UsuarioEntidad findById(Long id) throws I18NException {

        return em.find(UsuarioEntidad.class, id);
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public List<UsuarioEntidad> getAll() throws I18NException {

        return em.createQuery("Select usuarioEntidad from UsuarioEntidad as usuarioEntidad order by usuarioEntidad.id").getResultList();
    }

    @Override
    public Long getTotal() throws I18NException {

        Query q = em.createQuery("Select count(usuarioEntidad.id) from UsuarioEntidad as usuarioEntidad");

        return (Long) q.getSingleResult();
    }


    @Override
    @SuppressWarnings(value = "unchecked")
    public List<UsuarioEntidad> getPagination(int inicio, Long idEntidad) throws I18NException {

        Query q = em.createQuery("Select usuarioEntidad from UsuarioEntidad as usuarioEntidad " +
                "where usuarioEntidad.entidad.id = :idEntidad and usuarioEntidad.activo = true order by usuarioEntidad.id");

        q.setParameter("idEntidad", idEntidad);
        q.setFirstResult(inicio);
        q.setMaxResults(RESULTADOS_PAGINACION);
        q.setHint("org.hibernate.readOnly", true);

        return q.getResultList();
    }

    @Override
    public Long getTotal(Long idEntidad) throws I18NException {

        Query q = em.createQuery("Select count(usuarioEntidad.id) from UsuarioEntidad as usuarioEntidad " +
                "where usuarioEntidad.entidad.id = :idEntidad and usuarioEntidad.activo = true");

        q.setParameter("idEntidad", idEntidad);
        q.setHint("org.hibernate.readOnly", true);

        return (Long) q.getSingleResult();
    }


    @Override
    @SuppressWarnings(value = "unchecked")
    public List<UsuarioEntidad> getPagination(int inicio) throws I18NException {

        Query q = em.createQuery("Select usuarioEntidad from UsuarioEntidad as usuarioEntidad where usuarioEntidad.activo = true order by usuarioEntidad.id");
        q.setFirstResult(inicio);
        q.setMaxResults(RESULTADOS_PAGINACION);
        q.setHint("org.hibernate.readOnly", true);

        return q.getResultList();
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public UsuarioEntidad comprobarUsuarioEntidad(String identificador, Long idEntidad) throws I18NException {

        UsuarioEntidad usuarioEntidad = findByIdentificadorEntidad(identificador, idEntidad);

        if (usuarioEntidad == null) {

            log.info("Usuario " + identificador + " no existe en la entidad, lo buscamos en el sistema de autentificación");

            // Creamos el Usuario en el sistema
            Usuario usuario = usuarioEjb.crearUsuario(identificador);
            if (usuario != null) {
                return persist(new UsuarioEntidad(null, usuario, idEntidad));
            }
        }

        return usuarioEntidad;
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public UsuarioEntidad findByIdentificador(String identificador) throws I18NException {

        Query q = em.createQuery("Select usuarioEntidad from UsuarioEntidad as usuarioEntidad "
                + " where upper(usuarioEntidad.usuario.identificador) = :identificador");

        q.setParameter("identificador", identificador.toUpperCase());
        q.setHint("org.hibernate.readOnly", true);

        List<UsuarioEntidad> usuarioEntidad = q.getResultList();
        if (usuarioEntidad.size() == 1) {
            return usuarioEntidad.get(0);
        } else {
            return null;
        }

    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public List<Entidad> findByUsuario(Long idUsuario) throws I18NException {

        Query q = em.createQuery("Select usuarioEntidad.entidad.id, usuarioEntidad.entidad.nombre, usuarioEntidad.entidad.codigoDir3 from UsuarioEntidad as usuarioEntidad "
                + "where usuarioEntidad.usuario.id = :idUsuario and usuarioEntidad.activo = true and " +
                "usuarioEntidad.entidad.activo = true");

        q.setParameter("idUsuario", idUsuario);
        q.setHint("org.hibernate.readOnly", true);

        List<Entidad> entidades = new ArrayList<>();
        List<Object[]> result = q.getResultList();

        for (Object[] object : result) {
            Entidad entidad = new Entidad((Long) object[0], (String) object[1], (String) object[2]);
            entidades.add(entidad);
        }

        return entidades;

    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public UsuarioEntidad findByIdentificadorEntidad(String identificador, Long idEntidad) throws I18NException {

        Query q = em.createQuery("Select usuarioEntidad from UsuarioEntidad as usuarioEntidad where " +
                "upper(usuarioEntidad.usuario.identificador) = :identificador and " +
                "usuarioEntidad.entidad.id = :idEntidad");

        q.setParameter("identificador", identificador.toUpperCase());
        q.setParameter("idEntidad", idEntidad);
        q.setHint("org.hibernate.readOnly", true);

        List<UsuarioEntidad> usuarioEntidad = q.getResultList();
        if (usuarioEntidad.size() == 1) {
            return usuarioEntidad.get(0);
        } else {
            return null;
        }

    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public UsuarioEntidad findByIdentificadorCodigoEntidad(String identificador, String codigoEntidad) throws I18NException {
        Query q = em.createQuery("Select usuarioEntidad from UsuarioEntidad as usuarioEntidad where " +
                "upper(usuarioEntidad.usuario.identificador) = :identificador and " +
                "usuarioEntidad.entidad.codigoDir3 = :codigoEntidad");

        q.setParameter("identificador", identificador.toUpperCase());
        q.setParameter("codigoEntidad", codigoEntidad);
        q.setHint("org.hibernate.readOnly", true);

        List<UsuarioEntidad> usuarioEntidad = q.getResultList();
        if (usuarioEntidad.size() == 1) {
            return usuarioEntidad.get(0);
        } else {
            return null;
        }
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public UsuarioEntidad findByDocumento(String documento) throws I18NException {
        Query q = em.createQuery("Select usuarioEntidad from UsuarioEntidad as usuarioEntidad where usuarioEntidad.usuario.documento = :documento");

        q.setParameter("documento", documento);
        q.setHint("org.hibernate.readOnly", true);

        List<UsuarioEntidad> usuarioEntidad = q.getResultList();
        if (usuarioEntidad.size() == 1) {
            return usuarioEntidad.get(0);
        } else {
            return null;
        }
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public List<UsuarioEntidad> findByEntidad(Long idEntidad) throws I18NException {

        Query q = em.createQuery("Select usuarioEntidad.id, usuarioEntidad.usuario from UsuarioEntidad as usuarioEntidad where " +
                "usuarioEntidad.entidad.id= :idEntidad order by usuarioEntidad.usuario.apellido1");

        q.setParameter("idEntidad", idEntidad);
        q.setHint("org.hibernate.readOnly", true);

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
    public List<Usuario> findActivosByEntidad(Long idEntidad) throws I18NException {

        Query q = em.createQuery("Select usuarioEntidad.usuario from UsuarioEntidad as usuarioEntidad where " +
                "usuarioEntidad.entidad.id = :idEntidad and usuarioEntidad.activo = true and usuarioEntidad.usuario.rwe_admin = true order by usuarioEntidad.usuario.apellido1");

        q.setParameter("idEntidad", idEntidad);
        q.setHint("org.hibernate.readOnly", true);

        return q.getResultList();
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public List<UsuarioEntidad> findAdministradoresByEntidad(Long idEntidad) throws I18NException {

        Query q = em.createQuery("Select usuarioEntidad from UsuarioEntidad as usuarioEntidad where " +
                "usuarioEntidad.entidad.id= :idEntidad and usuarioEntidad.usuario.rwe_admin = true and usuarioEntidad.activo = true order by usuarioEntidad.usuario.apellido1");

        q.setParameter("idEntidad", idEntidad);
        q.setHint("org.hibernate.readOnly", true);

        return q.getResultList();
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public UsuarioEntidad findByUsuarioEntidad(Long idUsuario, Long idEntidad) throws I18NException {

        Query q = em.createQuery("Select usuarioEntidad from UsuarioEntidad as usuarioEntidad where " +
                "usuarioEntidad.entidad.id= :idEntidad and usuarioEntidad.usuario.id= :idUsuario");

        q.setParameter("idEntidad", idEntidad);
        q.setParameter("idUsuario", idUsuario);
        q.setHint("org.hibernate.readOnly", true);

        List<UsuarioEntidad> usuarios = q.getResultList();

        if (usuarios.size() > 0) {
            return usuarios.get(0);
        } else {
            return null;
        }
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public UsuarioEntidad findByUsuarioEntidadActivo(Long idUsuario, Long idEntidad) throws I18NException {

        Query q = em.createQuery("Select usuarioEntidad from UsuarioEntidad as usuarioEntidad where " +
                "usuarioEntidad.entidad.id= :idEntidad and usuarioEntidad.usuario.id= :idUsuario and " +
                "usuarioEntidad.activo = true");

        q.setParameter("idEntidad", idEntidad);
        q.setParameter("idUsuario", idUsuario);
        q.setHint("org.hibernate.readOnly", true);

        List<UsuarioEntidad> usuarios = q.getResultList();

        if (usuarios.size() > 0) {
            return usuarios.get(0);
        } else {
            return null;
        }
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public List<Entidad> getEntidadesByUsuario(Long idUsuario) throws I18NException {

        Query q = em.createQuery("Select usuarioEntidad.entidad.id, usuarioEntidad.entidad.nombre, usuarioEntidad.entidad.oficioRemision from UsuarioEntidad as usuarioEntidad where " +
                "usuarioEntidad.usuario.id = :idUsuario and usuarioEntidad.entidad.activo = true and usuarioEntidad.activo = true order by usuarioEntidad.entidad.id");

        q.setParameter("idUsuario", idUsuario);
        q.setHint("org.hibernate.readOnly", true);

        List<Entidad> entidades = new ArrayList<Entidad>();

        List<Object[]> result = q.getResultList();

        for (Object[] object : result) {
            Entidad entidad = new Entidad((Long) object[0], (String) object[1], (Boolean) object[2]);

            entidades.add(entidad);
        }

        return entidades;

    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public Paginacion busqueda(Integer pageNumber, Long idEntidad, UsuarioEntidad usuarioEntidad, Long idOrganismo, Long permiso) throws I18NException {

        Query q;
        Query q2;
        Map<String, Object> parametros = new HashMap<String, Object>();
        List<String> where = new ArrayList<String>();

        StringBuilder query;

        if (idOrganismo != null && idOrganismo > 0) { //Si s'ha triat un Organismo a la cerca
            query = new StringBuilder("Select DISTINCT usuarioEntidad.id, usuarioEntidad.usuario, usuarioEntidad.oamr from UsuarioEntidad as usuarioEntidad, PermisoOrganismoUsuario as pou ");
        } else {  //Si NO s'ha triat cap llibre a la cerca
            query = new StringBuilder("Select DISTINCT usuarioEntidad.id, usuarioEntidad.usuario, usuarioEntidad.oamr from UsuarioEntidad as usuarioEntidad ");
        }

        if (StringUtils.isNotEmpty(usuarioEntidad.getUsuario().getIdentificador())) {
            where.add(DataBaseUtils.like("usuarioEntidad.usuario.identificador", "identificador", parametros, usuarioEntidad.getUsuario().getIdentificador()));
        }
        if (StringUtils.isNotEmpty(usuarioEntidad.getUsuario().getNombre())) {
            where.add(DataBaseUtils.like("usuarioEntidad.usuario.nombre", "nombre", parametros, usuarioEntidad.getUsuario().getNombre()));
        }
        if (StringUtils.isNotEmpty(usuarioEntidad.getUsuario().getApellido1())) {
            where.add(DataBaseUtils.like("usuarioEntidad.usuario.apellido1", "apellido1", parametros, usuarioEntidad.getUsuario().getApellido1()));
        }
        if (StringUtils.isNotEmpty(usuarioEntidad.getUsuario().getApellido2())) {
            where.add(DataBaseUtils.like("usuarioEntidad.usuario.apellido2", "apellido2", parametros, usuarioEntidad.getUsuario().getApellido2()));
        }
        if (StringUtils.isNotEmpty(usuarioEntidad.getUsuario().getDocumento())) {
            where.add(" upper(usuarioEntidad.usuario.documento) like upper(:documento) ");
            parametros.put("documento", "%" + usuarioEntidad.getUsuario().getDocumento().toLowerCase() + "%");
        }
        if (usuarioEntidad.getUsuario().getTipoUsuario() != null) {
            where.add("usuarioEntidad.usuario.tipoUsuario = :tipoUsuario ");
            parametros.put("tipoUsuario", usuarioEntidad.getUsuario().getTipoUsuario());
        }

        if (idOrganismo != null && idOrganismo > 0) { //Si s'ha triat un organisme a la cerca
            where.add("usuarioEntidad.id = pou.usuario.id ");
            where.add("pou.organismo.id = :idOrganismo ");
            parametros.put("idOrganismo", idOrganismo);
            where.add("pou.activo = true ");
        }

        if (permiso != null && idOrganismo != null) { //Si s'ha triat un organisme a la cerca
            where.add("pou.permiso = :permiso ");
            parametros.put("permiso", permiso);
        }

        //OAMR
        if (usuarioEntidad.getOamr() != null) {
            where.add(" usuarioEntidad.oamr = :oamr ");
            parametros.put("oamr", usuarioEntidad.getOamr());
        }

        where.add("usuarioEntidad.entidad.id = :idEntidad ");
        parametros.put("idEntidad", idEntidad);
        where.add("usuarioEntidad.activo = true ");

        if (parametros.size() != 0) {
            query.append("where ");
            int count = 0;
            for (String w : where) {
                if (count != 0) {
                    query.append(" and ");
                }
                query.append(w);
                count++;
            }
            q2 = em.createQuery(query.toString().replaceAll("Select DISTINCT usuarioEntidad.id, usuarioEntidad.usuario, usuarioEntidad.oamr from UsuarioEntidad as usuarioEntidad", "Select count(DISTINCT usuarioEntidad.usuario.id) from UsuarioEntidad as usuarioEntidad "));
            query.append("order by usuarioEntidad.usuario.nombre, usuarioEntidad.usuario.apellido1");
            q = em.createQuery(query.toString());

            for (Map.Entry<String, Object> param : parametros.entrySet()) {
                q.setParameter(param.getKey(), param.getValue());
                q2.setParameter(param.getKey(), param.getValue());
            }

        } else {
            q2 = em.createQuery(query.toString().replaceAll("Select DISTINCT usuarioEntidad.id, usuarioEntidad.usuario, usuarioEntidad.oamr from UsuarioEntidad as usuarioEntidad", "Select count(DISTINCT usuarioEntidad.usuario.id) from UsuarioEntidad as usuarioEntidad "));
            query.append("order by usuarioEntidad.usuario.nombre, usuarioEntidad.usuario.apellido1");
            q = em.createQuery(query.toString());
        }

        Paginacion paginacion;

        if (pageNumber != null) { // Comprobamos si es una busqueda paginada o no
            q2.setHint("org.hibernate.readOnly", true);
            Long total = (Long) q2.getSingleResult();
            paginacion = new Paginacion(total.intValue(), pageNumber);
            int inicio = (pageNumber - 1) * BaseEjbJPA.RESULTADOS_PAGINACION;
            q.setFirstResult(inicio);
            q.setMaxResults(RESULTADOS_PAGINACION);
            q.setHint("org.hibernate.readOnly", true);
        } else {
            paginacion = new Paginacion(0, 0);
        }


        List<UsuarioEntidad> usuarios = new ArrayList<UsuarioEntidad>();

        List<Object[]> result = q.getResultList();

        for (Object[] object : result) {
            UsuarioEntidad usuario = new UsuarioEntidad((Long) object[0], (Usuario) object[1], null);
            usuario.setOamr((Boolean) object[2]);
            usuarios.add(usuario);
        }

        paginacion.setListado(usuarios);

        return paginacion;

    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public List<UsuarioEntidad> findUsuariosPlantilla(Long idEntidad, Long idUsuario, Long tipoUsuario) throws I18NException {

        Query q = em.createQuery("Select usuarioEntidad.id, usuarioEntidad.usuario.id, usuarioEntidad.usuario.identificador " +
                "from UsuarioEntidad as usuarioEntidad where usuarioEntidad.entidad.id= :idEntidad and " +
                "usuarioEntidad.usuario.id!= :idUsuario and usuarioEntidad.activo = true and " +
                "usuarioEntidad.usuario.tipoUsuario= :tipoUsuario order by usuarioEntidad.usuario.apellido1");

        q.setParameter("idEntidad", idEntidad);
        q.setParameter("idUsuario", idUsuario);
        q.setParameter("tipoUsuario", tipoUsuario);
        q.setHint("org.hibernate.readOnly", true);

        List<UsuarioEntidad> usuarios = new ArrayList<UsuarioEntidad>();

        List<Object[]> result = q.getResultList();

        for (Object[] object : result) {
            UsuarioEntidad usuario = new UsuarioEntidad((Long) object[0], (Long) object[1], (String) object[2]);

            usuarios.add(usuario);
        }

        return usuarios;
    }

    @Override
    public void actualizarOficinaUsuario(Long idUsuario, Long idOficina) throws I18NException {

        Query q = em.createQuery("Update UsuarioEntidad set ultimaOficina.id = :idOficina " +
                "where id = :idUsuario");

        q.setParameter("idOficina", idOficina);
        q.setParameter("idUsuario", idUsuario);
        q.executeUpdate();

    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public List<UsuarioEntidad> findOperadoresByEntidad(Long idEntidad) throws I18NException {

        Query q = em.createQuery("Select usuarioEntidad from UsuarioEntidad as usuarioEntidad where " +
                "usuarioEntidad.entidad.id= :idEntidad and usuarioEntidad.usuario.rwe_usuari = true order by usuarioEntidad.usuario.apellido1");

        q.setParameter("idEntidad", idEntidad);
        q.setHint("org.hibernate.readOnly", true);

        return q.getResultList();
    }

    @Override
    public Integer eliminarByEntidad(Long idEntidad) throws I18NException {

        List<?> usuarios = em.createQuery("Select distinct(o.id) from UsuarioEntidad as o where o.entidad.id =:idEntidad").setParameter("idEntidad", idEntidad).getResultList();
        Integer total = usuarios.size();

        if (usuarios.size() > 0) {

            // Si hay más de 1000 registros, dividimos las queries (ORA-01795).
            while (usuarios.size() > RegwebConstantes.NUMBER_EXPRESSIONS_IN) {

                List<?> subList = usuarios.subList(0, RegwebConstantes.NUMBER_EXPRESSIONS_IN);

                em.createNativeQuery("delete from RWE_ENTIDAD_USUENT where IDUSUENT in (:usuarios) ").setParameter("usuarios", subList).executeUpdate();
                em.createQuery("delete from UsuarioEntidad where id in (:usuarios) ").setParameter("usuarios", subList).executeUpdate();

                usuarios.subList(0, RegwebConstantes.NUMBER_EXPRESSIONS_IN).clear();
            }

            em.createNativeQuery("delete from RWE_ENTIDAD_USUENT where IDUSUENT in (:usuarios) ").setParameter("usuarios", usuarios).executeUpdate();
            em.createQuery("delete from UsuarioEntidad where id in (:usuarios) ").setParameter("usuarios", usuarios).executeUpdate();
        }

        return total;

    }

    @Override
    public void activarOAMR(Long idUsuarioEntidad, Boolean activo) throws I18NException {

        Query q = em.createQuery("Update UsuarioEntidad set oamr = :activo where id = :idUsuarioEntidad");

        q.setParameter("idUsuarioEntidad", idUsuarioEntidad);
        q.setParameter("activo", activo);
        q.executeUpdate();

    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public List<UsuarioEntidad> getOAMRByEntidad(Long idEntidad) throws I18NException {

        Query q = em.createQuery("Select usuarioEntidad from UsuarioEntidad as usuarioEntidad where " +
                "usuarioEntidad.entidad.id = :idEntidad and usuarioEntidad.activo = true and usuarioEntidad.usuario.rwe_usuari = true and usuarioEntidad.oamr = true");

        q.setParameter("idEntidad", idEntidad);
        q.setHint("org.hibernate.readOnly", true);

        return q.getResultList();
    }

    @Override
    public void activarOAMR(Long idUsuarioEntidad, Boolean activo) throws I18NException {

        Query q = em.createQuery("Update UsuarioEntidad set oamr = :activo where id = :idUsuarioEntidad");

        q.setParameter("idUsuarioEntidad", idUsuarioEntidad);
        q.setParameter("activo", activo);
        q.executeUpdate();

    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public List<UsuarioEntidad> getOAMRByEntidad(Long idEntidad) throws I18NException {

        Query q = em.createQuery("Select usuarioEntidad from UsuarioEntidad as usuarioEntidad where " +
                "usuarioEntidad.entidad.id = :idEntidad and usuarioEntidad.activo = true and usuarioEntidad.usuario.rwe_usuari = true and usuarioEntidad.oamr = true");

        q.setParameter("idEntidad", idEntidad);
        q.setHint("org.hibernate.readOnly", true);

        return q.getResultList();
    }

}