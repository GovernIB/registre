package es.caib.regweb3.persistence.ejb;

import es.caib.regweb3.model.Entidad;
import es.caib.regweb3.model.Usuario;
import es.caib.regweb3.model.UsuarioEntidad;
import es.caib.regweb3.persistence.utils.DataBaseUtils;
import es.caib.regweb3.persistence.utils.Paginacion;
import es.caib.regweb3.utils.RegwebConstantes;
import org.apache.log4j.Logger;
import org.fundaciobit.genapp.common.i18n.I18NException;
import org.jboss.ejb3.annotation.SecurityDomain;

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
@SecurityDomain("seycon")
public class UsuarioEntidadBean extends BaseEjbJPA<UsuarioEntidad, Long> implements UsuarioEntidadLocal{

    protected final Logger log = Logger.getLogger(getClass());

    @PersistenceContext(unitName="regweb3")
    private EntityManager em;

    @EJB private UsuarioLocal usuarioEjb;


    @Override
    public UsuarioEntidad getReference(Long id) throws Exception {

        return em.getReference(UsuarioEntidad.class, id);
    }

    @Override
    public UsuarioEntidad findById(Long id) throws Exception {

        return em.find(UsuarioEntidad.class, id);
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public List<UsuarioEntidad> getAll() throws Exception {

        return  em.createQuery("Select usuarioEntidad from UsuarioEntidad as usuarioEntidad order by usuarioEntidad.id").getResultList();
    }

    @Override
    public Long getTotal() throws Exception {

        Query q = em.createQuery("Select count(usuarioEntidad.id) from UsuarioEntidad as usuarioEntidad");

        return (Long) q.getSingleResult();
    }


    @Override
    @SuppressWarnings(value = "unchecked")
    public List<UsuarioEntidad> getPagination(int inicio, Long idEntidad) throws Exception {

        Query q = em.createQuery("Select usuarioEntidad from UsuarioEntidad as usuarioEntidad " +
                "where usuarioEntidad.entidad.id = :idEntidad and usuarioEntidad.activo = true order by usuarioEntidad.id");

        q.setParameter("idEntidad",idEntidad);
        q.setFirstResult(inicio);
        q.setMaxResults(RESULTADOS_PAGINACION);

        return q.getResultList();
    }

    @Override
    public Long getTotal(Long idEntidad) throws Exception {

        Query q = em.createQuery("Select count(usuarioEntidad.id) from UsuarioEntidad as usuarioEntidad " +
                "where usuarioEntidad.entidad.id = :idEntidad and usuarioEntidad.activo = true");

        q.setParameter("idEntidad", idEntidad);

        return (Long) q.getSingleResult();
    }


    @Override
    @SuppressWarnings(value = "unchecked")
    public List<UsuarioEntidad> getPagination(int inicio) throws Exception {

        Query q = em.createQuery("Select usuarioEntidad from UsuarioEntidad as usuarioEntidad where usuarioEntidad.activo = true order by usuarioEntidad.id");
        q.setFirstResult(inicio);
        q.setMaxResults(RESULTADOS_PAGINACION);

        return q.getResultList();
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public UsuarioEntidad comprobarUsuarioEntidad(String identificador, Long idEntidad) throws Exception, I18NException {

        UsuarioEntidad usuarioEntidad = findByIdentificadorEntidad(identificador, idEntidad);

        if(usuarioEntidad == null){

            log.info("Usuario " +identificador+" no existe en la entidad, lo buscamos en el sistema de autentificación");

            // Creamos el Usuario en el sistema
            Usuario usuario = usuarioEjb.crearUsuario(identificador);
            if(usuario != null){
                return persist(new UsuarioEntidad(null, usuario, idEntidad));
            }
        }

        return usuarioEntidad;
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public UsuarioEntidad findByIdentificador(String identificador) throws Exception {

        Query q = em.createQuery("Select usuarioEntidad from UsuarioEntidad as usuarioEntidad "
            + " where upper(usuarioEntidad.usuario.identificador) = :identificador");

        q.setParameter("identificador", identificador.toUpperCase());

        List<UsuarioEntidad> usuarioEntidad = q.getResultList();
        if(usuarioEntidad.size() == 1){
            return usuarioEntidad.get(0);
        }else{
            return  null;
        }

    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public List<UsuarioEntidad> findByUsuario(Long idUsuario) throws Exception{

        Query q = em.createQuery("Select usuarioEntidad from UsuarioEntidad as usuarioEntidad "
            + "where usuarioEntidad.usuario.id = :idUsuario and usuarioEntidad.activo = true and " +
                "usuarioEntidad.entidad.activo = true");

        q.setParameter("idUsuario",idUsuario);

        return q.getResultList();

    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public UsuarioEntidad findByIdentificadorEntidad(String identificador, Long idEntidad) throws Exception {

        Query q = em.createQuery("Select usuarioEntidad from UsuarioEntidad as usuarioEntidad where " +
                "upper(usuarioEntidad.usuario.identificador) = :identificador and " +
                "usuarioEntidad.entidad.id = :idEntidad");

        q.setParameter("identificador",identificador.toUpperCase());
        q.setParameter("idEntidad",idEntidad);

        List<UsuarioEntidad> usuarioEntidad = q.getResultList();
        if(usuarioEntidad.size() == 1){
            return usuarioEntidad.get(0);
        }else{
            return  null;
        }

    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public UsuarioEntidad findByIdentificadorCodigoEntidad(String identificador, String codigoEntidad) throws Exception{
        Query q = em.createQuery("Select usuarioEntidad from UsuarioEntidad as usuarioEntidad where " +
                "upper(usuarioEntidad.usuario.identificador) = :identificador and " +
                "usuarioEntidad.entidad.codigoDir3 = :codigoEntidad");

        q.setParameter("identificador",identificador.toUpperCase());
        q.setParameter("codigoEntidad",codigoEntidad);

        List<UsuarioEntidad> usuarioEntidad = q.getResultList();
        if(usuarioEntidad.size() == 1){
            return usuarioEntidad.get(0);
        }else{
            return  null;
        }
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public UsuarioEntidad findByDocumento(String documento) throws Exception{
        Query q = em.createQuery("Select usuarioEntidad from UsuarioEntidad as usuarioEntidad where usuarioEntidad.usuario.documento = :documento");

        q.setParameter("documento",documento);

        List<UsuarioEntidad> usuarioEntidad = q.getResultList();
        if(usuarioEntidad.size() == 1){
            return usuarioEntidad.get(0);
        }else{
            return  null;
        }
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public List<UsuarioEntidad> findByEntidad(Long idEntidad) throws Exception {

        Query q = em.createQuery("Select usuarioEntidad.id, usuarioEntidad.usuario from UsuarioEntidad as usuarioEntidad where " +
                "usuarioEntidad.entidad.id= :idEntidad order by usuarioEntidad.usuario.apellido1");

        q.setParameter("idEntidad",idEntidad);

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
    public List<Usuario> findActivosByEntidad(Long idEntidad) throws Exception {

        Query q = em.createQuery("Select usuarioEntidad.usuario from UsuarioEntidad as usuarioEntidad where " +
                "usuarioEntidad.entidad.id = :idEntidad and usuarioEntidad.activo = true and usuarioEntidad.usuario.rwe_admin = true order by usuarioEntidad.usuario.apellido1");

        q.setParameter("idEntidad",idEntidad);

        return q.getResultList();
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public List<UsuarioEntidad> findAdministradoresByEntidad(Long idEntidad) throws Exception {

        Query q = em.createQuery("Select usuarioEntidad from UsuarioEntidad as usuarioEntidad where " +
                "usuarioEntidad.entidad.id= :idEntidad and usuarioEntidad.usuario.rwe_admin = true and usuarioEntidad.activo = true order by usuarioEntidad.usuario.apellido1");

        q.setParameter("idEntidad",idEntidad);

        return q.getResultList();
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public UsuarioEntidad findByUsuarioEntidad(Long idUsuario, Long idEntidad) throws Exception{

        Query q = em.createQuery("Select usuarioEntidad from UsuarioEntidad as usuarioEntidad where " +
                "usuarioEntidad.entidad.id= :idEntidad and usuarioEntidad.usuario.id= :idUsuario");

        q.setParameter("idEntidad",idEntidad);
        q.setParameter("idUsuario",idUsuario);

        List<UsuarioEntidad> usuarios =  q.getResultList();

        if(usuarios.size()>0){
            return usuarios.get(0);
        }else{
            return  null;
        }
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public UsuarioEntidad findByUsuarioEntidadActivo(Long idUsuario, Long idEntidad) throws Exception{

        Query q = em.createQuery("Select usuarioEntidad from UsuarioEntidad as usuarioEntidad where " +
                "usuarioEntidad.entidad.id= :idEntidad and usuarioEntidad.usuario.id= :idUsuario and " +
                "usuarioEntidad.activo = true");

        q.setParameter("idEntidad",idEntidad);
        q.setParameter("idUsuario", idUsuario);

        List<UsuarioEntidad> usuarios =  q.getResultList();

        if(usuarios.size()>0){
            return usuarios.get(0);
        }else{
            return  null;
        }
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public List<Entidad> getEntidadesByUsuario(Long idUsuario) throws Exception {

        Query q = em.createQuery("Select usuarioEntidad.entidad.id, usuarioEntidad.entidad.nombre, usuarioEntidad.entidad.oficioRemision from UsuarioEntidad as usuarioEntidad where " +
                "usuarioEntidad.usuario.id = :idUsuario and usuarioEntidad.entidad.activo = true and usuarioEntidad.activo = true order by usuarioEntidad.entidad.id");

        q.setParameter("idUsuario",idUsuario);

        List<Entidad> entidades =  new ArrayList<Entidad>();

        List<Object[]> result = q.getResultList();

        for (Object[] object : result){
            Entidad entidad = new Entidad((Long)object[0],(String)object[1], (Boolean) object[2]);

            entidades.add(entidad);
        }

        return entidades;

    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public Paginacion busqueda(Integer pageNumber,Long idEntidad,String identificador,String nombre, String apellido1, String apellido2, String documento, Long tipoUsuario) throws Exception {

        Query q;
        Query q2;
        Map<String, Object> parametros = new HashMap<String, Object>();
        List<String> where = new ArrayList<String>();

        StringBuilder query = new StringBuilder("Select usuarioEntidad.id, usuarioEntidad.usuario from UsuarioEntidad as usuarioEntidad ");

        if(identificador!= null && identificador.length() > 0){where.add(DataBaseUtils.like("usuarioEntidad.usuario.identificador","identificador",parametros,identificador));}
        if(nombre!= null && nombre.length() > 0){where.add(DataBaseUtils.like("usuarioEntidad.usuario.nombre","nombre",parametros,nombre));}
        if(apellido1!= null && apellido1.length() > 0){where.add(DataBaseUtils.like("usuarioEntidad.usuario.apellido1","apellido1",parametros,apellido1));}
        if(apellido2!= null && apellido2.length() > 0){where.add(DataBaseUtils.like("usuarioEntidad.usuario.apellido2","apellido2", parametros,apellido2));}
        if(documento!= null && documento.length() > 0){where.add(" upper(usuarioEntidad.usuario.documento) like upper(:documento) "); parametros.put("documento","%"+documento.toLowerCase()+"%");}
        if (tipoUsuario != null && tipoUsuario > 0) {
            where.add("usuarioEntidad.usuario.tipoUsuario = :tipoUsuario ");
            parametros.put("tipoUsuario", tipoUsuario);
        }
        where.add("usuarioEntidad.entidad.id = :idEntidad "); parametros.put("idEntidad",idEntidad);
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
            q2 = em.createQuery(query.toString().replaceAll("Select usuarioEntidad.id, usuarioEntidad.usuario from UsuarioEntidad as usuarioEntidad ", "Select count(usuarioEntidad.usuario.id) from UsuarioEntidad as usuarioEntidad "));
            query.append("order by usuarioEntidad.usuario.nombre, usuarioEntidad.usuario.apellido1");
            q = em.createQuery(query.toString());

            for (Map.Entry<String, Object> param : parametros.entrySet()) {
                q.setParameter(param.getKey(), param.getValue());
                q2.setParameter(param.getKey(), param.getValue());
            }

        }else{
            q2 = em.createQuery(query.toString().replaceAll("Select usuarioEntidad.id, usuarioEntidad.usuario from UsuarioEntidad as usuarioEntidad ", "Select count(usuarioEntidad.usuario.id) from UsuarioEntidad as usuarioEntidad "));
            query.append("order by usuarioEntidad.usuario.nombre, usuarioEntidad.usuario.apellido1");
            q = em.createQuery(query.toString());
        }

        Paginacion paginacion;

        if(pageNumber != null){ // Comprobamos si es una busqueda paginada o no
            Long total = (Long)q2.getSingleResult();
            paginacion = new Paginacion(total.intValue(), pageNumber);
            int inicio = (pageNumber - 1) * BaseEjbJPA.RESULTADOS_PAGINACION;
            q.setFirstResult(inicio);
            q.setMaxResults(RESULTADOS_PAGINACION);
        }else{
            paginacion = new Paginacion(0, 0);
        }


        List<UsuarioEntidad> usuarios = new ArrayList<UsuarioEntidad>();

        List<Object[]> result = q.getResultList();

        for (Object[] object : result) {
            UsuarioEntidad usuario = new UsuarioEntidad((Long) object[0], (Usuario) object[1], null);

            usuarios.add(usuario);
        }

        paginacion.setListado(usuarios);

        return paginacion;

    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public List<UsuarioEntidad> findUsuariosPlantilla(Long idEntidad, Long idUsuario, Long tipoUsuario) throws Exception {

        Query q = em.createQuery("Select usuarioEntidad.id, usuarioEntidad.usuario.id, usuarioEntidad.usuario.identificador " +
                "from UsuarioEntidad as usuarioEntidad where usuarioEntidad.entidad.id= :idEntidad and " +
                "usuarioEntidad.usuario.id!= :idUsuario and usuarioEntidad.activo = true and " +
                "usuarioEntidad.usuario.tipoUsuario= :tipoUsuario order by usuarioEntidad.usuario.apellido1");

        q.setParameter("idEntidad",idEntidad);
        q.setParameter("idUsuario",idUsuario);
        q.setParameter("tipoUsuario",tipoUsuario);

        List<UsuarioEntidad> usuarios = new ArrayList<UsuarioEntidad>();

        List<Object[]> result = q.getResultList();

        for (Object[] object : result) {
            UsuarioEntidad usuario = new UsuarioEntidad((Long) object[0], (Long) object[1], (String) object[2]);

            usuarios.add(usuario);
        }

        return usuarios;
    }

    @Override
    public void actualizarOficinaUsuario(Long idUsuario, Long idOficina) throws Exception{

        Query q = em.createQuery("Update UsuarioEntidad set ultimaOficina.id = :idOficina " +
                "where id = :idUsuario");

        q.setParameter("idOficina",idOficina);
        q.setParameter("idUsuario",idUsuario);
        q.executeUpdate();

    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public List<UsuarioEntidad> findOperadoresByEntidad(Long idEntidad) throws Exception {

        Query q = em.createQuery("Select usuarioEntidad from UsuarioEntidad as usuarioEntidad where " +
                "usuarioEntidad.entidad.id= :idEntidad and usuarioEntidad.usuario.rwe_usuari = true order by usuarioEntidad.usuario.apellido1");

        q.setParameter("idEntidad", idEntidad);

        return q.getResultList();
    }

    @Override
    public Integer eliminarByEntidad(Long idEntidad) throws Exception{

        List<?> usuarios = em.createQuery("Select distinct(o.id) from UsuarioEntidad as o where o.entidad.id =:idEntidad").setParameter("idEntidad",idEntidad).getResultList();
        Integer total = usuarios.size();

        if(usuarios.size() > 0){

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
}
