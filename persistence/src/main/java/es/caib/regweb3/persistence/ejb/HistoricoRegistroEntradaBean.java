package es.caib.regweb3.persistence.ejb;

import es.caib.regweb3.model.HistoricoRegistroEntrada;
import es.caib.regweb3.model.RegistroEntrada;
import es.caib.regweb3.model.Usuario;
import es.caib.regweb3.model.UsuarioEntidad;
import es.caib.regweb3.persistence.utils.RegistroUtils;
import es.caib.regweb3.utils.RegwebConstantes;
import org.apache.log4j.Logger;
import org.jboss.ejb3.annotation.SecurityDomain;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.ArrayList;
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
    public HistoricoRegistroEntrada getReference(Long id) throws Exception {

        return em.getReference(HistoricoRegistroEntrada.class, id);
    }

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
    @SuppressWarnings("unchecked")
    public List<HistoricoRegistroEntrada> getPagination(int inicio) throws Exception {

        Query q = em.createQuery("Select historicoRegistroEntrada from HistoricoRegistroEntrada as historicoRegistroEntrada order by historicoRegistroEntrada.id");
        q.setFirstResult(inicio);
        q.setMaxResults(RESULTADOS_PAGINACION);
        q.setHint("org.hibernate.readOnly", true);

        return q.getResultList();
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public List<HistoricoRegistroEntrada> getByRegistroEntrada(Long idRegistro) throws Exception {

        Query q = em.createQuery("Select hre.id, hre.registroEntradaOriginal, hre.estado, hre.fecha, hre.modificacion, hre.usuario.id, hre.usuario.usuario from HistoricoRegistroEntrada as hre where hre.registroEntrada.id =:idRegistro order by hre.fecha desc");
        q.setParameter("idRegistro", idRegistro);
        q.setHint("org.hibernate.readOnly", true);

        List<HistoricoRegistroEntrada> hres = new ArrayList<HistoricoRegistroEntrada>();

        List<Object[]> result = q.getResultList();

        for (Object[] object : result) {
            HistoricoRegistroEntrada hre = new HistoricoRegistroEntrada((Long) object[0], (String) object[1], (Long) object[2], (Date) object[3], (String) object[4], (Long) object[5], (Usuario) object[6]);

            hres.add(hre);
        }

        return hres;
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
    public Boolean obtenerPorUsuario(Long idUsuarioEntidad) throws Exception {

        Query q;

        q = em.createQuery("Select count(hre.id) from HistoricoRegistroEntrada as hre where hre.usuario.id = :idUsuarioEntidad ");

        q.setParameter("idUsuarioEntidad", idUsuarioEntidad);
        q.setHint("org.hibernate.readOnly", true);

        return (Long) q.getSingleResult() > 0;
    }

    @Override
    public Integer eliminarByEntidad(Long idEntidad) throws Exception{

        List<?> hre = em.createQuery("Select distinct(hre.id) from HistoricoRegistroEntrada as hre where hre.registroEntrada.entidad.id =:idEntidad").setParameter("idEntidad",idEntidad).getResultList();
        Integer total = hre.size();

        if(hre.size() >0){

            // Si hay más de 1000 registros, dividimos las queries (ORA-01795).
            while (hre.size() > RegwebConstantes.NUMBER_EXPRESSIONS_IN) {

                List<?> subList = hre.subList(0, RegwebConstantes.NUMBER_EXPRESSIONS_IN);
                em.createQuery("delete from HistoricoRegistroEntrada where id in (:hre) ").setParameter("hre", subList).executeUpdate();
                hre.subList(0, RegwebConstantes.NUMBER_EXPRESSIONS_IN).clear();
            }

            em.createQuery("delete from HistoricoRegistroEntrada where id in (:hre) ").setParameter("hre", hre).executeUpdate();
        }

        return total;

    }

}
