package es.caib.regweb3.persistence.ejb;

import es.caib.regweb3.model.Contador;
import es.caib.regweb3.persistence.utils.NumeroRegistro;
import org.apache.log4j.Logger;
import org.jboss.ejb3.annotation.SecurityDomain;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Fundació BIT.
 *
 * @author earrivi
 * Date: 16/01/14
 */

@Stateless(name = "ContadorEJB")
@SecurityDomain("seycon")
public class ContadorBean extends BaseEjbJPA<Contador, Long> implements ContadorLocal{

    protected final Logger log = Logger.getLogger(getClass());

    @PersistenceContext(unitName="regweb3")
    private EntityManager em;


    @Override
    public Contador getReference(Long id) throws Exception {

        return em.getReference(Contador.class, id);
    }

    @Override
    public Contador findById(Long id) throws Exception {

        return em.find(Contador.class, id);
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public List<Contador> getAll() throws Exception {

        return  em.createQuery("Select contador from Contador as contador order by contador.id").getResultList();
    }

    @Override
    public Long getTotal() throws Exception {

        Query q = em.createQuery("Select count(contador.id) from Contador as contador");

        return (Long) q.getSingleResult();
    }


    @Override
    @SuppressWarnings(value = "unchecked")
    public List<Contador> getPagination(int inicio) throws Exception {

        Query q = em.createQuery("Select contador from Contador as contador order by contador.id");
        q.setFirstResult(inicio);
        q.setMaxResults(RESULTADOS_PAGINACION);

        return q.getResultList();
    }

    @Override
    public NumeroRegistro incrementarContador(Long idContador) throws Exception {

        //log.info("Antes: " + findById(idContador).getNumero());
        Query q = em.createQuery("update Contador set numero = numero + 1 where id = :idContador ");
        q.setParameter("idContador", idContador);
        q.executeUpdate();
        em.flush();

        Query q1 = em.createQuery("select numero from Contador where id = :idContador");
        q1.setParameter("idContador", idContador);
        Integer numero = (Integer) q1.getSingleResult();
        //log.info("Después: " + numero);

        return new NumeroRegistro(numero,Calendar.getInstance().getTime());


    }

    @Override
    public void reiniciarContador(Long idContador) throws Exception{

        Query q = em.createQuery("update Contador set numero = 0 where id = :idContador ");
        q.setParameter("idContador", idContador);
        q.executeUpdate();
        em.flush();
    }

    @Override
    public String secuenciaSir(Long idContador) throws Exception{

        NumeroRegistro numero = incrementarContador(idContador);

        String secuencia = numero.getNumero().toString();

        if(secuencia.length() < 8){
            secuencia = String.format("%08d", numero.getNumero());

        }else if(secuencia.length() > 8){
            throw new Exception("El valor de la secuencia obtenido del Contador no puede ser superior a 8");
        }

        return secuencia;

    }


}
