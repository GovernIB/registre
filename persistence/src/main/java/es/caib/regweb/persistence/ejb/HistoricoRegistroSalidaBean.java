package es.caib.regweb.persistence.ejb;

import es.caib.regweb.model.HistoricoRegistroSalida;
import es.caib.regweb.model.Libro;
import es.caib.regweb.model.RegistroSalida;
import es.caib.regweb.model.UsuarioEntidad;
import es.caib.regweb.persistence.utils.RegistroUtils;
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
 * @author jpernia
 * Date: 30/10/14
 */

@Stateless(name = "HistoricoRegistroSalidaEJB")
@SecurityDomain("seycon")
public class HistoricoRegistroSalidaBean extends BaseEjbJPA<HistoricoRegistroSalida, Long> implements HistoricoRegistroSalidaLocal{

    protected final Logger log = Logger.getLogger(getClass());

    @PersistenceContext(unitName="regweb")
    private EntityManager em;


    @Override
    public HistoricoRegistroSalida findById(Long id) throws Exception {

        return em.find(HistoricoRegistroSalida.class, id);
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public List<HistoricoRegistroSalida> getAll() throws Exception {

        return  em.createQuery("Select historicoRegistroSalida from HistoricoRegistroSalida as historicoRegistroSalida order by historicoRegistroSalida.id").getResultList();
    }

    @Override
    public Long getTotal() throws Exception {

        Query q = em.createQuery("Select count(historicoRegistroSalida.id) from HistoricoRegistroSalida as historicoRegistroSalida");

        return (Long) q.getSingleResult();
    }


    @Override
    public List<HistoricoRegistroSalida> getPagination(int inicio) throws Exception {

        Query q = em.createQuery("Select historicoRegistroSalida from HistoricoRegistroSalida as historicoRegistroSalida order by historicoRegistroSalida.id");
        q.setFirstResult(inicio);
        q.setMaxResults(RESULTADOS_PAGINACION);

        return q.getResultList();
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public List<HistoricoRegistroSalida> getByRegistroSalida(Long idRegistro) throws Exception {

        Query q = em.createQuery("Select historicoRegistroSalida from HistoricoRegistroSalida as historicoRegistroSalida where historicoRegistroSalida.registroSalida.id =:idRegistro order by historicoRegistroSalida.fecha desc");
        q.setParameter("idRegistro", idRegistro);
        return q.getResultList();
    }

    @Override
    public List<HistoricoRegistroSalida> salidaModificadaPorUsuario(Date fechaInicio, Date fechaFin, Long idUsuario, List<Libro> libros) throws Exception{

        Query q;

        q = em.createQuery("Select historicoRegistroSalida from HistoricoRegistroSalida as historicoRegistroSalida where historicoRegistroSalida.fecha >= :fechaInicio " +
                "and historicoRegistroSalida.fecha <= :fechaFin and historicoRegistroSalida.usuario.id = :idUsuario and historicoRegistroSalida.modificacion != 'Creación' and historicoRegistroSalida.registroSalida.libro in (:libros) order by historicoRegistroSalida.fecha desc");

        q.setParameter("fechaInicio", fechaInicio);
        q.setParameter("fechaFin", fechaFin);
        q.setParameter("idUsuario", idUsuario);
        q.setParameter("libros", libros);

        return q.getResultList();
    }

    @Override
    public List<HistoricoRegistroSalida> salidaModificadaPorUsuarioLibro(Date fechaInicio, Date fechaFin, Long idUsuario, Long idLibro) throws Exception{

        Query q;

        q = em.createQuery("Select historicoRegistroSalida from HistoricoRegistroSalida as historicoRegistroSalida where historicoRegistroSalida.fecha >= :fechaInicio " +
                "and historicoRegistroSalida.fecha <= :fechaFin and historicoRegistroSalida.usuario.id = :idUsuario and historicoRegistroSalida.registroSalida.libro.id = :idLibro and historicoRegistroSalida.modificacion != 'Creación' order by historicoRegistroSalida.fecha desc");

        q.setParameter("fechaInicio", fechaInicio);
        q.setParameter("fechaFin", fechaFin);
        q.setParameter("idUsuario", idUsuario);
        q.setParameter("idLibro", idLibro);

        return q.getResultList();
    }

    @Override
    public HistoricoRegistroSalida crearHistoricoRegistroSalida(RegistroSalida registroSalida, UsuarioEntidad usuarioEntidad, String modificacion, boolean serializar) throws Exception{

        HistoricoRegistroSalida historico = new HistoricoRegistroSalida();

        historico.setEstado(registroSalida.getEstado());
        historico.setRegistroSalida(registroSalida);
        historico.setFecha(new Date());
        historico.setModificacion(modificacion);
        historico.setUsuario(usuarioEntidad);
        //Serializamos el RegistroEntrada original
        if(serializar){
            String registroEntradaOrigial = RegistroUtils.serilizarXml(registroSalida);
            historico.setRegistroSalidaOriginal(registroEntradaOrigial);
        }

        // Guardamos el histórico
        return persist(historico);
    }

    @Override
    public Integer eliminarByEntidad(Long idEntidad) throws Exception{

        List hrs = em.createQuery("Select distinct(hre.id) from HistoricoRegistroSalida as hre where hre.registroSalida.usuario.entidad.id =:idEntidad").setParameter("idEntidad",idEntidad).getResultList();

        if(hrs.size() > 0){
            return em.createQuery("delete from HistoricoRegistroSalida where id in (:hrs) ").setParameter("hrs", hrs).executeUpdate();
        }

        return 0;
    }


}