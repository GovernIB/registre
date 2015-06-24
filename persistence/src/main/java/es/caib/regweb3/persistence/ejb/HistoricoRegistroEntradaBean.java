package es.caib.regweb3.persistence.ejb;

import es.caib.regweb3.model.HistoricoRegistroEntrada;
import es.caib.regweb3.model.Libro;
import es.caib.regweb3.model.RegistroEntrada;
import es.caib.regweb3.model.UsuarioEntidad;
import es.caib.regweb3.persistence.utils.RegistroUtils;
import org.apache.log4j.Logger;
import org.jboss.ejb3.annotation.SecurityDomain;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.Date;
import java.util.List;

/**
 * Created by Fundació BIT.
 *
 * @author earrivi
 * Date: 16/01/14
 */

@Stateless(name = "HistoricoRegistroEntradaEJB")
@SecurityDomain("seycon")
public class HistoricoRegistroEntradaBean extends BaseEjbJPA<HistoricoRegistroEntrada, Long> implements HistoricoRegistroEntradaLocal{

    protected final Logger log = Logger.getLogger(getClass());

    @PersistenceContext(unitName="regweb3")
    private EntityManager em;


    @Override
    public HistoricoRegistroEntrada findById(Long id) throws Exception {

        return em.find(HistoricoRegistroEntrada.class, id);
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public List<HistoricoRegistroEntrada> getAll() throws Exception {

        return  em.createQuery("Select historicoRegistroEntrada from HistoricoRegistroEntrada as historicoRegistroEntrada order by historicoRegistroEntrada.id").getResultList();
    }

    @Override
    public Long getTotal() throws Exception {

        Query q = em.createQuery("Select count(historicoRegistroEntrada.id) from HistoricoRegistroEntrada as historicoRegistroEntrada");

        return (Long) q.getSingleResult();
    }


    @Override
    public List<HistoricoRegistroEntrada> getPagination(int inicio) throws Exception {

        Query q = em.createQuery("Select historicoRegistroEntrada from HistoricoRegistroEntrada as historicoRegistroEntrada order by historicoRegistroEntrada.id");
        q.setFirstResult(inicio);
        q.setMaxResults(RESULTADOS_PAGINACION);

        return q.getResultList();
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public List<HistoricoRegistroEntrada> getByRegistroEntrada(Long idRegistro) throws Exception {

      Query q = em.createQuery("Select historicoRegistroEntrada from HistoricoRegistroEntrada as historicoRegistroEntrada where historicoRegistroEntrada.registroEntrada.id =:idRegistro order by historicoRegistroEntrada.fecha desc");
      q.setParameter("idRegistro", idRegistro);
      return q.getResultList();
    }

    @Override
    public List<HistoricoRegistroEntrada> entradaModificadaPorUsuario(Date fechaInicio, Date fechaFin, Long idUsuario, List<Libro> libros) throws Exception{

        Query q;

        q = em.createQuery("Select historicoRegistroEntrada from HistoricoRegistroEntrada as historicoRegistroEntrada where historicoRegistroEntrada.fecha >= :fechaInicio " +
                "and historicoRegistroEntrada.fecha <= :fechaFin and historicoRegistroEntrada.usuario.id = :idUsuario and historicoRegistroEntrada.modificacion != 'Creación' and historicoRegistroEntrada.registroEntrada.libro in (:libros) order by historicoRegistroEntrada.fecha desc");

        q.setParameter("fechaInicio", fechaInicio);
        q.setParameter("fechaFin", fechaFin);
        q.setParameter("idUsuario", idUsuario);
        q.setParameter("libros", libros);

        return q.getResultList();
    }

    @Override
    public List<HistoricoRegistroEntrada> entradaModificadaPorUsuarioLibro(Date fechaInicio, Date fechaFin, Long idUsuario, Long idLibro) throws Exception{

        Query q;

        q = em.createQuery("Select historicoRegistroEntrada from HistoricoRegistroEntrada as historicoRegistroEntrada where historicoRegistroEntrada.fecha >= :fechaInicio " +
                "and historicoRegistroEntrada.fecha <= :fechaFin and historicoRegistroEntrada.usuario.id = :idUsuario and historicoRegistroEntrada.registroEntrada.libro.id = :idLibro and historicoRegistroEntrada.modificacion != 'Creación' order by historicoRegistroEntrada.fecha desc");

        q.setParameter("fechaInicio", fechaInicio);
        q.setParameter("fechaFin", fechaFin);
        q.setParameter("idUsuario", idUsuario);
        q.setParameter("idLibro", idLibro);

        return q.getResultList();
    }

    @Override
    public HistoricoRegistroEntrada crearHistoricoRegistroEntrada(RegistroEntrada registroEntrada, UsuarioEntidad usuarioEntidad, String modificacion, boolean serializar) throws Exception{

        
        HistoricoRegistroEntrada historico = new HistoricoRegistroEntrada();

        historico.setEstado(registroEntrada.getEstado());
        historico.setRegistroEntrada(registroEntrada);
        historico.setFecha(new Date());
        historico.setModificacion(modificacion);
        historico.setUsuario(usuarioEntidad);
        //Serializamos el RegistroEntrada original
        if(serializar){
            String registroEntradaOrigial = RegistroUtils.serilizarXml(registroEntrada);
            historico.setRegistroEntradaOriginal(registroEntradaOrigial);
        }

        // Guardamos el histórico
        return persist(historico);
    }

    @Override
    public Integer eliminarByEntidad(Long idEntidad) throws Exception{

        List<?> hre = em.createQuery("Select distinct(hre.id) from HistoricoRegistroEntrada as hre where hre.registroEntrada.usuario.entidad.id =:idEntidad").setParameter("idEntidad",idEntidad).getResultList();

        if(hre.size() >0){
            return em.createQuery("delete from HistoricoRegistroEntrada where id in (:hre) ").setParameter("hre",hre).executeUpdate();
        }

        return 0;

    }

}
