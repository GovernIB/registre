package es.caib.regweb3.persistence.ejb;

import es.caib.regweb3.model.Rol;
import es.caib.regweb3.model.Usuario;
import es.caib.regweb3.persistence.utils.DataBaseUtils;
import es.caib.regweb3.persistence.utils.Paginacion;
import es.caib.regweb3.utils.Configuracio;
import es.caib.regweb3.utils.RegwebConstantes;
import org.apache.log4j.Logger;
import org.fundaciobit.genapp.common.i18n.I18NException;
import org.fundaciobit.pluginsib.userinformation.IUserInformationPlugin;
import org.fundaciobit.pluginsib.userinformation.RolesInfo;
import org.fundaciobit.pluginsib.userinformation.UserInfo;
import org.jboss.ejb3.annotation.SecurityDomain;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.naming.InitialContext;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.*;

/**
 * Created by Fundació BIT.
 *
 * @author earrivi
 * Date: 16/01/14
 */

@Stateless(name = "UsuarioEJB")
@SecurityDomain("seycon")
public class UsuarioBean extends BaseEjbJPA<Usuario, Long> implements UsuarioLocal{

    protected final Logger log = Logger.getLogger(getClass());

    @PersistenceContext(unitName="regweb3")
    private EntityManager em;

    @EJB private PluginLocal pluginEjb;
    @EJB private RolLocal rolEjb;

    @Override
    public Usuario getReference(Long id) throws Exception {

        return em.getReference(Usuario.class, id);
    }

