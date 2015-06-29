package es.caib.regweb3.persistence.ejb;

import es.caib.regweb3.model.Repro;
import org.apache.log4j.Logger;
import org.jboss.ejb3.annotation.SecurityDomain;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;


/**
 * Created by Fundació BIT.
 *
 * @author jpernia
 * Date: 16/07/14
 */

@Stateless(name = "ReproEJB")
@SecurityDomain("seycon")
public class ReproBean extends BaseEjbJPA<Repro, Long> implements ReproLocal{

    protected final Logger log = Logger.getLogger(getClass());

    @PersistenceContext(unitName="regweb3")
    private EntityManager em;


    @Override
    public Repro findById(Long id) throws Exception {

        return em.find(Repro.class, id);
    }

    @Override
    public Repro findByOrden(Long idUsuario, int orden) throws Exception {

        Query q = em.createQuery("Select repro from Repro as repro where " +
                "repro.usuario.id = :idUsuario and repro.orden = :orden");

        q.setParameter("idUsuario",idUsuario);
        q.setParameter("orden",orden);

        return (Repro) q.getSingleResult();
    }

    @Override
    public List<Repro> getAll() throws Exception {

        return  em.createQuery("Select repro from Repro as repro order by repro.id").getResultList();
    }

    @Override
    public Long getTotal() throws Exception {

        Query q = em.createQuery("Select count(repro.id) from Repro as repro");

        return (Long) q.getSingleResult();
    }

    @Override
    public List<Repro> getPagination(int inicio) throws Exception {

        Query q = em.createQuery("Select repro from Repro as repro order by repro.id");
        q.setFirstResult(inicio);
        q.setMaxResults(RESULTADOS_PAGINACION);

        return q.getResultList();
    }

    @Override
    public List<Repro> getPaginationUsuario(int inicio, Long idUsuario) throws Exception {

        Query q = em.createQuery("Select repro from Repro as repro  " +
                "where repro.usuario.id = :idUsuario order by repro.orden");

        q.setParameter("idUsuario",idUsuario);
        q.setFirstResult(inicio);
        q.setMaxResults(RESULTADOS_PAGINACION);

        return q.getResultList();
    }

    @Override
    public List<Repro> getAllbyUsuario(Long idUsuario) throws Exception {

        Query q = em.createQuery("Select repro from Repro as repro  " +
                "where repro.usuario.id = :idUsuario order by repro.orden");

        q.setParameter("idUsuario",idUsuario);

        return  q.getResultList();
    }

    @Override
    public List<Repro> getActivasbyUsuario(Long idUsuario, Long tipoRegistro) throws Exception {

        Query q = em.createQuery("Select repro from Repro as repro  " +
                "where repro.usuario.id = :idUsuario and repro.tipoRegistro = :tipoRegistro and repro.activo = true order by repro.orden");

        q.setParameter("idUsuario",idUsuario);
        q.setParameter("tipoRegistro",tipoRegistro);

        return  q.getResultList();
    }

    @Override
    public Long getTotalbyUsuario(Long idUsuario) throws Exception {

        Query q = em.createQuery("Select count(repro.id) from Repro as repro " +
                "where repro.usuario.id = :idUsuario");

        q.setParameter("idUsuario",idUsuario);

        return (Long) q.getSingleResult();
    }

    @Override
    public Integer maxOrdenRepro(Long idUsuario) throws Exception{

        Query q = em.createQuery("Select max(repro.orden) from Repro as repro  " +
                "where repro.usuario.id = :idUsuario");

        q.setParameter("idUsuario",idUsuario);

        return (Integer) q.getSingleResult();
    }

    @Override
    public Long obtenerUsuarioRepro(Long idRepro) throws Exception{

        Query q = em.createQuery("Select repro.usuario.id from Repro as repro  " +
                "where repro.id = :idRepro");

        q.setParameter("idRepro",idRepro);

        return (Long) q.getSingleResult();
    }


    @Override
    public void modificarOrden(Long idRepro, int orden) throws Exception{

        Query q = em.createQuery("Update Repro set orden = :orden where " +
                "id = :idRepro");

        q.setParameter("idRepro",idRepro);
        q.setParameter("orden",orden);
        q.executeUpdate();
    }


    @Override
    public void subirOrden(Long idRepro) throws Exception{

        Long idUsuario = obtenerUsuarioRepro(idRepro);

        Repro repro = findById(idRepro);

        int ordenActual = repro.getOrden();

        int ordenNuevo = 1;
        if(ordenActual > 1){
            ordenNuevo = ordenActual - 1;
        }

        Repro reproAnterior = findByOrden(idUsuario, ordenNuevo);

        modificarOrden(idRepro, ordenNuevo);

        modificarOrden(reproAnterior.getId(), ordenActual);

    }

    @Override
    public void bajarOrden(Long idRepro) throws Exception{

        Long idUsuario = obtenerUsuarioRepro(idRepro);
        List<Repro> repros = getAllbyUsuario(idUsuario);

        Repro repro = findById(idRepro);

        int ordenActual = repro.getOrden();

        int ordenNuevo = repros.size();
        if(ordenActual < repros.size()){
            ordenNuevo = ordenActual + 1;
        }

        Repro reproPosterior = findByOrden(idUsuario, ordenNuevo);

        modificarOrden(idRepro, ordenNuevo);

        modificarOrden(reproPosterior.getId(), ordenActual);

    }

    @Override
    public void cambiarEstado(Long idRepro) throws Exception{

        Repro repro = findById(idRepro);

        if(repro.getActivo()){
            repro.setActivo(false);
        }else{
            repro.setActivo(true);
        }

        merge(repro);
    }

    @Override
    public Integer eliminarByEntidad(Long idEntidad) throws Exception{

        List<?> repros =  em.createQuery("select distinct(r.id) from Repro as r where r.usuario.entidad.id =:idEntidad").setParameter("idEntidad",idEntidad).getResultList();

        if(repros.size() > 0){
            return em.createQuery("delete from Repro where id in (:repros)").setParameter("repros", repros).executeUpdate();
        }
        return 0;

    }

}
