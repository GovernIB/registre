package es.caib.regweb3.persistence.ejb;

import es.caib.regweb3.model.Usuario;
import es.caib.regweb3.persistence.utils.DataBaseUtils;
import es.caib.regweb3.persistence.utils.Paginacion;
import org.apache.log4j.Logger;
import org.jboss.ejb3.annotation.SecurityDomain;

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

@Stateless(name = "UsuarioEJB")
@SecurityDomain("seycon")
public class UsuarioBean extends BaseEjbJPA<Usuario, Long> implements UsuarioLocal{

    protected final Logger log = Logger.getLogger(getClass());

    @PersistenceContext(unitName="regweb3")
    private EntityManager em;


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
    public List<Usuario> getPagination(int inicio) throws Exception {

        Query q = em.createQuery("Select usuario from Usuario as usuario order by usuario.id");
        q.setFirstResult(inicio);
        q.setMaxResults(RESULTADOS_PAGINACION);

        return q.getResultList();
    }

    @Override
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
    public Paginacion busqueda(Integer pageNumber,String identificador,String nombre, String apellido1, String apellido2, String documento, Long tipoUsuario) throws Exception {

        Query q;
        Query q2;
        Map<String, Object> parametros = new HashMap<String, Object>();
        List<String> where = new ArrayList<String>();

        StringBuffer query = new StringBuffer("Select usuario from Usuario as usuario ");

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
        log.info("Query: " + query);

        Paginacion paginacion = null;

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
}