    @Override
    public Usuario findById(Long id) throws Exception {

        return em.find(Usuario.class, id);
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public List<Usuario> getAll() throws Exception {

        return  em.createQuery("Select usuario from Usuario as usuario order by usuario.id").getResultList();
    }

    @Override
    public Long getTotal() throws Exception {

        Query q = em.createQuery("Select count(usuario.id) from Usuario as usuario");

        return (Long) q.getSingleResult();
    }


    @Override
    @SuppressWarnings(value = "unchecked")
    public List<Usuario> getPagination(int inicio) throws Exception {

        Query q = em.createQuery("Select usuario from Usuario as usuario order by usuario.id");
        q.setFirstResult(inicio);
        q.setMaxResults(RESULTADOS_PAGINACION);

        return q.getResultList();
    }

    /**
     * Crea un nuevo usuario en REGWEB3, a partir del identificador de Seycon, obtiene sus
     * datos personales de la bbdd de Seycon, aunque no tenga ningún ROL de REGWEB3.
     * @param identificador
     * @return
     * @throws Exception
     */
    public Usuario crearUsuario(String identificador) throws Exception, I18NException {

        // Comprobamos si el Usuario ya existe en RWE_USUARIO
        Usuario usuario = findByIdentificador(identificador);

        // Si no existe, lo creamos
        if(usuario == null){

            IUserInformationPlugin loginPlugin = (IUserInformationPlugin) pluginEjb.getPlugin(null,RegwebConstantes.PLUGIN_USER_INFORMATION);
            UserInfo regwebUserInfo = loginPlugin.getUserInfoByUserName(identificador);

            if(regwebUserInfo != null){ // Si el documento existe en el Sistema de autentificación

                usuario = new Usuario();
                usuario.setNombre(regwebUserInfo.getName());

                //Idioma por defecto
                Long idioma  = RegwebConstantes.IDIOMA_ID_BY_CODIGO.get(Configuracio.getDefaultLanguage());
                usuario.setIdioma(idioma);

                if(regwebUserInfo.getSurname1() != null){
                    usuario.setApellido1(regwebUserInfo.getSurname1());
                }else{
                    usuario.setApellido1("");
                }

                if(regwebUserInfo.getSurname2() != null){
                    usuario.setApellido2(regwebUserInfo.getSurname2());
                } else {
                    usuario.setApellido2("");
                }

                usuario.setDocumento(regwebUserInfo.getAdministrationID());
                usuario.setIdentificador(identificador);

                // Email
                if(regwebUserInfo.getEmail() != null){
                    usuario.setEmail(regwebUserInfo.getEmail());
                }else{
                    usuario.setEmail("no hi ha email");
                }

                // Tipo Usuario
                if(identificador.startsWith("$")){
                    usuario.setTipoUsuario(RegwebConstantes.TIPO_USUARIO_APLICACION);
                }else{
                    usuario.setTipoUsuario(RegwebConstantes.TIPO_USUARIO_PERSONA);
                }

                // Roles
                RolesInfo rolesInfo = loginPlugin.getRolesByUsername(identificador);

                if(rolesInfo != null && rolesInfo.getRoles().length > 0){
                    List<String> roles = new ArrayList<String>();
                    Collections.addAll(roles, rolesInfo.getRoles());

                    if(roles.size() > 0){
                        usuario.setRoles(rolEjb.getByRol(roles));
                    }
                }else{
                    log.info("El usuario " + identificador + " no dispone de ningun Rol de REGWEB3 en el sistema de autentificacion");
                }

                // Guardamos el nuevo Usuario
                usuario = persist(usuario);
                log.info("Usuario " + usuario.getNombreCompleto() + " creado correctamente");
                return usuario;
            }else{
                log.info("Usuario " + identificador + " no encontrado en el sistema de usuarios");
                return null;
            }
        }

        return usuario;
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public Usuario findByIdentificador(String identificador) throws Exception {

        Query q = em.createQuery("Select usuario from Usuario as usuario where usuario.identificador = :identificador");

        q.setParameter("identificador",identificador);

        List<Usuario> usuario = q.getResultList();
        if(usuario.size() == 1){
            return usuario.get(0);
        }else{
            return  null;
        }

    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public Usuario findByDocumento(String documento) throws Exception{
        Query q = em.createQuery("Select usuario from Usuario as usuario where usuario.documento = :documento");

        q.setParameter("documento",documento);

        List<Usuario> usuario = q.getResultList();
        if(usuario.size() == 1){
            return usuario.get(0);
        }else{
            return  null;
        }
    }

    @Override
    public Boolean existeIdentificadorEdit(String identificador, Long idUsuario) throws Exception {

        Query q = em.createQuery("Select usuario.id from Usuario as usuario where " +
                "usuario.id != :idUsuario and usuario.identificador = :identificador");

        q.setParameter("identificador",identificador);
        q.setParameter("idUsuario",idUsuario);

        return q.getResultList().size() > 0;

    }

    @Override
    public Boolean existeDocumentioEdit(String documento, Long idUsuario) throws Exception{
        Query q = em.createQuery("Select usuario.id from Usuario as usuario where " +
                "usuario.id != :idUsuario and usuario.documento = :documento");

        q.setParameter("documento",documento);
        q.setParameter("idUsuario",idUsuario);

        return q.getResultList().size() > 0;
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public Paginacion busqueda(Integer pageNumber,String identificador,String nombre, String apellido1, String apellido2, String documento, Long tipoUsuario) throws Exception {

        Query q;
        Query q2;
        Map<String, Object> parametros = new HashMap<String, Object>();
        List<String> where = new ArrayList<String>();

        StringBuilder query = new StringBuilder("Select usuario from Usuario as usuario ");

        if(identificador!= null && identificador.length() > 0){where.add(DataBaseUtils.like("usuario.identificador","identificador",parametros,identificador));}
        if(nombre!= null && nombre.length() > 0){where.add(DataBaseUtils.like("usuario.nombre","nombre",parametros,nombre));}
        if(apellido1!= null && apellido1.length() > 0){where.add(DataBaseUtils.like("usuario.apellido1","apellido1",parametros,apellido1));}
        if(apellido2!= null && apellido2.length() > 0){where.add(DataBaseUtils.like("usuario.apellido2","apellido2",parametros,apellido2));}
        if(documento!= null && documento.length() > 0){where.add(" upper(usuario.documento) like upper(:documento) "); parametros.put("documento","%"+documento.toLowerCase()+"%");}
        if (tipoUsuario != null && tipoUsuario > 0) {
            where.add(" usuario.tipoUsuario = :tipoUsuario ");
            parametros.put("tipoUsuario", tipoUsuario);
        }


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
            q2 = em.createQuery(query.toString().replaceAll("Select usuario from Usuario as usuario ", "Select count(usuario.id) from Usuario as usuario "));
            query.append("order by usuario.nombre, usuario.apellido1");
            q = em.createQuery(query.toString());

            for (Map.Entry<String, Object> param : parametros.entrySet()) {
                q.setParameter(param.getKey(), param.getValue());
                q2.setParameter(param.getKey(), param.getValue());
            }

        }else{
            q2 = em.createQuery(query.toString().replaceAll("Select usuario from Usuario as usuario ", "Select count(usuario.id) from Usuario as usuario "));
            query.append("order by usuario.nombre, usuario.apellido1");
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

        paginacion.setListado(q.getResultList());

        return paginacion;

    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public Integer asociarIdioma() throws Exception {

        Query q = em.createQuery("update from Usuario set idioma = :idioma where idioma is null");

        q.setParameter("idioma", RegwebConstantes.IDIOMA_ID_BY_CODIGO.get(Configuracio.getDefaultLanguage()));

        return q.executeUpdate();

    }

    @Override
    public void actualizarRoles(Usuario usuario, List<Rol> rolesUsuario) throws Exception, I18NException {

        //List<Rol> rolesUsuario = rolEjb.obtenerRolesUserPlugin(usuario.getIdentificador());

        if(rolesUsuario != null) {

            // Actualizamos los Roles del usuario según sistema externo
            usuario.setRoles(rolesUsuario);
            merge(usuario);
        }
    }

    @Override
    public void actualizarRolesWs(Usuario usuario, RolesInfo rolesInfo) throws Exception, I18NException {

        RolLocal rolEjb = (RolLocal) new InitialContext().lookup("java:comp/env/regweb3/RolEJB/local");

        List<String> roles = new ArrayList<String>();
        List<Rol> rolesUsuario = null;

        if(rolesInfo != null && rolesInfo.getRoles().length > 0){

            Collections.addAll(roles, rolesInfo.getRoles());
            if(roles.size() > 0){

                rolesUsuario = rolEjb.getByRol(roles);
            }
        }else{
            log.info("El usuario " + usuario.getIdentificador() + " no dispone de ningun Rol de REGWEB3 en el sistema de autentificacion");
        }

        if(rolesUsuario != null) {

            // Actualizamos los Roles del usuario según sistema externo
            usuario.setRoles(rolesUsuario);
            merge(usuario);
        }

    }
}
