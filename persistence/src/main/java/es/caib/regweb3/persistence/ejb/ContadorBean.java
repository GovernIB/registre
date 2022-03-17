package es.caib.regweb3.persistence.ejb;

import es.caib.regweb3.model.Contador;
import es.caib.regweb3.model.Libro;
import es.caib.regweb3.persistence.utils.NumeroRegistro;
import org.apache.log4j.Logger;
import org.jboss.ejb3.annotation.SecurityDomain;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
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
@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
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
        q.setHint("org.hibernate.readOnly", true);

        return q.getResultList();
    }

    @Override
    public NumeroRegistro incrementarContador(Long idContador) throws Exception {

        Query q = em.createQuery("update Contador set numero = numero + 1 where id = :idContador ");
        q.setParameter("idContador", idContador);
        q.executeUpdate();
        //em.flush();

        Integer numero = (Integer) em.createQuery("select numero from Contador where id = :idContador").setParameter("idContador", idContador).getSingleResult();
        //q.setHint("org.hibernate.cacheMode", CacheMode.IGNORE);

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
    public void reiniciarContadoresLibro(Libro libro) throws Exception{

        if(libro.getContadorEntrada()!=null){reiniciarContador(libro.getContadorEntrada().getId());}
        if(libro.getContadorSalida()!=null){reiniciarContador(libro.getContadorSalida().getId());}
        if(libro.getContadorOficioRemision()!=null){reiniciarContador(libro.getContadorOficioRemision().getId());}
        if(libro.getContadorSir()!=null){reiniciarContador(libro.getContadorSir().getId());}
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
